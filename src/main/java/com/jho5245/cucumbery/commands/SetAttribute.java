package com.jho5245.cucumbery.commands;

import com.jho5245.cucumbery.util.MessageUtil;
import com.jho5245.cucumbery.util.Method;
import com.jho5245.cucumbery.util.SelectorUtil;
import com.jho5245.cucumbery.util.storage.data.Constant;
import com.jho5245.cucumbery.util.storage.data.Permission;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class SetAttribute implements CommandExecutor
{
	public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args)
	{
		if (!Method.hasPermission(sender, Permission.CMD_SET_ATTRIBUTE, true))
		{
			return true;
		}
		String usage = cmd.getUsage().replace("/<command> ", "");
		if (args.length < 3)
		{
			MessageUtil.shortArg(sender, 3, args);
			MessageUtil.commandInfo(sender, label, usage);
			return true;
		}
		else if (args.length <= 4)
		{
			Player target = SelectorUtil.getPlayer(sender, args[0]);
			if (target == null)
			{
				return true;
			}
			Attribute attribute;
			try
			{
				attribute = Attribute.valueOf(args[1]);
			}
			catch (Exception e)
			{
				MessageUtil.wrongArg(sender, 2, args);
				return true;
			}
			AttributeInstance attributeInstance = target.getAttribute(attribute);
			if (attribute == Attribute.GENERIC_MAX_HEALTH)
			{
				MessageUtil.sendError(sender, "해당 명령어로는 설정할 수 없는 값입니다.");
				MessageUtil.info(sender, "&e/maxhealthpoint &r명령어를 사용해주세요.");
				return true;
			}
			if (attributeInstance == null)
			{
				MessageUtil.wrongArg(sender, 2, args);
				return true;
			}
			double value;
			boolean reset = false;
			if (!args[2].equals("reset") && !MessageUtil.isDouble(sender, args[2], true))
			{
				return true;
			}
			if (args[2].equals("reset"))
			{
				value = attributeInstance.getDefaultValue();
				reset = true;
			}
			else
			{
				value = Double.parseDouble(args[2]);
			}
			boolean hideOutput = false;
			if (args.length == 4)
			{
				if (!args[3].equals("true") && !args[3].equals("false"))
				{
					MessageUtil.wrongBool(sender, 4, args);
					return true;
				}
				if (args[3].equals("true"))
				{
					hideOutput = true;
				}
			}
			Objects.requireNonNull(target.getAttribute(attribute)).setBaseValue(value);
			if (!hideOutput)
			{
				if (!target.equals(sender))
				{
					MessageUtil.info(sender,
															target,
															"의 순수 &e"+
															attribute+
															"&r 값을 &e"+
															(reset ? "기본값&r(&e" : "")+
															Constant.Sosu15.format(value)+
															(reset ? "&r)" : "")+
															"&r으로 설정하였습니다.");
				}
				MessageUtil.info(sender, sender,
														"이(가) 당신의 순수 &e"+
														attribute+
														"&r 값을 &e"+
														(reset ? "기본값&r(&e" : "")+
														Constant.Sosu15.format(value)+
														(reset ? "&r)" : "")+
														"&r으로 설정하였습니다.");
			}
		}
		else
		{
			MessageUtil.longArg(sender, 4, args);
			MessageUtil.commandInfo(sender, label, usage);
			return true;
		}
		return true;
	}
}
