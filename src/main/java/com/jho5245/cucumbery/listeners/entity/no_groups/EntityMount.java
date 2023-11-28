package com.jho5245.cucumbery.listeners.entity.no_groups;

import com.jho5245.cucumbery.Cucumbery;
import com.jho5245.cucumbery.util.no_groups.MessageUtil;
import com.jho5245.cucumbery.util.storage.component.util.ComponentUtil;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.entity.AreaEffectCloud;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.spigotmc.event.entity.EntityMountEvent;

public class EntityMount implements Listener
{
  @EventHandler
  public void onEntityMount(EntityMountEvent event)
  {
    if (event.isCancelled())
    {
      return;
    }
    Entity entity = event.getEntity();
    Entity mount = event.getMount();
    if (mount instanceof AreaEffectCloud && mount.getScoreboardTags().contains("cucumbery-command-ride"))
    {
      event.setCancelled(true);
      return;
    }
    if (entity instanceof Player player)
    {
      MessageUtil.sendActionBar(player, ComponentUtil.translate("mount.onboard", Component.keybind("key.sneak")));
      for (int i = 0; i < 5; i++)
      {
        Bukkit.getServer().getScheduler().runTaskLater(Cucumbery.getPlugin(), () ->
                MessageUtil.sendActionBar(player, ComponentUtil.translate("mount.onboard", Component.keybind("key.sneak"))), i);
      }
    }
  }
}
