<%-- 
    Document   : MyListings
    Created on : 17/06/2016, 6:28:16 PM
    Author     : Dom
--%>

<%@page import="helperclasses.User"%>
<%@page import="java.util.List"%>
<%@page import="helperclasses.Product"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link type="text/css" rel="stylesheet" href="Css/Style.css" />
        <title>My Listings</title>
    </head>
    <body>
            <!-----------------Header and Navigation bar------------------->
        <form action="http://localhost:8080/ProductListingWebService/RestClientServlet" 
                method="POST">
        <h1>My Listings</h1>
        <ul>
            <li ><input  type="submit" name="button" value="Homepage"  ></li>
            
            <li><input type="submit" name="button" value="All Listings"></li>
            <li><input type="submit" name="button" value="Create Listing"></li>
            <li><input type="submit" name="button" value="My Listings" class="active"></li>
            <li><input type="submit" name="button" value="Logout"></li>
        </ul>
        </form>
            
    </form>
    
    <div class="default">
        <h3>
            Profile
        </h3>
        <% User userName = (User) request.getAttribute( "User" ); %>
        <p>Name: <%= userName.getName()%></p>
        <p>Number of Products sold: <%= userName.getNumOfSales()%></p>
        <p>Total Earnings: $ <%= userName.getEarnings()%></p>
    </div>
    <span class="confirmation"><% if(request.getAttribute("Message") != null){
        out.print("<div class=\"listing\" style=\"text-align: center; font-size: 20px;\">"+request.getAttribute("Message") + "</div>");} %></span>
    
    <div class="default">
        <h3>Your Listings</h3>
    <table >
        <tr>
            <th> #</th>
            <th>Product</th>
            <th> Status</th>
            <th></th>
            
        </tr>
    <% List<Product> products = (List<Product>)request.getAttribute("myListings"); 
    for(Product prod: products){
            
        %>
        <tr>
            <td>
            <div class="left">
                <% out.print(prod.getProdID()); %>
            </div>
            </td>
            <td>
            <div class="left">
                <span class="confirmation"><% out.print(prod.getProdName()); %> </span><br/>
                <% out.print("$" + prod.getPrice()); %>
            </div>
            </td>
            <td>
            <div class="left">
                <span class="red"><% out.print(prod.getStatus()); %> </span>            
            </div>
            </td>
            
            
            <td>
            <div class="left" style="float: right">
                <form form action="http://localhost:8080/ProductListingWebService/RestClientServlet" method="POST" style=" float: right;">
                    <input class="form large" type="submit" name="button" value="View" >
                     <input type="hidden" name="prodID" value="<%= prod.getProdID()%>" />
                     <input type="hidden" name="user" value="<%= prod.getUsername()%>" />
                </form>
                <form form action="http://localhost:8080/ProductListingWebService/RestClientServlet" method="POST" style="margin-right: 20px;float: right">
                    <input class="form large" type="submit" name="button" value="Delete" >
                     <input type="hidden" name="prodID" value="<%= prod.getProdID()%>" />
                </form>
                
            </div>
            </td>
        
        
    <% } %>
    </tr>
</table>
    </div>
    </body>
</html>
