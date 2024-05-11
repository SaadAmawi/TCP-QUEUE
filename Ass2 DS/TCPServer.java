import java.net.*;
import java.util.*;
import java.io.*;
import java.util.concurrent.*;

public class TCPServer {


    public static void main(String[] args) throws IOException {
        ArrayList<ClientDetails> preQueue = new ArrayList<>();
        List<ClientDetails> preQueueSync = Collections.synchronizedList(preQueue);
        Queue<ClientDetails> FIFOQueue = new ConcurrentLinkedQueue<ClientDetails>();
        // Queue<ClientDetails> syncFIFOQueue = new ConcurrentLinkedQueue<>();
        long startTime = System.currentTimeMillis() + (2*30*1000);
        int serverPort = 6789;
        Scheduler scheduler = new Scheduler(startTime, preQueueSync, FIFOQueue);
        boolean started=false;
        scheduler.start();

        try (ServerSocket listenSocket = new ServerSocket(serverPort)) {
            HashMap<String, String> passDb = new HashMap<>();
            System.out.println("Server is running...");

            while (true) {
                started = System.currentTimeMillis() >= startTime;
                Socket clientSocket = listenSocket.accept();
                if(!started){
                    //run regular routine
                System.out.println("NEW AUTHENTICATION FROM NOT STARTED");
                Auth a = new Auth(clientSocket, listenSocket, passDb, preQueueSync);
                a.start();}
                else if(started){
                    //run started routine
                    System.out.println("NEW AUTHENTICATION FROM STARTED");
                Auth a = new Auth(clientSocket,listenSocket, passDb,FIFOQueue);
                a.start();
                    
                }
                
            }
        } catch (IOException e) {
            System.out.println("Exception: " + e.getMessage());
        }
    }
}

    class Scheduler extends Thread {
        private long startTime;
        private List<ClientDetails> preQueue;
        private Queue<ClientDetails> FIFOQueue;

        public Scheduler(long startTime, List<ClientDetails> preQueue, Queue<ClientDetails> FIFOQueue) {
            this.startTime = startTime;
            this.preQueue = preQueue;
            this.FIFOQueue = FIFOQueue;
        }

        public void run() {
            try {
                while (System.currentTimeMillis() < startTime) {
                    // Wait until it's time to start the event
                    Thread.sleep(startTime - System.currentTimeMillis());
                }
                
                System.out.println("Event starting...");
            for (ClientDetails client : preQueue) {
               	System.out.println("client in PreQ: " + client.getUsername());
               }
                QueueManager queueManager = new QueueManager(preQueue, FIFOQueue);
               for (ClientDetails client : FIFOQueue) {
               	System.out.println("client in FIFO: " + client.getUsername());
               }

                while (true) {
                    if (!FIFOQueue.isEmpty()) {
                    Event e1 = new Event(FIFOQueue.remove());
                    e1.join();
                    if (!FIFOQueue.isEmpty()) {
                        Event e2 = new Event(FIFOQueue.remove());
                        e2.join();
                    }

                    }
                    
                }
            } catch (InterruptedException e) {
                System.out.println("opkpokl;" + e.getMessage());
            }
        }
    }