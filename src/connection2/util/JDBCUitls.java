package connection2.util;

import com.alibaba.druid.pool.DruidDataSourceFactory;
import com.mchange.v2.c3p0.ComboPooledDataSource;
import org.apache.commons.dbcp.BasicDataSourceFactory;

import javax.sql.DataSource;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

/**
 * @authtor liFei
 * @date 2020/4/26-11:59
 */
//使用csp0的连接技术
public class JDBCUitls {
    private static ComboPooledDataSource cpds = new ComboPooledDataSource("hellc3p0");

    public static Connection getConnection1() throws SQLException {
        Connection connection = cpds.getConnection();
        return connection;
    }
//dbcp
    private static DataSource source;
    static {
        try {
            Properties pros = new Properties();
            //方式一：
            // InputStream is = ClassLoader.getSystemClassLoader().getResourceAsStream("dbcp.properties");
            //方式二：
            FileInputStream is = new FileInputStream(new File("D:\\Practice\\JDBC\\src\\dbcp.properties"));
            pros.load(is);
            //创建dbcp数据库连接池
            source = BasicDataSourceFactory.createDataSource(pros);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public static Connection getConnection2() throws Exception{
        Properties pros = new Properties();
        //方式一：
        // InputStream is = ClassLoader.getSystemClassLoader().getResourceAsStream("dbcp.properties");
        //方式二：
        FileInputStream is=new FileInputStream(new File("D:\\Practice\\JDBC\\src\\dbcp.properties"));
        pros.load(is);
        //创建dbcp数据库连接池
        DataSource source = BasicDataSourceFactory.createDataSource(pros);
        Connection connection=source.getConnection();
           return connection;
    }
    //druid
    private static DataSource dataSource;
    static {
        try {
            Properties pros = new Properties();
            InputStream is = ClassLoader.getSystemClassLoader().getResourceAsStream("druid.properties");
            pros.load(is);
            dataSource = DruidDataSourceFactory.createDataSource(pros);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public static Connection getConnection3() throws Exception {
        Connection connection=dataSource.getConnection();
        return connection;
    }

}
