package com.jho5245.cucumbery.listeners.world.chunk;

import com.jho5245.cucumbery.util.blockplacedata.BlockPlaceDataConfig;
import io.papermc.paper.event.packet.PlayerChunkUnloadEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class PlayerChunkUnload implements Listener
{
  @EventHandler
  public void onPlayerChunkUnload(PlayerChunkUnloadEvent event)
  {
    BlockPlaceDataConfig.onPlayerChunkUnload(event);
  }
}
