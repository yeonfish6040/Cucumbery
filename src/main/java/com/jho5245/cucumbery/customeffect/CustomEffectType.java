package com.jho5245.cucumbery.customeffect;

import com.jho5245.cucumbery.customeffect.CustomEffect.DisplayType;
import com.jho5245.cucumbery.util.storage.component.util.ComponentUtil;
import com.jho5245.cucumbery.util.storage.data.EnumHideable;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.translation.Translatable;
import org.bukkit.Color;
import org.bukkit.enchantments.Enchantment;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public enum CustomEffectType implements Translatable, EnumHideable
{
  /**
   * 채팅 금지
   */
  MUTE(0, true, true, true),
  /**
   * 버섯의 저주
   */
  CURSE_OF_MUSHROOM(999, true, true, true),
  /**
   * 무적
   */
  INVINCIBLE,
  /**
   * 버프 보호
   */
  BUFF_FREEZE,
  /**
   * 혼란
   */
  CONFUSION(false, true, true),
  /**
   * 리저렉션
   */
  RESURRECTION,
  /**
   * 리저렉션 무적
   */
  RESURRECTION_INVINCIBLE,
  /**
   * 리저렉션 쿨타임
   */
  RESURRECTION_COOLDOWN(true, true, true),
  /**
   * 차가운 걸음
   */
  FROST_WALKER,
  /**
   * 가벼운 착지
   */
  FEATHER_FALLING(9),
  /**
   * 샌즈의 축복
   */
  BLESS_OF_SANS(9),
  /**
   * 전이
   */
  METASASIS(99, false, true, true),
  /**
   * 폐렴
   */
  PNEUMONIA(false, true, true),
  /**
   * 불내증
   */
  EXPERIENCE_INTOLERANCE(false, true, true),
  /**
   * 뭐하지
   */
  WHAT_TO_DO(false, true, true),
  /**
   * 앵무새의 가호
   */
  PARROTS_CHEER,
  /**
   * 날카로움
   */
  SHARPNESS(255),
  /**
   * 강타
   */
  SMITE(255),
  /**
   * 살충
   */
  BANE_OF_ARTHROPODS(255),
  /**
   * 정지
   */
  STOP(100,false, true, true),
  /**
   * 인벤토리 보호
   */
  KEEP_INVENTORY,
  /**
   * 줍지 마, 던져!
   */
  DO_NOT_PICKUP_BUT_THROW_IT(9, false, true, true),
  /**
   * 인싸
   */
  INSIDER(9,true, true, true),
  /**
   * 아싸
   */
  OUTSIDER(9,true, true, true),
  /**
   * 콩의 저주 콩의 저주
   */
  CURSE_OF_BEANS(true, true, true),
  /**
   * 섬세한 손길
   */
  SILK_TOUCH,
  /**
   * 제련의 손길
   */
  SMELTING_TOUCH,
  /**
   * 염력
   */
  TELEKINESIS,
  /**
   * 인벤세이브 저주
   */
  CURSE_OF_INVENTORY(false, true, true),
  /**
   * 인벤토리 트롤
   */
  TROLL_INVENTORY_PROPERTY(Integer.MAX_VALUE - 1, true, true, true),
  /**
   * 인벤토리 트롤
   */
  TROLL_INVENTORY_PROPERTY_MIN(Integer.MAX_VALUE - 1, true, true, true),
  /**
   * 건축 불가능
   */
  CURSE_OF_CREATIVITY(true, true, true),
  /**
   * 블록 파괴 불가능
   */
  CURSE_OF_CREATIVITY_BREAK(true, true, true),
  /**
   * 블록 설치 불가능
   */
  CURSE_OF_CREATIVITY_PLACE(true, true, true),
  /**
   * 섭식 장애
   */
  CURSE_OF_CONSUMPTION(true, true, true),
  /**
   * 줍기 불가능
   */
  CURSE_OF_PICKUP(true, true, true),
  /**
   * 버리기 불가능
   */
  CURSE_OF_DROP(true, true, true),
  /**
   * 점프 불가능
   */
  CURSE_OF_JUMPING(true, true, true),
  /**
   * 운동 에너지 저항
   */
  KINETIC_RESISTANCE(9),
  /**
   * 겉날개 부스터
   */
  ELYTRA_BOOSTER(9),
  /**
   * 공중 부양 저항
   */
  LEVITATION_RESISTANCE(9),
  /**
   * 치즈 실험
   */
  CHEESE_EXPERIMENT(false, true, true),
  /**
   * 똥손
   */
  IDIOT_SHOOTER(19,true, true, true),
  /**
   * 디버그 염탐
   */
  DEBUG_WATCHER(true, true, false),
  /**
   * 즉시 큐컴버리 업데이트
   */
  CUCUMBERY_UPDATER(1, true, true, false),
  /**
   * 아무것도 아님
   */
  NOTHING,
  /**
   * 어색함
   */
  AWKWARD,
  /**
   * 평범함
   */
  MUNDANE,
  /**
   * 진함
   */
  THICK,
  /**
   * 제작 불가능함
   */
  UNCRAFTABLE(2, false, true, true),
  /**
   * 서버 라디오 분위기
   */
  SERVER_RADIO_LISTENING(2, true, false, false),
  /**
   * 어둠의 공포
   */
  DARKNESS_TERROR(false, true, true),
  /**
   * 어둠의 공포 내성
   */
  DARKNESS_TERROR_RESISTANCE,
  /**
   * 채팅 쿨타임
   */
  COOLDOWN_CHAT(true, true, true),
  /**
   * 아아템 확성기 쿨타임
   */
  COOLDOWN_ITEM_MEGAPHONE(true, true, true),
  /**
   * 회피
   */
  DODGE(99),
  ;

  private final int maxAmplifier;

  private final boolean isKeepOnDeath;

  private final boolean isKeepOnQuit;

  private final boolean isNegative;

  CustomEffectType()
  {
    this(0);
  }

  CustomEffectType(int maxAmplifier)
  {
    this(maxAmplifier, false);
  }

  CustomEffectType(int maxAmplifier, boolean isNegative)
  {
    this(maxAmplifier, false, true, isNegative);
  }

  CustomEffectType(boolean isKeepOnDeath, boolean isKeepOnQuit, boolean isNegative)
  {
    this(0, isKeepOnDeath, isKeepOnQuit, isNegative);
  }

  CustomEffectType(int maxAmplifier, boolean isKeepOnDeath, boolean isKeepOnQuit, boolean isNegative)
  {
    this.maxAmplifier = maxAmplifier;
    this.isKeepOnDeath = isKeepOnDeath;
    this.isKeepOnQuit = isKeepOnQuit;
    this.isNegative = isNegative;
  }

  @Override
  @NotNull
  public String translationKey()
  {
    return switch (this)
            {
              case MUTE -> "채팅 금지";
              case CURSE_OF_MUSHROOM -> "버섯의 저주";
              case INVINCIBLE -> "무적";
              case BUFF_FREEZE -> "버프 프리저";
              case CONFUSION -> "혼란";
              case RESURRECTION -> "리저렉션";
              case RESURRECTION_INVINCIBLE -> "리저렉션 무적";
              case RESURRECTION_COOLDOWN -> "리저렉션 쿨타임";
              case FROST_WALKER -> "차가운 걸음";
              case FEATHER_FALLING -> "가벼운 착지";
              case BLESS_OF_SANS -> "샌즈의 축복";
              case METASASIS -> "전이";
              case PNEUMONIA -> "폐렴";
              case EXPERIENCE_INTOLERANCE -> "불내증";
              case WHAT_TO_DO -> "뭐하지";
              case PARROTS_CHEER -> "앵무새의 가호";
              case SHARPNESS -> Enchantment.DAMAGE_ALL.translationKey();
              case SMITE -> Enchantment.DAMAGE_UNDEAD.translationKey();
              case BANE_OF_ARTHROPODS -> Enchantment.DAMAGE_ARTHROPODS.translationKey();
              case STOP -> "정지";
              case KEEP_INVENTORY -> "인벤토리 보존";
              case DO_NOT_PICKUP_BUT_THROW_IT -> "줍지 마, 던져!";
              case INSIDER -> "인싸";
              case OUTSIDER -> "아싸";
              case CURSE_OF_BEANS -> "콩의 저주 콩의 저주";
              case SILK_TOUCH -> Enchantment.SILK_TOUCH.translationKey();
              case SMELTING_TOUCH -> "제련의 손길";
              case TELEKINESIS -> "염력";
              case CURSE_OF_INVENTORY -> "인벤세이브 저주";
              case TROLL_INVENTORY_PROPERTY, TROLL_INVENTORY_PROPERTY_MIN -> "인벤토리 트롤";
              case CURSE_OF_CREATIVITY -> "건축 불가능";
              case CURSE_OF_CREATIVITY_BREAK -> "블록 파괴 불가능";
              case CURSE_OF_CREATIVITY_PLACE -> "블록 설치 불가능";
              case CURSE_OF_CONSUMPTION -> "섭식 장애";
              case CURSE_OF_PICKUP -> "줍기 불가능";
              case CURSE_OF_DROP -> "버리기 불가능";
              case CURSE_OF_JUMPING -> "점프 불가능";
              case KINETIC_RESISTANCE -> "운동 에너지 저항";
              case ELYTRA_BOOSTER -> "겉날개 부스터";
              case LEVITATION_RESISTANCE -> "공중 부양 저항";
              case CHEESE_EXPERIMENT -> "치즈 실험";
              case IDIOT_SHOOTER -> "똥손";
              case DEBUG_WATCHER -> "디버그 염탐";
              case CUCUMBERY_UPDATER -> "즉시 큐컴버리 업데이트";
              case NOTHING -> "아무것도 아님";
              case AWKWARD -> "어색함";
              case MUNDANE -> "평범함";
              case THICK -> "진함";
              case UNCRAFTABLE -> "제작 불가능함";
              case SERVER_RADIO_LISTENING -> "서버 라디오 분위기";
              case DARKNESS_TERROR -> "어둠의 공포";
              case DARKNESS_TERROR_RESISTANCE -> "어둠의 공포 내성";
              case COOLDOWN_CHAT -> "채팅 쿨타임";
              case COOLDOWN_ITEM_MEGAPHONE -> "아이템 확성기 쿨타임";
              case DODGE -> "회피";
            };
  }

  public int getMaxAmplifier()
  {
    return maxAmplifier;
  }

  @NotNull
  public DisplayType getDefaultDisplayType()
  {
    return switch (this)
            {
              case PARROTS_CHEER -> DisplayType.PLAYER_LIST;
              case TROLL_INVENTORY_PROPERTY_MIN -> DisplayType.NONE;
              default -> DisplayType.BOSS_BAR;
            };
  }

  public boolean isNegative()
  {
    return isNegative;
  }

  @NotNull
  public Component getDescription()
  {
    return switch (this)
    {
      case MUTE -> ComponentUtil.translate("채팅을 할 수 없는 상태입니다.");
      case CURSE_OF_MUSHROOM -> ComponentUtil.translate("농도 레벨 * 0.1% 확률로 5초마다 인벤토리에 버섯이 들어옵니다.");
      case INVINCIBLE -> ComponentUtil.translate("어떠한 형태의 피해도 받지 않습니다.");
      case BUFF_FREEZE -> ComponentUtil.translate("사망시 일부 버프를 제외한 버프가 사라지지 않습니다.");
      case CONFUSION -> ComponentUtil.translate("방향키가 반대로 작동합니다.");
      case RESURRECTION -> ComponentUtil.translate("죽음에 이르는 피해를 입었을 때, 죽지 않고")
              .append(Component.text("\n"))
              .append(ComponentUtil.translate("버프를 소모하여 2초간 무적이 됩니다."));
      case RESURRECTION_INVINCIBLE -> ComponentUtil.translate("2초간 무적이 됩니다.");
      case RESURRECTION_COOLDOWN -> ComponentUtil.translate("%s 버프를 받을 수 없는 상태입니다.", CustomEffectType.RESURRECTION);
      case FROST_WALKER -> ComponentUtil.translate("마그마 블록 위를 걸어도 피해를 입지 않습니다.");
      case FEATHER_FALLING -> ComponentUtil.translate("낙하 피해를 받기 위한 최소 높이가 증가하고,")
              .append(Component.text("\n"))
              .append(ComponentUtil.translate("낙하 피해량이 감소합니다."));
      case BLESS_OF_SANS, SHARPNESS -> ComponentUtil.translate("근거리 공격 피해량이 증가합니다.");
      case METASASIS -> ComponentUtil.translate("뭐");
      case PARROTS_CHEER -> ComponentUtil.translate("HP가 5 이하일 때 15 블록 이내에 자신이 길들인 앵무새가")
              .append(Component.text("\n"))
              .append(ComponentUtil.translate("있으면 받는 피해가 45% 감소하고 주는 피해량이 10% 증가합니다."));
      case SMITE -> ComponentUtil.translate("언데드 개체에게 주는 근거리 공격 피해량이 증가합니다.");
      case BANE_OF_ARTHROPODS ->
              ComponentUtil.translate("절지동물류 개체에게 주는 근거리 공격 피해량이 증가하고,")
                      .append(Component.text("\n"))
                      .append(ComponentUtil.translate("%s 효과를 지급합니다.", ComponentUtil.translate("effect.minecraft.slowness")));
      case STOP ->
              ComponentUtil.translate("모든 행동을 할 수 없는 상태입니다.")
                      .append(Component.text("\n"))
                      .append(ComponentUtil.translate("좌우이동, 웅크리기를 할 때마다 지속 시간이 6초씩 감소합니다."));
      case KEEP_INVENTORY -> ComponentUtil.translate("죽어도 아이템을 떨어뜨리지 않습니다.");
      case DO_NOT_PICKUP_BUT_THROW_IT -> ComponentUtil.translate("아이템을 줍는 대신 던집니다.")
              .append(Component.text("\n"))
              .append(ComponentUtil.translate("농도 레벨이 높을 수록 더 멀리 던집니다."));
      case INSIDER ->
              ComponentUtil.translate("채팅이 여러번 입력되고, 죽을 때 모든 플레이어에게")
              .append(Component.text("\n"))
              .append(ComponentUtil.translate("타이틀로 자신의 데스 메시지를 띄워줍니다."));
      case OUTSIDER ->
              ComponentUtil.translate("일정 확률로 채팅 메시지가 보내지지 않고")
              .append(Component.text("\n"))
              .append(ComponentUtil.translate("입장 메시지, 퇴장 메시지가 뜨지 않습니다."));
      case CURSE_OF_BEANS ->
              ComponentUtil.translate("뭔가.. 자꾸.. 2번씩 일어난다.")
                      .append(Component.text("\n"))
                      .append(ComponentUtil.translate("뭔가.. 자꾸.. 2번씩 일어난다."));
      case SILK_TOUCH ->
              ComponentUtil.translate("블록을 캐면 섬세한 손길 마법과")
              .append(Component.text("\n"))
              .append(ComponentUtil.translate("동일하게 아이템을 얻을 수 있습니다."));
      case TELEKINESIS ->
              ComponentUtil.translate("블록을 캐거나 적을 잡았을 때 드롭하는")
                      .append(Component.text("\n"))
                      .append(ComponentUtil.translate("아이템과 경험치가 즉시 인벤토리에 들어옵니다."));
      case SMELTING_TOUCH ->
              ComponentUtil.translate("블록을 캐거나 적을 잡았을 때 드롭하는")
                      .append(Component.text("\n"))
                      .append(ComponentUtil.translate("아이템을 제련된 형태로 바꿔줍니다."));
      case CURSE_OF_INVENTORY -> ComponentUtil.translate("죽으면 가지고 있는 모든 아이템을 떨어뜨립니다.");
      case CURSE_OF_CREATIVITY -> ComponentUtil.translate("블록을 설치하거나 파괴할 수 없습니다.");
      case CURSE_OF_CREATIVITY_BREAK -> ComponentUtil.translate("블록을 파괴할 수 없습니다.");
      case CURSE_OF_CREATIVITY_PLACE -> ComponentUtil.translate("블록을 설치할 수 없습니다.");
      case CURSE_OF_CONSUMPTION -> ComponentUtil.translate("음식이나 포션을 사용할 수 없습니다.");
      case CURSE_OF_PICKUP -> ComponentUtil.translate("아이템을 주울 수 없습니다.");
      case CURSE_OF_DROP -> ComponentUtil.translate("아이템을 버릴 수 없습니다.");
      case CURSE_OF_JUMPING -> ComponentUtil.translate("점프를 할 수 없습니다.");
      case KINETIC_RESISTANCE ->
              ComponentUtil.translate("겉날개 활강 중 블록에 부딪혀서 받는 피해량이 감소됩니다.")
                      .append(Component.text("\n"))
                      .append(ComponentUtil.translate("낙하 피해량은 감소되지 않습니다."));
      case ELYTRA_BOOSTER -> ComponentUtil.translate("겉날개 활강 중 폭죽으로 가속할 때")
              .append(Component.text("\n"))
              .append(ComponentUtil.translate("일정 확률로 폭죽을 소비하지 않습니다."));
      case LEVITATION_RESISTANCE -> ComponentUtil.translate("셜커에게 공격받아도 일정 확률로")
              .append(Component.text("\n"))
              .append(ComponentUtil.translate("공중 부양 상태 효과가 적용되지 않습니다."));
      case CHEESE_EXPERIMENT -> ComponentUtil.translate("우유를 마시면 효과가 사라지고 멀미가 30초간 지속됩니다.")
              .append(Component.text("\n"))
              .append(ComponentUtil.translate("또한, 80% 확률로 허기가 30초 지속됩니다."));
      case IDIOT_SHOOTER -> ComponentUtil.translate("발사체가 이상한 방향으로 날아갑니다.");
      case DEBUG_WATCHER -> ComponentUtil.translate("플러그인 디버그 메시지를 볼 수 있게 됩니다.");
      case CUCUMBERY_UPDATER -> ComponentUtil.translate("큐컴버리 플러그인을 업데이트합니다.");
      case NOTHING -> ComponentUtil.translate("놀랍게도 아무런 효과도 지니고 있지 않습니다.");
      case WHAT_TO_DO -> ComponentUtil.translate("너구리가 일을 했으면 좋겠군요");
      case EXPERIENCE_INTOLERANCE -> ComponentUtil.translate("경험치를 획득하는 양만큼 오히려 경험치를 잃습니다.");
      case TROLL_INVENTORY_PROPERTY, TROLL_INVENTORY_PROPERTY_MIN -> ComponentUtil.translate("인벤토리의 숫자가 자꾸 멋대로 바뀝니다.")
              .append(Component.text("\n"))
              .append(ComponentUtil.translate("잘만 이용하면 오히려 더 좋을 수도..?"));
      case MUNDANE -> ComponentUtil.translate("평범하다...");
      case AWKWARD -> ComponentUtil.translate("어... 그게.. 어색? 해 진다? 라고 생각? 합니다");
      case THICK -> ComponentUtil.translate("채팅이 진해집니다.");
      case UNCRAFTABLE -> ComponentUtil.translate("아이템을 제작할 수 없습니다.");
      case COOLDOWN_CHAT -> ComponentUtil.translate("채팅 쿨타임동안은 채팅하실 수 없습니다.");
      case COOLDOWN_ITEM_MEGAPHONE -> ComponentUtil.translate("아이템 확성기 쿨타임동안은 아이템 확성기를 사용하실 수 없습니다.");
      case SERVER_RADIO_LISTENING -> ComponentUtil.translate("서버 노래를 들어서 기분이 들떠 주는 피해량이 증가합니다.");
      case DARKNESS_TERROR -> ComponentUtil.translate("너무 어둡습니다! 받는 피해량이 30% 증가하고 블록을 캘 때마다")
              .append(Component.text("\n"))
              .append(ComponentUtil.translate("%s 확률로 받는 피해량 증가에 영향을 받는 1의 피해를 입습니다.", "&e5%"));
      case DARKNESS_TERROR_RESISTANCE -> ComponentUtil.translate("%s 효과에 대한 내성이 생깁니다.", DARKNESS_TERROR);
      case DODGE -> ComponentUtil.translate("일정 확률로 공격을 회피합니다.");
      default -> Component.empty();
    };
  }

  @NotNull
  public Component getPropertyDescription()
  {
    Component description = Component.empty();
    boolean isNegative = this.isNegative();
    boolean keepOnDeath = this.isKeepOnDeath(), keepOnQuit = this.isKeepOnQuit(), keepOnMilk = this.isKeepOnMilk();

    if (!this.getDescription().equals(Component.empty()) && (keepOnDeath || !keepOnQuit || keepOnMilk))
    {
      description = description.append(Component.text("\n"));
    }
    if (keepOnDeath)
    {
      description = description.append(Component.text("\n")).append(ComponentUtil.translate("&" + (isNegative ? "c" : "a") + "사망해도 효과가 사라지지 않습니다."));
      if (this == CustomEffectType.CURSE_OF_BEANS)
      {
        description = description.append(Component.text("\n")).append(ComponentUtil.translate("&" + (isNegative ? "c" : "a") + "사망해도 효과가 사라지지 않습니다."));
      }
    }
    if (!keepOnQuit)
    {
      description = description.append(Component.text("\n")).append(ComponentUtil.translate("&c접속을 종료하면 효과가 사라집니다."));
      if (this == CustomEffectType.CURSE_OF_BEANS)
      {
        description = description.append(Component.text("\n")).append(ComponentUtil.translate("&c접속을 종료하면 효과가 사라집니다."));
      }
    }
    if (keepOnMilk)
    {
      description = description.append(Component.text("\n")).append(ComponentUtil.translate("&" + (isNegative ? "c" : "a") + "우유를 마셔도 효과가 사라지지 않습니다."));
      if (this == CustomEffectType.CURSE_OF_BEANS)
      {
        description = description.append(Component.text("\n")).append(ComponentUtil.translate("&" + (isNegative ? "c" : "a") + "우유를 마셔도 효과가 사라지지 않습니다."));
      }
    }
    return description;
  }

  public boolean isKeepOnDeath()
  {
    return isKeepOnDeath;
  }

  public boolean isKeepOnQuit()
  {
    return isKeepOnQuit;
  }

  public boolean isKeepOnMilk()
  {
    return switch (this)
            {
              // 우유 마시면 사라짐
              case CONFUSION, NOTHING, THICK, AWKWARD, UNCRAFTABLE, MUNDANE, FROST_WALKER, FEATHER_FALLING, CHEESE_EXPERIMENT -> false;
              default -> true;
            };
  }

  public int getDefaultDuration()
  {
    return switch (this)
            {
              case RESURRECTION, CUCUMBERY_UPDATER, DARKNESS_TERROR -> -1;
              case SERVER_RADIO_LISTENING -> 2;
              case RESURRECTION_INVINCIBLE -> 20 * 2;
              case COOLDOWN_CHAT -> 20 * 3;
              case PARROTS_CHEER -> 20 * 5;
              case STOP, COOLDOWN_ITEM_MEGAPHONE -> 20 * 10;
              case RESURRECTION_COOLDOWN -> 20 * 60 * 10;
              default -> 20 * 30;
            };
  }

  @NotNull
  public List<CustomEffectType> getConflictEffects()
  {
    return switch (this)
            {
              case SHARPNESS -> Arrays.asList(CustomEffectType.SMITE, CustomEffectType.BANE_OF_ARTHROPODS);
              case SMITE -> Arrays.asList(CustomEffectType.SHARPNESS, CustomEffectType.BANE_OF_ARTHROPODS);
              case BANE_OF_ARTHROPODS -> Arrays.asList(CustomEffectType.SHARPNESS, CustomEffectType.SMITE);
              case INSIDER -> Collections.singletonList(CustomEffectType.OUTSIDER);
              case OUTSIDER -> Collections.singletonList(CustomEffectType.INSIDER);
              default -> Collections.emptyList();
            };
  }

  @SuppressWarnings("all")
  public boolean isHidden()
  {
    return switch (this)
            {
              case TROLL_INVENTORY_PROPERTY_MIN -> true;
              default -> false;
            };
  }

  public boolean isTimeHidden()
  {
    return isTimeHiddenWhenFull();
  }

  /**
   * 커스텀 효과의 지속 시간이 하나도 경과하지 않앗을 때 사간을 표시할지 말지 확인합니다.
   * @return 시간을 표시하지 않는 버프면 true 이오ㅔ에는 false
   */
  public boolean isTimeHiddenWhenFull()
  {
    return switch (this)
            {
              case PARROTS_CHEER, DARKNESS_TERROR, SERVER_RADIO_LISTENING -> true;
              default -> false;
    };
  }

  @SuppressWarnings("all")
  public boolean isRightClickRemovable()
  {
    return !isNegative() && switch (this)
            {
              case SERVER_RADIO_LISTENING -> false;
              default -> true;
            };
  }

  @Nullable
  public Color getColor()
  {
    return switch (this)
            {
              case MUTE -> Color.fromRGB(200, 100, 100);
              case AWKWARD, MUNDANE, THICK, NOTHING -> Color.fromRGB(10, 50, 255);
              default -> null;
            };
  }

  @Override
  public boolean isHiddenEnum()
  {
    return switch (this)
            {
              case COOLDOWN_CHAT, COOLDOWN_ITEM_MEGAPHONE, DARKNESS_TERROR, RESURRECTION_INVINCIBLE, RESURRECTION_COOLDOWN, SERVER_RADIO_LISTENING, PARROTS_CHEER -> true;
              default -> false;
            };
  }
}
















