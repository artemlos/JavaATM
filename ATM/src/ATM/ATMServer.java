package ATM;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import ATM.LanguageObject;
import ATM.LanguageObject.LanguageType;


public class ATMServer {
	
	/**
	 * Several Client objects run parallelly, one for each client connected to the server.
	 **/
	private static class Client implements Runnable {
		
		int[] balanaces = new int[100];
		List<LinkedList<Integer>> codes;
		
		OutputStream out; 
		InputStream in;
		
		public Client(Socket clientSocket) {
			try {
				this.out = clientSocket.getOutputStream();
				this.in = clientSocket.getInputStream();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			// initializing one time codes and balances.
			codes = new ArrayList<>(100);
			
			for (int i = 0; i < balanaces.length; i++) {
				balanaces[i] = 1000; // yeah, $1000! :)
				
				LinkedList<Integer> code = new LinkedList<>();
				for (int j = 0; j < balanaces.length; j++) {
					code.push(2*j+1);
				}
				
				codes.add(code);
				
			}
		}
		
		public void writeData(byte[] data) {
			int len = data.length;
			byte[] init = new byte[2];
			init[0] = (byte)(len & 0xff);
			init[1] = (byte)(len >> 8);
			write(init);
			
			for(int z = 0; z < len/10; z++) {
				write(Arrays.copyOfRange(data, z*10, (z+1)*10));
			}
			write(Arrays.copyOfRange(data, len - (len%10) , len));
		}
		
		private void write(byte[] data) {
			try {
				out.write(data);
				out.flush();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		private byte[] read(int n) {
			byte[] cbuf = new byte[n];
	    	try {
				in.read(cbuf);
			} catch (IOException e) {
				e.printStackTrace();
			}
	    	return cbuf;
		}
		
		private byte[] toList(ArrayList<Byte> bal) {
			byte[] ret = new byte[bal.size()];
			
			for (int i = 0; i < bal.size(); i++) {
				ret[i] = bal.get(i);
			}
			return ret;
		}
		
		public byte[] readData() {
			ArrayList<Byte> bl = new ArrayList<Byte>();
			byte[] init = read(2);
			int i = 0;
			i |= init[1] & 0xff;
			i <<= 8;
			i |= init[0] & 0xff;
			
			for (int z = 0; z < i/10; z++) {
				byte[] buff = read(10);
				for (int c = 0; c < 10; c++) {
					bl.add(buff[c]);
				}
			}
			byte[] buff = read(i%10);
			for (int c = 1; c <= i%10; c++) {
				bl.add(buff[c-1]);
			}
			
			return toList(bl);
		}
		
		public String getString() {
			try {
				return new String(readData(), "UTF-8");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			return null;
		}
		
		/**
		 * Sends a message to the client.
		 * @param key description of a statement in a language object.
		 * @param val Some other string. Could for example be a number stored in the server.
		 * @param menu description of some menu.
		 */
		public void sendmsg(String key, String val, String menu) {
			writeData(key.getBytes());
			writeData(val.getBytes());
			writeData(menu.getBytes());
		}
		
	    public void run() {
	    	//String read = null;
	    	LanguageObject lo = null;
	        while (true) {
	        	if (lo == null) {
	        		writeData("Choose language: (1) English (2) Swedish ".getBytes()); // Ask the user to choose a language.
	        		int l = Integer.parseInt(getString());
	        		if (l == 1) {
	        			lo = new LanguageObject(LanguageType.English, 1);
	        			lo.AddStatement("auth", "Please enter your card number:");
	        			lo.AddStatement("welcome", "Welcome to shitty bank.");
	        			lo.AddStatement("money", "You have");
	        			lo.AddStatement("logout", "Haha you can't log out.");
	        			lo.AddStatement("invalid", "Invalid input.");
	        			lo.AddStatement("menu", "Choose an alternative: (1) Show money (2) Log out");
	        			lo.AddStatement("empty", "");
	        			lo.AddStatement("auth2", "Please enter the one time phrase:");
	        			lo.AddStatement("errorcard", "Wrong card number or pass phrase.");
	        			lo.AddStatement("", "");
	        			writeData(lo.Serialize());
	        			// The messages should obviously be modified later.
	        			
	        		} else if (l == 2) {
	        			lo = new LanguageObject(LanguageType.Swedish, 1);
	        			lo.AddStatement("auth", "Vad vänlig och ange ditt kortnummer:");
	        			lo.AddStatement("welcome", "VÃ¤lkommen till shitty bank.");
	        			lo.AddStatement("money", "Du har");
	        			lo.AddStatement("logout", "Haha du kan inte logga ut.");
	        			lo.AddStatement("invalid", "Ogiltig input.");
	        			lo.AddStatement("menu", "VÃ¤lj ett altenativ: (1) Visa kredit (2) Logga ut");
	        			lo.AddStatement("empty", "");
	        			lo.AddStatement("auth2", "Var vänlig och ange ditt engångslösenord:");
	        			lo.AddStatement("errorcard", "Fel kortnummer eller lösenord!");
	        			lo.AddStatement("", "");
	        			writeData(lo.Serialize());
	        		}
	        	} else  {
	        		sendmsg("auth", "", "");
	        		int cardno = Integer.parseInt(getString()); // Get input from client.
	        		sendmsg("auth2", "", "");
	        		int pass = Integer.parseInt(getString()); // Get input from client.
	        		
	        		if (cardno > 99 || cardno < 0) {
	        			sendmsg("errorcard", "", "");
	        			return;
	        		}
	        		
	        		if (codes.get(cardno).peekLast() != pass) {
	        			sendmsg("errorcard", "", "");
	        			return;
	        		}
	        		
	        		codes.get(cardno).removeLast();
	        			
        			sendmsg("welcome", "", "menu");
	        		// This part is the user interface after the language has been chosen.

	        		
	        		// Respond accordingly. This should also be modified later.
	        		
	        	}
	        }
	    }
	}

	public static void main(String[] args) {
		int portNumber = HelperMethods.GetPortFromArgs(args, 0);
		ServerSocket serverSocket = null;
		
		if(portNumber == -1)
			return;
		
		try {
			serverSocket = new ServerSocket(portNumber);
			while(true) {
				Socket clientSocket = serverSocket.accept();
				new Thread(new Client(clientSocket)).start();
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
		    try {  
		        serverSocket.close();
		    } catch(Exception e) {
		        e.printStackTrace();
		    }
		}
		
	}

}
