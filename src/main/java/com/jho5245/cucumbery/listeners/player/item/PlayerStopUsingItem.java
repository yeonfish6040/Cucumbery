package com.jho5245.cucumbery.listeners.player.item;

import com.jho5245.cucumbery.custom.customeffect.CustomEffectManager;
import com.jho5245.cucumbery.custom.customeffect.type.CustomEffectType;
import com.jho5245.cucumbery.util.no_groups.MessageUtil;
import com.jho5245.cucumbery.util.storage.data.CustomMaterial;
import io.papermc.paper.event.player.PlayerStopUsingItemEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

public class PlayerStopUsingItem implements Listener
{
  @EventHandler
  public void onPlayerStopUsingItem(PlayerStopUsingItemEvent event)
  {
    Player player = event.getPlayer();
    ItemStack itemStack = event.getItem();
    int ticksHeldFor = event.getTicksHeldFor();
    CustomMaterial customMaterial = CustomMaterial.itemStackOf(itemStack);
    if (customMaterial == CustomMaterial.SPYGLASS_TELEPORT)
    {
      if (CustomEffectManager.hasEffect(player, CustomEffectType.SPYGLASS_TELEPORT))
      {
        CustomEffectManager.removeEffect(player, CustomEffectType.SPYGLASS_TELEPORT);
        MessageUtil.sendWarn(player, "우클릭을 멈춰 순간 이동이 취소되었습니다");
        CustomEffectManager.addEffect(player, CustomEffectType.SPYGLASS_TELEPORT_COOLDOWN, 100);
      }
    }
  }
}
