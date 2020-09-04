package org.micah.mnt.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.micah.exception.global.BadRequestException;
import org.micah.mnt.service.IAppService;
import org.micah.model.App;
import lombok.extern.slf4j.Slf4j;
import org.micah.core.util.FileUtils;
import lombok.RequiredArgsConstructor;
import org.micah.core.web.page.PageResult;
import org.micah.mnt.mapper.AppMapper;
import org.micah.model.dto.AppDto;
import org.micah.model.query.AppQueryCriteria;
import org.micah.model.mapstruct.AppMapStruct;
import org.micah.mp.util.PageUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.lang.IllegalArgumentException;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.micah.mp.util.QueryHelpUtils;
import org.springframework.data.domain.Pageable;
import java.io.IOException;
import javax.servlet.http.HttpServletResponse;
import org.micah.exception.global.CreateFailException;
import org.micah.exception.global.DeleteFailException;

import java.util.*;

/**
* @website https://el-admin.vip
* @description 服务实现
* @author Micah
* @date 2020-09-03
**/
@Slf4j
@Service
@RequiredArgsConstructor
public class AppServiceImpl extends ServiceImpl<AppMapper,App> implements IAppService {

    private final AppMapper appMapper;

    private final AppMapStruct appMapStruct;

    @Override
    public PageResult queryAll(AppQueryCriteria mntAppCriteria, Pageable pageable){
        Page<App> page = PageUtils.startPageAndSort(pageable);
        QueryWrapper<App> wrapper = QueryHelpUtils.getWrapper(mntAppCriteria, App.class);
        Page<App> mntAppPage = this.appMapper.selectPage(page, wrapper);
        return PageResult.success(mntAppPage.getTotal(), mntAppPage.getPages(),
                                    this.appMapStruct.toDto(mntAppPage.getRecords()));
    }

    @Override
    public List<AppDto> queryAll(AppQueryCriteria criteria){
        QueryWrapper<App> wrapper = QueryHelpUtils.getWrapper(criteria, App.class);
        return this.appMapStruct.toDto(this.list(wrapper));
    }

    @Override
    public AppDto findById(Long appId) {
        if (appId == null) {
            throw new IllegalArgumentException("参数为空");
        }
        App app = Optional.ofNullable(this.appMapper.selectById(appId)).orElse(null);
        return this.appMapStruct.toDto(app);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public AppDto create(App resources) {
        this.verification(resources);
        if (!this.save(resources)){
            log.warn("插入失败:{}", resources);
            throw new CreateFailException("插入一条数据失败,请联系管理员");
        }
        return appMapStruct.toDto(resources);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateMntApp(App resources) {
        this.verification(resources);
        if(!this.updateById(resources)){
            log.warn("更新失败:{}", resources);
            throw new CreateFailException("更新一条数据失败,请联系管理员");
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteAll(Set<Long> ids) {
        if(!this.removeByIds(ids)){
            log.warn("删除失败:{}", ids);
            throw new DeleteFailException("批量删除失败,请联系管理员");
        }
    }

    @Override
    public void download(List<AppDto> data, HttpServletResponse response) throws IOException {
        FileUtils.downloadFailedUsingJson(response, "mntApp-info", AppDto.class, data, "mntApp-sheet");
    }

    private void verification(App resources){
        String opt = "/opt";
        String home = "/home";
        if (!(resources.getUploadPath().startsWith(opt) || resources.getUploadPath().startsWith(home))) {
            throw new BadRequestException("文件只能上传在opt目录或者home目录 ");
        }
        if (!(resources.getDeployPath().startsWith(opt) || resources.getDeployPath().startsWith(home))) {
            throw new BadRequestException("文件只能部署在opt目录或者home目录 ");
        }
        if (!(resources.getBackupPath().startsWith(opt) || resources.getBackupPath().startsWith(home))) {
            throw new BadRequestException("文件只能备份在opt目录或者home目录 ");
        }
    }
}