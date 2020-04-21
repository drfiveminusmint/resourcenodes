package io.github.drfiveminusmint.resourcenodes.command;

import io.github.drfiveminusmint.resourcenodes.ResourceNodes;
import io.github.drfiveminusmint.resourcenodes.node.*;
import io.github.drfiveminusmint.resourcenodes.util.ChatUtils;
import io.github.drfiveminusmint.resourcenodes.util.EnumUtils.MaterialAction;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;


public class NodesCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {
		if(!command.getName().equalsIgnoreCase("nodes")) {
			return false;
		}
		
		if(args.length < 1) {
			return false;
		}
		
		if(args[0].equalsIgnoreCase("delete")) {
			if (args.length < 2) {
				commandSender.sendMessage("Usage: /nodes delete [id]");
				return true;
			}
			Node n = ResourceNodes.getInstance().getNodeManager().getNodeByID(args[1]);
			if (n == null) {
				commandSender.sendMessage("Node " + args[1] + " could not be found.");
			}
			if (!ResourceNodes.getInstance().getNodeManager().removeNodeByID(args[1])) {
				return false;
			}
			if (!ResourceNodes.getInstance().deleteNodeFromFile(n, true)) {
				return false;
			}
			commandSender.sendMessage("Node successfully deleted.");
			return true;
		}
		
		if(args[0].equalsIgnoreCase("list")) {
			String ret = ChatUtils.chatPrefix + "Nodes: ";
			for (Node n : ResourceNodes.getInstance().getNodeManager().getNodes()) {
				ret += (n.getID() +", ");
			}
			commandSender.sendMessage(ret);
			return true;
		}
		
		if(args[0].equalsIgnoreCase("reset")) {
			if (args.length < 2) {
				commandSender.sendMessage("Usage: /nodes reset [id]");
				return true;
			}
			Node n = ResourceNodes.getInstance().getNodeManager().getNodeByID(args[1]);
			if (n == null) {
				commandSender.sendMessage(ChatUtils.chatPrefix + "Node "+ args[1]+" could not be found.");
				return true;
			}
			n.reset();
			commandSender.sendMessage(ChatUtils.chatPrefix + "Successfully reset node " + args[1]);
			return true;
		}
		
		if(args[0].equalsIgnoreCase("data")) {
			if (args.length < 2) {
				commandSender.sendMessage("Usage: /nodes data [id]");
				return true;
			}
			Node n = ResourceNodes.getInstance().getNodeManager().getNodeByID(args[1]);
			commandSender.sendMessage(ChatUtils.chatPrefix + String.format("Data for node %s:", n.getID()));
			for (String str : n.getData()) {
				commandSender.sendMessage(str);
			}
			return true;
		}
		
		if(args[0].equalsIgnoreCase("edit")) {
			if (args.length < 4) {
				commandSender.sendMessage("Usage: /nodes edit [id] [action] [value]");
				return true;
			}
			Node n = ResourceNodes.getInstance().getNodeManager().getNodeByID(args[1]);
			if (n == null) {
				commandSender.sendMessage(ChatUtils.chatPrefix + "Node "+ args[1]+" could not be found.");
				return true;
			}
			//there's probably a more elegant way to do this
			switch (args[2].toLowerCase()) {
				case "changeblock":
					Material m = Material.getMaterial(args[3]);
					if(m == null) {
						commandSender.sendMessage(ChatUtils.chatPrefix + " \'"+args[3] + "\' is not a valid material.");
						return true;
					}
					if(!n.modifyMaterials(MaterialAction.CHANGE, m)) {
						commandSender.sendMessage(ChatUtils.chatPrefix + "The action \'changeblock " + args[3] 
								+ "\' could not be completed or is not valid for nodes of type " +n.getClass().getName());
						return true;
					}
					commandSender.sendMessage("Successfully changed block!");
					return true;
				case "addblock":
					Material m1 = Material.getMaterial(args[3]);
					if(m1 == null) {
						commandSender.sendMessage(ChatUtils.chatPrefix + " \'"+args[3] + "\' is not a valid material.");
						return true;
					}
					if(!n.modifyMaterials(MaterialAction.ADD, m1)) {
						commandSender.sendMessage(ChatUtils.chatPrefix + " The action \'addblock " + args[3] 
								+ "\' could not be completed or is not valid for nodes of type " +n.getClass().getName());
						return true;
					}
					commandSender.sendMessage("Successfully added block!");
					break;
				case "removeblock":
					Material m2 = Material.getMaterial(args[3]);
					if(m2 == null) {
						commandSender.sendMessage(ChatUtils.chatPrefix + " \'"+args[3] + "\' is not a valid material.");
						return true;
					}
					if(!n.modifyMaterials(MaterialAction.REMOVE, m2)) {
						commandSender.sendMessage(ChatUtils.chatPrefix + " The action \'removeblock " + args[3] 
								+ "\' could not be completed or is not valid for nodes of type " +n.getClass().getName());
						return true;
					}
					commandSender.sendMessage("Successfully removed block!");
					break;
				case "settimer":
					int x = Integer.parseInt(args[3]);
					if (x <= 0) {
						commandSender.sendMessage(ChatUtils.chatPrefix+" The number you entered is too low, it must be at least 1.");
						return true;
					}
					n.setResetInterval(x);
					commandSender.sendMessage("Successfully set timer!");
					break;
				case "setrichness":
					double d = Double.parseDouble(args[3]);
					if (d > 1) {
						commandSender.sendMessage(ChatUtils.chatPrefix + "Please enter a number between 1.0 and 0.0");
						return true;
					}
					n.setRichness(d);
					commandSender.sendMessage("Successfully set richness!");
					break;
				default:
					commandSender.sendMessage(ChatUtils.chatPrefix + "Invalid edit action. Valid edit actions are: addblock, removeblock, changeblock, settimer, and setrichness.");
					return true;
			}
			ResourceNodes.getInstance().deleteNodeFromFile(n, false);
			ResourceNodes.getInstance().addNodeToFile(n);
			return true;
		}
		
		
		return false;
	}

}
