import java.net.*;
import java.io.*;
import javax.crypto.*;
import javax.crypto.spec.*;
import java.security.spec.*;
import java.security.*;
import java.util.Scanner;

public class ClientAuto {
    public static void main(String[] args) {
        
        // Initialize variables
        //Auto pass 0-3 on signup
        //Auto pass 0-5 on login
        //args[0]: ip address of loadbalancer
        //args[1]: method
        //args[2]: username
        //args[3]: password
        //args[4]: Seat Choice
        //args[5]: Ticket Class Choice

        Socket socket = null;
        Socket lbSocket = null;
        Scanner scanner = new Scanner(System.in);
        int serverPort = 0;
        int lbPort = 22222;
        String lbAddress = args[0];
        String hostname = "";
        boolean receivedFromLB = false;
        boolean strongPass = false;
        long  startTime = System.currentTimeMillis();
        long eventEndTime;
        long signupEndTime;
        for (String s : args) {
            System.out.println(s);
        }
        

        try {
            //Get server details from LB
            lbSocket = new Socket(lbAddress, lbPort);
            DataInputStream inLb = new DataInputStream(lbSocket.getInputStream());
            
            String serverDetails[] = (inLb.readUTF()).split("@");
            hostname = serverDetails[0];
            serverPort = Integer.parseInt(serverDetails[1]);
            lbSocket.close();

            // Establish connection to server
            socket = new Socket(hostname, serverPort);
            DataInputStream in = new DataInputStream(socket.getInputStream());
            DataOutputStream out = new DataOutputStream(socket.getOutputStream());
            String RED = "\u001B[31m";
            String GREEN = "\u001B[32m";
            String BLUE = "\u001B[34m";
            String WHITE = "\u001B[37m";

            // Prompt user for authentication method
            System.out.println("PLEASE CHOOSE SIGNUP OR LOGIN");
            String method = args[1];
            System.out.println("Please Enter Username and Password: ");
            System.out.print("Username: ");
            String username = args[2];
            System.out.print("Password: ");
            String password = args[3];



            // Encrypt password using AES encryption
            SecretKeySpec secretKeySpec = SecurityUtil.generateAESKey();
            Cipher cipher = Cipher.getInstance("AES");
            String encryptedPassword = SecurityUtil.encrypt(password, cipher, secretKeySpec);
            String YELLOW = "\u001B[33m";

            // Send authentication data to server based on whether the method was signup or login
            if (method.equals("SIGNUP")) {
                out.writeUTF("SIGNUP@" + username + "@" + encryptedPassword);
                System.out.println(in.readUTF());
            } else if (method.equals("LOGIN")) {
                out.writeUTF("LOGIN@" + username + "@" + encryptedPassword);
                String reply = in.readUTF();
                System.out.println(reply);

                // Handle continuous server messages and respond based on message received 
                while (true) {
                    String serverMessage = in.readUTF();
                    if (serverMessage.equals("Please Select a Seat")) {
                        System.out.print(YELLOW+"\n\n-----Select a seat!-----\n CHOICE: ");
                        String seatChoice = args[4];

                        out.writeUTF(seatChoice);
                        out.flush();
                        if(serverMessage.equals("Selection Confirmed!"))
                        System.out.println("Seat Selection Confirmed!");
                        
                        
                        
                    } else if (serverMessage.equals("Choose Ticket Class: Standard(S), First Class(FC), VIP(VIP), Golden Circle(GC)")) {
                        System.out.print(YELLOW+"\n\n-----Choose Ticket Class: Standard(S), First Class(FC), VIP(VIP), Golden Circle(GC)-----\nCHOICE: ");
                        String ticketClass = args[5];

                        out.writeUTF(ticketClass);
                        out.flush();
                        if(serverMessage.equals("Selection Confirmed"))
                        System.out.println("Ticket Class Selection Confirmed!");
                      
                       
                    } else {
                        System.out.println(serverMessage);
                    }
                }
            }
        } catch (UnknownHostException e) {
            System.out.println("Socket error: " + e.getMessage());
        } catch (EOFException e) {
           
        } catch (IOException e) {
            System.out.println("IO error: " + e.getMessage());
        } catch (GeneralSecurityException e) {
            e.printStackTrace();
        } finally {
            // Close resources cleanly
            if (socket != null) {
                try {
                    socket.close();
                } catch (IOException e) {
                    System.out.println("Socket close error: " + e.getMessage());
                }
            }
            scanner.close();
        }
    }
}
