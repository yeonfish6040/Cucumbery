package com.jho5245.cucumbery.commands.no_groups;

import com.jho5245.cucumbery.util.no_groups.MessageUtil;
import com.jho5245.cucumbery.util.no_groups.Method;
import com.jho5245.cucumbery.util.no_groups.SelectorUtil;
import com.jho5245.cucumbery.util.storage.no_groups.ItemStackUtil;
import com.jho5245.cucumbery.util.storage.data.Permission;
import com.jho5245.cucumbery.util.storage.data.Prefix;
import org.bukkit.command.*;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;

public class CommandSwapHeldItem implements CommandExecutor, TabCompleter
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
        MessageUtil.sendError(player, "맞바꿀 아이템이 존재하지 않습니다");
        return true;
      }
      player.getInventory().setItemInMainHand(offHand);
      player.getInventory().setItemInOffHand(mainHand);
      MessageUtil.info(player, "주로 사용하는 손과 다른 손에 있는 아이템을 서로 맞바꾸었습니다");
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
          MessageUtil.sendError(sender, "맞바꿀 아이템이 존재하지 않습니다");
        }
        return true;
      }
      target.getInventory().setItemInMainHand(offHand);
      target.getInventory().setItemInOffHand(mainHand);
      if (!hideMessage)
      {
        if (!target.equals(sender))
        {
          MessageUtil.info(target, sender, "이 당신의 주로 사용하는 손과 다른 손에 있는 아이템을 서로 맞바꾸었습니다");
        }
        MessageUtil.info(sender, target, "의 주로 사용하는 손과 다른 손에 있는 아이템을 서로 맞바꾸었습니다");
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
  public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args)
  {
    if (!MessageUtil.checkQuoteIsValidInArgs(sender, args = MessageUtil.wrapWithQuote(true, args), true))
    {
      return Collections.singletonList(args[0]);
    }
    int length = args.length;

    if (length == 1)
    {
      return Method.tabCompleterPlayer(sender, args);
    }
    else if (length == 2)
    {
      return Method.tabCompleterBoolean(args, "[명령어 출력 숨김 여부]");
    }

    return Collections.singletonList(Prefix.ARGS_LONG.toString());
  }
}
