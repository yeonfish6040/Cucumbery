package com.jho5245.cucumbery.listeners.block;

import com.jho5245.cucumbery.util.no_groups.MessageUtil;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockDamageAbortEvent;

public class BlockDamageAbort implements Listener
{
  @EventHandler
  public void onBlockDamageAbort(BlockDamageAbortEvent event)
  {
    Player player = event.getPlayer();
    Block block = event.getBlock();
    if (Math.random() < 0.0001)
    {
      MessageUtil.sendMessage(player, "%s: 아 왜 저 안캐요", block);
    }
  }
}
