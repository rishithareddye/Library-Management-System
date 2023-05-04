
package com.libmgmt.cs5394;

import java.text.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;
import spark.*;
import spark.template.velocity.*;

public class Controller {

    public static Route loginPage = (Request req, Response res) -> {

        if (req.session().attribute("user") != null) {
            res.redirect("/dashboard");
        }

        Map<String, Object> model = new HashMap<>();
        return new VelocityTemplateEngine().render(new ModelAndView(model, "login.vm"));
    };

    public static Route logoutPage = (req, res) -> {
        
        if(req.session().attribute("user") != null) {
            req.session().removeAttribute("user");
        }
        
        res.redirect("/login");

        return null;
    };
    
    public static Route removeBookPage = (req, res) -> {
        Map<String, Object> model = new HashMap<>();
        return new VelocityTemplateEngine().render(new ModelAndView(model, "removeBook.vm"));
    };
   
    public static Route removeUserPage = (req, res) -> {
        
        Map<String, Object> model = new HashMap<>();
        return new VelocityTemplateEngine().render(new ModelAndView(model, "removeUser.vm"));
        
    };
    public static Route addBookPage = (req, res) -> {
        Map<String, Object> model = new HashMap<>();
        return new VelocityTemplateEngine().render(new ModelAndView(model, "addBook.vm"));
    };
    
    public static int addBookToDB(String id,String booktitle,String authorname,
            String booktype,String bestseller,String publishdate) {
        try {
           System.out.println("in addBookToDB..1 ");
           Database db = new Database();
           Properties props = new Properties();
           Connection conn = db.getConnection(); 
           Statement stmt = conn.createStatement();

            int sql = stmt.executeUpdate("INSERT INTO book (id, title,"
                    + "author, type, is_current_best_seller, published)"+" "
                    + "VALUES ('"+id+"','"+booktitle+"','"+authorname+"',"
                    + "'"+booktype+"','"+bestseller+"','"+publishdate+"')");
          
            System.out.println("sql statement result: "+sql);
            return sql;
        } catch (SQLException ex) {
            System.out.println(ex.getLocalizedMessage());
        }
        return 0;
    }
    
    public static Route handleAddBook = (req, res) -> {
        System.out.println("In handle add book\n");

        String id = req.queryParams("bookid");
        String booktitle = req.queryParams("booktitle");
        String authorname = req.queryParams("authorname");
        String booktype = req.queryParams("booktype");
        String bestseller = req.queryParams("bestseller");
        String publishdate = req.queryParams("publishdate");
        
        int register = addBookToDB(id, booktitle, authorname, booktype, bestseller, publishdate);
        if(register == 1)
            res.redirect("/addbooksuccess");
        else
            return "<h1>Invalid data, please try again</h1>";
        return null;
    };
    
    public static Route registerPage = (req, res) -> {
        
        Map<String, Object> model = new HashMap<>();
        return new VelocityTemplateEngine().render(new ModelAndView(model, "register.vm"));
        
    };
    
    public static Route removeusersuccess = (req, res) -> {
        Map<String, Object> model = new HashMap<>();
        return new VelocityTemplateEngine().render(new ModelAndView(model, "success_2.vm"));  
    };
    
    public static Route addBookSuccess = (req, res) -> {
        Map<String, Object> model = new HashMap<>();
        return new VelocityTemplateEngine().render(new ModelAndView(model, "success_1.vm"));  
    };
    public static Route handleSuccess = (req, res) -> {
        Map<String, Object> model = new HashMap<>();
        return new VelocityTemplateEngine().render(new ModelAndView(model, "success.vm"));  
    };
    
  public static Route handleRemoveBook = (req, res) -> {
      System.out.println("In Remove Book\n");
        String bookid = req.queryParams("bookid");
        try {
           Database db = new Database();
           Properties props = new Properties();
           Connection conn = db.getConnection();
           Statement stmt = conn.createStatement();
System.out.println("BookID:"+bookid);
           int sql = stmt.executeUpdate("DELETE FROM book WHERE id ='"+bookid+"';");
           if(sql != 0)
                return "<h1>Removed the Book.</h1>";
           else 
                return "<h1>Invalid Book ID.</h1>";
           
        } catch (SQLException ex) {
            System.out.println(ex.getLocalizedMessage());
        }
       return null;
  };
    public static Route handleRemoveUser = (req, res) -> {
        System.out.println("In Remove User\n");
        String userid = req.queryParams("userid");
        try {
           Database db = new Database();
           Properties props = new Properties();
           Connection conn = db.getConnection();
           Statement stmt = conn.createStatement();

           int sql = stmt.executeUpdate("DELETE FROM checked_out_book WHERE checked_out_by ='"+userid+"';");
           sql = stmt.executeUpdate("DELETE FROM USER WHERE id ='"+userid+"';");
           if(sql != 0)
               res.redirect("/removeusersuccess");
           else
               return "<h1>Invalid User ID </h1>";
            
        } catch (SQLException ex) {
            System.out.println(ex.getLocalizedMessage());
        }
        
        return null;
        
    };
    
    public static Route handleRegister = (req, res) -> {
        System.out.println("In handle Register\n");

        String name = req.queryParams("name");
        String username = req.queryParams("username");
        String password = req.queryParams("password");
        String age = req.queryParams("age");
        String address = req.queryParams("address");
        String phone = req.queryParams("phonenumber");
        
        int register = registerToDB(name, username, password, age, address, phone);
        if(register == 1)
            res.redirect("/success");
        else
            return "<h1>Invalid data, please try again</h1>";
        return null;
    };
    public static Route handleLogin = (req, res) -> {
        Map<String, Object> model = new HashMap<>();

        String username = req.queryParams("username");
        String password = req.queryParams("password");

        boolean authenticated = Controller.authenticate(username, password);

        if (authenticated) {

            model.put("username", username);
            Controller.setSession(req, username, password);

            User user = req.session().attribute("user");

            System.out.println(username);
            System.out.println(username.equalsIgnoreCase("admin"));

            if(username.equalsIgnoreCase("admin")) {
                res.redirect("/admin/dashboard");
            }

            res.redirect("/dashboard");

        }

        return "<h1>Invalid credentials</h1>";
    };

    public static Route admin_dashboard = (req, res) -> {

        User user = req.session().attribute("user");

        if(user != null) {
            Map<String, Object> model = new HashMap<>();

            List<Book> books = Controller.getAllBooks();
            List<BorrowedBook> borrowedBooks = Controller.getBorrowedBooks(user);
            HashSet<Integer> hs = new HashSet<>();

            if (borrowedBooks != null) {
                for (int i = 0; i < borrowedBooks.size(); i++) {
                    if (borrowedBooks.get(i) != null) {
                        borrowedBooks.get(i).setBorrowed(true);
                        hs.add(borrowedBooks.get(i).getId());
                    }
                }
            }

            if (books != null) {
                for (int i = 0; i < books.size(); i++) {
                    if (books.get(i) != null) {
                        if (hs.contains(books.get(i).getId())) {
                            books.get(i).setBorrowed(true);
                        }
                    }

                }
            }

            model.put("user", user);
            model.put("books", books);
            model.put("borrowedBooks", borrowedBooks);

            return new VelocityTemplateEngine().render(new ModelAndView(model, "admin.vm"));
        }

        res.redirect("/login");

        return null;
    };

    public static Route handleRequest = (req, res) -> {
        if (req.queryParams("book_id") == null) {
            res.redirect("/dashboard");
        }

        User user = Controller.getCurrentUser(req);

        int book_id = Integer.parseInt(req.queryParams("book_id"));
        int user_id = user.getId();
        
        try {
           System.out.println("in handlerequest..1 ");

           Database db = new Database();
          
            Properties props = new Properties();
          
            Connection conn = db.getConnection();
          
            Statement stmt = conn.createStatement();
          

            int sql = stmt.executeUpdate("INSERT INTO REQUESTED (USER_ID, BOOK_ID)"+" "
                    + "VALUES ('"+user_id+"','"+book_id+"')");
          
            System.out.println("sql statement result: "+sql);
            return "<h1>Selected item has been requested!!</h1>";
            
        } catch (SQLException ex) {
            System.out.println(ex.getLocalizedMessage());
            return "<h1>Selected item has already been requested!!!</h1>";
        }
        
    };
    public static Route dashboard = (req, res) -> {

        User user = req.session().attribute("user");

        if (user != null) {

            Map<String, Object> model = new HashMap<>();

            
            List<Book> books = Controller.getAvailableBooks();
            List<BorrowedBook> borrowedBooks = Controller.getBorrowedBooks(user);
            
            List<Book> allBooks = Controller.getAllBooks();
            List<Book> BorrowedBooks = new ArrayList<Book>();
            boolean flag = true;

            System.out.println("all books size: "+ allBooks.size());
            System.out.println("available books size: "+ books.size());
            for(int x = 0; x < allBooks.size(); x++) {
                flag = true;
                for(int z = 0; z < books.size(); z++) {
                    if (allBooks.get(x).getId() == books.get(z).getId()){
                        flag  = false;
                        break;
                    }
                }
                if(flag) {
                    System.out.println("Adding : "+ allBooks.get(x).getId());
                    BorrowedBooks.add(allBooks.get(x));
                }
            }
            
            for(int x = 0; x < BorrowedBooks.size();) {
                for(int z = 0; z < borrowedBooks.size(); z++) {
                    System.out.println("before if x & z "+x+" "+z);
                    if (BorrowedBooks.get(x).getId() == borrowedBooks.get(z).getId()){
                        System.out.println("x & z "+x+" "+z);
                        BorrowedBooks.remove(x);
                        if(x!=0)
                            x = x-1;
                        break;
                    }
                }
                x = x+1;
            }
            
            
            
            
            HashSet<Integer> hs = new HashSet<>();

            if (borrowedBooks != null) {
                for (int i = 0; i < borrowedBooks.size(); i++) {
                    if (borrowedBooks.get(i) != null) {
                        borrowedBooks.get(i).setBorrowed(true);
                        hs.add(borrowedBooks.get(i).getId());
                    }
                }
            }

            if (books != null) {
                for (int i = 0; i < books.size(); i++) {
                    if (books.get(i) != null) {
                        if (hs.contains(books.get(i).getId())) {
                            books.get(i).setBorrowed(true);
                        }
                    }

                }
            }

            model.put("user", user);
            model.put("books", books);
            model.put("borrowedBooks", borrowedBooks);
            model.put("BorrowedBooks", BorrowedBooks);
            

            return new VelocityTemplateEngine().render(new ModelAndView(model, "dashboard.vm"));
        }

        res.redirect("/login");
        return null;
    };
	
	public static int registerToDB(String name, String username,String password,
            String age,String address,String phone) {
            
        try {
           System.out.println("in registerTODB..1 ");

           Database db = new Database();
          
            Properties props = new Properties();
          
            Connection conn = db.getConnection();
          
            Statement stmt = conn.createStatement();
          

            int sql = stmt.executeUpdate("INSERT INTO USER (name, username,"
                    + "password, age, address, phone)"+" "
                    + "VALUES ('"+name+"','"+username+"','"+password+"',"
                    + ""+age+",'"+address+"','"+phone+"')");
          
            System.out.println("sql statement result: "+sql);
            return sql;
        } catch (SQLException ex) {
            System.out.println(ex.getLocalizedMessage());
        }
        return 0;
    }

    public static boolean authenticate(String username, String password) {

        try {

            Database db = new Database();
            Connection conn = db.getConnection();
            String sql = "select * from user where username='" + username + "' AND password='" + password + "';";
            ResultSet rs = db.getResults(conn, sql);

            boolean authenticated = !(rs.next() == false);
            System.out.println(sql);
            System.out.println(authenticated);

            if (authenticated) {
                while (rs.next()) {

                    break;
                }
            }
            conn.close();

            return authenticated;
        } catch (SQLException ex) {
        }

        return false;

    }

    public static Route borrow = (req, res) -> {

        if (req.queryParams("book_id") == null) {
            res.redirect("/dashboard");
        }

        User user = Controller.getCurrentUser(req);

        int book_id = Integer.parseInt(req.queryParams("book_id"));

        if (!borrowBook(user, book_id)) {
            return "<h1>You cannot checkout more than 5 books in a single day...</h1>";
        }

        res.redirect("/dashboard");

        return null;
    };
    
   
    public static Route renewBook = (req, res) -> {
        
        int book_id = Integer.parseInt(req.queryParams("book_id"));
        List<Date> dates = new ArrayList<Date>();
        User user = Controller.getCurrentUser(req);
        
        String pattern = "yyyy-MM-dd";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        
        
        try {
           System.out.println("in renewbook..1 ");

           Database db = new Database();
           Properties props = new Properties();
           Connection conn = db.getConnection();
           Statement stmt = conn.createStatement();
           
           String sql = "SELECT due_at from checked_out_book where book_id = '"+book_id+"' and returned = 0;";
           ResultSet rs = db.getResults(conn, sql);
           
           
           while (rs.next()) {
               dates.add(rs.getDate(1));
           }
           for(int i = 0; i < dates.size(); i++) {
            Calendar c = Calendar.getInstance();
            c.setTime(dates.get(i));
            c.add(Calendar.DATE, 5 );
            dates.set(i, c.getTime());
            String d = dateFormat.format(c.getTime());
            System.out.println("Date   --"+simpleDateFormat.format(dates.get(i)));
            sql = "SELECT COUNT(book_id) from requested where book_id = '"+book_id+"';";
            System.out.println("789");
            rs = db.getResults(conn, sql);
            rs.next();
            int count = rs.getInt(1);
            if(count > 0)
                return "<h1> There's a request for this book, so please return the book. Thank you! </h1>";
            
            int q = stmt.executeUpdate("INSERT INTO renewed(book_id)VALUES('"+book_id+"');");
            System.out.println("123");
            q = stmt.executeUpdate("UPDATE checked_out_book SET due_at = '"+d+"' WHERE book_id = '"+book_id+"' and returned = 0;");
            System.out.println("456");
            
                
            res.redirect("/dashboard");
           }
           
        } catch (SQLException ex) {
            System.out.println(ex.getLocalizedMessage());
                return "<h1> You have already renewed the book once, Please return the book. Thank you! </h1>";
                
        }
        
        return null;
        
    };

    public static Route returnBook = (req, res) -> {

        if (req.queryParams("book_id") == null) {
            res.redirect("/dashboard");
        }

        User user = Controller.getCurrentUser(req);

        int book_id = Integer.parseInt(req.queryParams("book_id"));

        returnBook(book_id, user);
        
        try {
           System.out.println("in returnbook..1 ");

           Database db = new Database();
          
            Properties props = new Properties();
          
            Connection conn = db.getConnection();
          
            Statement stmt = conn.createStatement();
          

            int sql = stmt.executeUpdate("DELETE FROM requested WHERE book_id = '"+book_id+"';");
            sql = stmt.executeUpdate("DELETE FROM renewed WHERE book_id = '"+book_id+"';");
          
            System.out.println("sql statement result: "+sql);
            
        } catch (SQLException ex) {
            System.out.println(ex.getLocalizedMessage());
        }
        
        
        res.redirect("/dashboard");

        return null;
    };

    public static User getCurrentUser(Request req) {
        return req.session().attribute("user");
    }

    public static void returnBook(int book_id, User user) {
        
        try {

            Database db = new Database();
            Connection conn = db.getConnection();
            Statement stmt = conn.createStatement();

            String sql = "update checked_out_book set returned=true where book_id=" + book_id + " and checked_out_by=" + user.getId();
            stmt.executeUpdate(sql);
            
            conn.close();

        } catch (SQLException ex) {
            System.out.println(ex.getLocalizedMessage());
        }
    }
    
    public static List<BorrowedBook> getBorrowedBooks(User user) {

        List<BorrowedBook> borrowedBooks = new ArrayList<BorrowedBook>();

        Map<Integer, User> users = User.getUsers();

        try {

            Database db = new Database();
            Connection conn = db.getConnection();

            String sql = "";

            if(user.getUsername().equalsIgnoreCase("admin")) {
                sql += "select * from checked_out_book where returned is false";
            } else {
                sql += "select * from checked_out_book where checked_out_by=" + user.getId() + " and returned is false;";
            }

            ResultSet rs = db.getResults(conn, sql);

            while (rs.next()) {
                Book book = Controller.getBook(rs.getInt(2));
                BorrowedBook bbook = new BorrowedBook(rs.getInt(3), rs.getDate(4), rs.getDate(5), rs.getBoolean(6));
                bbook.setBook(book);
                bbook.setBorrowedUser(users.get(bbook.getCheckedOutBy()));

                // book.setBorrowerName(users.get(rs.getInt(3)).getName());
                borrowedBooks.add(bbook);
            }

            conn.close();

        } catch (SQLException ex) {
            System.out.println(ex.getLocalizedMessage());
            return null;
        }
        return borrowedBooks;
    }

    public static boolean borrowBook(User user, int book_id) {

        try {

            System.out.println("User Age:" + user.getAge());

            if (user.getAge() <= 12) {

                Database db = new Database();
                Connection conn = db.getConnection();
                Statement stmt = conn.createStatement();
                String sql = "SELECT count(*) FROM checked_out_book WHERE DATE(checked_out_at) = CURDATE() AND returned = 0 AND `checked_out_by`=" + user.getId() + ";";
                ResultSet rs = stmt.executeQuery(sql);

                System.out.println(sql);

                rs.next();

                int count = rs.getInt(1);

                System.out.println("Book count: " + count);
                conn.close();

                if (count + 1 > 5) {
                    // Children cannot checkout more than 5 books in a single day.
                    return false;
                }
            }

            Database db = new Database();
            Connection conn = db.getConnection();
            Statement stmt = conn.createStatement();
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date = new Date();
            System.out.println(formatter.format(date));

            Book book = Controller.getBook(book_id);
            int days = 21;

            if (book.isBestSeller() || book.getType().equals("audio") || book.getType().equals("video")) {
                days = 14;
            }

            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Calendar c = Calendar.getInstance();
            String currentTime = dateFormat.format(c.getTime());
            c.add(Calendar.DATE, days);

            String dueDate = dateFormat.format(c.getTime());

            String sql = "insert into checked_out_book(book_id, checked_out_by, checked_out_at, due_at, returned)VALUES(" + book_id + ", " + user.getId() + ", '" + currentTime + "', '" + dueDate + "', false);";


            stmt.executeUpdate(sql);

//            System.out.println(sql);
            conn.close();

        } catch (SQLException ex) {
            System.out.println(ex.getLocalizedMessage());

            return false;
        }

        return true;
    }

    public static Book getBook(int id) {

        Book book = null;

        try {

            Database db = new Database();
            Connection conn = db.getConnection();
            String sql = "select * from book where id=" + id + ";";
            ResultSet rs = db.getResults(conn, sql);

            while (rs.next()) {
                book = new Book(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getBoolean(5), rs.getDate(6));
//                req.session().attribute("user", new User(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getInt(5), rs.getString(6), rs.getString(7)));
                break;
            }

            conn.close();

        } catch (SQLException ex) {
        }
        return book;
    }

    public static void setSession(Request req, String username, String password) {

        try {

            Database db = new Database();
            Connection conn = db.getConnection();
            String sql = "select * from user where username='" + username + "' AND password='" + password + "';";
            ResultSet rs = db.getResults(conn, sql);

            while (rs.next()) {
                req.session().attribute("user", new User(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getInt(5), rs.getString(6), rs.getString(7)));
                break;
            }

            conn.close();

        } catch (SQLException ex) {
        }

    }

    public static List<Book> getAllBooks() {

        List<Book> books = new ArrayList<Book>();

        try {

            Database db = new Database();
            Connection conn = db.getConnection();
            String sql = "select * from book;";
            ResultSet rs = db.getResults(conn, sql);

            while (rs.next()) {
                books.add(new Book(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getBoolean(5), rs.getDate(6)));
            }

            conn.close();

            return books;
        } catch (SQLException ex) {
        }

        return null;
    }

    public static List<Book> getAvailableBooks() {

        List<Book> books = new ArrayList<Book>();

        try {

            Database db = new Database();
            Connection conn = db.getConnection();
            String sql = "select * from book where id not in (select book_id from checked_out_book where returned is false);";
            ResultSet rs = db.getResults(conn, sql);

            while (rs.next()) {
                books.add(new Book(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getBoolean(5), rs.getDate(6)));
            }

            conn.close();

            return books;
        } catch (SQLException ex) {
        }

        return null;
    }


    public static void mydata() {
//
//        User user = Controller.getCurrentUser();
//
//        List<Book> borrowedBooks = Controller.getBorrowedBooks(user);
//
//        for(int i = 0; i < borrowedBooks.size(); i++) {
//
//        }
//        try {
//
//            Database db = new Database();
//            Connection conn = db.getConnection();
//            String sql = "select * from user;";
//            ResultSet rs = db.getResults(conn, sql);
//
//            while(rs.next()) {
//                data.get("id").add(rs.getInt(1));
//                data.get("name").add(rs.getString(2));
//                data.get("username").add(rs.getString(3));
//                data.get("address").add(rs.getString(5));
//                data.get("phone").add(rs.getString(6));
//            }
//
//
//            conn.close();
//
//        } catch(SQLException ex) {
//            return null;
//        }

    }
}
