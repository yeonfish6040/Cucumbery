package com.jho5245.cucumbery.listeners.entity.item;

import com.jho5245.cucumbery.Cucumbery;
import com.jho5245.cucumbery.util.MessageUtil;
import com.jho5245.cucumbery.util.Method;
import com.jho5245.cucumbery.util.itemlore.ItemLore;
import com.jho5245.cucumbery.util.nbt.CucumberyTag;
import com.jho5245.cucumbery.util.nbt.NBTAPI;
import com.jho5245.cucumbery.util.storage.ComponentUtil;
import com.jho5245.cucumbery.util.storage.ItemStackUtil;
import com.jho5245.cucumbery.util.storage.data.Constant;
import com.jho5245.cucumbery.util.storage.data.Constant.CucumberyHideFlag;
import com.jho5245.cucumbery.util.storage.data.Constant.RestrictionType;
import de.tr7zw.changeme.nbtapi.NBTList;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Item;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ItemSpawnEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Objects;

public class ItemSpawn implements Listener
{
  @EventHandler(priority = EventPriority.HIGHEST)
  public void onItemSpawn(ItemSpawnEvent event)
  {
    if (event.isCancelled())
    {
      return;
    }

    Item entity = event.getEntity();
    ItemStack item = entity.getItemStack();
    if (NBTAPI.isRestricted(item, RestrictionType.NO_ITEM_EXISTS))
    {
      event.setCancelled(true);
      return;
    }
    NBTList<String> extraTags = NBTAPI.getStringList(NBTAPI.getMainCompound(item), CucumberyTag.EXTRA_TAGS_KEY);
    if (Cucumbery.config.getBoolean("glow-dropped-items") || NBTAPI.arrayContainsValue(extraTags, Constant.ExtraTag.GLOWING.toString()))
    {
      entity.setGlowing(true);
    }
    if (NBTAPI.arrayContainsValue(extraTags, Constant.ExtraTag.INVULNERABLE.toString()))
    {
      entity.setInvulnerable(true);
    }
    if (Cucumbery.config.getBoolean("use-helpful-lore-feature"))
    {
      if (!Method.configContainsLocation(event.getLocation(), Cucumbery.config.getStringList("no-use-helpful-lore-feature-worlds")))
      {
        ItemLore.setItemLore(item);
      }
      else
      {
        ItemLore.removeItemLore(item);
      }
    }
    else
    {
      ItemLore.removeItemLore(item);
    }
    NBTList<String> hideFlags = NBTAPI.getStringList(NBTAPI.getMainCompound(item), CucumberyTag.HIDE_FLAGS_KEY);
    if (NBTAPI.arrayContainsValue(hideFlags, CucumberyHideFlag.CUSTOM_NAME.toString()))
    {
      return;
    }
    if (Cucumbery.config.getBoolean("name-tag-on-item-spawn"))
    {
      if (!Method.configContainsLocation(event.getLocation(), Cucumbery.config.getStringList("no-name-tag-on-item-spawn-worlds")))
      {
        if (ItemStackUtil.hasDisplayName(item) || !Cucumbery.config.getBoolean("name-tag-on-item-spawn-only-has-displayname") || Method.configContainsLocation(event.getLocation(), Cucumbery.config
                .getStringList("no-name-tag-on-item-spawn-only-has-displayname-worlds")))
        {
          String noItemTagString = Cucumbery.config.getString("no-name-tag-on-item-spawn-string");
          if (!ItemStackUtil.hasDisplayName(item) || !Cucumbery.config.getBoolean("use-no-name-tag-on-item-spawn-string") || Method.configContainsLocation(event.getLocation(), Cucumbery.config
                  .getStringList("no-use-no-name-tag-on-item-spawn-string-worlds")) || !item.getItemMeta().getDisplayName().contains(MessageUtil.n2s(Objects.requireNonNull(noItemTagString), MessageUtil.N2SType.SPECIAL)))
          {
            String customName = entity.getCustomName();
            boolean isDefault = customName != null && customName.startsWith("§기§본§아§이§템§이§름");
            ItemLore.setItemLore(item);
            entity.setItemStack(item);
            Component component = ComponentUtil.itemName(item);

            int amount = item.getAmount();

            if (amount > 1)
            {
              component = ComponentUtil.createTranslate("%s (%s)", component, ComponentUtil.create("&6" + amount));
            }

            entity.customName(component);
            entity.setCustomNameVisible(true);
          }
        }
      }
    }
  }
}
