package com.jho5245.cucumbery.listeners.world.chunk;

import com.jho5245.cucumbery.util.blockplacedata.BlockPlaceDataConfig;
import io.papermc.paper.event.packet.PlayerChunkLoadEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class PlayerChunkLoad implements Listener
{
  @EventHandler
  public void onPlayerChunkLoad(PlayerChunkLoadEvent event)
  {
    BlockPlaceDataConfig.onPlayerChunkLoad(event);
  }
}
