package org.micah.security.component;

import org.micah.core.constant.SecurityConstants;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.oauth2.provider.token.DefaultUserAuthenticationConverter;
import org.springframework.security.oauth2.provider.token.UserAuthenticationConverter;
import org.springframework.util.StringUtils;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @program: eladmin-cloud
 * @description: 自定义token信息处理，处理check_token之后的token信息
 * @author: Micah
 * @create: 2020-08-04 18:04
 **/
public class CustomizeUserAuthenticationConverter implements UserAuthenticationConverter {

    private static final String N_A = "N/A";

    /**
     * 将信息返回给资源服务器,使用的是默认的实现
     * {@link DefaultUserAuthenticationConverter#convertUserAuthentication(org.springframework.security.core.Authentication)}
     * @param userAuthentication 用户认证信息，包含了用户信息以及权限信息
     * @return
     */
    @Override
    public Map<String, ?> convertUserAuthentication(Authentication userAuthentication) {
        Map<String, Object> response = new LinkedHashMap<String, Object>();
        response.put(USERNAME, userAuthentication.getName());
        if (userAuthentication.getAuthorities() != null && !userAuthentication.getAuthorities().isEmpty()) {
            response.put(AUTHORITIES, AuthorityUtils.authorityListToSet(userAuthentication.getAuthorities()));
        }
        return response;
    }

    @Override
    public Authentication extractAuthentication(Map<String, ?> map) {
        if (map.containsKey(USERNAME)) {
            Collection<? extends GrantedAuthority> authorities = getAuthorities(map);
            // Long userId = (Long) map.get(SecurityConstants.DETAILS_USER_ID);
            String username = (String) map.get(SecurityConstants.DETAILS_USERNAME);
            // TODO: 2020/8/10 获取dataScopes
            /*LoginUser loginUser = new LoginUser(userId,username,N_A,true,true,true,true,
                    authorities, null);*/
            LoginUser loginUser = new LoginUser(username,N_A,true,true,true,true,
                    authorities,null);
            return new UsernamePasswordAuthenticationToken(loginUser, "N/A", authorities);
        }
        return null;
    }

    private Collection<? extends GrantedAuthority> getAuthorities(Map<String, ?> map) {
        Object authorities = map.get(AUTHORITIES);
        if (authorities instanceof String) {
            return AuthorityUtils.commaSeparatedStringToAuthorityList((String) authorities);
        }
        if (authorities instanceof Collection) {
            return AuthorityUtils.commaSeparatedStringToAuthorityList(StringUtils
                    .collectionToCommaDelimitedString((Collection<?>) authorities));
        }
        throw new IllegalArgumentException("Authorities must be either a String or a Collection");
    }
}
