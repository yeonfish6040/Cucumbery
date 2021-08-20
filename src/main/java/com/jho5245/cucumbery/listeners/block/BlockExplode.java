package com.jho5245.cucumbery.listeners.block;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockExplodeEvent;

import com.jho5245.cucumbery.Cucumbery;

public class BlockExplode implements Listener
{
	@EventHandler
	public void onBlockExplode(BlockExplodeEvent event)
	{
		this.respawnBlockRegionProjection(event);
	}

	private void respawnBlockRegionProjection(BlockExplodeEvent event)
	{
		FileConfiguration config = Cucumbery.config;
		if (!config.getBoolean("enable-respawn-block-protection"))
			return;
		List<String> keyList = config.getStringList("respawn-block-protection-coords");
		if (keyList.size() == 0)
			return;
		List<Block> blocks = new ArrayList<>();
		for (String key : keyList)
		{
			try
			{
				String[] splitter = key.split(",");
				String worldString = splitter[0];
				if (splitter.length == 1 && event.getBlock().getWorld().getName().equals(worldString))
				{
					blocks.addAll(event.blockList());
				}
				else
				{
					String xString = splitter[1];
					String yString = splitter[2];
					String zString = splitter[3];
					World world = Bukkit.getServer().getWorld(worldString);
					if (world == null)
						continue;
					double xFrom = Double.parseDouble(xString.split("~")[0]);
					double xTo = Double.parseDouble(xString.split("~")[1]);
					double yFrom = Double.parseDouble(yString.split("~")[0]);
					double yTo = Double.parseDouble(yString.split("~")[1]);
					double zFrom = Double.parseDouble(zString.split("~")[0]);
					double zTo = Double.parseDouble(zString.split("~")[1]);
					for (int i = 0; i < event.blockList().size(); i++)
					{
						Block block = event.blockList().get(i);
						Location loc = block.getLocation();
						World locWorld = loc.getWorld();
						int x = loc.getBlockX(), y = loc.getBlockY(), z = loc.getBlockZ();

						if (locWorld.getName().equals(world.getName()) && x >= xFrom && x <= xTo && y >= yFrom && y <= yTo && z >= zFrom && z <= zTo)
						{
							blocks.add(block);
						}
					}
				}
			}
			catch (Exception ignored)
			{
			}
		}
		event.blockList().removeAll(blocks);
	}
}
