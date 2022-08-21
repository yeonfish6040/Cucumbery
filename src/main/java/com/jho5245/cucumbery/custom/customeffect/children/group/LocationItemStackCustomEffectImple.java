package com.jho5245.cucumbery.custom.customeffect.children.group;

import com.jho5245.cucumbery.custom.customeffect.CustomEffect;
import com.jho5245.cucumbery.custom.customeffect.type.CustomEffectType;
import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class LocationItemStackCustomEffectImple extends CustomEffect implements LocationItemStackCustomEffect
{
  protected Location location;
  protected ItemStack itemStack;

  public LocationItemStackCustomEffectImple(CustomEffectType effectType, @NotNull Location location, @NotNull ItemStack itemStack)
  {
    super(effectType);
    this.location = location;
    this.itemStack = itemStack;
  }

  public LocationItemStackCustomEffectImple(CustomEffectType effectType, int duration, @NotNull Location location, @NotNull ItemStack itemStack)
  {
    super(effectType, duration);
    this.location = location;
    this.itemStack = itemStack;
  }

  public LocationItemStackCustomEffectImple(CustomEffectType effectType, int duration, int amplifier, @NotNull Location location, @NotNull ItemStack itemStack)
  {
    super(effectType, duration, amplifier);
    this.location = location;
    this.itemStack = itemStack;
  }

  public LocationItemStackCustomEffectImple(CustomEffectType effectType, int duration, int amplifier, @NotNull DisplayType displayType, @NotNull Location location, @NotNull ItemStack itemStack)
  {
    super(effectType, duration, amplifier, displayType);
    this.location = location;
    this.itemStack = itemStack;
  }

  @Override
  public @NotNull ItemStack getItemStack()
  {
    return itemStack;
  }

  @Override
  public void setItemStack(@NotNull ItemStack itemStack)
  {
    this.itemStack = itemStack;
  }

  @Override
  public @NotNull Location getLocation()
  {
    return location;
  }

  @Override
  public void setLocation(@NotNull Location location)
  {
    this.location = location;
  }

  @NotNull
  @Override
  protected LocationItemStackCustomEffectImple copy()
  {
    return new LocationItemStackCustomEffectImple(this.getType(), this.getDuration(), this.getAmplifier(), this.getDisplayType(), this.getLocation(), this.getItemStack());
  }
}
