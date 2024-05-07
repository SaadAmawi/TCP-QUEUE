import java.util.*;
import java.net.*; import java.io.*;

class QueueManager {
	//arraylist?
	Queue<ClientDetails> preQueue, FIFOQueue;
	
	public QueueManager (Queue<ClientDetails> preQueue, Queue<ClientDetails> FIFOQueue) {
		this.preQueue = preQueue;
		this.FIFOQueue = FIFOQueue;
		addPreQueueToQueue();
	}


	
	private void shuffle() {
		List<ClientDetails> tempList = new ArrayList<ClientDetails>(preQueue);
		Collections.shuffle(tempList);
		preQueue.clear();
		preQueue.addAll(tempList);

	}

	private void addPreQueueToQueue() {
		shuffle();
		for (int i = 0; i < preQueue.size(); i++) {
			FIFOQueue.add(preQueue.remove());
		}

	}
}

