package com.jho5245.cucumbery.customeffect;

import com.jho5245.cucumbery.util.storage.component.util.ComponentUtil;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class CustomEffect
{
  private final CustomEffectType effectType;
  private final int initDuration;
  private int duration;
  private final int initAmplifier;
  private int amplifier;
  private DisplayType displayType;
  private Component description;
  private boolean keepOnDeath;
  private boolean keepOnQuit;

  public CustomEffect(CustomEffectType effectType, int duration, int amplifier)
  {
    this(effectType, duration, amplifier, effectType.getDefaultDisplayType());
  }
  public CustomEffect(CustomEffectType effectType, int duration, int amplifier, @NotNull DisplayType displayType)
  {
    this.effectType = effectType;
    this.duration = duration;
    this.initDuration = duration;
    this.amplifier = amplifier;
    this.initAmplifier = amplifier;
    this.displayType = displayType;
    this.keepOnDeath = effectType.isDefaultKeepOnDeath();
    this.keepOnQuit = effectType.isDefaultKeepOnQuit();
    boolean isNegative = effectType.isNegative();
    this.description = switch (effectType)
            {
              case MUSHROOM ->
                      ComponentUtil.createTranslate("%s 확률로 5초마다 인벤토리에 버섯이 들어옵니다.", "&e" + ((amplifier + 1) / 10d) + "%");
              default -> effectType.getDescription();
            };

    if (keepOnDeath || !keepOnQuit)
    {
      this.description = this.description.append(Component.text("\n"));
    }
    if (keepOnDeath)
    {
      this.description = this.description.append(Component.text("\n")).append(ComponentUtil.createTranslate("&" + (isNegative ? "c" : "a") + "사망해도 효과가 사라지지 않습니다."));
    }
    if (!keepOnQuit)
    {
      this.description = this.description.append(Component.text("\n")).append(ComponentUtil.createTranslate("&c접속을 종료하면 효과가 사라집니다."));
    }
  }

  @NotNull
  public CustomEffectType getEffectType()
  {
    return effectType;
  }

  public void setDuration(int duration)
  {
    this.duration = duration;
  }

  public void tick()
  {
    this.duration--;
  }

  public int getDuration()
  {
    return duration;
  }

  public void setAmplifier(int amplifier)
  {
    this.amplifier = amplifier;
  }

  public int getAmplifier()
  {
    return amplifier;
  }

  public int getInitDuration()
  {
    return initDuration;
  }

  public int getInitAmplifier()
  {
    return initAmplifier;
  }

  @NotNull
  public DisplayType getDisplayType()
  {
    return displayType;
  }

  public void setDisplayType(@NotNull DisplayType displayType)
  {
    this.displayType = displayType;
  }

  @Nullable
  public Component getDescription()
  {
    return description;
  }

  public boolean isKeepOnDeath()
  {
    return keepOnDeath;
  }

  public CustomEffect setKeepOnDeath(boolean keepOnDeath)
  {
    this.keepOnDeath = keepOnDeath;
    return this;
  }

  public boolean isKeepOnQuit()
  {
    return keepOnQuit;
  }

  public CustomEffect setKeepOnQuit(boolean keepOnQuit)
  {
    this.keepOnQuit = keepOnQuit;
    return this;
  }

  @NotNull
  public CustomEffect copy()
  {
    return new CustomEffect(this.getEffectType(), this.getDuration(), this.getAmplifier(), this.getDisplayType());
  }
}
