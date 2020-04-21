package io.github.drfiveminusmint.resourcenodes.node;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.World;

import io.github.drfiveminusmint.resourcenodes.ResourceNodes;

public class Quarry extends Node {

	public Quarry(String s, World w, int r) {
		super(s, w, r);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public void reset() {
		ResourceNodes.getInstance().getNodeRepair().pasteNode(this);
		lastReset = System.currentTimeMillis();
	}
	
	@Override
	public List<String> getData() {
		List<String> ret = new ArrayList<String>();
		ret.add("Name: " + id);
		ret.add("Reset Interval: " + resetInterval);
		return ret;
	}
}
