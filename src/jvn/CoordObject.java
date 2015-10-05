package jvn;

import java.util.ArrayList;
import java.io.*;

public class CoordObject {
	int id;
	String name;
	JvnObject jo;
	ArrayList <JvnRemoteServer> users = new ArrayList<JvnRemoteServer>();
	static enum States {R, W, NL}
	ArrayList <States> state = new ArrayList<States>();
}