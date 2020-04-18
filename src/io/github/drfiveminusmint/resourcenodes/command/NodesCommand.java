package io.github.drfiveminusmint.resourcenodes.command;

import io.github.drfiveminusmint.resourcenodes.ResourceNodes;
import io.github.drfiveminusmint.resourcenodes.node.*;
import io.github.drfiveminusmint.resourcenodes.util.ChatUtils;
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
				return false;
			}
			Node n = ResourceNodes.getInstance().getNodeManager().getNodeByID(args[1]);
			if (n == null) {
				commandSender.sendMessage("Node " + args[1] + " could not be found.");
			}
			if (!ResourceNodes.getInstance().getNodeManager().removeNodeByID(args[1])) {
				return false;
			}
			if (!ResourceNodes.getInstance().deleteNodeFromFile(n)) {
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
				return false;
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
		
		
		return false;
	}

}
