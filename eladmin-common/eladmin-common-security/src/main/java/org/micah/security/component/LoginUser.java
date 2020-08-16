package org.micah.security.component;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import org.micah.model.SysUser;
import org.micah.model.dto.SysUserDto;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

/**
 * @program: eladmin-cloud
 * @description: 登录用户实体类
 * @author: Micah
 * @create: 2020-08-04 17:33
 **/
@Getter
@Setter
public class LoginUser extends User implements Serializable {

    private static final long serialVersionUID = 5232651448040614594L;

    private SysUser user;

    private List<Long> dataScopes;


    public LoginUser(String username, String password, boolean enabled,
                     boolean accountNonExpired, boolean credentialsNonExpired, boolean accountNonLocked,
                     Collection<? extends GrantedAuthority> authorities, SysUser user, List<Long> dataScopes) {
        super(username, password, enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, authorities);
        this.user = user;
        this.dataScopes = dataScopes;
    }
}
