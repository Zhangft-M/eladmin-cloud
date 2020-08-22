package org.micah.security.component;

import cn.hutool.core.collection.CollUtil;
import org.micah.security.util.SecurityUtils;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @program: eladmin-cloud
 * @description:
 * @author: Micah
 * @create: 2020-08-22 16:44
 **/
@Component("el")
public class PermissionComponent {

    /**
     * 权限验证
     */
    public Boolean check(String ... permissions){
        // 获取当前用户的权限
        List<String> userPermissions = SecurityUtils.getUser().getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList());
        if (CollUtil.isEmpty(userPermissions)){
            return false;
        }
        // 比较是否有权限
        return userPermissions.contains("admin") || Arrays.stream(permissions).anyMatch(userPermissions::contains);
    }
}
