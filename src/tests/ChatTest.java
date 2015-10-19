package tests;

import irc.AppObjInvocationHandler;
//import irc.Irc;
import irc.Sentence;
import irc.SentenceInt;

import java.io.Serializable;
import java.util.Random;

//import jvn.JvnObject;
//import jvn.JvnServerImpl;

public class ChatTest {
	
	
	public static void main(String args[]){
		
		
		
		try {
			   
			SentenceInt sent =  (SentenceInt) AppObjInvocationHandler.LookupObject("IRC");
			
			//SentenceInt sent2 = (SentenceInt) AppObjInvocationHandler.LookupObject("IRC2");
		
			if(sent == null){
				sent = (SentenceInt) AppObjInvocationHandler.RegisterObject("IRC", (Serializable) new Sentence());
			}
			
			
			Random randomGenerator = new Random();
			int value = 0;
			for(int i=0; i<=100; i++){
				//System.out.println("sthg happens");
				value = randomGenerator.nextInt(10);
				Thread.sleep((long)(Math.random() * 2000));
				if(value<5){
					sent.write("***********************Something"+value);
				}else
				{
					System.out.println(sent.read());
				}
			}
			
			//IrcV2.main(new String[0]);
			//IrcV2.main(new String[0]);
			//new IrcV2(sent);
			
			/*String separator = System.getProperty("file.separator");
			String classpath = System.getProperty("java.class.path");
			String path = System.getProperty("java.home")
		                + separator + "bin" + separator + "java";
			ProcessBuilder processBuilder = 
		                new ProcessBuilder(path, "-cp", 
		                classpath, 
		                IrcV2.class.getName());
			Process process = processBuilder.start();
			process.waitFor();*/
		
			
		   } catch (Exception e) {
			   System.out.println("IRC problem : " + e.getMessage());
		  }
	}
	
	
}
