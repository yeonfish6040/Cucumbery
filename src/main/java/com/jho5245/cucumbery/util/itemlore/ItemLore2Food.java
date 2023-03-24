package com.jho5245.cucumbery.util.itemlore;

import com.jho5245.cucumbery.util.nbt.CucumberyTag;
import com.jho5245.cucumbery.util.nbt.NBTAPI;
import com.jho5245.cucumbery.util.storage.component.util.ComponentUtil;
import com.jho5245.cucumbery.util.storage.data.Constant;
import com.jho5245.cucumbery.util.storage.data.Constant.RestrictionType;
import com.jho5245.cucumbery.util.storage.no_groups.ItemStackUtil;
import de.tr7zw.changeme.nbtapi.NBTCompound;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ItemLore2Food
{
  protected static void setItemLore(@NotNull ItemStack item, @NotNull Material type, @NotNull List<Component> lore, @Nullable NBTCompound foodTag, @Nullable Player viewer, boolean hideStatusEffects)
  {
    List<Component> foodLore = new ArrayList<>();

    if (!hideStatusEffects && (!NBTAPI.isRestricted(item, RestrictionType.NO_CONSUME) || NBTAPI.getRestrictionOverridePermission(item, RestrictionType.NO_CONSUME) != null)
            && (foodTag == null || !foodTag.hasTag(CucumberyTag.FOOD_DISABLE_STATUS_EFFECT_KEY) || !foodTag.getBoolean(CucumberyTag.FOOD_DISABLE_STATUS_EFFECT_KEY)))
    {
      switch (type)
      {
        case GOLDEN_APPLE -> foodLore.addAll(Arrays.asList(
                ItemLorePotionDescription.getDescription(ItemLorePotionDescription.REGENERATION, 5 * 20, 2),
                ItemLorePotionDescription.getDescription(ItemLorePotionDescription.ABSORPTION, 2 * 60 * 20, 1)));
        case ENCHANTED_GOLDEN_APPLE -> foodLore.addAll(Arrays.asList(
                ItemLorePotionDescription.getDescription(ItemLorePotionDescription.REGENERATION, 20 * 20, 2),
                ItemLorePotionDescription.getDescription(ItemLorePotionDescription.ABSORPTION, 2 * 60 * 20, 4),
                ItemLorePotionDescription.getDescription(ItemLorePotionDescription.RESISTANCE, 5 * 60 * 20, 1),
                ItemLorePotionDescription.getDescription(ItemLorePotionDescription.FIRE_RESISTANCE, 5 * 60 * 20, 1)));
        case POISONOUS_POTATO -> foodLore.add(
                ItemLorePotionDescription.getDescription(60d, ItemLorePotionDescription.POISON, 4 * 20, 1));
        case SPIDER_EYE -> foodLore.add(
                ItemLorePotionDescription.getDescription(100d, ItemLorePotionDescription.POISON, 4 * 20, 1));
        case PUFFERFISH -> foodLore.addAll(Arrays.asList(
                ItemLorePotionDescription.getDescription(100d, ItemLorePotionDescription.HUNGER, 15 * 20, 3),
                ItemLorePotionDescription.getDescription(100d, ItemLorePotionDescription.NAUSEA, 15 * 20, 2),
                ItemLorePotionDescription.getDescription(100d, ItemLorePotionDescription.POISON, 60 * 20, 4)));
        case ROTTEN_FLESH -> foodLore.add(
                ItemLorePotionDescription.getDescription(80d, ItemLorePotionDescription.HUNGER, 30 * 20, 1));
        case CHICKEN -> foodLore.add(
                ItemLorePotionDescription.getDescription(30d, ItemLorePotionDescription.HUNGER, 30 * 20, 1));
        case HONEY_BOTTLE -> foodLore.add(
                ComponentUtil.translate(ItemLorePotionDescription.POTION_DESCRIPTION_COLOR + "%s 효과 제거", ItemLorePotionDescription.getComponent(PotionEffectType.POISON)));
        case MILK_BUCKET -> foodLore.add(
                ComponentUtil.translate(ItemLorePotionDescription.POTION_DESCRIPTION_COLOR + "모든 효과 제거 (일부 효과 제외)"));
      }
    }
    if (ItemStackUtil.isEdible(type) && type != Material.POTION && type != Material.SUSPICIOUS_STEW)
    {
      foodLore.addAll(ItemLorePotionDescription.getCustomEffectList(viewer, item));
    }
    if (!foodLore.isEmpty())
    {
      lore.addAll(Arrays.asList(Component.empty(), ComponentUtil.translate(Constant.ITEM_LORE_STATUS_EFFECT)));
      lore.addAll(foodLore);
    }
  }
}
