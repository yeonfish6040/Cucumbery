package com.jho5245.cucumbery.customeffect;

import com.jho5245.cucumbery.util.storage.component.util.ComponentUtil;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.translation.Translatable;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("unused")
public enum CustomEffectType implements Translatable
{
  MUTE(0, ComponentUtil.createTranslate("채팅을 할 수 없는 상태입니다."), true, true, true),
  MUSHROOM(1000, ComponentUtil.createTranslate("5초마다 일정 확률로 인벤토리에 버섯이 들어옴니다."), true, true, true),
  INVINCIBLE(0, ComponentUtil.createTranslate("피해를 받지않는 상태입니다.")),
  BUFF_FREEZE(0, ComponentUtil.createTranslate("사망 시 일부 효과를 제외한 버프가 보호됩니다.")),
  ;

  private final int maxAmplifier;

  private final DisplayType defaultDisplayType;

  private final Component description;

  private final boolean defaultKeepOnDeath;

  private final boolean defaultKeepOnQuit;

  private final boolean isNegative;

  CustomEffectType()
  {
    this(Integer.MAX_VALUE);
  }

  CustomEffectType(int maxAmplifier)
  {
    this(maxAmplifier, false);
  }

  CustomEffectType(@NotNull Component description)
  {
    this(Integer.MAX_VALUE, description);
  }

  CustomEffectType(@NotNull Component description, boolean defaultKeepOnDeath, boolean defaultKeepOnQuit, boolean isNegative)
  {
    this(Integer.MAX_VALUE, DisplayType.PLAYER_LIST, description, defaultKeepOnDeath, defaultKeepOnQuit, isNegative);
  }

  CustomEffectType(int maxAmplifier, @NotNull Component description)
  {
    this(maxAmplifier, DisplayType.PLAYER_LIST, description, false, true, false);
  }

  CustomEffectType(int maxAmplifier, @NotNull Component description, boolean defaultKeepOnDeath, boolean defaultKeepOnQuit)
  {
    this(maxAmplifier, DisplayType.PLAYER_LIST, description, defaultKeepOnDeath, defaultKeepOnQuit, false);
  }

  CustomEffectType(int maxAmplifier, @NotNull Component description, boolean defaultKeepOnDeath, boolean defaultKeepOnQuit, boolean isNegative)
  {
    this(maxAmplifier, DisplayType.PLAYER_LIST, description, defaultKeepOnDeath, defaultKeepOnQuit, isNegative);
  }

  CustomEffectType(int maxAmplifier, boolean isNegative)
  {
    this(maxAmplifier, DisplayType.PLAYER_LIST, Component.empty(), false, true, isNegative);
  }

  CustomEffectType(@NotNull Component description, boolean defaultKeepOnDeath, boolean defaultKeepOnQuit)
  {
    this(Integer.MAX_VALUE, DisplayType.PLAYER_LIST, description, defaultKeepOnDeath, defaultKeepOnQuit, false);
  }

  CustomEffectType(@NotNull DisplayType defaultDisplayType, @NotNull Component description, boolean defaultKeepOnDeath, boolean defaultKeepOnQuit)
  {
    this(Integer.MAX_VALUE, defaultDisplayType, description, defaultKeepOnDeath, defaultKeepOnQuit, false);
  }

  CustomEffectType(int maxAmplifier, @NotNull DisplayType defaultDisplayType, @NotNull Component description)
  {
    this(maxAmplifier, defaultDisplayType, description, false, true, false);
  }

  CustomEffectType(int maxAmplifier, @NotNull DisplayType defaultDisplayType, @NotNull Component description, boolean defaultKeepOnDeath, boolean defaultKeepOnQuit)
  {
    this(maxAmplifier, defaultDisplayType, description, defaultKeepOnDeath, defaultKeepOnQuit, false);
  }

  CustomEffectType(int maxAmplifier, @NotNull DisplayType defaultDisplayType, @NotNull Component description, boolean defaultKeepOnDeath, boolean defaultKeepOnQuit, boolean isNegative)
  {
    this.maxAmplifier = maxAmplifier;
    this.defaultDisplayType = defaultDisplayType;
    this.description = description;
    this.defaultKeepOnDeath = defaultKeepOnDeath;
    this.defaultKeepOnQuit = defaultKeepOnQuit;
    this.isNegative = isNegative;
  }

  @Override
  public @NotNull String translationKey()
  {
    return switch (this)
            {
              case MUTE -> "벙어리";
              case MUSHROOM -> "버섯ㅅㅅㅅ";
              case INVINCIBLE -> "무적";
              case BUFF_FREEZE -> "사망 시 효과 보존";
            };
  }

  public int getMaxAmplifier()
  {
    return maxAmplifier;
  }

  public DisplayType getDefaultDisplayType()
  {
    return defaultDisplayType;
  }

  public boolean isNegative()
  {
    return isNegative;
  }

  public Component getDescription()
  {
    return description;
  }

  public boolean isDefaultKeepOnDeath()
  {
    return defaultKeepOnDeath;
  }

  public boolean isDefaultKeepOnQuit()
  {
    return defaultKeepOnQuit;
  }
}
