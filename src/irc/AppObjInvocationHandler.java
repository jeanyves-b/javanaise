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
		//if(ob != null){
		
		Object temp = java.lang.reflect.Proxy.newProxyInstance(
				ob.getClass().getClassLoader(),
				ob.getClass().getInterfaces(),
				new AppObjInvocationHandler(ob));

		System.out.println("Temp: "+temp);
		return temp;
		//}else
		//{
		//	return null;
		//}
		
	}
	
	public static Object RegisterObject(String jon, Serializable appObject) 
	throws JvnException{
		
		System.out.println("RegisterObject ");
		
		JvnObject obj = JvnServerImpl.jvnGetServer().jvnCreateObject(appObject);
		obj.jvnUnLock();
		JvnServerImpl.jvnGetServer().jvnRegisterObject("IRC", obj);
		
		System.out.println("The object in RegisterObject "+obj);
		
		return java.lang.reflect.Proxy.newProxyInstance(
				obj.getClass().getClassLoader(),
				obj.getClass().getInterfaces(),
				new AppObjInvocationHandler(obj));
	}
	public Object invoke(Object o, Method m, Object[] arg)
			throws Throwable {
		Object result = null;
		boolean execute = true;
		
		try {
			
			if(m.isAnnotationPresent(Ecriture.class)){
				((JvnObject) o).jvnLockWrite();
			}
			else if(m.isAnnotationPresent(Lecture.class)){
				((JvnObject) o).jvnLockRead();
			}else{
				execute = false;
			}
			
			if(execute){
				result = m.invoke(ob.jvnGetObjectState(), arg);
			}
			
		} catch (Exception e) {
				
		}

		return result;
	}
	
	

	
	
	
}
