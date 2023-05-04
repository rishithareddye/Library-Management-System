
package com.libmgmt.sampleproject;

import java.util.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

public class User {
    
    private int id;
    private String name;
    private String username;
    private int age;
    private String address;
    private String phone;

    public User() {
        
    }
    
    public User(int id, String name, String username, int age, String address, String phone) {
        this.id = id;
        this.name = name;
        this.username = username;
        this.age = age;
        this.address = address;
        this.phone = phone;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
    
    public static Map<String, ArrayList> getAllUsers() {
        
        Map<String, ArrayList> data = new HashMap<>();
        
        data.put("id", new ArrayList<Integer>());
        data.put("name", new ArrayList<String>());
        data.put("username", new ArrayList<String>());
        data.put("address", new ArrayList<String>());
        data.put("phone", new ArrayList<String>());
        
        try {
            
            Database db = new Database();
            Connection conn = db.getConnection();
            String sql = "select * from user;";
            ResultSet rs = db.getResults(conn, sql);
        
            while(rs.next()) {
                data.get("id").add(rs.getInt(1));
                data.get("name").add(rs.getString(2));
                data.get("username").add(rs.getString(3));
                data.get("address").add(rs.getString(5));
                data.get("phone").add(rs.getString(6));
            }
            
            
            conn.close();
            
        } catch(SQLException ex) {
            return null;
        }
        
        return data;
    }


    public static Map<Integer, User> getUsers() {

        Map<Integer, User> data = new HashMap<>();

        try {

            Database db = new Database();
            Connection conn = db.getConnection();
            String sql = "select * from user;";
            ResultSet rs = db.getResults(conn, sql);

            while(rs.next()) {
                data.put(rs.getInt(1), new User(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getInt(5), rs.getString(6), rs.getString(7)));
            }

            conn.close();

        } catch(SQLException ex) {
            return null;
        }

        return data;
    }
}
