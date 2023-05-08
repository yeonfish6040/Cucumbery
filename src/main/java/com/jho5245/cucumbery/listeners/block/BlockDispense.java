package com.jho5245.cucumbery.listeners.block;

import com.jho5245.cucumbery.Cucumbery;
import com.jho5245.cucumbery.custom.customeffect.CustomEffectManager;
import com.jho5245.cucumbery.custom.customeffect.children.group.LongCustomEffectImple;
import com.jho5245.cucumbery.custom.customeffect.type.CustomEffectType;
import com.jho5245.cucumbery.util.itemlore.ItemLore;
import com.jho5245.cucumbery.util.nbt.NBTAPI;
import com.jho5245.cucumbery.util.storage.data.Constant;
import com.jho5245.cucumbery.util.storage.data.CustomMaterial;
import com.jho5245.cucumbery.util.storage.data.Variable;
import com.jho5245.cucumbery.util.storage.no_groups.ItemStackUtil;
import de.tr7zw.changeme.nbtapi.NBTItem;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Dispenser;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockDispenseEvent;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

public class BlockDispense implements Listener
{
  @EventHandler
  public void onBlockDispense(BlockDispenseEvent event)
  {
    Block block = event.getBlock();
    ItemStack item = event.getItem().clone();
    Material itemType = item.getType();
    CustomMaterial customMaterial = CustomMaterial.itemStackOf(item);
    if (customMaterial != null)
    {
      switch (customMaterial)
      {
        case UNBINDING_SHEARS ->
        {
          if (block.getState() instanceof Dispenser)
          {
            event.setCancelled(true);
            return;
          }
        }
        case TNT_COMBAT, TNT_DONUT, TNT_DRAIN, TNT_I_WONT_LET_YOU_GO, TNT_SUPERIOR ->
        {
          if (block.getState() instanceof Dispenser dispenser)
          {
            Bukkit.getScheduler().runTaskLater(Cucumbery.getPlugin(), () ->
                    dispenser.getInventory().removeItem(item), 0L);
            event.setCancelled(true);
            Vector vector = event.getVelocity();
            Location location = new Location(block.getWorld(), vector.getX(), vector.getY(), vector.getZ());
            location.getWorld().spawnEntity(location, EntityType.PRIMED_TNT, SpawnReason.CUSTOM, entity ->
            {
              entity.getScoreboardTags().add("custom_material_" + customMaterial.toString().toLowerCase());
              if (customMaterial == CustomMaterial.TNT_DONUT)
              {
                CustomEffectManager.addEffect(entity, new LongCustomEffectImple(CustomEffectType.CUSTOM_MATERIAL_TNT_DONUT, new NBTItem(item).getShort("Size")));
              }
            });
            return;
          }
        }
      }
    }

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
          case FIRE_CHARGE, LINGERING_POTION, FIREWORK_ROCKET, ARROW, TIPPED_ARROW, SPECTRAL_ARROW, SPLASH_POTION, EGG, SNOWBALL, ENDER_PEARL, EXPERIENCE_BOTTLE ->
          {
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
        case BUCKET, COD_BUCKET, SALMON_BUCKET, LAVA_BUCKET, WATER_BUCKET, PUFFERFISH_BUCKET, TROPICAL_FISH_BUCKET ->
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
        default ->
        {
        }
      }
    }
  }
}
