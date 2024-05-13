import java.net.*;
import java.io.*;
import javax.crypto.*;
import javax.crypto.spec.*;
import java.security.spec.*;
import java.security.*;
import java.util.Scanner;

public class TCPClient {
    public static void main(String[] args) {
        // Initialize variables
        //args[0]: ip address of loadbalancer
        //args[1]: port of loadbalancer
        Socket socket = null;
        Socket lbSocket = null;
        Scanner scanner = new Scanner(System.in);
        int serverPort = 0;
        int lbPort = 22222;
        String lbAddress = args[0];
        String hostname = "";
        boolean receivedFromLB = false;
        boolean strongPass = false;
        String password=null;

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

            // Prompt user for authentication method
            System.out.println("PLEASE CHOOSE SIGNUP OR LOGIN");
            String method = scanner.nextLine();
            System.out.println("Please Enter Username and Password: ");
            System.out.print("Username: ");
            String username = scanner.nextLine();
            while(!strongPass){
            System.out.print("Password: ");
             password = scanner.nextLine();
            if(password.matches("(?=.*\\d)(?=.*\\p{Lu}).*")&& password.length()>=8){
                strongPass=true;
            }
            }


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
                        if(scanner.hasNextLine()){
                        String seatChoice = scanner.nextLine();
                        out.writeUTF(seatChoice);
                        out.flush();}
                        if(serverMessage.equals("Selection Confirmed!"))
                        System.out.println("Seat Selection Confirmed!");
                        
                        
                        
                    } else if (serverMessage.equals("Choose Ticket Class: Standard(S), First Class(FC), VIP(VIP), Golden Circle(GC)")) {
                        System.out.print(YELLOW+"\n\n-----Choose Ticket Class: Standard(S), First Class(FC), VIP(VIP), Golden Circle(GC)-----\nCHOICE: ");
                        if(scanner.hasNextLine()){
                        String ticketClass = scanner.nextLine();
                        out.writeUTF(ticketClass);
                        out.flush();}
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
            System.out.println("EOF error: " + e.getMessage());
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
