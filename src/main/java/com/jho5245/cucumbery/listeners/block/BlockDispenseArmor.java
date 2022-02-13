package com.jho5245.cucumbery.listeners.block;

import com.jho5245.cucumbery.util.nbt.NBTAPI;
import com.jho5245.cucumbery.util.storage.no_groups.ItemStackUtil;
import com.jho5245.cucumbery.util.storage.data.Constant;
import org.bukkit.GameMode;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockDispenseArmorEvent;
import org.bukkit.inventory.ItemStack;

public class BlockDispenseArmor implements Listener
{
	@EventHandler
	public void onBlockDispenseArmor(BlockDispenseArmorEvent event)
	{
		if (event.isCancelled())
			return;
		ItemStack item = event.getItem();
		if (ItemStackUtil.itemExists(item))
		{
			LivingEntity entity = event.getTargetEntity();
			if (entity.getType() == EntityType.PLAYER)
			{
				Player player = (Player) entity;
				if ((player.getGameMode() == GameMode.SURVIVAL || player.getGameMode() == GameMode.ADVENTURE)
						&& NBTAPI.isRestricted(player, item, Constant.RestrictionType.NO_EQUIP))
					event.setCancelled(true);
			}
		}
	}
}
