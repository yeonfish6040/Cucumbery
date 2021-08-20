package com.jho5245.cucumbery.commands;

import com.jho5245.cucumbery.Cucumbery;
import com.jho5245.cucumbery.util.MessageUtil;
import com.jho5245.cucumbery.util.Method;
import com.jho5245.cucumbery.util.storage.SoundPlay;
import com.jho5245.cucumbery.util.storage.data.Constant;
import com.jho5245.cucumbery.util.storage.data.Permission;
import com.jho5245.cucumbery.util.storage.data.Prefix;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.NotNull;

public class TrashCan implements CommandExecutor
{
  public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args)
  {
    if (!Cucumbery.config.getBoolean("grant-default-permission-to-players") && !Method.hasPermission(sender, Permission.CMD_TRASHCAN, true))
    {
      return true;
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
      Inventory inventory = Bukkit.createInventory(null, 54, Constant.TRASH_CAN + MessageUtil.n2s("&8쓰레기통"));
      player.openInventory(inventory);
      SoundPlay.playSound(player, Sound.BLOCK_CHEST_OPEN, SoundCategory.BLOCKS);
      //MessageUtil.sendMessage(player, Prefix.INFO_TRASHCAN, "쓰레기통을 엽니다.");
    }
    else
    {
      if (!Method.hasPermission(sender, Permission.CMD_TRASHCAN_OTHERS, false))
      {
        MessageUtil.longArg(sender, 0, args);
        MessageUtil.commandInfo(sender, label, "");
        return true;
      }
      Player target = Method.getPlayer(sender, args[0]);
      if (target == null)
      {
        return true;
      }
      String customName = "";
      if (args.length >= 3)
      {
        customName = MessageUtil.listToString(" ",2, args.length, args);
      }
      Inventory inventory = Bukkit.createInventory(null, 54, Constant.TRASH_CAN + ((customName.equals("")) ? MessageUtil.n2s("&8쓰레기통") : MessageUtil.n2s("&8" + customName)));
      target.openInventory(inventory);
      if (!(args.length >= 2 && args[1].equalsIgnoreCase("true")))
      {
        SoundPlay.playSound(target, Sound.BLOCK_CHEST_OPEN, SoundCategory.BLOCKS);
        if (!target.equals(sender))
        {
          MessageUtil.sendMessage(target, Prefix.INFO_TRASHCAN, sender, "이 당신에게 쓰레기통을 열어주었습니다.");
        }
        MessageUtil.sendMessage(sender, Prefix.INFO_TRASHCAN, target, "에게 쓰레기통을 열어줍니다.");
      }
    }
    return true;
  }
}
