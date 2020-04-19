package preparedstatement;

import bean.Customer;
import util.JDBCUtils;

import java.lang.reflect.Field;
import java.sql.*;

/**
 * @authtor liFei
 * @date 2020/4/19-15:44
 */
public class CustomerForQuery {
    /*
    针对于customer表的查询操作
     */
    public static void main(String[] args) {
        String sql="select id,name,email,birth from customers where id =?";
        Customer customer = selectForCustomers(sql, 13);
        System.out.println(customer.toString());
        String sql2="select name,email from customers where name =?";
        Customer customer2 = selectForCustomers(sql2, "周杰伦");
        System.out.println(customer2.toString());
    }
    public static Customer selectForCustomers(String sql,Object...args){

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
                //4:处理结果集
                //获取当前这条数据的各个字段值
                if(resultSet.next()){//判断结果集下一条是否有数据，如果有数据返回true，并指针下移，否则返回false
                    Customer customer=new Customer();
                    //处理结果集一行数据中的每一个列
                    for (int i = 0; i <columnCount ; i++) {
                        //获取列值
                       Object columnValue= resultSet.getObject(i+1);
                       //获取每个列的列名
                      String columnLabel=resultSetMetaData.getColumnLabel(i+1);
                        //给customer对象指定的columnName属性，赋值为columnValue,通过反射
                        Field declaredField = Customer.class.getDeclaredField(columnLabel);
                        declaredField.setAccessible(true);
                        declaredField.set(customer,columnValue);
                    }
                  return customer;
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
            String sql="select id,name,email,birth from customers where id =?";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setObject(1,1);
            //3：执行并返回结果集
           resultSet = preparedStatement.executeQuery();
            //4:处理结果集
            if(resultSet.next()){//判断结果集下一条是否有数据，如果有数据返回true，并指针下移，否则返回false
                //获取当前这条数据的各个字段值
                int id=resultSet.getInt(1);
                String name=resultSet.getString(2);
                String email=resultSet.getString(3);
                Date birth=resultSet.getDate(4);
                //方式一：
             //   System.out.println("id= "+id+",name= "+name+",email= "+email+",birth= "+birth);
                //方式二：
               // Object []data=new Object[]{id,name,email,birth};
                //方式三：将数据封装成一个对象(推荐)
                 Customer customer=new Customer(id,name,email,birth);
                System.out.println(customer.toString());
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            //5：资源的关闭
            JDBCUtils.closeResource(connection, preparedStatement,resultSet);
        }
    }
}
