package preparedstatement;

import util.JDBCUtils;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

/**
 * @authtor liFei
 * @date 2020/4/19-13:08
 */
/*
使用 PreparedStatement，实现对数据表的增删改查操作
 */
public class PreparedStatementUpdateTest {
    public static void main(String[] args) throws Exception {
      String sql="delete from customers where id=?";
      update(sql,3);
    }
    //向customers表中添加一条数据
    public static void testInsert() throws Exception{
            //1：读取配置文件中的4个基本信息
            InputStream is= PreparedStatementUpdateTest.class.getClassLoader().getResourceAsStream("jdbc.properties");
            Properties properties = new Properties();
            properties.load(is);
            String url=properties.getProperty("url");
            String user=properties.getProperty("user");
            String password=properties.getProperty("password");
            String driverClass=properties.getProperty("driverClass");
            //2：加载Driver
            Class.forName(driverClass);
            Connection connect= DriverManager.getConnection(url, user, password);
            //System.out.println(connect);
         //3:预编译sql语句，返回PrepareStatment的实例
         String sql="insert into customers(name,email,birth)values(?,?,?)";
        PreparedStatement ps=connect.prepareStatement(sql);
         //4：填充占位符
        ps.setString(1,"哪吒");
        ps.setString(2,"nezha@gmail.com");
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
        Date date=sdf.parse("1000-01-01");
        ps.setDate(3, new java.sql.Date(date.getTime()));
        //5:执行操作
        ps.execute();
        //6:资源的关闭
        ps.close();
        connect.close();
        }
        //修改customers表中一条数据
    public static void testUpdate()  {
        //1:获取数据库的连接
        Connection connection=null;
        PreparedStatement preparedStatement=null;
        try {
            connection = JDBCUtils.getConnection();
            //2：预编译sql语句，返回PreparedStatement的实例
            String sql = "update customers set name = ? where id = ?";
          preparedStatement = connection.prepareStatement(sql);
            //3：填充占位符
            preparedStatement.setObject(1, "莫扎特");
            preparedStatement.setObject(2, 18);
            //4：执行
            preparedStatement.execute();
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            //5：资源的关闭
            JDBCUtils.closeResource(connection, preparedStatement);
        }
    }
    //通用的增删改操作
    public static void update(String sql,Object ...args){
        //sql当中占位符的个数与可变形参的长度
        //1:获取数据库的连接
        Connection connection=null;
        PreparedStatement preparedStatement=null;
        try {
            connection = JDBCUtils.getConnection();
            //2：预编译sql语句，返回PreparedStatement的实例
            preparedStatement = connection.prepareStatement(sql);
            //3：填充占位符
            for(int i=0;i<args.length;i++){
                preparedStatement.setObject(i+1,args[i]);//小心参数声明错误
            }
            //4：执行
            preparedStatement.execute();
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            //5：资源的关闭
            JDBCUtils.closeResource(connection, preparedStatement);
        }
    }

    }

