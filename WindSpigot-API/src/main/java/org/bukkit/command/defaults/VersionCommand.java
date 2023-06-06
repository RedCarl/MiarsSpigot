package org.bukkit.command.defaults;

import com.google.common.collect.ImmutableList;
import org.bukkit.command.CommandSender;
import org.bukkit.util.MiarsUtils;

import java.util.Collections;
import java.util.List;

public class VersionCommand extends BukkitCommand {
	public VersionCommand(String name) {
		super(name);

		this.description = "Gets the version of this server including.";
		this.usageMessage = "/version";
		this.setAliases(Collections.singletonList("ver"));
	}

	@Override
	public boolean execute(CommandSender sender, String currentAlias, String[] args) {
		MiarsUtils.getVersion(sender);
		return true;
	}

	@Override
	public List<String> tabComplete(CommandSender sender, String alias, String[] args) {
		return ImmutableList.of();
	}
}
