package com.jho5245.cucumbery.util.itemlore;

import com.jho5245.cucumbery.util.nbt.CucumberyTag;
import com.jho5245.cucumbery.util.nbt.NBTAPI;
import com.jho5245.cucumbery.util.no_groups.MessageUtil;
import com.jho5245.cucumbery.util.storage.component.util.ComponentUtil;
import com.jho5245.cucumbery.util.storage.data.Constant.ExtraTag;
import com.jho5245.cucumbery.util.storage.data.Constant.RestrictionType;
import com.jho5245.cucumbery.util.storage.data.CustomMaterial;
import com.jho5245.cucumbery.util.storage.data.Variable;
import com.jho5245.cucumbery.util.storage.data.custom_enchant.CustomEnchant;
import com.jho5245.cucumbery.util.storage.no_groups.ItemStackUtil;
import com.jho5245.cucumbery.util.storage.no_groups.RecipeChecker;
import de.tr7zw.changeme.nbtapi.*;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.attribute.AttributeModifier.Operation;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.UUID;

public class ItemLoreCustomItem
{
  protected static void itemLore(@NotNull ItemStack itemStack, @NotNull NBTItem nbtItem, @NotNull CustomMaterial customMaterial)
  {
    Material displayMaterial = customMaterial.getDisplayMaterial();
    {
      NBTCompound itemTag = nbtItem.getCompound(CucumberyTag.KEY_MAIN);
      if (itemTag == null)
      {
        itemTag = nbtItem.addCompound(CucumberyTag.KEY_MAIN);
      }
      NBTCompoundList restrictionTag = itemTag.getCompoundList(CucumberyTag.ITEM_USAGE_RESTRICTIONS_KEY);
      NBTList<String> extraTags = itemTag.getStringList(CucumberyTag.EXTRA_TAGS_KEY);
      NBTCompound nbtCompound = new NBTContainer();
      if (RecipeChecker.hasCraftingRecipe(displayMaterial))
      {
        if (!NBTAPI.isRestricted(itemStack, RestrictionType.NO_CRAFT))
        {
          nbtCompound.setString(CucumberyTag.VALUE_KEY, RestrictionType.NO_CRAFT.toString());
          nbtCompound.setBoolean(CucumberyTag.ITEM_USAGE_RESTRICTIONS_HIDE_FROM_LORE_KEY, true);
          restrictionTag.addCompound(nbtCompound);
        }
      }
      if (RecipeChecker.hasSmeltingRecipe(displayMaterial))
      {
        if (!NBTAPI.isRestricted(itemStack, RestrictionType.NO_SMELT))
        {
          nbtCompound.setString(CucumberyTag.VALUE_KEY, RestrictionType.NO_SMELT.toString());
          nbtCompound.setBoolean(CucumberyTag.ITEM_USAGE_RESTRICTIONS_HIDE_FROM_LORE_KEY, true);
          restrictionTag.addCompound(nbtCompound);
        }
      }
      if (ItemStackUtil.isPlacable(displayMaterial))
      {
        switch (customMaterial)
        {
          case DOEHAERIM_BABO, BAMIL_PABO -> {
            if (!NBTAPI.arrayContainsValue(extraTags, ExtraTag.PRESERVE_BLOCK_NBT))
            {
              extraTags.add(ExtraTag.PRESERVE_BLOCK_NBT.toString());
            }
          }
          default -> {
            if (!NBTAPI.isRestricted(itemStack, RestrictionType.NO_PLACE))
            {
              nbtCompound.setString(CucumberyTag.VALUE_KEY, RestrictionType.NO_PLACE.toString());
              nbtCompound.setBoolean(CucumberyTag.ITEM_USAGE_RESTRICTIONS_HIDE_FROM_LORE_KEY, true);
              restrictionTag.addCompound(nbtCompound);
            }
          }
        }
      }
      if (ItemStackUtil.isBrewable(displayMaterial))
      {
        if (!NBTAPI.isRestricted(itemStack, RestrictionType.NO_BREW))
        {
          nbtCompound.setString(CucumberyTag.VALUE_KEY, RestrictionType.NO_BREW.toString());
          nbtCompound.setBoolean(CucumberyTag.ITEM_USAGE_RESTRICTIONS_HIDE_FROM_LORE_KEY, true);
          restrictionTag.addCompound(nbtCompound);
        }
      }
      if (ItemStackUtil.isEdible(displayMaterial))
      {
        switch (customMaterial)
        {
          case BAD_APPLE -> {
            NBTCompound foodTag = itemTag.addCompound(CucumberyTag.FOOD_KEY);
            foodTag.setInteger(CucumberyTag.FOOD_LEVEL_KEY, -20);
            foodTag.setDouble(CucumberyTag.SATURATION_KEY, -20d);
          }
          default -> {
            if (!NBTAPI.isRestricted(itemStack, RestrictionType.NO_CONSUME))
            {
              nbtCompound.setString(CucumberyTag.VALUE_KEY, RestrictionType.NO_CONSUME.toString());
              nbtCompound.setBoolean(CucumberyTag.ITEM_USAGE_RESTRICTIONS_HIDE_FROM_LORE_KEY, true);
              restrictionTag.addCompound(nbtCompound);
            }
          }
        }
      }
      if (ItemStackUtil.getFuelTimeInSecond(displayMaterial) > 0)
      {
        switch (customMaterial)
        {
          case ENCHANTED_COAL ->
          {
            nbtItem.setInteger("BurnTime", 20 * 60 * 20);
            nbtItem.setDouble("CookSpeed", 3d);
          }
          case ENCHANTED_COAL_BLOCK ->
          {
            nbtItem.setInteger("BurnTime", 20 * 60 * 25);
            nbtItem.setDouble("CookSpeed", 15d);
          }
          default -> {
            if (!NBTAPI.isRestricted(itemStack, RestrictionType.NO_FUEL))
            {
              nbtCompound.setString(CucumberyTag.VALUE_KEY, RestrictionType.NO_FUEL.toString());
              nbtCompound.setBoolean(CucumberyTag.ITEM_USAGE_RESTRICTIONS_HIDE_FROM_LORE_KEY, true);
              restrictionTag.addCompound(nbtCompound);
            }
          }
        }

      }
      if (ItemStackUtil.getCompostChance(displayMaterial) > 0)
      {
        if (!NBTAPI.isRestricted(itemStack, RestrictionType.NO_COMPOSTER))
        {
          nbtCompound.setString(CucumberyTag.VALUE_KEY, RestrictionType.NO_COMPOSTER.toString());
          nbtCompound.setBoolean(CucumberyTag.ITEM_USAGE_RESTRICTIONS_HIDE_FROM_LORE_KEY, true);
          restrictionTag.addCompound(nbtCompound);
        }
      }
      switch (customMaterial)
      {
        case TODWOT_PICKAXE -> {
          if (!NBTAPI.isRestricted(itemStack, RestrictionType.NO_ANVIL_ENCHANTED_BOOK_ENCHANT))
          {
            nbtCompound.setString(CucumberyTag.VALUE_KEY, RestrictionType.NO_ANVIL_ENCHANTED_BOOK_ENCHANT.toString());
            nbtCompound.setBoolean(CucumberyTag.ITEM_USAGE_RESTRICTIONS_HIDE_FROM_LORE_KEY, false);
            restrictionTag.addCompound(nbtCompound);
          }
        }
      }
    }
    switch (customMaterial)
    {
      case TEST_PICKAXE -> {
        nbtItem.setFloat("ToolSpeed", 18000f);
        nbtItem.setInteger("ToolTier", 10);
        NBTCompound duraTag = nbtItem.addCompound(CucumberyTag.KEY_MAIN).addCompound(CucumberyTag.CUSTOM_DURABILITY_KEY);
        duraTag.setLong(CucumberyTag.CUSTOM_DURABILITY_MAX_KEY, 12345678L);
        if (!duraTag.hasKey(CucumberyTag.CUSTOM_DURABILITY_CURRENT_KEY))
        {
          duraTag.setLong(CucumberyTag.CUSTOM_DURABILITY_CURRENT_KEY, 12345678L);
        }
      }
      case TODWOT_PICKAXE -> {
        nbtItem.setFloat("ToolSpeed", 1f);
        nbtItem.setInteger("ToolTier", 1);
        NBTCompound duraTag = nbtItem.addCompound(CucumberyTag.KEY_MAIN).addCompound(CucumberyTag.CUSTOM_DURABILITY_KEY);
        duraTag.setLong(CucumberyTag.CUSTOM_DURABILITY_MAX_KEY, 1000L);
        if (!duraTag.hasKey(CucumberyTag.CUSTOM_DURABILITY_CURRENT_KEY))
        {
          duraTag.setLong(CucumberyTag.CUSTOM_DURABILITY_CURRENT_KEY, 1000L);
        }
      }
      case MITHRIL_PICKAXE -> {
        nbtItem.setFloat("ToolSpeed", 550f);
        nbtItem.setInteger("ToolTier", 5);
        NBTCompound duraTag = nbtItem.addCompound(CucumberyTag.KEY_MAIN).addCompound(CucumberyTag.CUSTOM_DURABILITY_KEY);
        duraTag.setLong(CucumberyTag.CUSTOM_DURABILITY_MAX_KEY, 2000L);
        if (!duraTag.hasKey(CucumberyTag.CUSTOM_DURABILITY_CURRENT_KEY))
        {
          duraTag.setLong(CucumberyTag.CUSTOM_DURABILITY_CURRENT_KEY, 2000L);
        }
      }
      case REFINED_MITHRIL_PICKAXE -> {
        nbtItem.setFloat("ToolSpeed", 600f);
        nbtItem.setInteger("ToolTier", 5);
        NBTCompound duraTag = nbtItem.addCompound(CucumberyTag.KEY_MAIN).addCompound(CucumberyTag.CUSTOM_DURABILITY_KEY);
        duraTag.setLong(CucumberyTag.CUSTOM_DURABILITY_MAX_KEY, 2200L);
        duraTag.setDouble(CucumberyTag.CUSTOM_DURABILITY_CHANCE_NOT_TO_CONSUME_DURABILITY, 5d);
        if (!duraTag.hasKey(CucumberyTag.CUSTOM_DURABILITY_CURRENT_KEY))
        {
          duraTag.setLong(CucumberyTag.CUSTOM_DURABILITY_CURRENT_KEY, 2200L);
        }
      }
      case SANS_HELMET, SANS_CHESTPLATE, SANS_LEGGINGS, SANS_BOOTS -> {
        NBTCompound duraTag = nbtItem.addCompound(CucumberyTag.KEY_MAIN).addCompound(CucumberyTag.CUSTOM_DURABILITY_KEY);
        duraTag.setLong(CucumberyTag.CUSTOM_DURABILITY_MAX_KEY, 500L);
        if (!duraTag.hasKey(CucumberyTag.CUSTOM_DURABILITY_CURRENT_KEY))
        {
          duraTag.setLong(CucumberyTag.CUSTOM_DURABILITY_CURRENT_KEY, 500L);
        }
      }
      case TITANIUM_PICKAXE -> {
        nbtItem.setFloat("ToolSpeed", 750f);
        nbtItem.setInteger("ToolTier", 6);
        NBTCompound duraTag = nbtItem.addCompound(CucumberyTag.KEY_MAIN).addCompound(CucumberyTag.CUSTOM_DURABILITY_KEY);
        duraTag.setLong(CucumberyTag.CUSTOM_DURABILITY_MAX_KEY, 2500L);
        duraTag.setDouble(CucumberyTag.CUSTOM_DURABILITY_CHANCE_NOT_TO_CONSUME_DURABILITY, 7.5d);
        if (!duraTag.hasKey(CucumberyTag.CUSTOM_DURABILITY_CURRENT_KEY))
        {
          duraTag.setLong(CucumberyTag.CUSTOM_DURABILITY_CURRENT_KEY, 2500L);
        }
      }
      case REFINED_TITANIUM_PICKAXE -> {
        nbtItem.setFloat("ToolSpeed", 800f);
        nbtItem.setInteger("ToolTier", 6);
        NBTCompound duraTag = nbtItem.addCompound(CucumberyTag.KEY_MAIN).addCompound(CucumberyTag.CUSTOM_DURABILITY_KEY);
        duraTag.setLong(CucumberyTag.CUSTOM_DURABILITY_MAX_KEY, 2800L);
        duraTag.setDouble(CucumberyTag.CUSTOM_DURABILITY_CHANCE_NOT_TO_CONSUME_DURABILITY, 10d);
        if (!duraTag.hasKey(CucumberyTag.CUSTOM_DURABILITY_CURRENT_KEY))
        {
          duraTag.setLong(CucumberyTag.CUSTOM_DURABILITY_CURRENT_KEY, 2800L);
        }
      }
      case TITANIUM_DRILL_R266 -> {
        nbtItem.setFloat("ToolSpeed", 1000f);
        nbtItem.setInteger("ToolTier", 6);
        if (!nbtItem.hasKey("uuid"))
        {
          nbtItem.setString("uuid", UUID.randomUUID().toString());
        }
      }
      case TITANIUM_DRILL_R366 -> {
        nbtItem.setFloat("ToolSpeed", 1200f);
        nbtItem.setInteger("ToolTier", 7);
        if (!nbtItem.hasKey("uuid"))
        {
          nbtItem.setString("uuid", UUID.randomUUID().toString());
        }
      }
      case TITANIUM_DRILL_R466 -> {
        nbtItem.setFloat("ToolSpeed", 1500f);
        nbtItem.setInteger("ToolTier", 8);
        if (!nbtItem.hasKey("uuid"))
        {
          nbtItem.setString("uuid", UUID.randomUUID().toString());
        }
      }
      case TITANIUM_DRILL_R566 -> {
        nbtItem.setFloat("ToolSpeed", 1800f);
        nbtItem.setInteger("ToolTier", 9);
        if (!nbtItem.hasKey("uuid"))
        {
          nbtItem.setString("uuid", UUID.randomUUID().toString());
        }
      }
      case IQ_CHOOK_CHUCK -> {
        if (!nbtItem.hasKey("IQ"))
        {
          nbtItem.setInteger("IQ", 70);
        }
        if (Math.random() > 0.2)
        {
          nbtItem.setInteger("IQ", nbtItem.getInteger("IQ") + 1);
        }
        else
        {
          nbtItem.setInteger("IQ", nbtItem.getInteger("IQ") - 1);
        }
        if (!nbtItem.hasKey("uuid"))
        {
          nbtItem.setString("uuid", UUID.randomUUID().toString());
        }
      }
    }
    itemStack.setType(displayMaterial);
    ItemMeta itemMeta = itemStack.getItemMeta();
    itemMeta.displayName(customMaterial.getDisplayName());
    switch (customMaterial)
    {
      case STONK -> {
        itemMeta.setCustomModelData(25201);
        if (!itemMeta.hasEnchant(Enchantment.DIG_SPEED))
        {
          itemMeta.addEnchant(Enchantment.DIG_SPEED, 6, true);
        }
        itemMeta.setUnbreakable(true);
      }
      case TITANIUM_DRILL_R266, TITANIUM_DRILL_R366, TITANIUM_DRILL_R466, TITANIUM_DRILL_R566, CORE_GEMSTONE, CUTE_SUGAR, BOO -> {
        itemMeta.setCustomModelData(25201);
      }
      case BOO_HUNGRY, CUTE_SUGAR_HUNGRY -> {
        itemMeta.setCustomModelData(25202);
      }
      case TODWOT_PICKAXE -> {
        itemMeta.setCustomModelData(25201);
        if (!itemMeta.hasEnchant(Enchantment.LOOT_BONUS_BLOCKS))
        {
          itemMeta.addEnchant(Enchantment.LOOT_BONUS_BLOCKS, 1, true);
        }
      }
      case SANS_HELMET -> {
        if (!itemMeta.hasAttributeModifiers())
        {
          itemMeta.addAttributeModifier(Attribute.GENERIC_ARMOR, new AttributeModifier(UUID.randomUUID(), customMaterial.toString(), 2, Operation.ADD_NUMBER, EquipmentSlot.HEAD));
        }
      }
      case SANS_CHESTPLATE -> {
        if (!itemMeta.hasAttributeModifiers())
        {
          itemMeta.addAttributeModifier(Attribute.GENERIC_ARMOR, new AttributeModifier(UUID.randomUUID(), customMaterial.toString(), 2, Operation.ADD_NUMBER, EquipmentSlot.CHEST));
        }
      }
      case SANS_LEGGINGS -> {
        if (!itemMeta.hasAttributeModifiers())
        {
          itemMeta.addAttributeModifier(Attribute.GENERIC_ARMOR, new AttributeModifier(UUID.randomUUID(), customMaterial.toString(), 2, Operation.ADD_NUMBER, EquipmentSlot.LEGS));
        }
      }
      case SANS_BOOTS -> {
        if (!itemMeta.hasAttributeModifiers())
        {
          itemMeta.addAttributeModifier(Attribute.GENERIC_ARMOR, new AttributeModifier(UUID.randomUUID(), customMaterial.toString(), 2, Operation.ADD_NUMBER, EquipmentSlot.FEET));
          itemMeta.addAttributeModifier(Attribute.GENERIC_MOVEMENT_SPEED, new AttributeModifier(UUID.randomUUID(), customMaterial.toString(), 0.3, Operation.ADD_SCALAR, EquipmentSlot.FEET));
        }
      }
      case CORE_GEMSTONE_EXPERIENCE -> itemMeta.setCustomModelData(25202);
      case CORE_GEMSTONE_MIRROR -> itemMeta.setCustomModelData(25203);
      case CORE_GEMSTONE_MITRA -> itemMeta.setCustomModelData(25204);
    }
    if (itemMeta instanceof SkullMeta skullMeta)
    {
      switch (customMaterial)
      {
        case ENCHANTED_TITANIUM -> ItemStackUtil.setTexture(skullMeta, "https://textures.minecraft.net/texture/3dcc0ec9873f4f8d407ba0a0f983e257787772eaf8784e226a61c7f727ac9e26");
        case REFINED_MITHRIL -> ItemStackUtil.setTexture(skullMeta, "https://textures.minecraft.net/texture/35a7663300619bb6a156d76351ac05f7b3cafeac31e2ff04c55cc9f236327832");
        case REFINED_TITANIUM -> ItemStackUtil.setTexture(skullMeta, "https://textures.minecraft.net/texture/704baabf7ef854825aae1992e4a75ff7286ed1654d8f1a08952e7b8669cf692d");
        case TITANIUM_ORE -> ItemStackUtil.setTexture(skullMeta, "https://textures.minecraft.net/texture/13fa5265a336abde301a9d59af4783e82a10dad0817716ead2962ab7c6d3dff");
        case RUBY -> ItemStackUtil.setTexture(skullMeta, "https://textures.minecraft.net/texture/d159b03243be18a14f3eae763c4565c78f1f339a8742d26fde541be59b7de07");
        case AMBER -> ItemStackUtil.setTexture(skullMeta, "https://textures.minecraft.net/texture/da19436f6151a7b66d65ed7624add4325cfbbc2eee815fcf76f4c29ddf08f75b");
        case AMETHYST -> ItemStackUtil.setTexture(skullMeta, "https://textures.minecraft.net/texture/e493c6f540c7001fed97b07f6b4c89128e3a7c37563aa223f0acca314f175515");
        case JADE -> ItemStackUtil.setTexture(skullMeta, "https://textures.minecraft.net/texture/3b4c2afd544d0a6139e6ae8ef8f0bfc09a9fd837d0cad4f5cd0fe7f607b7d1a0");
        case SAPPHIRE -> ItemStackUtil.setTexture(skullMeta, "https://textures.minecraft.net/texture/cfcebe54dbc345ea7e22206f703e6b33befbe95b6a918bd1754b76188bc65bb5");
        case TOPAZ -> ItemStackUtil.setTexture(skullMeta, "https://textures.minecraft.net/texture/3fd960722ec29c66716ae5ca97b9b6b2628984e1d6f9d2592cd089914206a1b");
        case JASPER -> ItemStackUtil.setTexture(skullMeta, "https://textures.minecraft.net/texture/23d064ec150172d05844c11a18619c1421bbfb2ddd1dbb87cdc10e22252b773b");
        case PORTABLE_CRAFTING_TABLE -> ItemStackUtil.setTexture(skullMeta, "https://textures.minecraft.net/texture/4c36045208f9b5ddcf8c4433e424b1ca17b94f6b96202fb1e5270ee8d53881b1");
      }
    }
    if (customMaterial.isGlow())
    {
      itemMeta.addEnchant(CustomEnchant.GLOW, 1, true);
    }
    itemStack.setItemMeta(itemMeta);
    if (itemMeta instanceof SkullMeta skullMeta && !skullMeta.hasOwner())
    {
      switch (customMaterial)
      {
        case DOEHAERIM_BABO -> skullMeta.setOwningPlayer(Bukkit.getOfflinePlayer("Ai_vo"));
        case BAMIL_PABO -> skullMeta.setOwningPlayer(Bukkit.getOfflinePlayer("Pamille_27"));
      }
      itemStack.setItemMeta(skullMeta);
    }
    if (itemMeta instanceof LeatherArmorMeta leatherArmorMeta)
    {
      leatherArmorMeta.addItemFlags(ItemFlag.HIDE_DYE);
      switch (customMaterial)
      {
        case SANS_BOOTS, SANS_CHESTPLATE, SANS_HELMET, SANS_LEGGINGS -> {
          leatherArmorMeta.setColor(Color.fromRGB(240, 240, 240));
        }
      }
      itemStack.setItemMeta(leatherArmorMeta);
    }
  }

  protected static void itemLore(@NotNull ItemStack itemStack, @NotNull NBTItem nbtItem, @NotNull String id)
  {
    ConfigurationSection section = Variable.customItemsConfig.getConfigurationSection(id);
    if (section != null)
    {
      List<String> restrictions = section.getStringList("restrictions");
      NBTCompound itemTag = nbtItem.getCompound(CucumberyTag.KEY_MAIN);
      for (String r : restrictions)
      {
        if (itemTag == null)
        {
          itemTag = nbtItem.addCompound(CucumberyTag.KEY_MAIN);
        }
        NBTCompoundList restrictionsTag = itemTag.getCompoundList(CucumberyTag.ITEM_USAGE_RESTRICTIONS_KEY);
        try
        {
          String[] split = r.split("\\|");
          RestrictionType restrictionType = RestrictionType.valueOf(split[0]);
          if (!NBTAPI.isRestricted(itemStack, restrictionType))
          {
            boolean hideFromLore = Boolean.parseBoolean(split[1]);
            NBTCompound nbtCompound = new NBTContainer();
            nbtCompound.setString(CucumberyTag.VALUE_KEY, restrictionType.toString());
            nbtCompound.setBoolean(CucumberyTag.ITEM_USAGE_RESTRICTIONS_HIDE_FROM_LORE_KEY, hideFromLore);
            restrictionsTag.addCompound(nbtCompound);
          }
        }
        catch (Exception ignored)
        {

        }
      }
      String typeString = section.getString("type");
      try
      {
        if (typeString != null)
        {
          itemStack.setType(Material.valueOf(typeString.toUpperCase()));
        }
      }
      catch (Exception ignored)
      {
        return;
      }
      ItemMeta itemMeta = itemStack.getItemMeta();
      String displayName = section.getString("display-name");
      if (displayName == null)
      {
        throw new NullPointerException("display name cannot be null! CustomItem.yml - " + id);
      }
      itemMeta.displayName(ComponentUtil.create(MessageUtil.n2s(displayName)));
      List<String> enchants = section.getStringList("enchants");
      enchants.forEach(s ->
      {
        try
        {
          String[] split = s.split("\\|");
          Enchantment enchantment = Enchantment.getByKey(NamespacedKey.minecraft(split[0]));
          int level = Integer.parseInt(split[1]);
          if (enchantment != null)
          {
            itemMeta.addEnchant(enchantment, level, true);
          }
        }
        catch (Exception ignored)
        {
        }
      });
      String skull = section.getString("skull");
      if (skull != null && itemMeta instanceof SkullMeta skullMeta)
      {
        ItemStackUtil.setTexture(skullMeta, skull);
      }
      itemStack.setItemMeta(itemMeta);
      String nbt = section.getString("nbt");
      if (nbt != null)
      {
        try
        {
          NBTContainer nbtContainer = new NBTContainer("{" + nbt + "}");
          new NBTItem(itemStack, true).mergeCompound(nbtContainer);
        }
        catch (Exception ignored)
        {

        }
      }
    }
  }
}
