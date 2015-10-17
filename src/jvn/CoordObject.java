package jvn;

import java.util.concurrent.ConcurrentHashMap;

public class CoordObject {
	
	JvnObject jo;
	//String name;
	static enum States {R, W, NL};
	//States state;
	ConcurrentHashMap<JvnRemoteServer, States> serverState = new ConcurrentHashMap<JvnRemoteServer, States>();
	
	
}