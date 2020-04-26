package connection2;

import com.mchange.v2.c3p0.ComboPooledDataSource;

import java.sql.Connection;

/**
 * @authtor liFei
 * @date 2020/4/26-9:40
 */
public class C3P0Test {
    public static void main(String[] args) throws Exception {
//        //方式一
//        //获取csp0数据库连接池
//        ComboPooledDataSource cpds = new ComboPooledDataSource();
//        cpds.setDriverClass("com.mysql.jdbc.Driver");
//        //加载jdbc驱动程序
//        cpds.setJdbcUrl("jdbc：mysql://localhost:3306/test");
//         cpds.setUser("root");
//        cpds.setPassword("lf20000910");
//        //通过设置相关的参数对数据库连接池进行管理
//        //
//        //设置初始化时数据库连接池中的连接数
//        cpds.setInitialPoolSize(10);
//        Connection connection = cpds.getConnection();
//        System.out.println(connection);
//        //销毁c3p0连接池
//        DataSources.destroy(cpds);
        //方式二：使用配置文件
        ComboPooledDataSource cpds = new ComboPooledDataSource("hellc3p0");
        Connection connection = cpds.getConnection();
        System.out.println(connection);

    }
}
