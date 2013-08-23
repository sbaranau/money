package by.siarhei.baranau.server.DB;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Created with IntelliJ IDEA.
 * User: siarhei
 * Date: 8/2/13
 * Time: 10:32 PM
 * To change this template use File | Settings | File Templates.
 */
public class Connector {

    public static Connection connection;
    protected static Connection createConnection() {

        if (connection != null) {
            return connection;
        }
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.out.println("Check your MySQL JDBC Driver?");
            e.printStackTrace();
            return null;
        }

        try {
            connection = DriverManager
                    .getConnection("jdbc:mysql://localhost:3306/osdbm", "root", "11341003");

        } catch (SQLException e) {
            System.out.println("Connection Failed! Check output console");
            e.printStackTrace();
            return null;
        }

        if (connection != null) {
            System.out.println("You made it, take control your database now!");
        } else {
            System.out.println("Failed to make connection!");
        }
    return connection;
    }

}
