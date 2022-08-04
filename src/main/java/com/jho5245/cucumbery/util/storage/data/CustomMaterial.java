package com.jho5245.cucumbery.util.storage.data;

import com.jho5245.cucumbery.util.itemlore.ItemLore;
import com.jho5245.cucumbery.util.storage.component.util.ComponentUtil;
import com.jho5245.cucumbery.util.storage.no_groups.ItemCategory.Rarity;
import de.tr7zw.changeme.nbtapi.NBTItem;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.inventory.CreativeCategory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

/**
 * Represents custom {@link Material}.
 */
// 컬러코드 &R 은 해당 아이템의 등급의 색깔을 사용함을 뜻함
public enum CustomMaterial
{
  AAA(Material.STONE, "&iaaa"),
  ALMOND(Material.COCOA_BEANS, "&i아몬드"),
  AMAZON(Material.JUNGLE_SAPLING, "&i아마존"),
  AMBER(Material.PLAYER_HEAD, "&R호박", Rarity.UNIQUE),
  AMETHYST(Material.PLAYER_HEAD, "&R자수정 원석", Rarity.UNIQUE),
  BAD_APPLE(Material.APPLE, "&i나쁜 사과"),
  BAMIL_PABO(Material.PLAYER_HEAD, "&R머밀의 바리", Rarity.ARTIFACT),
  BANANA(Material.YELLOW_DYE, "&i바나나"),
  BOO(Material.SCUTE, "&b부우"),
  BOO_HUNGRY(Material.SCUTE, "&b배고프부우.."),
  CHERRY(Material.SWEET_BERRIES, "rgb255,200,200;체리"),
  COIN(Material.SUNFLOWER, "&e코인"),
  CORE_GEMSTONE(Material.PRISMARINE_CRYSTALS, "&b코어 젬스톤", Rarity.EPIC),
  CORE_GEMSTONE_EXPERIENCE(Material.PRISMARINE_CRYSTALS, "&a경험의 코어 젬스톤", Rarity.EPIC),
  CORE_GEMSTONE_MIRROR(Material.PRISMARINE_CRYSTALS, "&c거울세계의 코어 젬스톤", Rarity.EPIC),
  CORE_GEMSTONE_MITRA(Material.PRISMARINE_CRYSTALS, "&6미트라의 코어 젬스톤", Rarity.EPIC),
  CORN(Material.YELLOW_DYE, "&i옥수수"),
  CUCUMBER(Material.SEA_PICKLE, "&2오이"),
  CUTE_SUGAR(Material.SUGAR, "&a커여운 슦가", Rarity.EPIC, "슈가"),
  CUTE_SUGAR_HUNGRY(Material.SUGAR, "&a배고푼 커여운 슦가", Rarity.EPIC, "슈가"),
  DAGGER(Material.IRON_SWORD, "&i단검"),
  DOEHAERIM_BABO(Material.PLAYER_HEAD, "&R바보의 머리", Rarity.ARTIFACT),
  ENCHANTED_ACACIA_LOG(Material.ACACIA_LOG, "&R마법이 부여된 아카시아나무 원목", Rarity.RARE),
  ENCHANTED_BIRCH_LOG(Material.BIRCH_LOG, "&R마법이 부여된 자작나무 원목", Rarity.RARE),
  ENCHANTED_BONE(Material.BONE, "&R마법이 부여된 뼈다귀", Rarity.RARE),
  ENCHANTED_BONE_BLOCK(Material.BONE_BLOCK, "&R마법이 부여된 뼈 블록", Rarity.EPIC),
  ENCHANTED_COAL(Material.COAL, "&R마법이 부여된 석탄", Rarity.RARE),
  ENCHANTED_COAL_BLOCK(Material.COAL_BLOCK, "&R마법이 부여된 석탄 블록", Rarity.EPIC),
  ENCHANTED_BROWN_MUSHROOM(Material.BROWN_MUSHROOM, "&R마법이 부여된 갈색 버섯", Rarity.RARE),
  ENCHANTED_BROWN_MUSHROOM_BLOCK(Material.BROWN_MUSHROOM_BLOCK, "&R마법이 부여된 갈색 버섯 블록", Rarity.EPIC),
  ENCHANTED_RED_MUSHROOM(Material.RED_MUSHROOM, "&R마법이 부여된 빨간색 버섯", Rarity.RARE),
  ENCHANTED_RED_MUSHROOM_BLOCK(Material.RED_MUSHROOM_BLOCK, "&R마법이 부여된 빨간색 버섯 블록", Rarity.EPIC),
  ENCHANTED_CRIMSON_FUNGUS(Material.CRIMSON_FUNGUS, "&R마법이 부여된 진홍빛 균", Rarity.RARE),
  ENCHANTED_WARPED_FUNGUS(Material.WARPED_FUNGUS, "&R마법이 부여된 뒤틀린 균", Rarity.RARE),
  ENCHANTED_SLIME_BALL(Material.SLIME_BALL, "&R마법이 부여된 슴라임볼", Rarity.RARE),
  ENCHANTED_ROTTEN_FLESH(Material.ROTTEN_FLESH, "&R마법이 부여된 썩은 살점", Rarity.RARE),
  ENCHANTED_SWEET_BERRIES(Material.SWEET_BERRIES, "&R마법이 부여된 달콤한 열매", Rarity.RARE),
  ENCHANTED_GLOW_BERRIES(Material.GLOW_BERRIES, "&R마법이 부여된 발광 열매", Rarity.RARE),
  ENCHANTED_BEETROOT(Material.BEETROOT, "&R마법이 부여된 비트", Rarity.RARE),
  ENCHANTED_SAND(Material.SAND, "&R마법이 부여된 모래", Rarity.RARE),
  ENCHANTED_FEATHER(Material.FEATHER, "&R마법이 부여된 깃털", Rarity.RARE),
  ENCHANTED_GUNPOWDER(Material.GUNPOWDER, "&R마법이 부여된 화약", Rarity.RARE),
  ENCHANTED_FLINT(Material.FLINT, "&R마법이 부여된 부싯돌", Rarity.RARE),
  ENCHANTED_GRAVEL(Material.GRAVEL, "&R마법이 부여된 자갈", Rarity.EPIC),
  ENCHANTED_ICE(Material.ICE, "&R마법이 부여된 얼음", Rarity.RARE),
  ENCHANTED_PACKED_ICE(Material.PACKED_ICE, "&R마법이 부여된 꽁꽁 언 얼음", Rarity.EPIC),
  ENCHANTED_BLUE_ICE(Material.BLUE_ICE, "&R마법이 부여된 푸른얼음", Rarity.ELITE),
  ENCHANTED_SPONGE(Material.SPONGE, "&R마법이 부여된 스펀지", Rarity.ELITE),
  ENCHANTED_RED_SAND(Material.RED_SAND, "&R마법이 부여된 붉은 모래", Rarity.RARE),
  ENCHANTED_RABBIT_HIDE(Material.RABBIT_HIDE, "&R마법이 부여된 토끼 가죽", Rarity.RARE),
  ENCHANTED_RABBIT_FOOT(Material.RABBIT_FOOT, "&R마법이 부여된 토끼발", Rarity.EPIC),
  ENCHANTED_PHANTOM_MEMBRANE(Material.PHANTOM_MEMBRANE, "&R마법이 부여된 팬텀 막", Rarity.EPIC),
  ENCHANTED_DRAGON_BREATH(Material.RABBIT_FOOT, "&R마법이 부여된 드래곤의 숨결", Rarity.UNIQUE),
  ENCHANTED_LEATHER(Material.LEATHER, "&R마법이 부여된 가죽", Rarity.RARE),
  ENCHANTED_KELP(Material.KELP, "&R마법이 부여된 켈프", Rarity.RARE),
  ENCHANTED_OCHRE_FROGLIGHT(Material.OCHRE_FROGLIGHT, "&R마법이 부여된 황톳빛 개구리불", Rarity.EXCELLENT),
  ENCHANTED_VERDANT_FROGLIGHT(Material.VERDANT_FROGLIGHT, "&R마법이 부여된 잔딧빛 개구리불", Rarity.EXCELLENT),
  ENCHANTED_PEARLESCENT_FROGLIGHT(Material.PEARLESCENT_FROGLIGHT, "&R마법이 부여된 진줓빛 개구리불", Rarity.EXCELLENT),
  ENCHANTED_CHORUS_FRUIT(Material.CHORUS_FRUIT, "&R마법이 부여된 후렴과", Rarity.EPIC),
  ENCHANTED_CHORUS_FLOWER(Material.CHORUS_FLOWER, "&R마법이 부여된 후렴화", Rarity.ELITE),
  ENCHANTED_INK_SAC(Material.INK_SAC, "&R마법이 부여된 먹물 주머니", Rarity.RARE),
  ENCHANTED_GLOW_INK_SAC(Material.GLOW_INK_SAC, "&R마법이 부여된 발광 먹물 주머니", Rarity.RARE),
  ENCHANTED_TURTLE_EGG(Material.TURTLE_EGG, "&R마법이 부여된 거북 알", Rarity.EPIC),
  ENCHANTED_PUMPKIN(Material.PUMPKIN, "&R마법이 부여된 호박", Rarity.RARE),
  ENCHANTED_CARVED_PUMPKIN(Material.CARVED_PUMPKIN, "&R마법이 부여된 조각된 호박", Rarity.EPIC),
  ENCHANTED_SUGAR_CANE(Material.SUGAR_CANE, "&R마법이 부여된 사탕수수", Rarity.RARE),
  ENCHANTED_SUGAR(Material.SUGAR, "&R마법이 부여된 설탕", Rarity.EPIC),
  ENCHANTED_SNOWBALL(Material.SNOWBALL, "&R마법이 부여된 눈덩이", Rarity.RARE),
  ENCHANTED_SNOW_BLOCK(Material.SNOW_BLOCK, "&R마법이 부여된 눈 블록", Rarity.EPIC),
  ENCHANTED_AMETHYST_SHARD(Material.AMETHYST_SHARD, "&R마법이 부여된 자수정 조각", Rarity.RARE),
  ENCHANTED_AMETHYST_BLOCK(Material.AMETHYST_BLOCK, "&R마법이 부여된 자수정 블록", Rarity.EPIC),
  ENCHANTED_WHEAT(Material.WHEAT, "&R마법이 부여된 밀", Rarity.RARE),
  ENCHANTED_HAY_BLCOK(Material.HAY_BLOCK, "&R마법이 부여된 건초 더미", Rarity.EPIC),
  ENCHANTED_GLOWSTONE_DUST(Material.GLOWSTONE_DUST, "&R마법이 부여된 발광석 가루", Rarity.RARE),
  ENCHANTED_GLOWSTONE(Material.GLOWSTONE, "&R마법이 부여된 발광석", Rarity.EPIC),
  ENCHANTED_ENDER_PEARL(Material.ENDER_PEARL, "&R마법이 부여된 엔더 진주", Rarity.RARE),
  ENCHANTED_ENDER_EYE(Material.ENDER_EYE, "&R마법이 부여된 엔더의 눈", Rarity.EPIC),
  ENCHANTED_NETHER_WART(Material.NETHER_WART, "&R마법이 부여된 네더 와트", Rarity.RARE),
  ENCHANTED_NETHER_WART_BLOCK(Material.NETHER_WART_BLOCK, "&R마법이 부여된 네더 와트 블록", Rarity.EPIC),
  ENCHANTED_MELON_SLICE(Material.MELON_SLICE, "&R마법이 부여된 수박 조각", Rarity.RARE),
  ENCHANTED_MELON(Material.MELON, "&R마법이 부여된 수박", Rarity.EPIC),
  ENCHANTED_QUARTZ(Material.QUARTZ, "&R마법이 부여된 네더 석영", Rarity.RARE),
  ENCHANTED_QUARTZ_BLOCK(Material.QUARTZ_BLOCK, "&R마법이 부여된 석영 블록", Rarity.EPIC),
  ENCHANTED_COBBLESTONE(Material.COBBLESTONE, "&R마법이 부여된 조약돌", Rarity.RARE),
  ENCHANTED_COD(Material.COD, "&R마법이 부여된 대구", Rarity.RARE),
  ENCHANTED_COOKED_COD(Material.COOKED_COD, "&R마법이 부여된 익힌 대구", Rarity.EPIC),
  ENCHANTED_PUPPERFISH(Material.PUFFERFISH, "&R마법이 부여된 복어", Rarity.RARE),
  ENCHANTED_SALMON(Material.SALMON, "&R마법이 부여된 연어", Rarity.RARE),
  ENCHANTED_COOKED_SALMON(Material.COOKED_SALMON, "&R마법이 부여된 익힌 연어", Rarity.EPIC),
  ENCHANTED_POTATO(Material.POTATO, "&R마법이 부여된 감자", Rarity.RARE),
  ENCHANTED_BAKED_POTATO(Material.BAKED_POTATO, "&R마법이 부여된 구운 감자", Rarity.EPIC),
  ENCHANTED_CARROT(Material.CARROT, "&R마법이 부여된 당근", Rarity.RARE),
  ENCHANTED_GOLDEN_CARROT(Material.GOLDEN_CARROT, "&R마법이 부여된 황금 당근", Rarity.EPIC),
  ENCHANTED_APPLE(Material.APPLE, "&R마법이 부여된 사과", Rarity.RARE),
  ENCHANTED_STRING(Material.STRING, "&R마법이 부여된 실", Rarity.RARE),
  ENCHANTED_CLAY_BALL(Material.CLAY_BALL, "&R마법이 부여된 점토 덩이", Rarity.RARE),
  ENCHANTED_LILY_PAD(Material.LILY_PAD, "&R마법이 부여된 수련잎", Rarity.RARE),
  ENCHANTED_SCUTE(Material.SCUTE, "&R마법이 부여된 인갑", Rarity.UNIQUE),
  ENCHANTED_MAGMA_CREAM(Material.MAGMA_CREAM, "&R마법이 부여된 마그마 크림", Rarity.EPIC),
  ENCHANTED_BLAZE_ROD(Material.BLAZE_ROD, "&R마법이 부여된 블레이즈 막대기", Rarity.EPIC),
  ENCHANTED_CACTUS(Material.CACTUS, "&R마법이 부여된 선인장", Rarity.RARE),
  ENCHANTED_GREEN_DYE(Material.GREEN_DYE, "&R마법이 부여된 초록색 염료", Rarity.EPIC),
  ENCHANTED_SPIDER_EYE(Material.SPIDER_EYE, "&R마법이 부여된 거미 눈", Rarity.RARE),
  ENCHANTED_FERMENTED_SPIDER_EYE(Material.FERMENTED_SPIDER_EYE, "&R마법이 부여된 발효된 거미 눈", Rarity.EPIC),
  ENCHANTED_PORKCHOP(Material.PORKCHOP, "&R마법이 부여된 익히지 않은 돼지고기", Rarity.RARE),
  ENCHANTED_COOKED_PORKCHOP(Material.COOKED_PORKCHOP, "&R마법이 부여된 익힌 돼지고기", Rarity.EPIC),
  ENCHANTED_BEEF(Material.BEEF, "&R마법이 부여된 익히지 않은 소고기", Rarity.RARE),
  ENCHANTED_COOKED_BEEF(Material.COOKED_BEEF, "&R마법이 부여된 익힌 소고기", Rarity.EPIC),
  ENCHANTED_RABBIT(Material.RABBIT, "&R마법이 부여된 익히지 않은 토끼고기", Rarity.RARE),
  ENCHANTED_COOKED_RABBIT(Material.COOKED_RABBIT, "&R마법이 부여된 익힌 토끼고기", Rarity.EPIC),
  ENCHANTED_MUTTON(Material.MUTTON, "&R마법이 부여된 익히지 않은 양고기", Rarity.RARE),
  ENCHANTED_COOKED_MUTTON(Material.COOKED_MUTTON, "&R마법이 부여된 익힌 양고기", Rarity.EPIC),
  ENCHANTED_CHICKEN(Material.CHICKEN, "&R마법이 부여된 익히지 않은 닭고기", Rarity.RARE),
  ENCHANTED_COOKED_CHICKEN(Material.COOKED_CHICKEN, "&R마법이 부여된 익힌 닭고기", Rarity.EPIC),
  ENCHANTED_TROPICAL_FISH(Material.TROPICAL_FISH, "&R마법이 부여된 열대어", Rarity.RARE),
  ENCHANTED_COBBLED_DEEPSLATE(Material.COBBLED_DEEPSLATE, "&R마법이 부여된 심층암 조약돌", Rarity.RARE),
  ENCHANTED_COPPER_BLOCK(Material.COPPER_BLOCK, "&R마법이 부여된 구리 블록", Rarity.EPIC),
  ENCHANTED_COPPER_INGOT(Material.COPPER_INGOT, "&R마법이 부여된 구리 주괴", Rarity.RARE),
  ENCHANTED_CRIMSON_STEM(Material.CRIMSON_STEM, "&R마법이 부여된 진홍빛 자루", Rarity.RARE),
  ENCHANTED_DARK_OAK_LOG(Material.DARK_OAK_LOG, "&R마법이 부여된 짙은 참나무 원목", Rarity.RARE),
  ENCHANTED_DIAMOND(Material.DIAMOND, "&R마법이 부여된 다이아몬드", Rarity.EPIC),
  ENCHANTED_DIAMOND_BLOCK(Material.DIAMOND_BLOCK, "&R마법이 부여된 다이아몬드 블록", Rarity.ELITE),
  ENCHANTED_NETHERITE_INGOT(Material.NETHERITE_INGOT, "&R마법이 부여된 네더라이트 주괴", Rarity.EXCELLENT),
  ENCHANTED_NETHERITE_BLOCK(Material.NETHERITE_BLOCK, "&R마법이 부여된 네더라이트 블록", Rarity.LEGENDARY),
  ENCHANTED_EMERALD(Material.EMERALD, "&R마법이 부여된 에메랄드", Rarity.EPIC),
  ENCHANTED_EMERALD_BLOCK(Material.EMERALD_BLOCK, "&R마법이 부여된 에메랄드 블록", Rarity.ELITE),
  ENCHANTED_GOLD_BLOCK(Material.GOLD_BLOCK, "&R마법이 부여된 금 블록", Rarity.EPIC),
  ENCHANTED_GOLD_INGOT(Material.GOLD_INGOT, "&R마법이 부여된 금 주괴", Rarity.RARE),
  ENCHANTED_IRON_BLOCK(Material.IRON_BLOCK, "&R마법이 부여된 철 블록", Rarity.EPIC),
  ENCHANTED_IRON_INGOT(Material.IRON_INGOT, "&R마법이 부여된 철 주괴", Rarity.RARE),
  ENCHANTED_JUNGLE_LOG(Material.JUNGLE_LOG, "&R마법이 부여된 정글나무 원목", Rarity.RARE),
  ENCHANTED_LAPIS_BLOCK(Material.LAPIS_BLOCK, "&R마법이 부여된 청금석 블록", Rarity.EPIC),
  ENCHANTED_LAPIS_LAZULI(Material.LAPIS_LAZULI, "&R마법이 부여된 청금석", Rarity.RARE),
  ENCHANTED_MANGROVE_LOG(Material.MANGROVE_LOG, "&R마법이 부여된 맹그로브나무 원목", Rarity.RARE),
  ENCHANTED_MITHRIL(Material.PRISMARINE_CRYSTALS, "&R마법이 부여된 미스릴", Rarity.ELITE),
  ENCHANTED_OAK_LOG(Material.OAK_LOG, "&R마법이 부여된 참나무 원목", Rarity.RARE),
  ENCHANTED_REDSTONE(Material.REDSTONE, "&R마법이 부여된 레드스톤", Rarity.RARE),
  ENCHANTED_REDSTONE_BLOCK(Material.REDSTONE_BLOCK, "&R마법이 부여된 레드스톤 블록", Rarity.EPIC),
  ENCHANTED_SPRUCE_LOG(Material.SPRUCE_LOG, "&R마법이 부여된 가문비나무 원목", Rarity.RARE),
  ENCHANTED_TITANIUM(Material.PLAYER_HEAD, "&R마법이 부여된 티타늄", Rarity.UNIQUE),
  ENCHANTED_WARPED_STEM(Material.WARPED_STEM, "&R마법이 부여된 뒤틀린 자루", Rarity.RARE),
  IQ_CHOOK_CHUCK(Material.REDSTONE_BLOCK, "&RIQ 축척기", Rarity.RARE),
  JADE(Material.PLAYER_HEAD, "&R옥 원석", Rarity.UNIQUE),
  JASPER(Material.PLAYER_HEAD, "&R벽옥 원석", Rarity.UNIQUE),
  MITHRIL(Material.PRISMARINE_CRYSTALS, "&R미스릴 원석", Rarity.EPIC),
  MITHRIL_PICKAXE(Material.IRON_PICKAXE, "&R미스릴 곡괭이", Rarity.ELITE),
  PORTABLE_CRAFTING_TABLE(Material.PLAYER_HEAD, "&R휴대용 작업대", Rarity.RARE),
  REFINED_MITHRIL(Material.PLAYER_HEAD, "&R정제된 미스릴", Rarity.UNIQUE),
  REFINED_MITHRIL_PICKAXE(Material.IRON_PICKAXE, "&R정제된 미스릴 곡괭이", Rarity.ELITE),
  REFINED_TITANIUM(Material.PLAYER_HEAD, "&R정제된 티타늄", Rarity.EXCELLENT),
  REFINED_TITANIUM_PICKAXE(Material.IRON_PICKAXE, "&R정제된 티타늄 곡괭이", Rarity.ELITE),
  RUBY(Material.PLAYER_HEAD, "&R루비 원석", Rarity.UNIQUE),
  SANS_BOOTS(Material.LEATHER_BOOTS, "&7샌즈 부츠", Rarity.RARE),
  SANS_CHESTPLATE(Material.LEATHER_CHESTPLATE, "&7샌즈 흉갑", Rarity.RARE),
  SANS_HELMET(Material.LEATHER_HELMET, "&7샌즈 투구", Rarity.RARE),
  SANS_LEGGINGS(Material.LEATHER_LEGGINGS, "&7샌즈 각반", Rarity.RARE),
  SAPPHIRE(Material.PLAYER_HEAD, "&R사파이어", Rarity.UNIQUE),
  STONK(Material.GOLDEN_PICKAXE, "&R채굴기", Rarity.EPIC, CreativeCategory.TOOLS.translationKey()),
  TEST_PICKAXE(Material.IRON_PICKAXE, "&R테스트 곡괭이", Rarity.ARTIFACT, CreativeCategory.TOOLS.translationKey()),
  THE_MUSIC(Material.MUSIC_DISC_5, "&R'그' 노래", Rarity.EPIC),
  TITANIUM_DRILL_R266(Material.PRISMARINE_SHARD, "&R티타늄 드릴 R266", Rarity.UNIQUE, CreativeCategory.TOOLS.translationKey()),
  TITANIUM_DRILL_R366(Material.PRISMARINE_SHARD, "&R티타늄 드릴 R366", Rarity.UNIQUE, CreativeCategory.TOOLS.translationKey()),
  TITANIUM_DRILL_R466(Material.PRISMARINE_SHARD, "&R티타늄 드릴 R466", Rarity.UNIQUE, CreativeCategory.TOOLS.translationKey()),
  TITANIUM_DRILL_R566(Material.PRISMARINE_SHARD, "&R티타늄 드릴 R566", Rarity.EXCELLENT, CreativeCategory.TOOLS.translationKey()),
  TITANIUM_ORE(Material.PLAYER_HEAD, "&R티타늄 광석", Rarity.ELITE),
  TITANIUM_PICKAXE(Material.IRON_PICKAXE, "&R티타늄 곡괭이", Rarity.ELITE),
  TODWOT_PICKAXE(Material.WOODEN_PICKAXE, "&R섕쟀 곡괭이", Rarity.ARTIFACT, CreativeCategory.TOOLS.translationKey()),
  TOPAZ(Material.PLAYER_HEAD, "&R황옥 원석", Rarity.UNIQUE),
  ;

  private final Material displayMaterial;

  private final Rarity rarity;

  private final String category;

  private final Component displayName;

  CustomMaterial(Material displayMaterial, String displayName)
  {
    this(displayMaterial, displayName, Rarity.NORMAL, CreativeCategory.MISC.translationKey());
  }

  @SuppressWarnings("unused")
  CustomMaterial(Material displayMaterial, Component displayName)
  {
    this(displayMaterial, displayName, Rarity.NORMAL, CreativeCategory.MISC.translationKey());
  }

  @SuppressWarnings("unused")
  CustomMaterial(Material displayMaterial, String displayName, String category)
  {
    this(displayMaterial, displayName, Rarity.NORMAL, category);
  }

  @SuppressWarnings("unused")
  CustomMaterial(Material displayMaterial, Component displayName, String category)
  {
    this(displayMaterial, displayName, Rarity.NORMAL, category);
  }

  CustomMaterial(Material displayMaterial, String displayName, Rarity rarity)
  {
    this(displayMaterial, displayName, rarity, CreativeCategory.MISC.translationKey());
  }

  @SuppressWarnings("unused")
  CustomMaterial(Material displayMaterial, Component displayName, Rarity rarity)
  {
    this(displayMaterial, displayName, rarity, CreativeCategory.MISC.translationKey());
  }

  CustomMaterial(Material displayMaterial, String displayName, Rarity rarity, String category)
  {
    this(displayMaterial, ComponentUtil.translate(displayName.startsWith("&R") ? rarity.colorString() + displayName.substring(2) : displayName), rarity, category);
  }

  CustomMaterial(Material displayMaterial, Component displayName, Rarity rarity, String category)
  {
    this.displayMaterial = displayMaterial;
    this.displayName = displayName;
    this.rarity = rarity;
    this.category = category;
  }

  /**
   * @return A vanilla {@link Material} of this item.
   */
  @NotNull
  public Material getDisplayMaterial()
  {
    return displayMaterial;
  }

  /**
   * @return {@link Rarity} of this item.
   */
  @NotNull
  public Rarity getRarity()
  {
    return rarity;
  }

  /**
   * @return A translation key of this item's {@link CreativeCategory#translationKey} or custom category key.
   */
  @NotNull
  public String getCategory()
  {
    return category;
  }

  /**
   * @return Default displayname of this item.
   */
  @NotNull
  public Component getDisplayName()
  {
    return displayName;
  }

  /**
   * Creates an {@link ItemStack} of this {@link CustomMaterial}.
   * @return Created {@link ItemStack}.
   */
  @NotNull
  public ItemStack create()
  {
    ItemStack itemStack = new ItemStack(this.displayMaterial);
    NBTItem nbtItem = new NBTItem(itemStack, true);
    nbtItem.setString("id", this.toString().toLowerCase());
    return ItemLore.setItemLore(itemStack);
  }

  /**
   * @return true if this item should be glow even wihtout any {@link org.bukkit.enchantments.Enchantment} it has otherwise false.
   */
  public boolean isGlow()
  {
    return this.toString().startsWith("ENCHANTED") || switch (this)
            {
              case TODWOT_PICKAXE, IQ_CHOOK_CHUCK -> true;
              default -> false;
            };
  }
}
