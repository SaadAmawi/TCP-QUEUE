import java.util.*;
import java.net.*; import java.io.*;

class QueueManager {
	//arraylist?
	List<ClientDetails> preQueue;
	Queue<ClientDetails> FIFOQueue;
	
	public QueueManager (List<ClientDetails> preQueue, Queue<ClientDetails> FIFOQueue) {
		this.preQueue = preQueue;
		this.FIFOQueue = FIFOQueue;
		addPreQueueToQueue();
	}


	
	private void shuffle() {
		Collections.shuffle(preQueue);
	}

	private void PQ(){
		for(ClientDetails c : preQueue){
			System.out.println(c.getUsername());
		}
	}
	
	private void addPreQueueToQueue() {
		shuffle();
		PQ();
	
		while (!preQueue.isEmpty()) {
			ClientDetails client = preQueue.remove(0); 
			System.out.println("ADDING " + client.getUsername() +" TO QUEUE");
			FIFOQueue.add(client);
		}
	}
}

