package io.github.drfiveminusmint.resourcenodes.node;

import java.util.Random;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;

import com.sk89q.worldedit.BlockVector;

import io.github.drfiveminusmint.resourcenodes.ResourceNodes;

public class Mine extends Node {
	
	private Material ore;
	private double richness;
	
	@SuppressWarnings("deprecation")
	public Mine(String s, World w, int r, int o, double v) {
		super(s, w, r);
		this.ore = Material.getMaterial(o);
		this.richness = v;
		// TODO Auto-generated constructor stub
	}
	
	public void reset() {
		
		NodeRepair repair = ResourceNodes.getInstance().getNodeRepair();
		repair.pasteNode(this);
		for (BlockVector vector : repair.getNodeRegion(this)) {
			Location loc = new Location(world, vector.getBlockX(), vector.getBlockY(), vector.getBlockZ());
			if (loc.getBlock().getType() != Material.STONE) {
				continue;
			}
			Random random = new Random();
			if (random.nextInt((int)(1/richness)) == 0) {
				loc.getBlock().setType(ore);
			}
		}
		
	}
	
	@SuppressWarnings("deprecation")
	public int getOre() {
		return ore.getId();
	}
	
	public double getRichness() {
		return richness;
	}

}
