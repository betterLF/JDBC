package DAO2;

import bean.Customer;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.sql.Connection;
import java.sql.Date;
import java.util.List;

/**
 * @authtor liFei
 * @date 2020/4/25-9:32
 */
public class CustomerDaoImpl extends BaseDAO<Customer>implements CustomerDao {
    @Override
    public void insert(Connection connection, Customer customer) {
        String sql="insert into customers(name,email,birth)values(?,?,?)";
        update2(connection,sql,customer.getName(),customer.getEmail(),customer.getBirth());
    }

    @Override
    public void deleteById(Connection connection, int id) {
        String sql="delete from customers where id=?";
            update2(connection,sql,id);
    }

    @Override
    public void update(Connection connection, Customer customer) {
             String sql="update customers set name=?,email=?,birth=? where id=?";
             update2(connection,sql,customer.getName(),customer.getEmail(),customer.getBirth(),customer.getId());
    }

    @Override
    public Customer getCustomerById(Connection connection, int id) {
        String sql="select id,name,email,birth from customers where id=?";
        Customer instance = getInstance(connection, sql, id);
        return instance;
    }

    @Override
    public List<Customer> getAll(Connection connection) {
        String sql="select id,name,email,birth from customers";
        List<Customer> forList = getForList(connection, sql);
        return forList;
    }

    @Override
    public Long getCount(Connection connection) {
        String sql="select count(*) from customers";
        return getValue(connection, sql);
    }

    @Override
    public Date getMaxBirth(Connection connection) {
        String sql="select max(birth) from customers";
        return getValue(connection,sql);
    }
}
