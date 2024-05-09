import java.net.Socket;

class ClientDetails {
	String username;
	String password;
	Socket clientSocket;
	/*
	step 0: new client
	step 1: entered Q
	step 2-n: event step */
	int eventStep = 0;
	String eventInfo = "";

	boolean isReconnecting = false;



    ClientDetails() {

	}

	ClientDetails(String username, String password,Socket clientSocket) {
		this.username = username;
		this.password = password;
		this.clientSocket = clientSocket;
	}

	void reset() {
		clientSocket = null;
		eventStep = 0;
		eventInfo = "";
		isReconnecting = false;
	}

	boolean isReconnecting() {
		return isReconnecting;
	}
	
	void setRec(boolean isReconnecting) {
		this.isReconnecting = isReconnecting;
	}
    
	void iterateStep() {
		eventStep++;
	}

	void addInfo(String info) {
		eventInfo += "@" + info;
	}

	void setStep(int step) {
		eventStep = step;
	}

	int getStep() {
		return eventStep;
	}

	// void setInfo(String info)


	String getUsername() {
		return username;
	}
	String getPassword() {
		return password;
	}
	Socket getClientSocket() {
		return clientSocket;
	}

	void setUsername(String username) {
		this.username = username;
	}
	void setPassword(String password) {
		this.password = password;
	}
	void setClientSocket(Socket clientSocket) {
		this.clientSocket = clientSocket;
	}
}