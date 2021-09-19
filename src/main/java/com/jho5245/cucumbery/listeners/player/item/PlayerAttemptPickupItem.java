package com.jho5245.cucumbery.listeners.player.item;

import com.jho5245.cucumbery.util.Method;
import com.jho5245.cucumbery.util.itemlore.ItemLore;
import com.jho5245.cucumbery.util.storage.ComponentUtil;
import com.jho5245.cucumbery.util.storage.CustomConfig.UserData;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerAttemptPickupItemEvent;
import org.bukkit.inventory.ItemStack;

public class PlayerAttemptPickupItem implements Listener
{
  @EventHandler
  public void onPlayerAttemptPickupItem(PlayerAttemptPickupItemEvent event)
  {
    if (event.isCancelled())
    {
      return;
    }
    Player player = event.getPlayer();
    Item item = event.getItem();
    ItemStack itemStack = item.getItemStack();
    if (Method.usingLoreFeature(player))
    {
      ItemLore.setItemLore(itemStack);
    }
    else
    {
      ItemLore.removeItemLore(itemStack);
    }
    item.setItemStack(itemStack);
    int amount = itemStack.getAmount() - event.getRemaining();
    if (amount > 0)
    {
      if (UserData.SHOW_ACTIONBAR_ON_ITEM_PICKUP.getBoolean(player.getUniqueId()))
      {
        Component itemStackComponent = ComponentUtil.itemName(itemStack, TextColor.fromHexString("#00ff3c"));
        if (amount == 1 && itemStack.getType().getMaxStackSize() == 1)
        {
          player.sendActionBar(ComponentUtil.createTranslate("#00ccff;%s을(를) 주웠습니다.", itemStackComponent));
        }
        else
        {
          player.sendActionBar(ComponentUtil.createTranslate("#00ccff;%s을(를) %s개 주웠습니다.", itemStackComponent, "#00ff3c;" + amount));
        }
      }
    }
  }
}
