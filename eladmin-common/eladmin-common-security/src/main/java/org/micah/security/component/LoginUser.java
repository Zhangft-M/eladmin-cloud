package org.micah.security.component;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import org.micah.model.dto.SysUserDto;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

/**
 * @program: eladmin-cloud
 * @description: 登录用户实体类
 * @author: Micah
 * @create: 2020-08-04 17:33
 **/
@Getter
public class LoginUser extends User {

    private static final long serialVersionUID = 5232651448040614594L;

    private final SysUserDto user;

    private final List<Long> dataScopes;

    public LoginUser(String username, String password, boolean enabled,
                     boolean accountNonExpired, boolean credentialsNonExpired, boolean accountNonLocked,
                     Collection<? extends GrantedAuthority> authorities, SysUserDto user, List<Long> dataScopes) {
        super(username, password, enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, authorities);
        this.user = user;
        this.dataScopes = dataScopes;
    }
}
