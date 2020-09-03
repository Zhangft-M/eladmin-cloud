package org.micah.mnt.util;

import cn.hutool.crypto.SecureUtil;
import com.google.common.collect.Lists;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.micah.core.util.StringUtils;

import javax.sql.DataSource;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.sql.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @program: eladmin-cloud
 * @description:
 * @author: Micah
 * @create: 2020-09-03 17:42
 **/
@Slf4j
@UtilityClass
public class SqlUtils {

    public final String COLON = ":";

    private volatile Map<String, HikariDataSource> map = new HashMap<>();


    /**
     * 拼装数据库连接url用户名密码并加密作为map结构中的key
     *
     * @param url
     * @param username
     * @param password
     * @return
     */
    public String getKey(String url, String username, String password) {
        StringBuilder sb = new StringBuilder();
        if (StringUtils.isNotBlank(username)) {
            sb.append(username);
        }
        if (StringUtils.isNotBlank(password)) {
            sb.append(COLON).append(password);
        }
        if (StringUtils.isNotBlank(url)) {
            sb.append(COLON).append(url);
        }
        return SecureUtil.md5(sb.toString());
    }

    /**
     * 获取数据源
     *
     * @param url
     * @param username
     * @param password
     * @return
     */
    public DataSource getDataSource(String url, String username, String password) {
        String key = SqlUtils.getKey(url.trim(), username, password);
        // 判断缓存中是否存在该数据源
        if (!map.containsKey(key) || map.get(key) == null) {
            // 不存在，生成一个数据源
            HikariDataSource dataSource = new HikariDataSource();

            String driverClassname = null;
            // 获取驱动类名
            try {
                driverClassname = DriverManager.getDriver(url.trim()).getClass().getName();
            } catch (SQLException e) {
                log.info("连接的数据库为非关系型数据库,或者系统无法通过连接的url获取驱动类名");
                throw new RuntimeException("Get class name error: =" + url);
            }
            if (StringUtils.isBlank(driverClassname)) {
                // 连接的数据库为非关系型数据库,或者系统无法通过连接的url获取类名
                DataTypeEnum dataTypeEnum = DataTypeEnum.urlOf(url);
                if (null == dataTypeEnum) {
                    throw new RuntimeException("Not supported data type: jdbcUrl=" + url);
                }
                driverClassname = dataTypeEnum.getDriver();
            }
            // 设置驱动类名
            dataSource.setDriverClassName(driverClassname);
            dataSource.setJdbcUrl(url);
            dataSource.setUsername(username);
            dataSource.setPassword(password);
            // 配置获取连接等待超时的时间
            dataSource.setConnectionTimeout(3000);
            // 配置初始化大小、最小、最大
            dataSource.setMaximumPoolSize(10);
            // 配置一旦重试多次失败后等待多久再继续重试连接，单位是毫秒
            dataSource.setConnectionTimeout(18000);
            // 这个特性能解决 MySQL 服务器8小时关闭连接的问题
            dataSource.setIdleTimeout(25200000);
            map.put(key, dataSource);
            return dataSource;
        } else {
            return map.get(key);
        }
    }

    /**
     * 获取数据库连接
     *
     * @param url
     * @param username
     * @param password
     * @return
     */
    public Connection getConnection(String url, String username, String password) {
        DataSource dataSource = SqlUtils.getDataSource(url, username, password);
        Connection connection = null;
        try {
            connection = dataSource.getConnection();
        } catch (SQLException e) {
            log.info("获取连接失败，即将尝试重新获取");
        }
        try {
            if (connection == null || connection.isClosed() || !connection.isValid(5)){
                log.info("connection is closed or invalid, retry get connection!");
                connection = dataSource.getConnection();
            }
        }catch (SQLException e){
            log.error("create connection error, jdbcUrl: {}", url);
            throw new RuntimeException("create connection error, jdbcUrl: " + url);
        }
        return connection;
    }

    /**
     * 释放连接
     * @param connection
     */
    private void releaseConnection(Connection connection) {
        if (connection != null) {
            try {
                connection.close();
            } catch (Exception e) {
                log.error(e.getMessage(),e);
                log.error("connection close error：" + e.getMessage());
            }
        }
    }

    /**
     * 测试连接
     * @param jdbcUrl
     * @param userName
     * @param password
     * @return
     */
    public boolean testConnection(String jdbcUrl, String userName, String password) {
        Connection connection = null;
        try {
            connection = getConnection(jdbcUrl, userName, password);
            if (null != connection) {
                return true;
            }
        } catch (Exception e) {
            log.info("Get connection failed:" + e.getMessage());
        } finally {
            releaseConnection(connection);
        }
        return false;
    }


    /**
     * 批量执行sql
     * @param jdbcUrl 连接地址
     * @param userName 用户名
     * @param password 密码
     * @param sqlFile sql文件
     * @return
     */
    public String executeFile(String jdbcUrl, String userName, String password, File sqlFile) {
        Connection connection = getConnection(jdbcUrl, userName, password);
        try {
            batchExecute(connection, readSqlList(sqlFile));
        } catch (Exception e) {
            log.error("sql脚本执行发生异常:{}",e.getMessage());
            return e.getMessage();
        }finally {
            releaseConnection(connection);
        }
        return "success";
    }

    /**
     * 读取sql文件,以";"为单位读取
     * @param sqlFile
     * @return
     */
    public List<String> readSqlList(File sqlFile){
        List<String> sql = Lists.newArrayList();
        StringBuilder sb = new StringBuilder();
        try(BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(sqlFile), StandardCharsets.UTF_8))) {
            String temp;
            while ((temp = reader.readLine()) != null){
                log.info("读取文件的内容为:{}",temp);
                if (temp.endsWith(";")){
                    // 这一行刚好为一条sql
                    sb.append(temp);
                    sql.add(sb.toString());
                    // 清空
                    sb.delete(0,temp.length());
                }else {
                    // 一次读取没能读取完
                    sb.append(temp);
                }
            }
            if (!"".endsWith(sb.toString())){
                // 添加最后一条sql
                sql.add(sb.toString());
            }
        }catch (IOException e){
            log.warn("读取文件失败");
        }
        return sql;
    }

    /**
     * 批量执行sql
     * @param sqls
     */
    @SneakyThrows
    public void batchExecute(Connection connection , List<String> sqls){
        // 获取执行对象
        Statement statement = connection.createStatement();
        sqls.forEach(sql->{
            if (sql.endsWith(";")){
                sql = sql.substring(0, sql.length() - 1);
            }
            statement.addBatch(sql);
        });
        statement.executeBatch();
    }

    public void closeResult(ResultSet rs) {
        if (rs != null) {
            try {
                rs.close();
            } catch (Exception e) {
                log.error(e.getMessage(),e);
            }
        }
    }

}
