package com.jho5245.cucumbery.listeners.block;

import com.jho5245.cucumbery.Cucumbery;
import com.jho5245.cucumbery.events.block.PreCustomBlockBreakEvent;
import com.jho5245.cucumbery.util.storage.data.CustomMaterial;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

public class PreCustomBlockBreak implements Listener
{
  @EventHandler
  public void onPreCustomBlockBreak(PreCustomBlockBreakEvent event)
  {
    if (event.isCancelled())
    {
      return;
    }
    if (Cucumbery.using_mcMMO)
    {
      mcmmo(event);
    }
  }

  void mcmmo(PreCustomBlockBreakEvent event)
  {
    Player player = event.getPlayer();
    ItemStack itemStack = player.getInventory().getItemInMainHand();
    Material material = itemStack.getType();
    CustomMaterial customMaterial = CustomMaterial.itemStackOf(itemStack);
    if (!material.isAir())
    {
      switch (material)
      {
        case DIAMOND_PICKAXE, GOLDEN_PICKAXE, IRON_PICKAXE, NETHERITE_PICKAXE, STONE_PICKAXE, WOODEN_PICKAXE -> PreCustomBlockBreakMcMMO.skill(player);
      }
    }
    if (customMaterial != null)
    {
      switch (customMaterial)
      {
        case COBALT_PICKAXE, COPPER_PICKAXE, MITHRIL_PICKAXE, MITHRIL_PICKAXE_REFINED, MUSHROOM_STEW_PICKAXE, TEST_PICKAXE, TITANIUM_PICKAXE, TITANIUM_PICKAXE_REFINED, TODWOT_PICKAXE, TUNGSTEN_PICKAXE,
                MINDAS_DRILL, TITANIUM_DRILL_R266, TITANIUM_DRILL_R366, TITANIUM_DRILL_R466, TITANIUM_DRILL_R566 -> PreCustomBlockBreakMcMMO.skill(player);
      }
    }
  }
}
