package com.ehalferty.mccoords;

import java.io.BufferedReader;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

public class ServerThread extends Thread {
	
	private Socket socket;
	private BufferedReader din;
	private PrintWriter dout;
	private String password;

	public ServerThread(CoordsServer coordsServer, Socket s, String password) {
		this.socket = s;
		this.password = password;
		start();
	}
	
	public void run() {
		try {
			din = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			dout = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
		} catch (IOException e) {
			System.out.println("Error opening reader or writer for client stream");
			e.printStackTrace();
		}
		try {
			while(true) {
				String message = din.readLine();
				// Format: "|<password>|<name>|<x>|<y>|<z>"
				
				String splitMessage[] = message.split("\\|");
				if (splitMessage.length == 6) {
					String pass = splitMessage[1];
					if (pass.equals(password)) {
						String name = splitMessage[2];
						int x = Integer.parseInt(splitMessage[3]);
						int y = Integer.parseInt(splitMessage[4]);
						int z = Integer.parseInt(splitMessage[5]);
						CoordsServer.update(name, x, y, z);
						
						// Response: "<name>|<x>|<y>|<z>,<name>|<x>|<y>|<z>,etc."
						String reply = "";
						for (Player p: CoordsServer.players.values()) {
							if (!p.outdated()) {
								reply += p;
							}
						}
						dout.print(reply);
						dout.flush();
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
