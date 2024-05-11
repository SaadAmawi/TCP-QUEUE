import java.net.Socket;
import java.util.*;
public class ClientDatabase {
	static Map<String, ClientDetails> clientDb = new HashMap<String, ClientDetails>();
	static Map<String, ClientDetails> syncClientDb = Collections.synchronizedMap(clientDb);

	static void add(ClientDetails client) {
		syncClientDb.put(client.getUsername(), client);
	}

	static void remove(ClientDetails client) {
		syncClientDb.remove(client.getUsername());
	}

	static void updateSocket(ClientDetails client, Socket socket){
		syncClientDb.get(client.getUsername()).setClientSocket(socket);
	}

	static boolean exists(String username) {
		return (syncClientDb.get(username) != null);
	}

	static void updateSocketIfExists(String username, Socket socket) {
		if (exists(username)) {
			syncClientDb.get(username).setClientSocket(socket);
		}
	}

	static void updateSocketIfExists(ClientDetails client, Socket socket) {
		if (exists(client.getUsername())) {
			syncClientDb.get(client.getUsername()).setClientSocket(socket);
		}
	}

	static ClientDetails getClient(String username) {
		return syncClientDb.get(username);
	}
}


