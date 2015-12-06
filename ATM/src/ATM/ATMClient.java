package ATM;

import java.util.Scanner;
import ATM.LanguageObject;


public class ATMClient {

	public static void main(String[] args) {
		String hostName = args[0];
		int portNumber = HelperMethods.GetPortFromArgs(args, 1);
		
		if (portNumber == -1)
			return;
		Scanner scan = new Scanner(System.in);
		
		ClientInterface ci = new ClientInterface(hostName, portNumber);
		LanguageObject lo = null;
		
		while (true) {
			if (lo == null) { // If the language hasn't already been chosen.
				System.out.println(ci.getString());
				ci.writeData(scan.nextLine().getBytes());
				lo = new LanguageObject(ci.readData());
				ci.showmsg(lo);
				ci.writeData(scan.nextLine().getBytes());
			} else {
				// This part is the user interface after the language has been chosen.
				if(!ci.showmsg(lo)) { // Read from server and print out on screen.
					ci.close();
					return; // terminate
				}
				ci.writeData(scan.nextLine().getBytes()); // Read input from user and send the result to the server.
			}
		}
	}

}
