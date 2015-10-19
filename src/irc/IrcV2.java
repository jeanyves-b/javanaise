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


public class IrcV2 {
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
	
		SentenceInt sent;
		
		sent =  (SentenceInt) AppObjInvocationHandler.LookupObject("IRC");
		System.out.println("The object is: "+sent);

		if (sent == null) {
			
			sent = (SentenceInt) AppObjInvocationHandler.RegisterObject("IRC", (Serializable) new Sentence());
			System.out.println("object created");
			
		}
	
		// create the graphical part of the Chat application
		new IrcV2(sent);
	   
	   } catch (Exception e) {
		   e.printStackTrace();
		   System.out.println("IRC problem : " + e.getMessage());
	   }
	}

  /**
   * IRC Constructor
   * @param jo the JVN object representing the Chat
   **/
	public IrcV2(SentenceInt jo) {
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
		read_button.addActionListener(new readListener2(this));
		frame.add(read_button);
		Button write_button = new Button("write");
		write_button.addActionListener(new writeListener2(this));
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
 class readListener2 implements ActionListener {
	IrcV2 irc;
  
	public readListener2 (IrcV2 i) {
		irc = i;
	}
   
 /**
  * Management of user events
  **/
	public void actionPerformed (ActionEvent e) {
	 try {
	
		
		String s = irc.sentence.read();
		
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
 class writeListener2 implements ActionListener {
	IrcV2 irc;
  
	public writeListener2 (IrcV2 i) {
        	irc = i;
	}
  
  /**
    * Management of user events
   **/
	public void actionPerformed (ActionEvent e) {
	   try {	
		// get the value to be written from the buffer
		   String s = irc.data.getText();
        	
	
		   irc.sentence.write(s);
		
	 } catch (Exception je) {
		   System.out.println("IRC problem  : " + je.getMessage());
	 }
	}
}



