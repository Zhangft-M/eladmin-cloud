package org.micah.mnt.service.impl;

import org.micah.model.Deploy;
import lombok.extern.slf4j.Slf4j;
import org.micah.core.util.FileUtils;
import lombok.RequiredArgsConstructor;
import org.micah.core.web.page.PageResult;
import org.micah.mnt.mapper.DeployMapper;
import org.micah.model.dto.DeployDto;
import org.micah.model.query.DeployQueryCriteria;
import org.micah.model.mapstruct.DeployMapStruct;
import org.micah.mnt.service.IDeployService;
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
* @author Micah
* @date 2020-09-03
**/
@Slf4j
@Service
@RequiredArgsConstructor
public class DeployServiceImpl extends ServiceImpl<DeployMapper,Deploy> implements IDeployService {

    private final DeployMapper deployMapper;

    private final DeployMapStruct deployMapStruct;

    @Override
    public PageResult queryAll(DeployQueryCriteria deployCriteria, Pageable pageable){
        Page<Deploy> page = PageUtils.startPageAndSort(pageable);
        QueryWrapper<Deploy> wrapper = QueryHelpUtils.getWrapper(deployCriteria, Deploy.class);
        Page<Deploy> deployPage = this.deployMapper.selectPage(page, wrapper);
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
        Deploy deploy = Optional.ofNullable(this.deployMapper.selectById(deployId)).orElse(null);
        return this.deployMapStruct.toDto(deploy);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public DeployDto create(Deploy resources) {
        if(!this.save(resources)){
            log.warn("插入失败:{}", resources);
            throw new CreateFailException("插入一条数据失败,请联系管理员");
        }
        return deployMapStruct.toDto(resources);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateDeploy(Deploy resources) {
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
    public void download(List<DeployDto> data, HttpServletResponse response) throws IOException {
        FileUtils.downloadFailedUsingJson(response, "deploy-info", DeployDto.class, data, "deploy-sheet");
    }
}