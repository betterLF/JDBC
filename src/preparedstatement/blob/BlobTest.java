package preparedstatement.blob;

import bean.Customer;
import util.JDBCUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.sql.*;

/**
 * @authtor liFei
 * @date 2020/4/20-15:50
 */
public class BlobTest {
    /*
    使用Preparedstatement来操作Blob类型数据
     */

    public static void main(String[] args) throws Exception {
         //testInsert();
         testQuery();
    }
    //添加
    public static void testInsert() throws Exception {
        Connection connection = JDBCUtils.getConnection();
        String sql="insert into customers(name,email,birth,photo)values(?,?,?,?)";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setObject(1,"张宇豪");
        preparedStatement.setObject(2,"zhang@qq.com");
        preparedStatement.setObject(3,"1992-09-08");
        FileInputStream is=new FileInputStream(new File("D:\\Practice\\JDBC\\src\\preparedstatement\\blob\\d.jpg"));
        preparedStatement.setBlob(4,is);
        preparedStatement.execute();
        JDBCUtils.closeResource(connection,preparedStatement);
    }
    //查询
    public static void testQuery() throws Exception{
        Connection connection = JDBCUtils.getConnection();
        String sql="select id,name,email,birth,photo from customers where id=?";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setObject(1,20);
        ResultSet resultSet = preparedStatement.executeQuery();
        if(resultSet.next()){
            int id=resultSet.getInt(1);
            String name=resultSet.getString(2);
            String email=resultSet.getString(3);
            Date birth=resultSet.getDate(4);
            Customer customer=new Customer(id,name,email,birth);
            System.out.println(customer);
            //将blob类型的字段下载下来，以文件的方式保存在本地
            Blob photo=resultSet.getBlob("photo");
            InputStream is=photo.getBinaryStream();
            FileOutputStream fos=new FileOutputStream("朴敏英.jpg");
            byte buffer[]=new byte[1024];
            int len;
            while((len=is.read(buffer))!=-1){
                fos.write(buffer,0,len);
            }
            is.close();
            fos.close();
        }
        JDBCUtils.closeResource(connection,preparedStatement);
    }
}
