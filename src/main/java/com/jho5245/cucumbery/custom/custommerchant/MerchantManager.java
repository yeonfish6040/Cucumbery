package com.jho5245.cucumbery.custom.custommerchant;

import com.jho5245.cucumbery.util.no_groups.ItemSerializer;
import com.jho5245.cucumbery.util.no_groups.MessageUtil;
import com.jho5245.cucumbery.util.storage.no_groups.CustomConfig;
import com.jho5245.cucumbery.util.storage.no_groups.ItemStackUtil;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.MerchantRecipe;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class MerchantManager
{
  @NotNull
  public static List<MerchantRecipe> getRecipes(@NotNull YamlConfiguration configuration)
  {
    List<MerchantRecipe> merchantRecipes = new ArrayList<>();
    ConfigurationSection root = configuration.getConfigurationSection("recipes");
    MessageUtil.broadcastDebug(root + "");
    if (root != null)
    {
      MessageUtil.broadcastDebug("rootsize : " + root.getKeys(false).size());
      for (String number : root.getKeys(false))
      {
        String resultString = root.getString(number + ".result");
        MessageUtil.broadcastDebug("result : " + resultString);
        if (resultString == null)
        {
          continue;
        }
        ItemStack result = ItemSerializer.deserialize(resultString);
        if (!ItemStackUtil.itemExists(result))
        {
          continue;
        }
        List<ItemStack> ingredients = new ArrayList<>();
        String ingredient1String = root.getString(number + ".ingredient-1");
        if (ingredient1String != null)
        {
          ItemStack ingredient1 = ItemSerializer.deserialize(ingredient1String);
          if (ItemStackUtil.itemExists(ingredient1))
          {
            ingredients.add(ingredient1);
          }
        }
        String ingredient2String = root.getString(number + ".ingredient-2");
        if (ingredient2String != null)
        {
          ItemStack ingredient2 = ItemSerializer.deserialize(ingredient2String);
          if (ItemStackUtil.itemExists(ingredient2))
          {
            ingredients.add(ingredient2);
          }
        }
        if (ingredients.isEmpty())
        {
          continue;
        }
        MerchantRecipe recipe = new MerchantRecipe(result, 0, Integer.MAX_VALUE, false, 0, 0f, true);
        recipe.setIngredients(ingredients);
        merchantRecipes.add(recipe);
      }
    }
    return merchantRecipes;
  }

  public static void setRecipes(@NotNull CustomConfig customConfig, @NotNull List<MerchantRecipe> recipes)
  {
    YamlConfiguration configuration =customConfig.getConfig();
    for (int i = 0; i < recipes.size(); i++)
    {
      MerchantRecipe recipe = recipes.get(i);
      if (configuration.getConfigurationSection("recipes." + i) == null)
      {
        configuration.set("recipes." + i + ".result", ItemSerializer.serialize(recipe.getResult()));
        List<ItemStack> ingredients = recipe.getIngredients();
        for (int j = 1; j <= ingredients.size(); j++)
        {
          configuration.set("recipes." + i + ".ingredient-" + j, ItemSerializer.serialize(ingredients.get(j - 1)));
        }
      }
    }
    customConfig.saveConfig();
  }
}
