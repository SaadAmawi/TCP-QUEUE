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
    ClientDetails client;
    Socket clientSocket;
    public Auth(Socket aClientSocket, ServerSocket aServerSocket, HashMap<String,String> aDatabase){
        try{
        database = aDatabase;
        // client = aClient;
        serverSocket = aServerSocket;
        clientSocket = aClientSocket;
        in = new DataInputStream(client.getClientSocket().getInputStream());
        out = new DataOutputStream(client.getClientSocket().getOutputStream());
        this.start();
        // client.authenticate();
        }catch(IOException e ){System.out.println(e.getMessage());}

    }
    
    public String processMessage(String msg) {
        String [] msgSegments = msg.split("@");
        String method = msgSegments[0];
        client.setUsername(msgSegments[1]);
        client.setPassword(msgSegments[2]);
        return method;
    }

    public void authenticate(ClientDetails client) {
        if (database.containsKey(client.getUsername())) {
            String storedPassword = database.get(client.getUsername());
            if (storedPassword.equals(client.getPassword())) {

            }
        }
    }


    public void run(){
        try{
        String msg = in.readUTF();
		// System.out.println("MSG = "+msg);
        String [] arr = msg.split("@");
        String method = arr[0];
		// System.out.println("method = "+method);
        client.setUsername(arr[1]);
		// System.out.println("username = "+username);
        client.setPassword(arr[2]);
		// System.out.println("password = "+password);
		String username = client.getUsername();
        String password = client.getPassword();
        
        //TODO REFACOTOR:
        //String processMessage() returns method DONE
        //if method = signup: signup
        //if method = login: authenticate, login
        //if method = rejoin: authenticate, rejoin
        if(method.equals("SIGNUP")){
            if(!ClientDatabase.exists(username)){
            database.put(username,password);
            ClientDatabase.add(client);
            output=("ADDED TO DATABASE");
            }
            else {
                output=("FAILED. Username is already taken");
            }
        } else if (method.equals("LOGIN")) {
            // Handle login request
			 System.out.println(database);
            if(database.containsKey(username)){
                String storedPassword = database.get(username);
                if (storedPassword.equals(password)) {
                    ClientDatabase.updateSocketIfExists(username, clientSocket);
                    output = ("Login successful for username: " + username);
                    client.authenticate();
                } else {
                    output=("Incorrect password for username: " + username);
                }
            } else {
                output=("Username: " + username + " not found");
            }
        }  
        else if (method.equals("RECONNECT")&&database.containsKey(username)) {
            client.setRec(true);
            output="RECONNECTING...";
            // client.authenticate();
        }
        
        else {
            output=("Invalid method");
        }
        
            
            // String data = in.readUTF();
            out.writeUTF(output);
            
        }catch(EOFException e){System.out.println(e.getMessage());
        }catch(IOException e){ System.out.println(e.getMessage());
        }finally{
     
}
}
}