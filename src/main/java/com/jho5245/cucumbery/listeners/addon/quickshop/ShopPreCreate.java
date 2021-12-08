package com.jho5245.cucumbery.listeners.addon.quickshop;

import com.jho5245.cucumbery.util.MessageUtil;
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
			MessageUtil.sendError(player, "캐릭터 귀속 아이템으로는 상점을 개설할 수 없습니다.");
			event.setCancelled(true);
		}
	}
}
