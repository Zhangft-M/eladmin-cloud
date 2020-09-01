package org.micah.auth.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.alibaba.fastjson.JSON;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.micah.auth.entity.OnlineUser;
import org.micah.auth.service.IOnlineUserService;
import org.micah.core.constant.CacheKey;
import org.micah.core.constant.SecurityConstants;
import org.micah.core.util.RequestUtils;
import org.micah.core.util.StringUtils;
import org.micah.core.web.page.PageResult;
import org.micah.mp.util.PageUtils;
import org.micah.redis.util.RedisUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.context.request.ServletWebRequest;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @program: eladmin-cloud
 * @description:
 * @author: Micah
 * @create: 2020-09-01 18:19
 **/
@Slf4j
@Service
@RequiredArgsConstructor
public class OnlineServiceImpl implements IOnlineUserService {


    private final RedisUtils redisUtils;


    private TokenStore redisTokenStore;

    @Autowired
    public void setRedisTokenStore(TokenStore redisTokenStore) {
        this.redisTokenStore = redisTokenStore;
    }

    /**
     * 保存在线用户信息
     *
     * @param accessToken
     */
    @Override
    public void saveOnlineUser(OAuth2AccessToken accessToken) {
        HttpServletRequest request = ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();
        Map<String, Object> additionalInfo = accessToken.getAdditionalInformation();
        String browser = RequestUtils.getBrowser(request);
        String ip = RequestUtils.getIp(request);
        String cityInfo = RequestUtils.getCityInfo(ip);
        Long userId = (Long) additionalInfo.get(SecurityConstants.DETAILS_USER_ID);
        String username = (String) additionalInfo.get(SecurityConstants.DETAILS_USERNAME);
        String tokenValue = accessToken.getValue();
        int expiresIn = accessToken.getExpiresIn();
        String refreshToken = accessToken.getRefreshToken().getValue();
        OnlineUser onlineUser = new OnlineUser(userId, username, browser, ip, cityInfo,
                LocalDateTime.now(ZoneId.systemDefault()), tokenValue, refreshToken, expiresIn);
        this.redisUtils.set(CacheKey.ONLINE_USER + userId, onlineUser, expiresIn, TimeUnit.SECONDS);
    }

    /**
     * 查询全部数据
     *
     * @param filter   /
     * @param pageable /
     * @return /
     */
    @Override
    public PageResult getAll(String filter, Pageable pageable) {
        List<OnlineUser> onlineUsers = getAll(filter);
        return PageResult.success((long) onlineUsers.size(), PageUtils.toPage(pageable.getPageNumber() - 1, pageable.getPageSize(), onlineUsers));
    }

    /**
     * 踢掉用户下线
     *
     * @param userId
     */
    @Override
    public Boolean kickOut(Long userId, OAuth2AccessToken oAuth2AccessToken) {

        try {
            if (oAuth2AccessToken == null) {
                OnlineUser onlineUser = this.getOne(userId);
                oAuth2AccessToken = this.redisTokenStore.readAccessToken(onlineUser.getAccessToken());
            } else if (userId == null) {
                userId = (Long) oAuth2AccessToken.getAdditionalInformation().get(SecurityConstants.DETAILS_USER_ID);
            }
            // 判断oAuth2AccessToken是否为空，oAuth2AccessToken是否有token的值
            if (BeanUtil.isEmpty(oAuth2AccessToken)) {
                // 都为空说明已经注销，直接返回true
                return true;
            }
            // 清理redis中存储的信息
            this.redisTokenStore.removeAccessToken(oAuth2AccessToken);
            // 清空刷新token
            this.redisTokenStore.removeRefreshToken(oAuth2AccessToken.getRefreshToken());
            this.redisUtils.del(CacheKey.ONLINE_USER + userId);
            return true;
        } catch (Exception e) {
            log.info("注销失败");
            return false;
        }
    }

    @Override
    public OnlineUser getOne(Long userId) {
        return (OnlineUser) this.redisUtils.get(CacheKey.ONLINE_USER + userId);
    }

    /**
     * 获取所有的在线用户
     *
     * @param filter 过滤条件
     * @return /
     */
    @Override
    public List<OnlineUser> getAll(String filter) {
        List<String> keys = this.redisUtils.scan(CacheKey.ONLINE_USER + "*");
        Collections.reverse(keys);
        return keys.stream().map(key -> (OnlineUser) this.redisUtils.get(key)).filter(onlineUser -> {
            if (StringUtils.isNotBlank(filter)) {
                return onlineUser.toString().contains(filter);
            }
            return true;
        }).collect(Collectors.toList());
    }

    /**
     * 检测用户是否在之前已经登录，已经登录踢下线
     *
     * @param userName 用户名
     */
    @Override
    public void checkLoginOnUser(String userName, String ignoreToken) {
        List<OnlineUser> onlineUsers = getAll(userName);
        if (onlineUsers == null || onlineUsers.isEmpty()) {
            return;
        }
        onlineUsers.forEach(onlineUser -> {
            String accessToken = onlineUser.getAccessToken();
            if (StringUtils.isNotBlank(ignoreToken) && !StringUtils.equals(accessToken, ignoreToken)) {
                this.kickOut(onlineUser.getId(), null);
            } else if (StringUtils.isBlank(ignoreToken)) {
                this.kickOut(onlineUser.getId(), null);
            }
        });
    }

    /**
     * 根据用户名强退用户
     *
     * @param username /
     */
    @Override
    @Async
    public void kickOutForUsername(String username) {
        List<OnlineUser> onlineUsers = getAll(username);
        for (OnlineUser onlineUser : onlineUsers) {
            if (onlineUser.getUserName().equals(username)) {
                this.kickOut(onlineUser.getId(), null);
            }
        }
    }


}
