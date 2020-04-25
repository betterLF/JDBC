package Dao;

import bean.Customer;

import java.sql.Connection;
import java.sql.Date;
import java.util.Comparator;
import java.util.List;

/**
 * @authtor liFei
 * @date 2020/4/25-9:23
 */
//此接口用于规范针对于customers表的常用操作
public interface CustomerDao {
    //将customer对象添加到数据库中
    void insert(Connection connection, Customer customer);
    //针对指定的id，删除表中的一条数据
    void deleteById(Connection connection,int id);
    //针对内存中的customer对象，修改数据表中的记录
    void update(Connection connection,Customer customer);
    //针对指定的id查询得到对应的Customer对象
    Customer getCustomerById(Connection connection,int id);
    //查询表中所有记录构成的集合
    List<Customer> getAll(Connection connection);
    //返回数据表中数据的条目数
    Long getCount(Connection connection);
    //返回数据表中最大的生日
    Date getMaxBirth(Connection connection);
}
