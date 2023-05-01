
package com.libmgmt.cs5394;

import static spark.Spark.*;

public class Main {

    public static void main(String[] args) {
        
        
        get("/hello", (req, res) -> "Hello, World");
        get("/logout", Controller.logoutPage);
        get("/login", Controller.loginPage);
        get("/register", Controller.registerPage);
        get("/removeUser", Controller.removeUserPage);
        get("/success", Controller.handleSuccess);
        get("/addbooksuccess", Controller.addBookSuccess);
        get("/removeusersuccess",Controller.removeusersuccess );
        get("/removeBook", Controller.removeBookPage);
        get("/addBook", Controller.addBookPage);
        post("/login", Controller.handleLogin);
        post("/register", Controller.handleRegister);
        post("/removeUser", Controller.handleRemoveUser);
        post("/removeBook", Controller.handleRemoveBook);
        post("/addBook", Controller.handleAddBook);
        
        
        
        get("/admin/dashboard", Controller.admin_dashboard);
        get("/dashboard", Controller.dashboard);
        post("/borrow", Controller.borrow);
        post("/return", Controller.returnBook);
        post("/renew", Controller.renewBook);
        post("/request", Controller.handleRequest);
    }
}
