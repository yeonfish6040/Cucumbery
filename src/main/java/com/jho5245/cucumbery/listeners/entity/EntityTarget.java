package com.jho5245.cucumbery.listeners.entity;

import org.bukkit.GameMode;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.event.entity.EntityTargetEvent.TargetReason;

import com.jho5245.cucumbery.util.storage.CustomConfig.UserData;

public class EntityTarget implements Listener
{
	@EventHandler
	public void onEntityTarget(EntityTargetEvent event)
	{
		Entity entity = event.getEntity(); // 쫓아오는 개체
		Entity target = event.getTarget(); // 쫓기는 개체
		TargetReason reason = event.getReason();
//		try
//		{
//			Method.broadcastPlayer((target) + ", 어그로 : " + (event.getEntity()) + ", 이유 : " + reason);
//		}
//		catch (Exception e)
//		{
//			Method.broadcastPlayer("예외 발생");
//		}
		if (reason != TargetReason.TEMPT && entity.getType() != EntityType.EXPERIENCE_ORB && target instanceof Player)
		{
			Player player = (Player) target;
			GameMode gamemode = player.getGameMode();
			if (gamemode != GameMode.SURVIVAL && gamemode != GameMode.ADVENTURE)
				return;
			if (!UserData.ENTITY_AGGRO.getBoolean(player.getUniqueId()))
			{
				event.setTarget(null);
				event.setCancelled(true);
			}
		}
	}
}
