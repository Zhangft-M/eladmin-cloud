/*
 * Copyright 2019-2020 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.micah.core.constant;

import lombok.experimental.UtilityClass;

/**
 * @author: liaojinlong
 * @date: 2020/6/11 15:49
 * @apiNote: 关于缓存的Key集合
 */
@UtilityClass
public class CacheKey {

    /**
     * 内置 用户、岗位、应用、菜单、角色 相关key
     */
    public final String USER_MODIFY_TIME_KEY = "user:modify:time:key:";
    public final String APP_MODIFY_TIME_KEY = "app:modify:time:key:";
    public final String JOB_MODIFY_TIME_KEY = "job:modify:time:key:";
    public final String MENU_MODIFY_TIME_KEY = "menu:modify:time:key:";
    public final String ROLE_MODIFY_TIME_KEY = "role:modify:time:key:";
    public final String DEPT_MODIFY_TIME_KEY = "dept:modify:time:key:";

    /**
     * 用户
     */
    public final String USER_ID = "user::id:";
    public final String USER_NAME = "user::username:";
    /**
     * 数据
     */
    public final String DATE_USER = "data::user:";
    /**
     * 菜单
     */
    public final String MENU_USER = "menu::user:";
    /**
     * 角色授权
     */
    public final String ROLE_AUTH = "role::auth:";
    /**
     * 角色信息
     */
    public final String ROLE_ID = "role::id:";

    /**
     * 验证码前缀
     */
    public final String DEFAULT_CODE_KEY = "DEFAULT_CODE_KEY:";

    /**
     * 菜单信息缓存
     */
    public final String MENU_DETAILS = "menu_details";

    /**
     * 用户信息缓存
     */
    public final String USER_DETAILS = "user_details";

    /**
     * 字典信息缓存
     */
    public final String DICT_DETAILS = "dict_details";

    /**
     * oauth 客户端信息
     */
    public final String CLIENT_DETAILS_KEY = "oauth:client:details";

    /**
     * 参数缓存
     */
    public final String PARAMS_DETAILS = "params_details";

    /**
     * token前缀
     */
    public final String OAUTH_ACCESS = "el_oauth:access:";

    /**
     * 部门缓存前缀:id
     */
    public final String DEPT_PID_KEY_PRE = "dept::pid:";

    /**
     * 部门缓存前缀：pid
     */
    public final String DEPT_ID_KEY_PRE = "dept::id:";

    /**
     *  字典缓存前缀
     */
    public final String DICT_KEY_PRE = "dict::id:";

    /**
     * 职位缓存前缀
     */
    public final String JOB_CACHES_KEY_PRE = "job::id:";

    /**
     * 菜单缓存前缀：pid
     */
    public final String MENU_PID_KEY_PRE = "menu::pid:";

    /**
     * 菜单缓存前缀:id
     */
    public final String MENU_ID_KEY_PRE = "menu::id:";

    /**
     * 用户菜单缓存前缀
     */
    public final String MENU_USER_KEY_PRE = "menu::user:";

    /**
     * 角色缓存前缀
     */
    public final String ROLE_KEY_PRE = "role::id";

    /**
     * 用户缓存前缀:username
     */
    public final String USER_CACHE_KEY_PRE = "user::username:";
}
