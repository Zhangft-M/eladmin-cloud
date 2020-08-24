package org.micah.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.micah.core.constant.CacheKey;
import org.micah.core.util.FileUtils;
import org.micah.core.web.page.PageResult;
import org.micah.exception.global.BadRequestException;
import org.micah.exception.global.CreateFailException;
import org.micah.exception.global.DeleteFailException;
import org.micah.exception.global.EntityExistException;
import org.micah.model.Job;
import org.micah.model.UserJobRelation;
import org.micah.model.dto.JobDto;
import org.micah.model.mapstruct.JobMapStruct;
import org.micah.model.query.JobQueryCriteria;
import org.micah.mp.util.PageUtils;
import org.micah.mp.util.QueryHelpUtils;
import org.micah.redis.util.RedisUtils;
import org.micah.system.mapper.JobMapper;
import org.micah.system.mapper.UserJobMapper;
import org.micah.system.service.IJobService;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * @program: eladmin-cloud
 * @description: 职位业务实现类
 * @author: Micah
 * @create: 2020-08-10 16:30
 **/
@Slf4j
@Service
public class JobServiceImpl extends ServiceImpl<JobMapper, Job> implements IJobService {

    private final JobMapper jobMapper;

    private final RedisUtils redisUtils;

    private final JobMapStruct jobMapStruct;

    private final UserJobMapper userJobMapper;


    public JobServiceImpl(JobMapper jobMapper, RedisUtils redisUtils, JobMapStruct jobMapStruct, UserJobMapper userJobMapper) {
        this.jobMapper = jobMapper;
        this.redisUtils = redisUtils;
        this.jobMapStruct = jobMapStruct;
        this.userJobMapper = userJobMapper;
    }

    /**
     * 根据条件查询所有，不分页
     *
     * @param queryCriteria
     * @return
     */
    @Override
    public List<JobDto> queryAll(JobQueryCriteria queryCriteria) {
        QueryWrapper<Job> queryWrapper = QueryHelpUtils.getWrapper(queryCriteria,Job.class);
        List<Job> list = this.jobMapper.selectList(queryWrapper);
        return this.jobMapStruct.toDto(list);
    }

    /**
     * 根据条件查询所有，分页查询
     *
     * @param queryCriteria
     * @param pageable
     * @return
     */
    @Override
    public PageResult queryAll(JobQueryCriteria queryCriteria, Pageable pageable) {
        Page<Job> page = PageUtils.startPageAndSort(pageable);
        QueryWrapper<Job> queryWrapper = QueryHelpUtils.getWrapper(queryCriteria,Job.class);
        Page<Job> selectPage = this.jobMapper.selectPage(page, queryWrapper);
        return PageResult.success(selectPage.getTotal(), selectPage.getPages(), this.jobMapStruct.toDto(selectPage.getRecords()));
    }

    /**
     * 将数据导出为excel文件
     *
     * @param jobDtoList
     * @param response
     */
    @Override
    @SneakyThrows
    public void download(List<JobDto> jobDtoList, HttpServletResponse response) {
        FileUtils.downloadFailedUsingJson(response, "job", JobDto.class, jobDtoList, "sheet");
    }

    /**
     * 添加一条数据
     *
     * @param resources
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void create(Job resources) {
        //查询是否有相同名称的数据
        Job job = getJobByName(resources);
        if (!Objects.isNull(job)) {
            log.error("已经存在相同的职位名称:{}", resources.getName());
            throw new EntityExistException(Job.class, "name", resources.getName());
        }
        // 进行增加操作
        boolean save = this.save(resources);
        if (!save) {
            log.error("添加失败:{}", resources);
            throw new CreateFailException("添加失败,请联系管理员");
        }
    }

    /**
     * 更新一条数据
     *
     * @param resources
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateJob(Job resources) {
        // 通过名称查找职位信息
        Job oldJob = this.getJobByName(resources);
        if (Objects.nonNull(oldJob) && !oldJob.getId().equals(resources.getId())) {
            log.error("已经存在相同的职位名称:{}", resources.getName());
            throw new EntityExistException(Job.class, "name", resources.getName());
        }
        // 进行修改操作
        boolean update = this.updateById(resources);
        if (!update) {
            log.error("更新失败:{}", resources);
            throw new DeleteFailException("更新失败，请联系管理员");
        }
    }

    /**
     * 验证职位信息是否和用户相关联
     *
     * @param ids
     */
    @Override
    public void verification(Set<Long> ids) {
        // TODO: 2020/8/10 需要UserMapper，后续添加(已解决)
        Integer count = this.userJobMapper.selectCount(Wrappers.<UserJobRelation>lambdaQuery()
                .in(UserJobRelation::getJobId, ids));
        if (!count.equals(0)){
            throw new BadRequestException("该职位与用户存在关联，无法删除");
        }
    }

    /**
     * 批量删除
     *
     * @param ids
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Set<Long> ids) {
        boolean remove = this.removeByIds(ids);
        if (remove) {
            log.info("删除成功,删除的数据的id为:{}", ids);
            // 删除缓存
            this.redisUtils.delByKeys(CacheKey.JOB_CACHES_KEY_PRE, ids);
        } else {
            log.error("删除失败,删除的数据的id为:{}", ids);
            throw new DeleteFailException("删除失败，请联系管理员");
        }
    }

    /**
     * 通过名称查询职位信息
     *
     * @param resources
     * @return
     */
    private Job getJobByName(Job resources) {
        return this.jobMapper.selectOne(Wrappers.<Job>lambdaQuery().eq(Job::getName, resources.getName()));
    }


}
