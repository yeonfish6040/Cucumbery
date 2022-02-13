package com.jho5245.cucumbery.commands.no_groups;

import com.jho5245.cucumbery.util.no_groups.MessageUtil;
import com.jho5245.cucumbery.util.no_groups.Method;
import com.jho5245.cucumbery.util.no_groups.SelectorUtil;
import com.jho5245.cucumbery.util.storage.component.util.ComponentUtil;
import com.jho5245.cucumbery.util.storage.no_groups.CustomConfig;
import com.jho5245.cucumbery.util.storage.no_groups.CustomConfig.UserData;
import com.jho5245.cucumbery.util.storage.data.Permission;
import com.jho5245.cucumbery.util.storage.data.Prefix;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.*;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;

public class CommandGetUserData implements CommandExecutor, TabCompleter
{
  public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args)
  {
		if (!Method.hasPermission(sender, Permission.CMD_GETUSERDATA, true))
		{
			return true;
		}
    if (!MessageUtil.checkQuoteIsValidInArgs(sender, args = MessageUtil.wrapWithQuote(args)))
    {
      return !(sender instanceof BlockCommandSender);
    }
    String usage = cmd.getUsage().replace("/<command> ", "");
    if (args.length < 2)
    {
      MessageUtil.shortArg(sender, 2, args);
      MessageUtil.commandInfo(sender, label, usage);
      return true;
    }
    else if (args.length == 2)
    {
      OfflinePlayer player = SelectorUtil.getOfflinePlayer(sender, args[0]);
			if (player == null)
			{
				return true;
			}
      boolean isOnline = player.getPlayer() != null;
      String keyString = args[1];
      UserData key;
      try
      {
        key = UserData.valueOf(keyString.toUpperCase());
      }
      catch (Exception e)
      {
        MessageUtil.noArg(sender, Prefix.NO_KEY, keyString);
        return true;
      }
      Object value;
			if (isOnline)
			{
				value = key.get(player);
			}
			else
			{
				try
				{
					value = key.get(player);
				}
				catch (Exception e2)
				{
					value = CustomConfig.getPlayerConfig(player.getUniqueId()).getConfig().get(key.getKey());
				}
			}
      keyString += "(" + key.getKey().replace("-", " ") + ")";
			if (key == UserData.UUID)
			{
        if (value != null)
        {
          MessageUtil.sendMessage(sender, Prefix.INFO_SETDATA, player, "의 " + keyString + "&r 값은 "
                  ,ComponentUtil.create(value)
                          .hoverEvent(HoverEvent.showText(Component.translatable("chat.copy.click")))
                          .clickEvent(ClickEvent.copyToClipboard(value.toString())),
                  "입니다");
        }
      }
			else
			{
				MessageUtil.sendMessage(sender, Prefix.INFO_SETDATA, player, "의 " + keyString
                + (value == null ? "&r 값이 없습니다" : "&r 값은 &e" + value + "&r입니다"));
			}
    }
    else
    {
      MessageUtil.longArg(sender, 2, args);
      MessageUtil.commandInfo(sender, label, usage);
      return true;
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
      if (label.equals("gud"))
      {
        return Method.tabCompleterPlayer(sender, args);
      }
      return Method.tabCompleterOfflinePlayer(sender, args);
    }
    else if (length == 2)
    {
      return Method.tabCompleterList(args, UserData.values(), "<유저 데이터>");
    }

    return Collections.singletonList(Prefix.ARGS_LONG.toString());
  }
}
