package com.jho5245.cucumbery.listeners.inventory;

import com.jho5245.cucumbery.Cucumbery;
import com.jho5245.cucumbery.util.itemlore.ItemLore;
import com.jho5245.cucumbery.util.nbt.CucumberyTag;
import com.jho5245.cucumbery.util.nbt.NBTAPI;
import com.jho5245.cucumbery.util.no_groups.MessageUtil;
import com.jho5245.cucumbery.util.storage.data.Constant.ExtraTag;
import com.jho5245.cucumbery.util.storage.data.Constant.RestrictionType;
import com.jho5245.cucumbery.util.storage.no_groups.ItemStackUtil;
import de.tr7zw.changeme.nbtapi.NBTItem;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.FurnaceBurnEvent;
import org.bukkit.inventory.FurnaceInventory;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class FurnaceBurn implements Listener
{
  @EventHandler
  public void onFurnaceBurn(FurnaceBurnEvent event)
  {
    if (event.isCancelled())
    {
      return;
    }
    ItemStack fuel = event.getFuel();
    if (NBTAPI.isRestricted(fuel, RestrictionType.NO_FUEL))
    {
      event.setCancelled(true);
      return;
    }
    Block block = event.getBlock();
    BlockState blockState = block.getState();
    if (blockState instanceof Furnace furnace)
    {
      FurnaceInventory furnaceInventory = furnace.getInventory();
      ItemStack smelting = furnaceInventory.getSmelting();
      if (NBTAPI.isRestricted(smelting, RestrictionType.NO_SMELT))
      {
        event.setCancelled(true);
        return;
      }
      if (furnace instanceof Smoker && NBTAPI.isRestricted(smelting, RestrictionType.NO_SMOKER))
      {
        event.setCancelled(true);
        return;
      }
      if (furnace instanceof BlastFurnace && NBTAPI.isRestricted(smelting, RestrictionType.NO_BLAST_FURNACE))
      {
        event.setCancelled(true);
        return;
      }
    }
    NBTItem fuelNBTItem = new NBTItem(fuel);
    int burnTime = fuelNBTItem.hasTag("BurnTime") ? fuelNBTItem.getInteger("BurnTime") : -1;
    if (burnTime > 0)
    {
      if (blockState instanceof BlastFurnace || blockState instanceof Smoker)
      {
        burnTime /= 2;
      }
      event.setBurnTime(burnTime);
    }
    if (blockState instanceof Furnace furnace)
    {
      Double cookSpeed = fuelNBTItem.getDouble("CookSpeed");
      short burnTime2 = furnace.getBurnTime(), cookTime2 = furnace.getCookTime();
      int cookTimeTotal2 = furnace.getCookTimeTotal();
      if (fuelNBTItem.hasTag("CookSpeed") && cookSpeed != null && cookSpeed > 0d && furnace.getCookSpeedMultiplier() != Math.min(cookSpeed, 200d))
      {
        furnace.setCookSpeedMultiplier(Math.min(cookSpeed, 200d));
        furnace.update();
        if (event.willConsumeFuel())
        {
          Bukkit.getScheduler().runTaskLater(Cucumbery.getPlugin(), () ->
          {
            ItemStack fuel2 = furnace.getInventory().getFuel();
            if (ItemStackUtil.itemExists(fuel2))
            {
              fuel2.setAmount(fuel2.getAmount() - 1);
            }
            furnace.getInventory().setFuel(fuel2);
            furnace.setBurnTime(burnTime2);
            furnace.setCookTime(cookTime2);
            furnace.setCookTimeTotal(cookTimeTotal2);
          }, 0L);
        }
      }
      if (furnace.getCookSpeedMultiplier() != 1d && (!fuelNBTItem.hasTag("CookSpeed") || cookSpeed == null || cookSpeed == 0d))
      {
        furnace.setCookSpeedMultiplier(1d);
        furnace.update();
        if (event.willConsumeFuel())
        {
          Bukkit.getScheduler().runTaskLater(Cucumbery.getPlugin(), () ->
          {
            ItemStack fuel2 = furnace.getInventory().getFuel();
            if (ItemStackUtil.itemExists(fuel2))
            {
              fuel2.setAmount(fuel2.getAmount() - 1);
            }
            furnace.getInventory().setFuel(fuel2);
            furnace.setBurnTime(burnTime2);
            furnace.setCookTime(cookTime2);
            furnace.setCookTimeTotal(cookTimeTotal2);
          }, 0L);
        }
      }
    }
    if (!Cucumbery.config.getBoolean("use-helpful-lore-feature"))
    {
      return;
    }
    if (NBTAPI.arrayContainsValue(NBTAPI.getStringList(NBTAPI.getMainCompound(fuel), CucumberyTag.EXTRA_TAGS_KEY), ExtraTag.INFINITE_FUEL.toString()))
    {
      MessageUtil.broadcastDebug("here");
      event.setConsumeFuel(false);
      return;
    }
    if (fuel.getType() == Material.LAVA_BUCKET)
    {
      switch (block.getType())
      {
        case FURNACE -> Bukkit.getServer().getScheduler().runTaskLater(Cucumbery.getPlugin(), () ->
        {
          Furnace furnace = (Furnace) event.getBlock().getState();
          Inventory inv = furnace.getInventory();
          ItemStack bucket = inv.getItem(1);
          if (ItemStackUtil.itemExists(bucket))
          {
            ItemLore.setItemLore(bucket);
          }
        }, 0L);
        case BLAST_FURNACE -> Bukkit.getServer().getScheduler().runTaskLater(Cucumbery.getPlugin(), () ->
        {
          BlastFurnace blastFurnace = (BlastFurnace) event.getBlock().getState();
          Inventory inv = blastFurnace.getInventory();
          ItemStack bucket = inv.getItem(1);
          if (ItemStackUtil.itemExists(bucket))
          {
            ItemLore.setItemLore(bucket);
          }
        }, 0L);
        case SMOKER -> Bukkit.getServer().getScheduler().runTaskLater(Cucumbery.getPlugin(), () ->
        {
          Smoker smoker = (Smoker) event.getBlock().getState();
          Inventory inv = smoker.getInventory();
          ItemStack bucket = inv.getItem(1);
          if (ItemStackUtil.itemExists(bucket))
          {
            ItemLore.setItemLore(bucket);
          }
        }, 0L);
        default ->
        {
        }
      }
    }
  }
}
