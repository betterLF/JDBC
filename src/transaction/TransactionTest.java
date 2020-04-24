package transaction;

import org.junit.Test;
import util.JDBCUtils;

import java.lang.reflect.Field;
import java.sql.*;

import static java.sql.Connection.TRANSACTION_READ_UNCOMMITTED;

/**
 * @authtor liFei
 * @date 2020/4/23-9:36
 */

/*
1：什么叫数据库事务？
事务：一组逻辑操作单元，使数据从一种状态变换到另外一种状态
>一种逻辑操作单元，一个或多个DML操作
2：事务处理的原则：保证所有事务都作为一个工作单元来执行，即
使出现了故障，都不能改变这种执行方式。当在一个事务中执行多个操
作时，要么所有的事务都被提交(commit)，那么这些修改就永久地保存
下来；要么数据库管理系统将放弃所作的所有修改，整个事务回滚
(rollback)到最初状态
3:数据一旦提交，就不可回滚
4：哪些操作会导致数据的自动提交？
>DDL操作一旦执行，都会自动提交
>DML默认情况下，一旦执行，就会自动提交
我们可以通过set autocommit=false的方式取消DML操作的自动提交
>默认在关闭连接时，会自动的提交数据
 */
/*
===================未考虑数据库事务的操作================
针对于数据表user——table来说,
AA用户给BB用户转账100
update user_table set balance=balance-100 where user='AA';
update user_table set balance=balance+100 where user='BB';
 */
public class TransactionTest {
    public static void main(String[] args) throws Exception {
//       String sql1="update user_table set balance=balance-100 where user=?";
//       update1(sql1,"AA");
//       //模拟网络异常
//        System.out.println(10/0);
//       String sq12="update user_table set balance=balance+100 where user=?";
//       update1(sq12,"BB");
//        System.out.println("转账成功");
        Connection connection = JDBCUtils.getConnection();
        //取消数据的自动提交功能
        connection.setAutoCommit(false);
        try {
            String sql1 = "update user_table set balance=balance-100 where user=?";
            update2(connection, sql1, "AA");
            //模拟网络异常
            System.out.println(10/0);
            String sq12 = "update user_table set balance=balance+100 where user=?";
            update2(connection, sq12, "BB");
            System.out.println("转账成功");
            //提交数据
            connection.commit();
        } catch (Exception e) {
            e.printStackTrace();
            //回滚数据
            try {
                //修改其为自动提交数据
                //主要针对于使用数据库连接池的使用
                connection.setAutoCommit(true);
                connection.rollback();
            }catch (SQLException e1){
                e1.printStackTrace();
            }
        } finally {
            JDBCUtils.closeResource(connection, null);
        }
    }
    //通用的增删改操作--version1.0
    public static int update1(String sql,Object ...args){
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
           return preparedStatement.executeUpdate();
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            //5：资源的关闭
            JDBCUtils.closeResource(connection, preparedStatement);
        }
        return 0;
    }
    //=================考虑数据库事务的转账操作================
    //通用的增删改操作--version2.0(考虑上事务)
    public static int update2(Connection connection,String sql,Object ...args){
        //sql当中占位符的个数与可变形参的长度
        PreparedStatement preparedStatement=null;
        try {
            //1：预编译sql语句，返回PreparedStatement的实例
            preparedStatement = connection.prepareStatement(sql);
            //2：填充占位符
            for(int i=0;i<args.length;i++){
                preparedStatement.setObject(i+1,args[i]);//小心参数声明错误
            }
            //3：执行
            return preparedStatement.executeUpdate();
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            //4：资源的关闭
            JDBCUtils.closeResource(null, preparedStatement);
        }
        return 0;
    }
    //=========================================
    //通用的查询操作，用于返回数据表中的一条记录(version 2.0,考虑上事务)
    @Test
    public void test1()throws Exception{
        Connection connection=JDBCUtils.getConnection();
         connection.setTransactionIsolation(TRANSACTION_READ_UNCOMMITTED);
        String sql="select user,password,balance from user_table where user=?";
                User user=getInstance(connection,User.class,sql,"CC");
        System.out.println(user);
    }
    @Test
    public void test2()throws Exception{
        Connection connection=JDBCUtils.getConnection();
        String sql="update user_table set balance=? where user=?";
        update2(connection,sql,5000,"CC");
        Thread.sleep(12000);

    }
    public <T> T getInstance(Connection connection,Class<T> clazz,String sql,Object...args){
        //sql当中占位符的个数与可变形参的长度
        //1:获取数据库的连接
        PreparedStatement preparedStatement=null;
        ResultSet resultSet=null;
        try {
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
                T t = clazz.getDeclaredConstructor().newInstance();
                //处理结果集一行数据中的每一个列
                for (int i = 0; i <columnCount ; i++) {
                    //获取列值
                    Object columnValue= resultSet.getObject(i+1);
                    //获取每个列的列名
                    String columnLabel=resultSetMetaData.getColumnLabel(i+1);
                    //给指定对象指定的columnName属性，赋值为columnValue,通过反射
                    Field declaredField = clazz.getDeclaredField(columnLabel);
                    declaredField.setAccessible(true);
                    declaredField.set(t,columnValue);
                }
                return t ;
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            //5：资源的关闭
            JDBCUtils.closeResource(null, preparedStatement,resultSet);
        }
        return null;
    }
}
