package com.jho5245.cucumbery.listeners.player;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerGameModeChangeEvent;

import com.jho5245.cucumbery.Cucumbery;
import com.jho5245.cucumbery.util.storage.CustomConfig.UserData;
import com.jho5245.cucumbery.util.Method;

public class PlayerGameModeChange implements Listener
{
	@EventHandler
	public void onPlayerGameModeChange(PlayerGameModeChangeEvent event)
	{
		Player player = event.getPlayer();
		if (UserData.USE_HELPFUL_LORE_FEATURE.getBoolean(player.getUniqueId()) && UserData.DISABLE_HELPFUL_FEATURE_WHEN_CREATIVE.getBoolean(player.getUniqueId()))
		{
			Bukkit.getScheduler().runTaskLater(Cucumbery.getPlugin(), () -> Method.updateInventory(player), 0L);
		}
	}
}