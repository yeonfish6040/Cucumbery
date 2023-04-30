package com.jho5245.cucumbery.listeners.player.item;

import com.jho5245.cucumbery.Cucumbery;
import com.jho5245.cucumbery.custom.customeffect.CustomEffectManager;
import com.jho5245.cucumbery.custom.customeffect.type.CustomEffectType;
import com.jho5245.cucumbery.util.itemlore.ItemLore;
import com.jho5245.cucumbery.util.nbt.NBTAPI;
import com.jho5245.cucumbery.util.no_groups.Method;
import com.jho5245.cucumbery.util.storage.component.util.ComponentUtil;
import com.jho5245.cucumbery.util.storage.component.util.ItemNameUtil;
import com.jho5245.cucumbery.util.storage.data.Constant;
import com.jho5245.cucumbery.util.storage.data.CustomMaterial;
import com.jho5245.cucumbery.util.storage.data.Permission;
import com.jho5245.cucumbery.util.storage.data.Variable;
import com.jho5245.cucumbery.util.storage.no_groups.CustomConfig.UserData;
import com.jho5245.cucumbery.util.storage.no_groups.ItemStackUtil;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Bukkit;
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
    if (UserData.SPECTATOR_MODE.getBoolean(player))
    {
      return;
    }
    Item item = event.getItem();
    ItemStack itemStack = item.getItemStack();
    if (NBTAPI.isRestricted(player, itemStack, Constant.RestrictionType.NO_PICKUP))
    {
      return;
    }
    // 아이템 섭취 사용에서 사라지지 않을 경우 아이템 소실 방지를 위한 쿨타임
    if (Variable.playerItemConsumeCauseSwapCooldown.contains(uuid))
    {
      return;
    }
    // 아이템 줍기 모드가 비활성화 되어 있을때
    if (UserData.ITEM_PICKUP_MODE.getString(uuid).equals("disabled"))
    {
      return;
    }
    // 아이템 줍기 모드가 시프트 드롭일때 시프트 상태가 아니면
    else if (UserData.ITEM_PICKUP_MODE.getString(uuid).equals("sneak"))
    {
      if (!player.isSneaking())
      {
        return;
      }
    }

    if (CustomEffectManager.hasEffect(player, CustomEffectType.CURSE_OF_PICKUP) || Variable.scrollReinforcing.contains(uuid) || (!Permission.EVENT2_ANTI_ALLPLAYER.has(player) && Constant.AllPlayer.ITEM_PICKUP.isEnabled()))
    {
      return;
    }

    if (Method.usingLoreFeature(player))
    {
      ItemLore.setItemLore(itemStack);
    }
    else
    {
      ItemLore.removeItemLore(itemStack);
    }
    item.setItemStack(itemStack);
    CustomMaterial customMaterial = CustomMaterial.itemStackOf(itemStack);
    if (customMaterial != null)
    {
      switch (customMaterial)
      {
        case RUNE_DESTRUCTION, RUNE_EARTHQUAKE ->
        {
          return;
        }
      }
    }
    int amount = itemStack.getAmount() - event.getRemaining();
    if (amount > 0)
    {
      Bukkit.getScheduler().runTaskLaterAsynchronously(Cucumbery.getPlugin(), () ->
      {
        if (item.isValid() && ItemStackUtil.itemExists(item.getItemStack()))
        {
          Method.updateItem(item);
        }
      }, 0L);
      if (UserData.SHOW_ACTIONBAR_ON_ITEM_PICKUP.getBoolean(player.getUniqueId()))
      {
        Component itemStackComponent = ItemNameUtil.itemName(itemStack, TextColor.fromHexString("#00ff3c"));
        if (amount == 1 && itemStack.getType().getMaxStackSize() == 1)
        {
          player.sendActionBar(ComponentUtil.translate("#00ccff;%s을(를) 주웠습니다", itemStackComponent));
        }
        else
        {
          player.sendActionBar(ComponentUtil.translate("#00ccff;%s을(를) %s개 주웠습니다", itemStackComponent, "#00ff3c;" + amount));
        }
      }
    }
  }
}
