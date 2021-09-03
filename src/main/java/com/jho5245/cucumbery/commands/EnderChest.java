package com.jho5245.cucumbery.commands;

import com.jho5245.cucumbery.util.MessageUtil;
import com.jho5245.cucumbery.util.Method;
import com.jho5245.cucumbery.util.SelectorUtil;
import com.jho5245.cucumbery.util.storage.ComponentUtil;
import com.jho5245.cucumbery.util.storage.SoundPlay;
import com.jho5245.cucumbery.util.storage.data.Permission;
import com.jho5245.cucumbery.util.storage.data.Prefix;
import net.kyori.adventure.text.Component;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class EnderChest implements CommandExecutor
{
	public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args)
	{
		if (!Method.hasPermission(sender, Permission.CMD_ENDERCHEST, true))
		{
			return true;
		}
		String usage = cmd.getUsage().replace("/<command> ", ""), consoleUsage = usage.replace("[상자 주인 ID]", "<상자 주인 ID>");
		if (args.length == 0)
		{
			if (!(sender instanceof Player player))
			{
				MessageUtil.shortArg(sender, 1, args);
				MessageUtil.commandInfo(sender, label, consoleUsage);
				return true;
			}
			player.openInventory(player.getEnderChest());
			MessageUtil.sendMessage(player, Prefix.INFO_ENDERCHEST+"엔더 상자를 엽니다.");
			SoundPlay.playSound(player, Sound.BLOCK_ENDER_CHEST_OPEN, SoundCategory.BLOCKS);
		}
		else if (args.length == 1)
		{
			Player target = SelectorUtil.getPlayer(sender, args[0]);
			if (target == null)
			{
				return true;
			}
			Component targetDisplayName = ComponentUtil.senderComponent(target);
			if (sender instanceof Player player)
			{
				player.openInventory(target.getEnderChest());
				SoundPlay.playSound(player, Sound.BLOCK_ENDER_CHEST_OPEN, SoundCategory.BLOCKS);
				MessageUtil.sendMessage(player, Prefix.INFO_ENDERCHEST,targetDisplayName,"&r의 엔더 상자를 엽니다.");
				return true;
			}
			target.openInventory(target.getEnderChest());
			SoundPlay.playSound(target, Sound.BLOCK_ENDER_CHEST_OPEN, SoundCategory.BLOCKS);
			MessageUtil.sendMessage(sender, Prefix.INFO_ENDERCHEST,targetDisplayName,"&r에게 자신의 엔더 상자를 열어줍니다.");
			if (!target.equals(sender))
			{
				MessageUtil.sendMessage(target, Prefix.INFO_ENDERCHEST, ComponentUtil.senderComponent(sender),"이 당신의 엔더 상자를 열어주었습니다.");
			}
		}
		else if (args.length <= 3)
		{
			boolean hideOutput = false;
			if (args.length == 3)
			{
				if (!args[2].equals("true") && !args[2].equals("false"))
				{
					MessageUtil.wrongBool(sender, 3, args);
					return true;
				}
				if (args[2].equals("true"))
				{
					hideOutput = true;
				}
			}
			Player target = SelectorUtil.getPlayer(sender, args[0]), target2 = SelectorUtil.getPlayer(sender, args[1]);
			if (target == null || target2 == null)
			{
				return true;
			}
			Component targetDisplayName = ComponentUtil.senderComponent(target);
			Component target2DisplayName = ComponentUtil.senderComponent(target2);
			target.openInventory(target2.getEnderChest());
			if (!hideOutput)
			{
				SoundPlay.playSound(target, Sound.BLOCK_ENDER_CHEST_OPEN, SoundCategory.BLOCKS);
				if (!target.equals(sender))
				{
					MessageUtil.sendMessage(target, Prefix.INFO_ENDERCHEST,ComponentUtil.senderComponent(sender)+"이 당신에게 &e"+target2DisplayName+"&r의 엔더 상자를 열어주었습니다.");
				}
				MessageUtil.sendMessage(sender, Prefix.INFO_ENDERCHEST,"&e"+targetDisplayName+"&r에게 &e"+target2DisplayName+"&r의 엔더 상자를 열어줍니다.");
			}
		}
		else
		{
			MessageUtil.longArg(sender, 3, args);
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
