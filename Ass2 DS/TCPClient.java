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
            while(true){
  
              

              System.out.println(in.readUTF());}
            }
            


  
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



