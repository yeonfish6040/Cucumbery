package com.jho5245.cucumbery.listeners.entity.item;

import com.jho5245.cucumbery.Cucumbery;
import com.jho5245.cucumbery.util.MessageUtil;
import com.jho5245.cucumbery.util.Method;
import com.jho5245.cucumbery.util.itemlore.ItemLore;
import com.jho5245.cucumbery.util.nbt.CucumberyTag;
import com.jho5245.cucumbery.util.nbt.NBTAPI;
import com.jho5245.cucumbery.util.storage.ComponentUtil;
import com.jho5245.cucumbery.util.storage.ItemStackUtil;
import com.jho5245.cucumbery.util.storage.data.Constant.CucumberyHideFlag;
import com.jho5245.cucumbery.util.storage.data.Constant.RestrictionType;
import de.tr7zw.changeme.nbtapi.NBTList;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Item;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ItemMergeEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Objects;

public class ItemMerge implements Listener
{
  @EventHandler
  @SuppressWarnings("all")
  public void onItemMerge(ItemMergeEvent event)
  {
    if (event.isCancelled())
    {
      return;
    }

    Item entity = event.getEntity();
    Item target = event.getTarget();
    if (Cucumbery.config.getBoolean("glow-dropped-items"))
    {
      entity.setGlowing(true);
      target.setGlowing(true);
    }
    ItemStack item1 = entity.getItemStack(), item2 = target.getItemStack();
    if (NBTAPI.isRestricted(item1, RestrictionType.NO_MERGE))
    {
      event.setCancelled(true);
      return;
    }
    if (NBTAPI.isRestricted(item2, RestrictionType.NO_MERGE))
    {
      event.setCancelled(true);
      return;
    }
    NBTList<String> hideFlags = NBTAPI.getStringList(NBTAPI.getMainCompound(item1), CucumberyTag.HIDE_FLAGS_KEY);
    if (NBTAPI.arrayContainsValue(hideFlags, CucumberyHideFlag.CUSTOM_NAME.toString()))
    {
      return;
    }
    hideFlags = NBTAPI.getStringList(NBTAPI.getMainCompound(item2), CucumberyTag.HIDE_FLAGS_KEY);
    if (NBTAPI.arrayContainsValue(hideFlags, CucumberyHideFlag.CUSTOM_NAME.toString()))
    {
      return;
    }
    if (Cucumbery.config.getBoolean("name-tag-on-item-spawn"))
    {
      if (!Method.configContainsLocation(entity.getLocation(), Cucumbery.config.getStringList("no-name-tag-on-item-spawn-worlds")))
      {
        if (ItemStackUtil.hasDisplayName(item1) || !Cucumbery.config.getBoolean("name-tag-on-item-spawn-only-has-displayname")
                || Method.configContainsLocation(entity.getLocation(), Cucumbery.config.getStringList("no-name-tag-on-item-spawn-only-has-displayname-worlds")))
        {
          String noItemTagString = Cucumbery.config.getString("no-name-tag-on-item-spawn-string");
          Component display = item1.getItemMeta().displayName();
          if (display != null || !Cucumbery.config.getBoolean("use-no-name-tag-on-item-spawn-string")
                  || Method.configContainsLocation(entity.getLocation(), Cucumbery.config.getStringList("no-use-no-name-tag-on-item-spawn-string-worlds"))
                  || !ComponentUtil.serialize(display).contains(MessageUtil.n2s(Objects.requireNonNull(noItemTagString), MessageUtil.N2SType.SPECIAL)))
          {
            ItemLore.setItemLore(item2);
            target.setItemStack(item2);
            Component component = ComponentUtil.itemName(item2);

            int amount = item2.getAmount() + event.getEntity().getItemStack().getAmount();

            if (amount > 1)
            {
              component = ComponentUtil.createTranslate("%s (%s)", component, ComponentUtil.create("&6" + amount));
            }
            target.customName(component);
            target.setCustomNameVisible(true);
            return;
          }
        }
      }
    }
    target.setCustomName(null);
    target.setCustomNameVisible(false);
  }
}
