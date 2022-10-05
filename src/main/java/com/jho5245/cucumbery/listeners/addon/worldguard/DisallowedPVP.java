package com.jho5245.cucumbery.listeners.addon.worldguard;

import com.jho5245.cucumbery.Cucumbery;
import com.jho5245.cucumbery.custom.customeffect.CustomEffectManager;
import com.jho5245.cucumbery.custom.customeffect.type.CustomEffectTypeCustomMining;
import com.jho5245.cucumbery.util.no_groups.MessageUtil;
import com.jho5245.cucumbery.util.storage.no_groups.CustomConfig.UserData;
import com.sk89q.worldguard.bukkit.protection.events.DisallowedPVPEvent;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class DisallowedPVP implements Listener
{
  private final Set<UUID> showWarn = new HashSet<>();

  @EventHandler
  public void onDisallowedPVP(DisallowedPVPEvent event)
  {
    Player attacker = event.getAttacker(), depender = event.getDefender();
    if (UserData.EVENT_EXCEPTION_ACCESS.getBoolean(attacker))
    {
      event.setCancelled(true);
      UUID uuid = attacker.getUniqueId();
      if (!showWarn.contains(uuid))
      {
        showWarn.add(uuid);
        MessageUtil.sendWarn(attacker, "이벤트 예외 액세스 모드가 활성화되어 있어 월드가드 PvP 불가능을 우회하고 피해를 입혔습니다!");
        Bukkit.getScheduler().runTaskLater(Cucumbery.getPlugin(), () -> showWarn.remove(uuid), 100L);
      }
    }

    if (CustomEffectManager.hasEffect(attacker, CustomEffectTypeCustomMining.MINER_PVP) && CustomEffectManager.hasEffect(depender, CustomEffectTypeCustomMining.MINER_PVP))
    {
      Location attackerLocation = attacker.getLocation(), dependerLocation = depender.getLocation();
      if (attackerLocation.getBlock().getLightLevel() == 0 && dependerLocation.getBlock().getLightLevel() == 0)
      {
        event.setCancelled(true);
      }
    }
  }
}
