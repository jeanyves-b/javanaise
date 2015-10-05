/***
 * JAVANAISE Implementation
 * JvnServerImpl class
 * Contact: 
 *
 * Authors: 
 */

package jvn;

import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.io.Serializable;
import java.util.ArrayList;

import jvn.CoordObject.States;

public class JvnCoordImpl 	
              extends UnicastRemoteObject
							implements JvnRemoteCoord{


  /**
  * Default constructor
  * @throws JvnException
  **/
	
	public int count;
	public ArrayList<CoordObject> list = new ArrayList<CoordObject>();

	public static void main(String[] args){
		try {
			JvnCoordImpl coord = new JvnCoordImpl();
			Registry registry = LocateRegistry.createRegistry(1414);
	        registry.bind("Coordinator2", coord);
	        System.out.println ("Coord ready");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}   
	}	
	
	/**
	* Default constructor
	* @throws JvnException²
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
		count++;
	    return count -1;
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
		CoordObject obj = new CoordObject();
		obj.name = jon;
		obj.id = jo.jvnGetObjectId();
		if (obj.id == -1) //just in case, security check
			obj.id = count++;
		obj.users.add(js);
		obj.state.add(States.W);
		list.add(obj);
	}

	/**
	* Get the reference of a JVN object managed by a given JVN server 
	* @param jon : the JVN object name
	* @param js : the remote reference of the JVNServer
	* @throws java.rmi.RemoteException,JvnException
	**/
	public JvnObject jvnLookupObject(String jon, JvnRemoteServer js)
	throws java.rmi.RemoteException,jvn.JvnException{
	for (int i=0 ; i<list.size() ; i++)
    	if (list.get(i).name == jon)
    		return list.get(i).jo;
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
    	CoordObject obj = null;
	for (int i=0 ; i<list.size() ; i++)
		if (list.get(i).id == joi)
		{
			obj = list.get(i);
			for (int j=0 ; j<obj.users.size() ; j++)
			{
				if (obj.state.get(j) == States.W)
					obj.users.get(j).jvnInvalidateWriterForReader(joi);
			}
    		
		}
	return obj.jo;
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
	CoordObject obj = null;
	for (int i=0 ; i<list.size() ; i++)
		if (list.get(i).id == joi)
		{
			obj = list.get(i);
			for (int j=0 ; j<obj.users.size() ; j++)
			{
				if (obj.state.get(j) == States.W)
					obj.users.get(j).jvnInvalidateWriter(joi);
				else
					obj.users.get(j).jvnInvalidateReader(joi);
			}
    		
		}
	
	return obj.jo;
	}
	

	/**
	* A JVN server terminates
	* @param js  : the remote reference of the server
	* @throws java.rmi.RemoteException, JvnException
	**/
    public void jvnTerminate(JvnRemoteServer js)
	throws java.rmi.RemoteException, JvnException {
	for (int i=0 ; i<list.size() ; i++)
		if (list.get(i).users.contains(js))
		{
			int id = list.get(i).users.indexOf(js);
			list.get(i).users.remove(id);
			list.get(i).state.remove(id);
		}
    }
}

 
