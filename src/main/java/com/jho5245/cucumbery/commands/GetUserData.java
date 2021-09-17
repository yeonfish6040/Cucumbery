package com.jho5245.cucumbery.commands;

import com.jho5245.cucumbery.util.MessageUtil;
import com.jho5245.cucumbery.util.Method;
import com.jho5245.cucumbery.util.SelectorUtil;
import com.jho5245.cucumbery.util.storage.ComponentUtil;
import com.jho5245.cucumbery.util.storage.CustomConfig;
import com.jho5245.cucumbery.util.storage.CustomConfig.UserData;
import com.jho5245.cucumbery.util.storage.data.Permission;
import com.jho5245.cucumbery.util.storage.data.Prefix;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class GetUserData implements CommandExecutor
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
      boolean isOnline;
      OfflinePlayer player = SelectorUtil.getOfflinePlayer(sender, args[0]);
			if (player == null)
			{
				return true;
			}
      isOnline = player.getPlayer() != null;
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
				value = key.get(player.getUniqueId());
			}
			else
			{
				try
				{
					value = key.get(player.getUniqueId());
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
                  "입니다.");
        }
      }
			else
			{
				MessageUtil.sendMessage(sender, Prefix.INFO_SETDATA, player, "의 " + keyString
                + (value == null ? "&r 값이 없습니다." : "&r 값은 &e" + value + "&r입니다."));
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
}
