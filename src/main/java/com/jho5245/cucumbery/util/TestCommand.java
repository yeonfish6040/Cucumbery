package com.jho5245.cucumbery.util;

import de.tr7zw.changeme.nbtapi.NBTContainer;
import de.tr7zw.changeme.nbtapi.NBTItem;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class TestCommand implements CommandExecutor
{
  public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args)
  {
    if (!Method.hasPermission(sender, "asdf", true))
    {
      return true;
    }
    Player player = (Player) sender;
    ItemStack itemStack = player.getInventory().getItemInMainHand();
    NBTContainer nbtContainer = new NBTContainer(MessageUtil.listToString(args));
    NBTItem nbtItem = new NBTItem(itemStack);
    nbtItem.mergeCompound(nbtContainer);
    player.sendMessage(nbtItem.getItem().equals(itemStack) + "");
    return true;
  }
}
