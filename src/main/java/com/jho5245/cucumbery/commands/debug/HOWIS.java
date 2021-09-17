package com.jho5245.cucumbery.commands.debug;

import com.jho5245.cucumbery.util.MessageUtil;
import com.jho5245.cucumbery.util.Method;
import com.jho5245.cucumbery.util.storage.data.Permission;
import com.jho5245.cucumbery.util.storage.data.Prefix;
import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class HOWIS implements CommandExecutor
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
      MessageUtil.sendMessage(sender, Prefix.INFO_HOWIS, "-------------------------------------------------");
      MessageUtil.sendMessage(sender, Prefix.INFO_HOWIS, "&6서버 메모리 사용량");
      MessageUtil.sendMessage(sender, Prefix.INFO_HOWIS, "사용 메모리 : &e" + usingMem / 1024 / 1024 + "MB / " + maxMem / 1024 / 1024 + "MB");
      MessageUtil.sendMessage(sender, Prefix.INFO_HOWIS, "총합 메모리 : &e" + totalMem / 1024 / 1024 + "MB");
      MessageUtil.sendMessage(sender, Prefix.INFO_HOWIS, "여유 메모리 : &e" + freeMem / 1024 / 1024 + "MB");
      double[] tps = Bukkit.getTPS();
      MessageUtil.sendMessage(sender, Prefix.INFO_HOWIS, "TPS : &e" + tps[0] + ", " + tps[1] + ", " + tps[2]);
      MessageUtil.sendMessage(sender, Prefix.INFO_HOWIS, "------------------------");
      MessageUtil.sendMessage(sender, Prefix.INFO_HOWIS, (server.hasWhitelist() ? "&a화이트리스트 사용 서버" : "&e화이트리스트 미사용 서버"));
      MessageUtil.sendMessage(sender, Prefix.INFO_HOWIS, "화이트리스트에 등록된 플레이어 : &e" + server.getWhitelistedPlayers().size() + "명");
      MessageUtil.sendMessage(sender, Prefix.INFO_HOWIS, "-------------------------------------------------");
    }
    else
    {
      MessageUtil.longArg(sender, 0, args);
      MessageUtil.commandInfo(sender, label, usage);
      return true;
    }
    return true;
  }
}
