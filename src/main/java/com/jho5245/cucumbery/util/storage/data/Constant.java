package com.jho5245.cucumbery.util.storage.data;

import com.destroystokyo.paper.MaterialTags;
import com.jho5245.cucumbery.Cucumbery;
import com.jho5245.cucumbery.util.nbt.CucumberyTag;
import com.jho5245.cucumbery.util.storage.component.util.ComponentUtil;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TranslatableComponent;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.translation.Translatable;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.Tag;
import org.jetbrains.annotations.NotNull;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.*;

@SuppressWarnings("unused")
public class Constant
{
  // 아이템 제한 태그

  public static final String NO_CRAFT_ITEM_DISPLAYNAME = "&c[조합 불가!]";

  public static final String NO_ANVIL_ITEM_DISPLAYNAME = "&c[모루 사용 불가!]";

  public static final String NO_SMITHING_ITEM_DISPLAYNAME = "&c[대장장이 작업대 사용 불가!]";

  public static final String CONSOLE = "콘솔";

  public static final String AIR = "산소량";

  public static final String AIR_PREFIX = "§산§소§량§b";

  public static final String SEPARATOR = "&m                                                                        ";

  /**
   * {@link Constant#SEPARATOR}에 공백 문자열을 더하거나 줄여서 반환합니다.
   *
   * @param i 줄일 문자열 길이 혹은 음수를 입력하여 연장
   * @return 공백 문자열이 더해지거나 줄여진 {@link Constant#SEPARATOR}
   */
  @NotNull
  public static String separatorSubString(int i)
  {
    if (i < 0)
    {
      return SEPARATOR + " ".repeat(-i);
    }
    return SEPARATOR.substring(0, SEPARATOR.length() - i);
  }

  public static final String TAB_COMPLETER_QUOTE_ESCAPE = "tab_completer_quote_escape";
  public static final TextColor THE_COLOR = TextColor.color(255, 204, 0);
  public static final String THE_COLOR_HEX = THE_COLOR.asHexString() + ";";
  public static final String ITEM_MODIFIERS_BRACKET = "rgb235,170,41;[%s]";
  public static final Component ITEM_MODIFIERS_CHEST = ComponentUtil.translate(ITEM_MODIFIERS_BRACKET, ComponentUtil.translate("item.modifiers.chest"));
  public static final Component ITEM_MODIFIERS_FEET = ComponentUtil.translate(ITEM_MODIFIERS_BRACKET, ComponentUtil.translate("item.modifiers.feet"));
  public static final Component ITEM_MODIFIERS_HEAD = ComponentUtil.translate(ITEM_MODIFIERS_BRACKET, ComponentUtil.translate("item.modifiers.head"));
  public static final Component ITEM_MODIFIERS_LEGS = ComponentUtil.translate(ITEM_MODIFIERS_BRACKET, ComponentUtil.translate("item.modifiers.legs"));
  public static final Component ITEM_MODIFIERS_MAINHAND = ComponentUtil.translate(ITEM_MODIFIERS_BRACKET, ComponentUtil.translate("item.modifiers.mainhand"));
  public static final Component ITEM_MODIFIERS_OFFHAND = ComponentUtil.translate(ITEM_MODIFIERS_BRACKET, ComponentUtil.translate("item.modifiers.offhand"));
  public static final String TMI_LORE_NBT_TAG_COPIED = "[NBT 태그 복사됨]";
  public static final String ITEM_LORE_MATERIAL_CRAFTABLE = "#F0E68C;[조합 가능]";
  public static final String ITEM_LORE_MATERIAL_CRAFTABLE_ONLY_CRAFTING_TABLE = "#F0E68C;[제작대에서만 조합 가능]";
  public static final String ITEM_LORE_MATERIAL_CRAFTABLE_ONLY_INVENTORY = "#F0E68C;[인벤토리에서만 조합 가능]";

  // ItemLore 기능 관련 상수
  public static final String ITEM_LORE_MATERIAL_BREWABLE = "rgb255,56,159;[양조 가능]";
  public static final String ITEM_LORE_MATERIAL_SMELTABLE = "rgb255,79,48;[제련 가능]";
  public static final String ITEM_LORE_MATERIAL_SMELTABLE_COOK = "#F07447;[조리 가능]";
  public static final String ITEM_LORE_MATERIAL_COMPOSTABLE = "rgb200,151,119;퇴비 제작 확률 : %s";
  public static final String ITEM_LORE_FUEL = "rgb255,49,18;[연소 가능]";
  public static final String ITEM_LORE_STATUS_EFFECT = "rgb255,56,159;[상태 효과]";
  public static final String ITEM_LORE_CONSUMABLE = "rgb213,114,0;[섭취 가능]";
  public static final String ITEM_LORE_PLACABLE = "#32CD32;[설치 가능]";
  public static final String ITEM_LORE_ENCHANTED = "rgb224,133,255;[부여된 마법]";
  public static final String ITEM_LORE_STORED_ENCHANT = "rgb224,133,255;[부여 가능한 마법]";
  public static final String ITEM_LORE_RETURN_BOWL_AFTER_EATEN = "&7섭취 후 그릇은 반환된다";
  public static final String ITEM_LORE_GRAVITION = "&7중력의 영향을 받는다";
  public static final String ITEM_LORE_ONLY_WITH_SILKTOUCH_OR_SWEARS = "%s이 부여된 아이템이나 %s로만 직접 얻을 수 있다";
  public static final Component ITEM_LORE_ONLY_WITH_SILKTOUCH_OR_SWEARS_COMPONENT =
          ComponentUtil.translate(ITEM_LORE_ONLY_WITH_SILKTOUCH_OR_SWEARS)
                  .decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)
                  .color(TextColor.color(170, 170, 170))
                  .args(
                          ComponentUtil.translate("enchantment.minecraft.silk_touch").color(TextColor.color(255, 255, 85)),
                          ComponentUtil.translate("item.minecraft.shears").color(TextColor.color(255, 255, 85)));
  public static final String ITEM_LORE_ONLY_WITH_SWEARS = "%s로만 직접 얻을 수 있다";
  public static final Component ITEM_LORE_ONLY_WITH_SHEARS_COMPONENT =
          ComponentUtil.translate(ITEM_LORE_ONLY_WITH_SWEARS)
                  .decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)
                  .color(TextColor.color(170, 170, 170))
                  .args(
                          ComponentUtil.translate("item.minecraft.shears").color(TextColor.color(255, 255, 85)));
  public static final String ITEM_LORE_ONLY_WITH_SILKTOUCH = "%s이 부여된 아이템으로만 직접 얻을 수 있다";
  public static final Component ITEM_LORE_ONLY_WITH_SILKTOUCH_COMPONENT =
          ComponentUtil.translate(ITEM_LORE_ONLY_WITH_SILKTOUCH)
                  .decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)
                  .color(TextColor.color(170, 170, 170))
                  .args(
                          ComponentUtil.translate("enchantment.minecraft.silk_touch").color(TextColor.color(255, 255, 85)));

  // DecimalFormat
  public static final DecimalFormat rawFormat = new DecimalFormat("###0.#############");
  public static final DecimalFormat Sosu2rawFormat = new DecimalFormat("###0.##");
  public static final DecimalFormat Jeongsu = new DecimalFormat("#,##0");
  public static final DecimalFormat JeongsuFloor = new DecimalFormat("#,##0");
  public static final DecimalFormat JeongsuRawFloor = new DecimalFormat("###0");
  public static final DecimalFormat Sosu1 = new DecimalFormat("#,##0.#");
  public static final DecimalFormat Sosu1Force = new DecimalFormat("#,##0.0");
  public static final DecimalFormat Sosu1ForceFloor = new DecimalFormat("#,##0.0");
  public static final DecimalFormat Sosu2 = new DecimalFormat("#,##0.##");
  public static final DecimalFormat Sosu2Floor = new DecimalFormat("#,##0.##");
  public static final DecimalFormat Sosu2Force = new DecimalFormat("#,##0.00");
  public static final DecimalFormat Sosu3 = new DecimalFormat("#,##0.###");
  public static final DecimalFormat Sosu3Force = new DecimalFormat("#,##0.000");
  public static final DecimalFormat Sosu4 = new DecimalFormat("#,##0.####");
  public static final DecimalFormat Sosu15 = new DecimalFormat("#,##0.###############");
  public static final String GUI_SUFFIX = "§큐§컴§버§리§G§U§I";
  public static final String GUI_NO_ITEM_LORE_FEATURE = "§아§이§템§ §설§명§ §없§음" + GUI_SUFFIX;
  public static final String CANCEL_STRING = "§클§릭§캔§슬" + GUI_SUFFIX;
  public static final TranslatableComponent GUI_PREFIX = ComponentUtil.translate("%1$s").args(Component.empty(), Component.empty(), Component.text(CANCEL_STRING));
  public static final String POTION_EFFECTS = "potion_effects";
  public static final String TRASH_CAN = "§쓰§레§기§통" + GUI_SUFFIX;
  public static final String MAIN_MENU = "§메§인§메§뉴§3[§2메뉴§3]" + GUI_SUFFIX;
  public static final String SERVER_SETTINGS = "§개§인§서§버§ §설§정§2개인 서버 설정" + GUI_SUFFIX;
  public static final String SERVER_SETTINGS_ADMIN = "§서§버§ §설§정§[§관§리§자§]§2서버 설정[관리자]" + GUI_SUFFIX;
  public static final String ITEM_DROP_MODE_MENU = "§아§이§템§ §버§리§기§ §모§드§ §설§정§3아이템 버리기 모드 설정" + GUI_SUFFIX;

  // GUI
  public static final String ITEM_PICKUP_MODE_MENU = "§아§이§템§ §줍§기§ §모§드§ §설§정§3아이템 줍기 모드 설정" + GUI_SUFFIX;
  public static final String ITEM_USE_MODE_MENU = "§아§이§템§ §사§용§ §모§드§ §설§정§3아이템 사용 모드 설정" + GUI_SUFFIX;
  public static final String CUSTOM_RECIPE_RECIPE_LIST_MENU = "§커§스§텀§레§시§피§ §메§인§ §메§뉴§8레시피 목록" + GUI_SUFFIX;
  public static final String CUSTOM_RECIPE_MENU = "§커§스§텀§레§시§피§ §카§테§고§리§ §메§뉴" + GUI_SUFFIX;
  public static final String CUSTOM_RECIPE_CRAFTING_MENU = "§커§스§텀§레§시§피§ §제§작§ §메§뉴" + GUI_SUFFIX;
  public static final String CUSTOM_RECIPE_CREATE_GUI = "§커§스§텀§레§시§피§ §생§성§ §메§뉴" + GUI_SUFFIX;
  public static final String VIRTUAL_CHEST_MENU_PREFIX = "§가§상§창§고" + GUI_SUFFIX;
  public static final String VIRTUAL_CHEST_ADMIN_MENU_PREFIX = "§가§상§창§고§관§리§자" + GUI_SUFFIX;
  public static final String ITEM_PORTABLE_SHULKER_BOX_GUI = "휴대용 셜커 상자";
  public static final String REINFORCE_QUIT = "/강화 quit";
  public static final String REINFORCE_USE_ANTI_DESTRUCTION = "/강화 파괴방지사용";
  public static final String REINFORCE_DO_NOT_USE_ANTI_DESTRUCTION = "/강화 파괴방지미사용";
  public static final String REINFORCE_USE_DISABLE_STAR_CATCH = "/강화 스타캐치해제사용";
  public static final String REINFORCE_DO_NOT_USE_DISABLE_STAR_CATCH = "/강화 스타캐치해제미사용";
  public static final String REINFORCE_START = "/강화 realstart";
  public static final String REINFORCE_STAR_CATCH = "/강화 starcatch";
  public static final String DROP_UNTRADABLE_ITEM = "/cucumbery-drop-untradable-item";
  public static final Set<Material> SWORDS = new HashSet<>(
          Arrays.asList(Material.DIAMOND_SWORD, Material.GOLDEN_SWORD, Material.IRON_SWORD, Material.STONE_SWORD, Material.WOODEN_SWORD, Material.NETHERITE_SWORD));
  public static final Set<Material> AXES = new HashSet<>(Arrays.asList(Material.DIAMOND_AXE, Material.GOLDEN_AXE, Material.IRON_AXE, Material.STONE_AXE, Material.WOODEN_AXE, Material.NETHERITE_AXE));
  public static final Set<Material> PICKAXES = new HashSet<>(
          Arrays.asList(Material.DIAMOND_PICKAXE, Material.GOLDEN_PICKAXE, Material.IRON_PICKAXE, Material.STONE_PICKAXE, Material.WOODEN_PICKAXE, Material.NETHERITE_PICKAXE));
  public static final Set<Material> SHOVELS = new HashSet<>(
          Arrays.asList(Material.DIAMOND_SHOVEL, Material.GOLDEN_SHOVEL, Material.IRON_SHOVEL, Material.STONE_SHOVEL, Material.WOODEN_SHOVEL, Material.NETHERITE_SHOVEL));

  // 강화
  public static final Set<Material> HOES = new HashSet<>(Arrays.asList(Material.DIAMOND_HOE, Material.GOLDEN_HOE, Material.IRON_HOE, Material.STONE_HOE, Material.WOODEN_HOE, Material.NETHERITE_HOE));
  public static final Set<Material> HELMETS = new HashSet<>(
          Arrays.asList(Material.CHAINMAIL_HELMET, Material.DIAMOND_HELMET, Material.GOLDEN_HELMET, Material.IRON_HELMET, Material.LEATHER_HELMET, Material.NETHERITE_HELMET, Material.TURTLE_HELMET));
  public static final Set<Material> CHESTPLATES = new HashSet<>(
          Arrays.asList(Material.CHAINMAIL_CHESTPLATE, Material.DIAMOND_CHESTPLATE, Material.GOLDEN_CHESTPLATE, Material.IRON_CHESTPLATE, Material.LEATHER_CHESTPLATE, Material.NETHERITE_CHESTPLATE));
  public static final Set<Material> LEGGINGSES = new HashSet<>(
          Arrays.asList(Material.CHAINMAIL_LEGGINGS, Material.DIAMOND_LEGGINGS, Material.GOLDEN_LEGGINGS, Material.IRON_LEGGINGS, Material.LEATHER_LEGGINGS, Material.NETHERITE_LEGGINGS));
  public static final Set<Material> BOOTSES = new HashSet<>(
          Arrays.asList(Material.CHAINMAIL_BOOTS, Material.DIAMOND_BOOTS, Material.GOLDEN_BOOTS, Material.IRON_BOOTS, Material.LEATHER_BOOTS, Material.NETHERITE_BOOTS));
  public static final Set<Material> DURABLE_ITEMS = new HashSet<>();

  // Material List
  public static final Set<Material> DEFAULT_MODIFIER_ITEMS = new HashSet<>();
  public static final Set<Material> TOOLS = new HashSet<>();
  public static final Set<Material> ARMORS = new HashSet<>();
  public static final Set<Material> INSTANTLY_BREAKABLE_BLOCKS, TOOLS_LOSE_DURABILITY_BY_BREAKING_BLOCKS;
  public static final Set<Material> CROP_BLOCKS = new HashSet<>(Arrays.asList(Material.WHEAT, Material.CARROTS, Material.POTATOES, Material.NETHER_WART, Material.BEETROOTS));
  public static final Set<Material> BUCKETS = new HashSet<>(Arrays.asList(Material.BUCKET, Material.WATER_BUCKET, Material.LAVA_BUCKET, Material.MILK_BUCKET, Material.PUFFERFISH_BUCKET,
          Material.SALMON_BUCKET, Material.COD_BUCKET, Material.TROPICAL_FISH_BUCKET, Material.AXOLOTL_BUCKET));
  public static final Set<Material> EQUIPABLE_HEADS = new HashSet<>(
          Arrays.asList(Material.CREEPER_HEAD, Material.DRAGON_HEAD, Material.PLAYER_HEAD, Material.ZOMBIE_HEAD, Material.SKELETON_SKULL, Material.WITHER_SKELETON_SKULL));
  /**
   * 6면이 완전히 꽉찬 블록
   */
  public static final Set<Material> PENETRATABLE_BLOCKS = new HashSet<>(Arrays.asList(Material.AIR, Material.CAVE_AIR, Material.VOID_AIR, Material.WATER, Material.LAVA));
  public static final Set<Material> LEAVES = new HashSet<>(
          Arrays.asList(Material.ACACIA_LEAVES, Material.BIRCH_LEAVES, Material.JUNGLE_LEAVES, Material.OAK_LEAVES, Material.SPRUCE_LEAVES, Material.DARK_OAK_LEAVES));
  public static final Set<Material> SAPLINGS = new HashSet<>(
          Arrays.asList(Material.ACACIA_SAPLING, Material.BIRCH_SAPLING, Material.JUNGLE_SAPLING, Material.OAK_SAPLING, Material.SPRUCE_SAPLING, Material.DARK_OAK_SAPLING));
  public static final Set<Material> OCCLUDING_BLOCKS = new HashSet<>();
  public static final List<Material> WOOL = new ArrayList<>(), PLANKS = new ArrayList<>(), FLOWERS = new ArrayList<>(), SMALL_FLOWERS = new ArrayList<>(), TALL_FLOWERS = new ArrayList<>(),
          WITHER_IMMUNE = new ArrayList<>(), BEACON_BASE_BLOCKS = new ArrayList<>(), DYES = new ArrayList<>(), SHULKER_BOXES = new ArrayList<>();
  public static final Set<Material> OPTIFINE_DYNAMIC_LIGHT_ITEMS = new HashSet<>(Arrays.asList(Material.TORCH, Material.REDSTONE_TORCH, Material.LAVA_BUCKET, Material.NETHER_STAR, Material.BEACON,
          Material.GLOWSTONE, Material.SEA_LANTERN, Material.OCHRE_FROGLIGHT, Material.PEARLESCENT_FROGLIGHT, Material.VERDANT_FROGLIGHT));
  private static final DecimalFormat privateSosu5Force = new DecimalFormat("00000,000.00000");
  private static final DecimalFormat privateSosu2Force = new DecimalFormat("00000,000.00");
  private static final DecimalFormat privateOnlySosu2Force = new DecimalFormat("#,##0.00");
  public static Sound WARNING_SOUND, ERROR_SOUND;
  public static double WARNING_SOUND_VOLUME, WARNING_SOUND_PITCH, ERROR_SOUND_VOLUME, ERROR_SOUND_PITCH;

  static
  {
    try
    {
      WARNING_SOUND = Sound.valueOf(Cucumbery.config.getString("sound-const.warning-sound.sound"));
    }
    catch (Exception e)
    {
      WARNING_SOUND = Sound.ENTITY_ENDERMAN_TELEPORT;
    }
    try
    {
      ERROR_SOUND = Sound.valueOf(Cucumbery.config.getString("sound-const.error-sound.sound"));
    }
    catch (Exception e)
    {
      ERROR_SOUND = Sound.BLOCK_ANVIL_LAND;
    }
    WARNING_SOUND_PITCH = Cucumbery.config.getDouble("sound-const.warning-sound.pitch");
    WARNING_SOUND_VOLUME = Cucumbery.config.getDouble("sound-const.warning-sound.volume");
    ERROR_SOUND_PITCH = Cucumbery.config.getDouble("sound-const.error-sound.pitch");
    ERROR_SOUND_VOLUME = Cucumbery.config.getDouble("sound-const.error-sound.volume");
  }

  static
  {
    Sosu1ForceFloor.setRoundingMode(RoundingMode.FLOOR);
    Sosu2Floor.setRoundingMode(RoundingMode.FLOOR);
    JeongsuFloor.setRoundingMode(RoundingMode.FLOOR);
    JeongsuRawFloor.setRoundingMode(RoundingMode.FLOOR);
  }

  static
  {
    TOOLS.addAll(AXES);
    TOOLS.addAll(PICKAXES);
    TOOLS.addAll(SHOVELS);
    TOOLS.addAll(HOES);
    ARMORS.addAll(HELMETS);
    ARMORS.addAll(CHESTPLATES);
    ARMORS.addAll(LEGGINGSES);
    ARMORS.addAll(BOOTSES);
    for (Material material : Material.values())
    {
      if (material.getMaxDurability() != 0)
      {
        DURABLE_ITEMS.add(material);
      }
      if (material.isOccluding())
      {
        OCCLUDING_BLOCKS.add(material);
      }

      if (Tag.WOOL.isTagged(material))
      {
        WOOL.add(material);
      }
      if (Tag.PLANKS.isTagged(material))
      {
        PLANKS.add(material);
      }
      if (Tag.FLOWERS.isTagged(material))
      {
        FLOWERS.add(material);
      }
      if (Tag.SMALL_FLOWERS.isTagged(material))
      {
        SMALL_FLOWERS.add(material);
      }
      if (Tag.TALL_FLOWERS.isTagged(material))
      {
        TALL_FLOWERS.add(material);
      }
      if (Tag.WITHER_IMMUNE.isTagged(material))
      {
        WITHER_IMMUNE.add(material);
      }
      if (Tag.BEACON_BASE_BLOCKS.isTagged(material))
      {
        BEACON_BASE_BLOCKS.add(material);
      }
      if (MaterialTags.DYES.isTagged(material))
      {
        DYES.add(material);
      }
      if (Tag.SHULKER_BOXES.isTagged(material))
      {
        SHULKER_BOXES.add(material);
      }
    }
    DEFAULT_MODIFIER_ITEMS.addAll(ARMORS);
    DEFAULT_MODIFIER_ITEMS.addAll(SWORDS);
    DEFAULT_MODIFIER_ITEMS.addAll(TOOLS);
    DEFAULT_MODIFIER_ITEMS.add(Material.TRIDENT);
    DEFAULT_MODIFIER_ITEMS.add(Material.TURTLE_HELMET);

    INSTANTLY_BREAKABLE_BLOCKS = new HashSet<>();
    Arrays.stream(Material.values()).forEach(material ->
    {
      if (material.isBlock() && material.getHardness() == 0)
      {
        INSTANTLY_BREAKABLE_BLOCKS.add(material);
      }
    });
    TOOLS_LOSE_DURABILITY_BY_BREAKING_BLOCKS = new HashSet<>(Arrays
            .asList(Material.SHEARS, Material.TRIDENT, Material.WOODEN_AXE, Material.WOODEN_PICKAXE, Material.WOODEN_SHOVEL, Material.WOODEN_SWORD,
                    Material.STONE_AXE, Material.STONE_PICKAXE, Material.STONE_SHOVEL, Material.STONE_SWORD, Material.IRON_AXE,
                    Material.IRON_PICKAXE, Material.IRON_SHOVEL, Material.IRON_SWORD, Material.DIAMOND_AXE, Material.DIAMOND_PICKAXE,
                    Material.DIAMOND_SHOVEL, Material.DIAMOND_SWORD, Material.GOLDEN_AXE, Material.GOLDEN_PICKAXE, Material.GOLDEN_SHOVEL,
                    Material.GOLDEN_SWORD, Material.NETHERITE_AXE, Material.NETHERITE_PICKAXE, Material.NETHERITE_SHOVEL, Material.NETHERITE_SWORD,
                    Material.DIAMOND_HOE, Material.GOLDEN_HOE, Material.IRON_HOE, Material.STONE_HOE, Material.NETHERITE_HOE, Material.WOODEN_HOE));
  }

  public static String Sosu5Force(double input)
  {
    String value = Constant.privateSosu5Force.format(input);
    return (!value.startsWith("-") ? "&a+" : "&c") + value;
  }

  public static String Sosu2Force(double input)
  {
    String value = Constant.privateSosu2Force.format(input);
    return (!value.startsWith("-") ? "&a+" : "&c") + value;
  }

  public static String Sosu2OnlyForce(double input)
  {
    String value = Constant.privateOnlySosu2Force.format(input);
    return (!value.startsWith("-") ? "&a+" : "&c") + value;
  }

  public enum RestrictionType
  {
    NO_ANVIL("모루에서 사용 불가"),
    NO_ANVIL_COMPOSITION("모루에서 같은 아이템과 합성 불가"),
    NO_ANVIL_COMPOSITION_LEFT("모루에서 같은 아이템과 합성 불가(왼쪽만)"),
    NO_ANVIL_COMPOSITION_RIGHT("모루에서 같은 아이템과 합성 불가(오른쪽만)"),
    NO_ANVIL_ENCHANT("모루에서 마법 부여 불가"),
    NO_ANVIL_ENCHANTED_BOOK_COMPOSITION("모루에서 합성 불가"),
    NO_ANVIL_ENCHANTED_BOOK_COMPOSITION_LEFT("모루에서 합성 불가(왼쪽만)"),
    NO_ANVIL_ENCHANTED_BOOK_COMPOSITION_RIGHT("모루에서 합성 불가(오른쪽만)"),
    NO_ANVIL_ENCHANTED_BOOK_ENCHANT("마법이 부여된 책으로 마법 부여 불가"),
    NO_ANVIL_RENAME("모루에서 이름 바꾸기 불가"),
    NO_ANVIL_REPAIR("모루에서 수리 불가"),
    NO_ARMOR_STAND("갑옷 거치대에 사용 불가"),
    NO_ATTACK("공격 도구로 사용 불가"),
    NO_BEACON("신호기에서 사용 불가"),
    NO_BLAST_FURNACE("용광로에서 사용 불가"),
    NO_BLOCK_BEACON("신호기로 사용 불가"),
    NO_BLOCK_BREAK("설치된 상태면 파괴 불가"),

    NO_BLOCK_BURN("불에 안탐"),
    NO_BLOCK_CLICK_INVENTORY("클릭 불가(블록)"),

    NO_BLOCK_ENTITY_CHANGE("엔티티가 못만짐"),
    NO_BLOCK_LOOM("베틀로 사용 불가(블록)"),
    NO_BLOCK_LOOT("꺼내기 불가(블록)"),
    NO_BLOCK_PISTON_MOVE("피스톤으로 밀고 당기기 불가"),
    NO_BLOCK_PISTON_PULL("피스톤으로 당기기 불가"),
    NO_BLOCK_PISTON_PUSH("피스톤으로 밀기 불가"),
    NO_BLOCK_STORE("보관 불가(블록)"),
    NO_BLOCK_TRADE("캐릭터 귀속(블록)"),
    NO_BREAK("블록 파괴 도구로 사용 불가"),
    NO_BREW("양조기에서 사용 불가"),
    NO_BUCKET("양동이로 사용 불가"),
    NO_CAMPFIRE("모닥불에 사용 불가"),
    NO_CARTOGRAPHY_TABLE("지도 제작대에서 사용 불가"),
    NO_CLICK_INVENTORY("클릭 불가"),
    NO_COMPOSTER("퇴비통에 사용 불가"),
    NO_CONSUME("섭취 불가"),
    NO_CRAFT("조합 불가"),
    NO_CRAFT_IN_CRAFTING_TABLE("제작대에서 조합 불가"),
    NO_CRAFT_IN_INVENTORY("인벤토리에서 조합 불가"),
    NO_CROSSBOW_LOAD("장전 불가"),
    NO_DISPENSER("발사기에서 사용 불가"),
    NO_DISPENSER_DISPENSE("발사기에서 발사 불가"),
    NO_DROP("버리기 불가"),
    NO_DROPPER("공급기에서 사용 불가"),
    NO_DROPPER_DISPENSE("공급기에서 발사 불가"),
    NO_ENCHANT("마법 부여 불가"),
    NO_EQUIP("장착 불가"),
    NO_FISH("낚시 불가"),
    NO_FLOWER_POT("화분에 사용 불가"),
    NO_FUEL("땔감으로 사용 불가"),
    NO_FURNACE("화로에서 사용 불가"),
    NO_GRINDSTONE("숫돌에서 사용 불가"),
    NO_HOPPER("호퍼에서 사용 불가"),
    NO_ITEM_EXISTS("떨어트리면 아이템 소실"),
    NO_ITEM_FRAME("아이템 액자에 사용 불가"),
    NO_JUKEBOX("주크박스에 사용 불가"),
    NO_LECTERN("독서대에 사용 불가"),
    NO_LECTERN_BOOK_TAKE("독서대에서 책 회수 불가"),
    NO_LECTERN_CHANGE_PAGE("독서대에서 쪽수 변경 불가"),
    NO_LODESTONE("자석석에 사용 불가"),
    NO_LOOM("베틀에서 사용 불가"),
    NO_LOOT("꺼내기 불가"),
    NO_MERGE("떨어트린 상태로 겹치기 불가"),
    NO_PICKUP("줍기 불가"),
    NO_PLACE("설치 불가"),
    NO_RESPAWN_ANCHOR("리스폰 정박기에 사용 불가"),
    NO_SHOT("발사 불가"),
    NO_SHOT_AMMO("발사 탄환으로 사용 불가"),
    NO_SMELT("제련 불가"),
    NO_SMITHING_TABLE("대장장이 작업대에서 사용 불가"),
    NO_SMOKER("훈연기에서 사용 불가"),
    NO_STONECUTTER("석재 절단기에서 사용 불가"),
    NO_STORE("보관 불가"),
    NO_TRADE("캐릭터 귀속"),
    NO_TRADE_VILLAGER("주민과 거래 불가"),
    NO_UNBINDING_SHEARS("해방의 가위 사용 불가"),
    NO_UNDYE("탈색 불가"),
    NO_USE("사용 불가"),

    NO_USE_AIR_CLICK("허공에 대고 사용 불가"),
    NO_USE_BLOCK_CLICK("블록에 대고 사용 불가"),
    NO_USE_ENTITY("개체에게 사용 불가"),
    NO_USE_LEFT_CLICK("좌클릭 사용 불가"),
    NO_USE_LEFT_CLICK_AIR("허공에 대고 좌클릭 불가"),
    NO_USE_LEFT_CLICK_BLOCK("블록에 대고 좌클릭 불가"),
    NO_USE_MAIN_HAND("주로 사용하는 손에 들고 사용 불가"),
    NO_USE_OFF_HAND("다른 손에 들고 사용 불가"),

    NO_USE_RIGHT_CLICK("우클릭 사용 불가"),
    NO_USE_RIGHT_CLICK_AIR("허공에 대고 우클릭 불가"),

    NO_USE_RIGHT_CLICK_BLOCK("블록에 대고 우클릭 불가"),
    ;

    private final String tag;

    RestrictionType(String tag)
    {
      this.tag = tag;
    }

    public String get()
    {
      return this.toString();
    }

    public String getTag()
    {
      return "[" + tag + "]";
    }

    public String getRawTag()
    {
      return tag;
    }
  }

  public enum AllPlayer implements Translatable
  {
    CHAT("채팅"),
    EXECUTE_COMMAND("명령어-실행"),
    BLOCK_PLACE("블록-설치"),
    BLOCK_BREAK("블록-파괴"),
    ITEM_DROP("아이템-버리기"),
    ITEM_PICKUP("아이템-줍기"),
    ITEM_INTERACT("아이템-사용"),
    ITEM_CONSUME("아이템-사용"),
    OPEN_CONTAINER("인벤토리-GUI-열기"),
    ITEM_HELD("단축바-슬롯-변경"),
    MOVE("움직이기"),
    ;

    private final String key;

    AllPlayer(String key)
    {
      this.key = key;
    }

    public String getKey()
    {
      return key;
    }

    public boolean isEnabled()
    {
      return Variable.allPlayerConfig.getBoolean(this.getKey());
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
      return this.key.replace("-", " ");
    }
  }

  public enum CucumberyHideFlag
  {
    ENCHANTS("마법 부여 숨김"),
    ENCHANTS_TMI("TMI 마법 부여 숨김"),
    TMI_DESCRIPTION("추가 TMI 설명 숨김"),
    ATTRIBUTE_MODIFIERS("속성 수식어 숨김"),
    DURABILITY("내구도 숨김"),
    DURABILITY_CHANCE_NOT_TO_CONSUME("내구도 감소 무효 확률 숨김"),
    ANVIL_USE_TIME("모루 사용 횟수 숨김"),
    STATUS_EFFECTS("상태 효과 숨김"),
    FIREWORK_EFFECTS("폭죽 효과 숨김"),
    FIREWORK_FLIGHT_TIME("폭죽 체공 시간 숨김"),
    COMPOSTABLE("퇴비 제작 확률 숨김"),
    PLACABLE("설치 가능 숨김"),
    CRAFTABLE("조합 가능 숨김"),
    SMELTABLE("제련 가능 숭김"),
    FUEL("연소 가능 숨김"),
    CONSUMABLE("섭취 가능 숨김"),
    BREWABLE("양조 가능 숨김"),
    BANNER_PATTERN("현수막 무늬 숨김"),
    STORAGE_CONTENTS("컨테이너 내용물 숨김"),
    BOOK_TAG("책 태그 숨김"),
    EXPIRE_DATE("유효 기간 숨김"),
    CAN_PLACE_ON("마인크래프트 이 위에 설치 가능 숨김"),
    CAN_DESTROY("마인크래프트 부술 수 있는 것 숨김"),


    COOLDOWN("대기 시간 숨김"),

    COOLDOWN_RIGHT_CLICK(ItemUsageType.RIGHT_CLICK.getDisplay() + " 대기 시간 숨김"),
    COOLDOWN_LEFT_CLICK(ItemUsageType.LEFT_CLICK.getDisplay() + " 대기 시간 숨김"),

    COOLDOWN_ATTACKS("공격 관련 대기 시간 숨김"),

    COOLDOWN_ATTACK(ItemUsageType.ATTACK.getDisplay() + " 대기 시간 숨김"),
    COOLDOWN_ATTACK_PLAYER(ItemUsageType.ATTACK_PLAYER.getDisplay() + " 대기 시간 숨김"),
    COOLDOWN_ATTACK_ENTITY(ItemUsageType.ATTACK_ENTITY.getDisplay() + " 대기 시간 숨김"),

    COOLDOWN_ATTACK_MELEE(ItemUsageType.ATTACK_MELEE.getDisplay() + " 대기 시간 숨김"),
    COOLDOWN_ATTACK_PLAYER_MELEE(ItemUsageType.ATTACK_PLAYER_MELEE.getDisplay() + " 대기 시간 숨김"),
    COOLDOWN_ATTACK_ENTITY_MELEE(ItemUsageType.ATTACK_ENTITY_MELEE.getDisplay() + " 대기 시간 숨김"),

    COOLDOWN_ATTACK_RANGED(ItemUsageType.ATTACK_RANGED.getDisplay() + " 대기 시간 숨김"),
    COOLDOWN_ATTACK_PLAYER_RANGED(ItemUsageType.ATTACK_PLAYER_RANGED.getDisplay() + " 대기 시간 숨김"),
    COOLDOWN_ATTACK_ENTITY_RANGED(ItemUsageType.ATTACK_ENTITY_RANGED.getDisplay() + " 대기 시간 숨김"),

    COOLDOWN_SNEAK_ATTACK(ItemUsageType.SNEAK_ATTACK.getDisplay() + " 대기 시간 숨김"),
    COOLDOWN_SNEAK_ATTACK_PLAYER(ItemUsageType.SNEAK_ATTACK_PLAYER.getDisplay() + " 대기 시간 숨김"),
    COOLDOWN_SNEAK_ATTACK_ENTITY(ItemUsageType.SNEAK_ATTACK_ENTITY.getDisplay() + " 대기 시간 숨김"),

    COOLDOWN_SNEAK_ATTACK_MELEE(ItemUsageType.SNEAK_ATTACK_MELEE.getDisplay() + " 대기 시간 숨김"),
    COOLDOWN_SNEAK_ATTACK_PLAYER_MELEE(ItemUsageType.SNEAK_ATTACK_PLAYER_MELEE.getDisplay() + " 대기 시간 숨김"),
    COOLDOWN_SNEAK_ATTACK_ENTITY_MELEE(ItemUsageType.SNEAK_ATTACK_ENTITY_MELEE.getDisplay() + " 대기 시간 숨김"),

    COOLDOWN_SNEAK_ATTACK_RANGED(ItemUsageType.SNEAK_ATTACK_RANGED.getDisplay() + " 대기 시간 숨김"),
    COOLDOWN_SNEAK_ATTACK_PLAYER_RANGED(ItemUsageType.SNEAK_ATTACK_PLAYER_RANGED.getDisplay() + " 대기 시간 숨김"),
    COOLDOWN_SNEAK_ATTACK_ENTITY_RANGED(ItemUsageType.SNEAK_ATTACK_ENTITY_RANGED.getDisplay() + " 대기 시간 숨김"),


    COOLDOWN_CONSUME(ItemUsageType.CONSUME.getDisplay() + " 대기 시간 숨김"),
    COOLDOWN_RESURRECT(ItemUsageType.RESURRECT.getDisplay() + " 대기 시간 숨김"),
    COOLDOWN_SNEAK(ItemUsageType.SNEAK.getDisplay() + " 대기 시간 숨김"),
    COOLDOWN_SWAP(ItemUsageType.SWAP.getDisplay() + " 대기 시간 숨김"),

    BANNER_PATTERN_ITEM("현수막 무늬의 무늬 설명 숨김"),
    CUSTOM_NAME("바닥에 떨어뜨렸을 때 네임 태그 숨김"),
    BLOCK_DATA("블록 데이터 태그 숨김"),
    BLOCK_STATE("블록 엔티티 태그 숨김"),
    RESTRICTION("사용 제한 태그 숨김"),
    MUSIC_DISC("곡 설명 숨김"),
    TOTEM_OF_UNDYING("불사의 토템 설명 숨김"),
    PLAYER_HEAD("플레이어 머리 정보 숨김"),
    DEBUG_STICK("디버그 막대기의 디버그 속성 숨김"),
    COMMAND_BLOCK("명령 블록 태그 숨김"),
    BEACON("신호기 설명 숨김"),
    JUKEBOX("주크박스 태그 숨김"),
    LECTERN("독서대의 등록된 책 정보 태그 숨김"),
    LEATHER_COLOR("가죽 관련 아이템 색상 태그 숨김"),
    LODESTONE_LOCATION("자석석 좌표 태그 숨김"),
    CUSTOM_LORE("커스텀 설명 숨김"),
    ABOVE_CUSTOM_LORE("상단 커스텀 설명 숨김"),

    ORIGINAL_DISPLAY_NAME("원래 이름 표시 설명 숨김"),

    CUSTOM_MININGS("채굴 관련 설명 숨김"),
    ;

    private final String display;

    CucumberyHideFlag(String display)
    {
      this.display = display;
    }

    public String getDisplay()
    {
      return display;
    }
  }

  public enum ExtraTag
  {
    PORTABLE_SHULKER_BOX("휴대용 셜커 상자"),
    NO_TMI_LORE("TMI 설명 사용 안함"),
    BLOCK_PLACE_BECOME_WATER("설치 시 물이 됨(네더 제외)"),
    BLOCK_PLACE_BECOME_WATER_EVEN_IN_NETHER("설치 시 물이 됨(네더 포함)"),
    BLOCK_PLACE_BECOME_LAVA("설치 시 용암이 됨"),
    CUSTOM_RECIPE_REUSABLE("커스텀 레시피에서 제작 시 사라지지 않음"),
    GLOWING("바닥에 버렸을 때 발광"),
    INFINITE("아이템 사용 시 소비되지 않음"),
    INFINITE_BEACON_ITEM("신호기에 사용 시 소비되지 않음"),
    INFINITE_FUEL("화로에서 땔감으로 사용 시 소비되지 않음"),
    NO_COOLDOWN_ENDER_PEARL("쿨타임 없는 엔더 진주"),
    PRESERVE_BLOCK_NBT("설치 시 블록의 nbt 보존"),
    PRESERVE_BLOCK_DATA_TAG("설치 시 블록의 nbt 블록 데이터 보존"),
    PRESERVE_BLOCK_ENTITY_TAG("설치 시 블록의 블록 엔티티 데이터 보존"),
    NAME_TAG("플레이어 이름 새겨짐"),

    // Invulnerable Tags

    INVULNERABLE("바닥에 버렸을 때 피해를 입지 않음"),
    INVULNERABLE_EXPLOSION("바닥에 버렸을 때 폭발에 의한 피해를 입지 않음"),
    INVULNERABLE_EXPLOSION_BLOCK("바닥에 버렸을 때 블록 폭발에 의한 피해를 입지 않음"),
    INVULNERABLE_EXPLOSION_ENTITY("바닥에 버렸을 때 개체 폭발에 의한 피해를 입지 않음"),
    INVULNERABLE_FIRES("바닥에 버렸을 때 화염에 의한 피해를 입지 않음"),
    INVULNERABLE_FIRES_LAVA("바닥에 버렸을 때 용암에 의한 피해를 입지 않음"),
    INVULNERABLE_FIRES_FIRE("바닥에 버렸을 때 불에 의한 피해를 입지 않음"),
    INVULNERABLE_FIRES_FIRE_TICK("바닥에 버렸을 때 불 블록에 의한 피해를 입지 않음"),
    INVULNERABLE_CONTACT("바닥에 버렸을 때 블록 접촉(주로 선인장)에 의한 패히를 입지 않음"),

    ;

    private final String display;

    ExtraTag(String display)
    {
      this.display = display;
    }

    public String getDisplay()
    {
      return display;
    }
  }

  public enum ItemUsageType
  {
    ATTACK("공격", CucumberyTag.USAGE_COMMANDS_ATTACK_KEY),
    ATTACK_ENTITY("PvE", CucumberyTag.USAGE_COMMANDS_ATTACK_ENTITY_KEY),

    ATTACK_ENTITY_MELEE("근접 PvE", CucumberyTag.USAGE_COMMANDS_ATTACK_ENTITY_MELEE_KEY),
    ATTACK_ENTITY_RANGED("원거리 PvE", CucumberyTag.USAGE_COMMANDS_ATTACK_ENTITY_RANGED_KEY),

    ATTACK_MELEE("근접 공격", CucumberyTag.USAGE_COMMANDS_ATTACK_MELEE_KEY),
    ATTACK_PLAYER("PvP", CucumberyTag.USAGE_COMMANDS_ATTACK_PLAYER_KEY),

    ATTACK_PLAYER_MELEE("근접 PvP", CucumberyTag.USAGE_COMMANDS_ATTACK_PLAYER_MELEE_KEY),
    ATTACK_PLAYER_RANGED("원거리 PvP", CucumberyTag.USAGE_COMMANDS_ATTACK_PLAYER_RANGED_KEY),
    ATTACK_RANGED("원거리 공격", CucumberyTag.USAGE_COMMANDS_ATTACK_RANGED_KEY),

    CONSUME("섭취", CucumberyTag.USAGE_COMMANDS_CONSUME_KEY),

    DROP("버리기", CucumberyTag.USAGE_COMMANDS_DROP_KEY),

    SNEAK_DROP("웅크리고 버리기", CucumberyTag.USAGE_COMMANDS_SNEAK_DROP_KEY),
    LEFT_CLICK("좌클릭", CucumberyTag.USAGE_COMMANDS_LEFT_CLICK_KEY),
    RESURRECT("부활", CucumberyTag.USAGE_COMMANDS_RESURRECT_KEY),

    RIGHT_CLICK("우클릭", CucumberyTag.USAGE_COMMANDS_RIGHT_CLICK_KEY),
    SNEAK("웅크리기", CucumberyTag.USAGE_COMMANDS_SNEAK_KEY),
    SNEAK_ATTACK("웅크리고 공격", CucumberyTag.USAGE_COMMANDS_SNEAK_ATTACK_KEY),

    SNEAK_ATTACK_ENTITY("웅크리고 PvE", CucumberyTag.USAGE_COMMANDS_SNEAK_ATTACK_ENTITY_KEY),
    SNEAK_ATTACK_ENTITY_MELEE("웅크리고 근접 PvE", CucumberyTag.USAGE_COMMANDS_SNEAK_ATTACK_ENTITY_MELEE_KEY),
    SNEAK_ATTACK_ENTITY_RANGED("웅크리고 원거리 PvE", CucumberyTag.USAGE_COMMANDS_SNEAK_ATTACK_ENTITY_RANGED_KEY),

    SNEAK_ATTACK_MELEE("웅크리고 근접 공격", CucumberyTag.USAGE_COMMANDS_SNEAK_ATTACK_MELEE_KEY),
    SNEAK_ATTACK_PLAYER("웅크리고 PvP", CucumberyTag.USAGE_COMMANDS_SNEAK_ATTACK_PLAYER_KEY),
    SNEAK_ATTACK_PLAYER_MELEE("웅크리고 근접 PvP", CucumberyTag.USAGE_COMMANDS_SNEAK_ATTACK_PLAYER_MELEE_KEY),

    SNEAK_ATTACK_PLAYER_RANGED("웅크리고 원거리 PvP", CucumberyTag.USAGE_COMMANDS_SNEAK_ATTACK_PLAYER_RANGED_KEY),
    SNEAK_ATTACK_RANGED("웅크리고 원거리 공격", CucumberyTag.USAGE_COMMANDS_SNEAK_ATTACK_RANGED_KEY),
    SNEAK_CONSUME("웅크리고 섭취", CucumberyTag.USAGE_COMMANDS_SNEAK_CONSUME_KEY),

    SNEAK_LEFT_CLICK("웅크리고 좌클릭", CucumberyTag.USAGE_COMMANDS_SNEAK_LEFT_CLICK_KEY),
    SNEAK_RIGHT_CLICK("웅크리고 우클릭", CucumberyTag.USAGE_COMMANDS_SNEAK_RIGHT_CLICK_KEY),

    SNEAK_SWAP("웅크리고 스와핑", CucumberyTag.USAGE_COMMANDS_SNEAK_SWAP_KEY),
    SPRINT("달리기", CucumberyTag.USAGE_COMMANDS_SPRINT_KEY),

    SWAP("스와핑", CucumberyTag.USAGE_COMMANDS_SWAP_KEY),
    ;

    private final String display;

    private final String key;

    ItemUsageType(String display, String key)
    {
      this.display = display;
      this.key = key;
    }

    public String getDisplay()
    {
      return display;
    }

    public String getKey()
    {
      return key;
    }
  }
}
