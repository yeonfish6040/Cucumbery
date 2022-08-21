package com.jho5245.cucumbery.listeners.entity.no_groups;

import com.jho5245.cucumbery.util.itemlore.ItemLore;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.VillagerAcquireTradeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.MerchantRecipe;

import java.util.ArrayList;
import java.util.List;

public class VillagerAcquireTrade implements Listener
{
  @EventHandler
  public void onVillagerAcquireTrade(VillagerAcquireTradeEvent event)
  {
    if (event.isCancelled())
    {
      return;
    }

    MerchantRecipe recipe = event.getRecipe();
    List<ItemStack> ingredients = recipe.getIngredients();
    List<ItemStack> newIngredients = new ArrayList<>();
    ItemStack result = recipe.getResult();
    ItemLore.setItemLore(result);
    for (ItemStack item : ingredients)
    {
      ItemLore.setItemLore(item);
      newIngredients.add(item);
    }
    MerchantRecipe newRecipe = new MerchantRecipe(result, recipe.getUses(), recipe.getMaxUses(), recipe.hasExperienceReward(), recipe.getVillagerExperience(), recipe.getPriceMultiplier());
    newRecipe.setIngredients(newIngredients);
    event.setRecipe(newRecipe);
  }
}
