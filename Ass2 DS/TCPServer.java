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
        Queue<ClientDetails> preQueue = new LinkedList<>();
        Queue<ClientDetails> FIFOQueue = new LinkedList<>();
        QueueManager queueManager;
        long startTime = System.currentTimeMillis()+1000;

		
        
        try {
            
          
            HashMap<String, String> passDb = new HashMap<>();
            int serverPort = 6789;
            ServerSocket listenSocket = new ServerSocket(serverPort);
            System.out.println("Server is running...");
            while (true) {
                ClientDetails client = new ClientDetails();
                client.setClientSocket(listenSocket.accept());
                Auth a = new Auth(client, listenSocket, passDb);
                a.join();
                System.out.println(client.isAuthenticated());
                
                if (client.isAuthenticated()) {
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
