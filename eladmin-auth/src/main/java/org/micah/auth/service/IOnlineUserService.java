package org.micah.auth.service;

import cn.hutool.core.bean.BeanUtil;
import org.micah.auth.entity.OnlineUser;
import org.micah.core.constant.CacheKey;
import org.micah.core.constant.SecurityConstants;
import org.micah.core.util.RequestUtils;
import org.micah.core.util.StringUtils;
import org.micah.core.web.page.PageResult;
import org.micah.mp.util.PageUtils;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.oauth2.common.OAuth2AccessToken;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @program: eladmin-cloud
 * @description:
 * @author: Micah
 * @create: 2020-09-01 18:19
 **/
public interface IOnlineUserService {
    /**
     * 保存在线用户信息
     *
     * @param accessToken
     */
    void saveOnlineUser(OAuth2AccessToken accessToken);

    /**
     * 查询全部数据
     * @param filter /
     * @param pageable /
     * @return /
     */
     PageResult getAll(String filter, Pageable pageable);

    /**
     * 踢掉用户下线
     * @param userId
     */
     Boolean kickOut(Long userId,OAuth2AccessToken oAuth2AccessToken);

     OnlineUser getOne(Long userId);

    /**
     * 获取所有的在线用户
     * @param filter 过滤条件
     * @return /
     */
     List<OnlineUser> getAll(String filter);

    /**
     * 检测用户是否在之前已经登录，已经登录踢下线
     * @param userName 用户名
     */
    void checkLoginOnUser(String userName, String ignoreToken);

    /**
     * 根据用户名强退用户
     * @param username /
     */
    void kickOutForUsername(String username);
}
