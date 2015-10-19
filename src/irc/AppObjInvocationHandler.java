package irc;

import java.io.Serializable;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

import annotations.Ecriture;
import annotations.Lecture;

import jvn.JvnException;
import jvn.JvnObject;
import jvn.JvnServerImpl;

public class AppObjInvocationHandler implements InvocationHandler {

	JvnObject ob;

	public AppObjInvocationHandler(JvnObject o) {
		ob=o;
	}

	public static Object newInstance(JvnObject jo) throws JvnException{
		
		Object o=jo.jvnGetObjectState();
		
		return  java.lang.reflect.Proxy.newProxyInstance(
				o.getClass().getClassLoader(), //ob
				o.getClass().getInterfaces(),
				new AppObjInvocationHandler(jo));
		
	}
	
	public static Object LookupObject(String jon) 
	throws jvn.JvnException{
		
		
		
		JvnObject ob = JvnServerImpl.jvnGetServer().jvnLookupObject(jon);
		
		System.out.println("The object in LookUpObject AppObjInv " + ob);
		if(ob != null){
		
			//Object o=ob.jvnGetObjectState();
			Object temp = newInstance(ob);
	
			System.out.println("Temp: "+temp);
			return temp;
		}else
		{
			System.out.println("It came here");
			return null;
		}
		
	}
	
	public static Object RegisterObject(String jon, Serializable appObject) 
	throws JvnException{
		
		System.out.println("RegisterObject ");
		
		JvnObject obj = JvnServerImpl.jvnGetServer().jvnCreateObject(appObject);
		obj.jvnUnLock();
		JvnServerImpl.jvnGetServer().jvnRegisterObject("IRC", obj);

		System.out.println("The object in RegisterObject "+obj);
		
		Object temp = newInstance(obj);
		
		return temp;
	}

	
	/*public Object invoke(Object proxy, Method method, Object[] args)
			throws Throwable {
		// TODO Auto-generated method stub
		return null;
	}*/
	
	
	public Object invoke(Object o, Method m, Object[] arg)
			throws Throwable {
		
		System.out.println("Method invoke "+m.getName());
		Object result = null;
		boolean execute = true;
		
		try {
			
			if(m.isAnnotationPresent(Ecriture.class)){
				System.out.println("Write thing");
				ob.jvnLockWrite();
			}
			else if(m.isAnnotationPresent(Lecture.class)){
				System.out.println("Read thing");
				ob.jvnLockRead();
			}else{
				execute = false;
			}
			
			if(execute){
				result = m.invoke(ob.jvnGetObjectState(), arg);
				ob.jvnUnLock();
			}
			
		} catch (Exception e) {
				
		}

		return result;
	}
	
	

	
	
	
}
