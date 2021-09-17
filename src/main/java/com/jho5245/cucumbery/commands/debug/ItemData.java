package com.jho5245.cucumbery.commands.debug;

import com.jho5245.cucumbery.util.MessageUtil;
import com.jho5245.cucumbery.util.Method;
import com.jho5245.cucumbery.util.SelectorUtil;
import com.jho5245.cucumbery.util.storage.CustomConfig.UserData;
import com.jho5245.cucumbery.util.storage.ItemInfo;
import com.jho5245.cucumbery.util.storage.data.Permission;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.jetbrains.annotations.NotNull;

public class ItemData implements CommandExecutor
{
  public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args)
  {
    if (!Method.hasPermission(sender, Permission.CMD_ITEMDATA, true))
    {
      return true;
    }
    if (!MessageUtil.checkQuoteIsValidInArgs(sender, args = MessageUtil.wrapWithQuote(args)))
    {
      return !(sender instanceof BlockCommandSender);
    }
    String usage = cmd.getUsage().replace("/<command> ", ""), consoleUsage = usage.replace("[플레이어 ID]", "<플레이어 ID>");
    if (args.length == 0)
    {
      if (!(sender instanceof Player player))
      {
        MessageUtil.shortArg(sender, 1, args);
        MessageUtil.commandInfo(sender, label, consoleUsage);
        return true;
      }
      PlayerInventory playerInventory = player.getInventory();
      ItemInfo.sendInfo(player, playerInventory.getItemInMainHand(), true, UserData.SHORTEND_DEBUG_MESSAGE.getBoolean(player.getUniqueId()));
    }
    else if (args.length == 1)
    {
      if (!Permission.CMD_ITEMDATA_OTHERS.has(sender))
      {
        return true;
      }
      Player target = SelectorUtil.getPlayer(sender, args[0]);
      if (target == null)
      {
        return true;
      }
      PlayerInventory targetInventory = target.getInventory();
      ItemStack item = targetInventory.getItemInMainHand();
      if (sender instanceof Player)
      {
        ItemInfo.sendInfo(sender, item, true, UserData.SHORTEND_DEBUG_MESSAGE.getBoolean(((Player) sender).getUniqueId()));
      }
      else
      {
        ItemInfo.sendInfo(sender, item, true, false);
      }
    }
    else
    {
      MessageUtil.longArg(sender, 1, args);
      if (sender instanceof Player)
      {
        MessageUtil.commandInfo(sender, label, usage);
        return true;
      }
      MessageUtil.commandInfo(sender, label, consoleUsage);
      return true;
    }
    return true;
  }
}
