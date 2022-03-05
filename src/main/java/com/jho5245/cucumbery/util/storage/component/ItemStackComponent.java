package com.jho5245.cucumbery.util.storage.component;

import com.jho5245.cucumbery.Cucumbery;
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
    NBTItem nbtItem = new NBTItem(itemStack);
    int tagSize = nbtItem.getKeys().size();
    nbtItem.removeKey("BlockEntityTag");
    nbtItem.removeKey("BlockStateTag");
    itemStack.setItemMeta(nbtItem.getItem().getItemMeta());
    Component itemName = ItemNameUtil.itemName(itemStack, showAmount ? defaultColor : null);
    ItemStack hover = new ItemStack(Material.BUNDLE);
    BundleMeta bundleMeta = (BundleMeta) hover.getItemMeta();
    //   ItemStack frame = ItemSerializer.deserialize("{id:light_gray_stained_glass_pane,Count:1,tag:{CustomModelData:5248}}");
    //   bundleMeta.addItem(frame);
    bundleMeta.addItem(itemStack);
    bundleMeta.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
    bundleMeta.displayName(ItemNameUtil.itemName(itemStack));
    List<Component> lore = itemStack.getItemMeta().lore();
    if (lore == null)
    {
      lore = new ArrayList<>();
    }
   // lore.add(Component.text("minecraft:" + itemStack.getType().toString().toLowerCase(), NamedTextColor.DARK_GRAY));
    if (tagSize > 0)
    {
    //  lore.add(ComponentUtil.translate("&8item.nbt_tags", tagSize));
    }
    //lore.add(ComponentUtil.create(Constant.SEPARATOR));
    bundleMeta.lore(lore);
    hover.setItemMeta(bundleMeta);
    if (itemName instanceof TextComponent textComponent && textComponent.content().equals(""))
    {
      List<Component> children = new ArrayList<>(itemName.children());
      for (int i = 0; i < children.size(); i++)
      {
        Component child = children.get(i).hoverEvent(hover.asHoverEvent());
        children.set(i, child);
      }
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
    String giveCommand = Cucumbery.using_CommandAPI ? "/cgive" : "/give";
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
