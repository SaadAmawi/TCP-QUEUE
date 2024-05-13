import java.net.Socket;
import java.util.*;

// Class to manage client details in a database
public class ClientDatabase {
    // HashMap to store client details with username as key
    static Map<String, ClientDetails> clientDb = new HashMap<String, ClientDetails>();
    // Synchronized version of clientDb to handle concurrent access
    static Map<String, ClientDetails> syncClientDb = Collections.synchronizedMap(clientDb);

    // Method to add a client to the database
    static void add(ClientDetails client) {
        syncClientDb.put(client.getUsername(), client);
    }

    // Method to remove a client from the database
    static void remove(ClientDetails client) {
        syncClientDb.remove(client.getUsername());
    }

    // Method to update socket for a client in the database
    static void updateSocket(ClientDetails client, Socket socket) {
        syncClientDb.get(client.getUsername()).setClientSocket(socket);
    }

    // Method to check if a client exists in the database
    static boolean exists(String username) {
        return (syncClientDb.get(username) != null);
    }

    // Method to update socket for a client if it exists in the database
    static void updateSocketIfExists(String username, Socket socket) {
        if (exists(username)) {
            syncClientDb.get(username).setClientSocket(socket);
        }
    }

    // Method to update socket for a client if it exists in the database (overloaded method)
    static void updateSocketIfExists(ClientDetails client, Socket socket) {
        if (exists(client.getUsername())) {
            syncClientDb.get(client.getUsername()).setClientSocket(socket);
        }
    }

    // Method to get client details by username
    static ClientDetails getClient(String username) {
        return syncClientDb.get(username);
    }
}
