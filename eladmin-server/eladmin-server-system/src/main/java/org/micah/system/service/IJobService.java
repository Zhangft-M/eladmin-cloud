package org.micah.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.micah.core.web.page.PageResult;
import org.micah.model.Job;
import org.micah.model.dto.JobDto;
import org.micah.model.query.JobQueryCriteria;
import org.springframework.data.domain.Pageable;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Set;

/**
 * @program: eladmin-cloud
 * @description:
 * @author: Micah
 * @create: 2020-08-10 16:29
 **/
public interface IJobService extends IService<Job> {

    /**
     * 根据条件查询所有，不分页
     * @param queryCriteria
     * @return
     */
    List<JobDto> queryAll(JobQueryCriteria queryCriteria);

    /**
     * 根据条件查询所有，分页查询
     * @param queryCriteria
     * @param pageable
     * @return
     */
    PageResult queryAll(JobQueryCriteria queryCriteria, Pageable pageable);

    /**
     * 将数据导出为excel文件
     * @param queryAll
     * @param response
     */
    void download(List<JobDto> queryAll, HttpServletResponse response);

    /**
     * 添加一条数据
     * @param resources
     */
    void create(Job resources);

    /**
     * 更新一条数据
     * @param resources
     */
    void updateJob(Job resources);

    /**
     * 验证职位信息是否和用户相关联
     * @param ids
     */
    void verification(Set<Long> ids);

    /**
     * 批量删除
     * @param ids
     */
    void delete(Set<Long> ids);
}
