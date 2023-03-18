package com.jho5245.cucumbery.util.storage.component;

import com.jho5245.cucumbery.util.itemlore.ItemLore;
import com.jho5245.cucumbery.util.storage.component.util.ComponentUtil;
import com.jho5245.cucumbery.util.storage.component.util.ItemNameUtil;
import de.tr7zw.changeme.nbtapi.NBTItem;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BundleMeta;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class ItemStackComponent
{
  @NotNull
  public static Component itemStackComponent(@NotNull ItemStack itemStack)
  {
    return itemStackComponent(itemStack, itemStack.getAmount(), NamedTextColor.GRAY, true);
  }

  @NotNull
  @SuppressWarnings("unused")
  public static Component itemStackComponent(@NotNull ItemStack itemStack, int amount)
  {
    return itemStackComponent(itemStack, amount, NamedTextColor.GRAY, true);
  }

  @NotNull
  public static Component itemStackComponent(@NotNull ItemStack itemStack, @Nullable TextColor defaultColor)
  {
    return itemStackComponent(itemStack, itemStack.getAmount(), defaultColor, true);
  }

  @NotNull
  public static Component itemStackComponent(@NotNull ItemStack itemStack, int amount, @Nullable TextColor defaultColor, boolean showAmount)
  {
    final ItemStack giveItem = ItemLore.removeItemLore(itemStack.clone());
    itemStack = itemStack.clone();
    itemStack.setAmount(Math.max(1, Math.min(64, itemStack.getAmount())));
    Component itemName = ItemNameUtil.itemName(itemStack, showAmount ? defaultColor : null, true);
    ItemStack hover = new ItemStack(Material.BUNDLE);
    BundleMeta bundleMeta = (BundleMeta) hover.getItemMeta();
    bundleMeta.addItem(itemStack);
    bundleMeta.addItemFlags(ItemFlag.HIDE_ITEM_SPECIFICS);
    bundleMeta.displayName(ItemNameUtil.itemName(itemStack));
    List<Component> lore = itemStack.getItemMeta().lore();
    bundleMeta.lore(lore);
    hover.setItemMeta(bundleMeta);
    NBTItem nbtItem = new NBTItem(hover, true);
    nbtItem.setString("test", "test");
    if (itemName instanceof TextComponent textComponent && textComponent.content().equals(""))
    {
      List<Component> children = new ArrayList<>(itemName.children());
      children.replaceAll(component -> component.hoverEvent(hover.asHoverEvent()));
      itemName = itemName.children(children);
    }
    else
    {
      itemName = itemName.hoverEvent(hover.asHoverEvent());
    }
    String nbt = new NBTItem(giveItem).toString();
    if (nbt.equals("{}"))
    {
      nbt = "";
    }
    String giveCommand = "/give";
    ClickEvent clickEvent = ClickEvent.copyToClipboard(giveCommand + " @p minecraft:" + itemStack.getType().toString().toLowerCase() + nbt);
    if (!showAmount || (amount == 1 && itemStack.getType().getMaxStackSize() == 1))
    {
      return itemName.clickEvent(clickEvent);
    }
    return ComponentUtil.translate("&i%s %s", itemName, ComponentUtil.translate("%s개", amount))
            .color(defaultColor)
            .hoverEvent(hover.asHoverEvent())
            .clickEvent(clickEvent);
  }
}
