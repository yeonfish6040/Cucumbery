package com.jho5245.cucumbery.listeners.player.item;

import com.jho5245.cucumbery.Cucumbery;
import com.jho5245.cucumbery.util.MessageUtil;
import com.jho5245.cucumbery.util.MessageUtil.ConsonantType;
import com.jho5245.cucumbery.util.Method;
import com.jho5245.cucumbery.util.storage.ComponentUtil;
import com.jho5245.cucumbery.util.storage.CustomConfig.UserData;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemBreakEvent;
import org.bukkit.inventory.ItemStack;

public class PlayerItemBreak implements Listener
{
  @EventHandler
  public void onPlayerItemBreak(PlayerItemBreakEvent event)
  {
    Player player = event.getPlayer();
    ItemStack item = event.getBrokenItem();
    if (UserData.SHOW_ITEM_BREAK_TITLE.getBoolean(player.getUniqueId()) && Cucumbery.config.getBoolean("send-title-on-item-break"))
    {
      if (!Method.configContainsLocation(player.getLocation(), Cucumbery.getPlugin().getConfig().getStringList("no-send-title-on-item-break-worlds")))
      {
        MessageUtil.sendTitle(player, MessageUtil.as(ComponentUtil.create("&c장비 파괴됨!")),
                MessageUtil.as(ComponentUtil.create("&e인벤토리 아이템 중 "), (ComponentUtil.itemName(item, TextColor.fromHexString("#52ee52"))),
                        (ComponentUtil.create("&e" + MessageUtil.getFinalConsonant(ComponentUtil.serialize(ComponentUtil.itemName(item)), ConsonantType.이가) + " 파괴되었습니다.")))
                , 5, 100, 15);
      }
    }
  }
}
