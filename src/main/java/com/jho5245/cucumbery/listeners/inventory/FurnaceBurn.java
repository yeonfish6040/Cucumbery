package com.jho5245.cucumbery.listeners.inventory;

import com.jho5245.cucumbery.Cucumbery;
import com.jho5245.cucumbery.util.itemlore.ItemLore;
import com.jho5245.cucumbery.util.nbt.CucumberyTag;
import com.jho5245.cucumbery.util.nbt.NBTAPI;
import com.jho5245.cucumbery.util.storage.ItemStackUtil;
import com.jho5245.cucumbery.util.storage.data.Constant;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.BlastFurnace;
import org.bukkit.block.Furnace;
import org.bukkit.block.Smoker;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.FurnaceBurnEvent;
import org.bukkit.inventory.FurnaceInventory;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class FurnaceBurn implements Listener
{
  @EventHandler public void onFurnaceBurn(FurnaceBurnEvent event)
  {
    if (event.isCancelled())
    {
      return;
    }
    if (!Cucumbery.config.getBoolean("use-helpful-lore-feature"))
    {
      return;
    }
    World world = event.getBlock().getLocation().getWorld();
    if (Cucumbery.config.getStringList("no-use-helpful-lore-feature-worlds").contains(world.getName()))
    {
      return;
    }
    ItemStack fuel = event.getFuel();
    if (NBTAPI.arrayContainsValue(NBTAPI.getStringList(NBTAPI.getMainCompound(fuel), CucumberyTag.EXTRA_TAGS_KEY), Constant.ExtraTag.INFINITE_FUEL.toString()))
    {
      ItemStack finalItem = fuel.clone();
      switch (event.getBlock().getType())
      {
        case FURNACE -> {
          FurnaceInventory furnaceInventory = ((Furnace) event.getBlock().getState()).getInventory();
          Bukkit.getServer().getScheduler().runTaskLater(Cucumbery.getPlugin(), () -> furnaceInventory.setFuel(finalItem), 0L);
        }
        case BLAST_FURNACE -> {
          FurnaceInventory furnaceInventory = ((BlastFurnace) event.getBlock().getState()).getInventory();
          Bukkit.getServer().getScheduler().runTaskLater(Cucumbery.getPlugin(), () -> furnaceInventory.setFuel(finalItem), 0L);
        }
        case SMOKER -> {
          FurnaceInventory furnaceInventory = ((Smoker) event.getBlock().getState()).getInventory();
          Bukkit.getServer().getScheduler().runTaskLater(Cucumbery.getPlugin(), () -> furnaceInventory.setFuel(finalItem), 0L);
        }
      }
    }
    if (fuel.getType() == Material.LAVA_BUCKET)
    {
      switch (event.getBlock().getType())
      {
        case FURNACE:
          Bukkit.getServer().getScheduler().runTaskLater(Cucumbery.getPlugin(), () -> {
            Furnace furnace = (Furnace) event.getBlock().getState();
            Inventory inv = furnace.getInventory();
            ItemStack bucket = inv.getItem(1);
            if (ItemStackUtil.itemExists(bucket))
            {
              ItemLore.setItemLore(bucket);
            }
          }, 0L);
          break;
        case BLAST_FURNACE:
          Bukkit.getServer().getScheduler().runTaskLater(Cucumbery.getPlugin(), () -> {
            BlastFurnace blastFurnace = (BlastFurnace) event.getBlock().getState();
            Inventory inv = blastFurnace.getInventory();
            ItemStack bucket = inv.getItem(1);
            if (ItemStackUtil.itemExists(bucket))
            {
              ItemLore.setItemLore(bucket);
            }
          }, 0L);
          break;
        case SMOKER:
          Bukkit.getServer().getScheduler().runTaskLater(Cucumbery.getPlugin(), () -> {
            Smoker smoker = (Smoker) event.getBlock().getState();
            Inventory inv = smoker.getInventory();
            ItemStack bucket = inv.getItem(1);
            if (ItemStackUtil.itemExists(bucket))
            {
              ItemLore.setItemLore(bucket);
            }
          }, 0L);
          break;
        default:
          break;
      }
    }
  }
}
