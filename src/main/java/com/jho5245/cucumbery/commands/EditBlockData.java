package com.jho5245.cucumbery.commands;

import com.jho5245.cucumbery.util.MessageUtil;
import com.jho5245.cucumbery.util.Method;
import com.jho5245.cucumbery.util.nbt.CucumberyTag;
import com.jho5245.cucumbery.util.nbt.NBTAPI;
import com.jho5245.cucumbery.util.storage.BlockDataInfo;
import com.jho5245.cucumbery.util.storage.ComponentUtil;
import com.jho5245.cucumbery.util.storage.ItemStackUtil;
import com.jho5245.cucumbery.util.storage.data.Permission;
import com.jho5245.cucumbery.util.storage.data.Prefix;
import de.tr7zw.changeme.nbtapi.NBTCompound;
import de.tr7zw.changeme.nbtapi.NBTItem;
import org.bukkit.Material;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.jetbrains.annotations.NotNull;

public class EditBlockData implements CommandExecutor
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
				MessageUtil.sendError(sender, ComponentUtil.createTranslate("%s에게는 속성이 없습니다.", item));
				return true;
			}
			String key = args[0];
			String[] values = BlockDataInfo.getBlockDataValues(material, key);
			boolean removal = args[1].equals("--remove");
			if (!removal && values == null)
			{
				if (!Method.equals(args[0], keys))
				{
					MessageUtil.sendError(sender, ComponentUtil.createTranslate("%s은(는) 알 수 없는 속성입니다.", "&e" + args[0]));
					return true;
				}
				MessageUtil.sendError(sender, ComponentUtil.createTranslate("%s은(는) 알 수 없는 값입니다.", "&e" + args[1]));
				return true;
			}
			if (!removal && !Method.equals(args[1], values))
			{
				MessageUtil.sendError(sender, ComponentUtil.createTranslate("%s은(는) 알 수 없는 값입니다.", "&e" + args[1]));
				return true;
			}
			NBTItem nbtItem = new NBTItem(item);
			NBTCompound blockStateTag = nbtItem.getCompound(CucumberyTag.MINECRAFT_BLOCK_STATE_TAG_KEY);
			if (removal)
			{
				if (blockStateTag == null || !blockStateTag.hasKey(key))
				{
					MessageUtil.sendError(sender, ComponentUtil.createTranslate("%s에게는 %s 속성이 없습니다.", item, "&e" + key));
					return true;
				}
				NBTAPI.removeKey(blockStateTag, key);
				playerInventory.setItemInMainHand(nbtItem.getItem());
				Method.updateInventory(player);
				MessageUtil.info(sender, ComponentUtil.createTranslate("%s에서 %s 속성을 제거하였습니다.", item, "&e" + key));
				return true;
			}
			if (blockStateTag == null)
			{
				blockStateTag = nbtItem.addCompound(CucumberyTag.MINECRAFT_BLOCK_STATE_TAG_KEY);
			}
			blockStateTag.setString(key, args[1]);
			playerInventory.setItemInMainHand(nbtItem.getItem());
			Method.updateInventory(player);
			MessageUtil.info(sender, ComponentUtil.createTranslate("%s에서 %s 속성의 값을 %s(으)로 설정하였습니다.", item, "&e" + key, "&e" + args[1]));
		}
		else
		{
			MessageUtil.longArg(sender, 2, args);
			MessageUtil.commandInfo(sender, label, Method.getUsage(cmd));
		}
		return true;
	}
}
