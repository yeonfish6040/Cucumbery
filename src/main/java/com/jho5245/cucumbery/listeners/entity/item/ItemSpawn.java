package com.jho5245.cucumbery.listeners.entity.item;

import com.jho5245.cucumbery.Cucumbery;
import com.jho5245.cucumbery.custom.customeffect.CustomEffect;
import com.jho5245.cucumbery.custom.customeffect.CustomEffectManager;
import com.jho5245.cucumbery.custom.customeffect.type.CustomEffectType;
import com.jho5245.cucumbery.util.no_groups.MessageUtil;
import com.jho5245.cucumbery.util.no_groups.Method;
import com.jho5245.cucumbery.util.storage.component.util.ComponentUtil;
import com.jho5245.cucumbery.util.storage.data.Constant;
import com.jho5245.cucumbery.util.storage.data.CustomMaterial;
import net.kyori.adventure.text.event.ClickEvent;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Item;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ItemSpawnEvent;
import org.bukkit.inventory.ItemStack;

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
    {
      Bukkit.getScheduler().runTaskLater(Cucumbery.getPlugin(), () ->
              Method.updateItem(entity), 0L);
    }
    ItemStack itemStack = entity.getItemStack();
    CustomMaterial customMaterial = CustomMaterial.itemStackOf(itemStack);
    if (customMaterial != null)
    {
      switch (customMaterial)
      {
        case DOEHAERIM_BABO, BAMIL_PABO, CUTE_SUGAR -> entity.setGravity(false);
        case RUNE_DESTRUCTION, RUNE_EARTHQUAKE ->
        {
          CustomEffectManager.addEffect(entity, new CustomEffect(CustomEffectType.INVINCIBLE, -1));
          Location location = entity.getLocation();
          entity.getNearbyEntities(30, 30, 30).forEach(e ->
                  MessageUtil.info(e, "룬이 등장 했습니다! 룬을 해방 시켜 그 힘을 누리세요. (%s)",
                          ComponentUtil.translate(Constant.THE_COLOR_HEX + "위치")
                                  .hoverEvent(ComponentUtil.create(location))
                                  .clickEvent(e.hasPermission("asdf") ? ClickEvent.suggestCommand("/tp " + location.getX() + " " + location.getY() + " " + location.getZ()) : null)
                  )
          );
          entity.setWillAge(false);
        }
      }
    }
  }
}
