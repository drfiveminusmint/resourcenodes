package io.github.drfiveminusmint.resourcenodes.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.regions.CuboidRegion;

import io.github.drfiveminusmint.resourcenodes.ResourceNodes;
import io.github.drfiveminusmint.resourcenodes.node.*;
import io.github.drfiveminusmint.resourcenodes.util.ChatUtils;

public class CreateNodeCommand implements CommandExecutor {
	public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {
		if (!command.getName().equalsIgnoreCase("createnode")) {
            return false;
        }
		if (!(commandSender instanceof Player)) {
			commandSender.sendMessage(ChatUtils.chatPrefix + "Nodes must be created by a player.");
			return true;
		}
		
		Player player = (Player) commandSender;
		if(args.length == 0) {
			player.sendMessage("/createNode (quarry | mine | garden) [args]"); 
		}
		
		if (args[0].equalsIgnoreCase("quarry")) {
			return createQuarryCommand(player, args);
		}
		if (args[0].equalsIgnoreCase("mine")) {
			return createMineCommand(player, args);
		}
		if (args[0].equalsIgnoreCase("garden")) {
			return createGardenCommand(player, args);
		}
		
		return true;
	}
	
	public boolean createQuarryCommand(Player player, String[] args) {
		if (args.length < 8) {
			player.sendMessage(ChatUtils.chatPrefix + "Usage: /createnode quarry [id] [x1] [y1] [z1] [x2] [y2] [z2]");
			return true;
		}
		
		String nodeName = args[1];
		Vector pos1 = new Vector(Integer.parseInt(args[2]), Integer.parseInt(args[3]), Integer.parseInt(args[4]));
		Vector pos2 = new Vector(Integer.parseInt(args[5]), Integer.parseInt(args[6]), Integer.parseInt(args[7]));
		CuboidRegion c = new CuboidRegion(pos1,pos2);
		Quarry n = new Quarry(nodeName, player.getWorld(), 1000*60);
		if(!ResourceNodes.getInstance().getNodeManager().addNode(n)) {
			player.sendMessage(ChatUtils.chatPrefix + "Something went wrong adding your node.");
			return true;
		}
		if(!ResourceNodes.getInstance().getNodeRepair().saveNode(c, n)) {
			player.sendMessage(ChatUtils.chatPrefix + "Something went wrong  trying to save your node as a schematic.");
		}
		
		return true;
	}
	
	public boolean createMineCommand(Player player, String[] args) {
		if (args.length < 10) {
			player.sendMessage(ChatUtils.chatPrefix + "Usage: /createnode mine [id] [ore] [density] [x1] [y1] [z1] [x2] [y2] [z2]");
			return true;
		}
		
		String nodeName = args[1];
		int oreID = Integer.parseInt(args[2]);
		double density = Double.parseDouble(args[3]);
		Vector pos1 = new Vector(Integer.parseInt(args[4]), Integer.parseInt(args[5]), Integer.parseInt(args[6]));
		Vector pos2 = new Vector(Integer.parseInt(args[7]), Integer.parseInt(args[8]), Integer.parseInt(args[9]));
		CuboidRegion c = new CuboidRegion(pos1,pos2);
		Mine m = new Mine(nodeName, player.getWorld(), 1000*60, oreID, density);
		if(!ResourceNodes.getInstance().getNodeManager().addNode(m)) {
			player.sendMessage(ChatUtils.chatPrefix + "Something went wrong adding your node.");
			return true;
		}
		if(!ResourceNodes.getInstance().getNodeRepair().saveNode(c, m)) {
			player.sendMessage(ChatUtils.chatPrefix + "Something went wrong  trying to save your node as a schematic.");
		}
		
		return true;
	}
	public boolean createGardenCommand(Player player, String[] args) {
		if (args.length < 10) {
			player.sendMessage(ChatUtils.chatPrefix + "Usage: /createnode mine [id] [ore] [density] [x1] [y1] [z1] [x2] [y2] [z2]");
			return true;
		}
		
		String nodeName = args[1];
		int plantID = Integer.parseInt(args[2]);
		double density = Double.parseDouble(args[3]);
		Vector pos1 = new Vector(Integer.parseInt(args[4]), Integer.parseInt(args[5]), Integer.parseInt(args[6]));
		Vector pos2 = new Vector(Integer.parseInt(args[7]), Integer.parseInt(args[8]), Integer.parseInt(args[9]));
		CuboidRegion c = new CuboidRegion(pos1,pos2);
		Garden g = new Garden(nodeName, player.getWorld(), 1000*60, plantID, density);
		if(!ResourceNodes.getInstance().getNodeManager().addNode(g)) {
			player.sendMessage(ChatUtils.chatPrefix + "Something went wrong adding your node.");
			return true;
		}
		if(!ResourceNodes.getInstance().getNodeRepair().saveNode(c, g)) {
			player.sendMessage(ChatUtils.chatPrefix + "Something went wrong  trying to save your node as a schematic.");
		}
		player.sendMessage("Node Successfully Created!");
		return true;
	}
}
