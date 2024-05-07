import java.net.*; import java.io.*;

public class Event extends Thread{
    DataInputStream in;
    DataOutputStream out;
    // Socket clientSocket;
    // String clientUsername;
    ClientDetails client;
    public Event(ClientDetails aClient){
        try{
            client = aClient;
        in = new DataInputStream(client.getClientSocket().getInputStream());
        out = new DataOutputStream(client.getClientSocket().getOutputStream());
        this.start();
		System.out.println("EVENT STARTED");
        }catch(IOException e ){System.out.println("UH OHHHH"+e.getMessage());}

    }

    // boolean checkExisting()

    void step1() {
        try {
            out.writeUTF("THIS IS STEP 1");
        } catch (IOException e) {
            e.printStackTrace();
        }
        client.iterateStep(); }
        
    void step2() {
        try {
            out.writeUTF("THIS IS STEP 2");
        } catch (IOException e) {
            e.printStackTrace();
        }
        client.iterateStep(); step3();}

    void step3() {
        try {
            out.writeUTF("THIS IS STEP 2");
        } catch (IOException e) {
            e.printStackTrace();
        }
        client.iterateStep(); step4();}

    void step4() {
        try {
            out.writeUTF("THIS IS STEP 2");
        } catch (IOException e) {
            e.printStackTrace();
        }
        client.reset();
        try {
            client.getClientSocket().close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
  

   
    public void run(){
        try{
            if(!client.isReconnecting()){
            // String data = in.readUTF();
            // out.writeUTF("Received Message: "+data);
            step1();
            Thread.sleep(10000);
            out.writeUTF("HEHEHEHE");
            step2();}
        else{ int currStep = client.getStep();
            switch (currStep) {
                case 0:
                    step1(); 
                    break;
                case 1:
                    step2(); 
                    break;
                case 2:
                    step3(); 
                    break;
                case 3:
                    step4(); 
                    break;
                default:
                    out.writeUTF("Unknown step. Starting from step 1.");
                    step1(); // Default to step 1 if the step is unknown
                    break;
            }
            }

            // System.out.println("kill me");
        }catch(EOFException e){System.out.println("sadknajsd "+e.getMessage());
        }catch(IOException e){ System.out.println("Event Waiting... "+e.getMessage());
    } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }finally{
            try{
                client.getClientSocket().close();    
            }catch(IOException e ){System.out.println(e.getMessage());
            }
}
}
}