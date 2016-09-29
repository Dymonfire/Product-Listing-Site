/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Resource;
import javax.ejb.ActivationConfigProperty;
import javax.ejb.EJB;
import javax.ejb.MessageDriven;
import javax.ejb.MessageDrivenContext;
import javax.inject.Inject;
import javax.jms.JMSConnectionFactory;
import javax.jms.JMSContext;
import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.MessageListener;
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
import servlets.MessageSenderServlet;

/**
 *
 * @author Dom
 */
@MessageDriven(mappedName="jms/MyQueue")
   public class ProductMessageBean implements MessageListener
{

    @EJB
    private DatabaseBeanLocal databaseEJB;
   // field obtained via dependency injection (not used here)
   @Resource private MessageDrivenContext mdc;
   @Resource(mappedName = "jms/ClientQueue")
    private Queue ClientQueue;


    private QueueConnection conn;
    private QueueSession session;
    private QueueSender producer;

   public ProductMessageBean()
   {
//       try
//      {  // obtain a connection to the JMS provider
//        
//       
//        
//      }
//      catch (JMSException e)
//      {     System.err.println("Unable to open connection: " + e);
//      } catch (NamingException ex) {
//            Logger.getLogger(MessageSenderServlet.class.getName()).log(Level.SEVERE, null, ex);
//        }
   
  }

    @Override
    public void onMessage(Message message)
     {  try
         {  if (message instanceof TextMessage)
               System.out.println("MessageBean received text message: "
                  + ((TextMessage)message).getText());
            else if(message instanceof ObjectMessage)
                objectMessageRecieved((ObjectMessage) message);
            else if(message instanceof MapMessage)
                mapMessageRecieved((MapMessage) message);
            else
               System.out.println
                  ("MessageBean received non-text messagebye: " + message.getJMSType());
        }
        catch(JMSException e)
        {  System.err.println("Exception with incoming message: "+e);
        }
    }
    
    public void objectMessageRecieved(ObjectMessage message){
        
       try {
           if(message.getObject() instanceof ArrayList){
                ArrayList<String> strings;
                strings = (ArrayList<String>) message.getObject();
                System.out.print("Recieved: " + strings.get(0));        

                    //if successful
                    try
                     {  
                        InitialContext ic = new InitialContext();
                        QueueConnectionFactory qcf = 
                          (QueueConnectionFactory)ic.lookup("jms/receiveConnectionFactory");
                        conn = qcf.createQueueConnection();
                        session = conn.createQueueSession(false, Session.AUTO_ACKNOWLEDGE);
                        producer = session.createSender(ClientQueue);
                        // create and send a string message
                        if(databaseEJB.addUser(strings.get(0), strings.get(1))){

                            
                           
                           MapMessage reply = session.createMapMessage();
                           String replyMessage = "User: "+strings.get(0) + " has been created successfully.";
                           reply.setString("User", strings.get(0));
                           reply.setString("Message", replyMessage);
                           System.out.print("Sending Message");
                           producer.send(reply);
                    
                        }
                        else{
                            MapMessage reply = session.createMapMessage();
                           String replyMessage = "User: "+strings.get(0) + " already exists, please try another name.";
                           reply.setString("User", strings.get(0));
                           reply.setString("Message", replyMessage);
                            System.out.print("Sending Message");
                           producer.send(reply);
                    }

                    }
                    catch (JMSException e)
                    {  System.err.println("Unable to send message: " + e);
                    } catch (NamingException ex) {
                       Logger.getLogger(ProductMessageBean.class.getName()).log(Level.SEVERE, null, ex);
                   }
               
               
               
           }
       } catch (JMSException ex) {
           Logger.getLogger(ProductMessageBean.class.getName()).log(Level.SEVERE, null, ex);
           System.err.println("Exception with incoming message: "+ex);
       }
        
    }

    private void mapMessageRecieved(MapMessage mapMessage) {
        
        try {
            InitialContext ic = new InitialContext();
            QueueConnectionFactory qcf = 
              (QueueConnectionFactory)ic.lookup("jms/receiveConnectionFactory");
            conn = qcf.createQueueConnection();
            session = conn.createQueueSession(false, Session.AUTO_ACKNOWLEDGE);
            producer = session.createSender(ClientQueue);
            TextMessage text = session.createTextMessage();
            //add purchase
            if(databaseEJB.addPurchase(mapMessage.getInt("productID"), mapMessage.getString("buyer")
                    , mapMessage.getString("seller"))){
                databaseEJB.updateUser(mapMessage.getDouble("price"), mapMessage.getString("seller"));
                databaseEJB.updateProduct(mapMessage.getInt("productID"));
                //send back reply
                
                text.setText("Purchase Successful");
            }
            else{
                
                text.setText("Purchase Failed");
            }
            producer.send(text);
        } catch (JMSException ex) {
            Logger.getLogger(ProductMessageBean.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NamingException ex) {
            Logger.getLogger(ProductMessageBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
    

