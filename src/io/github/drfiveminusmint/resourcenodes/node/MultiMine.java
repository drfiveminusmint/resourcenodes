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

public class MultiMine extends Node {
	
	private List<Material> oreMaterials = new ArrayList<Material>();
	private double richness;
	
	public MultiMine(String id, World w, int r, List<String> o, double d) {
		super(id, w, r);
		this.richness = d;
		for (String s : o) {
			if (Material.getMaterial(s) != null) {
				this.oreMaterials.add(Material.getMaterial(s));
			}
		}
	}
	
	public List<String> getOres() {
		List<String> ret = new ArrayList<String>();
		for (Material m : oreMaterials) {
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
			if (loc.getBlock().getType() != Material.STONE && loc.getBlock().getType() != Material.NETHERRACK) {
				continue;
			}
			Random random = new Random();
			if (random.nextInt((int)(1/richness)) == 0) {
				Material material = oreMaterials.get(random.nextInt(oreMaterials.size()));
				loc.getBlock().setType(material);
			}
		}
	}
	
	@Override
	public List<String> getData() {
		List<String> ret = new ArrayList<String>();
		ret.add("Name: " + id);
		ret.add("Ores: ");
		for (Material m : oreMaterials) {
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
				return oreMaterials.add(b);
			case "REMOVE":
				return oreMaterials.remove(b);
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
