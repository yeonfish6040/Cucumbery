package com.jho5245.cucumbery.commands.brigadier;

import com.jho5245.cucumbery.commands.brigadier.base.CommandBase;
import com.jho5245.cucumbery.util.no_groups.MessageUtil;
import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.Argument;
import dev.jorel.commandapi.arguments.EntitySelectorArgument;
import dev.jorel.commandapi.arguments.GreedyStringArgument;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class CommandSendTitle extends CommandBase
{
	final private List<Argument<?>> argument = new ArrayList<>();

	{
		argument.add(new EntitySelectorArgument.ManyPlayers("플레이어"));
		argument.add(new GreedyStringArgument("메시지"));
	}

	@SuppressWarnings("unchecked")
	public void registerCommand(String command, String permission, String... aliases)
	{
		CommandAPICommand commandAPICommand = getCommandBase(command, permission, aliases);
		commandAPICommand = commandAPICommand.withArguments(argument);
		commandAPICommand = commandAPICommand.executesNative((sender, args) ->
		{
			Collection<Player> players = (Collection<Player>) args.get(0);
			String input = (String) args.get(1);
			String title = null;
			String subTtitle = null;
			int fadeIn = -1;
			int stay = -1;
			int fadeOut = -1;
			try
			{
				String[] split = MessageUtil.splitEscape(input, ';');
				if (!split[0].equals(""))
				{
					title = split[0];
				}
				if (!split[1].equals(""))
				{
					subTtitle = split[1];
				}
				fadeIn = Integer.parseInt(split[2]);
				stay = Integer.parseInt(split[3]);
				fadeOut = Integer.parseInt(split[4]);
			}
			catch (Exception ignored)
			{

			}
			for (Player player : players)
			{
				MessageUtil.sendTitle(player, title, subTtitle, fadeIn, stay, fadeOut);
			}
		});
		commandAPICommand.register();
	}
}
