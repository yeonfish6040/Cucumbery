package com.jho5245.cucumbery.commands.debug;

import com.jho5245.cucumbery.util.MessageUtil;
import com.jho5245.cucumbery.util.Method;
import com.jho5245.cucumbery.util.SelectorUtil;
import com.jho5245.cucumbery.util.storage.ComponentUtil;
import com.jho5245.cucumbery.util.storage.data.Permission;
import com.jho5245.cucumbery.util.storage.data.Prefix;
import net.kyori.adventure.text.event.ClickEvent;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class UUID implements CommandExecutor
{
	public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args)
	{
		if (!Method.hasPermission(sender, Permission.CMD_UUID, true))
			return true;
		String usage = cmd.getUsage().replace("/<command> ", ""), consoleUsage = usage.replace("[플레이어 ID]", "<플레이어 ID>");
		if (args.length == 0)
		{
			if (!(sender instanceof Player))
			{
				MessageUtil.shortArg(sender, 1, args);
				MessageUtil.commandInfo(sender, label, consoleUsage);
				return true;
			}
			Player player = (Player) sender;
			java.util.UUID uuid = player.getUniqueId();
			MessageUtil.sendMessage(player, ComponentUtil.create(Prefix.INFO_UUID + "UUID는 "), ComponentUtil.create("&e" + uuid.toString() + "&r"),
					"&r클릭하시면 클립보드에 복사됩니다.", ClickEvent.Action.COPY_TO_CLIPBOARD, player.getUniqueId().toString(), ComponentUtil.create("입니다."));
		}
		else if (args.length == 1)
		{
			OfflinePlayer target = SelectorUtil.getOfflinePlayer(sender, args[0]);
			if (target == null)
				return true;
			if (sender instanceof Player && sender.equals(target))
				Bukkit.getServer().dispatchCommand(sender, "uuid");
			else
			{
				MessageUtil.sendMessage(sender, ComponentUtil.create(Prefix.INFO_UUID + "&e" + Method.getDisplayName(target) + "&r의 UUID는 "),
						ComponentUtil.create("&e" + target.getUniqueId().toString() + "&r", "&r클릭하시면 클립보드에 복사됩니다.",
								ClickEvent.Action.COPY_TO_CLIPBOARD, target.getUniqueId().toString()),
						ComponentUtil.create("입니다."));
			}
		}
		else
		{
			MessageUtil.longArg(sender, 1, args);
			if (sender instanceof Player)
			{
				MessageUtil.commandInfo(sender, label, usage);
			}
			else
			{
				MessageUtil.commandInfo(sender, label, consoleUsage);
			}
		}
		return true;
	}
}
