package Dao;

import util.JDBCUtils;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.List;

/**
 * @authtor liFei
 * @date 2020/4/24-18:50
 */
/*
封装了针对于数据表的通用操作
 */
public abstract class BaseDAO {
    //用于查询特殊值的操作
    public <E>E getValue(Connection connection,String sql,Object...args){
        PreparedStatement preparedStatement=null;
        ResultSet resultSet=null;
        try {
            preparedStatement = connection.prepareStatement(sql);
            for(int i=0;i<args.length;i++){
                preparedStatement.setObject(i+1,args[i]);
            }
           resultSet = preparedStatement.executeQuery();
            if(resultSet.next()){
               return (E)resultSet.getObject(1);
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            //4：资源的关闭
            JDBCUtils.closeResource(null, preparedStatement,resultSet);
        }
        return null;
    }
    //通用的增删改操作--version2.0(考虑上事务)
    public static int update2(Connection connection, String sql, Object ...args){
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
    //使用PreparedStatement来实现针对不同表的通用的查询操作，返回表中的单条记录
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
    //使用PreparedStatement来实现针对不同表的通用的查询操作，返回表中的多条记录
    public <T> List<T> getForList(Connection connection,Class<T> clazz, String sql, Object...args){
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
            //创建集合对象
            ArrayList<T> ts = new ArrayList<T>();
            while (resultSet.next()){//判断结果集下一条是否有数据，如果有数据返回true，并指针下移，否则返回false
                T t = clazz.newInstance();
                //处理结果集一行数据中的每一个列,给t对象指定的属性赋值
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
                ts.add(t);
            }
            return ts;
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            //5：资源的关闭
            JDBCUtils.closeResource(null, preparedStatement,resultSet);
        }
        return null;
    }
}
