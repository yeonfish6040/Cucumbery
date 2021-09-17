package com.jho5245.cucumbery.commands.debug;

import com.jho5245.cucumbery.Cucumbery;
import com.jho5245.cucumbery.util.MessageUtil;
import com.jho5245.cucumbery.util.Method;
import com.jho5245.cucumbery.util.storage.data.Permission;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class CheckConfig implements CommandExecutor
{
  public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args)
  {
    if (!Method.hasPermission(sender, Permission.CMD_CHECK_CONFIG, true))
    {
      return true;
    }
    if (!MessageUtil.checkQuoteIsValidInArgs(sender, args = MessageUtil.wrapWithQuote(args)))
    {
      return !(sender instanceof BlockCommandSender);
    }
    if (args.length == 0)
    {
      MessageUtil.shortArg(sender, 1, args);
      MessageUtil.commandInfo(sender, label, Method.getUsage(cmd));
      return true;
    }
    String directory = MessageUtil.listToString(" ", args);
    Object value = Cucumbery.config.get(directory);
    String display = Cucumbery.config.contains(directory) ? "&e" + value + "&r, 자료형 : &e" + (value != null ? value.getClass().getName() : null) : "&c없음";
    MessageUtil.info(sender, "&e" + directory + " &r디렉터리 값 : " + display);
    return true;
  }
}
