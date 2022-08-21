package com.jho5245.cucumbery.util.rpg;

import com.jho5245.cucumbery.Cucumbery;
import com.jho5245.cucumbery.util.no_groups.MessageUtil;
import com.jho5245.cucumbery.util.no_groups.Method;
import com.jho5245.cucumbery.util.storage.data.Prefix;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;

public class DamageDebugCommand implements CommandExecutor
{
	public static HashMap<Player, Boolean> debug = new HashMap<Player, Boolean>();
	
	public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, String[] args)
	{
		if (!Cucumbery.config.getBoolean("rpg-enabled"))
		{
			Bukkit.dispatchCommand(sender, "cucumberyunknowncommand");
			return true;
		}
		
		if (args.length == 0)
		{
			if (!(sender instanceof Player))
			{
				MessageUtil.sendError(sender, Prefix.ARGS_SHORT + "(" + args.length + "§e개 입력, 최소 1개)");
				MessageUtil.info(sender, "/" + label + " <플레이어>");
				return true;
			}
			
			Player player = (Player) sender;
			
			List<String> worlds = Cucumbery.config.getStringList("no-rpg-enabled-worlds");
			
			if (Method.configContainsLocation(player.getLocation(), worlds))
			{
				Bukkit.dispatchCommand(sender, "cucumberyunknowncommand");
				return true;
			}
			
			if (debug.containsKey(player))
			{
				debug.remove(player);
				
				MessageUtil.sendMessage(player, Prefix.INFO_DAMAGE, "대미지 디버깅을 끕니다");
				return true;
			}
			
			else if (!debug.containsKey(player))
			{
				debug.put(player, true);
				
				MessageUtil.sendMessage(player, Prefix.INFO_DAMAGE, "대미지 디버깅을 켭니다");
				return true;
			}
		}
		
		else
		{
			MessageUtil.sendError(sender, Prefix.ARGS_LONG + "(" + args.length + "§e개 입력, 최대 1개)");
			MessageUtil.info(sender, "/" + label + " <플레이어>");
		}
		return true;
	}
}
