/***
 * JAVANAISE Implementation
 * JvnServerImpl class
 * Contact: 
 *
 * Authors: 
 */

package jvn;

import java.rmi.server.UnicastRemoteObject;
import java.io.Serializable;
import java.util.ArrayList;

public class JvnCoordImpl 	
              extends UnicastRemoteObject 
							implements JvnRemoteCoord{
	

	public int count;
	public ArrayList<CoordObject> list = new ArrayList<>();
	
	/**
	* Default constructor
	* @throws JvnException
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
		obj.id = jo.id;
		if (obj.id == -1)
			obj.id = count++;
		obj.users.add(js);
		else if (jo.state == R || jo.state == RC)
			obj.state.add(R);
		else
			obj.state.add(W);
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
    		list.get(i).jo;
    return NULL;
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
	for (int i=0 ; i<list.size() ; i++)
		if (list.get(i).id == joi)
		{
			CoordObject obj = list.get(i);
			for (int j=0 ; j<obj.users.size() ; j++)
			{
				if (obj.state.get(j) == W)
					obj.users.get(j).jvnInvalidateWriterForReader(joi);
			}
    		return obj.jo;
		}
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
	for (int i=0 ; i<list.size() ; i++)
		if (list.get(i).id == joi)
		{
			CoordObject obj = list.get(i);
			for (int j=0 ; j<obj.users.size() ; j++)
			{
				if (obj.state.get(j) == W)
					obj.users.get(j).jvnInvalidateWriter(joi);
				else
					obj.users.get(j).jvnInvalidateReader(joi);
			}
    		return jo;
		}
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
			int id = list.get(i).users.indexOf(js)
			list.get(i).users.remove(id);
			list.get(i).state.remove(id);
		}
    }
}

 
