package io.github.drfiveminusmint.resourcenodes.node;

import org.bukkit.World;

public class Node {
	protected boolean active;
	protected int resetInterval;
	protected long lastReset;
	protected World world;
	protected String id;
	
	public Node(String s, World w, int r) {
		this.active = true;
		this.id = s;
		this.resetInterval = r;
		this.world = w;
		lastReset = System.currentTimeMillis();
	}
	
	public void reset() {
		lastReset = System.currentTimeMillis();
	}
	
	public int getResetInterval() {
		return resetInterval;
	}
	
	public long getLastReset() {
		return lastReset;
	}
	
	public boolean isActive() {
		return active;
	}
	
	public String getID() {
		return id;
	}
	
	public World getWorld() {
		return world;
	}
	
}
