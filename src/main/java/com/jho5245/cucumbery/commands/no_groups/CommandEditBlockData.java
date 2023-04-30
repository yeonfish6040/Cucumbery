package com.jho5245.cucumbery.commands.no_groups;

import com.jho5245.cucumbery.util.nbt.CucumberyTag;
import com.jho5245.cucumbery.util.nbt.NBTAPI;
import com.jho5245.cucumbery.util.no_groups.MessageUtil;
import com.jho5245.cucumbery.util.no_groups.Method;
import com.jho5245.cucumbery.util.storage.component.util.ComponentUtil;
import com.jho5245.cucumbery.util.storage.component.util.ItemNameUtil;
import com.jho5245.cucumbery.util.storage.data.Constant;
import com.jho5245.cucumbery.util.storage.data.Permission;
import com.jho5245.cucumbery.util.storage.data.Prefix;
import com.jho5245.cucumbery.util.storage.no_groups.BlockDataInfo;
import com.jho5245.cucumbery.util.storage.no_groups.ItemStackUtil;
import de.tr7zw.changeme.nbtapi.NBTCompound;
import de.tr7zw.changeme.nbtapi.NBTItem;
import org.bukkit.Material;
import org.bukkit.command.*;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;

public class CommandEditBlockData implements CommandExecutor, TabCompleter
{
  public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args)
  {
    if (!Method.hasPermission(sender, Permission.CMD_EDIT_BLOCK_DATA, true))
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
    PlayerInventory playerInventory = player.getInventory();
    ItemStack item = playerInventory.getItemInMainHand();
    if (!ItemStackUtil.itemExists(item))
    {
      MessageUtil.sendError(player, Prefix.NO_HOLDING_ITEM);
      return true;
    }
    if (args.length < 2)
    {
      MessageUtil.shortArg(sender, 2, args);
      MessageUtil.commandInfo(sender, label, Method.getUsage(cmd));
    }
    else if (args.length == 2)
    {
      Material material = item.getType();
      String[] keys = BlockDataInfo.getBlockDataKeys(material);
      if (keys == null)
      {
        MessageUtil.sendError(sender, ComponentUtil.translate("%s에게는 속성이 없습니다", item));
        return true;
      }
      String key = args[0];
      String[] values = BlockDataInfo.getBlockDataValues(material, key);
      boolean removal = args[1].equals("--remove");
      if (!removal && values == null)
      {
        if (!Method.equals(args[0], keys))
        {
          MessageUtil.sendError(sender, ComponentUtil.translate("%s은(는) 알 수 없는 속성입니다", Constant.THE_COLOR_HEX + args[0]));
          return true;
        }
        MessageUtil.sendError(sender, ComponentUtil.translate("%s은(는) 알 수 없는 값입니다", Constant.THE_COLOR_HEX + args[1]));
        return true;
      }
      if (!removal && !Method.equals(args[1], values))
      {
        MessageUtil.sendError(sender, ComponentUtil.translate("%s은(는) 알 수 없는 값입니다", Constant.THE_COLOR_HEX + args[1]));
        return true;
      }
      NBTItem nbtItem = new NBTItem(item);
      NBTCompound blockStateTag = nbtItem.getCompound(CucumberyTag.MINECRAFT_BLOCK_STATE_TAG_KEY);
      if (removal)
      {
        if (blockStateTag == null || !blockStateTag.hasTag(key))
        {
          MessageUtil.sendError(sender, ComponentUtil.translate("%s에게는 %s 속성이 없습니다", item, Constant.THE_COLOR_HEX + key));
          return true;
        }
        NBTAPI.removeKey(blockStateTag, key);
        playerInventory.setItemInMainHand(nbtItem.getItem());
        ItemStackUtil.updateInventory(player);
        MessageUtil.info(sender, ComponentUtil.translate("%s에서 %s 속성을 제거했습니다", item, Constant.THE_COLOR_HEX + key));
        return true;
      }
      if (blockStateTag == null)
      {
        blockStateTag = nbtItem.addCompound(CucumberyTag.MINECRAFT_BLOCK_STATE_TAG_KEY);
      }
      blockStateTag.setString(key, args[1]);
      playerInventory.setItemInMainHand(nbtItem.getItem());
      ItemStackUtil.updateInventory(player);
      MessageUtil.info(sender, ComponentUtil.translate("%s에서 %s 속성의 값을 %s(으)로 설정했습니다", item, Constant.THE_COLOR_HEX + key, Constant.THE_COLOR_HEX + args[1]));
    }
    else
    {
      MessageUtil.longArg(sender, 2, args);
      MessageUtil.commandInfo(sender, label, Method.getUsage(cmd));
    }
    return true;
  }

  public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args)
  {
		if (!(sender instanceof Player player))
		{
			return Collections.emptyList();
		}
    if (!MessageUtil.checkQuoteIsValidInArgs(sender, args = MessageUtil.wrapWithQuote(true, args), true))
    {
      return Collections.singletonList(args[0]);
    }
    int length = args.length;
    ItemStack item = player.getInventory().getItemInMainHand();
    if (!ItemStackUtil.itemExists(item))
    {
      return Collections.singletonList(Prefix.NO_HOLDING_ITEM.toString());
    }
    String itemName = MessageUtil.stripColor(ComponentUtil.serialize(ItemNameUtil.itemName(item)));
    String[] keys = BlockDataInfo.getBlockDataKeys(item.getType());
    if (keys == null)
    {
      return Collections.singletonList(itemName + "에는 속성이 없습니다");
    }
    if (length == 1)
    {
      return Method.tabCompleterList(args, "<속성>", keys);
    }
    else if (length == 2)
    {
      String[] values = BlockDataInfo.getBlockDataValues(item.getType(), args[0]);
      if (!args[1].equals("--remove") && values == null)
      {
        if (!Method.equals(args[0], keys))
        {
          return Collections.singletonList(args[0] + MessageUtil.getFinalConsonant(args[0], MessageUtil.ConsonantType.은는) + " 잘못되거나 알 수 없는 속성입니다");
        }
        return Collections.singletonList(args[1] + MessageUtil.getFinalConsonant(args[1], MessageUtil.ConsonantType.은는) + " 잘못되거나 알 수 없는 값입니다");
      }
      return Method.tabCompleterList(args, Method.addAll(Method.arrayToList(values), "--remove"), "<값>");
    }

    return Collections.singletonList(Prefix.ARGS_LONG.toString());
  }
}
