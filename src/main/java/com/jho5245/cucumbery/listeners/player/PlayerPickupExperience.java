package com.jho5245.cucumbery.listeners.player;

import com.destroystokyo.paper.event.player.PlayerPickupExperienceEvent;
import com.jho5245.cucumbery.customeffect.CustomEffectManager;
import com.jho5245.cucumbery.customeffect.CustomEffectType;
import com.jho5245.cucumbery.util.MessageUtil;
import com.jho5245.cucumbery.util.storage.component.util.ComponentUtil;
import com.jho5245.cucumbery.util.storage.CustomConfig.UserData;
import org.bukkit.entity.ExperienceOrb;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class PlayerPickupExperience implements Listener
{
  @EventHandler
  public void onPlayerPickupExperience(PlayerPickupExperienceEvent event)
  {
    if (event.isCancelled())
    {
      return;
    }
    Player player = event.getPlayer();
    if (UserData.SPECTATOR_MODE.getBoolean(player))
    {
      event.setCancelled(true);
      return;
    }
    ExperienceOrb experienceOrb = event.getExperienceOrb();
    int experience = experienceOrb.getExperience();
    if (CustomEffectManager.hasEffect(player, CustomEffectType.EXPERIENCE_INTOLERANCE))
    {
      if (player.getTotalExperience() > experience)
      {
        player.giveExp(experience * (-2));
      }
      else
      {
        experienceOrb.remove();
        event.setCancelled(true);
        player.setTotalExperience(0);
        player.setLevel(0);
        player.setExp(0f);
      }
      if (UserData.SHOW_ACTIONBAR_ON_ITEM_PICKUP.getBoolean(player))
      {
        MessageUtil.sendActionBar(player, ComponentUtil.translate("&c%s을(를) 잃었습니다", ComponentUtil.translate("&e경험치 %s", experience)));
      }
      return;
    }
    if (UserData.SHOW_ACTIONBAR_ON_ITEM_PICKUP.getBoolean(player))
    {
      MessageUtil.sendActionBar(player, ComponentUtil.translate("&b%s을(를) 주웠습니다", ComponentUtil.translate("&e경험치 %s", experience)));
    }
  }
}
