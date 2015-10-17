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

	
	
	
	public static Object LookupObject(String jon) 
	throws jvn.JvnException{
		
		
		
		JvnObject ob = JvnServerImpl.jvnGetServer().jvnLookupObject(jon);
		
		System.out.println("The object in LookUpObject AppObjInv " + ob);
		if(ob != null){
		
		Object o=ob.jvnGetObjectState();
		Object temp = java.lang.reflect.Proxy.newProxyInstance(
				ob.getClass().getClassLoader(),
				o.getClass().getInterfaces(),
				new AppObjInvocationHandler(ob));

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
		Object o=obj.jvnGetObjectState();
		obj.jvnUnLock();
		JvnServerImpl.jvnGetServer().jvnRegisterObject("IRC", obj);
	
		
		System.out.println("The object in RegisterObject "+obj);
		
		return java.lang.reflect.Proxy.newProxyInstance(
				obj.getClass().getClassLoader(),
				o.getClass().getInterfaces(),
				new AppObjInvocationHandler(obj));
	}




	
	/*public Object invoke(Object proxy, Method method, Object[] args)
			throws Throwable {
		// TODO Auto-generated method stub
		return null;
	}*/
	
	
	public Object invoke(Object o, Method m, Object[] arg)
			throws Throwable {
		
		System.out.println("Method invoke");
		Object result = null;
		boolean execute = true;
		
		try {
			
			if(m.isAnnotationPresent(Ecriture.class)){
				System.out.println("WRite thing");
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
