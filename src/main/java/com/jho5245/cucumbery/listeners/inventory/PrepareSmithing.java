package com.jho5245.cucumbery.listeners.inventory;

import com.jho5245.cucumbery.Cucumbery;
import com.jho5245.cucumbery.util.MessageUtil;
import com.jho5245.cucumbery.util.itemlore.ItemLore;
import com.jho5245.cucumbery.util.nbt.NBTAPI;
import com.jho5245.cucumbery.util.storage.ComponentUtil;
import com.jho5245.cucumbery.util.storage.CreateItemStack;
import com.jho5245.cucumbery.util.storage.CustomConfig;
import com.jho5245.cucumbery.util.storage.ItemStackUtil;
import com.jho5245.cucumbery.util.storage.data.Constant;
import com.jho5245.cucumbery.util.storage.data.Constant.RestrictionType;
import com.jho5245.cucumbery.util.storage.data.Permission;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.PrepareSmithingEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.SmithingInventory;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class PrepareSmithing implements Listener
{
  @EventHandler
  public void onPrepareSmithing(PrepareSmithingEvent event)
  {
    HumanEntity humanEntity = event.getView().getPlayer();
    if (humanEntity instanceof Player player)
    {
      if (!Cucumbery.config.getBoolean("grant-default-permission-to-players"))
      {
        ItemStack originalResult = event.getView().getItem(2);
        if (!ItemStackUtil.itemExists(originalResult)) // 제작 결과물 아이템이 존재하지 않으면 조합 불가 방벽을 보여주지 않음
          return;
        ItemStack result = Cucumbery.config.getBoolean("blind-result-when-crafting-with-uncraftable-item") ? null
                : CreateItemStack.newItem(Material.BARRIER, 1, Constant.NO_CRAFT_ITEM_DISPLAYNAME, "&7아이템을 조합할 권한이 없습니다.", true);
        if (!Permission.EVENT_ITEM_CRAFT.has(player))
        {
          event.getView().setItem(0, result);
          return;
        }
        Material material = originalResult.getType();
        if (!player.hasPermission(Permission.EVENT_ITEM_CRAFT + "." + material.toString().toLowerCase()))
        {
          event.getView().setItem(0, result);
          return;
        }
      }
      SmithingInventory smithingInventory = event.getInventory();
      ItemStack firstItem = smithingInventory.getItem(0), secondItem = smithingInventory.getItem(1), resultItem = smithingInventory.getItem(2);
      ItemStack restrictedItem = CreateItemStack.newItem(Material.BARRIER, 1, Constant.NO_SMITHING_ITEM_DISPLAYNAME, true);
      boolean restricted = false;
      ItemMeta itemMeta = restrictedItem.getItemMeta();
      List<String> lores = new ArrayList<>();

      boolean firstItemNoAnvil = NBTAPI.isRestricted(player, firstItem, RestrictionType.NO_SMITHING_TABLE),
              secondItemNoAnvil = NBTAPI.isRestricted(player, secondItem, RestrictionType.NO_SMITHING_TABLE);
      if (firstItemNoAnvil || secondItemNoAnvil)
      {
        lores.add(MessageUtil.n2s("&c대장장이 작업대에서 사용할 수 없는 아이템이 존재합니다."));
      }
      if (firstItemNoAnvil)
      {
        restricted = true;
        if (firstItem != null)
        {
          lores.add(MessageUtil.n2s("&e왼쪽에 있는 아이템(" + ComponentUtil.serialize(firstItem.displayName()) + "&e)"));
        }
      }
      if (secondItemNoAnvil)
      {
        restricted = true;
        if (secondItem != null)
        {
          lores.add(MessageUtil.n2s("&e오른쪽에 있는 아이템(" + ComponentUtil.serialize(secondItem.displayName()) + "&e)"));
        }
      }
      if (restricted && ItemStackUtil.itemExists(resultItem))
      {
        itemMeta.setLore(lores);
        restrictedItem.setItemMeta(itemMeta);
        if (Cucumbery.config.getBoolean("blind-result-when-using-smithing-table-with-unavailable-item"))
          event.setResult(null);
        else
          event.setResult(restrictedItem);
        return;
      }
      if (CustomConfig.UserData.USE_HELPFUL_LORE_FEATURE.getBoolean(player.getUniqueId())
              && Cucumbery.config.getBoolean("use-helpful-lore-feature"))
      {
        String worldName = player.getLocation().getWorld().getName();
        if (Cucumbery.config.getStringList("no-name-tag-on-item-spawn-worlds").contains(worldName))
          return;
        ItemStack item = event.getResult();
        if (item != null)
        {
          ItemLore.setItemLore(item);
        }
      }
    }
  }
}
