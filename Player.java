package com.ehalferty.mccoords;

import java.util.Date;

public class Player {
	
	// Entries expire after 5 minutes
	public static final int EXPIRE_TIME = 5 * 60;
	
	private String name;
	private int x, y, z;
	private Date lastUpdate;
	
	public Player(String name, int x, int y, int z) {
		this.name = name;
		this.x = x;
		this.y = y;
		this.z = z;
		lastUpdate = new Date();
	}
	
	public boolean outdated() {
		return ((((new Date()).getTime() - lastUpdate.getTime()) / 1000) > EXPIRE_TIME);
	}
	
	public String toString() {
		return name + "|" + x + "|" + y + "|" + z + ",";
	}

	public String getName() {
		return name;
	}
}