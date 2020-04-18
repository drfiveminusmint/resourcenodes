package io.github.drfiveminusmint.resourcenodes;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
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
    	this.getCommand("nodes").setExecutor(new NodesCommand());
    	
    	fixFileSpacing();
    	loadNodes(true);
        
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
    
    public NodeManager getNodeManager() {
    	return nodeManager;
    }
    
    public NodeRepair getNodeRepair() {
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
    		String s = n.getClass().getName();
    		switch (s) {
    			case "io.github.drfiveminusmint.resourcenodes.node.Quarry":
    				break;
    			case "io.github.drfiveminusmint.resourcenodes.node.Mine":
    				writer.write("    ore: "+Integer.toString(((Mine) n).getOre()));
        			writer.newLine();
        			writer.write("    richness: "+Double.toString(((Mine) n).getRichness()));
        			writer.newLine();
        			break;
    			case "io.github.drfiveminusmint.resourcenodes.node.Garden":
    				writer.write("    plant: "+Integer.toString(((Garden) n).getPlant()));
        			writer.newLine();
        			writer.write("    fertility: "+Double.toString(((Garden) n).getFertility()));
        			writer.newLine();
        			break;
        		default:
        			break;
    		}
    		writer.write("    interval: "+n.getResetInterval());
    		writer.close();
    	} catch (IOException e) {
    		e.printStackTrace();
    		return false;
    	}
    		
    	
    	return true;
    }
    
    public void loadNodes (boolean init) {
    	if (!init) {
    		try {
    			nodeManager.cancel();
    		} catch (IllegalStateException e) {
    			e.printStackTrace();
    		}
    	}
    	
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
                	case "io.github.drfiveminusmint.resourcenodes.node.Garden":
                		n = new Garden(entry.getKey(), w, (int)nodeMap.get("interval"), (int)nodeMap.get("plant"), (double)nodeMap.get("fertility"));
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
    
    public boolean deleteNodeFromFile (Node n) {
    	try {
    		File nodesFile = new File(ResourceNodes.getInstance().getDataFolder().getAbsolutePath() + "/nodes.yml");
    		String tempNodesPath = ResourceNodes.getInstance().getDataFolder().getAbsolutePath() + "/tempnodes.yml";
    		File tempNodesFile = new File(tempNodesPath);
    		File dir = new File(ResourceNodes.getInstance().getDataFolder().getAbsolutePath());
    		if(!tempNodesFile.exists()) {
    			tempNodesFile = File.createTempFile("tempnodes", ".yml", dir);
    		}
    		PrintWriter wr = new PrintWriter(tempNodesFile);
    		FileReader reader = new FileReader(nodesFile);
    		BufferedReader bufferedReader = new BufferedReader(reader);
    		boolean nodeFound = false;
    		String line = bufferedReader.readLine();
    		while (line != null) {
    			if (line.equalsIgnoreCase("  "+n.getID()+ ":")) {
    				logger.log(Level.INFO,"Node found");
    				nodeFound = true;
    				break;	
    			}
    			wr.println(line);
    			line = bufferedReader.readLine();
    		}
    	
    		if(!nodeFound) {
    			bufferedReader.close();
        		wr.close();
    			return false;
    		}

    		line = bufferedReader.readLine();
    		while (line != null) {
    			if (line.length() < 2) {
    				break;
    			}
    			line = bufferedReader.readLine();
    		}

    		while (line != null) {
    			wr.println(line);
    			line = bufferedReader.readLine();
    		}
    		bufferedReader.close();
    		wr.close();
    		boolean success = tempNodesFile.renameTo(nodesFile);
    		tempNodesFile.delete();
    		return true;
    	} catch (IOException e) {
    		e.printStackTrace();
    		return false;
    	}
    }
    
    public void fixFileSpacing () {
    	try {
    		File nodesFile = new File(ResourceNodes.getInstance().getDataFolder().getAbsolutePath() + "/nodes.yml");
    		String tempNodesPath = ResourceNodes.getInstance().getDataFolder().getAbsolutePath() + "/tempnodes.yml";
    		File tempNodesFile = new File(tempNodesPath);
    		File dir = new File(ResourceNodes.getInstance().getDataFolder().getAbsolutePath());
    		if(!tempNodesFile.exists()) {
    			tempNodesFile = File.createTempFile("tempnodes", ".yml", dir);
    		}
    		PrintWriter wr = new PrintWriter(tempNodesFile);
    		FileReader reader = new FileReader(nodesFile);
    		BufferedReader bufferedReader = new BufferedReader(reader);
    		String line1 = bufferedReader.readLine();
    		String line2 = bufferedReader.readLine();
    		while (line2 != null) {
    			if (line1.length() < 2 && line2.length() <2) {
    				line2 = bufferedReader.readLine();
    				continue;
    			}
    			wr.println(line1);
    			line1 = line2;
    			line2 = bufferedReader.readLine();
    		}
    		if (line1.length() > 2) {
    			wr.println(line1);
    		}
    		bufferedReader.close();
    		wr.close();
    		boolean success = tempNodesFile.renameTo(nodesFile);
    		tempNodesFile.delete();
    		
    	} catch (IOException e) {
    		e.printStackTrace();
    		return;
    	}
    }
}
