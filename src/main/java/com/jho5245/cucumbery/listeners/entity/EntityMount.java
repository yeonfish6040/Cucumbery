package com.jho5245.cucumbery.listeners.entity;

import com.jho5245.cucumbery.Cucumbery;
import com.jho5245.cucumbery.util.MessageUtil;
import com.jho5245.cucumbery.util.storage.ComponentUtil;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
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
    Entity entity = event.getEntity();
    if (entity instanceof Player player)
    {
      MessageUtil.sendActionBar(player, ComponentUtil.createTranslate("mount.onboard", Component.keybind("key.sneak")));
      for (int i = 0; i < 5; i++)
      {
        Bukkit.getServer().getScheduler().runTaskLater(Cucumbery.getPlugin(), () ->
                MessageUtil.sendActionBar(player, ComponentUtil.createTranslate("mount.onboard", Component.keybind("key.sneak"))), i);
      }
    }
  }
}
