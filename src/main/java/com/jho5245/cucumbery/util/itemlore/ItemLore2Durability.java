package com.jho5245.cucumbery.util.itemlore;

import com.jho5245.cucumbery.util.nbt.CucumberyTag;
import com.jho5245.cucumbery.util.no_groups.Method2;
import com.jho5245.cucumbery.util.storage.component.util.ComponentUtil;
import com.jho5245.cucumbery.util.storage.data.Constant;
import com.jho5245.cucumbery.util.storage.data.CustomMaterial;
import com.jho5245.cucumbery.util.storage.no_groups.ItemCategory;
import de.tr7zw.changeme.nbtapi.NBTCompound;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ItemLore2Durability
{
  protected static void setItemLore(@NotNull ItemStack item, @NotNull Material type,
                                    @Nullable CustomMaterial customMaterial, @NotNull ItemMeta itemMeta,
                                    @NotNull List<Component> lore, @Nullable NBTCompound duraTag,
                                    boolean isDrill, boolean hideDurability, boolean hideDurabilityChanceNotToConsume)
  {
    if (customMaterial != CustomMaterial.UNBINDING_SHEARS)
    {
      if (itemMeta.isUnbreakable())
      {
        ItemLoreUtil.setItemRarityValue(lore, ItemCategory.Rarity.UNIQUE.getRarityValue());
        if (!hideDurability)
        {

          lore.add(Component.empty());
          lore.add(ComponentUtil.translate("rgb225,100,205;" + (isDrill ? "연료" : "내구도") + " : %s / %s", Component.text("∞"), Component.text("∞")));
        }
        itemMeta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
      }
      else if (Constant.DURABLE_ITEMS.contains(type) || duraTag != null)
      {
        long currentDurability = 0, maxDurability = 0;
        double chanceNotToConsumeDura = 0d;
        boolean duraTagExists = duraTag != null;
        if (duraTagExists)
        {
          try
          {
            maxDurability = duraTag.getLong(CucumberyTag.CUSTOM_DURABILITY_MAX_KEY);
            currentDurability = duraTag.getLong(CucumberyTag.CUSTOM_DURABILITY_CURRENT_KEY);
          }
          catch (Exception e)
          {
            currentDurability = 0;
            maxDurability = 0;
          }
          try
          {
            chanceNotToConsumeDura = duraTag.getDouble(CucumberyTag.CUSTOM_DURABILITY_CHANCE_NOT_TO_CONSUME_DURABILITY);
          }
          catch (Exception e)
          {
            chanceNotToConsumeDura = 0d;
          }

          if (maxDurability != 0)
          {
            double originItemDuraDouble = getOriginItemDuraDouble(type, maxDurability, currentDurability);
            Damageable damageable = (Damageable) itemMeta;
            damageable.setDamage((int) originItemDuraDouble);
            item.setItemMeta(damageable);
          }
        }

        if (maxDurability == 0 && Constant.DURABLE_ITEMS.contains(type))
        {
          maxDurability = type.getMaxDurability();
          currentDurability = ((Damageable) item.getItemMeta()).getDamage();
        }

        if (maxDurability != 0 || Constant.DURABLE_ITEMS.contains(type))
        {
          if (!hideDurability)
          {
            lore.add(Component.empty());
            String color = Method2.getPercentageColor(maxDurability - currentDurability, maxDurability);
            lore.add(ComponentUtil.translate("rg255,204;" + (isDrill ? "연료" : "내구도") + " : %s",
                    ComponentUtil.translate("&7%s / %s", color + Constant.Jeongsu.format(maxDurability - currentDurability), "g255;" + Constant.Jeongsu.format(maxDurability))));
          }
        }

        int dura = 0;
        if (itemMeta.hasEnchant(Enchantment.DURABILITY))
        {
          dura = itemMeta.getEnchantLevel(Enchantment.DURABILITY);
        }
        if (itemMeta.hasEnchant(Enchantment.MENDING))
        {
          dura += 9;
        }
        double ratio = 1d * (maxDurability - currentDurability) / maxDurability;
        // 내구도로 인한 아이템 등급 수치 감소에서는 내구도가 꽉 찼을 때 비율이 0으로 되게 함
        ItemLoreUtil.setItemRarityValue(lore, (long) (ItemLoreUtil.getItemRarityValue(lore) * 0.01 + maxDurability * 0.05));
        long duraNegative = (long) ((1 + ItemLoreUtil.getItemRarityValue(lore) / 10000) * Math.pow(1.055 - (dura / 1000d), (100 - ratio * 100)) - 1);
        //(long) Math.pow(ratio * (2.0 + (20.0 / maxDurability)), Math.abs(Math.pow(3.0 - dura / 10.0, Math.abs(ratio)) + 1.7 + (200.0 / maxDurability) - maxDurability / 1300.0)); // 내구도로 인한 아이템 등급 수치 감소
        if (duraNegative > 0)
        {
          ItemLoreUtil.setItemRarityValue(lore, -duraNegative);
        }
        if (maxDurability != 0 && chanceNotToConsumeDura > 0d && chanceNotToConsumeDura <= 100d)
        {
          if (!hideDurabilityChanceNotToConsume)
          {
            lore.add(Component.empty());
            lore.add(ComponentUtil.create("&a내구도 감소 무효 확률 : +" + Constant.Sosu4.format(chanceNotToConsumeDura) + "%"));
          }
        }
      }
      else if (type != Material.MAP)
      {
        if (itemMeta instanceof Damageable duraMeta)
        {
          if (duraMeta.getDamage() != 0)
          {
            lore.add(Component.empty());
            lore.add(ComponentUtil.translate("&6내구도 손상 : %s", duraMeta.getDamage() + ""));
          }
        }
      }
    }
  }

  private static double getOriginItemDuraDouble(@NotNull Material type, long maxDurability, long currentDurability)
  {
    int originMaxDura = type.getMaxDurability();
    double originItemDuraDouble = (originMaxDura * ((currentDurability) * 1d / maxDurability));
    if (originItemDuraDouble > 0d && originItemDuraDouble < 1d)
    {
      originItemDuraDouble = 1d;
    }
    if (type == Material.ELYTRA)
    {
      if (currentDurability == 1)
      {
        originItemDuraDouble = Material.ELYTRA.getMaxDurability() - 1;
      }
      else if (currentDurability > 1 && originItemDuraDouble > 430)
      {
        originItemDuraDouble = Material.ELYTRA.getMaxDurability() - 2;
      }
    }
    return originItemDuraDouble;
  }
}
