package io.github.drfiveminusmint.resourcenodes.command;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
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
		
		switch(args[0].toLowerCase()) {
			case "quarry":
				return createQuarryCommand(player, args);
			case "mine":
				return createMineCommand(player, args);
			case "garden":
				return createGardenCommand(player, args);
			case "multimine":
				return createMultiMineCommand(player, args);
			case "multigarden":
				return createMultiGardenCommand(player, args);
			default:
				player.sendMessage(ChatUtils.chatPrefix + String.format(" %s is not a valid node type.", args[0]));
				return true;
		}
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
		Quarry n = new Quarry(nodeName, player.getWorld(), 300);
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
		
		if(Material.getMaterial(args[2].toUpperCase()) == null) {
			player.sendMessage(ChatUtils.chatPrefix + args[2].toUpperCase() + " is not a valid material.");
		}
		
		String nodeName = args[1];
		double density = Double.parseDouble(args[3]);
		Vector pos1 = new Vector(Integer.parseInt(args[4]), Integer.parseInt(args[5]), Integer.parseInt(args[6]));
		Vector pos2 = new Vector(Integer.parseInt(args[7]), Integer.parseInt(args[8]), Integer.parseInt(args[9]));
		CuboidRegion c = new CuboidRegion(pos1,pos2);
		Mine m = new Mine(nodeName, player.getWorld(), 300, args[2].toUpperCase(), density);
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
			player.sendMessage(ChatUtils.chatPrefix + "Usage: /createnode mine [id] [plant] [density] [x1] [y1] [z1] [x2] [y2] [z2]");
			return true;
		}
		
		if(Material.getMaterial(args[2].toUpperCase()) == null) {
			player.sendMessage(ChatUtils.chatPrefix + args[2].toUpperCase() + " is not a valid material.");
		}
		
		String nodeName = args[1];
		double density = Double.parseDouble(args[3]);
		Vector pos1 = new Vector(Integer.parseInt(args[4]), Integer.parseInt(args[5]), Integer.parseInt(args[6]));
		Vector pos2 = new Vector(Integer.parseInt(args[7]), Integer.parseInt(args[8]), Integer.parseInt(args[9]));
		CuboidRegion c = new CuboidRegion(pos1,pos2);
		Garden g = new Garden(nodeName, player.getWorld(), 300, args[2].toUpperCase(), density);
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
	
	public boolean createMultiMineCommand(Player player, String[] args) {
		if (args.length < 10) {
			player.sendMessage(ChatUtils.chatPrefix + "Usage: /createnode multimine [id] [ore] [density] [x1] [y1] [z1] [x2] [y2] [z2]");
			return true;
		}
		
		if(Material.getMaterial(args[2].toUpperCase()) == null) {
			player.sendMessage(ChatUtils.chatPrefix + args[2].toUpperCase() + " is not a valid material.");
		}
		
		String nodeName = args[1];
		double density = Double.parseDouble(args[3]);
		Vector pos1 = new Vector(Integer.parseInt(args[4]), Integer.parseInt(args[5]), Integer.parseInt(args[6]));
		Vector pos2 = new Vector(Integer.parseInt(args[7]), Integer.parseInt(args[8]), Integer.parseInt(args[9]));
		CuboidRegion c = new CuboidRegion(pos1,pos2);
		List<String> l = new ArrayList<String>();
		l.add(args[2]);
		MultiMine m = new MultiMine(nodeName, player.getWorld(), 300, l, density);
		if(!ResourceNodes.getInstance().getNodeManager().addNode(m)) {
			player.sendMessage(ChatUtils.chatPrefix + "Something went wrong adding your node.");
			return true;
		}
		if(!ResourceNodes.getInstance().getNodeRepair().saveNode(c, m)) {
			player.sendMessage(ChatUtils.chatPrefix + "Something went wrong trying to save your node as a schematic.");
		}
		
		return true;
	}
	
	public boolean createMultiGardenCommand(Player player, String[] args) {
		if (args.length < 10) {
			player.sendMessage(ChatUtils.chatPrefix + "Usage: /createnode multigarden [id] [ore] [density] [x1] [y1] [z1] [x2] [y2] [z2]");
			return true;
		}
		
		if(Material.getMaterial(args[2].toUpperCase()) == null) {
			player.sendMessage(ChatUtils.chatPrefix + args[2].toUpperCase() + " is not a valid material.");
		}
		
		String nodeName = args[1];
		double density = Double.parseDouble(args[3]);
		Vector pos1 = new Vector(Integer.parseInt(args[4]), Integer.parseInt(args[5]), Integer.parseInt(args[6]));
		Vector pos2 = new Vector(Integer.parseInt(args[7]), Integer.parseInt(args[8]), Integer.parseInt(args[9]));
		CuboidRegion c = new CuboidRegion(pos1,pos2);
		List<String> l = new ArrayList<String>();
		l.add(args[2]);
		MultiGarden m = new MultiGarden(nodeName, player.getWorld(), 300, l, density);
		if(!ResourceNodes.getInstance().getNodeManager().addNode(m)) {
			player.sendMessage(ChatUtils.chatPrefix + "Something went wrong adding your node.");
			return true;
		}
		if(!ResourceNodes.getInstance().getNodeRepair().saveNode(c, m)) {
			player.sendMessage(ChatUtils.chatPrefix + "Something went wrong trying to save your node as a schematic.");
		}
		
		return true;
	}
}
