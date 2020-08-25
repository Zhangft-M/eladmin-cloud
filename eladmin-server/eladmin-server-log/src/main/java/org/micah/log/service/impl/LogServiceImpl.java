package org.micah.log.service.impl;

import cn.hutool.core.lang.Dict;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.micah.core.util.StringUtils;
import org.micah.core.web.page.PageResult;
import org.micah.exception.global.CreateFailException;
import org.micah.exception.global.DeleteFailException;
import org.micah.log.mapper.LogMapper;
import org.micah.log.service.ILogService;
import org.micah.model.Log;
import org.micah.model.mapstruct.LogErrorMapStruct;
import org.micah.model.mapstruct.LogSmallMapStruct;
import org.micah.model.query.LogQueryCriteria;
import org.micah.mp.util.PageUtils;
import org.micah.mp.util.QueryHelpUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * @program: eladmin-cloud
 * @description:
 * @author: Micah
 * @create: 2020-08-25 15:30
 **/
@Service
@RequiredArgsConstructor
public class LogServiceImpl extends ServiceImpl<LogMapper, Log> implements ILogService {

    private static final Logger logger = LoggerFactory.getLogger(LogServiceImpl.class);
    
    private static final String ERROR = "ERROR";
    private static final String INFO = "INFO";

    private final LogErrorMapStruct logErrorMapStruct;

    private final LogSmallMapStruct logSmallMapStruct;
    /**
     * 查询所有的日志
     *
     * @param criteria
     * @return
     */
    @Override
    public List<Log> queryAll(LogQueryCriteria criteria) {
        QueryWrapper<Log> wrapper = QueryHelpUtils.getWrapper(criteria, Log.class);
        return this.list(wrapper);
    }

    /**
     * 分页查询所有的日志
     *
     * @param criteria /
     * @param pageable /
     * @return /
     */
    @Override
    public PageResult queryAll(LogQueryCriteria criteria, Pageable pageable) {
        Page<Log> page = PageUtils.startPageAndSort(pageable);
        QueryWrapper<Log> wrapper = QueryHelpUtils.getWrapper(criteria, Log.class);
        Page<Log> logPage = this.page(page,wrapper);
        if (StringUtils.equalsIgnoreCase(criteria.getLogType(),ERROR)){
            return PageResult.success(logPage.getTotal(),logPage.getPages(),this.logErrorMapStruct.toDto(logPage.getRecords()));
        }
        return PageResult.success(logPage.getTotal(),logPage.getPages(),logPage.getRecords());
    }

    /**
     * 导出数据
     *
     * @param data     /
     * @param response /
     */
    @Override
    public void download(List<Log> data, HttpServletResponse response) {

    }

    /**
     * 根据用户名查询
     *
     * @param criteria
     * @param pageable
     * @return
     */
    @Override
    public PageResult queryAllByUser(LogQueryCriteria criteria, Pageable pageable) {
        Page<Log> page = PageUtils.startPageAndSort(pageable);
        QueryWrapper<Log> wrapper = QueryHelpUtils.getWrapper(criteria, Log.class);
        Page<Log> logPage = this.page(page, wrapper);
        return PageResult.success(logPage.getTotal(),logPage.getPages(),this.logSmallMapStruct.toDto(logPage.getRecords()));
    }

    /**
     * 查询详细的异常
     * @param id
     * @return
     */
    @Override
    public Dict findByErrDetail(Long id) {
        Log log = Optional.ofNullable(this.getById(id)).orElse(null);
        byte[] details = Objects.isNull(log) ? "".getBytes() : log.getExceptionDetail();
        return Dict.create().set("exception",details);
    }

    /**
     * 保存日志
     *
     * @param log
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveLog(Log log) {
        if (!this.save(log)){
            logger.warn("插入失败:{}",log);
            throw new CreateFailException("添加失败");
        }
    }

    /**
     * 删除错误日志
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delAllByError() {
        if (!this.remove(Wrappers.<Log>lambdaUpdate().eq(Log::getLogType,ERROR))){
            logger.warn("删除错误日志失败");
            throw new DeleteFailException("删除错误日志失败");
        }
    }

    /**
     * 删除所有的info日志
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delAllByInfo() {
        if (!this.remove(Wrappers.<Log>lambdaUpdate().eq(Log::getLogType,INFO))){
            logger.warn("删除信息日志失败");
            throw new DeleteFailException("删除信息日志失败");
        }
    }

}
