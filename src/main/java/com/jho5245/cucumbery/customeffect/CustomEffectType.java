package com.jho5245.cucumbery.customeffect;

import com.jho5245.cucumbery.customeffect.CustomEffect.DisplayType;
import com.jho5245.cucumbery.util.storage.component.util.ComponentUtil;
import com.jho5245.cucumbery.util.storage.data.EnumHideable;
import com.jho5245.cucumbery.util.storage.data.TranslatableKeyParser;
import com.jho5245.cucumbery.util.storage.data.custom_enchant.CustomEnchant;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.translation.Translatable;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public enum CustomEffectType implements Translatable, EnumHideable
{
  /**
   * 어색함
   */
  AWKWARD,
  /**
   * 살충
   */
  BANE_OF_ARTHROPODS(255),
  /**
   * 샌즈의 축복
   */
  BLESS_OF_SANS(9),
  /**
   * 버프 보호
   */
  BUFF_FREEZE,
  /**
   * 치즈 실험
   */
  CHEESE_EXPERIMENT(false, true, true),
  /**
   * 혼란
   */
  CONFUSION(false, true, true),
  /**
   * 관전 지속
   */
  CONTINUAL_SPECTATING(true, true, false),
  /**
   * 채팅 쿨타임
   */
  COOLDOWN_CHAT(true, true, true),
  /**
   * 아아템 확성기 쿨타임
   */
  COOLDOWN_ITEM_MEGAPHONE(true, true, true),
  /**
   * 즉시 큐컴버리 업데이트
   */
  CUCUMBERY_UPDATER(1),
  /**
   * 콩의 저주 콩의 저주
   */
  CURSE_OF_BEANS(true, true, true),
  /**
   * 섭식 장애
   */
  CURSE_OF_CONSUMPTION(true, true, true),
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
   * 버리기 불가능
   */
  CURSE_OF_DROP(true, true, true),
  /**
   * 인벤세이브 저주
   */
  CURSE_OF_INVENTORY(false, true, true),
  /**
   * 점프 불가능
   */
  CURSE_OF_JUMPING(true, true, true),
  /**
   * 버섯의 저주
   */
  CURSE_OF_MUSHROOM(999, true, true, true),
  /**
   * 줍기 불가능
   */
  CURSE_OF_PICKUP(true, true, true),
  /**
   * 어둠의 공포
   */
  DARKNESS_TERROR(false, true, true),
  /**
   * 어둠의 공포
   */
  DARKNESS_TERROR_ACTIVATED(false, true, true),
  /**
   * 어둠의 공포 내성
   */
  DARKNESS_TERROR_RESISTANCE,
  /**
   * 디버그 염탐
   */
  DEBUG_WATCHER(true, true, false),
  /**
   * 회피
   */
  DODGE(99),
  /**
   * 줍지 마, 던져!
   */
  DO_NOT_PICKUP_BUT_THROW_IT(9, false, true, true),
  /**
   * 겉날개 부스터
   */
  ELYTRA_BOOSTER(9),
  /**
   * 불내증
   */
  EXPERIENCE_INTOLERANCE(false, true, true),
  /**
   * 화려한 조명
   */
  FANCY_SPOTLIGHT(false, false, false),
  /**
   * 화려한 조명 효과
   */
  FANCY_SPOTLIGHT_ACTIVATED(false, false, false),
  /**
   * 가벼운 착지
   */
  FEATHER_FALLING(9),
  /**
   * 차가운 걸음
   */
  FROST_WALKER,
  /**
   * HP 증가
   */
  HEALTH_INCREASE(99),
  /**
   * 영웅의 메아리
   */
  HEROS_ECHO(false, false, false),
  /**
   * 영웅의 메아리
   */
  HEROS_ECHO_OTHERS(false, false, false),
  /**
   * 똥손
   */
  IDIOT_SHOOTER(19, true, true, true),
  /**
   * 인싸
   */
  INSIDER(9, true, true, true),
  /**
   * 무적
   */
  INVINCIBLE,
  /**
   * 플러그인 리로드 무적
   */
  INVINCIBLE_PLUGIN_RELOAD(true, false, false),
  /**
   * 리스폰 무적
   */
  INVINCIBLE_RESPAWN(true, false, false),
  /**
   * 인벤토리 보호
   */
  KEEP_INVENTORY,
  /**
   * 운동 에너지 저항
   */
  KINETIC_RESISTANCE(9),
  /**
   * 넉백 저항
   */
  KNOCKBACK_RESISTANCE(99),
  /**
   * 넉백 저항 (PvP)
   */
  KNOCKBACK_RESISTANCE_COMBAT(99),
  /**
   * 넉백 저항 (PvE)
   */
  KNOCKBACK_RESISTANCE_NON_COMBAT(99),
  /**
   * 공중 부양 저항
   */
  LEVITATION_RESISTANCE(9),
  /**
   * 전이
   */
  METASASIS(99, false, true, true),
  MINECRAFT_ABSORPTION(255),
  MINECRAFT_BAD_OMEN(255, true),
  MINECRAFT_BLINDNESS(255, true),
  MINECRAFT_CONDUIT_POWER(255),
  MINECRAFT_DOLPHINS_GRACE(255),
  MINECRAFT_FIRE_RESISTANCE(255),
  MINECRAFT_GLOWING(255, true),
  MINECRAFT_HASTE(255),
  MINECRAFT_HEALTH_BOOST(255),
  MINECRAFT_HERO_OF_THE_VILLAGE(255),
  MINECRAFT_HUNGER(255, true),
  MINECRAFT_INSTANT_DAMAGE(255, true),
  MINECRAFT_INSTANT_HEAL(255),
  MINECRAFT_INVISIBILITY(255),
  MINECRAFT_JUMP_BOOST(255),
  MINECRAFT_LEVITATION(255, true),
  MINECRAFT_LUCK(255),
  MINECRAFT_MINING_FATIGUE(255, true),
  MINECRAFT_NAUSEA(255, true),
  MINECRAFT_NIGHT_VISION(255),
  MINECRAFT_POISON(255, true),
  MINECRAFT_REGENERATION(255),
  MINECRAFT_RESISTANCE(255),
  MINECRAFT_SATURATION(255),
  MINECRAFT_SLOWNESS(255, true),
  MINECRAFT_SLOW_FALLING(255),
  MINECRAFT_SPEED(255),
  MINECRAFT_STRENGTH(255),
  MINECRAFT_UNLUCK(255, true),
  MINECRAFT_WATER_BREATHING(255),
  MINECRAFT_WEAKNESS(255, true),
  MINECRAFT_WITHER(255, true),
  /**
   * 평범함
   */
  MUNDANE,
  /**
   * 채팅 금지
   */
  MUTE(0, true, true, true),
  /**
   * 뉴비 보호막
   */
  NEWBIE_SHIELD(2, true, true, false),
  /**
   * 아무것도 아님
   */
  NOTHING,
  /**
   * 아싸
   */
  OUTSIDER(9, true, true, true),
  /**
   * 앵무새의 가호
   */
  PARROTS_CHEER,
  /**
   * 폐렴
   */
  PNEUMONIA(false, true, true),
  /**
   * 리저렉션
   */
  RESURRECTION,
  /**
   * 리저렉션 쿨타임
   */
  RESURRECTION_COOLDOWN(true, true, true),
  /**
   * 리저렉션 무적
   */
  RESURRECTION_INVINCIBLE,
  /**
   * 서버 라디오 분위기
   */
  SERVER_RADIO_LISTENING(2, true, false, false),
  /**
   * 날카로움
   */
  SHARPNESS(255),
  /**
   * 섬세한 손길
   */
  SILK_TOUCH,
  /**
   * 제련의 손길
   */
  SMELTING_TOUCH,
  /**
   * 강타
   */
  SMITE(255),
  /**
   * 정지
   */
  STOP(100, false, true, true),
  /**
   * 염력
   */
  TELEKINESIS,
  /**
   * 진함
   */
  THICK,
  /**
   * 인벤토리 트롤
   */
  TROLL_INVENTORY_PROPERTY(Integer.MAX_VALUE - 1, true, true, true),
  /**
   * 인벤토리 트롤
   */
  TROLL_INVENTORY_PROPERTY_MIN(Integer.MAX_VALUE - 1, true, true, true),
  /**
   * 찐 투명화
   */
  TRUE_INVISIBILITY,
  /**
   * 제작 불가능함
   */
  UNCRAFTABLE(2, false, true, true),
  /**
   * 와 샌즈
   */
  WA_SANS(9),
  /**
   * 뭐하지
   */
  WHAT_TO_DO(false, true, true),
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
              case RESURRECTION_COOLDOWN -> "리저렉션 저주";
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
              case DARKNESS_TERROR -> "어두운거 싫어요";
              case DARKNESS_TERROR_ACTIVATED -> "어둠의 공포";
              case DARKNESS_TERROR_RESISTANCE -> "어둠의 공포 내성";
              case COOLDOWN_CHAT -> "채팅 쿨타임";
              case COOLDOWN_ITEM_MEGAPHONE -> "아이템 확성기 쿨타임";
              case DODGE -> "회피";
              case KNOCKBACK_RESISTANCE -> "넉백 저항";
              case KNOCKBACK_RESISTANCE_COMBAT -> "넉백 저항(전투형)";
              case KNOCKBACK_RESISTANCE_NON_COMBAT -> "넉백 저항 (비전투형)";
              case NEWBIE_SHIELD -> "뉴비 보호막";
              case INVINCIBLE_PLUGIN_RELOAD -> "플러그인 리로드 무적";
              case INVINCIBLE_RESPAWN -> "리스폰 무적";
              case HEROS_ECHO, HEROS_ECHO_OTHERS -> "영웅의 메아리";
              case FANCY_SPOTLIGHT -> "화려한 조명";
              case FANCY_SPOTLIGHT_ACTIVATED -> "화려한 조명 효과";
              case WA_SANS -> "와 샌즈";
              case HEALTH_INCREASE -> "HP 증가";
              case CONTINUAL_SPECTATING -> "관전 지속";
              case TRUE_INVISIBILITY -> "찐 투명화";
              case MINECRAFT_SPEED -> TranslatableKeyParser.getKey(PotionEffectType.SPEED);
              case MINECRAFT_SLOWNESS -> TranslatableKeyParser.getKey(PotionEffectType.SLOW);
              case MINECRAFT_HASTE -> TranslatableKeyParser.getKey(PotionEffectType.FAST_DIGGING);
              case MINECRAFT_MINING_FATIGUE -> TranslatableKeyParser.getKey(PotionEffectType.SLOW_DIGGING);
              case MINECRAFT_STRENGTH -> TranslatableKeyParser.getKey(PotionEffectType.INCREASE_DAMAGE);
              case MINECRAFT_WEAKNESS -> TranslatableKeyParser.getKey(PotionEffectType.WEAKNESS);
              case MINECRAFT_INSTANT_DAMAGE -> TranslatableKeyParser.getKey(PotionEffectType.HARM);
              case MINECRAFT_INSTANT_HEAL -> TranslatableKeyParser.getKey(PotionEffectType.HEAL);
              case MINECRAFT_JUMP_BOOST -> TranslatableKeyParser.getKey(PotionEffectType.JUMP);
              case MINECRAFT_NAUSEA -> TranslatableKeyParser.getKey(PotionEffectType.CONFUSION);
              case MINECRAFT_REGENERATION -> TranslatableKeyParser.getKey(PotionEffectType.REGENERATION);
              case MINECRAFT_RESISTANCE -> TranslatableKeyParser.getKey(PotionEffectType.DAMAGE_RESISTANCE);
              case MINECRAFT_FIRE_RESISTANCE -> TranslatableKeyParser.getKey(PotionEffectType.FIRE_RESISTANCE);
              case MINECRAFT_WATER_BREATHING -> TranslatableKeyParser.getKey(PotionEffectType.WATER_BREATHING);
              case MINECRAFT_BLINDNESS -> TranslatableKeyParser.getKey(PotionEffectType.BLINDNESS);
              case MINECRAFT_INVISIBILITY -> TranslatableKeyParser.getKey(PotionEffectType.INVISIBILITY);
              case MINECRAFT_NIGHT_VISION -> TranslatableKeyParser.getKey(PotionEffectType.NIGHT_VISION);
              case MINECRAFT_HUNGER -> TranslatableKeyParser.getKey(PotionEffectType.HUNGER);
              case MINECRAFT_POISON -> TranslatableKeyParser.getKey(PotionEffectType.POISON);
              case MINECRAFT_WITHER -> TranslatableKeyParser.getKey(PotionEffectType.WITHER);
              case MINECRAFT_HEALTH_BOOST -> TranslatableKeyParser.getKey(PotionEffectType.HEALTH_BOOST);
              case MINECRAFT_ABSORPTION -> TranslatableKeyParser.getKey(PotionEffectType.ABSORPTION);
              case MINECRAFT_SATURATION -> TranslatableKeyParser.getKey(PotionEffectType.SATURATION);
              case MINECRAFT_LEVITATION -> TranslatableKeyParser.getKey(PotionEffectType.LEVITATION);
              case MINECRAFT_SLOW_FALLING -> TranslatableKeyParser.getKey(PotionEffectType.SLOW_FALLING);
              case MINECRAFT_GLOWING -> TranslatableKeyParser.getKey(PotionEffectType.GLOWING);
              case MINECRAFT_LUCK -> TranslatableKeyParser.getKey(PotionEffectType.LUCK);
              case MINECRAFT_UNLUCK -> TranslatableKeyParser.getKey(PotionEffectType.UNLUCK);
              case MINECRAFT_CONDUIT_POWER -> TranslatableKeyParser.getKey(PotionEffectType.CONDUIT_POWER);
              case MINECRAFT_DOLPHINS_GRACE -> TranslatableKeyParser.getKey(PotionEffectType.DOLPHINS_GRACE);
              case MINECRAFT_BAD_OMEN -> TranslatableKeyParser.getKey(PotionEffectType.BAD_OMEN);
              case MINECRAFT_HERO_OF_THE_VILLAGE -> TranslatableKeyParser.getKey(PotionEffectType.HERO_OF_THE_VILLAGE);
            };
  }

  @NotNull
  public Component getDescription()
  {
    return switch (this)
            {
              case MUTE -> ComponentUtil.translate("채팅을 할 수 없는 상태입니다");
              case CURSE_OF_MUSHROOM -> ComponentUtil.translate("농도 레벨 * 0.1% 확률로 5초마다 인벤토리에 버섯이 들어옵니다");
              case INVINCIBLE -> ComponentUtil.translate("어떠한 형태의 피해도 받지 않습니다");
              case BUFF_FREEZE -> ComponentUtil.translate("사망시 버프를 소모하여 일부 버프를 제외한 버프가 사라지지 않습니다");
              case CONFUSION -> ComponentUtil.translate("방향키가 반대로 작동합니다");
              case RESURRECTION -> ComponentUtil.translate("죽음에 이르는 피해를 입었을 때, 죽지 않고")
                      .append(Component.text("\n"))
                      .append(ComponentUtil.translate("버프를 소모하여 2초간 무적이 됩니다"));
              case RESURRECTION_INVINCIBLE -> ComponentUtil.translate("2초간 무적이 됩니다");
              case RESURRECTION_COOLDOWN -> ComponentUtil.translate("%s 버프를 받을 수 없는 상태입니다", CustomEffectType.RESURRECTION);
              case FROST_WALKER -> ComponentUtil.translate("마그마 블록 위를 걸어도 피해를 입지 않습니다");
              case FEATHER_FALLING -> ComponentUtil.translate("낙하 피해를 받기 위한 최소 높이가 증가하고,")
                      .append(Component.text("\n"))
                      .append(ComponentUtil.translate("낙하 피해량이 감소합니다"));
              case BLESS_OF_SANS, SHARPNESS -> ComponentUtil.translate("근거리 공격 피해량이 증가합니다");
              case METASASIS -> ComponentUtil.translate("뭐");
              case PARROTS_CHEER -> ComponentUtil.translate("HP가 5 이하일 때 15 블록 이내에 자신이 길들인 앵무새가")
                      .append(Component.text("\n"))
                      .append(ComponentUtil.translate("있으면 받는 피해가 45% 감소하고 주는 피해량이 10% 증가합니다"));
              case SMITE -> ComponentUtil.translate("언데드 개체에게 주는 근거리 공격 피해량이 증가합니다");
              case BANE_OF_ARTHROPODS -> ComponentUtil.translate("절지동물류 개체에게 주는 근거리 공격 피해량이 증가하고,")
                      .append(Component.text("\n"))
                      .append(ComponentUtil.translate("%s 효과를 지급합니다", ComponentUtil.translate("effect.minecraft.slowness")));
              case STOP -> ComponentUtil.translate("모든 행동을 할 수 없는 상태입니다")
                      .append(Component.text("\n"))
                      .append(ComponentUtil.translate("좌우이동, 웅크리기를 할 때마다 지속 시간이 6초씩 감소합니다"));
              case KEEP_INVENTORY -> ComponentUtil.translate("죽어도 아이템을 떨어뜨리지 않습니다");
              case DO_NOT_PICKUP_BUT_THROW_IT -> ComponentUtil.translate("아이템을 줍는 대신 던집니다")
                      .append(Component.text("\n"))
                      .append(ComponentUtil.translate("농도 레벨이 높을 수록 더 멀리 던집니다"));
              case INSIDER -> ComponentUtil.translate("채팅이 여러번 입력되고, 죽을 때 모든 플레이어에게")
                      .append(Component.text("\n"))
                      .append(ComponentUtil.translate("타이틀로 자신의 데스 메시지를 띄워줍니다"));
              case OUTSIDER -> ComponentUtil.translate("일정 확률로 채팅 메시지가 보내지지 않고")
                      .append(Component.text("\n"))
                      .append(ComponentUtil.translate("입장 메시지, 퇴장 메시지가 뜨지 않습니다"));
              case CURSE_OF_BEANS -> ComponentUtil.translate("뭔가.. 자꾸.. 2번씩 일어난다")
                      .append(Component.text("\n"))
                      .append(ComponentUtil.translate("뭔가.. 자꾸.. 2번씩 일어난다"));
              case SILK_TOUCH -> ComponentUtil.translate("블록을 캐면 섬세한 손길 마법과")
                      .append(Component.text("\n"))
                      .append(ComponentUtil.translate("동일하게 아이템을 얻을 수 있습니다"));
              case TELEKINESIS -> ComponentUtil.translate("블록을 캐거나 적을 잡았을 때 드롭하는")
                      .append(Component.text("\n"))
                      .append(ComponentUtil.translate("아이템과 경험치가 즉시 인벤토리에 들어옵니다"));
              case SMELTING_TOUCH -> ComponentUtil.translate("블록을 캐거나 적을 잡았을 때 드롭하는")
                      .append(Component.text("\n"))
                      .append(ComponentUtil.translate("아이템을 제련된 형태로 바꿔줍니다"));
              case CURSE_OF_INVENTORY -> ComponentUtil.translate("죽으면 가지고 있는 모든 아이템을 떨어뜨립니다");
              case CURSE_OF_CREATIVITY -> ComponentUtil.translate("블록을 설치하거나 파괴할 수 없습니다");
              case CURSE_OF_CREATIVITY_BREAK -> ComponentUtil.translate("블록을 파괴할 수 없습니다");
              case CURSE_OF_CREATIVITY_PLACE -> ComponentUtil.translate("블록을 설치할 수 없습니다");
              case CURSE_OF_CONSUMPTION -> ComponentUtil.translate("음식이나 포션을 사용할 수 없습니다");
              case CURSE_OF_PICKUP -> ComponentUtil.translate("아이템을 주울 수 없습니다");
              case CURSE_OF_DROP -> ComponentUtil.translate("아이템을 버릴 수 없습니다");
              case CURSE_OF_JUMPING -> ComponentUtil.translate("점프를 할 수 없습니다");
              case KINETIC_RESISTANCE -> ComponentUtil.translate("겉날개 활강 중 블록에 부딪혀서 받는 피해량이 감소됩니다")
                      .append(Component.text("\n"))
                      .append(ComponentUtil.translate("낙하 피해량은 감소되지 않습니다"));
              case ELYTRA_BOOSTER -> ComponentUtil.translate("겉날개 활강 중 폭죽으로 가속할 때")
                      .append(Component.text("\n"))
                      .append(ComponentUtil.translate("일정 확률로 폭죽을 소비하지 않습니다"));
              case LEVITATION_RESISTANCE -> ComponentUtil.translate("셜커에게 공격받아도 일정 확률로")
                      .append(Component.text("\n"))
                      .append(ComponentUtil.translate("공중 부양 상태 효과가 적용되지 않습니다"));
              case CHEESE_EXPERIMENT -> ComponentUtil.translate("우유를 마시면 효과가 사라지고 멀미가 30초간 지속됩니다")
                      .append(Component.text("\n"))
                      .append(ComponentUtil.translate("또한, 80% 확률로 허기가 30초 지속됩니다"));
              case IDIOT_SHOOTER -> ComponentUtil.translate("발사체가 이상한 방향으로 날아갑니다");
              case DEBUG_WATCHER -> ComponentUtil.translate("플러그인 디버그 메시지를 볼 수 있게 됩니다");
              case CUCUMBERY_UPDATER -> ComponentUtil.translate("큐컴버리 플러그인을 업데이트합니다");
              case NOTHING -> ComponentUtil.translate("놀랍게도 아무런 효과도 지니고 있지 않습니다");
              case WHAT_TO_DO -> ComponentUtil.translate("너구리가 일을 했으면 좋겠군요");
              case EXPERIENCE_INTOLERANCE -> ComponentUtil.translate("경험치를 획득하는 양만큼 오히려 경험치를 잃습니다");
              case TROLL_INVENTORY_PROPERTY, TROLL_INVENTORY_PROPERTY_MIN -> ComponentUtil.translate("인벤토리의 숫자가 자꾸 멋대로 바뀝니다")
                      .append(Component.text("\n"))
                      .append(ComponentUtil.translate("잘만 이용하면 오히려 더 좋을 수도..?"));
              case MUNDANE -> ComponentUtil.translate("평범하다...");
              case AWKWARD -> ComponentUtil.translate("어... 그게.. 어색? 해 진다? 라고 생각? 합니다");
              case THICK -> ComponentUtil.translate("채팅이 진해집니다");
              case UNCRAFTABLE -> ComponentUtil.translate("아이템을 제작할 수 없습니다");
              case COOLDOWN_CHAT -> ComponentUtil.translate("채팅 쿨타임동안은 채팅하실 수 없습니다");
              case COOLDOWN_ITEM_MEGAPHONE -> ComponentUtil.translate("아이템 확성기 쿨타임동안은 아이템 확성기를 사용하실 수 없습니다");
              case SERVER_RADIO_LISTENING -> ComponentUtil.translate("서버 노래를 들어서 기분이 들떠 주는 피해량이 증가합니다");
              case DARKNESS_TERROR -> ComponentUtil.translate("어두운거.. 무섭다..")
                      .append(Component.text("\n"))
                      .append(ComponentUtil.translate("%s 효과나 손에 빛을 내는 아이템 없이 어두운 곳에 가면 %s 효과가 걸립니다",
                              Component.translatable(TranslatableKeyParser.getKey(PotionEffectType.NIGHT_VISION), NamedTextColor.GREEN), DARKNESS_TERROR_ACTIVATED));
              case DARKNESS_TERROR_ACTIVATED -> ComponentUtil.translate("너무 어둡습니다! 받는 피해량이 30% 증가하고 블록을 캘 때마다")
                      .append(Component.text("\n"))
                      .append(ComponentUtil.translate("%s 확률로 받는 피해량 증가에 영향을 받는 1의 피해를 입습니다", "&e5%"));
              case DARKNESS_TERROR_RESISTANCE -> ComponentUtil.translate("%s 효과에 대한 내성이 생깁니다", DARKNESS_TERROR_ACTIVATED);
              case DODGE -> ComponentUtil.translate("일정 확률로 공격을 회피합니다");
              case NEWBIE_SHIELD -> ComponentUtil.translate("플레이 시간이 1시간 미만인 당신!")
                      .append(Component.text("\n"))
                      .append(ComponentUtil.translate("받는 피해량이 감소하고 주는 피해량이 증가합니다"))
                      .append(Component.text("\n"))
                      .append(ComponentUtil.translate("플레이 시간이 증가할 수록 효과가 감소하고 1시간이 지나면 효과가 사라집니다"));
              case INVINCIBLE_PLUGIN_RELOAD -> ComponentUtil.translate("플러그인을 리도드하는 중입니다");
              case INVINCIBLE_RESPAWN -> ComponentUtil.translate("리스폰 무적 상태입니다");
              case HEROS_ECHO, HEROS_ECHO_OTHERS -> ComponentUtil.translate("최종 대미지가 5% 증가합니다");
              case FANCY_SPOTLIGHT -> ComponentUtil.translate("화려한 조명이 나를 비추네~")
                      .append(Component.text("\n"))
                      .append(ComponentUtil.translate("주변에 충분히 밝은 블록이 있으면 %s와(과) %s이(가) 적용됩니다",
                              Component.translatable(TranslatableKeyParser.getKey(PotionEffectType.SPEED), NamedTextColor.GREEN), Component.translatable(TranslatableKeyParser.getKey(PotionEffectType.REGENERATION), NamedTextColor.GREEN)));
              case FANCY_SPOTLIGHT_ACTIVATED -> ComponentUtil.translate("주변에 충분히 밝은 블록이 있어 %s와(과) %s이(가) 적용됩니다",
                      Component.translatable(TranslatableKeyParser.getKey(PotionEffectType.SPEED), NamedTextColor.GREEN), Component.translatable(TranslatableKeyParser.getKey(PotionEffectType.REGENERATION), NamedTextColor.GREEN));
              case WA_SANS -> ComponentUtil.translate("스켈레톤 유형의 개체에게 주는")
                      .append(Component.text("\n"))
                      .append(ComponentUtil.translate("피해량이 증가하고 받는 피해량이 감소합니다"));
              case HEALTH_INCREASE -> ComponentUtil.translate("최대 HP가 증가합니다");
              case CONTINUAL_SPECTATING -> ComponentUtil.translate("플레이어를 지속적으로 관전합니다")
                      .append(Component.text("\n"))
                      .append(ComponentUtil.translate("해당 플레이어가 재접속하거나 리스폰해도 자동으로 관전합니다"));
              case TRUE_INVISIBILITY -> ComponentUtil.translate("말 그대로 완전히 다른 플레이어로부터 보이지 않습니다");
              case MINECRAFT_SPEED -> VanillaEffectDescription.getDescription(PotionEffectType.SPEED);
              case MINECRAFT_SLOWNESS -> VanillaEffectDescription.getDescription(PotionEffectType.SLOW);
              case MINECRAFT_HASTE -> VanillaEffectDescription.getDescription(PotionEffectType.FAST_DIGGING);
              case MINECRAFT_MINING_FATIGUE -> VanillaEffectDescription.getDescription(PotionEffectType.SLOW_DIGGING);
              case MINECRAFT_STRENGTH -> VanillaEffectDescription.getDescription(PotionEffectType.INCREASE_DAMAGE);
              case MINECRAFT_WEAKNESS -> VanillaEffectDescription.getDescription(PotionEffectType.WEAKNESS);
              case MINECRAFT_INSTANT_DAMAGE -> VanillaEffectDescription.getDescription(PotionEffectType.HARM);
              case MINECRAFT_INSTANT_HEAL -> VanillaEffectDescription.getDescription(PotionEffectType.HEAL);
              case MINECRAFT_JUMP_BOOST -> VanillaEffectDescription.getDescription(PotionEffectType.JUMP);
              case MINECRAFT_NAUSEA -> VanillaEffectDescription.getDescription(PotionEffectType.CONFUSION);
              case MINECRAFT_REGENERATION -> VanillaEffectDescription.getDescription(PotionEffectType.REGENERATION);
              case MINECRAFT_RESISTANCE -> VanillaEffectDescription.getDescription(PotionEffectType.DAMAGE_RESISTANCE);
              case MINECRAFT_FIRE_RESISTANCE -> VanillaEffectDescription.getDescription(PotionEffectType.FIRE_RESISTANCE);
              case MINECRAFT_WATER_BREATHING -> VanillaEffectDescription.getDescription(PotionEffectType.WATER_BREATHING);
              case MINECRAFT_BLINDNESS -> VanillaEffectDescription.getDescription(PotionEffectType.BLINDNESS);
              case MINECRAFT_INVISIBILITY -> VanillaEffectDescription.getDescription(PotionEffectType.INVISIBILITY);
              case MINECRAFT_NIGHT_VISION -> VanillaEffectDescription.getDescription(PotionEffectType.NIGHT_VISION);
              case MINECRAFT_HUNGER -> VanillaEffectDescription.getDescription(PotionEffectType.HUNGER);
              case MINECRAFT_POISON -> VanillaEffectDescription.getDescription(PotionEffectType.POISON);
              case MINECRAFT_WITHER -> VanillaEffectDescription.getDescription(PotionEffectType.WITHER);
              case MINECRAFT_HEALTH_BOOST -> VanillaEffectDescription.getDescription(PotionEffectType.HEALTH_BOOST);
              case MINECRAFT_ABSORPTION -> VanillaEffectDescription.getDescription(PotionEffectType.ABSORPTION);
              case MINECRAFT_SATURATION -> VanillaEffectDescription.getDescription(PotionEffectType.SATURATION);
              case MINECRAFT_LEVITATION -> VanillaEffectDescription.getDescription(PotionEffectType.LEVITATION);
              case MINECRAFT_SLOW_FALLING -> VanillaEffectDescription.getDescription(PotionEffectType.SLOW_FALLING);
              case MINECRAFT_GLOWING -> VanillaEffectDescription.getDescription(PotionEffectType.GLOWING);
              case MINECRAFT_LUCK -> VanillaEffectDescription.getDescription(PotionEffectType.LUCK);
              case MINECRAFT_UNLUCK -> VanillaEffectDescription.getDescription(PotionEffectType.UNLUCK);
              case MINECRAFT_CONDUIT_POWER -> VanillaEffectDescription.getDescription(PotionEffectType.CONDUIT_POWER);
              case MINECRAFT_DOLPHINS_GRACE -> VanillaEffectDescription.getDescription(PotionEffectType.DOLPHINS_GRACE);
              case MINECRAFT_BAD_OMEN -> VanillaEffectDescription.getDescription(PotionEffectType.BAD_OMEN);
              case MINECRAFT_HERO_OF_THE_VILLAGE -> VanillaEffectDescription.getDescription(PotionEffectType.HERO_OF_THE_VILLAGE);
              default -> Component.empty();
            };
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
    return !isInstant() && switch (this)
            {
              // 우유 마시면 사라짐
              case CONFUSION, NOTHING, THICK, AWKWARD, UNCRAFTABLE, MUNDANE, FROST_WALKER, FEATHER_FALLING, CHEESE_EXPERIMENT, TRUE_INVISIBILITY -> false;
              default -> true;
            };
  }

  public boolean isInstant()
  {
    return switch (this)
            {
              case CUCUMBERY_UPDATER, MINECRAFT_SPEED, MINECRAFT_SLOWNESS, MINECRAFT_HASTE, MINECRAFT_MINING_FATIGUE, MINECRAFT_STRENGTH, MINECRAFT_WEAKNESS, MINECRAFT_INSTANT_DAMAGE, MINECRAFT_INSTANT_HEAL,
                      MINECRAFT_JUMP_BOOST, MINECRAFT_NAUSEA, MINECRAFT_REGENERATION, MINECRAFT_RESISTANCE, MINECRAFT_FIRE_RESISTANCE, MINECRAFT_WATER_BREATHING,
                      MINECRAFT_BLINDNESS, MINECRAFT_INVISIBILITY, MINECRAFT_NIGHT_VISION, MINECRAFT_HUNGER, MINECRAFT_POISON, MINECRAFT_WITHER, MINECRAFT_HEALTH_BOOST,
                      MINECRAFT_ABSORPTION, MINECRAFT_SATURATION, MINECRAFT_LEVITATION, MINECRAFT_SLOW_FALLING, MINECRAFT_GLOWING, MINECRAFT_LUCK, MINECRAFT_UNLUCK,
                      MINECRAFT_CONDUIT_POWER, MINECRAFT_DOLPHINS_GRACE, MINECRAFT_BAD_OMEN, MINECRAFT_HERO_OF_THE_VILLAGE -> true;
              default -> false;
            };
  }

  public int getDefaultDuration()
  {
    return switch (this)
            {
              case RESURRECTION, CUCUMBERY_UPDATER, DARKNESS_TERROR_ACTIVATED, CONTINUAL_SPECTATING -> -1;
              case SERVER_RADIO_LISTENING, NEWBIE_SHIELD, FANCY_SPOTLIGHT_ACTIVATED -> 2;
              case RESURRECTION_INVINCIBLE -> 20 * 2;
              case COOLDOWN_CHAT -> 20 * 3;
              case PARROTS_CHEER, INVINCIBLE_PLUGIN_RELOAD -> 20 * 5;
              case STOP, COOLDOWN_ITEM_MEGAPHONE, INVINCIBLE_RESPAWN -> 20 * 10;
              case RESURRECTION_COOLDOWN -> 20 * 60 * 10;
              case HEROS_ECHO, HEROS_ECHO_OTHERS -> 20 * 60 * 60 * 2;
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
              case HEROS_ECHO -> Collections.singletonList(CustomEffectType.HEROS_ECHO_OTHERS);
              case HEROS_ECHO_OTHERS -> Collections.singletonList(CustomEffectType.HEROS_ECHO);
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
    return isTimeHiddenWhenFull() || switch (this)
            {
              case DARKNESS_TERROR_ACTIVATED, SERVER_RADIO_LISTENING, NEWBIE_SHIELD, FANCY_SPOTLIGHT_ACTIVATED -> true;
              default -> false;
            };
  }

  /**
   * 커스텀 효과의 지속 시간이 하나도 경과하지 않았을 때 사간을 표시할지 말지 확인합니다.
   *
   * @return 시간을 표시하지 않는 버프면 true 이외에는 false
   */
  @SuppressWarnings("all")
  public boolean isTimeHiddenWhenFull()
  {
    return switch (this)
            {
              case PARROTS_CHEER -> true;
              default -> false;
            };
  }

  public boolean isRightClickRemovable()
  {
    return !isNegative() && switch (this)
            {
              case SERVER_RADIO_LISTENING, NEWBIE_SHIELD, FANCY_SPOTLIGHT_ACTIVATED -> false;
              default -> true;
            };
  }

  public boolean isBuffFreezable()
  {
    return switch (this)
            {
              case INVINCIBLE_PLUGIN_RELOAD, INVINCIBLE_RESPAWN -> false;
              default -> true;
            };
  }

  /**
   * @return 효과가 같은 여러 농도의 효과를 가지고 있을 때 해당 효과를 전부 표시할 효과면 true 이외에는 false
   */
  @SuppressWarnings("all")
  public boolean isStackDisplayed()
  {
    return getMaxAmplifier() > 0 && switch (this)
            {
              case HEALTH_INCREASE -> true;
              default -> false;
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

  @Nullable
  public ItemStack getIcon()
  {
    ItemStack itemStack = new ItemStack(Material.STONE);
    ItemMeta itemMeta = itemStack.getItemMeta();
    switch (this)
    {
      case SERVER_RADIO_LISTENING -> itemStack = new ItemStack(Material.MUSIC_DISC_CAT);
      case ELYTRA_BOOSTER -> itemStack = new ItemStack(Material.FIREWORK_ROCKET);
      case INVINCIBLE, INVINCIBLE_PLUGIN_RELOAD, INVINCIBLE_RESPAWN, RESURRECTION_INVINCIBLE, RESURRECTION -> itemStack = new ItemStack(Material.TOTEM_OF_UNDYING);
      case RESURRECTION_COOLDOWN -> {
        itemStack = new ItemStack(Material.TOTEM_OF_UNDYING);
        itemMeta.addEnchant(CustomEnchant.GLOW, 1, true);
      }
      case FEATHER_FALLING -> itemStack = new ItemStack(Material.FEATHER);
    }
    if (itemStack.getType() == Material.STONE)
    {
      return null;
    }
    itemStack.setItemMeta(itemMeta);
    return itemStack;
  }

  @Override
  public boolean isHiddenEnum()
  {
    return switch (this)
            {
              case COOLDOWN_CHAT, COOLDOWN_ITEM_MEGAPHONE, DARKNESS_TERROR_ACTIVATED, RESURRECTION_INVINCIBLE, RESURRECTION_COOLDOWN, SERVER_RADIO_LISTENING, PARROTS_CHEER,
                      INVINCIBLE_PLUGIN_RELOAD, INVINCIBLE_RESPAWN, HEROS_ECHO_OTHERS, FANCY_SPOTLIGHT_ACTIVATED, NEWBIE_SHIELD -> true;
              default -> false;
            };
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

  public int getMaxAmplifier()
  {
    return maxAmplifier;
  }

  public boolean isNegative()
  {
    return isNegative;
  }

  @NotNull
  public Component getPropertyDescription()
  {
    Component description = Component.empty();
    boolean isNegative = this.isNegative();
    boolean keepOnDeath = this.isKeepOnDeath(), keepOnQuit = this.isKeepOnQuit(), keepOnMilk = this.isKeepOnMilk(), buffFreezable = this.isBuffFreezable();

    if (!this.getDescription().equals(Component.empty()) && (keepOnDeath || !keepOnQuit || keepOnMilk || !buffFreezable))
    {
      description = description.append(Component.text("\n"));
    }
    if (keepOnDeath)
    {
      description = description.append(Component.text("\n")).append(ComponentUtil.translate("&" + (isNegative ? "c" : "a") + "사망해도 효과가 사라지지 않습니다"));
      if (this == CustomEffectType.CURSE_OF_BEANS)
      {
        description = description.append(Component.text("\n")).append(ComponentUtil.translate("&" + (isNegative ? "c" : "a") + "사망해도 효과가 사라지지 않습니다"));
      }
    }
    if (!keepOnQuit)
    {
      description = description.append(Component.text("\n")).append(ComponentUtil.translate("&c접속을 종료하면 효과가 사라집니다"));
    }
    if (keepOnMilk)
    {
      description = description.append(Component.text("\n")).append(ComponentUtil.translate("&" + (isNegative ? "c" : "a") + "우유를 마셔도 효과가 사라지지 않습니다"));
      if (this == CustomEffectType.CURSE_OF_BEANS)
      {
        description = description.append(Component.text("\n")).append(ComponentUtil.translate("&" + (isNegative ? "c" : "a") + "우유를 마셔도 효과가 사라지지 않습니다"));
      }
    }
    if (!buffFreezable)
    {
      description = description.append(Component.text("\n")).append(ComponentUtil.translate("&c%s의 영향을 받지 않습니다", CustomEffectType.BUFF_FREEZE));
    }
    return description;
  }
}
















