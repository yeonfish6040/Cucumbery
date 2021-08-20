package com.jho5245.cucumbery.commands.debug;

import com.jho5245.cucumbery.util.Method;
import com.jho5245.cucumbery.util.MessageUtil;
import com.jho5245.cucumbery.util.storage.data.Permission;
import com.jho5245.cucumbery.util.storage.data.Prefix;
import com.jho5245.cucumbery.util.storage.data.Variable;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class ViewDamage implements CommandExecutor
{
	public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args)
	{
		if (!Method.hasPermission(sender, Permission.CMD_VIEWDAMAGE, true))
			return true;
		String usage = cmd.getUsage().replace("/<command> ", "");
		if (!(sender instanceof Player))
		{
			MessageUtil.sendError(sender, Prefix.ONLY_PLAYER);
			return true;
		}
		Player player = (Player) sender;
		if (args.length == 0)
		{
			if (Variable.viewDamage.contains(player.getUniqueId()))
			{
				Variable.viewDamage.remove(player.getUniqueId());
				MessageUtil.sendMessage(player, Prefix.INFO_DAMAGE, "대미지를 더 이상 보지 않습니다.");
			}
			else
			{
				Variable.viewDamage.add(player.getUniqueId());
				MessageUtil.sendMessage(player, Prefix.INFO_DAMAGE, "대미지를 봅니다.");
			}
		}
		else if (args.length == 1)
		{
			switch (args[0].toLowerCase())
			{
				case "on" -> {
					Variable.viewDamage.add(player.getUniqueId());
					MessageUtil.sendMessage(player, Prefix.INFO_DAMAGE, "대미지를 봅니다.");
				}
				case "off" -> {
					Variable.viewDamage.remove(player.getUniqueId());
					MessageUtil.sendMessage(player, Prefix.INFO_DAMAGE, "대미지를 더 이상 보지 않습니다.");
				}
				default -> {
					MessageUtil.wrongArg(sender, 1, args);
					MessageUtil.commandInfo(sender, label, usage);
					return true;
				}
			}
		}
		else
		{
			MessageUtil.longArg(sender, 1, args);
			MessageUtil.commandInfo(sender, label, usage);
		}
		return true;
	}
}
