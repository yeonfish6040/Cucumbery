package com.jho5245.cucumbery.commands.debug;

import com.jho5245.cucumbery.util.Method;
import com.jho5245.cucumbery.util.MessageUtil;
import com.jho5245.cucumbery.util.storage.data.Permission;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class CheckPermission implements CommandExecutor
{
	public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args)
	{
		if (!Method.hasPermission(sender, Permission.CMD_CHECKPERMISSION, true))
			return true;
		String usage = cmd.getUsage().replace("/<command> ", "");
		if (args.length < 2)
		{
			MessageUtil.shortArg(sender, 1, args);
			MessageUtil.commandInfo(sender, label, usage);
			return true;
		}
		else if (args.length == 2)
		{
			Player target = Method.getPlayer(sender, args[0]);
			if (target == null)
				return true;
			MessageUtil.info(sender, "&e" + Method.getDisplayName(target) + "&r은(는) &e" + args[1] + "&r 퍼미션 노드를 " + ((target.hasPermission(args[1])) ? "&a가지고 있습니다." : "&c가지고 있지 않습니다."));
		}
		else
		{
			MessageUtil.longArg(sender, 2, args);
			MessageUtil.commandInfo(sender, label, usage);
			return true;
		}
		return true;
	}
}
