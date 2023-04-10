package com.jho5245.cucumbery.util.itemlore;

import com.google.common.collect.Multimap;
import com.jho5245.cucumbery.Cucumbery;
import com.jho5245.cucumbery.util.storage.component.util.ComponentUtil;
import com.jho5245.cucumbery.util.storage.data.Constant;
import com.jho5245.cucumbery.util.storage.data.CustomMaterial;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ItemLore2Attribute
{
  protected static void setItemLore(@NotNull Material type, @NotNull ItemStack itemStack, @NotNull ItemMeta itemMeta, @NotNull List<Component> originalLore, boolean hideAttributes)
  {
    List<Component> lore = new ArrayList<>();
    if (!Constant.DEFAULT_MODIFIER_ITEMS.contains(type) && !itemMeta.hasAttributeModifiers())
    {
      itemMeta.removeItemFlags(ItemFlag.HIDE_ATTRIBUTES);
    }
    else
    {
      itemMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
      if (!hideAttributes)
      {
        if (!Constant.DEFAULT_MODIFIER_ITEMS.contains(type) || (Constant.DEFAULT_MODIFIER_ITEMS.contains(type) && itemMeta.hasAttributeModifiers()))
        {
          for (EquipmentSlot slot : EquipmentSlot.values())
          {
            Multimap<Attribute, AttributeModifier> attrs = itemMeta.getAttributeModifiers(slot);
            if (attrs.isEmpty())
            {
              continue;
            }
            switch (slot)
            {
              case HAND -> lore.add(Constant.ITEM_MODIFIERS_MAINHAND);
              case OFF_HAND -> lore.add(Constant.ITEM_MODIFIERS_OFFHAND);
              case FEET -> lore.add(Constant.ITEM_MODIFIERS_FEET);
              case LEGS -> lore.add(Constant.ITEM_MODIFIERS_LEGS);
              case CHEST -> lore.add(Constant.ITEM_MODIFIERS_CHEST);
              case HEAD -> lore.add(Constant.ITEM_MODIFIERS_HEAD);
            }
            for (Attribute attribute : Attribute.values())
            {
              Collection<AttributeModifier> attr = attrs.get(attribute);
              for (AttributeModifier modifier : attr)
              {
                double amount = modifier.getAmount();
                if (amount == 0d)
                {
                  continue;
                }
                AttributeModifier.Operation operation = modifier.getOperation();
                if (operation != AttributeModifier.Operation.ADD_NUMBER)
                {
                  amount *= 100d;
                }
                String operationString = ItemLoreUtil.operationValue(operation);
                Component component = ComponentUtil.translate("rgb255,142,82;%s : %s",
                        ComponentUtil.translate(attribute.translationKey()), (amount > 0 ? "+" : "") + Constant.Sosu2.format(amount) + operationString);
                lore.add(component);
              }
            }
          }
        }
        else if (!itemMeta.hasAttributeModifiers())
        {
          Component mainHand = Constant.ITEM_MODIFIERS_MAINHAND;
          Component helmet = Constant.ITEM_MODIFIERS_HEAD;
          Component chestplate = Constant.ITEM_MODIFIERS_CHEST;
          Component leggings = Constant.ITEM_MODIFIERS_LEGS;
          Component boots = Constant.ITEM_MODIFIERS_FEET;
          Component attackSpeed = ComponentUtil.translate("attribute.name.generic.attack_speed");
          Component damage = ComponentUtil.translate("attribute.name.generic.attack_damage");
          Component armor = ComponentUtil.translate("attribute.name.generic.armor");
          Component armorToughness = ComponentUtil.translate("attribute.name.generic.armor_toughness");
          Component knockbackResistance = ComponentUtil.translate("attribute.name.generic.knockback_resistance");
          switch (type)
          {
            case WOODEN_AXE ->
            {
              lore.add(mainHand);
              lore.add(ComponentUtil.translate("rgb255,142,82;%s : %s", damage, "+6"));
              lore.add(ComponentUtil.translate("rgb255,142,82;%s : %s", attackSpeed, "-3.2"));
            }
            case STONE_AXE ->
            {
              lore.add(mainHand);
              lore.add(ComponentUtil.translate("rgb255,142,82;%s : %s", damage, "+8"));
              lore.add(ComponentUtil.translate("rgb255,142,82;%s : %s", attackSpeed, "-3.2"));
            }
            case IRON_AXE ->
            {
              lore.add(mainHand);
              lore.add(ComponentUtil.translate("rgb255,142,82;%s : %s", damage, "+8"));
              lore.add(ComponentUtil.translate("rgb255,142,82;%s : %s", attackSpeed, "-3.1"));
            }
            case DIAMOND_AXE ->
            {
              lore.add(mainHand);
              lore.add(ComponentUtil.translate("rgb255,142,82;%s : %s", damage, "+8"));
              lore.add(ComponentUtil.translate("rgb255,142,82;%s : %s", attackSpeed, "-3"));
            }
            case GOLDEN_AXE ->
            {
              lore.add(mainHand);
              lore.add(ComponentUtil.translate("rgb255,142,82;%s : %s", damage, "+6"));
              lore.add(ComponentUtil.translate("rgb255,142,82;%s : %s", attackSpeed, "-3"));
            }
            case WOODEN_PICKAXE, GOLDEN_PICKAXE ->
            {
              lore.add(mainHand);
              lore.add(ComponentUtil.translate("rgb255,142,82;%s : %s", damage, "+1"));
              lore.add(ComponentUtil.translate("rgb255,142,82;%s : %s", attackSpeed, "-2.8"));
            }
            case STONE_PICKAXE ->
            {
              lore.add(mainHand);
              lore.add(ComponentUtil.translate("rgb255,142,82;%s : %s", damage, "+2"));
              lore.add(ComponentUtil.translate("rgb255,142,82;%s : %s", attackSpeed, "-2.8"));
            }
            case IRON_PICKAXE ->
            {
              lore.add(mainHand);
              lore.add(ComponentUtil.translate("rgb255,142,82;%s : %s", damage, "+3"));
              lore.add(ComponentUtil.translate("rgb255,142,82;%s : %s", attackSpeed, "-2.8"));
            }
            case DIAMOND_PICKAXE ->
            {
              lore.add(mainHand);
              lore.add(ComponentUtil.translate("rgb255,142,82;%s : %s", damage, "+4"));
              lore.add(ComponentUtil.translate("rgb255,142,82;%s : %s", attackSpeed, "-2.8"));
            }
            case WOODEN_SHOVEL, GOLDEN_SHOVEL ->
            {
              lore.add(mainHand);
              lore.add(ComponentUtil.translate("rgb255,142,82;%s : %s", damage, "+1.5"));
              lore.add(ComponentUtil.translate("rgb255,142,82;%s : %s", attackSpeed, "-3"));
            }
            case STONE_SHOVEL ->
            {
              lore.add(mainHand);
              lore.add(ComponentUtil.translate("rgb255,142,82;%s : %s", damage, "+2.5"));
              lore.add(ComponentUtil.translate("rgb255,142,82;%s : %s", attackSpeed, "-3"));
            }
            case IRON_SHOVEL ->
            {
              lore.add(mainHand);
              lore.add(ComponentUtil.translate("rgb255,142,82;%s : %s", damage, "+3.5"));
              lore.add(ComponentUtil.translate("rgb255,142,82;%s : %s", attackSpeed, "-3"));
            }
            case DIAMOND_SHOVEL ->
            {
              lore.add(mainHand);
              lore.add(ComponentUtil.translate("rgb255,142,82;%s : %s", damage, "+4.5"));
              lore.add(ComponentUtil.translate("rgb255,142,82;%s : %s", attackSpeed, "-3"));
            }
            case WOODEN_HOE, GOLDEN_HOE ->
            {
              lore.add(mainHand);
              lore.add(ComponentUtil.translate("rgb255,142,82;%s : %s", attackSpeed, "-3"));
            }
            case STONE_HOE ->
            {
              lore.add(mainHand);
              lore.add(ComponentUtil.translate("rgb255,142,82;%s : %s", attackSpeed, "-2"));
            }
            case IRON_HOE ->
            {
              lore.add(mainHand);
              lore.add(ComponentUtil.translate("rgb255,142,82;%s : %s", attackSpeed, "-1"));
            }
            case WOODEN_SWORD ->
            {
              lore.add(mainHand);
              lore.add(ComponentUtil.translate("rgb255,142,82;%s : %s", damage, "+3"));
              lore.add(ComponentUtil.translate("rgb255,142,82;%s : %s", attackSpeed, "-2.4"));
            }
            case STONE_SWORD ->
            {
              lore.add(mainHand);
              lore.add(ComponentUtil.translate("rgb255,142,82;%s : %s", damage, "+4"));
              lore.add(ComponentUtil.translate("rgb255,142,82;%s : %s", attackSpeed, "-2.4"));
            }
            case IRON_SWORD ->
            {
              lore.add(mainHand);
              lore.add(ComponentUtil.translate("rgb255,142,82;%s : %s", damage, "+5"));
              lore.add(ComponentUtil.translate("rgb255,142,82;%s : %s", attackSpeed, "-2.4"));
            }
            case DIAMOND_SWORD ->
            {
              lore.add(mainHand);
              lore.add(ComponentUtil.translate("rgb255,142,82;%s : %s", damage, "+6"));
              lore.add(ComponentUtil.translate("rgb255,142,82;%s : %s", attackSpeed, "-2.4"));
            }
            case GOLDEN_SWORD ->
            {
              lore.add(mainHand);
              lore.add(ComponentUtil.translate("rgb255,142,82;%s : %s", damage, "+3"));
              lore.add(ComponentUtil.translate("rgb255,142,82;%s : %s", attackSpeed, "-2.4"));
            }
            case TRIDENT ->
            {
              lore.add(mainHand);
              lore.add(ComponentUtil.translate("rgb255,142,82;%s : %s", damage, "+8"));
              lore.add(ComponentUtil.translate("rgb255,142,82;%s : %s", attackSpeed, "-2.9"));
            }
            case LEATHER_HELMET ->
            {
              lore.add(helmet);
              lore.add(ComponentUtil.translate("rgb255,142,82;%s : %s", armor, "+1"));
            }
            case LEATHER_CHESTPLATE ->
            {
              lore.add(chestplate);
              lore.add(ComponentUtil.translate("rgb255,142,82;%s : %s", armor, "+3"));
            }
            case LEATHER_LEGGINGS ->
            {
              lore.add(leggings);
              lore.add(ComponentUtil.translate("rgb255,142,82;%s : %s", armor, "+2"));
            }
            case LEATHER_BOOTS, CHAINMAIL_BOOTS, GOLDEN_BOOTS ->
            {
              lore.add(boots);
              lore.add(ComponentUtil.translate("rgb255,142,82;%s : %s", armor, "+1"));
            }
            case CHAINMAIL_HELMET, TURTLE_HELMET, IRON_HELMET, GOLDEN_HELMET ->
            {
              lore.add(helmet);
              lore.add(ComponentUtil.translate("rgb255,142,82;%s : %s", armor, "+2"));
            }
            case CHAINMAIL_CHESTPLATE, GOLDEN_CHESTPLATE ->
            {
              lore.add(chestplate);
              lore.add(ComponentUtil.translate("rgb255,142,82;%s : %s", armor, "+5"));
            }
            case CHAINMAIL_LEGGINGS ->
            {
              lore.add(leggings);
              lore.add(ComponentUtil.translate("rgb255,142,82;%s : %s", armor, "+4"));
            }
            case IRON_CHESTPLATE ->
            {
              lore.add(chestplate);
              lore.add(ComponentUtil.translate("rgb255,142,82;%s : %s", armor, "+6"));
            }
            case IRON_LEGGINGS ->
            {
              lore.add(leggings);
              lore.add(ComponentUtil.translate("rgb255,142,82;%s : %s", armor, "+5"));
            }
            case IRON_BOOTS ->
            {
              lore.add(boots);
              lore.add(ComponentUtil.translate("rgb255,142,82;%s : %s", armor, "+2"));
            }
            case GOLDEN_LEGGINGS ->
            {
              lore.add(leggings);
              lore.add(ComponentUtil.translate("rgb255,142,82;%s : %s", armor, "+3"));
            }
            case DIAMOND_HELMET ->
            {
              lore.add(helmet);
              lore.add(ComponentUtil.translate("rgb255,142,82;%s : %s", armorToughness, "+2"));
              lore.add(ComponentUtil.translate("rgb255,142,82;%s : %s", armor, "+3"));
            }
            case DIAMOND_CHESTPLATE ->
            {
              lore.add(chestplate);
              lore.add(ComponentUtil.translate("rgb255,142,82;%s : %s", armor, "+8"));
              lore.add(ComponentUtil.translate("rgb255,142,82;%s : %s", armorToughness, "+2"));
            }
            case DIAMOND_LEGGINGS ->
            {
              lore.add(leggings);
              lore.add(ComponentUtil.translate("rgb255,142,82;%s : %s", armor, "+6"));
              lore.add(ComponentUtil.translate("rgb255,142,82;%s : %s", armorToughness, "+2"));
            }
            case DIAMOND_BOOTS ->
            {
              lore.add(boots);
              lore.add(ComponentUtil.translate("rgb255,142,82;%s : %s", armor, "+3"));
              lore.add(ComponentUtil.translate("rgb255,142,82;%s : %s", armorToughness, "+2"));
            }
            case NETHERITE_AXE ->
            {
              lore.add(mainHand);
              lore.add(ComponentUtil.translate("rgb255,142,82;%s : %s", damage, "+9"));
              lore.add(ComponentUtil.translate("rgb255,142,82;%s : %s", attackSpeed, "-3"));
            }
            case NETHERITE_PICKAXE ->
            {
              lore.add(mainHand);
              lore.add(ComponentUtil.translate("rgb255,142,82;%s : %s", damage, "+5"));
              lore.add(ComponentUtil.translate("rgb255,142,82;%s : %s", attackSpeed, "-2.8"));
            }
            case NETHERITE_SHOVEL ->
            {
              lore.add(mainHand);
              lore.add(ComponentUtil.translate("rgb255,142,82;%s : %s", damage, "+5.5"));
              lore.add(ComponentUtil.translate("rgb255,142,82;%s : %s", attackSpeed, "-3"));
            }
            case NETHERITE_SWORD ->
            {
              lore.add(mainHand);
              lore.add(ComponentUtil.translate("rgb255,142,82;%s : %s", damage, "+7"));
              lore.add(ComponentUtil.translate("rgb255,142,82;%s : %s", attackSpeed, "-2.4"));
            }
            case NETHERITE_HELMET ->
            {
              lore.add(helmet);
              lore.add(ComponentUtil.translate("rgb255,142,82;%s : %s", armor, "+3"));
              lore.add(ComponentUtil.translate("rgb255,142,82;%s : %s", armorToughness, "+3"));
              lore.add(ComponentUtil.translate("rgb255,142,82;%s : %s", knockbackResistance, "+1"));
            }
            case NETHERITE_CHESTPLATE ->
            {
              lore.add(chestplate);
              lore.add(ComponentUtil.translate("rgb255,142,82;%s : %s", armor, "+8"));
              lore.add(ComponentUtil.translate("rgb255,142,82;%s : %s", armorToughness, "+3"));
              lore.add(ComponentUtil.translate("rgb255,142,82;%s : %s", knockbackResistance, "+1"));
            }
            case NETHERITE_LEGGINGS ->
            {
              lore.add(leggings);
              lore.add(ComponentUtil.translate("rgb255,142,82;%s : %s", armor, "+6"));
              lore.add(ComponentUtil.translate("rgb255,142,82;%s : %s", armorToughness, "+3"));
              lore.add(ComponentUtil.translate("rgb255,142,82;%s : %s", knockbackResistance, "+1"));
            }
            case NETHERITE_BOOTS ->
            {
              lore.add(boots);
              lore.add(ComponentUtil.translate("rgb255,142,82;%s : %s", armor, "+3"));
              lore.add(ComponentUtil.translate("rgb255,142,82;%s : %s", armorToughness, "+3"));
              lore.add(ComponentUtil.translate("rgb255,142,82;%s : %s", knockbackResistance, "+1"));
            }
            default ->
            {
            }
          }
        }
      }
    }
    if (Cucumbery.config.getBoolean("use-damage-spread-feature"))
    {
      int damageSpread = getDamageSpread(itemStack);
      if (damageSpread > 0)
      {
        lore.add(ComponentUtil.translate("rgb255,142,82;%s : %s", "피해 발산", "+" + damageSpread));
      }
    }
    if (!lore.isEmpty())
    {
      originalLore.add(Component.empty());
      originalLore.addAll(lore);
    }
  }

  public static int getDamageSpread(@NotNull ItemStack itemStack)
  {
    int damageSpread = 0;
    CustomMaterial customMaterial = CustomMaterial.itemStackOf(itemStack);
    if (customMaterial == null)
    {
      switch (itemStack.getType())
      {
        case CHAINMAIL_CHESTPLATE, CHAINMAIL_LEGGINGS, NETHERITE_HELMET, NETHERITE_CHESTPLATE, NETHERITE_LEGGINGS, NETHERITE_BOOTS -> damageSpread = 1;
      }
    }
    else
    {
      switch (customMaterial)
      {
        case MITHRIL_HELMET, MITHRIL_BOOTS, COBALT_HELMET, COBALT_BOOTS -> damageSpread = 1;
        case COBALT_CHESTPLATE, COBALT_LEGGINGS, TITANIUM_HELMET, TITANIUM_CHESTPLATE, TITANIUM_LEGGINGS, TITANIUM_BOOTS -> damageSpread = 2;
        case NAUTILITE_HELMET, NAUTILITE_CHESTPLATE, NAUTILITE_LEGGINGS, NAUTILITE_BOOTS -> damageSpread = 3;
      }
    }
    return damageSpread;
  }
}
