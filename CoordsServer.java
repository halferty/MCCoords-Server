package com.ehalferty.mccoords;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Hashtable;

public class CoordsServer {

	// Constants
	public static final int PORT = 1338;
	public static final String PASSWORD = "PA55W0RD";
	
	private ServerSocket ss;
	protected static Hashtable<String, Player> players = new Hashtable<String, Player>();
	protected static Hashtable<Socket, DataOutputStream> outputStreams = new Hashtable<Socket, DataOutputStream>();

	public CoordsServer(int port, String password) throws IOException {
		try {
			ss = new ServerSocket(port);
		} catch (IOException e) {
			System.out.println("Could not listen on port " + port);
			e.printStackTrace();
		}
		System.out.println("Listening on " + ss);
		while (true) {
			Socket s = ss.accept();
			System.out.println("Connection from " + s);
			new ServerThread(this, s, password);
		}
	}
	
	public static void main(String[] args) throws Exception {
		System.out.println("Running on port " + PORT + " with password " + PASSWORD);
		new CoordsServer(PORT, PASSWORD);
	}

	public static void update(String name, int x, int y, int z) {
		System.out.println("Adding coords for player " + name);
		players.put(name, new Player(name, x, y, z));
	}
}
