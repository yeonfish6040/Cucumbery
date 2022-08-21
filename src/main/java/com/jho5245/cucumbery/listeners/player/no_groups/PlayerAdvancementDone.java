package com.jho5245.cucumbery.listeners.player.no_groups;

import com.jho5245.cucumbery.util.storage.no_groups.CustomConfig.UserData;
import com.jho5245.cucumbery.util.storage.component.util.ComponentUtil;
import com.jho5245.cucumbery.util.storage.data.Prefix;
import io.papermc.paper.advancement.AdvancementDisplay;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TranslatableComponent;
import org.bukkit.GameMode;
import org.bukkit.advancement.Advancement;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerAdvancementDoneEvent;

public class PlayerAdvancementDone implements Listener
{
  @EventHandler
  public void onPlayerAdvancementDone(PlayerAdvancementDoneEvent event)
  {
    Player player = event.getPlayer();
    if (player.getGameMode() == GameMode.SPECTATOR || UserData.SPECTATOR_MODE.getBoolean(player) || !UserData.ANNOUNCE_ADVANCEMENTS.getBoolean(player))
    {
      event.message(null);
      return;
    }
    Component message = event.message();
    if (message instanceof TranslatableComponent translatableComponent)
    {
      Advancement advancement = event.getAdvancement();
      AdvancementDisplay display = advancement.getDisplay();
      if (display != null)
      {
        Component prefix = (switch (display.frame())
                {
                  case CHALLENGE -> Prefix.INFO_ADVANCEMENT_CHALLENGE;
                  case GOAL -> Prefix.INFO_ADVANCEMENT_GOAL;
                  default -> Prefix.INFO_ADVANCEMENT;
                }).get();
        String key = translatableComponent.key();
        event.message(Component.empty().append(prefix).append(ComponentUtil.translate(key, player, advancement)));
      }
    }
  }
}
