/***
 * Irc class : simple implementation of a chat using JAVANAISE
 * Contact: 
 *
 * Authors: 
 */

package irc;

import java.awt.*;
import java.awt.event.*;


import jvn.*;
import java.io.*;

import javax.swing.JFrame;


public class Irc {
	public TextArea		text;
	public TextField	data;
	JFrame 			frame;
	//JvnObject       sentence;
	
	SentenceInt       sentence;


  /**
  * main method
  * create a JVN object nammed IRC for representing the Chat application
  **/
	public static void main(String argv[]) {
	   try {
		   
		// initialize JVN
		//JvnServerImpl js = JvnServerImpl.jvnGetServer();
		//System.out.println("server ok : js = " + js);
		
		// look up the IRC object in the JVN server
		// if not found, create it, and register it in the JVN server
		   
		   //TODO: Essayer avec la classe et pas l'interface.
		SentenceInt sent;
		
		System.out.println("whatever");
		
		//System.out.println(AppObjInvocationHandler.LookupObject("IRC").getClass().getName());
		sent =  (SentenceInt) AppObjInvocationHandler.LookupObject("IRC");
		
		//System.out.println("Sent: "+sent.getClass().getName());
		//JvnObject jo = js.jvnLookupObject("IRC");
		//System.out.println("The object was found "+sent );
		
		if (sent == null) {
			
			System.out.println("Thibi");
			sent = (SentenceInt) AppObjInvocationHandler.RegisterObject("IRC", (Serializable) new Sentence());
			System.out.println("object created");
			
		}else{
			//System.out.println("The complete obj "+jo.jvnToString());
		}
	
		// create the graphical part of the Chat application

		new Irc(sent);
	   
	   } catch (Exception e) {
		   e.printStackTrace();
		   System.out.println("IRC problem : " + e.getMessage());
	   }
	}

  /**
   * IRC Constructor
   * @param jo the JVN object representing the Chat
   **/
	public Irc(SentenceInt jo) {
		sentence = jo;
		frame=new JFrame();
		frame.setLayout(new GridLayout(1,1));
		text=new TextArea(10,60);
		text.setEditable(false);
		text.setForeground(Color.red);
		frame.add(text);
		data=new TextField(40);
		frame.add(data);
		Button read_button = new Button("read");
		read_button.addActionListener(new readListener(this));
		frame.add(read_button);
		Button write_button = new Button("write");
		write_button.addActionListener(new writeListener(this));
		frame.add(write_button);
		frame.setSize(545,201);
		//frame.setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
		frame.addWindowListener(new WindowAdapter()
        {
            @Override
            public void windowClosing(WindowEvent e)
            {
                System.out.println("Closed");
                try{
                	JvnServerImpl.jvnGetServer().jvnTerminate();
                }catch(Exception exc){
                	System.out.println("An exception: "+exc);
                }
                
                e.getWindow().dispose();
            }
        });
		text.setBackground(Color.black); 
		frame.setVisible(true);
	}
}


 /**
  * Internal class to manage user events (read) on the CHAT application
  **/
 class readListener implements ActionListener {
	Irc irc;
  
	public readListener (Irc i) {
		irc = i;
	}
   
 /**
  * Management of user events
  **/
	public void actionPerformed (ActionEvent e) {
	 try {
		// lock the object in read mode
		//irc.sentence.jvnLockRead();
		
		// invoke the method
		//String s = ((Sentence)(irc.sentence.jvnGetObjectState())).read();
		
		String s = irc.sentence.read();
		
		// unlock the object
		//irc.sentence.jvnUnLock();
		
		// display the read value
		irc.data.setText(s);
		irc.text.append(s+"\n");
	   } catch (Exception je) {
		   System.out.println("IRC problem : " + je.getMessage());
	   }
	}
}

 /**
  * Internal class to manage user events (write) on the CHAT application
  **/
 class writeListener implements ActionListener {
	Irc irc;
  
	public writeListener (Irc i) {
        	irc = i;
	}
  
  /**
    * Management of user events
   **/
	public void actionPerformed (ActionEvent e) {
	   try {	
		// get the value to be written from the buffer
		   String s = irc.data.getText();
        	
    // lock the object in write mode
		   
		   //System.out.println("write in the action performed -------- 1");
		   irc.sentence.write(s);
		   //System.out.println("write in the action performed ------- 2");
		   // invoke the method
		   //((Sentence)(irc.sentence.jvnGetObjectState())).write(s);
		
		// unlock the object
		  // irc.sentence.jvnUnLock();
	 } catch (Exception je) {
		   System.out.println("IRC problem  : " + je.getMessage());
	 }
	}
}



