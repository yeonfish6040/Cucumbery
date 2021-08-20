package com.jho5245.cucumbery.listeners.hanging;

import com.jho5245.cucumbery.Cucumbery;
import com.jho5245.cucumbery.util.MessageUtil;
import com.jho5245.cucumbery.util.nbt.NBTAPI;
import com.jho5245.cucumbery.util.storage.ItemStackUtil;
import com.jho5245.cucumbery.util.storage.SoundPlay;
import com.jho5245.cucumbery.util.storage.data.Constant;
import com.jho5245.cucumbery.util.storage.data.Permission;
import com.jho5245.cucumbery.util.storage.data.Variable;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.hanging.HangingPlaceEvent;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

public class HangingPlace implements Listener
{
	@EventHandler
	public void onHangingPlace(HangingPlaceEvent event)
	{
		Player player = event.getPlayer();
		if (player == null)
			return;
		UUID uuid = player.getUniqueId();
		ItemStack item = ItemStackUtil.getPlayerUsingItem(player, Material.ITEM_FRAME, Material.PAINTING);
		if (NBTAPI.isRestricted(player, item, Constant.RestrictionType.NO_PLACE))
		{
			event.setCancelled(true);
			if (!Permission.EVENT_ERROR_HIDE.has(player) && !Variable.blockPlaceAlertCooldown2.contains(uuid))
			{
				Variable.blockPlaceAlertCooldown2.add(uuid);
				MessageUtil.sendTitle(player, "&c설치 불가!", "&r설치할 수 없는 블록입니다.", 5, 80, 15);
				SoundPlay.playSound(player, Constant.ERROR_SOUND);
				Bukkit.getServer().getScheduler().runTaskLater(Cucumbery.getPlugin(), () -> Variable.blockPlaceAlertCooldown2.remove(uuid), 100L);
			}
		}
	}
}
