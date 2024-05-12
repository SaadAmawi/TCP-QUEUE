import java.util.*;
import java.net.*;
import java.io.*;

// Class to manage the queue of clients
class QueueManager {
    // List to hold clients before they enter the queue
    List<ClientDetails> preQueue;
    // Queue to manage clients in FIFO order
    Queue<ClientDetails> FIFOQueue;

    // Constructor to initialize preQueue and FIFOQueue and add clients from preQueue to FIFOQueue
    public QueueManager(List<ClientDetails> preQueue, Queue<ClientDetails> FIFOQueue) {
        this.preQueue = preQueue;
        this.FIFOQueue = FIFOQueue;
        addPreQueueToQueue();
    }

    // Method to shuffle the preQueue list
    private void shuffle() {
        Collections.shuffle(preQueue);
    }

    // Method to print the clients in preQueue (for debugging purposes)
    private void printPreQueue() {
        for (ClientDetails c : preQueue) {
            System.out.println(c.getUsername());
        }
    }

    // Method to add clients from preQueue to FIFOQueue
    private void addPreQueueToQueue() {
        // Shuffle the preQueue list to randomize the order
        shuffle();
        // Print the preQueue (for debugging purposes)
        printPreQueue();

        // Transfer clients from preQueue to FIFOQueue
        while (!preQueue.isEmpty()) {
            // Remove the first client from preQueue
            ClientDetails client = preQueue.remove(0);
            // Print the client being added to the queue (for debugging purposes)
            System.out.println("ADDING " + client.getUsername() + " TO QUEUE");
            // Add the client to FIFOQueue
            FIFOQueue.add(client);
            // try {
			// 	DataOutputStream dos = new DataOutputStream(client.getClientSocket().getOutputStream());
            //     dos.writeUTF("Estimated waiting time: " );
			// } catch (IOException e) {
			// 	// TODO Auto-generated catch block
			// 	e.printStackTrace();
			// }
        }
    }
}
