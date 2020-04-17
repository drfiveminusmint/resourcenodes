package io.github.drfiveminusmint.resourcenodes.command;

import io.github.drfiveminusmint.resourcenodes.ResourceNodes;
import io.github.drfiveminusmint.resourcenodes.node.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import com.sk89q.worldedit.entity.Player;

public class NodesCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {
		if(!command.getName().equalsIgnoreCase("nodes")) {
			return false;
		}
		
		if(args.length < 1) {
			return false;
		}
		
		if(args[0].equalsIgnoreCase("list")) {
			String ret = "Nodes: ";
			for (Node n : ResourceNodes.getNodeManager().getNodes()) {
				ret += (n.getID() +", ");
			}
			commandSender.sendMessage(ret);
		}
		return true;
	}

}
