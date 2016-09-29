/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package helperclasses;

import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Dom
 */
@XmlRootElement
public class User {
    
    private String name;
    private double earnings;
    private int numOfSales;

    public User() {
    }

    public User(String name, double earnings, int numOfSales) {
        this.name = name;
        this.earnings = earnings;
        this.numOfSales = numOfSales;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getEarnings() {
        return earnings;
    }

    public void setEarnings(double earnings) {
        this.earnings = earnings;
    }

    public int getNumOfSales() {
        return numOfSales;
    }

    public void setNumOfSales(int numOfSales) {
        this.numOfSales = numOfSales;
    }
    
    
}
