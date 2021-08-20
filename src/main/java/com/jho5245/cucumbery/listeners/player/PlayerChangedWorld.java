package com.jho5245.cucumbery.listeners.player;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;

import com.jho5245.cucumbery.Cucumbery;
import com.jho5245.cucumbery.util.Method;

public class PlayerChangedWorld implements Listener
{
	@EventHandler public void onPlayerChangedWorld(PlayerChangedWorldEvent event)
	{
		Bukkit.getServer().getScheduler().runTaskLater(Cucumbery.getPlugin(), () -> Method.updateInventory(event.getPlayer()), 0L);
	}
}
