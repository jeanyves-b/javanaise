package jvn;

import java.util.ArrayList;
import java.util.HashMap;
import java.io.*;

public class CoordObject {
	
	JvnObject jo;
	String name;
	static enum States {R, W, NL}
	HashMap<JvnRemoteServer, States> serverState = new HashMap<JvnRemoteServer, States>();
	States state;
	
}