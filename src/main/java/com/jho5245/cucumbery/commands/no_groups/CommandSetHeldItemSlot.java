package com.jho5245.cucumbery.commands.no_groups;

import com.jho5245.cucumbery.util.no_groups.MessageUtil;
import com.jho5245.cucumbery.util.no_groups.Method;
import com.jho5245.cucumbery.util.no_groups.SelectorUtil;
import com.jho5245.cucumbery.util.storage.data.Permission;
import com.jho5245.cucumbery.util.storage.data.Prefix;
import org.bukkit.command.*;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;

public class CommandSetHeldItemSlot implements CommandExecutor, TabCompleter
{
	public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args)
	{
		if (!Method.hasPermission(sender, Permission.CMD_SETHELDITEMSLOT, true))
		{
			return true;
		}
		if (!MessageUtil.checkQuoteIsValidInArgs(sender, args = MessageUtil.wrapWithQuote(args)))
		{
			return !(sender instanceof BlockCommandSender);
		}
		String usage = cmd.getUsage().replace("/<command> ", ""), consoleUsage = usage.replace("[플레이어 ID]", "<플레이어 ID>");
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
		else if (args.length == 1)
		{
			if (!(sender instanceof Player player))
			{
				MessageUtil.shortArg(sender, 2, args);
				MessageUtil.commandInfo(sender, label, consoleUsage);
				return true;
			}
			if (!MessageUtil.isInteger(sender, args[0], true))
			{
				return true;
			}
			int slot = Integer.parseInt(args[0]);
			if (!MessageUtil.checkNumberSize(sender, slot, 1, 9))
			{
				return true;
			}
			player.getInventory().setHeldItemSlot(slot-1);
			MessageUtil.info(player, "단축바 슬롯을 rg255,204;"+slot+"번&r으로 변경했습니다");
		}
		else if (args.length <= 3)
		{
			if (!MessageUtil.isInteger(sender, args[0], true))
			{
				return true;
			}
			int slot = Integer.parseInt(args[0]);
			if (!MessageUtil.checkNumberSize(sender, slot, 1, 9))
			{
				return true;
			}
			Player target = SelectorUtil.getPlayer(sender, args[1]);
			if (target == null)
			{
				return true;
			}
			target.getInventory().setHeldItemSlot(slot-1);
			if (!MessageUtil.isBoolean(sender, args, 3, true))
			{
				return true;
			}
			boolean hideMessage = args.length == 3 && args[2].equals("true");
			if (!hideMessage)
			{
				if (!target.equals(sender))
				{
					MessageUtil.info(target, sender, "이 당신의 단축바 슬롯을 rg255,204;"+slot+"번&r으로 변경했습니다");
				}
				MessageUtil.info(sender, target, "의 단축바 슬롯을 rg255,204;"+slot+"번&r으로 변경했습니다");
			}
		}
		else
		{
			MessageUtil.longArg(sender, 3, args);
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
	public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args)
	{
		if (!MessageUtil.checkQuoteIsValidInArgs(sender, args = MessageUtil.wrapWithQuote(true, args), true))
		{
			return Collections.singletonList(args[0]);
		}
		int length = args.length;

		if (length == 1)
		{
			return Method.tabCompleterIntegerRadius(args, 1, 9, "<슬롯>");
		}
		else if (length == 2)
		{
			return Method.tabCompleterPlayer(sender, args);
		}
		else if (length == 3)
		{
			return Method.tabCompleterBoolean(args, "[명령어 출력 숨김 여부]");
		}

		return Collections.singletonList(Prefix.ARGS_LONG.toString());
	}
}
