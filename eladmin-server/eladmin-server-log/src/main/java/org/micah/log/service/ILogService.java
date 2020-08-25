package org.micah.log.service;

import cn.hutool.core.lang.Dict;
import com.baomidou.mybatisplus.extension.service.IService;
import org.micah.core.web.page.PageResult;
import org.micah.model.Log;
import org.micah.model.query.LogQueryCriteria;
import org.springframework.data.domain.Pageable;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @program: eladmin-cloud
 * @description:
 * @author: Micah
 * @create: 2020-08-25 15:27
 **/
public interface ILogService extends IService<Log> {
    /**
     * 查询所有的日志
     * @param criteria
     * @return
     */
    List<Log> queryAll(LogQueryCriteria criteria);

    /**
     * 分页查询所有的日志
     * @param criteria /
     * @param pageable /
     * @return /
     */
    PageResult queryAll(LogQueryCriteria criteria, Pageable pageable);

    /**
     * 导出数据
     * @param data /
     * @param response /
     */
    void download(List<Log> data, HttpServletResponse response);

    /**
     * 根据用户名查询
     * @param criteria
     * @param pageable
     * @return
     */
    PageResult queryAllByUser(LogQueryCriteria criteria, Pageable pageable);

    /**
     *
     * @param id
     * @return
     */
    Dict findByErrDetail(Long id);

    /**
     * 删除错误日志
     */
    void delAllByError();

    /**
     * 删除所有的info日志
     */
    void delAllByInfo();

    /**
     * 保存日志
     * @param log
     */
    void saveLog(Log log);
}
