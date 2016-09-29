<%-- 
    Document   : IndividualListing
    Created on : 26/06/2016, 4:16:57 AM
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
        <title>JSP Page</title>
    </head>
    <body>
        <!-----------------Header and Navigation bar------------------->
    <form action="http://localhost:8080/ProductListingWebService/RestClientServlet" 
            method="POST">
    <h1>All Listings</h1>
    <ul>
        <li ><input  type="submit" name="button" value="Homepage"  ></li>
        <li ><input  type="submit" name="button" value="All Listings" class="active" ></li>
        <li><input type="submit" name="button" value="Create Listing"></li>
        <li><input type="submit" name="button" value="My Listings" ></li>
        <li><input type="submit" name="button" value="Logout"></li>
    </ul>
    <h3> Logged in as: <%= session.getAttribute( "User" ) %> </h3>
    </form>
    <% Product product = (Product)request.getAttribute("Listing"); 
        User user = (User)request.getAttribute("Lister"); 
        
    %>
    
    <div class="default">
        <h3>Lister Information</h3>
        <p><% out.print("<strong>Username: </strong>" + user.getName());%></p>
        <p><% out.print("<strong>Total Earnings: $</strong>" + user.getEarnings());%>
            <br/><% out.print("<strong>Number of Products Sold: </strong>" + user.getNumOfSales());%></p>
    </div>
    <div class="default">
        <h3>Product</h3>
        <p><span class="confirmation"><% out.print(product.getProdName());%></span><br/><br/>
            <% out.print("<strong>ID: #</strong>" +product.getProdID() ); %><br/>
        <% out.print("<strong>Price: $</strong>" +product.getPrice()); %><br/>
        <% out.print("<strong>Status: </strong>");%><span class="red"><% out.print(product.getStatus());%></span><br/><br/>
        <label><strong>Description:</strong></label><br/>
        <% out.print(product.getDescription());%></p>
        
        <% if(!session.getAttribute( "User" ).equals(user.getName()) && 
                product.getStatus().equals("Avaliable")){%>
        <form action="http://localhost:8080/ProductListingWebService/MessageSenderServlet" method="POST">
            <input type="submit" name="button" value="Purchase" class="form confirm">
            <input type="hidden" name= "buyer" value="<%=session.getAttribute( "User" )%>">
            <input type="hidden" name= "seller" value="<%= user.getName()%>">
            <input type="hidden" name= "price" value="<%= product.getPrice()%>">
            <input type="hidden" name= "productID" value="<%= product.getProdID()%>">
        </form> 
        <% } %>
        <button  type="button" name="Back" onclick="history.back()" class="form ">Back</button>
    </div>
    
    </body>
</html>
