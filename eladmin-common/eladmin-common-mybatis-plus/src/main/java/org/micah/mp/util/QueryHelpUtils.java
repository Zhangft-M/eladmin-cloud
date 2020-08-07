package org.micah.mp.util;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import org.micah.mp.annotation.Query;
import org.micah.mp.annotation.type.OrderType;
import org.micah.mp.annotation.type.SelectType;


import java.lang.reflect.Field;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @program: eladmin-cloud
 * @description: 查询工具类
 * @author: Micah
 * @create: 2020-07-31 19:34
 **/

public class QueryHelpUtils {


    private static final char SEPARATOR = '_';

    public static <T> Wrapper<T> getWrapper(T queryEntity) {
        // 初始化一个查询对象
        QueryWrapper<T> queryWrapper = new QueryWrapper<>();
        // 判断查询条件是否为空
        if (Objects.isNull(queryEntity)) {
            return null;
        }
        try {
            // 获取类中的所有的成员变量
            List<Field> fields = QueryHelpUtils.getAllFiles(queryEntity.getClass(), new ArrayList<>());
            // 遍历所有的变量
            for (Field field : fields) {
                // 获取成员变量访问权限
                boolean accessible = field.isAccessible();
                // 设置成员变量可以访问，避免private修饰的变量无法访问的问题
                field.setAccessible(true);
                // 获取成员变量的值
                Object val = field.get(queryEntity);
                if (Objects.isNull(val)) {
                    // 如果该值为null，直接开始下一次的循环
                    continue;
                }

                // 获取字段上面的注解
                Query query = field.getAnnotation(Query.class);
                if (!Objects.isNull(query)) {
                    // 数据库中的字段名,如果为""则默认为成员变量名转化为有下划线形式
                    // 如果该值没有指定则成员变量名与数据库字段名严格以驼峰命名方式转换
                    String attributeName = QueryHelpUtils.isBlank(query.value()) ? QueryHelpUtils.toUnderScoreCase(field.getName()) : query.value();
                    // 多字段模糊查询,该字段与数据库中的多个字段进行模糊匹配查询
                    // 该属性的值为数据库中的字段名
                    String[] blurry = query.blurry();
                    // 字段是否进行排序
                    boolean isOrder = query.isOder();
                    // 字段查询的类型
                    SelectType selectType = query.type();
                    OrderType orderType = isOrder ? query.order() : null;
                    if (blurry.length > 0) {
                        // 初始化多字段模糊匹配查询
                        QueryHelpUtils.createBlurryQuery(queryWrapper, blurry, val, orderType);
                        continue;
                    }
                    // 初始化基本查询条件
                    QueryHelpUtils.createTypeQuery(selectType, attributeName, val, orderType, queryWrapper);

                }

            }
        } catch (IllegalAccessException e) {
            System.out.println(e.getMessage());
        }
        return queryWrapper;
    }

    /**
     * 获取所有的成员变量
     *
     * @param clazz
     * @return
     */
    private static List<Field> getAllFiles(Class<?> clazz, List<Field> fields) {
        if (clazz != null) {
            fields.addAll(Arrays.asList(clazz.getDeclaredFields()));
            getAllFiles(clazz.getSuperclass(), fields);
        }
        return fields;
    }

    /**
     * 根据查询方式来查询
     *
     * @param selectType    查询方式
     * @param attributeName 数据库字段名
     * @param val           值
     * @param orderType     排序方式
     */
    private static <T> void createTypeQuery(SelectType selectType, String attributeName, Object val, OrderType orderType, QueryWrapper<T> queryWrapper) {
        switch (selectType) {
            case EQUAL:
                queryWrapper.eq(attributeName, val);
                break;
            case NOT_EQUAL:
                queryWrapper.ne(attributeName, val);
                break;
            case GREATER_THAN_NQ:
                queryWrapper.gt(attributeName, val);
                break;
            case GREATER_THAN:
                queryWrapper.ge(attributeName, val);
                break;
            case LESS_THAN:
                queryWrapper.le(attributeName, val);
                break;
            case LESS_THAN_NQ:
                queryWrapper.lt(attributeName, val);
                break;
            case INNER_LIKE:
                queryWrapper.like(attributeName, val);
                break;
            case LEFT_LIKE:
                queryWrapper.likeLeft(attributeName, val);
                break;
            case RIGHT_LIKE:
                queryWrapper.likeRight(attributeName, val);
                break;
            case IN:
                queryWrapper.in(attributeName, val);
                break;
            case NOT_NULL:
                queryWrapper.isNotNull(attributeName);
                break;
            case IS_NULL:
                queryWrapper.isNull(attributeName);
                break;
            case BETWEEN:
                List<Object> between = Collections.singletonList(val);
                queryWrapper.between(attributeName, between.get(0), between.get(1));
                break;
            default:
                break;

        }
        if (orderType != null && orderType.name().equals(OrderType.ASC.name())) {
            queryWrapper.orderByAsc(val.toString());
        } else if (orderType != null && orderType.name().equals(OrderType.DESC.name())) {
            queryWrapper.orderByDesc(val.toString());
        }
    }


    /**
     * 生成模糊匹配查询
     *
     * @param queryWrapper 查询条件封装类
     * @param blurry       数据库对应需要匹配的字段
     * @param val          值
     * @param orderType    排序方式
     * @param <T>
     */
    private static <T> void createBlurryQuery(QueryWrapper<T> queryWrapper, String[] blurry, Object val, OrderType orderType) {
        for (int i = 0; i < blurry.length; i++) {
            if (i == blurry.length - 1) {
                queryWrapper.like(blurry[i], val);
            }
            queryWrapper.like(blurry[i], val).or();
        }
        if (orderType != null && orderType.name().equals(OrderType.ASC.name())) {
            queryWrapper.orderByAsc(val.toString());
        } else if (orderType != null && orderType.name().equals(OrderType.DESC.name())) {
            queryWrapper.orderByDesc(val.toString());
        }
    }

    private static boolean isBlank(final CharSequence cs) {
        int strLen;
        if (cs == null || (strLen = cs.length()) == 0) {
            return true;
        }
        for (int i = 0; i < strLen; i++) {
            if (!Character.isWhitespace(cs.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    /**
     * 驼峰命名法工具
     *
     * @return toUnderScoreCase("helloWorld") = "hello_world"
     */
    private static String toUnderScoreCase(String s){
        if (QueryHelpUtils.isBlank(s)){
            return null;
        }

        // 是否是大写
        boolean isUpperCase = false;
        
        // 初始化一个StringBuilder
        StringBuilder sb = new StringBuilder();
        
        // 遍历字符
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);

            // 下一个字符是否是大写的
            boolean nextIsUpperCase = true;

            if (i < (s.length() - 1)){
                // 判断下一个字母是否为大写的
                nextIsUpperCase = Character.isUpperCase(s.charAt(i + 1));
            }
            // 当前字母是大写
            if (i > 0 && Character.isUpperCase(c)){
                // 如果前一个字母不是大写字母，当前字母是大写
                if (!isUpperCase || !nextIsUpperCase){
                    sb.append(SEPARATOR);
                }
                // 设置当前字母是大写
                isUpperCase = true;
            }else {
                isUpperCase = false;
            }
            // 将当前的字母放入StringBuilder
            sb.append(Character.toLowerCase(c));
        }
        return sb.toString();

    }

}
