package org.micah.mnt.service.impl;

import org.micah.model.DeployHistory;
import lombok.extern.slf4j.Slf4j;
import org.micah.core.util.FileUtils;
import lombok.RequiredArgsConstructor;
import org.micah.core.web.page.PageResult;
import org.micah.mnt.mapper.DeployHistoryMapper;
import org.micah.model.dto.DeployHistoryDto;
import org.micah.model.query.DeployHistoryQueryCriteria;
import org.micah.model.mapstruct.DeployHistoryMapStruct;
import org.micah.mnt.service.IDeployHistoryService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import cn.hutool.core.util.IdUtil;
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
public class DeployHistoryServiceImpl extends ServiceImpl<DeployHistoryMapper,DeployHistory> implements IDeployHistoryService {

    private final DeployHistoryMapper deployHistoryMapper;

    private final DeployHistoryMapStruct deployHistoryMapStruct;

    @Override
    public PageResult queryAll(DeployHistoryQueryCriteria deployHistoryCriteria, Pageable pageable){
        Page<DeployHistory> page = PageUtils.startPageAndSort(pageable);
        QueryWrapper<DeployHistory> wrapper = QueryHelpUtils.getWrapper(deployHistoryCriteria, DeployHistory.class);
        Page<DeployHistory> deployHistoryPage = this.deployHistoryMapper.selectPage(page, wrapper);
        return PageResult.success(deployHistoryPage.getTotal(), deployHistoryPage.getPages(),
                                    this.deployHistoryMapStruct.toDto(deployHistoryPage.getRecords()));
    }

    @Override
    public List<DeployHistoryDto> queryAll(DeployHistoryQueryCriteria criteria){
        QueryWrapper<DeployHistory> wrapper = QueryHelpUtils.getWrapper(criteria, DeployHistory.class);
        return this.deployHistoryMapStruct.toDto(this.list(wrapper));
    }

    @Override
    public DeployHistoryDto findById(String historyId) {
        if (historyId == null) {
            throw new IllegalArgumentException("参数为空");
        }
        DeployHistory deployHistory = Optional.ofNullable(this.deployHistoryMapper.selectById(historyId)).orElse(null);
        return this.deployHistoryMapStruct.toDto(deployHistory);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public DeployHistoryDto create(DeployHistory resources) {
        if(!this.save(resources)){
            log.warn("插入失败:{}", resources);
            throw new CreateFailException("插入一条数据失败,请联系管理员");
        }
        return deployHistoryMapStruct.toDto(resources);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateDeployHistory(DeployHistory resources) {
        if(this.updateById(resources)){
            log.warn("更新失败:{}", resources);
            throw new CreateFailException("更新一条数据失败,请联系管理员");
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteAll(Set<String> ids) {
        if(!this.removeByIds(ids)){
            log.warn("删除失败:{}", ids);
            throw new DeleteFailException("批量删除失败,请联系管理员");
        }
    }

    @Override
    public void download(List<DeployHistoryDto> data, HttpServletResponse response) throws IOException {
        FileUtils.downloadFailedUsingJson(response, "deployHistory-info", DeployHistoryDto.class, data, "deployHistory-sheet");
    }
}