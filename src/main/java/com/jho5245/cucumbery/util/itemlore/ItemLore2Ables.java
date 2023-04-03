package com.jho5245.cucumbery.util.itemlore;

import com.jho5245.cucumbery.custom.customeffect.CustomEffectManager;
import com.jho5245.cucumbery.custom.customeffect.type.CustomEffectType;
import com.jho5245.cucumbery.custom.customeffect.type.CustomEffectTypeCustomMining;
import com.jho5245.cucumbery.util.nbt.CucumberyTag;
import com.jho5245.cucumbery.util.nbt.NBTAPI;
import com.jho5245.cucumbery.util.storage.component.util.ComponentUtil;
import com.jho5245.cucumbery.util.storage.data.Constant;
import com.jho5245.cucumbery.util.storage.data.Constant.CucumberyHideFlag;
import com.jho5245.cucumbery.util.storage.data.Constant.RestrictionType;
import com.jho5245.cucumbery.util.storage.no_groups.ItemStackUtil;
import de.tr7zw.changeme.nbtapi.NBTCompound;
import de.tr7zw.changeme.nbtapi.NBTItem;
import de.tr7zw.changeme.nbtapi.NBTList;
import de.tr7zw.changeme.nbtapi.NBTType;
import net.kyori.adventure.text.Component;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ItemLore2Ables
{
  protected static void setItemLore(@NotNull NBTItem nbtItem, @Nullable NBTCompound foodTag, @Nullable NBTList<String> hideFlags, @NotNull ItemStack item, @NotNull List<Component> lore, @Nullable Object params)
  {
    Material type = item.getType();
    // 설치 가능
    if (ItemStackUtil.isPlacable(type) && !NBTAPI.arrayContainsValue(hideFlags, CucumberyHideFlag.PLACABLE.toString())
            && !NBTAPI.isRestrictedFinal(item, RestrictionType.NO_PLACE))
    {
      lore.add(Component.empty());
      boolean no = false, commandBlockNoOp = false, customEffectNoPlace = params instanceof ItemLoreView view &&
              (CustomEffectManager.hasEffect(view.getPlayer(), CustomEffectType.CURSE_OF_CREATIVITY) ||
                      CustomEffectManager.hasEffect(view.getPlayer(), CustomEffectType.CURSE_OF_CREATIVITY_PLACE) ||
                      (view.getPlayer().getGameMode() != GameMode.CREATIVE &&
                              CustomEffectManager.hasEffect(view.getPlayer(), CustomEffectTypeCustomMining.CUSTOM_MINING_SPEED_MODE) &&
                              !CustomEffectManager.hasEffect(view.getPlayer(), CustomEffectTypeCustomMining.CUSTOM_MINING_SPEED_MODE_2_NO_RESTORE)));
      if (customEffectNoPlace)
      {
        no = true;
      }
      switch (type)
      {
        case COMMAND_BLOCK, REPEATING_COMMAND_BLOCK, CHAIN_COMMAND_BLOCK, JIGSAW, STRUCTURE_BLOCK ->
        {
          if (params instanceof ItemLoreView view && (!view.getPlayer().isOp() || view.getPlayer().getGameMode() != GameMode.CREATIVE))
          {
            no = true;
            commandBlockNoOp = true;
          }
        }
      }
      if (no)
      {
        lore.add(ComponentUtil.translate("&4[설치 불가]"));
        if (commandBlockNoOp)
        {
          lore.add(ComponentUtil.translate("&7크리에이티브 상태인 관리자가 아니여서 설치할 수 없습니다"));
        }
        if (customEffectNoPlace)
        {
          lore.add(ComponentUtil.translate("&7블록을 설치할 수 없는 상태입니다"));
        }
      }
      else
      {
        lore.add(ComponentUtil.translate(Constant.ITEM_LORE_PLACABLE));
      }
    }

/*    if (RecipeChecker.hasCraftingRecipe(type) && !NBTAPI.arrayContainsValue(hideFlags, CucumberyHideFlag.CRAFTABLE.toString()))
    {
      boolean noCraft = NBTAPI.isRestrictedFinal(item, RestrictionType.NO_CRAFT);
      boolean noCraftInventory = NBTAPI.isRestrictedFinal(item, RestrictionType.NO_CRAFT_IN_INVENTORY);
      boolean noCraftCraftingTable = NBTAPI.isRestrictedFinal(item, RestrictionType.NO_CRAFT_IN_CRAFTING_TABLE);
      if (!noCraft && !(noCraftInventory && noCraftCraftingTable))
      {
        lore.add(Component.empty());
        if (!noCraftInventory && !noCraftCraftingTable)
        {
          lore.add(ComponentUtil.translate(Constant.ITEM_LORE_MATERIAL_CRAFTABLE));
        }
        else if (!noCraftCraftingTable)
        {
          lore.add(ComponentUtil.translate(Constant.ITEM_LORE_MATERIAL_CRAFTABLE_ONLY_CRAFTING_TABLE));
        }
        else
        {
          lore.add(ComponentUtil.translate(Constant.ITEM_LORE_MATERIAL_CRAFTABLE_ONLY_INVENTORY));
        }
      }
    }*/

/*    if (ItemStackUtil.isBrewable(type) && !NBTAPI.arrayContainsValue(hideFlags, CucumberyHideFlag.BREWABLE.toString())
            && (!NBTAPI.isRestricted(item, RestrictionType.NO_BREW) || NBTAPI.getRestrictionOverridePermission(item, RestrictionType.NO_BREW) != null))
    {
      lore.add(Component.empty());
      lore.add(ComponentUtil.translate(Constant.ITEM_LORE_MATERIAL_BREWABLE));
    }*/

/*    boolean noFurnace = NBTAPI.isRestrictedFinal(item, RestrictionType.NO_FURNACE);
    boolean noSmoker = NBTAPI.isRestrictedFinal(item, RestrictionType.NO_SMOKER);
    boolean noBlastFurnace = NBTAPI.isRestrictedFinal(item, RestrictionType.NO_BLAST_FURNACE);
    boolean noCampfire = NBTAPI.isRestrictedFinal(item, RestrictionType.NO_CAMPFIRE);

    if (RecipeChecker.hasSmeltingRecipe(item) &&
            !NBTAPI.arrayContainsValue(hideFlags, CucumberyHideFlag.SMELTABLE.toString()) &&
            !NBTAPI.isRestrictedFinal(item, RestrictionType.NO_SMELT) &&
            !(noBlastFurnace && noSmoker && noFurnace && noCampfire))
    {
      lore.add(Component.empty());
      if (noBlastFurnace || noCampfire || noFurnace || noSmoker)
      {
        String available = "";

        if (!noFurnace)
        {
          available += "화로, ";
        }
        if (!noBlastFurnace)
        {
          available += "용광로, ";
        }
        if (!noSmoker)
        {
          available += "훈연기, ";
        }
        if (!noCampfire)
        {
          available += "모닥불, ";
        }
        available = available.substring(0, available.length() - 2);
        if (type.isEdible())
        {
          lore.add(ComponentUtil.translate("#F07447;[" + available + "에서만 조리 가능]"));
        }
        else
        {
          lore.add(ComponentUtil.translate("rgb255,79,48;[" + available + "에서만 제련 가능]"));
        }
      }
      else
      {
        if (type.isEdible())
        {
          lore.add(ComponentUtil.translate(Constant.ITEM_LORE_MATERIAL_SMELTABLE_COOK));
        }
        else
        {
          lore.add(ComponentUtil.translate(Constant.ITEM_LORE_MATERIAL_SMELTABLE));
        }
      }
    }*/

    if ((ItemStackUtil.isEdible(type) || type == Material.CAKE) && !NBTAPI.arrayContainsValue(hideFlags, CucumberyHideFlag.CONSUMABLE.toString())
            && !NBTAPI.isRestrictedFinal(item, RestrictionType.NO_CONSUME))
    {
      String nourishment = ItemStackUtil.getNourishment(type);
      lore.add(Component.empty());
      boolean noConsume = params instanceof ItemLoreView view && CustomEffectManager.hasEffect(view.getPlayer(), CustomEffectType.CURSE_OF_CONSUMPTION);
      if (noConsume)
      {
        lore.add(ComponentUtil.translate("&4[섭취 불가]"));
        lore.add(ComponentUtil.translate("&7아이템을 사용할 수 없는 상태입니다"));
      }
      else
      {
        lore.add(ComponentUtil.translate(Constant.ITEM_LORE_CONSUMABLE));
      }
      if (foodTag != null)
      {
        Integer foodLevel = foodTag.hasTag(CucumberyTag.FOOD_LEVEL_KEY) ? foodTag.getInteger(CucumberyTag.FOOD_LEVEL_KEY) : null;
        if (foodLevel == null)
        {
          foodLevel = 0;
        }
        Double saturation = foodTag.hasTag(CucumberyTag.SATURATION_KEY) ? foodTag.getDouble(CucumberyTag.SATURATION_KEY) : null;
        if (saturation == null)
        {
          saturation = 0d;
        }
        if (foodTag.hasTag(CucumberyTag.NOURISHMENT_KEY))
        {
          nourishment = foodTag.getString(CucumberyTag.NOURISHMENT_KEY);
          lore.add(ComponentUtil.translate("rgb235,163,0;든든함 : %s", ComponentUtil.translate(nourishment)));
        }
        else if (foodLevel != 0 || saturation != 0)
        {
          nourishment = ItemStackUtil.getNourishment(foodLevel, saturation);
          lore.add(ComponentUtil.translate("rgb235,163,0;든든함 : %s", ComponentUtil.translate(nourishment)));
        }
        if (!foodTag.hasTag(CucumberyTag.FOOD_LEVEL_KEY) && ItemStackUtil.getFoodLevel(type) != 0)
        {
          lore.add(ComponentUtil.translate("rgb255,183,0;음식 포인트 : %s", "+" + ItemStackUtil.getFoodLevel(type)));
        }
        else if (foodLevel != 0)
        {
          lore.add(ComponentUtil.translate("rgb255,183,0;음식 포인트 : %s", (foodLevel > 0 ? "+" : "") + foodLevel));
        }
        if (!foodTag.hasTag(CucumberyTag.SATURATION_KEY) && ItemStackUtil.getSaturation(type) != 0d)
        {
          lore.add(ComponentUtil.translate("rgb255,183,0;포화도 : %s", "+" + Constant.Sosu2.format(ItemStackUtil.getSaturation(type))));
        }
        else if (saturation != 0d)
        {
          lore.add(ComponentUtil.translate("rgb255,183,0;포화도 : %s", (saturation > 0d ? "+" : "") + Constant.Sosu2.format(saturation)));
        }
      }
      else if (nourishment != null && !nourishment.equals("기본"))
      {
        lore.add(ComponentUtil.translate("rgb235,163,0;든든함 : %s", ComponentUtil.translate(nourishment)));
        lore.add(ComponentUtil.translate("rgb255,183,0;음식 포인트 : %s", "+" + ItemStackUtil.getFoodLevel(type)));
        lore.add(ComponentUtil.translate("rgb255,183,0;포화도 : %s", "+" + Constant.Sosu2.format(ItemStackUtil.getSaturation(type))));
      }
    }

    if (type.isFuel() && !NBTAPI.arrayContainsValue(hideFlags, CucumberyHideFlag.FUEL.toString())
            && !NBTAPI.isRestrictedFinal(item, RestrictionType.NO_FUEL))
    {
      double sec = ItemStackUtil.getFuelTimeInSecond(type);
      if (nbtItem.hasTag("BurnTime") && nbtItem.getType("BurnTime") == NBTType.NBTTagInt)
      {
        sec = nbtItem.getInteger("BurnTime") / 20d;
      }
      lore.add(Component.empty());
      lore.add(ComponentUtil.translate(Constant.ITEM_LORE_FUEL));
      lore.add(ComponentUtil.translate("rgb232,99,79;지속 시간 : %s"
              , ComponentUtil.translate("%s초", Constant.Sosu2.format(sec))));
      if (nbtItem.hasTag("CookSpeed") && nbtItem.getType("CookSpeed") == NBTType.NBTTagDouble)
      {
        lore.add(ComponentUtil.translate("rgb232,99,79;아이템 굽는 속도 : %s", Constant.Sosu2.format(nbtItem.getDouble("CookSpeed") * 100) + "%"));
      }
    }

    double compostChance = ItemStackUtil.getCompostChance(type);

    if (compostChance > 0d && !NBTAPI.arrayContainsValue(hideFlags, CucumberyHideFlag.COMPOSTABLE.toString())
            && !NBTAPI.isRestrictedFinal(item, RestrictionType.NO_COMPOSTER))
    {
      lore.add(Component.empty());
      lore.add(ComponentUtil.translate(Constant.ITEM_LORE_MATERIAL_COMPOSTABLE, Constant.Sosu2.format(compostChance) + "%"));
    }
  }
}
