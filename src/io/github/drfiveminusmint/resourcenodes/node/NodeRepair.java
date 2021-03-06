package io.github.drfiveminusmint.resourcenodes.node;

import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.WorldEditException;
import com.sk89q.worldedit.bukkit.BukkitWorld;
import com.sk89q.worldedit.extent.Extent;
import com.sk89q.worldedit.extent.clipboard.BlockArrayClipboard;
import com.sk89q.worldedit.extent.clipboard.Clipboard;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormat;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardReader;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardWriter;
import com.sk89q.worldedit.function.operation.ForwardExtentCopy;
import com.sk89q.worldedit.function.operation.Operation;
import com.sk89q.worldedit.function.operation.Operations;
import com.sk89q.worldedit.regions.CuboidRegion;
import com.sk89q.worldedit.regions.Region;
import com.sk89q.worldedit.session.ClipboardHolder;
import com.sk89q.worldedit.world.registry.WorldData;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import org.bukkit.*;
import org.bukkit.plugin.Plugin;

public class NodeRepair {
	private final Plugin plugin;
	public NodeRepair (Plugin p) {
		this.plugin = p;
	}
	
	public boolean saveNode(CuboidRegion c, Node n) {
		File nodeSaveDir = new File(plugin.getDataFolder(), "NodeSchematics");
		if(!nodeSaveDir.exists()) {
			nodeSaveDir.mkdirs();
		}
		World world = n.getWorld();
		File nodeSaveFile = new File(nodeSaveDir, n.getID()+".schematic");
		com.sk89q.worldedit.world.World weWorld = new BukkitWorld(world);
		WorldData worldData = weWorld.getWorldData();
		Vector origin = new Vector(0,0,0);
		try {
			BlockArrayClipboard clipboard = new BlockArrayClipboard(c);
			clipboard.setOrigin(origin);
			Extent source = WorldEdit.getInstance().getEditSessionFactory().getEditSession(weWorld, -1);
			ForwardExtentCopy copy = new ForwardExtentCopy(source, c, clipboard.getOrigin(), clipboard, clipboard.getOrigin());
			Operations.complete(copy);
			ClipboardWriter writer = ClipboardFormat.SCHEMATIC.getWriter(new FileOutputStream(nodeSaveFile, false));
            writer.write(clipboard, worldData);
            writer.close();
            return true;
		} catch (IOException | WorldEditException e) {
            e.printStackTrace();
            return false;
        }
		
	}
	
	public boolean pasteNode (Node n) {
		File nodeSaveDir = new File(plugin.getDataFolder(), "NodeSchematics");
		if(!nodeSaveDir.exists()) {
			return false;
		}
		World world = n.getWorld();
		File nodeSaveFile = new File(nodeSaveDir, n.getID()+".schematic");
		if(!nodeSaveFile.exists()) {
			return false;
		}
		com.sk89q.worldedit.world.World weWorld = new BukkitWorld(world);
		WorldData worldData = weWorld.getWorldData();
		Clipboard clipboard;

		
		try {
			ClipboardFormat format = ClipboardFormat.findByFile(nodeSaveFile);
			ClipboardReader reader = format.getReader(new FileInputStream(nodeSaveFile));
		    clipboard = reader.read(worldData);
		    Operation operation = new ClipboardHolder(clipboard, worldData)
		            .createPaste(weWorld, worldData)
		            .build();
		    Operations.complete(operation);
		} catch (IOException | WorldEditException e) {
			e.printStackTrace();
			return false;
		}
		
		return true;
	}
	
	public Region getNodeRegion (Node n) {
		File nodeSaveDir = new File(plugin.getDataFolder(), "NodeSchematics");
		if(!nodeSaveDir.exists()) {
			return null;
		}
		World world = n.getWorld();
		File nodeSaveFile = new File(nodeSaveDir, n.getID()+".schematic");
		if(!nodeSaveFile.exists()) {
			return null;
		}
		com.sk89q.worldedit.world.World weWorld = new BukkitWorld(world);
		WorldData worldData = weWorld.getWorldData();
		Clipboard clipboard;

		try {
			ClipboardFormat format = ClipboardFormat.findByFile(nodeSaveFile);
			ClipboardReader reader = format.getReader(new FileInputStream(nodeSaveFile));
		    clipboard = reader.read(worldData);
		} catch(IOException e) {
			e.printStackTrace();
			return null;
		}
		return clipboard.getRegion();
	}
}
