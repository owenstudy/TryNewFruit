package db.connect;

import static org.junit.Assert.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import org.junit.Test;

public class MysqlConnectTest {

    @Test
    public void test() {

      Statement stmt = null;
      ResultSet rs = null;
      Connection conn=null;

      try {
        Class.forName("com.mysql.jdbc.Driver");
        conn = DriverManager.getConnection("jdbc:mysql://w.rdc.sae.sina.com.cn:3306/app_trynewfruit", "xyxo14xokx", "5i3ykzh3z1kk043izx053wjyziwx02m1z4h3m4kx");
        stmt = conn.createStatement();
        rs = stmt.executeQuery("select * from T_user_list");
        while(rs.next()){
            System.out.println(rs.getInt("User_id"));
            System.out.println(rs.getString("User_name"));
            System.out.println(rs.getString("Mobile"));
            System.out.println(rs.getString("Mail"));
            System.out.println(rs.getDate("Insert_time"));
            System.out.println(rs.getDate("Insert_by"));           
            System.out.println(rs.getString("Update_time"));
            System.out.println(rs.getString("Update_by"));
        }
      } catch (Exception e) {
        e.printStackTrace();
      }
    }

}