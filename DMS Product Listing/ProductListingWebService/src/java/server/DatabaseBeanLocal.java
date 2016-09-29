/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import helperclasses.Product;
import helperclasses.User;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.Local;
import org.json.JSONArray;

/**
 *
 * @author Dom
 */
@Local
public interface DatabaseBeanLocal {

    void createConnection();

    void closeConnection();

    boolean addUser(String name, String password);

    boolean verifyUser(String name, String password);

    boolean addProduct(String prodName, double price, String description, String username);

    ArrayList<Product> getProducts(String requester);

    ArrayList<Product> getMyListings(String username);

    Product getProduct(int name);

    User getUser(String name);

    boolean addPurchase(int productID, String buyer, String seller);

    void updateUser(double price, String user);

    void updateProduct(int productID);

    String deleteProduct(int productID);
    
}
