package tests;

import irc.AppObjInvocationHandler;
//import irc.Irc;
import irc.IrcV2;
import irc.Sentence;
import irc.SentenceInt;

import java.io.Serializable;

//import jvn.JvnObject;
//import jvn.JvnServerImpl;

public class ChatTest {

	
	
	public static void main(String args[]){
		
		
		
		try {
			   
			/*SentenceInt sent =  (SentenceInt) AppObjInvocationHandler.LookupObject("IRC");
			
			SentenceInt sent2 = (SentenceInt) AppObjInvocationHandler.LookupObject("IRC2");
		
			if(sent == null){
				sent = (SentenceInt) AppObjInvocationHandler.RegisterObject("IRC", (Serializable) new Sentence());
			}
			if(sent2 == null){
				sent2 = (SentenceInt) AppObjInvocationHandler.RegisterObject("IRC2", (Serializable) new Sentence());
			}*/
			
			
			//IrcV2.main(new String[0]);
			//IrcV2.main(new String[0]);
			//new IrcV2(sent);
			
			String separator = System.getProperty("file.separator");
			String classpath = System.getProperty("java.class.path");
			String path = System.getProperty("java.home")
		                + separator + "bin" + separator + "java";
			ProcessBuilder processBuilder = 
		                new ProcessBuilder(path, "-cp", 
		                classpath, 
		                IrcV2.class.getName());
			Process process = processBuilder.start();
			process.waitFor();
		
			
		   } catch (Exception e) {
			   System.out.println("IRC problem : " + e.getMessage());
		  }
	}
	
	
}
