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
                test2();
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
            System.out.println("花费的时间为"+(end-start));
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            JDBCUtils.closeResource(connection,preparedStatement);
        }
    }
}
