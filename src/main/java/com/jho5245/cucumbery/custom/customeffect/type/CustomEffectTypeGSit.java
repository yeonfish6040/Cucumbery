package com.jho5245.cucumbery.custom.customeffect.type;

public class CustomEffectTypeGSit extends CustomEffectType
{
  public static final CustomEffectType
    GSIT_SIT = new CustomEffectType("gsit_sit", "GSit 앉기", builder().instant()),
    GSIT_SPIN = new CustomEffectType("gsit_spin", "GSit 돌기", builder().instant()),
    GSIT_LAY= new CustomEffectType("gsit_lay", "GSit 눕기", builder().instant());

  protected static void registerEffect() {
    register(GSIT_SIT, GSIT_SPIN, GSIT_LAY);
  }
}
