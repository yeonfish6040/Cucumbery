package com.jho5245.cucumbery.custom.customeffect;

import com.jho5245.cucumbery.custom.customeffect.children.group.OfflinePlayerCustomEffect;
import com.jho5245.cucumbery.custom.customeffect.children.group.PlayerCustomEffect;
import com.jho5245.cucumbery.util.no_groups.Method;
import com.jho5245.cucumbery.util.storage.component.util.ComponentUtil;
import com.jho5245.cucumbery.util.storage.data.Constant;
import net.kyori.adventure.text.Component;
import org.bukkit.Color;
import org.bukkit.OfflinePlayer;
import org.bukkit.Statistic;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class CustomEffect
{
  private final CustomEffectType effectType;
  private final int initDuration;
  private final int initAmplifier;
  private int duration;
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
  public CustomEffectType getType()
  {
    return effectType;
  }

  public void tick()
  {
    if (this.duration != -1)
    {
      this.duration--;
    }
  }

  public int getDuration()
  {
    return duration;
  }

  public void setDuration(int duration)
  {
    this.duration = duration;
  }

  public int getAmplifier()
  {
    return amplifier;
  }

  public void setAmplifier(int amplifier)
  {
    this.amplifier = amplifier;
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
              case CURSE_OF_MUSHROOM -> ComponentUtil.translate("%s 확률로 5초마다 인벤토리에 버섯이 들어옵니다", "&e" + ((amplifier + 1) / 10d) + "%");
              case FEATHER_FALLING -> ComponentUtil.translate("낙하 피해를 받기 위한 최소 높이가 %sm 증가하고,", "&e" + ((amplifier + 1) * 5))
                      .append(Component.text("\n"))
                      .append(ComponentUtil.translate("낙하 피해량이 %s 감소합니다", "&e" + ((amplifier + 1) * 8) + "%"));
              case BLESS_OF_SANS -> ComponentUtil.translate("근거리 공격 피해량이 %s 증가합니다", "&e" + ((amplifier + 1) * 10) + "%");
              case SHARPNESS -> ComponentUtil.translate("근거리 공격 피해량이 %s 증가합니다", "&e" + (amplifier + 1.5));
              case SMITE -> ComponentUtil.translate("언데드 개체에게 주는 근거리 공격 피해량이 %s 증가합니다", "&e" + Constant.Sosu1.format((amplifier + 1) * 2.5));
              case BANE_OF_ARTHROPODS -> ComponentUtil.translate("절지동물류 개체에게 주는 근거리 공격 피해량이 %s 증가하고,", "&e" + Constant.Sosu1.format((amplifier + 1) * 2.5))
                      .append(Component.text("\n"))
                      .append(ComponentUtil.translate("%s 효과를 1~%s초간 지급합니다", ComponentUtil.translate("effect.minecraft.slowness"), "&r" + Constant.Sosu1.format((amplifier + 1) * 0.5)));
              case INSIDER -> ComponentUtil.translate("채팅이 %s배로 입력되고, 죽을 때 모든 플레이어에게", amplifier + 2)
                      .append(Component.text("\n"))
                      .append(ComponentUtil.translate("타이틀로 자신의 데스 메시지를 띄워줍니다"));
              case OUTSIDER -> ComponentUtil.translate("%s 확률로 채팅 메시지가 보내지지 않고", "&e" + ((amplifier + 1) * 10) + "%")
                      .append(Component.text("\n"))
                      .append(ComponentUtil.translate("입장 메시지, 퇴장 메시지가 뜨지 않습니다"));
              case KINETIC_RESISTANCE -> ComponentUtil.translate("겉날개 활강 중 블록에 부딪혀서 받는 피해량이 %s 감소됩니다", "&e" + ((amplifier + 1) * 10) + "%")
                      .append(Component.text("\n"))
                      .append(ComponentUtil.translate("낙하 피해량은 감소되지 않습니다"));
              case ELYTRA_BOOSTER -> ComponentUtil.translate("겉날개 활강 중 폭죽으로 가속할 때")
                      .append(Component.text("\n"))
                      .append(ComponentUtil.translate("%s 확률로 폭죽을 소비하지 않습니다", "&e" + ((amplifier + 1) * 10) + "%"));
              case LEVITATION_RESISTANCE -> ComponentUtil.translate("셜커에게 공격받아도 %s 확률로", "&e" + ((amplifier + 1) * 10) + "%")
                      .append(Component.text("\n"))
                      .append(ComponentUtil.translate("공중 부양 상태 효과가 적용되지 않습니다"));
              case UNCRAFTABLE -> switch (amplifier)
                      {
                        case 0 -> ComponentUtil.translate("인벤토리에서 아이템을 제작할 수 없습니다");
                        case 1 -> ComponentUtil.translate("제작대에서 아이템을 제작할 수 없습니다");
                        default -> ComponentUtil.translate("아이템을 제작할 수 없습니다");
                      };
              case SERVER_RADIO_LISTENING -> ComponentUtil.translate("서버 노래를 들어서 기분이 들떠 주는 피해량이 %s 증가합니다", "&e" + ((amplifier + 2) * 5) + "%");
              case DODGE -> ComponentUtil.translate("%s 확률로 공격을 회피합니다", "&e" + (amplifier + 1) + "%");
              case NEWBIE_SHIELD -> ComponentUtil.translate("플레이 시간이 1시간 미만인 당신!")
                      .append(Component.text("\n"))
                      .append(ComponentUtil.translate("받는 피해량이 %s 감소하고 주는 피해량이 %s 증가합니다", "&e" + (switch (amplifier + 1)
                              {
                                case 1 -> "10%";
                                case 2 -> "20%";
                                default -> "40%";
                              }), "&e" + (switch (amplifier + 1)
                              {
                                case 1 -> "5%";
                                case 2 -> "15%";
                                default -> "25%";
                              })
                      ))
                      .append(Component.text("\n"))
                      .append(ComponentUtil.translate("플레이 시간이 증가할 수록 효과가 감소하고 1시간이 지나면 효과가 사라집니다"));
              case WA_SANS -> ComponentUtil.translate("스켈레톤 유형의 개체에게 받는")
                      .append(Component.text("\n"))
                      .append(ComponentUtil.translate("피해량이 %s 감소하고 주는 피해량이 %s 증가합니다", "&e" + ((amplifier + 1) * 3) + "%", "&e" + ((amplifier + 1) * 10) + "%"));
              case HEALTH_INCREASE -> ComponentUtil.translate("최대 HP가 %s 증가합니다", "&e" + ((amplifier + 1) * 10) + "%p");
              case SPREAD -> ComponentUtil.translate("주변 %s블록 내의 다른 플레이어에게 자신이 가장 최근에 획득한 전이 효과를 옮깁니다", amplifier + 1)
                      .append(Component.text("\n"))
                      .append(ComponentUtil.translate("추가로, 1분 마다 0.08% 확률로 변이 효과가 생기거나 사라질 수 있습니다"))
                      .append(Component.text("\n"))
                      .append(ComponentUtil.translate("단, 변이 효과가 4개 이상이면 생기지 않고 1개 이하면 사라지지 않습니다"));
              case VAR_STOMACHACHE -> ComponentUtil.translate("경험치를 획득하는 양만큼 오히려 경험치를 잃습니다")
                      .append(Component.text("\n"))
                      .append(ComponentUtil.translate("가지고 있는 경험치보다 잃는 경험치가 더 많을 경우"))
                      .append(Component.text("\n"))
                      .append(ComponentUtil.translate("경험치 1당 대미지 0.1로 변환되어 들어옵니다"))
                      .append(Component.text("\n"))
                      .append(ComponentUtil.translate("추가로, 한번에 경험치 100 이상을 획득 시 %s 확률로 즉사합니다", "&e" + Constant.Sosu1.format((amplifier + 1) * 0.1) + "%"));
              case VAR_PNEUMONIA -> ComponentUtil.translate("물 속에서 산소 소모 속도가 %s 증가하고", "&e" + (amplifier + 1) * 10 + "%")
                      .append(Component.text("\n"))
                      .append(ComponentUtil.translate("물 밖으로 나와도 산소가 회복되지 않습니다"));
              case VAR_DETOXICATE -> ComponentUtil.translate("%s, %s, %s, %s 상태 효과를 가지고 있을 경우", PotionEffectType.POISON, PotionEffectType.CONFUSION, PotionEffectType.BLINDNESS, PotionEffectType.UNLUCK)
                      .append(Component.text("\n"))
                      .append(ComponentUtil.translate("해당 상태 효과의 농도 레벨을 1단계 낮추거나 제거합니다"))
                      .append(Component.text("\n"))
                      .append(ComponentUtil.translate("%s 확률로 농도 레벨이 2단계가 낮아지거나 %s 확률로 3단계가 낮아질", "&e" + (amplifier + 1) + "%", "&e" + Constant.Sosu1.format((amplifier + 1) * 0.1) + "%"))
                      .append(Component.text("\n"))
                      .append(ComponentUtil.translate("수 있으며, %s 확률로 표시된 모든 디버프 4개가 제거될 수 있습니다", "&e" + Constant.Sosu4.format((amplifier + 1) * (amplifier + 1) * 0.001) + "%"));
              case VAR_PODAGRA -> ComponentUtil.translate("이동하거나 점프할 때 웅크리지 않은 상태라면")
                      .append(Component.text("\n"))
                      .append(ComponentUtil.translate("행동을 시작할 때 1의 피해를 입고 행동을 지속할 시"))
                      .append(Component.text("\n"))
                      .append(ComponentUtil.translate("3초마다 %s의 피해를 입습니다. 추가로 3.5블록 미만의 높이에서 낙하할", Constant.Sosu1.format(0.2 + (amplifier * 0.1))))
                      .append(Component.text("\n"))
                      .append(ComponentUtil.translate("경우 1의 피해를 입고 그 이상의 높이에서 낙하할 경우 낙하 피해량이 50% 증가합니다"));
              default -> effectType.getDescription();
            };
    if (this instanceof OfflinePlayerCustomEffect offlinePlayerCustomEffect)
    {
      OfflinePlayer player = offlinePlayerCustomEffect.getOfflinePlayer();
      if (effectType == CustomEffectType.NEWBIE_SHIELD)
      {
        description = description.append(Component.text("\n"));
        description = description.append(Component.text("\n"));
        description = description.append(ComponentUtil.translate("&7플레이 시간 : %s", Method.timeFormatMilli(player.getStatistic(Statistic.PLAY_ONE_MINUTE) * 50L, false)));
      }
    }
    if (this instanceof PlayerCustomEffect playerCustomEffect)
    {
      Player player = playerCustomEffect.getPlayer();
      if (effectType == CustomEffectType.CONTINUAL_SPECTATING)
      {
        description = description.append(Component.text("\n"));
        description = description.append(Component.text("\n"));
        description = description.append(ComponentUtil.translate("&7관전 중인 플레이어 : %s", player));
      }
    }
    description = ComponentUtil.create(description, effectType.getPropertyDescription());
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
    return new CustomEffect(this.getType(), this.getDuration(), this.getAmplifier(), this.getDisplayType());
  }

  public boolean isHidden()
  {
    return this.effectType.isHidden();
  }

  @SuppressWarnings("all")
  public boolean isTimeHidden()
  {
    return isTimeHiddenWhenFull() || this.duration == -1 || this.effectType.isTimeHidden();
  }

  public boolean isTimeHiddenWhenFull()
  {
    return this.effectType.isTimeHiddenWhenFull() && this.duration + 1 >= this.initDuration;
  }

  @Nullable
  public Color getColor()
  {
    return this.effectType.getColor();
  }

  @Nullable
  public ItemStack getIcon()
  {
    return this.effectType.getIcon();
  }

  public boolean isHiddenEnum()
  {
    return this.effectType.isHiddenEnum();
  }

  public enum DisplayType
  {
    ACTION_BAR,
    PLAYER_LIST,
    BOSS_BAR,
    GUI,
    NONE
  }
}
