package com.jho5245.cucumbery.listeners.enchantment;

import com.jho5245.cucumbery.Cucumbery;
import com.jho5245.cucumbery.util.itemlore.ItemLore;
import com.jho5245.cucumbery.util.itemlore.ItemLoreView;
import com.jho5245.cucumbery.util.no_groups.Method;
import com.jho5245.cucumbery.util.storage.no_groups.CustomConfig.UserData;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.enchantment.EnchantItemEvent;
import org.bukkit.inventory.ItemStack;

public class EnchantItem implements Listener
{
	@EventHandler
	public void onEnchantItem(EnchantItemEvent event)
	{
		Player player = event.getEnchanter();
		if (UserData.USE_HELPFUL_LORE_FEATURE.getBoolean(player.getUniqueId()) && Cucumbery.config.getBoolean("use-helpful-lore-feature")
				&& !Method.configContainsLocation(player.getLocation(), Cucumbery.config.getStringList("no-use-helpful-lore-feature-worlds")))
		{
			Bukkit.getServer().getScheduler().runTaskLater(Cucumbery.getPlugin(), () ->
			{
				ItemStack item = event.getView().getItem(0);
				if (item != null)
				{
					ItemLore.setItemLore(item, new ItemLoreView(player) );
				}
			}, 0L);
		}
	}
}
