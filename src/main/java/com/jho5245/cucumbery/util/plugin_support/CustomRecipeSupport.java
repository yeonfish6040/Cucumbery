package com.jho5245.cucumbery.util.plugin_support;

import com.jho5245.cucumbery.Cucumbery;
import com.jho5245.cucumbery.util.no_groups.ItemSerializer;
import com.jho5245.cucumbery.util.itemlore.ItemLore;
import com.jho5245.cucumbery.util.storage.no_groups.CustomConfig;
import com.jho5245.cucumbery.util.storage.data.Variable;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;

public class CustomRecipeSupport
{
  public static int updateCustomRecipe()
  {
    int size = 0;
    boolean usefulLore = Cucumbery.config.getBoolean("use-helpful-lore-feature");
    for (String fileName : Variable.customRecipes.keySet())
    {
      CustomConfig customRecipeListConfig = CustomConfig.getCustomConfig("data/CustomRecipe/" + fileName + ".yml");
      YamlConfiguration configuration = customRecipeListConfig.getConfig();
      ConfigurationSection recipes = configuration.getConfigurationSection("recipes");
      if (recipes != null)
      {
        for (String recipeName : recipes.getKeys(false))
        {
          String resultString = recipes.getString(recipeName + ".result");
          ItemStack result = ItemSerializer.deserialize(resultString);
          if (usefulLore)
          {
            ItemLore.setItemLore(result);
          }
          else
          {
            ItemLore.removeItemLore(result);
          }
          size++;
          configuration.set("recipes." + recipeName + ".result", ItemSerializer.serialize(result));
          ConfigurationSection ingredients = recipes.getConfigurationSection(recipeName + ".ingredients");
          if (ingredients != null)
          {
            for (String ingredientNumber : ingredients.getKeys(false))
            {
              String ingredientString = ingredients.getString(ingredientNumber + ".item");
              if (ingredientString == null || ingredientString.startsWith("predicate:"))
              {
                continue;
              }
              ItemStack ingredient = ItemSerializer.deserialize(ingredientString);
              if (usefulLore)
              {
                ItemLore.setItemLore(ingredient);
              }
              else
              {
                ItemLore.removeItemLore(ingredient);
              }
              size++;
              configuration.set("recipes." + recipeName + ".ingredients." + ingredientNumber + ".item", ItemSerializer.serialize(ingredient));
            }
          }
        }
      }
      customRecipeListConfig.saveConfig();
      Variable.customRecipes.put(fileName, configuration);
    }

    return size;
  }
}
