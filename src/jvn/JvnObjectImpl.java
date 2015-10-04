/***
 * JAVANAISE API
 * Contact: 
 *
 * Authors: 
 */

package jvn;

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
	
	public enum States{NL, R, W, RC, WC, RWC};
	States state = States.NL;
	int joi;
	
	Serializable appObject;
	JvnLocalServer server;
	
	public JvnObjectImpl(Serializable appObject, int joi){
		this.appObject = appObject;
		server = JvnServerImpl.jvnGetServer();
		state = States.W; //After creation, there is a write lock in the object
		this.joi = joi;
	}
	
	public synchronized void jvnLockRead() throws jvn.JvnException{
		
		if(state == States.RC)
		{
			state = States.R;
		}
		else if( state == States.NL)
		{
			appObject = server.jvnLockRead(joi);
			state = States.R;
		}
		else if(state == States.WC)
		{
			state = States.RWC;
		}
		
	} 

	/**
	* Get a Write lock on the object 
	* @throws JvnException
	**/
	public synchronized void jvnLockWrite() throws jvn.JvnException{
	
		if(state == States.NL){
			server.jvnLockWrite(joi);
			state = States.W;
		}
		else if(state==States.WC || state == States.RWC){
			state = States.W;
		}
		
	} 

	/**
	* Unlock  the object 
	* @throws JvnException
	**/
	public synchronized void jvnUnLock() throws jvn.JvnException{
		
		if(state != States.NL)
			//tell the server //What to do in the case where it is recently created and I have the write lock?

		if(state == States.R){
	    	state = States.RC;
	    }else if(state == States.W)
	    {
	    	state = States.WC; //cuando estará en WC y cuando en RWC?
	    }
		state = States.NL;
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
	public synchronized Serializable jvnGetObjectState() throws jvn.JvnException{
		
		
		return appObject;
		
	}
	
	
	/**
	* Invalidate the Read lock of the JVN object 
	* @throws JvnException
	**/
  public synchronized void jvnInvalidateReader() throws jvn.JvnException{
	 
	  
	  
	  if(state == States.RC){
		  state = States.NL;
	  }
	  
	//Supposing that the user is reading... It will wait
	  while(state == States.R || state == States.RWC)
	  {
		  try {
			wait();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	  }
	  
	  
	  
  }
	    
	/**
	* Invalidate the Write lock of the JVN object  
	* @return the current JVN object state
	* @throws JvnException
	**/
  public synchronized Serializable jvnInvalidateWriter() throws jvn.JvnException{
	
	  try{
		   while(state == States.W){
			   wait();
		   }
		   
		   state = States.NL;
	   }
	   catch(InterruptedException e){
		   e.printStackTrace();
	   }
	   
	   
	   return appObject;
	  
  }
	
	/**
	* Reduce the Write lock of the JVN object 
	* @return the current JVN object state
	* @throws JvnException
	**/
   public synchronized Serializable jvnInvalidateWriterForReader() throws jvn.JvnException{
	
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
	   
	   
	   return appObject;
	   
   }
}
