package com.jho5245.cucumbery.commands;

import com.jho5245.cucumbery.util.*;
import com.jho5245.cucumbery.util.storage.component.util.ComponentUtil;
import com.jho5245.cucumbery.util.storage.data.Permission;
import com.jho5245.cucumbery.util.storage.data.Prefix;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;

public class CommandSudo implements CommandExecutor, TabCompleter
{
  public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args)
  {
    if (!Method.hasPermission(sender, Permission.CMD_SUDO, true))
    {
      return true;
    }
    String usage = cmd.getUsage().replace("/<command> ", "");
    if (args.length < 3)
    {
      MessageUtil.shortArg(sender, 3, args);
      MessageUtil.commandInfo(sender, label, usage);
      return true;
    }
    else
    {
      List<Player> targets = SelectorUtil.getPlayers(sender, args[0]);
      if (targets == null)
      {
        return true;
      }
      String command = MessageUtil.listToString(" ", 2, args.length, args);
      boolean op = false;
      if (!args[1].equals("true") && !args[1].equals("false"))
      {
        MessageUtil.wrongBool(sender, 2, args);
        return true;
      }
      boolean hideMessage = args[1].equals("true");
      if (command.startsWith("op:"))
      {
        op = true;
        command = command.substring(3);
      }
      final String finalCommand = command;
      if (!hideMessage)
      {
        if (op)
        {
          MessageUtil.sendMessage(sender, Prefix.INFO_SUDO, ComponentUtil.createTranslate("%s에게 다음 명령어를 오피 권한으로 강제로 시행합니다.", targets));
          MessageUtil.sendMessage(sender, Prefix.INFO_SUDO, "&e/" + command);
          if (!(sender instanceof Player player && targets.contains(player)))
          {
            MessageUtil.sendMessage(targets, Prefix.INFO_SUDO, ComponentUtil.createTranslate("%s이(가) 당신에게 다음 명령어를 오피 권한으로 강제로 시행합니다.", sender));
            MessageUtil.sendMessage(targets, Prefix.INFO_SUDO, "&e/" + command);
          }
        }
        else
        {
          MessageUtil.sendMessage(sender, Prefix.INFO_SUDO, ComponentUtil.createTranslate("%s에게 다음 명령어를 강제로 시행합니다.", targets));
          MessageUtil.sendMessage(sender, Prefix.INFO_SUDO, "&e/" + command);
          if (!(sender instanceof Player player && targets.contains(player)))
          {
            MessageUtil.sendMessage(targets, Prefix.INFO_SUDO, ComponentUtil.createTranslate("%s이(가) 당신에게 다음 명령어를 강제로 시행합니다.", sender));
            MessageUtil.sendMessage(targets, Prefix.INFO_SUDO, "&e/" + command);
          }
        }
      }
      for (Player target : targets)
      {
        if (!command.contains("--noph"))
        {
          command = PlaceHolderUtil.placeholder(target, command, null);
        }
        else
        {
          command = command.replaceFirst("--noph", "");
        }
        if (!command.contains("--noeval"))
        {
          command = PlaceHolderUtil.evalString(command);
        }
        else
        {
          command = command.replaceFirst("--noeval", "");
        }
        command = MessageUtil.n2s(command, MessageUtil.N2SType.SPECIAL);
        if (op)
        {
          if (!target.isOp())
          {
            target.setOp(true);
            target.performCommand(command);
            target.setOp(false);
          }
          else
          {
            target.performCommand(command);
          }
        }
        else
        {
          target.performCommand(command);
        }
        command = finalCommand;
      }
    }
    return true;
  }
  public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args)
  {
    if (!MessageUtil.checkQuoteIsValidInArgs(sender, args = MessageUtil.wrapWithQuote(true, args), true))
    {
      return Collections.singletonList(args[0]);
    }
    int length = args.length;

    if (length == 1)
    {
      return Method.tabCompleterPlayer(sender, args, "<플레이어>", true);
    }
    else if (length == 2)
    {
      return Method.tabCompleterBoolean(args, "<명령어 출력 숨김 여부>");
    }
    else
    {
      return TabCompleterUtil.getCommandsTabCompleter(sender, args, 3, true);
    }
  }
}
