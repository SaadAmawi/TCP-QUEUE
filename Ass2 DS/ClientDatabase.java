import java.net.Socket;
import java.util.HashMap;

public class ClientDatabase {
	static HashMap<String, ClientDetails> clientDb = new HashMap<String, ClientDetails>();

	static void add(ClientDetails client) {
		clientDb.put(client.getUsername(), client);
	}

	static void remove(ClientDetails client) {
		clientDb.remove(client.getUsername());
	}

	static void updateSocket(ClientDetails client, Socket socket){
		clientDb.get(client.getUsername()).setClientSocket(socket);
	}

	static boolean exists(String username) {
		return (clientDb.get(username) != null);
	}

	static void updateSocketIfExists(String username, Socket socket) {
		if (exists(username)) {
			clientDb.get(username).setClientSocket(socket);
		}
	}
}


