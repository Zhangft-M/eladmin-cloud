package org.micah.gateway.service;

import cn.hutool.db.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.micah.core.web.page.PageResult;
import org.micah.gateway.entity.Router;
import org.springframework.context.ApplicationEventPublisherAware;

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
     * @param size
     * @param sort
     */
    PageResult queryPage(int page, int size, String sort);

    /**
     * 通过id查询
     * @param id
     * @return
     */
    Router selectById(Integer id);
}
