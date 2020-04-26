package connection2;

import com.alibaba.druid.pool.DruidDataSourceFactory;

import javax.sql.DataSource;
import java.io.InputStream;
import java.sql.Connection;
import java.util.Properties;

/**
 * @authtor liFei
 * @date 2020/4/26-12:38
 */
public class DruidTest {
    public static void main(String[] args) throws Exception {
        Properties pros=new Properties();
        InputStream is= ClassLoader.getSystemClassLoader().getResourceAsStream("druid.properties");
        pros.load(is);
        DataSource dataSource = DruidDataSourceFactory.createDataSource(pros);
        Connection connection=dataSource.getConnection();
        System.out.println(connection);
    }
}
