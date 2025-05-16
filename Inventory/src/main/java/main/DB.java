package main;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DB {

    private static final String
            db_name = "db0321",
            db_password = "password_123",
            db_user = "root",
            db_port = "3306",
            db_host = "localhost";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(
                "jdbc:mysql://" + db_host + ":" + db_port + "/" + db_name, db_user, db_password
        );
    }
}