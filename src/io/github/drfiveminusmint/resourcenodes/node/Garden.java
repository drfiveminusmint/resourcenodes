package io.github.drfiveminusmint.resourcenodes.node;

import java.util.Random;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;

import com.sk89q.worldedit.BlockVector;

import io.github.drfiveminusmint.resourcenodes.ResourceNodes;

public class Garden extends Node {
	
	private Material plant;
	private double fertility;

	@SuppressWarnings("deprecation")
	public Garden(String s, World w, int r, int p, double f) {
		super(s, w, r);
		this.plant = Material.getMaterial(p);
		this.fertility = f;
	}
	
	@Override
	public void reset() {
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
			if (random.nextInt((int)(1/fertility)) == 0) {
				loc.getBlock().setType(plant);
			}
		}
	}
	
	@SuppressWarnings("deprecation")
	public int getPlant() {
		return plant.getId();
	}
	
	public double getFertility() {
		return fertility;
	}

}
