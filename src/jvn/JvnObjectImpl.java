/***
 * JAVANAISE API
 * Contact: 
 *
 * Authors: 
 */

package jvn;
import java.io.*;

/**
 * Interface of a JVN object. 
 * The serializable property is required in order to be able to transfer 
 * a reference to a JVN object remotely
 */

public class JvnObjectImpl implements JvnObject {

	//pointeur vers le sérializable
	enum States {NL, R, W, RC, WC, RWC};
	private States state = NL;
	private int id = -1;
	private Lock working = new ReentrantLock();
	private Condition testing = working.newcondition();

	/**
	* Get a Read lock on the object
	* @throws JvnException
	**/
	public void jvnLockRead()
	throws jvn.JvnException{
		working.Lock();
		if (state == WC || state == W || state == RWC)
		{
			//call server to RWC
			state = RWC;
		}
		else
		{
			if (state == RC || state = NL)
				if (state == NL)
				//call server to R
			state = R;
		}
		working.Unlock();
	}

	/**
	* Get a Write lock on the object 
	* @throws JvnException
	**/
	public void jvnLockWrite()
    throws jvn.JvnException{
		working.Lock();
    	if (state == NL || state == R || state == RC)
			//{call server;}
		state = W;
		working.Unlock();
    }

	/**
	* Unlock  the object 
	* @throws JvnException
	**/
	public void jvnUnLock()
	throws jvn.JvnException{
		working.Lock();
		if (state == R || state == RC)
		{
			state = RC;
		}
		if (state == W || state == WC || state == RWC)
		{
			state = WC;
		}
		working.Unlock;
	}
	
	/**
	* Get the object identification
	* @throws JvnException
	**/
	public int jvnGetObjectId()
	throws jvn.JvnException{
		return id;
	}
	
	/**
	* Get the object state
	* @throws JvnException
	**/
	public Serializable jvnGetObjectState()
	throws jvn.JvnException{
		return state;
	}
	
	
	/**
	* Invalidate the Read lock of the JVN object 
	* @throws JvnException
	**/
    public void jvnInvalidateReader()
	throws jvn.JvnException{
		working.Lock();
		//wait to stop working
		state = NL;
		working.Unlock();
	}
	    
	/**
	* Invalidate the Write lock of the JVN object  
	* @return the current JVN object state
	* @throws JvnException
	**/
    public Serializable jvnInvalidateWriter()
	throws jvn.JvnException{
		working.Lock();
		//wait to stop working
		state = NL;
		return this;
		working.Unlock();
	}
	
	/**
	* Reduce the Write lock of the JVN object 
	* @return the current JVN object state
	* @throws JvnException
	**/
    public Serializable jvnInvalidateWriterForReader()
	throws jvn.JvnException{
		working.Lock();
		while (state == W)

		if (state == RC)
			state = RC
		else
			state = R;
		return this;
		working.Unlock();
	}
}
