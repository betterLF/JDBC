package preparedstatement;

import bean.Customer;
import bean.Order;
import util.JDBCUtils;

import java.lang.reflect.Field;
import java.sql.*;

/**
 * @authtor liFei
 * @date 2020/4/19-16:37
 */
/*
针对于Order表的通用查询
针对于表的字段名与类的属性名不相同的情况
1：必须声明sql时，使用类的属性名来命名字段的别名
2：使用ResultSetMetaData时，需要使用getColumnLabel()来获取别名(列名)
 */
public class OrderForQuery {
    public static void main(String[] args) {
        String sql="select order_id orderId,order_name orderName,order_date orderDate from `order` where order_id = ?";
        Order order = selectForOrder(sql, 1);
        System.out.println(order.toString());
    }
    //针对于order表的通用查询操作
    public static Order selectForOrder(String sql,Object...args){
        //sql当中占位符的个数与可变形参的长度
        //1:获取数据库的连接
        Connection connection=null;
        PreparedStatement preparedStatement=null;
        ResultSet resultSet=null;
        try {
            connection = JDBCUtils.getConnection();
            //2：预编译sql语句，返回PreparedStatement的实例
            preparedStatement = connection.prepareStatement(sql);
            //3:填充占位符
            for(int i=0;i<args.length;i++){
                preparedStatement.setObject(i+1,args[i]);
            }
            //4：执行并返回结果集
            resultSet = preparedStatement.executeQuery();
            //获取结果集的元数据
            ResultSetMetaData resultSetMetaData =resultSet.getMetaData();
            //通过 resultSetMetaData获取结果集中的列数
            int columnCount=resultSetMetaData.getColumnCount();
            //5:处理结果集
            //获取当前这条数据的各个字段值
            if(resultSet.next()){//判断结果集下一条是否有数据，如果有数据返回true，并指针下移，否则返回false
               Order order=new Order();
                //处理结果集一行数据中的每一个列
                for (int i = 0; i <columnCount ; i++) {
                    //获取列值
                    Object columnValue= resultSet.getObject(i+1);
                    //获取每个列的列名 getColumnName（不推荐）
                    //获取每个列的别名 getColumnLabel（没其别名，就是表名）
                    String columnLabel=resultSetMetaData.getColumnLabel(i+1);
                    //给customer对象指定的columnName属性，赋值为columnValue,通过反射
                    Field declaredField = Order.class.getDeclaredField(columnLabel);
                    declaredField.setAccessible(true);
                    declaredField.set(order,columnValue);
                }
                return order;
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            //5：资源的关闭
            JDBCUtils.closeResource(connection, preparedStatement,resultSet);
        }
        return null;
    }
    public static void select(){
        //sql当中占位符的个数与可变形参的长度
        //1:获取数据库的连接
        Connection connection=null;
        PreparedStatement preparedStatement=null;
        ResultSet resultSet=null;
        try {
            connection = JDBCUtils.getConnection();
            //2：预编译sql语句，返回PreparedStatement的实例
            String sql="select order_id,order_name,order_date from `order` where order_id = ?";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setObject(1,1);
            //3：执行并返回结果集
            resultSet = preparedStatement.executeQuery();
            //4:处理结果集
            if(resultSet.next()){//判断结果集下一条是否有数据，如果有数据返回true，并指针下移，否则返回false
                //获取当前这条数据的各个字段值
                int id=(int)resultSet.getObject(1);
                String name=(String) resultSet.getObject(2);
                Date date= (Date) resultSet.getObject(3);
                Order order=new Order(id,name,date);
                System.out.println(order.toString());
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            //5：资源的关闭
            JDBCUtils.closeResource(connection, preparedStatement,resultSet);
        }
    }
}
