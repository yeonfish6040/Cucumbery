package com.jho5245.cucumbery.listeners.vehicle;

import com.jho5245.cucumbery.custom.customeffect.CustomEffectManager;
import com.jho5245.cucumbery.custom.customeffect.CustomEffectType;
import org.bukkit.GameMode;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.vehicle.VehicleDamageEvent;

public class VehicleDamage implements Listener
{
  @EventHandler
  public void onVehicleDamage(VehicleDamageEvent event)
  {
    if (event.isCancelled())
    {
      return;
    }
    Entity attacker = event.getAttacker();
    if (attacker instanceof Player player && player.getGameMode() != GameMode.CREATIVE && CustomEffectManager.hasEffect(player, CustomEffectType.CUSTOM_MINING_SPEED_MODE))
    {
      event.setCancelled(true);
    }
  }
}
