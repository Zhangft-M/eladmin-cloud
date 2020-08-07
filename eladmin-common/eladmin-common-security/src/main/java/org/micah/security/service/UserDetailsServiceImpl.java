package org.micah.security.service;

import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.system.UserInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.micah.core.constant.CacheConstants;
import org.micah.core.constant.SecurityConstants;
import org.micah.security.component.LoginUser;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * @program: eladmin-cloud
 * @description: 用户详细信息
 * @author: Micah
 * @create: 2020-08-05 17:35
 **/
@Service
@Slf4j
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return null;
    }

    /* *//**
     * feign接口
     *//*
    private final RemoteUserService remoteUserService;

    *//**
     * 缓存管理器，在第一次调用loadUserByUsername之后会把用户信息给缓存
     *//*
    private final CacheManager cacheManager;

    *//**
     * 用户密码登录，远程调用feign客户端加载用户信息
     * @param username 用户名
     * @return
     * @throws UsernameNotFoundException
     *//*
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Cache cache = this.cacheManager.getCache(CacheConstants.USER_DETAILS);
        if (cache!=null && cache.get(username)!=null){
            // 缓存中存在，直接从缓存中获取
            return (LoginUser)cache.get(username).get();
        }
        // 缓存不存在则调用feign获取用户信息
        ResponseEntity<UserInfo> result = this.remoteUserService.getUserInfo(username);
        UserDetails userDetails = this.getUserDetails(result);
        // 放入缓存
        cache.put(username,userDetails);
        return userDetails;
    }

    *//**
     * 构造UserDetails
     * @param result
     * @return
     *TODO: 2020/8/5 在系统用户功能处理完毕再处理
     *//*
    private UserDetails getUserDetails(ResponseEntity<UserInfo> result) {
        if (result == null || result.getBody() == null) {
            throw new UsernameNotFoundException("用户不存在");
        }
        UserInfo info = result.getBody();
        Set<String> dbAuthsSet = new HashSet<>();
        if (ArrayUtil.isNotEmpty(info.getRoles())) {
            // 获取角色
            Arrays.stream(info.getRoles()).forEach(role -> dbAuthsSet.add(SecurityConstants.ROLE + role));
            // 获取资源
            dbAuthsSet.addAll(Arrays.asList(info.getPermissions()));

        }
        Collection<? extends GrantedAuthority> authorities = AuthorityUtils
                .createAuthorityList(dbAuthsSet.toArray(new String[0]));
        SysUser user = info.getSysUser();

        // 构造security用户
        return new PigUser(user.getUserId(), user.getDeptId(), user.getUsername(),
                SecurityConstants.BCRYPT + user.getPassword(),
                StrUtil.equals(user.getLockFlag(), CommonConstants.STATUS_NORMAL), true, true, true, authorities);

    }*/
}
