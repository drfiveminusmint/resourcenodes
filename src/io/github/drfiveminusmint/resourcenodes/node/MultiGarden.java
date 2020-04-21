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

public class MultiGarden extends Node {
	
	private List<Material> plants = new ArrayList<Material>();
	private double richness;
	
	public MultiGarden(String s, World w, int r, List<String> p, double f) {
		super(s, w, r);
		// TODO Auto-generated constructor stub
		this.richness = f;
		for(String str : p) {
			Material m = Material.getMaterial(str);
			if(m == null) {
				continue;
			}
			plants.add(m);
		}
	}
	
	public List<String> getPlants() {
		List<String> ret = new ArrayList<String>();
		for (Material m : plants) {
			ret.add(m.name());
		}
		return ret;
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
				Material m = plants.get(random.nextInt(plants.size()));
				loc.getBlock().setType(m);
			}
		}
	}
	
	@Override
	public List<String> getData() {
		List<String> ret = new ArrayList<String>();
		ret.add("Name: " + id);
		ret.add("Plants: ");
		for (Material m : plants) {
			ret.add(m.name());
		}
		ret.add("Richness: " + Double.toString(richness));
		ret.add("Reset Interval: " + Integer.toString(resetInterval));
		return ret;
	}
	
	@Override
	public boolean modifyMaterials(MaterialAction a, Material b) {
		String s = a.toString();
		switch(s) {
			case "ADD":
				return plants.add(b);
			case "REMOVE":
				return plants.remove(b);
			default:
				return false;
		}
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
