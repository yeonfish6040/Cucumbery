package com.jho5245.cucumbery.commands;

import com.jho5245.cucumbery.util.MessageUtil;
import com.jho5245.cucumbery.util.Method;
import com.jho5245.cucumbery.util.SelectorUtil;
import com.jho5245.cucumbery.util.storage.ItemStackUtil;
import com.jho5245.cucumbery.util.storage.data.Permission;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class SwapHeldItem implements CommandExecutor
{
  public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args)
  {
    if (!Method.hasPermission(sender, Permission.CMD_SWAPHELDITEM, true))
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
      ItemStack mainHand = player.getInventory().getItemInMainHand(), offHand = player.getInventory().getItemInOffHand();
      if (!ItemStackUtil.itemExists(mainHand) && !ItemStackUtil.itemExists(offHand))
      {
        MessageUtil.sendError(player, "맞바꿀 아이템이 존재하지 않습니다.");
        return true;
      }
      player.getInventory().setItemInMainHand(offHand);
      player.getInventory().setItemInOffHand(mainHand);
      MessageUtil.info(player, "주로 사용하는 손과 다른 손에 있는 아이템을 서로 맞바꾸었습니다.");
    }
    else if (args.length <= 2)
    {
      Player target = SelectorUtil.getPlayer(sender, args[0]);
      if (target == null)
      {
        return true;
      }
      ItemStack mainHand = target.getInventory().getItemInMainHand(), offHand = target.getInventory().getItemInOffHand();
      boolean hideMessage = args.length == 2 && args[1].equals("true");
      if (!ItemStackUtil.itemExists(mainHand) && !ItemStackUtil.itemExists(offHand))
      {
        if (!hideMessage)
        {
          MessageUtil.sendError(sender, "맞바꿀 아이템이 존재하지 않습니다.");
        }
        return true;
      }
      target.getInventory().setItemInMainHand(offHand);
      target.getInventory().setItemInOffHand(mainHand);
      if (!hideMessage)
      {
        if (!target.equals(sender))
        {
          MessageUtil.info(target, sender, "이 당신의 주로 사용하는 손과 다른 손에 있는 아이템을 서로 맞바꾸었습니다.");
        }
        MessageUtil.info(sender, target, "의 주로 사용하는 손과 다른 손에 있는 아이템을 서로 맞바꾸었습니다.");
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
    }
    return true;
  }
}
