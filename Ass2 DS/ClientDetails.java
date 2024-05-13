import java.net.Socket;

// Class to store details of a client
class ClientDetails {
    String username;
    String password;
    Socket clientSocket;
    int eventStep = 0; // Represents the current step of the client in an event
    String eventInfo = ""; // Additional information related to the event
    boolean isReconnecting = false; // Flag indicating whether the client is reconnecting

    // Default constructor
    ClientDetails() {}

    // Constructor with username, password, and clientSocket parameters
    ClientDetails(String username, String password, Socket clientSocket) {
        this.username = username;
        this.password = password;
        this.clientSocket = clientSocket;
    }

    // Method to reset client details
    void reset() {
        clientSocket = null;
        eventStep = 0;
        eventInfo = "";
        isReconnecting = false;
    }

    // Method to check if the client is reconnecting
    boolean isReconnecting() {
        return isReconnecting;
    }

    // Method to set the isReconnecting flag
    void setRec(boolean isReconnecting) {
        this.isReconnecting = isReconnecting;
    }

    // Method to increment the eventStep
    void iterateStep() {
        eventStep++;
    }

    // Method to add information to eventInfo
    void addInfo(String info) {
        eventInfo += "@" + info;
    }

    // Method to set the eventStep
    void setStep(int step) {
        eventStep = step;
    }

    // Method to get the eventStep
    int getStep() {
        return eventStep;
    }

    // Getter method for username
    String getUsername() {
        return username;
    }

    // Getter method for password
    String getPassword() {
        return password;
    }

    // Getter method for clientSocket
    Socket getClientSocket() {
        return clientSocket;
    }

    // Setter method for username
    void setUsername(String username) {
        this.username = username;
    }

    // Setter method for password
    void setPassword(String password) {
        this.password = password;
    }

    // Setter method for clientSocket
    void setClientSocket(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }
}
