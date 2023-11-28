package com.jho5245.cucumbery.commands.brigadier;

import com.jho5245.cucumbery.commands.brigadier.base.CommandBase;
import com.jho5245.cucumbery.util.no_groups.MessageUtil;
import com.jho5245.cucumbery.util.no_groups.Method;
import com.jho5245.cucumbery.util.storage.data.Prefix;
import com.jho5245.cucumbery.util.storage.no_groups.CustomConfig;
import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.*;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class CommandSudo2 extends CommandBase
{
  static String[] commands;

  static List<String> commandList = CustomConfig.getCustomConfig("data/brigadier_tab_list.yml").getConfig().getStringList("commands");

  static
  {
    if (commandList.isEmpty())
    {
      commandList = Method.getAllServerCommands();
    }
    CommandSudo2.commands = new String[commandList.size() * 2];
    for (int i = 0; i < commandList.size(); i++)
    {
      CommandSudo2.commands[i * 2] = commandList.get(i);
      CommandSudo2.commands[i * 2 + 1] = "op:" + commandList.get(i);
    }
  }

  final private List<Argument<?>> arguments = new ArrayList<>();

  {
    arguments.add(new EntitySelectorArgument.ManyPlayers("플레이어"));
    arguments.add(new BooleanArgument("명령어 출력 숨김 여부"));
    arguments.add(new GreedyStringArgument("명령어").replaceSuggestions(ArgumentSuggestions.strings(commands)));
  }

  @SuppressWarnings("unchecked")
  public void registerCommand(String command, String permission, String... aliases)
  {
    /*
     * /sudo2 <개체> <명령어 출력 숨김 여부> <명령어>
     */

    CommandAPICommand commandAPICommand = getCommandBase(command, permission, aliases);
    commandAPICommand = commandAPICommand.withArguments(arguments);
    commandAPICommand = commandAPICommand.executesNative((sender, args) ->
    {
      Collection<Player> players = (Collection<Player>) args.get(0);
      boolean hideOutput = (boolean) args.get(1);
      String cmd = (String) args.get(2);
      boolean asOp = cmd.startsWith("op:");
      if (asOp)
      {
        cmd = cmd.substring(3);
      }
      final String originCmd = cmd;
      if (cmd.startsWith("console:"))
      {
        cmd = cmd.substring(8);
        cmd = Method.parseCommandString(Bukkit.getConsoleSender(), cmd);
        for (int i = 0; i < players.size(); i++)
        {
          Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), cmd);
        }
      }
      else
      {
        if (players.size() == 1)
        {
          Entity entity = players.iterator().next();
          cmd = Method.parseCommandString(entity, cmd);
          boolean alreadyOp = entity.isOp();
          if (asOp && !alreadyOp)
          {
            entity.setOp(true);
          }
          Bukkit.getServer().dispatchCommand(entity, cmd);
          if (asOp && !alreadyOp)
          {
            entity.setOp(false);
          }
        }
        else if (players.size() > 1)
        {
          for (Entity entity : players)
          {
            cmd = Method.parseCommandString(entity, cmd);
            boolean alreadyOp = entity.isOp();
            if (asOp && !alreadyOp)
            {
              entity.setOp(true);
            }
            Bukkit.getServer().dispatchCommand(entity, cmd);
            if (asOp && !alreadyOp)
            {
              entity.setOp(false);
            }
            cmd = originCmd;
          }
        }
        else if (!hideOutput)
        {
          MessageUtil.sendError(sender, Prefix.NO_ENTITY);
        }
      }
    });
    commandAPICommand.register();
  }
}
