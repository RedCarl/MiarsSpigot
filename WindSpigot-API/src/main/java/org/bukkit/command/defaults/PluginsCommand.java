package org.bukkit.command.defaults;

import org.bukkit.command.CommandSender;
import org.bukkit.util.MiarsUtils;

import java.util.Arrays;

public class PluginsCommand extends BukkitCommand {
	public PluginsCommand(String name) {
		super(name);
		this.description = "Gets a list of plugins running on the server";
		this.usageMessage = "/plugins";
		this.setPermission("bukkit.command.plugins");
		this.setAliases(Arrays.asList("pl"));
	}

	@Override
	public boolean execute(CommandSender sender, String currentAlias, String[] args) {
		MiarsUtils.getPlugins(sender);
		return true;
	}

	// Spigot Start
	@Override
	public java.util.List<String> tabComplete(CommandSender sender, String alias, String[] args) throws IllegalArgumentException {
		return java.util.Collections.emptyList();
	}
	// Spigot End


}
