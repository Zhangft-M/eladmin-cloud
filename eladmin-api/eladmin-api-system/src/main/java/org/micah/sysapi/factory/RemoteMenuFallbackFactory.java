package org.micah.sysapi.factory;

import feign.hystrix.FallbackFactory;
import org.micah.sysapi.api.IRemoteMenuService;

/**
 * @program: eladmin-cloud
 * @description:
 * @author: Micah
 * @create: 2020-08-12 17:52
 **/
public class RemoteMenuFallbackFactory implements FallbackFactory<IRemoteMenuService> {
    @Override
    public IRemoteMenuService create(Throwable throwable) {
        return null;
    }
}
