package NodeMCU;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;

public class SendRequest {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		//System.out.println(excutePost("http://192.168.43.77/on", ""));
		
		ReturnRequest("Hello!");
	}
	
	// from http://stackoverflow.com/a/1359700/1275924
	public static String excutePost(String targetURL, String urlParameters) {
		  HttpURLConnection connection = null;  
		  try {
		    //Create connection
		    URL url = new URL(targetURL);
		    connection = (HttpURLConnection)url.openConnection();
		    connection.setRequestMethod("POST");
		    connection.setRequestProperty("Content-Type", 
		        "application/x-www-form-urlencoded");

		    connection.setRequestProperty("Content-Length", 
		        Integer.toString(urlParameters.getBytes().length));
		    connection.setRequestProperty("Content-Language", "en-US");  

		    connection.setUseCaches(false);
		    connection.setDoOutput(true);

		    //Send request
		    DataOutputStream wr = new DataOutputStream (
		        connection.getOutputStream());
		    wr.writeBytes(urlParameters);
		    wr.close();

		    //Get Response  
		    InputStream is = connection.getInputStream();
		    BufferedReader rd = new BufferedReader(new InputStreamReader(is));
		    StringBuilder response = new StringBuilder(); // or StringBuffer if not Java 5+ 
		    String line;
		    while((line = rd.readLine()) != null) {
		      response.append(line);
		      response.append('\r');
		    }
		    rd.close();
		    return response.toString();
		  } catch (Exception e) {
		    e.printStackTrace();
		    return null;
		  } finally {
		    if(connection != null) {
		      connection.disconnect(); 
		    }
		  }
		}
	
	public static void ReturnRequest (String message) {

		try {
			ServerSocket ss = new ServerSocket(4711);
			while(true) {
				
				Socket client = ss.accept();
				
				BufferedReader bf = new BufferedReader(new InputStreamReader(client.getInputStream()));
				
				PrintWriter response = new PrintWriter(client.getOutputStream());
				
				response.print("HTTP/1.1 200 \r\n"); 
				response.print("Content-Type: text/plain\r\n");
				response.print("Connection: close\r\n");
				response.print("\r\n");
				response.print(message);
				response.print("\r\n");
				
		        response.close();
		        bf.close();
		        client.close();
				
			}
		
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
