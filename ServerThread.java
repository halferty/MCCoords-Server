package com.ehalferty.mccoords;

import java.io.BufferedReader;
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
				System.out.println("___" + message + "___");
				// Format: "|<password>|<name>|<x>|<y>|<z>|<server address>"
				if (message != null) {
					if (message.length() > 10) {
						String splitMessage[] = message.split("\\|");
						if (splitMessage.length >= 6) {
							String pass = splitMessage[1];
							if (pass.equals(password)) {
								String name = splitMessage[2];
								int x = (int)Double.parseDouble(splitMessage[3]);
								int y = (int)Double.parseDouble(splitMessage[4]);
								int z = (int)Double.parseDouble(splitMessage[5]);
								String addr = splitMessage[6];
								CoordsServer.update(name, x, y, z, addr);
								
								// Response: "<name>|<x>|<y>|<z>|<server address>,etc."
								String reply = "";
								for (Player p: CoordsServer.players.values()) {
									if (!p.outdated()) {
										reply += p;
									}
								}
								reply += "\n";
								dout.print(reply);
								dout.flush();
							}
						}
					}
				}
				din.close();
				dout.close();
				return;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
