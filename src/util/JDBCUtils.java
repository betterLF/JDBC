package util;

import preparedstatement.PreparedStatementUpdateTest;

import java.io.InputStream;
import java.sql.*;
import java.util.Properties;

/**
 * @authtor liFei
 * @date 2020/4/19-13:29
 */
/*
操作数据库的工具类
 */
public class JDBCUtils {
    //获取数据库的连接
    public static Connection getConnection() throws Exception{
        InputStream is=  ClassLoader.getSystemClassLoader().getResourceAsStream("jdbc.properties");
        Properties properties = new Properties();
        properties.load(is);
        String url=properties.getProperty("url");
        String user=properties.getProperty("user");
        String password=properties.getProperty("password");
        String driverClass=properties.getProperty("driverClass");
        //2：加载Driver
        Class.forName(driverClass);
        Connection connect= DriverManager.getConnection(url, user, password);
        return connect;
    }
    //关闭连接和Statement的操作
    public static void closeResource(Connection connect, Statement ps){
        try {
            if(ps!=null)
                ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try{
            if(connect!=null){
                connect.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    //关闭连接和Statement的操作
    public static void closeResource(Connection connect, Statement ps, ResultSet rs){
        try {
            if(ps!=null)
                ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try{
            if(connect!=null){
                connect.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try{
            if(rs!=null){
                rs.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
