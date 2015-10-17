/***
 * JAVANAISE API
 * Contact: 
 *
 * Authors: 
 */

package jvn;

import irc.Sentence;

import java.io.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Interface of a JVN object. 
 * The serializable property is required in order to be able to transfer 
 * a reference to a JVN object remotely
 */



public class JvnObjectImpl implements JvnObject {

	/**
	* Get a Read lock on the object 
	* @throws JvnException
	**/
	
	
	States state = States.NL;
	int joi;
	
	Serializable appObject;
	//JvnLocalServer server;
	
	public JvnObjectImpl(Serializable appObject, int joi, JvnObject.States state){
		this.appObject = appObject;
		//server = JvnServerImpl.jvnGetServer();
		this.state = state; //After creation, there is a write lock in the object
		this.joi = joi;
	}
	
	public synchronized void jvnLockRead() throws jvn.JvnException{
		
		 System.out.println("jvnLockRead en jvnObjectImpl");
		
		if(state == States.RC)
		{
			state = States.R;
		}
		else if( state == States.NL)
		{
			appObject = JvnServerImpl.jvnGetServer().jvnLockRead(joi);
			Sentence s = (Sentence) appObject;
			System.out.println("The appObject is "+ s.read());
			state = States.R;
		}
		else if(state == States.WC)
		{
			state = States.RWC;
		}
		 System.out.println("The state went to: "+state);
	} 

	/**
	* Get a Write lock on the object 
	* @throws JvnException
	**/
	public synchronized void jvnLockWrite() throws jvn.JvnException{
	
		System.out.println("jvnLockWrite en jvnObjectImpl");
		if(state == States.NL || state == States.RC){
			JvnServerImpl.jvnGetServer().jvnLockWrite(joi);
			System.out.println("LockWrite object");
			state = States.W;
		}
		else if(state==States.WC || state == States.RWC){
			state = States.W;
		}
		 System.out.println("The state went to: "+state);
	} 

	/**
	* Unlock  the object 
	* @throws JvnException
	**/
	public synchronized void jvnUnLock() throws jvn.JvnException{
		
		System.out.println("Unlock en jvnObjectImpl");
		if(state == States.R){
	    	state = States.RC;
	    }else if(state == States.W || state == States.RWC) //maybe: || state == States.RWC
	    {
	    	state = States.WC; //when will it be in WC and when in RWC? In the lock read
	    }
		
		 System.out.println("The state went to: "+state);
		notifyAll();
	}
	
	
	/**
	* Get the object identification
	* @throws JvnException
	**/
	public int jvnGetObjectId() throws jvn.JvnException{
		return joi;
	}
	
	/**
	* Get the object state
	* @throws JvnException
	**/
	public Serializable jvnGetObjectState() throws jvn.JvnException{
		return appObject;
	}
	
	
	/**
	* Invalidate the Read lock of the JVN object 
	* @throws JvnException
	**/
  public synchronized void jvnInvalidateReader() throws jvn.JvnException{
	 
	  System.out.println("jvnInvalidateReader en jvnObjectImpl");
	  
	//Supposing that the user is reading... It will wait
	  while(state == States.R)
	  {
		  try {
			wait();
			
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	  }
	  
	  
	  state = States.NL;
	  System.out.println("The state went to: "+state);
	  
  }
	    
	/**
	* Invalidate the Write lock of the JVN object  
	* @return the current JVN object state
	* @throws JvnException
	**/
  public synchronized Serializable jvnInvalidateWriter() throws jvn.JvnException{
	
	  System.out.println("jvnInvalidateWriter en jvnObjectImpl");
	  try{
		   while(state == States.W){
			   wait();
		   }
		   
		   
	   }
	   catch(InterruptedException e){
		   e.printStackTrace();
	   }
	   
	  state = States.NL;
	   Sentence sent = (Sentence) appObject;
	  System.out.println("The state went to: "+state + " sentence "+sent);
	   return appObject;
	  
  }
	
	/**
	* Reduce the Write lock of the JVN object 
	* @return the current JVN object state
	* @throws JvnException
	**/
   public synchronized Serializable jvnInvalidateWriterForReader() throws jvn.JvnException{
	
	   System.out.println("jvnInvalidateWriterForReader en jvnObjectImpl");
	   try{
		   
		   if(state == States.RWC){
			   state = States.R;
		   }
		   else{
			   while(state == States.W){
				   wait();
			   }
			   state = States.RC;
		   }
	   }
	   catch(InterruptedException e){
		   e.printStackTrace();
	   }
	   
	   System.out.println("The state went to: "+state);
	   
	   return appObject;
	   
   }

	public Serializable jvnToString() throws JvnException {
		// TODO Auto-generated method stub
		return state + " joi: "+joi +" application object "+appObject;
	}

	public void jvnSetObjectState(Serializable o) throws JvnException {
		appObject = o;
		
	}

	
}
