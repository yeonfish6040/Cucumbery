package com.jho5245.cucumbery.util.storage.no_groups;

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
//        if (recipe instanceof ShapedRecipe shapedRecipe)
//        {
//          Map<Character, RecipeChoice> itemStackMap = shapedRecipe.getChoiceMap();
//          String keyValue = shapedRecipe.getKey().getKey();
//          ShapedRecipe newShapedRecipe = new ShapedRecipe(NamespacedKey.fromString("cucumbery:" + keyValue), recipe.getResult());
//          for (char c : itemStackMap.keySet())
//          {
//            RecipeChoice recipeChoice = itemStackMap.get(c);
//            if (recipeChoice instanceof MaterialChoice materialChoice)
//            {
//              List<Material> materials = materialChoice.getChoices();
//              List<ItemStack> itemStacks = new ArrayList<>();
//              for (Material material : materials)
//              {
//                itemStacks.add(ItemStackUtil.loredItemStack(material));
//              }
//              recipeChoice = new RecipeChoice.ExactChoice(itemStacks);
//            }
//            if (recipeChoice instanceof ExactChoice exactChoice)
//            {
//              List<ItemStack> itemStacks = new ArrayList<>(exactChoice.getChoices());
//              for (int i = 0; i < itemStacks.size(); i++)
//              {
//                itemStacks.set(i, ItemLore.setItemLore(itemStacks.get(i)));
//              }
//              recipeChoice = new RecipeChoice.ExactChoice(itemStacks);
//            }
//            newShapedRecipe.setIngredient(c, recipeChoice);
//          }
//          recipes.add(newShapedRecipe);
//        }
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
      case ACACIA_WOOD:
      case BIRCH_WOOD:
      case DARK_OAK_WOOD:
      case JUNGLE_WOOD:
      case OAK_WOOD:
      case SPRUCE_WOOD:
      case STRIPPED_ACACIA_WOOD:
      case STRIPPED_BIRCH_WOOD:
      case STRIPPED_JUNGLE_WOOD:
      case STRIPPED_OAK_WOOD:
      case STRIPPED_SPRUCE_WOOD:
      case STRIPPED_DARK_OAK_WOOD:
      case LEATHER_HORSE_ARMOR:
      case WHITE_BANNER:
      case ORANGE_BANNER:
      case MAGENTA_BANNER:
      case LIGHT_BLUE_BANNER:
      case YELLOW_BANNER:
      case LIME_BANNER:
      case PINK_BANNER:
      case GRAY_BANNER:
      case LIGHT_GRAY_BANNER:
      case CYAN_BANNER:
      case PURPLE_BANNER:
      case BLUE_BANNER:
      case BROWN_BANNER:
      case GREEN_BANNER:
      case RED_BANNER:
      case BLACK_BANNER:
        return true;
      default:
        break;
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
      e.printStackTrace();
      return false;
    }
    return false;
  }

  public static boolean hasSmeltingRecipe(@NotNull ItemStack itemStack)
  {
    // Special Case
    switch (itemStack.getType())
    {
      case IRON_AXE:
      case IRON_BOOTS:
      case IRON_CHESTPLATE:
      case IRON_HELMET:
      case IRON_HOE:
      case IRON_HORSE_ARMOR:
      case IRON_LEGGINGS:
      case IRON_PICKAXE:
      case IRON_SHOVEL:
      case IRON_SWORD:
      case CHAINMAIL_BOOTS:
      case CHAINMAIL_CHESTPLATE:
      case CHAINMAIL_HELMET:
      case CHAINMAIL_LEGGINGS:
      case GOLDEN_AXE:
      case GOLDEN_BOOTS:
      case GOLDEN_CHESTPLATE:
      case GOLDEN_HELMET:
      case GOLDEN_HOE:
      case GOLDEN_HORSE_ARMOR:
      case GOLDEN_LEGGINGS:
      case GOLDEN_PICKAXE:
      case GOLDEN_SHOVEL:
      case GOLDEN_SWORD:
      case ACACIA_LOG:
      case BIRCH_LOG:
      case DARK_OAK_LOG:
      case JUNGLE_LOG:
      case OAK_LOG:
      case SPRUCE_LOG:
      case STRIPPED_ACACIA_LOG:
      case STRIPPED_BIRCH_LOG:
      case STRIPPED_DARK_OAK_LOG:
      case STRIPPED_JUNGLE_LOG:
      case STRIPPED_OAK_LOG:
      case STRIPPED_SPRUCE_LOG:
      case ACACIA_WOOD:
      case BIRCH_WOOD:
      case DARK_OAK_WOOD:
      case JUNGLE_WOOD:
      case OAK_WOOD:
      case SPRUCE_WOOD:
      case STRIPPED_ACACIA_WOOD:
      case STRIPPED_BIRCH_WOOD:
      case STRIPPED_JUNGLE_WOOD:
      case STRIPPED_OAK_WOOD:
      case STRIPPED_SPRUCE_WOOD:
      case STRIPPED_DARK_OAK_WOOD:
      case NETHER_GOLD_ORE:
        return true;
      default:
        break;
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
      e.printStackTrace();
      return false;
    }
    return false;
  }
}
