package org.micah.mnt.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.SneakyThrows;
import org.micah.exception.global.BadRequestException;
import org.micah.mnt.mapper.DeployServerRelationMapper;
import org.micah.mnt.service.IDeployHistoryService;
import org.micah.mnt.service.IServerDeployService;
import org.micah.mnt.util.ExecuteShellUtil;
import org.micah.mnt.util.ScpClientUtil;
import org.micah.mnt.websocket.MsgType;
import org.micah.mnt.websocket.SocketMsg;
import org.micah.mnt.websocket.WebSocketServer;
import org.micah.model.*;
import lombok.extern.slf4j.Slf4j;
import org.micah.core.util.FileUtils;
import lombok.RequiredArgsConstructor;
import org.micah.core.web.page.PageResult;
import org.micah.mnt.mapper.DeployMapper;
import org.micah.model.dto.AppDto;
import org.micah.model.dto.DeployDto;
import org.micah.model.dto.ServerDeployDto;
import org.micah.model.query.DeployQueryCriteria;
import org.micah.model.mapstruct.DeployMapStruct;
import org.micah.mnt.service.IDeployService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.micah.security.util.SecurityUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.lang.IllegalArgumentException;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.micah.mp.util.QueryHelpUtils;
import org.springframework.data.domain.Pageable;
import org.micah.mp.util.PageUtils;
import java.io.IOException;
import javax.servlet.http.HttpServletResponse;
import org.micah.exception.global.CreateFailException;
import org.micah.exception.global.DeleteFailException;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

/**
* @author Micah ZhangHouYing
* @date 2020-09-03
**/
@Slf4j
@Service
@RequiredArgsConstructor
public class DeployServiceImpl extends ServiceImpl<DeployMapper,Deploy> implements IDeployService {

    private static final String FILE_SEPARATOR = "/";

    private final DeployMapper deployMapper;

    private final DeployServerRelationMapper deployServerRelationMapper;

    private final DeployMapStruct deployMapStruct;

    private final IServerDeployService serverDeployService;

    private final IDeployHistoryService deployHistoryService;

    private static final Integer count = 30;

    private static final String FILE_SAVE_PATH = FileUtil.getTmpDirPath()+"/";

    @Override
    public PageResult queryAll(DeployQueryCriteria deployCriteria, Pageable pageable){
        Page<Deploy> page = PageUtils.startPageAndSort(pageable);
        QueryWrapper<Deploy> wrapper = QueryHelpUtils.getWrapper(deployCriteria, Deploy.class);
        Page<Deploy> deployPage = this.deployMapper.queryAll(wrapper,page);
        return PageResult.success(deployPage.getTotal(), deployPage.getPages(),
                                    this.deployMapStruct.toDto(deployPage.getRecords()));
    }

    @Override
    public List<DeployDto> queryAll(DeployQueryCriteria criteria){
        QueryWrapper<Deploy> wrapper = QueryHelpUtils.getWrapper(criteria, Deploy.class);
        return this.deployMapStruct.toDto(this.list(wrapper));
    }

    @Override
    public DeployDto findById(Long deployId) {
        if (deployId == null) {
            throw new IllegalArgumentException("参数为空");
        }
        Deploy deploy = Optional.ofNullable(this.deployMapper.findById(deployId)).orElse(null);
        return this.deployMapStruct.toDto(deploy);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public DeployDto create(Deploy resources) {
        if(!this.save(resources)){
            log.warn("插入失败:{}", resources);
            throw new CreateFailException("插入一条数据失败,请联系管理员");
        }
        this.insertDeployServerRelation(resources);
        return deployMapStruct.toDto(resources);
    }

    private void insertDeployServerRelation(Deploy resources) {
        // 在中间表插入数据
        if (CollUtil.isNotEmpty(resources.getDeploys())){
            resources.getDeploys().forEach(server->{
                DeployServerRelation relation = new DeployServerRelation(resources.getId(),server.getId());
                this.deployServerRelationMapper.insert(relation);
            });
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateDeploy(Deploy resources) {
        resources.setAppId(resources.getApp().getId());
        if(!this.updateById(resources)){
            log.warn("更新失败:{}", resources);
            throw new CreateFailException("更新一条数据失败,请联系管理员");
        }
        // 删除中间表信息
        this.deployServerRelationMapper.delete(Wrappers.<DeployServerRelation>lambdaUpdate().eq(DeployServerRelation::getDeployId,resources.getId()));
        if (CollUtil.isNotEmpty(resources.getDeploys())){
            // 插入
            this.insertDeployServerRelation(resources);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteAll(Set<Long> ids) {
        if(!this.removeByIds(ids)){
            log.warn("删除失败:{}", ids);
            throw new DeleteFailException("批量删除失败,请联系管理员");
        }
        // 删除中间表信息
        ids.forEach(id->{
            this.deployServerRelationMapper.delete(Wrappers.<DeployServerRelation>lambdaUpdate().eq(DeployServerRelation::getDeployId,id));
        });
    }

    @Override
    public void download(List<DeployDto> data, HttpServletResponse response) throws IOException {
        FileUtils.downloadFailedUsingJson(response, "deploy-info", DeployDto.class, data, "deploy-sheet");
    }

    /**
     * 上传文件部署
     *
     * @param id
     * @param file
     * @return
     */
    @Override
    @SneakyThrows
    public Map<String, Object> deployApp(Long id, MultipartFile file) {
        String fileName = "";
        String fileSavePath = "";
        if (file != null){
            fileName = file.getOriginalFilename();
            File executeFile = new File(FILE_SAVE_PATH + fileName);
            fileSavePath = FILE_SAVE_PATH + fileName;
            FileUtil.del(executeFile);
            file.transferTo(executeFile);
        }
        DeployDto deployDto = this.findById(id);
        if (Objects.isNull(deployDto)){
            sendMsg("部署信息不存在", MsgType.ERROR);
            throw new BadRequestException("部署信息不存在");
        }
        AppDto app = deployDto.getApp();
        if (app == null) {
            sendMsg("包对应应用信息不存在", MsgType.ERROR);
            throw new BadRequestException("包对应应用信息不存在");
        }
        // 应用端口号
        Integer port = app.getPort();
        // 服务器部署路径
        String uploadPath = app.getUploadPath();
        StringBuilder sb = new StringBuilder();
        String msg;
        Set<ServerDeployDto> deploys = deployDto.getDeploys();
        for (ServerDeployDto deployDTO : deploys) {
            String ip = deployDTO.getIp();
            ExecuteShellUtil executeShellUtil = getExecuteShellUtil(ip);
            //判断是否第一次部署
            boolean flag = checkFile(executeShellUtil, app);
            //第一步要确认服务器上有这个目录
            executeShellUtil.execute("mkdir -p " + app.getUploadPath());
            executeShellUtil.execute("mkdir -p " + app.getBackupPath());
            executeShellUtil.execute("mkdir -p " + app.getDeployPath());
            //上传文件
            msg = String.format("登陆到服务器:%s", ip);
            ScpClientUtil scpClientUtil = getScpClientUtil(ip);
            log.info(msg);
            sendMsg(msg, MsgType.INFO);
            msg = String.format("上传文件到服务器:%s<br>目录:%s下，请稍等...", ip, uploadPath);
            sendMsg(msg, MsgType.INFO);
            scpClientUtil.putFile(fileSavePath, uploadPath);
            if (flag) {
                sendMsg("停止原来应用", MsgType.INFO);
                //停止应用
                stopApp(port, executeShellUtil);
                sendMsg("备份原来应用", MsgType.INFO);
                //备份应用
                backupApp(executeShellUtil, ip, app.getDeployPath()+FILE_SEPARATOR, app.getName(), app.getBackupPath()+FILE_SEPARATOR, id);
            }
            sendMsg("部署应用", MsgType.INFO);
            //部署文件,并启动应用
            String deployScript = app.getDeployScript();
            executeShellUtil.execute(deployScript);
            sleep(3);
            sendMsg("应用部署中，请耐心等待部署结果，或者稍后手动查看部署状态", MsgType.INFO);
            int i  = 0;
            boolean result = false;
            // 由于启动应用需要时间，所以需要循环获取状态，如果超过30次，则认为是启动失败
            while (i++ < count){
                result = checkIsRunningStatus(port, executeShellUtil);
                if(result){
                    break;
                }
                // 休眠6秒
                sleep(6);
            }
            sb.append("服务器:").append(deployDTO.getName()).append("<br>应用:").append(app.getName());
            sendResultMsg(result, sb);
            executeShellUtil.close();
        }
        return null;
    }

    private void sleep(int second) {
        try {
            Thread.sleep(second * 1000);
        } catch (InterruptedException e) {
            log.error(e.getMessage(),e);
        }
    }

    private ExecuteShellUtil getExecuteShellUtil(String ip) {
        ServerDeployDto serverDeployDTO = serverDeployService.findByIp(ip);
        if (serverDeployDTO == null) {
            sendMsg("IP对应服务器信息不存在：" + ip, MsgType.ERROR);
            throw new BadRequestException("IP对应服务器信息不存在：" + ip);
        }
        return new ExecuteShellUtil(ip, serverDeployDTO.getAccount(), serverDeployDTO.getPassword(),serverDeployDTO.getPort());
    }

    private ScpClientUtil getScpClientUtil(String ip) {
        ServerDeployDto serverDeployDTO = serverDeployService.findByIp(ip);
        if (serverDeployDTO == null) {
            sendMsg("IP对应服务器信息不存在：" + ip, MsgType.ERROR);
            throw new BadRequestException("IP对应服务器信息不存在：" + ip);
        }
        return ScpClientUtil.getInstance(ip, serverDeployDTO.getPort(), serverDeployDTO.getAccount(), serverDeployDTO.getPassword());
    }

    private void sendResultMsg(boolean result, StringBuilder sb) {
        if (result) {
            sb.append("<br>启动成功!");
            sendMsg(sb.toString(), MsgType.INFO);
        } else {
            sb.append("<br>启动失败!");
            sendMsg(sb.toString(), MsgType.ERROR);
        }
    }

    private boolean checkFile(ExecuteShellUtil executeShellUtil, AppDto appDTO) {
        String result = executeShellUtil.executeForResult("find " + appDTO.getDeployPath() + " -name " + appDTO.getName());
        return result.indexOf(appDTO.getName())>0;
    }

    /**
     * 指定端口程序是否在运行
     *
     * @param port 端口
     * @param executeShellUtil /
     * @return true 正在运行  false 已经停止
     */
    private boolean checkIsRunningStatus(int port, ExecuteShellUtil executeShellUtil) {
        String result = executeShellUtil.executeForResult(String.format("fuser -n tcp %d", port));
        return result.indexOf("/tcp:")>0;
    }

    private void sendMsg(String msg, MsgType msgType) {
        try {
            WebSocketServer.sendInfo(new SocketMsg(msg, msgType), "deploy");
        } catch (IOException e) {
            log.error(e.getMessage(),e);
        }
    }

    private void backupApp(ExecuteShellUtil executeShellUtil, String ip, String fileSavePath, String appName, String backupPath, Long id) {
        String deployDate = DateUtil.format(new Date(), DatePattern.PURE_DATETIME_PATTERN);
        StringBuilder sb = new StringBuilder();
        backupPath += appName + FILE_SEPARATOR + deployDate + "\n";
        sb.append("mkdir -p ").append(backupPath);
        sb.append("mv -f ").append(fileSavePath);
        sb.append(appName).append(" ").append(backupPath);
        log.info("备份应用脚本:" + sb.toString());
        executeShellUtil.execute(sb.toString());
        //还原信息入库
        DeployHistory deployHistory = new DeployHistory();
        deployHistory.setAppName(appName);
        deployHistory.setDeployUser(SecurityUtils.getCurrentUsername());
        deployHistory.setIp(ip);
        deployHistory.setDeployId(id);
        deployHistoryService.create(deployHistory);
    }


    /**
     * 系统还原
     *
     * @param resources
     * @return
     */
    @Override
    public String serverReduction(DeployHistory resources) {
        Long deployId = resources.getDeployId();
        Deploy deployInfo = Optional.ofNullable(this.getById(deployId)).orElseGet(Deploy::new);
        String deployDate = DateUtil.format(resources.getDeployDate(), DatePattern.PURE_DATETIME_PATTERN);
        App app = deployInfo.getApp();
        if (app == null) {
            sendMsg("应用信息不存在：" + resources.getAppName(), MsgType.ERROR);
            throw new BadRequestException("应用信息不存在：" + resources.getAppName());
        }
        String backupPath = app.getBackupPath()+FILE_SEPARATOR;
        backupPath += resources.getAppName() + FILE_SEPARATOR + deployDate;
        //这个是服务器部署路径
        String deployPath = app.getDeployPath();
        String ip = resources.getIp();
        ExecuteShellUtil executeShellUtil = getExecuteShellUtil(ip);
        String msg;

        msg = String.format("登陆到服务器:%s", ip);
        log.info(msg);
        sendMsg(msg, MsgType.INFO);
        sendMsg("停止原来应用", MsgType.INFO);
        //停止应用
        stopApp(app.getPort(), executeShellUtil);
        //删除原来应用
        sendMsg("删除应用", MsgType.INFO);
        executeShellUtil.execute("rm -rf " + deployPath + FILE_SEPARATOR + resources.getAppName());
        //还原应用
        sendMsg("还原应用", MsgType.INFO);
        executeShellUtil.execute("cp -r " + backupPath + "/. " + deployPath);
        sendMsg("启动应用", MsgType.INFO);
        executeShellUtil.execute(app.getStartScript());
        sendMsg("应用启动中，请耐心等待启动结果，或者稍后手动查看启动状态", MsgType.INFO);
        int i  = 0;
        boolean result = false;
        // 由于启动应用需要时间，所以需要循环获取状态，如果超过30次，则认为是启动失败
        while (i++ < count){
            result = checkIsRunningStatus(app.getPort(), executeShellUtil);
            if(result){
                break;
            }
            // 休眠6秒
            sleep(6);
        }
        StringBuilder sb = new StringBuilder();
        sb.append("服务器:").append(ip).append("<br>应用:").append(resources.getAppName());
        sendResultMsg(result, sb);
        executeShellUtil.close();
        return "";
    }

    /**
     * 获取服务运行状态
     *
     * @param resources
     * @return
     */
    @Override
    public String serverStatus(Deploy resources) {
        Set<ServerDeploy> serverDeploys = resources.getDeploys();
        App app = resources.getApp();
        for (ServerDeploy serverDeploy : serverDeploys) {
            StringBuilder sb = new StringBuilder();
            ExecuteShellUtil executeShellUtil = getExecuteShellUtil(serverDeploy.getIp());
            sb.append("服务器:").append(serverDeploy.getName()).append("<br>应用:").append(app.getName());
            boolean result = checkIsRunningStatus(app.getPort(), executeShellUtil);
            if (result) {
                sb.append("<br>正在运行");
                sendMsg(sb.toString(), MsgType.INFO);
            } else {
                sb.append("<br>已停止!");
                sendMsg(sb.toString(), MsgType.ERROR);
            }
            log.info(sb.toString());
            executeShellUtil.close();
        }
        return "执行完毕";
    }

    /**
     * 启动服务
     *
     * @param resources
     * @return
     */
    @Override
    public String startServer(Deploy resources) {
        Set<ServerDeploy> deploys = resources.getDeploys();
        App app = resources.getApp();
        for (ServerDeploy deploy : deploys) {
            StringBuilder sb = new StringBuilder();
            ExecuteShellUtil executeShellUtil = getExecuteShellUtil(deploy.getIp());
            //为了防止重复启动，这里先停止应用
            stopApp(app.getPort(), executeShellUtil);
            sb.append("服务器:").append(deploy.getName()).append("<br>应用:").append(app.getName());
            sendMsg("下发启动命令", MsgType.INFO);
            executeShellUtil.execute(app.getStartScript());
            sleep(3);
            sendMsg("应用启动中，请耐心等待启动结果，或者稍后手动查看运行状态", MsgType.INFO);
            int i  = 0;
            boolean result = false;
            // 由于启动应用需要时间，所以需要循环获取状态，如果超过30次，则认为是启动失败
            while (i++ < count){
                result = checkIsRunningStatus(app.getPort(), executeShellUtil);
                if(result){
                    break;
                }
                // 休眠6秒
                sleep(6);
            }
            sendResultMsg(result, sb);
            log.info(sb.toString());
            executeShellUtil.close();
        }
        return "执行完毕";
    }

    /**
     * 停止服务
     *
     * @param resources
     * @return
     */
    @Override
    public String stopServer(Deploy resources) {
        Set<ServerDeploy> deploys = resources.getDeploys();
        App app = resources.getApp();
        for (ServerDeploy deploy : deploys) {
            StringBuilder sb = new StringBuilder();
            ExecuteShellUtil executeShellUtil = getExecuteShellUtil(deploy.getIp());
            sb.append("服务器:").append(deploy.getName()).append("<br>应用:").append(app.getName());
            sendMsg("下发停止命令", MsgType.INFO);
            //停止应用
            stopApp(app.getPort(), executeShellUtil);
            sleep(1);
            boolean result = checkIsRunningStatus(app.getPort(), executeShellUtil);
            if (result) {
                sb.append("<br>关闭失败!");
                sendMsg(sb.toString(), MsgType.ERROR);
            } else {
                sb.append("<br>关闭成功!");
                sendMsg(sb.toString(), MsgType.INFO);
            }
            log.info(sb.toString());
            executeShellUtil.close();
        }
        return "执行完毕";
    }

    /**
     * 停App
     *
     * @param port 端口
     * @param executeShellUtil /
     */
    private void stopApp(int port, ExecuteShellUtil executeShellUtil) {
        //发送停止命令
        executeShellUtil.execute(String.format("lsof -i :%d|grep -v \"PID\"|awk '{print \"kill -9\",$2}'|sh", port));

    }
}