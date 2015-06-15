package com.hewei.spider.jdbc;

import com.alibaba.druid.pool.DruidDataSource;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.concurrent.TimeUnit;

/**
 * 
 * @author hewei
 * 
 * @date 2015/6/15  13:32
 *
 * @version 5.0
 *
 * @desc 
 *
 */
public class DataSourceUtils {

    private static final String driverClassName = "com.mysql.jdbc.Driver";

    private static final String url = "jdbc:mysql://172.18.2.37/channel?useUnicode=true&characterEncoding=utf8&autoReconnect=true&allowMultiQueries=true&noAccessToProcedureBodies=true";

    private static final String username = "development";

    private static final String password = "development";

    private static final int initialSize = 1;

    private static final int minIdle = 1;

    private static final int maxActive = 30;

    private static final int maxWait = 60000;

    public static class HEWEI {

        private static final DataSource ds = DataSourceUtils.buildDataSource();
    }

    public static DataSource getInstance() {
        return HEWEI.ds;
    }

    private static DataSource buildDataSource() {
        DruidDataSource ds = new DruidDataSource();
        ds.setDriverClassName(driverClassName);
        ds.setUrl(url);
        ds.setUsername(username);
        ds.setPassword(password);
        ds.setInitialSize(initialSize);
        ds.setMinIdle(minIdle);
        ds.setMaxActive(maxActive);
        ds.setMaxWait(maxWait);
        return ds;
    }

    public static Connection getConnection() {
        try {
            return getInstance().getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void main(String[] args) {
//        createTable();
//        for(int i=0;i<1;i++){
//            insertData(new Star("hha"+i,"hha"+i));
//        }

        long s = System.nanoTime();
        long s2 = s + TimeUnit.NANOSECONDS.convert(1500, TimeUnit.MILLISECONDS);

        System.out.println(s+"--"+s2);

    }

    public static void createTable(){
        try {
            try (Connection c = getConnection(); Statement statement = c.createStatement()) {
                String sql = "CREATE TABLE If Not Exists `z_star` (\n" +
                        "  `id` bigint(20) NOT NULL AUTO_INCREMENT,\n" +
                        "  `name` varchar(100) NOT NULL,\n" +
                        "  `url` varchar(255) NOT NULL,\n" +
                        "  PRIMARY KEY (`id`)\n" +
                        ") ENGINE=InnoDB DEFAULT CHARSET=utf8;";
                statement.execute(sql);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void insertData(Star star) {
        String sql = "insert into z_star (name,url) values (" + "\"" + star.getName() + "\"," + "\"" + star.getUrl() + "\"" + ");";
        try {
            try (Connection c = getConnection(); Statement statement = c.createStatement()) {
                statement.execute(sql);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
