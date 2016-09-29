<%-- 
    Document   : AddListing
    Created on : 17/06/2016, 5:02:48 PM
    Author     : Dom
--%>

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
        <h1>Home Page</h1>
        
        <ul>
            <li ><input  type="submit" name="button" value="Homepage"  ></li>
            <li ><input  type="submit" name="button" value="All Listings"  ></li>
            <li><input type="submit" name="button" value="Create Listing" class="active"></li>
            <li><input type="submit" name="button" value="My Listings"></li>
            <li><input type="submit" name="button" value="Logout"></li>

        </ul>
        <h3> Logged in as: <%= session.getAttribute( "User" ) %> </h3>
        </form>
        <div class="default">
        <form action="http://localhost:8080/ProductListingWebService/RestClientServlet" method="POST">
            <br/>
            <label> Product Name: </label>
            <input type="text" name="productName" required> 
            </br></br>
            
            <label> Product Price: $</label>
            <input type="number" value="0" min="0" name="productPrice" required> 

             </br></br>
            
            <label> Product Description:  </label> 
            <br/>
            <textarea draggable="false" cols="100" rows="10" name="description" maxlength="200" title="Max length 200"></textarea>
            </br></br>
           
            
            
            <!-----------------Stores and passes current date------------------->        
             <input type="submit" name="button" value="Add Product" class="form"> 
            <input type="reset" value="Reset Form" class="form right" > </br></br>
            <span><% if(request.getAttribute("message") != null) 
                out.print(request.getAttribute("message")); %></span>
            </div>
        </form>
    </body>
</html>
