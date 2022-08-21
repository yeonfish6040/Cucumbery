package com.jho5245.cucumbery.listeners.server;

import com.jho5245.cucumbery.Cucumbery;
import com.jho5245.cucumbery.util.no_groups.MessageUtil;
import com.jho5245.cucumbery.util.no_groups.PlaceHolderUtil;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.ServerListPingEvent;

import java.util.List;
import java.util.Random;

public class ServerListPing implements Listener
{
	@EventHandler
	public void onServerListPing(ServerListPingEvent event)
	{
		if (Cucumbery.config.getBoolean("ping-loading-message-usage"))
		{
			List<String> motds = Cucumbery.config.getStringList("ping-loading-messages");
			if (motds.size() == 0)
				return;
			event.setMotd(PlaceHolderUtil.placeholder(Bukkit.getServer().getConsoleSender(), MessageUtil.n2s(motds.get(new Random().nextInt(motds.size()))), null));
		}
	}
}
