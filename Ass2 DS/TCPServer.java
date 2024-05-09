import java.net.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;
import java.io.*;

public class TCPServer {

    public static long getStartTime(){
        return System.currentTimeMillis()+1000;
    }
    public static void main(String[] args) throws InterruptedException {
        ArrayList<ClientDetails> preQueue = new ArrayList<>();
        Queue<ClientDetails> FIFOQueue = new LinkedList<>();
        QueueManager queueManager;
        long startTime = System.currentTimeMillis()+10000;

		
        
        try {
            
          
            HashMap<String, String> passDb = new HashMap<>();
            int serverPort = 6789;
            ServerSocket listenSocket = new ServerSocket(serverPort);
            System.out.println("Server is running...");
            int loopCounter = 0;
            while (true) {
                System.out.println("Current Loop: " + loopCounter);
                loopCounter++;
                Socket clientSocket = (listenSocket.accept());
                Auth a = new Auth(clientSocket, listenSocket, passDb);
                a.join();
                ClientDetails client = ClientDatabase.getClient(a.getUsername());
                System.out.println("Current client: " + client.getUsername());
                System.out.println("Current socket: " + client.getClientSocket());
                
                if (a.authenticate() && client.getStep() == 0) {
                    preQueue.add(client);
                    // Thread.sleep(3000);
                }
                if(System.currentTimeMillis()>=startTime){
                    System.out.println("EVENT STARTING IN"+(startTime-System.currentTimeMillis()));
                    queueManager = new QueueManager(preQueue, FIFOQueue);
                    while(!FIFOQueue.isEmpty()){
                    Event e = new Event(FIFOQueue.remove());}
                }
            }
        } catch (IOException e) {
            System.out.println("readline: " + e.getMessage());
        }
    }
}
