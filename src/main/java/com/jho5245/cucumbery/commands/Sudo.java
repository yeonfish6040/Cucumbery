package com.jho5245.cucumbery.commands;

import com.jho5245.cucumbery.util.MessageUtil;
import com.jho5245.cucumbery.util.Method;
import com.jho5245.cucumbery.util.PlaceHolderUtil;
import com.jho5245.cucumbery.util.storage.data.Permission;
import com.jho5245.cucumbery.util.storage.data.Prefix;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class Sudo implements CommandExecutor
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
      Player target = Method.getPlayer(sender, args[0]);
      if (target == null)
      {
        return true;
      }
      String command;
      boolean op = false;
      if (!args[1].equals("true") && !args[1].equals("false"))
      {
        MessageUtil.wrongBool(sender, 2, args);
        return true;
      }
      boolean hideMessage = args[1].equals("true");
      if (args[2].startsWith("op:"))
      {
        op = true;
        args[2] = args[2].substring(3);
      }
      command = MessageUtil.listToString(" ", 2, args.length, args);
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

      if (!op)
      {
        if (!hideMessage)
        {
          if (!target.equals(sender))
          {
            MessageUtil.sendMessage(target, Prefix.INFO_SUDO, sender, "이 당신에게 다음 명령어를 강제로 시행합니다.");
            MessageUtil.sendMessage(target, Prefix.INFO_SUDO, "&e/" + command);
          }
          MessageUtil.sendMessage(sender, Prefix.INFO_SUDO, target, "에게 다음 명령어를 강제로 시행합니다.");
          MessageUtil.sendMessage(sender, Prefix.INFO_SUDO, "&e/" + command);
        }
        target.performCommand(command);
      }
      else
      {
        if (!hideMessage)
        {
          if (!target.equals(sender))
          {
            MessageUtil.sendMessage(target, Prefix.INFO_SUDO, sender + "이 당신에게 다음 명령어를 오피 권한으로 강제로 시행합니다.");
            MessageUtil.sendMessage(target, Prefix.INFO_SUDO, "&e/" + command);
          }
          MessageUtil.sendMessage(sender, Prefix.INFO_SUDO, "&e" + target.getDisplayName() + "&r에게 다음 명령어를 오피 권한으로 강제로 시행합니다.");
          MessageUtil.sendMessage(sender, Prefix.INFO_SUDO, "&e/" + command);
        }
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
    }
    return true;
  }
}
