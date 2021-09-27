package com.jho5245.cucumbery.commands.brigadier;

import com.jho5245.cucumbery.commands.brigadier.base.CommandBase;
import com.jho5245.cucumbery.util.MessageUtil;
import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.Argument;
import dev.jorel.commandapi.arguments.EntitySelectorArgument;
import dev.jorel.commandapi.arguments.EntitySelectorArgument.EntitySelector;
import dev.jorel.commandapi.arguments.GreedyStringArgument;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class CommandSendActionbar extends CommandBase
{
	final private List<Argument> argument = new ArrayList<>();
	{
		argument.add(new EntitySelectorArgument("플레이어", EntitySelector.MANY_PLAYERS));
		argument.add( new GreedyStringArgument("메시지"));
	}
	@SuppressWarnings("unchecked")
	public void registerCommand(String command, String permission, String...aliases)
	{
		CommandAPICommand commandAPICommand = getCommandBase(command, permission, aliases);
		commandAPICommand = commandAPICommand.withArguments(argument);
		commandAPICommand = commandAPICommand.executesNative((sender, args) -> {
			Collection<Player> players = (Collection<Player>) args[0];
			String message = (String) args[1];
			for (Player player : players)
			{
				MessageUtil.sendActionBar(player, message);
			}
		});
		commandAPICommand.register();
	}
}
