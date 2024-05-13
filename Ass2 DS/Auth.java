import java.net.*;
import java.util.*;
import java.io.*;
import javax.crypto.spec.*;

// Class to handle authentication of clients
public class Auth extends Thread {
    HashMap<String, String> database; // Database to store username-password pairs
    DataInputStream in;
    DataOutputStream out;
    ServerSocket serverSocket;
    Socket clientSocket;
    ClientDetails tempClient;
    List<ClientDetails> preQueue; // List to hold clients before entering the queue
    Queue<ClientDetails> FIFOQueue; // Queue to manage clients in FIFO order
    boolean started = false; // Flag indicating if the event has started

    // Constructor for Auth with preQueue
    public Auth(Socket aClientSocket, ServerSocket aServerSocket, HashMap<String, String> aDatabase, List<ClientDetails> apreQueue) {
        try {
            System.out.println("Starting Auth Process..");
            database = aDatabase;
            serverSocket = aServerSocket;
            clientSocket = aClientSocket;
            preQueue = apreQueue;
            
            started = false;
            in = new DataInputStream(clientSocket.getInputStream());
            out = new DataOutputStream(clientSocket.getOutputStream());
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    // Constructor for Auth with FIFOQueue
    public Auth(Socket aClientSocket, ServerSocket aServerSocket, HashMap<String, String> aDatabase, Queue<ClientDetails> aFIFOQueue) {
        try {
            System.out.println("Starting Auth Process..");
            database = aDatabase;
            serverSocket = aServerSocket;
            clientSocket = aClientSocket;
            FIFOQueue = aFIFOQueue;
            started = true;
            in = new DataInputStream(clientSocket.getInputStream());
            out = new DataOutputStream(clientSocket.getOutputStream());
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
    
   
    // Method to process the incoming message
    public String processMessage(String msg) {
        System.out.println("Processing Message...");
        String[] msgSegments = msg.split("@");
        String method = msgSegments[0];
        tempClient = new ClientDetails(msgSegments[1], msgSegments[2], clientSocket);
        return method;
    }

    // Method to authenticate the client
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

    // Method to handle the sign-up process
    public String signUp() {
        if (!ClientDatabase.exists(tempClient.getUsername())) {
            database.put(tempClient.getUsername(), tempClient.getPassword());
            ClientDatabase.add(tempClient);
            return ("ADDED TO DATABASE");
        } else {
            return ("SIGN UP FAILED. USERNAME ALREADY EXISTS.");
        }
    }

    // Method to handle the login process
    public String logIn() throws IOException {
        System.out.println("LOGGING " + tempClient.getUsername() + " IN WHILE EVEN STARTED: " + started);
        if (authenticate() && !started) {
            if (tempClient.getStep() == 0) {
                System.out.println("Adding " + tempClient.getUsername() + " to preQ");
                preQueue.add(tempClient);
                ClientDatabase.getClient(tempClient.getUsername()).iterateStep();
            }
            ClientDatabase.updateSocketIfExists(tempClient.getUsername(), clientSocket);
            
            return ("Login successful for: " + tempClient.getUsername());
        } else if (authenticate() && started) {
            if (tempClient.getStep() == 0) {
                System.out.println("Adding " + tempClient.getUsername() + " to Q");
                FIFOQueue.add(tempClient);
                ClientDatabase.getClient(tempClient.getUsername()).iterateStep();
            }
            ClientDatabase.updateSocketIfExists(tempClient.getUsername(), clientSocket);
            return ("Login successful for: " + tempClient.getUsername());
        } else {
            return ("Login failed. One or more of your credentials is incorrect.");
        }
    }

    // Method to get the username of the current client
    public String getUsername() {
        return tempClient.getUsername();
    }

    // Thread's run method to execute authentication process
    public void run() {
        try {
            String msg = in.readUTF();
            String method = processMessage(msg);
            String output = "";

            if (method.equals("SIGNUP"))
                output = signUp();
            else if (method.equals("LOGIN"))
                output = logIn();
            else
                output = ("Invalid method");

            out.writeUTF(output);

        } catch (EOFException e) {
            System.out.println(e.getMessage());
        } catch (IOException e) {
            System.out.println(e.getMessage());
        } finally {
        }
    }
}
