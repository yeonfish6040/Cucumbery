package com.jho5245.cucumbery.listeners.entity.no_groups;

import com.google.common.base.Function;
import com.jho5245.cucumbery.custom.customeffect.CustomEffectManager;
import com.jho5245.cucumbery.custom.customeffect.type.CustomEffectType;
import com.jho5245.cucumbery.custom.customeffect.children.group.StringCustomEffectImple;
import com.jho5245.cucumbery.events.entity.EntityLandOnGroundEvent;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByBlockEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.EntityDamageEvent.DamageModifier;

import java.util.HashMap;

@SuppressWarnings("all")
public class EntityLandOnGround implements Listener
{
  @EventHandler
  public void onEntityLandOnGround(EntityLandOnGroundEvent event)
  {
    Entity entity = event.getEntity();
    if (entity instanceof Player player)
    {
      double fallDistance = entity.getFallDistance();
      if (!player.isSneaking() && CustomEffectManager.hasEffect(entity, CustomEffectType.VAR_PODAGRA) && fallDistance > 0 && fallDistance < 3.5)
      {
        HashMap<DamageModifier, Double> map = new HashMap<>();
        HashMap<DamageModifier, Function<? super Double, Double>> map2 = new HashMap<>();
        map.put(DamageModifier.BASE, 1d);
        Function<Double, Double> function = input -> 1d;
        map2.put(DamageModifier.BASE, function);
        EntityDamageEvent lastDamageCause = entity.getLastDamageCause();
        if (lastDamageCause instanceof EntityDamageByEntityEvent damageByEntityEvent)
        {
          Entity damager = damageByEntityEvent.getDamager();
          boolean isCritical = damageByEntityEvent.isCritical();
          lastDamageCause = new EntityDamageByEntityEvent(damager, entity, DamageCause.FALL, map, map2, isCritical);
        }
        else if (lastDamageCause instanceof EntityDamageByBlockEvent damageByBlockEvent)
        {
          Block block = damageByBlockEvent.getDamager();
          lastDamageCause = new EntityDamageByBlockEvent(block, entity, DamageCause.FALL, map, map2);
        }
        else
        {
          lastDamageCause = new EntityDamageEvent(entity, DamageCause.FALL, 1);
        }
        CustomEffectManager.addEffect(entity, new StringCustomEffectImple(CustomEffectType.CUSTOM_DEATH_MESSAGE, "fall"));
        player.damage(1);
        player.setNoDamageTicks(0);
        entity.setLastDamageCause(lastDamageCause);
      }
    }

  }
}
