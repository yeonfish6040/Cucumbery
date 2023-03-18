package com.jho5245.cucumbery.commands.no_groups;

import com.destroystokyo.paper.event.server.AsyncTabCompleteEvent.Completion;
import com.jho5245.cucumbery.util.no_groups.*;

import com.jho5245.cucumbery.util.storage.component.util.ComponentUtil;
import com.jho5245.cucumbery.util.storage.data.Permission;
import com.jho5245.cucumbery.util.storage.data.Prefix;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;

public class CommandSudo implements CucumberyCommandExecutor
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
          MessageUtil.sendMessage(sender, Prefix.INFO_SUDO, ComponentUtil.translate("%s에게 다음 명령어를 오피 권한으로 강제로 시행합니다", targets));
          MessageUtil.sendMessage(sender, Prefix.INFO_SUDO, "rg255,204;/" + command);
          if (!(sender instanceof Player player && targets.contains(player)))
          {
            MessageUtil.sendMessage(targets, Prefix.INFO_SUDO, ComponentUtil.translate("%s이(가) 당신에게 다음 명령어를 오피 권한으로 강제로 시행합니다", sender));
            MessageUtil.sendMessage(targets, Prefix.INFO_SUDO, "rg255,204;/" + command);
          }
        }
        else
        {
          MessageUtil.sendMessage(sender, Prefix.INFO_SUDO, ComponentUtil.translate("%s에게 다음 명령어를 강제로 시행합니다", targets));
          MessageUtil.sendMessage(sender, Prefix.INFO_SUDO, "rg255,204;/" + command);
          if (!(sender instanceof Player player && targets.contains(player)))
          {
            MessageUtil.sendMessage(targets, Prefix.INFO_SUDO, ComponentUtil.translate("%s이(가) 당신에게 다음 명령어를 강제로 시행합니다", sender));
            MessageUtil.sendMessage(targets, Prefix.INFO_SUDO, "rg255,204;/" + command);
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
      return CommandTabUtil.getCommandsTabCompleter(sender, args, 3, true);
    }
  }

  /**
   * Requests a list of possible completions for a command argument.
   *
   * @param sender   Source of the command.  For players tab-completing a
   *                 command inside a command block, this will be the player, not
   *                 the command block.
   * @param cmd      the command to be executed.
   * @param label    Alias of the command which was used
   * @param args     The arguments passed to the command, including final
   *                 partial argument to be completed
   * @param location The location of this command was executed.
   * @return A List of possible completions for the final argument, or an empty list.
   */
  @Override
  public @NotNull List<Completion> completion(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args, @NotNull Location location)
  {
    int length = args.length;

    if (length == 1)
    {
      return CommandTabUtil.tabCompleterPlayer(sender, args, "<플레이어>");
    }
    else if (length == 2)
    {
      return CommandTabUtil.tabCompleterBoolean(args, "<명령어 출력 숨김 여부>");
    }
    else
    {
      return CommandTabUtil.getCommandsTabCompleter2(sender, args, 3, true);
    }
  }
}
