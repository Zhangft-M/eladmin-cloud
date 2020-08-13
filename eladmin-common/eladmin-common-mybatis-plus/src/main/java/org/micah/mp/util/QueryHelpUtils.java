package org.micah.mp.util;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfoHelper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.sun.javafx.binding.StringConstant;
import org.micah.mp.annotation.Query;
import org.micah.mp.annotation.type.SortType;
import org.micah.mp.annotation.type.SelectType;


import java.lang.reflect.Field;

import java.util.*;

/**
 * @program: eladmin-cloud
 * @description: 查询工具类
 * @author: Micah
 * @create: 2020-07-31 19:34
 **/

@SuppressWarnings("unchecked")
public final class QueryHelpUtils {


    private static final char SEPARATOR = '_';

    private static final char DOT = '.';

    public static <E, T> QueryWrapper<T> getWrapper(E queryEntity, Class<T> clazz) {
        // 判断查询条件是否为空
        if (Objects.isNull(queryEntity) || Objects.isNull(clazz)) {
            return null;
        }
        // 获取对应的表的名称
        TableInfo tableInfo = TableInfoHelper.getTableInfo(clazz);
        String tableName = tableInfo.getTableName() + DOT;
        // 初始化一个查询对象
        QueryWrapper<T> queryWrapper = new QueryWrapper<>();

        try {
            // 获取类中的所有的成员变量
            List<Field> fields = QueryHelpUtils.getAllFiles(queryEntity.getClass(), new ArrayList<>());
            // 遍历所有的变量
            for (Field field : fields) {
                // 获取成员变量访问权限
                boolean accessible = field.isAccessible();
                // 设置成员变量可以访问，避免private修饰的变量无法访问的问题
                field.setAccessible(true);
                // 获取字段上面的注解
                Query query = field.getAnnotation(Query.class);
                if (query != null) {
                    // 获取成员变量的值
                    Object val = field.get(queryEntity);
                    // 数据库中的字段名,如果为""则默认为成员变量名转化为有下划线形式
                    // 如果该值没有指定则成员变量名与数据库字段名严格以驼峰命名方式转换
                    String attributeName = QueryHelpUtils.isBlank(query.value()) ? QueryHelpUtils.toUnderScoreCase(field.getName()) : query.value();
                    if (Objects.isNull(val)) {
                       /* if (query.isSort()){
                            SortType sortType = query.sort();
                            if (sortType.name().equals(SortType.ASC.name())) {
                                queryWrapper.orderByAsc(attributeName);
                            } else if ( sortType.name().equals(SortType.DESC.name())) {
                                queryWrapper.orderByDesc(attributeName);
                            }
                        }*/
                        continue;
                    }
                    // 多字段模糊查询,该字段与数据库中的多个字段进行模糊匹配查询
                    // 该属性的值为数据库中的字段名
                    String[] blurry = query.blurry();
                    // 字段是否进行排序
                    // boolean isSort = query.isSort();
                    // 字段查询的类型
                    SelectType selectType = query.type();
                    // SortType sortType = isSort ? query.sort() : null;
                    if (blurry.length > 0) {
                        // 初始化多字段模糊匹配查询
                        QueryHelpUtils.createBlurryQuery(queryWrapper, blurry, val, tableName);
                        continue;
                    }
                    // 初始化基本查询条件
                    QueryHelpUtils.createTypeQuery(selectType, attributeName, val, queryWrapper, tableName);

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
     * @param tableName
     */
    private static <T> void createTypeQuery(SelectType selectType, String attributeName, Object val, QueryWrapper<T> queryWrapper, String tableName) {
        switch (selectType) {
            case EQUAL:
                queryWrapper.eq(tableName + attributeName, val);
                break;
            case NOT_EQUAL:
                queryWrapper.ne(tableName + attributeName, val);
                break;
            case GREATER_THAN_NQ:
                queryWrapper.gt(tableName + attributeName, val);
                break;
            case GREATER_THAN:
                queryWrapper.ge(tableName + attributeName, val);
                break;
            case LESS_THAN:
                queryWrapper.le(tableName + attributeName, val);
                break;
            case LESS_THAN_NQ:
                queryWrapper.lt(tableName + attributeName, val);
                break;
            case INNER_LIKE:
                queryWrapper.like(tableName + attributeName, val);
                break;
            case LEFT_LIKE:
                queryWrapper.likeLeft(tableName + attributeName, val);
                break;
            case RIGHT_LIKE:
                queryWrapper.likeRight(tableName + attributeName, val);
                break;
            case IN:
                if (CollectionUtils.isNotEmpty((Collection<Long>) val)) {
                    queryWrapper.in(tableName + attributeName, (Collection<Long>) val);
                }
                break;
            case NOT_NULL:
                queryWrapper.isNotNull(tableName + attributeName);
                break;
            case IS_NULL:
                queryWrapper.isNull(tableName + attributeName);
                break;
            case BETWEEN:
                List<Object> between = new ArrayList<>((List<Object>) val);
                queryWrapper.between(tableName + attributeName, between.get(0), between.get(1));
                break;
            default:
                break;

        }
    }


    /**
     * 生成模糊匹配查询
     *
     * @param <T>
     * @param queryWrapper 查询条件封装类
     * @param blurry       数据库对应需要匹配的字段
     * @param val          值
     * @param tableName
     */
    private static <T> void createBlurryQuery(QueryWrapper<T> queryWrapper, String[] blurry, Object val, String tableName) {
        for (int i = 0; i < blurry.length; i++) {
            if (i == blurry.length - 1) {
                queryWrapper.like(tableName + QueryHelpUtils.toUnderScoreCase(blurry[i]), val);
                break;
            }
            queryWrapper.like(tableName + QueryHelpUtils.toUnderScoreCase(blurry[i]), val).or();
        }
       /* if (sortType != null && sortType.name().equals(SortType.ASC.name())) {
            queryWrapper.orderByAsc(val.toString());
        } else if (sortType != null && sortType.name().equals(SortType.DESC.name())) {
            queryWrapper.orderByDesc(val.toString());
        }*/
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
     * @return toUnderScoreCase(" helloWorld ") = "hello_world"
     */
    public static String toUnderScoreCase(String s) {
        if (QueryHelpUtils.isBlank(s)) {
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

            if (i < (s.length() - 1)) {
                // 判断下一个字母是否为大写的
                nextIsUpperCase = Character.isUpperCase(s.charAt(i + 1));
            }
            // 当前字母是大写
            if (i > 0 && Character.isUpperCase(c)) {
                // 如果前一个字母不是大写字母，当前字母是大写
                if (!isUpperCase || !nextIsUpperCase) {
                    sb.append(SEPARATOR);
                }
                // 设置当前字母是大写
                isUpperCase = true;
            } else {
                isUpperCase = false;
            }
            // 将当前的字母放入StringBuilder
            sb.append(Character.toLowerCase(c));
        }
        return sb.toString();

    }

}
