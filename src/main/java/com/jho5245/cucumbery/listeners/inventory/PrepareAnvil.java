package com.jho5245.cucumbery.listeners.inventory;

import com.jho5245.cucumbery.Cucumbery;
import com.jho5245.cucumbery.util.MessageUtil;
import com.jho5245.cucumbery.util.Method;
import com.jho5245.cucumbery.util.itemlore.ItemLore;
import com.jho5245.cucumbery.util.nbt.CucumberyTag;
import com.jho5245.cucumbery.util.nbt.NBTAPI;
import com.jho5245.cucumbery.util.storage.component.util.ComponentUtil;
import com.jho5245.cucumbery.util.storage.CreateItemStack;
import com.jho5245.cucumbery.util.storage.ItemStackUtil;
import com.jho5245.cucumbery.util.storage.data.Constant;
import com.jho5245.cucumbery.util.storage.data.Constant.CustomEnchant;
import com.jho5245.cucumbery.util.storage.data.Constant.RestrictionType;
import de.tr7zw.changeme.nbtapi.NBTCompound;
import de.tr7zw.changeme.nbtapi.NBTItem;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.PrepareAnvilEvent;
import org.bukkit.inventory.AnvilInventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class PrepareAnvil implements Listener
{
  @EventHandler
  public void onPrepareAnvil(PrepareAnvilEvent event)
  {
    String title = event.getView().getTitle();
    if (!title.equals("Repair & Name"))
    {
      return;
    }
    HumanEntity humanEntity = event.getView().getPlayer();
    if (humanEntity instanceof Player player)
    {
      AnvilInventory anvilInventory = event.getInventory();
      ItemStack firstItem = anvilInventory.getItem(0), secondItem = anvilInventory.getItem(1), resultItem = anvilInventory.getItem(2);
      String firstItemName = null;
      if (firstItem != null)
      {
        firstItemName = ComponentUtil.serialize(firstItem.displayName());
      }
      String secondItemName = null;
      if (secondItem != null)
      {
        secondItemName = ComponentUtil.serialize(secondItem.displayName());
      }
      String resultItemName = null;
      if (resultItem != null)
      {
        resultItemName = ComponentUtil.serialize(resultItem.displayName());
      }
      ItemStack restrictedItem = CreateItemStack.newItem(Material.BARRIER, 1, Constant.NO_ANVIL_ITEM_DISPLAYNAME, true);
      boolean restricted = false;
      ItemMeta itemMeta = restrictedItem.getItemMeta();
      List<String> lores = new ArrayList<>();

      boolean firstItemNoAnvil = NBTAPI.isRestricted(player, firstItem, RestrictionType.NO_ANVIL), secondItemNoAnvil = NBTAPI.isRestricted(player, secondItem, RestrictionType.NO_ANVIL);
      if (firstItemNoAnvil || secondItemNoAnvil)
      {
        lores.add(MessageUtil.n2s("&c모루에서 사용할 수 없는 아이템이 존재합니다"));
      }
      if (firstItemNoAnvil)
      {
        restricted = true;
        lores.add(MessageUtil.n2s("&e왼쪽에 있는 아이템(" + firstItemName + "&e)"));
      }
      if (secondItemNoAnvil)
      {
        restricted = true;
        lores.add(MessageUtil.n2s("&e오른쪽에 있는 아이템(" + secondItemName + "&e)"));
      }

      boolean firstItemNoUseEnchantedBookEnchant = NBTAPI.isRestricted(player, firstItem, RestrictionType.NO_ANVIL_ENCHANTED_BOOK_ENCHANT), secondItemNoUseEnchantedBookEnchant = NBTAPI
              .isRestricted(player, secondItem, RestrictionType.NO_ANVIL_ENCHANTED_BOOK_ENCHANT);
      if ((firstItemNoUseEnchantedBookEnchant || secondItemNoUseEnchantedBookEnchant) && ItemStackUtil.itemExists(firstItem) && firstItem.getType() != Material.ENCHANTED_BOOK && ItemStackUtil.itemExists(secondItem) && secondItem.getType() == Material.ENCHANTED_BOOK)
      {
        restricted = true;
        lores.add(MessageUtil.n2s("&c모루에서 마법이 부여된 책으로 마법을 부여할 수 없는 아이템이 존재합니다"));
        if (firstItemNoUseEnchantedBookEnchant)
        {
          lores.add(MessageUtil.n2s("&e왼쪽에 있는 아이템(" + firstItemName + "&e)"));
        }
        if (secondItemNoUseEnchantedBookEnchant)
        {
          lores.add(MessageUtil.n2s("&e오른쪽에 있는 아이템(" + secondItemName + "&e)"));
        }
      }

      boolean firstItemNoComposition = NBTAPI.isRestricted(player, firstItem, RestrictionType.NO_ANVIL_COMPOSITION), secondItemNoComposition = NBTAPI
              .isRestricted(player, secondItem, RestrictionType.NO_ANVIL_COMPOSITION);
      if ((firstItemNoComposition || secondItemNoComposition) && ItemStackUtil.itemExists(firstItem) && ItemStackUtil.itemExists(secondItem) && firstItem.getType() != Material.ENCHANTED_BOOK && firstItem
              .getType() == secondItem.getType())
      {
        restricted = true;
        lores.add(MessageUtil.n2s("&c모루에서 같은 종류의 아이템끼리 합성할 수 없는 아이템이 존재합니다"));
        if (firstItemNoComposition)
        {
          lores.add(MessageUtil.n2s("&e왼쪽에 있는 아이템(" + firstItemName + "&e)"));
        }
        if (secondItemNoComposition)
        {
          lores.add(MessageUtil.n2s("&e오른쪽에 있는 아이템(" + secondItemName + "&e)"));
        }
      }

      if (NBTAPI.isRestricted(player, firstItem, RestrictionType.NO_ANVIL_COMPOSITION_LEFT))
      {
        if (NBTAPI.getCustomEnchantLevel(firstItem, CustomEnchant.ENCHANT_CURSE) > 0 ||
                (Objects.requireNonNull(firstItem).getType() != Material.ENCHANTED_BOOK && ItemStackUtil.itemExists(secondItem) && firstItem.getType() == secondItem.getType()))
        {
          restricted = true;
          lores.add(MessageUtil.n2s("&c(더 깔끔한 설명 필요) 모루에서 왼쪽에 있는 아이템 같은 종류의 아이템끼리 합성할 수 없는 아이템이 존재합니다"));
          lores.add(MessageUtil.n2s("&e왼쪽에 있는 아이템(" + firstItemName + "&e)"));
        }
      }

      if (NBTAPI.isRestricted(player, secondItem, RestrictionType.NO_ANVIL_COMPOSITION_RIGHT))
      {
        if (
                NBTAPI.getCustomEnchantLevel(secondItem, CustomEnchant.ENCHANT_CURSE) > 0 ||
                        (Objects.requireNonNull(secondItem).getType() != Material.ENCHANTED_BOOK && ItemStackUtil.itemExists(firstItem) && firstItem.getType() == secondItem.getType()))
        {
          restricted = true;
          lores.add(MessageUtil.n2s("&c(더 깔끔한 설명 필요) 모루에서 오른쪽에 있는 아이템 같은 종류의 아이템끼리 합성할 수 없는 아이템이 존재합니다"));
          lores.add(MessageUtil.n2s("&e오른쪽에 있는 아이템(" + secondItemName + "&e)"));
        }
      }

      boolean firstItemNoUseEnchantedBookComposition = ItemStackUtil.itemExists(firstItem) && firstItem.getType() == Material.ENCHANTED_BOOK && NBTAPI
              .isRestricted(player, firstItem, RestrictionType.NO_ANVIL_ENCHANTED_BOOK_COMPOSITION), secondItemNoUseEnchantedBookComposition = ItemStackUtil.itemExists(secondItem) && secondItem
              .getType() == Material.ENCHANTED_BOOK && NBTAPI.isRestricted(player, secondItem, RestrictionType.NO_ANVIL_ENCHANTED_BOOK_COMPOSITION);
      if (firstItemNoUseEnchantedBookComposition || secondItemNoUseEnchantedBookComposition)
      {
        lores.add(MessageUtil.n2s("&c모루에서 마법이 부여된 책끼리 합성할 수 없는 아이템이 존재합니다"));
      }
      if (firstItemNoUseEnchantedBookComposition)
      {
        restricted = true;
        lores.add(MessageUtil.n2s("&e왼쪽에 있는 아이템(" + firstItemName + "&e)"));
      }
      if (secondItemNoUseEnchantedBookComposition)
      {
        restricted = true;
        lores.add(MessageUtil.n2s("&e오른쪽에 있는 아이템(" + secondItemName + "&e)"));
      }

      if (NBTAPI.isRestricted(player, firstItem, RestrictionType.NO_ANVIL_ENCHANTED_BOOK_COMPOSITION_LEFT))
      {
        if (Objects.requireNonNull(firstItem).getType() == Material.ENCHANTED_BOOK && ItemStackUtil.itemExists(secondItem) && firstItem.getType() == secondItem.getType())
        {
          restricted = true;
          lores.add(MessageUtil.n2s("&c(더 깔끔한 설명 필요) 모루에서 왼쪽에 있는 마법이 부여된 책끼리 합성할 수 없는 아이템이 존재합니다"));
          lores.add(MessageUtil.n2s("&e왼쪽에 있는 아이템(" + firstItemName + "&e)"));
        }
      }

      if (NBTAPI.isRestricted(player, secondItem, RestrictionType.NO_ANVIL_ENCHANTED_BOOK_COMPOSITION_RIGHT))
      {
        if (Objects.requireNonNull(secondItem).getType() == Material.ENCHANTED_BOOK && ItemStackUtil.itemExists(firstItem) && firstItem.getType() == secondItem.getType())
        {
          restricted = true;
          lores.add(MessageUtil.n2s("&c(더 깔끔한 설명 필요) 모루에서 오른쪽에 있는 마법이 부여된 책끼리 합성할 수 없는 아이템이 존재합니다"));
          lores.add(MessageUtil.n2s("&e오른쪽에 있는 아이템(" + secondItemName + "&e)"));
        }
      }

      if (NBTAPI.isRestricted(player, firstItem, RestrictionType.NO_ANVIL_RENAME))
      {
        if (ItemStackUtil.hasDisplayName(firstItem))
        {
          firstItemName = Objects.requireNonNull(firstItem).getItemMeta().getDisplayName();
        }
        if (ItemStackUtil.hasDisplayName(resultItem))
        {
          resultItemName = Objects.requireNonNull(resultItem).getItemMeta().getDisplayName();
        }
        if (!firstItemName.equals(resultItemName))
        {
          restricted = true;
          lores.add(MessageUtil.n2s("&c모루에서 이름을 바꿀 수 없는 아이템이 존재합니다"));
          lores.add(MessageUtil.n2s("&e왼쪽에 있는 아이템(" + firstItemName + "&e)"));
        }
      }

      if (NBTAPI.isRestricted(player, firstItem, RestrictionType.NO_ANVIL_ENCHANT) && ItemStackUtil.itemExists(resultItem))
      {
        ItemMeta firstItemMeta = Objects.requireNonNull(firstItem).getItemMeta();
        ItemMeta resultItemMeta = resultItem.getItemMeta();

        if (!firstItemMeta.getEnchants().equals(resultItemMeta.getEnchants()))
        {
          restricted = true;
          lores.add(MessageUtil.n2s("&c모루에서 마법을 부여할 수 없는 아이템이 존재합니다"));
          lores.add(MessageUtil.n2s("&e왼쪽에 있는 아이템(" + firstItemName + "&e)"));
        }
      }

      boolean firstItemNoRepair = (NBTAPI.isRestricted(player, firstItem, RestrictionType.NO_ANVIL_REPAIR));
      boolean secondItemNoRepair = (NBTAPI.isRestricted(player, secondItem, RestrictionType.NO_ANVIL_REPAIR));
      if ((firstItemNoRepair || secondItemNoRepair) && ItemStackUtil.itemExists(resultItem))
      {
        int firstItemDamage = 0, secondItemDamage = 0;
        if (firstItemNoRepair)
        {
          firstItemDamage = ((Damageable) Objects.requireNonNull(firstItem).getItemMeta()).getDamage();
        }
        if (secondItemNoRepair)
        {
          secondItemDamage = ((Damageable) Objects.requireNonNull(secondItem).getItemMeta()).getDamage();
        }
        Damageable resultItemDuraMeta = (Damageable) resultItem.getItemMeta();
        int resultItemDamage = resultItemDuraMeta.getDamage();
        if ((firstItemNoRepair && firstItemDamage != resultItemDamage) || (secondItemNoRepair && secondItemDamage != resultItemDamage))
        {
          restricted = true;
          lores.add(MessageUtil.n2s("&c모루에서 수리할 수 없는 아이템이 존재합니다"));
          if (firstItemNoRepair)
          {
            lores.add(MessageUtil.n2s("&e왼쪽에 있는 아이템(" + firstItemName + "&e)"));
          }
          if (secondItemNoRepair)
          {
            lores.add(MessageUtil.n2s("&e오른쪽에 있는 아이템(" + secondItemName + "&e)"));
          }
        }
      }
      if (restricted && ItemStackUtil.itemExists(resultItem))
      {
        itemMeta.setLore(lores);
        restrictedItem.setItemMeta(itemMeta);
        if (Cucumbery.config.getBoolean("blind-result-when-using-anvil-with-unavailable-item"))
        {
          event.setResult(null);
        }
        else
        {
          event.setResult(restrictedItem);
        }
        return;
      }
      int maxRepairCost = Cucumbery.config.getInt("max-anvil-repair-cost");
      if (maxRepairCost > -1)
      {
        anvilInventory.setMaximumRepairCost(maxRepairCost);
      }
      NBTCompound itemTag = NBTAPI.getMainCompound(firstItem);
      NBTCompound duraTag = NBTAPI.getCompound(itemTag, CucumberyTag.CUSTOM_DURABILITY_KEY);
      if (ItemStackUtil.itemExists(resultItem) && duraTag != null)
      {
        long maxDura = duraTag.getLong(CucumberyTag.CUSTOM_DURABILITY_MAX_KEY);
        long curDura = duraTag.getLong(CucumberyTag.CUSTOM_DURABILITY_CURRENT_KEY);
        int firstItemMaxOriginMaxDura = Objects.requireNonNull(firstItem).getType().getMaxDurability();
        int firstItemOriginDura = firstItem.getType().getMaxDurability() - ((Damageable) firstItem.getItemMeta()).getDamage();
        int resultDamage = ((Damageable) resultItem.getItemMeta()).getDamage();
        int resultItemOriginDura = resultItem.getType().getMaxDurability() - resultDamage;
        double ratio = (resultItemOriginDura - firstItemOriginDura * 1d) / firstItemMaxOriginMaxDura;
        NBTCompound secondItemDuraTag = NBTAPI.getCompound(NBTAPI.getMainCompound(secondItem), CucumberyTag.CUSTOM_DURABILITY_KEY);
        if (secondItemDuraTag != null)
        {
          long secondItemcurDura = secondItemDuraTag.getLong(CucumberyTag.CUSTOM_DURABILITY_CURRENT_KEY);
          curDura += secondItemcurDura;
          curDura += maxDura * 0.1;
        }
        else if (secondItem != null && firstItem.getType() == secondItem.getType())
        {
          int fixAmount = firstItemMaxOriginMaxDura - ((Damageable) secondItem.getItemMeta()).getDamage();
          curDura += fixAmount;
          curDura += maxDura * 0.1;
        }
        else
        {
          long fixAmount = resultItemOriginDura == resultItem.getType().getMaxDurability() ? (maxDura - curDura) : (long) Math.min(ratio * maxDura, maxDura);
          curDura += fixAmount;
        }
        if (curDura > maxDura)
        {
          curDura = maxDura;
        }
        NBTItem nbtItem = new NBTItem(resultItem);
        NBTCompound resultItemTag = nbtItem.getCompound(CucumberyTag.KEY_MAIN);
        NBTCompound resultDuraTag = NBTAPI.getCompound(resultItemTag, CucumberyTag.CUSTOM_DURABILITY_KEY);
        if (resultDuraTag != null)
        {
          resultDuraTag.setLong(CucumberyTag.CUSTOM_DURABILITY_CURRENT_KEY, curDura);
        }
        resultItem = nbtItem.getItem();
        Damageable duraMeta = (Damageable) resultItem.getItemMeta();
        duraMeta.setDamage(resultDamage);
        resultItem.setItemMeta(duraMeta);
        ItemLore.setItemLore(resultItem);
        event.setResult(resultItem);
      }
      else if (Method.usingLoreFeature(player))
      {
        ItemStack item = event.getResult();
        if (ItemStackUtil.itemExists(item))
        {
          ItemLore.setItemLore(item);
        }
      }
    }
  }
}
