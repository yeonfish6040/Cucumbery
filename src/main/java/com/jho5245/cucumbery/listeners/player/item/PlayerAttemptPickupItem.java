package com.jho5245.cucumbery.listeners.player.item;

import com.jho5245.cucumbery.util.Method;
import com.jho5245.cucumbery.util.itemlore.ItemLore;
import com.jho5245.cucumbery.util.storage.component.util.ComponentUtil;
import com.jho5245.cucumbery.util.storage.CustomConfig.UserData;
import com.jho5245.cucumbery.util.storage.component.util.ItemNameUtil;
import com.jho5245.cucumbery.util.storage.data.Variable;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerAttemptPickupItemEvent;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

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
    UUID uuid = player.getUniqueId();
    // 아이템 섭취 사용에서 사라지지 않을 경우 아이템 소실 방지를 위한 쿨타임
    if (Variable.playerItemConsumeCauseSwapCooldown.contains(uuid))
    {
      event.setCancelled(true);
      return;
    }
    // 아이템 줍기 모드가 비활성화 되어 있을때
    if (UserData.ITEM_PICKUP_MODE.getString(uuid).equals("disabled"))
    {
      event.setCancelled(true);
      return;
    }
    // 아이템 줍기 모드가 시프트 드롭일때 시프트 상태가 아니면
    else if (UserData.ITEM_PICKUP_MODE.getString(uuid).equals("sneak"))
    {
      if (!player.isSneaking())
      {
        event.setCancelled(true);
        return;
      }
    }
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
        Component itemStackComponent = ItemNameUtil.itemName(itemStack, TextColor.fromHexString("#00ff3c"));
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
