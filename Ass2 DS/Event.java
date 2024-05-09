import java.net.*; import java.io.*;

public class Event extends Thread{
    DataInputStream in;
    DataOutputStream out;
    // Socket clientSocket;
    // String clientUsername;
    long endTime = System.currentTimeMillis() + (3*60*1000);
    long stepTime = System.currentTimeMillis() + (3*60*1000);
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

    void step1() throws IOException, InterruptedException {
        out.writeUTF("THIS IS STEP 1");
        client.iterateStep(); 
        Thread.sleep(10000);
        step2();
    }
        
        void step2() throws IOException,InterruptedException {
        out.writeUTF("THIS IS STEP 2");
        client.iterateStep(); 
        Thread.sleep(10000);

        step3();
    }

    void step3() throws IOException, InterruptedException {
        out.writeUTF("THIS IS STEP 3");
        client.iterateStep(); 
        Thread.sleep(10000);
        step4();
    }

    void step4() throws IOException {
        out.writeUTF("THIS IS STEP 4");
        client.reset();
        try {
            client.getClientSocket().close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

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

    // public void waitForTen() {
    //     while (System.currentTimeMillis() < )
    // }

    public void waitForClient(boolean dc) {
        while (System.currentTimeMillis() < endTime && dc) {
            if (!client.getClientSocket().isClosed()){
                dc = false;
                try{
                in = new DataInputStream(client.getClientSocket().getInputStream());
                out = new DataOutputStream(client.getClientSocket().getOutputStream());
                checkStep(client.getStep());
                }catch (IOException e1) {}
                catch (InterruptedException e){}
            }
        }
    }
        
  

   
    public void run(){
        try{
            int currStep = client.getStep();
            checkStep(currStep);
        }
        catch(EOFException e){System.out.println("sadknajsd "+e.getMessage());}
        //if client disconnects
        catch(IOException e) {
            System.out.println("Event Waiting... "+e.getMessage());
            waitForClient(true);
        }catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        finally {
            try{
                client.getClientSocket().close();    
            }catch(IOException e ){System.out.println(e.getMessage());
            }
}
}
}