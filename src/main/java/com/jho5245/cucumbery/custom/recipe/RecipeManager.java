package com.jho5245.cucumbery.custom.recipe;

import com.jho5245.cucumbery.Cucumbery;
import com.jho5245.cucumbery.util.storage.data.CustomMaterial;
import org.bukkit.Bukkit;
import org.bukkit.Keyed;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.BlastingRecipe;
import org.bukkit.inventory.FurnaceRecipe;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.RecipeChoice.ExactChoice;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.Set;

public class RecipeManager
{
  public static Set<NamespacedKey> recipes = new HashSet<>();

  public static void registerRecipe()
  {
    register(new FurnaceRecipe(of("tungsten_ingot_from_furnace"), CustomMaterial.TUNGSTEN_INGOT.create(false), new ExactChoice(CustomMaterial.TUNGSTEN_ORE.create()), 0.7f, 200));
    register(new FurnaceRecipe(of("tungsten_ingot_from_furnace_2"), CustomMaterial.TUNGSTEN_INGOT.create(false), new ExactChoice(CustomMaterial.TUNGSTEN_ORE.create(false)), 0.7f, 200));
    register(new BlastingRecipe(of("tungsten_ingot_from_blast_furnace"), CustomMaterial.TUNGSTEN_INGOT.create(false), new ExactChoice(CustomMaterial.TUNGSTEN_ORE.create()), 0.7f, 100));
    register(new BlastingRecipe(of("tungsten_ingot_from_blast_furnace_2"), CustomMaterial.TUNGSTEN_INGOT.create(false), new ExactChoice(CustomMaterial.TUNGSTEN_ORE.create(false)), 0.7f, 100));

    register(new FurnaceRecipe(of("cobalt_ingot_from_furnace"), CustomMaterial.COBALT_INGOT.create(false), new ExactChoice(CustomMaterial.COBALT_ORE.create()), 1f, 400));
    register(new FurnaceRecipe(of("cobalt_ingot_from_furnace_2"), CustomMaterial.COBALT_INGOT.create(false), new ExactChoice(CustomMaterial.COBALT_ORE.create(false)), 1f, 400));
    register(new BlastingRecipe(of("cobalt_ingot_from_blast_furnace"), CustomMaterial.COBALT_INGOT.create(false), new ExactChoice(CustomMaterial.COBALT_ORE.create()), 1f, 200));
    register(new BlastingRecipe(of("cobalt_ingot_from_blast_furnace_2"), CustomMaterial.COBALT_INGOT.create(false), new ExactChoice(CustomMaterial.COBALT_ORE.create(false)), 1f, 200));
    
    register(new FurnaceRecipe(of("mithril_ingot_from_furnace"), CustomMaterial.MITHRIL_INGOT.create(false), new ExactChoice(CustomMaterial.MITHRIL_ORE.create()), 1f, 400));
    register(new FurnaceRecipe(of("mithril_ingot_from_furnace_2"), CustomMaterial.MITHRIL_INGOT.create(false), new ExactChoice(CustomMaterial.MITHRIL_ORE.create(false)), 1f, 400));
    register(new BlastingRecipe(of("mithril_ingot_from_blast_furnace"), CustomMaterial.MITHRIL_INGOT.create(false), new ExactChoice(CustomMaterial.MITHRIL_ORE.create()), 1f, 200));
    register(new BlastingRecipe(of("mithril_ingot_from_blast_furnace_2"), CustomMaterial.MITHRIL_INGOT.create(false), new ExactChoice(CustomMaterial.MITHRIL_ORE.create(false)), 1f, 200));

    register(new FurnaceRecipe(of("titanium_ingot_from_furnace"), CustomMaterial.TITANIUM_INGOT.create(false), new ExactChoice(CustomMaterial.TITANIUM_ORE.create()), 1f, 400));
    register(new FurnaceRecipe(of("titanium_ingot_from_furnace_2"), CustomMaterial.TITANIUM_INGOT.create(false), new ExactChoice(CustomMaterial.TITANIUM_ORE.create(false)), 1f, 400));
    register(new BlastingRecipe(of("titanium_ingot_from_blast_furnace"), CustomMaterial.TITANIUM_INGOT.create(false), new ExactChoice(CustomMaterial.TITANIUM_ORE.create()), 1f, 200));
    register(new BlastingRecipe(of("titanium_ingot_from_blast_furnace_2"), CustomMaterial.TITANIUM_INGOT.create(false), new ExactChoice(CustomMaterial.TITANIUM_ORE.create(false)), 1f, 200));

    register(new FurnaceRecipe(of("shroomite_ingot_from_furnace"), CustomMaterial.SHROOMITE_INGOT.create(false), new ExactChoice(CustomMaterial.SHROOMITE_ORE.create()), 1f, 400));
    register(new FurnaceRecipe(of("shroomite_ingot_from_furnace_2"), CustomMaterial.SHROOMITE_INGOT.create(false), new ExactChoice(CustomMaterial.SHROOMITE_ORE.create(false)), 1f, 400));
    register(new BlastingRecipe(of("shroomite_ingot_from_blast_furnace"), CustomMaterial.SHROOMITE_INGOT.create(false), new ExactChoice(CustomMaterial.SHROOMITE_ORE.create()), 1f, 200));
    register(new BlastingRecipe(of("shroomite_ingot_from_blast_furnace_2"), CustomMaterial.SHROOMITE_INGOT.create(false), new ExactChoice(CustomMaterial.SHROOMITE_ORE.create(false)), 1f, 200));

    register(new FurnaceRecipe(of("cucumberite_ingot_from_furnace"), CustomMaterial.CUCUMBERITE_INGOT.create(false), new ExactChoice(CustomMaterial.CUCUMBERITE_ORE.create()), 1f, 400));
    register(new FurnaceRecipe(of("cucumberite_ingot_from_furnace_2"), CustomMaterial.CUCUMBERITE_INGOT.create(false), new ExactChoice(CustomMaterial.CUCUMBERITE_ORE.create(false)), 1f, 400));
    register(new BlastingRecipe(of("cucumberite_ingot_from_blast_furnace"), CustomMaterial.CUCUMBERITE_INGOT.create(false), new ExactChoice(CustomMaterial.CUCUMBERITE_ORE.create()), 1f, 200));
    register(new BlastingRecipe(of("cucumberite_ingot_from_blast_furnace_2"), CustomMaterial.CUCUMBERITE_INGOT.create(false), new ExactChoice(CustomMaterial.CUCUMBERITE_ORE.create(false)), 1f, 200));
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
