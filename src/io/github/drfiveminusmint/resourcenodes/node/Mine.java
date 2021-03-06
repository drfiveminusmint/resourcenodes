package io.github.drfiveminusmint.resourcenodes.node;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;

import com.sk89q.worldedit.BlockVector;

import io.github.drfiveminusmint.resourcenodes.ResourceNodes;
import io.github.drfiveminusmint.resourcenodes.util.EnumUtils.MaterialAction;

public class Mine extends Node {
	
	private Material ore;
	private double richness;
	
	public Mine(String s, World w, int r, String o, double v) {
		super(s, w, r);
		this.ore = Material.getMaterial(o);
		this.richness = v;
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public void reset() {
		lastReset = System.currentTimeMillis();
		NodeRepair repair = ResourceNodes.getInstance().getNodeRepair();
		repair.pasteNode(this);
		for (BlockVector vector : repair.getNodeRegion(this)) {
			Location loc = new Location(world, vector.getBlockX(), vector.getBlockY(), vector.getBlockZ());
			if (loc.getBlock().getType() != Material.STONE && loc.getBlock().getType() != Material.NETHERRACK) {
				continue;
			}
			Random random = new Random();
			if (random.nextInt((int)(1/richness)) == 0) {
				loc.getBlock().setType(ore);
			}
		}
	}
	
	@Override
	public boolean modifyMaterials(MaterialAction a, Material b) {
		if(a != MaterialAction.CHANGE) {
			return false;
		}
		this.ore = b;
		return true;
	}
	
	@Override
	public List<String> getData() {
		List<String> ret = new ArrayList<String>();
		ret.add("Name: " + id);
		ret.add("Ore: " + ore.name());
		ret.add("Richness: " + Double.toString(richness));
		ret.add("Reset Interval: " + Integer.toString(resetInterval));
		return ret;
	}
	
	public String getOre() {
		return ore.name();
	}
	
	@Override
	public double getRichness() {
		return richness;
	}
	
	@Override
	public void setRichness(double d) {
		richness = d;
	}

}
