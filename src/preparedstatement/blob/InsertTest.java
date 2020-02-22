package preparedstatement.blob;

import util.JDBCUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;

/**
 * @authtor liFei
 * @date 2020/4/21-17:37
 */
//使用PreparedStatement实现批量数据的操作
    //update,delete本身就具有批量操作的效果
    //此时的批量操作主要指的是批量插入，使用PreparedStatement如何实现更高效的批量插入。
    /*
     题目：向goods表中插入20000条数据
    CREATE TABLE goods(
    id INT PRIMARY KEY AUTO_INCREMENT,
    NAME VARCHAR(25)
     );
     */
    /*
    方式一：
        Connection connection = JDBCUtils.getConnection();
         Statement statement = connection.createStatement();
         for(int i=1;i<=20000;i++){
         String sql='insert into goods(name)values('name_"+i+"')";
         st.execute(sql);
     */
public class InsertTest {
    public static void main(String[] args) {
                //test2();
                //test3();
                test4();
    }
    //批量插入的方式二：
    public static void test2()  {
              Connection connection=null;
              PreparedStatement preparedStatement=null;
        try {
            long start=System.currentTimeMillis();
           connection = JDBCUtils.getConnection();
            String sql = "insert into goods(name)values(?)";
           preparedStatement = connection.prepareStatement(sql);
            for (int i = 1; i <= 20000; i++) {
                preparedStatement.setObject(1, "name_" + i);
                preparedStatement.execute();
            }
            long end=System.currentTimeMillis();
            System.out.println("花费的时间为"+(end-start));//20000:83065ms
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            JDBCUtils.closeResource(connection,preparedStatement);
        }
    }
    /*
    批量插入的方式三：
    1.addBatch(),executeBatch(),clearBatch()
    2.mysql服务器默认是关闭批处理的，我们需要通过一个参数，让mysql去开启批处理的支持
    ？rewriteBatchedStatements=true 写在配置文件的url后面
    3.使用更新后的mysql驱动：mysql-connector-java-5.1.37_bin.jar
     */
    public static void test3()  {
        Connection connection=null;
        PreparedStatement preparedStatement=null;
        try {
            long start=System.currentTimeMillis();
            connection = JDBCUtils.getConnection();
            String sql = "insert into goods(name)values(?)";
            preparedStatement = connection.prepareStatement(sql);
            for (int i = 1; i <= 20000; i++) {
                preparedStatement.setObject(1, "name_" + i);
                //1:"攒sql
                preparedStatement.addBatch();
                if (i % 500 == 0) {
                    //2:执行batch
                    preparedStatement.execute();
                    //3。清空batch
                    preparedStatement.clearBatch();
                }
            }
            long end=System.currentTimeMillis();
            System.out.println("花费的时间为"+(end-start));//20000:537
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            JDBCUtils.closeResource(connection,preparedStatement);
        }
    }
    //批量插入的操作4:设置连接不允许自动提交数据
    public static void test4()  {
        Connection connection=null;
        PreparedStatement preparedStatement=null;
        try {
            long start=System.currentTimeMillis();
            connection = JDBCUtils.getConnection();
            //设置不允许自动提交数据
            connection.setAutoCommit(false);
            String sql = "insert into goods(name)values(?)";
            preparedStatement = connection.prepareStatement(sql);
            for (int i = 1; i <= 20000; i++) {
                preparedStatement.setObject(1, "name_" + i);
                //1:"攒sql
                preparedStatement.addBatch();
                if (i % 500 == 0) {
                    //2:执行batch
                    preparedStatement.execute();
                    //3。清空batch
                    preparedStatement.clearBatch();
                }
            }
            long end=System.currentTimeMillis();
            System.out.println("花费的时间为"+(end-start));//20000:284
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            JDBCUtils.closeResource(connection,preparedStatement);
        }
    }
}
