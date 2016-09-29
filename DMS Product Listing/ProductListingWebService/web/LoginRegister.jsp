<%-- 
    Document   : LoginRegister
    Created on : 24/06/2016, 12:36:59 AM
    Author     : Dom
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link type="text/css" rel="stylesheet" href="Css/Style.css" />
        <title>Login</title>
    </head>
    <body>
        <h1>Login / Register</h1>
        
        <div class="default" style="float: left; width: 40%; margin-left: 2%; margin-right: 0.5%;">
            <h3 style="text-align: center">Login</h3>
            
            <form action="http://localhost:8080/ProductListingWebService/RestClientServlet" method="POST">
            <br/>
            <label> Name: </label>
            <input type="text" name="loginName" required> 
            
            </br></br>
            
            <label> Password: </label>
            <input type="password" name="loginPassword" required> 
            
            </br></br>
            
            <input type="submit" name="button" value="Login" class="form"> 
            <br/>
            <span class="red"><% if(request.getAttribute("LoginError") != null) 
                out.print(request.getAttribute("LoginError")); %></span>
            </form>
        </div>
        
        
        
        <div class="default" style="float: left;  width: 40%; margin-left: 0.5%;">
            <h3 style="text-align: center">Register</h3>
            
            <form action="http://localhost:8080/ProductListingWebService/MessageSenderServlet" method="POST">
            <br/>
            <label> Name: </label>
            <input type="text" name="registerName" required> 
            
            </br></br>
            
            <label> Password: </label>
            <input type="password" name="registerPassword"  required> 
            
            </br></br>
            
            <input type="submit" name="button" value="Register" class="form"> 
            </form>
            <span class="red"><% if(request.getAttribute("Message") != null) 
                out.print(request.getAttribute("Message")); %></span>
        </div>
        
    </body>
</html>
