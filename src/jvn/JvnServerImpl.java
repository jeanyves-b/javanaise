/***
 * JAVANAISE Implementation
 * JvnServerImpl class
 * Contact: 
 *
 * Authors: 
 */

package jvn;



import java.rmi.Naming;
import java.rmi.RMISecurityManager;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.io.*;



public class JvnServerImpl 	
              extends UnicastRemoteObject 
							implements JvnLocalServer, JvnRemoteServer{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static JvnRemoteCoord coord;
	// A JVN server is managed as a singleton 
	private static JvnServerImpl js = null;
	static Registry registry;
	
	ArrayList<JvnObject> jvnObjs;
	
	
  /**
  * Default constructor
  * @throws JvnException
  **/
	private JvnServerImpl() throws Exception {
		super();
      
		jvnObjs = new ArrayList<JvnObject>();

        try 
        { 
        	registry = LocateRegistry.getRegistry("localhost",1414);
            coord = (JvnRemoteCoord) registry.lookup("Coordinator2");
        } 
        catch (Exception e) 
        { 
           System.out.println("exception: " + e.getMessage()); 
           e.printStackTrace(); 
        } 
		
	}
	
  /**
    * Static method allowing an application to get a reference to 
    * a JVN server instance
    * @throws JvnException
    **/
	public static JvnServerImpl jvnGetServer() {
		//System.out.println("get server");
		//System.out.println("JS "+js);
		if (js == null){
			
			try {
				js = new JvnServerImpl();
				//System.out.println("Creates new jvn server");
			} catch (Exception e) {
				return null;
			}
		}
		return js;
	}
	
	/**
	* The JVN service is not used anymore
	* @throws JvnException
	**/
	public  void jvnTerminate()
	throws jvn.JvnException {
       try {
		coord.jvnTerminate(js);
	} catch (RemoteException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	} 
	
	/**
	* creation of a JVN object
	* @param o : the JVN object state
	* @throws JvnException
	**/
	public  JvnObject jvnCreateObject(Serializable o)
	throws jvn.JvnException { 

		JvnObject obj = null;
		
		try {
			obj = new JvnObjectImpl(o, coord.jvnGetObjectId());
			jvnObjs.add(obj);

		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		
		//System.out.println("the object "+obj.jvnGetObjectId());
		return obj; 
	}
	
	
	/**
	*  Associate a symbolic name with a JVN object
	* @param jon : the JVN object name
	* @param jo : the JVN object 
	* @throws JvnException
	**/
	public  void jvnRegisterObject(String jon, JvnObject jo)
	throws jvn.JvnException {
		// to be completed 
		
		try {
			coord.jvnRegisterObject(jon, jo, js);
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//register the object in the server and also in the coordinator?
		
	}
	
	/**
	* Provide the reference of a JVN object beeing given its symbolic name
	* @param jon : the JVN object name
	* @return the JVN object 
	* @throws JvnException
	 * @throws java.rmi.RemoteException 
	**/
	public  JvnObject jvnLookupObject(String jon)
	throws jvn.JvnException {
		
		JvnObject obj = null;
		 try {
			 
			obj = coord.jvnLookupObject(jon, js);
			System.out.println("LookUpObject "+obj);
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return obj;
	}	
	
	/**
	* Get a Read lock on a JVN object 
	* @param joi : the JVN object identification
	* @return the current JVN object state
	* @throws  JvnException
	**/
   public Serializable jvnLockRead(int joi)
	 throws JvnException {
	
	    Serializable obj = null;
	   
	    try {
			obj = coord.jvnLockRead(joi, js);
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return obj;

	}	
	/**
	* Get a Write lock on a JVN object 
	* @param joi : the JVN object identification
	* @return the current JVN object state
	* @throws  JvnException
	**/
   public Serializable jvnLockWrite(int joi)
	 throws JvnException {
		// to be completed 
	   Serializable obj = null;
	   
	   try {
		obj = coord.jvnLockWrite(joi, js);
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return obj;
	}	

	
  /**
	* Invalidate the Read lock of the JVN object identified by id 
	* called by the JvnCoord
	* @param joi : the JVN object id
	* @return void
	* @throws java.rmi.RemoteException,JvnException
	**/
  public void jvnInvalidateReader(int joi)
	throws java.rmi.RemoteException,jvn.JvnException {
		
	  for(JvnObject obj: jvnObjs){
		  if(obj.jvnGetObjectId() == joi)
		  {
			  obj.jvnInvalidateReader();
		  }
	  }
	  	
	}
	    
	/**
	* Invalidate the Write lock of the JVN object identified by id 
	* @param joi : the JVN object id
	* @return the current JVN object state
	* @throws java.rmi.RemoteException,JvnException
	**/
  public Serializable jvnInvalidateWriter(int joi)
	throws java.rmi.RemoteException,jvn.JvnException { 
		// to be completed 
	  
	  Serializable o = null;
	  	
		  for(JvnObject obj: jvnObjs){
			  if(obj.jvnGetObjectId() == joi)
			  {
				  o = obj.jvnInvalidateWriter();
			  }
		  }
		return o;
	}
	
	/**
	* Reduce the Write lock of the JVN object identified by id 
	* @param joi : the JVN object id
	* @return the current JVN object state
	* @throws java.rmi.RemoteException,JvnException
	**/
   public Serializable jvnInvalidateWriterForReader(int joi)
	 throws java.rmi.RemoteException,jvn.JvnException { 
	   
	   Serializable o = null;
	  	
		  for(JvnObject obj: jvnObjs){
			  if(obj.jvnGetObjectId() == joi)
			  {
				  o = obj.jvnInvalidateWriterForReader();
			  }
		  }
		return o;
	   
	 }

}

 
