package com.jho5245.cucumbery.listeners.enchantment;

import com.jho5245.cucumbery.Cucumbery;
import com.jho5245.cucumbery.util.nbt.NBTAPI;
import com.jho5245.cucumbery.util.storage.data.Constant;
import com.jho5245.cucumbery.util.storage.data.Permission;
import com.jho5245.cucumbery.util.storage.no_groups.ItemStackUtil;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.enchantment.PrepareItemEnchantEvent;
import org.bukkit.inventory.ItemStack;

public class PrepareItemEnchant implements Listener
{
	@EventHandler
	public void onPrepareItemEnchant(PrepareItemEnchantEvent event)
	{
		HumanEntity humanEntity = event.getView().getPlayer();
		ItemStack item = event.getItem(), lapis = event.getInventory().getItem(1);
		if (!ItemStackUtil.itemExists(item))
			return;
		if (humanEntity.getType() == EntityType.PLAYER)
		{
			Player player = (Player) humanEntity;
			if (!Cucumbery.config.getBoolean("grant-default-permission-to-players"))
			{
				if (!Permission.EVENT_ITEM_ENCHANT.has(player))
				{
					event.setCancelled(true);
					return;
				}
				Material material = item.getType();
				if (!player.hasPermission(Permission.EVENT_ITEM_ENCHANT + "." + material.toString().toLowerCase()))
				{
					event.setCancelled(true);
					return;
				}
			}
			if (NBTAPI.isRestricted(player, item, Constant.RestrictionType.NO_ENCHANT))
			{
				event.setCancelled(true);
				return;
			}
			if (ItemStackUtil.itemExists(lapis) && NBTAPI.isRestricted(player, lapis, Constant.RestrictionType.NO_ENCHANT))
			{
				event.setCancelled(true);
			}
		}
	}
}
