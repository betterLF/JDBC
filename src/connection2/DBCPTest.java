package connection2;

import org.apache.commons.dbcp.BasicDataSourceFactory;

import javax.sql.DataSource;
import java.io.File;
import java.io.FileInputStream;
import java.sql.Connection;
import java.util.Properties;

/**
 * @authtor liFei
 * @date 2020/4/26-12:07
 */
public class DBCPTest {
    public static void main(String[] args) throws Exception {
//        //方式一：
//        //创建dbcp的数据库连接池
//        BasicDataSource source=new BasicDataSource();
//        //设置基本信息
//        source.setDriverClassName("com.mysql.jdbc.Driver");
//        source.setUrl("jdbc：mysql:///test");
//         source.setUsername("root");
//        source.setPassword("lf20000910");
//        //设置涉及数据库连接值管理的属性
//        source.setInitialSize(10);
//        source.setMaxActive(10);
//        //-------
//        Connection connection = source.getConnection();
//        System.out.println(connection);
         //方式二：使用配置文件
        Properties pros = new Properties();
        //方式一：
       // InputStream is = ClassLoader.getSystemClassLoader().getResourceAsStream("dbcp.properties");
        //方式二：
        FileInputStream is=new FileInputStream(new File("D:\\Practice\\JDBC\\src\\dbcp.properties"));
        pros.load(is);
        DataSource source = BasicDataSourceFactory.createDataSource(pros);
        Connection connection=source.getConnection();
        System.out.println(connection);
    }
}
