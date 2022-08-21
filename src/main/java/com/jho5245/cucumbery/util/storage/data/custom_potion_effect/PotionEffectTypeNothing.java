package com.jho5245.cucumbery.util.storage.data.custom_potion_effect;

import org.jetbrains.annotations.NotNull;

@Deprecated
public class PotionEffectTypeNothing extends CustomPotionEffectType
{
  protected PotionEffectTypeNothing(int id, @NotNull String name)
  {
    super(id, name);
  }

  @Override
  public @NotNull String translationKey()
  {
    return "아무것도 아님";
  }

  @Override
  public @NotNull String getName()
  {
    return "nothing";
  }
}
