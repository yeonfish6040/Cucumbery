package com.jho5245.cucumbery.customeffect;

import com.jho5245.cucumbery.customeffect.CustomEffect.DisplayType;
import com.jho5245.cucumbery.util.storage.component.util.ComponentUtil;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.translation.Translatable;
import org.bukkit.enchantments.Enchantment;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public enum CustomEffectType implements Translatable
{
  MUTE(0, true, true, true),
  CURSE_OF_MUSHROOM(999, true, true, true),
  INVINCIBLE,
  BUFF_FREEZE,
  CONFUSION(false, true, true),
  RESURRECTION,
  RESURRECTION_INVINCIBLE,
  RESURRECTION_COOLDOWN(true, true, true),
  FROST_WALKER,
  FEATHER_FALLING(9),
  BLESS_OF_SANS(9),
  METASASIS(99, false, true, true),
  PNEUMONIA(false, true, true),
  EXPERIENCE_INTOLERANCE(false, true, true),
  WHAT_TO_DO(false, true, true),
  PARROTS_CHEER,
  SHARPNESS,
  SMITE,
  BANE_OF_ARTHROPODS,
  STOP(100,false, true, true),
  KEEP_INVENTORY,
  DO_NOT_PICKUP_BUT_THROW_IT(false, true, true),
  INSIDER(9,true, true, true),
  OUTSIDER(9,true, true, true),
  CURSE_OF_BEANS(true, true, true),
  SILK_TOUCH,
  SMELTING_TOUCH,
  TELEKINESIS,








  NOTHING,
  AWKWARD,
  MUNDANE,
  NORMAL,
  UNCRAFTABLE,
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
              case WHAT_TO_DO -> "뭐히지";
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



              case NOTHING -> "아무것도 아님";
              case NORMAL -> "평범함";
              case AWKWARD -> "어색함";
              case MUNDANE -> "진함";
              case UNCRAFTABLE -> "제작 불가능함";
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
      case MUTE -> ComponentUtil.createTranslate("채팅을 할 수 없는 상태입니다.");
      case CURSE_OF_MUSHROOM -> ComponentUtil.createTranslate("농도 레벨 * 0.1% 확률로 5초마다 인벤토리에 버섯이 들어옵니다.");
      case INVINCIBLE -> ComponentUtil.createTranslate("어떠한 형태의 피해도 받지 않습니다.");
      case BUFF_FREEZE -> ComponentUtil.createTranslate("사망시 일부 버프를 제외한 버프가 사라지지 않습니다.");
      case CONFUSION -> ComponentUtil.createTranslate("방향키가 반대로 작동합니다.");
      case RESURRECTION -> ComponentUtil.createTranslate("죽음에 이르는 피해를 입었을 때, 죽지 않고")
              .append(Component.text("\n"))
              .append(ComponentUtil.createTranslate("버프를 소모하여 2초간 무적이 됩니다."));
      case RESURRECTION_INVINCIBLE -> ComponentUtil.createTranslate("2초간 무적이 됩니다.");
      case RESURRECTION_COOLDOWN -> ComponentUtil.createTranslate("%s 버프를 받을 수 없는 상태입니다.", CustomEffectType.RESURRECTION);
      case FROST_WALKER -> ComponentUtil.createTranslate("마그마 블록 위를 걸어도 피해를 입지 않습니다.");
      case FEATHER_FALLING -> ComponentUtil.createTranslate("낙하 피해를 받기 위한 최소 높이가 증가하고,")
              .append(Component.text("\n"))
              .append(ComponentUtil.createTranslate("낙하 피해량이 감소합니다."));
      case BLESS_OF_SANS, SHARPNESS -> ComponentUtil.createTranslate("근거리 공격 피해량이 증가합니다.");
      case METASASIS -> ComponentUtil.createTranslate("뭐");
      case PARROTS_CHEER -> ComponentUtil.createTranslate("HP가 5 이하일 때 15 블록 이내에 자신이 길들인 앵무새가")
              .append(Component.text("\n"))
              .append(ComponentUtil.createTranslate("있으면 받는 피해가 45% 감소하고 주는 피해량이 10% 증가합니다."));
      case SMITE -> ComponentUtil.createTranslate("언데드 개체에게 주는 근거리 공격 피해량이 증가합니다.");
      case BANE_OF_ARTHROPODS ->
              ComponentUtil.createTranslate("절지동물류 개체에게 주는 근거리 공격 피해량이 증가하고,")
                      .append(Component.text("\n"))
                      .append(ComponentUtil.createTranslate("%s 효과를 지급합니다.", ComponentUtil.createTranslate("effect.minecraft.slowness")));
      case STOP ->
              ComponentUtil.createTranslate("모든 행동을 할 수 없는 상태입니다.")
                      .append(Component.text("\n"))
                      .append(ComponentUtil.createTranslate("좌우이동, 웅크리기를 할 때마다 지속 시간이 6초씩 감소합니다."));
      case KEEP_INVENTORY -> ComponentUtil.createTranslate("죽어도 아이템을 떨어뜨리지 않습니다.");
      case DO_NOT_PICKUP_BUT_THROW_IT -> ComponentUtil.createTranslate("아이템을 줍는 대신 던집니다.");
      case INSIDER ->
              ComponentUtil.createTranslate("채팅이 여러번 입력되고, 죽을 때 모든 플레이어에게")
              .append(Component.text("\n"))
              .append(ComponentUtil.createTranslate("타이틀로 자신의 데스 메시지를 띄워줍니다."));
      case OUTSIDER ->
              ComponentUtil.createTranslate("일정 확률로 채팅 메시지가 보내지지 않고")
              .append(Component.text("\n"))
              .append(ComponentUtil.createTranslate("입장 메시지, 퇴장 메시지가 뜨지 않습니다."));
      case CURSE_OF_BEANS ->
              ComponentUtil.createTranslate("뭔가.. 자꾸.. 2번씩 일어난다.")
                      .append(Component.text("\n"))
                      .append(ComponentUtil.createTranslate("뭔가.. 자꾸.. 2번씩 일어난다."));
      case SILK_TOUCH ->
              ComponentUtil.createTranslate("맨손으로 블록을 캐면 섬세한 손길 마법과")
              .append(Component.text("\n"))
              .append(ComponentUtil.createTranslate("동일하게 아이템을 얻을 수 있습니다."));



      default -> Component.empty();
    };
  }

  @NotNull
  public Component getPropertyDescription()
  {
    Component description = Component.empty();
    boolean isNegative = this.isNegative();
    boolean keepOnDeath = this.isKeepOnDeath(), keepOnQuit = this.isKeepOnQuit(), keepOnMilk = this.isKeepOnMilk();

    if (keepOnDeath || !keepOnQuit || keepOnMilk)
    {
      description = description.append(Component.text("\n"));
    }
    if (keepOnDeath)
    {
      description = description.append(Component.text("\n")).append(ComponentUtil.createTranslate("&" + (isNegative ? "c" : "a") + "사망해도 효과가 사라지지 않습니다."));
      if (this == CustomEffectType.CURSE_OF_BEANS)
      {
        description = description.append(Component.text("\n")).append(ComponentUtil.createTranslate("&" + (isNegative ? "c" : "a") + "사망해도 효과가 사라지지 않습니다."));
      }
    }
    if (!keepOnQuit)
    {
      description = description.append(Component.text("\n")).append(ComponentUtil.createTranslate("&c접속을 종료하면 효과가 사라집니다."));
      if (this == CustomEffectType.CURSE_OF_BEANS)
      {
        description = description.append(Component.text("\n")).append(ComponentUtil.createTranslate("&c접속을 종료하면 효과가 사라집니다."));
      }
    }
    if (keepOnMilk)
    {
      description = description.append(Component.text("\n")).append(ComponentUtil.createTranslate("&" + (isNegative ? "c" : "a") + "우유를 마셔도 효과가 사라지지 않습니다."));
      if (this == CustomEffectType.CURSE_OF_BEANS)
      {
        description = description.append(Component.text("\n")).append(ComponentUtil.createTranslate("&" + (isNegative ? "c" : "a") + "우유를 마셔도 효과가 사라지지 않습니다."));
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
              case CONFUSION, NOTHING, NORMAL, AWKWARD, UNCRAFTABLE, MUNDANE, FROST_WALKER, FEATHER_FALLING -> false;
              default -> true;
            };
  }

  public int getDefaultDuration()
  {
    return switch (this)
            {
              case RESURRECTION -> -1;
              case RESURRECTION_INVINCIBLE -> 20 * 2;
              case PARROTS_CHEER -> 20 * 5;
              case STOP -> 20 * 10;
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
              default -> Collections.emptyList();
            };
  }
}
















