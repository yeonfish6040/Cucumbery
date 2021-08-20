package com.jho5245.cucumbery.commands.debug;

import com.jho5245.cucumbery.util.MessageUtil;
import com.jho5245.cucumbery.util.Method;
import com.jho5245.cucumbery.util.storage.CustomConfig.UserData;
import com.jho5245.cucumbery.util.storage.ItemInfo;
import com.jho5245.cucumbery.util.storage.data.Permission;
import com.jho5245.cucumbery.util.storage.data.Prefix;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.jetbrains.annotations.NotNull;

public class ItemData implements CommandExecutor
{
	public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args)
	{
		if (!Method.hasPermission(sender, Permission.CMD_ITEMDATA, true))
			return true;
		String usage = cmd.getUsage().replace("/<command> ", ""), consoleUsage = usage.replace("[플레이어 ID]", "<플레이어 ID>");
		switch (cmd.getName())
		{
			case "itemdata":
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
						return true;
					Player target = Method.getPlayer(sender, args[0]);
					if (target == null)
						return true;
					PlayerInventory targetInventory = target.getInventory();
					ItemStack item = targetInventory.getItemInMainHand();
					if (sender instanceof Player)
						ItemInfo.sendInfo(sender, item, true, UserData.SHORTEND_DEBUG_MESSAGE.getBoolean(((Player) sender).getUniqueId()));
					else
						ItemInfo.sendInfo(sender, item, true, false);
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
				break;
			case "itemdata2":
				if (args.length == 0)
				{
					if (sender instanceof Player)
					{
						MessageUtil.shortArg(sender, 1, args);
						MessageUtil.commandInfo(sender, label, usage);
					}
					else
					{
						MessageUtil.shortArg(sender, 2, args);
						MessageUtil.commandInfo(sender, label, consoleUsage);
					}
					return true;
				}
				else if (args.length <= 2)
				{
					if (args.length == 1 && !(sender instanceof Player))
					{
						MessageUtil.shortArg(sender, 2, args);
						MessageUtil.commandInfo(sender, label, consoleUsage);
						return true;
					}
					Player player;
					if (args.length == 1)
						player = (Player) sender;
					else
					{
						if (! Permission.CMD_ITEMDATA_OTHERS.has(sender))
							return true;
						else
							player = Method.getPlayer(sender, args[1]);
						if (player == null)
							return true;
					}
					ItemStack item;
					boolean armor;
					String armorString;
					PlayerInventory playerInventory = player.getInventory();
					MessageUtil.sendMessage(sender, Prefix.INFO_ITEMSTORAGE, "&e-----------------------아이템 정보-----------------------");
					switch (args[0])
					{
						case "helmet" -> {
							item = playerInventory.getHelmet();
							armor = true;
							armorString = "모자";
						}
						case "chestplate" -> {
							item = playerInventory.getChestplate();
							armor = true;
							armorString = "샌즈";
						}
						case "leggings" -> {
							item = playerInventory.getLeggings();
							armor = true;
							armorString = "ㅔㅔㅔ";
						}
						case "boots" -> {
							item = playerInventory.getBoots();
							armor = true;
							armorString = "ㅇㅇ";
						}
						case "offhand" -> {
							item = playerInventory.getItemInOffHand();
							armor = true;
							armorString = "ㅋ";
						}
						case "cursor" -> {
							item = player.getItemOnCursor();
							armor = true;
							armorString = "커서";
						}
						default -> {
							if (!MessageUtil.isInteger(sender, args[0], true))
								return true;
							int slot = Integer.parseInt(args[0]);
							if (!MessageUtil.checkNumberSize(sender, slot, 1, player.getInventory().getSize()))
								return true;
							item = playerInventory.getItem(slot - 1);
							armor = false;
							armorString = null;
							MessageUtil.sendMessage(sender, Prefix.INFO_ITEMSTORAGE, player, "의 인벤토리의 &e" + slot + "번&r 슬롯 아이템");
						}
					}
					if (armor)
						MessageUtil.sendMessage(sender, Prefix.INFO_ITEMSTORAGE, player, "의 &e" + armorString + "&r에 있는 아이템");
					if (sender instanceof Player)
						ItemInfo.sendInfo(sender, item, UserData.SHORTEND_DEBUG_MESSAGE.getBoolean(((Player) sender).getUniqueId()));
					else
						ItemInfo.sendInfo(sender, item);
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
					return true;
				}
				break;
			case "itemdata3": // 엔더상자
				if (args.length == 0)
				{
					if (sender instanceof Player)
					{
						MessageUtil.shortArg(sender, 1, args);
						MessageUtil.commandInfo(sender, label, usage);
					}
					else
					{
						MessageUtil.shortArg(sender, 2, args);
						MessageUtil.commandInfo(sender, label, consoleUsage);
					}
					return true;
				}
				else if (args.length <= 2)
				{
					if (args.length == 1 && !(sender instanceof Player))
					{
						MessageUtil.shortArg(sender, 2, args);
						MessageUtil.commandInfo(sender, label, consoleUsage);
						return true;
					}
					Player player;
					if (args.length == 1)
						player = (Player) sender;
					else
					{
						if (!Permission.CMD_ITEMDATA_OTHERS.has(sender))
							return true;
						else
							player = Method.getPlayer(sender, args[1]);
						if (player == null)
							return true;
					}
					ItemStack item;
					Inventory enderchest = player.getEnderChest();
					if (!MessageUtil.isInteger(sender, args[0], true))
						return true;
					int slot = Integer.parseInt(args[0]);
					if (!MessageUtil.checkNumberSize(sender, slot, 1, enderchest.getSize()))
						return true;
					item = enderchest.getItem(slot - 1);
					MessageUtil.sendMessage(sender, Prefix.INFO_ITEMSTORAGE, "&e-----------------------아이템 정보-----------------------");
					MessageUtil.sendMessage(sender, Prefix.INFO_ITEMSTORAGE, player, "&r의 엔더상자의 &e" + slot + "번&r 슬롯 아이템");
					if (sender instanceof Player)
						ItemInfo.sendInfo(sender, item, UserData.SHORTEND_DEBUG_MESSAGE.getBoolean(((Player) sender).getUniqueId()));
					else
						ItemInfo.sendInfo(sender, item);
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
					return true;
				}
				break;
		}
		return true;
	}
}
