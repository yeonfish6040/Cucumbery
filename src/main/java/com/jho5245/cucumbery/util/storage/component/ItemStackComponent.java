package com.jho5245.cucumbery.util.storage.component;

import com.jho5245.cucumbery.util.storage.component.util.ComponentUtil;
import com.jho5245.cucumbery.util.storage.component.util.ItemNameUtil;
import de.tr7zw.changeme.nbtapi.NBTItem;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class ItemStackComponent
{
  @NotNull
  public static Component itemStackComponent(@NotNull ItemStack itemStack)
  {
    return itemStackComponent(itemStack, itemStack.getAmount(), null);
  }

  @NotNull
  @SuppressWarnings("unused")
  public static Component itemStackComponent(@NotNull ItemStack itemStack, int amount)
  {
    return itemStackComponent(itemStack, amount, null);
  }

  @NotNull
  public static Component itemStackComponent(@NotNull ItemStack itemStack, @Nullable TextColor defaultColor)
  {
    return itemStackComponent(itemStack, itemStack.getAmount(), defaultColor);
  }

  @NotNull
  public static Component itemStackComponent(@NotNull ItemStack itemStack, int amount, @Nullable TextColor defaultColor)
  {
    if (defaultColor == null)
    {
      defaultColor = NamedTextColor.GRAY;
    }
    itemStack = itemStack.clone();
    NBTItem nbtItem = new NBTItem(itemStack);
    nbtItem.removeKey("BlockEntityTag");
    nbtItem.removeKey("BlockStateTag");
    itemStack.setItemMeta(nbtItem.getItem().getItemMeta());
    Component itemName = ItemNameUtil.itemName(itemStack, defaultColor);
    if (itemName instanceof TextComponent textComponent && textComponent.content().equals(""))
    {
      List<Component> children = new ArrayList<>(itemName.children());
      for (int i = 0; i < children.size(); i++)
      {
        Component child = children.get(i).hoverEvent(itemStack.asHoverEvent());
        children.set(i, child);
      }
      itemName = itemName.children(children);
    }
    else
    {
      itemName = itemName.hoverEvent(itemStack.asHoverEvent());
    }
    if (amount == 1 && itemStack.getType().getMaxStackSize() == 1)
    {
      return itemName;
    }

    return ComponentUtil.translate("&o&q%s %s", itemName, ComponentUtil.translate("%s개", Component.text(amount)).color(defaultColor)).color(itemName.color()).hoverEvent(itemStack.asHoverEvent());
  }
}
