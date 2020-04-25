package Dao.junit;

import Dao.CustomerDaoImpl;
import bean.Customer;
import util.JDBCUtils;

import java.sql.Connection;
import java.sql.Date;
import java.util.List;

/**
 * @authtor liFei
 * @date 2020/4/25-9:49
 */
public class CustomerDAOImplText {
    public static void main(String[] args) throws Exception {
        CustomerDaoImpl dao=new CustomerDaoImpl();
        Connection connection = JDBCUtils.getConnection();
//        Customer customer = new Customer(1, "天绝", "tianjue@126.com", new Date(13414411L));1
//         dao.insert(connection,customer);1
//        dao.deleteById(connection,13);2
//        Customer customer=new Customer(18,"黑明","heiming@126.com",new Date(134314254L));3
//       dao.update(connection,customer);3
//       Customer cust=dao.getCustomerById(connection,6);4
//         System.out.println(cust);4
//        List<Customer> all = dao.getAll(connection);5
//        all.forEach(System.out::println);5
//        long count=dao.getCount(connection);6
//        System.out.println("表中数据个数"+count);6
//        Date maxBirth=dao.getMaxBirth(connection);7
//        System.out.println("表中最大生日为"+maxBirth);7
        JDBCUtils.closeResource(connection,null);
    }
}
