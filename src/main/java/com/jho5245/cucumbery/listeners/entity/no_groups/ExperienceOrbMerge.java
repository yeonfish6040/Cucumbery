package com.jho5245.cucumbery.listeners.entity.no_groups;

import com.destroystokyo.paper.event.entity.ExperienceOrbMergeEvent;
import com.jho5245.cucumbery.Cucumbery;
import com.jho5245.cucumbery.custom.customeffect.CustomEffectManager;
import com.jho5245.cucumbery.custom.customeffect.type.CustomEffectType;
import com.jho5245.cucumbery.util.no_groups.MessageUtil;
import com.jho5245.cucumbery.util.no_groups.Method;
import org.bukkit.entity.ExperienceOrb;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class ExperienceOrbMerge implements Listener
{
  @EventHandler
  public void onExperienceOrbMerge(ExperienceOrbMergeEvent event)
  {

    if (event.isCancelled())
    {
      return;
    }

    if (!Cucumbery.config.getBoolean("display-xp-orb-value"))
    {
      return;
    }

    ExperienceOrb source = event.getMergeSource();
    ExperienceOrb target = event.getMergeTarget();

    if (CustomEffectManager.hasEffect(source, CustomEffectType.COMBO_EXPERIENCE) || CustomEffectManager.hasEffect(target, CustomEffectType.COMBO_EXPERIENCE))
    {
      event.setCancelled(true);
      return;
    }

    if (!Method.configContainsLocation(target.getLocation(), Cucumbery.config.getStringList("no-display-xp-orb-value-worlds")))
    {
      int total = source.getExperience() + target.getExperience();
      String configString = Cucumbery.config.getString("display-xp-orb-value-format");
      if (configString != null)
      {
        target.setCustomNameVisible(true);
        target.setCustomName(MessageUtil.n2s(
                "§경§험§치" + configString.replace("%value%", total + "")));
      }
    }
  }
}
