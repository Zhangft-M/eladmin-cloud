package org.micah.mp.annotation.type;

/**
 * @program: eladmin-cloud
 * @description: 查询方式
 * @author: Micah
 * @create: 2020-07-31 19:17
 **/
public enum SelectType {

    // jie 2019/6/4 相等
    EQUAL
    // 大于
    ,GREATER_THAN_NQ
    // Dong ZhaoYang 2017/8/7 大于等于
    , GREATER_THAN
    // Dong ZhaoYang 2017/8/7 小于
    , LESS_THAN_NQ
    // Dong ZhaoYang 2017/8/7 小于等于
    , LESS_THAN
    // Dong ZhaoYang 2017/8/7 中模糊查询
    , INNER_LIKE
    // Dong ZhaoYang 2017/8/7 左模糊查询
    , LEFT_LIKE
    // Dong ZhaoYang 2017/8/7 右模糊查询
    , RIGHT_LIKE
    // jie 2019/6/4 包含
    , IN
    // 不等于
    , NOT_EQUAL
    // between
    , BETWEEN
    // 不为空
    , NOT_NULL
    // 为空
    , IS_NULL

}
