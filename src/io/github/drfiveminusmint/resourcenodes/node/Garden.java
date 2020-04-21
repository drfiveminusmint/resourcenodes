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

public class Garden extends Node {
	
	private Material plant;
	private double richness;

	public Garden(String s, World w, int r, String p, double f) {
		super(s, w, r);
		this.plant = Material.getMaterial(p);
		this.richness = f;
	}
	
	@Override
	public void reset() {
		lastReset = System.currentTimeMillis();
		NodeRepair repair = ResourceNodes.getInstance().getNodeRepair();
		repair.pasteNode(this);
		for (BlockVector vector : repair.getNodeRegion(this)) {
			Location loc = new Location(world, vector.getBlockX(), vector.getBlockY(), vector.getBlockZ());
			if (loc.getBlock().getType() != Material.AIR) {
				continue;
			}
			Location underLoc = new Location(world, vector.getBlockX(), vector.getBlockY()-1, vector.getBlockZ());
			if (underLoc.getBlock().getType() != Material.GRASS && underLoc.getBlock().getType() != Material.DIRT) {
				continue;
			}
			Random random = new Random();
			if (random.nextInt((int)(1/richness)) == 0) {
				loc.getBlock().setType(plant);
			}
		}
	}
	
	@Override
	public boolean modifyMaterials(MaterialAction a, Material b) {
		if(a != MaterialAction.CHANGE) {
			return false;
		}
		this.plant = b;
		return true;
	}
	
	@Override
	public List<String> getData() {
		List<String> ret = new ArrayList<String>();
		ret.add("Name: " + id);
		ret.add("Plant: " + plant.name());
		ret.add("Richness: " + Double.toString(richness));
		ret.add("Reset Interval: " + Integer.toString(resetInterval));
		return ret;
	}
	
	public String getPlant() {
		return plant.name();
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
