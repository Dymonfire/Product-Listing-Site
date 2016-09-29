/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package helperclasses;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Dom
 */
@XmlRootElement
public class Product implements Serializable{
    
    private int prodID;
    private String prodName;
    private double price;
    private String description;
    private String username;
    private String status;

    public Product() {
    }

    
    public Product(int prodID, String prodName, double price, String description, String username, String status) {
        this.prodID = prodID;
        this.prodName = prodName;
        this.price = price;
        description = description.replaceAll("\n", "<br/>");
        this.description = description;
        this.username = username;
        this.status = status;
    }

    
    public int getProdID() {return prodID;}
    public String getProdName() { return prodName;}
    public double getPrice() {return price;}
    public String getDescription() {return description;}
    public String getUsername() {return username;}

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
    

    public void setProdID(int prodID) {       this.prodID = prodID;    }
    public void setProdName(String prodName) {this.prodName = prodName;}
    public void setPrice(double price) {this.price = price;}
    public void setDescription(String description) {this.description = description;}
    public void setUsername(String username) {this.username = username;}
    
    public void print(){
        System.out.println(prodID);
        System.out.println(prodName);
        System.out.println(price);
        System.out.println(description);
        System.out.println(username);
        System.out.println(status);
    }
}
