package com.jho5245.cucumbery.util.storage.data;

import com.jho5245.cucumbery.util.itemlore.ItemLore;
import com.jho5245.cucumbery.util.no_groups.MessageUtil;
import com.jho5245.cucumbery.util.storage.component.util.ComponentUtil;
import com.jho5245.cucumbery.util.storage.no_groups.ItemCategory.Rarity;
import com.jho5245.cucumbery.util.storage.no_groups.ItemStackUtil;
import de.tr7zw.changeme.nbtapi.NBTItem;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.translation.Translatable;
import org.bukkit.Material;
import org.bukkit.inventory.CreativeCategory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Represents custom {@link Material}.
 */
public enum CustomMaterial implements Translatable
{
  AMBER(Material.ORANGE_DYE, "key:item.cucumbery.amber;&i호박", Rarity.UNIQUE),
  ARROW_CRIT(Material.ARROW, "&i치명적인 화살", Rarity.RARE, CreativeCategory.COMBAT.translationKey()),
  ARROW_EXPLOSIVE(Material.ARROW, "&i폭발성 화살", Rarity.RARE, CreativeCategory.COMBAT.translationKey()),
  ARROW_EXPLOSIVE_DESTRUCTION(Material.ARROW, "&i파괴형 폭발성 화살", Rarity.RARE, CreativeCategory.COMBAT.translationKey()),
  ARROW_FLAME(Material.ARROW, "&i화염 화살", Rarity.RARE, CreativeCategory.COMBAT.translationKey()),
  ARROW_INFINITE(Material.ARROW, "&i무한의 화살", Rarity.ELITE, CreativeCategory.COMBAT.translationKey()),
  ARROW_MOUNT(Material.ARROW, "&i라이딩 화살", Rarity.RARE, CreativeCategory.COMBAT.translationKey()),
  ARROW_MOUNT_DISPOSAL(Material.ARROW, "&i라이딩 화살 (1회용)", Rarity.RARE, CreativeCategory.COMBAT.translationKey()),
  ARROW_MOUNT_INFINITE(Material.ARROW, "&i라이딩 화살 (무제한)", Rarity.RARE, CreativeCategory.COMBAT.translationKey()),
  BAD_APPLE(Material.APPLE, "&i나쁜 사과", CreativeCategory.FOOD.translationKey()),
  BAD_APPLE_EXPIRE_1D(Material.APPLE, "&i나쁜 사과", CreativeCategory.FOOD.translationKey()),
  BAD_APPLE_EXPIRE_1D_UNTRADEABLE(Material.APPLE, "&i나쁜 사과", CreativeCategory.FOOD.translationKey()),
  BAD_APPLE_EXPIRE_7D(Material.APPLE, "&i나쁜 사과", CreativeCategory.FOOD.translationKey()),
  BAD_APPLE_EXPIRE_7D_UNTRADEABLE(Material.APPLE, "&i나쁜 사과", CreativeCategory.FOOD.translationKey()),
  BAD_APPLE_UNTRADEABLE(Material.APPLE, "&i나쁜 사과", CreativeCategory.FOOD.translationKey()),
  BAMIL_PABO(Material.PLAYER_HEAD, "&i머밀의 바리", Rarity.ARTIFACT, CreativeCategory.DECORATIONS.translationKey()),
  BEACON_DECORATIVE(Material.BEACON, "&i장식용 신호기", Rarity.EPIC, CreativeCategory.DECORATIONS.translationKey()),
  BEACON_HAT(Material.BEACON, "&i신호기 모자", Rarity.EPIC, CreativeCategory.DECORATIONS.translationKey()),
  BEACON_HAT_EXPIRE_1D(Material.BEACON, "&i신호기 모자", Rarity.EPIC, CreativeCategory.DECORATIONS.translationKey()),
  BEACON_HAT_EXPIRE_1D_UNTRADEABLE(Material.BEACON, "&i신호기 모자", Rarity.EPIC, CreativeCategory.DECORATIONS.translationKey()),
  BEACON_HAT_EXPIRE_7D(Material.BEACON, "&i신호기 모자", Rarity.EPIC, CreativeCategory.DECORATIONS.translationKey()),
  BEACON_HAT_EXPIRE_7D_UNTRADEABLE(Material.BEACON, "&i신호기 모자", Rarity.EPIC, CreativeCategory.DECORATIONS.translationKey()),
  BEACON_HAT_UNTRADEABLE(Material.BEACON, "&i신호기 모자", Rarity.EPIC, CreativeCategory.DECORATIONS.translationKey()),
  BOO(Material.SCUTE, "&b부우"),
  BOO_HUNGRY(Material.SCUTE, "&b배고프부우.."),
  BOW_CRIT(Material.BOW, "&i치명적인 활", Rarity.RARE, CreativeCategory.COMBAT.translationKey()),
  BOW_ENDER_PEARL(Material.BOW, "&i엔더 진주 활", Rarity.RARE, CreativeCategory.COMBAT.translationKey()),
  BOW_EXPLOSIVE(Material.BOW, "&i폭발성 활", Rarity.RARE, CreativeCategory.COMBAT.translationKey()),
  BOW_EXPLOSIVE_DESTRUCTION(Material.BOW, "&i파괴형 폭발성 활", Rarity.RARE, CreativeCategory.COMBAT.translationKey()),
  BOW_FLAME(Material.BOW, "&i화염 활", Rarity.RARE, CreativeCategory.COMBAT.translationKey()),
  BOW_INFINITE(Material.BOW, "&i무한의 활", Rarity.ELITE, CreativeCategory.COMBAT.translationKey()),
  BOW_MOUNT(Material.BOW, "&i라이딩 활", Rarity.RARE, CreativeCategory.COMBAT.translationKey()),
  BREAD_DIRTY(Material.BREAD, "&i더러운 빵", Rarity.JUNK, CreativeCategory.FOOD.translationKey()),
  BRONZE_INGOT(Material.BRICK, "&i청동 주괴"),
  COBALT_AXE(Material.DIAMOND_AXE, "&i코발트 도끼", Rarity.UNIQUE, CreativeCategory.TOOLS.translationKey()),
  COBALT_BOOTS(Material.LEATHER_BOOTS, "&i코발트 부츠", Rarity.UNIQUE, CreativeCategory.COMBAT.translationKey()),

  COBALT_CHESTPLATE(Material.LEATHER_CHESTPLATE, "&i코발트 흉갑", Rarity.UNIQUE, CreativeCategory.COMBAT.translationKey()),
  COBALT_HELMET(Material.LEATHER_HELMET, "&i코발트 투구", Rarity.UNIQUE, CreativeCategory.COMBAT.translationKey()),
  COBALT_HOE(Material.DIAMOND_HOE, "&i코발트 괭이", Rarity.UNIQUE, CreativeCategory.TOOLS.translationKey()),
  COBALT_INGOT(Material.LAPIS_LAZULI, "&i코발트 주괴", Rarity.UNIQUE),

  COBALT_LEGGINGS(Material.LEATHER_LEGGINGS, "&i코발트 레깅스", Rarity.UNIQUE, CreativeCategory.COMBAT.translationKey()),
  COBALT_ORE(Material.BLUE_DYE, "&i코발트 원석", Rarity.UNIQUE),

  COBALT_PICKAXE(Material.DIAMOND_PICKAXE, "&i코발트 곡괭이", Rarity.UNIQUE, CreativeCategory.TOOLS.translationKey()),

  COBALT_SHOVEL(Material.DIAMOND_SHOVEL, "&i코발트 삽", Rarity.UNIQUE, CreativeCategory.TOOLS.translationKey()),
  COBALT_SWORD(Material.DIAMOND_SWORD, "&i코발트 검", Rarity.UNIQUE, CreativeCategory.COMBAT.translationKey()),
  COPPER_AXE(Material.STONE_AXE, "&i구리 도끼", CreativeCategory.TOOLS.translationKey()),
  COPPER_BOOTS(Material.LEATHER_BOOTS, "&i구리 부츠", CreativeCategory.COMBAT.translationKey()),
  COPPER_CHESTPLATE(Material.LEATHER_CHESTPLATE, "&i구리 흉갑", CreativeCategory.COMBAT.translationKey()),
  COPPER_HELMET(Material.LEATHER_HELMET, "&i구리 투구", CreativeCategory.COMBAT.translationKey()),
  COPPER_HOE(Material.STONE_HOE, "&i구리 괭이", CreativeCategory.TOOLS.translationKey()),
  COPPER_LEGGINGS(Material.LEATHER_LEGGINGS, "&i구리 레깅스", CreativeCategory.COMBAT.translationKey()),
  COPPER_PICKAXE(Material.STONE_PICKAXE, "&i구리 곡괭이", CreativeCategory.TOOLS.translationKey()),
  COPPER_SHOVEL(Material.STONE_SHOVEL, "&i구리 삽", CreativeCategory.TOOLS.translationKey()),
  COPPER_SWORD(Material.STONE_SWORD, "&i구리 검", CreativeCategory.COMBAT.translationKey()),

  CORE_GEMSTONE(Material.PRISMARINE_CRYSTALS, "&b코어 젬스톤", Rarity.EPIC),
  CORE_GEMSTONE_EXPERIENCE(Material.PRISMARINE_CRYSTALS, "&a경험의 코어 젬스톤", Rarity.EPIC),

  CORE_GEMSTONE_MIRROR(Material.PRISMARINE_CRYSTALS, "&c거울세계의 코어 젬스톤", Rarity.EPIC),

  CORE_GEMSTONE_MITRA(Material.PRISMARINE_CRYSTALS, "&6미트라의 코어 젬스톤", Rarity.EPIC),

  CUCUMBERITE_INGOT(Material.EMERALD, "&i오이스터늄 주괴"),
  CUCUMBERITE_ORE(Material.SCUTE, "&i오이스터늄 원석"),
  CUTE_SUGAR(Material.SUGAR, "&a커여운 슦가", Rarity.EPIC, "슈가"),
  CUTE_SUGAR_HUNGRY(Material.SUGAR, "&a배고푼 커여운 슦가", Rarity.EPIC, "슈가"),
  DIAMOND_BLOCK_DECORATIVE(Material.DIAMOND_BLOCK, "&i장식용 다이아몬드 블록", Rarity.NORMAL, CreativeCategory.BUILDING_BLOCKS.translationKey()),
  DIAMOND_CHESTPLATE_WITH_ELYTRA(Material.ELYTRA, "&i다이아몬드 겉날개", Rarity.EXCELLENT, CreativeCategory.TRANSPORTATION.translationKey()),
  DOEHAERIM_BABO(Material.PLAYER_HEAD, "&i바보의 머리", Rarity.ARTIFACT, CreativeCategory.DECORATIONS.translationKey()),
  DRILL_ENGINE(Material.BLAST_FURNACE, "&i드릴 엔진", Rarity.RARE, "재료"),
  DRILL_FUEL_TANK(Material.BARREL, "&i드릴 연료 탱크", Rarity.UNIQUE, "재료"),
  ELYTRA_SHIVA_AMOODO_NAREUL_MAKEURLSOON_UPSOROAN(Material.ELYTRA, "&i시바 아무도 나를 막을 순 없겉날개", Rarity.LEGENDARY, CreativeCategory.COMBAT.translationKey()),
  ENCHANTED_ACACIA_LOG(Material.ACACIA_LOG, "&i마법이 부여된 아카시아나무 원목", Rarity.RARE),

  ENCHANTED_AMETHYST_BLOCK(Material.AMETHYST_BLOCK, "&i마법이 부여된 자수정 블록", Rarity.EPIC),
  ENCHANTED_AMETHYST_SHARD(Material.AMETHYST_SHARD, "&i마법이 부여된 자수정 조각", Rarity.RARE),
  ENCHANTED_APPLE(Material.APPLE, "&i마법이 부여된 사과", Rarity.RARE),
  ENCHANTED_BAKED_POTATO(Material.BAKED_POTATO, "&i마법이 부여된 구운 감자", Rarity.EPIC),
  ENCHANTED_BEEF(Material.BEEF, "&i마법이 부여된 익히지 않은 소고기", Rarity.RARE),
  ENCHANTED_BEETROOT(Material.BEETROOT, "&i마법이 부여된 비트", Rarity.RARE),
  ENCHANTED_BIRCH_LOG(Material.BIRCH_LOG, "&i마법이 부여된 자작나무 원목", Rarity.RARE),
  ENCHANTED_BLAZE_ROD(Material.BLAZE_ROD, "&i마법이 부여된 블레이즈 막대기", Rarity.EPIC),
  ENCHANTED_BLUE_ICE(Material.BLUE_ICE, "&i마법이 부여된 푸른얼음", Rarity.ELITE),
  ENCHANTED_BONE(Material.BONE, "&i마법이 부여된 뼈다귀", Rarity.RARE),
  ENCHANTED_BONE_BLOCK(Material.BONE_BLOCK, "&i마법이 부여된 뼈 블록", Rarity.EPIC),
  ENCHANTED_BROWN_MUSHROOM(Material.BROWN_MUSHROOM, "&i마법이 부여된 갈색 버섯", Rarity.RARE),
  ENCHANTED_BROWN_MUSHROOM_BLOCK(Material.BROWN_MUSHROOM_BLOCK, "&i마법이 부여된 갈색 버섯 블록", Rarity.EPIC),
  ENCHANTED_CACTUS(Material.CACTUS, "&i마법이 부여된 선인장", Rarity.RARE),
  ENCHANTED_CARROT(Material.CARROT, "&i마법이 부여된 당근", Rarity.RARE),
  ENCHANTED_CARVED_PUMPKIN(Material.CARVED_PUMPKIN, "&i마법이 부여된 조각된 호박", Rarity.EPIC),
  ENCHANTED_CHICKEN(Material.CHICKEN, "&i마법이 부여된 익히지 않은 닭고기", Rarity.RARE),
  ENCHANTED_CHORUS_FLOWER(Material.CHORUS_FLOWER, "&i마법이 부여된 후렴화", Rarity.ELITE),
  ENCHANTED_CHORUS_FRUIT(Material.CHORUS_FRUIT, "&i마법이 부여된 후렴과", Rarity.EPIC),
  ENCHANTED_CLAY_BALL(Material.CLAY_BALL, "&i마법이 부여된 점토 덩이", Rarity.RARE),
  ENCHANTED_COAL(Material.COAL, "&i마법이 부여된 석탄", Rarity.RARE),

  ENCHANTED_COAL_BLOCK(Material.COAL_BLOCK, "&i마법이 부여된 석탄 블록", Rarity.EPIC),
  ENCHANTED_COBBLED_DEEPSLATE(Material.COBBLED_DEEPSLATE, "&i마법이 부여된 심층암 조약돌", Rarity.RARE),
  ENCHANTED_COBBLESTONE(Material.COBBLESTONE, "&i마법이 부여된 조약돌", Rarity.RARE),
  ENCHANTED_COD(Material.COD, "&i마법이 부여된 대구", Rarity.RARE),
  ENCHANTED_COOKED_BEEF(Material.COOKED_BEEF, "&i마법이 부여된 익힌 소고기", Rarity.EPIC),
  ENCHANTED_COOKED_CHICKEN(Material.COOKED_CHICKEN, "&i마법이 부여된 익힌 닭고기", Rarity.EPIC),
  ENCHANTED_COOKED_COD(Material.COOKED_COD, "&i마법이 부여된 익힌 대구", Rarity.EPIC),
  ENCHANTED_COOKED_MUTTON(Material.COOKED_MUTTON, "&i마법이 부여된 익힌 양고기", Rarity.EPIC),
  ENCHANTED_COOKED_PORKCHOP(Material.COOKED_PORKCHOP, "&i마법이 부여된 익힌 돼지고기", Rarity.EPIC),
  ENCHANTED_COOKED_RABBIT(Material.COOKED_RABBIT, "&i마법이 부여된 익힌 토끼고기", Rarity.EPIC),
  ENCHANTED_COOKED_SALMON(Material.COOKED_SALMON, "&i마법이 부여된 익힌 연어", Rarity.EPIC),
  ENCHANTED_COPPER_BLOCK(Material.COPPER_BLOCK, "&i마법이 부여된 구리 블록", Rarity.EPIC),
  ENCHANTED_COPPER_INGOT(Material.COPPER_INGOT, "&i마법이 부여된 구리 주괴", Rarity.RARE),
  ENCHANTED_CRIMSON_FUNGUS(Material.CRIMSON_FUNGUS, "&i마법이 부여된 진홍빛 균", Rarity.RARE),
  ENCHANTED_CRIMSON_STEM(Material.CRIMSON_STEM, "&i마법이 부여된 진홍빛 자루", Rarity.RARE),
  ENCHANTED_DARK_OAK_LOG(Material.DARK_OAK_LOG, "&i마법이 부여된 짙은 참나무 원목", Rarity.RARE),
  ENCHANTED_DIAMOND(Material.DIAMOND, "&i마법이 부여된 다이아몬드", Rarity.EPIC),
  ENCHANTED_DIAMOND_BLOCK(Material.DIAMOND_BLOCK, "&i마법이 부여된 다이아몬드 블록", Rarity.ELITE),
  ENCHANTED_DRAGON_BREATH(Material.RABBIT_FOOT, "&i마법이 부여된 드래곤의 숨결", Rarity.UNIQUE),
  ENCHANTED_EMERALD(Material.EMERALD, "&i마법이 부여된 에메랄드", Rarity.EPIC),
  ENCHANTED_EMERALD_BLOCK(Material.EMERALD_BLOCK, "&i마법이 부여된 에메랄드 블록", Rarity.ELITE),
  ENCHANTED_ENDER_EYE(Material.ENDER_EYE, "&i마법이 부여된 엔더의 눈", Rarity.EPIC),
  ENCHANTED_ENDER_PEARL(Material.ENDER_PEARL, "&i마법이 부여된 엔더 진주", Rarity.RARE),
  ENCHANTED_FEATHER(Material.FEATHER, "&i마법이 부여된 깃털", Rarity.RARE),
  ENCHANTED_FERMENTED_SPIDER_EYE(Material.FERMENTED_SPIDER_EYE, "&i마법이 부여된 발효된 거미 눈", Rarity.EPIC),
  ENCHANTED_FLINT(Material.FLINT, "&i마법이 부여된 부싯돌", Rarity.RARE),
  ENCHANTED_GLOWSTONE(Material.GLOWSTONE, "&i마법이 부여된 발광석", Rarity.EPIC),
  ENCHANTED_GLOWSTONE_DUST(Material.GLOWSTONE_DUST, "&i마법이 부여된 발광석 가루", Rarity.RARE),
  ENCHANTED_GLOW_BERRIES(Material.GLOW_BERRIES, "&i마법이 부여된 발광 열매", Rarity.RARE),
  ENCHANTED_GLOW_INK_SAC(Material.GLOW_INK_SAC, "&i마법이 부여된 발광 먹물 주머니", Rarity.RARE),
  ENCHANTED_GOLDEN_CARROT(Material.GOLDEN_CARROT, "&i마법이 부여된 황금 당근", Rarity.EPIC),
  ENCHANTED_GOLD_BLOCK(Material.GOLD_BLOCK, "&i마법이 부여된 금 블록", Rarity.EPIC),
  ENCHANTED_GOLD_INGOT(Material.GOLD_INGOT, "&i마법이 부여된 금 주괴", Rarity.RARE),
  ENCHANTED_GRAVEL(Material.GRAVEL, "&i마법이 부여된 자갈", Rarity.EPIC),
  ENCHANTED_GREEN_DYE(Material.GREEN_DYE, "&i마법이 부여된 초록색 염료", Rarity.EPIC),
  ENCHANTED_GUNPOWDER(Material.GUNPOWDER, "&i마법이 부여된 화약", Rarity.RARE),
  ENCHANTED_HAY_BLCOK(Material.HAY_BLOCK, "&i마법이 부여된 건초 더미", Rarity.EPIC),
  ENCHANTED_ICE(Material.ICE, "&i마법이 부여된 얼음", Rarity.RARE),
  ENCHANTED_INK_SAC(Material.INK_SAC, "&i마법이 부여된 먹물 주머니", Rarity.RARE),
  ENCHANTED_IRON_BLOCK(Material.IRON_BLOCK, "&i마법이 부여된 철 블록", Rarity.EPIC),
  ENCHANTED_IRON_INGOT(Material.IRON_INGOT, "&i마법이 부여된 철 주괴", Rarity.RARE),
  ENCHANTED_JUNGLE_LOG(Material.JUNGLE_LOG, "&i마법이 부여된 정글나무 원목", Rarity.RARE),
  ENCHANTED_KELP(Material.KELP, "&i마법이 부여된 켈프", Rarity.RARE),
  ENCHANTED_LAPIS_BLOCK(Material.LAPIS_BLOCK, "&i마법이 부여된 청금석 블록", Rarity.EPIC),
  ENCHANTED_LAPIS_LAZULI(Material.LAPIS_LAZULI, "&i마법이 부여된 청금석", Rarity.RARE),
  ENCHANTED_LEATHER(Material.LEATHER, "&i마법이 부여된 가죽", Rarity.RARE),
  ENCHANTED_LILY_PAD(Material.LILY_PAD, "&i마법이 부여된 수련잎", Rarity.RARE),
  ENCHANTED_MAGMA_CREAM(Material.MAGMA_CREAM, "&i마법이 부여된 마그마 크림", Rarity.EPIC),
  ENCHANTED_MANGROVE_LOG(Material.MANGROVE_LOG, "&i마법이 부여된 맹그로브나무 원목", Rarity.RARE),
  ENCHANTED_MELON(Material.MELON, "&i마법이 부여된 수박", Rarity.EPIC),
  ENCHANTED_MELON_SLICE(Material.MELON_SLICE, "&i마법이 부여된 수박 조각", Rarity.RARE),
  ENCHANTED_MITHRIL_INGOT(Material.IRON_INGOT, "&i마법이 부여된 미스릴 주괴", Rarity.EXCELLENT),
  ENCHANTED_MUTTON(Material.MUTTON, "&i마법이 부여된 익히지 않은 양고기", Rarity.RARE),
  ENCHANTED_NETHERITE_BLOCK(Material.NETHERITE_BLOCK, "&i마법이 부여된 네더라이트 블록", Rarity.LEGENDARY),
  ENCHANTED_NETHERITE_INGOT(Material.NETHERITE_INGOT, "&i마법이 부여된 네더라이트 주괴", Rarity.EXCELLENT),
  ENCHANTED_NETHER_WART(Material.NETHER_WART, "&i마법이 부여된 네더 와트", Rarity.RARE),
  ENCHANTED_NETHER_WART_BLOCK(Material.NETHER_WART_BLOCK, "&i마법이 부여된 네더 와트 블록", Rarity.EPIC),
  ENCHANTED_OAK_LOG(Material.OAK_LOG, "&i마법이 부여된 참나무 원목", Rarity.RARE),
  ENCHANTED_OCHRE_FROGLIGHT(Material.OCHRE_FROGLIGHT, "&i마법이 부여된 황톳빛 개구리불", Rarity.EXCELLENT),
  ENCHANTED_PACKED_ICE(Material.PACKED_ICE, "&i마법이 부여된 꽁꽁 언 얼음", Rarity.EPIC),
  ENCHANTED_PEARLESCENT_FROGLIGHT(Material.PEARLESCENT_FROGLIGHT, "&i마법이 부여된 진줓빛 개구리불", Rarity.EXCELLENT),
  ENCHANTED_PHANTOM_MEMBRANE(Material.PHANTOM_MEMBRANE, "&i마법이 부여된 팬텀 막", Rarity.EPIC),
  ENCHANTED_PORKCHOP(Material.PORKCHOP, "&i마법이 부여된 익히지 않은 돼지고기", Rarity.RARE),
  ENCHANTED_POTATO(Material.POTATO, "&i마법이 부여된 감자", Rarity.RARE),
  ENCHANTED_PUMPKIN(Material.PUMPKIN, "&i마법이 부여된 호박", Rarity.RARE),
  ENCHANTED_PUPPERFISH(Material.PUFFERFISH, "&i마법이 부여된 복어", Rarity.RARE),
  ENCHANTED_QUARTZ(Material.QUARTZ, "&i마법이 부여된 네더 석영", Rarity.RARE),
  ENCHANTED_QUARTZ_BLOCK(Material.QUARTZ_BLOCK, "&i마법이 부여된 석영 블록", Rarity.EPIC),
  ENCHANTED_RABBIT(Material.RABBIT, "&i마법이 부여된 익히지 않은 토끼고기", Rarity.RARE),
  ENCHANTED_RABBIT_FOOT(Material.RABBIT_FOOT, "&i마법이 부여된 토끼발", Rarity.EPIC),
  ENCHANTED_RABBIT_HIDE(Material.RABBIT_HIDE, "&i마법이 부여된 토끼 가죽", Rarity.RARE),
  ENCHANTED_REDSTONE(Material.REDSTONE, "&i마법이 부여된 레드스톤", Rarity.RARE),
  ENCHANTED_REDSTONE_BLOCK(Material.REDSTONE_BLOCK, "&i마법이 부여된 레드스톤 블록", Rarity.EPIC),
  ENCHANTED_RED_MUSHROOM(Material.RED_MUSHROOM, "&i마법이 부여된 빨간색 버섯", Rarity.RARE),
  ENCHANTED_RED_MUSHROOM_BLOCK(Material.RED_MUSHROOM_BLOCK, "&i마법이 부여된 빨간색 버섯 블록", Rarity.EPIC),
  ENCHANTED_RED_SAND(Material.RED_SAND, "&i마법이 부여된 붉은 모래", Rarity.RARE),
  ENCHANTED_ROTTEN_FLESH(Material.ROTTEN_FLESH, "&i마법이 부여된 썩은 살점", Rarity.RARE),
  ENCHANTED_SALMON(Material.SALMON, "&i마법이 부여된 연어", Rarity.RARE),
  ENCHANTED_SAND(Material.SAND, "&i마법이 부여된 모래", Rarity.RARE),
  ENCHANTED_SCUTE(Material.SCUTE, "&i마법이 부여된 인갑", Rarity.UNIQUE),
  ENCHANTED_SLIME_BALL(Material.SLIME_BALL, "&i마법이 부여된 슴라임볼", Rarity.RARE),
  ENCHANTED_SNOWBALL(Material.SNOWBALL, "&i마법이 부여된 눈덩이", Rarity.RARE),
  ENCHANTED_SNOW_BLOCK(Material.SNOW_BLOCK, "&i마법이 부여된 눈 블록", Rarity.EPIC),
  ENCHANTED_SPIDER_EYE(Material.SPIDER_EYE, "&i마법이 부여된 거미 눈", Rarity.RARE),
  ENCHANTED_SPONGE(Material.SPONGE, "&i마법이 부여된 스펀지", Rarity.ELITE),
  ENCHANTED_SPRUCE_LOG(Material.SPRUCE_LOG, "&i마법이 부여된 가문비나무 원목", Rarity.RARE),
  ENCHANTED_STRING(Material.STRING, "&i마법이 부여된 실", Rarity.RARE),
  ENCHANTED_SUGAR(Material.SUGAR, "&i마법이 부여된 설탕", Rarity.EPIC),
  ENCHANTED_SUGAR_CANE(Material.SUGAR_CANE, "&i마법이 부여된 사탕수수", Rarity.RARE),
  ENCHANTED_SWEET_BERRIES(Material.SWEET_BERRIES, "&i마법이 부여된 달콤한 열매", Rarity.RARE),
  ENCHANTED_TITANIUM(Material.PLAYER_HEAD, "&i마법이 부여된 티타늄", Rarity.EXCELLENT),
  ENCHANTED_TROPICAL_FISH(Material.TROPICAL_FISH, "&i마법이 부여된 열대어", Rarity.RARE),
  ENCHANTED_TURTLE_EGG(Material.TURTLE_EGG, "&i마법이 부여된 거북 알", Rarity.EPIC),
  ENCHANTED_VERDANT_FROGLIGHT(Material.VERDANT_FROGLIGHT, "&i마법이 부여된 잔딧빛 개구리불", Rarity.EXCELLENT),
  ENCHANTED_WARPED_FUNGUS(Material.WARPED_FUNGUS, "&i마법이 부여된 뒤틀린 균", Rarity.RARE),
  ENCHANTED_WARPED_STEM(Material.WARPED_STEM, "&i마법이 부여된 뒤틀린 자루", Rarity.RARE),
  ENCHANTED_WHEAT(Material.WHEAT, "&i마법이 부여된 밀", Rarity.RARE),
  EXPERIENCE_BOTTLE_COLOSSAL(Material.EXPERIENCE_BOTTLE, "&R위대한 경험치 병", Rarity.EXCELLENT),
  EXPERIENCE_BOTTLE_GRAND(Material.EXPERIENCE_BOTTLE, "&R대단한 경험치 병", Rarity.ELITE),
  EXPERIENCE_BOTTLE_TITANIC(Material.EXPERIENCE_BOTTLE, "&R엄청난 경험치 병", Rarity.UNIQUE),
  FIREWORK_ROCKET_CHAIN(Material.FIREWORK_ROCKET, "&i연쇄형 폭죽", Rarity.EPIC),
  FIREWORK_ROCKET_EXPLOSIVE(Material.FIREWORK_ROCKET, "&i폭발성 폭죽 기본형", Rarity.EPIC),
  FIREWORK_ROCKET_EXPLOSIVE_DESTRUCTION(Material.FIREWORK_ROCKET, "&i폭발성 폭죽 파괴형", Rarity.EPIC),
  FIREWORK_ROCKET_EXPLOSIVE_FLAME(Material.FIREWORK_ROCKET, "&i폭발성 폭죽 발화형", Rarity.EPIC),
  FIREWORK_ROCKET_REPEATING(Material.FIREWORK_ROCKET, "&i반복형 폭죽", Rarity.EPIC),
  FLINT_SHOVEL(Material.IRON_SHOVEL, "&i부싯돌 삽", Rarity.RARE, CreativeCategory.TOOLS.translationKey()),
  FROG_BOOTS(Material.LEATHER_BOOTS, "&2개구리 신발", Rarity.RARE, CreativeCategory.COMBAT.translationKey()),
  FROG_CHESTPLATE(Material.LEATHER_CHESTPLATE, "&2개구리 옷", Rarity.RARE, CreativeCategory.COMBAT.translationKey()),
  FROG_HELMET(Material.PLAYER_HEAD, "&2개구리 모자", Rarity.RARE, CreativeCategory.COMBAT.translationKey()),
  FROG_LEGGINGS(Material.LEATHER_LEGGINGS, "&2개구리 바지", Rarity.RARE, CreativeCategory.COMBAT.translationKey()),
  IQ_CHOOK_CHUCK(Material.REDSTONE_BLOCK, "&iIQ 축척기", Rarity.RARE),
  JADE(Material.LIME_DYE, "&i옥 원석", Rarity.UNIQUE),
  LARGE_DRILL_FUEL(Material.COAL, "&i대형 드릴 연료", Rarity.UNIQUE, "드릴 연료"),
  LEAD_INGOT(Material.IRON_INGOT, "&i납 주괴"),
  LEAD_ORE(Material.GRAY_DYE, "&i납 원석"),
  MEDIUM_DRILL_FUEL(Material.COAL, "&i중형 드릴 연료", Rarity.RARE, "드릴 연료"),
  MINDAS_BOOTS(Material.GOLDEN_BOOTS, "&R마인더스의 신발", Rarity.LEGENDARY, "갑옷"),
  MINDAS_CHESTPLATE(Material.GOLDEN_CHESTPLATE, "&R마인더스의 상의", Rarity.LEGENDARY, "갑옷"),
  MINDAS_DRILL(Material.PRISMARINE_SHARD, "&i마인더스의 드릴", Rarity.LEGENDARY, CreativeCategory.TOOLS.translationKey()),
  MINDAS_HELMET(Material.PLAYER_HEAD, "&R마인더스의 헬멧", Rarity.LEGENDARY, "갑옷"),
  MINDAS_LEGGINGS(Material.GOLDEN_LEGGINGS, "&R마인더스의 하의", Rarity.LEGENDARY, "갑옷"),
  MINER_BOOTS(Material.LEATHER_BOOTS, "&i광부 신발", Rarity.RARE, "갑옷"),
  MINER_CHESTPLATE(Material.LEATHER_CHESTPLATE, "&i광부 상의", Rarity.RARE, "갑옷"),
  MINER_HELMET(Material.LEATHER_HELMET, "&i광부 헬멧", Rarity.RARE, "갑옷"),
  MINER_LEGGINGS(Material.LEATHER_LEGGINGS, "&i광부 하의", Rarity.RARE, "갑옷"),
  MITHRIL_AXE(Material.DIAMOND_AXE, "&i미스릴 도끼", Rarity.UNIQUE, CreativeCategory.TOOLS.translationKey()),
  MITHRIL_BOOTS(Material.LEATHER_BOOTS, "&i미스릴 부츠", Rarity.UNIQUE, CreativeCategory.COMBAT.translationKey()),
  MITHRIL_CHESTPLATE(Material.LEATHER_CHESTPLATE, "&i미스릴 흉갑", Rarity.UNIQUE, CreativeCategory.COMBAT.translationKey()),

  MITHRIL_HELMET(Material.LEATHER_HELMET, "&i미스릴 투구", Rarity.UNIQUE, CreativeCategory.COMBAT.translationKey()),

  MITHRIL_HOE(Material.DIAMOND_HOE, "&i미스릴 괭이", Rarity.UNIQUE, CreativeCategory.TOOLS.translationKey()),
  MITHRIL_INGOT(Material.IRON_INGOT, "&i미스릴 주괴", Rarity.UNIQUE),
  MITHRIL_LEGGINGS(Material.LEATHER_LEGGINGS, "&i미스릴 레깅스", Rarity.UNIQUE, CreativeCategory.COMBAT.translationKey()),
  MITHRIL_ORE(Material.PRISMARINE_CRYSTALS, "&i미스릴 원석", Rarity.UNIQUE),
  MITHRIL_PICKAXE(Material.DIAMOND_PICKAXE, "&i미스릴 곡괭이", Rarity.UNIQUE, CreativeCategory.TOOLS.translationKey()),
  MITHRIL_PICKAXE_REFINED(Material.DIAMOND_PICKAXE, "&i정제된 미스릴 곡괭이", Rarity.UNIQUE, CreativeCategory.TOOLS.translationKey()),
  MITHRIL_REFINED(Material.PLAYER_HEAD, "&i정제된 미스릴", Rarity.LEGENDARY),
  MITHRIL_SHOVEL(Material.DIAMOND_SHOVEL, "&i미스릴 삽", Rarity.UNIQUE, CreativeCategory.TOOLS.translationKey()),
  MITHRIL_SWORD(Material.DIAMOND_SWORD, "&i미스릴 검", Rarity.UNIQUE, CreativeCategory.TOOLS.translationKey()),
  MORGANITE(Material.PINK_DYE, "&i홍주석 원석", Rarity.UNIQUE),
  MUSHROOM_STEW_PICKAXE(Material.MUSHROOM_STEW, "&i스튜 곡괭이?", Rarity.ARTIFACT, CreativeCategory.TOOLS.translationKey()),
  NAUTILITE_INGOT(Material.IRON_INGOT, "&i노틸라이트 주괴", Rarity.EXCELLENT),
  NAUTILITE_ORE(Material.WHITE_DYE, "&i노틸라이트 원석", Rarity.EXCELLENT),
  NETHERITE_BLOCK_DECORATIVE(Material.NETHERITE_BLOCK, "&i장식용 네더라이트 블록", Rarity.NORMAL, CreativeCategory.BUILDING_BLOCKS.translationKey()),
  NETHERITE_CHESTPLATE_WITH_ELYTRA(Material.ELYTRA, "&i네더라이트 겉날개", Rarity.EXCELLENT, CreativeCategory.TRANSPORTATION.translationKey()),
  PLASTIC_DEBRIS(Material.LIGHT_BLUE_DYE, "&i플라스틱 파편", Rarity.RARE),
  PLASTIC_MATERIAL(Material.LAPIS_LAZULI, "&i플라스틱 자재", Rarity.RARE),
  PLATINUM_INGOT(Material.GOLD_INGOT, "&i백금 주괴", Rarity.RARE),
  PLATINUM_ORE(Material.YELLOW_DYE, "&i백금 원석", Rarity.RARE),
  PORTABLE_CRAFTING_TABLE(Material.PLAYER_HEAD, "&i휴대용 작업대", Rarity.RARE, CreativeCategory.TOOLS.translationKey()),
  PORTABLE_ENDER_CHEST(Material.PLAYER_HEAD, "&i휴대용 엔더 상자", Rarity.UNIQUE, CreativeCategory.TOOLS.translationKey()),
  RAINBOW_BOOTS(Material.IRON_BOOTS, "&i무지개 부츠", Rarity.RARE, CreativeCategory.COMBAT.translationKey()),
  RAINBOW_CHESTPLATE(Material.IRON_CHESTPLATE, "&i무지개 흉갑", Rarity.RARE, CreativeCategory.COMBAT.translationKey()),
  RAINBOW_HELMET(Material.IRON_HELMET, "&i무지개 투구", Rarity.RARE, CreativeCategory.COMBAT.translationKey()),
  RAINBOW_LEGGINGS(Material.IRON_LEGGINGS, "&i무지개 레깅스", Rarity.RARE, CreativeCategory.COMBAT.translationKey()),
  RUBY(Material.RED_DYE, "&i루비 원석", Rarity.UNIQUE),

  RUNE_DESTRUCTION(Material.TNT_MINECART, "&i파멸의 룬"),
  RUNE_EARTHQUAKE(Material.TNT_MINECART, "&i지진의 룬"),

  SANS_BOOTS(Material.LEATHER_BOOTS, "&7샌즈 부츠", Rarity.RARE, "갑옷"),

  SANS_CHESTPLATE(Material.LEATHER_CHESTPLATE, "&7샌즈 흉갑", Rarity.RARE, "갑옷"),

  SANS_HELMET(Material.LEATHER_HELMET, "&7샌즈 투구", Rarity.RARE, "갑옷"),

  SANS_LEGGINGS(Material.LEATHER_LEGGINGS, "&7샌즈 각반", Rarity.RARE, "갑옷"),
  SAPPHIRE(Material.BLUE_DYE, "&i사파이어", Rarity.UNIQUE),

  SHROOMITE_INGOT(Material.NETHER_BRICK, "&i쉬루마이트 주괴"),
  SHROOMITE_ORE(Material.RED_DYE, "&i쉬루마이트 원석"),
  SMALL_DRILL_FUEL(Material.COAL, "&i소형 드릴 연료", Rarity.NORMAL, "드릴 연료"),
  SMALL_MINING_SACK(Material.PLAYER_HEAD, "&i소형 광물 가방", Rarity.RARE, "가방"),
  SNOWBALL_AI_VO(Material.SNOWBALL, "&i위대한 센잿 눈덩이", Rarity.RARE),
  SNOWBALL_GGUMONG(Material.SNOWBALL, "&i꾸쏴쒸쓰와씌쏴수 눈덩이", Rarity.RARE),
  SPIDER_BOOTS(Material.IRON_BOOTS, "&i거미 부츠", Rarity.RARE, CreativeCategory.COMBAT.translationKey()),
  SPYGLASS_TELEPORT(Material.SPYGLASS, "&i텔레포트 망원경", Rarity.ELITE, "마법 도구"),
  STONK(Material.GOLDEN_PICKAXE, "&i채굴기", Rarity.EPIC, CreativeCategory.TOOLS.translationKey()),
  TEST_PICKAXE(Material.IRON_PICKAXE, "&i테스트 곡괭이", Rarity.ARTIFACT, CreativeCategory.TOOLS.translationKey()),
  THE_MUSIC(Material.MUSIC_DISC_5, "&i'그' 노래", Rarity.EPIC),
  TIN_INGOT(Material.COPPER_INGOT, "&i주석 주괴"),
  TIN_ORE(Material.LIGHT_GRAY_DYE, "&i주석 원석"),
  TITANIUM_AXE(Material.IRON_AXE, "&i티타늄 도끼", Rarity.UNIQUE, CreativeCategory.TOOLS.translationKey()),
  TITANIUM_BOOTS(Material.LEATHER_BOOTS, "&i티타늄 부츠", Rarity.UNIQUE, CreativeCategory.COMBAT.translationKey()),
  TITANIUM_CHESTPLATE(Material.LEATHER_CHESTPLATE, "&i티타늄 흉갑", Rarity.UNIQUE, CreativeCategory.COMBAT.translationKey()),
  TITANIUM_DRILL_R266(Material.PRISMARINE_SHARD, "&i티타늄 드릴 R266", Rarity.EXCELLENT, CreativeCategory.TOOLS.translationKey()),
  TITANIUM_DRILL_R366(Material.PRISMARINE_SHARD, "&i티타늄 드릴 R366", Rarity.EXCELLENT, CreativeCategory.TOOLS.translationKey()),
  TITANIUM_DRILL_R466(Material.PRISMARINE_SHARD, "&i티타늄 드릴 R466", Rarity.EXCELLENT, CreativeCategory.TOOLS.translationKey()),
  TITANIUM_DRILL_R566(Material.PRISMARINE_SHARD, "&i티타늄 드릴 R566", Rarity.LEGENDARY, CreativeCategory.TOOLS.translationKey()),
  TITANIUM_HELMET(Material.LEATHER_HELMET, "&i티타늄 투구", Rarity.UNIQUE, CreativeCategory.COMBAT.translationKey()),

  TITANIUM_HOE(Material.IRON_HOE, "&i티타늄 괭이", Rarity.UNIQUE, CreativeCategory.TOOLS.translationKey()),
  TITANIUM_INGOT(Material.IRON_INGOT, "&i티타늄 주괴", Rarity.UNIQUE),
  TITANIUM_LEGGINGS(Material.LEATHER_LEGGINGS, "&i티타늄 레깅스", Rarity.UNIQUE, CreativeCategory.COMBAT.translationKey()),
  TITANIUM_ORE(Material.QUARTZ, "&i티타늄 원석", Rarity.UNIQUE),
  TITANIUM_PICKAXE(Material.IRON_PICKAXE, "&i티타늄 곡괭이", Rarity.UNIQUE, CreativeCategory.TOOLS.translationKey()),
  TITANIUM_PICKAXE_REFINED(Material.IRON_PICKAXE, "&i정제된 티타늄 곡괭이", Rarity.UNIQUE, CreativeCategory.TOOLS.translationKey()),

  TITANIUM_REFINED(Material.PLAYER_HEAD, "&i정제된 티타늄", Rarity.LEGENDARY),
  TITANIUM_SHOVEL(Material.IRON_SHOVEL, "&i티타늄 삽", Rarity.UNIQUE, CreativeCategory.TOOLS.translationKey()),

  TITANIUM_SWORD(Material.IRON_SWORD, "&i티타늄 검", Rarity.UNIQUE, CreativeCategory.TOOLS.translationKey()),
  TNT_COMBAT(Material.TNT, "&i전투용 TNT", Rarity.RARE, CreativeCategory.REDSTONE.translationKey()),
  TNT_DONUT(Material.TNT, "&i도넛 TNT", Rarity.EPIC, CreativeCategory.REDSTONE.translationKey()),
  TNT_DRAIN(Material.TNT, "&i탈수 TNT", Rarity.RARE, CreativeCategory.REDSTONE.translationKey()),
  TNT_I_WONT_LET_YOU_GO(Material.TNT, "&i워어 아워 래 유 TNT", Rarity.RARE, CreativeCategory.REDSTONE.translationKey()),
  TNT_SUPERIOR(Material.TNT, "&i강력한 TNT", Rarity.EPIC, CreativeCategory.REDSTONE.translationKey()),
  TODWOT_PICKAXE(Material.WOODEN_PICKAXE, "&i섕쟀 곡괭이", Rarity.ARTIFACT, CreativeCategory.TOOLS.translationKey()),

  TOMATO(Material.APPLE, "&i토마토", CreativeCategory.FOOD.translationKey()),

  TOPAZ(Material.YELLOW_DYE, "&i황옥 원석", Rarity.UNIQUE),

  TRACKER(Material.NAME_TAG, "&i트래커", Rarity.ELITE, "마법 도구"),

  TUNGSTEN_AXE(Material.IRON_AXE, "&i텅스텐 도끼", Rarity.RARE, CreativeCategory.TOOLS.translationKey()),

  TUNGSTEN_BOOTS(Material.LEATHER_BOOTS, "&i텅스텐 부츠", Rarity.RARE, CreativeCategory.COMBAT.translationKey()),

  TUNGSTEN_CHESTPLATE(Material.LEATHER_CHESTPLATE, "&i텅스텐 흉갑", Rarity.RARE, CreativeCategory.COMBAT.translationKey()),

  TUNGSTEN_HELMET(Material.LEATHER_HELMET, "&i텅스텐 투구", Rarity.RARE, CreativeCategory.COMBAT.translationKey()),

  TUNGSTEN_HOE(Material.IRON_HOE, "&i텅스텐 괭이", Rarity.RARE, CreativeCategory.TOOLS.translationKey()),

  TUNGSTEN_INGOT(Material.SLIME_BALL, "&i텅스텐 주괴", Rarity.RARE),

  TUNGSTEN_LEGGINGS(Material.LEATHER_LEGGINGS, "&i텅스텐 레깅스", Rarity.RARE, CreativeCategory.COMBAT.translationKey()),

  TUNGSTEN_ORE(Material.LIME_DYE, "&i텅스텐 원석", Rarity.RARE),
  TUNGSTEN_PICKAXE(Material.IRON_PICKAXE, "&i텅스텐 곡괭이", Rarity.RARE, CreativeCategory.TOOLS.translationKey()),
  TUNGSTEN_SHOVEL(Material.IRON_SHOVEL, "&i텅스텐 삽", Rarity.RARE, CreativeCategory.TOOLS.translationKey()),
  TUNGSTEN_SWORD(Material.IRON_SWORD, "&i텅스텐 검", Rarity.RARE, CreativeCategory.COMBAT.translationKey()),

  UNBINDING_SHEARS(Material.SHEARS, "&i해방의 가위", Rarity.EPIC, "마법 도구"),

  WEATHER_FORECAST(Material.ENDER_EYE, "&i날씨를 알려주는 눈", Rarity.ELITE, CreativeCategory.TOOLS.translationKey()),
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

  public boolean isUntradeable()
  {
    return this.toString().endsWith("UNTRADEABLE");
  }

  @Nullable
  public CustomMaterial getOrigin()
  {
    return switch (this)
            {
              case BAD_APPLE_UNTRADEABLE, BAD_APPLE_EXPIRE_7D, BAD_APPLE_EXPIRE_7D_UNTRADEABLE, BAD_APPLE_EXPIRE_1D, BAD_APPLE_EXPIRE_1D_UNTRADEABLE -> BAD_APPLE;
              case BEACON_HAT_EXPIRE_1D_UNTRADEABLE, BEACON_HAT_EXPIRE_7D, BEACON_HAT_UNTRADEABLE, BEACON_HAT_EXPIRE_1D, BEACON_HAT_EXPIRE_7D_UNTRADEABLE -> BEACON_HAT;
              default -> null;
            };
  }

  public int getExpireDateInDays()
  {
    return switch (this)
            {
              case BAD_APPLE_EXPIRE_7D, BAD_APPLE_EXPIRE_7D_UNTRADEABLE, BEACON_HAT_EXPIRE_7D, BEACON_HAT_EXPIRE_7D_UNTRADEABLE -> 7;
              case BAD_APPLE_EXPIRE_1D, BAD_APPLE_EXPIRE_1D_UNTRADEABLE, BEACON_HAT_EXPIRE_1D, BEACON_HAT_EXPIRE_1D_UNTRADEABLE -> 1;
              default -> -1;
            };
  }

  @Nullable
  public CustomMaterial getSmeltedItem()
  {
    return switch (this)
            {
              case TUNGSTEN_ORE -> TUNGSTEN_INGOT;
              case COBALT_ORE -> COBALT_INGOT;
              case MITHRIL_ORE -> MITHRIL_INGOT;
              case TITANIUM_ORE -> TITANIUM_INGOT;
              case SHROOMITE_ORE -> SHROOMITE_INGOT;
              case CUCUMBERITE_ORE -> CUCUMBERITE_INGOT;
              default -> null;
            };
  }

  @Nullable
  public static CustomMaterial itemStackOf(@Nullable ItemStack itemStack)
  {
    if (!ItemStackUtil.itemExists(itemStack))
    {
      return null;
    }
    try
    {
      return CustomMaterial.valueOf(new NBTItem(itemStack).getString("id").toUpperCase());
    }
    catch (Exception e)
    {
      return null;
    }
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
   * @see CustomMaterial#create(int, boolean)
   *
   * @return Created {@link ItemStack}.
   */
  @NotNull
  public ItemStack create()
  {
    return create(1, true);
  }

  /**
   * Creates an {@link ItemStack} of this {@link CustomMaterial}.
   * @see CustomMaterial#create(int, boolean)
   *
   * @param amount Amount of this item.
   * @return Created {@link ItemStack}.
   */
  @NotNull
  public ItemStack create(int amount)
  {
    return create(amount, true);
  }

  /**
   * Creates an {@link ItemStack} of this {@link CustomMaterial}.
   * @see CustomMaterial#create(int, boolean)
   *
   * @param removeLore If true, will be removed lore item
   * @return Created {@link ItemStack}.
   */
  @NotNull
  public ItemStack create(boolean removeLore)
  {
    return create(1, removeLore);
  }

  /**
   * Creates an {@link ItemStack} of this {@link CustomMaterial}.
   *
   * @param amount     Amount of this item.
   * @param removeLore If true, will be removed lore item
   * @return Created {@link ItemStack}.
   */
  @NotNull
  public ItemStack create(int amount, boolean removeLore)
  {
    ItemStack itemStack = new ItemStack(this.displayMaterial, amount);
    NBTItem nbtItem = new NBTItem(itemStack, true);
    nbtItem.setString("id", this.toString().toLowerCase());
    ItemLore.setItemLore(itemStack);
    if (removeLore)
    {
      return ItemLore.removeItemLore(itemStack, true);
    }
    return itemStack;
  }

  @Override
  public @NotNull String translationKey()
  {
    return MessageUtil.stripColor(MessageUtil.n2s(ComponentUtil.serialize(getDisplayName())));
  }
}
