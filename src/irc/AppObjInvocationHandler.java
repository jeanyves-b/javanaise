package irc;

import java.io.Serializable;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

import jvn.JvnObject;
import jvn.JvnObjectImpl;
import jvn.JvnServerImpl;

public class AppObjInvocationHandler implements InvocationHandler {

	JvnObject ob;

	public AppObjInvocationHandler(JvnObject o) {
		ob=o;
	}

	public Object invoke(Object arg0, Method arg1, Object[] arg2)
			throws Throwable {
		// TODO Auto-generated method stub
		return null;
	}
	
	
	/*public static Object LookupObject(String jon){
		JvnObject ob=new JvnObjectImpl(o, joi, state);
		JvnServerImpl.jvnGetServer();
		return java.lang.reflect.Proxy.newProxyInstance(
				ob.getClass().getClassLoader(),
				o.getClass().getInterfaces(),
				new AppObjInvocationHandler(ob));
	}
	
	public static Object RegisterObject(String on, Serializable o){
		JvnObject ob=new JvnObjectImpl(o, joi, state)
		JvnServerImpl.jvnGetServer();
		return java.lang.reflect.Proxy.newProxyInstance(
				ob.getClass().getClassLoader(),
				o.getClass().getInterfaces(),
				new AppObjInvocationHandler(ob));
	}
	public Object invoke(Object o, Method m, Object[] arg)
			throws Throwable {
		try {
			Object result = m.invoke(ob.jvnGetObjectState(), arg);
			} catch (Exception e) {...}
			return result; 
		
		
		return null;
	}*/

	
	
	
}
