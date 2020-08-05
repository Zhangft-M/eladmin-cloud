package org.micah.core.constant;

import lombok.experimental.UtilityClass;

/**
 * @program: eladmin-cloud
 * @description: 缓存相关的静态变量
 * @author: Micah
 * @create: 2020-08-04 17:27
 **/

public class CacheConstants {

    /**
     * 验证码前缀
     */
    public static final String DEFAULT_CODE_KEY = "DEFAULT_CODE_KEY:";

    /**
     * 菜单信息缓存
     */
    public static final String MENU_DETAILS = "menu_details";

    /**
     * 用户信息缓存
     */
    public static final String USER_DETAILS = "user_details";

    /**
     * 字典信息缓存
     */
    public static final String DICT_DETAILS = "dict_details";

    /**
     * oauth 客户端信息
     */
    public static final String CLIENT_DETAILS_KEY = "oauth:client:details";

    /**
     * 参数缓存
     */
    public static final String PARAMS_DETAILS = "params_details";

    /**
     * token前缀
     */
    public static final String OAUTH_ACCESS = "oauth:access:";
}
