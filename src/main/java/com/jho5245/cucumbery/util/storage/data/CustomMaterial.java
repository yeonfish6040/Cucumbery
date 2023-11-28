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
  I_WONT_LET_YOU_GO_BLOCK(Material.PLAYER_HEAD, "&ikey:block.cucumbery.i_wont_let_you_go_block|워어 아워 래 유 블록", Rarity.RARE, "key:" + CreativeCategory.DECORATIONS + "|치장 아이템"),
  BLUE_NUMBER_BLOCK(Material.PLAYER_HEAD, "&ikey:block.cucumbery.blue_number_block|파란 숫자 블록", Rarity._ADMIN, "key:" + CreativeCategory.DECORATIONS + "|치장 아이템"),
  TIGHTLY_TIED_HAY_BLOCK(Material.PLAYER_HEAD, "&ikey:item.cucumbery.tightly_tied_hay_block|꽉 묶은 건초 더미", Rarity.ELITE),
  AMBER(Material.ORANGE_DYE, "&ikey:item.cucumbery.amber|호박", Rarity.UNIQUE),
  ARROW_CRIT(Material.ARROW, "&ikey:item.cucumbery.arrow_crit|치명적인 화살", Rarity.RARE, CreativeCategory.COMBAT),
  ARROW_EXPLOSIVE(Material.ARROW, "&ikey:item.cucumbery.arrow_explosive|폭발성 화살", Rarity.RARE, CreativeCategory.COMBAT),
  ARROW_EXPLOSIVE_DESTRUCTION(Material.ARROW, "&ikey:item.cucumbery.arrow_explosive_destruction|파괴형 폭발성 화살", Rarity.RARE, CreativeCategory.COMBAT),
  ARROW_FLAME(Material.ARROW, "&ikey:item.cucumbery.arrow_flame|화염 화살", Rarity.RARE, CreativeCategory.COMBAT),
  ARROW_INFINITE(Material.ARROW, "&ikey:item.cucumbery.arrow_infinite|무한의 화살", Rarity.ELITE, CreativeCategory.COMBAT),
  ARROW_MOUNT(Material.ARROW, "&ikey:item.cucumbery.arrow_mount|라이딩 화살", Rarity.RARE, CreativeCategory.COMBAT),
  ARROW_MOUNT_DISPOSAL(Material.ARROW, "&ikey:item.cucumbery.arrow_mount_disposal|라이딩 화살 (1회용)", Rarity.RARE, CreativeCategory.COMBAT),
  ARROW_MOUNT_INFINITE(Material.ARROW, "&ikey:item.cucumbery.arrow_mount_infinite|라이딩 화살 (무제한)", Rarity.RARE, CreativeCategory.COMBAT),
  BAD_APPLE(Material.APPLE, "&ikey:item.cucumbery.bad_apple|나쁜 사과", "itemGroup.foodAndDrink"),

  BAD_APPLE_EXPIRE_1D(Material.APPLE, "&ikey:item.cucumbery.bad_apple|나쁜 사과", "itemGroup.foodAndDrink"),
  BAD_APPLE_EXPIRE_1D_UNTRADEABLE(Material.APPLE, "&ikey:item.cucumbery.bad_apple|나쁜 사과", "itemGroup.foodAndDrink"),
  BAD_APPLE_EXPIRE_7D(Material.APPLE, "&ikey:item.cucumbery.bad_apple|나쁜 사과", "itemGroup.foodAndDrink"),
  BAD_APPLE_EXPIRE_7D_UNTRADEABLE(Material.APPLE, "&ikey:item.cucumbery.bad_apple_expire_7d_untradeable|나쁜 사과", "itemGroup.foodAndDrink"),
  BAD_APPLE_UNTRADEABLE(Material.APPLE, "&ikey:item.cucumbery.bad_apple_untradeable|나쁜 사과", "itemGroup.foodAndDrink"),

  BAMIL_PABO(Material.PLAYER_HEAD, "&ikey:block.cucumbery.bamil_pabo|머밀의 바리", Rarity.ARTIFACT, "itemGroup.functional"),
  BEACON_DECORATIVE(Material.BEACON, "&ikey:block.cucumbery.beacon_decorative|장식용 신호기", Rarity.EPIC, "itemGroup.functional"),
  BEACON_HAT(Material.BEACON, "&ikey:item.cucumbery.beacon_hat|신호기 모자", Rarity.EPIC, "key:" + CreativeCategory.DECORATIONS + "|치장 아이템"),
  BEACON_HAT_EXPIRE_1D(Material.BEACON, "&ikey:item.cucumbery.beacon_hat|신호기 모자", Rarity.EPIC, "key:" + CreativeCategory.DECORATIONS + "|치장 아이템"),
  BEACON_HAT_EXPIRE_1D_UNTRADEABLE(Material.BEACON, "&ikey:item.cucumbery.beacon_hat|신호기 모자", Rarity.EPIC, "key:" + CreativeCategory.DECORATIONS + "|치장 아이템"),

  BEACON_HAT_EXPIRE_7D(Material.BEACON, "&ikey:item.cucumbery.beacon_hat|신호기 모자", Rarity.EPIC, "key:" + CreativeCategory.DECORATIONS + "|치장 아이템"),
  BEACON_HAT_EXPIRE_7D_UNTRADEABLE(Material.BEACON, "&ikey:item.cucumbery.beacon_hat|신호기 모자", Rarity.EPIC, "key:" + CreativeCategory.DECORATIONS + "|치장 아이템"),
  BEACON_HAT_UNTRADEABLE(Material.BEACON, "&ikey:item.cucumbery.beacon_hat|신호기 모자", Rarity.EPIC, "key:" + CreativeCategory.DECORATIONS + "|치장 아이템"),
  BOO(Material.SCUTE, "&bkey:item.cucumbery.boo|부우"),
  BOO_HUNGRY(Material.SCUTE, "&bkey:item.cucumbery.boo_hungry|배고프부우.."),

  BOW_CRIT(Material.BOW, "&ikey:item.cucumbery.bow_crit|치명적인 활", Rarity.RARE, CreativeCategory.COMBAT),
  BOW_ENDER_PEARL(Material.BOW, "&ikey:item.cucumbery.bow_ender_pearl|엔더 진주 활", Rarity.RARE, CreativeCategory.COMBAT),
  BOW_EXPLOSIVE(Material.BOW, "&ikey:item.cucumbery.bow_explosive|폭발성 활", Rarity.RARE, CreativeCategory.COMBAT),
  BOW_EXPLOSIVE_DESTRUCTION(Material.BOW, "&ikey:item.cucumbery.bow_explosive_destruction|파괴형 폭발성 활", Rarity.RARE, CreativeCategory.COMBAT),
  BOW_FLAME(Material.BOW, "&ikey:item.cucumbery.bow_flame|화염 활", Rarity.RARE, CreativeCategory.COMBAT),

  BOW_INFINITE(Material.BOW, "&ikey:item.cucumbery.bow_infinite|무한의 활", Rarity.ELITE, CreativeCategory.COMBAT),
  BOW_MOUNT(Material.BOW, "&ikey:item.cucumbery.bow_mount|라이딩 활", Rarity.RARE, CreativeCategory.COMBAT),
  BREAD_DIRTY(Material.BREAD, "&ikey:item.cucumbery.bread_dirty|더러운 빵", Rarity.JUNK, "itemGroup.foodAndDrink"),
  BRONZE_AXE(Material.GOLDEN_AXE, "&ikey:item.cucumbery.bronze_axe|청동 도끼", CreativeCategory.TOOLS),
  BRONZE_HOE(Material.GOLDEN_HOE, "&ikey:item.cucumbery.bronze_hoe|청동 괭이", CreativeCategory.TOOLS),

  BRONZE_INGOT(Material.BRICK, "&ikey:item.cucumbery.bronze_ingot|청동 주괴"),
  BRONZE_PICKAXE(Material.GOLDEN_PICKAXE, "&ikey:item.cucumbery.bronze_pickaxe|청동 곡괭이", CreativeCategory.TOOLS),
  BRONZE_SHOVEL(Material.GOLDEN_SHOVEL, "&ikey:item.cucumbery.bronze_shovel|청동 삽", CreativeCategory.TOOLS),
  BRONZE_SWORD(Material.GOLDEN_SWORD, "&ikey:item.cucumbery.bronze_sword|청동 검", CreativeCategory.COMBAT),
  CEMENTED_CARBIDE_AXE(Material.STONE_AXE, "&ikey:item.cucumbery.cemented_carbide_axe|초경합금 도끼", Rarity.UNIQUE, CreativeCategory.TOOLS),
  CEMENTED_CARBIDE_HOE(Material.STONE_HOE, "&ikey:item.cucumbery.cemented_carbide_hoe|초경합금 괭이", Rarity.UNIQUE, CreativeCategory.TOOLS),

  CEMENTED_CARBIDE_INGOT(Material.IRON_INGOT, "&ikey:item.cucumbery.cemented_carbide_ingot|초경합금 주괴", Rarity.UNIQUE),
  CEMENTED_CARBIDE_PICKAXE(Material.STONE_PICKAXE, "&ikey:item.cucumbery.cemented_carbide_pickaxe|초경합금 곡괭이", Rarity.UNIQUE, CreativeCategory.TOOLS),
  CEMENTED_CARBIDE_SHOVEL(Material.STONE_SHOVEL, "&ikey:item.cucumbery.cemented_carbide_shovel|초경합금 삽", Rarity.UNIQUE, CreativeCategory.TOOLS),
  CEMENTED_CARBIDE_SWORD(Material.STONE_SWORD, "&ikey:item.cucumbery.cemented_carbide_sword|초경합금 검", Rarity.UNIQUE, CreativeCategory.COMBAT),
  COBALT_AXE(Material.DIAMOND_AXE, "&ikey:item.cucumbery.cobalt_axe|코발트 도끼", Rarity.UNIQUE, CreativeCategory.TOOLS),
  COBALT_BOOTS(Material.LEATHER_BOOTS, "&ikey:item.cucumbery.cobalt_boots|코발트 부츠", Rarity.UNIQUE, CreativeCategory.COMBAT),
  COBALT_CHESTPLATE(Material.LEATHER_CHESTPLATE, "&ikey:item.cucumbery.cobalt_chestplate|코발트 흉갑", Rarity.UNIQUE, CreativeCategory.COMBAT),
  COBALT_HELMET(Material.LEATHER_HELMET, "&ikey:item.cucumbery.cobalt_helmet|코발트 투구", Rarity.UNIQUE, CreativeCategory.COMBAT),
  COBALT_HOE(Material.DIAMOND_HOE, "&ikey:item.cucumbery.cobalt_hoe|코발트 괭이", Rarity.UNIQUE, CreativeCategory.TOOLS),
  COBALT_INGOT(Material.LAPIS_LAZULI, "&ikey:item.cucumbery.cobalt_ingot|코발트 주괴", Rarity.UNIQUE),
  COBALT_LEGGINGS(Material.LEATHER_LEGGINGS, "&ikey:item.cucumbery.cobalt_leggings|코발트 레깅스", Rarity.UNIQUE, CreativeCategory.COMBAT),
  COBALT_ORE(Material.BLUE_DYE, "&ikey:item.cucumbery.cobalt_ore|코발트 원석", Rarity.UNIQUE),
  COBALT_PICKAXE(Material.DIAMOND_PICKAXE, "&ikey:item.cucumbery.cobalt_pickaxe|코발트 곡괭이", Rarity.UNIQUE, CreativeCategory.TOOLS),
  COBALT_SHOVEL(Material.DIAMOND_SHOVEL, "&ikey:item.cucumbery.cobalt_shovel|코발트 삽", Rarity.UNIQUE, CreativeCategory.TOOLS),
  COBALT_SWORD(Material.DIAMOND_SWORD, "&ikey:item.cucumbery.cobalt_sword|코발트 검", Rarity.UNIQUE, CreativeCategory.COMBAT),
  COPPER_AXE(Material.STONE_AXE, "&ikey:item.cucumbery.copper_axe|구리 도끼", CreativeCategory.TOOLS),
  COPPER_BOOTS(Material.LEATHER_BOOTS, "&ikey:item.cucumbery.copper_boots|구리 부츠", CreativeCategory.COMBAT),
  COPPER_CHESTPLATE(Material.LEATHER_CHESTPLATE, "&ikey:item.cucumbery.copper_chestplate|구리 흉갑", CreativeCategory.COMBAT),
  COPPER_HELMET(Material.LEATHER_HELMET, "&ikey:item.cucumbery.copper_helmet|구리 투구", CreativeCategory.COMBAT),
  COPPER_HOE(Material.STONE_HOE, "&ikey:item.cucumbery.copper_hoe|구리 괭이", CreativeCategory.TOOLS),
  COPPER_LEGGINGS(Material.LEATHER_LEGGINGS, "&ikey:item.cucumbery.copper_leggings|구리 레깅스", CreativeCategory.COMBAT),
  COPPER_PICKAXE(Material.STONE_PICKAXE, "&ikey:item.cucumbery.copper_pickaxe|구리 곡괭이", CreativeCategory.TOOLS),
  COPPER_SHOVEL(Material.STONE_SHOVEL, "&ikey:item.cucumbery.copper_shovel|구리 삽", CreativeCategory.TOOLS),
  COPPER_SWORD(Material.STONE_SWORD, "&ikey:item.cucumbery.copper_sword|구리 검", CreativeCategory.COMBAT),
  CORE_GEMSTONE(Material.PRISMARINE_CRYSTALS, "&bkey:item.cucumbery.core_gemstone|코어 젬스톤", Rarity.EPIC, "key:itemGroup.misc|기타 아이템"),
  CORE_GEMSTONE_EXPERIENCE(Material.PRISMARINE_CRYSTALS, "&akey:item.cucumbery.core_gemstone_experience|경험의 코어 젬스톤", Rarity.EPIC, "key:itemGroup.misc|기타 아이템"),
  CORE_GEMSTONE_MIRROR(Material.PRISMARINE_CRYSTALS, "&ckey:item.cucumbery.core_gemstone_mirror|거울세계의 코어 젬스톤", Rarity.EPIC, "key:itemGroup.misc|기타 아이템"),
  CORE_GEMSTONE_MITRA(Material.PRISMARINE_CRYSTALS, "&6key:item.cucumbery.core_gemstone_mitra|미트라의 코어 젬스톤", Rarity.EPIC, "key:itemGroup.misc|기타 아이템"),
  CUCUMBERITE_INGOT(Material.EMERALD, "&ikey:item.cucumbery.cucumberite_ingot|오이스터늄 주괴"),
  CUCUMBERITE_ORE(Material.SCUTE, "&ikey:item.cucumbery.cucumberite_ore|오이스터늄 원석"),
  CUSTOM_CRAFTING_TABLE(Material.PLAYER_HEAD, "&ikey:block.cucumbery.custom_crafting_table|커스텀 제작대", Rarity.RARE, "itemGroup.functional"),
  CUSTOM_CRAFTING_TABLE_PORTABLE(Material.CRAFTING_TABLE, "&ikey:block.cucumbery.custom_crafting_table_portable|휴대용 커스텀 제작대", Rarity.RARE, CreativeCategory.TOOLS),
  CUTE_SUGAR(Material.SUGAR, "&akey:item.cucumbery.cute_sugar|커여운 슦가", Rarity.EPIC, "key:itemGroup.sugar|슈가"),
  CUTE_SUGAR_HUNGRY(Material.SUGAR, "&akey:item.cucumbery.cute_sugar_hungry|배고푼 커여운 슦가", Rarity.EPIC, "key:itemGroup.sugar|슈가"),
  DIAMOND_BLOCK_DECORATIVE(Material.DIAMOND_BLOCK, "&ikey:item.cucumbery.diamond_block_decorative|장식용 다이아몬드 블록", Rarity.NORMAL, CreativeCategory.BUILDING_BLOCKS),
  DIAMOND_CHESTPLATE_WITH_ELYTRA(Material.ELYTRA, "&ikey:item.cucumbery.diamond_chestplate_with_elytra|다이아몬드 겉날개", Rarity.EXCELLENT, CreativeCategory.TOOLS),
  DOEHAERIM_BABO(Material.PLAYER_HEAD, "&ikey:block.cucumbery.doehaerim_babo|바보의 머리", Rarity.ARTIFACT, "itemGroup.functional"),
  DRILL_ENGINE(Material.BLAST_FURNACE, "&ikey:item.cucumbery.drill_engine|드릴 엔진", Rarity.RARE),
  DRILL_FUEL_TANK(Material.BARREL, "&ikey:item.cucumbery.drill_fuel_tank|드릴 연료 탱크", Rarity.UNIQUE),
  ELYTRA_SHIVA_AMOODO_NAREUL_MAKEURLSOON_UPSOROAN(Material.ELYTRA, "&ikey:item.cucumbery.elytra_shiva_amoodo_nareul_makeurlsoon_upsoroan|시바 아무도 나를 막을 순 없겉날개", Rarity.LEGENDARY, CreativeCategory.COMBAT),
  ENCHANTED_ACACIA_LOG(Material.ACACIA_LOG, "&ikey:item.cucumbery.enchanted_acacia_log|마법이 부여된 아카시아나무 원목", Rarity.RARE),
  ENCHANTED_AMETHYST_BLOCK(Material.AMETHYST_BLOCK, "&ikey:item.cucumbery.enchanted_amethyst_block|마법이 부여된 자수정 블록", Rarity.EPIC),

  ENCHANTED_AMETHYST_SHARD(Material.AMETHYST_SHARD, "&ikey:item.cucumbery.enchanted_amethyst_shard|마법이 부여된 자수정 조각", Rarity.RARE),
  ENCHANTED_APPLE(Material.APPLE, "&ikey:item.cucumbery.enchanted_apple|마법이 부여된 사과", Rarity.RARE),
  ENCHANTED_BAKED_POTATO(Material.BAKED_POTATO, "&ikey:item.cucumbery.enchanted_baked_potato|마법이 부여된 구운 감자", Rarity.EPIC),
  ENCHANTED_BEEF(Material.BEEF, "&ikey:item.cucumbery.enchanted_beef|마법이 부여된 익히지 않은 소고기", Rarity.RARE),

  ENCHANTED_BEETROOT(Material.BEETROOT, "&ikey:item.cucumbery.enchanted_beetroot|마법이 부여된 비트", Rarity.RARE),
  ENCHANTED_BIRCH_LOG(Material.BIRCH_LOG, "&ikey:item.cucumbery.enchanted_birch_log|마법이 부여된 자작나무 원목", Rarity.RARE),

  ENCHANTED_BLAZE_ROD(Material.BLAZE_ROD, "&ikey:item.cucumbery.enchanted_blaze_rod|마법이 부여된 블레이즈 막대기", Rarity.EPIC),

  ENCHANTED_BLUE_ICE(Material.BLUE_ICE, "&ikey:item.cucumbery.enchanted_blue_ice|마법이 부여된 푸른얼음", Rarity.ELITE),
  ENCHANTED_BONE(Material.BONE, "&ikey:item.cucumbery.enchanted_bone|마법이 부여된 뼈다귀", Rarity.RARE),
  ENCHANTED_BONE_BLOCK(Material.BONE_BLOCK, "&ikey:item.cucumbery.enchanted_bone_block|마법이 부여된 뼈 블록", Rarity.EPIC),
  ENCHANTED_BROWN_MUSHROOM(Material.BROWN_MUSHROOM, "&ikey:item.cucumbery.enchanted_brown_mushroom|마법이 부여된 갈색 버섯", Rarity.RARE),
  ENCHANTED_BROWN_MUSHROOM_BLOCK(Material.BROWN_MUSHROOM_BLOCK, "&ikey:item.cucumbery.enchanted_brown_mushroom_block|마법이 부여된 갈색 버섯 블록", Rarity.EPIC),
  ENCHANTED_CACTUS(Material.CACTUS, "&ikey:item.cucumbery.enchanted_cactus|마법이 부여된 선인장", Rarity.RARE),
  ENCHANTED_CARROT(Material.CARROT, "&ikey:item.cucumbery.enchanted_carrot|마법이 부여된 당근", Rarity.RARE),
  ENCHANTED_CARVED_PUMPKIN(Material.CARVED_PUMPKIN, "&ikey:item.cucumbery.enchanted_carved_pumpkin|마법이 부여된 조각된 호박", Rarity.EPIC),
  ENCHANTED_CHICKEN(Material.CHICKEN, "&ikey:item.cucumbery.enchanted_chicken|마법이 부여된 익히지 않은 닭고기", Rarity.RARE),
  ENCHANTED_CHORUS_FLOWER(Material.CHORUS_FLOWER, "&ikey:item.cucumbery.enchanted_chorus_flower|마법이 부여된 후렴화", Rarity.ELITE),
  ENCHANTED_CHORUS_FRUIT(Material.CHORUS_FRUIT, "&ikey:item.cucumbery.enchanted_chorus_fruit|마법이 부여된 후렴과", Rarity.EPIC),

  ENCHANTED_CLAY_BALL(Material.CLAY_BALL, "&ikey:item.cucumbery.enchanted_clay_ball|마법이 부여된 점토 덩이", Rarity.RARE),
  ENCHANTED_COAL(Material.COAL, "&ikey:item.cucumbery.enchanted_coal|마법이 부여된 석탄", Rarity.RARE),

  ENCHANTED_COAL_BLOCK(Material.COAL_BLOCK, "&ikey:item.cucumbery.enchanted_coal_block|마법이 부여된 석탄 블록", Rarity.EPIC),

  ENCHANTED_COBBLED_DEEPSLATE(Material.COBBLED_DEEPSLATE, "&ikey:item.cucumbery.enchanted_cobbled_deepslate|마법이 부여된 심층암 조약돌", Rarity.RARE),

  ENCHANTED_COBBLESTONE(Material.COBBLESTONE, "&ikey:item.cucumbery.enchanted_cobblestone|마법이 부여된 조약돌", Rarity.RARE),
  ENCHANTED_COD(Material.COD, "&ikey:item.cucumbery.enchanted_cod|마법이 부여된 대구", Rarity.RARE),
  ENCHANTED_COOKED_BEEF(Material.COOKED_BEEF, "&ikey:item.cucumbery.enchanted_cooked_beef|마법이 부여된 익힌 소고기", Rarity.EPIC),
  ENCHANTED_COOKED_CHICKEN(Material.COOKED_CHICKEN, "&ikey:item.cucumbery.enchanted_cooked_chicken|마법이 부여된 익힌 닭고기", Rarity.EPIC),
  ENCHANTED_COOKED_COD(Material.COOKED_COD, "&ikey:item.cucumbery.enchanted_cooked_cod|마법이 부여된 익힌 대구", Rarity.EPIC),
  ENCHANTED_COOKED_MUTTON(Material.COOKED_MUTTON, "&ikey:item.cucumbery.enchanted_cooked_mutton|마법이 부여된 익힌 양고기", Rarity.EPIC),
  ENCHANTED_COOKED_PORKCHOP(Material.COOKED_PORKCHOP, "&ikey:item.cucumbery.enchanted_cooked_porkchop|마법이 부여된 익힌 돼지고기", Rarity.EPIC),
  ENCHANTED_COOKED_RABBIT(Material.COOKED_RABBIT, "&ikey:item.cucumbery.enchanted_cooked_rabbit|마법이 부여된 익힌 토끼고기", Rarity.EPIC),
  ENCHANTED_COOKED_SALMON(Material.COOKED_SALMON, "&ikey:item.cucumbery.enchanted_cooked_salmon|마법이 부여된 익힌 연어", Rarity.EPIC),
  ENCHANTED_COPPER_BLOCK(Material.COPPER_BLOCK, "&ikey:item.cucumbery.enchanted_copper_block|마법이 부여된 구리 블록", Rarity.EPIC),
  ENCHANTED_COPPER_INGOT(Material.COPPER_INGOT, "&ikey:item.cucumbery.enchanted_copper_ingot|마법이 부여된 구리 주괴", Rarity.RARE),

  ENCHANTED_CRIMSON_FUNGUS(Material.CRIMSON_FUNGUS, "&ikey:item.cucumbery.enchanted_crimson_fungus|마법이 부여된 진홍빛 균", Rarity.RARE),
  ENCHANTED_CRIMSON_STEM(Material.CRIMSON_STEM, "&ikey:item.cucumbery.enchanted_crimson_stem|마법이 부여된 진홍빛 자루", Rarity.RARE),
  ENCHANTED_DARK_OAK_LOG(Material.DARK_OAK_LOG, "&ikey:item.cucumbery.enchanted_dark_oak_log|마법이 부여된 짙은 참나무 원목", Rarity.RARE),
  ENCHANTED_DIAMOND(Material.DIAMOND, "&ikey:item.cucumbery.enchanted_diamond|마법이 부여된 다이아몬드", Rarity.EPIC),
  ENCHANTED_DIAMOND_BLOCK(Material.DIAMOND_BLOCK, "&ikey:item.cucumbery.enchanted_diamond_block|마법이 부여된 다이아몬드 블록", Rarity.ELITE),
  ENCHANTED_DRAGON_BREATH(Material.RABBIT_FOOT, "&ikey:item.cucumbery.enchanted_dragon_breath|마법이 부여된 드래곤의 숨결", Rarity.UNIQUE),
  ENCHANTED_EMERALD(Material.EMERALD, "&ikey:item.cucumbery.enchanted_emerald|마법이 부여된 에메랄드", Rarity.EPIC),
  ENCHANTED_EMERALD_BLOCK(Material.EMERALD_BLOCK, "&ikey:item.cucumbery.enchanted_emerald_block|마법이 부여된 에메랄드 블록", Rarity.ELITE),
  ENCHANTED_ENDER_EYE(Material.ENDER_EYE, "&ikey:item.cucumbery.enchanted_ender_eye|마법이 부여된 엔더의 눈", Rarity.EPIC),
  ENCHANTED_ENDER_PEARL(Material.ENDER_PEARL, "&ikey:item.cucumbery.enchanted_ender_pearl|마법이 부여된 엔더 진주", Rarity.RARE),
  ENCHANTED_FEATHER(Material.FEATHER, "&ikey:item.cucumbery.enchanted_feather|마법이 부여된 깃털", Rarity.RARE),
  ENCHANTED_FERMENTED_SPIDER_EYE(Material.FERMENTED_SPIDER_EYE, "&ikey:item.cucumbery.enchanted_fermented_spider_eye|마법이 부여된 발효된 거미 눈", Rarity.EPIC),
  ENCHANTED_FLINT(Material.FLINT, "&ikey:item.cucumbery.enchanted_flint|마법이 부여된 부싯돌", Rarity.RARE),
  ENCHANTED_GLOWSTONE(Material.GLOWSTONE, "&ikey:item.cucumbery.enchanted_glowstone|마법이 부여된 발광석", Rarity.EPIC),
  ENCHANTED_GLOWSTONE_DUST(Material.GLOWSTONE_DUST, "&ikey:item.cucumbery.enchanted_glowstone_dust|마법이 부여된 발광석 가루", Rarity.RARE),
  ENCHANTED_GLOW_BERRIES(Material.GLOW_BERRIES, "&ikey:item.cucumbery.enchanted_glow_berries|마법이 부여된 발광 열매", Rarity.RARE),
  ENCHANTED_GLOW_INK_SAC(Material.GLOW_INK_SAC, "&ikey:item.cucumbery.enchanted_glow_ink_sac|마법이 부여된 발광 먹물 주머니", Rarity.RARE),
  ENCHANTED_GOLDEN_CARROT(Material.GOLDEN_CARROT, "&ikey:item.cucumbery.enchanted_golden_carrot|마법이 부여된 황금 당근", Rarity.EPIC),
  ENCHANTED_GOLD_BLOCK(Material.GOLD_BLOCK, "&ikey:item.cucumbery.enchanted_gold_block|마법이 부여된 금 블록", Rarity.EPIC),
  ENCHANTED_GOLD_INGOT(Material.GOLD_INGOT, "&ikey:item.cucumbery.enchanted_gold_ingot|마법이 부여된 금 주괴", Rarity.RARE),
  ENCHANTED_GRAVEL(Material.GRAVEL, "&ikey:item.cucumbery.enchanted_gravel|마법이 부여된 자갈", Rarity.EPIC),

  ENCHANTED_GREEN_DYE(Material.GREEN_DYE, "&ikey:item.cucumbery.enchanted_green_dye|마법이 부여된 초록색 염료", Rarity.EPIC),
  ENCHANTED_GUNPOWDER(Material.GUNPOWDER, "&ikey:item.cucumbery.enchanted_gunpowder|마법이 부여된 화약", Rarity.RARE),
  ENCHANTED_HAY_BLOCK(Material.HAY_BLOCK, "&ikey:item.cucumbery.enchanted_hay_block|마법이 부여된 건초 더미", Rarity.EPIC),
  ENCHANTED_ICE(Material.ICE, "&ikey:item.cucumbery.enchanted_ice|마법이 부여된 얼음", Rarity.RARE),
  ENCHANTED_INK_SAC(Material.INK_SAC, "&ikey:item.cucumbery.enchanted_ink_sac|마법이 부여된 먹물 주머니", Rarity.RARE),
  ENCHANTED_IRON_BLOCK(Material.IRON_BLOCK, "&ikey:item.cucumbery.enchanted_iron_block|마법이 부여된 철 블록", Rarity.EPIC),
  ENCHANTED_IRON_INGOT(Material.IRON_INGOT, "&ikey:item.cucumbery.enchanted_iron_ingot|마법이 부여된 철 주괴", Rarity.RARE),
  ENCHANTED_JUNGLE_LOG(Material.JUNGLE_LOG, "&ikey:item.cucumbery.enchanted_jungle_log|마법이 부여된 정글나무 원목", Rarity.RARE),
  ENCHANTED_KELP(Material.KELP, "&ikey:item.cucumbery.enchanted_kelp|마법이 부여된 켈프", Rarity.RARE),
  ENCHANTED_LAPIS_BLOCK(Material.LAPIS_BLOCK, "&ikey:item.cucumbery.enchanted_lapis_block|마법이 부여된 청금석 블록", Rarity.EPIC),
  ENCHANTED_LAPIS_LAZULI(Material.LAPIS_LAZULI, "&ikey:item.cucumbery.enchanted_lapis_lazuli|마법이 부여된 청금석", Rarity.RARE),
  ENCHANTED_LEATHER(Material.LEATHER, "&ikey:item.cucumbery.enchanted_leather|마법이 부여된 가죽", Rarity.RARE),
  ENCHANTED_LILY_PAD(Material.LILY_PAD, "&ikey:item.cucumbery.enchanted_lily_pad|마법이 부여된 수련잎", Rarity.RARE),
  ENCHANTED_MAGMA_CREAM(Material.MAGMA_CREAM, "&ikey:item.cucumbery.enchanted_magma_cream|마법이 부여된 마그마 크림", Rarity.EPIC),
  ENCHANTED_MANGROVE_LOG(Material.MANGROVE_LOG, "&ikey:item.cucumbery.enchanted_mangrove_log|마법이 부여된 맹그로브나무 원목", Rarity.RARE),
  ENCHANTED_MELON(Material.MELON, "&ikey:item.cucumbery.enchanted_melon|마법이 부여된 수박", Rarity.EPIC),
  ENCHANTED_MELON_SLICE(Material.MELON_SLICE, "&ikey:item.cucumbery.enchanted_melon_slice|마법이 부여된 수박 조각", Rarity.RARE),
  ENCHANTED_MITHRIL_INGOT(Material.IRON_INGOT, "&ikey:item.cucumbery.enchanted_mithril_ingot|마법이 부여된 미스릴 주괴", Rarity.EXCELLENT),
  ENCHANTED_MUTTON(Material.MUTTON, "&ikey:item.cucumbery.enchanted_mutton|마법이 부여된 익히지 않은 양고기", Rarity.RARE),
  ENCHANTED_NETHERITE_BLOCK(Material.NETHERITE_BLOCK, "&ikey:item.cucumbery.enchanted_netherite_block|마법이 부여된 네더라이트 블록", Rarity.LEGENDARY),
  ENCHANTED_NETHERITE_INGOT(Material.NETHERITE_INGOT, "&ikey:item.cucumbery.enchanted_netherite_ingot|마법이 부여된 네더라이트 주괴", Rarity.EXCELLENT),
  ENCHANTED_NETHER_WART(Material.NETHER_WART, "&ikey:item.cucumbery.enchanted_nether_wart|마법이 부여된 네더 와트", Rarity.RARE),
  ENCHANTED_NETHER_WART_BLOCK(Material.NETHER_WART_BLOCK, "&ikey:item.cucumbery.enchanted_nether_wart_block|마법이 부여된 네더 와트 블록", Rarity.EPIC),
  ENCHANTED_OAK_LOG(Material.OAK_LOG, "&ikey:item.cucumbery.enchanted_oak_log|마법이 부여된 참나무 원목", Rarity.RARE),
  ENCHANTED_OCHRE_FROGLIGHT(Material.OCHRE_FROGLIGHT, "&ikey:item.cucumbery.enchanted_ochre_froglight|마법이 부여된 황톳빛 개구리불", Rarity.EXCELLENT),
  ENCHANTED_PACKED_ICE(Material.PACKED_ICE, "&ikey:item.cucumbery.enchanted_packed_ice|마법이 부여된 꽁꽁 언 얼음", Rarity.EPIC),
  ENCHANTED_PEARLESCENT_FROGLIGHT(Material.PEARLESCENT_FROGLIGHT, "&ikey:item.cucumbery.enchanted_pearlescent_froglight|마법이 부여된 진줓빛 개구리불", Rarity.EXCELLENT),
  ENCHANTED_PHANTOM_MEMBRANE(Material.PHANTOM_MEMBRANE, "&ikey:item.cucumbery.enchanted_phantom_membrane|마법이 부여된 팬텀 막", Rarity.EPIC),
  ENCHANTED_PORKCHOP(Material.PORKCHOP, "&ikey:item.cucumbery.enchanted_porkchop|마법이 부여된 익히지 않은 돼지고기", Rarity.RARE),
  ENCHANTED_POTATO(Material.POTATO, "&ikey:item.cucumbery.enchanted_potato|마법이 부여된 감자", Rarity.RARE),
  ENCHANTED_PUMPKIN(Material.PUMPKIN, "&ikey:item.cucumbery.enchanted_pumpkin|마법이 부여된 호박", Rarity.RARE),
  ENCHANTED_PUPPERFISH(Material.PUFFERFISH, "&ikey:item.cucumbery.enchanted_pupperfish|마법이 부여된 복어", Rarity.RARE),
  ENCHANTED_QUARTZ(Material.QUARTZ, "&ikey:item.cucumbery.enchanted_quartz|마법이 부여된 네더 석영", Rarity.RARE),
  ENCHANTED_QUARTZ_BLOCK(Material.QUARTZ_BLOCK, "&ikey:item.cucumbery.enchanted_quartz_block|마법이 부여된 석영 블록", Rarity.EPIC),
  ENCHANTED_RABBIT(Material.RABBIT, "&ikey:item.cucumbery.enchanted_rabbit|마법이 부여된 익히지 않은 토끼고기", Rarity.RARE),
  ENCHANTED_RABBIT_FOOT(Material.RABBIT_FOOT, "&ikey:item.cucumbery.enchanted_rabbit_foot|마법이 부여된 토끼발", Rarity.EPIC),
  ENCHANTED_RABBIT_HIDE(Material.RABBIT_HIDE, "&ikey:item.cucumbery.enchanted_rabbit_hide|마법이 부여된 토끼 가죽", Rarity.RARE),
  ENCHANTED_REDSTONE(Material.REDSTONE, "&ikey:item.cucumbery.enchanted_redstone|마법이 부여된 레드스톤", Rarity.RARE),
  ENCHANTED_REDSTONE_BLOCK(Material.REDSTONE_BLOCK, "&ikey:item.cucumbery.enchanted_redstone_block|마법이 부여된 레드스톤 블록", Rarity.EPIC),
  ENCHANTED_RED_MUSHROOM(Material.RED_MUSHROOM, "&ikey:item.cucumbery.enchanted_red_mushroom|마법이 부여된 빨간색 버섯", Rarity.RARE),
  ENCHANTED_RED_MUSHROOM_BLOCK(Material.RED_MUSHROOM_BLOCK, "&ikey:item.cucumbery.enchanted_red_mushroom_block|마법이 부여된 빨간색 버섯 블록", Rarity.EPIC),
  ENCHANTED_RED_SAND(Material.RED_SAND, "&ikey:item.cucumbery.enchanted_red_sand|마법이 부여된 붉은 모래", Rarity.RARE),
  ENCHANTED_ROTTEN_FLESH(Material.ROTTEN_FLESH, "&ikey:item.cucumbery.enchanted_rotten_flesh|마법이 부여된 썩은 살점", Rarity.RARE),
  ENCHANTED_SALMON(Material.SALMON, "&ikey:item.cucumbery.enchanted_salmon|마법이 부여된 연어", Rarity.RARE),
  ENCHANTED_SAND(Material.SAND, "&ikey:item.cucumbery.enchanted_sand|마법이 부여된 모래", Rarity.RARE),
  ENCHANTED_SCUTE(Material.SCUTE, "&ikey:item.cucumbery.enchanted_scute|마법이 부여된 인갑", Rarity.UNIQUE),
  ENCHANTED_SLIME_BALL(Material.SLIME_BALL, "&ikey:item.cucumbery.enchanted_slime_ball|마법이 부여된 슴라임볼", Rarity.RARE),
  ENCHANTED_SNOWBALL(Material.SNOWBALL, "&ikey:item.cucumbery.enchanted_snowball|마법이 부여된 눈덩이", Rarity.RARE),
  ENCHANTED_SNOW_BLOCK(Material.SNOW_BLOCK, "&ikey:item.cucumbery.enchanted_snow_block|마법이 부여된 눈 블록", Rarity.EPIC),
  ENCHANTED_SPIDER_EYE(Material.SPIDER_EYE, "&ikey:item.cucumbery.enchanted_spider_eye|마법이 부여된 거미 눈", Rarity.RARE),
  ENCHANTED_SPONGE(Material.SPONGE, "&ikey:item.cucumbery.enchanted_sponge|마법이 부여된 스펀지", Rarity.ELITE),
  ENCHANTED_SPRUCE_LOG(Material.SPRUCE_LOG, "&ikey:item.cucumbery.enchanted_spruce_log|마법이 부여된 가문비나무 원목", Rarity.RARE),
  ENCHANTED_STRING(Material.STRING, "&ikey:item.cucumbery.enchanted_string|마법이 부여된 실", Rarity.RARE),
  ENCHANTED_SUGAR(Material.SUGAR, "&ikey:item.cucumbery.enchanted_sugar|마법이 부여된 설탕", Rarity.EPIC),
  ENCHANTED_SUGAR_CANE(Material.SUGAR_CANE, "&ikey:item.cucumbery.enchanted_sugar_cane|마법이 부여된 사탕수수", Rarity.RARE),
  ENCHANTED_SWEET_BERRIES(Material.SWEET_BERRIES, "&ikey:item.cucumbery.enchanted_sweet_berries|마법이 부여된 달콤한 열매", Rarity.RARE),
  ENCHANTED_TITANIUM(Material.PLAYER_HEAD, "&ikey:item.cucumbery.enchanted_titanium|마법이 부여된 티타늄", Rarity.EXCELLENT),
  ENCHANTED_TROPICAL_FISH(Material.TROPICAL_FISH, "&ikey:item.cucumbery.enchanted_tropical_fish|마법이 부여된 열대어", Rarity.RARE),
  ENCHANTED_TURTLE_EGG(Material.TURTLE_EGG, "&ikey:item.cucumbery.enchanted_turtle_egg|마법이 부여된 거북 알", Rarity.EPIC),
  ENCHANTED_VERDANT_FROGLIGHT(Material.VERDANT_FROGLIGHT, "&ikey:item.cucumbery.enchanted_verdant_froglight|마법이 부여된 잔딧빛 개구리불", Rarity.EXCELLENT),
  ENCHANTED_WARPED_FUNGUS(Material.WARPED_FUNGUS, "&ikey:item.cucumbery.enchanted_warped_fungus|마법이 부여된 뒤틀린 균", Rarity.RARE),
  ENCHANTED_WARPED_STEM(Material.WARPED_STEM, "&ikey:item.cucumbery.enchanted_warped_stem|마법이 부여된 뒤틀린 자루", Rarity.RARE),
  ENCHANTED_WHEAT(Material.WHEAT, "&ikey:item.cucumbery.enchanted_wheat|마법이 부여된 밀", Rarity.RARE),
  EXPERIENCE_BOTTLE_COLOSSAL(Material.EXPERIENCE_BOTTLE, "&Rkey:item.cucumbery.experience_bottle_colossal|위대한 경험치 병", Rarity.EXCELLENT, CreativeCategory.TOOLS),
  EXPERIENCE_BOTTLE_GRAND(Material.EXPERIENCE_BOTTLE, "&Rkey:item.cucumbery.experience_bottle_grand|대단한 경험치 병", Rarity.ELITE, CreativeCategory.TOOLS),
  EXPERIENCE_BOTTLE_TITANIC(Material.EXPERIENCE_BOTTLE, "&Rkey:item.cucumbery.experience_bottle_titanic|엄청난 경험치 병", Rarity.UNIQUE, CreativeCategory.TOOLS),
  FIREWORK_ROCKET_CHAIN(Material.FIREWORK_ROCKET, "&ikey:item.cucumbery.firework_rocket_chain|연쇄형 폭죽", Rarity.EPIC),
  FIREWORK_ROCKET_EXPLOSIVE(Material.FIREWORK_ROCKET, "&ikey:item.cucumbery.firework_rocket_explosive|폭발성 폭죽 기본형", Rarity.EPIC),
  FIREWORK_ROCKET_EXPLOSIVE_DESTRUCTION(Material.FIREWORK_ROCKET, "&ikey:item.cucumbery.firework_rocket_explosive_destruction|폭발성 폭죽 파괴형", Rarity.EPIC),
  FIREWORK_ROCKET_EXPLOSIVE_FLAME(Material.FIREWORK_ROCKET, "&ikey:item.cucumbery.firework_rocket_explosive_flame|폭발성 폭죽 발화형", Rarity.EPIC),
  FIREWORK_ROCKET_REPEATING(Material.FIREWORK_ROCKET, "&ikey:item.cucumbery.firework_rocket_repeating|반복형 폭죽", Rarity.EPIC),
  FLINT_SHOVEL(Material.IRON_SHOVEL, "&ikey:item.cucumbery.flint_shovel|부싯돌 삽", Rarity.RARE, CreativeCategory.TOOLS),
  FROG_BOOTS(Material.LEATHER_BOOTS, "&2key:item.cucumbery.frog_boots|개구리 신발", Rarity.RARE, CreativeCategory.COMBAT),
  FROG_CHESTPLATE(Material.LEATHER_CHESTPLATE, "&2key:item.cucumbery.frog_chestplate|개구리 옷", Rarity.RARE, CreativeCategory.COMBAT),
  FROG_HELMET(Material.PLAYER_HEAD, "&2key:item.cucumbery.frog_helmet|개구리 모자", Rarity.RARE, CreativeCategory.COMBAT),
  FROG_LEGGINGS(Material.LEATHER_LEGGINGS, "&2key:item.cucumbery.frog_leggings|개구리 바지", Rarity.RARE, CreativeCategory.COMBAT),
  IQ_CHOOK_CHUCK(Material.REDSTONE_BLOCK, "&ikey:item.cucumbery.iq_chook_chuck|iq 축척기", Rarity.RARE),
  JADE(Material.LIME_DYE, "&ikey:item.cucumbery.jade|옥 원석", Rarity.UNIQUE),
  LARGE_DRILL_FUEL(Material.COAL, "&ikey:item.cucumbery.large_drill_fuel|대형 드릴 연료", Rarity.UNIQUE, "key:itemGroup.drill_fuel|드릴 연료"),
  LEAD_AXE(Material.STONE_AXE, "&ikey:item.cucumbery.lead_axe|납 도끼", CreativeCategory.TOOLS),
  LEAD_HOE(Material.STONE_HOE, "&ikey:item.cucumbery.lead_hoe|납 괭이", CreativeCategory.TOOLS),
  LEAD_INGOT(Material.IRON_INGOT, "&ikey:item.cucumbery.lead_ingot|납 주괴"),
  LEAD_ORE(Material.GRAY_DYE, "&ikey:item.cucumbery.lead_ore|납 원석"),
  LEAD_PICKAXE(Material.STONE_PICKAXE, "&ikey:item.cucumbery.lead_pickaxe|납 곡괭이", CreativeCategory.TOOLS),
  LEAD_SHOVEL(Material.STONE_SHOVEL, "&ikey:item.cucumbery.lead_shovel|납 삽", CreativeCategory.TOOLS),
  LEAD_SWORD(Material.STONE_SWORD, "&ikey:item.cucumbery.lead_sword|납 검", CreativeCategory.COMBAT),
  MEDIUM_DRILL_FUEL(Material.COAL, "&ikey:item.cucumbery.medium_drill_fuel|중형 드릴 연료", Rarity.RARE, "key:itemGroup.drill_fuel|드릴 연료"),
  MINDAS_BOOTS(Material.GOLDEN_BOOTS, "&Rkey:item.cucumbery.mindas_boots|마인더스의 신발", Rarity.LEGENDARY, CreativeCategory.COMBAT),
  MINDAS_CHESTPLATE(Material.GOLDEN_CHESTPLATE, "&Rkey:item.cucumbery.mindas_chestplate|마인더스의 상의", Rarity.LEGENDARY, CreativeCategory.COMBAT),
  MINDAS_DRILL(Material.PRISMARINE_SHARD, "&ikey:item.cucumbery.mindas_drill|마인더스의 드릴", Rarity.LEGENDARY, CreativeCategory.TOOLS),
  MINDAS_HELMET(Material.PLAYER_HEAD, "&Rkey:item.cucumbery.mindas_helmet|마인더스의 헬멧", Rarity.LEGENDARY, CreativeCategory.COMBAT),
  MINDAS_LEGGINGS(Material.GOLDEN_LEGGINGS, "&Rkey:item.cucumbery.mindas_leggings|마인더스의 하의", Rarity.LEGENDARY, CreativeCategory.COMBAT),
  MINER_BOOTS(Material.LEATHER_BOOTS, "&ikey:item.cucumbery.miner_boots|광부 신발", Rarity.RARE, CreativeCategory.COMBAT),
  MINER_CHESTPLATE(Material.LEATHER_CHESTPLATE, "&ikey:item.cucumbery.miner_chestplate|광부 상의", Rarity.RARE, CreativeCategory.COMBAT),
  MINER_HELMET(Material.LEATHER_HELMET, "&ikey:item.cucumbery.miner_helmet|광부 헬멧", Rarity.RARE, CreativeCategory.COMBAT),
  MINER_LEGGINGS(Material.LEATHER_LEGGINGS, "&ikey:item.cucumbery.miner_leggings|광부 하의", Rarity.RARE, CreativeCategory.COMBAT),
  MITHRIL_AXE(Material.DIAMOND_AXE, "&ikey:item.cucumbery.mithril_axe|미스릴 도끼", Rarity.UNIQUE, CreativeCategory.TOOLS),
  MITHRIL_BOOTS(Material.LEATHER_BOOTS, "&ikey:item.cucumbery.mithril_boots|미스릴 부츠", Rarity.UNIQUE, CreativeCategory.COMBAT),
  MITHRIL_CHESTPLATE(Material.LEATHER_CHESTPLATE, "&ikey:item.cucumbery.mithril_chestplate|미스릴 흉갑", Rarity.UNIQUE, CreativeCategory.COMBAT),
  MITHRIL_HELMET(Material.LEATHER_HELMET, "&ikey:item.cucumbery.mithril_helmet|미스릴 투구", Rarity.UNIQUE, CreativeCategory.COMBAT),
  MITHRIL_HOE(Material.DIAMOND_HOE, "&ikey:item.cucumbery.mithril_hoe|미스릴 괭이", Rarity.UNIQUE, CreativeCategory.TOOLS),
  MITHRIL_INGOT(Material.IRON_INGOT, "&ikey:item.cucumbery.mithril_ingot|미스릴 주괴", Rarity.UNIQUE),
  MITHRIL_LEGGINGS(Material.LEATHER_LEGGINGS, "&ikey:item.cucumbery.mithril_leggings|미스릴 레깅스", Rarity.UNIQUE, CreativeCategory.COMBAT),
  MITHRIL_ORE(Material.PRISMARINE_CRYSTALS, "&ikey:item.cucumbery.mithril_ore|미스릴 원석", Rarity.UNIQUE),
  MITHRIL_PICKAXE(Material.DIAMOND_PICKAXE, "&ikey:item.cucumbery.mithril_pickaxe|미스릴 곡괭이", Rarity.UNIQUE, CreativeCategory.TOOLS),
  MITHRIL_PICKAXE_REFINED(Material.DIAMOND_PICKAXE, "&ikey:item.cucumbery.mithril_pickaxe_refined|정제된 미스릴 곡괭이", Rarity.UNIQUE, CreativeCategory.TOOLS),
  MITHRIL_REFINED(Material.PLAYER_HEAD, "&ikey:item.cucumbery.mithril_refined|정제된 미스릴", Rarity.LEGENDARY),
  MITHRIL_SHOVEL(Material.DIAMOND_SHOVEL, "&ikey:item.cucumbery.mithril_shovel|미스릴 삽", Rarity.UNIQUE, CreativeCategory.TOOLS),
  MITHRIL_SWORD(Material.DIAMOND_SWORD, "&ikey:item.cucumbery.mithril_sword|미스릴 검", Rarity.UNIQUE, CreativeCategory.TOOLS),
  MORGANITE(Material.PINK_DYE, "&ikey:item.cucumbery.morganite|홍주석 원석", Rarity.UNIQUE),
  MUSHROOM_STEW_PICKAXE(Material.MUSHROOM_STEW, "&ikey:item.cucumbery.mushroom_stew_pickaxe|스튜 곡괭이?", Rarity.ARTIFACT, CreativeCategory.TOOLS),
  NAUTILITE_AXE(Material.IRON_AXE, "&ikey:item.cucumbery.nautilite_axe|노틸라이트 도끼", Rarity.UNIQUE, CreativeCategory.TOOLS),
  NAUTILITE_BOOTS(Material.NETHERITE_BOOTS, "&ikey:item.cucumbery.nautilite_boots|노틸라이트 부츠", Rarity.UNIQUE, CreativeCategory.COMBAT),
  NAUTILITE_CHESTPLATE(Material.NETHERITE_CHESTPLATE, "&ikey:item.cucumbery.nautilite_chestplate|노틸라이트 흉갑", Rarity.UNIQUE, CreativeCategory.COMBAT),
  NAUTILITE_HELMET(Material.NETHERITE_HELMET, "&ikey:item.cucumbery.nautilite_helmet|노틸라이트 투구", Rarity.UNIQUE, CreativeCategory.COMBAT),
  NAUTILITE_HOE(Material.IRON_HOE, "&ikey:item.cucumbery.nautilite_hoe|노틸라이트 괭이", Rarity.UNIQUE, CreativeCategory.TOOLS),
  NAUTILITE_INGOT(Material.IRON_INGOT, "&ikey:item.cucumbery.nautilite_ingot|노틸라이트 주괴", Rarity.EXCELLENT),
  NAUTILITE_LEGGINGS(Material.NETHERITE_LEGGINGS, "&ikey:item.cucumbery.nautilite_leggings|노틸라이트 레깅스", Rarity.UNIQUE, CreativeCategory.COMBAT),
  NAUTILITE_ORE(Material.WHITE_DYE, "&ikey:item.cucumbery.nautilite_ore|노틸라이트 원석", Rarity.EXCELLENT),
  NAUTILITE_PICKAXE(Material.IRON_PICKAXE, "&ikey:item.cucumbery.nautilite_pickaxe|노틸라이트 곡괭이", Rarity.UNIQUE, CreativeCategory.TOOLS),
  NAUTILITE_SHOVEL(Material.IRON_SHOVEL, "&ikey:item.cucumbery.nautilite_shovel|노틸라이트 삽", Rarity.UNIQUE, CreativeCategory.TOOLS),
  NAUTILITE_SWORD(Material.IRON_SWORD, "&ikey:item.cucumbery.nautilite_sword|노틸라이트 검", Rarity.UNIQUE, CreativeCategory.COMBAT),
  NETHERITE_BLOCK_DECORATIVE(Material.NETHERITE_BLOCK, "&ikey:item.cucumbery.netherite_block_decorative|장식용 네더라이트 블록", Rarity.NORMAL, CreativeCategory.BUILDING_BLOCKS),
  NETHERITE_CHESTPLATE_WITH_ELYTRA(Material.ELYTRA, "&ikey:item.cucumbery.netherite_chestplate_with_elytra|네더라이트 겉날개", Rarity.EXCELLENT, CreativeCategory.TOOLS),
  ONYX(Material.COAL, "&ikey:item.cucumbery.onyx|석탄", Rarity.EPIC),
  OPAL(Material.WHITE_DYE, "&ikey:item.cucumbery.opal|오팔", Rarity.EPIC),
  PLASTIC_AXE(Material.DIAMOND_AXE, "&ikey:item.cucumbery.plastic_axe|플라스틱 도끼", CreativeCategory.TOOLS),
  PLASTIC_DEBRIS(Material.LIGHT_BLUE_DYE, "&ikey:item.cucumbery.plastic_debris|플라스틱 파편", Rarity.RARE),

  PLASTIC_HOE(Material.DIAMOND_HOE, "&ikey:item.cucumbery.plastic_hoe|플라스틱 괭이", CreativeCategory.TOOLS),

  PLASTIC_MATERIAL(Material.LAPIS_LAZULI, "&ikey:item.cucumbery.plastic_material|플라스틱", Rarity.RARE),
  PLASTIC_PICKAXE(Material.DIAMOND_PICKAXE, "&ikey:item.cucumbery.plastic_pickaxe|플라스틱 곡괭이", CreativeCategory.TOOLS),
  PLASTIC_SHOVEL(Material.DIAMOND_SHOVEL, "&ikey:item.cucumbery.plastic_shovel|플라스틱 삽", CreativeCategory.TOOLS),
  PLASTIC_SWORD(Material.DIAMOND_SWORD, "&ikey:item.cucumbery.plastic_sword|플라스틱 검", CreativeCategory.COMBAT),
  PLATINUM_AXE(Material.GOLDEN_AXE, "&ikey:item.cucumbery.platinum_axe|백금 도끼", Rarity.RARE, CreativeCategory.TOOLS),
  PLATINUM_HOE(Material.GOLDEN_HOE, "&ikey:item.cucumbery.platinum_hoe|백금 괭이", Rarity.RARE, CreativeCategory.TOOLS),
  PLATINUM_INGOT(Material.GOLD_INGOT, "&ikey:item.cucumbery.platinum_ingot|백금 주괴", Rarity.RARE),
  PLATINUM_ORE(Material.YELLOW_DYE, "&ikey:item.cucumbery.platinum_ore|백금 원석", Rarity.RARE),
  PLATINUM_PICKAXE(Material.GOLDEN_PICKAXE, "&ikey:item.cucumbery.platinum_pickaxe|백금 곡괭이", Rarity.RARE, CreativeCategory.TOOLS),
  PLATINUM_SHOVEL(Material.GOLDEN_SHOVEL, "&ikey:item.cucumbery.platinum_shovel|백금 삽", Rarity.RARE, CreativeCategory.TOOLS),
  PLATINUM_SWORD(Material.GOLDEN_SWORD, "&ikey:item.cucumbery.platinum_sword|백금 검", Rarity.RARE, CreativeCategory.COMBAT),
  PORTABLE_CRAFTING_TABLE(Material.PLAYER_HEAD, "&ikey:item.cucumbery.portable_crafting_table|휴대용 작업대", Rarity.RARE, CreativeCategory.TOOLS),
  PORTABLE_ENDER_CHEST(Material.PLAYER_HEAD, "&ikey:item.cucumbery.portable_ender_chest|휴대용 엔더 상자", Rarity.UNIQUE, CreativeCategory.TOOLS),
  RAINBOW_BOOTS(Material.IRON_BOOTS, "&ikey:item.cucumbery.rainbow_boots|무지개 부츠", Rarity.RARE, CreativeCategory.COMBAT),
  RAINBOW_CHESTPLATE(Material.IRON_CHESTPLATE, "&ikey:item.cucumbery.rainbow_chestplate|무지개 흉갑", Rarity.RARE, CreativeCategory.COMBAT),
  RAINBOW_HELMET(Material.IRON_HELMET, "&ikey:item.cucumbery.rainbow_helmet|무지개 투구", Rarity.RARE, CreativeCategory.COMBAT),
  RAINBOW_LEGGINGS(Material.IRON_LEGGINGS, "&ikey:item.cucumbery.rainbow_leggings|무지개 레깅스", Rarity.RARE, CreativeCategory.COMBAT),
  REDSTONE_BLOCK_INSTA_BREAK(Material.REDSTONE_BLOCK, "&ikey:block.cucumbery.redstone_block_insta_break|즉시 부서지는 레드스톤 블록", Rarity.RARE, CreativeCategory.REDSTONE),
  RUBY(Material.RED_DYE, "&ikey:item.cucumbery.ruby|루비 원석", Rarity.UNIQUE),
  RUNE_DESTRUCTION(Material.TNT_MINECART, "&ikey:item.cucumbery.rune_destruction|파멸의 룬"),
  RUNE_EARTHQUAKE(Material.TNT_MINECART, "&ikey:item.cucumbery.rune_earthquake|지진의 룬"),
  SANS_BOOTS(Material.LEATHER_BOOTS, "&7key:item.cucumbery.sans_boots|샌즈 부츠", Rarity.RARE, CreativeCategory.COMBAT),
  SANS_CHESTPLATE(Material.LEATHER_CHESTPLATE, "&7key:item.cucumbery.sans_chestplate|샌즈 흉갑", Rarity.RARE, CreativeCategory.COMBAT),
  SANS_HELMET(Material.LEATHER_HELMET, "&7key:item.cucumbery.sans_helmet|샌즈 투구", Rarity.RARE, CreativeCategory.COMBAT),
  SANS_LEGGINGS(Material.LEATHER_LEGGINGS, "&7key:item.cucumbery.sans_leggings|샌즈 각반", Rarity.RARE, CreativeCategory.COMBAT),
  SAPPHIRE(Material.BLUE_DYE, "&ikey:item.cucumbery.sapphire|사파이어", Rarity.UNIQUE),

  SHROOMITE_INGOT(Material.NETHER_BRICK, "&ikey:item.cucumbery.shroomite_ingot|쉬루마이트 주괴"),
  SHROOMITE_ORE(Material.RED_DYE, "&ikey:item.cucumbery.shroomite_ore|쉬루마이트 원석"),

  SMALL_DRILL_FUEL(Material.COAL, "&ikey:item.cucumbery.small_drill_fuel|소형 드릴 연료", Rarity.NORMAL, "key:itemGroup.drill_fuel|드릴 연료"),

  SMALL_MINING_SACK(Material.PLAYER_HEAD, "&ikey:item.cucumbery.small_mining_sack|소형 광물 가방", Rarity.RARE, "key:itemGroup.sack가방"),

  SNOWBALL_AI_VO(Material.SNOWBALL, "&ikey:item.cucumbery.snowball_ai_vo|위대한 센잿 눈덩이", Rarity.RARE),

  SNOWBALL_GGUMONG(Material.SNOWBALL, "&ikey:item.cucumbery.snowball_ggumong|꾸쏴쒸쓰와씌쏴수 눈덩이", Rarity.RARE),
  SPIDER_BOOTS(Material.IRON_BOOTS, "&ikey:item.cucumbery.spider_boots|거미 부츠", Rarity.RARE, CreativeCategory.COMBAT),

  SPYGLASS_TELEPORT(Material.SPYGLASS, "&ikey:item.cucumbery.spyglass_teleport|텔레포트 망원경", Rarity.ELITE, "key:itemGroup.magical_tools|마법 도구"),
  STONK(Material.GOLDEN_PICKAXE, "&ikey:item.cucumbery.stonk|채굴기", Rarity.EPIC, CreativeCategory.TOOLS),
  TEST_PICKAXE(Material.IRON_PICKAXE, "&ikey:item.cucumbery.test_pickaxe|테스트 곡괭이", Rarity._ADMIN, CreativeCategory.TOOLS),
  THE_MUSIC(Material.MUSIC_DISC_5, "&ikey:item.cucumbery.the_music|'그' 노래", Rarity.EPIC),
  TIN_AXE(Material.GOLDEN_AXE, "&ikey:item.cucumbery.tin_axe|주석 도끼", CreativeCategory.TOOLS),
  TIN_HOE(Material.GOLDEN_HOE, "&ikey:item.cucumbery.tin_hoe|주석 괭이", CreativeCategory.TOOLS),
  TIN_INGOT(Material.COPPER_INGOT, "&ikey:item.cucumbery.tin_ingot|주석 주괴"),
  TIN_ORE(Material.LIGHT_GRAY_DYE, "&ikey:item.cucumbery.tin_ore|주석 원석"),
  TIN_PICKAXE(Material.GOLDEN_PICKAXE, "&ikey:item.cucumbery.tin_pickaxe|주석 곡괭이", CreativeCategory.TOOLS),
  TIN_SHOVEL(Material.GOLDEN_SHOVEL, "&ikey:item.cucumbery.tin_shovel|주석 삽", CreativeCategory.TOOLS),
  TIN_SWORD(Material.GOLDEN_SWORD, "&ikey:item.cucumbery.tin_sword|주석 검", CreativeCategory.COMBAT),
  TITANIUM_AXE(Material.IRON_AXE, "&ikey:item.cucumbery.titanium_axe|티타늄 도끼", Rarity.UNIQUE, CreativeCategory.TOOLS),
  TITANIUM_BOOTS(Material.LEATHER_BOOTS, "&ikey:item.cucumbery.titanium_boots|티타늄 부츠", Rarity.UNIQUE, CreativeCategory.COMBAT),
  TITANIUM_CHESTPLATE(Material.LEATHER_CHESTPLATE, "&ikey:item.cucumbery.titanium_chestplate|티타늄 흉갑", Rarity.UNIQUE, CreativeCategory.COMBAT),
  TITANIUM_DRILL_R266(Material.PRISMARINE_SHARD, "&ikey:item.cucumbery.titanium_drill_r266|티타늄 드릴 r266", Rarity.EXCELLENT, CreativeCategory.TOOLS),
  TITANIUM_DRILL_R366(Material.PRISMARINE_SHARD, "&ikey:item.cucumbery.titanium_drill_r366|티타늄 드릴 r366", Rarity.EXCELLENT, CreativeCategory.TOOLS),
  TITANIUM_DRILL_R466(Material.PRISMARINE_SHARD, "&ikey:item.cucumbery.titanium_drill_r466|티타늄 드릴 r466", Rarity.EXCELLENT, CreativeCategory.TOOLS),
  TITANIUM_DRILL_R566(Material.PRISMARINE_SHARD, "&ikey:item.cucumbery.titanium_drill_r566|티타늄 드릴 r566", Rarity.LEGENDARY, CreativeCategory.TOOLS),
  TITANIUM_HELMET(Material.LEATHER_HELMET, "&ikey:item.cucumbery.titanium_helmet|티타늄 투구", Rarity.UNIQUE, CreativeCategory.COMBAT),
  TITANIUM_HOE(Material.IRON_HOE, "&ikey:item.cucumbery.titanium_hoe|티타늄 괭이", Rarity.UNIQUE, CreativeCategory.TOOLS),
  TITANIUM_INGOT(Material.IRON_INGOT, "&ikey:item.cucumbery.titanium_ingot|티타늄 주괴", Rarity.UNIQUE),

  TITANIUM_LEGGINGS(Material.LEATHER_LEGGINGS, "&ikey:item.cucumbery.titanium_leggings|티타늄 레깅스", Rarity.UNIQUE, CreativeCategory.COMBAT),
  TITANIUM_ORE(Material.QUARTZ, "&ikey:item.cucumbery.titanium_ore|티타늄 원석", Rarity.UNIQUE),
  TITANIUM_PICKAXE(Material.IRON_PICKAXE, "&ikey:item.cucumbery.titanium_pickaxe|티타늄 곡괭이", Rarity.UNIQUE, CreativeCategory.TOOLS),
  TITANIUM_PICKAXE_REFINED(Material.IRON_PICKAXE, "&ikey:item.cucumbery.titanium_pickaxe_refined|정제된 티타늄 곡괭이", Rarity.UNIQUE, CreativeCategory.TOOLS),
  TITANIUM_REFINED(Material.PLAYER_HEAD, "&ikey:item.cucumbery.titanium_refined|정제된 티타늄", Rarity.LEGENDARY),
  TITANIUM_SHOVEL(Material.IRON_SHOVEL, "&ikey:item.cucumbery.titanium_shovel|티타늄 삽", Rarity.UNIQUE, CreativeCategory.TOOLS),

  TITANIUM_SWORD(Material.IRON_SWORD, "&ikey:item.cucumbery.titanium_sword|티타늄 검", Rarity.UNIQUE, CreativeCategory.TOOLS),
  TNT_COMBAT(Material.TNT, "&ikey:block.cucumbery.tnt_combat|전투용 TNT", Rarity.RARE, CreativeCategory.REDSTONE),
  TNT_DONUT(Material.TNT, "&ikey:block.cucumbery.tnt_donut|도넛 TNT", Rarity.EPIC, CreativeCategory.REDSTONE),
  TNT_DRAIN(Material.TNT, "&ikey:block.cucumbery.tnt_drain|탈수 TNT", Rarity.RARE, CreativeCategory.REDSTONE),
  TNT_I_WONT_LET_YOU_GO(Material.TNT, "&ikey:block.cucumbery.tnt_i_wont_let_you_go|워어 아워 래 유 TNT", Rarity.RARE, CreativeCategory.REDSTONE),
  TNT_SUPERIOR(Material.TNT, "&ikey:block.cucumbery.tnt_superior|강력한 TNT", Rarity.EPIC, CreativeCategory.REDSTONE),
  TODWOT_PICKAXE(Material.WOODEN_PICKAXE, "&ikey:item.cucumbery.todwot_pickaxe|섕쟀 곡괭이", Rarity.ARTIFACT, CreativeCategory.TOOLS),
  TOMATO(Material.APPLE, "&ikey:item.cucumbery.tomato|토마토", "itemGroup.foodAndDrink"),
  TOPAZ(Material.YELLOW_DYE, "&ikey:item.cucumbery.topaz|황옥 원석", Rarity.UNIQUE),

  TRACKER(Material.NAME_TAG, "&ikey:item.cucumbery.tracker|트래커", Rarity.ELITE, "key:itemGroup.magical_tools|마법 도구"),

  TUNGSTEN_AXE(Material.IRON_AXE, "&ikey:item.cucumbery.tungsten_axe|텅스텐 도끼", Rarity.RARE, CreativeCategory.TOOLS),

  TUNGSTEN_BOOTS(Material.LEATHER_BOOTS, "&ikey:item.cucumbery.tungsten_boots|텅스텐 부츠", Rarity.RARE, CreativeCategory.COMBAT),

  TUNGSTEN_CHESTPLATE(Material.LEATHER_CHESTPLATE, "&ikey:item.cucumbery.tungsten_chestplate|텅스텐 흉갑", Rarity.RARE, CreativeCategory.COMBAT),

  TUNGSTEN_HELMET(Material.LEATHER_HELMET, "&ikey:item.cucumbery.tungsten_helmet|텅스텐 투구", Rarity.RARE, CreativeCategory.COMBAT),

  TUNGSTEN_HOE(Material.IRON_HOE, "&ikey:item.cucumbery.tungsten_hoe|텅스텐 괭이", Rarity.RARE, CreativeCategory.TOOLS),

  TUNGSTEN_INGOT(Material.SLIME_BALL, "&ikey:item.cucumbery.tungsten_ingot|텅스텐 주괴", Rarity.RARE),

  TUNGSTEN_LEGGINGS(Material.LEATHER_LEGGINGS, "&ikey:item.cucumbery.tungsten_leggings|텅스텐 레깅스", Rarity.RARE, CreativeCategory.COMBAT),

  TUNGSTEN_ORE(Material.LIME_DYE, "&ikey:item.cucumbery.tungsten_ore|텅스텐 원석", Rarity.RARE),

  TUNGSTEN_PICKAXE(Material.IRON_PICKAXE, "&ikey:item.cucumbery.tungsten_pickaxe|텅스텐 곡괭이", Rarity.RARE, CreativeCategory.TOOLS),

  TUNGSTEN_SHOVEL(Material.IRON_SHOVEL, "&ikey:item.cucumbery.tungsten_shovel|텅스텐 삽", Rarity.RARE, CreativeCategory.TOOLS),
  TUNGSTEN_SWORD(Material.IRON_SWORD, "&ikey:item.cucumbery.tungsten_sword|텅스텐 검", Rarity.RARE, CreativeCategory.COMBAT),
  TURQUOISE(Material.LIGHT_BLUE_DYE, "&ikey:item.cucumbery.turquoise|터키석", Rarity.EPIC),
  UNBINDING_SHEARS(Material.SHEARS, "&ikey:item.cucumbery.unbinding_shears|해방의 가위", Rarity.EPIC, "key:itemGroup.magical_tools|마법 도구"),

  WEATHER_FORECAST(Material.ENDER_EYE, "&ikey:item.cucumbery.weather_forecast|날씨를 알려주는 눈", Rarity.ELITE, CreativeCategory.TOOLS),

  WNYNYA_ORE(Material.PLAYER_HEAD, "rgb200,100,255;key:block.cucumbery.wnynya_ore|완YEE 광석", Rarity.RARE, "itemGroup.natural"),
  SUS(Material.PLAYER_HEAD, "&ikey:block.cucumbery.sus|Amogus Gusensus sansus 광석", Rarity.EPIC, "itemGroup.natural"),
  ;

  private final Material displayMaterial;

  private final Rarity rarity;

  private final String category;

  private final Component displayName;

  CustomMaterial(Material displayMaterial, String displayName)
  {
    this(displayMaterial, displayName, Rarity.NORMAL, "itemGroup.ingredients");
  }

  CustomMaterial(Material displayMaterial, Component displayName)
  {
    this(displayMaterial, displayName, Rarity.NORMAL, "itemGroup.ingredients");
  }

  CustomMaterial(Material displayMaterial, String displayName, CreativeCategory category)
  {
    this(displayMaterial, displayName, Rarity.NORMAL, category.translationKey());
  }

  CustomMaterial(Material displayMaterial, String displayName, String category)
  {
    this(displayMaterial, displayName, Rarity.NORMAL, category);
  }

  CustomMaterial(Material displayMaterial, Component displayName, CreativeCategory category)
  {
    this(displayMaterial, displayName, Rarity.NORMAL, category.translationKey());
  }

  CustomMaterial(Material displayMaterial, Component displayName, String category)
  {
    this(displayMaterial, displayName, Rarity.NORMAL, category);
  }

  CustomMaterial(Material displayMaterial, String displayName, Rarity rarity)
  {
    this(displayMaterial, displayName, rarity, "itemGroup.ingredients");
  }

  @SuppressWarnings("unused")
  CustomMaterial(Material displayMaterial, Component displayName, Rarity rarity)
  {
    this(displayMaterial, displayName, rarity, "itemGroup.ingredients");
  }

  CustomMaterial(Material displayMaterial, String displayName, Rarity rarity, String category)
  {
    this(displayMaterial, ComponentUtil.translate(displayName.startsWith("&R") ? rarity.colorString() + displayName.substring(2) : displayName), rarity, category);
  }

  CustomMaterial(Material displayMaterial, String displayName, Rarity rarity, CreativeCategory category)
  {
    this(displayMaterial, ComponentUtil.translate(displayName.startsWith("&R") ? rarity.colorString() + displayName.substring(2) : displayName), rarity, category);
  }

  CustomMaterial(Material displayMaterial, Component displayName, Rarity rarity, CreativeCategory category)
  {
    this.displayMaterial = displayMaterial;
    this.displayName = displayName;
    this.rarity = rarity;
    this.category = category.translationKey();
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

  public boolean isDrill()
  {
    return switch (this)
    {
      case TITANIUM_DRILL_R266, TITANIUM_DRILL_R366, TITANIUM_DRILL_R466, TITANIUM_DRILL_R566, MINDAS_DRILL -> true;
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
  public Material getSmeltedItemVanilla()
  {
    return switch (this)
            {
              case WNYNYA_ORE -> Material.TNT;
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
