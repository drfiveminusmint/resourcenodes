package io.github.drfiveminusmint.resourcenodes.node;

import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import io.github.drfiveminusmint.resourcenodes.ResourceNodes;
import io.github.drfiveminusmint.resourcenodes.node.Node;

public class NodeManager extends BukkitRunnable {
	private List<Node> nodes = new ArrayList<Node>();
	
	@Override
	public void run() {
		for(Node node : nodes) {
			if (!node.isActive()) {
				continue;
			}
			if (node.getLastReset()+node.getResetInterval()*1000 > System.currentTimeMillis()) {
				continue;
			}
			node.reset();
		}

	}
	
	public boolean addNode (Node node) {
		if(node == null) {
			return false;
		}
		if(getNodeByID(node.getID()) != null) {
			return false;
		}
		nodes.add(node);
		if(!ResourceNodes.getInstance().addNodeToFile(node)) {
			nodes.remove(node);
			return false;
		}
		return true;
	}
	
	public  Node getNodeByID (String id) {
		if (nodes == null) {
			return null;
		}
		for (Node node : nodes) {
			if (node.getID().equalsIgnoreCase(id)) {
				return node;
			}
		}
		return null;
	}
	
	public void removeNodeByID (String id) {
		Node node = getNodeByID (id);
		if (node == null) {
			return;
		}
		nodes.remove(node);
		
	}
	
	public List<Node> getNodes() {
		return nodes;
	}
	

}
