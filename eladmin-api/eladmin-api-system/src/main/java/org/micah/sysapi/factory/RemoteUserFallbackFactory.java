package org.micah.sysapi.factory;

import feign.hystrix.FallbackFactory;
import org.micah.sysapi.api.IRemoteUserService;

/**
 * @program: eladmin-cloud
 * @description:
 * @author: Micah
 * @create: 2020-08-12 16:52
 **/
public class RemoteUserFallbackFactory implements FallbackFactory<IRemoteUserService> {
    @Override
    public IRemoteUserService create(Throwable throwable) {
        return null;
    }
}
