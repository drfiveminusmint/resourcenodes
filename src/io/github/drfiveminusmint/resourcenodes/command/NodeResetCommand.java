package io.github.drfiveminusmint.resourcenodes.command;

import io.github.drfiveminusmint.resourcenodes.ResourceNodes;
import io.github.drfiveminusmint.resourcenodes.node.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class NodeResetCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {
		if (!command.getName().equalsIgnoreCase("nodereset")) {
			return false;
		}
		if (args.length < 1) {
			return false;
		}
		Node n = ResourceNodes.getInstance().getNodeManager().getNodeByID(args[0]);
		if (n == null) {
			commandSender.sendMessage("Node "+ args[0]+" could not be found.");
			return true;
		}
		n.reset();
		commandSender.sendMessage("Successfully reset node " + args[0]);
		return true;
	}

}
