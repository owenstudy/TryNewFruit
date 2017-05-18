package db.connect;

import java.sql.Connection;  
import java.sql.SQLException;  
import java.sql.DriverManager;  
  
public class JDBCUtil_simple {  
    private String dbDriver = "com.mysql.cj.jdbc.Driver"; // �뱾��������ͬ  
    private String dbUrl = "jdbc:mysql://w.rdc.sae.sina.com.cn:3306/app_trynewfruit"; // app_yanzelΪ����app���ݿ����ƣ���ͨmysql�����ͨ��[�������]-��[MySql]->[����MySql]�У��鿴���ݿ�����  
    private String dbUser = ""; // Ϊ[Ӧ����Ϣ]->[������Ϣ]->[key]�е�access key  
    private String dbPassword = ""; // Ϊ[Ӧ����Ϣ]->[������Ϣ]->[key]�е�secret  
  
    public Connection createConnection() throws Exception {  
        Connection connection = null;  
        try {  
            Class.forName(this.dbDriver);  
        } catch (ClassNotFoundException e) {  
            e.printStackTrace();  
            throw e;  
        }  
  
        try {  
            connection = DriverManager.getConnection(dbUrl, dbUser,  
                    dbPassword);  
        } catch (SQLException e) {  
            e.printStackTrace();  
            throw e;  
        }  
        return connection;  
    }  
      
  
}  