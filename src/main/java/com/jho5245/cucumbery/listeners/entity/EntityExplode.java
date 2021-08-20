package com.jho5245.cucumbery.listeners.entity;

import com.jho5245.cucumbery.Cucumbery;
import com.jho5245.cucumbery.util.ItemSerializer;
import com.jho5245.cucumbery.util.nbt.CucumberyTag;
import com.jho5245.cucumbery.util.nbt.NBTAPI;
import com.jho5245.cucumbery.util.storage.data.Constant;
import com.jho5245.cucumbery.util.storage.data.Variable;
import de.tr7zw.changeme.nbtapi.NBTList;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class EntityExplode implements Listener
{
  @EventHandler
  public void onEntityExplode(EntityExplodeEvent event)
  {
    if (event.isCancelled())
    {
      return;
    }
    this.TNTRegionProjection(event);
    this.creeperRegionProjection(event);
    this.endCrystalRegionProjection(event);
    this.fireballRegionProjection(event);
    this.witherSkullRegionProjection(event);
    this.blockPlaceData(event);
  }

  public void blockPlaceData(EntityExplodeEvent event)
  {
    YamlConfiguration yamlConfiguration = Variable.blockPlaceData.get(event.getEntity().getWorld().getName());
    List<Block> removeList = new ArrayList<>();
    if (yamlConfiguration != null)
    {
      for (Block block : event.blockList())
      {
        Location location = block.getLocation();
        String itemData = yamlConfiguration.getString(location.getBlockX() + "_" + location.getBlockY() + "_" + location.getBlockZ());
        if (itemData != null)
        {
          ItemStack item = ItemSerializer.deserialize(itemData);
          NBTList<String> extraTag = NBTAPI.getStringList(NBTAPI.getMainCompound(item), CucumberyTag.EXTRA_TAGS_KEY);
          for (ItemStack drop : block.getDrops())
          {
            if (drop.getType() == item.getType() || NBTAPI.arrayContainsValue(extraTag, Constant.ExtraTag.FORCE_PRESERVE_BLOCK_NBT.toString()))
            {
              removeList.add(block);
              if (event.getEntity().getType() == EntityType.PRIMED_TNT || Math.random() >= 0.5)
              {
                location.getWorld().dropItemNaturally(location, item);
              }
              break;
            }
          }
          yamlConfiguration.set(location.getBlockX() + "_" + location.getBlockY() + "_" + location.getBlockZ(), null);
        }
      }
      Variable.blockPlaceData.put(event.getEntity().getWorld().getName(), yamlConfiguration);
    }
    event.blockList().removeAll(removeList);
    for (Block block : removeList)
    {
      Bukkit.getServer().getScheduler().runTaskLater(Cucumbery.getPlugin(), () ->
              block.setBlockData(Bukkit.createBlockData(Material.AIR)), 0L);
    }
  }

  private void TNTRegionProjection(EntityExplodeEvent event)
  {
    if (event.getEntityType() != EntityType.PRIMED_TNT && event.getEntityType() != EntityType.MINECART_TNT)
    {
      return;
    }
    FileConfiguration config = Cucumbery.config;
    if (!config.getBoolean("enable-tnt-protection"))
    {
      return;
    }
    List<String> keyList = config.getStringList("tnt-protection-coords");
    if (keyList.size() == 0)
    {
      return;
    }
    List<Block> blocks = new ArrayList<>();
    for (String key : keyList)
    {
      try
      {
        String[] splitter = key.split(",");
        String worldString = splitter[0];
        if (splitter.length == 1 && event.getEntity().getWorld().getName().equals(worldString))
        {
          for (int i = 0; i < event.blockList().size(); i++)
          {
            Block block = event.blockList().get(i);
            if (!(Cucumbery.config.getBoolean("bypass-tnt-protection-if-block-is-tnt") && block.getType() == Material.TNT))
            {
              blocks.add(block);
            }
          }
        }
        else
        {
          String xString = splitter[1];
          String yString = splitter[2];
          String zString = splitter[3];
          World world = Bukkit.getServer().getWorld(worldString);
          if (world == null)
          {
            continue;
          }
          double xFrom = Double.parseDouble(xString.split("~")[0]);
          double xTo = Double.parseDouble(xString.split("~")[1]);
          double yFrom = Double.parseDouble(yString.split("~")[0]);
          double yTo = Double.parseDouble(yString.split("~")[1]);
          double zFrom = Double.parseDouble(zString.split("~")[0]);
          double zTo = Double.parseDouble(zString.split("~")[1]);
          for (int i = 0; i < event.blockList().size(); i++)
          {
            Block block = event.blockList().get(i);
            Location loc = block.getLocation();
            World locWorld = loc.getWorld();
            int x = loc.getBlockX(), y = loc.getBlockY(), z = loc.getBlockZ();

            if (!(Cucumbery.config.getBoolean("bypass-tnt-protection-if-block-is-tnt") && block.getType() == Material.TNT))
            {
              if (locWorld.getName().equals(world.getName()) && x >= xFrom && x <= xTo && y >= yFrom && y <= yTo && z >= zFrom && z <= zTo)
              {
                blocks.add(block);
              }
            }
          }
        }
      }
      catch (Exception ignored)
      {
      }
    }
    event.blockList().removeAll(blocks);
  }

  private void creeperRegionProjection(EntityExplodeEvent event)
  {
    if (event.getEntityType() != EntityType.CREEPER)
    {
      return;
    }
    FileConfiguration config = Cucumbery.config;
    if (!config.getBoolean("enable-creeper-protection"))
    {
      return;
    }
    List<String> keyList = config.getStringList("creeper-protection-coords");
    if (keyList.size() == 0)
    {
      return;
    }
    List<Block> blocks = new ArrayList<>();
    for (String key : keyList)
    {
      try
      {
        String[] splitter = key.split(",");
        String worldString = splitter[0];
        if (splitter.length == 1 && event.getEntity().getWorld().getName().equals(worldString))
        {
          blocks.addAll(event.blockList());
        }
        else
        {
          String xString = splitter[1];
          String yString = splitter[2];
          String zString = splitter[3];
          World world = Bukkit.getServer().getWorld(worldString);
          if (world == null)
          {
            continue;
          }
          double xFrom = Double.parseDouble(xString.split("~")[0]);
          double xTo = Double.parseDouble(xString.split("~")[1]);
          double yFrom = Double.parseDouble(yString.split("~")[0]);
          double yTo = Double.parseDouble(yString.split("~")[1]);
          double zFrom = Double.parseDouble(zString.split("~")[0]);
          double zTo = Double.parseDouble(zString.split("~")[1]);
          for (int i = 0; i < event.blockList().size(); i++)
          {
            Block block = event.blockList().get(i);
            Location loc = block.getLocation();
            World locWorld = loc.getWorld();
            int x = loc.getBlockX(), y = loc.getBlockY(), z = loc.getBlockZ();
            if (locWorld.getName().equals(world.getName()) && x >= xFrom && x <= xTo && y >= yFrom && y <= yTo && z >= zFrom && z <= zTo)
            {
              blocks.add(block);
            }
          }
        }
      }
      catch (Exception ignored)
      {
      }
    }
    event.blockList().removeAll(blocks);
  }

  private void endCrystalRegionProjection(EntityExplodeEvent event)
  {
    if (event.getEntityType() != EntityType.ENDER_CRYSTAL)
    {
      return;
    }
    FileConfiguration config = Cucumbery.config;
    if (!config.getBoolean("enable-end-crystal-protection"))
    {
      return;
    }
    List<String> keyList = config.getStringList("end-crystal-protection-coords");
    if (keyList.size() == 0)
    {
      return;
    }
    List<Block> blocks = new ArrayList<>();
    for (String key : keyList)
    {
      try
      {
        String[] splitter = key.split(",");
        String worldString = splitter[0];
        if (splitter.length == 1 && event.getEntity().getWorld().getName().equals(worldString))
        {
          blocks.addAll(event.blockList());
        }
        else
        {
          String xString = splitter[1];
          String yString = splitter[2];
          String zString = splitter[3];
          World world = Bukkit.getServer().getWorld(worldString);
          if (world == null)
          {
            continue;
          }
          double xFrom = Double.parseDouble(xString.split("~")[0]);
          double xTo = Double.parseDouble(xString.split("~")[1]);
          double yFrom = Double.parseDouble(yString.split("~")[0]);
          double yTo = Double.parseDouble(yString.split("~")[1]);
          double zFrom = Double.parseDouble(zString.split("~")[0]);
          double zTo = Double.parseDouble(zString.split("~")[1]);
          for (int i = 0; i < event.blockList().size(); i++)
          {
            Block block = event.blockList().get(i);
            Location loc = block.getLocation();
            World locWorld = loc.getWorld();
            int x = loc.getBlockX(), y = loc.getBlockY(), z = loc.getBlockZ();

            if (locWorld.getName().equals(world.getName()) && x >= xFrom && x <= xTo && y >= yFrom && y <= yTo && z >= zFrom && z <= zTo)
            {
              blocks.add(block);
            }
          }
        }
      }
      catch (Exception ignored)
      {
      }
    }
    event.blockList().removeAll(blocks);
  }

  private void fireballRegionProjection(EntityExplodeEvent event)
  {
    if (event.getEntityType() != EntityType.FIREBALL)
    {
      return;
    }
    FileConfiguration config = Cucumbery.config;
    if (!config.getBoolean("enable-fireball-protection"))
    {
      return;
    }
    List<String> keyList = config.getStringList("fireball-protection-coords");
    if (keyList.size() == 0)
    {
      return;
    }
    List<Block> blocks = new ArrayList<>();
    for (String key : keyList)
    {
      try
      {
        String[] splitter = key.split(",");
        String worldString = splitter[0];
        if (splitter.length == 1 && event.getEntity().getWorld().getName().equals(worldString))
        {
          blocks.addAll(event.blockList());
        }
        else
        {
          String xString = splitter[1];
          String yString = splitter[2];
          String zString = splitter[3];
          World world = Bukkit.getServer().getWorld(worldString);
          if (world == null)
          {
            continue;
          }
          double xFrom = Double.parseDouble(xString.split("~")[0]);
          double xTo = Double.parseDouble(xString.split("~")[1]);
          double yFrom = Double.parseDouble(yString.split("~")[0]);
          double yTo = Double.parseDouble(yString.split("~")[1]);
          double zFrom = Double.parseDouble(zString.split("~")[0]);
          double zTo = Double.parseDouble(zString.split("~")[1]);
          for (int i = 0; i < event.blockList().size(); i++)
          {
            Block block = event.blockList().get(i);
            Location loc = block.getLocation();
            World locWorld = loc.getWorld();
            int x = loc.getBlockX(), y = loc.getBlockY(), z = loc.getBlockZ();

            if (locWorld.getName().equals(world.getName()) && x >= xFrom && x <= xTo && y >= yFrom && y <= yTo && z >= zFrom && z <= zTo)
            {
              blocks.add(block);
            }
          }
        }
      }
      catch (Exception ignored)
      {
      }
    }
    event.blockList().removeAll(blocks);
  }

  private void witherSkullRegionProjection(EntityExplodeEvent event)
  {
    if (event.getEntityType() != EntityType.FIREBALL)
    {
      return;
    }
    FileConfiguration config = Cucumbery.config;
    if (!config.getBoolean("enable-wither-skull-protection"))
    {
      return;
    }
    List<String> keyList = config.getStringList("wither-skull-protection-coords");
    if (keyList.size() == 0)
    {
      return;
    }
    List<Block> blocks = new ArrayList<>();
    for (String key : keyList)
    {
      try
      {
        String[] splitter = key.split(",");
        String worldString = splitter[0];
        if (splitter.length == 1 && event.getEntity().getWorld().getName().equals(worldString))
        {
          blocks.addAll(event.blockList());
        }
        else
        {
          String xString = splitter[1];
          String yString = splitter[2];
          String zString = splitter[3];
          World world = Bukkit.getServer().getWorld(worldString);
          if (world == null)
          {
            continue;
          }
          double xFrom = Double.parseDouble(xString.split("~")[0]);
          double xTo = Double.parseDouble(xString.split("~")[1]);
          double yFrom = Double.parseDouble(yString.split("~")[0]);
          double yTo = Double.parseDouble(yString.split("~")[1]);
          double zFrom = Double.parseDouble(zString.split("~")[0]);
          double zTo = Double.parseDouble(zString.split("~")[1]);
          for (int i = 0; i < event.blockList().size(); i++)
          {
            Block block = event.blockList().get(i);
            Location loc = block.getLocation();
            World locWorld = loc.getWorld();
            int x = loc.getBlockX(), y = loc.getBlockY(), z = loc.getBlockZ();

            if (locWorld.getName().equals(world.getName()) && x >= xFrom && x <= xTo && y >= yFrom && y <= yTo && z >= zFrom && z <= zTo)
            {
              blocks.add(block);
            }
          }
        }
      }
      catch (Exception ignored)
      {
      }
    }
    event.blockList().removeAll(blocks);
  }
}
