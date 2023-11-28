package com.jho5245.cucumbery.util.storage.no_groups;

import com.jho5245.cucumbery.Cucumbery;
import com.jho5245.cucumbery.util.itemlore.ItemLore;
import com.jho5245.cucumbery.util.storage.data.Constant;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.*;
import org.bukkit.inventory.RecipeChoice.ExactChoice;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class RecipeChecker
{
  public static List<Recipe> recipes;

  //  @SuppressWarnings("ConstantConditions")
  public static void setRecipes()
  {
    recipes = new ArrayList<>();
    Iterator<Recipe> recipeIterator = Bukkit.recipeIterator();
    while (recipeIterator.hasNext())
    {
      try
      {
        Recipe recipe = recipeIterator.next();
        recipes.add(recipe);
      }
      catch (Exception ignored)
      {

      }
    }
    recipes = Collections.unmodifiableList(recipes);
  }

  public static boolean hasCraftingRecipe(@NotNull Material type)
  {
    // SpecialCase
    if (Constant.DURABLE_ITEMS.contains(type))
    {
      return true;
    }
		switch (type)
		{
			case ACACIA_WOOD, BIRCH_WOOD, DARK_OAK_WOOD, JUNGLE_WOOD, OAK_WOOD, SPRUCE_WOOD, STRIPPED_ACACIA_WOOD,
          STRIPPED_BIRCH_WOOD, STRIPPED_JUNGLE_WOOD, STRIPPED_OAK_WOOD, STRIPPED_SPRUCE_WOOD, STRIPPED_DARK_OAK_WOOD,
          LEATHER_HORSE_ARMOR, WHITE_BANNER, ORANGE_BANNER, MAGENTA_BANNER, LIGHT_BLUE_BANNER, YELLOW_BANNER, LIME_BANNER,
          PINK_BANNER, GRAY_BANNER, LIGHT_GRAY_BANNER, CYAN_BANNER, PURPLE_BANNER, BLUE_BANNER, BROWN_BANNER, GREEN_BANNER,
          RED_BANNER, BLACK_BANNER ->
			{
				return true;
			}
			default ->
			{
			}
		}
    try
    {
      for (Recipe recipe : recipes)
      {
        if (recipe instanceof ShapelessRecipe shapelessRecipe)
        {
          List<ItemStack> ingredientList = shapelessRecipe.getIngredientList();
          for (ItemStack ingredient : ingredientList)
          {
            if (ingredient.getType() == type)
            {
              return true;
            }
          }
        }
        if (recipe instanceof ShapedRecipe shapedRecipe)
        {
          Map<Character, ItemStack> ingredientMap = shapedRecipe.getIngredientMap();
          for (char c : ingredientMap.keySet())
          {
            ItemStack item = ingredientMap.get(c);
            if (item != null && item.getType() == type)
            {
              return true;
            }
          }
        }
      }
    }
    catch (Exception e)
    {
      Cucumbery.getPlugin().getLogger().warning(      e.getMessage());
      return false;
    }
    return false;
  }

  public static boolean hasSmeltingRecipe(@NotNull ItemStack itemStack)
  {
    // Special Case
		switch (itemStack.getType())
		{
			case IRON_AXE, IRON_BOOTS, IRON_CHESTPLATE, IRON_HELMET, IRON_HOE, IRON_HORSE_ARMOR,
          IRON_LEGGINGS, IRON_PICKAXE, IRON_SHOVEL, IRON_SWORD, CHAINMAIL_BOOTS, CHAINMAIL_CHESTPLATE,
          CHAINMAIL_HELMET, CHAINMAIL_LEGGINGS, GOLDEN_AXE, GOLDEN_BOOTS, GOLDEN_CHESTPLATE, GOLDEN_HELMET,
          GOLDEN_HOE, GOLDEN_HORSE_ARMOR, GOLDEN_LEGGINGS, GOLDEN_PICKAXE, GOLDEN_SHOVEL, GOLDEN_SWORD, ACACIA_LOG,
          BIRCH_LOG, DARK_OAK_LOG, JUNGLE_LOG, OAK_LOG, SPRUCE_LOG, STRIPPED_ACACIA_LOG, STRIPPED_BIRCH_LOG, STRIPPED_DARK_OAK_LOG,
          STRIPPED_JUNGLE_LOG, STRIPPED_OAK_LOG, STRIPPED_SPRUCE_LOG, ACACIA_WOOD, BIRCH_WOOD, DARK_OAK_WOOD, JUNGLE_WOOD, OAK_WOOD,
          SPRUCE_WOOD, STRIPPED_ACACIA_WOOD, STRIPPED_BIRCH_WOOD, STRIPPED_JUNGLE_WOOD, STRIPPED_OAK_WOOD, STRIPPED_SPRUCE_WOOD,
          STRIPPED_DARK_OAK_WOOD, NETHER_GOLD_ORE ->
			{
				return true;
			}
			default ->
			{
			}
		}
    try
    {
      Iterator<Recipe> recipeIterator = Bukkit.recipeIterator();
      while (recipeIterator.hasNext())
      {
        Recipe recipe = recipeIterator.next();
        if (recipe instanceof CookingRecipe<?> cookingRecipe)
        {
          RecipeChoice recipeChoice = cookingRecipe.getInputChoice();
          if (recipeChoice.test(itemStack) || cookingRecipe.getInput().isSimilar(itemStack))
          {
            return true;
          }
          if (recipeChoice instanceof ExactChoice exactChoice)
          {
            List<ItemStack> choices = exactChoice.getChoices();
            for (ItemStack item : choices)
            {
              if (ItemLore.removeItemLore(item).isSimilar(ItemLore.removeItemLore(itemStack)))
              {
                return true;
              }
            }
          }
        }
      }
    }
    catch (Exception e)
    {
Cucumbery.getPlugin().getLogger().warning(      e.getMessage());
      return false;
    }
    return false;
  }
}
