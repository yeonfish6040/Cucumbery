package com.jho5245.cucumbery.commands;

import com.jho5245.cucumbery.util.MessageUtil;
import com.jho5245.cucumbery.util.Method;
import com.jho5245.cucumbery.util.storage.data.Permission;
import com.jho5245.cucumbery.util.storage.data.Prefix;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class Workbench implements CommandExecutor
{
  public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args)
  {
    if (!Method.hasPermission(sender, Permission.CMD_WORKBENCH, true))
    {
      return true;
    }
    String usage = cmd.getUsage().replace("/<command> ", ""), consoleUsage = usage.replace("[플레이어 ID]", "<플레이어 ID>");
    if (args.length <= 2)
    {
      if (args.length == 0)
      {
        if (!(sender instanceof Player))
        {
          MessageUtil.shortArg(sender, 1, args);
          MessageUtil.commandInfo(sender, label, consoleUsage);
          return true;
        }
        Player player = (Player) sender;
        player.openWorkbench(player.getLocation(), true);
        MessageUtil.sendMessage(player, Prefix.INFO_WORKBENCH, "제작대를 엽니다.");
        return true;
      }
      Player target = Method.getPlayer(sender, args[0]);
      if (target == null)
      {
        return true;
      }
      target.openWorkbench(target.getLocation(), true);
      if (!(args.length == 2 && args[1].equalsIgnoreCase("true")))
      {
        if (!target.equals(sender))
        {
          MessageUtil.sendMessage(sender, Prefix.INFO_WORKBENCH, target, "에게 제작대를 열어줍니다.");
        }
        MessageUtil.sendMessage(target, Prefix.INFO_WORKBENCH, sender, "이 당신에게 제작대를 열어주었습니다.");
      }
    }
    else
    {
      MessageUtil.longArg(sender, 2, args);
      if (sender instanceof Player)
      {
        MessageUtil.commandInfo(sender, label, usage);
      }
      else
      {
        MessageUtil.commandInfo(sender, label, consoleUsage);
      }
      return true;
    }
    return true;
  }
}
