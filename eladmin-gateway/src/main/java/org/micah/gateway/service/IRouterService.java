package org.micah.gateway.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.micah.core.web.page.PageResult;
import org.micah.gateway.entity.Router;
import org.micah.gateway.entity.query.RouterQueryCriteria;
import org.springframework.context.ApplicationEventPublisherAware;

import java.util.Set;

/**
 * @program: eladmin-cloud
 * @description: 网关业务接口
 * @author: MicahZhang
 * @create: 2020-07-29 15:27
 **/
public interface IRouterService extends IService<Router>, ApplicationEventPublisherAware {
    /**
     * 初始化数据
     */
    void initData();

    /**
     * 查询所有的路由接口数据
     * @return
     * @param pageable
     * @param criteria
     * @param size
     * @param sort
     */
    PageResult queryPage(RouterQueryCriteria criteria, int page, int size, String sort);

    /**
     * 通过id查询
     * @param id
     * @return
     */
    Router selectById(Long id);

    /**
     * 更新路由断言
     * @param router
     */
    void updateRouterPredicate(Router router);

    /**
     * 跟新路由过滤器
     * @param router
     */
    void updateRouterFilter(Router router);


    /**
     * 根据id删除
     * @param ids
     */
    void deleteByIds(Set<Long> ids);
}
