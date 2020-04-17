package io.github.drfiveminusmint.resourcenodes.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.regions.CuboidRegion;

import io.github.drfiveminusmint.resourcenodes.ResourceNodes;
import io.github.drfiveminusmint.resourcenodes.node.Mine;
import io.github.drfiveminusmint.resourcenodes.node.Node;
import io.github.drfiveminusmint.resourcenodes.node.NodeManager;
import io.github.drfiveminusmint.resourcenodes.node.Quarry;

public class CreateNodeCommand implements CommandExecutor {
	public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {
		if (!command.getName().equalsIgnoreCase("createnode")) {
            return false;
        }
		if (!(commandSender instanceof Player)) {
			commandSender.sendMessage("Oh you fucking idiot, did you really think you could create nodes through the console of all places?");
			return false;
		}
		
		Player player = (Player) commandSender;
		if(args.length == 0) {
			player.sendMessage("/createNode (quarry | mine) [args]"); 
		}
		
		if (args[0].equalsIgnoreCase("quarry")) {
			return createQuarryCommand(player, args);
		}
		if (args[0].equalsIgnoreCase("mine")) {
			return createMineCommand(player, args);
		}
		
		return true;
	}
	
	public boolean createQuarryCommand(Player player, String[] args) {
		if (args.length < 8) {
			player.sendMessage("Usage: /createnode quarry [id] [x1] [y1] [z1] [x2] [y2] [z2]");
			return true;
		}
		
		String nodeName = args[1];
		Vector pos1 = new Vector(Integer.parseInt(args[2]), Integer.parseInt(args[3]), Integer.parseInt(args[4]));
		Vector pos2 = new Vector(Integer.parseInt(args[5]), Integer.parseInt(args[6]), Integer.parseInt(args[7]));
		CuboidRegion c = new CuboidRegion(pos1,pos2);
		Quarry n = new Quarry(nodeName, player.getWorld(), 1000*60);
		if(!ResourceNodes.getNodeManager().addNode(n)) {
			player.sendMessage("Something went wrong adding your node. In other words, get fucked.");
			return true;
		}
		if(!ResourceNodes.getNodeRepair().saveNode(c, n)) {
			player.sendMessage("Something went wrong  trying to save your node as a schematic. Maybe take a look at your code, genius.");
		}
		
		return true;
	}
	
	public boolean createMineCommand(Player player, String[] args) {
		if (args.length < 10) {
			player.sendMessage("Usage: /createnode mine [id] [ore] [density] [x1] [y1] [z1] [x2] [y2] [z2]");
			return true;
		}
		
		String nodeName = args[1];
		int oreID = Integer.parseInt(args[2]);
		double density = Double.parseDouble(args[3]);
		Vector pos1 = new Vector(Integer.parseInt(args[4]), Integer.parseInt(args[5]), Integer.parseInt(args[6]));
		Vector pos2 = new Vector(Integer.parseInt(args[7]), Integer.parseInt(args[8]), Integer.parseInt(args[9]));
		CuboidRegion c = new CuboidRegion(pos1,pos2);
		Mine m = new Mine(nodeName, player.getWorld(), 1000*60, oreID, density);
		if(!ResourceNodes.getNodeManager().addNode(m)) {
			player.sendMessage("Something went wrong adding your node. In other words, get fucked.");
			return true;
		}
		if(!ResourceNodes.getNodeRepair().saveNode(c, m)) {
			player.sendMessage("Something went wrong  trying to save your node as a schematic. Maybe take a look at your code, genius.");
		}
		
		return true;
	}
}
