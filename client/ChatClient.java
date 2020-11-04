// This file contains material supporting section 3.7 of the textbook:
// "Object Oriented Software Engineering" and is issued under the open-source
// license found at www.lloseng.com 

package client;

import ocsf.client.*;
import common.*;
import java.io.*;

/**
 * This class overrides some of the methods defined in the abstract
 * superclass in order to give more functionality to the client.
 *
 * @author Dr Timothy C. Lethbridge
 * @author Dr Robert Lagani&egrave;
 * @author Fran&ccedil;ois B&eacute;langer
 * @version July 2000
 */
public class ChatClient extends AbstractClient
{
  //Instance variables **********************************************
  
  /**
   * The interface type variable.  It allows the implementation of 
   * the display method in the client.
   */
  ChatIF clientUI; 
  
  String loginID;

  
  //Constructors ****************************************************
  
  /**
   * Constructs an instance of the chat client.
   *
   * @param host The server to connect to.
   * @param port The port number to connect on.
   * @param clientUI The interface type variable.
   */
  
  public ChatClient(String loginID, String host, int port, ChatIF clientUI) 
  {
    super(host, port); //Call the superclass constructor
    try {
    	openConnection();
    	sendToServer("#login "+loginID);
    }
    catch(Exception e) {
    	System.out.println("Cannot open connection. Awaiting command.");
    }
    
    this.loginID = loginID;
    this.clientUI = clientUI;

  }

  
  //Instance methods ************************************************
    
  /**
   * This method handles all data that comes in from the server.
   *
   * @param msg The message from the server.
   */
  public void handleMessageFromServer(Object msg) 
  {
    clientUI.display(msg.toString());
  }

  /**
   * This method handles all data coming from the UI            
   *
   * @param message The message from the UI.    
   */
  public void handleMessageFromClientUI(String message)
  {
    /*try
    {
      sendToServer(message);
    }
    catch(IOException e)
    {
      clientUI.display
        ("Could not send message to server.  Terminating client.");
      quit();
    }*/
	  	if(message.charAt(0)!='#') {
	      try
	    {
	      sendToServer(message);
	    }
	    catch(IOException e)
	    {
	      clientUI.display("Could not send message to server.  Terminating client.");
	      quit();
	    }
	      
	  	}
	  	else {
		  
		  boolean isConnected = isConnected();
		  
		  if (message.startsWith("#quit")) {
			 quit();
		  }
		  
		  else if (message.startsWith("#logoff")) {
			  if(isConnected) {
				  try {
					  closeConnection();
				  }
				  catch (Exception e) {
					  System.out.println("Could not logoff");
				  }
			  }
		  }
		  
		  else if (message.startsWith("#sethost")) {
			  if(!isConnected) {
				  setHost(message.substring(9));
				  System.out.println("Host set to "+message.substring(9));
			  }
			  else {
				  System.out.println(this+" is already connected - cannot set host");
			  }
		  }
		  
		  else if (message.startsWith("#setport")) {
			  if(!isConnected) {
			
				  setPort(Integer.parseInt(message.substring(9))); 
				  System.out.println("Port set to "+message.substring(9));
			  }
			  else {
				  System.out.println(this+" is already connected - cannot set port");
			  }
		  }
		  
		  else if (message.startsWith("#login")) {
			  
			  if(!isConnected) {
				  try {
					  openConnection();
					  sendToServer("#login "+message.substring(7));
				  }
				  catch (Exception e) {
					  System.out.println("Could not login");
				  }
			  }		  
		  }
		  
		  else if (message.startsWith("#gethost")) {
			  if(isConnected) {
				  System.out.println("The host is "+getHost());
			  }
		  }
		  
		  else if (message.startsWith("#getport")) {
			  if(isConnected) {
				  System.out.println("The port is "+getPort());
			  }
		  }
		  
		  else {
			  System.out.println("No valid command given");
		  }
		  
	  	}
		  
  }
  
  /**
   * This method terminates the client.
   */
  public void quit()
  {
    try
    {
      closeConnection();
    }
    catch(IOException e) {}
    System.exit(0);
  }


  protected void connectionClosed() {
	System.out.println("Connection closed");
  }
  
  protected void connectionException(Exception exception) {
	  System.out.println("WARNING - The server has stopped listening for connections");
	  System.out.println("SERVER SHUTTING DOWN! DISCONNECTING!");
	  System.out.println("Abnormal termination of connection.");
	  //quit();
  }

}
//End of ChatClient class
