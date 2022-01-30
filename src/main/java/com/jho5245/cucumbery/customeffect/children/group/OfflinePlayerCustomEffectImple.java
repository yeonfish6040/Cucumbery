package com.jho5245.cucumbery.customeffect.children.group;

import com.jho5245.cucumbery.customeffect.CustomEffect;
import com.jho5245.cucumbery.customeffect.CustomEffectType;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;

public class OfflinePlayerCustomEffectImple extends CustomEffect implements OfflinePlayerCustomEffect
{
  private OfflinePlayer offlinePlayer;

  public OfflinePlayerCustomEffectImple(CustomEffectType effectType, OfflinePlayer offlinePlayer)
  {
    super(effectType);
    this.offlinePlayer = offlinePlayer;
  }

  public OfflinePlayerCustomEffectImple(CustomEffectType effectType, int duration, OfflinePlayer offlinePlayer)
  {
    super(effectType, duration);
    this.offlinePlayer = offlinePlayer;
  }

  public OfflinePlayerCustomEffectImple(CustomEffectType effectType, int duration, int amplifier, OfflinePlayer offlinePlayer)
  {
    super(effectType, duration, amplifier);
    this.offlinePlayer = offlinePlayer;
  }

  public OfflinePlayerCustomEffectImple(CustomEffectType effectType, int duration, int amplifier, @NotNull DisplayType displayType, OfflinePlayer offlinePlayer)
  {
    super(effectType, duration, amplifier, displayType);
    this.offlinePlayer = offlinePlayer;
  }

  @Override
  public @NotNull OfflinePlayer getOfflinePlayer()
  {
    return offlinePlayer;
  }

  @Override
  public void setOfflinePlayer(@NotNull OfflinePlayer offlinePlayer)
  {
    this.offlinePlayer = offlinePlayer;
  }
}
