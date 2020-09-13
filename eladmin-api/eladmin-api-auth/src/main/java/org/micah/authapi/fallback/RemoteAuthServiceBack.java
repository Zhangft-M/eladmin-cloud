package org.micah.authapi.fallback;

import org.micah.authapi.api.IRemoteAuthService;
import org.springframework.http.ResponseEntity;

import java.util.Set;

/**
 * @program: eladmin-cloud
 * @description:
 * @author: Micah
 * @create: 2020-09-13 16:20
 **/
public class RemoteAuthServiceBack implements IRemoteAuthService {
    @Override
    public ResponseEntity<Void> delete(Set<Long> ids) throws Exception {
        return null;
    }
}
