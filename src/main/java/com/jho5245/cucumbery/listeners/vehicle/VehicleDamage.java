package com.jho5245.cucumbery.listeners.vehicle;

import com.jho5245.cucumbery.custom.customeffect.CustomEffectManager;
import com.jho5245.cucumbery.custom.customeffect.type.CustomEffectType;
import com.jho5245.cucumbery.custom.customeffect.type.CustomEffectTypeCustomMining;
import org.bukkit.GameMode;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Vehicle;
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
    Vehicle vehicle = event.getVehicle();
    if (vehicle.getScoreboardTags().contains("invincible") || CustomEffectManager.hasEffect(vehicle, CustomEffectType.INVINCIBLE))
    {
      event.setCancelled(true);
      return;
    }
    Entity attacker = event.getAttacker();
    if (attacker instanceof Player player && player.getGameMode() != GameMode.CREATIVE && CustomEffectManager.hasEffect(player, CustomEffectTypeCustomMining.CUSTOM_MINING_SPEED_MODE) &&
            !CustomEffectManager.hasEffect(player, CustomEffectTypeCustomMining.CUSTOM_MINING_SPEED_MODE_2_NO_RESTORE))
    {
      event.setCancelled(true);
    }
  }
}
