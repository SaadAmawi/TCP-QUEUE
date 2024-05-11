import java.net.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;
import java.io.*;

public class TCPServer {

    public static long getStartTime(){
        return System.currentTimeMillis() + 15000;
    }

    public static void main(String[] args) throws IOException {
        ArrayList<ClientDetails> preQueue = new ArrayList<>();
        Queue<ClientDetails> FIFOQueue = new LinkedList<>();
        long startTime = System.currentTimeMillis() + 10000;
        int serverPort = 6789;
        Scheduler scheduler = new Scheduler(startTime, preQueue, FIFOQueue);

        scheduler.start();

        try (ServerSocket listenSocket = new ServerSocket(serverPort)) {
            HashMap<String, String> passDb = new HashMap<>();
            System.out.println("Server is running...");

            while (true) {
                Socket clientSocket = listenSocket.accept();
                Auth a = new Auth(clientSocket, listenSocket, passDb, preQueue);
                a.start();
            }
        } catch (IOException e) {
            System.out.println("Exception: " + e.getMessage());
        }
    }
}

    class Scheduler extends Thread {
        private long startTime;
        private ArrayList<ClientDetails> preQueue;
        private Queue<ClientDetails> FIFOQueue;

        public Scheduler(long startTime, ArrayList<ClientDetails> preQueue, Queue<ClientDetails> FIFOQueue) {
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
                QueueManager queueManager = new QueueManager(preQueue, FIFOQueue);
                while (!FIFOQueue.isEmpty()) {
                    Event e = new Event(FIFOQueue.remove());
                }
            } catch (InterruptedException e) {
                System.out.println("opkpokl;" + e.getMessage());
            }
        }
    }