import java.net.*;
import java.io.*;
import java.util.*;
import java.util.concurrent.*;
public class Event extends Thread {
    DataInputStream in;
    DataOutputStream out;
    long endTime = System.currentTimeMillis() + (3 * 60 * 1000);
    long stepTime = System.currentTimeMillis() + (3 * 60 * 1000);
    long eventEndTime = System.currentTimeMillis() + (1 * 7 * 1000);
    ClientDetails client;
    List<String> seats = null;
    String ticketClass = null;
    String seatChoice = null;

    // Constructor to initialize the event with a client
    public Event(ClientDetails aClient,List<String>seats) {
        try {
            client = aClient;
            this.seats = seats;
            // Initialize input and output streams with client's socket
            in = new DataInputStream(client.getClientSocket().getInputStream());
            out = new DataOutputStream(client.getClientSocket().getOutputStream());
            // Start the event as a thread
            this.start();
            System.out.println("EVENT STARTED");
        } catch (IOException e) {
            System.out.println("UH OHHHH" + e.getMessage());
        }
    }
    String RED = "\u001B[31m";
    String GREEN = "\u001B[32m";
    String BLUE = "\u001B[34m";
    String WHITE = "\u001B[37m";
   
   

    void step1() throws IOException, InterruptedException {
        String sets = new String("AVAILABLE SEATS: ");
        for (String c : seats)
            sets += "[" + c + "]" + " ";
        out.writeUTF(sets);
        out.flush();
        client.iterateStep();
        // Thread.sleep(5000);
        step2();
    }

    // Step 2: Ask the client to select a seat
    void step2() throws IOException, InterruptedException {
        out.writeUTF("Please Select a Seat");
        out.flush();
        String reply = in.readUTF();
        if (reply != null && seats.contains(reply)) {
            seats.remove(reply);
            out.writeUTF(GREEN+"Selection Confirmed!");
            out.flush();
            seatChoice = reply;
        }else{
            out.writeUTF(RED+"Selection Not Available, Please Select Another Seat");
            step2();
        }
        client.iterateStep();
        // Thread.sleep(1000);
        step3();
    }

    // Step 3: Ask the client to choose a ticket class
    void step3() throws IOException, InterruptedException {
        out.writeUTF("Choose Ticket Class: Standard(S), First Class(FC), VIP(VIP), Golden Circle(GC)");
        String choice = in.readUTF();
        if (choice.equals("S") || choice.equals("FC") || choice.equals("VIP") || choice.equals("GC")){
            ticketClass = choice;}
            else{
                out.writeUTF(RED+"Please Select One Of The Mentioned Ticket Classes");
                step3();
            }
        out.writeUTF(GREEN+"Selection Confirmed");
        out.flush();
        client.iterateStep();
        Thread.sleep(1000);
        step4();
    }

    // Step 4: Display ticket reservation details and close client socket
    void step4() throws IOException {
       
        out.writeUTF(BLUE+"\n\n---------Ticket Reservation Completed Successfully--------- \n--------------------------Details-------------------------- \n Seat: " + seatChoice + "\n Ticket Class: " + ticketClass+"\n-----------------------------------------------------------"+WHITE);
        out.flush();
        client.reset();
        Thread.currentThread().interrupt();
        return;
    }

    // Check the current step and execute corresponding method
    public void checkStep(int currStep) throws IOException, InterruptedException {
        switch (currStep) {
            case 1:
                step1();
                break;
            case 2:
                step2();
                break;
            case 3:
                step3();
                break;
            case 4:
                step4();
                break;
            default:
                out.writeUTF("Unknown step. Starting from step 1.");
                step1(); // Default to step 1 if the step is unknown
                break;
        }
    }

    // Wait for the client to reconnect or until the event ends
    public void waitForClient(boolean dc) throws InterruptedException , InterruptedException {
        while (System.currentTimeMillis() < endTime && dc) {
            Thread.sleep(2000);
            System.out.println("Currently waiting...");
            System.out.println("The client socket is closed: " + client.getClientSocket().isClosed());
            if (!client.getClientSocket().isClosed()) {
                dc = false;
                try {
                    in = new DataInputStream(client.getClientSocket().getInputStream());
                    out = new DataOutputStream(client.getClientSocket().getOutputStream());
                    checkStep(client.getStep());
                    System.out.println(client.getStep());
                } catch (IOException e1) {
                } catch (InterruptedException e) {
                }
            }
        }
    }

    // Thread's run method to execute event steps
    public void run() {
        while(System.currentTimeMillis()<=eventEndTime){
        try {
            int currStep = client.getStep();
            checkStep(currStep);
        } catch (EOFException e) {
        } catch (IOException e) {
            System.out.println("Event Waiting... " + e.getMessage());
            try {
                waitForClient(true);
                client.getClientSocket().close();
            } catch (IOException e1) {
                e1.printStackTrace();
            } catch (InterruptedException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            try {
                client.getClientSocket().close();
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }
    }
   
    try{
        out.writeUTF("EVENT IS OVER...");
        
    }finally{
        try {
            client.getClientSocket().close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        Thread.currentThread().interrupt();
        return;
    }
}

}
