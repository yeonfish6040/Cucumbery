package com.jho5245.cucumbery.commands.no_groups;

import com.jho5245.cucumbery.Cucumbery;
import com.jho5245.cucumbery.util.no_groups.MessageUtil;
import com.jho5245.cucumbery.util.no_groups.Method;
import com.jho5245.cucumbery.util.nbt.CucumberyTag;
import com.jho5245.cucumbery.util.nbt.NBTAPI;
import com.jho5245.cucumbery.util.storage.no_groups.ItemStackUtil;
import com.jho5245.cucumbery.util.storage.data.Constant;
import com.jho5245.cucumbery.util.storage.data.Permission;
import com.jho5245.cucumbery.util.storage.data.Prefix;
import de.tr7zw.changeme.nbtapi.NBTCompound;
import de.tr7zw.changeme.nbtapi.NBTItem;
import org.bukkit.GameMode;
import org.bukkit.command.*;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;

public class CommandCustomFix implements CommandExecutor, TabCompleter
{
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
    ItemStack item = player.getInventory().getItemInMainHand();
    if (!ItemStackUtil.itemExists(item))
    {
      MessageUtil.sendError(player, Prefix.NO_HOLDING_ITEM);
      return true;
    }
    if (!Constant.DURABLE_ITEMS.contains(item.getType()))
    {
      MessageUtil.sendError(player, "수리할 수 없는 아이템입니다");
      return true;
    }
    Damageable duraMeta = (Damageable) item.getItemMeta();
    if (duraMeta.getDamage() <= 0)
    {
      MessageUtil.sendError(player, "내구도가 가득 찬 아이템은 수리할 수 없습니다");
      return true;
    }
    double cost = Cucumbery.config.getDouble("fix-command-cost");
    boolean useCost = Cucumbery.using_Vault_Economy && cost > 0;
    String fixMessage = "주로 사용하는 손에 들고 있는 아이템을 수리하였습니다";
    if (player.getGameMode() != GameMode.CREATIVE && useCost)
    {
      double playerMoney = Cucumbery.eco.getBalance(player);
			if (cost > playerMoney)
			{
				MessageUtil.sendError(player, "수리하는데 필요한 비용이 부족합니다. 소지 금액 : &e" + Constant.Sosu2.format(playerMoney) + "원&r, 수리 비용 : &e" + Constant.Sosu2.format(cost) + "원");
				return true;
			}
			else
			{
				Cucumbery.eco.withdrawPlayer(player, cost);
			}
      fixMessage = "&e" + Constant.Sosu2.format(cost) + "원&r을 지불하고 " + fixMessage + " 소지 금액 : &e" + Constant.Sosu2.format(Cucumbery.eco.getBalance(player)) + "원";
    }
    item = item.clone();
    NBTItem nbtItem = new NBTItem(item);
    NBTCompound itemTag = nbtItem.getCompound(CucumberyTag.KEY_MAIN);
    NBTCompound duraTag = NBTAPI.getCompound(itemTag, CucumberyTag.CUSTOM_DURABILITY_KEY);
    if (duraTag == null)
    {
      MessageUtil.sendMessage(player, Prefix.INFO_CUSTOM_FIX, fixMessage);
      duraMeta.setDamage(0);
      item.setItemMeta(duraMeta);
      player.getInventory().setItemInMainHand(item);
      Method.updateInventory(player);
      return true;
    }
    long maxDura = duraTag.getLong(CucumberyTag.CUSTOM_DURABILITY_MAX_KEY);
    duraTag.setLong(CucumberyTag.CUSTOM_DURABILITY_CURRENT_KEY, maxDura);
    player.getInventory().setItemInMainHand(nbtItem.getItem());
    Method.updateInventory(player);
    if (!(args.length == 1 && args[0].equals("true")))
    {
      MessageUtil.info(player, Prefix.INFO_CUSTOM_FIX, fixMessage);
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
    if (args.length == 1)
    {
      return Method.tabCompleterBoolean(args, "[명령어 출력 숨김 여부]");
    }
    return Collections.singletonList(Prefix.ARGS_LONG.toString());
  }
}
