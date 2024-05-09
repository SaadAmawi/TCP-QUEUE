import java.net.*;
import java.util.HashMap;
import java.io.*;

public class Auth extends Thread{
    HashMap<String,String> database;
    DataInputStream in;
    DataOutputStream out;
    DataOutputStream outToServer;
    ServerSocket serverSocket;
    String output ;
	boolean authenticated=false;
    ClientDetails tempClient;
    Socket clientSocket;
    public Auth(Socket aClientSocket, ServerSocket aServerSocket, HashMap<String,String> aDatabase){
        try{
        System.out.println("Starting Auth Process..");
        database = aDatabase;
        serverSocket = aServerSocket;
        clientSocket = aClientSocket;
        in = new DataInputStream(clientSocket.getInputStream());
        out = new DataOutputStream(clientSocket.getOutputStream());
        this.start();
        }catch(IOException e ){System.out.println(e.getMessage());}

    }
    
    public String processMessage(String msg) {
        System.out.println("Processing Message...");
        String [] msgSegments = msg.split("@");
        String method = msgSegments[0];
        tempClient = new ClientDetails(msgSegments[1], msgSegments[2], clientSocket);
        return method;
    }

    public boolean authenticate() {
        System.out.println("Authenticating...");
        if (database.containsKey(tempClient.getUsername())) {
            String storedPassword = database.get(tempClient.getUsername());
            if (storedPassword.equals(tempClient.getPassword())) {
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
        if(authenticate()) {
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
        String output;
        
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