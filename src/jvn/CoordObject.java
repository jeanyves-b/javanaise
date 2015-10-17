package jvn;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;
import java.io.*;

public class CoordObject {
	
	JvnObject jo;
	//String name;
	static enum States {R, W, NL};
	//States state;
	ConcurrentHashMap<JvnRemoteServer, States> serverState = new ConcurrentHashMap<JvnRemoteServer, States>();
	
	
}