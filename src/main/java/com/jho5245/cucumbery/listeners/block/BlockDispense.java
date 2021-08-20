package com.jho5245.cucumbery.listeners.block;

import com.jho5245.cucumbery.Cucumbery;
import com.jho5245.cucumbery.util.Method;
import com.jho5245.cucumbery.util.itemlore.ItemLore;
import com.jho5245.cucumbery.util.nbt.NBTAPI;
import com.jho5245.cucumbery.util.storage.ItemStackUtil;
import com.jho5245.cucumbery.util.storage.data.Constant;
import com.jho5245.cucumbery.util.storage.data.Variable;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Dispenser;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockDispenseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class BlockDispense implements Listener
{
  @EventHandler
  public void onBlockDispense(BlockDispenseEvent event)
  {
    Block block = event.getBlock();
    ItemStack item = event.getItem();
    Material itemType = item.getType();
    if (ItemStackUtil.itemExists(item))
    {
      Material blockType = block.getType();
      if (blockType == Material.DISPENSER)
      {
        if (NBTAPI.isRestricted(item, Constant.RestrictionType.NO_DISPENSER_DISPENSE))
        {
          event.setCancelled(true);
          return;
        }
        switch (itemType)
        {
          case FIRE_CHARGE, LINGERING_POTION, FIREWORK_ROCKET, ARROW, TIPPED_ARROW, SPECTRAL_ARROW, SPLASH_POTION, EGG, SNOWBALL, ENDER_PEARL, EXPERIENCE_BOTTLE -> {
            Variable.blockAttackerAndBlock.put(block.getLocation().toString(), ItemStackUtil.getItemStackFromBlock(block));
            Variable.blockAttackerAndWeapon.put(block.getLocation().toString(), item.clone());
                    }
        }
      }
      if (blockType == Material.DROPPER)
      {
        if (NBTAPI.isRestricted(item, Constant.RestrictionType.NO_DROPPER_DISPENSE))
        {
          event.setCancelled(true);
          return;
        }
      }
      switch (item.getType())
      {
        case BUCKET:
        case COD_BUCKET:
        case SALMON_BUCKET:
        case LAVA_BUCKET:
        case WATER_BUCKET:
        case PUFFERFISH_BUCKET:
        case TROPICAL_FISH_BUCKET:
          boolean usefulLore = Cucumbery.config.getBoolean("use-helpful-lore-feature")
                  && !Method.configContainsLocation(block.getLocation(), Cucumbery.config.getStringList("no-use-helpful-lore-feature-worlds"));
          if (usefulLore)
          {
            Bukkit.getServer().getScheduler().runTaskLater(Cucumbery.getPlugin(), () ->
            {
              Dispenser dispenser = (Dispenser) block.getState();
              Inventory inv = dispenser.getInventory();
              for (int i = 0; i < inv.getSize(); i++)
              {
                ItemStack invItem = inv.getItem(i);
                if (ItemStackUtil.itemExists(invItem))
                {
                  ItemLore.setItemLore(invItem);
                }
              }
            }, 0L);
          }
          break;
        default:
          break;
      }
    }
  }
}
