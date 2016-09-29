/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import Util.ResultSetConverter;
import helperclasses.Product;
import helperclasses.User;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Singleton;
import org.json.JSONArray;

/**
 *
 * @author Dom
 */
@Singleton
public class DatabaseEJB implements DatabaseBeanLocal {

    private static final String DRIVER = "org.apache.derby.jdbc.ClientDriver";
    private static final String DATABASE = "jdbc:derby://localhost:1527/ass4db";
    
    private static final String username = "dom";
    private static final String password = "password";
    
    private static Connection connect;
    private static Statement statement;
    private static PreparedStatement ps;
    
    @Override
    public void createConnection() {
        try {
            // attempt retreaving driver
            Class.forName(DRIVER);
            
            // connect to database
            connect = DriverManager.getConnection(DATABASE, username, password);
            statement = connect.createStatement();
            
            System.out.println("Connection Successful");
        } catch (ClassNotFoundException ex) {
            System.err.println("Incorrect Database Driver");
        } catch (SQLException ex) {
            System.err.println("Do not have connection to database");
        }
    }

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")

    @Override
    public void closeConnection() {
        
        try {
            // close statement first
            if (statement != null) { statement.close(); }
            if (connect != null) { connect.close(); }
            if (ps != null) { ps.close(); }
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseEJB.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public boolean addUser(String name, String password) {
        createConnection();
        boolean successful = false;
        String command = "INSERT INTO Dom.Users(Name, Password) "
                + "Values  "
                + "(?,"
                +  "?)";
        
        try {
            synchronized(this) // synchronize access to stmt
            { 
                ps = connect.prepareStatement(command);
                ps.setString(1, name);
                ps.setString(2, password);
                

                ps.executeUpdate();

                successful = true;
            }
            
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseEJB.class.getName()).log(Level.SEVERE, null, ex);
            
        }
        closeConnection();
        return successful;
        
    }

    @Override
    public boolean verifyUser(String name, String password) {
        createConnection();
        boolean exists = false;
        String command = "SELECT * "
                + "FROM DOM.Users "
                + "WHERE NAME = ? "
                + "AND Password = ?";
        
        try {
            synchronized(this) // synchronize access to stmt
            { 
                ps = connect.prepareStatement(command);
                ps.setString(1, name);
                ps.setString(2, password);
                ResultSet set = ps.executeQuery();


                // check if resultSet holds any clients
                if (set.next()) { exists = true; }
            }
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseEJB.class.getName()).log(Level.SEVERE, null, ex);
        }
        closeConnection();  
        return exists;
    }

    @Override
    public boolean addProduct(String prodName, double price, String description, String username) {
        createConnection();
        boolean successful = false;
        //for new lines
        //description = description.replaceAll("\n", "%0A");
        String command = "INSERT INTO Dom.Product(Prod_Name, Price, Description, Username ) "
                + "Values  "
                + "(?,"
                +  "?, ?, ?)";
        
        try {
            synchronized(this) // synchronize access to stmt
            { 
                ps = connect.prepareStatement(command);
                ps.setString(1, prodName);
                ps.setDouble(2, price);
                ps.setString(3, description);
                ps.setString(4, username);
                ps.executeUpdate();
                successful = true;
            }
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseEJB.class.getName()).log(Level.SEVERE, null, ex);     
        }
        closeConnection();
        return successful;
    }

    @Override
    public ArrayList<Product> getProducts(String requester) {
        createConnection();
        
        ArrayList<Product> allProducts = new ArrayList<>();
        requester = requester.replace("\"", "");
        String command;
        if(requester == null || requester == ""){   
            command = "select * "
           + "from DOM.Product";
        }else{
            command = "select PRODUCTID, PROD_NAME, PRICE, DESCRIPTION, USERNAME, STATUS "
           + "from DOM.PRODUCT Where NOT USERNAME LIKE ?";
        }
        System.out.println(command);
        System.out.println(requester);
        try {
            synchronized(this) // synchronize access to stmt
            { 
                
                ps = connect.prepareStatement(command);
                if( !"".equals(requester))
                    ps.setString(1, requester);
                
                
                ResultSet rs = ps.executeQuery();
                while(rs.next()){
                    
                    Product product = new Product( rs.getInt("productID"), 
                                                rs.getString("prod_name"), 
                                                rs.getDouble("price"),  
                                                rs.getString("description"),  
                                                rs.getString("username"),
                                                rs.getString("status"));
                    allProducts.add(product);
                }                
            }
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseEJB.class.getName()).log(Level.SEVERE, null, ex);
        }
        closeConnection();
        return allProducts;
    }

    @Override
    public ArrayList<Product> getMyListings(String username) {
        createConnection();
        
        ArrayList<Product> allProducts = new ArrayList<>();
        username = username.replace("\"", "");
        String command = "select PRODUCTID, PROD_NAME, PRICE, DESCRIPTION, USERNAME, STATUS "
           + "from DOM.PRODUCT Where USERNAME LIKE ?"; 
        try {
            synchronized(this) // synchronize access to stmt
            { 
                
                ps = connect.prepareStatement(command);
                ps.setString(1, username);
                ResultSet rs = ps.executeQuery();
                while(rs.next()){
                    
                    Product product = new Product( rs.getInt("productID"), 
                                                rs.getString("prod_name"), 
                                                rs.getDouble("price"),  
                                                rs.getString("description"),  
                                                rs.getString("username"),
                                                rs.getString("status"));
                    allProducts.add(product);
                }                
            }
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseEJB.class.getName()).log(Level.SEVERE, null, ex);
        }
        closeConnection();
        return allProducts;
    }

    @Override
    public Product getProduct(int id) {
        createConnection();
        
        Product product = new Product();
        
        String command = "select PRODUCTID, PROD_NAME, PRICE, DESCRIPTION, USERNAME, STATUS "
           + "from DOM.PRODUCT Where PRODUCTID = ?"; 
        try {
            synchronized(this) // synchronize access to stmt
            { 
                
                ps = connect.prepareStatement(command);
                ps.setInt(1, id);
                ResultSet rs = ps.executeQuery();
                if(rs.next()){
                    
                    product = new Product( rs.getInt("productID"), 
                                                rs.getString("prod_name"), 
                                                rs.getDouble("price"),  
                                                rs.getString("description"),  
                                                rs.getString("username"),
                                                rs.getString("status"));
                    
                }                
            }
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseEJB.class.getName()).log(Level.SEVERE, null, ex);
        }
        closeConnection();
        return product;
    }

    @Override
    public User getUser(String name) {
        createConnection();
        
        User user = new User();
        name = name.replace("\"", "");
        String command = "select NAME, EARNINGS, NUMOFSALES "
           + "from DOM.Users Where NAME LIKE ?"; 
        try {
            synchronized(this) // synchronize access to stmt
            { 
                
                ps = connect.prepareStatement(command);
                ps.setString(1, name);
                ResultSet rs = ps.executeQuery();
                if(rs.next()){
                    
                    user = new User( rs.getString("NAME"), 
                                                rs.getDouble("EARNINGS"),                                    
                                                rs.getInt("NUMOFSALES"));
                    
                }                
            }
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseEJB.class.getName()).log(Level.SEVERE, null, ex);
        }
        closeConnection();
        return user;
    }

    @Override
    public boolean addPurchase(int productID, String buyer, String seller) {
        createConnection();
        boolean successful = false;
        //for new lines
        //description = description.replaceAll("\n", "%0A");
        String command = "INSERT INTO Dom.Purchases(ProductID, Buyer, Seller ) "
                + "Values  "
                + "(?,"
                +  "?, ?)";
        
        try {
            synchronized(this) // synchronize access to stmt
            { 
                ps = connect.prepareStatement(command);
                ps.setInt(1, productID);
                ps.setString(2, buyer);
                ps.setString(3, seller);
                ps.executeUpdate();
                successful = true;
            }
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseEJB.class.getName()).log(Level.SEVERE, null, ex);     
        }
        closeConnection();
        return successful;
    }

    @Override
    public void updateUser(double price, String user) {
        createConnection();
        user = user.replace("\"", "");
        String command = "UPDATE Dom.Users SET NUMOFSALES = NUMOFSALES + 1,"
                + " Earnings = Earnings + ? WHERE "
                + "Name Like ?";

        try {
            synchronized(this) // synchronize access to stmt
            { 
                ps = connect.prepareStatement(command);
                ps.setDouble(1, price);
                ps.setString(2, user);
                ps.executeUpdate();   
            }
            
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseEJB.class.getName()).log(Level.SEVERE, null, ex);
            
        }
        closeConnection();
        
    }

    @Override
    public void updateProduct(int productID) {
        //update product status
         createConnection();
        
        String command = "UPDATE Dom.Product SET Status = 'Sold' "
                + "WHere ProductID = ?";

        try {
            synchronized(this) // synchronize access to stmt
            { 
                ps = connect.prepareStatement(command);
                ps.setInt(1, productID);
                ps.executeUpdate();   
            }
            
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseEJB.class.getName()).log(Level.SEVERE, null, ex);
            
        }
        closeConnection();
    }

    @Override
    public String deleteProduct(int productID) {
        //update product status
         createConnection();
        String message = "Delete failed";
        String command = "DELETE FROM Dom.Product "
                + "WHere ProductID = ?";

        try {
            synchronized(this) // synchronize access to stmt
            { 
                ps = connect.prepareStatement(command);
                ps.setInt(1, productID);
                ps.executeUpdate();   
                message = "Delete Successful";
            }
            
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseEJB.class.getName()).log(Level.SEVERE, null, ex);
            
        }
        closeConnection();
        return message;
    }
    
    
    
    
}
