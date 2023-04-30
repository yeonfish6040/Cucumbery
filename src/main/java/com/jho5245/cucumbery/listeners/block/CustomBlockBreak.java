package com.jho5245.cucumbery.listeners.block;

import com.jho5245.cucumbery.Cucumbery;
import com.jho5245.cucumbery.events.block.CustomBlockBreakEvent;
import com.songoda.ultimatetimber.UltimateTimber;
import net.coreprotect.CoreProtect;
import net.coreprotect.CoreProtectAPI;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

public class CustomBlockBreak implements Listener
{
  @EventHandler
  public void onCustomBlockBreak(CustomBlockBreakEvent event)
  {
    ultimateTimber(event);
    mcmmo(event);
    coreProtect(event);
  }

  void coreProtect(CustomBlockBreakEvent event)
  {
    if (!Cucumbery.using_CoreProtect)
    {
      return;
    }
    CoreProtectAPI coreProtectAPI = CoreProtect.getInstance().getAPI();
    coreProtectAPI.logRemoval(event.getPlayer().getName(), event.getBlock().getLocation(), event.getBlock().getType(), event.getBlock().getBlockData());
  }
  void ultimateTimber(CustomBlockBreakEvent event)
  {
    if (!Cucumbery.using_UltimateTimber)
    {
      return;
    }
    UltimateTimber.getInstance().getTreeFallManager().onBlockBreak(new BlockBreakEvent(event.getBlock(), event.getPlayer()));
  }

  void mcmmo(CustomBlockBreakEvent event)
  {
    if (!Cucumbery.using_mcMMO)
    {
      return;
    }
    // 스킬 경험치 줘야함
  }
}
