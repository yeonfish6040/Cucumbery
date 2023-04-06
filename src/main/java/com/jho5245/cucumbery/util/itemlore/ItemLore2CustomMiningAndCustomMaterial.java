package com.jho5245.cucumbery.util.itemlore;

import com.jho5245.cucumbery.custom.customeffect.CustomEffectManager;
import com.jho5245.cucumbery.custom.customeffect.custom_mining.MiningManager;
import com.jho5245.cucumbery.custom.customeffect.type.CustomEffectTypeCustomMining;
import com.jho5245.cucumbery.util.nbt.NBTAPI;
import com.jho5245.cucumbery.util.storage.component.util.ComponentUtil;
import com.jho5245.cucumbery.util.storage.component.util.ItemNameUtil;
import com.jho5245.cucumbery.util.storage.data.Constant;
import com.jho5245.cucumbery.util.storage.data.Constant.CucumberyHideFlag;
import com.jho5245.cucumbery.util.storage.data.CustomMaterial;
import de.tr7zw.changeme.nbtapi.NBTItem;
import de.tr7zw.changeme.nbtapi.NBTList;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ItemLore2CustomMiningAndCustomMaterial
{
  protected static void setItemLore(@NotNull ItemStack item, @NotNull Material type,
                                    @Nullable CustomMaterial customMaterial, @NotNull List<Component> lore,
                                    @NotNull NBTItem nbtItem, @Nullable NBTList<String> hideFlags,
                                    @Nullable Player viewer, @Nullable Object params)
  {
    if (customMaterial != CustomMaterial.UNBINDING_SHEARS)
    {
      if (!NBTAPI.arrayContainsValue(hideFlags, CucumberyHideFlag.CUSTOM_MININGS) &&
              (Constant.TOOLS_LOSE_DURABILITY_BY_BREAKING_BLOCKS.contains(type) || nbtItem.hasTag(MiningManager.TOOL_TIER) || nbtItem.hasTag(MiningManager.TOOL_SPEED) || nbtItem.hasTag(MiningManager.TOOL_FORTUNE)))
      {
        if (params instanceof ItemLoreView view && CustomEffectManager.hasEffect(view.getPlayer(), CustomEffectTypeCustomMining.CUSTOM_MINING_SPEED_MODE))
        {
          int toolTier = MiningManager.getToolTier(item);
          float toolSpeed = MiningManager.getToolSpeed(item);
          Float toolFortune = nbtItem.getFloat(MiningManager.TOOL_FORTUNE);
          if (toolTier > 0 || toolSpeed > 0f || toolFortune > 0f)
          {
            lore.add(Component.empty());
          }
          if (toolTier > 0)
          {
            lore.add(ComponentUtil.translate("&6채광 등급 : %s", ComponentUtil.create(Constant.THE_COLOR_HEX + Constant.Jeongsu.format(toolTier)).append(
                    (viewer != null && CustomEffectManager.hasEffect(viewer, CustomEffectTypeCustomMining.MINDAS_TOUCH) ? (ComponentUtil.translate("&a (+%s) (%s)",
                            CustomEffectManager.getEffect(viewer, CustomEffectTypeCustomMining.MINDAS_TOUCH).getAmplifier() + 1, CustomEffectTypeCustomMining.MINDAS_TOUCH)) : Component.empty()))));
          }
          if (toolSpeed > 0f)
          {
            String prefix = "채광";
            if (toolTier == 0)
            {
              if (Constant.AXES.contains(type))
              {
                prefix = "벌목";
              }
              if (Constant.HOES.contains(type))
              {
                prefix = "재배";
              }
              if (Constant.SHOVELS.contains(type))
              {
                prefix = "굴착";
              }
              if (Constant.SWORDS.contains(type) || type == Material.TRIDENT)
              {
                prefix = "블록 파괴";
              }
            }
            lore.add(ComponentUtil.translate("&6" + prefix + " 속도 : %s", Constant.THE_COLOR_HEX + "+" + Constant.Sosu2.format(toolSpeed)));
          }
          if (toolFortune != null && toolFortune > 0f)
          {
            lore.add(ComponentUtil.translate("&6채광 행운 : %s", Constant.THE_COLOR_HEX + "+" + Constant.Sosu2.format(toolFortune)));
          }
        }
        else if (nbtItem.hasTag(MiningManager.TOOL_TIER) || nbtItem.hasTag(MiningManager.TOOL_SPEED) || nbtItem.hasTag(MiningManager.TOOL_FORTUNE))
        {
          lore.add(Component.empty());
          lore.add(ComponentUtil.translate("&c이 아이템은 현재 위치에서는 제 기능을 발휘할 수 없습니다!"));
          lore.add(ComponentUtil.translate("&c현재 기능하는 아이템 : %s", ItemNameUtil.itemName(type)));
        }
      }
    }
  }
}
