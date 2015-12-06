package ATM;

/**
 * ClientInterface sets up a connections to a server and sends or receives data according to the system agreed upon
 * (see separate description).
 * Important: close before disconnecting.
 * @author Abdallah Hassan
 */

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;

import ATM.LanguageObject;


public class ClientInterface {
	Socket socket;
	OutputStream out;
	InputStream in;
	
	/**
	 * Create a new connection to a server.
	 * @param hostname Host name of the server.
	 * @param portnumber Port number of the server.
	 */
	public ClientInterface(String hostname, int portnumber) {
		try {
			socket = new Socket(hostname, portnumber);
			this.out = socket.getOutputStream();
			this.in = socket.getInputStream();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	/**
	 * Writes a byte array to the server max ten bytes at a time. The first two bytes specify how many bytes will
	 * be sent next.
	 * @param data The data to be sent.
	 */
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
	
	/**
	 * Reads data max ten bytes at a time. The first two bytes MUST SPECIFY how many bytes that will be read.
	 * @return All the bytes in one list.
	 */
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
	
	public void showmsg(LanguageObject lo) {
		String key = getString();
		String val = getString();
		String menu = getString();
		System.out.println(lo.GetStatement(key) + " " + val + " " + lo.GetStatement(menu));
	}
	
	/**
	 * Close this client.
	 */
	public void close() {
		try {
			socket.close();
			in.close();
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
