import java.util.*;
import java.net.*; import java.io.*;

class QueueManager {
	//arraylist?
	ArrayList<ClientDetails> preQueue;
	Queue<ClientDetails> FIFOQueue;
	
	public QueueManager (ArrayList<ClientDetails> preQueue, Queue<ClientDetails> FIFOQueue) {
		this.preQueue = preQueue;
		this.FIFOQueue = FIFOQueue;
		addPreQueueToQueue();
	}


	
	private void shuffle() {
		Collections.shuffle(preQueue);
	}

	private void addPreQueueToQueue() {
		shuffle();
		for (int i = 0; i < preQueue.size(); i++) {
			FIFOQueue.add(preQueue.remove(i));
		}

	}
}

