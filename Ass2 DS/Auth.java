import java.net.*;
import java.util.*;
import java.io.*;
import java.util.ArrayList;

public class Auth extends Thread{
    HashMap<String,String> database;
    DataInputStream in;
    DataOutputStream out;
    DataOutputStream outToServer;
    ServerSocket serverSocket;
    String output ;
	boolean authenticated=false;
    ClientDetails tempClient;
    ArrayList<ClientDetails> preQueue;
    Queue<ClientDetails> FIFOQueue;
    List<ClientDetails> preQ;
    Socket clientSocket;
    boolean started = false;
    public Auth(Socket aClientSocket, ServerSocket aServerSocket, HashMap<String,String> aDatabase, List<ClientDetails> apreQueue){
        try{
        System.out.println("Starting Auth Process..");
        database = aDatabase;
        serverSocket = aServerSocket;
        clientSocket = aClientSocket;
        preQ = apreQueue;
        started = false;
        in = new DataInputStream(clientSocket.getInputStream());
        out = new DataOutputStream(clientSocket.getOutputStream());
        }catch(IOException e ){System.out.println(e.getMessage());
        }

    }

    public Auth(Socket aClientSocket, ServerSocket aServerSocket, HashMap<String,String> aDatabase, Queue<ClientDetails> aFIFOQueue){
        try{
        System.out.println("Starting Auth Process..");
        database = aDatabase;
        serverSocket = aServerSocket;
        clientSocket = aClientSocket;
        FIFOQueue = aFIFOQueue;
        started = true;
        in = new DataInputStream(clientSocket.getInputStream());
        out = new DataOutputStream(clientSocket.getOutputStream());
        }catch(IOException e ){System.out.println(e.getMessage());
        }

    }
    
    public String processMessage(String msg) {
        System.out.println("Processing Message...");
        String [] msgSegments = msg.split("@");
        String method = msgSegments[0];
        tempClient = new ClientDetails(msgSegments[1], msgSegments[2], clientSocket);
        return method;
    }

    public boolean authenticate() {
        System.out.println("Authenticating..." + tempClient.getUsername());
        if (database.containsKey(tempClient.getUsername())) {
            String storedPassword = database.get(tempClient.getUsername());
            if (storedPassword.equals(tempClient.getPassword())) {
            	tempClient = ClientDatabase.getClient(tempClient.getUsername());
                return true;
            }
        }
        return false;
    }
    
    public String signUp() {
        if (!ClientDatabase.exists(tempClient.getUsername())) {
            database.put(tempClient.getUsername(), tempClient.getPassword());
            ClientDatabase.add(tempClient);
            return ("ADDED TO DATABASE");
            
        }
        else {
            return ("SIGN UP FAILED. USERNAME ALREADY EXISTS.");
        }
    }

    public String logIn() {
        System.out.println("LOGGING " + tempClient.getUsername() + " IN WHILE EVEN STARTED: " + started);
        if(authenticate() && !started) {
            if (tempClient.getStep() == 0) {
            		System.out.println("Adding " + tempClient.getUsername() + " to preQ");
                    preQ.add(tempClient);
                    ClientDatabase.getClient(tempClient.getUsername()).iterateStep();
             }
            ClientDatabase.updateSocketIfExists(tempClient.getUsername(), clientSocket);
            return ("Login successful for: " + tempClient.getUsername());
        }
        else if(authenticate() && started) {
            if (tempClient.getStep() == 0) {
            		System.out.println("Adding " + tempClient.getUsername() + " to Q");
                    FIFOQueue.add(tempClient);
                    ClientDatabase.getClient(tempClient.getUsername()).iterateStep();
             }
            ClientDatabase.updateSocketIfExists(tempClient.getUsername(), clientSocket);
            return ("Login successful for: " + tempClient.getUsername());
        }
        else {
            return ("Login failed. One or more of your credentials is incorrect.");
        }
    }

    public String getUsername() {
        return tempClient.getUsername();
    }



    public void run(){
        try{
        String msg = in.readUTF();
        String method = processMessage(msg);
        String output = "";
        
        if(method.equals("SIGNUP"))
            output = signUp();
        else if (method.equals("LOGIN")) 
            output = logIn();
        else
            output=("Invalid method");

        out.writeUTF(output);
       
            
        }catch(EOFException e){System.out.println(e.getMessage());
        }catch(IOException e){ System.out.println(e.getMessage());
        }finally{
     
}
}
}