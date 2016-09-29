/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servlets;

import helperclasses.Product;
import helperclasses.User;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import org.glassfish.jersey.client.ClientConfig;

/**
 *
 * @author Dom
 */
public class RestClientServlet extends HttpServlet {

    ClientConfig config;
    Client client;
    WebTarget target;
    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        config = new ClientConfig();
        client = ClientBuilder.newClient(config);
        target = client.target(getBaseURI());
        
        String button = request.getParameter("button");
       
        if(button != null){
            switch (button) {
                // Direct to Add sale page
                case "Add Product":
                    addProduct(request, response);
                    break;
                // Direct to Confirm Sale page
                case "Login":
                    login(request, response);
                    break;
                case "Register":
                    register(request, response);
                    break;
                case "Create Listing":
                    changePage(request, response, "/AddListing.jsp");
                    break;
                // direct to CustomerRegistryPage
                case "All Listings":
                    allListings(request, response);  
                    break;
                case "My Listings":
                    myListings(request, response);
                    break;
                case "Homepage":
                    changePage(request, response, "/Homepage.jsp");
                    break;
                case "Logout":
                    changePage(request, response, "/LoginRegister.jsp");
                    break;
                // default to Homepage
                case "View":
                    viewListing(request, response);
                    break;
                case "Delete":
                    deleteProduct(request, response);
                    break;
                default:
                    request.setAttribute("Message", request.getAttribute("Message"));
                    allListings(request, response);
                break;
            }
        }
        else{
            request.setAttribute("Message", request.getAttribute("Message"));
                allListings(request, response);
            }
                
    }
    
    public void login(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        String name                   = request.getParameter("loginName");
        String password               = request.getParameter("loginPassword");
        
        Boolean returned = target.path("webresources").
                path("productListing").
                path("login").
                queryParam("name", name).
                queryParam("password", password).
                request().
                get(Boolean.class);
        
        if(returned){
            HttpSession session = request.getSession();
            session.setAttribute("User", name);
            changePage(request, response, "/Homepage.jsp");
        }
        else{
            request.setAttribute("LoginError", name +" doesn't exist or your password is incorrect");
            changePage(request, response, "/LoginRegister.jsp");
        }
    }
    
    public void register(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        String name                 = request.getParameter("registerName");
        String password             = request.getParameter("registerPassword");
    }
    
    public void addProduct(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        //Extracts information from the Add Product Page form
        String name                  = request.getParameter("productName");
        String priceString           = request.getParameter("productPrice");
        String description           = request.getParameter("description");
        double price = (double) Double.parseDouble(priceString);
        description = description.replaceAll("\r\n", "%0A");
        System.out.print("fyuv");
       
        
        Boolean returned = target.path("webresources").
                path("productListing").
                path("product").
                queryParam("prodName", name).
                queryParam("price", price).
                queryParam("description", description).
                queryParam("username", request.getSession().getAttribute("User")).
                request().
                post(Entity.text(""), Boolean.class);
        System.out.println(returned);
        if(returned){
            request.setAttribute("message", "You Product: " + name + " has been listed");
            
        }
        else{
            request.setAttribute("message", "Your listing failed please try again");
        }
        changePage(request, response, "/AddListing.jsp");
                   
    }
    
    public void allListings(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{

        List<Product> returned = target.path("webresources").
                path("productListing").
                path("allproducts").
                queryParam("username", request.getSession().getAttribute("User")).
                request().
                get(new GenericType<List<Product>>() {});
        request.setAttribute("allListings", returned);
        changePage(request, response, "/AllListings.jsp");
    }
    public void myListings(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{

        List<Product> returned = target.path("webresources").
                path("productListing").
                path("myproducts").
                queryParam("username", request.getSession().getAttribute("User")).
                request().
                get(new GenericType<List<Product>>() {});
        
        User user = target.path("webresources").
                path("productListing").
                path("user").
                queryParam("name", request.getSession().getAttribute("User")).
                request().
                get(User.class);
        request.setAttribute("myListings", returned);
        request.setAttribute("User", user);
        changePage(request, response, "/MyListings.jsp");
    }
    
    public void viewListing(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        int listing               = Integer.parseInt((String) request.getParameter("prodID"));
        String username           = request.getParameter("user");
        
        Product product = target.path("webresources").
                path("productListing").
                path("product").
                queryParam("productID", listing).
                request().
                get(Product.class);
        
        User user = target.path("webresources").
                path("productListing").
                path("user").
                queryParam("name", username).
                request().
                get(User.class);
        
        request.setAttribute("Listing", product);
        request.setAttribute("Lister", user);
        changePage(request, response, "/IndividualListing.jsp");
        
    }
    
    public void deleteProduct(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        int listing               = Integer.parseInt((String) request.getParameter("prodID"));
        
        String reponse = target.path("webresources").
                path("productListing").
                path("delete").
                queryParam("productID", listing).
                request().
                post(Entity.text(""), String.class);
        
        request.setAttribute("Message", reponse);
        
        myListings(request, response);
        
    }
    protected void changePage(HttpServletRequest request, HttpServletResponse response, String SendTo)
        throws ServletException, IOException{
        RequestDispatcher dispatcher = getServletContext()
        .getRequestDispatcher(SendTo);
        dispatcher.forward(request, response);
        
    }
    private static URI getBaseURI() {
    return UriBuilder.fromUri("http://localhost:8080/ProductListingWebService").build();
  }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
