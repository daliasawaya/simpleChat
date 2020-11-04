import java.util.Scanner;

import common.ChatIF;

public class ServerConsole implements ChatIF {
	
	EchoServer server; 
	
	Scanner fromConsole; 
	
	final public static int DEFAULT_PORT = 5555;
	
	public ServerConsole(EchoServer echoServer) 
	  {
	    fromConsole = new Scanner(System.in); 
	    server=echoServer;
	  }
	
	
	public void accept() 
  	{
    	try
    	{
      		String message;

      		while (true) 
      		{
        		message = fromConsole.nextLine();
        		server.handleMessageFromServerUI(message);
      		}
    	} 
    	catch (Exception ex) 
    	{
      		System.out.println("Unexpected error while reading from console!");
    	}
  	}

	@Override
	public void display(String message) {
		    System.out.println("> " + message);
	}

}
