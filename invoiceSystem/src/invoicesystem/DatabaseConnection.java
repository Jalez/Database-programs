package invoicesystem;

import java.sql.*;

public class DatabaseConnection {
    private String url;
    private String user;
    private String password;
    private Connection conn;

    public DatabaseConnection(String database, String user, String password) {
        this.url = "jdbc:postgresql://dbstud2.sis.uta.fi:5432/" + database;
        this.user = user;
        this.password = password;
    }

    public Connection getConnection() {
        if (this.conn == null) {
            connect();
        }
        return this.conn;
    }
    
    public void connect() {
        try {
            this.conn = DriverManager.getConnection(this.url, this.user, this.password);
        } catch (Exception e) {
            System.out.println("Unable to connect to database.");
            System.out.println(e.getMessage());
        }
    }

    public boolean close() {
        if (this.conn == null) {
            return true;
        }
        try {
            conn.close();
            return true;
        } catch (Exception e) {
            System.out.println("Unable to close database connection.");
            System.out.println(e.getMessage());
            return false;
        }
    }

}