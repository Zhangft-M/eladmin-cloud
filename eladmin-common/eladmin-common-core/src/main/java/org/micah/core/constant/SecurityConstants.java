package org.micah.core.constant;

/**
 * @program: eladmin-cloud
 * @description:
 * @author: Micah
 * @create: 2020-08-03 18:59
 **/
public final class SecurityConstants {

    public static final String BEARER_TOKEN_TYPE = "Bearer";

    /**
     * 授权token url
     */
    public static final String AUTH_TOKEN = "/oauth/token";

    /**
     * 注销token url
     */
    public static final String TOKEN_LOGOUT = "/token/logout";

    /**
     * 用户ID字段
     */
    public static final String DETAILS_USER_ID = "user_id";

    /**
     * 用户名字段
     */
    public static final String DETAILS_USERNAME = "username";

    /**
     * 登录成功
     */
    public static final String LOGIN_SUCCESS = "Success";

    /**
     * 注销
     */
    public static final String LOGOUT = "Logout";

    /**
     * 登录
     */
    public static final String LOGIN = "Login";

    /**
     * 注册
     */
    public static final String REGISTER = "Register";

    /**
     * 登录失败
     */
    public static final String LOGIN_FAIL = "Error";

    /**
     * sys_oauth_client_details 表的字段，不包括client_id、client_secret
     */
    public static final String CLIENT_FIELDS = "client_id, client_secret, resource_ids, scope, "
            + "authorized_grant_types, web_server_redirect_uri, authorities, access_token_validity, "
            + "refresh_token_validity, additional_information, autoapprove";

    /**
     * JdbcClientDetailsService 查询语句
     */
    public static final String BASE_FIND_STATEMENT = "select " + CLIENT_FIELDS + " from sys_oauth_client_details";

    /**
     * 按条件client_id 查询
     */
    public static final String DEFAULT_SELECT_STATEMENT = BASE_FIND_STATEMENT + " where client_id = ?";

    /**
     * 默认的查询语句,按照id排序
     */
    public static final String DEFAULT_FIND_STATEMENT = BASE_FIND_STATEMENT + " order by client_id";

    public static final String ROLE = "ROLE_";
    public static final String RESOURCE_SERVER_CONFIGURER = "resourceServerConfigurerAdapter";

    public static final String FROM = "from";


    public static final String FROM_IN = "Y";

    public static final String AUTHORITIES_KEY = "auth";

    public static final String DATA_SCOPES = "data_scopes";
}
