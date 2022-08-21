package com.jho5245.cucumbery.commands.debug;

import com.jho5245.cucumbery.util.no_groups.MessageUtil;
import com.jho5245.cucumbery.util.no_groups.Method;
import com.jho5245.cucumbery.util.storage.data.Constant;
import com.jho5245.cucumbery.util.storage.data.Permission;
import com.jho5245.cucumbery.util.storage.data.Prefix;
import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.command.*;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;

public class CommandHowIs implements CommandExecutor, TabCompleter
{
  public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args)
  {
		if (!Method.hasPermission(sender, Permission.CMD_HOWIS, true))
		{
			return true;
		}
    if (!MessageUtil.checkQuoteIsValidInArgs(sender, args = MessageUtil.wrapWithQuote(args)))
    {
      return !(sender instanceof BlockCommandSender);
    }
    String usage = cmd.getUsage().replace("/<command> ", "");
    if (args.length == 0)
    {
      Server server = Bukkit.getServer();
      Runtime runTime = Runtime.getRuntime();
      long maxMem = runTime.maxMemory(), freeMem = runTime.freeMemory(), totalMem = runTime.totalMemory(), usingMem = totalMem - freeMem;
      MessageUtil.sendMessage(sender, Prefix.INFO_HOWIS, Constant.separatorSubString(5));
      MessageUtil.sendMessage(sender, Prefix.INFO_HOWIS, "&6서버 메모리 사용량");
      MessageUtil.sendMessage(sender, Prefix.INFO_HOWIS, "사용 메모리 : rg255,204;" + usingMem / 1024 / 1024 + "MB / " + maxMem / 1024 / 1024 + "MB");
      MessageUtil.sendMessage(sender, Prefix.INFO_HOWIS, "총합 메모리 : rg255,204;" + totalMem / 1024 / 1024 + "MB");
      MessageUtil.sendMessage(sender, Prefix.INFO_HOWIS, "여유 메모리 : rg255,204;" + freeMem / 1024 / 1024 + "MB");
      double[] tps = Bukkit.getTPS();
      MessageUtil.sendMessage(sender, Prefix.INFO_HOWIS, "TPS : rg255,204;" + Constant.Sosu2.format(tps[0]) + ", " + Constant.Sosu2.format(tps[1]) + ", " + Constant.Sosu2.format(tps[2]));
      MessageUtil.sendMessage(sender, Prefix.INFO_HOWIS, Constant.separatorSubString(5));
      MessageUtil.sendMessage(sender, Prefix.INFO_HOWIS, (server.hasWhitelist() ? "&a화이트리스트 사용 서버" : "rg255,204;화이트리스트 미사용 서버"));
      MessageUtil.sendMessage(sender, Prefix.INFO_HOWIS, "화이트리스트에 등록된 플레이어 : rg255,204;" + server.getWhitelistedPlayers().size() + "명");
      MessageUtil.sendMessage(sender, Prefix.INFO_HOWIS, Constant.separatorSubString(5));
    }
    else
    {
      MessageUtil.longArg(sender, 0, args);
      MessageUtil.commandInfo(sender, label, usage);
      return true;
    }
    return true;
  }

  public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args)
  {
    return Collections.singletonList(Prefix.ARGS_LONG.toString());
  }
}
