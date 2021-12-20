package com.jho5245.cucumbery.customeffect;

import com.jho5245.cucumbery.util.storage.component.util.ComponentUtil;
import com.jho5245.cucumbery.util.storage.data.Constant;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;

public class CustomEffect
{
  private final CustomEffectType effectType;
  private final int initDuration;
  private int duration;
  private final int initAmplifier;
  private int amplifier;
  private DisplayType displayType;

  public CustomEffect(CustomEffectType effectType)
  {
    this(effectType, effectType.getDefaultDuration());
  }

  public CustomEffect(CustomEffectType effectType, int duration)
  {
    this(effectType, duration, 0);
  }

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
  }

  @NotNull
  public CustomEffectType getEffectType()
  {
    return effectType;
  }

  public void tick()
  {
    if (this.duration != -1)
    this.duration--;
  }

  public void setDuration(int duration)
  {
    this.duration = duration;
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

  @NotNull
  public Component getDescription()
  {
    Component description = switch (effectType)
            {
              case CURSE_OF_MUSHROOM ->
                      ComponentUtil.createTranslate("%s 확률로 5초마다 인벤토리에 버섯이 들어옵니다.", "&e" + ((amplifier + 1) / 10d) + "%");
              case FEATHER_FALLING ->
                      ComponentUtil.createTranslate("낙하 피해를 받기 위한 최소 높이가 %sm 증가하고,","&e" + ((amplifier + 1) * 5))
                      .append(Component.text("\n"))
                      .append(ComponentUtil.createTranslate("낙하 피해량이 %s 감소합니다.","&e" + ((amplifier + 1) * 8) + "%"));
              case BLESS_OF_SANS ->
                      ComponentUtil.createTranslate("근거리 공격 피해량이 %s 증가합니다.", "&e" + ((amplifier + 1) * 10) + "%");
              case SHARPNESS ->
                      ComponentUtil.createTranslate("근거리 공격 피해량이 %s 증가합니다.", "&e" + (amplifier + 1.5));
              case SMITE ->
                      ComponentUtil.createTranslate("언데드 개체에게 주는 근거리 공격 피해량이 %s 증가합니다.", "&e" + Constant.Sosu1.format((amplifier + 1) * 2.5));
              case BANE_OF_ARTHROPODS ->
                      ComponentUtil.createTranslate("절지동물류 개체에게 주는 근거리 공격 피해량이 %s 증가하고,", "&e" + Constant.Sosu1.format((amplifier + 1) * 2.5))
                      .append(Component.text("\n"))
                      .append(ComponentUtil.createTranslate("%s 효과를 1~%s초간 지급합니다.", ComponentUtil.createTranslate("effect.mineract.slowness"), "&r" + Constant.Sosu1.format((amplifier + 1) * 0.5)));
              case INSIDER ->
                      ComponentUtil.createTranslate("채팅이 %s배로 입력되고, 죽을 때 모든 플레이어에게", amplifier + 2)
                              .append(Component.text("\n"))
                              .append(ComponentUtil.createTranslate("타이틀로 자신의 데스 메시지를 띄워줍니다."));
              case OUTSIDER ->
                      ComponentUtil.createTranslate("%s 확률로 채팅 메시지가 보내지지 않고", "&e" + ((amplifier + 1) * 10) + "%")
                              .append(Component.text("\n"))
                              .append(ComponentUtil.createTranslate("입장 메시지, 퇴장 메시지가 뜨지 않습니다."));
              default -> effectType.getDescription();
            };
    description = description.append(effectType.getPropertyDescription());
    return description;
  }

  public boolean isKeepOnDeath()
  {
    return this.effectType.isKeepOnDeath();
  }

  public boolean isKeepOnQuit()
  {
    return this.effectType.isKeepOnQuit();
  }

  public boolean isKeepOnMilk()
  {
    return this.effectType.isKeepOnMilk();
  }

  @NotNull
  public CustomEffect copy()
  {
    return new CustomEffect(this.getEffectType(), this.getDuration(), this.getAmplifier(), this.getDisplayType());
  }

  public enum DisplayType
  {
    ACTION_BAR, PLAYER_LIST, BOSS_BAR, NONE
  }
}
