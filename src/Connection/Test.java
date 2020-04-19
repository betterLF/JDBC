package Connection;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Properties;

/**
 * @authtor liFei
 * @date 2020/4/19-9:16
 */
public class Test {
    public static void main(String[] args) throws Exception {
        //testConnection1();
        //testConnection2();
        //testConnection3();
       // testConnection4();
        testConnection5();
    }
    //方式一：
    public static void testConnection1() throws SQLException {
        //获取Driver的实现类对象
        Driver driver=new com.mysql.jdbc.Driver();
        //jdbc:mysql: 协议
        //localhost:ip地址
        //3306：默认mysql的端口号
        //test：test数据库
        String url="jdbc:mysql://localhost:3306/test";
        Properties info=new Properties();
        //将用户名和密码封装在Properties中
        info.setProperty("user","root");
        info.setProperty("password","lf20000910");
        Connection connect1 = driver.connect(url, info);
        System.out.println(connect1);
    }
    //方式二：对方式一的迭代:在如下的程序中不出现第三方的api，使得程序具有更好的可移植性
    public static void testConnection2() throws Exception {
        //获取Driver的实现类对象，使用反射
        Class clazz=Class.forName("com.mysql.jdbc.Driver");
        Driver driver=(Driver)clazz.newInstance();
        //jdbc:mysql: 协议
        //localhost:ip地址
        //3306：默认mysql的端口号
        //test：test数据库
        String url="jdbc:mysql://localhost:3306/test";
        Properties info=new Properties();
        //将用户名和密码封装在Properties中
        info.setProperty("user","root");
        info.setProperty("password","lf20000910");
        Connection connect2 = driver.connect(url, info);
        System.out.println(connect2);
    }
    //方式三：使用DriverManger替换Driver
    public static void testConnection3() throws Exception {
        //1：获取Driver的实现类对象，使用反射
        Class clazz=Class.forName("com.mysql.jdbc.Driver");
        Driver driver=(Driver)clazz.newInstance();
        //2：提供另外三个连接的基本信息
        String url="jdbc:mysql://localhost:3306/test";
        String user="root";
        String password="lf20000910";
        //注册驱动
        DriverManager.registerDriver(driver);
        //获取连接
        Connection connect3 = DriverManager.getConnection(url, user, password);
        System.out.println(connect3);
    }
    //方式四：将数据库连接需要的4个基本信息声明在配置文件中，通过读配置文件的方式获取连接
    public static void testConnection4() throws Exception {
        //1：提供另外三个连接的基本信息
        String url="jdbc:mysql://localhost:3306/test";
        String user="root";
        String passworld="lf20000910";
        //2：获取Driver的实现类对象，使用反射
        Class.forName("com.mysql.jdbc.Driver");
//        Class clazz=Class.forName("com.mysql.jdbc.Driver");
//        Driver driver=(Driver)clazz.newInstance();
//        //注册驱动
//        DriverManager.registerDriver(driver);
        //获取连接
        Connection connect4= DriverManager.getConnection(url, user, passworld);
        System.out.println(connect4);
    }
    //方式五(final版)：
    // 1：实现了数据和代码的分离，实现了解耦
    //2：如果需要修改配置文件信息，可以避免程序重新打包
    public static void testConnection5() throws Exception {
        //1：读取配置文件中的4个基本信息
        InputStream is= Test.class.getClassLoader().getResourceAsStream("jdbc.properties");
        Properties properties = new Properties();
        properties.load(is);
        String url=properties.getProperty("url");
        String user=properties.getProperty("user");
        String password=properties.getProperty("password");
        String driverClass=properties.getProperty("driverClass");
        //2：加载Driver
        Class.forName(driverClass);
        Connection connect5= DriverManager.getConnection(url, user, password);
        System.out.println(connect5);
    }
}
