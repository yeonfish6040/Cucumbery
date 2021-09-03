package com.jho5245.cucumbery.commands.debug;

import com.jho5245.cucumbery.util.storage.ComponentUtil;
import com.jho5245.cucumbery.util.MessageUtil;
import com.jho5245.cucumbery.util.itemlore.ItemLore;
import de.tr7zw.changeme.nbtapi.NBTCompound;
import de.tr7zw.changeme.nbtapi.NBTItem;
import de.tr7zw.changeme.nbtapi.NBTList;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class CommandItemData implements CommandExecutor
{
  public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args)
  {
    if (!(sender instanceof Player player))
    {
      return true;
    }
    ItemStack item = player.getInventory().getItemInMainHand();
    Component component = ComponentUtil.itemName(item).hoverEvent(item.asHoverEvent());
    MessageUtil.sendMessage(player, "아이템 미리보기 : ", component);
    component = component.hoverEvent(HoverEvent.showText(Component.text(NBTItem.convertItemtoNBT(item).toString()))).clickEvent(ClickEvent.copyToClipboard(NBTItem.convertItemtoNBT(item).toString()));
    MessageUtil.sendMessage(player, "json : ", component);
    ItemStack clone = item.clone();
    ItemLore.removeItemLore(clone);
    component = component.hoverEvent(HoverEvent.showText(Component.text(NBTItem.convertItemtoNBT(clone).toString()))).clickEvent(ClickEvent.copyToClipboard(NBTItem.convertItemtoNBT(clone).toString()));
    MessageUtil.sendMessage(player, "json (TMI제거) : ", component);
    clone = item.clone();
    NBTItem nbtItem = new NBTItem(clone);
    for (String key : nbtItem.getKeys())
    {
      if (!key.equals("display"))
      {
        nbtItem.removeKey(key);
      }
    }
    clone = nbtItem.getItem();
    component = component.hoverEvent(HoverEvent.showText(Component.text(NBTItem.convertItemtoNBT(clone).toString()))).clickEvent(ClickEvent.copyToClipboard(NBTItem.convertItemtoNBT(clone).toString()));
    MessageUtil.sendMessage(player, "json (display만) : ", component);
    clone = item.clone();
    nbtItem = new NBTItem(clone);
    nbtItem.removeKey("display");
    clone = nbtItem.getItem();
    component = component.hoverEvent(HoverEvent.showText(Component.text(NBTItem.convertItemtoNBT(clone).toString()))).clickEvent(ClickEvent.copyToClipboard(NBTItem.convertItemtoNBT(clone).toString()));
    MessageUtil.sendMessage(player, "json (display 제거) : ", component);
    ItemMeta itemMeta = item.getItemMeta();
    if (itemMeta.hasDisplayName() || itemMeta.hasLore())
    {
      clone = item.clone();
      nbtItem = new NBTItem(clone);
      NBTCompound display = nbtItem.getCompound("display");
      NBTList<String> lore = display.getStringList("Lore");
      Component nameComponent = itemMeta.displayName();
      List<Component> loreComponent = itemMeta.lore();
      if (nameComponent != null)
      {
        display.setString("Name", "{\"text\":\"" + MessageUtil.n2s(ComponentUtil.serialize(nameComponent).replace("\\", "\\\\").replace("\"", "\\\"") + "&f") + "\"}");
      }
      if (loreComponent != null)
      {
        for (int i = 0; i < loreComponent.size(); i++)
        {
          Component lorComponent = loreComponent.get(i);
          lore.set(i, "{\"text\":\"" + MessageUtil.n2s(ComponentUtil.serialize(lorComponent).replace("\\", "\\\\").replace("\"", "\\\"") + "&f") + "\"}");
        }
      }
      clone = nbtItem.getItem();
      component = component.hoverEvent(HoverEvent.showText(ComponentUtil.create(NBTItem.convertItemtoNBT(clone).toString()))).clickEvent(ClickEvent.copyToClipboard(NBTItem.convertItemtoNBT(clone).toString()));
      MessageUtil.sendMessage(player, "json (display 콤팩트) : ", component);
    }
    return true;
  }
}
