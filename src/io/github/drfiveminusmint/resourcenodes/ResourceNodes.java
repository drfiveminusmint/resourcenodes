package io.github.drfiveminusmint.resourcenodes;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.yaml.snakeyaml.Yaml;

import io.github.drfiveminusmint.resourcenodes.command.*;
import io.github.drfiveminusmint.resourcenodes.node.*;

public class ResourceNodes extends JavaPlugin {
	private static ResourceNodes instance;
	private static NodeManager nodeManager;
	private static NodeRepair nodeRepair;
	private Logger logger;
	// Fired when plugin is first enabled
    @Override
    public void onEnable() {
    	
    	nodeManager = new NodeManager();
    	nodeRepair = new NodeRepair(this);
    	
    	this.getCommand("createnode").setExecutor(new CreateNodeCommand());
    	this.getCommand("nodereset").setExecutor(new NodeResetCommand());
    	this.getCommand("nodes").setExecutor(new NodesCommand());
    	
    	File nodesFile = new File(ResourceNodes.getInstance().getDataFolder().getAbsolutePath() + "/nodes.yml");
        InputStream input;
        try {
            input = new FileInputStream(nodesFile);
        } catch (FileNotFoundException e) {
            input = null;
        }
        if (input != null) {
            Map data = new Yaml().loadAs(input, Map.class);
            Map<String, Map<String, ?>> nodesMap = (Map<String, Map<String, ?>>) data.get("nodes");
            for (Map.Entry<String, Map<String, ?>> entry : nodesMap.entrySet()) {
                Map<String,Object> nodeMap = (Map<String, Object>) entry.getValue();
                World w = Bukkit.getWorld((String)nodeMap.get("world"));
                if (w == null) {
                	continue;
                }
                if (nodeManager.getNodeByID(entry.getKey()) != null) {
                	continue;
                }
                Node n;
                String s = (String)nodeMap.getOrDefault("type", "node");
                switch(s) {
                	case "io.github.drfiveminusmint.resourcenodes.node.Quarry":
                		n = new Quarry(entry.getKey(), w, (int)nodeMap.get("interval"));
                		break;
                	case "io.github.drfiveminusmint.resourcenodes.node.Mine":
                		n = new Mine(entry.getKey(), w, (int)nodeMap.get("interval"), (int)nodeMap.get("ore"), (double)nodeMap.get("richness"));
                		break;
                	default:
                		n = new Node(entry.getKey(), w, (int)nodeMap.get("interval"));
                		break;
                }
                
                nodeManager.getNodes().add(n);
            }   

        }
        logger.log(Level.INFO, String.format("Loaded %d resource nodes:", nodeManager.getNodes().size()));
        for (Node n : nodeManager.getNodes()) {
        	logger.log(Level.INFO, n.getID()+" | "+n.getClass().getName());
        }
        nodeManager.runTaskTimer(this, 0, 20*60);
        
    }
    @Override
    public void onLoad() {
        super.onLoad();
        instance = this;
        logger = getLogger();
    }
    
    // Fired when plugin is disabled
    @Override
    public void onDisable() {

    }
    
    public static ResourceNodes getInstance() {
    	return instance;
    }
    
    public static NodeManager getNodeManager() {
    	return nodeManager;
    }
    
    public static NodeRepair getNodeRepair() {
    	return nodeRepair;
    }
    
    public boolean addNodeToFile (Node n) {
    	File nodesFile = new File(ResourceNodes.getInstance().getDataFolder().getAbsolutePath() + "/nodes.yml");
    	try {
    		FileWriter f = new FileWriter(nodesFile, true);
    		BufferedWriter writer = new BufferedWriter(f);
    		writer.newLine();
    		writer.write("  "+n.getID()+":");
    		writer.newLine();
    		writer.write("    world: "+n.getWorld().getName());
    		writer.newLine();
    		writer.write("    type: "+n.getClass().getName());
    		writer.newLine();
    		if(n instanceof Mine) {
    			writer.write("    ore: "+Integer.toString(((Mine) n).getOre()));
    			writer.newLine();
    			writer.write("    richness: "+Double.toString(((Mine) n).getRichness()));
    			writer.newLine();
    		}
    		
    		writer.write("    interval: "+n.getResetInterval());
    		writer.close();
    	} catch (IOException e) {
    		e.printStackTrace();
    		return false;
    	}
    		
    	
    	return true;
    }
}
