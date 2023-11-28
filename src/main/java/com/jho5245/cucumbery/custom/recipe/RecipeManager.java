package com.jho5245.cucumbery.custom.recipe;

import com.jho5245.cucumbery.Cucumbery;
import com.jho5245.cucumbery.util.storage.data.CustomMaterial;
import io.papermc.paper.potion.PotionMix;
import org.bukkit.Bukkit;
import org.bukkit.Keyed;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.*;
import org.bukkit.inventory.RecipeChoice.ExactChoice;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionType;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.Set;

public class RecipeManager
{
  public static Set<NamespacedKey> recipes = new HashSet<>();

  public static void registerRecipe()
  {
    register(new FurnaceRecipe(of("tungsten_ingot_from_furnace"), CustomMaterial.TUNGSTEN_INGOT.create(false), new ExactChoice(CustomMaterial.TUNGSTEN_ORE.create(false)), 0.7f, 200));
    register(new BlastingRecipe(of("tungsten_ingot_from_blast_furnace"), CustomMaterial.TUNGSTEN_INGOT.create(false), new ExactChoice(CustomMaterial.TUNGSTEN_ORE.create(false)), 0.7f, 100));

    register(new FurnaceRecipe(of("cobalt_ingot_from_furnace"), CustomMaterial.COBALT_INGOT.create(false), new ExactChoice(CustomMaterial.COBALT_ORE.create(false)), 1f, 400));
    register(new BlastingRecipe(of("cobalt_ingot_from_blast_furnace"), CustomMaterial.COBALT_INGOT.create(false), new ExactChoice(CustomMaterial.COBALT_ORE.create(false)), 1f, 200));

    register(new FurnaceRecipe(of("titanium_ingot_from_furnace"), CustomMaterial.TITANIUM_INGOT.create(false), new ExactChoice(CustomMaterial.TITANIUM_ORE.create(false)), 1f, 400));
    register(new BlastingRecipe(of("titanium_ingot_from_blast_furnace"), CustomMaterial.TITANIUM_INGOT.create(false), new ExactChoice(CustomMaterial.TITANIUM_ORE.create(false)), 1f, 200));

    register(new FurnaceRecipe(of("shroomite_ingot_from_furnace"), CustomMaterial.SHROOMITE_INGOT.create(false), new ExactChoice(CustomMaterial.SHROOMITE_ORE.create(false)), 1f, 400));
    register(new BlastingRecipe(of("shroomite_ingot_from_blast_furnace"), CustomMaterial.SHROOMITE_INGOT.create(false), new ExactChoice(CustomMaterial.SHROOMITE_ORE.create(false)), 1f, 200));

    register(new FurnaceRecipe(of("cucumberite_ingot_from_furnace"), CustomMaterial.CUCUMBERITE_INGOT.create(false), new ExactChoice(CustomMaterial.CUCUMBERITE_ORE.create(false)), 1f, 400));
    register(new BlastingRecipe(of("cucumberite_ingot_from_blast_furnace"), CustomMaterial.CUCUMBERITE_INGOT.create(false), new ExactChoice(CustomMaterial.CUCUMBERITE_ORE.create(false)), 1f, 200));

    register(new FurnaceRecipe(of("tnt_from_furnace"), new ItemStack(Material.TNT), new ExactChoice(CustomMaterial.WNYNYA_ORE.create(false)), 2f, 100));
    register(new BlastingRecipe(of("tnt_from_blast_furnace"), new ItemStack(Material.TNT), new ExactChoice(CustomMaterial.WNYNYA_ORE.create(false)), 2f, 50));

    register(new PotionMix(of("test_jade_to_tnt"), new ItemStack(Material.TNT), INPUT_WATER_BOTTLE,
        PotionMix.createPredicateChoice(itemStack -> isAdminOnline() && CustomMaterial.itemStackOf(itemStack) == CustomMaterial.JADE)));

    register(new PotionMix(of("test_enchanted_jade_to_diamond"), new ItemStack(Material.STONE), INPUT_WATER_BOTTLE,
        PotionMix.createPredicateChoice(itemStack -> isAdminOnline() && CustomMaterial.itemStackOf(itemStack) == CustomMaterial.JADE && itemStack.hasItemMeta() && itemStack.getItemMeta().hasEnchants())));

    register(new PotionMix(of("test_enchanted_ruby_to_stone"), new ItemStack(Material.STONE), INPUT_WATER_BOTTLE,
        PotionMix.createPredicateChoice(itemStack -> isAdminOnline() && CustomMaterial.itemStackOf(itemStack) == CustomMaterial.RUBY && itemStack.hasItemMeta() && itemStack.getItemMeta().hasEnchants())));

    register(new PotionMix(of("test_amber_to_tnt"), new ItemStack(Material.TNT), INPUT_WATER_BOTTLE,
        PotionMix.createPredicateChoice(itemStack -> isAdminOnline() && CustomMaterial.itemStackOf(itemStack) == CustomMaterial.AMBER && Bukkit.getOnlinePlayers().size() > 1)));
  }


  private static boolean isAdminOnline()
  {
    return Bukkit.getPlayer("jho5245") != null;
  }

  private static final RecipeChoice INPUT_WATER_BOTTLE = PotionMix.createPredicateChoice(itemStack ->
  {
    ItemMeta itemMeta = itemStack.getItemMeta();
    return itemMeta instanceof PotionMeta potionMeta && potionMeta.getBasePotionData().getType() == PotionType.WATER && potionMeta.getCustomEffects().isEmpty();
  });

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

  private static void register(@NotNull PotionMix potionMix)
  {
    NamespacedKey namespacedKey = potionMix.getKey();
    Bukkit.getPotionBrewer().removePotionMix(namespacedKey);
    Bukkit.getPotionBrewer().addPotionMix(potionMix);
  }

  public static void unload()
  {
    recipes.forEach(Bukkit::removeRecipe);
  }
}
