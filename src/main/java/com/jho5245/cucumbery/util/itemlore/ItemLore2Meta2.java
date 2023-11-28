package com.jho5245.cucumbery.util.itemlore;

import com.jho5245.cucumbery.util.nbt.NBTAPI;
import com.jho5245.cucumbery.util.storage.component.ItemStackComponent;
import com.jho5245.cucumbery.util.storage.component.util.ComponentUtil;
import com.jho5245.cucumbery.util.storage.component.util.ItemNameUtil;
import com.jho5245.cucumbery.util.storage.data.Constant.RestrictionType;
import com.jho5245.cucumbery.util.storage.data.CustomMaterial;
import de.tr7zw.changeme.nbtapi.NBTItem;
import de.tr7zw.changeme.nbtapi.NBTType;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.FireworkEffect;
import org.bukkit.Material;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BundleMeta;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class ItemLore2Meta2
{
  protected static void setItemLore(@NotNull ItemStack item, @NotNull Material type, @Nullable CustomMaterial customMaterial, @NotNull ItemMeta itemMeta, @NotNull List<Component> lore, @NotNull NBTItem nbtItem, boolean hideFireworkEffects)
  {
    switch (type)
    {
      case FIREWORK_ROCKET ->
      {
        FireworkMeta fireworkMeta = (FireworkMeta) itemMeta;
        fireworkMeta.addItemFlags(ItemFlag.HIDE_ITEM_SPECIFICS);
        int power = fireworkMeta.getPower();
        if (power >= 0 && power <= 127)
        {
          ItemLoreUtil.setItemRarityValue(lore, 20 * power);
        }
        if (!hideFireworkEffects)
        {
          lore.add(Component.empty());
          if (power >= 0 && power <= 127)
          {
            lore.add(ComponentUtil.translate("&7체공 시간 : %s", ComponentUtil.translate("&6약 ").append(ComponentUtil.translate("&6%s초", "" + (0.5d * (power + 1d) + 0.3)))));
          }
          else if (power == 255)
          {
            lore.add(ComponentUtil.translate("&7체공 시간 : %s", ComponentUtil.translate("&6약 ").append(ComponentUtil.translate("&6%s초", "0.3"))));
          }
          else
          {
            lore.add(ComponentUtil.translate("&7체공 시간 : %s", ComponentUtil.translate("&6즉시 폭발")));
          }

          // 폭발형 폭죽은 효과 출력 안함
          if (fireworkMeta.hasEffects() &&
                  customMaterial != CustomMaterial.FIREWORK_ROCKET_EXPLOSIVE &&
                  customMaterial != CustomMaterial.FIREWORK_ROCKET_EXPLOSIVE_DESTRUCTION && customMaterial != CustomMaterial.FIREWORK_ROCKET_EXPLOSIVE_FLAME)
          {
            lore.add(Component.empty());
            lore.add(ComponentUtil.translate("rg255,204;[폭죽 효과 목록]"));

            int effectSize = fireworkMeta.getEffectsSize();
            for (int i = 0; i < fireworkMeta.getEffectsSize(); i++)
            {
              if (effectSize > 5)
              {
                int skipped = effectSize - 5;
                if (i > 2 && i < effectSize - 2)
                {
                  if (i == 3)
                  {
                    lore.add(ComponentUtil.translate("&7&ocontainer.shulkerBox.more", Component.text(skipped)));
                  }
                  continue;
                }
              }
              Component add = ComponentUtil.translate("&3&m          %s          ", ComponentUtil.translate("&m&q[%s]", ComponentUtil.translate("&9%s번째 효과", i + 1)));
              lore.add(add);
              FireworkEffect fireworkEffect = fireworkMeta.getEffects().get(i);
              ItemLoreUtil.addFireworkEffectLore(lore, fireworkEffect);
            }
          }
        }
      }
      case EXPERIENCE_BOTTLE ->
      {
        lore.add(Component.empty());
        int minExp = 3, maxExp = 11;
        if (nbtItem.hasTag("MinExp") && nbtItem.getType("MinExp") == NBTType.NBTTagInt)
        {
          minExp = nbtItem.getInteger("MinExp");
        }
        if (nbtItem.hasTag("MaxExp") && nbtItem.getType("MaxExp") == NBTType.NBTTagInt)
        {
          maxExp = nbtItem.getInteger("MaxExp");
        }
        if (minExp > maxExp)
        {
          minExp = maxExp;
        }
        lore.add(ComponentUtil.translate("&7경험치 : %s", minExp != maxExp ? ComponentUtil.translate("&a%s~%s", minExp, maxExp) : "&a" + maxExp));
        if (customMaterial == null)
        {
          ItemLoreUtil.setItemRarityValue(lore, (long) (minExp * 0.01 + maxExp * 0.001));
        }

      }
      case BUNDLE -> {
        if (itemMeta instanceof BundleMeta bundleMeta)
        {
          List<ItemStack> noDrops = new ArrayList<>(), noTrades = new ArrayList<>();
          for (ItemStack itemStack : bundleMeta.getItems())
          {
            if (NBTAPI.isRestricted(itemStack, RestrictionType.NO_DROP))
            {
              noDrops.add(itemStack);
            }
            if (NBTAPI.isRestricted(itemStack, RestrictionType.NO_TRADE))
            {
              noTrades.add(itemStack);
            }
          }
          if (!noDrops.isEmpty())
          {
            lore.add(Component.empty());
            lore.add(ComponentUtil.translate("&c%s에 버릴 수 없는 아이템이 들어있습니다!", ItemNameUtil.itemName(item, NamedTextColor.RED)));
            for (ItemStack itemStack : noDrops)
            {
              lore.add(ItemStackComponent.itemStackComponent(itemStack, NamedTextColor.GRAY));
            }
          }
          if (!noTrades.isEmpty())
          {
            lore.add(Component.empty());
            lore.add(ComponentUtil.translate("&c%s에 캐릭터 귀속 아이템이 들어있습니다!", ItemNameUtil.itemName(item, NamedTextColor.RED)));
            for (ItemStack itemStack : noTrades)
            {
              lore.add(ItemStackComponent.itemStackComponent(itemStack, NamedTextColor.GRAY));
            }
          }
        }
      }
    }
  }
}
