package com.jho5245.cucumbery.listeners.inventory;

import com.jho5245.cucumbery.Cucumbery;
import com.jho5245.cucumbery.util.MessageUtil;
import com.jho5245.cucumbery.util.Method;
import com.jho5245.cucumbery.util.itemlore.ItemLore;
import com.jho5245.cucumbery.util.nbt.NBTAPI;
import com.jho5245.cucumbery.util.storage.component.util.ComponentUtil;
import com.jho5245.cucumbery.util.storage.CreateItemStack;
import com.jho5245.cucumbery.util.storage.CustomConfig.UserData;
import com.jho5245.cucumbery.util.storage.ItemStackUtil;
import com.jho5245.cucumbery.util.storage.data.Constant;
import com.jho5245.cucumbery.util.storage.data.Constant.RestrictionType;
import com.jho5245.cucumbery.util.storage.data.Permission;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class PrepareItemCraft implements Listener
{
  @EventHandler
  public void onPrepareItemCraft(PrepareItemCraftEvent event)
  {
    HumanEntity humanEntity = event.getView().getPlayer();
    if (humanEntity.getType() == EntityType.PLAYER)
    {
      Player player = (Player) humanEntity;
      if (!Cucumbery.config.getBoolean("grant-default-permission-to-players"))
      {
        ItemStack originalResult = event.getView().getItem(0);
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
      if (!UserData.EVENT_EXCEPTION_ACCESS.getBoolean(player.getUniqueId()))
      {
        ItemStack originalResult = event.getView().getItem(0);
        if (!ItemStackUtil.itemExists(originalResult)) // 제작 결과물 아이템이 존재하지 않으면 조합 불가 방벽을 보여주지 않음
          return;
        ItemStack result = CreateItemStack.newItem(Material.BARRIER, 1, Constant.NO_CRAFT_ITEM_DISPLAYNAME, MessageUtil.n2s("&c조합 재료 아이템들 중에서 조합이 불가능한 아이템이 존재합니다."), true);
        ItemMeta itemMeta = result.getItemMeta();
        List<String> lores = itemMeta.getLore();
        if (lores == null)
          lores = new ArrayList<>();
        if (event.getInventory().getType() == InventoryType.CRAFTING)
        {
          ItemStack item1 = event.getView().getItem(1);
          ItemStack item2 = event.getView().getItem(2);
          ItemStack item3 = event.getView().getItem(3);
          ItemStack item4 = event.getView().getItem(4);
          boolean yes = false;
          if (NBTAPI.isRestricted(item1, RestrictionType.NO_CRAFT) || NBTAPI.isRestricted(item1, RestrictionType.NO_CRAFT_IN_INVENTORY))
          {
            yes = true;
            if (item1 != null)
            {
              lores.add(MessageUtil.n2s("&e왼쪽 윗칸 아이템 (" + ComponentUtil.serialize(item1.displayName()) + "&e)"));
            }
          }
          if (NBTAPI.isRestricted(item2, RestrictionType.NO_CRAFT) || NBTAPI.isRestricted(item2, RestrictionType.NO_CRAFT_IN_INVENTORY))
          {
            yes = true;
            if (item2 != null)
            {
              lores.add(MessageUtil.n2s("&e오른쪽 윗칸 아이템 (" + ComponentUtil.serialize(item2.displayName()) + "&e)"));
            }
          }
          if (NBTAPI.isRestricted(item3, RestrictionType.NO_CRAFT) || NBTAPI.isRestricted(item3, RestrictionType.NO_CRAFT_IN_INVENTORY))
          {
            yes = true;
            if (item3 != null)
            {
              lores.add(MessageUtil.n2s("&e왼쪽 아랫칸 아이템 (" + ComponentUtil.serialize(item3.displayName()) + "&e)"));
            }
          }
          if (NBTAPI.isRestricted(item4, RestrictionType.NO_CRAFT) || NBTAPI.isRestricted(item4, RestrictionType.NO_CRAFT_IN_INVENTORY))
          {
            yes = true;
            if (item4 != null)
            {
              lores.add(MessageUtil.n2s("&e오른쪽 아랫칸 아이템 (" + ComponentUtil.serialize(item4.displayName()) + "&e)"));
            }
          }
          if (yes)
          {
            itemMeta.setLore(lores);
            result.setItemMeta(itemMeta);
            if (Cucumbery.config.getBoolean("blind-result-when-crafting-with-uncraftable-item"))
              event.getView().setItem(0, null);
            else
              event.getView().setItem(0, result);
            return;
          }
        }
        else if (event.getInventory().getType() == InventoryType.WORKBENCH)
        {
          ItemStack item1 = event.getView().getItem(1);
          ItemStack item2 = event.getView().getItem(2);
          ItemStack item3 = event.getView().getItem(3);
          ItemStack item4 = event.getView().getItem(4);
          ItemStack item5 = event.getView().getItem(5);
          ItemStack item6 = event.getView().getItem(6);
          ItemStack item7 = event.getView().getItem(7);
          ItemStack item8 = event.getView().getItem(8);
          ItemStack item9 = event.getView().getItem(9);
          boolean yes = false;
          if (NBTAPI.isRestricted(item1, RestrictionType.NO_CRAFT) || NBTAPI.isRestricted(item1, RestrictionType.NO_CRAFT_IN_CRAFTING_TABLE))
          {
            yes = true;
            if (item1 != null)
            {
              lores.add(MessageUtil.n2s("&e왼쪽 윗칸 아이템 (" + ComponentUtil.serialize(item1.displayName()) + "&e)"));
            }
          }
          if (NBTAPI.isRestricted(item2, RestrictionType.NO_CRAFT) || NBTAPI.isRestricted(item2, RestrictionType.NO_CRAFT_IN_CRAFTING_TABLE))
          {
            yes = true;
            if (item2 != null)
            {
              lores.add(MessageUtil.n2s("&e중앙 윗칸 아이템 (" + ComponentUtil.serialize(item2.displayName()) + "&e)"));
            }
          }
          if (NBTAPI.isRestricted(item3, RestrictionType.NO_CRAFT) || NBTAPI.isRestricted(item3, RestrictionType.NO_CRAFT_IN_CRAFTING_TABLE))
          {
            yes = true;
            if (item3 != null)
            {
              lores.add(MessageUtil.n2s("&e오른쪽 윗칸 아이템 (" + ComponentUtil.serialize(item3.displayName()) + "&e)"));
            }
          }
          if (NBTAPI.isRestricted(item4, RestrictionType.NO_CRAFT) || NBTAPI.isRestricted(item4, RestrictionType.NO_CRAFT_IN_CRAFTING_TABLE))
          {
            yes = true;
            if (item4 != null)
            {
              lores.add(MessageUtil.n2s("&e왼쪽 중앙 아이템 (" + ComponentUtil.serialize(item4.displayName()) + "&e)"));
            }
          }
          if (NBTAPI.isRestricted(item5, RestrictionType.NO_CRAFT) || NBTAPI.isRestricted(item5, RestrictionType.NO_CRAFT_IN_CRAFTING_TABLE))
          {
            yes = true;
            if (item5 != null)
            {
              lores.add(MessageUtil.n2s("&e중앙 아이템 (" + ComponentUtil.serialize(item5.displayName()) + "&e)"));
            }
          }
          if (NBTAPI.isRestricted(item6, RestrictionType.NO_CRAFT) || NBTAPI.isRestricted(item6, RestrictionType.NO_CRAFT_IN_CRAFTING_TABLE))
          {
            yes = true;
            if (item6 != null)
            {
              lores.add(MessageUtil.n2s("&e오른쪽 중앙 아이템 (" + ComponentUtil.serialize(item6.displayName()) + "&e)"));
            }
          }
          if (NBTAPI.isRestricted(item7, RestrictionType.NO_CRAFT) || NBTAPI.isRestricted(item7, RestrictionType.NO_CRAFT_IN_CRAFTING_TABLE))
          {
            yes = true;
            if (item7 != null)
            {
              lores.add(MessageUtil.n2s("&e왼쪽 아랫칸 아이템 (" + ComponentUtil.serialize(item7.displayName()) + "&e)"));
            }
          }
          if (NBTAPI.isRestricted(item8, RestrictionType.NO_CRAFT) || NBTAPI.isRestricted(item8, RestrictionType.NO_CRAFT_IN_CRAFTING_TABLE))
          {
            yes = true;
            if (item8 != null)
            {
              lores.add(MessageUtil.n2s("&e중앙 아랫칸 아이템 (" + ComponentUtil.serialize(item8.displayName()) + "&e)"));
            }
          }
          if (NBTAPI.isRestricted(item9, RestrictionType.NO_CRAFT) || NBTAPI.isRestricted(item9, RestrictionType.NO_CRAFT_IN_CRAFTING_TABLE))
          {
            yes = true;
            if (item9 != null)
            {
              lores.add(MessageUtil.n2s("&e오른쪽 아랫칸 아이템 (" + ComponentUtil.serialize(item9.displayName()) + "&e)"));
            }
          }
          if (yes)
          {
            itemMeta.setLore(lores);
            result.setItemMeta(itemMeta);
            if (Cucumbery.config.getBoolean("blind-result-when-crafting-with-uncraftable-item"))
              event.getView().setItem(0, null);
            else
              event.getView().setItem(0, result);
            return;
          }
        }
      }
      this.itemLore(event, player);
    }
  }

  private void itemLore(PrepareItemCraftEvent event, Player player)
  {
    if (!Method.usingLoreFeature(player))
      return;
    ItemStack result = event.getView().getItem(0);
    if (!ItemStackUtil.itemExists(result))
      return;
    ItemLore.setItemLore(result, event);
  }
}
