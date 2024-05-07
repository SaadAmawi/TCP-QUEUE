import java.net.*;
import java.util.ArrayList;
import java.io.*;

public class SendMessagesThread extends Thread {
    MulticastSocket groupSocket;
    InetAddress groupAddress;
    int numMsgs;
    String ID;
    public SendMessagesThread (MulticastSocket groupSocket, InetAddress groupAddress, String ID, int numMsgs) {
        	this.groupSocket = groupSocket;
        	this.groupAddress = groupAddress;
        	this.ID = ID;
        	this.numMsgs = numMsgs;
        	this.start();
     }

    public void run(){
    	try {			              
    		System.out.println("Transmitting new messages to the group: ");
			for ( int i = 1; i <= numMsgs ; i++ ) {
					String m = new String ( "MSG" + ID +" "+ i ); 
					DatagramPacket msgOut = new DatagramPacket(m.getBytes(), m.length(), groupAddress, groupSocket.getLocalPort());
					groupSocket.send(msgOut);	
					delay(1000);
			}
         }catch (EOFException e){System.out.println("EOF:"+e.getMessage());
         } catch(IOException e) {System.out.println("readline:"+e.getMessage());
         } finally{ }
    }
    
    public void delay(int milliseconds){
    	try {
    		Thread.sleep(milliseconds);        
        }catch (InterruptedException ie) { System.out.println(ie.getMessage()); }
    }
}
