package com.jho5245.cucumbery.listeners.entity.no_groups;

import com.destroystokyo.paper.event.entity.WitchThrowPotionEvent;
import com.jho5245.cucumbery.util.storage.data.Variable;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Witch;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

public class WitchThrowPotion implements Listener
{
  @EventHandler
  public void onWitchThrowPotion(WitchThrowPotionEvent event)
  {
    Witch witch = event.getEntity();
    LivingEntity target = event.getTarget();
    ItemStack potion = event.getPotion();

    if (potion != null)
    {
      Variable.projectile.put(target.getUniqueId(), potion.clone());
      Variable.attackerAndWeapon.put(witch.getUniqueId(), potion.clone());
    }
  }
}
