package org.micah.security.service;

import cn.hutool.core.collection.CollUtil;
import lombok.extern.slf4j.Slf4j;
import org.micah.core.constant.CacheKey;
import org.micah.core.constant.SecurityConstants;
import org.micah.core.util.StringUtils;
import org.micah.model.Role;
import org.micah.model.SysUser;
import org.micah.model.dto.MenuDto;
import org.micah.model.dto.RoleSmallDto;
import org.micah.model.dto.SysUserDto;
import org.micah.model.dto.UserSmallDto;
import org.micah.security.component.LoginUser;
import org.micah.security.config.LoginProperties;
import org.micah.sysapi.api.IRemoteMenuService;
import org.micah.sysapi.api.IRemoteUserService;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import javax.validation.constraints.NotNull;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @program: eladmin-cloud
 * @description: 用户详细信息
 * @author: Micah
 * @create: 2020-08-05 17:35
 **/
@Service
@Slf4j
public class UserDetailsServiceImpl implements UserDetailsService {


    /**
     * userfeign接口
     */
    private final IRemoteUserService remoteUserService;




    /**
     * 缓存管理器，在第一次调用加载用户的方法系统会自动将其缓存
     */
    private final CacheManager cacheManager;

    /**
     * 用户登录设置
     */
    private final LoginProperties loginProperties;

    public UserDetailsServiceImpl(IRemoteUserService remoteUserService, CacheManager cacheManager,
                                  LoginProperties loginProperties) {
        this.remoteUserService = remoteUserService;
        this.cacheManager = cacheManager;
        this.loginProperties = loginProperties;
    }


    /**
     * 设置用户信息是否被缓存
     * @param enableCache
     */
    public void setEnableCache(Boolean enableCache) {
        this.loginProperties.setCacheEnable(enableCache);
    }

    /**
     * 通过用户名加载用户
     * @param username
     * @return
     * @throws UsernameNotFoundException
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Cache cache = this.cacheManager.getCache(CacheKey.USER_DETAILS);
        if (cache != null && cache.get(username) != null) {
            // 缓存中存在，直接从缓存中获取
            return (LoginUser) cache.get(username).get();
        }
        // 缓存不存在则调用feign获取用户信息
        UserSmallDto result = this.remoteUserService.getUserDetails(username,SecurityConstants.FROM_IN);
        UserDetails userDetails = this.getUserDetails(result);
        // 放入缓存
        if (loginProperties.getCacheEnable()) {
            assert cache != null;
            cache.put(username, userDetails);
        }
        return userDetails;
    }


    /**
     * 构建UserDetails
     * @param user
     * @return
     */
    private UserDetails getUserDetails(UserSmallDto user) {
        if (user == null) {
            throw new UsernameNotFoundException("用户不存在");
        }
        // SysUser sysUser = result.getBody();
        Set<String> dbAuthsSet = new HashSet<>();
        if (CollUtil.isNotEmpty(user.getRoleNames())) {
            user.getRoleNames().forEach(role -> {
                dbAuthsSet.add(SecurityConstants.ROLE + role);
            });
            // 获取所有的角色
            // user.ge.forEach(role -> dbAuthsSet.add(SecurityConstants.ROLE + role.getName()));
            // 根据用户的角色查询用户的菜单权限
            // Set<Long> ids = sysUser.getRoles().stream().map(Role::getId).collect(Collectors.toSet());
            // ResponseEntity<List<MenuDto>> menuResult = this.remoteMenuService.queryByRoleIds(ids,SecurityConstants.FROM_IN);
            // List<MenuDto> menuDtos = menuResult.getBody();
            // TODO: 2020/8/12 远程查询部门数据权限
            //......
        }
        if (CollUtil.isNotEmpty(user.getPermissions())){
            dbAuthsSet.addAll(user.getPermissions());
        }
        // 封装为Collection<? extends GrantedAuthority>
        String[] authArray = dbAuthsSet.stream().filter(StringUtils::isNotBlank).toArray(String[]::new);
        // 不能传入空字符串
        List<GrantedAuthority> authorityList = AuthorityUtils.createAuthorityList(authArray);
        // Collection<GrantedAuthority> authorities = AuthorityUtils.createAuthorityList(dbAuthsSet.toArray(new String[0]));
        return new LoginUser(user.getId(), user.getUsername(), user.getPassword(), user.getEnabled(),
                true, true, true, authorityList, new ArrayList<>());

    }
}

