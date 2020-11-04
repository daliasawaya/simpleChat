// This file contains material supporting section 3.7 of the textbook:
// "Object Oriented Software Engineering" and is issued under the open-source
// license found at www.lloseng.com 

import ocsf.server.*;

/**
 * This class overrides some of the methods in the abstract 
 * superclass in order to give more functionality to the server.
 *
 * @author Dr Timothy C. Lethbridge
 * @author Dr Robert Lagani&egrave;re
 * @author Fran&ccedil;ois B&eacute;langer
 * @author Paul Holden
 * @version July 2000
 */
public class EchoServer extends AbstractServer 
{
  //Class variables *************************************************
  
  /**
   * The default port to listen on.
   */
  final public static int DEFAULT_PORT = 5555;
  
  ServerConsole serverUI; 
  
  //Constructors ****************************************************
  
  /**
   * Constructs an instance of the echo server.
   *
   * @param port The port number to connect on.
   */
  public EchoServer(int port) 
  {
    super(port);
    //this.serverUI = serverUI;
    serverUI = new ServerConsole(this);
  }
  
  
  //Instance methods ************************************************
  
  /**
   * This method handles any messages received from the client.
   *
   * @param msg The message received from the client.
   * @param client The connection from which the message originated.
   */
  public void handleMessageFromClient
    (Object msg, ConnectionToClient client)
  {
	  if (msg.toString().startsWith("#login")) {
		  
		  System.out.println("A new client is attempting to connect to the server.");
		  System.out.println("Message received: " + msg + " from " + client.getInfo("loginID"));
		  
		  if ((boolean)client.getInfo("firstClientMessage")) {
			 
			  client.setInfo("loginID", msg.toString().substring(7)); 
			  System.out.println(client.getInfo("loginID")+" has logged on");
			  try {
				  client.sendToClient(client.getInfo("loginID")+" has logged on");
			  }catch(Exception e) {}
			  
		  }
		  else {
			  System.out.println("Cannot login, login must be first command, closing connection");
			  try {
			  client.close();
			  }
			  catch (Exception e) {
				  System.out.println("Could not close client "+client.getInfo("loginID"));
			  }
		  }
	  }
	  else {			 
		  System.out.println("Message received: " + msg + " from " + client.getInfo("loginID"));
		  this.sendToAllClients(client.getInfo("loginID")+ " " +msg);
	  }
	  client.setInfo("firstClientMessage", false);
  }
  
  public void handleMessageFromServerUI(String message)
  {
	  
	  boolean isListening = isListening();
	  
	  if (message.startsWith("#quit")) {
		  try {
			  close();
		  }
		  catch (Exception e) {}
		  System.exit(0);
	  }
	  
	  else if (message.startsWith("#stop")) {
		  if (isListening ) {
			  stopListening();
		  }
	  }
	  
	  else if (message.startsWith("#close")) {
		  try {
			  close();
		  }
		  catch (Exception e) {}
	  }
	  
	  else if (message.startsWith("#setport")) {
		  if (!isListening ) {
			  setPort(Integer.parseInt(message.substring(9))); 
			  System.out.println("port set to "+message.substring(9));
		  }
	  }
	  
	  else if (message.startsWith("#start")) {
		  if (!isListening ) {
			  try {
				  listen();
			  }
			  catch (Exception e) {}
		  }
		  else {
			  System.out.println("Cannot start, it is already started");
		  }
	  }
	  
	  else if (message.startsWith("#getport")) {
		  if (isListening ) {
			  System.out.println("The port is "+getPort());
		  }
	  }
	  
	  else {
		  try {
			  serverUI.display(message);
			  sendToAllClients("SERVER MSG> "+message);
		  }
		  catch (Exception e) {
			  System.out.println("Could not send message to server or clients.");
		  }
	  }
  }
    
  /**
   * This method overrides the one in the superclass.  Called
   * when the server starts listening for connections.
   */
  protected void serverStarted()
  {
    System.out.println
      ("Server listening for connections on port " + getPort());
  }
  
  /**
   * This method overrides the one in the superclass.  Called
   * when the server stops listening for connections.
   */
  protected void serverStopped()
  {
    System.out.println
      ("Server has stopped listening for connections.");
  }
  
  protected void clientConnected(ConnectionToClient client) {

	  //System.out.println(client.getInfo("loginID")+" has logged on");
	  
	  client.setInfo("firstClientMessage", true); //not sure abt this line cant lie its sus
	  
	  //commented for test 2004
	  //System.out.println(client.getInfo("firstClientMessage"));
  }
  
  synchronized protected void clientDisconnected(ConnectionToClient client) {
	  System.out.println(client.getInfo("loginID")+" has disconnected");
	  sendToAllClients(client.getInfo("loginID")+" has disconnected");
  }
  
  synchronized protected void clientException(ConnectionToClient client, Throwable exception) {
	  System.out.println(client.getInfo("loginID")+" has disconnected");
	  sendToAllClients(client.getInfo("loginID")+" has disconnected");
  }

  
  //Class methods ***************************************************
  
  /**
   * This method is responsible for the creation of 
   * the server instance (there is no UI in this phase).
   *
   * @param args[0] The port number to listen on.  Defaults to 5555 
   *          if no argument is entered.
   */
  public static void main(String[] args) 
  {
    int port = 0; //Port to listen on

    try
    {
      port = Integer.parseInt(args[0]); //Get port from command line
    }
    catch(Throwable t)
    {
      port = DEFAULT_PORT; //Set port to 5555
    }
	
    //ServerConsole serverUI = new ServerConsole(port);
    
    EchoServer sv = new EchoServer(port);
    
    try 
    {
      sv.listen(); //Start listening for connections
    } 
    catch (Exception ex) 
    {
      System.out.println("ERROR - Could not listen for clients!");
    }
    sv.serverUI.accept();
  }
}
//End of EchoServer class
