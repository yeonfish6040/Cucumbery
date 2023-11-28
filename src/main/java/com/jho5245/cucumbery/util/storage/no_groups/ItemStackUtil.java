package com.jho5245.cucumbery.util.storage.no_groups;

import com.destroystokyo.paper.profile.PlayerProfile;
import com.destroystokyo.paper.profile.ProfileProperty;
import com.jho5245.cucumbery.Cucumbery;
import com.jho5245.cucumbery.custom.customeffect.CustomEffectManager;
import com.jho5245.cucumbery.custom.customeffect.type.CustomEffectType;
import com.jho5245.cucumbery.util.itemlore.ItemLore;
import com.jho5245.cucumbery.util.itemlore.ItemLoreView;
import com.jho5245.cucumbery.util.nbt.CucumberyTag;
import com.jho5245.cucumbery.util.no_groups.MessageUtil;
import com.jho5245.cucumbery.util.no_groups.Method;
import com.jho5245.cucumbery.util.no_groups.Method2;
import com.jho5245.cucumbery.util.storage.component.util.ComponentUtil;
import com.jho5245.cucumbery.util.storage.component.util.ItemNameUtil;
import com.jho5245.cucumbery.util.storage.data.Constant;
import com.jho5245.cucumbery.util.storage.data.CustomMaterial;
import de.tr7zw.changeme.nbtapi.NBTCompound;
import de.tr7zw.changeme.nbtapi.NBTContainer;
import de.tr7zw.changeme.nbtapi.NBTItem;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TranslatableComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.format.TextDecoration.State;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.data.BlockData;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.*;
import org.bukkit.inventory.meta.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class ItemStackUtil
{
	/**
	 * 아이템의 모루 사용 횟수를 가져옵니다. 정상적인 횟수가 아닐 경우, -1을 반환합니다.
	 *
	 * @param itemMeta
	 * 		모루 사용 횟수를 가져올 아이템의 메타
	 * @return 아이템의 모루 사용 횟수
	 */
	public static int getAnvilUsedTime(@NotNull ItemMeta itemMeta)
	{
		if (itemMeta instanceof Repairable repairMeta)
		{
			int repairCost = repairMeta.getRepairCost();
			if (repairCost <= 0)
			{
				return 0;
			}
			repairCost++;
			int useTime = 0;
			while (repairCost / 2D != 1D)
			{
				repairCost /= 2;
				useTime++;
				if (useTime > 1000)
				{
					break;
				}
			}
			return useTime + 1;
		}
		return -1;
	}

	/**
	 * 설치할 수 있는 아이템인지 확인합니다.
	 *
	 * @param type
	 * 		확인할 아이템의 종류
	 * @return 설치할 수 있을 경우 true
	 */
	public static boolean isPlacable(@NotNull Material type)
	{
		return switch (type)
		{
			case REDSTONE, BEETROOT_SEEDS, MELON_SEEDS, PUMPKIN_SEEDS, WHEAT_SEEDS, COCOA_BEANS, ITEM_FRAME, PAINTING, ARMOR_STAND, STRING, END_CRYSTAL, SWEET_BERRIES, CARROT, POTATO ->
					true;
			case WHEAT -> false;
			default -> type.isBlock();
		};
	}

	/**
	 * 양조할 수 있는 아이템인지 확인합니다.
	 *
	 * @param type
	 * 		확인할 아이템의 종류
	 * @return 양조기에 사용할 수 있을 경우 true, 아닐 경우 false
	 */
	public static boolean isBrewable(@NotNull Material type)
	{
		return switch (type)
		{
			case REDSTONE, GLOWSTONE_DUST, GHAST_TEAR, NETHER_WART, GOLDEN_CARROT, BLAZE_POWDER, FERMENTED_SPIDER_EYE, GUNPOWDER, DRAGON_BREATH, SUGAR, RABBIT_FOOT, GLISTERING_MELON_SLICE, SPIDER_EYE, PUFFERFISH, MAGMA_CREAM, TURTLE_HELMET, PHANTOM_MEMBRANE ->
					true;
			default -> false;
		};
	}

	/**
	 * 먹을 수 있는 아이템인지 확인합니다.
	 *
	 * @param type
	 * 		확인할 아이템의 종류
	 * @return 배고픔에 영향을 주지 않아도, 먹을 수 있으면 true, 아닐 경우 false
	 */
	public static boolean isEdible(@NotNull Material type)
	{
		return type.isEdible() || type == Material.MILK_BUCKET || type == Material.POTION;
	}

	/**
	 * 해당 아이템을 먹으면 상태 효과에 영향을 미치는 지 확인합니다. {@link #isEdible(Material)} 이 false일 경우, false를 반환합니다.
	 *
	 * @param type
	 * 		확인할 아이템의 종류
	 * @return 상태 효과에 영향을 미치면 true, 아닐 경우 false
	 */
	public static boolean hasStatusEffect(@NotNull Material type)
	{
		return ItemStackUtil.isEdible(type) && (type == Material.GOLDEN_APPLE || type == Material.ENCHANTED_GOLDEN_APPLE || type == Material.POISONOUS_POTATO
				|| type == Material.SPIDER_EYE || type == Material.PUFFERFISH || type == Material.ROTTEN_FLESH || type == Material.CHICKEN
				|| type == Material.HONEY_BOTTLE || type == Material.MILK_BUCKET || type == Material.POTION);
	}

	public static int getFoodLevel(@NotNull Material type)
	{
		return switch (type)
		{
			case BEETROOT, DRIED_KELP, POTATO, PUFFERFISH, TROPICAL_FISH -> 1;
			case COOKIE, MELON_SLICE, POISONOUS_POTATO, CHICKEN, COD, MUTTON, SALMON, SPIDER_EYE, SWEET_BERRIES, GLOW_BERRIES -> 2;
			case CARROT, BEEF, PORKCHOP, RABBIT -> 3;
			case APPLE, CHORUS_FRUIT, ENCHANTED_GOLDEN_APPLE, GOLDEN_APPLE, ROTTEN_FLESH -> 4;
			case BAKED_POTATO, BREAD, COOKED_COD, COOKED_RABBIT -> 5;
			case BEETROOT_SOUP, COOKED_CHICKEN, COOKED_MUTTON, COOKED_SALMON, GOLDEN_CARROT, HONEY_BOTTLE, MUSHROOM_STEW, SUSPICIOUS_STEW -> 6;
			case COOKED_PORKCHOP, PUMPKIN_PIE, COOKED_BEEF -> 8;
			case RABBIT_STEW -> 10;
			case CAKE -> 14;
			default -> 0;
		};
	}

	public static double getSaturation(@NotNull Material type)
	{
		return switch (type)
		{
			case PUFFERFISH, TROPICAL_FISH -> 0.2;
			case COOKIE, COD, SALMON, SWEET_BERRIES, GLOW_BERRIES -> 0.4;
			case DRIED_KELP, POTATO -> 0.6;
			case ROTTEN_FLESH -> 0.8;
			case BEETROOT, MELON_SLICE, POISONOUS_POTATO, CHICKEN, MUTTON, HONEY_BOTTLE -> 1.2;
			case BEEF, PORKCHOP, RABBIT -> 1.8;
			case APPLE, CHORUS_FRUIT -> 2.4;
			case CAKE -> 2.8;
			case SPIDER_EYE -> 3.2;
			case CARROT -> 3.6;
			case PUMPKIN_PIE -> 4.8;
			case BAKED_POTATO, BREAD, COOKED_COD, COOKED_RABBIT -> 6;
			case BEETROOT_SOUP, COOKED_CHICKEN, MUSHROOM_STEW, SUSPICIOUS_STEW -> 7.2;
			case ENCHANTED_GOLDEN_APPLE, GOLDEN_APPLE, COOKED_MUTTON, COOKED_SALMON -> 9.6;
			case RABBIT_STEW -> 12;
			case COOKED_PORKCHOP, COOKED_BEEF -> 12.8;
			case GOLDEN_CARROT -> 14.4;
			default -> 0d;
		};
	}

	public static String getNourishment(int foodLevel, double saturation)
	{
		if (foodLevel == 0 || (foodLevel < 0 && saturation < 0))
		{
			return "#57B6F0;공허";
		}
		double ratio = saturation / foodLevel;
		if (ratio < 0.4)
		{
			return "#47B6F0;허-전";
		}
		else if (ratio < 1.0)
		{
			return "#16F06C;낮음";
		}
		else if (ratio < 1.5)
		{
			return "#F0CA4F;보통";
		}
		else if (ratio < 2.0)
		{
			return "#F05C48;높음";
		}
		else
		{
			return "#E553F0;든-든";
		}
	}

	@Nullable
	public static String getNourishment(@NotNull Material type)
	{
		if (!type.isEdible() && type != Material.CAKE && type != Material.MILK_BUCKET && type != Material.POTION)
		{
			return null;
		}
		return switch (type)
		{
			case GOLDEN_APPLE, ENCHANTED_GOLDEN_APPLE, GOLDEN_CARROT -> "#E553F0;든-든";
			case COOKED_MUTTON, COOKED_PORKCHOP, COOKED_SALMON, COOKED_BEEF -> "#F05C48;높음";
			case BAKED_POTATO, BEETROOT_SOUP, BEETROOT, BREAD, CARROT, COOKED_CHICKEN, COOKED_COD, COOKED_RABBIT, MUSHROOM_STEW, RABBIT_STEW, SUSPICIOUS_STEW ->
					"#F0CA4F;보통";
			case APPLE, CHORUS_FRUIT, DRIED_KELP, MELON_SLICE, POISONOUS_POTATO, POTATO, PUMPKIN_PIE, BEEF, PORKCHOP, MUTTON, CHICKEN, RABBIT -> "#16F06C;낮음";
			case CAKE, COOKIE, HONEY_BOTTLE, PUFFERFISH, COD, SALMON, ROTTEN_FLESH, SPIDER_EYE, SWEET_BERRIES, TROPICAL_FISH, GLOW_BERRIES -> "#47B6F0;허-전";
			default -> null;
		};
	}

	public static double getFuelTimeInSecond(@NotNull Material type)
	{
		return switch (type)
		{
			case BAMBOO, SCAFFOLDING -> 2.5;
			case BLACK_CARPET, BLUE_CARPET, BROWN_CARPET, CYAN_CARPET, GRAY_CARPET, GREEN_CARPET, LIGHT_BLUE_CARPET, LIGHT_GRAY_CARPET, LIME_CARPET, MAGENTA_CARPET, ORANGE_CARPET, PINK_CARPET, PURPLE_CARPET, RED_CARPET, WHITE_CARPET, YELLOW_CARPET ->
					3.35;
			case ACACIA_BUTTON, BIRCH_BUTTON, DARK_OAK_BUTTON, MANGROVE_BUTTON, JUNGLE_BUTTON, OAK_BUTTON, SPRUCE_BUTTON, CHERRY_BUTTON, BAMBOO_BUTTON, BOWL, ACACIA_SAPLING, BIRCH_SAPLING, DARK_OAK_SAPLING, MANGROVE_PROPAGULE, JUNGLE_SAPLING, OAK_SAPLING, SPRUCE_SAPLING, CHERRY_SAPLING, STICK, BLACK_WOOL, BLUE_WOOL, BROWN_WOOL, CYAN_WOOL, GRAY_WOOL, GREEN_WOOL, LIGHT_BLUE_WOOL, LIGHT_GRAY_WOOL, LIME_WOOL, MAGENTA_WOOL, ORANGE_WOOL, PINK_WOOL, PURPLE_WOOL, RED_WOOL, WHITE_WOOL, YELLOW_WOOL, AZALEA, FLOWERING_AZALEA ->
					5;
			case ACACIA_SLAB, BIRCH_SLAB, DARK_OAK_SLAB, MANGROVE_SLAB, JUNGLE_SLAB, OAK_SLAB, SPRUCE_SLAB, CHERRY_SLAB, BAMBOO_SLAB -> 7.5;
			case ACACIA_DOOR, BIRCH_DOOR, DARK_OAK_DOOR, MANGROVE_DOOR, JUNGLE_DOOR, OAK_DOOR, SPRUCE_DOOR, CHERRY_DOOR, BAMBOO_DOOR, ACACIA_SIGN, BIRCH_SIGN, DARK_OAK_SIGN, MANGROVE_SIGN, JUNGLE_SIGN, OAK_SIGN, SPRUCE_SIGN, CHERRY_SIGN, BAMBOO_SIGN, WOODEN_AXE, WOODEN_HOE, WOODEN_PICKAXE, WOODEN_SHOVEL, WOODEN_SWORD ->
					10;
			case ACACIA_LOG, BIRCH_LOG, DARK_OAK_LOG, MANGROVE_LOG, JUNGLE_LOG, OAK_LOG, SPRUCE_LOG, CHERRY_LOG, STRIPPED_ACACIA_LOG, STRIPPED_BIRCH_LOG, STRIPPED_DARK_OAK_LOG, STRIPPED_MANGROVE_LOG, STRIPPED_JUNGLE_LOG, STRIPPED_OAK_LOG, STRIPPED_SPRUCE_LOG, STRIPPED_CHERRY_LOG, ACACIA_WOOD, BIRCH_WOOD, DARK_OAK_WOOD, MANGROVE_WOOD, JUNGLE_WOOD, OAK_WOOD, SPRUCE_WOOD, CHERRY_WOOD, STRIPPED_ACACIA_WOOD, STRIPPED_BIRCH_WOOD, STRIPPED_DARK_OAK_WOOD, STRIPPED_MANGROVE_WOOD, STRIPPED_JUNGLE_WOOD, STRIPPED_OAK_WOOD, STRIPPED_SPRUCE_WOOD, STRIPPED_CHERRY_WOOD, ACACIA_PLANKS, BIRCH_PLANKS, DARK_OAK_PLANKS, MANGROVE_PLANKS, JUNGLE_PLANKS, OAK_PLANKS, SPRUCE_PLANKS, CHERRY_PLANKS, BAMBOO_PLANKS, ACACIA_STAIRS, BIRCH_STAIRS, DARK_OAK_STAIRS, MANGROVE_STAIRS, JUNGLE_STAIRS, OAK_STAIRS, SPRUCE_STAIRS, CHERRY_STAIRS, BAMBOO_STAIRS, ACACIA_PRESSURE_PLATE, BIRCH_PRESSURE_PLATE, DARK_OAK_PRESSURE_PLATE, MANGROVE_PRESSURE_PLATE, JUNGLE_PRESSURE_PLATE, OAK_PRESSURE_PLATE, SPRUCE_PRESSURE_PLATE, CHERRY_PRESSURE_PLATE, BAMBOO_PRESSURE_PLATE, ACACIA_TRAPDOOR, BIRCH_TRAPDOOR, DARK_OAK_TRAPDOOR, MANGROVE_TRAPDOOR, JUNGLE_TRAPDOOR, OAK_TRAPDOOR, SPRUCE_TRAPDOOR, CHERRY_TRAPDOOR, BAMBOO_TRAPDOOR, ACACIA_FENCE_GATE, BIRCH_FENCE_GATE, DARK_OAK_FENCE_GATE, MANGROVE_FENCE_GATE, JUNGLE_FENCE_GATE, OAK_FENCE_GATE, SPRUCE_FENCE_GATE, CHERRY_FENCE_GATE, BAMBOO_FENCE_GATE, ACACIA_FENCE, BIRCH_FENCE, DARK_OAK_FENCE, MANGROVE_FENCE, JUNGLE_FENCE, OAK_FENCE, SPRUCE_FENCE, CHERRY_FENCE, BAMBOO_FENCE, BAMBOO_BLOCK, BAMBOO_MOSAIC, STRIPPED_BAMBOO_BLOCK, BEEHIVE, BEE_NEST, LADDER, CRAFTING_TABLE, CARTOGRAPHY_TABLE, FLETCHING_TABLE, SMITHING_TABLE, LOOM, BOOKSHELF, LECTERN, COMPOSTER, CHEST, TRAPPED_CHEST, BARREL, DAYLIGHT_DETECTOR, JUKEBOX, NOTE_BLOCK, BROWN_MUSHROOM_BLOCK, RED_MUSHROOM_BLOCK, MUSHROOM_STEM, MANGROVE_ROOTS, BLACK_BANNER, BLUE_BANNER, BROWN_BANNER, CYAN_BANNER, GRAY_BANNER, GREEN_BANNER, LIGHT_BLUE_BANNER, LIGHT_GRAY_BANNER, LIME_BANNER, MAGENTA_BANNER, ORANGE_BANNER, PINK_BANNER, PURPLE_BANNER, RED_BANNER, WHITE_BANNER, YELLOW_BANNER, CROSSBOW, BOW, FISHING_ROD ->
					15;
			case ACACIA_BOAT, BIRCH_BOAT, DARK_OAK_BOAT, MANGROVE_BOAT, JUNGLE_BOAT, OAK_BOAT, SPRUCE_BOAT, CHERRY_BOAT, BAMBOO_RAFT, ACACIA_CHEST_BOAT, BIRCH_CHEST_BOAT, DARK_OAK_CHEST_BOAT, MANGROVE_CHEST_BOAT, JUNGLE_CHEST_BOAT, OAK_CHEST_BOAT, SPRUCE_CHEST_BOAT, CHERRY_CHEST_BOAT, BAMBOO_CHEST_RAFT ->
					60;
			case COAL, CHARCOAL -> 80;
			case BLAZE_ROD -> 120;
			case DRIED_KELP_BLOCK -> 200;
			case COAL_BLOCK -> 800;
			case LAVA_BUCKET -> 1000;
			default -> 0d;
		};
	}

	public static double getCompostChance(@NotNull Material type)
	{
		return switch (type)
		{
			case BEETROOT_SEEDS, DRIED_KELP, GLOW_BERRIES, GRASS, HANGING_ROOTS, MANGROVE_ROOTS, KELP, ACACIA_LEAVES, AZALEA_LEAVES, BIRCH_LEAVES, DARK_OAK_LEAVES, JUNGLE_LEAVES, MANGROVE_LEAVES, OAK_LEAVES, SPRUCE_LEAVES, MELON_SEEDS, MOSS_CARPET, PUMPKIN_SEEDS, ACACIA_SAPLING, BIRCH_SAPLING, SPRUCE_SAPLING, DARK_OAK_SAPLING, JUNGLE_SAPLING, MANGROVE_PROPAGULE, OAK_SAPLING, SEAGRASS, TALL_SEAGRASS, SMALL_DRIPLEAF, SWEET_BERRIES, WHEAT_SEEDS ->
					30d;
			case CACTUS, DRIED_KELP_BLOCK, FLOWERING_AZALEA_LEAVES, GLOW_LICHEN, MELON_SLICE, NETHER_SPROUTS, SUGAR_CANE, TALL_GRASS, VINE, WEEPING_VINES, TWISTING_VINES ->
					50d;
			case APPLE, AZALEA, BEETROOT, BIG_DRIPLEAF, CARROT, COCOA_BEANS, FERN, LARGE_FERN, DANDELION, POPPY, BLUE_ORCHID, ALLIUM, AZURE_BLUET, RED_TULIP, ORANGE_TULIP, WHITE_TULIP, PINK_TULIP, OXEYE_DAISY, CORNFLOWER, LILY_OF_THE_VALLEY, WITHER_ROSE, SUNFLOWER, LILAC, ROSE_BUSH, PEONY, LILY_PAD, MELON, MOSS_BLOCK, BROWN_MUSHROOM, RED_MUSHROOM, MUSHROOM_STEM, NETHER_WART, POTATO, PUMPKIN, CARVED_PUMPKIN, SEA_PICKLE, SHROOMLIGHT, SPORE_BLOSSOM, WHEAT, CRIMSON_FUNGUS, WARPED_FUNGUS, CRIMSON_ROOTS, WARPED_ROOTS ->
					65d;
			case BAKED_POTATO, BREAD, COOKIE, FLOWERING_AZALEA, HAY_BLOCK, BROWN_MUSHROOM_BLOCK, RED_MUSHROOM_BLOCK, NETHER_WART_BLOCK, WARPED_WART_BLOCK -> 85d;
			case PUMPKIN_PIE, CAKE -> 100d;
			default -> 0d;
		};
	}

	public static boolean isPickBlockable(@NotNull Material type)
	{
		return type.isBlock() || switch (type)
		{
			case POTATO, CARROT, COCOA_BEANS, WHEAT_SEEDS, NETHER_WART, MELON_SEEDS, PUMPKIN_SEEDS, REDSTONE, SWEET_BERRIES, ARMOR_STAND, ITEM_FRAME, PAINTING, BAT_SPAWN_EGG, BEE_SPAWN_EGG, BLAZE_SPAWN_EGG, CAT_SPAWN_EGG, CAVE_SPIDER_SPAWN_EGG, CHICKEN_SPAWN_EGG, COD_SPAWN_EGG, COW_SPAWN_EGG, CREEPER_SPAWN_EGG, DOLPHIN_SPAWN_EGG, DONKEY_SPAWN_EGG, DROWNED_SPAWN_EGG, ELDER_GUARDIAN_SPAWN_EGG, ENDERMAN_SPAWN_EGG, ENDERMITE_SPAWN_EGG, EVOKER_SPAWN_EGG, FOX_SPAWN_EGG, GHAST_SPAWN_EGG, GUARDIAN_SPAWN_EGG, HORSE_SPAWN_EGG, HUSK_SPAWN_EGG, LLAMA_SPAWN_EGG, MAGMA_CUBE_SPAWN_EGG, MOOSHROOM_SPAWN_EGG, MULE_SPAWN_EGG, OCELOT_SPAWN_EGG, PANDA_SPAWN_EGG, PARROT_SPAWN_EGG, PHANTOM_SPAWN_EGG, PIG_SPAWN_EGG, PILLAGER_SPAWN_EGG, POLAR_BEAR_SPAWN_EGG, PUFFERFISH_SPAWN_EGG, RABBIT_SPAWN_EGG, RAVAGER_SPAWN_EGG, SALMON_SPAWN_EGG, SHEEP_SPAWN_EGG, SHULKER_SPAWN_EGG, SILVERFISH_SPAWN_EGG, SKELETON_HORSE_SPAWN_EGG, SKELETON_SPAWN_EGG, SLIME_SPAWN_EGG, SPIDER_SPAWN_EGG, SQUID_SPAWN_EGG, STRAY_SPAWN_EGG, TRADER_LLAMA_SPAWN_EGG, TROPICAL_FISH_SPAWN_EGG, TURTLE_SPAWN_EGG, VEX_SPAWN_EGG, VILLAGER_SPAWN_EGG, VINDICATOR_SPAWN_EGG, WANDERING_TRADER_SPAWN_EGG, WITCH_SPAWN_EGG, WITHER_SKELETON_SPAWN_EGG, WOLF_SPAWN_EGG, ZOMBIE_HORSE_SPAWN_EGG, ZOMBIFIED_PIGLIN_SPAWN_EGG, ZOMBIE_SPAWN_EGG, ZOMBIE_VILLAGER_SPAWN_EGG, ACACIA_BOAT, BIRCH_BOAT, DARK_OAK_BOAT, JUNGLE_BOAT, OAK_BOAT, SPRUCE_BOAT, MINECART, CHEST_MINECART, COMMAND_BLOCK_MINECART, FURNACE_MINECART, HOPPER_MINECART, TNT_MINECART, LEAD, END_CRYSTAL, STRING, HOGLIN_SPAWN_EGG, PIGLIN_SPAWN_EGG, STRIDER_SPAWN_EGG, ZOGLIN_SPAWN_EGG, AXOLOTL_SPAWN_EGG, GLOW_SQUID_SPAWN_EGG, GOAT_SPAWN_EGG, PIGLIN_BRUTE_SPAWN_EGG, POWDER_SNOW_BUCKET, GLOW_BERRIES, ALLAY_SPAWN_EGG, FROG_SPAWN_EGG, TADPOLE_SPAWN_EGG, WARDEN_SPAWN_EGG ->
					true;
			default -> false;
		};
	}

	/**
	 * 해당하는 물질의 아이템을 설명을 붙여 반환합니다.
	 *
	 * @param type
	 * 		아이템의 물질
	 * @return 설명이 적힌 아이템
	 */
	@NotNull
	public static ItemStack loredItemStack(@NotNull Material type)
	{
		return loredItemStack(type, null);
	}

	/**
	 * 해당하는 물질의 아이템을 설명을 붙여 반환합니다.
	 *
	 * @param type
	 * 		아이템의 물질
	 * @return 설명이 적힌 아이템
	 */
	@NotNull
	public static ItemStack loredItemStack(@NotNull Material type, @Nullable Player player)
	{
		ItemStack item = new ItemStack(type);
		if (player == null || Method.usingLoreFeature(player))
		{
			ItemLore.setItemLore(item, player != null ? ItemLoreView.of(player) : null);
		}
		return item;
	}

	public static boolean itemExists(ItemStack item) // 아이템이 존재하는지 아닌지 판별
	{
		return (item != null && item.getType() != Material.AIR) && item.getAmount() > 0;
	}

	public static boolean hasItemMeta(ItemStack item)
	{
		return ItemStackUtil.itemExists(item) && !new ItemStack(item.getType(), item.getAmount()).equals(item);
	}

	@SuppressWarnings("unused")
	public static boolean hasItemMeta(ItemStack item, boolean exist)
	{
		if (exist)
		{
			return !new ItemStack(item.getType(), item.getAmount()).equals(item);
		}
		return ItemStackUtil.itemExists(item) && !new ItemStack(item.getType(), item.getAmount()).equals(item);
	}

	public static boolean hasDisplayName(ItemStack item) // 아이템이 이름을 가지고 있는지 판별
	{
		return ItemStackUtil.itemExists(item) && item.hasItemMeta() && item.getItemMeta().hasDisplayName();
	}

	@SuppressWarnings("unused")
	public static boolean hasDisplayName(ItemStack item, boolean exist) // 아이템이 이름을 가지고 있는지만 판별
	{
		if (exist)
		{
			return item.hasItemMeta() && item.getItemMeta().hasDisplayName();
		}
		return ItemStackUtil.itemExists(item) && item.hasItemMeta() && item.getItemMeta().hasDisplayName();
	}

	public static boolean hasLore(ItemStack item) // 아이템이 설명을 가지고 있는지 판별
	{
		return ItemStackUtil.itemExists(item) && item.hasItemMeta() && item.getItemMeta().hasLore() && item.getItemMeta().lore() != null && !Objects.requireNonNull(
				item.getItemMeta().lore()).isEmpty();
	}

	public static boolean hasLore(ItemStack item, boolean exist) // 아이템이 설명만 가지고 있는지 판별
	{
		if (exist)
		{
			return item.hasItemMeta() && item.getItemMeta().hasLore();
		}
		return ItemStackUtil.itemExists(item) && item.hasItemMeta() && item.getItemMeta().hasLore();
	}

	/**
	 * 두 아이템을 서로 비교하여 같으면 true를 반환합니다. 내구도는 무시할 수 있습니다.
	 *
	 * @param item1
	 * 		첫 번째 아이템
	 * @param item2
	 * 		두 번째 아이템
	 * @param ignoreDurability
	 * 		내구도 무시
	 * @return 만약 두 아이템이 완전히 값이 일치하면 true
	 */
	public static boolean itemEquals(@Nullable ItemStack item1, @Nullable ItemStack item2, boolean ignoreDurability)
	{
		if (!itemExists(item1) && !itemExists(item2))
		{
			return true;
		}
		if (!itemExists(item1) || !itemExists(item2))
		{
			return false;
		}
		item1 = item1.clone();
		item1.setAmount(1);
		item2 = item2.clone();
		item2.setAmount(1);
		if (ignoreDurability)
		{
			((Damageable) item2.getItemMeta()).setDamage(((Damageable) item1.getItemMeta()).getDamage());
		}
		return item1.equals(item2);
	}

	/**
	 * 두 아이템을 서로 비교하여 같으면 true를 반환합니다.
	 *
	 * @param item1
	 * 		첫 번째 아이템
	 * @param item2
	 * 		두 번째 아이템
	 * @return 만약 두 아이템이 완전히 값이 일치하면 true
	 */
	public static boolean itemEquals(@Nullable ItemStack item1, @Nullable ItemStack item2)
	{
		return itemEquals(item1, item2, false);
	}

	/**
	 * 주어진 인벤토리에 해당 아이템이 얼마나 있는지 반환합니다. 내구도는 무시할 수 있습니다.
	 *
	 * @param inv
	 * 		주어진 인벤토리
	 * @param item
	 * 		해당 아이템
	 * @param ignoreDurability
	 * 		내구도 무시
	 * @return 일치하는 아이템 개수를 반환합니다.
	 */
	public static int countItem(@NotNull Inventory inv, @NotNull ItemStack item, boolean ignoreDurability)
	{
		int amount = 0;
		for (ItemStack iStack : inv.getStorageContents())
		{
			if (itemEquals(item, iStack, ignoreDurability))
			{
				if (itemExists(iStack))
				{
					amount += iStack.getAmount();
				}
			}
		}
		return amount;
	}

	/**
	 * 주어진 인벤토리에 해당 아이템이 얼마나 있는지 반환합니다.
	 *
	 * @param inv
	 * 		주어진 인벤토리
	 * @param item
	 * 		해당 아이템
	 * @return 일치하는 아이템 개수를 반환합니다.
	 */
	public static int countItem(@NotNull Inventory inv, @NotNull ItemStack item)
	{
		return countItem(inv, item, false);
	}

	public static int countItem(@NotNull Inventory inv, @NotNull String predicate)
	{
		int amount = 0;
		for (ItemStack iStack : inv.getStorageContents())
		{
			if (iStack != null && ItemStackUtil.predicateItem(iStack, predicate))
			{
				if (itemExists(iStack))
				{
					amount += iStack.getAmount();
				}
			}
		}
		return amount;
	}

	/**
	 * 주어진 인벤토리에 해당 아이템이 (예외 처리 없이) 얼마나 들어갈 수 있는지 반환합니다. Method.itemEquals(item1, item2) 로 아이템이 같은지 확인합니다. 내구도는 무시할 수 있습니다.
	 *
	 * @param inv
	 * 		주어진 인벤토리
	 * @param item
	 * 		해당 아이템
	 * @param ignoreDurability
	 * 		내구도 무시
	 * @return 해당 아이템이 인벤토리에 얼마나 들어갈 수 있는지 개수를 반환합니다.
	 */
	public static int countSpace(@NotNull Inventory inv, @NotNull ItemStack item, boolean ignoreDurability)
	{
		int space = 0;
		ItemStack[] contents = inv.getStorageContents();
		for (ItemStack iStack : contents)
		{
			int maxStackSize = item.getMaxStackSize();
			if (!itemExists(iStack))
			{
				space += maxStackSize;
			}
			else if (itemEquals(item, iStack, ignoreDurability))
			{
				space += Math.max(0, maxStackSize - iStack.getAmount());
			}
		}
		if (space < 0)
		{
			space = 0;
		}
		return space;
	}

	/**
	 * 주어진 인벤토리에 해당 아이템이 (예외 처리 없이) 얼마나 들어갈 수 있는지 반환합니다. Method.itemEquals(item1, item2) 로 아이템이 같은지 확인합니다.
	 *
	 * @param inv
	 * 		주어진 인벤토리
	 * @param item
	 * 		해당 아이템
	 * @return 해당 아이템이 인벤토리에 얼마나 들어갈 수 있는지 개수를 반환합니다.
	 */
	public static int countSpace(@NotNull Inventory inv, @NotNull ItemStack item)
	{
		return countSpace(inv, item, false);
	}

	/**
	 * 플레이어의 인벤토리에 해당 아이템이 (예외 처리 없이) 얼마나 들어갈 수 있는지 반환합니다. Method.itemEquals(item1, item2) 로 아이템이 같은지 확인합니다.
	 *
	 * @param player
	 * 		비교할 플레이어
	 * @param itemStack
	 * 		아이템
	 * @return 해당 아이템이 인벤토리에 얼마나 들어갈 수 있는지 개수를 반환합니다.
	 */
	public static int countSpace(@NotNull Player player, @NotNull ItemStack itemStack)
	{
		return countSpace(player.getInventory(), ItemLore.setItemLore(itemStack.clone(), ItemLoreView.of(player)));
	}

	/**
	 * 목록에 있는 아이템들 중 화로에서 제련할 수 있는 아이템이 있으면 제련된 형태로 바꿔서 반환합니다.
	 *
	 * @param player
	 * 		제련하는 플레이어
	 * @param input
	 * 		교체할 아이템이 있는 목록
	 * @param expOutput
	 * 		각 아이템의 제련 경험치를 반환하기 위한 리스트
	 * @return 아이템이 교체된 배열
	 */
	public static List<ItemStack> getSmeltedResult(@Nullable Player player, @NotNull Collection<ItemStack> input, @Nullable List<Double> expOutput)
	{
		List<ItemStack> dropsClone = new ArrayList<>();
		for (ItemStack drop : input)
		{
			for (Recipe recipe : RecipeChecker.recipes)
			{
				if (recipe instanceof CookingRecipe<?> cookingRecipe)
				{
					RecipeChoice recipeChoice = cookingRecipe.getInputChoice();
					if (recipeChoice.test(drop))
					{
						drop.setType(cookingRecipe.getResult().getType());
						if (expOutput != null)
						{
							double exp = cookingRecipe.getExperience();
							if (player != null && CustomEffectManager.hasEffect(player, CustomEffectType.EXPERIENCE_BOOST))
							{
								int amplifier = CustomEffectManager.getEffect(player, CustomEffectType.EXPERIENCE_BOOST).getAmplifier();
								exp = (1d * exp * (1 + (amplifier + 1) * 0.05));
							}
							expOutput.add(exp);
						}
						break;
					}
				}
			}
			if (ItemStackUtil.itemExists(drop))
			{
				NBTItem nbtItem = new NBTItem(drop, true);
				CustomMaterial customMaterial = CustomMaterial.itemStackOf(drop);
				if (customMaterial != null)
				{
					CustomMaterial smeltedItem = customMaterial.getSmeltedItem();
					if (smeltedItem != null)
					{
						nbtItem.setString("id", smeltedItem.toString().toLowerCase());
					}
					Material smeltedItemVanilla = customMaterial.getSmeltedItemVanilla();
					if (smeltedItemVanilla != null)
					{
						drop = new ItemStack(smeltedItemVanilla, drop.getAmount());
					}
				}
			}
			if (player != null)
			{
				if (Method.usingLoreFeature(player))
				{
					ItemLore.setItemLore(drop);
				}
				else
				{
					ItemLore.removeItemLore(drop);
				}
			}
			dropsClone.add(drop);
		}
		return dropsClone;
	}

	/**
	 * 사용하는 손이 상관없는 아이템을 사용하는 이벤트 (예 : 양동이 비우기/채우기는 주로 사용하는 손이나 다른 손이나 상관 없음)에서 어느 손에 아이템을 사용하는지 가져옵니다.
	 *
	 * @param livingEntity
	 * 		이벤트를 호출하는 플레이어
	 * @param typeList
	 * 		감지할 아이템 (목록)
	 * @return 아이템 목록에 플레이어가 들고 있는 아이템이 없을 경우 null
	 */
	public static ItemStack getPlayerUsingItem(@NotNull LivingEntity livingEntity, @NotNull Set<Material> typeList)
	{
		EntityEquipment entityEquipment = livingEntity.getEquipment();
		if (entityEquipment == null)
		{
			return null;
		}
		ItemStack mainHand = entityEquipment.getItemInMainHand(), offHand = entityEquipment.getItemInOffHand();
		boolean isMainHand = false;
		boolean hasItem = false;
		if (itemExists(mainHand) && typeList.contains(mainHand.getType()))
		{
			isMainHand = true;
			hasItem = true;
		}
		else if (itemExists(offHand) && typeList.contains(offHand.getType()) && (!itemExists(mainHand) || !typeList.contains(mainHand.getType())))
		{
			hasItem = true;
		}
		if (hasItem)
		{
			return isMainHand ? mainHand : offHand;
		}
		return null;
	}

	/**
	 * 사용하는 손이 상관없는 아이템을 사용하는 이벤트 (예 : 양동이 비우기/채우기는 주로 사용하는 손이나 다른 손이나 상관 없음)에서 어느 손에 아이템을 사용하는지 가져옵니다.
	 *
	 * @param livingEntity
	 * 		이벤트를 호출하는 플레이어
	 * @param typeList
	 * 		감지할 아이템 (목록)
	 * @return 아이템 목록에 플레이어가 들고 있는 아이템이 없을 경우 null
	 */
	public static ItemStack getPlayerUsingItem(@NotNull LivingEntity livingEntity, Material... typeList)
	{
		return getPlayerUsingItem(livingEntity, materialArrayToList(typeList));
	}

	private static Set<Material> materialArrayToList(Material... typeList)
	{
		Set<Material> list = new HashSet<>();
		Collections.addAll(list, typeList);
		return list;
	}

	@NotNull
	public static EquipmentSlot getPlayerUsingSlot(@NotNull LivingEntity livingEntity, @NotNull Set<Material> typeList)
	{
		EntityEquipment entityEquipment = livingEntity.getEquipment();
		if (entityEquipment == null)
		{
			return EquipmentSlot.HAND;
		}
		ItemStack mainHand = entityEquipment.getItemInMainHand(), offHand = entityEquipment.getItemInOffHand();
		boolean isMainHand = false;
		boolean hasItem = false;
		if (itemExists(mainHand) && typeList.contains(mainHand.getType()))
		{
			isMainHand = true;
			hasItem = true;
		}
		else if (itemExists(offHand) && typeList.contains(offHand.getType()) && (!itemExists(mainHand) || !typeList.contains(mainHand.getType())))
		{
			hasItem = true;
		}
		if (hasItem)
		{
			return isMainHand ? EquipmentSlot.HAND : EquipmentSlot.OFF_HAND;
		}
		return EquipmentSlot.HAND;
	}

	@NotNull
	@SuppressWarnings("unused")
	public static EquipmentSlot getPlayerUsingSlot(@NotNull Player player, @NotNull Material... typeList)
	{
		return getPlayerUsingSlot(player, materialArrayToList(typeList));
	}

	@NotNull
	public static ItemStack getItemStackFromBlock(@NotNull Block block)
	{
		Material type = block.getType();
		BlockData blockData = block.getBlockData();
		Material placedBlockType = blockData.getPlacementMaterial();
		if (!placedBlockType.isAir())
		{
			type = placedBlockType;
		}

		ItemStack itemStack = new ItemStack(type.isAir() || !type.isItem() ? Material.BARRIER : type);
		ItemMeta itemMeta = itemStack.getItemMeta();
		if (itemMeta == null)
		{
			return itemStack;
		}
		if (type.isAir() || !type.isItem())
		{
			itemMeta.displayName(ComponentUtil.translate("벽").color(Constant.THE_COLOR).decoration(TextDecoration.ITALIC, State.FALSE));
		}
		else
		{
			itemMeta.displayName(ItemNameUtil.itemName(type));
		}
		itemStack.setItemMeta(itemMeta);
		if (itemMeta instanceof BlockStateMeta blockStateMeta)
		{
			BlockState blockState = block.getState();
			blockStateMeta.setBlockState(blockState);
			if (blockState instanceof Nameable nameable && nameable.customName() != null)
			{
				blockStateMeta.displayName(nameable.customName());
			}
			itemStack.setItemMeta(blockStateMeta);
		}
		Location location = block.getLocation();
		if (Method.usingLoreFeature(location))
		{
			ItemLore.setItemLore(itemStack);
			itemMeta = itemStack.getItemMeta();
			if (itemMeta instanceof BlockDataMeta blockDataMeta)
			{
				blockDataMeta.setBlockData(block.getBlockData());
				itemStack.setItemMeta(blockDataMeta);
				ItemLore.setItemLore(itemStack);
			}
		}
		List<Component> lore = itemMeta.lore();
		if (lore == null)
		{
			lore = new ArrayList<>();
		}
		if (!lore.isEmpty())
		{
			lore.add(ComponentUtil.create2(Constant.SEPARATOR));
		}
		lore.set(0, Component.translatable(" "));
		lore.add(ComponentUtil.translate("&f좌표 : %s", location));
		itemMeta.lore(lore);
		itemStack.setItemMeta(itemMeta);
		return itemStack;
	}

	public static boolean isBlockStateMetadatable(@NotNull Material type)
	{
		return new ItemStack(type).getItemMeta() instanceof BlockStateMeta;
	}

	public static boolean predicateItem(@NotNull ItemStack itemStack, @NotNull String nbt)
	{
		try
		{
			itemStack = itemStack.clone();
			NBTContainer nbtContainer = new NBTContainer(nbt);
			NBTItem nbtItem = new NBTItem(itemStack);
			nbtItem.mergeCompound(nbtContainer);
			return nbtItem.getItem().equals(itemStack);
		}
		catch (Exception ignored)
		{
			return false;
		}
	}

	/**
	 * 음표에 따른 음높이 문자열을 반환합니다.
	 *
	 * @param note
	 * 		음
	 * @return 문자열
	 */
	public static String getNoteString(int note)
	{
		return switch (note)
		{
			case 0 -> "낮은 파#(F#3)";
			case 1 -> "낮은 솔(G3)";
			case 2 -> "낮은 솔#(G#3)";
			case 3 -> "낮은 라(A3)";
			case 4 -> "낮은 라#(A#3)";
			case 5 -> "낮은 시(B3)";
			case 6 -> "도(C4)";
			case 7 -> "도#(C#4)";
			case 8 -> "레(D4)";
			case 9 -> "레#(D#4)";
			case 10 -> "미(E4)";
			case 11 -> "파(F4)";
			case 12 -> "파#(F#4)";
			case 13 -> "솔(G4)";
			case 14 -> "솔#(G#4)";
			case 15 -> "라(A4)";
			case 16 -> "라#(A#4)";
			case 17 -> "시(B4)";
			case 18 -> "높은 도(C5)";
			case 19 -> "높은 도#(C#5)";
			case 20 -> "높은 레(D5)";
			case 21 -> "높은 레#(D#5)";
			case 22 -> "높은 미(E5)";
			case 23 -> "높은 파(F5)";
			case 24 -> "높은 파#(F#5)";
			default -> note + "";
		};
	}

	@NotNull
	public static List<Component> getItemInfoAsComponents(@NotNull ItemStack itemStack, @Nullable Component tag, boolean separator)
	{
		return getItemInfoAsComponents(itemStack, null, tag, separator);
	}

	@NotNull
	public static List<Component> getItemInfoAsComponents(@NotNull ItemStack itemStack, @Nullable Object param, @Nullable Component tag, boolean separator)
	{
		itemStack = itemStack.clone();
		ItemLore.setItemLore(itemStack, param);
		ItemMeta itemMeta = itemStack.getItemMeta();
		if (itemMeta instanceof FireworkMeta fireworkMeta)
		{
			fireworkMeta.clearEffects();
			itemStack.setItemMeta(fireworkMeta);
		}
		List<Component> components = new ArrayList<>();
		if (separator)
		{
			components.add(ComponentUtil.create2("&8" + Constant.SEPARATOR));
		}
		if (tag != null)
		{
			components.add(tag);
		}
		components.add(ItemNameUtil.itemName(itemStack, NamedTextColor.WHITE));
		if (itemStack.hasItemMeta() && itemStack.getItemMeta().hasLore())
		{
			List<Component> lore = itemStack.getItemMeta().lore();
			if (lore != null)
			{
				for (int i = 0; i < lore.size(); i++)
				{
					if (i == 50)
					{
						components.add(ComponentUtil.translate("&7&ocontainer.shulkerBox.more", lore.size() - i));
						break;
					}
					components.add(lore.get(i));
				}
			}
		}
		if (separator)
		{
			components.add(ComponentUtil.create2("&8" + Constant.SEPARATOR));
		}
		return components;
	}

	@NotNull
	public static Block getExactBlockFromWithLimited(@NotNull Location location, @NotNull Collection<Material> include)
	{
		Collection<Material> values = new ArrayList<>(Arrays.asList(Material.values()));
		values.removeAll(include);
		return getExactBlockFrom(location, values);
	}

	@NotNull
	public static Block getExactBlockFrom(@NotNull Location location, @Nullable Collection<Material> exceptions)
	{
		Block block = location.getBlock();
		if (exceptions != null)
		{
			if (!exceptions.contains(block.getType()))
			{
				return block;
			}
			block = new Location(location.getWorld(), Math.round(location.getX()), Math.round(location.getY()), Math.round(location.getZ())).getBlock();
			if (!exceptions.contains(block.getType()))
			{
				return block;
			}
			for (int x = location.getBlockX() - 1; x <= location.getBlockX() + 1; x++)
			{
				for (int y = location.getBlockY() - 1; y <= location.getBlockY() + 1; y++)
				{
					for (int k = location.getBlockZ() - 1; k <= location.getBlockZ() + 1; k++)
					{
						block = location.getWorld().getBlockAt(x, y, k);
						if (!exceptions.contains(block.getType()))
						{
							return block;
						}
					}
				}
			}
		}
		return block;
	}

	@NotNull
	private static Material getAnimatedMaterial(@NotNull Collection<Material> materials)
	{
		List<Material> newList = new ArrayList<>(materials);
		newList.removeIf(m -> !m.isItem() || m.isAir());
		return Method2.getAnimated(newList, 2000);
	}

	@NotNull
	private static ItemStack getAnimatedItemStack(@Nullable Collection<ItemStack> itemStacks, @Nullable Collection<CustomMaterial> customMaterials,
			@Nullable Collection<Material> materials)
	{
		List<ItemStack> newList = new ArrayList<>();
		if (itemStacks != null)
		{
			newList.addAll(itemStacks);
		}
		if (customMaterials != null)
		{
			for (CustomMaterial customMaterial : customMaterials)
			{
				newList.add(customMaterial.create());
			}
		}
		if (materials != null)
		{
			for (Material material : materials)
			{
				if (!material.isItem() || material.isAir())
				{
					continue;
				}
				newList.add(new ItemStack(material));
			}
		}
		return Method2.getAnimated(newList, 2000);
	}

	/**
	 * gets an itemstack preview depending on the predicate nbt
	 *
	 * @param predicate
	 * 		nbt
	 * @return a previes itemstack
	 */
	@NotNull
	public static ItemStack getItemStackPredicate(@NotNull String predicate)
	{
		ItemStack itemStack = new ItemStack(Material.PAPER);
		ItemMeta itemMeta = itemStack.getItemMeta();
		Component display = ComponentUtil.translate("&aPredicate: %s", predicate);
		try
		{
			NBTContainer nbtContainer = new NBTContainer(predicate);
			String id = nbtContainer.getString("id") + "";
			if (!id.equals(""))
			{
				CustomMaterial customMaterial = CustomMaterial.itemStackOf(itemStack);
				if (customMaterial != null)
				{
					itemStack = customMaterial.create();
					display = customMaterial.getDisplayName();
				}
				else
				{
					ItemStack i = new ItemStack(Material.STONE);
					NBTItem nbtItem = new NBTItem(i, true);
					nbtItem.setString("id", id);
					ItemLore.setItemLore(i);
					itemStack = i;
					display = i.getItemMeta().displayName();
				}
			}
			else
			{
				NBTCompound tmi = nbtContainer.getCompound(CucumberyTag.KEY_TMI);
				if (tmi != null)
				{
					NBTCompound vanillaTags = tmi.getCompound(CucumberyTag.TMI_VANILLA_TAGS), customTags = tmi.getCompound(CucumberyTag.TMI_CUSTOM_TAGS);
					if (vanillaTags != null)
					{
						boolean containerEmpty = vanillaTags.getBoolean("container_empty");
						if (vanillaTags.getBoolean("minecraft:planks"))
						{
							display = ComponentUtil.translate("아무 종류의 나무 판자");
							itemStack.setType(getAnimatedMaterial(Tag.PLANKS.getValues()));
						}
						else if (vanillaTags.getBoolean("minecraft:mineable/pickaxe"))
						{
							display = ComponentUtil.translate("곡괭이로 채광할 수 있는 아무 종류의 블록");
							itemStack.setType(getAnimatedMaterial(Tag.MINEABLE_PICKAXE.getValues()));
						}
						else if (vanillaTags.getBoolean("minecraft:mineable/hoe"))
						{
							display = ComponentUtil.translate("괭이로 수확할 수 있는 아무 종류의 블록");
							itemStack.setType(getAnimatedMaterial(Tag.MINEABLE_HOE.getValues()));
						}
						else if (vanillaTags.getBoolean("minecraft:mineable/hoe"))
						{
							display = ComponentUtil.translate("도끼로 벌목할 수 있는 아무 종류의 블록");
							itemStack.setType(getAnimatedMaterial(Tag.MINEABLE_AXE.getValues()));
						}
						else if (vanillaTags.getBoolean("minecraft:mineable/hoe"))
						{
							display = ComponentUtil.translate("삽으로 굴착할 수 있는 아무 종류의 블록");
							itemStack.setType(getAnimatedMaterial(Tag.MINEABLE_SHOVEL.getValues()));
						}
						else if (vanillaTags.getBoolean("minecraft:stone_tool_materials"))
						{
							display = ComponentUtil.translate("아무 종류의 돌 도구 재료");
							itemStack.setType(getAnimatedMaterial(Tag.ITEMS_STONE_TOOL_MATERIALS.getValues()));
						}
						else if (vanillaTags.getBoolean("minecraft:wool"))
						{
							display = ComponentUtil.translate("아무 종류의 양털");
							itemStack.setType(getAnimatedMaterial(Tag.WOOL.getValues()));
						}
						else if (vanillaTags.getBoolean("minecraft:flowers"))
						{
							display = ComponentUtil.translate("아무 종류의 꽃");
							itemStack.setType(getAnimatedMaterial(Constant.FLOWERS));
						}
						else if (vanillaTags.getBoolean("minecraft:small_flowers"))
						{
							display = ComponentUtil.translate("아무 종류의 작은 크기의 꽃");
							itemStack.setType(getAnimatedMaterial(Constant.SMALL_FLOWERS));
						}
						else if (vanillaTags.getBoolean("minecraft:tall_flowers"))
						{
							display = ComponentUtil.translate("아무 종류의 큰 크기의 꽃");
							itemStack.setType(getAnimatedMaterial(Constant.TALL_FLOWERS));
						}
						else if (vanillaTags.getBoolean("minecraft:wither_immune"))
						{
							display = ComponentUtil.translate("위더가 부술 수 없는 블록");
							itemStack.setType(getAnimatedMaterial(Constant.WITHER_IMMUNE));
						}
						else if (vanillaTags.getBoolean("minecraft:beacon_base_blocks"))
						{
							display = ComponentUtil.translate("신호기를 작동시킬 수 있는 블록");
							itemStack.setType(getAnimatedMaterial(Constant.BEACON_BASE_BLOCKS));
						}
						else if (vanillaTags.getBoolean("minecraft:dyes"))
						{
							display = ComponentUtil.translate("아무 염료");
							itemStack.setType(getAnimatedMaterial(Constant.DYES));
						}
						else if (vanillaTags.getBoolean("minecraft:coals"))
						{
							display = ComponentUtil.translate("석탄 또는 숯");
							itemStack.setType(getAnimatedMaterial(Tag.ITEMS_COALS.getValues()));
						}
						else if (vanillaTags.getBoolean("minecraft:shulker_boxes"))
						{
							display = ComponentUtil.translate(containerEmpty ? "아이템이 들어있지 않은 아무 셜커 상자" : "아무 셜커 상자");
							itemStack.setType(getAnimatedMaterial(Constant.SHULKER_BOXES));
						}
						for (Material material : Material.values())
						{
							if (vanillaTags.getBoolean("material_" + material.toString().toLowerCase()))
							{
								display = ComponentUtil.translate("&o아무 %s", ItemNameUtil.itemName(material));
								itemStack.setType(material);
								break;
							}
						}
					}
					else if (customTags != null)
					{
						if (customTags.getBoolean("cucumbery_likes"))
						{
							display = ComponentUtil.translate("굳검버리가 좋아하는 것");
							itemStack.setType(
									getAnimatedMaterial(Arrays.asList(Material.TNT, Material.TNT_MINECART, Material.COMMAND_BLOCK, Material.FLINT_AND_STEEL, Material.SCUTE)));
						}
						if (customTags.getBoolean("gemstones"))
						{
							display = ComponentUtil.translate("아무 젬스톤");
							ItemStack clone = getAnimatedItemStack(null,
									Arrays.asList(CustomMaterial.AMBER, CustomMaterial.JADE, CustomMaterial.MORGANITE, CustomMaterial.SAPPHIRE, CustomMaterial.TOPAZ), null);
							itemStack.setType(clone.getType());
							itemStack.setItemMeta(clone.getItemMeta());
							itemMeta = itemStack.getItemMeta();
						}
					}
					else
					{
						throw new Exception();
					}
				}

			}
		}
		catch (Exception e)
		{
			Cucumbery.getPlugin().getLogger().warning(e.getMessage());
			display = ComponentUtil.translate("&cInvalid Predicate!: %s", predicate);
		}
		itemMeta.displayName(display);
		itemStack.setItemMeta(itemMeta);
		ItemLore.setItemLore(itemStack);
		return itemStack;
	}

	/**
	 * 마인크래프트 명령어 형식으로 아이템을 제작합니다
	 * <p>
	 * <p>
	 * <p>
	 * 아이템 예시
	 * <p>
	 * stone = {@link Material#STONE}
	 * <p>
	 * minecraft:diamond_sowrd = {@link Material#DIAMOND_SWORD}
	 * <p>
	 * stick{display:{Name:'{"text":"foo","italic":false}'},Enchantments:[{id:"minecraft:sharpness",lvl:3s}]} = {@link Enchantment#DAMAGE_ALL} 인챈트 레벨 3이 부여된 foo
	 * 라는 이름의 {@link Material#STICK}
	 *
	 * @param sender
	 * 		제작할 때 오류 발생 시 메시지를 보낼 개체
	 * @param input
	 * 		명령어 형식의 아이템
	 * @return 제작된 아이템 또는 null
	 */
	@Nullable
	public static ItemStack createItemStack(@Nullable CommandSender sender, @NotNull String input)
	{
		return createItemStack(sender, input, true);
	}

	/**
	 * 마인크래프트 명령어 형식으로 아이템을 제작합니다
	 * <p>
	 * <p>
	 * <p>
	 * 아이템 예시
	 * <p>
	 * stone = {@link Material#STONE}
	 * <p>
	 * minecraft:diamond_sowrd = {@link Material#DIAMOND_SWORD}
	 * <p>
	 * stick{display:{Name:'{"text":"foo","italic":false}'},Enchantments:[{id:"minecraft:sharpness",lvl:3s}]} = {@link Enchantment#DAMAGE_ALL} 인챈트 레벨 3이 부여된 foo
	 * 라는 이름의 {@link Material#STICK}
	 *
	 * @param sender
	 * 		제작할 때 오류 발생 시 메시지를 보낼 개체
	 * @param input
	 * 		명령어 형식의 아이템
	 * @param notice
	 * 		제작 실패 시 오류 메시지 출력 여부
	 * @return 제작된 아이템 또는 null
	 */
	@Nullable
	public static ItemStack createItemStack(@Nullable CommandSender sender, @NotNull String input, boolean notice)
	{
		try
		{
			if (!input.contains("{") && input.endsWith("}"))
			{
				if (sender != null && notice)
				{
					MessageUtil.sendError(sender, "command.expected.separator");
				}
				return null;
			}
			if (!input.matches("(.*)(.[a-z0-9_}-])$"))
			{
				if (sender != null && notice)
				{
					MessageUtil.sendError(sender, "command.expected.separator");
				}
				return null;
			}
			ItemStack itemStack = Bukkit.getItemFactory().createItemStack(input);
			if (!ItemStackUtil.itemExists(itemStack))
			{
				if (sender != null && notice)
				{
					MessageUtil.sendError(sender, "argument.item.id.invalid", input.split("\\{")[0]);
				}
				return null;
			}
			return itemStack;
		}
		catch (Exception e)
		{
			if (sender != null && notice)
			{
				Throwable t = e.getCause();
				MessageUtil.sendError(sender, getErrorCreateItemStack(t != null ? t : e));
			}
		}
		return null;
	}

	@NotNull
	public static TranslatableComponent getErrorCreateItemStack(@NotNull Throwable throwable)
	{
		String message = throwable.getMessage();
		if (message.startsWith("Unknown item '"))
		{
			String s = message.split("'")[1];
			try
			{
				s = s.split(":")[1];
			}
			catch (Exception ignored)
			{
			}
			return ComponentUtil.translate("argument.item.id.invalid", s);
		}
		if (message.startsWith("Expected '"))
		{
			String s = message.split("'")[1];
			return ComponentUtil.translate("parsing.expected", s);
		}

		if (message.startsWith("Invalid array type '"))
		{
			String s = message.split("'")[1];
			return ComponentUtil.translate("argument.nbt.array.invalid", s);
		}

		if (message.startsWith("Can't insert"))
		{
			String[] split = message.split(" ");
			if (message.contains("into list of"))
			{
				String s = split[2], s1 = split[6];
				return ComponentUtil.translate("argument.nbt.list.mixed", s, s1);
			}
			else
			{
				String s = split[2], s1 = split[4];
				return ComponentUtil.translate("argument.nbt.array.mixed", s, s1);
			}
		}
		if (message.startsWith("Expected key"))
		{
			return ComponentUtil.translate("argument.nbt.expected.key");
		}
		if (message.startsWith("Expected value"))
		{
			return ComponentUtil.translate("argument.nbt.expected.value");
		}
		return ComponentUtil.translate("");
	}

	public static void setTexture(@NotNull SkullMeta skullMeta, @NotNull String url)
	{
		if (url.startsWith("https://textures.minecraft.net/texture/"))
		{
			url = url.substring("https://textures.minecraft.net/texture/".length());
		}
		String decodedString;
		try
		{
			decodedString = new String(Base64.getDecoder().decode(url));
		}
		catch (Exception e)
		{
			decodedString = "";
		}
		String base64Data;
		if (decodedString.startsWith("{\"textures\":{\"SKIN\":{\"url\":\"http://textures.minecraft.net/texture/"))
		{
			base64Data = url;
		}
		else
		{
			if (!url.startsWith("http://textures.minecraft.net/texture/"))
			{
				url = "http://textures.minecraft.net/texture/" + url;
			}
			String JSONData = "{\"textures\":{\"SKIN\":{\"url\":\"" + url + "\"}}}";
			base64Data = Base64.getEncoder().encodeToString(JSONData.getBytes());
		}
		PlayerProfile playerProfile = Bukkit.createProfile(UUID.fromString("0-0-0-0-0"), null);
		playerProfile.setProperty(new ProfileProperty("textures", base64Data));
		skullMeta.setPlayerProfile(playerProfile);
	}

	/**
	 * 플레이어의 인벤토리에 있는 모든 아이템의 설명을 새로고침합니다.
	 *
	 * @param player
	 * 		인벤토리에 있는 모든 아이템의 설명을 업데이트할 플레이어
	 */
	public static void updateInventory(@NotNull Player player)
	{
		updateInventory(player, false);
	}

	/**
	 * 플레이어의 인벤토리에 있는 모든 아이템의 설명을 새로고침합니다.
	 *
	 * @param player
	 * 		인벤토리에 있는 모든 아이템의 설명을 업데이트할 플레이어
	 */
	public static void updateInventory(@NotNull Player player, boolean callAPI)
	{
		InventoryView openInventory = player.getOpenInventory();
		String title = openInventory.getTitle();
		if (!title.contains(Constant.SERVER_SETTINGS) && (title.contains(Constant.CANCEL_STRING) || title.contains(Constant.CUSTOM_RECIPE_CREATE_GUI)))
		{
			return;
		}
		Inventory inventory = player.getInventory();
		for (int i = 0; i < inventory.getSize(); i++)
		{
			ItemStack item = inventory.getItem(i);
			if (item == null)
			{
				continue;
			}
			ItemLore.setItemLore(item, new ItemLoreView(player));
		}
		inventory = openInventory.getTopInventory();
		for (int i = 0; i < inventory.getSize(); i++)
		{
			ItemStack item = inventory.getItem(i);
			if (item == null)
			{
				continue;
			}
			ItemLore.setItemLore(item, new ItemLoreView(player));
		}
		player.setItemOnCursor(ItemLore.setItemLore(player.getItemOnCursor(), new ItemLoreView(player)));
		if (callAPI)
		{
			player.updateInventory();
		}
	}

	/**
	 * 플레이어의 인벤토리에 있는 아이템의 설명을 새로고침합니다.
	 *
	 * @param player
	 * 		인벤토리에 있는 아이템의 설명을 업데이트할 플레이어
	 * @param item
	 * 		설명을 새로고침할 아이템
	 */
	public static void updateInventory(@NotNull Player player, @NotNull ItemStack item)
	{
		ItemLore.setItemLore(item, new ItemLoreView(player));
	}
}


















