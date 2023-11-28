package com.jho5245.cucumbery.commands.brigadier;

import com.jho5245.cucumbery.Cucumbery;
import com.jho5245.cucumbery.commands.brigadier.base.CommandBase;
import com.jho5245.cucumbery.util.no_groups.Method;
import com.jho5245.cucumbery.util.storage.no_groups.CustomConfig;
import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.Argument;
import dev.jorel.commandapi.arguments.GreedyStringArgument;
import dev.jorel.commandapi.arguments.IntegerArgument;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

import java.util.Arrays;
import java.util.List;

public class CommandRepeat2 extends CommandBase
{
  static String[] commands;

  static List<String> commandList = CustomConfig.getCustomConfig("data/brigadier_tab_list.yml").getConfig().getStringList("commands");

  static
  {
    if (commandList.isEmpty())
    {
      commandList = Method.getAllServerCommands();
    }
    CommandRepeat2.commands = new String[commandList.size()];
    for (int i = 0; i < commandList.size(); i++)
    {
      CommandRepeat2.commands[i] = commandList.get(i);
    }
  }

  final private  List<Argument<?>> arguments = Arrays.asList(new IntegerArgument("반복 횟수", 1, 10000), new IntegerArgument("딜레이", 0, 20 * 60 * 60),
          new GreedyStringArgument("명령어"));

  public void registerCommand(String command, String permission, String... aliases)
  {
    CommandAPICommand commandAPICommand = getCommandBase(command, permission, aliases);
    commandAPICommand = commandAPICommand.withArguments(arguments);
    commandAPICommand = commandAPICommand.executesNative((sender, args) ->
    {
      CommandSender commandSender = sender.getCallee();
      try
      {
        commandSender.getName();
      }
      catch (Exception e)
      {
        commandSender = sender.getCaller();
      }
      int repeat = (int) args.get(0);
      int delay = (int) args.get(1);
      String commandString = (String) args.get(2);
      final String originalCommandString = commandString;
      if (delay == 0)
      {
        for (int i = 1; i <= repeat; i++)
        {
          commandString = commandString.replace("%repeat%", i + "");
          commandString = Method.parseCommandString(commandSender, commandString);
          Bukkit.dispatchCommand(commandSender, commandString);
          commandString = originalCommandString;
        }
      }
      else
      {
        for (int i = 1; i <= repeat; i++)
        {
          commandString = commandString.replace("%repeat%", i + "");
          String finalCommand1 = commandString;
          CommandSender finalCommandSender = commandSender;
          Bukkit.getServer().getScheduler().runTaskLater(Cucumbery.getPlugin(), () ->
          {
            String finalCommand = Method.parseCommandString(finalCommandSender, finalCommand1);
            Bukkit.dispatchCommand(finalCommandSender, finalCommand);
          }, (long) (i - 1) * (long) delay);
          commandString = originalCommandString;
        }
      }
    });
    commandAPICommand.register();
  }
}
