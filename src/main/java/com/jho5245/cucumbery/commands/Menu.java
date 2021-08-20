package com.jho5245.cucumbery.commands;

import com.jho5245.cucumbery.Cucumbery;
import com.jho5245.cucumbery.gui.GUI;
import com.jho5245.cucumbery.gui.GUI.GUIType;
import com.jho5245.cucumbery.util.MessageUtil;
import com.jho5245.cucumbery.util.Method;
import com.jho5245.cucumbery.util.storage.data.Permission;
import com.jho5245.cucumbery.util.storage.data.Prefix;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class Menu implements CommandExecutor
{
	public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args)
	{
		if (!Cucumbery.config.getBoolean("grant-default-permission-to-players") && !Method.hasPermission(sender, Permission.CMD_GUICOMMANDS, true))
		{
			return true;
		}
		String usage = cmd.getUsage().replace("/<command> ", ""), consoleUsage = usage.replace("[플레이어 ID]", "<플레이어 ID>");
		if (args.length == 0)
		{
			if (!(sender instanceof Player))
			{
				MessageUtil.sendError(sender, Prefix.ARGS_SHORT, "(", args.length, "&e개 입력, 최소 1개)");
				MessageUtil.info(sender, "/" + label + consoleUsage);
				return true;
			}

			Player player = (Player) sender;
			Method.playSound(player, Sound.BLOCK_NOTE_BLOCK_PLING, 1F, 1.5F);
			//MessageUtil.sendMessage(player, Prefix.INFO_MENU+"메뉴를 엽니다.");
			Bukkit.getServer().getScheduler().runTaskLater(Cucumbery.getPlugin(), () -> GUI.openGUI(player, GUIType.MAIN_MENU), 0L);
		}

		else if (args.length <= 2)
		{
			if (!Method.hasPermission(sender, Permission.CMD_GUICOMMANDS_OTHERS, true))
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
			if (!(args.length == 2 && args[1].equalsIgnoreCase("true")))
			{
				Method.playSound(target, Sound.BLOCK_NOTE_BLOCK_PLING, 1F, 1.5F);
				MessageUtil.sendMessage(sender, Prefix.INFO_MENU, "&e", target, "&r에게 메뉴를 열어줍니다.");
				if (!target.equals(sender))
				{
					//MessageUtil.sendMessage(target, Prefix.INFO_MENU+"메뉴를 엽니다.");
				}
			}
			Bukkit.getServer().getScheduler().runTaskLater(Cucumbery.getPlugin(), () -> GUI.openGUI(target, GUIType.MAIN_MENU), 0L);
		}
		else
		{
			if (!Method.hasPermission(sender, Permission.CMD_GUICOMMANDS_OTHERS, false))
			{
				MessageUtil.longArg(sender, 0, args);
				MessageUtil.commandInfo(sender, label, "");
				return true;
			}
			MessageUtil.longArg(sender, 2, args);
			MessageUtil.commandInfo(sender, label, usage);
			return true;
		}
		return true;
	}
}