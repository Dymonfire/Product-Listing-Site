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
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PUT;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import org.json.JSONArray;

/**
 * REST Web Service
 *
 * @author Dom
 */
@Path("/productListing")
public class ProductListingResource {

    DatabaseBeanLocal databaseEJB = lookupDatabaseEJBLocal();

    @Context
    private UriInfo context;

    /**
     * Creates a new instance of ProductListingResource
     */
    public ProductListingResource() {
    }

    /**
     * Retrieves representation of an instance of server.ProductListingResource
     * @param name
     * @param password
     * @return an instance of java.lang.String
     */
    @Path ("{name}/{password}")
    @GET
    @Produces(MediaType.TEXT_HTML)
    public String getHtml(@PathParam("name") String name, @PathParam("password") String password ) {
        //TODO return proper representation object
        //throw new UnsupportedOperationException();
        System.out.println("hih");
        databaseEJB.addUser(name, password);
        
        return "<h1>"+ name+ " "+ password+"</h1>";
    }

    @Path ("/login")
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public Boolean verifyUser(@QueryParam("name") String name, @QueryParam("password") String password ) {
       
        return databaseEJB.verifyUser(name, password);
 
    }
    
    @Path ("/product")
    @POST
    @Produces(MediaType.TEXT_PLAIN)
    public Boolean addProduct(@QueryParam("prodName") String prodName, @QueryParam("price") double price,
            @QueryParam("description") String description, @QueryParam("username") String username) {
       
        return databaseEJB.addProduct(prodName, price, description, username);
    }
    
    @Path ("/allproducts")
    @GET
    @Produces(MediaType.APPLICATION_XML)
    public List<Product> getAllProducts(@QueryParam("username") String requester) {//
       
        return databaseEJB.getProducts(requester);
    }
    
    @Path ("/myproducts")
    @GET
    @Produces(MediaType.APPLICATION_XML)
    public List<Product> getMyProducts(@QueryParam("username") String requester) {
       

        return databaseEJB.getMyListings(requester);
    }
    
    @Path ("/product")
    @GET
    @Produces(MediaType.APPLICATION_XML)
    public Product getProduct(@QueryParam("productID") int id) {
       

        return databaseEJB.getProduct(id);
    }
    
    @Path ("/user")
    @GET
    @Produces(MediaType.APPLICATION_XML)
    public User getUser(@QueryParam("name") String name) {
        return databaseEJB.getUser(name);
    }
    
    @Path ("/delete")
    @POST
    @Produces(MediaType.APPLICATION_XML)
    public String getUser(@QueryParam("productID") int id) {
        return databaseEJB.deleteProduct(id);
    }
    
    /**
     * PUT method for updating or creating an instance of ProductListingResource
     * @param content representation for the resource
     */
    @PUT
    @Consumes(MediaType.TEXT_HTML)
    public void putHtml(String content) {
    }

    private DatabaseBeanLocal lookupDatabaseEJBLocal() {
        try {
            javax.naming.Context c = new InitialContext();
            return (DatabaseBeanLocal) c.lookup("java:global/ProductListingWebService/DatabaseEJB!server.DatabaseBeanLocal");
        } catch (NamingException ne) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "exception caught", ne);
            throw new RuntimeException(ne);
        }
    }
}
