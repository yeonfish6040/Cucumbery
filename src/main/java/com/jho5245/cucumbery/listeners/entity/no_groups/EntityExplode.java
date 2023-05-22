package com.jho5245.cucumbery.listeners.entity.no_groups;

import com.jho5245.cucumbery.Cucumbery;
import com.jho5245.cucumbery.custom.customeffect.CustomEffectManager;
import com.jho5245.cucumbery.custom.customeffect.children.group.LongCustomEffect;
import com.jho5245.cucumbery.custom.customeffect.type.CustomEffectType;
import com.jho5245.cucumbery.util.blockplacedata.BlockPlaceDataConfig;
import com.jho5245.cucumbery.util.blockplacedata.ExplodeEventManager;
import com.jho5245.cucumbery.util.storage.data.CustomMaterial;
import com.jho5245.cucumbery.util.storage.no_groups.SoundPlay;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.Waterlogged;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

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
    if (!event.isCancelled())
    {
      Entity entity = event.getEntity();
      if (entity instanceof TNTPrimed tntPrimed)
      {
        if (entity.getScoreboardTags().contains("custom_material_tnt_i_wont_let_you_go"))
        {
          SoundPlay.playSoundLocation(entity.getLocation(), "custom_i_wont_let_you_go", 4F, 1F);
        }
        if (entity.getScoreboardTags().contains("custom_material_tnt_combat"))
        {
          event.blockList().removeIf(block -> block.getType() != Material.TNT);
        }
        if (entity.getScoreboardTags().contains("custom_material_tnt_drain"))
        {
          event.blockList().clear();
          float radius = tntPrimed.getYield();
          Location location = event.getLocation();
          for (int x = (int) (location.getX() - radius); x <= location.getX() + radius; x++)
          {
            for (int y = (int) (location.getY() - radius); y <= location.getY() + radius; y++)
            {
              for (int z = (int) (location.getZ() - radius); z <= location.getZ() + radius; z++)
              {
                Block block = location.getWorld().getBlockAt(x, y, z);
                if (block.getLocation().distance(location) <= radius)
                {
                  Material blockType = block.getType();
                  if (blockType == Material.WATER || blockType == Material.BUBBLE_COLUMN)
                  {
                    block.setType(Material.AIR);
                  }
                  if (blockType == Material.SEAGRASS || blockType == Material.TALL_SEAGRASS || blockType == Material.KELP || blockType == Material.KELP_PLANT)
                  {
                    Entity owner = tntPrimed.getSource();
                    if (owner instanceof Player player)
                    {
                      BlockBreakEvent blockBreakEvent = new BlockBreakEvent(block, player);
                      Bukkit.getPluginManager().callEvent(blockBreakEvent);
                      if (!blockBreakEvent.isCancelled())
                      {
                        block.breakNaturally();
                        block.setType(Material.AIR);
                      }
                    }
                  }
                  Location loc = new Location(location.getWorld(), x, y, z);
                  ItemStack itemStack = BlockPlaceDataConfig.getItem(loc);
                  CustomMaterial customMaterial = CustomMaterial.itemStackOf(itemStack);
                  if (customMaterial == CustomMaterial.TNT_DRAIN)
                  {
                    event.blockList().add(block);
                  }
                  BlockData blockData = block.getBlockData();
                  if (blockData instanceof Waterlogged waterlogged && waterlogged.isWaterlogged())
                  {
                    waterlogged.setWaterlogged(false);
                    block.setBlockData(waterlogged);
                  }
                }
              }
            }
          }
        }
        if (entity.getScoreboardTags().contains("custom_material_tnt_donut"))
        {
          if (CustomEffectManager.getEffectNullable(entity, CustomEffectType.CUSTOM_MATERIAL_TNT_DONUT) instanceof LongCustomEffect longCustomEffect)
          {
            long l = longCustomEffect.getLong();
            double offset = 360d / l;
            for (int i = 1; i <= l; i++)
            {
              TNTPrimed tnt = (TNTPrimed) entity.getWorld().spawnEntity(entity.getLocation(), EntityType.PRIMED_TNT, SpawnReason.CUSTOM, (e) ->
              {
                TNTPrimed tnt2 = (TNTPrimed) e;
                tnt2.setSource(((TNTPrimed) entity).getSource());
              });
              tnt.setVelocity(new Vector(Math.sin(Math.toRadians(offset * i)), 1, Math.cos(Math.toRadians(offset * i))));
            }
          }
        }
      }
    }
  }
  public void blockPlaceData(EntityExplodeEvent event)
  {
    int defaultExplosionPower = Cucumbery.config.getInt("custom-mining.default-explosion-power.default");
    Entity entity = event.getEntity();
    EntityType entityType = event.getEntityType();
    if (Cucumbery.config.contains("custom-mining.default-explosion-power." + entityType))
    {
      if (entity instanceof Creeper creeper)
      {
        if (creeper.isPowered())
        {
          defaultExplosionPower = Cucumbery.config.getInt("custom-mining.default-explosion-power." + entityType + ".powered");
        }
        else
        {
          defaultExplosionPower = Cucumbery.config.getInt("custom-mining.default-explosion-power." + entityType + ".default");
        }
      }
      else if (entity instanceof WitherSkull witherSkull)
      {
        if (witherSkull.isCharged())
        {
          defaultExplosionPower = Cucumbery.config.getInt("custom-mining.default-explosion-power." + entityType + ".blue");
        }
        else
        {
          defaultExplosionPower = Cucumbery.config.getInt("custom-mining.default-explosion-power." + entityType + ".default");
        }
      }
      else
      {
        defaultExplosionPower = Cucumbery.config.getInt("custom-mining.default-explosion-power." + entityType);
      }
    }
    List<Block> removeList = new ArrayList<>();
    List<Block> removeListButNobreak = new ArrayList<>();
    ExplodeEventManager.manage(defaultExplosionPower, event.getYield(), event.blockList(), removeList, removeListButNobreak);
    event.blockList().removeAll(removeList);
    removeList.removeAll(removeListButNobreak);
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
    if (keyList.isEmpty())
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
    if (keyList.isEmpty())
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
    if (keyList.isEmpty())
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
    if (keyList.isEmpty())
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
    if (keyList.isEmpty())
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
