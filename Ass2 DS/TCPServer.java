import java.net.*;
import java.util.*;
import java.io.*;
import java.util.concurrent.*;

public class TCPServer {


    public static void main(String[] args) throws IOException {
        
        ArrayList<ClientDetails> preQueue = new ArrayList<>();
        List<ClientDetails> preQueueSync = Collections.synchronizedList(preQueue);
        Queue<ClientDetails> FIFOQueue = new ConcurrentLinkedQueue<ClientDetails>();
        long startTime = System.currentTimeMillis() + (60*1000);
        int serverPort = 6789;
        Auth a=null;
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
                 a = new Auth(clientSocket, listenSocket, passDb, preQueueSync);
                a.start();}
                else if(started){
                    //run started routine
                    System.out.println("NEW AUTHENTICATION FROM STARTED");
                 a = new Auth(clientSocket,listenSocket, passDb,FIFOQueue);
                a.start();
                    
                }
                // LinkedList<ClientDetails> FIFOQueueCopy = new LinkedList<ClientDetails>(FIFOQueue);
                // while(FIFOQueueCopy.size()>=0){
                // a.findUser(FIFOQueueCopy.remove());
                // }
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

        List<String> seats = Collections.synchronizedList(generateSeats());

        public Scheduler(long startTime, List<ClientDetails> preQueue, Queue<ClientDetails> FIFOQueue) {
           // try {
            //     QueueTimer qt = new QueueTimer(FIFOQueue);
            // } catch (IOException e) {
            //     // TODO Auto-generated catch block
            //     e.printStackTrace();
            // }
            this.startTime = startTime;
            this.preQueue = preQueue;
            this.FIFOQueue = FIFOQueue;
        }

            public ArrayList<String> generateSeats() {
        int count;
        ArrayList<String> seats = new ArrayList<String>();

        char[] letters = new char[10];
        for (int i = 0; i < letters.length; i++) {
            letters[i] = (char) ('A' + i);
        }
        int[] nums = new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9};
        for (count = 0; count < letters.length; count++) {
            for (int j = 0; j < nums.length; j++) {
                seats.add(letters[count] + "" + nums[j]);
            }
        }
        return seats;
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
                Iterator<ClientDetails> qI = FIFOQueue.iterator();
                int counter = 0;
               while(qI.hasNext()) {
                ClientDetails client = qI.next();
               	DataOutputStream dos = new DataOutputStream(client.getClientSocket().getOutputStream());
                dos.writeUTF("Current Position in Q: "+counter+" Time Remaining: "+counter *15+" Minutes");
                counter++;
               }

                while (true) {
                    if (!FIFOQueue.isEmpty()) {
                    Event e1 = new Event(FIFOQueue.remove(), seats);
                    if (!FIFOQueue.isEmpty()) {
                        Event e2 = new Event(FIFOQueue.remove(), seats);
                        e2.join();
                    }
                    e1.join();

                    }
                    
                }
            } catch (InterruptedException e) {
                System.out.println("opkpokl;" + e.getMessage());
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

//     class QueueTimer extends Thread {
//         DataOutputStream clientOS = null; 
//         Queue<ClientDetails> FIFOQueue = null;
//         QueueTimer (Queue<ClientDetails> aFIFOQueue) throws IOException {
//             FIFOQueue = aFIFOQueue;
//             start();

//         }
//         public void run() {
//                         while (true) {
//                             Iterator<ClientDetails> queueIterator = FIFOQueue.iterator();
//                             int counter = 0;
//                 while(queueIterator.hasNext()) {
//                     try {
//                         ClientDetails client = queueIterator.next();
//                         clientOS = new DataOutputStream(client.getClientSocket().getOutputStream());
//                         System.out.println(client.getUsername());
//                         clientOS.writeUTF("APPROX " + 15*counter++ + " Minutes Remaining");
//                         Thread.sleep(30*1000);
//                     } catch (IOException | InterruptedException e) {
//                         // TODO Auto-generated catch block
//                         e.printStackTrace();
                
                
//                 }
//             }
//         }
//     }
// }