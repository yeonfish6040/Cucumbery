package com.jho5245.cucumbery.util.storage.no_groups;

import com.jho5245.cucumbery.Cucumbery;
import com.jho5245.cucumbery.custom.customeffect.CustomEffectManager;
import com.jho5245.cucumbery.custom.customeffect.type.CustomEffectTypeCooldown;
import com.jho5245.cucumbery.util.no_groups.Method2;
import com.jho5245.cucumbery.util.storage.data.Constant;
import dev.jorel.commandapi.wrappers.NativeProxyCommandSender;
import org.bukkit.*;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class SoundPlay
{
  public static void playSound(@NotNull Object audience, @NotNull Sound sound)
  {
    playSound(audience, sound, SoundCategory.MASTER, 1F, 1F);
  }

  public static void playSound(@NotNull Object audience, @NotNull String sound)
  {
    playSound(audience, sound, SoundCategory.MASTER, 1F, 1F);
  }

  public static void playSound(@NotNull Object audience, @NotNull Sound sound, double pitch)
  {
    playSound(audience, sound, SoundCategory.MASTER, 1F, pitch);
  }

  public static void playSound(@NotNull Object audience, @NotNull String sound, double pitch)
  {
    playSound(audience, sound, SoundCategory.MASTER, 1F, pitch);
  }

  public static void playSound(@NotNull Object audience, @NotNull Sound sound, double volume, double pitch)
  {
    playSound(audience, sound, SoundCategory.MASTER, volume, pitch);
  }

  public static void playSound(@NotNull Object audience, @NotNull String sound, double volume, double pitch)
  {
    playSound(audience, sound, SoundCategory.MASTER, volume, pitch);
  }

  public static void playSound(@NotNull Object audience, @NotNull Sound sound, @NotNull SoundCategory category)
  {
    playSound(audience, sound, category, 1F, 1F);
  }

  public static void playSound(@NotNull Object audience, @NotNull String sound, @NotNull SoundCategory category)
  {
    playSound(audience, sound, category, 1F, 1F);
  }

  public static void playSound(@NotNull Object audience, @NotNull Sound sound, @NotNull SoundCategory category, double pitch)
  {
    playSound(audience, sound, category, 1F, pitch);
  }

  public static void playSound(@NotNull Object audience, @NotNull String sound, @NotNull SoundCategory category, double pitch)
  {
    playSound(audience, sound, category, 1F, pitch);
  }

  public static void playSound(@NotNull Object audience, @NotNull Sound sound, @NotNull SoundCategory category, double volume, double pitch)
  {
    if (Cucumbery.using_CommandAPI && audience instanceof NativeProxyCommandSender sender)
    {
      audience = sender.getCaller();
    }
    if (audience instanceof Player player)
    {
      player.playSound(player.getLocation(), sound, category, (float) volume, (float) pitch);
    }
  }

  public static void playSound(@NotNull Object audience, Location location, @NotNull Sound sound, @NotNull SoundCategory category, double volume, double pitch)
  {
    if (audience instanceof Player player)
    {
      player.playSound(location, sound, category, (float) volume, (float) pitch);
    }
  }

  public static void playSound(@NotNull Object audience, @NotNull String sound, @NotNull SoundCategory category, double volume, double pitch)
  {
    if (audience instanceof Player player)
    {
      player.playSound(player.getLocation(), sound, category, (float) volume, (float) pitch);
    }
  }

  public static void playSound(@NotNull Object audience, Location location, @NotNull String sound, @NotNull SoundCategory category, double volume, double pitch)
  {
    if (Cucumbery.using_CommandAPI && audience instanceof NativeProxyCommandSender proxyCommandSender)
    {
      audience = proxyCommandSender.getCallee();
    }
    if (audience instanceof Player player)
    {
      double distance = Method2.distance(player.getLocation(), location);
      if (distance <= volume * 30)
      {
        volume *= Math.pow(0.5, distance / 30);
        player.playSound(location, sound, category, (float) volume, (float) pitch);
      }
    }
  }

  public static void playWarnSound(@NotNull Object audience)
  {
    if (audience instanceof UUID uuid)
    {
      Entity entity = Method2.getEntityAsync(uuid);
      if (entity != null)
      {
        playWarnSound(entity);
      }
    }
    if (audience instanceof Player player)
    {
      if (CustomEffectManager.hasEffect(player, CustomEffectTypeCooldown.COOLDOWN_ERROR_WARN_SOUND))
      {
        return;
      }
      Bukkit.getScheduler().runTaskLater(Cucumbery.getPlugin(), () ->
              CustomEffectManager.addEffect(player, CustomEffectTypeCooldown.COOLDOWN_ERROR_WARN_SOUND), 0L);
    }
    playSound(audience, Constant.WARNING_SOUND, Constant.WARNING_SOUND_VOLUME, Constant.WARNING_SOUND_PITCH);
  }

  public static void playErrorSound(@NotNull Object audience)
  {
    if (audience instanceof UUID uuid)
    {
      Entity entity = Method2.getEntityAsync(uuid);
      if (entity != null)
      {
        playErrorSound(entity);
      }
    }
    if (audience instanceof Player player)
    {
      if (CustomEffectManager.hasEffect(player, CustomEffectTypeCooldown.COOLDOWN_ERROR_WARN_SOUND))
      {
        return;
      }
      Bukkit.getScheduler().runTaskLater(Cucumbery.getPlugin(), () ->
              CustomEffectManager.addEffect(player, CustomEffectTypeCooldown.COOLDOWN_ERROR_WARN_SOUND), 0L);
    }
    playSound(audience, Constant.ERROR_SOUND, Constant.ERROR_SOUND_VOLUME, Constant.ERROR_SOUND_PITCH);
  }

  public static void playSoundLocation(Location location, @NotNull Sound sound)
  {
    playSoundLocation(location, sound, SoundCategory.MASTER, 1F, 1F);
  }

  public static void playSoundLocation(Location location, @NotNull String sound)
  {
    playSoundLocation(location, sound, SoundCategory.MASTER, 1F, 1F);
  }

  public static void playSoundLocation(Location location, @NotNull Sound sound, double pitch)
  {
    playSoundLocation(location, sound, SoundCategory.MASTER, 1F, pitch);
  }

  public static void playSoundLocation(Location location, @NotNull String sound, double pitch)
  {
    playSoundLocation(location, sound, SoundCategory.MASTER, 1F, pitch);
  }

  public static void playSoundLocation(Location location, @NotNull Sound sound, double volume, double pitch)
  {
    playSoundLocation(location, sound, SoundCategory.MASTER, volume, pitch);
  }

  public static void playSoundLocation(Location location, @NotNull String sound, double volume, double pitch)
  {
    playSoundLocation(location, sound, SoundCategory.MASTER, volume, pitch);
  }

  public static void playSoundLocation(Location location, @NotNull Sound sound, @NotNull SoundCategory category)
  {
    playSoundLocation(location, sound, category, 1F, 1F);
  }

  public static void playSoundLocation(Location location, @NotNull String sound, @NotNull SoundCategory category)
  {
    playSoundLocation(location, sound, category, 1f, 1F);
  }

  public static void playSoundLocation(Location location, @NotNull Sound sound, @NotNull SoundCategory category, double pitch)
  {
    playSoundLocation(location, sound, category, 1F, pitch);
  }

  public static void playSoundLocation(Location location, @NotNull String sound, @NotNull SoundCategory category, double pitch)
  {
    playSoundLocation(location, sound, category, 1F, pitch);
  }

  public static void playSoundLocation(Location location, @NotNull Sound sound, @NotNull SoundCategory category, double volume, double pitch)
  {
    World world = location.getWorld();
    for (Player player : Bukkit.getServer().getOnlinePlayers())
    {
      if (player.getLocation().getWorld().equals(world))
      {
        playSound(player, location, sound, category, volume, pitch);
      }
    }
  }

  public static void playSoundLocation(Location location, @NotNull String sound, @NotNull SoundCategory category, double volume, double pitch)
  {
    World world = location.getWorld();
    for (Player player : Bukkit.getServer().getOnlinePlayers())
    {
      if (player.getLocation().getWorld().equals(world))
      {
        playSound(player, location, sound, category, volume, pitch);
      }
    }
  }

  public static void playSoundGlobally(@NotNull Sound sound)
  {
    playSoundGlobally(sound, SoundCategory.MASTER, 1F, 1F);
  }

  public static void playSoundGlobally(@NotNull String sound)
  {
    playSoundGlobally(sound, SoundCategory.MASTER, 1F, 1F);
  }

  public static void playSoundGlobally(@NotNull Sound sound, double pitch)
  {
    playSoundGlobally(sound, SoundCategory.MASTER, 1F, pitch);
  }

  public static void playSoundGlobally(@NotNull String sound, double pitch)
  {
    playSoundGlobally(sound, SoundCategory.MASTER, 1F, pitch);
  }

  public static void playSoundGlobally(@NotNull Sound sound, double volume, double pitch)
  {
    playSoundGlobally(sound, SoundCategory.MASTER, volume, pitch);
  }

  public static void playSoundGlobally(@NotNull String sound, double volume, double pitch)
  {
    playSoundGlobally(sound, SoundCategory.MASTER, volume, pitch);
  }

  public static void playSoundGlobally(@NotNull Sound sound, @NotNull SoundCategory category)
  {
    playSoundGlobally(sound, category, 1F, 1F);
  }

  public static void playSoundGlobally(@NotNull String sound, @NotNull SoundCategory category)
  {
    playSoundGlobally(sound, category, 1F, 1F);
  }

  public static void playSoundGlobally(@NotNull Sound sound, @NotNull SoundCategory category, double pitch)
  {
    playSoundGlobally(sound, category, 1F, pitch);
  }

  public static void playSoundGlobally(@NotNull String sound, @NotNull SoundCategory category, double pitch)
  {
    playSoundGlobally(sound, category, 1F, pitch);
  }

  public static void playSoundGlobally(@NotNull Sound sound, @NotNull SoundCategory category, double volume, double pitch)
  {
    for (Player player : Bukkit.getServer().getOnlinePlayers())
    {
      playSound(player, sound, category, volume, pitch);
    }
  }

  public static void playSoundGlobally(@NotNull String sound, @NotNull SoundCategory category, double volume, double pitch)
  {
    for (Player player : Bukkit.getServer().getOnlinePlayers())
    {
      playSound(player, sound, category, volume, pitch);
    }
  }
}
