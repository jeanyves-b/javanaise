package jvn;

import java.util.ArrayList;
import java.io.*;

public class CoordObject {
	int id;
	String name;
	JvnObject jo;
	ArrayList <JvnRemoteServer> users = new ArrayList();
	enum States {R, W}
	ArrayList <States> state = new ArrayList();
}