package ATM;

import java.io.*;   
import java.net.*;  
import java.util.Scanner;

/**
   @author Snilledata
*/
public class ATMClient {
    private static int connectionPort = 8989;
    
    public static void main(String[] args) throws IOException {
        
       	connectionPort = HelperMethods.GetPortFromArgs(args, 1);
    	if (connectionPort == -1)
    		return;
    	
       
        String adress = "";

        try {
            adress = args[0];
        } catch (ArrayIndexOutOfBoundsException e) {
            System.err.println("Missing argument ip-adress");
            System.exit(1);
        }
        
        ClientInterface ci = new ClientInterface(adress, connectionPort);
      
        // ask user about the language.
        // send that to the server and get the appropriate language data.
        // save data in a file using SaveBytesToFile. load it later unless language changes
        // or its version number.
        // take user input.
        // etc.
        
    }
    
    
}   
