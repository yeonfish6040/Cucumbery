package com.jho5245.cucumbery.commands.brigadier;

import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.Argument;
import dev.jorel.commandapi.arguments.ArgumentSuggestions;
import dev.jorel.commandapi.arguments.GreedyStringArgument;
import com.jho5245.cucumbery.commands.brigadier.base.CommandBase;
import com.jho5245.cucumbery.util.storage.no_groups.CustomConfig;
import com.jho5245.cucumbery.util.no_groups.Method;
import org.bukkit.Bukkit;

import java.util.ArrayList;
import java.util.List;

public class CommandConsoleSudo2 extends CommandBase
{
	static String[] commands;

	static List<String> commandList = CustomConfig.getCustomConfig("data/brigadier_tab_list.yml").getConfig().getStringList("commands");

	static
	{
		if (commandList.size() == 0)
			commandList = Method.getAllServerCommands();
		CommandConsoleSudo2.commands = new String[commandList.size()];
		for (int i = 0; i < commandList.size(); i++)
		{
			CommandConsoleSudo2.commands[i] = commandList.get(i);
		}
	}

	final private List<Argument<?>> argument = new ArrayList<>();
	{
		argument.add( new GreedyStringArgument("명령어").replaceSuggestions(ArgumentSuggestions.strings(commands)));
	}

	public void registerCommand(String command, String permission, String... aliases)
	{
		CommandAPICommand commandAPICommand = getCommandBase(command, permission, aliases);
		commandAPICommand = commandAPICommand.withArguments(argument);
		commandAPICommand = commandAPICommand.executesNative((sender, args) -> {
			String cmd = (String) args.get(0);
			Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), cmd);
		});
		commandAPICommand.register();
	}
}
