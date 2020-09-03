package org.micah.mnt.service.impl;

import org.micah.model.ServerDeploy;
import lombok.extern.slf4j.Slf4j;
import org.micah.core.util.FileUtils;
import lombok.RequiredArgsConstructor;
import org.micah.core.web.page.PageResult;
import org.micah.mnt.mapper.ServerDeployMapper;
import org.micah.model.dto.ServerDeployDto;
import org.micah.model.query.ServerDeployQueryCriteria;
import org.micah.model.mapstruct.ServerDeployMapStruct;
import org.micah.mnt.service.IServerDeployService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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

import java.util.*;

/**
* @website https://el-admin.vip
* @description 服务实现
* @author micah
* @date 2020-09-03
**/
@Slf4j
@Service
@RequiredArgsConstructor
public class ServerDeployServiceImpl extends ServiceImpl<ServerDeployMapper,ServerDeploy> implements IServerDeployService {

    private final ServerDeployMapper serverDeployMapper;

    private final ServerDeployMapStruct serverDeployMapStruct;

    @Override
    public PageResult queryAll(ServerDeployQueryCriteria serverDeployCriteria, Pageable pageable){
        Page<ServerDeploy> page = PageUtils.startPageAndSort(pageable);
        QueryWrapper<ServerDeploy> wrapper = QueryHelpUtils.getWrapper(serverDeployCriteria, ServerDeploy.class);
        Page<ServerDeploy> serverDeployPage = this.serverDeployMapper.selectPage(page, wrapper);
        return PageResult.success(serverDeployPage.getTotal(), serverDeployPage.getPages(),
                                    this.serverDeployMapStruct.toDto(serverDeployPage.getRecords()));
    }

    @Override
    public List<ServerDeployDto> queryAll(ServerDeployQueryCriteria criteria){
        QueryWrapper<ServerDeploy> wrapper = QueryHelpUtils.getWrapper(criteria, ServerDeploy.class);
        return this.serverDeployMapStruct.toDto(this.list(wrapper));
    }

    @Override
    public ServerDeployDto findById(Long serverId) {
        if (serverId == null) {
            throw new IllegalArgumentException("参数为空");
        }
        ServerDeploy serverDeploy = Optional.ofNullable(this.serverDeployMapper.selectById(serverId)).orElse(null);
        return this.serverDeployMapStruct.toDto(serverDeploy);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ServerDeployDto create(ServerDeploy resources) {
        if(!this.save(resources)){
            log.warn("插入失败:{}", resources);
            throw new CreateFailException("插入一条数据失败,请联系管理员");
        }
        return serverDeployMapStruct.toDto(resources);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateServerDeploy(ServerDeploy resources) {
        if(this.updateById(resources)){
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
    public void download(List<ServerDeployDto> data, HttpServletResponse response) throws IOException {
        FileUtils.downloadFailedUsingJson(response, "serverDeploy-info", ServerDeployDto.class, data, "serverDeploy-sheet");
    }
}