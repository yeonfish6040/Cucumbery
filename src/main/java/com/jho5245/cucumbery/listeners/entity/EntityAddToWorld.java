package com.jho5245.cucumbery.listeners.entity;

import com.destroystokyo.paper.event.entity.EntityAddToWorldEvent;
import com.jho5245.cucumbery.Cucumbery;
import com.jho5245.cucumbery.util.MessageUtil;
import com.jho5245.cucumbery.util.Method;
import org.bukkit.entity.Entity;
import org.bukkit.entity.ExperienceOrb;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class EntityAddToWorld implements Listener
{
  @EventHandler
  public void onEntityAddToWorld(EntityAddToWorldEvent event)
  {
    Entity entity = event.getEntity();
    this.experienceOrbDisplay(entity);
  }

  private void experienceOrbDisplay(Entity entity)
  {
    if (!(entity instanceof ExperienceOrb))
    {
      return;
    }
    if (!Cucumbery.config.getBoolean("display-xp-orb-value"))
    {
      return;
    }

    ExperienceOrb experienceOrb = (ExperienceOrb) entity;

    if (!Method.configContainsLocation(experienceOrb.getLocation(), Cucumbery.config.getStringList("no-display-xp-orb-value-worlds")))
    {
      int xp = experienceOrb.getExperience();
      String configString = Cucumbery.config.getString("display-xp-orb-value-format");
      if (configString != null)
      {

        experienceOrb.setCustomNameVisible(true);
        experienceOrb.setCustomName(MessageUtil.n2s(
                "§경§험§치" + configString.replace("%value%", xp + "")));
      }
    }
  }
}
