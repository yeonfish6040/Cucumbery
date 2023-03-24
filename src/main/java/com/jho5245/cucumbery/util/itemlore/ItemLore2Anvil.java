package com.jho5245.cucumbery.util.itemlore;

import com.jho5245.cucumbery.util.storage.component.util.ComponentUtil;
import com.jho5245.cucumbery.util.storage.no_groups.ItemStackUtil;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ItemLore2Anvil
{
  protected static void setItemLore(@NotNull ItemStack item, @NotNull Material type, @NotNull ItemMeta itemMeta, @NotNull List<Component> lore)
  {
    int anvilUsedTime = ItemStackUtil.getAnvilUsedTime(itemMeta);
    if (anvilUsedTime > 0)
    {
      if (anvilUsedTime > 30)
      {
        itemMeta.displayName(ComponentUtil.translate(""));
        itemMeta.lore(null);
        item.setItemMeta(itemMeta);
      }
      else
      {
        long penalty = switch (anvilUsedTime)
                {
                  case 1 -> 20;
                  case 2 -> 40;
                  case 3 -> 60;
                  case 4 -> 100;
                  case 5 -> 200;
                  default -> (long) Math.pow(2, anvilUsedTime + 3);
                };
        // 아이템의 현재 내구도의 비율에 따른 아이템 희귀도 감소
        int maxDura = type.getMaxDurability();
        if (maxDura != -1 && itemMeta instanceof Damageable)
        {
          int dura = ((Damageable) itemMeta).getDamage();
          penalty *= dura * 1d / maxDura;
          // 내구도가 높을 수록 희귀도 차감량 감소
          double what = dura / 200d;
          if (what < 0.5d)
          {
            what = 0.5d;
          }
          if (what > 0d)
          {
            penalty /= what;
          }
        }

        // 수선이 있을 경우 모루 합성 횟수로 인한 아이템 희귀도 차감량 반감
        if (itemMeta.hasEnchant(Enchantment.MENDING) && itemMeta.getEnchantLevel(Enchantment.MENDING) > 0)
        {
          penalty /= 2.0;
        }
        // 내구성이 있을 경우 모루 합성 횟수로 인한 아이템 희귀도 차감량 감소
        int duraEnch = -1;
        if (itemMeta.hasEnchant(Enchantment.DURABILITY))
        {
          duraEnch = itemMeta.getEnchantLevel(Enchantment.DURABILITY);
        }
        if (duraEnch > 0)
        {
          penalty /= duraEnch;
        }

        ItemLoreUtil.setItemRarityValue(lore, -penalty);

        lore.add(Component.empty());
        lore.add(ComponentUtil.translate("rgb203,164,12;누적 모루 합성 횟수 : %s", ComponentUtil.translate("rg255,204;%s회", anvilUsedTime + "")));
      }
    }
  }
}
