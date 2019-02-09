package warsztaty.modul2;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    public static Connection getConnection() throws SQLException{
        Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/warsztaty_modul_2?serverTimezone=UTC&useSSL=false&characterEncoding=utf8",
                "root",
                "coderslab");
        return connection;
    }
}
