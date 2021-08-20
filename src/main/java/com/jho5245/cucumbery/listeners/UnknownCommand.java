package com.jho5245.cucumbery.listeners;

import com.jho5245.cucumbery.util.Method;
import com.jho5245.cucumbery.util.storage.ComponentUtil;
import com.jho5245.cucumbery.util.storage.data.Prefix;
import net.kyori.adventure.text.Component;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.command.UnknownCommandEvent;

public class UnknownCommand implements Listener
{
  @EventHandler
  public void onUnknownCommand(UnknownCommandEvent event)
  {
    event.message(ComponentUtil.create(Prefix.INFO_ERROR, Component.translatable("commands.help.failed")));
    Method.playErrorSound(event.getSender());
  }
}
