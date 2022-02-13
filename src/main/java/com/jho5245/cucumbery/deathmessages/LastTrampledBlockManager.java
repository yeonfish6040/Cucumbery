package com.jho5245.cucumbery.deathmessages;

import com.jho5245.cucumbery.util.no_groups.Method;
import com.jho5245.cucumbery.util.storage.no_groups.ItemStackUtil;
import com.jho5245.cucumbery.util.storage.data.Variable;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class LastTrampledBlockManager
{
  public static void lastTrampledBlock(@NotNull LivingEntity livingEntity, boolean hasChangedBlock)
  {
    if (livingEntity.getFallDistance() >= 4)
    {
      return;
    }
    if (hasChangedBlock)
    {
      Location location = livingEntity.getLocation().add(0, -0.1, 0);
      Block block = location.getBlock();
      Material type = block.getType();
      @Nullable ItemStack blockItem = null;
      if (ItemStackUtil.isBlockStateMetadatable(type))
      {
        blockItem = ItemStackUtil.getItemStackFromBlock(block);
        if (Method.containsIgnoreCase(type.toString(), "banner", "sign"))
        {
          blockItem = null;
        }
      }
      switch (type)
      {
        case WATER,
                LAVA, POWDER_SNOW,
                CAVE_VINES_PLANT,
                TWISTING_VINES_PLANT,
                WEEPING_VINES_PLANT,
                LADDER, VINE, SCAFFOLDING, END_ROD, BAMBOO, PLAYER_HEAD, PLAYER_WALL_HEAD,
                TURTLE_EGG -> blockItem = ItemStackUtil.getItemStackFromBlock(block);
        case LIGHT, AIR, CAVE_AIR, VOID_AIR -> {
          return;
        }
      }
      if (livingEntity.isGliding())
      {
        blockItem = null;
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
