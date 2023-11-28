package com.jho5245.cucumbery.commands.no_groups;

import com.destroystokyo.paper.event.server.AsyncTabCompleteEvent.Completion;
import com.jho5245.cucumbery.Cucumbery;
import com.jho5245.cucumbery.util.nbt.CucumberyTag;
import com.jho5245.cucumbery.util.nbt.NBTAPI;
import com.jho5245.cucumbery.util.no_groups.CommandTabUtil;
import com.jho5245.cucumbery.util.no_groups.CucumberyCommandExecutor;
import com.jho5245.cucumbery.util.no_groups.MessageUtil;
import com.jho5245.cucumbery.util.no_groups.Method;
import com.jho5245.cucumbery.util.storage.data.Constant;
import com.jho5245.cucumbery.util.storage.data.Permission;
import com.jho5245.cucumbery.util.storage.data.Prefix;
import com.jho5245.cucumbery.util.storage.no_groups.ItemStackUtil;
import de.tr7zw.changeme.nbtapi.NBTCompound;
import de.tr7zw.changeme.nbtapi.NBTItem;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class CommandCustomFix implements CucumberyCommandExecutor
{
  @Override
  public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args)
  {
    if (!Method.hasPermission(sender, Permission.CMD_CUSTOM_FIX, true))
    {
      return true;
    }
    if (!MessageUtil.checkQuoteIsValidInArgs(sender, args = MessageUtil.wrapWithQuote(args)))
    {
      return !(sender instanceof BlockCommandSender);
    }
    if (!(sender instanceof Player player))
    {
      MessageUtil.sendError(sender, Prefix.ONLY_PLAYER);
      return true;
    }
    ItemStack item = player.getInventory().getItemInMainHand().clone();
    if (!ItemStackUtil.itemExists(item))
    {
      MessageUtil.sendError(player, Prefix.NO_HOLDING_ITEM);
      return true;
    }
    NBTItem nbtItem = new NBTItem(item);
    NBTCompound itemTag = nbtItem.getCompound(CucumberyTag.KEY_MAIN);
    NBTCompound duraTag = NBTAPI.getCompound(itemTag, CucumberyTag.CUSTOM_DURABILITY_KEY);
    if (!Constant.DURABLE_ITEMS.contains(item.getType()) && duraTag == null)
    {
      MessageUtil.sendError(player, "수리할 수 없는 아이템입니다");
      return true;
    }
    Damageable duraMeta = (Damageable) item.getItemMeta();
    long maxDura = duraTag != null ? duraTag.getLong(CucumberyTag.CUSTOM_DURABILITY_MAX_KEY) : 0;
    long curDura = duraTag != null ? duraTag.getLong(CucumberyTag.CUSTOM_DURABILITY_CURRENT_KEY) : 0;
    if (duraMeta.getDamage() <= 0 && (duraTag == null || curDura <= 0))
    {
      MessageUtil.sendError(player, "내구도가 가득 찬 아이템은 수리할 수 없습니다");
      return true;
    }
    double cost = Cucumbery.config.getDouble("fix-command-cost");
    boolean useCost = Cucumbery.using_Vault_Economy && cost > 0;
    String fixMessage = "주로 사용하는 손에 들고 있는 아이템을 수리했습니다";
    if (player.getGameMode() != GameMode.CREATIVE && useCost)
    {
      double playerMoney = Cucumbery.eco.getBalance(player);
      if (cost > playerMoney)
      {
        MessageUtil.sendError(player, "수리하는데 필요한 비용이 부족합니다. 소지 금액 : rg255,204;" + Constant.Sosu2.format(playerMoney) + "원&r, 수리 비용 : rg255,204;" + Constant.Sosu2.format(cost) + "원");
        return true;
      }
      else
      {
        Cucumbery.eco.withdrawPlayer(player, cost);
      }
      fixMessage = Constant.THE_COLOR_HEX + Constant.Sosu2.format(cost) + "원&r을 지불하고 " + fixMessage + " 소지 금액 : rg255,204;" + Constant.Sosu2.format(Cucumbery.eco.getBalance(player)) + "원";
    }
    if (duraTag == null)
    {
      MessageUtil.sendMessage(player, Prefix.INFO_CUSTOM_FIX, fixMessage);
      duraMeta.setDamage(0);
      item.setItemMeta(duraMeta);
      player.getInventory().setItemInMainHand(item);
      ItemStackUtil.updateInventory(player);
      return true;
    }
    duraTag.setLong(CucumberyTag.CUSTOM_DURABILITY_CURRENT_KEY, 0L);
    player.getInventory().setItemInMainHand(nbtItem.getItem());
    ItemStackUtil.updateInventory(player);
    if (!(args.length == 1 && args[0].equals("true")))
    {
      MessageUtil.sendMessage(player, Prefix.INFO_CUSTOM_FIX, fixMessage);
    }
    return true;
  }

  @Override
  public @NotNull List<Completion> completion(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args, @NotNull Location location)
  {

    int length = args.length;
    if (length == 1)
    {
      return CommandTabUtil.tabCompleterBoolean(args, "[명령어 출력 숨김 여부]");
    }
    return CommandTabUtil.ARGS_LONG;
  }
}
