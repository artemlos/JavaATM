package ATM;

import java.net.*;

import ATM.LanguageObject.LanguageType;

import java.io.*;

/**
   @author Viebrapadata
*/
public class ATMServer {

    private static int connectionPort = 8989;
    
    public static void main(String[] args) throws IOException {
    	
    	connectionPort = HelperMethods.GetPortFromArgs(args, 0);
    	if (connectionPort == -1)
    		return;
    	
    	LanguageObject english = new LanguageObject(LanguageType.English, 1);
    	
    	english.AddStatement("welcome", "Welcome to the Bank!");
    	english.AddStatement("banner", "Get 50% discount on business tools.");
    	
        ServerSocket serverSocket = null;
       
        boolean listening = true;
        
        try {
            serverSocket = new ServerSocket(connectionPort); 
        } catch (IOException e) {
            System.err.println("Could not listen on port: " + connectionPort);
            System.exit(1);
        }
	
        System.out.println("Bank started listening on port: " + connectionPort);
        while (listening)
            new ATMServerThread(serverSocket.accept()).start();

        serverSocket.close();
    }
}
