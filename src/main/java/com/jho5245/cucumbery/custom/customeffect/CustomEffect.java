package com.jho5245.cucumbery.custom.customeffect;

import com.jho5245.cucumbery.custom.customeffect.children.group.*;
import com.jho5245.cucumbery.custom.customeffect.type.CustomEffectType;
import com.jho5245.cucumbery.custom.customeffect.type.CustomEffectTypeCustomMining;
import com.jho5245.cucumbery.util.no_groups.Method;
import com.jho5245.cucumbery.util.storage.component.util.ComponentUtil;
import com.jho5245.cucumbery.util.storage.data.Constant;
import com.jho5245.cucumbery.util.storage.data.CustomMaterial;
import net.kyori.adventure.text.Component;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.Statistic;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Represents a custom effect of {@link PotionEffect}.
 */
public class CustomEffect
{
  private final CustomEffectType effectType;
  private final int initDuration;
  private final int initAmplifier;
  private int duration;
  private int amplifier;
  private DisplayType displayType;

  public CustomEffect(@NotNull CustomEffectType effectType)
  {
    this(effectType, effectType.getDefaultDuration());
  }

  public CustomEffect(@NotNull CustomEffectType effectType, int duration)
  {
    this(effectType, duration, 0);
  }

  public CustomEffect(@NotNull CustomEffectType effectType, int duration, int amplifier)
  {
    this(effectType, duration, amplifier, effectType.getDefaultDisplayType());
  }

  public CustomEffect(@NotNull CustomEffectType effectType, int duration, int amplifier, @NotNull DisplayType displayType)
  {
    this.effectType = effectType;
    if (effectType.isToggle())
    {
      this.duration = -1;
      this.initDuration = -1;
    }
    else
    {
      this.duration = duration;
      this.initDuration = duration;
    }
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
    return getDescription(null, false);
  }

  @NotNull
  public Component getDescription(boolean excludeProperty)
  {
    return getDescription(null, excludeProperty);
  }

  @NotNull
  public Component getDescription(@Nullable Player viewer)
  {
    return getDescription(viewer, false);
  }

  @NotNull
  public Component getDescription(@Nullable Player viewer, boolean excludeProperty)
  {
    Component description = switch ((this.effectType.getNamespacedKey().getNamespace().equals("minecraft") ? "MINECRAFT_" : "") + effectType.getIdString().toUpperCase())
            {
              case "CURSE_OF_MUSHROOM" -> ComponentUtil.translate("%s 확률로 5초마다 인벤토리에 버섯이 들어옵니다", Constant.THE_COLOR_HEX + ((amplifier + 1) / 10d) + "%");
              case "FEATHER_FALLING" -> ComponentUtil.translate("낙하 피해를 받기 위한 최소 높이가 %sm 증가하고,", Constant.THE_COLOR_HEX + ((amplifier + 1) * 5))
                      .append(Component.text("\n"))
                      .append(ComponentUtil.translate("낙하 피해량이 %s 감소합니다", Constant.THE_COLOR_HEX + ((amplifier + 1) * 8) + "%"));
              case "BLESS_OF_SANS" -> ComponentUtil.translate("근거리 대미지가 %s 증가합니다", Constant.THE_COLOR_HEX + ((amplifier + 1) * 10) + "%");
              case "SHARPNESS" -> ComponentUtil.translate("근거리 대미지가 %s 증가합니다", Constant.THE_COLOR_HEX + (amplifier + 1.5));
              case "SMITE" -> ComponentUtil.translate("언데드 개체 공격 시 근거리 대미지가 %s 증가합니다", Constant.THE_COLOR_HEX + Constant.Sosu1.format((amplifier + 1) * 2.5));
              case "BANE_OF_ARTHROPODS" -> ComponentUtil.translate("절지동물류 개체 공격 시 근거리 대미지가 %s 증가하고,", Constant.THE_COLOR_HEX + Constant.Sosu1.format((amplifier + 1) * 2.5))
                      .append(Component.text("\n"))
                      .append(ComponentUtil.translate("%s 4단계 효과를 %s초간 부여합니다", ComponentUtil.translate("&ceffect.minecraft.slowness"), "&a1~" + Constant.Sosu1.format((amplifier + 3) * 0.5)));
              case "INSIDER" -> ComponentUtil.translate("채팅이 %s배로 입력되고, 죽을 때 모든 플레이어에게", amplifier + 2)
                      .append(Component.text("\n"))
                      .append(ComponentUtil.translate("타이틀로 자신의 데스 메시지를 띄워줍니다"));
              case "OUTSIDER" -> ComponentUtil.translate("%s 확률로 채팅 메시지가 보내지지 않고", Constant.THE_COLOR_HEX + ((amplifier + 1) * 10) + "%")
                      .append(Component.text("\n"))
                      .append(ComponentUtil.translate("입장 메시지, 퇴장 메시지가 뜨지 않습니다"));
              case "KINETIC_RESISTANCE" -> ComponentUtil.translate("겉날개 활강 중 블록에 부딪혀서 받는 피해량이 %s 감소됩니다", Constant.THE_COLOR_HEX + ((amplifier + 1) * 10) + "%")
                      .append(Component.text("\n"))
                      .append(ComponentUtil.translate("낙하 피해량은 감소되지 않습니다"));
              case "ELYTRA_BOOSTER" -> ComponentUtil.translate("겉날개 활강 중 폭죽으로 가속할 때")
                      .append(Component.text("\n"))
                      .append(ComponentUtil.translate("%s 확률로 폭죽을 소비하지 않습니다", Constant.THE_COLOR_HEX + ((amplifier + 1) * 10) + "%"));
              case "LEVITATION_RESISTANCE" -> ComponentUtil.translate("셜커에게 공격받아도 %s 확률로", Constant.THE_COLOR_HEX + ((amplifier + 1) * 10) + "%")
                      .append(Component.text("\n"))
                      .append(ComponentUtil.translate("공중 부양 상태 효과가 적용되지 않습니다"));
              case "UNCRAFTABLE" -> switch (amplifier)
                      {
                        case 0 -> ComponentUtil.translate("인벤토리에서 아이템을 제작할 수 없는 상태입니다");
                        case 1 -> ComponentUtil.translate("제작대에서 아이템을 제작할 수 없는 상태입니다");
                        default -> ComponentUtil.translate("아이템을 제작할 수 없는 상태입니다");
                      };
              case "SERVER_RADIO_LISTENING" -> ComponentUtil.translate("서버 노래를 들어서 기분이 들떠 대미지가 %s 증가합니다", Constant.THE_COLOR_HEX + ((amplifier + 2) * 5) + "%");
              case "DODGE" -> ComponentUtil.translate("%s 확률로 공격을 회피합니다", Constant.THE_COLOR_HEX + (amplifier + 1) + "%");
              case "NEWBIE_SHIELD" -> ComponentUtil.translate("플레이 시간이 1시간 미만인 당신!")
                      .append(Component.text("\n"))
                      .append(ComponentUtil.translate("받는 피해량이 %s 감소하고, 대미지가 %s 증가합니다", Constant.THE_COLOR_HEX + (switch (amplifier + 1)
                              {
                                case 1 -> "10%";
                                case 2 -> "20%";
                                default -> "40%";
                              }), Constant.THE_COLOR_HEX + (switch (amplifier + 1)
                              {
                                case 1 -> "5%";
                                case 2 -> "15%";
                                default -> "25%";
                              })
                      ))
                      .append(Component.text("\n"))
                      .append(ComponentUtil.translate("플레이 시간이 증가할 수록 효과가 감소하고 1시간이 지나면 효과가 사라집니다"));
              case "WA_SANS" -> ComponentUtil.translate("스켈레톤 유형의 개체에게 받는")
                      .append(Component.text("\n"))
                      .append(ComponentUtil.translate("피해량이 %s 감소하고 대미지가 %s 증가합니다", Constant.THE_COLOR_HEX + ((amplifier + 1) * 3) + "%", Constant.THE_COLOR_HEX + ((amplifier + 1) * 10) + "%"));
              case "HEALTH_INCREASE" -> ComponentUtil.translate("최대 HP가 %s 증가합니다", Constant.THE_COLOR_HEX + ((amplifier + 1) * 10) + "%");
              case "SPREAD" -> ComponentUtil.translate("주변 %s블록 내의 다른 플레이어에게 자신이 가장 최근에 획득한 전이 효과를 옮깁니다", amplifier + 1)
                      .append(Component.text("\n"))
                      .append(ComponentUtil.translate("추가로, 1분 마다 0.08% 확률로 변이 효과가 생기거나 사라질 수 있습니다"))
                      .append(Component.text("\n"))
                      .append(ComponentUtil.translate("단, 변이 효과가 4개 이상이면 생기지 않고 1개 이하면 사라지지 않습니다"));
              case "VAR_STOMACHACHE" -> ComponentUtil.translate("경험치를 획득하는 양만큼 오히려 경험치를 잃습니다")
                      .append(Component.text("\n"))
                      .append(ComponentUtil.translate("가지고 있는 경험치보다 잃는 경험치가 더 많을 경우"))
                      .append(Component.text("\n"))
                      .append(ComponentUtil.translate("경험치 1당 대미지 0.1로 변환되어 들어옵니다"))
                      .append(Component.text("\n"))
                      .append(ComponentUtil.translate("추가로, 한번에 경험치 100 이상을 획득 시 %s 확률로 즉사합니다", Constant.THE_COLOR_HEX + Constant.Sosu1.format((amplifier + 1) * 0.1) + "%"));
              case "VAR_PNEUMONIA" -> ComponentUtil.translate("물 속에서 산소 소모 속도가 %s 증가하고", Constant.THE_COLOR_HEX + (amplifier + 1) * 10 + "%")
                      .append(Component.text("\n"))
                      .append(ComponentUtil.translate("물 밖으로 나와도 산소가 회복되지 않습니다"));
              case "VAR_DETOXICATE" ->
                      ComponentUtil.translate("%s, %s, %s, %s 상태 효과를 가지고 있을 경우", PotionEffectType.POISON, PotionEffectType.CONFUSION, PotionEffectType.BLINDNESS, PotionEffectType.UNLUCK)
                              .append(Component.text("\n"))
                              .append(ComponentUtil.translate("해당 상태 효과의 농도 레벨을 1단계 낮추거나 제거합니다"))
                              .append(Component.text("\n"))
                              .append(ComponentUtil.translate("%s 확률로 농도 레벨이 2단계가 낮아지거나 %s 확률로 3단계가 낮아질", Constant.THE_COLOR_HEX + (amplifier + 1) + "%", Constant.THE_COLOR_HEX + Constant.Sosu1.format((amplifier + 1) * 0.1) + "%"))
                              .append(Component.text("\n"))
                              .append(ComponentUtil.translate("수 있으며, %s 확률로 표시된 모든 디버프 4개가 제거될 수 있습니다", Constant.THE_COLOR_HEX + Constant.Sosu4.format((amplifier + 1) * (amplifier + 1) * 0.001) + "%"));
              case "VAR_PODAGRA" -> ComponentUtil.translate("이동하거나 점프할 때 웅크리지 않은 상태라면")
                      .append(Component.text("\n"))
                      .append(ComponentUtil.translate("행동을 시작할 때 1의 피해를 입고 행동을 지속할 시"))
                      .append(Component.text("\n"))
                      .append(ComponentUtil.translate("3초마다 %s의 피해를 입습니다. 추가로 3.5블록 미만의 높이에서 낙하할", Constant.Sosu1.format(0.2 + (amplifier * 0.1))))
                      .append(Component.text("\n"))
                      .append(ComponentUtil.translate("경우 1의 피해를 입고 그 이상의 높이에서 낙하할 경우 받는 피해량이 50% 증가합니다"));
              case "ENDER_SLAYER" ->
                      ComponentUtil.translate("%s, %s 또는 %s 공격 시 대미지가 %s 증가합니다", EntityType.ENDERMAN, EntityType.ENDERMITE, EntityType.ENDER_DRAGON, Constant.THE_COLOR_HEX + ((amplifier + 1) * 10) + "%");
              case "BOSS_SLAYER" -> ComponentUtil.translate("보스 몬스터 공격 시 대미지가 %s 증가합니다", Constant.THE_COLOR_HEX + (amplifier + 1) * 10 + "%");
              case "EXPERIENCE_BOOST" -> ComponentUtil.translate("경험치 획득량이 %s 증가합니다", Constant.THE_COLOR_HEX + ((amplifier + 1) * 5) + "%");
              case "MINING_FATIGUE" -> ComponentUtil.translate("채광 속도가 %s 감소합니다", Constant.THE_COLOR_HEX + (amplifier + 1) + "%");
              case "MINDAS_TOUCH" -> ComponentUtil.translate("채광 등급이 %s 증가합니다", Constant.THE_COLOR_HEX + (amplifier + 1));
              case "HASTE" -> ComponentUtil.translate("채광 속도가 %s 증가합니다", Constant.THE_COLOR_HEX + (amplifier + 1) * 10 + "%");
              case "MINING_FORTUNE" -> ComponentUtil.translate("채광 행운이 %s 증가합니다", Constant.THE_COLOR_HEX + (amplifier + 1) * 5);
              case "MINECRAFT_SPEED" -> VanillaEffectDescription.getDescription(new PotionEffect(PotionEffectType.SPEED, duration, amplifier));
              case "MINECRAFT_SLOWNESS" -> VanillaEffectDescription.getDescription(new PotionEffect(PotionEffectType.SLOW, duration, amplifier));
              case "MINECRAFT_HASTE" -> VanillaEffectDescription.getDescription(new PotionEffect(PotionEffectType.FAST_DIGGING, duration, amplifier), viewer);
              case "MINECRAFT_MINING_FATIGUE" -> VanillaEffectDescription.getDescription(new PotionEffect(PotionEffectType.SLOW_DIGGING, duration, amplifier), viewer);
              case "MINECRAFT_STRENGTH" -> VanillaEffectDescription.getDescription(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, duration, amplifier));
              case "MINECRAFT_WEAKNESS" -> VanillaEffectDescription.getDescription(new PotionEffect(PotionEffectType.WEAKNESS, duration, amplifier));
              case "MINECRAFT_INSTANT_DAMAGE" -> VanillaEffectDescription.getDescription(new PotionEffect(PotionEffectType.HARM, duration, amplifier));
              case "MINECRAFT_INSTANT_HEAL" -> VanillaEffectDescription.getDescription(new PotionEffect(PotionEffectType.HEAL, duration, amplifier));
              case "MINECRAFT_JUMP_BOOST" -> VanillaEffectDescription.getDescription(new PotionEffect(PotionEffectType.JUMP, duration, amplifier));
              case "MINECRAFT_NAUSEA" -> VanillaEffectDescription.getDescription(new PotionEffect(PotionEffectType.CONFUSION, duration, amplifier));
              case "MINECRAFT_REGENERATION" -> VanillaEffectDescription.getDescription(new PotionEffect(PotionEffectType.REGENERATION, duration, amplifier));
              case "MINECRAFT_RESISTANCE" -> VanillaEffectDescription.getDescription(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, duration, amplifier));
              case "MINECRAFT_FIRE_RESISTANCE" -> VanillaEffectDescription.getDescription(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, duration, amplifier));
              case "MINECRAFT_WATER_BREATHING" -> VanillaEffectDescription.getDescription(new PotionEffect(PotionEffectType.WATER_BREATHING, duration, amplifier));
              case "MINECRAFT_BLINDNESS" -> VanillaEffectDescription.getDescription(new PotionEffect(PotionEffectType.BLINDNESS, duration, amplifier));
              case "MINECRAFT_INVISIBILITY" -> VanillaEffectDescription.getDescription(new PotionEffect(PotionEffectType.INVISIBILITY, duration, amplifier));
              case "MINECRAFT_NIGHT_VISION" -> VanillaEffectDescription.getDescription(new PotionEffect(PotionEffectType.NIGHT_VISION, duration, amplifier));
              case "MINECRAFT_HUNGER" -> VanillaEffectDescription.getDescription(new PotionEffect(PotionEffectType.HUNGER, duration, amplifier));
              case "MINECRAFT_POISON" -> VanillaEffectDescription.getDescription(new PotionEffect(PotionEffectType.POISON, duration, amplifier));
              case "MINECRAFT_WITHER" -> VanillaEffectDescription.getDescription(new PotionEffect(PotionEffectType.WITHER, duration, amplifier));
              case "MINECRAFT_HEALTH_BOOST" -> VanillaEffectDescription.getDescription(new PotionEffect(PotionEffectType.HEALTH_BOOST, duration, amplifier));
              case "MINECRAFT_ABSORPTION" -> VanillaEffectDescription.getDescription(new PotionEffect(PotionEffectType.ABSORPTION, duration, amplifier));
              case "MINECRAFT_SATURATION" -> VanillaEffectDescription.getDescription(new PotionEffect(PotionEffectType.SATURATION, duration, amplifier));
              case "MINECRAFT_LEVITATION" -> VanillaEffectDescription.getDescription(new PotionEffect(PotionEffectType.LEVITATION, duration, amplifier));
              case "MINECRAFT_SLOW_FALLING" -> VanillaEffectDescription.getDescription(new PotionEffect(PotionEffectType.SLOW_FALLING, duration, amplifier));
              case "MINECRAFT_GLOWING" -> VanillaEffectDescription.getDescription(new PotionEffect(PotionEffectType.GLOWING, duration, amplifier));
              case "MINECRAFT_LUCK" -> VanillaEffectDescription.getDescription(new PotionEffect(PotionEffectType.LUCK, duration, amplifier));
              case "MINECRAFT_UNLUCK" -> VanillaEffectDescription.getDescription(new PotionEffect(PotionEffectType.UNLUCK, duration, amplifier));
              case "MINECRAFT_CONDUIT_POWER" -> VanillaEffectDescription.getDescription(new PotionEffect(PotionEffectType.CONDUIT_POWER, duration, amplifier));
              case "MINECRAFT_DOLPHINS_GRACE" -> VanillaEffectDescription.getDescription(new PotionEffect(PotionEffectType.DOLPHINS_GRACE, duration, amplifier));
              case "MINECRAFT_BAD_OMEN" -> VanillaEffectDescription.getDescription(new PotionEffect(PotionEffectType.BAD_OMEN, duration, amplifier));
              case "MINECRAFT_HERO_OF_THE_VILLAGE" -> VanillaEffectDescription.getDescription(new PotionEffect(PotionEffectType.HERO_OF_THE_VILLAGE, duration, amplifier));
              case "MINECRAFT_DARKNESS" -> VanillaEffectDescription.getDescription(new PotionEffect(PotionEffectType.DARKNESS, duration, amplifier));
              default -> effectType.getDescription(viewer);
            };
    if (effectType == CustomEffectTypeCustomMining.TITANIUM_FINDER)
    {
      description = ComponentUtil.translate("%s 채굴 시 %s 등장 확률이 %s 증가합니다 (기본 %s)",
              CustomMaterial.TITANIUM_ORE, CustomMaterial.TITANIUM_ORE, Constant.THE_COLOR_HEX + Constant.Sosu2.format((amplifier + 1) * 0.1) + "%", Constant.THE_COLOR_HEX + "5%");
    }
    if (effectType == CustomEffectType.SUPERIOR_LEVITATION)
    {
      description = ComponentUtil.translate("노빠꾸로 공중으로 떠오릅니다");
      description = description.append(Component.text("\n"));
      description = description.append(ComponentUtil.translate("말 그대로 빠꾸 없이 초당 약 %s 블록만큼 떠오릅니다", Constant.Sosu2.format((amplifier + 1) * 0.9)));
    }
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
    if (this instanceof EntityCustomEffect entityCustomEffect)
    {
      Entity entity = entityCustomEffect.getEntity();
      if (effectType == CustomEffectType.CONTINUAL_RIDING)
      {
        description = description.append(Component.text("\n"));
        description = description.append(Component.text("\n"));
        description = description.append(ComponentUtil.translate("&7탑승 중인 개체 : %s", entity));
      }
    }
    if (this instanceof LocationCustomEffect locationCustomEffect)
    {
      Location location = locationCustomEffect.getLocation();
      if (effectType == CustomEffectType.POSITION_MEMORIZE)
      {
        description = description.append(Component.text("\n"));
        description = description.append(Component.text("\n"));
        description = description.append(ComponentUtil.translate("&7저장된 위치 : %s", location));
      }
    }
    if (this instanceof DoubleCustomEffect doubleCustomEffect)
    {
      double data = doubleCustomEffect.getDouble();
      if (effectType == CustomEffectType.DAMAGE_SPREAD)
      {
        description = description.append(Component.text("\n"));
        description = description.append(Component.text("\n"));
        description = description.append(ComponentUtil.translate("&7누적된 피해량 : %s", data));
      }
    }
    if (this instanceof StringCustomEffect stringCustomEffect)
    {
      String data = stringCustomEffect.getString();
      if (effectType == CustomEffectType.STRANGE_CREATIVE_MODE)
      {
        try
        {
          GameMode gameMode = GameMode.valueOf(data.split("\\|")[0]);
          description = description.append(Component.text("\n"));
          description = description.append(Component.text("\n"));
          description = description.append(ComponentUtil.translate("&7이전 게임 모드 : %s", gameMode));
          if (viewer != null && viewer.hasPermission("asdf"))
          {
            description = description.append(Component.text("\n"));
            description = description.append(ComponentUtil.translate("&7데이터 : %s", data));
          }
        }
        catch (Exception ignored)
        {

        }
      }
    }
    if (!excludeProperty)
    {
      description = ComponentUtil.create(description, effectType.getPropertyDescription());
    }
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
  protected CustomEffect copy()
  {
    return new CustomEffect(getType(), getInitDuration(), getInitAmplifier(), getDisplayType());
  }

  public boolean isHidden()
  {
    return this.effectType.isHidden();
  }

  @SuppressWarnings("all")
  public boolean isTimeHidden()
  {
    return effectType.isTimeHidden() || duration == -1;
  }

  public boolean isTimeHiddenWhenFull()
  {
    return effectType.isTimeHiddenWhenFull() || duration + 1 >= initDuration;
  }

  /**
   * @return 해당 효과의 아이콘이 있으면 아이콘을 반환하고 없으면 <code>null</code>을 반환합니다.
   */
  @Nullable
  public ItemStack getIcon()
  {
    return this.effectType.getIcon();
  }

  public boolean isHiddenEnum()
  {
    return this.effectType.isHiddenEnum();
  }

  /**
   * 효과의 표시 유형
   * <p>기본적으로 모든 효과는 GUI에서 표시되지만 {@link DisplayType#BOSS_BAR_ONLY}와 {@link DisplayType#NONE}은 GUI에서 표시되지 않는다</p>
   */
  public enum DisplayType
  {
    /**
     * 액션바
     */
    ACTION_BAR,
    /**
     * 플레이어 리스트 - Tab 키를 눌렀을 때 나오는 플레이어 목록의 하단에 표시됨
     * <p>번지코드 플레이어 리스트 플러그인은 호환되지 않을 수 있음
     */
    PLAYER_LIST,
    /**
     * 보스바. 일반적으로 가장 많이 표시되는 기본 유형
     */
    BOSS_BAR,
    /**
     * 보스바에만 보이고 gui에서는 보이지 않고, 우클릭으로 해제 불가
     */
    BOSS_BAR_ONLY,
    /**
     * /effect3 query 명령어를 통해 보이는 GUI에서만 보이는 효과
     */
    GUI,
    /**
     * 어디에도 표시하지 않음. 관리자가 /effect3 query [개체] 명령어를 통해서만 참조 가능
     */
    NONE
  }
}
