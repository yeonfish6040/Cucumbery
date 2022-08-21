package com.jho5245.cucumbery.listeners.player.no_groups;

import com.destroystokyo.paper.event.player.PlayerElytraBoostEvent;
import com.jho5245.cucumbery.custom.customeffect.CustomEffect;
import com.jho5245.cucumbery.custom.customeffect.CustomEffectManager;
import com.jho5245.cucumbery.custom.customeffect.type.CustomEffectType;
import com.jho5245.cucumbery.util.nbt.CucumberyTag;
import com.jho5245.cucumbery.util.nbt.NBTAPI;
import com.jho5245.cucumbery.util.storage.data.Constant.ExtraTag;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

public class PlayerElytraBoost implements Listener
{
  @EventHandler
  public void onPlayerElytraBoost(PlayerElytraBoostEvent event)
  {
    if (event.isCancelled())
    {
      return;
    }
    Player player = event.getPlayer();
    if (CustomEffectManager.hasEffect(player, CustomEffectType.ELYTRA_BOOSTER))
    {
      CustomEffect customEffect = CustomEffectManager.getEffect(player, CustomEffectType.ELYTRA_BOOSTER);
      int amplifier = customEffect.getAmplifier() + 1;
      if (Math.random() * 10d < amplifier)
      {
        event.setShouldConsume(false);
      }
    }

    ItemStack itemStack = event.getItemStack();
    if (NBTAPI.arrayContainsValue(NBTAPI.getStringList(NBTAPI.getMainCompound(itemStack.clone()), CucumberyTag.EXTRA_TAGS_KEY), ExtraTag.INFINITE))
    {
      event.setShouldConsume(false);
    }
  }
}
