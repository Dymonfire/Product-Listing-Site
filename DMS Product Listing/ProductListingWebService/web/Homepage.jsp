<%-- 
    Document   : Homepage
    Created on : 12/04/2016, 1:33:49 AM
    Author     : Tristan Borja (1322097) & Dominic Yuen (1324837)
--%>


<!DOCTYPE html>
<html>
<head>
    <link type="text/css" rel="stylesheet" href="Css/Style.css" />
    <title>Home page</title>
</head>
<body>
    <!-----------------Header and Navigation bar------------------->
    <form action="http://localhost:8080/ProductListingWebService/RestClientServlet" 
            method="POST">
    <h1>Home Page</h1>
    
    <ul>
        <li ><input  type="submit" name="button" value="Home" class="active" ></li>
        <li><input type="submit" name="button" value="All Listings"></li>
        <li><input type="submit" name="button" value="Create Listing"></li>
        <li><input type="submit" name="button" value="My Listings" ></li>
        <li><input type="submit" name="button" value="Logout"></li>
    </ul>
    <h3> Logged in as: <%= session.getAttribute( "User" ) %> </h3>
    <!-----------------Body and Documentation------------------->
    
    <div class="default">
        
        <h3>Creator</h3>
        <p>
           Dominic Yuen (1324837)</p>
    </div>  
    <div class="default">
        <h3>About </h3>
        <p> DMS Assignment 4 </p>
        <p>
        -	First you must login or register, by entering a username and corresponding password.
        <br/>-	Once you do so you will be able to view the Homage, which has general information about the site.
<br/> -	You can then view "All Listings" which displays all other users' products, you can click the "view" button to view additional information about that product and its seller. You will also have the option to buy the product if it is unsold.
<br/>-	You can list a product clicking "Create a Listing" this displays a form which you can fill out to add your products information.
<br/>-	You can view your own listings clicking the "My Listings" button, here you can view your listings and delete them if you want to. Your personal information will also be displayed.
<br/>-	Lastly, you can logout, which brings you back to the login screen.

        </p>
    </div >
    <div class="default">
        <h3> Documentation</h3>
        <a href="Instructions.docx" type="Content-Type: application/msword; " target="_blank">Documentation</a>
    </div>
</body>
</html>
