package com.jho5245.cucumbery.util.no_groups;

import com.jho5245.cucumbery.Cucumbery;
import com.jho5245.cucumbery.util.blockplacedata.BlockPlaceDataConfig;
import com.jho5245.cucumbery.util.storage.no_groups.CustomConfig;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.util.Timer;
import java.util.TimerTask;

public abstract class ChunkConfig
{
	private final Chunk chunk;

	private final String rootDirectory;

	private @Nullable CustomConfig customConfig;

	private YamlConfiguration cfg;

	protected ChunkConfig(@NotNull Chunk chunk, String rootDirectory, boolean createNew)
	{
		this.chunk = chunk;
		this.rootDirectory = rootDirectory;
		if (!createNew && !new File(Cucumbery.getPlugin().getDataFolder() + "/" + getFileDirectory()).exists())
		{
			this.customConfig = null;
			this.cfg = new YamlConfiguration();
			return;
		}
		this.customConfig = CustomConfig.getCustomConfig(getFileDirectory());
		cfg = customConfig.getConfig();
	}

	@NotNull
	public Chunk getChunk()
	{
		return chunk;
	}

	@NotNull
	public World getWorld()
	{
		return chunk.getWorld();
	}

	@NotNull
	public String getKey()
	{
		int x = chunk.getX(), z = chunk.getZ();
		return chunk.getWorld().getName() + "/" + x + "_" + z;
	}

	@NotNull
	public String getFileDirectory()
	{
		return "data/" + rootDirectory + "/" + getKey() + ".yml";
	}

	@NotNull
	public YamlConfiguration getConfig()
	{
		return cfg;
	}

	public void saveConfig()
	{
		Timer timer = new Timer();
		TimerTask timerTask = new TimerTask()
		{
			@Override
			public void run()
			{
				if (customConfig == null)
				{
					customConfig = CustomConfig.getCustomConfig(getFileDirectory());
					String str = cfg.saveToString();
					cfg = customConfig.getConfig();
					try
					{
						cfg.loadFromString(str);
					}
					catch (InvalidConfigurationException e)
					{
						Cucumbery.getPlugin().getLogger().warning(e.getMessage());
					}
				}
				if (cfg.getRoot() == null || cfg.getRoot().getKeys(false).isEmpty())
				{
					customConfig.delete();
					timer.cancel();
					return;
				}
				customConfig.saveConfig();
				timer.cancel();
			}
		};
		timer.schedule(timerTask, 1L);
		BlockPlaceDataConfig.TIMERS.add(timer);
	}

	public static String locationToString(@NotNull Location location)
	{
		return location.getBlockX() + "_" + location.getBlockY() + "_" + location.getBlockZ();
	}

	public static Location stringToLocation(@NotNull World world, String key)
	{
		String[] split = key.split("_");
		try
		{
			return new Location(world, Integer.parseInt(split[0]), Integer.parseInt(split[1]), Integer.parseInt(split[2]));
		}
		catch (Exception e)
		{
			return null;
		}
	}

	@NotNull
	public static String getKey(@NotNull Chunk chunk)
	{
		return chunk.getWorld().getName() + "/" + chunk.getX() + "_" + chunk.getZ();
	}
}
