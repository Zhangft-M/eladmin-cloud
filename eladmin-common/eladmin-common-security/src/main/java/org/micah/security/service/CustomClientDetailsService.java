package org.micah.security.service;

import lombok.SneakyThrows;
import org.micah.core.constant.CacheKey;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.client.JdbcClientDetailsService;

import javax.sql.DataSource;

/**
 * @program: eladmin-cloud
 * @description: 对jdbcClientsService进行增强
 * @author: Micah
 * @create: 2020-08-04 17:23
 **/
public class CustomClientDetailsService extends JdbcClientDetailsService {
    public CustomClientDetailsService(DataSource dataSource) {
        super(dataSource);
    }

    /**
     * 将查询结果进行缓存
     * @param clientId
     * @return
     */
    @Override
    @SneakyThrows
    @Cacheable(value = CacheKey.CLIENT_DETAILS_KEY, key = "#clientId", unless = "#result == null")
    public ClientDetails loadClientByClientId(String clientId) {
        return super.loadClientByClientId(clientId);
    }
}
