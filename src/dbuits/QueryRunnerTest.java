package dbuits;

import bean.Customer;
import connection2.util.JDBCUitls;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import util.JDBCUtils;

import java.sql.Connection;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @authtor liFei
 * @date 2020/4/27-7:09
 */
public class QueryRunnerTest {
    public static void main(String[] args) throws Exception {
//        //测试插入
//        Connection connection3=null;
//        try {
//            QueryRunner queryRunner = new QueryRunner();
//            connection3 = JDBCUitls.getConnection3();
//            String sql = "insert into customers(name,email,birth)values(?,?,?)";
//            int insertCount = queryRunner.update(connection3, sql, "风尘", "fengchen@126.com", "1997-09-08");
//            System.out.println("添加了" + insertCount + "条记录");
//        }catch (Exception e){
//            e.printStackTrace();
//        }finally {
//            JDBCUtils.closeResource(connection3, null);
//        }
//    }
    //测试查询
        /*
        beanHandler:是ResultSetHandler接口的实现类，用于封装表中的一条记录
         */
//    Connection connection3=null;
//        try {
//        QueryRunner queryRunner = new QueryRunner();
//        connection3 = JDBCUitls.getConnection3();
//        String sql = "select id,name,email,birth from customers where id=?";
//        BeanHandler<Customer> handler=new BeanHandler<>(Customer.class);
//            Customer query = queryRunner.query(connection3, sql, handler, 7);
//            System.out.println(query);
//    }catch (Exception e){
//        e.printStackTrace();
//    }finally {
//        JDBCUtils.closeResource(connection3, null);
//    }
        /*
          beanListHandler:是ResultSetHandler接口的实现类，用于封装表中的多条记录
         */
//        Connection connection3=null;
//        try {
//            QueryRunner queryRunner = new QueryRunner();
//            connection3 = JDBCUitls.getConnection3();
//            String sql = "select id,name,email,birth from customers where id<?";
//            BeanListHandler<Customer> handler=new BeanListHandler<>(Customer.class);
//            List<Customer> query = queryRunner.query(connection3, sql, handler, 7);
//            query.forEach(System.out::println);
//        }catch (Exception e){
//            e.printStackTrace();
//        }finally {
//            JDBCUtils.closeResource(connection3, null);
//        }
           /*
          MapHandler:是ResultSetHandler接口的实现类，对应表中的一条记录
         */
//        Connection connection3=null;
//        try {
//            QueryRunner queryRunner = new QueryRunner();
//            connection3 = JDBCUitls.getConnection3();
//            String sql = "select id,name,email,birth from customers where id=?";
//            MapHandler handler=new MapHandler();
//            Map<String, Object> query = queryRunner.query(connection3, sql, handler, 7);
//            System.out.println(query);
//        }catch (Exception e){
//            e.printStackTrace();
//        }finally {
//            JDBCUtils.closeResource(connection3, null);
//        }
         /*
          MapListedHandler:是ResultSetHandler接口的实现类，对应表中的多条记录
         */
//        Connection connection3=null;
//        try {
//            QueryRunner queryRunner = new QueryRunner();
//            connection3 = JDBCUitls.getConnection3();
//            String sql = "select id,name,email,birth from customers where id<?";
//            MapListHandler handler=new MapListHandler();
//            List<Map<String, Object>> query = queryRunner.query(connection3, sql, handler, 7);
//              query.forEach(System.out::println);
//        }catch (Exception e){
//            e.printStackTrace();
//        }finally {
//            JDBCUtils.closeResource(connection3, null);
//        }
        /*
        ScalarHandler用于查询特殊值
         */
//        Connection connection3=null;
//        try {
//            QueryRunner queryRunner = new QueryRunner();
//            connection3 = JDBCUitls.getConnection3();
//            String sql = "select count(*) from customers";
//            ScalarHandler scalarHandler = new ScalarHandler();
//            Long count=(long)queryRunner.query(connection3, sql, scalarHandler);
//            System.out.println(count);
//        }catch (Exception e){
//            e.printStackTrace();
//        }finally {
//            JDBCUtils.closeResource(connection3, null);
//        }
        /*
       自定义ResultSetHandler的实现类
         */
        Connection connection3=null;
        try {
            QueryRunner queryRunner = new QueryRunner();
            connection3 = JDBCUitls.getConnection3();
            String sql = "select id,name,email,birth from customers where id=?";
            ResultSetHandler<Customer> handler=new ResultSetHandler<Customer>() {
                @Override
                public Customer handle(ResultSet resultSet) throws SQLException {
                  if(resultSet.next()){
                      int id=resultSet.getInt("id");
                      String name=resultSet.getString("name");
                      String email = resultSet.getString("email");
                      Date birth = resultSet.getDate("birth");
                      Customer customer = new Customer(id, name, email, birth);
                      return customer;
                  }
                  return null;
                }
            };
            Customer query = queryRunner.query(connection3, sql, handler,7);
            System.out.println(query);

        }catch (Exception e){
            e.printStackTrace();
        }finally {
            JDBCUtils.closeResource(connection3, null);
        }
}
}
