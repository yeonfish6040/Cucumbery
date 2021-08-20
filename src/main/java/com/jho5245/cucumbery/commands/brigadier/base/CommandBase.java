package com.jho5245.cucumbery.commands.brigadier.base;

import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.CommandPermission;

public abstract class CommandBase
{
	public static CommandAPICommand getCommandBase(String command, String permission, String... aliases)
	{
		CommandAPICommand commandAPICommand = new CommandAPICommand(command);
		if (aliases != null && aliases.length > 0)
			commandAPICommand = commandAPICommand.withAliases(aliases);
		commandAPICommand = commandAPICommand.withPermission(CommandPermission.fromString(permission));
		return commandAPICommand;
	}

	public abstract void registerCommand(String command, String permission, String... aliases);
}
