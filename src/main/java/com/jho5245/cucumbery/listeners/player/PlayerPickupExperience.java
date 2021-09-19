package com.jho5245.cucumbery.listeners.player;

import com.destroystokyo.paper.event.player.PlayerPickupExperienceEvent;
import com.jho5245.cucumbery.util.MessageUtil;
import com.jho5245.cucumbery.util.storage.ComponentUtil;
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
    if (UserData.SHOW_ACTIONBAR_ON_ITEM_PICKUP.getBoolean(player))
    {
      ExperienceOrb experienceOrb = event.getExperienceOrb();
      int experience = experienceOrb.getExperience();
      MessageUtil.sendMessage(player, ComponentUtil.createTranslate("&b%s을(를) 주웠습니다.", ComponentUtil.createTranslate("&e경험치 %s", experience)));
    }
  }
}
