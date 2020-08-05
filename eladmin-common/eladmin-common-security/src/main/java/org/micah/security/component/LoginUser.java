package org.micah.security.component;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

/**
 * @program: eladmin-cloud
 * @description: 登录用户实体类
 * @author: Micah
 * @create: 2020-08-04 17:33
 **/
public class LoginUser extends User {
    private static final long serialVersionUID = -7474056335067943624L;

    /**
     * 用户ID
     */
    private Long userId;

    public LoginUser(Long userId, String username, String password, boolean enabled, boolean accountNonExpired,
                     boolean credentialsNonExpired, boolean accountNonLocked, Collection<? extends GrantedAuthority> authorities)
    {
        super(username, password, enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, authorities);
        this.userId = userId;
    }

    public Long getUserId()
    {
        return userId;
    }

    public void setUserId(Long userId)
    {
        this.userId = userId;
    }
}
