import java.net.*;
import java.io.*;
 
public class LoadBalancer {
 
   
   
    public static void main(String[] args)
    {
        //args[0]: specifies number of servers to balance the load over.
		//args[1]: ip address of server 1.
		//args[2]: port address of server 1.
		//args[3]: ip address of server 2.
		//args[4]: port address of server 2.
		//...
        ServerSocket LBSocket = null;
       
        int numOfServers = Integer.parseInt(args[0]);
//         int numOfServers = 1;
        try{
			//initializing LoadBalancer socket
			LBSocket = new ServerSocket(22222);

			//creating sockets to communicate with each server
			String servers[] = new String[numOfServers];
			for (int i = 0; i < servers.length; i++) {
				servers[i] = args[i+1] +"@" +args[i+2];
			} 

            System.out.println("Load Balancer is ready and accepting clients' requests ... ");
			int clientNum = 0;
            while (true) {
                //accept new client connections
                Socket clientSocket = LBSocket.accept();
				clientNum++;

				//send sned Client with num n to [remainder]th 
				DataOutputStream out = new DataOutputStream(clientSocket.getOutputStream());
				out.writeUTF(servers[clientNum%numOfServers]);

            }
                   
 
           
        }
        //Implementing exception handling
         catch (SocketException e){System.out.println("Error Socket: " + e.getMessage());
         }catch (IOException e) {System.out.println("Error IO: " + e.getMessage());
        }finally {
        }
       }
    }
 