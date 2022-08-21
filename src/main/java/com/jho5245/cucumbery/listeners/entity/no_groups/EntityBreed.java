package com.jho5245.cucumbery.listeners.entity.no_groups;

import com.jho5245.cucumbery.util.no_groups.MessageUtil;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.AnimalTamer;
import org.bukkit.entity.Horse;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityBreedEvent;

public class EntityBreed implements Listener
{
  @EventHandler
  @SuppressWarnings("all")
  public void onEntityBreed(EntityBreedEvent event)
  {
    if (event.isCancelled())
    {
      return;
    }
    LivingEntity baby = event.getEntity(), mother = event.getMother(), father = event.getFather();
    if (baby instanceof Horse babyHorse && mother instanceof Horse motherHorse && father instanceof Horse fatherHorse)
    {
      double motherHealth = motherHorse.getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue();
      double fatherHealth = fatherHorse.getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue();
      double motherSpeed = motherHorse.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).getBaseValue();
      double fatherSpeed = fatherHorse.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).getBaseValue();
      double motherJump = motherHorse.getAttribute(Attribute.HORSE_JUMP_STRENGTH).getBaseValue();
      double fatherJump = fatherHorse.getAttribute(Attribute.HORSE_JUMP_STRENGTH).getBaseValue();

      double health = Math.random() * 4 + 25;
      double speed = Math.random() * 0.0625 + 0.275;
      double jump = Math.random() * 0.75 + 0.4;

      health = (motherHealth + fatherHealth + health) / 3;
      speed = (motherSpeed + fatherSpeed + speed) / 3;
      jump = (motherJump + fatherJump + jump) / 3;
      babyHorse.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(health);
      babyHorse.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).setBaseValue(speed);
      babyHorse.getAttribute(Attribute.HORSE_JUMP_STRENGTH).setBaseValue(jump);
      babyHorse.setHealth(health);
      babyHorse.setTamed(true);
      babyHorse.setOwner((AnimalTamer) event.getBreeder());
      MessageUtil.broadcastDebug("health : ", health, ", speed : ", speed, ", jump : ", jump);
    }
  }
}
