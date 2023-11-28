package com.jho5245.cucumbery.custom.customeffect.type;

import com.jho5245.cucumbery.Cucumbery;
import com.jho5245.cucumbery.custom.customeffect.CustomEffect;
import com.jho5245.cucumbery.custom.customeffect.CustomEffect.DisplayType;
import com.jho5245.cucumbery.custom.customeffect.CustomEffectManager;
import com.jho5245.cucumbery.custom.customeffect.TypeBuilder;
import com.jho5245.cucumbery.custom.customeffect.VanillaEffectDescription;
import com.jho5245.cucumbery.util.no_groups.MessageUtil;
import com.jho5245.cucumbery.util.storage.component.util.ComponentUtil;
import com.jho5245.cucumbery.util.storage.data.CustomMaterial;
import com.jho5245.cucumbery.util.storage.data.EnumHideable;
import com.jho5245.cucumbery.util.storage.data.TranslatableKeyParser;
import com.jho5245.cucumbery.util.storage.data.custom_enchant.CustomEnchant;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.translation.Translatable;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

/**
 * The type of {@link CustomEffect}
 */
@SuppressWarnings("unused")
public class CustomEffectType implements Translatable, EnumHideable
{
  public static final TypeBuilder
          REMOVE_ON_MILK = builder().removeOnMilk(),
          NEGATIVE = builder().negative(),
          NEGATIVE_MILK = builder().removeOnMilk().negative(),
          HIDDEN = builder().hidden();

  public static final CustomEffectType
          AWKWARD = new CustomEffectType("awkward", "어색함", "어색", builder().removeOnMilk()),
          THICK = new CustomEffectType("thick", "진함", builder().removeOnMilk()),

  BACKWARDS_CHAT = new CustomEffectType("backwards_chat", "팅채 로꾸거", builder().removeOnMilk().description("다니됩력입 로꾸거 이팅채")),
          MUNDANE = new CustomEffectType("mundane", "평범함", "평범", builder().removeOnMilk()),
          UNCRAFTABLE = new CustomEffectType("uncraftable", "제작 불가능함", builder().negative().maxAmplifier(2)),

  /**/ BANE_OF_ARTHROPODS = new CustomEffectType("bane_of_arthropods", Enchantment.DAMAGE_ARTHROPODS.translationKey(), builder().removeOnMilk().maxAmplifier(255)),
          SHARPNESS = new CustomEffectType("sharpness", Enchantment.DAMAGE_ALL.translationKey(), builder().removeOnMilk().maxAmplifier(255)),
          SMITE = new CustomEffectType("smite", Enchantment.DAMAGE_UNDEAD.translationKey(), builder().removeOnMilk().maxAmplifier(255)),

  /**/ CONTINUAL_SPECTATING = new CustomEffectType("continual_spectating", "관전 지속", builder().keepOnDeath().defaultDuration(-1)),
          CONTINUAL_SPECTATING_EXEMPT = new CustomEffectType("continual_spectating_exempt", "관전 지속 외출", builder().keepOnDeath()),

  CONTINUAL_RIDING = new CustomEffectType("continual_riding", "라이딩 지속", builder().keepOnDeath().defaultDuration(-1).description(ComponentUtil.translate("플레이어를 지속적으로 탑승합니다")
          .append(ComponentUtil.translate("해당 플레이어가 재접속하거나 리스폰해도 자동으로 탑승합니다")))),
          CONTINUAL_RIDING_EXEMPT = new CustomEffectType("continual_riding_exempt", "라이딩 지속 외출", builder().keepOnDeath().description("지속시간 동안은 강제 탑승이 되지 않습니다")),

  /**/ CURSE_OF_BEANS = new CustomEffectType("curse_of_beans", "콩의 저주 콩의 저주", builder().negative().keepOnDeath()),
          CURSE_OF_CONSUMPTION = new CustomEffectType("curse_of_consumption", "섭식 장애", builder().negative().keepOnDeath()),
          CURSE_OF_CREATIVITY = new CustomEffectType("curse_of_creativity", "건축 불가능", builder().negative().keepOnDeath()),
          CURSE_OF_CREATIVITY_BREAK = new CustomEffectType("curse_of_creativity_break", "블록 파괴 불가능", builder().negative().keepOnDeath()),
          CURSE_OF_CREATIVITY_PLACE = new CustomEffectType("curse_of_creativity_place", "블록 설치 불가능", builder().negative().keepOnDeath()),
          CURSE_OF_DROP = new CustomEffectType("curse_of_drop", "버리기 불가능", builder().negative().keepOnDeath()),
          CURSE_OF_PICKUP = new CustomEffectType("curse_of_pickup", "줍기 불가능", builder().negative().keepOnDeath()),
          CURSE_OF_INVENTORY = new CustomEffectType("curse_of_inventory", "인벤세이브 저주", builder().negative().keepOnDeath()),
          CURSE_OF_JUMPING = new CustomEffectType("curse_of_jumping", "점프 불가능", builder().negative().keepOnDeath()),
          CURSE_OF_MUSHROOM = new CustomEffectType("curse_of_mushroom", "버섯의 저주", builder().negative().keepOnDeath().maxAmplifier(999)),
          CURSE_OF_VANISHING = new CustomEffectType("curse_of_vanishing", Enchantment.VANISHING_CURSE.translationKey(), builder().negative().keepOnDeath().description("아이템을 버리면 즉시 사라집니다")),

  /**/ DARKNESS_TERROR = new CustomEffectType("darkness_terror", "어두운거 싫어요", builder().negative()),
          DARKNESS_TERROR_ACTIVATED = new CustomEffectType("darkness_terror_activated", "어둠의 공포", builder().negative().enumHidden().timeHidden().skipEvent()),
          DARKNESS_TERROR_RESISTANCE = new CustomEffectType("darkness_terror_resistance", "어둠의 공포 내성"),

  /**/ FANCY_SPOTLIGHT = new CustomEffectType("fancy_spotlight", "화려한 조명", builder().removeOnQuit()),
          FANCY_SPOTLIGHT_ACTIVATED = new CustomEffectType("fancy_spotlight_activated", "화려한 조명 활성화", builder().removeOnQuit().enumHidden().timeHidden().defaultDuration(20).skipEvent()),

  /**/ HEROS_ECHO = new CustomEffectType("heros_echo", "영웅의 메아리", builder().removeOnQuit().defaultDuration(20 * 60 * 40)),
          HEROS_ECHO_OTHERS = new CustomEffectType("heros_echo_others", "영웅의 메아리", builder().removeOnQuit().defaultDuration(20 * 60 * 40).enumHidden()),

  /**/ INVINCIBLE = new CustomEffectType("invincible", "무적"),
          INVINCIBLE_PLUGIN_RELOAD = new CustomEffectType("invincible_plugin_reload", "플러그인 리로드 무적", builder().defaultDuration(100).nonBuffFreezable().enumHidden()),
          INVINCIBLE_RESPAWN = new CustomEffectType("invincible_respawn", "리스폰 무적", builder().defaultDuration(100).nonBuffFreezable().enumHidden()),

  /**/ KINETIC_RESISTANCE = new CustomEffectType("kinetic_resistacne", "운동 에너지 저항", builder().maxAmplifier(9)),
          KNOCKBACK_RESISTANCE = new CustomEffectType("knockback_resistance", "넉백 저항", HIDDEN),
          KNOCKBACK_RESISTANCE_COMBAT = new CustomEffectType("knockback_resistance_combat", "넉백 저항(전투)", HIDDEN),
          KNOCKBACK_RESISTANCE_NON_COMBAT = new CustomEffectType("knockback_resistance_non_combat", "넉백 저항(비전투)", HIDDEN),
          LEVITATION_RESISTANCE = new CustomEffectType("levitation_resistance", "공중 부양 저항", builder().maxAmplifier(9)),

  /**/ INSIDER = new CustomEffectType("insider", "인싸", builder().negative().maxAmplifier(9)),
          OUTSIDER = new CustomEffectType("outsider", "아싸", builder().negative().maxAmplifier(9)),

  /**/ RESURRECTION = new CustomEffectType("resurrection", "리저렉션", builder().realDuration().defaultDuration(20 * 60 * 30)),
          RESURRECTION_COOLDOWN = new CustomEffectType("resurrection_cooldown", "리저렉션 저주", builder().enumHidden().keepOnDeath().defaultDuration(20 * 60 * 10).negative()),
          RESURRECTION_INVINCIBLE = new CustomEffectType("resurrection_invincible", "리저렉션 무적", builder().enumHidden().defaultDuration(20 * 2)),

  /**/ BLESS_OF_SANS = new CustomEffectType("bless_of_sans", "샌즈의 축복", builder().maxAmplifier(9)),
          WA_SANS = new CustomEffectType("wa_sans", "와 샌즈", builder().maxAmplifier(9)),

  /**/ SILK_TOUCH = new CustomEffectType("silk_touch", Enchantment.SILK_TOUCH.translationKey()),
          SMELTING_TOUCH = new CustomEffectType("smelting_touch", "제련의 손길"),
          TELEKINESIS = new CustomEffectType("telekinesis", "염력"),

  /**/ TROLL_INVENTORY_PROPERTY = new CustomEffectType("troll_inventory_property", "인벤토리 트롤", HIDDEN),
          TROLL_INVENTORY_PROPERTY_MIN = new CustomEffectType("troll_inventory_property_min", "인벤토리 트롤", HIDDEN),

  /**/ SPREAD = new CustomEffectType("spread", "전이", builder().negative().maxAmplifier(99)),
          VAR_DETOXICATE = new CustomEffectType("var_detoxicate", "변이 - 해독", builder().negative().maxAmplifier(99)),
          VAR_DETOXICATE_ACTIVATED = new CustomEffectType("var_detoxicate_activated", "a", HIDDEN),
          VAR_PNEUMONIA = new CustomEffectType("var_pneumonia", "변이 - 폐렴", builder().negative().maxAmplifier(99)),
          VAR_PODAGRA = new CustomEffectType("var_podagra", "변이 - 통풍", builder().negative().maxAmplifier(99)),
          VAR_PODAGRA_ACTIVATED = new CustomEffectType("var_podagra_activated", "f", HIDDEN),
          VAR_STOMACHACHE = new CustomEffectType("var_stomachache", "변이 - 소화불량", builder().negative().maxAmplifier(99)),

  /**/ COMBAT_BOOSTER = new CustomEffectType("combat_booster", "컴뱃 부스터"),
          COMBAT_MODE_MELEE = new CustomEffectType("combat_mode_melee", "전투 모드 - 근거리", builder().toggle()),
          COMBAT_MODE_MELEE_COOLDOWN = new CustomEffectType("combat_mode_melee_cooldown", "전투 모드 - 쿨타임", builder().negative().defaultDuration(100).enumHidden()),
          COMBAT_MODE_RANGED = new CustomEffectType("combat_mode_ranged", "전투 모드 - 원거리", builder().toggle()),
          COMBAT_MODE_RANGED_COOLDOWN = new CustomEffectType("combat_mode_ranged_cooldown", "전투 모드 - 원거리", builder().negative().defaultDuration(100).enumHidden()),

  /**/ ENDER_SLAYER = new CustomEffectType("ender_slayer", "엔더 슬레이어", builder().maxAmplifier(9999)),
          BOSS_SLAYER = new CustomEffectType("boss_slayer", "보스 슬레이어", builder().maxAmplifier(9999)),

  /**/ BREAD_KIMOCHI = new CustomEffectType("bread_kimochi", "빵 기모띠"),
          BREAD_KIMOCHI_SECONDARY_EFFECT = new CustomEffectType("bread_kimochi_2", "", HIDDEN),

  /**/ PVP_MODE = new CustomEffectType("pvp_mode", "", HIDDEN),
          PVP_MODE_ENABLED = new CustomEffectType("pvp_mode_enabled", "", HIDDEN),
          PVP_MODE_COOLDOWN = new CustomEffectType("pvp_mode_cooldown", "", HIDDEN),

  /**/ POSITION_MEMORIZE = new CustomEffectType("position_memorize", "위치 기억"),
          DORMAMMU = new CustomEffectType("dormammu", "도르마무", builder().negative().defaultDuration(100)),

  /**/ COMBO = new CustomEffectType("combo", "콤보", builder().removeOnQuit()),
          COMBO_STACK = new CustomEffectType("combo_stack", "콤보 스택", builder().enumHidden().defaultDuration(200).maxAmplifier(9999).removeOnQuit()),
          COMBO_DELAY = new CustomEffectType("combo_delay", "콤보 딜레이", builder().hidden().defaultDuration(2)),
          COMBO_EXPERIENCE = new CustomEffectType("combo_experience", "콤보 구슬", builder().hidden().defaultDuration(20 * 15)),

  /**/ KEEP_INVENTORY = new CustomEffectType("keep_inventory", "인벤토리 보존"),
          ADVANCED_KEEP_INVENTORY = new CustomEffectType("advanced_keep_inventory", "인벤토리 보존 Ver.2", builder().keepOnDeath()),

  /**/ BUFF_FREEZE = new CustomEffectType("buff_freeze", "버프 프리저"),
          BUFF_FREEZE_D = new CustomEffectType("buff_freeze_d", "버프 프리저", builder().keepOnDeath()),
          CUCUMBERY_UPDATER = new CustomEffectType("cucumbery_updater", "즉시 큐컴버리 업데이트", builder().instant()),
          CHEESE_EXPERIMENT = new CustomEffectType("cheese_experiment", "치즈 실험", builder().negative().removeOnMilk()),
          CONFUSION = new CustomEffectType("confusion", "혼란", builder().negative().keepOnDeath().hidden()),
          DEBUG_WATCHER = new CustomEffectType("debug_watcher", "디버그 염탐", builder().keepOnDeath()),
          DO_NOT_PICKUP_BUT_THROW_IT = new CustomEffectType("do_not_pickup_but_throw_it", "줍지 마, 던져!", builder().negative().maxAmplifier(9)),
          DODGE = new CustomEffectType("dodge", "회피", builder().maxAmplifier(99)),
          ELYTRA_BOOSTER = new CustomEffectType("elytra_booster", "겉날개 부스터", builder().maxAmplifier(9)),
          FEATHER_FALLING = new CustomEffectType("feather_falling", Enchantment.PROTECTION_FALL.translationKey(), builder().maxAmplifier(9)),
          FROST_WALKER = new CustomEffectType("frost_walker", Enchantment.FROST_WALKER.translationKey()),
          HEALTH_INCREASE = new CustomEffectType("health_increase", "HP 증가", builder().maxAmplifier(99)),
          IDIOT_SHOOTER = new CustomEffectType("idiot_shooter", "똥손", builder().negative().keepOnDeath().maxAmplifier(19)),
          MUTE = new CustomEffectType("mute", "채팅 금지", builder().negative().keepOnDeath()),
          NEWBIE_SHIELD = new CustomEffectType("newbie_shield", "뉴비 보호막", builder().nonRemovable().enumHidden().defaultDuration(2).timeHidden().skipEvent()),
          NO_ENTITY_AGGRO = new CustomEffectType("no_entity_aggro", "개체 관심 없음"),
          PARROTS_CHEER = new CustomEffectType("parrots_cheer", "앵무새의 가호", builder().timeHiddenWhenFull().defaultDuration(100)),
          SERVER_RADIO_LISTENING = new CustomEffectType("server_radio_listening", "서버 라디오 분위기", builder().enumHidden().nonRemovable().maxAmplifier(4).timeHidden().defaultDuration(-1).removeOnQuit().skipEvent()),
          STOP = new CustomEffectType("stop", "멈춰!", NEGATIVE),
          TRUE_INVISIBILITY = new CustomEffectType("true_invisibility", "찐 투명화"),
          BLESS_OF_VILLAGER = new CustomEffectType("bless_of_villager", "주민의 축복"),
          TOWN_SHIELD = new CustomEffectType("town_shield", "마을 보호막", builder().timeHidden().nonRemovable()),
          EXPERIENCE_BOOST = new CustomEffectType("experience_boost", "EXP 부스트", builder().maxAmplifier(9999)),
          DISAPPEAR = new CustomEffectType("disappear", "", HIDDEN),
          NO_BUFF_REMOVE = new CustomEffectType("no_buff_remove", "효과 제거 불능", NEGATIVE),
          NO_REGENERATION = new CustomEffectType("no_regeneration", "재생 불능", builder().negative().removeOnMilk()),
          DAMAGE_INDICATOR = new CustomEffectType("damage_indicator", "", builder().hidden().defaultDuration(23)),
          FREEZING = new CustomEffectType("freezing", "얼음!", builder().negative()),
          NO_CUCUMBERY_ITEM_USAGE_ATTACK = new CustomEffectType("no_cucumbery_item_usage_attack", "", HIDDEN),
          GLIDING = new CustomEffectType("gliding", "", builder().hidden().skipEvent()),
          NOTIFY_NO_TRADE_ITEM_DROP = new CustomEffectType("notify_no_trade_item_drop", "아이템", HIDDEN),
          CUSTOM_DEATH_MESSAGE = new CustomEffectType("custom_death_message", "커스텀 데스 메시지", HIDDEN),
          DYNAMIC_LIGHT = new CustomEffectType("dynamic_light", "동적 조명", builder().keepOnDeath().description(ComponentUtil.translate("손에 빛이 나는 아이템을 들고 있으면 %s 효과가 적용됩니다", PotionEffectType.NIGHT_VISION))),
          REMOVE_NO_DAMAGE_TICKS = new CustomEffectType("remove_no_damage_ticks", "피격 시 무적 시간 미적용"),
          MASTER_OF_FISHING = new CustomEffectType("master_of_fishing", "낚시의 대가", builder().description("캐스팅 즉시 입질됩니다!")),
          MASTER_OF_FISHING_D = new CustomEffectType("master_of_fishing_d", "낚시의 대가", builder().keepOnDeath().description("캐스팅 즉시 입질됩니다!")),
          ASSASSINATION = new CustomEffectType("assassination", "암살", builder().description(ComponentUtil.translate("다른 플레이어나 동물을 잡아서 뜨는 데스 메시지에").append(Component.text("\n")).append(ComponentUtil.translate("자신의 이름이 나타나지 않고 감춰집니다")))),
          ALARM = new CustomEffectType("alarm", "알람", builder().enumHidden().keepOnDeath().removeOnQuit().maxAmplifier(2).description("알람이다! 소리 꺼라")),
          COOLDOWN_THE_MUSIC = new CustomEffectType("cooldown_the_music", MessageUtil.stripColor(ComponentUtil.serialize(CustomMaterial.THE_MUSIC.getDisplayName())), builder().hidden().defaultDuration(20 * 10).keepOnDeath()),
          GAESANS = new CustomEffectType("gaesans", "개샌즈", builder().description("웅크리면 %s 효과가 적용됩니다", PotionEffectType.INVISIBILITY)),
          PICKLED = new CustomEffectType("pickled", "절여짐", builder().description("당신은 절여졌습니다!")),
          NIGHT_VISION_SPECTATOR = new CustomEffectType("night_vision_spectator", "관전 모드 야간 투시", builder().keepOnDeath().defaultDuration(-1)
                  .description("관전 모드에서 다른 개체 관전 시 %s 효과가 적용됩니다", PotionEffectType.NIGHT_VISION)),

  SUPERIOR_LEVITATION = new CustomEffectType("superior_levitation", "강력한 공중 부양", builder().negative().maxAmplifier(127).icon(new ItemStack(Material.FEATHER)).description("노빠꾸로 공중으로 떠오릅니다")),

  SPYGLASS_TELEPORT = new CustomEffectType("spyglass_teleport", "망원경 텔레포트", builder().enumHidden().defaultDuration(60)),

  SPYGLASS_TELEPORT_COOLDOWN = new CustomEffectType("spyglass_teleport_cooldown", "망원경 텔레포트 쿨타임", builder().negative().enumHidden().defaultDuration(1200).keepOnDeath()),

  ENTITY_REMOVER = new CustomEffectType("entity_remover", "개체 삭제", builder().instant().description("개체를 삭제하거나 죽입니다")),

  SHIVA_NO_ONE_CAN_BLOCK_ME = new CustomEffectType("shiva_no_one_can_block_me", "씨바 아무도 날 막을 순 없으셈 ㅋㅋ", builder().description("다 죽어부러!")),

  GD_TO_HI = new CustomEffectType("gd_to_hi", "gd를 ㅎㅇ로 바꿔주는 마법의 효과", builder().keepOnDeath()),

  AUTO_CHAT_PARSER = new CustomEffectType("auto_chat_parser", "채팅을 자동으로 파싱해주는 마법의 효과", builder().keepOnDeath()),

  STRANGE_CREATIVE_MODE = new CustomEffectType("strange_creative_mode", "뭔가 이상한 크리에이티브 모드", builder().removeOnQuit().keepOnDeath()
          .description(ComponentUtil.translate("크리에이티브인데.. 서바이벌인가..?")
                  .append(Component.text("\n"))
                  .append(Component.text("\n"))
                  .append(ComponentUtil.translate("크리에이티브 모드처럼 블록과 아이템을 자유롭게"))
                  .append(Component.text("\n"))
                  .append(ComponentUtil.translate("사용할 수 있으나 비행 모드가 비활성화되고"))
                  .append(Component.text("\n"))
                  .append(ComponentUtil.translate("%s(%s) 기능을 사용할 수 없으며", ComponentUtil.translate("key.pickItem"), Component.keybind("key.pickItem")))
                  .append(Component.text("\n"))
                  .append(ComponentUtil.translate("서바이벌 모드와 동일하게 다른 적대적 개체의"))
                  .append(Component.text("\n"))
                  .append(ComponentUtil.translate("표적이 될 수 있고 피해를 입을 수 있습니다"))
          )),

  ASCENSION = new CustomEffectType("ascension", "승천", builder().description(
          ComponentUtil.translate("%s 효과와 비슷하게 공중으로 떠오르나", PotionEffectType.LEVITATION)
                  .append(Component.text("\n"))
                  .append(ComponentUtil.translate("지면에 맞닿아 있을 때와 이후 0.3초간은"))
                  .append(Component.text("\n"))
                  .append(ComponentUtil.translate("지면에 맞닿아 있지 않아도 공중으로 떠오르지 않고"))
                  .append(Component.text("\n"))
                  .append(ComponentUtil.translate("웅크리면 천천히 하강하고 낙하 피해를 입지 않습니다"))
                  .append(Component.text("\n"))
                  .append(ComponentUtil.translate("농도 레벨이 높을 수록 더 빨리 상승하거나 하강합니다"))
  ).maxAmplifier(60)),

  ASCENSION_COOLDOWN = new CustomEffectType("ascension_cooldown", "승천(저항)", builder().defaultDuration(6)),

  FLY = new CustomEffectType("fly", "플라이", builder().keepOnDeath().description(
          ComponentUtil.translate("비행 모드가 활성화 됩니다")
                  .append(Component.text("\n"))
                  .append(ComponentUtil.translate("자유롭게 비행할 수 있으며 낙하 피해를 입지 않습니다"))
  ).icon(() ->
  {
    ItemStack itemStack = new ItemStack(Material.ELYTRA);
    ItemMeta itemMeta = itemStack.getItemMeta();
    itemMeta.setCustomModelData(5201);
    itemStack.setItemMeta(itemMeta);
    return itemStack;
  })),

  FLY_REMOVE_ON_QUIT = new CustomEffectType("fly_remove_on_quit", "플라이", builder().keepOnDeath().removeOnQuit().description(
          ComponentUtil.translate("비행 모드가 활성화 됩니다")
                  .append(Component.text("\n"))
                  .append(ComponentUtil.translate("자유롭게 비행할 수 있으며 낙하 피해를 입지 않습니다"))
  ).icon(() ->
  {
    ItemStack itemStack = new ItemStack(Material.ELYTRA);
    ItemMeta itemMeta = itemStack.getItemMeta();
    itemMeta.setCustomModelData(5201);
    itemStack.setItemMeta(itemMeta);
    return itemStack;
  })),

  FLY_NOT_ENABLED = new CustomEffectType("fly_not_enabled", "플라이 불가 지역", builder().keepOnDeath().negative().description("비행 모드를 사용할 수 없는 지역입니다").timeHidden().icon(() ->
  {
    ItemStack itemStack = new ItemStack(Material.ELYTRA);
    ItemMeta itemMeta = itemStack.getItemMeta();
    itemMeta.setCustomModelData(5203);
    itemMeta.setUnbreakable(true);
    ((Damageable) itemMeta).setDamage(Material.ELYTRA.getMaxDurability() - 1);
    itemStack.setItemMeta(itemMeta);
    return itemStack;
  })),

  MAESTRO = new CustomEffectType("maestro", "마에스트로", builder().description(
          ComponentUtil.translate("작곡가가 되었다. 크리에이티브 모드에서 채팅창에")
                  .append(Component.text("\n"))
                  .append(ComponentUtil.translate("숫자를 입력하면 바로 밑에 해당 음높이를 가진 소리 블록"))
                  .append(Component.text("\n"))
                  .append(ComponentUtil.translate("을 설치하거나 이미 설치되어 있는 소리 블록의 음높이를 변경한다"))
                  .append(Component.text("\n"))
                  .append(ComponentUtil.translate("단, 해당 위치에 다른 블록이 있을 경우 설치되지 않는다"))
  ).keepOnDeath().defaultDuration(-1)),

  CUSTOM_MATERIAL_TNT_DONUT = new CustomEffectType("custom_material_tnt_donut", "", builder().hidden().defaultDuration(-1).skipEvent()),

  DAMAGE_SPREAD = new CustomEffectType("damage_spread", "피해 발산", builder().hidden().defaultDuration(100).skipEvent().removeOnQuit().nonBuffFreezable()),

  /**/ NOTHING = new CustomEffectType("nothing", "아무것도 아님"),

  /**/ TEST = new CustomEffectType(new NamespacedKey("test", "test"), "key:effect.cucumbery.test|테스트뭐", builder());

  private static final HashMap<NamespacedKey, CustomEffectType> effects = new HashMap<>();

  public static void register()
  {
    register(AWKWARD, THICK, BACKWARDS_CHAT, MUNDANE, UNCRAFTABLE,

            DAMAGE_SPREAD,

            BANE_OF_ARTHROPODS, SHARPNESS, SMITE, MAESTRO, CUSTOM_MATERIAL_TNT_DONUT,

            ASCENSION, ASCENSION_COOLDOWN, FLY, FLY_REMOVE_ON_QUIT, FLY_NOT_ENABLED,

            CONTINUAL_RIDING, CONTINUAL_RIDING_EXEMPT,

            ENTITY_REMOVER, SHIVA_NO_ONE_CAN_BLOCK_ME, GD_TO_HI, AUTO_CHAT_PARSER, STRANGE_CREATIVE_MODE,

            SPYGLASS_TELEPORT, SPYGLASS_TELEPORT_COOLDOWN,

            COOLDOWN_THE_MUSIC, PICKLED, NIGHT_VISION_SPECTATOR,

            CONTINUAL_SPECTATING, CONTINUAL_SPECTATING_EXEMPT,

            CURSE_OF_BEANS, CURSE_OF_CONSUMPTION, CURSE_OF_CREATIVITY, CURSE_OF_CREATIVITY_BREAK, CURSE_OF_CREATIVITY_PLACE, CURSE_OF_DROP, CURSE_OF_PICKUP, CURSE_OF_VANISHING,

            CURSE_OF_INVENTORY, CURSE_OF_JUMPING, CURSE_OF_MUSHROOM,

            DARKNESS_TERROR, DARKNESS_TERROR_ACTIVATED, DARKNESS_TERROR_RESISTANCE,

            FANCY_SPOTLIGHT, FANCY_SPOTLIGHT_ACTIVATED,

            HEROS_ECHO, HEROS_ECHO_OTHERS,

            INVINCIBLE, INVINCIBLE_RESPAWN, INVINCIBLE_PLUGIN_RELOAD,

            KINETIC_RESISTANCE, KNOCKBACK_RESISTANCE, KNOCKBACK_RESISTANCE_COMBAT, KNOCKBACK_RESISTANCE_NON_COMBAT, LEVITATION_RESISTANCE,

            INSIDER, OUTSIDER,

            RESURRECTION, RESURRECTION_COOLDOWN, RESURRECTION_INVINCIBLE,

            BLESS_OF_SANS, WA_SANS,

            SILK_TOUCH, SMELTING_TOUCH, TELEKINESIS,

            TROLL_INVENTORY_PROPERTY, TROLL_INVENTORY_PROPERTY_MIN,

            SPREAD, VAR_DETOXICATE, VAR_DETOXICATE_ACTIVATED, VAR_PNEUMONIA, VAR_PODAGRA, VAR_PODAGRA_ACTIVATED, VAR_STOMACHACHE,

            COMBAT_BOOSTER, COMBAT_MODE_MELEE, COMBAT_MODE_MELEE_COOLDOWN, COMBAT_MODE_RANGED, COMBAT_MODE_RANGED_COOLDOWN,

            ENDER_SLAYER, BOSS_SLAYER,

            BREAD_KIMOCHI, BREAD_KIMOCHI_SECONDARY_EFFECT,

            PVP_MODE, PVP_MODE_ENABLED, PVP_MODE_COOLDOWN,

            POSITION_MEMORIZE, DORMAMMU,

            COMBO, COMBO_DELAY, COMBO_STACK,

            KEEP_INVENTORY, ADVANCED_KEEP_INVENTORY,

            BUFF_FREEZE, BUFF_FREEZE_D, CUCUMBERY_UPDATER, CHEESE_EXPERIMENT,
            CONFUSION, DEBUG_WATCHER, DO_NOT_PICKUP_BUT_THROW_IT,
            DODGE, ELYTRA_BOOSTER, FEATHER_FALLING, FROST_WALKER,
            HEALTH_INCREASE, IDIOT_SHOOTER, MUTE,
            NEWBIE_SHIELD, NO_ENTITY_AGGRO, PARROTS_CHEER,
            SERVER_RADIO_LISTENING, STOP, TRUE_INVISIBILITY, BLESS_OF_VILLAGER,
            TOWN_SHIELD, EXPERIENCE_BOOST, DISAPPEAR, NO_BUFF_REMOVE, NO_REGENERATION,
            DAMAGE_INDICATOR, FREEZING, NO_CUCUMBERY_ITEM_USAGE_ATTACK, GLIDING, NOTIFY_NO_TRADE_ITEM_DROP, DYNAMIC_LIGHT,
            CUSTOM_DEATH_MESSAGE,
            REMOVE_NO_DAMAGE_TICKS, MASTER_OF_FISHING, MASTER_OF_FISHING_D, ASSASSINATION, ALARM, GAESANS, SUPERIOR_LEVITATION,

            NOTHING,
            TEST);
    CustomEffectTypeCooldown.registerEffect();
    CustomEffectTypeCustomMining.registerEffect();
    if (Cucumbery.using_GSit)
    {
      CustomEffectTypeGSit.registerEffect();
    }
    CustomEffectTypeMinecraft.registerEffect();
    CustomEffectTypeReinforce.registerEffect();
    CustomEffectTypeRune.registerEffect();
  }

  public static void unregister()
  {
    effects.clear();
  }

  private final NamespacedKey namespacedKey;
  private final String id, translationKey, shortenTranslationKey;
  private final int maxAmplifier, defaultDuration, customModelData;
  private final boolean isBuffFreezable, isKeepOnDeath, isKeepOnMilk, isKeepOnQuit, isRealDuration, isRemoveable, isNegative, isInstant;
  private final boolean isToggle, isHidden, isTimeHidden, isTimeHiddenWhenFull, isEnumHidden, isStackDisplayed;

  private final boolean callEvent;
  private final Component description;
  private final ItemStack icon;
  private final DisplayType defaultDisplayType;

  protected CustomEffectType()
  {
    this("foo", "bar");
  }

  protected CustomEffectType(@NotNull String id, @NotNull String key)
  {
    this(id, key, null, new TypeBuilder());
  }

  protected CustomEffectType(@NotNull String id, @NotNull String key, @NotNull TypeBuilder builder)
  {
    this(id, key, null, builder);
  }

  protected CustomEffectType(@NotNull String id, @NotNull String key, @Nullable String key2, @NotNull TypeBuilder builder)
  {
    NamespacedKey namespacedKey = NamespacedKey.fromString(id, Cucumbery.getPlugin());
    if (namespacedKey == null)
    {
      throw new IllegalArgumentException("Invalid Custom Effect Type id! : " + id);
    }
    this.namespacedKey = namespacedKey;
    this.id = namespacedKey.value();
    this.translationKey = key;
    this.shortenTranslationKey = key2;

    this.isBuffFreezable = builder.isBuffFreezable();
    this.isKeepOnDeath = builder.isKeepOnDeath();
    this.isKeepOnMilk = builder.isKeepOnMilk();
    this.isKeepOnQuit = builder.isKeepOnQuit();
    this.isRealDuration = builder.isRealDuration();
    this.isRemoveable = builder.isRemoveable();
    this.isNegative = builder.isNegative();
    this.isInstant = builder.isInstant();
    this.isToggle = builder.isToggle();
    this.isHidden = builder.isHidden();
    this.isTimeHidden = builder.isTimeHidden();
    this.isTimeHiddenWhenFull = builder.isTimeHiddenWhenFull();
    this.isEnumHidden = builder.isEnumHidden();
    this.isStackDisplayed = builder.isStackDisplayed();

    this.maxAmplifier = builder.getMaxAmplifier();
    this.defaultDuration = builder.getDefaultDuration();
    this.customModelData = builder.getCustomModelData();

    this.description = builder.getDescription();
    this.icon = builder.getIcon();
    this.defaultDisplayType = builder.getDefaultDisplayType();

    this.callEvent = builder.doesCallEvent();
  }

  public CustomEffectType(@NotNull NamespacedKey namespacedKey, @NotNull String translationKey, @NotNull TypeBuilder builder)
  {
    this.namespacedKey = namespacedKey;
    this.id = namespacedKey.value();
    this.translationKey = translationKey;
    this.shortenTranslationKey = builder.getShortenTranslationKey();

    this.isBuffFreezable = builder.isBuffFreezable();
    this.isKeepOnDeath = builder.isKeepOnDeath();
    this.isKeepOnMilk = builder.isKeepOnMilk();
    this.isKeepOnQuit = builder.isKeepOnQuit();
    this.isRealDuration = builder.isRealDuration();
    this.isRemoveable = builder.isRemoveable();
    this.isNegative = builder.isNegative();
    this.isInstant = builder.isInstant();
    this.isToggle = builder.isToggle();
    this.isHidden = builder.isHidden();
    this.isTimeHidden = builder.isTimeHidden();
    this.isTimeHiddenWhenFull = builder.isTimeHiddenWhenFull();
    this.isEnumHidden = builder.isEnumHidden();
    this.isStackDisplayed = builder.isStackDisplayed();

    this.maxAmplifier = builder.getMaxAmplifier();
    this.defaultDuration = builder.getDefaultDuration();
    this.customModelData = builder.getCustomModelData();

    this.description = builder.getDescription();
    this.icon = builder.getIcon();
    this.defaultDisplayType = builder.getDefaultDisplayType();

    this.callEvent = builder.doesCallEvent();
  }

  public static void register(@NotNull CustomEffectType... CustomEffectTypes)
  {
    for (CustomEffectType effectType : CustomEffectTypes)
    {
      if (effects.containsKey(effectType.namespacedKey))
      {
        Cucumbery.getPlugin().getLogger().severe(
                "Already existing custom effect type registration! id : " + effectType.id + ", translation key : " + effects.get(effectType.namespacedKey).translationKey);
        continue;
      }
      effects.put(effectType.namespacedKey, effectType);
    }
  }

  protected static TypeBuilder builder()
  {
    return new TypeBuilder();
  }

  @Nullable
  public static CustomEffectType getByKey(@NotNull NamespacedKey key)
  {
    if (effects.containsKey(key))
    {
      return effects.get(key);
    }
    return null;
  }

  /**
   * Gets an immutable map of {@link CustomEffectType}
   *
   * @return map of all available {@link CustomEffectType}
   */
  @NotNull
  public static Map<NamespacedKey, CustomEffectType> getEffectTypeMap()
  {
    return Collections.unmodifiableMap(effects);
  }

  /**
   * Gets an immutable list of {@link CustomEffectType}
   *
   * @return list of all available {@link CustomEffectType}
   */
  @NotNull
  public static List<CustomEffectType> getEffectTypeList()
  {
    return List.copyOf(effects.values());
  }

  @NotNull
  public static CustomEffectType valueOf(String namespaceKey)
  {
    if (!namespaceKey.contains(":"))
    {
      namespaceKey = "cucumbery:" + namespaceKey;
    }
    String[] rawKeySplit = namespaceKey.split(":");
    CustomEffectType customEffectType = CustomEffectType.getByKey(new NamespacedKey(rawKeySplit[0], rawKeySplit[1]));
    if (customEffectType == null)
    {
      throw new NoSuchElementException("Invalid Custom Effect Type! " + namespaceKey);
    }
    return customEffectType;
  }

  @NotNull
  public Component getDescription()
  {
    return getDescription(null);
  }

  @NotNull
  public Component getDescription(@Nullable Player viewer)
  {
    if (!this.description.equals(Component.empty()))
    {
      Component modifiedDescrption = description;
      if (viewer != null)
      {
        if (!CustomEffectManager.hasEffect(viewer, CustomEffectTypeCustomMining.CUSTOM_MINING_SPEED_MODE))
        {
          if (this == CustomEffectTypeCustomMining.AQUA_AFFINITY || this == CustomEffectTypeCustomMining.AIR_SCAFFOLDING || this == CustomEffectTypeCustomMining.HASTE || this == CustomEffectTypeCustomMining.MINING_FATIGUE ||
                  this == CustomEffectTypeCustomMining.TITANIUM_FINDER || this == CustomEffectTypeCustomMining.MINING_FORTUNE || this == CustomEffectTypeCustomMining.MOLE_CLAW || this == CustomEffectTypeCustomMining.MINDAS_TOUCH ||
                  this == CustomEffectTypeCustomMining.MINING_BOOSTER)
          {
            modifiedDescrption = modifiedDescrption.append(Component.text("\n"));
            modifiedDescrption = modifiedDescrption.append(ComponentUtil.translate("&c현재 위치에서는 적용되지 않는 효과입니다!"));
          }
        }
        if (CustomEffectManager.hasEffect(viewer, CustomEffectType.FLY_NOT_ENABLED))
        {
          if (this == CustomEffectType.FLY || this == CustomEffectType.FLY_REMOVE_ON_QUIT)
          {
            modifiedDescrption = modifiedDescrption.append(Component.text("\n"));
            modifiedDescrption = modifiedDescrption.append(ComponentUtil.translate("&c현재 위치에서는 적용되지 않는 효과입니다!"));
          }
        }
      }
      return modifiedDescrption;
    }
    return switch ((this.getNamespacedKey().getNamespace().equals("minecraft") ? "MINECRAFT_" : "") + this.getIdString().toUpperCase())
            {
              case "MUTE" -> ComponentUtil.translate("채팅을 할 수 없는 상태입니다");
              case "CURSE_OF_MUSHROOM" -> ComponentUtil.translate("농도 레벨 * 0.1% 확률로 5초마다 인벤토리에 버섯이 들어옵니다");
              case "INVINCIBLE" -> ComponentUtil.translate("어떠한 형태의 피해도 받지 않습니다");
              case "BUFF_FREEZE" -> ComponentUtil.translate("사망 시 버프를 소모하여 일부 버프를 제외한 버프가 사라지지 않습니다");
              case "CONFUSION" -> ComponentUtil.translate("방향키가 반대로 작동합니다");
              case "RESURRECTION" -> ComponentUtil.translate("죽음에 이르는 피해를 입었을 때, 죽지 않고")
                      .append(Component.text("\n"))
                      .append(ComponentUtil.translate("버프를 소모하여 2초간 무적이 됩니다"));
              case "RESURRECTION_INVINCIBLE" -> ComponentUtil.translate("2초간 무적이 됩니다");
              case "RESURRECTION_COOLDOWN" -> ComponentUtil.translate("%s 버프를 받을 수 없는 상태입니다", CustomEffectType.RESURRECTION);
              case "FROST_WALKER" -> ComponentUtil.translate("마그마 블록 위를 걸어도 피해를 입지 않습니다");
              case "FEATHER_FALLING" -> ComponentUtil.translate("낙하 대미지를 받기 위한 최소 높이가 증가하고,")
                      .append(Component.text("\n"))
                      .append(ComponentUtil.translate("낙하 피해량이 감소합니다"));
              case "BLESS_OF_SANS", "SHARPNESS" -> ComponentUtil.translate("근거리 대미지가 증가합니다");
              case "PARROTS_CHEER" -> ComponentUtil.translate("HP가 5 이하일 때 15 블록 이내에 자신이 길들인 앵무새가")
                      .append(Component.text("\n"))
                      .append(ComponentUtil.translate("있으면 받는 피해량이 45% 감소하고, 대미지가 10% 증가합니다"));
              case "SMITE" -> ComponentUtil.translate("언데드 개체 공격 시 근거리 대미지가 증가합니다");
              case "BANE_OF_ARTHROPODS" -> ComponentUtil.translate("절지동물류 개체 공격 시 근거리 대미지가 증가하고,")
                      .append(Component.text("\n"))
                      .append(ComponentUtil.translate("%s 4단계 효과를 부여합니다", ComponentUtil.translate("&ceffect.minecraft.slowness")));
              case "STOP" -> ComponentUtil.translate("모든 행동을 할 수 없는 상태입니다")
                      .append(Component.text("\n"))
                      .append(ComponentUtil.translate("좌우이동, 웅크리기를 할 때마다 지속 시간이 6초씩 감소합니다"));
              case "KEEP_INVENTORY", "ADVANCED_KEEP_INVENTORY" -> ComponentUtil.translate("죽어도 아이템을 떨어뜨리지 않습니다");
              case "DO_NOT_PICKUP_BUT_THROW_IT" -> ComponentUtil.translate("아이템을 줍는 대신 던집니다")
                      .append(Component.text("\n"))
                      .append(ComponentUtil.translate("농도 레벨이 높을 수록 더 멀리 던집니다"));
              case "INSIDER" -> ComponentUtil.translate("채팅이 여러번 입력되고, 사망 시 모든 플레이어에게")
                      .append(Component.text("\n"))
                      .append(ComponentUtil.translate("타이틀로 자신의 데스 메시지를 띄워줍니다"));
              case "OUTSIDER" -> ComponentUtil.translate("일정 확률로 채팅 메시지가 보내지지 않고")
                      .append(Component.text("\n"))
                      .append(ComponentUtil.translate("입장 메시지, 퇴장 메시지가 뜨지 않습니다"));
              case "CURSE_OF_BEANS" -> ComponentUtil.translate("뭔가.. 자꾸.. 2번씩 일어난다")
                      .append(Component.text("\n"))
                      .append(ComponentUtil.translate("뭔가.. 자꾸.. 2번씩 일어난다"));
              case "SILK_TOUCH" -> ComponentUtil.translate("블록을 캐면 섬세한 손길 마법과")
                      .append(Component.text("\n"))
                      .append(ComponentUtil.translate("동일하게 아이템을 얻을 수 있습니다"));
              case "TELEKINESIS" -> ComponentUtil.translate("블록을 캐거나 적을 처치했을 때 드롭하는")
                      .append(Component.text("\n"))
                      .append(ComponentUtil.translate("아이템이 즉시 인벤토리에 들어옵니다"))
                      .append((Cucumbery.using_mcMMO ? (
                              Component.text("\n")
                                      .append(ComponentUtil.translate("&e단, mcMMO의 추가 아이템 드롭은 적용되지 않습니다"))
                      ) : Component.empty()));
              case "SMELTING_TOUCH" -> ComponentUtil.translate("블록을 캐거나 적을 처치했을 때 드롭하는")
                      .append(Component.text("\n"))
                      .append(ComponentUtil.translate("아이템을 제련된 형태로 바꿔줍니다"));
              case "CURSE_OF_INVENTORY" -> ComponentUtil.translate("사망 시 인벤세이브 여부와 관계없이")
                      .append(Component.text("\n"))
                      .append(ComponentUtil.translate("가지고 있는 모든 아이템과 경험치를 떨어뜨립니다"));
              case "CURSE_OF_CREATIVITY" -> ComponentUtil.translate("블록을 설치하거나 파괴할 수 없는 상태입니다");
              case "CURSE_OF_CREATIVITY_BREAK" -> ComponentUtil.translate("블록을 파괴할 수 없는 상태입니다");
              case "CURSE_OF_CREATIVITY_PLACE" -> ComponentUtil.translate("블록을 설치할 수 없는 상태입니다");
              case "CURSE_OF_CONSUMPTION" -> ComponentUtil.translate("음식이나 포션을 사용할 수 없는 상태입니다");
              case "CURSE_OF_PICKUP" -> ComponentUtil.translate("아이템을 주울 수 없는 상태입니다");
              case "CURSE_OF_DROP" -> ComponentUtil.translate("아이템을 버릴 수 없는 상태입니다");
              case "CURSE_OF_JUMPING" -> ComponentUtil.translate("점프를 할 수 없는 상태입니다");
              case "KINETIC_RESISTANCE" -> ComponentUtil.translate("겉날개 활강 중 블록에 부딪혀서 받는 피해량이 감소합니다")
                      .append(Component.text("\n"))
                      .append(ComponentUtil.translate("낙하 대미지는 감소하지 않습니다"));
              case "ELYTRA_BOOSTER" -> ComponentUtil.translate("겉날개 활강 중 폭죽으로 가속할 때")
                      .append(Component.text("\n"))
                      .append(ComponentUtil.translate("일정 확률로 폭죽을 소비하지 않습니다"));
              case "LEVITATION_RESISTANCE" -> ComponentUtil.translate("셜커에게 공격받아도 일정 확률로")
                      .append(Component.text("\n"))
                      .append(ComponentUtil.translate("공중 부양 상태 효과가 적용되지 않습니다"));
              case "CHEESE_EXPERIMENT" -> ComponentUtil.translate("우유를 마시면 효과가 사라지고 멀미가 30초간 지속됩니다")
                      .append(Component.text("\n"))
                      .append(ComponentUtil.translate("또한, 80% 확률로 허기가 30초 지속됩니다"));
              case "IDIOT_SHOOTER" -> ComponentUtil.translate("발사체가 이상한 방향으로 날아갑니다");
              case "DEBUG_WATCHER" -> ComponentUtil.translate("플러그인 디버그 메시지를 볼 수 있게 됩니다");
              case "CUCUMBERY_UPDATER" -> ComponentUtil.translate("큐컴버리 플러그인을 업데이트합니다");
              case "NOTHING" -> ComponentUtil.translate("놀랍게도 아무런 효과도 지니고 있지 않습니다");
              case "TROLL_INVENTORY_PROPERTY", "TROLL_INVENTORY_PROPERTY_MIN" -> ComponentUtil.translate("인벤토리의 숫자가 자꾸 멋대로 바뀝니다")
                      .append(Component.text("\n"))
                      .append(ComponentUtil.translate("잘만 이용하면 오히려 더 좋을 수도..?"));
              case "MUNDANE" -> ComponentUtil.translate("평범하다...");
              case "AWKWARD" -> ComponentUtil.translate("어... 그게.. 어색? 해 진다? 라고 생각? 합니다");
              case "THICK" -> ComponentUtil.translate("채팅이 진해집니다");
              case "UNCRAFTABLE" -> ComponentUtil.translate("아이템을 제작할 수 없습니다");
              case "COOLDOWN_CHAT" -> ComponentUtil.translate("쿨타임 동안 채팅을 할 수 없습니다");
              case "COOLDOWN_ITEM_MEGAPHONE" -> ComponentUtil.translate("쿨타임 동안 아이템 확성기를 사용할 수 없습니다");
              case "SERVER_RADIO_LISTENING" -> ComponentUtil.translate("서버 노래를 들어서 기분이 들떠 대미지가 증가합니다");
              case "DARKNESS_TERROR" -> ComponentUtil.translate("어두운거... 무섭다...")
                      .append(Component.text("\n"))
                      .append(ComponentUtil.translate("%s 효과나 손에 빛을 내는 아이템 없이 어두운 곳에 가면 %s 효과가 걸립니다",
                              ComponentUtil.translate(TranslatableKeyParser.getKey(PotionEffectType.NIGHT_VISION), NamedTextColor.GREEN), DARKNESS_TERROR_ACTIVATED));
              case "DARKNESS_TERROR_ACTIVATED" -> ComponentUtil.translate("너무 어둡습니다! 피해량이 30% 증가하고 블록을 캘 때마다")
                      .append(Component.text("\n"))
                      .append(ComponentUtil.translate("%s 확률로 받는 피해량 증가에 영향을 받는 1의 피해를 입습니다", "rg255,204;15%"));
              case "DARKNESS_TERROR_RESISTANCE" -> ComponentUtil.translate("%s 효과에 대한 내성이 생깁니다", DARKNESS_TERROR_ACTIVATED);
              case "DODGE" -> ComponentUtil.translate("일정 확률로 공격을 회피합니다");
              case "NEWBIE_SHIELD" -> ComponentUtil.translate("플레이 시간이 1시간 미만인 당신!")
                      .append(Component.text("\n"))
                      .append(ComponentUtil.translate("받는 피해량이 감소하고, 대미지가 증가합니다"))
                      .append(Component.text("\n"))
                      .append(ComponentUtil.translate("플레이 시간이 증가할 수록 효과가 감소하고 1시간이 지나면 효과가 사라집니다"));
              case "INVINCIBLE_PLUGIN_RELOAD" -> ComponentUtil.translate("플러그인을 리도드하는 중입니다");
              case "INVINCIBLE_RESPAWN" -> ComponentUtil.translate("리스폰 무적 상태입니다");
              case "HEROS_ECHO", "HEROS_ECHO_OTHERS" -> ComponentUtil.translate("최종 대미지가 5% 증가합니다");
              case "FANCY_SPOTLIGHT" -> ComponentUtil.translate("화려한 조명이 나를 비추네~")
                      .append(Component.text("\n"))
                      .append(ComponentUtil.translate("주변에 충분히 밝은 블록이 있으면 %s와(과) %s이(가) 적용됩니다",
                              ComponentUtil.translate(TranslatableKeyParser.getKey(PotionEffectType.SPEED), NamedTextColor.GREEN), ComponentUtil.translate(TranslatableKeyParser.getKey(PotionEffectType.REGENERATION), NamedTextColor.GREEN)));
              case "FANCY_SPOTLIGHT_ACTIVATED" -> ComponentUtil.translate("주변에 충분히 밝은 블록이 있어 %s와(과) %s이(가) 적용됩니다",
                      ComponentUtil.translate(TranslatableKeyParser.getKey(PotionEffectType.SPEED), NamedTextColor.GREEN), ComponentUtil.translate(TranslatableKeyParser.getKey(PotionEffectType.REGENERATION), NamedTextColor.GREEN));
              case "WA_SANS" -> ComponentUtil.translate("스켈레톤 유형의 개체에게 받는")
                      .append(Component.text("\n"))
                      .append(ComponentUtil.translate("피해량이 감소하고, 대미지가 증가합니다"));
              case "HEALTH_INCREASE" -> ComponentUtil.translate("최대 HP가 증가합니다");
              case "CONTINUAL_SPECTATING" -> ComponentUtil.translate("플레이어를 지속적으로 관전합니다")
                      .append(Component.text("\n"))
                      .append(ComponentUtil.translate("해당 플레이어가 재접속하거나 리스폰해도 자동으로 관전합니다"));
              case "TRUE_INVISIBILITY" -> ComponentUtil.translate("말 그대로 완전히 다른 플레이어로부터 보이지 않습니다");
              case "SPREAD" -> ComponentUtil.translate("주변의 다른 플레이어에게 자신이 가장 최근에 획득한 전이 효과를 옮깁니다")
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
                      .append(ComponentUtil.translate("추가로, 한번에 경험치 100 이상을 획득 시 일정 확률로 즉사합니다"));
              case "VAR_PNEUMONIA" -> ComponentUtil.translate("물 속에서 산소 소모 속도가 증가하고")
                      .append(Component.text("\n"))
                      .append(ComponentUtil.translate("물 밖으로 나와도 산소가 회복되지 않습니다"));
              case "VAR_DETOXICATE" ->
                      ComponentUtil.translate("%s, %s, %s, %s 상태 효과를 가지고 있을 경우", PotionEffectType.POISON, PotionEffectType.CONFUSION, PotionEffectType.BLINDNESS, PotionEffectType.UNLUCK)
                              .append(Component.text("\n"))
                              .append(ComponentUtil.translate("해당 상태 효과의 농도 레벨을 1단계 낮추거나 제거합니다"))
                              .append(Component.text("\n"))
                              .append(ComponentUtil.translate("일정 확률로 농도 레벨이 2단계가 낮아지거나 3단계가 낮아질"))
                              .append(Component.text("\n"))
                              .append(ComponentUtil.translate("수 있으며, 일정 확률로 표시된 모든 디버프 4개가 제거될 수 있습니다"));
              case "VAR_PODAGRA" -> ComponentUtil.translate("이동하거나 점프할 때 웅크리지 않은 상태라면")
                      .append(Component.text("\n"))
                      .append(ComponentUtil.translate("행동을 시작할 때 1의 피해를 입고 행동을 지속할 시"))
                      .append(Component.text("\n"))
                      .append(ComponentUtil.translate("3초마다 0.2의 피해를 입습니다. 추가로 3.5블록 미만의 높이에서 낙하할"))
                      .append(Component.text("\n"))
                      .append(ComponentUtil.translate("경우 1의 피해를 입고 그 이상의 높이에서 낙하할 경우 낙하 대미지가 50% 증가합니다"));
              case "NO_ENTITY_AGGRO" -> ComponentUtil.translate("중립적이거나 적대적 몹이 적대적이지 않게 됩니다");
              case "COMBAT_MODE_MELEE" -> ComponentUtil.translate("근거리 전투에 대한 집중력이 비약적으로 상승하여")
                      .append(Component.text("\n"))
                      .append(ComponentUtil.translate("상대방에게 입히는 근거리 대미지가 100% 증가하는 대신"))
                      .append(Component.text("\n"))
                      .append(ComponentUtil.translate("상대방에게서 받는 원거리 피해량이 100% 증가합니다"))
                      .append(Component.text("\n"))
                      .append(ComponentUtil.translate("이 효과는 %s 효과와 동시에 적용되지 않습니다", ComponentUtil.translate("&a" + CustomEffectType.COMBAT_MODE_RANGED.translationKey())));
              case "COMBAT_MODE_RANGED" -> ComponentUtil.translate("원거리 전투에 대한 집중력이 비약적으로 상승하여")
                      .append(Component.text("\n"))
                      .append(ComponentUtil.translate("상대방에게 입히는 원거리 대미지가 100% 증가하는 대신"))
                      .append(Component.text("\n"))
                      .append(ComponentUtil.translate("상대방에게서 받는 근거리 피해량이 100% 증가합니다"))
                      .append(Component.text("\n"))
                      .append(ComponentUtil.translate("이 효과는 %s 효과와 동시에 적용되지 않습니다", ComponentUtil.translate("&a" + CustomEffectType.COMBAT_MODE_RANGED.translationKey())));
              case "BLESS_OF_VILLAGER" -> ComponentUtil.translate("대미지가 10% 증가합니다");
              case "BREAD_KIMOCHI", "BREAD_KIMOCHI_SECONDARY_EFFECT" -> ComponentUtil.translate("이동 속도가 10% 증가하고 방어력이 2 증가합니다");
              case "COMBAT_MODE_MELEE_COOLDOWN", "COMBAT_MODE_RANGED_COOLDOWN" -> ComponentUtil.translate("전투 모드를 변경할 수 없는 상태입니다");
              case "ENDER_SLAYER" -> ComponentUtil.translate("%s, %s 또는 %s 공격 시 대미지가 증가합니다", EntityType.ENDERMAN, EntityType.ENDERMITE, EntityType.ENDER_DRAGON);
              case "BOSS_SLAYER" -> ComponentUtil.translate("보스 몬스터 공격 시 대미지가 증가합니다");
              case "TOWN_SHIELD" -> ComponentUtil.translate("평화로운 마을입니다! %s 효과가 적용되며", ComponentUtil.translate("&a" + TranslatableKeyParser.getKey(PotionEffectType.SATURATION)))
                      .append(Component.text("\n"))
                      .append(ComponentUtil.translate("이동 속도가 30% 증가하고 받는 피해량이 50% 감소합니다"));
              case "CONTINUAL_SPECTATING_EXEMPT" -> ComponentUtil.translate("지속시간 동안은 강제 관전이 되지 않습니다");
              case "EXPERIENCE_BOOST" -> ComponentUtil.translate("경험치 획득량이 증가합니다");
              case "NO_BUFF_REMOVE" -> ComponentUtil.translate("버프를 해제할 수 없는 상태입니다");
              case "NO_REGENERATION" -> ComponentUtil.translate("HP가 재생되지 않습니다");
              case "FREEZING" -> ComponentUtil.translate("춥다.. 매우.. 춥다..");
              case "COMBAT_BOOSTER" -> ComponentUtil.translate("공격 속도가 25% 증가합니다");
              case "PVP_MODE" -> ComponentUtil.translate("%s 효과가 있어야 PvP를 할 수 있습니다", ComponentUtil.translate("&a" + CustomEffectType.PVP_MODE_ENABLED.translationKey()));
              case "PVP_MODE_ENABLED" -> ComponentUtil.translate("이 효과를 동시에 가지고 있는")
                      .append(Component.text("\n"))
                      .append(ComponentUtil.translate("플레이어와 PvP할 수 있습니다"));
              case "POSITION_MEMORIZE" -> ComponentUtil.translate("현재 위치를 기억합니다")
                      .append(Component.text("\n"))
                      .append(ComponentUtil.translate("버프를 좌클릭하여 즉시 이동할 수 있습니다"));
              case "DORMAMMU" -> ComponentUtil.translate("잠시 후, 원래 있던 곳으로 강제 이동됩니다");
              case "COMBO" -> ComponentUtil.translate("적을 처치하면 %s 효과가 중첩됩니다", CustomEffectType.COMBO_STACK)
                      .append(Component.text("\n"))
                      .append(ComponentUtil.translate("10콤보가 쌓일 때마다 현재 콤보의 수치만큼"))
                      .append(Component.text("\n"))
                      .append(ComponentUtil.translate("을 지니고 있는 콤보 구슬이 생성됩니다"))
                      .append(Component.text("\n"))
                      .append(ComponentUtil.translate("콤보 스택의 지속 시간은 10초이며 지속 시간 이내에"))
                      .append(Component.text("\n"))
                      .append(ComponentUtil.translate("중첩을 갱신하지 못하면 스택이 사라집니다"))
                      .append(Component.text("\n"))
                      .append(ComponentUtil.translate("rg255,204;또한, 스택이 중첩될 수록 최대 지속 시간이 감소합니다"));
              case "MINECRAFT_SPEED" -> VanillaEffectDescription.getDescription(PotionEffectType.SPEED);
              case "MINECRAFT_SLOWNESS" -> VanillaEffectDescription.getDescription(PotionEffectType.SLOW);
              case "MINECRAFT_HASTE" -> VanillaEffectDescription.getDescription(PotionEffectType.FAST_DIGGING, viewer);
              case "MINECRAFT_MINING_FATIGUE" -> VanillaEffectDescription.getDescription(PotionEffectType.SLOW_DIGGING, viewer);
              case "MINECRAFT_STRENGTH" -> VanillaEffectDescription.getDescription(PotionEffectType.INCREASE_DAMAGE);
              case "MINECRAFT_WEAKNESS" -> VanillaEffectDescription.getDescription(PotionEffectType.WEAKNESS);
              case "MINECRAFT_INSTANT_DAMAGE" -> VanillaEffectDescription.getDescription(PotionEffectType.HARM);
              case "MINECRAFT_INSTANT_HEALTH" -> VanillaEffectDescription.getDescription(PotionEffectType.HEAL);
              case "MINECRAFT_JUMP_BOOST" -> VanillaEffectDescription.getDescription(PotionEffectType.JUMP);
              case "MINECRAFT_NAUSEA" -> VanillaEffectDescription.getDescription(PotionEffectType.CONFUSION);
              case "MINECRAFT_REGENERATION" -> VanillaEffectDescription.getDescription(PotionEffectType.REGENERATION);
              case "MINECRAFT_RESISTANCE" -> VanillaEffectDescription.getDescription(PotionEffectType.DAMAGE_RESISTANCE);
              case "MINECRAFT_FIRE_RESISTANCE" -> VanillaEffectDescription.getDescription(PotionEffectType.FIRE_RESISTANCE);
              case "MINECRAFT_WATER_BREATHING" -> VanillaEffectDescription.getDescription(PotionEffectType.WATER_BREATHING);
              case "MINECRAFT_BLINDNESS" -> VanillaEffectDescription.getDescription(PotionEffectType.BLINDNESS);
              case "MINECRAFT_INVISIBILITY" -> VanillaEffectDescription.getDescription(PotionEffectType.INVISIBILITY);
              case "MINECRAFT_NIGHT_VISION" -> VanillaEffectDescription.getDescription(PotionEffectType.NIGHT_VISION);
              case "MINECRAFT_HUNGER" -> VanillaEffectDescription.getDescription(PotionEffectType.HUNGER);
              case "MINECRAFT_POISON" -> VanillaEffectDescription.getDescription(PotionEffectType.POISON);
              case "MINECRAFT_WITHER" -> VanillaEffectDescription.getDescription(PotionEffectType.WITHER);
              case "MINECRAFT_HEALTH_BOOST" -> VanillaEffectDescription.getDescription(PotionEffectType.HEALTH_BOOST);
              case "MINECRAFT_ABSORPTION" -> VanillaEffectDescription.getDescription(PotionEffectType.ABSORPTION);
              case "MINECRAFT_SATURATION" -> VanillaEffectDescription.getDescription(PotionEffectType.SATURATION);
              case "MINECRAFT_LEVITATION" -> VanillaEffectDescription.getDescription(PotionEffectType.LEVITATION);
              case "MINECRAFT_SLOW_FALLING" -> VanillaEffectDescription.getDescription(PotionEffectType.SLOW_FALLING);
              case "MINECRAFT_GLOWING" -> VanillaEffectDescription.getDescription(PotionEffectType.GLOWING);
              case "MINECRAFT_LUCK" -> VanillaEffectDescription.getDescription(PotionEffectType.LUCK);
              case "MINECRAFT_UNLUCK" -> VanillaEffectDescription.getDescription(PotionEffectType.UNLUCK);
              case "MINECRAFT_CONDUIT_POWER" -> VanillaEffectDescription.getDescription(PotionEffectType.CONDUIT_POWER);
              case "MINECRAFT_DOLPHINS_GRACE" -> VanillaEffectDescription.getDescription(PotionEffectType.DOLPHINS_GRACE);
              case "MINECRAFT_BAD_OMEN" -> VanillaEffectDescription.getDescription(PotionEffectType.BAD_OMEN);
              case "MINECRAFT_HERO_OF_THE_VILLAGE" -> VanillaEffectDescription.getDescription(PotionEffectType.HERO_OF_THE_VILLAGE);
              case "DARKNESS" -> VanillaEffectDescription.getDescription(PotionEffectType.DARKNESS);
              default -> Component.empty();
            };
  }

  /**
   * @return 해당 효과의 아이콘이 있으면 아이콘을 반환하고 없으면 <code>null</code>을 반환합니다.
   */
  @Nullable
  public ItemStack getIcon()
  {
    if (this.icon != null)
    {
      return this.icon.clone();
    }
    if (this.namespacedKey.namespace().equals("minecraft"))
    {
      ItemStack itemStack = new ItemStack(Material.POTION);
      PotionMeta potionMeta = (PotionMeta) itemStack.getItemMeta();
      PotionEffectType effectType = PotionEffectType.getByKey(namespacedKey);
      if (effectType != null)
      {
        potionMeta.setColor(effectType.getColor());
      }
      itemStack.setItemMeta(potionMeta);
      return itemStack;
    }
    ItemStack itemStack = new ItemStack(Material.STONE);
    ItemMeta itemMeta = itemStack.getItemMeta();
    switch (this.getIdString().toUpperCase())
    {
      case "AWKWARD", "CHEESE_EXPERIMENT", "VAR_PNEUMONIA" -> itemStack = new ItemStack(Material.PUFFERFISH);
      case "BANE_OF_ARTHROPODS", "DARKNESS_TERROR_RESISTANCE" -> itemStack = new ItemStack(Material.GLOW_INK_SAC);
      case "BLESS_OF_SANS", "WA_SANS" -> itemStack = new ItemStack(Material.BONE);
      case "BLESS_OF_VILLAGER", "TOWN_SHIELD" -> itemStack = new ItemStack(Material.EMERALD);
      case "BOSS_SLAYER", "COMBAT_MODE_MELEE", "COMBAT_MODE_MELEE_COOLDOWN", "PVP_MODE", "PVP_MODE_ENABLED", "PVP_MODE_COOLDOWN", "SHARPNESS" -> itemStack = new ItemStack(Material.IRON_SWORD);
      case "BREAD_KIMOCHI" -> itemStack = new ItemStack(Material.BREAD);
      case "BUFF_FREEZE" -> itemStack = new ItemStack(Material.RABBIT_FOOT);
      case "COMBAT_BOOSTER" -> itemStack = new ItemStack(Material.SUGAR);
      case "COMBAT_MODE_RANGED", "COMBAT_MODE_RANGED_COOLDOWN", "IDIOT_SHOOTER" -> itemStack = new ItemStack(Material.BOW);
      case "CONFUSION", "TELEKINESIS", "CONTINUAL_SPECTATING_EXEMPT" -> itemStack = new ItemStack(Material.ENDER_PEARL);
      case "CONTINUAL_SPECTATING", "ENDER_SLAYER" -> itemStack = new ItemStack(Material.ENDER_EYE);
      case "COOLDOWN_CHAT", "COOLDOWN_ITEM_MEGAPHONE" -> itemStack = new ItemStack(Material.CLOCK);
      case "CURSE_OF_BEANS" -> itemStack = new ItemStack(Material.COCOA_BEANS);
      case "CURSE_OF_CONSUMPTION", "VAR_STOMACHACHE" -> itemStack = new ItemStack(Material.POISONOUS_POTATO);
      case "CURSE_OF_CREATIVITY" -> itemStack = new ItemStack(Material.WEATHERED_COPPER);
      case "CURSE_OF_CREATIVITY_BREAK" -> itemStack = new ItemStack(Material.GOLDEN_PICKAXE);
      case "CURSE_OF_CREATIVITY_PLACE" -> itemStack = new ItemStack(Material.STRUCTURE_VOID);
      case "CURSE_OF_DROP" -> itemStack = new ItemStack(Material.TURTLE_EGG);
      case "CURSE_OF_INVENTORY" -> itemStack = new ItemStack(Material.HOPPER);
      case "CURSE_OF_JUMPING" -> itemStack = new ItemStack(Material.LODESTONE);
      case "CURSE_OF_MUSHROOM" -> itemStack = new ItemStack(Material.RED_MUSHROOM);
      case "CURSE_OF_PICKUP" -> itemStack = new ItemStack(Material.COMPOSTER);
      case "DARKNESS_TERROR", "DARKNESS_TERROR_ACTIVATED" -> itemStack = new ItemStack(Material.INK_SAC);
      case "DEBUG_WATCHER" -> itemStack = new ItemStack(Material.COMMAND_BLOCK);
      case "DO_NOT_PICKUP_BUT_THROW_IT" -> itemStack = new ItemStack(Material.POPPED_CHORUS_FRUIT);
      case "DODGE" -> itemStack = new ItemStack(Material.PHANTOM_MEMBRANE);
      case "DYNAMIC_LIGHT" -> itemStack = new ItemStack(Material.TORCH);
      case "ELYTRA_BOOSTER" -> itemStack = new ItemStack(Material.FIREWORK_ROCKET);
      case "EXPERIENCE_BOOST" -> itemStack = new ItemStack(Material.EXPERIENCE_BOTTLE);
      case "FANCY_SPOTLIGHT", "FANCY_SPOTLIGHT_ACTIVATED" -> itemStack = new ItemStack(Material.SEA_LANTERN);
      case "FEATHER_FALLING" -> itemStack = new ItemStack(Material.FEATHER);
      case "FREEZING" -> itemStack = new ItemStack(Material.POWDER_SNOW_BUCKET);
      case "FROST_WALKER" ->
      {
        itemStack = new ItemStack(Material.DIAMOND_BOOTS);
        if (CustomEnchant.isEnabled())
        {
          itemMeta.addEnchant(CustomEnchant.GLOW, 1, true);
        }
      }
      case "GAESANS" -> itemStack = new ItemStack(Material.POTION);
      case "HEALTH_INCREASE" -> itemStack = new ItemStack(Material.RED_DYE);
      case "HEROS_ECHO", "HEROS_ECHO_OTHERS" -> itemStack = new ItemStack(Material.BEACON);
      case "INSIDER" -> itemStack = new ItemStack(Material.CAKE);
      case "INVINCIBLE", "INVINCIBLE_PLUGIN_RELOAD", "INVINCIBLE_RESPAWN", "RESURRECTION", "RESURRECTION_COOLDOWN", "RESURRECTION_INVINCIBLE" -> itemStack = new ItemStack(Material.TOTEM_OF_UNDYING);
      case "KEEP_INVENTORY", "ADVANCED_KEEP_INVENTORY" -> itemStack = new ItemStack(Material.BARREL);
      case "KINETIC_RESISTANCE" -> itemStack = new ItemStack(Material.SPONGE);
      case "KNOCKBACK_RESISTANCE", "KNOCKBACK_RESISTANCE_COMBAT", "KNOCKBACK_RESISTANCE_NON_COMBAT", "LEVITATION_RESISTANCE" -> itemStack = new ItemStack(Material.SHIELD);
      case "MUNDANE" -> itemStack = new ItemStack(Material.STONE);
      case "MUTE", "NO_BUFF_REMOVE", "STOP" -> itemStack = new ItemStack(Material.BARRIER);
      case "NEWBIE_SHIELD" -> itemStack = new ItemStack(Material.CHAINMAIL_CHESTPLATE);
      case "NOTHING", "TRUE_INVISIBILITY" -> itemStack = new ItemStack(Material.LIGHT_GRAY_STAINED_GLASS_PANE);
      case "NO_ENTITY_AGGRO" -> itemStack = new ItemStack(Material.BROWN_DYE);
      case "NO_REGENERATION" -> itemStack = new ItemStack(Material.REDSTONE);
      case "OUTSIDER" -> itemStack = new ItemStack(Material.DEAD_TUBE_CORAL_FAN);
      case "PARROTS_CHEER" -> itemStack = new ItemStack(Material.PARROT_SPAWN_EGG);
      case "SERVER_RADIO_LISTENING" -> itemStack = new ItemStack(Material.JUKEBOX);
      case "SILK_TOUCH" -> itemStack = new ItemStack(Material.SHEARS);
      case "SMELTING_TOUCH" -> itemStack = new ItemStack(Material.LAVA_BUCKET);
      case "SMITE" -> itemStack = new ItemStack(Material.LIGHTNING_ROD);
      case "SPREAD" -> itemStack = new ItemStack(Material.WITHER_ROSE);
      case "THICK" -> itemStack = new ItemStack(Material.NETHERITE_BLOCK);
      case "TROLL_INVENTORY_PROPERTY", "TROLL_INVENTORY_PROPERTY_MIN" -> itemStack = new ItemStack(Material.REPEATING_COMMAND_BLOCK);
      case "UNCRAFTABLE" -> itemStack = new ItemStack(Material.CRAFTING_TABLE);
      case "VAR_DETOXICATE", "VAR_DETOXICATE_ACTIVATED" -> itemStack = new ItemStack(Material.MILK_BUCKET);
      case "VAR_PODAGRA", "VAR_PODAGRA_ACTIVATED" -> itemStack = new ItemStack(Material.GRINDSTONE);
      case "CUCUMBERY_UPDATER", "BREAD_KIMOCHI_SECONDARY_EFFECT", "MINECRAFT_WITHER", "MINECRAFT_WEAKNESS", "MINECRAFT_WATER_BREATHING", "MINECRAFT_UNLUCK", "MINECRAFT_STRENGTH", "MINECRAFT_SPEED", "MINECRAFT_SLOW_FALLING",
              "MINECRAFT_ABSORPTION", "MINECRAFT_BAD_OMEN", "MINECRAFT_BLINDNESS", "MINECRAFT_CONDUIT_POWER", "MINECRAFT_DOLPHINS_GRACE", "MINECRAFT_FIRE_RESISTANCE", "MINECRAFT_GLOWING", "MINECRAFT_HASTE", "MINECRAFT_HEALTH_BOOST",
              "MINECRAFT_HERO_OF_THE_VILLAGE", "MINECRAFT_HUNGER", "MINECRAFT_INSTANT_DAMAGE", "MINECRAFT_INSTANT_HEALTH", "MINECRAFT_INVISIBILITY", "MINECRAFT_JUMP_BOOST", "MINECRAFT_LEVITATION", "MINECRAFT_LUCK", "MINECRAFT_MINING_FATIGUE",
              "MINECRAFT_NAUSEA", "MINECRAFT_NIGHT_VISION", "MINECRAFT_POISON", "MINECRAFT_REGENERATION", "MINECRAFT_RESISTANCE", "MINECRAFT_SATURATION", "MINECRAFT_SLOWNESS", "DISAPPEAR", "DAMAGE_INDICATOR" ->
      {
        return null;
      }
      case "RUNE_COOLDOWN" -> itemStack = new ItemStack(Material.TURTLE_EGG);
      case "RUNE_EXPERIENCE" -> itemStack = new ItemStack(Material.DIAMOND);
    }
    if (itemStack.getType() == Material.STONE && this != MUNDANE)
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
  public int getId()
  {
    if (this.namespacedKey.getNamespace().equals("minecraft"))
    {
      PotionEffectType effectType = PotionEffectType.getByKey(namespacedKey);
      if (effectType != null)
      {
        return 15200 + effectType.getId();
      }
    }
    // 가장 높은 버프 숫자 : 92 = RUNE_EXPERIENCE;
    return 5200 + switch (this.id)
            {
              case "awkward" -> 1;
              case "bane_of_arthropods" -> 2;
              case "bless_of_sans" -> 54;
              case "bless_of_villager" -> 79;
              case "boss_slayer" -> 76;
              case "bread_kimochi" -> 78;
              case "buff_freeze" -> 3;
              case "cheese_experiment" -> 4;
              case "combat_booster" -> 86;
              case "combat_mode_melee" -> 72;
              case "combat_mode_melee_cooldown" -> 73;
              case "combat_mode_ranged" -> 74;
              case "combat_mode_ranged_cooldown" -> 75;
              case "confusion" -> 5;
              case "continual_spectating" -> 6;
              case "continual_spectating_exempt" -> 81;
              case "cooldown_chat" -> 7;
              case "cooldown_item_megaphone" -> 8;
              case "curse_of_beans" -> 9;
              case "curse_of_consumption" -> 10;
              case "curse_of_creativity" -> 11;
              case "curse_of_creativity_break" -> 12;
              case "curse_of_creativity_place" -> 13;
              case "curse_of_drop" -> 14;
              case "curse_of_inventory" -> 15;
              case "curse_of_jumping" -> 16;
              case "curse_of_mushroom" -> 17;
              case "curse_of_pickup" -> 18;
              case "darkness_terror" -> 19;
              case "darkness_terror_activated" -> 20;
              case "darkness_terror_resistance" -> 21;
              case "debug_watcher" -> 22;
              case "do_not_pickup_but_throw_it" -> 23;
              case "dodge" -> 24;
              case "elytra_booster" -> 25;
              case "experience_boost" -> 82;
              case "ender_slayer" -> 77;
              case "fancy_spotlight" -> 26;
              case "fancy_spotlight_activated" -> 27;
              case "feather_falling" -> 28;
              case "freezing" -> 85;
              case "frost_walker" -> 29;
              case "gaesans" -> 90;
              case "health_increase" -> 30;
              case "heros_echo" -> 31;
              case "heros_echo_others" -> 32;
              case "idiot_shooter" -> 33;
              case "insider" -> 34;
              case "invincible" -> 35;
              case "invincible_plugin_reload" -> 36;
              case "invincible_respawn" -> 37;
              case "keep_inventory" -> 38;
              case "kinetic_resistance" -> 39;
              case "knockback_resistance" -> 40;
              case "knockback_resistance_combat" -> 41;
              case "knockback_resistance_non_combat" -> 42;
              case "levitation_resistance" -> 43;
              case "mundane" -> 44;
              case "mute" -> 45;
              case "newbie_shield" -> 46;
              case "no_buff_remove" -> 83;
              case "no_entity_aggro" -> 47;
              case "no_regeneration" -> 84;
              case "nothing" -> 48;
              case "outsider" -> 49;
              case "parrots_cheer" -> 50;
              case "pvp_mode" -> 87;
              case "pvp_mode_enabled" -> 88;
              case "pvp_mode_cooldown" -> 89;
              case "resurrection" -> 51;
              case "resurrection_cooldown" -> 52;
              case "resurrection_invincible" -> 53;
              case "rune_cooldown" -> 91;
              case "rune_experience" -> 92;  ///////////////////////////////// <--[here]
              case "server_radio_listening" -> 56;
              case "sharpness" -> 57;
              case "silk_touch" -> 58;
              case "smelting_touch" -> 59;
              case "smite" -> 60;
              case "spread" -> 61;
              case "stop" -> 62;
              case "telekinesis" -> 63;
              case "thick" -> 64;
              case "town_shield" -> 80;
              case "troll_inventory_property" -> 65;
              case "true_invisibility" -> 66;
              case "uncraftable" -> 67;
              case "var_detoxicate" -> 68;
              case "var_pneumonia" -> 69;
              case "var_podagra" -> 70;
              case "var_stomachache" -> 71;
              case "wa_sans" -> 55;
              default -> 0;
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
    return switch (this.id)
            {
              case "sharpness" -> Arrays.asList(SMITE, BANE_OF_ARTHROPODS);
              case "smite" -> Arrays.asList(SHARPNESS, BANE_OF_ARTHROPODS);
              case "bane_of_arthropods" -> Arrays.asList(SHARPNESS, SMITE);
              case "insider" -> Collections.singletonList(OUTSIDER);
              case "outsider" -> Collections.singletonList(INSIDER);
              case "heros_echo" -> Collections.singletonList(HEROS_ECHO_OTHERS);
              case "heros_echo_others" -> Collections.singletonList(HEROS_ECHO);
              case "combat_mode_melee" -> Collections.singletonList(COMBAT_MODE_RANGED);
              case "combat_mode_ranged" -> Collections.singletonList(COMBAT_MODE_MELEE);
              case "buff_freeze" -> Collections.singletonList(BUFF_FREEZE_D);
              case "buff_freeze_d" -> Collections.singletonList(BUFF_FREEZE);
              default -> Collections.emptyList();
            };
  }

  @Override
  public boolean equals(Object o)
  {
    return this == o || o instanceof CustomEffectType effectType && this.namespacedKey.equals(effectType.namespacedKey);
  }

  @NotNull
  public NamespacedKey getNamespacedKey()
  {
    return namespacedKey;
  }

  /**
   * Gets the {@link CustomEffectType}'s unique id.
   *
   * @return the id
   */
  @NotNull
  public String getIdString()
  {
    return id;
  }

  /**
   * Gets the translation key.
   *
   * @return the translation key
   * @since 4.8.0
   */
  @Override
  public @NotNull String translationKey()
  {
    return translationKey;
  }

  /**
   * Gets the shortened translation key or normal {@link CustomEffectType#translationKey()} if not defined.
   *
   * @return the translation key
   */
  @NotNull
  public String getShortenTranslationKey()
  {
    if (shortenTranslationKey == null)
    {
      return translationKey;
    }
    return shortenTranslationKey;
  }

  /**
   * @return true if this {@link CustomEffect} is keep on {@link  PlayerDeathEvent#getPlayer()} death and the {@link Player} has {@link CustomEffectType#BUFF_FREEZE} effect.
   */
  public boolean isBuffFreezable()
  {
    return isBuffFreezable;
  }

  /**
   * @return true if the {@link CustomEffect} is keep on {@link PlayerDeathEvent#getPlayer()} death
   */
  public boolean isKeepOnDeath()
  {
    return isKeepOnDeath;
  }

  /**
   * @return true if the {@link CustomEffect} is keep on {@link PlayerItemConsumeEvent#getPlayer()} use {@link Material#MILK_BUCKET}
   */
  public boolean isKeepOnMilk()
  {
    return isKeepOnMilk;
  }

  /**
   * @return true if the {@link CustomEffect} is keep on {@link PlayerQuitEvent#getPlayer()} quit.
   */
  public boolean isKeepOnQuit()
  {
    return isKeepOnQuit;
  }

  /**
   * @return true if the {@link CustomEffect#getDuration()} still decreases even the {@link Player} is offline
   */
  public boolean isRealDuration()
  {
    return isRealDuration;
  }

  /**
   * @return true if {@link CustomEffectType#isNegative()} is false and gui right click removeable
   */
  public boolean isRemoveable()
  {
    return !isNegative && isRemoveable;
  }

  /**
   * @return true if {@link CustomEffectType} is negative
   */
  public boolean isNegative()
  {
    return isNegative;
  }

  public int getMaxAmplifier()
  {
    return maxAmplifier;
  }

  public int getDefaultDuration()
  {
    return defaultDuration;
  }

  public int getCustomModelData()
  {
    return customModelData;
  }

  public boolean isInstant()
  {
    return isInstant;
  }

  public boolean isToggle()
  {
    return isToggle;
  }
  @Override
  public boolean isHiddenEnum()
  {
    return false;
  }

  public boolean isHidden()
  {
    return isHidden;
  }

  public boolean isTimeHidden()
  {
    return isTimeHidden;
  }

  public boolean isTimeHiddenWhenFull()
  {
    return isTimeHiddenWhenFull;
  }

  public boolean isEnumHidden()
  {
    return isEnumHidden;
  }

  public boolean isStackDisplayed()
  {
    return isStackDisplayed;
  }

  public boolean doesCallEvent()
  {
    return this.callEvent;
  }

  @NotNull
  public DisplayType getDefaultDisplayType()
  {
    return defaultDisplayType;
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
      description = description.append(Component.text("\n")).append(ComponentUtil.translate("&e사망해도 효과가 사라지지 않습니다"));
    }
    if (!keepOnQuit)
    {
      description = description.append(Component.text("\n")).append(ComponentUtil.translate("&e접속을 종료하거나 서버를 이동하면 효과가 사라집니다"));
    }
    if (keepOnMilk)
    {
      description = description.append(Component.text("\n")).append(ComponentUtil.translate("&e우유를 마셔도 효과가 사라지지 않습니다"));
    }
    if (!buffFreezable)
    {
      description = description.append(Component.text("\n")).append(ComponentUtil.translate("&e%s의 영향을 받지 않습니다", ComponentUtil.translate(CustomEffectType.BUFF_FREEZE.translationKey())));
    }
    if (isRealDuration)
    {
      description = description.append(Component.text("\n")).append(
              ComponentUtil.translate("&e접속을 종료해도 시간이 흐르는 효과입니다"));
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

  @NotNull
  public String toString()
  {
    return this.getNamespacedKey().toString();
  }
}
