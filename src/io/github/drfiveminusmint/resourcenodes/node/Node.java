package io.github.drfiveminusmint.resourcenodes.node;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.World;
import io.github.drfiveminusmint.resourcenodes.util.EnumUtils.MaterialAction;

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
	
	public boolean modifyMaterials(MaterialAction a, Material b) {
		return false;
	}
	
	public int getResetInterval() {
		return resetInterval;
	}
	
	public List<String> getData() {
		List<String> ret = new ArrayList<String>();
		return ret;
	}
	
	public double getRichness() {
		return 0.0;
	}
	
	public void setRichness(double d) {
		return;
	}
	
	public void setResetInterval(int x) {
		resetInterval = x;
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
