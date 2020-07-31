package org.micah.gateway.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.micah.gateway.entity.GatewayEntity;
import org.springframework.context.ApplicationEventPublisherAware;

/**
 * @program: eladmin-cloud
 * @description: 网关业务接口
 * @author: MicahZhang
 * @create: 2020-07-29 15:27
 **/
public interface IGateWayService extends IService<GatewayEntity>, ApplicationEventPublisherAware {
    void initData();
}
