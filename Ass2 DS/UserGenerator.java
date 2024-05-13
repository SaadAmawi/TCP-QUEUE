import java.util.Random;
import java.io.*;
 
public class UserGenerator {
 
    private static final String LOWERCASE = "abcdefghijklmnopqrstuvwxyz";
    private static final String UPPERCASE = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String DIGITS = "0123456789";
    private static final String SPECIAL = "@";
    private static String[] choices = {"S", "FC", "VIP", "GC"};
 
    public static void main(String[] args) throws IOException {
    	String ip = "192.168.1.250";
    	
    	
    	FileWriter signWriter;
    	FileWriter logWriter;
		
    	signWriter = new FileWriter("SignUps10.txt");
		logWriter = new FileWriter("LogIns10.txt");
 
    	
    	char seatLtr = 'A';
    	int seatNo = 1;
    	String serverInfo = ip ;
    	for (int i = 0; i < 10; i++) {
    		String tmpStrSign = serverInfo;
    		String tmpStrLog = serverInfo;
    		
    		tmpStrSign += " SIGNUP";
    		tmpStrLog += " LOGIN";
    		
    		String tmpUserInfo = " " + genUsername() + " " + genPassword();
    		
    		tmpStrSign += tmpUserInfo;
    		tmpStrLog += tmpUserInfo;
			
    		tmpStrLog += " " + seatLtr + seatNo + " " +genClass();
    		if (seatNo < 9) {
    			seatNo++;
    		}
    		else {
    			seatLtr++;
    			seatNo = 1;	
    		}
    		
			logWriter.write(tmpStrLog + "\n");
			signWriter.write(tmpStrSign + "\n");
    		
    		
    		
    	}
    	
    	logWriter.close();
    	signWriter.close();
    	System.out.println("Done");
    	
    }
    public static String genClass() {
    	Random r = new Random();
    	return choices[r.nextInt(4)];
    }
    
    public static String genUsername() {
    	Random r = new Random();
    	int usernameLen = r.nextInt(5, 12);
    	String username = "";
    	for (int i = 0; i < usernameLen; i++) {
    		
    		boolean upperCase = r.nextBoolean();
    		if (upperCase)
    			username += UPPERCASE.charAt(r.nextInt(UPPERCASE.length()));
    		else
    			username += LOWERCASE.charAt(r.nextInt(LOWERCASE.length()));
    	}
    	return(username);
    }
    
    public static String genPassword() {
		Random r = new Random();
    	int passLen = r.nextInt(8, 12);
    	
    	//initially with 1 char of each type:
    	String password = ""
    			+UPPERCASE.charAt(r.nextInt(UPPERCASE.length()))
    			+LOWERCASE.charAt(r.nextInt(LOWERCASE.length()))
    			+DIGITS.charAt(r.nextInt(DIGITS.length()))
    			+SPECIAL.charAt(r.nextInt(SPECIAL.length()));
    			
    	
    	for (int i = 0; i < passLen; i++) {
 
    		//0 UPPERCASE, 1 LOWERCASE, 2 DIGIT, 3 SPECIAL
    		int type = r.nextInt(3);
    		
    		
    		switch(type) {
    		case 0:
    			password += UPPERCASE.charAt(r.nextInt(UPPERCASE.length()));
    			break;
    		case 1:
    			password += LOWERCASE.charAt(r.nextInt(LOWERCASE.length()));
    			break;
    		case 2:
    			password += DIGITS.charAt(r.nextInt(DIGITS.length()));
    			break;
    		case 3:
    			password += SPECIAL.charAt(r.nextInt(DIGITS.length()));
    			break;
    	
    			
    		}
    	}
    	password += UPPERCASE.charAt(r.nextInt(UPPERCASE.length()));
    	
    	return (password); //return
    }
}