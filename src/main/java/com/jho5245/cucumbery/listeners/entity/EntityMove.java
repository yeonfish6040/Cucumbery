package com.jho5245.cucumbery.listeners.entity;

import com.jho5245.cucumbery.util.storage.ItemStackUtil;
import com.jho5245.cucumbery.util.storage.data.Variable;
import io.papermc.paper.event.entity.EntityMoveEvent;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class EntityMove implements Listener
{
  @EventHandler
  public void onEntityMove(EntityMoveEvent event)
  {
    if (event.isCancelled())
    {
      return;
    }

    this.getLastTrampledBlock(event);
  }

  private void getLastTrampledBlock(EntityMoveEvent event)
  {
    LivingEntity livingEntity = event.getEntity();
    if (livingEntity.getFallDistance() >= 4)
      return;
    if (event.hasChangedBlock())
    {
      Location location = livingEntity.getLocation().add(0, -0.5, 0);
      Block block = location.getBlock();
      location = block.getLocation();
      Material type = block.getType();
      Material type2 = null;
      ItemStack blockItem = null;
      switch (type)
      {
        case WATER -> type2 = Material.WATER_BUCKET;
        case LAVA -> type2 = Material.LAVA_BUCKET;
        case POWDER_SNOW -> type2 = Material.POWDER_SNOW_BUCKET;
        case CAVE_VINES_PLANT -> type2 = Material.CAVE_VINES;
        case TWISTING_VINES_PLANT -> type2 = Material.TWISTING_VINES;
        case WEEPING_VINES_PLANT -> type2 = Material.WEEPING_VINES;
        case LADDER, VINE, SCAFFOLDING, END_ROD, BAMBOO -> blockItem = ItemStackUtil.getItemStackFromBlock(block);
        case LIGHT, AIR, CAVE_AIR, VOID_AIR -> {
          return;
        }
      }
      if (type2 != null)
      {
        blockItem = ItemStackUtil.getItemStackFromBlock(type2, location);
        ItemMeta itemMeta = blockItem.getItemMeta();
        itemMeta.displayName(Component.translatable(type.getTranslationKey()).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE));
        blockItem.setItemMeta(itemMeta);
      }
      if (blockItem != null)
      {
        Variable.lastTrampledBlock.put(livingEntity.getUniqueId(), blockItem);
        Variable.lastTrampledBlockType.put(livingEntity.getUniqueId(), type);
      }
      else
      {
        Variable.lastTrampledBlock.remove(livingEntity.getUniqueId());
        Variable.lastTrampledBlockType.remove(livingEntity.getUniqueId());
      }
    }
  }
}
