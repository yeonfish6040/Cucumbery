package com.jho5245.cucumbery.listeners.player;

import com.jho5245.cucumbery.Cucumbery;
import com.jho5245.cucumbery.util.MessageUtil;
import com.jho5245.cucumbery.util.storage.SoundPlay;
import com.jho5245.cucumbery.util.storage.data.Constant;
import com.jho5245.cucumbery.util.storage.data.Permission;
import com.jho5245.cucumbery.util.storage.data.Variable;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;

import java.util.UUID;


public class PlayerTeleport implements Listener
{
	@EventHandler
	public void onPlayerTeleport(PlayerTeleportEvent event)
	{
		if (Cucumbery.config.getBoolean("grant-default-permission-to-players"))
			return;
		Player player = event.getPlayer();
		UUID uuid = player.getUniqueId();
		TeleportCause teleCause = event.getCause();
		if (teleCause == TeleportCause.SPECTATE)
		{
			Location dest = event.getTo();
			Player target = null;
			for (Entity entity : dest.getWorld().getNearbyEntities(dest, 0.000_000_0001D, 0.000_000_0001D, 0.000_000_0001D))
			{
				if (entity instanceof Player)
				{
					target = (Player) entity;
					break;
				}
			}
			if (target != null)
			{
				if (Permission.EVENT2_ANTI_SPECTATE.has(target) && !Permission.EVENT2_ANTI_SPECTATE_BYPASS.has(player))
				{
					event.setCancelled(true);
					if (!Permission.EVENT_ERROR_HIDE.has(player) && !Variable.antispecateAlertCooldown.contains(uuid))
					{
						Variable.antispecateAlertCooldown.add(uuid);
						MessageUtil.sendTitle(player, "&c관전 불가!", "&r관전할 수 없는 플레이어입니다", 5, 80, 15);
						SoundPlay.playSound(player, Constant.ERROR_SOUND);
						Bukkit.getServer().getScheduler().runTaskLater(Cucumbery.getPlugin(), () -> Variable.antispecateAlertCooldown.remove(uuid), 100L);
					}
				}
			}
		}
		else if (player.getGameMode() == GameMode.SPECTATOR)
			player.setSpectatorTarget(null);
	}
}
