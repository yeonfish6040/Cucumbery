package com.jho5245.cucumbery.custom.customeffect;

import com.jho5245.cucumbery.custom.customeffect.CustomEffect.DisplayType;
import com.jho5245.cucumbery.util.no_groups.Method2;
import com.jho5245.cucumbery.util.storage.component.util.ComponentUtil;
import com.jho5245.cucumbery.util.storage.data.EnumHideable;
import com.jho5245.cucumbery.util.storage.data.TranslatableKeyParser;
import com.jho5245.cucumbery.util.storage.data.custom_enchant.CustomEnchant;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.translation.Translatable;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Custom Effect type. see {@link CustomEffect}, {@link PotionEffect} and {@link PotionEffectType}
 */
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
   * 아이템 확성기 쿨타임
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
   * 어두운거 싫어요
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
  DEBUG_WATCHER(true, false, false),
  /**
   * 겉날개 부스터
   */
  DO_NOT_PICKUP_BUT_THROW_IT(9, false, true, true),
  /**
   * 줍지 마, 던져!
   */
  DODGE(99),
  /**
   * 회피
   */
  ELYTRA_BOOSTER(9),
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
   * 몹 적대 무효화
   */
  NO_ENTITY_AGGRO,
  /**
   * 아싸
   */
  OUTSIDER(9, true, true, true),
  /**
   * 앵무새의 가호
   */
  PARROTS_CHEER,
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
  RESURRECTION_INVINCIBLE(false, false, false),
  /**
   * 샌즈의 축복
   */
  BLESS_OF_SANS(9),
  /**
   * 와 샌즈
   */
  WA_SANS(9),
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
   * 전이
   */
  SPREAD(99, false, true, true),
  /**
   * 멈춰!
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
   * 변이 해독
   */
  VAR_DETOXICATE(99),
  /**
   * 변이 해독
   */
  VAR_DETOXICATE_ACTIVATED,
  /**
   * 변이 폐렴
   */
  VAR_PNEUMONIA(99, false, true, true),
  /**
   * 변이 통풍
   */
  VAR_PODAGRA(99, false, true, true),
  /**
   * 변이 통풍
   */
  VAR_PODAGRA_ACTIVATED(99, false, true, true),
  /**
   * 변이 소화불량
   */
  VAR_STOMACHACHE(99, false, true, true),
  /**
   * 전투 모드 - 근거리
   */
  COMBAT_MODE_MELEE(false, false, false),
  /**
   * 전투 모드 - 쿨타임
   */
  COMBAT_MODE_MELEE_COOLDOWN(false, false, true),
  /**
   * 전투 모드 - 원거리
   */
  COMBAT_MODE_RANGED(false, false, false),
  /**
   * 전투 모드 - 쿨타임
   */
  COMBAT_MODE_RANGED_COOLDOWN(false, false, true),
  /**
   * 엔더 슬레이어
   */
  ENDER_SLAYER(9999),
  /**
   * 보스 슬레이어
   */
  BOSS_SLAYER(9999),
  /**
   * 주민의 축복
   */
  BLESS_OF_VILLAGER,
  /**
   * 빵 기모띠
   */
  BREAD_KIMOCHI,
  /**
   * 빵 기모띠
   */
  BREAD_KIMOCHI_SECONDARY_EFFECT,
  /**
   * 마을 보호막
   */
  TOWN_SHIELD,
  /**
   * 관전 지속 외출
   */
  CONTINUAL_SPECTATING_EXEMPT(true, true, false),
  /**
   * EXP 부스트
   */
  EXPERIENCE_BOOST(199),
  /**
   * 사라짐
   */
  DISAPPEAR(true, true, true),
  /**
   * 버프 해제 불가
   */
  NO_BUFF_REMOVE(true, true, true),
  /**
   * 재생 불능
   */
  NO_REGENERATION(false, true, true),
  /**
   * 대미지 인디케이터
   */
  DAMAGE_INDICATOR,
  /**
   * 얼어붙음
   */
  FREEZING(false, true, true),
  NO_CUCUMBERY_ITEM_USAGE_ATTACK(true, true, true),
  /**
   * 컴뱃 부스터
   */
  COMBAT_BOOSTER,

  /**
   * PvP 모드
   */
  PVP_MODE(true, true, false),

  /**
   * PvP 활성화
   */
  PVP_MODE_ENABLED(true, true,  false),

  /**
   * PvP 비활성화
   */
  PVP_MODE_DISABLED(true, true, true),

  /**
   * PvP 활성화 쿨타임
   */
  PVP_MODE_COOLDOWN(false, false, true),
  GLIDING(false, false, false),
  NOTIFY_NO_TRADE_ITEM_DROP(false, false, false),
  /**
   * 위치 기억
   */
  POSITION_MEMORIZE,
  /**
   * 도르마무
   */
  DORMAMMU(false, false, true),
  CUSTOM_DEATH_MESSAGE,
  ;

  private final int maxAmplifier;

  private final boolean isKeepOnDeath;

  private final boolean isKeepOnQuit;

  private final boolean isNegative;

  public List<Material> ICON_BUFF_FREEZE = Arrays.asList(Material.LAVA_BUCKET, Material.WATER_BUCKET, Material.COD_BUCKET);

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
              case SPREAD -> "전이";
              case VAR_STOMACHACHE -> "변이 소화불량";
              case PARROTS_CHEER -> "앵무새의 가호";
              case SHARPNESS -> Enchantment.DAMAGE_ALL.translationKey();
              case SMITE -> Enchantment.DAMAGE_UNDEAD.translationKey();
              case BANE_OF_ARTHROPODS -> Enchantment.DAMAGE_ARTHROPODS.translationKey();
              case STOP -> "멈춰!";
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
              case VAR_PNEUMONIA -> "변이 폐렴";
              case VAR_DETOXICATE, VAR_DETOXICATE_ACTIVATED -> "변이 해독";
              case VAR_PODAGRA, VAR_PODAGRA_ACTIVATED -> "변이 통풍";
              case NO_ENTITY_AGGRO -> "몹 적대 무효화";
              case COMBAT_MODE_MELEE -> "전투 모드 - 근거리";
              case COMBAT_MODE_RANGED -> "전투 모드 - 원거리";
              case COMBAT_MODE_MELEE_COOLDOWN, COMBAT_MODE_RANGED_COOLDOWN -> "전투 모드 - 쿨타임";
              case ENDER_SLAYER -> "엔더 슬레이어";
              case BOSS_SLAYER -> "보스 슬레이어";
              case BLESS_OF_VILLAGER -> "주민의 축복";
              case BREAD_KIMOCHI, BREAD_KIMOCHI_SECONDARY_EFFECT -> "빵 기모띠";
              case TOWN_SHIELD -> "마을 보호막";
              case CONTINUAL_SPECTATING_EXEMPT -> "관전 지속 외출";
              case EXPERIENCE_BOOST -> "EXP 부스트";
              case DISAPPEAR -> "사라짐";
              case NO_BUFF_REMOVE -> "버프 해제 불가";
              case NO_REGENERATION -> "재생 불능";
              case DAMAGE_INDICATOR -> "대미지 인디케이터";
              case FREEZING -> "얼어붙음";
              case NO_CUCUMBERY_ITEM_USAGE_ATTACK -> "대미지 이벤트 무시";
              case COMBAT_BOOSTER -> "컴뱃 부스터";
              case PVP_MODE -> "PvP 모드";
              case PVP_MODE_ENABLED -> "PvP 모드 활성화";
              case PVP_MODE_DISABLED -> "PvP 모드 비활성화";
              case PVP_MODE_COOLDOWN -> "PvP 모드 쿨타임";
              case GLIDING -> "활강 중";
              case NOTIFY_NO_TRADE_ITEM_DROP -> "캐릭터 귀속 아이템 버리기 알림";
              case POSITION_MEMORIZE -> "위치 기억";
              case DORMAMMU -> "도르마무";
              case CUSTOM_DEATH_MESSAGE -> "커스텀 데스 메시지";
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

  /**
   * 효과가 너무 많아 짧게 표시할 경우에 사용할 이름입니다
   *
   * @return 짧은 이름 혹은 원래 짧을 경우 그대로의 이름
   */
  @NotNull
  public String shortTranslationKey()
  {
    return switch (this)
            {
              case AWKWARD -> "어색";
              case BUFF_FREEZE -> "버프";
              case CHEESE_EXPERIMENT -> "치즈";
              case CONTINUAL_SPECTATING, CONTINUAL_SPECTATING_EXEMPT -> "관전";
              case COOLDOWN_CHAT -> "챗쿨";
              case COOLDOWN_ITEM_MEGAPHONE -> "아확쿨";
              case CURSE_OF_BEANS -> "콩콩";
              case CURSE_OF_CONSUMPTION -> "소비X";
              case CURSE_OF_CREATIVITY -> "건축X";
              case CURSE_OF_CREATIVITY_BREAK -> "파괴X";
              case CURSE_OF_CREATIVITY_PLACE -> "설치X";
              case CURSE_OF_DROP -> "드롭X";
              case CURSE_OF_INVENTORY -> "인벤셉X";
              case CURSE_OF_JUMPING -> "점프X";
              case CURSE_OF_MUSHROOM -> "버섯";
              case CURSE_OF_PICKUP -> "줍기X";
              case DARKNESS_TERROR -> "어두워";
              case DARKNESS_TERROR_ACTIVATED -> "어둡다";
              case DARKNESS_TERROR_RESISTANCE -> "안어둡";
              case DEBUG_WATCHER -> "디버그";
              case DO_NOT_PICKUP_BUT_THROW_IT -> "노줍던";
              case ELYTRA_BOOSTER -> "겉가";
              case FANCY_SPOTLIGHT -> "조명";
              case FANCY_SPOTLIGHT_ACTIVATED -> "조명!";
              case FEATHER_FALLING -> "가착";
              case FROST_WALKER -> "차걸";
              case HEALTH_INCREASE -> "체력";
              case HEROS_ECHO, HEROS_ECHO_OTHERS -> "영메";
              case INVINCIBLE_PLUGIN_RELOAD, INVINCIBLE_RESPAWN, RESURRECTION_INVINCIBLE -> "무적";
              case KEEP_INVENTORY -> "인벤O";
              case KINETIC_RESISTANCE -> "운동젛";
              case KNOCKBACK_RESISTANCE -> "넉젛";
              case KNOCKBACK_RESISTANCE_COMBAT -> "전넉젛";
              case KNOCKBACK_RESISTANCE_NON_COMBAT -> "!넉젛";
              case LEVITATION_RESISTANCE -> "공젛";
              case MUTE -> "채금";
              case NEWBIE_SHIELD -> "늅실드";
              case NOTHING -> ".";
              case NO_ENTITY_AGGRO -> "노어글";
              case PARROTS_CHEER -> "앵가호";
              case RESURRECTION -> "리저";
              case RESURRECTION_COOLDOWN -> "리저쿨";
              case BLESS_OF_SANS -> "샌축";
              case SERVER_RADIO_LISTENING -> "라디오";
              case SHARPNESS -> "날카";
              case SILK_TOUCH -> "섬세";
              case SMELTING_TOUCH -> "제련손";
              case TROLL_INVENTORY_PROPERTY, TROLL_INVENTORY_PROPERTY_MIN -> "트롤";
              case TRUE_INVISIBILITY -> "찐투명";
              case UNCRAFTABLE -> "노제작";
              case VAR_DETOXICATE -> "해독";
              case VAR_PNEUMONIA -> "폐렴";
              case VAR_PODAGRA -> "통풍";
              case VAR_STOMACHACHE -> "복통";
              case WA_SANS -> "와샌";
              case COMBAT_MODE_MELEE -> "전모근";
              case COMBAT_MODE_RANGED -> "전모원";
              case COMBAT_MODE_MELEE_COOLDOWN, COMBAT_MODE_RANGED_COOLDOWN -> "전모클";
              case ENDER_SLAYER -> "엔슬";
              case BOSS_SLAYER -> "보슬";
              case BLESS_OF_VILLAGER -> "주축";
              case BREAD_KIMOCHI, BREAD_KIMOCHI_SECONDARY_EFFECT -> "빵";
              case TOWN_SHIELD -> "마을";
              case EXPERIENCE_BOOST -> "EXP";
              case NO_BUFF_REMOVE -> "버해불";
              case NO_REGENERATION -> "재불";
              case FREEZING -> "추움";
              case COMBAT_BOOSTER -> "부스터";
              case POSITION_MEMORIZE -> "위치";
              default -> translationKey();
            };
  }

  /**
   * 효과에 해당하는 설명을 Component 형태로 반환합니다.
   *
   * @return Component 설명
   */
  @NotNull
  public Component getDescription()
  {
    return switch (this)
            {
              case MUTE -> ComponentUtil.translate("채팅을 할 수 없는 상태입니다");
              case CURSE_OF_MUSHROOM -> ComponentUtil.translate("농도 레벨 * 0.1% 확률로 5초마다 인벤토리에 버섯이 들어옵니다");
              case INVINCIBLE -> ComponentUtil.translate("어떠한 형태의 피해도 받지 않습니다");
              case BUFF_FREEZE -> ComponentUtil.translate("사망 시 버프를 소모하여 일부 버프를 제외한 버프가 사라지지 않습니다");
              case CONFUSION -> ComponentUtil.translate("방향키가 반대로 작동합니다");
              case RESURRECTION -> ComponentUtil.translate("죽음에 이르는 피해를 입었을 때, 죽지 않고")
                      .append(Component.text("\n"))
                      .append(ComponentUtil.translate("버프를 소모하여 2초간 무적이 됩니다"));
              case RESURRECTION_INVINCIBLE -> ComponentUtil.translate("2초간 무적이 됩니다");
              case RESURRECTION_COOLDOWN -> ComponentUtil.translate("%s 버프를 받을 수 없는 상태입니다", CustomEffectType.RESURRECTION);
              case FROST_WALKER -> ComponentUtil.translate("마그마 블록 위를 걸어도 피해를 입지 않습니다");
              case FEATHER_FALLING -> ComponentUtil.translate("낙하 대미지를 받기 위한 최소 높이가 증가하고,")
                      .append(Component.text("\n"))
                      .append(ComponentUtil.translate("낙하 피해량이 감소합니다"));
              case BLESS_OF_SANS, SHARPNESS -> ComponentUtil.translate("근거리 대미지가 증가합니다");
              case PARROTS_CHEER -> ComponentUtil.translate("HP가 5 이하일 때 15 블록 이내에 자신이 길들인 앵무새가")
                      .append(Component.text("\n"))
                      .append(ComponentUtil.translate("있으면 받는 피해량이 45% 감소하고 대미지가 10% 증가합니다"));
              case SMITE -> ComponentUtil.translate("언데드 개체 공격 시 근거리 대미지가 증가합니다");
              case BANE_OF_ARTHROPODS -> ComponentUtil.translate("절지동물류 개체 공격 시 근거리 대미지가 증가하고,")
                      .append(Component.text("\n"))
                      .append(ComponentUtil.translate("%s 4단계 효과를 부여합니다", ComponentUtil.translate("&ceffect.minecraft.slowness")));
              case STOP -> ComponentUtil.translate("모든 행동을 할 수 없는 상태입니다")
                      .append(Component.text("\n"))
                      .append(ComponentUtil.translate("좌우이동, 웅크리기를 할 때마다 지속 시간이 6초씩 감소합니다"));
              case KEEP_INVENTORY -> ComponentUtil.translate("죽어도 아이템을 떨어뜨리지 않습니다");
              case DO_NOT_PICKUP_BUT_THROW_IT -> ComponentUtil.translate("아이템을 줍는 대신 던집니다")
                      .append(Component.text("\n"))
                      .append(ComponentUtil.translate("농도 레벨이 높을 수록 더 멀리 던집니다"));
              case INSIDER -> ComponentUtil.translate("채팅이 여러번 입력되고, 사망 시 모든 플레이어에게")
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
              case TELEKINESIS -> ComponentUtil.translate("블록을 캐거나 적을 처치했을 때 드롭하는")
                      .append(Component.text("\n"))
                      .append(ComponentUtil.translate("아이템과 경험치가 즉시 인벤토리에 들어옵니다"));
              case SMELTING_TOUCH -> ComponentUtil.translate("블록을 캐거나 적을 처치했을 때 드롭하는")
                      .append(Component.text("\n"))
                      .append(ComponentUtil.translate("아이템을 제련된 형태로 바꿔줍니다"));
              case CURSE_OF_INVENTORY -> ComponentUtil.translate("사망 시 인벤세이브 여부와 관계없이")
                      .append(Component.text("\n"))
                      .append(ComponentUtil.translate("가지고 있는 모든 아이템과 경험치를 떨어뜨립니다"));
              case CURSE_OF_CREATIVITY -> ComponentUtil.translate("블록을 설치하거나 파괴할 수 없는 상태입니다");
              case CURSE_OF_CREATIVITY_BREAK -> ComponentUtil.translate("블록을 파괴할 수 없는 상태입니다");
              case CURSE_OF_CREATIVITY_PLACE -> ComponentUtil.translate("블록을 설치할 수 없는 상태입니다");
              case CURSE_OF_CONSUMPTION -> ComponentUtil.translate("음식이나 포션을 사용할 수 없는 상태입니다");
              case CURSE_OF_PICKUP -> ComponentUtil.translate("아이템을 주울 수 없는 상태입니다");
              case CURSE_OF_DROP -> ComponentUtil.translate("아이템을 버릴 수 없는 상태입니다");
              case CURSE_OF_JUMPING -> ComponentUtil.translate("점프를 할 수 없는 상태입니다");
              case KINETIC_RESISTANCE -> ComponentUtil.translate("겉날개 활강 중 블록에 부딪혀서 받는 피해량이 감소합니다")
                      .append(Component.text("\n"))
                      .append(ComponentUtil.translate("낙하 대미지는 감소하지 않습니다"));
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
              case TROLL_INVENTORY_PROPERTY, TROLL_INVENTORY_PROPERTY_MIN -> ComponentUtil.translate("인벤토리의 숫자가 자꾸 멋대로 바뀝니다")
                      .append(Component.text("\n"))
                      .append(ComponentUtil.translate("잘만 이용하면 오히려 더 좋을 수도..?"));
              case MUNDANE -> ComponentUtil.translate("평범하다...");
              case AWKWARD -> ComponentUtil.translate("어... 그게.. 어색? 해 진다? 라고 생각? 합니다");
              case THICK -> ComponentUtil.translate("채팅이 진해집니다");
              case UNCRAFTABLE -> ComponentUtil.translate("아이템을 제작할 수 없습니다");
              case COOLDOWN_CHAT -> ComponentUtil.translate("쿨타임 동안 채팅을 할 수 없습니다");
              case COOLDOWN_ITEM_MEGAPHONE -> ComponentUtil.translate("쿨타임 동안 아이템 확성기를 사용할 수 없습니다");
              case SERVER_RADIO_LISTENING -> ComponentUtil.translate("서버 노래를 들어서 기분이 들떠 대미지가 증가합니다");
              case DARKNESS_TERROR -> ComponentUtil.translate("어두운거.. 무섭다..")
                      .append(Component.text("\n"))
                      .append(ComponentUtil.translate("%s 효과나 손에 빛을 내는 아이템 없이 어두운 곳에 가면 %s 효과가 걸립니다",
                              Component.translatable(TranslatableKeyParser.getKey(PotionEffectType.NIGHT_VISION), NamedTextColor.GREEN), DARKNESS_TERROR_ACTIVATED));
              case DARKNESS_TERROR_ACTIVATED -> ComponentUtil.translate("너무 어둡습니다! 피해량이 30% 증가하고 블록을 캘 때마다")
                      .append(Component.text("\n"))
                      .append(ComponentUtil.translate("%s 확률로 받는 피해량 증가에 영향을 받는 1의 피해를 입습니다", "&e15%"));
              case DARKNESS_TERROR_RESISTANCE -> ComponentUtil.translate("%s 효과에 대한 내성이 생깁니다", DARKNESS_TERROR_ACTIVATED);
              case DODGE -> ComponentUtil.translate("일정 확률로 공격을 회피합니다");
              case NEWBIE_SHIELD -> ComponentUtil.translate("플레이 시간이 1시간 미만인 당신!")
                      .append(Component.text("\n"))
                      .append(ComponentUtil.translate("받는 피해량이 감소하고 대미지가 증가합니다"))
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
              case WA_SANS -> ComponentUtil.translate("스켈레톤 유형의 개체에게 받는")
                      .append(Component.text("\n"))
                      .append(ComponentUtil.translate("피해량이 감소하고 대미지가 증가합니다"));
              case HEALTH_INCREASE -> ComponentUtil.translate("최대 HP가 증가합니다");
              case CONTINUAL_SPECTATING -> ComponentUtil.translate("플레이어를 지속적으로 관전합니다")
                      .append(Component.text("\n"))
                      .append(ComponentUtil.translate("해당 플레이어가 재접속하거나 리스폰해도 자동으로 관전합니다"));
              case TRUE_INVISIBILITY -> ComponentUtil.translate("말 그대로 완전히 다른 플레이어로부터 보이지 않습니다");
              case SPREAD -> ComponentUtil.translate("주변의 다른 플레이어에게 자신이 가장 최근에 획득한 전이 효과를 옮깁니다")
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
                      .append(ComponentUtil.translate("추가로, 한번에 경험치 100 이상을 획득 시 일정 확률로 즉사합니다"));
              case VAR_PNEUMONIA -> ComponentUtil.translate("물 속에서 산소 소모 속도가 증가하고")
                      .append(Component.text("\n"))
                      .append(ComponentUtil.translate("물 밖으로 나와도 산소가 회복되지 않습니다"));
              case VAR_DETOXICATE -> ComponentUtil.translate("%s, %s, %s, %s 상태 효과를 가지고 있을 경우", PotionEffectType.POISON, PotionEffectType.CONFUSION, PotionEffectType.BLINDNESS, PotionEffectType.UNLUCK)
                      .append(Component.text("\n"))
                      .append(ComponentUtil.translate("해당 상태 효과의 농도 레벨을 1단계 낮추거나 제거합니다"))
                      .append(Component.text("\n"))
                      .append(ComponentUtil.translate("일정 확률로 농도 레벨이 2단계가 낮아지거나 3단계가 낮아질"))
                      .append(Component.text("\n"))
                      .append(ComponentUtil.translate("수 있으며, 일정 확률로 표시된 모든 디버프 4개가 제거될 수 있습니다"));
              case VAR_PODAGRA -> ComponentUtil.translate("이동하거나 점프할 때 웅크리지 않은 상태라면")
                      .append(Component.text("\n"))
                      .append(ComponentUtil.translate("행동을 시작할 때 1의 피해를 입고 행동을 지속할 시"))
                      .append(Component.text("\n"))
                      .append(ComponentUtil.translate("3초마다 0.2의 피해를 입습니다. 추가로 3.5블록 미만의 높이에서 낙하할"))
                      .append(Component.text("\n"))
                      .append(ComponentUtil.translate("경우 1의 피해를 입고 그 이상의 높이에서 낙하할 경우 낙하 대미지가 50% 증가합니다"));
              case NO_ENTITY_AGGRO -> ComponentUtil.translate("중립적이거나 적대적 몹이 적대적이지 않게 됩니다");
              case COMBAT_MODE_MELEE -> ComponentUtil.translate("근거리 전투에 대한 집중력이 비약적으로 상승하여")
                      .append(Component.text("\n"))
                      .append(ComponentUtil.translate("상대방에게 입히는 근거리 대미지가 100% 증가하는 대신"))
                      .append(Component.text("\n"))
                      .append(ComponentUtil.translate("상대방에게서 받는 원거리 피해량이 100% 증가합니다"))
                      .append(Component.text("\n"))
                      .append(ComponentUtil.translate("이 효과는 %s 효과와 동시에 적용되지 않습니다", ComponentUtil.translate("&a" + CustomEffectType.COMBAT_MODE_RANGED.translationKey())));
              case COMBAT_MODE_RANGED -> ComponentUtil.translate("원거리 전투에 대한 집중력이 비약적으로 상승하여")
                      .append(Component.text("\n"))
                      .append(ComponentUtil.translate("상대방에게 입히는 원거리 대미지가 100% 증가하는 대신"))
                      .append(Component.text("\n"))
                      .append(ComponentUtil.translate("상대방에게서 받는 근거리 피해량이 100% 증가합니다"))
                      .append(Component.text("\n"))
                      .append(ComponentUtil.translate("이 효과는 %s 효과와 동시에 적용되지 않습니다", ComponentUtil.translate("&a" + CustomEffectType.COMBAT_MODE_RANGED.translationKey())));
              case BLESS_OF_VILLAGER -> ComponentUtil.translate("대미지가 10% 증가합니다");
              case BREAD_KIMOCHI, BREAD_KIMOCHI_SECONDARY_EFFECT -> ComponentUtil.translate("이동 속도가 10% 증가하고 방어력이 2 증가합니다");
              case COMBAT_MODE_MELEE_COOLDOWN, COMBAT_MODE_RANGED_COOLDOWN -> ComponentUtil.translate("전투 모드를 변경할 수 없는 상태입니다");
              case ENDER_SLAYER -> ComponentUtil.translate("%s, %s 또는 %s 공격 시 대미지가 증가합니다", EntityType.ENDERMAN, EntityType.ENDERMITE, EntityType.ENDER_DRAGON);
              case BOSS_SLAYER -> ComponentUtil.translate("보스 몬스터 공격 시 대미지가 증가합니다");
              case TOWN_SHIELD -> ComponentUtil.translate("평화로운 마을입니다! %s 효과가 적용되며", ComponentUtil.translate("&a" + TranslatableKeyParser.getKey(PotionEffectType.SATURATION)))
                      .append(Component.text("\n"))
                      .append(ComponentUtil.translate("이동 속도가 30% 증가하고 받는 피해량이 50% 감소합니다"));
              case CONTINUAL_SPECTATING_EXEMPT -> ComponentUtil.translate("지속시간 동안은 강제 관전이 되지 않습니다");
              case EXPERIENCE_BOOST -> ComponentUtil.translate("경험치 획득량이 증가합니다");
              case NO_BUFF_REMOVE -> ComponentUtil.translate("버프를 해제할 수 없는 상태입니다");
              case NO_REGENERATION -> ComponentUtil.translate("HP가 재생되지 않습니다");
              case FREEZING -> ComponentUtil.translate("춥다.. 매우.. 춥다..");
              case COMBAT_BOOSTER -> ComponentUtil.translate("공격 속도가 25% 증가합니다");
              case PVP_MODE -> ComponentUtil.translate("%s 효과가 있어야 PvP를 할 수 있습니다", ComponentUtil.translate("&a" + CustomEffectType.PVP_MODE_ENABLED.translationKey()));
              case PVP_MODE_ENABLED -> ComponentUtil.translate("이 효과를 동시에 가지고 있는")
                      .append(Component.text("\n"))
                      .append(ComponentUtil.translate("플레이어와 PvP할 수 있습니다"));
              case PVP_MODE_DISABLED -> ComponentUtil.translate("");
              case POSITION_MEMORIZE -> ComponentUtil.translate("현재 위치를 기억합니다")
                      .append(Component.text("\n"))
                      .append(ComponentUtil.translate("버프를 좌클릭하여 즉시 이동할 수 있습니다"));
              case DORMAMMU -> ComponentUtil.translate("잠시 후, 원래 있던 곳으로 강제 이동됩니다");
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

  /**
   * 즉발형 효과는 버프창에 표시되지 않고 효과가 적용되는 즉시 능력이 발동됩니다.
   *
   * @return 즉발형 효과면 true 이외에는 false
   */
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

  /**
   * 각 효과의 기본 지속 시간을 틱 단위로 반환합니다. -1의 경우 무제한입니다.
   * <p>온오프 버프의 경우 항상 -1입니다.
   *
   * @return 효과에 따른 기본 지속 시간 혹은 30초 (600틱)
   */
  public int getDefaultDuration()
  {
    if (isToggle())
    {
      return -1;
    }
    return switch (this)
            {
              case RESURRECTION, CUCUMBERY_UPDATER, DARKNESS_TERROR_ACTIVATED, CONTINUAL_SPECTATING -> -1;
              case SERVER_RADIO_LISTENING, NEWBIE_SHIELD, VAR_DETOXICATE_ACTIVATED, NO_CUCUMBERY_ITEM_USAGE_ATTACK -> 2;
              case GLIDING -> 3;
              case VAR_PODAGRA_ACTIVATED -> 5;
              case DAMAGE_INDICATOR -> 16;
              case FANCY_SPOTLIGHT_ACTIVATED -> 20;
              case RESURRECTION_INVINCIBLE, DISAPPEAR -> 20 * 2;
              case COOLDOWN_CHAT -> 20 * 3;
              case PARROTS_CHEER, INVINCIBLE_PLUGIN_RELOAD, INVINCIBLE_RESPAWN, COMBAT_MODE_MELEE_COOLDOWN, COMBAT_MODE_RANGED_COOLDOWN, TOWN_SHIELD,
                      PVP_MODE_COOLDOWN, DORMAMMU -> 20 * 5;
              case STOP, COOLDOWN_ITEM_MEGAPHONE, NOTIFY_NO_TRADE_ITEM_DROP -> 20 * 10;
              case CUSTOM_DEATH_MESSAGE -> 20 * 15;
              case BREAD_KIMOCHI -> 20 * 60 * 3;
              case RESURRECTION_COOLDOWN -> 20 * 60 * 10;
              case HEROS_ECHO, HEROS_ECHO_OTHERS -> 20 * 60 * 60 * 2;
              default -> 20 * 30;
            };
  }

  /**
   * @return 해당 효과의 아이콘이 있으면 아이콘을 반환하고 없으면 <code>null</code>을 반환합니다.
   */
  @Nullable
  public ItemStack getIcon()
  {
    ItemStack itemStack = new ItemStack(Material.STONE);
    ItemMeta itemMeta = itemStack.getItemMeta();
    switch (this)
    {
      case AWKWARD, CHEESE_EXPERIMENT, VAR_PNEUMONIA -> itemStack = new ItemStack(Material.PUFFERFISH);
      case BANE_OF_ARTHROPODS, DARKNESS_TERROR_RESISTANCE -> itemStack = new ItemStack(Material.GLOW_INK_SAC);
      case BLESS_OF_SANS, WA_SANS -> itemStack = new ItemStack(Material.BONE);
      case BLESS_OF_VILLAGER, TOWN_SHIELD -> itemStack = new ItemStack(Material.EMERALD);
      case BOSS_SLAYER, COMBAT_MODE_MELEE, COMBAT_MODE_MELEE_COOLDOWN, PVP_MODE, PVP_MODE_ENABLED, PVP_MODE_COOLDOWN, SHARPNESS -> itemStack = new ItemStack(Material.IRON_SWORD);
      case BREAD_KIMOCHI -> itemStack = new ItemStack(Material.BREAD);
      case BUFF_FREEZE -> itemStack = new ItemStack(Material.RABBIT_FOOT);
      case COMBAT_BOOSTER -> itemStack = new ItemStack(Material.SUGAR);
      case COMBAT_MODE_RANGED, COMBAT_MODE_RANGED_COOLDOWN, IDIOT_SHOOTER -> itemStack = new ItemStack(Material.BOW);
      case CONFUSION, TELEKINESIS, CONTINUAL_SPECTATING_EXEMPT -> itemStack = new ItemStack(Material.ENDER_PEARL);
      case CONTINUAL_SPECTATING, ENDER_SLAYER -> itemStack = new ItemStack(Material.ENDER_EYE);
      case COOLDOWN_CHAT, COOLDOWN_ITEM_MEGAPHONE -> itemStack = new ItemStack(Material.CLOCK);
      case CURSE_OF_BEANS -> itemStack = new ItemStack(Material.COCOA_BEANS);
      case CURSE_OF_CONSUMPTION, VAR_STOMACHACHE -> itemStack = new ItemStack(Material.POISONOUS_POTATO);
      case CURSE_OF_CREATIVITY -> itemStack = new ItemStack(Material.WEATHERED_COPPER);
      case CURSE_OF_CREATIVITY_BREAK -> itemStack = new ItemStack(Material.GOLDEN_PICKAXE);
      case CURSE_OF_CREATIVITY_PLACE -> itemStack = new ItemStack(Material.STRUCTURE_VOID);
      case CURSE_OF_DROP -> itemStack = new ItemStack(Material.TURTLE_EGG);
      case CURSE_OF_INVENTORY -> itemStack = new ItemStack(Material.HOPPER);
      case CURSE_OF_JUMPING -> itemStack = new ItemStack(Material.LODESTONE);
      case CURSE_OF_MUSHROOM -> itemStack = new ItemStack(Material.RED_MUSHROOM);
      case CURSE_OF_PICKUP -> itemStack = new ItemStack(Material.COMPOSTER);
      case DARKNESS_TERROR, DARKNESS_TERROR_ACTIVATED -> itemStack = new ItemStack(Material.INK_SAC);
      case DEBUG_WATCHER -> itemStack = new ItemStack(Material.COMMAND_BLOCK);
      case DO_NOT_PICKUP_BUT_THROW_IT -> itemStack = new ItemStack(Material.POPPED_CHORUS_FRUIT);
      case DODGE -> itemStack = new ItemStack(Material.PHANTOM_MEMBRANE);
      case ELYTRA_BOOSTER -> itemStack = new ItemStack(Material.FIREWORK_ROCKET);
      case EXPERIENCE_BOOST -> itemStack = new ItemStack(Material.EXPERIENCE_BOTTLE);
      case FANCY_SPOTLIGHT, FANCY_SPOTLIGHT_ACTIVATED -> itemStack = new ItemStack(Material.SEA_LANTERN);
      case FEATHER_FALLING -> itemStack = new ItemStack(Material.FEATHER);
      case FREEZING -> itemStack = new ItemStack(Material.POWDER_SNOW_BUCKET);
      case FROST_WALKER -> {
        itemStack = new ItemStack(Material.DIAMOND_BOOTS);
        itemMeta.addEnchant(CustomEnchant.GLOW, 1, true);
      }
      case HEALTH_INCREASE -> itemStack = new ItemStack(Material.RED_DYE);
      case HEROS_ECHO, HEROS_ECHO_OTHERS -> itemStack = new ItemStack(Material.BEACON);
      case INSIDER -> itemStack = new ItemStack(Material.CAKE);
      case INVINCIBLE, INVINCIBLE_PLUGIN_RELOAD, INVINCIBLE_RESPAWN, RESURRECTION, RESURRECTION_INVINCIBLE -> itemStack = new ItemStack(Material.TOTEM_OF_UNDYING);
      case KEEP_INVENTORY -> itemStack = new ItemStack(Material.BARREL);
      case KINETIC_RESISTANCE -> itemStack = new ItemStack(Material.SPONGE);
      case KNOCKBACK_RESISTANCE, KNOCKBACK_RESISTANCE_COMBAT, KNOCKBACK_RESISTANCE_NON_COMBAT, LEVITATION_RESISTANCE -> itemStack = new ItemStack(Material.SHIELD);
      case MUNDANE -> itemStack = new ItemStack(Material.STONE);
      case MUTE, NO_BUFF_REMOVE, STOP -> itemStack = new ItemStack(Material.BARRIER);
      case NEWBIE_SHIELD -> itemStack = new ItemStack(Material.CHAINMAIL_CHESTPLATE);
      case NOTHING, TRUE_INVISIBILITY -> itemStack = new ItemStack(Material.LIGHT_GRAY_STAINED_GLASS_PANE);
      case NO_ENTITY_AGGRO -> itemStack = new ItemStack(Material.BROWN_DYE);
      case NO_REGENERATION -> itemStack = new ItemStack(Material.REDSTONE);
      case OUTSIDER -> itemStack = new ItemStack(Material.DEAD_TUBE_CORAL_FAN);
      case PARROTS_CHEER -> itemStack = new ItemStack(Material.PARROT_SPAWN_EGG);
      case RESURRECTION_COOLDOWN -> {
        itemStack = new ItemStack(Material.TOTEM_OF_UNDYING);
        itemMeta.addEnchant(CustomEnchant.GLOW, 1, true);
      }
      case SERVER_RADIO_LISTENING -> itemStack = new ItemStack(Material.JUKEBOX);
      case SILK_TOUCH -> itemStack = new ItemStack(Material.SHEARS);
      case SMELTING_TOUCH -> itemStack = new ItemStack(Material.LAVA_BUCKET);
      case SMITE -> itemStack = new ItemStack(Material.LIGHTNING_ROD);
      case SPREAD -> itemStack = new ItemStack(Material.WITHER_ROSE);
      case THICK -> itemStack = new ItemStack(Material.NETHERITE_BLOCK);
      case TROLL_INVENTORY_PROPERTY, TROLL_INVENTORY_PROPERTY_MIN -> itemStack = new ItemStack(Material.REPEATING_COMMAND_BLOCK);
      case UNCRAFTABLE -> itemStack = new ItemStack(Material.CRAFTING_TABLE);
      case VAR_DETOXICATE, VAR_DETOXICATE_ACTIVATED -> itemStack = new ItemStack(Material.MILK_BUCKET);
      case VAR_PODAGRA, VAR_PODAGRA_ACTIVATED -> itemStack = new ItemStack(Material.GRINDSTONE);
      case CUCUMBERY_UPDATER, BREAD_KIMOCHI_SECONDARY_EFFECT, MINECRAFT_WITHER, MINECRAFT_WEAKNESS, MINECRAFT_WATER_BREATHING, MINECRAFT_UNLUCK, MINECRAFT_STRENGTH, MINECRAFT_SPEED, MINECRAFT_SLOW_FALLING,
              MINECRAFT_ABSORPTION, MINECRAFT_BAD_OMEN, MINECRAFT_BLINDNESS, MINECRAFT_CONDUIT_POWER, MINECRAFT_DOLPHINS_GRACE, MINECRAFT_FIRE_RESISTANCE, MINECRAFT_GLOWING, MINECRAFT_HASTE, MINECRAFT_HEALTH_BOOST,
              MINECRAFT_HERO_OF_THE_VILLAGE, MINECRAFT_HUNGER, MINECRAFT_INSTANT_DAMAGE, MINECRAFT_INSTANT_HEAL, MINECRAFT_INVISIBILITY, MINECRAFT_JUMP_BOOST, MINECRAFT_LEVITATION, MINECRAFT_LUCK, MINECRAFT_MINING_FATIGUE,
              MINECRAFT_NAUSEA, MINECRAFT_NIGHT_VISION, MINECRAFT_POISON, MINECRAFT_REGENERATION, MINECRAFT_RESISTANCE, MINECRAFT_SATURATION, MINECRAFT_SLOWNESS, DISAPPEAR, DAMAGE_INDICATOR -> {
        return null;
      }
    }
    if (itemStack.getType() == Material.STONE && this != CustomEffectType.MUNDANE)
    {
      return null;
    }
    itemMeta.addItemFlags(ItemFlag.values());
    itemStack.setItemMeta(itemMeta);
    return itemStack;
  }

  /**
   * @return 아이콘에 부여할 CustomModelData magic value입니다
   */
  int getId()
  {
    // 가장 높은 버프 숫자 : 89 = PVP_MODE_COOLDOWN
    return switch (this)
            {
              case AWKWARD -> 1;
              case BANE_OF_ARTHROPODS -> 2;
              case BLESS_OF_SANS -> 54;
              case BLESS_OF_VILLAGER -> 79;
              case BOSS_SLAYER -> 76;
              case BREAD_KIMOCHI -> 78;
              case BUFF_FREEZE -> 3;
              case CHEESE_EXPERIMENT -> 4;
              case COMBAT_BOOSTER -> 86;  ///////////////////////////////// <--[HERE]
              case COMBAT_MODE_MELEE -> 72;
              case COMBAT_MODE_MELEE_COOLDOWN -> 73;
              case COMBAT_MODE_RANGED -> 74;
              case COMBAT_MODE_RANGED_COOLDOWN -> 75;
              case CONFUSION -> 5;
              case CONTINUAL_SPECTATING -> 6;
              case CONTINUAL_SPECTATING_EXEMPT -> 81;
              case COOLDOWN_CHAT -> 7;
              case COOLDOWN_ITEM_MEGAPHONE -> 8;
              case CURSE_OF_BEANS -> 9;
              case CURSE_OF_CONSUMPTION -> 10;
              case CURSE_OF_CREATIVITY -> 11;
              case CURSE_OF_CREATIVITY_BREAK -> 12;
              case CURSE_OF_CREATIVITY_PLACE -> 13;
              case CURSE_OF_DROP -> 14;
              case CURSE_OF_INVENTORY -> 15;
              case CURSE_OF_JUMPING -> 16;
              case CURSE_OF_MUSHROOM -> 17;
              case CURSE_OF_PICKUP -> 18;
              case DARKNESS_TERROR -> 19;
              case DARKNESS_TERROR_ACTIVATED -> 20;
              case DARKNESS_TERROR_RESISTANCE -> 21;
              case DEBUG_WATCHER -> 22;
              case DO_NOT_PICKUP_BUT_THROW_IT -> 23;
              case DODGE -> 24;
              case ELYTRA_BOOSTER -> 25;
              case EXPERIENCE_BOOST -> 82;
              case ENDER_SLAYER -> 77;
              case FANCY_SPOTLIGHT -> 26;
              case FANCY_SPOTLIGHT_ACTIVATED -> 27;
              case FEATHER_FALLING -> 28;
              case FREEZING -> 85;
              case FROST_WALKER -> 29;
              case HEALTH_INCREASE -> 30;
              case HEROS_ECHO -> 31;
              case HEROS_ECHO_OTHERS -> 32;
              case IDIOT_SHOOTER -> 33;
              case INSIDER -> 34;
              case INVINCIBLE -> 35;
              case INVINCIBLE_PLUGIN_RELOAD -> 36;
              case INVINCIBLE_RESPAWN -> 37;
              case KEEP_INVENTORY -> 38;
              case KINETIC_RESISTANCE -> 39;
              case KNOCKBACK_RESISTANCE -> 40;
              case KNOCKBACK_RESISTANCE_COMBAT -> 41;
              case KNOCKBACK_RESISTANCE_NON_COMBAT -> 42;
              case LEVITATION_RESISTANCE -> 43;
              case MUNDANE -> 44;
              case MUTE -> 45;
              case NEWBIE_SHIELD -> 46;
              case NO_BUFF_REMOVE -> 83;
              case NO_ENTITY_AGGRO -> 47;
              case NO_REGENERATION -> 84;
              case NOTHING -> 48;
              case OUTSIDER -> 49;
              case PARROTS_CHEER -> 50;
              case PVP_MODE -> 87;
              case PVP_MODE_ENABLED -> 88;
              case PVP_MODE_COOLDOWN -> 89;
              case RESURRECTION -> 51;
              case RESURRECTION_COOLDOWN -> 52;
              case RESURRECTION_INVINCIBLE -> 53;
              case SERVER_RADIO_LISTENING -> 56;
              case SHARPNESS -> 57;
              case SILK_TOUCH -> 58;
              case SMELTING_TOUCH -> 59;
              case SMITE -> 60;
              case SPREAD -> 61;
              case STOP -> 62;
              case TELEKINESIS -> 63;
              case THICK -> 64;
              case TOWN_SHIELD -> 80;
              case TROLL_INVENTORY_PROPERTY -> 65;
              case TRUE_INVISIBILITY -> 66;
              case UNCRAFTABLE -> 67;
              case VAR_DETOXICATE -> 68;
              case VAR_PNEUMONIA -> 69;
              case VAR_PODAGRA -> 70;
              case VAR_STOMACHACHE -> 71;
              case WA_SANS -> 55;
              default -> 0;
            };
  }

  /**
   * 온오프 효과는 해당 효과를 적용받고 있지 않은 상태에서 효과를 받으면 무제한으로 적용이 되고
   * <p>해당 효과를 적용받고 있는 상태에서 해당 효과를 다시 받으면 해당 효과가 사라지는 효과임
   *
   * @return 온오프 효과면 true, 이외에는 false
   */
  public boolean isToggle()
  {
    return switch (this)
            {
              case COMBAT_MODE_MELEE, COMBAT_MODE_RANGED, PVP_MODE_ENABLED -> true;
              default -> false;
            };
  }

  /**
   * 다른 효과와 중첩되지 않는 효과를 반환합니다.
   *
   * @return 중첩되지 않는 효과 목록 혹은 빈 목록
   */
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
              case COMBAT_MODE_MELEE -> Collections.singletonList(CustomEffectType.COMBAT_MODE_RANGED);
              case COMBAT_MODE_RANGED -> Collections.singletonList(CustomEffectType.COMBAT_MODE_MELEE);
              default -> Collections.emptyList();
            };
  }

  /**
   * 해당 효과는 버프창에 표시되지 않으며 오로지 관리자가 명령어로만 참조할 수 있습니다.
   *
   * @return 숨겨진 효과면 true 이외에는 false
   */
  @SuppressWarnings("all")
  public boolean isHidden()
  {
    return switch (this)
            {
              case TROLL_INVENTORY_PROPERTY_MIN, VAR_PODAGRA_ACTIVATED, VAR_DETOXICATE_ACTIVATED, BREAD_KIMOCHI_SECONDARY_EFFECT, DISAPPEAR, DAMAGE_INDICATOR, NO_CUCUMBERY_ITEM_USAGE_ATTACK,
                      GLIDING, NOTIFY_NO_TRADE_ITEM_DROP, CUSTOM_DEATH_MESSAGE-> true;
              default -> false;
            };
  }

  /**
   * @return 버프창에서 시간을 표시할 효과면 true 이외에는 false
   */
  public boolean isTimeHidden()
  {
    return isToggle() || switch (this)
            {
              case DARKNESS_TERROR_ACTIVATED, SERVER_RADIO_LISTENING, NEWBIE_SHIELD, FANCY_SPOTLIGHT_ACTIVATED, TOWN_SHIELD -> true;
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

  /**
   * @return 디버프가 아니며, 우클릭으로 제거할 수 있는 효과면 true, 이외에는 false
   */
  public boolean isRightClickRemovable()
  {
    return !isNegative() && switch (this)
            {
              case SERVER_RADIO_LISTENING, NEWBIE_SHIELD, FANCY_SPOTLIGHT_ACTIVATED, VAR_DETOXICATE, TOWN_SHIELD, PARROTS_CHEER -> false;
              default -> true;
            };
  }

  /**
   * @return {@link CustomEffectType#BUFF_FREEZE} 효과로 인해 사망해도 효과가 사라지지 않으면 true, 이외에는 false
   */
  public boolean isBuffFreezable()
  {
    return switch (this)
            {
              case INVINCIBLE_PLUGIN_RELOAD, INVINCIBLE_RESPAWN, PVP_MODE_ENABLED, PVP_MODE_COOLDOWN -> false;
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

  /**
   * @return 해당 효과의 애니메이션 아이콘입니다.
   */
  @SuppressWarnings("unused")
  @NotNull
  private ItemStack getAnimatedIcon()
  {
    ItemStack itemStack = new ItemStack(Material.STONE);
    ItemMeta itemMeta = itemStack.getItemMeta();
    int period = 1000; // in millis;
    switch (this)
    {
      case BUFF_FREEZE -> itemStack = new ItemStack(Method2.getAnimated(ICON_BUFF_FREEZE, period));
      case NO_ENTITY_AGGRO -> {
        return itemStack;
      }
    }
    itemStack.setItemMeta(itemMeta);
    return itemStack;
  }

  /**
   * 효과의 기본 표시 유형을 반환합니다. 대부분의 경우 {@link DisplayType#BOSS_BAR}입니다.
   *
   * @return 해당 효과의 기본 표시 유형
   */
  @NotNull
  @SuppressWarnings("all")
  public DisplayType getDefaultDisplayType()
  {
    if (isHidden())
    {
      return DisplayType.NONE;
    }
    return switch (this)
            {
              default -> DisplayType.BOSS_BAR;
            };
  }

  @Override
  public boolean isHiddenEnum()
  {
    return isHidden() || switch (this)
            {
              case COOLDOWN_CHAT, COOLDOWN_ITEM_MEGAPHONE, DARKNESS_TERROR_ACTIVATED, RESURRECTION_INVINCIBLE, RESURRECTION_COOLDOWN, SERVER_RADIO_LISTENING, PARROTS_CHEER,
                      INVINCIBLE_PLUGIN_RELOAD, INVINCIBLE_RESPAWN, HEROS_ECHO_OTHERS, FANCY_SPOTLIGHT_ACTIVATED, NEWBIE_SHIELD, VAR_PODAGRA_ACTIVATED, VAR_DETOXICATE_ACTIVATED,
                      COMBAT_MODE_MELEE_COOLDOWN, COMBAT_MODE_RANGED_COOLDOWN, BREAD_KIMOCHI_SECONDARY_EFFECT, DISAPPEAR, DAMAGE_INDICATOR, NO_CUCUMBERY_ITEM_USAGE_ATTACK,
                      PVP_MODE_COOLDOWN -> true;
              default -> false;
            };
  }

  /**
   * @return 해당 효과의 최대 농도 레벨, 0부터 시작한다
   */
  public int getMaxAmplifier()
  {
    return maxAmplifier;
  }

  /**
   * @return 사망해도 사라지지 않을 효과면 true 이외에는 false
   */
  public boolean isKeepOnDeath()
  {
    return isKeepOnDeath;
  }

  /**
   * @return 접속을 종료해도 사라지지 않을 효과면 true 이외에는 false
   */
  public boolean isKeepOnQuit()
  {
    return isKeepOnQuit;
  }

  /**
   * @return 우유를 마셔도 사라지지 않을 효과면 true 이외에는 false
   */
  public boolean isKeepOnMilk()
  {
    return !isInstant() && switch (this)
            {
              // 우유 마시면 사라짐
              case CONFUSION, NOTHING, THICK, AWKWARD, UNCRAFTABLE, MUNDANE, FROST_WALKER, FEATHER_FALLING, CHEESE_EXPERIMENT, TRUE_INVISIBILITY, NO_ENTITY_AGGRO,
                      BREAD_KIMOCHI, BREAD_KIMOCHI_SECONDARY_EFFECT, FREEZING -> false;
              default -> true;
            };
  }

  /**
   * 디버프는 일반적으로 개체에게 부정적인 효과를 제공하며, 우클릭으로 제거할 수 없다.
   *
   * @return 디버프일 경우 true, 아닐 경우 false
   */
  public boolean isNegative()
  {
    return isNegative;
  }

  /**
   * 일반적으로 효과의 지속 시간은 접속을 유지한 상태에서만 흐르지만 이 효과는 접속을 하지 않아도 흐르는 효과를 나타냅니다
   *
   * @return 접속을 해도 시간이 흐르는 효과면 true, 이외에는 false
   */
  @SuppressWarnings("all")
  public boolean isRealDuration()
  {
    return switch (this)
            {
              case BLESS_OF_VILLAGER, BUFF_FREEZE, KEEP_INVENTORY, RESURRECTION, RESURRECTION_COOLDOWN -> true;
              default -> false;
            };
  }

  /**
   * @return 해당 효과의 농도 레벨이나 지속 시간 등 공통되는 설명 부분을 {@link Component}로 반환
   */
  @NotNull
  public Component getPropertyDescription()
  {
    Component description = Component.empty();
    boolean isNegative = this.isNegative();
    boolean keepOnDeath = this.isKeepOnDeath(), keepOnQuit = this.isKeepOnQuit(), keepOnMilk = this.isKeepOnMilk(), buffFreezable = this.isBuffFreezable();
    boolean isRealDuration = isRealDuration();

    if (!this.getDescription().equals(Component.empty()) && (keepOnDeath || !keepOnQuit || keepOnMilk || !buffFreezable || isRealDuration))
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
      description = description.append(Component.text("\n")).append(ComponentUtil.translate("&" + (isNegative ? "a" : "c") + "접속을 종료하면 효과가 사라집니다"));
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
    if (isRealDuration)
    {
      description = description.append(Component.text("\n")).append(
              ComponentUtil.translate("&" + (isNegative ? "a" : "c") + "접속을 유지하지 않아도 시간이 흐르는 효과입니다"));
    }
    if (isToggle())
    {
      description = description.append(Component.text("\n"));
      description = description.append(Component.text("\n"));
      description = description.append(ComponentUtil.translate("&6온오프 효과입니다 - 효과의 지속 시간이 무제한이고"));
      description = description.append(Component.text("\n"));
      description = description.append(ComponentUtil.translate("&6효과가 있을 때 효과를 적용받으면 사라지는 효과"));
    }
    return description;
  }
}
















