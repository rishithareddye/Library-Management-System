
package com.libmgmt.cs5394;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class Database {
    
    String connectionUrl = "jdbc:mysql://localhost:3306/librarymgmt";
    
    String username = "libraryuser";
    //region Password
    String password = "Library@2023";
    //endregion

    public Database() {}
    
    public Database(String username, String password) {
        this.username = username;
        this.password = password;
    }
    public Connection getConnection() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DriverManager.getConnection(this.connectionUrl, this.username, this.password);

            return conn;
            
        } catch (Exception ex) {
            System.out.println("Exception Error: " + ex.getLocalizedMessage());
        }
        
        return null;
    }
    
    public ResultSet getResults(Connection conn, String sql) {
        
        try {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            return rs;
        } catch(Exception ex) {
            System.out.println("Exception Error: " + ex.getLocalizedMessage());
        }
        
        return null;
        
    }
    
}
