package com.jho5245.cucumbery.listeners.addon.quickshop;

import com.jho5245.cucumbery.util.no_groups.MessageUtil;
import com.jho5245.cucumbery.util.nbt.NBTAPI;
import com.jho5245.cucumbery.util.storage.data.Constant;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.maxgamer.quickshop.api.event.ShopPreCreateEvent;

public class ShopPreCreate implements Listener
{
	@EventHandler
	public void onShopPreCreate(ShopPreCreateEvent event)
	{
		Player player = event.getPlayer();
		ItemStack item = player.getInventory().getItemInMainHand();
		if (NBTAPI.isRestricted(player, item, Constant.RestrictionType.NO_TRADE))
		{
			MessageUtil.sendError(player, "%s은 다른 플레이어와 거래할 수 없는 아이템입니다", item);
			event.setCancelled(true);
		}
	}
}
