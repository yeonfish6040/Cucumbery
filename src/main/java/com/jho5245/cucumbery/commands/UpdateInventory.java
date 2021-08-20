package com.jho5245.cucumbery.commands;

import com.jho5245.cucumbery.util.MessageUtil;
import com.jho5245.cucumbery.util.Method;
import com.jho5245.cucumbery.util.storage.data.Permission;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class UpdateInventory implements CommandExecutor
{
  public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args)
  {
    if (!Method.hasPermission(sender, Permission.CMD_UPDATE_INVENTORY, true))
    {
      return true;
    }
    if (args.length <= 2)
    {
      Player player;
      if (args.length == 0)
      {
        if (!(sender instanceof Player))
        {
          MessageUtil.shortArg(sender, 1, args);
          MessageUtil.commandInfo(sender, label, Method.getUsage(cmd));
          return true;
        }
        player = (Player) sender;
      }
      else
      {
        player = Method.getPlayer(sender, args[0]);
        if (player == null)
        {
          return true;
        }
      }
      boolean hideOutput = false;
      if (args.length == 2)
      {
        if (!args[1].equals("true") && !args[1].equals("false"))
        {
          MessageUtil.wrongBool(sender, 2, args);
          return true;
        }
        if (args[1].equals("true"))
        {
          hideOutput = true;
        }
      }
      Method.updateInventory(player);
      if (!hideOutput)
      {
        if (!player.equals(sender))
        {
          MessageUtil.info(sender, player, "의 인벤토리를 업데이트 하였습니다.");
        }
        MessageUtil.info(player, sender, "이(가) 당신의 인벤토리를 업데이트 하였습니다.");
      }
    }
    else
    {
      MessageUtil.longArg(sender, 2, args);
      MessageUtil.commandInfo(sender, label, Method.getUsage(cmd));
      return true;
    }
    return true;
  }
}
