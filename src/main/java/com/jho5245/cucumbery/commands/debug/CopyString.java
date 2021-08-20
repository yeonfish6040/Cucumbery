package com.jho5245.cucumbery.commands.debug;

import com.jho5245.cucumbery.util.Method;
import com.jho5245.cucumbery.util.MessageUtil;
import com.jho5245.cucumbery.util.storage.ComponentUtil;
import com.jho5245.cucumbery.util.storage.ItemStackUtil;
import com.jho5245.cucumbery.util.storage.data.Permission;
import com.jho5245.cucumbery.util.storage.data.Prefix;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class CopyString implements CommandExecutor
{
	public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args)
	{
		if (!Method.hasPermission(sender, Permission.CMD_COPYSTRING, true))
			return true;
		String usage = cmd.getUsage().replace("/<command> ", "");
		if (!(sender instanceof Player))
		{
			MessageUtil.sendError(sender, Prefix.ONLY_PLAYER);
			return true;
		}
		Player player = (Player) sender;
		ItemStack item = player.getInventory().getItemInMainHand();
		if (!ItemStackUtil.itemExists(item))
		{
			MessageUtil.sendError(sender, Prefix.NO_HOLDING_ITEM);
			return true;
		}
		if (args.length == 0)
		{
			MessageUtil.shortArg(sender, 1, args);
			MessageUtil.commandInfo(sender, label, usage);
			return true;
		}
		else if (args.length <= 3)
		{
			switch (args[0].toLowerCase())
			{
				case "name":
					if (args.length >= 3)
					{
						MessageUtil.longArg(sender, 2, args);
						MessageUtil.commandInfo(sender, label, "name [색깔 없앰 해제 여부]");
						return true;
					}
					boolean color = args.length == 2 && args[1].equalsIgnoreCase("true");
					if (!color)
					{
						boolean moreFishItem = false;
						String name = ComponentUtil.itemName(item).toString();
						String[] split = name.replace("§", "").split("\\|");
						if ((name.contains("§n§a§m§e")) && (name.contains("§l§e§n§g§t§h")) && (name.contains("§c§a§t§c§h§e§r")))
						{
							moreFishItem = true;
						}
						if (moreFishItem)
						{
							for (int i = 1; i < split.length; i++)
							{
								String[] arr = split[i].split(":");
								if (arr.length < 2)
								{
									break;
								}
								String key = arr[0];
								String value = arr[1];
								String str1;
								if ((str1 = key).hashCode() == 3373707)
								{
									if (str1.equals("name"))
									{
										name = value;
									}
								}
							}
							if (name == null)
							{
								name = "";
							}
						}
						Component a = ComponentUtil.create(Prefix.INFO_ITEMSTORAGE + "주로 사용하는 손에 들고 있는 아이템의 이름(색깔 없음)을 클립보드에 복사하려면 ");
						Component b = ComponentUtil.create("§9§l여기", "&r클릭하면 클립보드에 복사됩니다.\n&r" + name, ClickEvent.Action.COPY_TO_CLIPBOARD,
								name);
						Component c = ComponentUtil.create("&r를 클릭하세요.");
						MessageUtil.sendMessage(player, a, b, c);
					}
					else
					{
						Component a = ComponentUtil.create(Prefix.INFO_ITEMSTORAGE + "주로 사용하는 손에 들고 있는 아이템의 이름을 클립보드에 복사하려면 ");
						Component b = ComponentUtil.create("§9§l여기", "&r클릭하면 클립보드에 복사됩니다.\n&r" + ComponentUtil.itemName(item).toString().replace("§", "&"),
								ClickEvent.Action.COPY_TO_CLIPBOARD, ComponentUtil.itemName(item).toString().replace("§", "&"));
						Component c = ComponentUtil.create("&r를 클릭하세요.");
						MessageUtil.sendMessage(player, a, b, c);
					}
					break;
				case "lore":
					if (args.length < 2)
					{
						MessageUtil.shortArg(sender, 2, args);
						MessageUtil.commandInfo(sender, label, "lore <줄> [색깔 없앰 해제 여부]");
						return true;
					}
					else
					{
						ItemMeta itemMeta = item.getItemMeta();
						List<String> lore = itemMeta.getLore();
						if (lore == null || lore.size() == 0)
						{
							MessageUtil.sendError(player, "이 아이템에는 설명이 없습니다.");
							return true;
						}
						if (!MessageUtil.isInteger(sender, args[1], true))
							return true;
						int input = Integer.parseInt(args[1]);
						if (!MessageUtil.checkNumberSize(sender, input, 1, lore.size()))
							return true;
						input--;
						color = (args.length == 3) && (args[2].equalsIgnoreCase("true"));
						Component a;
						Component b;
						if (!color)
						{
							a = ComponentUtil.create(Prefix.INFO_ITEMSTORAGE + "주로 사용하는 손에 들고 있는 아이템의 §e" + (input + 1) + "번&r째 설명(색깔 없음)을 클립보드에 복사하려면 ");
							b = ComponentUtil.create("§9§l여기", "&r클릭하면 클립보드에 복사됩니다.\n&r" + MessageUtil.stripColor(lore.get(input)), ClickEvent.Action.COPY_TO_CLIPBOARD,
								MessageUtil.stripColor(lore.get(input)));
						}
						else
						{
							a = ComponentUtil.create(Prefix.INFO_ITEMSTORAGE + "주로 사용하는 손에 들고 있는 아이템의 §e" + (input + 1) + "번&r째 설명을 클립보드에 복사하려면 ");
							b = ComponentUtil.create("§9§l여기", "&r클릭하면 클립보드에 복사됩니다.\n&r" + lore.get(input).replace("§", "&"), ClickEvent.Action.COPY_TO_CLIPBOARD, lore.get(input).replace("§", "&"));
						}
						Component c = ComponentUtil.create("&r를 클릭하세요.");
						MessageUtil.sendMessage(player, a, b, c);
					}
					break;
				default:
					MessageUtil.wrongArg(sender, 1, args);
					MessageUtil.commandInfo(sender, label, usage);
					return true;
			}
		}
		return true;
	}
}
