/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Resource;
import javax.inject.Inject;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSConnectionFactory;
import javax.jms.JMSContext;
import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.MessageProducer;
import javax.jms.ObjectMessage;
import javax.jms.Queue;
import javax.jms.QueueConnection;
import javax.jms.QueueConnectionFactory;
import javax.jms.QueueSender;
import javax.jms.QueueSession;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Dom
 */
public class MessageSenderServlet extends HttpServlet {

    @Resource(mappedName = "jms/MyQueue")
    private Queue myQueue;

   

    @Resource(mappedName="jms/myQueueConnectionFactory")
      private ConnectionFactory connectionFactory;
   @Resource(mappedName="jms/ClientQueue") private  Queue clientQueue;
    

    
    private QueueConnection conn;
    private QueueSession session;
    private QueueSender producer;
    public static boolean messageRecieved = true;
    public static String messageToBeSent = "";
    public static String userRegistered ="";
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
        
        try
      {  // obtain a connection to the JMS provider
        
          InitialContext ic = new InitialContext();
          QueueConnectionFactory qcf = 
            (QueueConnectionFactory)ic.lookup("jms/myQueueConnectionFactory");
          
        conn = qcf.createQueueConnection();
         session = conn.createQueueSession(false, Session.AUTO_ACKNOWLEDGE);
         producer = session.createSender(myQueue);
        // obtain an untransacted context for producing messages
//        session = conn.createSession(false, Session.AUTO_ACKNOWLEDGE);
//        // obtain a producer of messages to send to the queue
//        producer = session.createProducer(myQueue);
      }
      catch (JMSException e)
      {  System.err.println("Unable to open connection: " + e);
      } catch (NamingException ex) {
            Logger.getLogger(MessageSenderServlet.class.getName()).log(Level.SEVERE, null, ex);
        }
        String button = request.getParameter("button");
       
        
        switch (button) {
            // Direct to Add sale page
            case "Register":
                registerUser(request, response);
                break;
            // Direct to Confirm Sale page
            case "Purchase":
                purchaseProduct(request, response);
                break;
                
                
        }
    }
    
   
   
   public void closeConnection()
   {  try
      {  if (session != null)
            session.close();
         if (conn != null)
            conn.close();
      }
      catch (JMSException e)
      {  System.err.println("Unable to close connection: " + e);
      }
   }
    
    public void registerUser(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        try {
            //Extracts information from the Add Product Page form
            String name             = request.getParameter("registerName");
            String password            = request.getParameter("registerPassword");
            ArrayList<String> user = new ArrayList<>();
            user.add(name);
            user.add(password);
            userRegistered = name;
            InitialContext ic = new InitialContext();
            QueueConnectionFactory qcf = 
            (QueueConnectionFactory)ic.lookup("jms/myQueueConnectionFactory");
          
            conn = qcf.createQueueConnection();
            session = conn.createQueueSession(false, Session.AUTO_ACKNOWLEDGE);
            producer = session.createSender(myQueue);
            //sending message after receiver has been started
            ObjectMessage message = session.createObjectMessage();
            message.setObject(user);
            producer.send(message);
            recieveMessage(request, response, "/LoginRegister.jsp");
            
        } catch (JMSException ex) {
            Logger.getLogger(MessageSenderServlet.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NamingException ex) {
            Logger.getLogger(MessageSenderServlet.class.getName()).log(Level.SEVERE, null, ex);
        }
       
    }
    
    public void purchaseProduct(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        try {
            //Extracts information from the Add Product Page form
            String buyer                  = request.getParameter("buyer");
            String seller               = request.getParameter("seller");
            String priceString          = request.getParameter("price");
            String productID            = request.getParameter("productID");
            double price                = Double.parseDouble(priceString);
            
            InitialContext ic = new InitialContext();
            QueueConnectionFactory qcf = 
            (QueueConnectionFactory)ic.lookup("jms/myQueueConnectionFactory");
          //sets up jms and sends map message containing purchase info
            conn = qcf.createQueueConnection();
            session = conn.createQueueSession(false, Session.AUTO_ACKNOWLEDGE);
            producer = session.createSender(myQueue);
            //sending message after receiver has been started
            MapMessage message = session.createMapMessage();
            message.setString("buyer", buyer);
            message.setString("seller", seller);
            message.setDouble("price", price);
            message.setString("productID", productID);
            
            producer.send(message);
            
            recieveMessage(request, response, "/RestClientServlet");
            
        } catch (JMSException ex) {
            Logger.getLogger(MessageSenderServlet.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NamingException ex) {
            Logger.getLogger(MessageSenderServlet.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public void recieveMessage(HttpServletRequest request, HttpServletResponse response, String page) throws ServletException, IOException{
        Connection conn = null;
        Session session = null;
        MessageConsumer consumer = null;
        try
        {  
            InitialContext ic = new InitialContext();
            QueueConnectionFactory qcf = 
            (QueueConnectionFactory)ic.lookup("jms/receiveConnectionFactory");
            // obtain a connection to the JMS provider
            conn = qcf.createQueueConnection();
            // obtain an untransacted context for producing messages
            session = conn.createSession(false, Session.AUTO_ACKNOWLEDGE);
            // obtain a producer of messages to send to the queue
            consumer = session.createConsumer(clientQueue);
            consumer.setMessageListener(new IncomingListener());
            messageRecieved = false;
            conn.start(); // start delivery of incoming messages

            System.out.println("Waiting for incoming messages");
            try
            {  
                while(messageRecieved == false)
                    Thread.sleep(30);
            }
           catch (InterruptedException e)
           {} // ignore
           System.out.println("Finished waiting for messages");
        }
        catch (JMSException e)
        {  System.err.println("Unable to open connection: " + e);
        } catch (NamingException ex) {
            Logger.getLogger(MessageSenderServlet.class.getName()).log(Level.SEVERE, null, ex);
        }
        finally
        {  try
           {  if (session != null)
                 session.close();
              if (conn != null)
                 conn.close();
           }
           catch (JMSException e)
           {  System.err.println("Unable to close connection: " + e);
           }
        }
        
        
        request.setAttribute("Message", messageToBeSent);
        
        changePage(request, response, page);
    }
    
    // static inner class that listens for incoming JMS messages
   private static class IncomingListener implements MessageListener
   {
      @Override
      public void onMessage(Message message)
      {  try
         {  if (message instanceof TextMessage){
                System.out.println("Received text messagee: "
                  + ((TextMessage)message).getText());
                messageToBeSent = ((TextMessage)message).getText();
                messageRecieved = true;
         }
            else if(message instanceof MapMessage){
                try {
                    
                    MapMessage map = (MapMessage) message;
                    if(map.getString("User").equals(userRegistered)){
                        messageToBeSent = map.getString("Message");
                        messageRecieved = true;
                    }
                } catch (JMSException ex) {
                  Logger.getLogger(MessageSenderServlet.class.getName()).log(Level.SEVERE, null, ex +"90");
                }
            }
            else
               System.out.println("Received non-text messagehi: "
                  + message.getJMSType());
         }
         catch(JMSException e)
         {  System.err.println("Exception with incoming message: "+e);
         }
         
         
      }
      
      
   }
    
    protected void changePage(HttpServletRequest request, HttpServletResponse response, String SendTo)
        throws ServletException, IOException{
        RequestDispatcher dispatcher = getServletContext()
        .getRequestDispatcher(SendTo);
        dispatcher.forward(request, response);
        
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
