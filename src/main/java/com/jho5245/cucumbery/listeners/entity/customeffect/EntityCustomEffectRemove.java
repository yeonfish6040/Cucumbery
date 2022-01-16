package com.jho5245.cucumbery.listeners.entity.customeffect;

import com.jho5245.cucumbery.customeffect.CustomEffect;
import com.jho5245.cucumbery.customeffect.CustomEffect.DisplayType;
import com.jho5245.cucumbery.customeffect.CustomEffectManager;
import com.jho5245.cucumbery.customeffect.CustomEffectType;
import com.jho5245.cucumbery.events.entity.EntityCustomEffectRemoveEvent;
import com.jho5245.cucumbery.util.MessageUtil;
import com.jho5245.cucumbery.util.storage.data.Prefix;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class EntityCustomEffectRemove implements Listener
{
  @EventHandler
  public void onEntityCustomEffectRemove(EntityCustomEffectRemoveEvent event)
  {
    Entity entity = event.getEntity();
    CustomEffect customEffect = event.getCustomEffect();
    CustomEffectType customEffectType = customEffect.getEffectType();
    if (entity instanceof Player player && customEffect.getDisplayType() == DisplayType.PLAYER_LIST && CustomEffectManager.getEffects(entity, DisplayType.PLAYER_LIST).isEmpty())
    {
      player.sendPlayerListFooter(Component.empty());
    }
    if (customEffectType == CustomEffectType.MUTE)
    {
      MessageUtil.sendMessage(entity, Prefix.INFO, "채팅 금지가 해제되었습니다");
    }
  }
}
