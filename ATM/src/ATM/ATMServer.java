package ATM;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;

import ATM.LanguageObject;
import ATM.LanguageObject.LanguageType;


public class ATMServer {
	
	private static class Client implements Runnable {
		
		OutputStream out; 
		InputStream in;
		
		public Client(Socket clientSocket) {
			try {
				this.out = clientSocket.getOutputStream();
				this.in = clientSocket.getInputStream();
			} catch (IOException e) {
				e.printStackTrace();
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
			i |= init[1];
			i <<= 8;
			i |= init[0];
			
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
		
	    public void run() {
	    	String read = null;
	    	LanguageObject lo = null;
	        while (true) {
	        	if (lo == null) {
	        		writeData("Choose language: (1) English (2) Swedish ".getBytes());
	        		int l = Integer.parseInt(getString());
	        		if (l == 1) {
	        			lo = new LanguageObject(LanguageType.English, 1);
	        			lo.AddStatement("welcome", "Welcome to shitty bank. Choose an alternative: (1) Show money (2) Log out ");
	        			lo.AddStatement("money", "Haha you have no money. Choose an alternative: (1) Show money (2) Log out ");
	        			lo.AddStatement("logout", "Haha you can't log out. Choose an alternative: (1) Show money (2) Log out ");
	        			lo.AddStatement("invalid", "Invalid input. Choose an alternative: (1) Show money (2) Log out ");
	        			writeData(lo.Serialize());
	        		} else if (l == 2) {
	        			lo = new LanguageObject(LanguageType.Swedish, 1);
	        			lo.AddStatement("welcome", "Välkommen till shitty bank. Välj ett alternativ: (1) Visa kredit (2) Logga ut ");
	        			lo.AddStatement("money", "Haha du har inga pengar. Välj ett alternativ: (1) Visa kredit (2) Logga ut ");
	        			lo.AddStatement("logout", "Haha du kan inte logga ut. Välj ett alternativ: (1) Visa kredit (2) Logga ut ");
	        			lo.AddStatement("invalid", "Ogilitig input. Välj ett alternativ: (1) Visa kredit (2) Logga ut ");
	        			writeData(lo.Serialize());
	        		}
	        	} else  {
	        		// This part is the user interface after the language has been chosen.
	        		int l = Integer.parseInt(getString());
	        		if (l == 1) {
	        			writeData("money".getBytes());
	        		} else if (l == 2) {
	        			writeData("logout".getBytes());
	        		} else {
	        			writeData("invalid".getBytes());
	        		}
	        	}
	        }
	    }
	}

	public static void main(String[] args) {
		int portNumber = Integer.parseInt(args[0]);
		ServerSocket serverSocket = null;
		
		
		
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
