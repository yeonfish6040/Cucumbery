package com.jho5245.cucumbery.listeners.entity.no_groups;

import com.jho5245.cucumbery.util.storage.data.Variable;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityChangeBlockEvent;

public class EntityChangeBlock implements Listener
{
  @EventHandler
  public void onEntityChangeBlock(EntityChangeBlockEvent event)
  {
    Entity entity = event.getEntity();
    Block block = event.getBlock();
    Location location = block.getLocation();
    YamlConfiguration yamlConfiguration = Variable.blockPlaceData.get(location.getWorld().getName());
    if (yamlConfiguration != null)
    {
     // 불 붙은 화살이 TNT를 붙이는 경우는 제외, 다른 이벤트에서 처리함
      if (entity instanceof Arrow && block.getType() == Material.TNT)
      {
        return;
      }
      yamlConfiguration.set(location.getBlockX() + "_" + location.getBlockY() + "_" + location.getBlockZ(), null);
      Variable.blockPlaceData.put(location.getWorld().getName(), yamlConfiguration);
    }
  }
}
