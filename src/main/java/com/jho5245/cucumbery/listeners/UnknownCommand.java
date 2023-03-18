package com.jho5245.cucumbery.listeners;

import com.jho5245.cucumbery.Cucumbery;
import com.jho5245.cucumbery.custom.customeffect.CustomEffectManager;
import com.jho5245.cucumbery.custom.customeffect.type.CustomEffectType;
import com.jho5245.cucumbery.util.no_groups.Method;
import com.jho5245.cucumbery.util.storage.component.util.ComponentUtil;
import com.jho5245.cucumbery.util.storage.data.Prefix;
import net.kyori.adventure.text.Component;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.command.UnknownCommandEvent;

public class UnknownCommand implements Listener
{
  public void onUnknownCommand(UnknownCommandEvent event)
  {
    if (Cucumbery.config.getBoolean("use-custom-unknown-command-message"))
    {
      event.message(ComponentUtil.create(Prefix.INFO_ERROR, Cucumbery.config.getString("custom-unknown-command-message","translate:commands.help.failed")));
      Method.playErrorSound(event.getSender());
    }
    CommandSender sender = event.getSender();
    Component message = event.message();
    if (message != null && sender instanceof Player player && CustomEffectManager.hasEffect(player, CustomEffectType.CURSE_OF_BEANS))
    {
      player.sendMessage(message);
    }
  }
}
