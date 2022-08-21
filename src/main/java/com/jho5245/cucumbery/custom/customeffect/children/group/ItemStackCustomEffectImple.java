package com.jho5245.cucumbery.custom.customeffect.children.group;

import com.jho5245.cucumbery.custom.customeffect.CustomEffect;
import com.jho5245.cucumbery.custom.customeffect.type.CustomEffectType;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class ItemStackCustomEffectImple extends CustomEffect implements ItemStackCustomEffect
{
  protected ItemStack itemStack;

  public ItemStackCustomEffectImple(CustomEffectType effectType, @NotNull ItemStack itemStack)
  {
    super(effectType);
    this.itemStack = itemStack;
  }

  public ItemStackCustomEffectImple(CustomEffectType effectType, int duration, @NotNull ItemStack itemStack)
  {
    super(effectType, duration);
    this.itemStack = itemStack;
  }

  public ItemStackCustomEffectImple(CustomEffectType effectType, int duration, int amplifier, @NotNull ItemStack itemStack)
  {
    super(effectType, duration, amplifier);
    this.itemStack = itemStack;
  }

  public ItemStackCustomEffectImple(CustomEffectType effectType, int duration, int amplifier, @NotNull DisplayType displayType, @NotNull ItemStack itemStack)
  {
    super(effectType, duration, amplifier, displayType);
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
  @NotNull
  protected ItemStackCustomEffectImple copy()
  {
    return new ItemStackCustomEffectImple(this.getType(), this.getDuration(), this.getAmplifier(), this.getDisplayType(), this.getItemStack());
  }
}
