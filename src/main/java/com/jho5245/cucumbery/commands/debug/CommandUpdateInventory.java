package com.jho5245.cucumbery.commands.debug;

import com.jho5245.cucumbery.util.Method;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class CommandUpdateInventory implements CommandExecutor
{
  public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args)
  {
    if (!(sender instanceof Player))
    {
      return true;
    }
    Player player = (Player) sender;
    if (args.length == 0)
    {
      Method.updateInventory(player);
    }
    else
    {
      Player target = Bukkit.getPlayer(args[0]);
      if (target != null)
      {
        Method.updateInventory(target);
      }
    }
    return true;
  }
}
