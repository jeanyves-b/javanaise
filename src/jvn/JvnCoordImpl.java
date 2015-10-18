/***
 * JAVANAISE Implementation
 * JvnServerImpl class
 * Contact: 
 *
 * Authors: 
 */

package jvn;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.io.Serializable;
import java.util.concurrent.ConcurrentHashMap;

import jvn.CoordObject.States;



public class JvnCoordImpl 	
              extends UnicastRemoteObject
							implements JvnRemoteCoord{


  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
/**
  * Default constructor
  * @throws JvnException
  **/
	
	public int count;
	//public ArrayList<CoordObject> list = new ArrayList<CoordObject>();
	//public
	public ConcurrentHashMap<Integer, CoordObject> listObjects = new ConcurrentHashMap<Integer, CoordObject>();
	public ConcurrentHashMap<String, Integer> listLookUp = new ConcurrentHashMap<String, Integer>();
	
	
	public static void main(String[] args){

		try {
			
			JvnCoordImpl coord = new JvnCoordImpl();

				Registry registry = LocateRegistry.createRegistry(1414); //port as integer
				System.out.println("registry  "+registry);
	        	registry.bind("Coordinator2", coord); //registry.bind Naming
	        	System.out.println ("Coord ready");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		   
	}
	


	/**
	* Default constructor
	* @throws JvnExceptionÂ²
	**/
	private JvnCoordImpl() throws Exception {
		count = 0;

	}

	/**
	*  Allocate a NEW JVN object id (usually allocated to a 
	*  newly created JVN object)
	* @throws java.rmi.RemoteException,JvnException
	**/
	public int jvnGetObjectId()
	throws java.rmi.RemoteException,jvn.JvnException {
	    return count++;
	}


	/**
	* Associate a symbolic name with a JVN object
	* @param jon : the JVN object name
	* @param jo  : the JVN object 
	* @param joi : the JVN object identification
	* @param js  : the remote reference of the JVNServer
	* @throws java.rmi.RemoteException,JvnException
	**/
	
	public void jvnRegisterObject(String jon, JvnObject jo, JvnRemoteServer js)
	throws java.rmi.RemoteException,jvn.JvnException{
		
		
		//We have the hypothesis that the object is registered for the first time in the coordinator
		
		//register in the lookUpList
		listLookUp.put(jon, jo.jvnGetObjectId());
		System.out.println("sthg");
			
			//Create the object and add it with the server. 
			CoordObject obj = new CoordObject();
			obj.jo = jo;
			obj.serverState.put(js, States.W);
			listObjects.put(jo.jvnGetObjectId(), obj);
	
		printCoordState();
	}

	/**
	* Get the reference of a JVN object managed by a given JVN server 
	* @param jon : the JVN object name
	* @param js : the remote reference of the JVNServer
	* @throws java.rmi.RemoteException,JvnException
	**/
	public JvnObject jvnLookupObject(String jon, JvnRemoteServer js)
	throws java.rmi.RemoteException,jvn.JvnException{
		
		System.out.println("The LookUpObject in coord");
		
		if(listLookUp.containsKey(jon)){
			CoordObject c = listObjects.get(listLookUp.get(jon));
			c.serverState.put(js, States.NL);
			printCoordState();
			return c.jo;
		}
		
		/*for (CoordObject obj : listObjects.values()) {
			if(obj.name.equalsIgnoreCase(jon)){
				obj.serverState.put(js, States.NL);
				printCoordState();
				return obj.jo;
			}
		}*/
		printCoordState();

		return null;
	}
  
  /**
  * Get a Read lock on a JVN object managed by a given JVN server 
  * @param joi : the JVN object identification
  * @param js  : the remote reference of the server
  * @return the current JVN object state
  * @throws java.rmi.RemoteException, JvnException
  **/
    public Serializable jvnLockRead(int joi, JvnRemoteServer js)
    throws java.rmi.RemoteException, JvnException{
    	
    	System.out.println("In the jvnLockRead");
    	
    	CoordObject obj = listObjects.get(joi);
    	Serializable appObj = obj.jo.jvnGetObjectState();
    	
    	for(JvnRemoteServer server: obj.serverState.keySet()){
    		try{
    			System.out.println("Server state "+server+ "es: "+obj.serverState.get(server));
        		if(obj.serverState.get(server).compareTo(States.W) == 0){
        			if(!server.equals(js)){
        				appObj = server.jvnInvalidateWriterForReader(joi);
        				//Mettre a jour le appObj dedans le jvnObject in the coordinator?
        				obj.jo.jvnSetObjectState(appObj);
            			obj.serverState.put(server, States.R);
            			System.out.println("Invalidating the writer for the server "+server);
        			}
        		}
    		}catch(RemoteException e){
    			System.out.println("The connection to the server: "+server+" couldn't get done and it will be deleted");
    			//obj.serverState.remove(server);
    		}
    		
    	}
    	
    	obj.serverState.put(js, States.R);
    	
    	printCoordState();
    	
    	return appObj;
    }

	/**
	* Get a Write lock on a JVN object managed by a given JVN server 
	* @param joi : the JVN object identification
	* @param js  : the remote reference of the server
	* @return the current JVN object state
	* @throws java.rmi.RemoteException, JvnException
	**/
	public Serializable jvnLockWrite(int joi, JvnRemoteServer js)
	throws java.rmi.RemoteException, JvnException{
		
		
    	CoordObject obj = listObjects.get(joi);
    	Serializable appObj = obj.jo.jvnGetObjectState();
    	System.out.println("Lock Write in Coord");
    	for(JvnRemoteServer server: obj.serverState.keySet()){
    		try{
    			System.out.println("Server state "+server+ "es: "+obj.serverState.get(server));
    			if(!server.equals(js)){
        			if(obj.serverState.get(server).equals(States.W)){
            			appObj = server.jvnInvalidateWriter(joi);
            			obj.jo.jvnSetObjectState(appObj);
            			System.out.println("LockWrite- Invalidating the writer for the server "+server);
            		}
            		else{
            			System.out.println("Invalidating the reader for the server 1"+server);
            			server.jvnInvalidateReader(joi);
            			System.out.println("Invalidating the reader for the server "+server);
            		}
        			obj.serverState.put(server, States.NL);
        		}
    		}catch(RemoteException e){
    			System.out.println("The connection to the server: "+server+" couldn't get done and it will be deleted");
    			//obj.serverState.remove(server);
    		}
    	}
    	
    	obj.serverState.put(js, States.W);
    	
    	printCoordState();
    	return appObj;
	
	}
	

	/**
	* A JVN server terminates
	* @param js  : the remote reference of the server
	* @throws java.rmi.RemoteException, JvnException
	**/
    public void jvnTerminate(JvnRemoteServer js)
	throws java.rmi.RemoteException, JvnException {
    	
    	
    	for(Integer i: listObjects.keySet()){
    		System.out.println("El servidor que está por ser eliminado: "+js);
    		listObjects.get(i).serverState.remove(js);
    	}
    	
    }
    
    private void printCoordState(){
    	CoordObject cord;
    	for(Integer i: listObjects.keySet()){
    		cord = listObjects.get(i);
    		
    		System.out.println(" The object: "+cord.jo);
    		System.out.println("The server state: ");
    		
    		for(JvnRemoteServer server : cord.serverState.keySet()){
    			
    			System.out.println("The server "+server+" and state "+ cord.serverState.get(server));
    		}
    		
    		cord.serverState.toString();
    		
    		
    	}
    }
	
}

 
