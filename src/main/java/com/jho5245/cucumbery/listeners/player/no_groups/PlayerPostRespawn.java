package com.jho5245.cucumbery.listeners.player.no_groups;

import com.destroystokyo.paper.event.player.PlayerPostRespawnEvent;
import com.jho5245.cucumbery.custom.customeffect.CustomEffectManager;
import com.jho5245.cucumbery.custom.customeffect.CustomEffectType;
import com.jho5245.cucumbery.util.no_groups.MessageUtil;
import com.jho5245.cucumbery.util.storage.data.Prefix;
import com.jho5245.cucumbery.util.storage.data.Variable;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.UUID;

public class PlayerPostRespawn implements Listener
{
  @EventHandler
  public void onPlayerPostRespawn(PlayerPostRespawnEvent event)
  {
    Player player = event.getPlayer();
    UUID uuid = player.getUniqueId();
    if (Variable.buffFreezerEffects.containsKey(uuid))
    {
      MessageUtil.sendMessage(player, Prefix.INFO_CUSTOM_EFFECT, "%s 효과로 버프를 보호하였습니다",
              CustomEffectManager.hasEffect(player, CustomEffectType.BUFF_FREEZE_D) ? CustomEffectType.BUFF_FREEZE_D : CustomEffectType.BUFF_FREEZE);
      player.addPotionEffects(Variable.buffFreezerEffects.get(uuid));
      Variable.buffFreezerEffects.remove(uuid);
    }
    CustomEffectManager.refreshAttributeEffects(player);
  }
}
