import java.net.*;
import java.io.*;
import javax.crypto.*;
import javax.crypto.spec.*;
import java.security.spec.*;
import java.security.*;
import java.util.Scanner;


public class TCPClient {
    public static void main(String[] args) throws InterruptedException {
        
        Socket socket = null;
        Scanner s = new Scanner(System.in);
        // long start = TCPServer.getStartTime();

        try{
            int serverPort = 6789;
            String hostname = args[0];
            socket = new Socket(hostname, serverPort);
            DataInputStream in = new DataInputStream(socket.getInputStream());
            DataOutputStream out = new DataOutputStream(socket.getOutputStream());
            // String username = args[1];
            // String password = args[2];
            // String method = args[3];
            // String msg = args[4];
            
            // while(true){
            System.out.println("PLEASE CHOOSE SIGNUP OR LOGIN");
            String methods = s.nextLine();
            System.out.println("Please Enter Username and Password: ");
            System.out.print("Username: ");
            String usernames = s.nextLine();
            System.out.print("Password: ");
            String passwords = s.nextLine();
            SecretKeySpec secretKeySpec = SecurityUtil.generateAESKey();
            Cipher cipher = Cipher.getInstance("AES");
            String encryptedPass = SecurityUtil.encrypt(passwords, cipher, secretKeySpec);

            if(methods.equals("SIGNUP")){
            out.writeUTF("SIGNUP"+"@"+ usernames+ "@"+encryptedPass);
            System.out.println(in.readUTF());
            }


            else if(methods.equals("LOGIN")){
            out.writeUTF("LOGIN"+"@"+ usernames+ "@"+encryptedPass);
            String reply= in.readUTF().toString();
            System.out.println(reply);
            while(true) {
                String serverMessage = in.readUTF();  // Read once at the start of the loop
                // System.out.println("Server says: " + serverMessage);
            
                if (serverMessage.equals("Please Select a Seat")) {
                    System.out.print("\n\n-----Select a seat!-----\n CHOICE: ");
                    String seatChoice = s.nextLine();
                    out.writeUTF(seatChoice);
                    out.flush();
                    System.out.println(in.readUTF());  // Confirm seat selection
                } else if (serverMessage.equals("Choose Ticket Class: Standard(S), First Class(FC), VIP(VIP), Golden Circle(GC)")) {
                    System.out.print("\n\n-----Choose Ticket Class: Standard(S), First Class(FC), VIP(VIP), Golden Circle(GC)-----\nCHOICE: ");
                    String levelChoice = s.nextLine();
                    out.writeUTF(levelChoice);
                    out.flush();
                    System.out.println(in.readUTF());  // Confirm ticket class
                } else {
                    System.out.println(serverMessage);  // Handle other or unexpected messages
                }
            }}


  
            s.close();
        // }
         } catch (UnknownHostException e) {
            System.out.println("Error Socket:" + e.getMessage());
        } catch (EOFException e) {
            System.out.println("Error EOF:" + e.getMessage());
        } catch (IOException e) {
            System.out.println("Error readline:" + e.getMessage());
        } catch (GeneralSecurityException e) {
            e.printStackTrace();
        } finally {
            if (socket != null) {
                try {
                    socket.close();
                } catch (IOException e) {
                    System.out.println("Error close:" + e.getMessage());
                }
            }
        }
    }
}



