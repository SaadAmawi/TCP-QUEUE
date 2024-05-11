import java.net.*; import java.io.*;
import java.util.ArrayList;
public class Event extends Thread{
    DataInputStream in;
    DataOutputStream out;
    // Socket clientSocket;
    // String clientUsername;
    long endTime = System.currentTimeMillis() + (3*60*1000);
    long stepTime = System.currentTimeMillis() + (3*60*1000);
    ClientDetails client;
    ArrayList<String> seats = generateSeats();
    String ticketClass=null;
    String seatChoice = null;

    public Event(ClientDetails aClient){
        try{
        client = aClient;
        in = new DataInputStream(client.getClientSocket().getInputStream());
        out = new DataOutputStream(client.getClientSocket().getOutputStream());
        this.start();
		System.out.println("EVENT STARTED");
        }catch(IOException e ){System.out.println("UH OHHHH"+e.getMessage());}
    }
    
    public ArrayList<String> generateSeats(){
        int count ;
        ArrayList<String> seats = new ArrayList<String>(){};

        char[] letters = new char[10];
        for (int i = 0; i < letters.length; i++) {
            letters[i] = (char) ('A' + i);
        };
        int[] nums = new int[]{1,2,3,4,5,6,7,8,9};
        for(count = 0; count<letters.length;count++){
            for(int j=0; j<nums.length; j++){
                seats.add(letters[count]+""+nums[j]);
            }
        }
        return seats;
    }

    void step1() throws IOException, InterruptedException {
        String sets =new String("AVAILALE SEATS: ");
        for (String c : seats)
        sets+="["+c+"]"+" ";
        out.writeUTF(sets);
        out.flush();
        client.iterateStep(); 
        Thread.sleep(5000);
        step2();
    }
        
        void step2() throws IOException,InterruptedException {
        out.writeUTF("Please Select a Seat");
        out.flush();
        String reply = in.readUTF();
        if(reply!=null && seats.contains(reply) ){
            seats.remove(reply);
            out.writeUTF("Selection Confirmed!");
            out.flush();
           seatChoice = reply;
        }
        
   
        client.iterateStep(); 
        Thread.sleep(1000);

        step3();
    }

    void step3() throws IOException, InterruptedException {
        out.writeUTF("Choose Ticket Class: Standard(S), First Class(FC), VIP(VIP), Golden Circle(GC)");
        String choice = in.readUTF();
        if(choice.equals("S") || choice.equals("FC")||choice.equals("VIP")||choice.equals("GC"))
        ticketClass = choice;
        out.writeUTF("Selection Confirmed");
        out.flush();
        client.iterateStep(); 
        Thread.sleep(1000);
        step4();
    }

    void step4() throws IOException {
        out.writeUTF("\n\n---------Ticket Reservation Completed Successfully--------- \n--------------------------Details-------------------------- \n Seat: "+seatChoice+"\n Ticket Class: "+ticketClass);
        out.flush();
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