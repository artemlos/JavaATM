package ATM;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;


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
     * Writes a byte array to the server ten bytes at a time. The first byte specifies how many tens of bytes will
     * be sent.
     * @param data The data to be sent.
     */
    public void writeData(byte[] data) {
        int tens = ((data.length + 1)/ 10) + 1;
        byte[] cbuf = new byte[10];
        cbuf[0] = (byte)tens;
        for (int i = 0; i < tens; i++) {
            if (i == 0) {
                for (int x = 1; x < 10; x++) {
                    cbuf[x] = data[x];
                }
                writeTen(cbuf);
            } else {
                for (int x = 0; x < 10; x++) {
                    cbuf[x] = data[i+x];
                }
                writeTen(cbuf);
            }
        }
    }
    
    private void writeTen(byte[] data) {
        try {
            out.write(data);
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    private byte[] readTen() {
        byte[] cbuf = new byte[10];
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
     * Reads data ten bytes at a time. first byte MUST SPECIFY how many tens of bytes there are.
     * @return All the bytes in one list.
     */
    public byte[] readData() {
        ArrayList<Byte> bl = new ArrayList<Byte>();
        byte[] ten = readTen();
        int i = ten[0];
        
        for (int n = 0; n < i; n++) {
            if (bl.size() == 0) {
                for (int m = 1; m < 10; m++) {
                    bl.add(ten[m]);
                }
            } else {
                for (int m = 0; m < 10; m++) {
                    bl.add(ten[m]);
                }
            }
            ten = readTen();
        }
        
        return toList(bl);
    }
    
    /**
     * Close this client.
     */
    public void close() {
        //byte[] kill = {0};
        //writeData(kill);
        try {
            socket.close();
            in.close();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}