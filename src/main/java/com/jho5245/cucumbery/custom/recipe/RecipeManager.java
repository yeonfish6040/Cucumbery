package com.jho5245.cucumbery.custom.recipe;

import com.jho5245.cucumbery.Cucumbery;
import org.bukkit.Bukkit;
import org.bukkit.Keyed;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.Recipe;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.Set;

public class RecipeManager
{
  public static Set<NamespacedKey> recipes = new HashSet<>();

  public static void registerRecipe()
  {
//    register(new ShapedRecipe(of("towdot_pickaxe"), CustomMaterial.TODWOT_PICKAXE.create())
//            .shape("aaa", "xbx", "xbx")
//            .setIngredient('a', Material.BONE_BLOCK).setIngredient('b', Material.BONE)
//    );
//    register(new ShapedRecipe(of("mithril_pickaxe"), CustomMaterial.MITHRIL_PICKAXE.create())
//            .shape("aaa", "xbx", "xbx")
//            .setIngredient('a', new ExactChoice(CustomMaterial.ENCHANTED_MITHRIL.create()))
//            .setIngredient('b', Material.STICK)
//    );
  }

  @NotNull
  private static NamespacedKey of(@NotNull String s)
  {
    NamespacedKey namespacedKey = NamespacedKey.fromString(s, Cucumbery.getPlugin());
    recipes.add(namespacedKey);
    if (namespacedKey == null)
    {
      throw new IllegalArgumentException("Invalid key!: " + s);
    }
    return namespacedKey;
  }

  private static void register(@NotNull Recipe recipe)
  {
    if (recipe instanceof Keyed keyed && Bukkit.getRecipe(keyed.getKey()) != null)
    {
      Bukkit.removeRecipe(keyed.getKey());
    }
    Bukkit.addRecipe(recipe);
  }

  public static void unload()
  {
    recipes.forEach(Bukkit::removeRecipe);
  }
}
