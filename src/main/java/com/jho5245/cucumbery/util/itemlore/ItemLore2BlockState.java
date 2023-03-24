package com.jho5245.cucumbery.util.itemlore;

import com.jho5245.cucumbery.util.no_groups.Method;
import com.jho5245.cucumbery.util.storage.component.ItemStackComponent;
import com.jho5245.cucumbery.util.storage.component.util.ComponentUtil;
import com.jho5245.cucumbery.util.storage.component.util.ItemNameUtil;
import com.jho5245.cucumbery.util.storage.data.Constant;
import com.jho5245.cucumbery.util.storage.no_groups.ItemStackUtil;
import de.tr7zw.changeme.nbtapi.NBTCompound;
import de.tr7zw.changeme.nbtapi.NBTItem;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Color;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.Nameable;
import org.bukkit.block.*;
import org.bukkit.inventory.*;
import org.bukkit.inventory.meta.BlockStateMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.loot.LootTable;
import org.bukkit.loot.Lootable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class ItemLore2BlockState
{
  protected static void setItemLore(@NotNull ItemStack item, @NotNull Material type, @NotNull ItemMeta itemMeta, @NotNull List<Component> lore, @NotNull NBTItem nbtItem, boolean hideBlockState, @Nullable Object params)
  {
    if (itemMeta instanceof BlockStateMeta blockStateMeta)
    {
      itemMeta.addItemFlags(ItemFlag.HIDE_ITEM_SPECIFICS);
      if (!hideBlockState && blockStateMeta.hasBlockState())
      {
        NBTCompound blockEntityTag = nbtItem.getCompound("BlockEntityTag");
        BlockState blockState = blockStateMeta.getBlockState();
        Component customName = blockState instanceof Nameable nameable ? nameable.customName() : null;
        if (customName == null)
        {
          customName = ItemNameUtil.itemName(item);
        }

        String colorPrefix = "&2";
        switch (type)
        {
          case SHULKER_BOX -> colorPrefix = "#B274E2;";
          case BLACK_SHULKER_BOX, GRAY_SHULKER_BOX, LIGHT_GRAY_SHULKER_BOX -> colorPrefix = "#808080;";
          case BLUE_SHULKER_BOX -> colorPrefix = "#415DFF;";
          case BROWN_SHULKER_BOX -> colorPrefix = "#995700;";
          case CYAN_SHULKER_BOX -> colorPrefix = "&3";
          case GREEN_SHULKER_BOX -> colorPrefix = "#00A800;";
          case LIGHT_BLUE_SHULKER_BOX -> colorPrefix = "#46CDEC;";
          case LIME_SHULKER_BOX -> colorPrefix = "#73DC00;";
          case MAGENTA_SHULKER_BOX -> colorPrefix = "#D043C4;";
          case ORANGE_SHULKER_BOX -> colorPrefix = "#FF7414;";
          case PINK_SHULKER_BOX -> colorPrefix = "#FF8FA8;";
          case PURPLE_SHULKER_BOX -> colorPrefix = "#A400D6;";
          case RED_SHULKER_BOX -> colorPrefix = "#E9483A;";
          case WHITE_SHULKER_BOX -> colorPrefix = "#F5F5F5;";
          case YELLOW_SHULKER_BOX -> colorPrefix = "#FFD400;";
          case CHEST, TRAPPED_CHEST -> colorPrefix = "#CC8500;";
          case BARREL -> colorPrefix = "#B5703B;";
          case CAMPFIRE -> colorPrefix = "#FFC700;";
          case SOUL_CAMPFIRE -> colorPrefix = "#81E9F4;";
          case BEE_NEST -> colorPrefix = "#F8AD0D;";
          case BEEHIVE -> colorPrefix = "#CAA863;";
          case FURNACE, BLAST_FURNACE, SMOKER, DROPPER, DISPENSER, HOPPER -> colorPrefix = "#978E87;";
        }
        Component customNameLore = ComponentUtil.translate(colorPrefix + "[%s의 내용물]", customName);

        if (blockState instanceof Sign sign)
        {
          boolean isGlowingText = sign.isGlowingText();
          lore.add(Component.empty());
          lore.add(ComponentUtil.translate("&9" + (isGlowingText ? "&l" : "") + "[%s의 내용]", customName));

          DyeColor dyeColor = sign.getColor();
          Color color = dyeColor != null ? dyeColor.getColor() : null;
          TextColor textColor = color != null ? TextColor.color(color.asRGB()) : NamedTextColor.WHITE;
          if (dyeColor == DyeColor.BLACK)
          {
            textColor = NamedTextColor.WHITE;
          }
          List<Component> lines = sign.lines();
          boolean hasAtleastOne = false;
          for (int i = 0; i < lines.size(); i++)
          {
            Component line = lines.get(i);
            if (line.color() == null)
            {
              line = line.color(textColor);
            }
            if (!(line.equals(Component.empty())))
            {
              hasAtleastOne = true;
              lore.add(ComponentUtil.translate("&7%s번째 텍스트 : %s", (i + 1), line));
            }
          }

          if (!hasAtleastOne)
          {
            lore.remove(lore.size() - 1);
            lore.remove(lore.size() - 1);
          }
        }
        if (blockState instanceof Lockable lockable && lockable.isLocked())
        {
          lore.add(Component.empty());
          lore.add(ComponentUtil.translate("&7잠금 태그 : %s", ComponentUtil.create2(lockable.getLock(), false)));
        }
        if (blockState instanceof BrewingStand brewingStand)
        {
          int brewingTime = brewingStand.getBrewingTime();
          int fuelLevel = brewingStand.getFuelLevel();
          lore.add(Component.empty());
          lore.add(ComponentUtil.translate("&6양조 완료까지 남은 시간 : %s", ComponentUtil.translate("rg255,204;%s초", Constant.Sosu2.format(brewingTime / 20D))));
          lore.add(ComponentUtil.translate("&6남은 땔감 : %s", Constant.THE_COLOR_HEX + fuelLevel));
        }
        if (blockState instanceof Campfire campfire)
        {
          lore.add(Component.empty());
          lore.add(customNameLore);
          boolean hasAtLeastOne = false;
          for (int i = 0; i < campfire.getSize(); i++)
          {
            ItemStack itemStack = campfire.getItem(i);
            if (ItemStackUtil.itemExists(itemStack))
            {
              hasAtLeastOne = true;
              int cookTime = campfire.getCookTime(i);
              int cookTimeTotal = campfire.getCookTimeTotal(i);
              Component itemStackComponent = ItemStackComponent.itemStackComponent(itemStack);
              lore.add(ComponentUtil.translate("&7%s번째 아이템 : %s, 조리 진행도 : %s / %s (%s)",
                      i + 1,
                      itemStackComponent, ComponentUtil.translate("rg255,204;%s초", Constant.Sosu2.format(cookTime / 20d)),
                      ComponentUtil.translate("&6%s초", Constant.Sosu2.format(cookTimeTotal / 20d)),
                      ComponentUtil.translate("&a%s%%", Constant.Sosu2.format(100d * cookTime / cookTimeTotal))));
            }
          }

          if (!hasAtLeastOne)
          {
            lore.remove(lore.size() - 1);
            lore.remove(lore.size() - 1);
          }
        }
        if (blockState instanceof Furnace furnace)
        {
          FurnaceInventory furnaceInventory = furnace.getInventory();
          ItemStack ingredient = furnaceInventory.getSmelting();
          String smeltType = "제련";
          if (ItemStackUtil.itemExists(ingredient) && ItemStackUtil.isEdible(ingredient.getType()))
          {
            smeltType = "조리";
          }
          int cookTimeTotal = furnace.getCookTimeTotal();
          if (cookTimeTotal != 0)
          {
            short burnTime = furnace.getBurnTime();
            short cookTime = furnace.getCookTime();
            double speed = furnace.getCookSpeedMultiplier();
            lore.add(Component.empty());
            lore.add(ComponentUtil.translate("&7땔감 지속 시간 : %s초", Constant.Sosu2.format(burnTime / 20d)));
            if (speed != 1d)
            {
              lore.add(ComponentUtil.translate("&7" + smeltType + " 진행 속도 : %s배", Constant.Sosu2.format(speed)));
            }
            lore.add(ComponentUtil.translate("&7" + smeltType + " 진행도 : %s / %s (%s)",
                    ComponentUtil.translate("%s초", Constant.Sosu2.format(cookTime / 20d)),
                    ComponentUtil.translate("%s초", Constant.Sosu2.format(cookTimeTotal / 20d)),
                    ComponentUtil.translate("%s%%", Constant.Sosu2.format(100d * cookTime / cookTimeTotal))));
          }
          if (!Method.inventoryEmpty(furnaceInventory))
          {
            lore.add(Component.empty());
            lore.add(customNameLore);
            if (ItemStackUtil.itemExists(ingredient))
            {
              Component itemStackComponent = ItemStackComponent.itemStackComponent(ingredient);
              lore.add(ComponentUtil.translate("&7" + smeltType + " 중인 아이템 : %s", itemStackComponent));
            }
            ItemStack fuel = furnaceInventory.getFuel();
            if (ItemStackUtil.itemExists(fuel))
            {
              Component itemStackComponent = ItemStackComponent.itemStackComponent(fuel);
              lore.add(ComponentUtil.translate("&7땔감 아이템 : %s", itemStackComponent));
            }
            ItemStack result = furnaceInventory.getResult();
            if (ItemStackUtil.itemExists(result))
            {
              Component itemStackComponent = ItemStackComponent.itemStackComponent(result);
              lore.add(ComponentUtil.translate("&7결과물 아이템 : %s", itemStackComponent));
            }
          }
        }
        try
        {
          if (blockState instanceof InventoryHolder inventoryHolder && !(blockState instanceof Furnace))
          {
            Inventory inventory = inventoryHolder.getInventory();
            if (!Method.inventoryEmpty(inventory))
            {
              lore.add(Component.empty());
              List<ItemStack> itemStackList = new ArrayList<>();
              for (int i = 0; i < inventory.getSize(); i++)
              {
                ItemStack itemStack = inventory.getItem(i);
                if (ItemStackUtil.itemExists(itemStack))
                {
                  itemStackList.add(itemStack);
                }
              }
              if (itemStackList.size() == 1)
              {
                lore.addAll(ItemStackUtil.getItemInfoAsComponents(itemStackList.get(0), params, customNameLore, true));
              }
              else
              {
                lore.add(customNameLore);
                for (int i = 0; i < itemStackList.size(); i++)
                {
                  if (i == 27)
                  {
                    lore.add(ComponentUtil.translate("&7&ocontainer.shulkerBox.more", Component.text(itemStackList.size() - i)));
                    break;
                  }
                  ItemStack itemStack = itemStackList.get(i);
                  lore.add(ItemStackComponent.itemStackComponent(ItemLore.removeItemLore(itemStack)));
                }
              }
            }
          }
        }
        catch (Exception e)
        {
          lore.add(Component.empty());
          lore.add(ComponentUtil.translate("&7일해라 페이퍼 오류 안고치니"));
        }
        if (blockState instanceof Beehive beehive)
        {
          customNameLore = ComponentUtil.translate(colorPrefix + "[%s의 벌들]", customName);
          int beeCount = beehive.getEntityCount();
          lore.add(Component.empty());
          lore.add(customNameLore);
          lore.add(ComponentUtil.translate("#b07c15;벌 %s", beeCount == 0 ? "없음" : "#e6ac6d;" + beeCount + "마리"));
          //Location flowerPos = beehive.getFlower(); must be placed
          NBTCompound flowerPos = blockEntityTag.getCompound("FlowerPos");
          if (flowerPos != null)
          {
            lore.add(ComponentUtil.translate("#b07c15;꽃 좌표 : %s",
                    ComponentUtil.translate("#b07c15;%s, %s, %s", "#e6ac6d;" + flowerPos.getInteger("X"), "#e6ac6d;" + flowerPos.getInteger("Y"), "#e6ac6d;" + flowerPos.getInteger("Z"))));
          }
        }
        if (blockState instanceof Lootable lootable)
        {
          LootTable lootTable = lootable.getLootTable();
          if (lootTable != null)
          {
            Long seed = blockEntityTag.getLong("LootTableSeed");
            lore.add(Component.empty());
            lore.add(ComponentUtil.translate("&7루트테이블 : %s", lootTable.getKey()));
            if (seed != null)
            {
              lore.add(ComponentUtil.translate("&7시드 : %s", seed));
            }
          }
        }
        if (blockState instanceof Jukebox jukebox)
        {
          ItemStack record = jukebox.getRecord();
          if (ItemStackUtil.itemExists(record))
          {
            lore.addAll(ItemStackUtil.getItemInfoAsComponents(record, ComponentUtil.translate("rg255,204;[음반]"), true));
          }
        }
        if (blockState instanceof CommandBlock commandBlock)
        {
          String command = commandBlock.getCommand();
          if (command.length() > 100)
          {
            command = command.substring(0, 90) + " ..." + (command.length() - 90) + "글자 더...";
          }
          Component name = commandBlock.name();
          Component lastOutput = commandBlock.lastOutput();
          int successCount = commandBlock.getSuccessCount();
          lore.add(Component.empty());
          lore.add(ComponentUtil.translate("&7이름 : %s", name));
          if (!command.equals(""))
          {
            lore.add(ComponentUtil.translate("&7명령어 : %s", Component.text(command)));
          }
          if (!lastOutput.equals(Component.empty()))
          {
            lore.add(ComponentUtil.translate("&7%s : %s", ComponentUtil.translate("advMode.previousOutput"), lastOutput));
          }
          if (successCount != 0)
          {
            lore.add(ComponentUtil.translate("&7성공 횟수 : %s", successCount));
          }
          Boolean auto = blockEntityTag.getBoolean("auto");
          if (auto != null)
          {
            lore.add(ComponentUtil.translate("&7항상 활성화 : %s", auto + ""));
          }
          Boolean powered = blockEntityTag.getBoolean("powered");
          if (powered != null)
          {
            lore.add(ComponentUtil.translate("&7활성화 여부 : %s", powered + ""));
          }
          Long lastExecution = blockEntityTag.getLong("LastExecution");
          if (lastExecution != null)
          {
            lore.add(ComponentUtil.translate("&7마지막으로 실행된 시각 : %s", lastExecution));
          }
        }
      }
    }
  }
}
