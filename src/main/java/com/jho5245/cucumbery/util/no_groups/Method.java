package com.jho5245.cucumbery.util.no_groups;

import com.comphenix.protocol.PacketType.Play;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.reflect.StructureModifier;
import com.comphenix.protocol.wrappers.WrappedDataValue;
import com.comphenix.protocol.wrappers.WrappedDataWatcher;
import com.google.common.base.Predicates;
import com.google.common.collect.Lists;
import com.jho5245.cucumbery.Cucumbery;
import com.jho5245.cucumbery.custom.customeffect.type.CustomEffectType;
import com.jho5245.cucumbery.util.itemlore.ItemLore;
import com.jho5245.cucumbery.util.nbt.CucumberyTag;
import com.jho5245.cucumbery.util.nbt.NBTAPI;
import com.jho5245.cucumbery.util.storage.component.util.ComponentUtil;
import com.jho5245.cucumbery.util.storage.component.util.ItemNameUtil;
import com.jho5245.cucumbery.util.storage.component.util.sendercomponent.SenderComponentUtil;
import com.jho5245.cucumbery.util.storage.data.*;
import com.jho5245.cucumbery.util.storage.no_groups.CustomConfig;
import com.jho5245.cucumbery.util.storage.no_groups.CustomConfig.UserData;
import com.jho5245.cucumbery.util.storage.no_groups.ItemStackUtil;
import com.jho5245.cucumbery.util.storage.no_groups.SoundPlay;
import de.tr7zw.changeme.nbtapi.NBTCompound;
import de.tr7zw.changeme.nbtapi.NBTItem;
import de.tr7zw.changeme.nbtapi.NBTList;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.format.TextDecoration.State;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;
import org.bukkit.*;
import org.bukkit.Note.Tone;
import org.bukkit.attribute.Attributable;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.block.CommandBlock;
import org.bukkit.block.ShulkerBox;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.*;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.help.HelpTopic;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BlockStateMeta;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class Method extends SoundPlay
{
	private static final Random RANDOM_INTEGER = new Random();

	@SuppressWarnings("all")
	private static final List<String> VANILLA_COMMANDS = new ArrayList<>(/*Arrays
          .asList("advancement", "attribute", "ban", "ban-ip", "banlist", "bossbar", "cucumbery", "ccucumbery", "cgive", "chp", "ckill",
                  "clear", "clone", "cride", "csetitem", "csudo2", "cvelo", "cvelocity", "data", "datapack", "debug",
                  "defaultgamemode", "deop", "difficulty", "effect", "enchant", "execute", "experience", "fill", "forceload", "function",
                  "gamemode", "gamerule", "give", "haspermission", "healthpoint", "help", "hp", "kick", "kill", "list", "locate",
                  "locatebiome", "loot", "me", "msg", "op", "pardon", "pardon-ip", "particle", "playsound", "randomchance", "recipe",
                  "reload", "ride", "rideoff", "save-all", "save-off", "save-on", "say", "schedule", "scoreboard", "seed",
                  "setblock", "setidletimeout", "setitem", "setworldspawn", "spawnpoint", "spectate", "spreadplayers", "stop",
                  "stopsound", "sudo2", "summon", "tag", "team", "teammsg", "teleport", "tell", "tellraw", "time", "title", "tm", "tp",
                  "trigger", "velo", "velocity", "w", "weather", "whitelist", "worldborder", "xp", "날리기", "item")*/);

	public static String timeFormatMilli(long ms)
	{
		return Method.timeFormatMilli(ms, true, 2);
	}

	public static String timeFormatMilli(long ms, int decimalSize)
	{
		return Method.timeFormatMilli(ms, true, decimalSize);
	}

	public static String timeFormatMilli(long ms, boolean showMs)
	{
		return Method.timeFormatMilli(ms, showMs, 2, false);
	}

	public static String timeFormatMilli(long ms, boolean showMs, int decimalSize)
	{
		return timeFormatMilli(ms, showMs, decimalSize, false);
	}

	@SuppressWarnings("all")
	public static String timeFormatMilli(long ms, boolean showMs, int decimalSize, boolean shorten)
	{
		if (ms == 0 || (!showMs && ms < 1000))
		{
			return "0초";
		}
		if (shorten)
		{
			if (ms >= 1000L * 60 * 60 * 24 * 365)
			{
				return Constant.Sosu1ForceFloor.format(1d * ms / (1000L * 60 * 60 * 24 * 365)) + "년";
			}
			if (ms >= 1000L * 60 * 60 * 24)
			{
				return Constant.Sosu1ForceFloor.format(1d * ms / (1000L * 60 * 60 * 24)) + "일";
			}
			if (ms >= 1000L * 60 * 999)
			{
				return Constant.Sosu1ForceFloor.format(1d * ms / (1000L * 60 * 60)) + "시간";
			}
			if (ms >= 1000L * 60 * 10)
			{
				return Constant.JeongsuFloor.format(Math.floor(1d * ms / (1000L * 60))) + "분";
			}
			if (ms >= 1000L * 60)
			{
				return Constant.Sosu1ForceFloor.format(1d * ms / (1000L * 60)) + "분";
			}
			if (ms >= 1000L * 10)
			{
				return Constant.JeongsuFloor.format(1d * ms / 1000L) + "초";
			}
			return Constant.Sosu1ForceFloor.format(1d * ms / 1000L) + "초";
		}
		long year = ms / (1000L * 60 * 60 * 24 * 365);
		ms %= (1000L * 60 * 60 * 24 * 365);
		long day = ms / (1000L * 60 * 60 * 24);
		ms %= (1000L * 60 * 60 * 24);
		long hour = ms / (1000L * 60 * 60);
		ms %= (1000L * 60 * 60);
		long min = ms / (1000L * 60);
		ms %= (1000L * 60);
		double sec = ms / 1000D;
		if (!showMs)
		{
			sec = Math.floor(sec);
		}
		String displaySec;
		if (showMs)
		{
			switch (decimalSize)
			{
				case 0:
					displaySec = Constant.Jeongsu.format(sec);
					break;
				case 1:
					displaySec = Constant.Sosu1Force.format(sec);
					break;
				default:
				case 2:
					displaySec = Constant.Sosu2.format(sec);
					break;
				case 3:
					displaySec = Constant.Sosu3.format(sec);
					break;
				case 4:
					displaySec = Constant.Sosu4.format(sec);
					break;
			}
		}
		else
		{
			displaySec = Constant.Jeongsu.format(Math.floor(ms / 1000d));
		}
		return ((year != 0) ? year + "년" : "") + ((day != 0) ? ((year != 0) ? " " : "") + day + "일" : "") + ((hour != 0) ? ((year != 0 || day != 0) ? " " : "")
				+ hour + "시간" : "") + ((min != 0) ? ((year != 0 || day != 0 || hour != 0) ? " " : "") + min + "분" : "") + ((sec != 0) ?
				((year != 0 || day != 0 || hour != 0 || min != 0) ? " " : "") + displaySec + "초" : "");
	}

	/**
	 * min 이상, max 이하의 랜덤한 정수를 반환합니다.
	 * <p>만약 min이 max보다 클 경우 min을 반환합니다.
	 *
	 * @param min
	 * 		랜덤 숫자의 최솟값
	 * @param max
	 * 		랜덤 숫자의 최댓값
	 * @return min 이상, max 이하의 랜덤한 정수
	 */
	public static int random(int min, int max)
	{
		if (min >= max)
		{
			return min;
		}
		return RANDOM_INTEGER.nextInt(max - min + 1) + min;
	}

	/**
	 * 단축바 슬롯을 바꾸었을때 소리를 재생합니다.
	 */
	public static void heldItemSound(@NotNull Player player, @Nullable ItemStack item)
	{
		if (item == null)
		{
			playSound(player, Sound.BLOCK_NOTE_BLOCK_HAT, 0.5F, Method.random(130, 170) / 100.0F);
			return;
		}
		CustomMaterial customMaterial = CustomMaterial.itemStackOf(item);
		if (customMaterial != null)
		{
			switch (customMaterial)
			{
				case TUNGSTEN_INGOT, COBALT_INGOT, CUCUMBERITE_INGOT, MITHRIL_INGOT, SHROOMITE_INGOT, TITANIUM_INGOT, COBALT_ORE, CUCUMBERITE_ORE, MITHRIL_ORE, SHROOMITE_ORE, TITANIUM_ORE, TUNGSTEN_ORE ->
						heldItemSound(player, new ItemStack(Material.IRON_INGOT));
			}
		}
		else
		{
			switch (item.getType())
			{
				case WOODEN_SWORD:
				case STONE_SWORD:
				case IRON_SWORD:
				case DIAMOND_SWORD:
				case GOLDEN_SWORD:
				case NETHERITE_SWORD:
					int random = Method.random(1, 6);
					switch (random)
					{
						case 1 ->
						{
							playSound(player, Sound.ENTITY_PLAYER_ATTACK_CRIT, 0.5F, Method.random(80, 102) / 100.0F);
							return;
						}
						case 2 ->
						{
							playSound(player, Sound.ENTITY_PLAYER_ATTACK_KNOCKBACK, 0.5F, Method.random(80, 102) / 100.0F);
							return;
						}
						case 3 ->
						{
							playSound(player, Sound.ENTITY_PLAYER_ATTACK_NODAMAGE, 0.5F, Method.random(80, 102) / 100.0F);
							return;
						}
						case 4 ->
						{
							playSound(player, Sound.ENTITY_PLAYER_ATTACK_STRONG, 0.5F, Method.random(80, 102) / 100.0F);
							return;
						}
						case 5 ->
						{
							playSound(player, Sound.ENTITY_PLAYER_ATTACK_SWEEP, 0.5F, Method.random(80, 102) / 100.0F);
							return;
						}
						case 6 ->
						{
							playSound(player, Sound.ENTITY_PLAYER_ATTACK_WEAK, 0.5F, Method.random(80, 102) / 100.0F);
							return;
						}
					}
					return;
				case WOODEN_PICKAXE:
				case STONE_PICKAXE:
				case IRON_PICKAXE:
				case DIAMOND_PICKAXE:
				case GOLDEN_PICKAXE:
				case NETHERITE_PICKAXE:
					playSound(player, Sound.BLOCK_STONE_BREAK, 0.5F, Method.random(80, 120) / 100.0F);
					return;
				case WOODEN_AXE:
				case STONE_AXE:
				case IRON_AXE:
				case DIAMOND_AXE:
				case GOLDEN_AXE:
				case NETHERITE_AXE:
					playSound(player, Sound.BLOCK_WOOD_BREAK, 0.5F, Method.random(80, 120) / 100.0F);
					return;
				case WOODEN_SHOVEL:
				case STONE_SHOVEL:
				case IRON_SHOVEL:
				case DIAMOND_SHOVEL:
				case GOLDEN_SHOVEL:
				case NETHERITE_SHOVEL:
					random = Method.random(1, 3);
					switch (random)
					{
						case 1 ->
						{
							playSound(player, Sound.BLOCK_GRAVEL_BREAK, 0.5F, Method.random(80, 120) / 100.0F);
							return;
						}
						case 2 ->
						{
							playSound(player, Sound.BLOCK_GRASS_BREAK, 0.5F, Method.random(80, 120) / 100.0F);
							return;
						}
						case 3 -> playSound(player, Sound.BLOCK_SAND_BREAK, 0.5F, Method.random(80, 120) / 100.0F);
					}
					return;
				case WOODEN_HOE:
				case STONE_HOE:
				case IRON_HOE:
				case DIAMOND_HOE:
				case GOLDEN_HOE:
				case NETHERITE_HOE:
					playSound(player, Sound.ITEM_HOE_TILL, 0.5F, Method.random(80, 120) / 100.0F);
					return;
				case SHEARS:
					playSound(player, Sound.ENTITY_SHEEP_SHEAR, 0.5F, Method.random(80, 120) / 100.0F);
					return;
				case BOW:
					playSound(player, Sound.ENTITY_ARROW_SHOOT, 0.5F, Method.random(80, 120) / 100.0F);
					return;
				case SHIELD:
					playSound(player, Sound.ENTITY_HORSE_ARMOR, 0.5F, Method.random(120, 200) / 100.0F);
					return;
				case NAME_TAG:
					playSound(player, Sound.ENTITY_HORSE_SADDLE, 0.5F, Method.random(120, 200) / 100.0F);
					return;
				case OAK_DOOR:
				case SPRUCE_DOOR:
				case BIRCH_DOOR:
				case JUNGLE_DOOR:
				case ACACIA_DOOR:
				case DARK_OAK_DOOR:
				case CRIMSON_DOOR:
				case WARPED_DOOR:
					random = Method.random(1, 2);
					switch (random)
					{
						case 1 ->
						{
							playSound(player, Sound.BLOCK_WOODEN_DOOR_OPEN, 0.5F, Method.random(80, 100) / 100.0F);
							return;
						}
						case 2 ->
						{
							playSound(player, Sound.BLOCK_WOODEN_DOOR_CLOSE, 0.5F, Method.random(80, 100) / 100.0F);
							return;
						}
					}
					return;
				case OAK_FENCE_GATE:
				case SPRUCE_FENCE_GATE:
				case BIRCH_FENCE_GATE:
				case JUNGLE_FENCE_GATE:
				case ACACIA_FENCE_GATE:
				case DARK_OAK_FENCE_GATE:
				case CRIMSON_FENCE_GATE:
				case WARPED_FENCE_GATE:
					random = Method.random(1, 2);
					switch (random)
					{
						case 1 ->
						{
							playSound(player, Sound.BLOCK_FENCE_GATE_OPEN, 0.5F, Method.random(80, 100) / 100.0F);
							return;
						}
						case 2 ->
						{
							playSound(player, Sound.BLOCK_FENCE_GATE_CLOSE, 0.5F, Method.random(80, 100) / 100.0F);
							return;
						}
					}
					return;
				case IRON_DOOR:
					random = Method.random(1, 2);
					switch (random)
					{
						case 1 ->
						{
							playSound(player, Sound.BLOCK_IRON_DOOR_OPEN, 0.5F, Method.random(80, 100) / 100.0F);
							return;
						}
						case 2 ->
						{
							playSound(player, Sound.BLOCK_IRON_DOOR_CLOSE, 0.5F, Method.random(80, 100) / 100.0F);
							return;
						}
					}
					return;
				case INFESTED_CHISELED_STONE_BRICKS:
				case INFESTED_COBBLESTONE:
				case INFESTED_CRACKED_STONE_BRICKS:
				case INFESTED_MOSSY_STONE_BRICKS:
				case INFESTED_STONE:
				case INFESTED_STONE_BRICKS:
					playSound(player, Sound.ENTITY_SILVERFISH_AMBIENT, 0.5F, Method.random(80, 100) / 100.0F);
					return;
				case BREWING_STAND:
					playSound(player, Sound.BLOCK_BREWING_STAND_BREW, 1.0F, Method.random(150, 200) / 100.0F);
					return;
				case TNT:
				case TNT_MINECART:
					playSound(player, Sound.ENTITY_TNT_PRIMED, 0.5F, Method.random(150, 200) / 100.0F);
					return;
				case FISHING_ROD:
				case LEAD:
					playSound(player, Sound.ENTITY_WITCH_THROW, 0.5F, Method.random(80, 150) / 100.0F);
					return;
				case CLOCK:
				case COMPASS:
				case STONE_BUTTON:
				case OAK_BUTTON:
				case SPRUCE_BUTTON:
				case BIRCH_BUTTON:
				case JUNGLE_BUTTON:
				case ACACIA_BUTTON:
				case DARK_OAK_BUTTON:
				case LEVER:
				case REPEATER:
				case COMPARATOR:
				case HOPPER:
				case DROPPER:
				case DISPENSER:
				case OBSERVER:
				case LIGHT_WEIGHTED_PRESSURE_PLATE:
				case HEAVY_WEIGHTED_PRESSURE_PLATE:
				case STONE_PRESSURE_PLATE:
				case OAK_PRESSURE_PLATE:
				case SPRUCE_PRESSURE_PLATE:
				case BIRCH_PRESSURE_PLATE:
				case JUNGLE_PRESSURE_PLATE:
				case ACACIA_PRESSURE_PLATE:
				case DARK_OAK_PRESSURE_PLATE:
				case DAYLIGHT_DETECTOR:
					playSound(player, Sound.BLOCK_LEVER_CLICK, 0.5F, Method.random(80, 120) / 100.0F);
					return;
				case POTION:
				case SPLASH_POTION:
				case LINGERING_POTION:
				case EXPERIENCE_BOTTLE:
				case DRAGON_BREATH:
				case ENCHANTED_BOOK:
				case ENCHANTING_TABLE:
				case ENCHANTED_GOLDEN_APPLE:
				case BEACON:
					playSound(player, Sound.BLOCK_ENCHANTMENT_TABLE_USE, 0.5F, Method.random(180, 200) / 100.0F);
					return;
				case ENDER_CHEST:
				case ENDER_PEARL:
				case ENDER_EYE:
				case END_PORTAL_FRAME:
					playSound(player, Sound.ENTITY_ENDERMAN_AMBIENT, 0.5F, Method.random(80, 100) / 100.0F);
					return;
				case LEATHER_HELMET:
				case LEATHER_CHESTPLATE:
				case LEATHER_LEGGINGS:
				case LEATHER_BOOTS:
					playSound(player, Sound.ITEM_ARMOR_EQUIP_LEATHER, 0.5F, Method.random(80, 100) / 100.0F);
					return;
				case IRON_HELMET:
				case IRON_CHESTPLATE:
				case IRON_LEGGINGS:
				case IRON_BOOTS:
					playSound(player, Sound.ITEM_ARMOR_EQUIP_IRON, 0.5F, Method.random(80, 100) / 100.0F);
					return;
				case GOLDEN_HELMET:
				case GOLDEN_CHESTPLATE:
				case GOLDEN_LEGGINGS:
				case GOLDEN_BOOTS:
					playSound(player, Sound.ITEM_ARMOR_EQUIP_GOLD, 0.5F, Method.random(80, 100) / 100.0F);
					return;
				case DIAMOND_HELMET:
				case DIAMOND_CHESTPLATE:
				case DIAMOND_LEGGINGS:
				case DIAMOND_BOOTS:
					playSound(player, Sound.ITEM_ARMOR_EQUIP_DIAMOND, 0.5F, Method.random(80, 100) / 100.0F);
					return;
				case CHAINMAIL_HELMET:
				case CHAINMAIL_CHESTPLATE:
				case CHAINMAIL_LEGGINGS:
				case CHAINMAIL_BOOTS:
					playSound(player, Sound.ITEM_ARMOR_EQUIP_CHAIN, 0.5F, Method.random(80, 100) / 100.0F);
					return;
				case ELYTRA:
					playSound(player, Sound.ITEM_ARMOR_EQUIP_ELYTRA, 0.5F, Method.random(80, 100) / 100.0F);
					return;
				case WATER_BUCKET:
					playSound(player, Sound.ITEM_BUCKET_FILL, 0.5F, Method.random(80, 100) / 100.0F);
					return;
				case PUFFERFISH_BUCKET:
				case COD_BUCKET:
				case SALMON_BUCKET:
					playSound(player, Sound.ITEM_BUCKET_FILL_FISH, 0.5F, Method.random(80, 100) / 100.0F);
					return;
				case LAVA_BUCKET:
					playSound(player, Sound.ITEM_BUCKET_FILL_LAVA, 0.5F, Method.random(80, 100) / 100.0F);
					return;
				case BUCKET:
					playSound(player, Sound.ITEM_BUCKET_EMPTY, 0.5F, Method.random(180, 200) / 100.0F);
					return;
				case BLACK_WOOL:
				case BLUE_WOOL:
				case BROWN_WOOL:
				case CYAN_WOOL:
				case GRAY_WOOL:
				case GREEN_WOOL:
				case LIGHT_BLUE_WOOL:
				case LIGHT_GRAY_WOOL:
				case LIME_WOOL:
				case MAGENTA_WOOL:
				case ORANGE_WOOL:
				case PINK_WOOL:
				case PURPLE_WOOL:
				case RED_WOOL:
				case WHITE_WOOL:
				case YELLOW_WOOL:
				case BLACK_CARPET:
				case BLUE_CARPET:
				case BROWN_CARPET:
				case CYAN_CARPET:
				case GRAY_CARPET:
				case GREEN_CARPET:
				case LIGHT_BLUE_CARPET:
				case LIGHT_GRAY_CARPET:
				case LIME_CARPET:
				case MAGENTA_CARPET:
				case ORANGE_CARPET:
				case PINK_CARPET:
				case PURPLE_CARPET:
				case RED_CARPET:
				case WHITE_CARPET:
				case YELLOW_CARPET:
				case BLACK_BANNER:
				case BLUE_BANNER:
				case BROWN_BANNER:
				case CYAN_BANNER:
				case GRAY_BANNER:
				case GREEN_BANNER:
				case LIGHT_BLUE_BANNER:
				case LIGHT_GRAY_BANNER:
				case LIME_BANNER:
				case MAGENTA_BANNER:
				case ORANGE_BANNER:
				case PINK_BANNER:
				case PURPLE_BANNER:
				case RED_BANNER:
				case WHITE_BANNER:
				case YELLOW_BANNER:
					playSound(player, Sound.BLOCK_WOOL_BREAK, 0.5F, Method.random(80, 100) / 100.0F);
					return;
				case GLASS:
				case GLASS_BOTTLE:
				case GLASS_PANE:
				case BLACK_STAINED_GLASS:
				case BLUE_STAINED_GLASS:
				case BROWN_STAINED_GLASS:
				case CYAN_STAINED_GLASS:
				case GRAY_STAINED_GLASS:
				case GREEN_STAINED_GLASS:
				case LIGHT_BLUE_STAINED_GLASS:
				case LIGHT_GRAY_STAINED_GLASS:
				case LIME_STAINED_GLASS:
				case MAGENTA_STAINED_GLASS:
				case ORANGE_STAINED_GLASS:
				case PINK_STAINED_GLASS:
				case PURPLE_STAINED_GLASS:
				case RED_STAINED_GLASS:
				case WHITE_STAINED_GLASS:
				case YELLOW_STAINED_GLASS:
				case BLACK_STAINED_GLASS_PANE:
				case BLUE_STAINED_GLASS_PANE:
				case BROWN_STAINED_GLASS_PANE:
				case CYAN_STAINED_GLASS_PANE:
				case GRAY_STAINED_GLASS_PANE:
				case GREEN_STAINED_GLASS_PANE:
				case LIGHT_BLUE_STAINED_GLASS_PANE:
				case LIGHT_GRAY_STAINED_GLASS_PANE:
				case LIME_STAINED_GLASS_PANE:
				case MAGENTA_STAINED_GLASS_PANE:
				case ORANGE_STAINED_GLASS_PANE:
				case PINK_STAINED_GLASS_PANE:
				case PURPLE_STAINED_GLASS_PANE:
				case RED_STAINED_GLASS_PANE:
				case WHITE_STAINED_GLASS_PANE:
				case YELLOW_STAINED_GLASS_PANE:
					playSound(player, Sound.BLOCK_GLASS_BREAK, 0.5F, Method.random(180, 200) / 100.0F);
					return;
				case REDSTONE:
				case GLOWSTONE_DUST:
				case BLAZE_POWDER:
				case GUNPOWDER:
				case CLAY_BALL:
					playSound(player, Sound.BLOCK_SAND_HIT, 0.5F, Method.random(180, 200) / 100.0F);
					return;
				case FLINT_AND_STEEL:
					playSound(player, Sound.ITEM_FLINTANDSTEEL_USE, 0.5F, Method.random(80, 100) / 100.0F);
					return;
				case OAK_TRAPDOOR:
				case SPRUCE_TRAPDOOR:
				case BIRCH_TRAPDOOR:
				case JUNGLE_TRAPDOOR:
				case ACACIA_TRAPDOOR:
				case DARK_OAK_TRAPDOOR:
				case CRIMSON_TRAPDOOR:
				case WARPED_TRAPDOOR:
					random = Method.random(1, 2);
					switch (random)
					{
						case 1 ->
						{
							playSound(player, Sound.BLOCK_WOODEN_TRAPDOOR_OPEN, 0.5F, Method.random(80, 100) / 100.0F);
							return;
						}
						case 2 ->
						{
							playSound(player, Sound.BLOCK_WOODEN_TRAPDOOR_CLOSE, 0.5F, Method.random(80, 100) / 100.0F);
							return;
						}
					}
					return;
				case IRON_TRAPDOOR:
					random = Method.random(1, 2);
					switch (random)
					{
						case 1 ->
						{
							playSound(player, Sound.BLOCK_IRON_TRAPDOOR_OPEN, 0.5F, Method.random(80, 100) / 100.0F);
							return;
						}
						case 2 ->
						{
							playSound(player, Sound.BLOCK_IRON_TRAPDOOR_CLOSE, 0.5F, Method.random(80, 100) / 100.0F);
							return;
						}
					}
					return;
				case CHEST:
					playSound(player, Sound.BLOCK_CHEST_OPEN, 0.5F, Method.random(100, 150) / 100.0F);
					return;
				case TRAPPED_CHEST:
					playSound(player, Sound.BLOCK_CHEST_OPEN, 0.5F, Method.random(80, 100) / 100.0F);
					playSound(player, Sound.BLOCK_CHEST_OPEN, 0.5F, Method.random(50, 80) / 100.0F);
					return;
				case FIREWORK_ROCKET:
					playSound(player, Sound.ENTITY_FIREWORK_ROCKET_LAUNCH, 0.5F, Method.random(80, 120) / 100.0F);
					return;
				case SLIME_BALL:
				case SLIME_BLOCK:
					playSound(player, Sound.BLOCK_SLIME_BLOCK_BREAK, 0.5F, Method.random(180, 200) / 100.0F);
					return;
				case PAINTING:
					playSound(player, Sound.ENTITY_PAINTING_PLACE, 0.5F, Method.random(80, 120) / 100.0F);
					return;
				case ITEM_FRAME:
					playSound(player, Sound.ENTITY_ITEM_FRAME_PLACE, 0.5F, Method.random(80, 120) / 100.0F);
					return;
				case OAK_LEAVES:
				case SPRUCE_LEAVES:
				case BIRCH_LEAVES:
				case JUNGLE_LEAVES:
				case ACACIA_LEAVES:
				case DARK_OAK_LEAVES:
				case OAK_SAPLING:
				case SPRUCE_SAPLING:
				case BIRCH_SAPLING:
				case JUNGLE_SAPLING:
				case ACACIA_SAPLING:
				case DARK_OAK_SAPLING:
				case GRASS:
				case GRASS_BLOCK:
				case FERN:
				case DEAD_BUSH:
				case SEAGRASS:
				case DANDELION:
				case POPPY:
				case BLUE_ORCHID:
				case ALLIUM:
				case AZURE_BLUET:
				case RED_TULIP:
				case ORANGE_TULIP:
				case PINK_TULIP:
				case WHITE_TULIP:
				case VINE:
				case SUNFLOWER:
				case LILAC:
				case ROSE_BUSH:
				case PEONY:
				case TALL_GRASS:
				case TALL_SEAGRASS:
				case LARGE_FERN:
				case BROWN_MUSHROOM:
				case RED_MUSHROOM:
				case BEETROOT_SEEDS:
				case MELON_SEEDS:
				case PUMPKIN_SEEDS:
				case WHEAT_SEEDS:
				case SUGAR_CANE:
				case WHEAT:
				case OXEYE_DAISY:
				case CORNFLOWER:
				case LILY_OF_THE_VALLEY:
				case WITHER_ROSE:
					playSound(player, Sound.BLOCK_GRASS_HIT, 0.5F, Method.random(100, 120) / 100.0F);
					return;
				case CONDUIT:
					playSound(player, Sound.BLOCK_CONDUIT_ACTIVATE, 0.5F, Method.random(100, 120) / 100.0F);
					return;
				case NETHER_STAR:
					playSound(player, Sound.BLOCK_BEACON_ACTIVATE, 0.5F, Method.random(100, 120) / 100.0F);
					return;
				case LILY_PAD:
					playSound(player, Sound.BLOCK_GRASS_HIT, 0.5F, Method.random(100, 120) / 100.0F);
					return;
				case BAT_SPAWN_EGG:
					playSound(player, Sound.ENTITY_BAT_AMBIENT, 0.5F, Method.random(100, 120) / 100.0F);
					return;
				case BLAZE_SPAWN_EGG:
					playSound(player, Sound.ENTITY_BLAZE_AMBIENT, 0.5F, Method.random(100, 120) / 100.0F);
					return;
				case SPIDER_SPAWN_EGG:
				case CAVE_SPIDER_SPAWN_EGG:
					playSound(player, Sound.ENTITY_SPIDER_AMBIENT, 0.5F, Method.random(100, 120) / 100.0F);
					return;
				case CHICKEN_SPAWN_EGG:
					playSound(player, Sound.ENTITY_CHICKEN_AMBIENT, 0.5F, Method.random(100, 120) / 100.0F);
					return;
				case COD_SPAWN_EGG:
					playSound(player, Sound.ENTITY_COD_AMBIENT, 0.5F, Method.random(100, 120) / 100.0F);
					return;
				case COW_SPAWN_EGG:
					playSound(player, Sound.ENTITY_COW_AMBIENT, 0.5F, Method.random(100, 120) / 100.0F);
					return;
				case CREEPER_SPAWN_EGG:
					playSound(player, Sound.ENTITY_CREEPER_HURT, 0.5F, Method.random(100, 120) / 100.0F);
					return;
				case DOLPHIN_SPAWN_EGG:
					playSound(player, Sound.ENTITY_DOLPHIN_AMBIENT, 0.5F, Method.random(100, 120) / 100.0F);
					return;
				case DONKEY_SPAWN_EGG:
					playSound(player, Sound.ENTITY_DONKEY_AMBIENT, 0.5F, Method.random(100, 120) / 100.0F);
					return;
				case DROWNED_SPAWN_EGG:
					playSound(player, Sound.ENTITY_DROWNED_AMBIENT, 0.5F, Method.random(100, 120) / 100.0F);
					return;
				case ELDER_GUARDIAN_SPAWN_EGG:
					playSound(player, Sound.ENTITY_ELDER_GUARDIAN_AMBIENT, 0.5F, Method.random(100, 120) / 100.0F);
					return;
				case ENDERMAN_SPAWN_EGG:
					playSound(player, Sound.ENTITY_ENDERMAN_AMBIENT, 0.5F, Method.random(100, 120) / 100.0F);
					return;
				case ENDERMITE_SPAWN_EGG:
					playSound(player, Sound.ENTITY_ENDERMITE_AMBIENT, 0.5F, Method.random(100, 120) / 100.0F);
					return;
				case EVOKER_SPAWN_EGG:
					playSound(player, Sound.ENTITY_EVOKER_AMBIENT, 0.5F, Method.random(100, 120) / 100.0F);
					return;
				case GHAST_SPAWN_EGG:
					playSound(player, Sound.ENTITY_GHAST_AMBIENT, 0.5F, Method.random(100, 120) / 100.0F);
					return;
				case GUARDIAN_SPAWN_EGG:
					playSound(player, Sound.ENTITY_GUARDIAN_AMBIENT, 0.5F, Method.random(100, 120) / 100.0F);
					return;
				case HUSK_SPAWN_EGG:
					playSound(player, Sound.ENTITY_HUSK_AMBIENT, 0.5F, Method.random(100, 120) / 100.0F);
					return;
				case LLAMA_SPAWN_EGG:
				case TRADER_LLAMA_SPAWN_EGG:
					playSound(player, Sound.ENTITY_LLAMA_AMBIENT, 0.5F, Method.random(100, 120) / 100.0F);
					return;
				case HORSE_SPAWN_EGG:
					playSound(player, Sound.ENTITY_HORSE_AMBIENT, 0.5F, Method.random(100, 120) / 100.0F);
					return;
				case MOOSHROOM_SPAWN_EGG:
					playSound(player, Sound.ENTITY_COW_AMBIENT, 0.5F, Method.random(100, 120) / 100.0F);
					return;
				case MAGMA_CUBE_SPAWN_EGG:
					playSound(player, Sound.ENTITY_MAGMA_CUBE_HURT, 0.5F, Method.random(100, 120) / 100.0F);
					return;
				case MULE_SPAWN_EGG:
					playSound(player, Sound.ENTITY_MULE_AMBIENT, 0.5F, Method.random(100, 120) / 100.0F);
					return;
				case OCELOT_SPAWN_EGG:
					playSound(player, Sound.ENTITY_CAT_AMBIENT, 0.5F, Method.random(100, 120) / 100.0F);
					return;
				case PARROT_SPAWN_EGG:
					playSound(player, Sound.ENTITY_PARROT_AMBIENT, 0.5F, Method.random(100, 120) / 100.0F);
					return;
				case PHANTOM_SPAWN_EGG:
					playSound(player, Sound.ENTITY_PHANTOM_AMBIENT, 0.5F, Method.random(100, 120) / 100.0F);
					return;
				case PIG_SPAWN_EGG:
					playSound(player, Sound.ENTITY_PIG_AMBIENT, 0.5F, Method.random(100, 120) / 100.0F);
					return;
				case POLAR_BEAR_SPAWN_EGG:
					playSound(player, Sound.ENTITY_POLAR_BEAR_AMBIENT, 0.5F, Method.random(100, 120) / 100.0F);
					return;
				case PUFFERFISH_SPAWN_EGG:
					playSound(player, Sound.ENTITY_PUFFER_FISH_AMBIENT, 0.5F, Method.random(100, 120) / 100.0F);
					return;
				case RABBIT_SPAWN_EGG:
					playSound(player, Sound.ENTITY_RABBIT_AMBIENT, 0.5F, Method.random(100, 120) / 100.0F);
					return;
				case SALMON_SPAWN_EGG:
					playSound(player, Sound.ENTITY_SALMON_AMBIENT, 0.5F, Method.random(100, 120) / 100.0F);
					return;
				case SHEEP_SPAWN_EGG:
					playSound(player, Sound.ENTITY_SHEEP_AMBIENT, 0.5F, Method.random(100, 120) / 100.0F);
					return;
				case SHULKER_SPAWN_EGG:
					playSound(player, Sound.ENTITY_SHULKER_AMBIENT, 0.5F, Method.random(100, 120) / 100.0F);
					return;
				case SKELETON_HORSE_SPAWN_EGG:
					playSound(player, Sound.ENTITY_SKELETON_HORSE_AMBIENT, 0.5F, Method.random(100, 120) / 100.0F);
					return;
				case SKELETON_SPAWN_EGG:
					playSound(player, Sound.ENTITY_SKELETON_AMBIENT, 0.5F, Method.random(100, 120) / 100.0F);
					return;
				case SILVERFISH_SPAWN_EGG:
					playSound(player, Sound.ENTITY_SILVERFISH_AMBIENT, 0.5F, Method.random(100, 120) / 100.0F);
					return;
				case SLIME_SPAWN_EGG:
					playSound(player, Sound.ENTITY_SLIME_HURT, 0.5F, Method.random(100, 120) / 100.0F);
					return;
				case SQUID_SPAWN_EGG:
					playSound(player, Sound.ENTITY_SQUID_AMBIENT, 0.5F, Method.random(100, 120) / 100.0F);
					return;
				case STRAY_SPAWN_EGG:
					playSound(player, Sound.ENTITY_STRAY_AMBIENT, 0.5F, Method.random(100, 120) / 100.0F);
					return;
				case TROPICAL_FISH_SPAWN_EGG:
					playSound(player, Sound.ENTITY_TROPICAL_FISH_AMBIENT, 0.5F, Method.random(100, 120) / 100.0F);
					return;
				case TURTLE_SPAWN_EGG:
					playSound(player, Sound.ENTITY_TURTLE_AMBIENT_LAND, 0.5F, Method.random(100, 120) / 100.0F);
					return;
				case VEX_SPAWN_EGG:
					playSound(player, Sound.ENTITY_VEX_AMBIENT, 0.5F, Method.random(100, 120) / 100.0F);
					return;
				case VILLAGER_SPAWN_EGG:
					playSound(player, Sound.ENTITY_VILLAGER_AMBIENT, 0.5F, Method.random(100, 120) / 100.0F);
					return;
				case WITCH_SPAWN_EGG:
					playSound(player, Sound.ENTITY_WITCH_AMBIENT, 0.5F, Method.random(100, 120) / 100.0F);
					return;
				case VINDICATOR_SPAWN_EGG:
					playSound(player, Sound.ENTITY_VINDICATOR_AMBIENT, 0.5F, Method.random(100, 120) / 100.0F);
					return;
				case WITHER_SKELETON_SPAWN_EGG:
					playSound(player, Sound.ENTITY_WITHER_SKELETON_AMBIENT, 0.5F, Method.random(100, 120) / 100.0F);
					return;
				case WOLF_SPAWN_EGG:
					playSound(player, Sound.ENTITY_WOLF_AMBIENT, 0.5F, Method.random(100, 120) / 100.0F);
					return;
				case ZOMBIE_HORSE_SPAWN_EGG:
					playSound(player, Sound.ENTITY_ZOMBIE_HORSE_AMBIENT, 0.5F, Method.random(100, 120) / 100.0F);
					return;
				case ZOMBIFIED_PIGLIN_SPAWN_EGG:
					playSound(player, Sound.ENTITY_ZOMBIFIED_PIGLIN_AMBIENT, 0.5F, Method.random(100, 120) / 100.0F);
					return;
				case ZOMBIE_SPAWN_EGG:
					playSound(player, Sound.ENTITY_ZOMBIE_AMBIENT, 0.5F, Method.random(100, 120) / 100.0F);
					return;
				case ZOMBIE_VILLAGER_SPAWN_EGG:
					playSound(player, Sound.ENTITY_ZOMBIE_VILLAGER_AMBIENT, 0.5F, Method.random(100, 120) / 100.0F);
					return;
				case CAT_SPAWN_EGG:
					playSound(player, Sound.ENTITY_CAT_AMBIENT, 0.5F, Method.random(100, 120) / 100.0F);
					return;
				case FOX_SPAWN_EGG:
					playSound(player, Sound.ENTITY_FOX_AMBIENT, 0.5F, Method.random(100, 120) / 100.0F);
					return;
				case PANDA_SPAWN_EGG:
					playSound(player, Sound.ENTITY_PANDA_AMBIENT, 0.5F, Method.random(100, 120) / 100.0F);
					return;
				case PILLAGER_SPAWN_EGG:
					playSound(player, Sound.ENTITY_PILLAGER_AMBIENT, 0.5F, Method.random(100, 120) / 100.0F);
					return;
				case RAVAGER_SPAWN_EGG:
					playSound(player, Sound.ENTITY_RAVAGER_AMBIENT, 0.5F, Method.random(100, 120) / 100.0F);
					return;
				case WANDERING_TRADER_SPAWN_EGG:
					playSound(player, Sound.ENTITY_WANDERING_TRADER_AMBIENT, 0.5F, Method.random(100, 120) / 100.0F);
					return;
				case BEE_SPAWN_EGG:
					playSound(player, Sound.ENTITY_BEE_LOOP, 0.5F, Method.random(100, 120) / 100.0F);
					return;
				case SPAWNER:
					playSound(player, Sound.ENTITY_SILVERFISH_AMBIENT, 0.5F, Method.random(100, 120) / 100.0F);
					return;
				case STONE:
				case STONE_BRICK_SLAB:
				case STONE_BRICK_STAIRS:
				case STONE_BRICKS:
				case STONE_SLAB:
				case CHISELED_STONE_BRICKS:
				case CHISELED_QUARTZ_BLOCK:
				case CHISELED_RED_SANDSTONE:
				case CHISELED_SANDSTONE:
				case COBBLESTONE:
				case COBBLESTONE_SLAB:
				case COBBLESTONE_STAIRS:
				case COBBLESTONE_WALL:
				case MOSSY_COBBLESTONE:
				case MOSSY_COBBLESTONE_WALL:
				case SANDSTONE:
				case SANDSTONE_SLAB:
				case SANDSTONE_STAIRS:
				case CUT_RED_SANDSTONE:
				case CUT_SANDSTONE:
				case RED_SANDSTONE:
				case RED_SANDSTONE_SLAB:
				case RED_SANDSTONE_STAIRS:
				case SMOOTH_RED_SANDSTONE:
				case SMOOTH_SANDSTONE:
				case COAL_BLOCK:
				case LAPIS_BLOCK:
				case COAL_ORE:
				case DIAMOND_ORE:
				case EMERALD_ORE:
				case GOLD_ORE:
				case IRON_ORE:
				case LAPIS_ORE:
				case REDSTONE_ORE:
				case NETHER_QUARTZ_ORE:
				case CRACKED_STONE_BRICKS:
				case END_STONE:
				case END_STONE_BRICKS:
				case GLOWSTONE:
				case MOSSY_STONE_BRICKS:
				case POLISHED_ANDESITE:
				case POLISHED_DIORITE:
				case POLISHED_GRANITE:
				case ANDESITE:
				case GRANITE:
				case DIORITE:
					playSound(player, Sound.BLOCK_STONE_PLACE, 0.5F, Method.random(100, 120) / 100.0F);
					return;
				case NOTE_BLOCK:
				case JUKEBOX:
					try
					{
						NBTCompound blockStateTag = new NBTItem(item).getCompound(CucumberyTag.MINECRAFT_BLOCK_STATE_TAG_KEY);
						String pitchString = blockStateTag.getString("note");
						String instrument = blockStateTag.getString("instrument");
						float pitch = switch (pitchString)
						{
							case "0" -> 0.5F;
							case "1" -> 0.529732F;
							case "2" -> 0.561231F;
							case "3" -> 0.594604F;
							case "4" -> 0.629961F;
							case "5" -> 0.667420F;
							case "6" -> 0.707107F;
							case "7" -> 0.749154F;
							case "8" -> 0.793701F;
							case "9" -> 0.840896F;
							case "10" -> 0.890899F;
							case "11" -> 0.943874F;
							case "12" -> 1F;
							case "13" -> 1.059463F;
							case "14" -> 1.122462F;
							case "15" -> 1.189207F;
							case "16" -> 1.259921F;
							case "17" -> 1.334840F;
							case "18" -> 1.414214F;
							case "19" -> 1.498307F;
							case "20" -> 1.587401F;
							case "21" -> 1.681793F;
							case "22" -> 1.781797F;
							case "23" -> 1.887749F;
							case "24" -> 2F;
							default -> 0F;
						};
						Sound sound = switch (instrument)
						{
							case "banjo" -> Sound.BLOCK_NOTE_BLOCK_BANJO;
							case "bassdrum" -> Sound.BLOCK_NOTE_BLOCK_BASEDRUM;
							case "bass" -> Sound.BLOCK_NOTE_BLOCK_GUITAR;
							case "bell" -> Sound.BLOCK_NOTE_BLOCK_BELL;
							case "bit" -> Sound.BLOCK_NOTE_BLOCK_BIT;
							case "chime" -> Sound.BLOCK_NOTE_BLOCK_CHIME;
							case "cow_bell" -> Sound.BLOCK_NOTE_BLOCK_COW_BELL;
							case "dingeridoo" -> Sound.BLOCK_NOTE_BLOCK_DIDGERIDOO;
							case "flute" -> Sound.BLOCK_NOTE_BLOCK_FLUTE;
							case "iron_xylophone" -> Sound.BLOCK_NOTE_BLOCK_IRON_XYLOPHONE;
							case "pling" -> Sound.BLOCK_NOTE_BLOCK_PLING;
							case "snare" -> Sound.BLOCK_NOTE_BLOCK_SNARE;
							case "hat" -> Sound.BLOCK_NOTE_BLOCK_HAT;
							case "xylophone" -> Sound.BLOCK_NOTE_BLOCK_XYLOPHONE;
							default -> Sound.BLOCK_NOTE_BLOCK_HARP;
						};
						playSound(player, sound, 1F, pitch);
					}
					catch (Exception e)
					{
						random = Method.random(1, 16);
						switch (random)
						{
							case 1 ->
							{
								playSound(player, Sound.BLOCK_NOTE_BLOCK_BASEDRUM, 0.5F, Method.random(50, 200) / 100.0F);
								return;
							}
							case 2 ->
							{
								playSound(player, Sound.BLOCK_NOTE_BLOCK_BASS, 0.5F, Method.random(50, 200) / 100.0F);
								return;
							}
							case 3 ->
							{
								playSound(player, Sound.BLOCK_NOTE_BLOCK_HARP, 0.5F, Method.random(50, 200) / 100.0F);
								return;
							}
							case 4 ->
							{
								playSound(player, Sound.BLOCK_NOTE_BLOCK_HAT, 0.5F, Method.random(50, 200) / 100.0F);
								return;
							}
							case 5 ->
							{
								playSound(player, Sound.BLOCK_NOTE_BLOCK_PLING, 0.5F, Method.random(50, 200) / 100.0F);
								return;
							}
							case 6 ->
							{
								playSound(player, Sound.BLOCK_NOTE_BLOCK_SNARE, 0.5F, Method.random(50, 200) / 100.0F);
								return;
							}
							case 7 ->
							{
								playSound(player, Sound.BLOCK_NOTE_BLOCK_BELL, 0.5F, Method.random(50, 200) / 100.0F);
								return;
							}
							case 8 ->
							{
								playSound(player, Sound.BLOCK_NOTE_BLOCK_CHIME, 0.5F, Method.random(50, 200) / 100.0F);
								return;
							}
							case 9 ->
							{
								playSound(player, Sound.BLOCK_NOTE_BLOCK_FLUTE, 0.5F, Method.random(50, 200) / 100.0F);
								return;
							}
							case 10 ->
							{
								playSound(player, Sound.BLOCK_NOTE_BLOCK_GUITAR, 0.5F, Method.random(50, 200) / 100.0F);
								return;
							}
							case 11 ->
							{
								playSound(player, Sound.BLOCK_NOTE_BLOCK_XYLOPHONE, 0.5F, Method.random(50, 200) / 100.0F);
								return;
							}
							case 12 ->
							{
								playSound(player, Sound.BLOCK_NOTE_BLOCK_BANJO, 0.5F, Method.random(50, 200) / 100.0F);
								return;
							}
							case 13 ->
							{
								playSound(player, Sound.BLOCK_NOTE_BLOCK_BIT, 0.5F, Method.random(50, 200) / 100.0F);
								return;
							}
							case 14 ->
							{
								playSound(player, Sound.BLOCK_NOTE_BLOCK_COW_BELL, 0.5F, Method.random(50, 200) / 100.0F);
								return;
							}
							case 15 ->
							{
								playSound(player, Sound.BLOCK_NOTE_BLOCK_DIDGERIDOO, 0.5F, Method.random(50, 200) / 100.0F);
								return;
							}
							case 16 ->
							{
								playSound(player, Sound.BLOCK_NOTE_BLOCK_IRON_XYLOPHONE, 0.5F, Method.random(50, 200) / 100.0F);
								return;
							}
						}
					}
					return;
				case SAND:
				case RED_SAND:
					playSound(player, Sound.BLOCK_SAND_HIT, 0.5F, Method.random(100, 120) / 100.0F);
					return;
				case ACACIA_BOAT:
				case BIRCH_BOAT:
				case DARK_OAK_BOAT:
				case JUNGLE_BOAT:
				case OAK_BOAT:
				case SPRUCE_BOAT:
					playSound(player, Sound.ENTITY_BOAT_PADDLE_LAND, 2F, Method.random(100, 120) / 100.0F);
					return;
				case RAIL:
				case ACTIVATOR_RAIL:
				case DETECTOR_RAIL:
				case POWERED_RAIL:
				case IRON_BLOCK:
				case GOLD_BLOCK:
				case DIAMOND_BLOCK:
				case EMERALD_BLOCK:
				case REDSTONE_BLOCK:
					playSound(player, Sound.BLOCK_METAL_PLACE, 0.5F, Method.random(100, 120) / 100.0F);
					return;
				case SNOW:
				case SNOW_BLOCK:
				case SNOWBALL:
					playSound(player, Sound.BLOCK_SNOW_PLACE, 0.5F, Method.random(100, 120) / 100.0F);
					return;
				case SCUTE:
				case TURTLE_HELMET:
					playSound(player, Sound.ITEM_ARMOR_EQUIP_TURTLE, 0.5F, Method.random(100, 120) / 100.0F);
					return;
				case TURTLE_EGG:
					playSound(player, Sound.ENTITY_TURTLE_EGG_CRACK, 0.5F, Method.random(100, 120) / 100.0F);
					return;
				case ACACIA_FENCE:
				case ACACIA_LOG:
				case ACACIA_SLAB:
				case STRIPPED_ACACIA_LOG:
				case ACACIA_STAIRS:
				case ACACIA_WOOD:
				case STRIPPED_ACACIA_WOOD:
				case BIRCH_FENCE:
				case BIRCH_LOG:
				case BIRCH_SLAB:
				case STRIPPED_BIRCH_LOG:
				case BIRCH_STAIRS:
				case BIRCH_WOOD:
				case STRIPPED_BIRCH_WOOD:
				case SPRUCE_FENCE:
				case SPRUCE_LOG:
				case SPRUCE_SLAB:
				case STRIPPED_SPRUCE_LOG:
				case SPRUCE_STAIRS:
				case SPRUCE_WOOD:
				case STRIPPED_SPRUCE_WOOD:
				case JUNGLE_FENCE:
				case JUNGLE_LOG:
				case JUNGLE_SLAB:
				case STRIPPED_JUNGLE_LOG:
				case JUNGLE_STAIRS:
				case JUNGLE_WOOD:
				case STRIPPED_JUNGLE_WOOD:
				case OAK_FENCE:
				case OAK_LOG:
				case OAK_SLAB:
				case STRIPPED_OAK_LOG:
				case OAK_STAIRS:
				case OAK_WOOD:
				case STRIPPED_OAK_WOOD:
				case DARK_OAK_FENCE:
				case DARK_OAK_LOG:
				case DARK_OAK_SLAB:
				case STRIPPED_DARK_OAK_LOG:
				case DARK_OAK_STAIRS:
				case DARK_OAK_WOOD:
				case STRIPPED_DARK_OAK_WOOD:
				case PUMPKIN:
				case CARVED_PUMPKIN:
				case JACK_O_LANTERN:
					playSound(player, Sound.BLOCK_WOOD_PLACE, 0.5F, Method.random(100, 120) / 100.0F);
					return;
				case BEE_NEST:
				case BEEHIVE:
					playSound(player, Sound.ENTITY_BEE_POLLINATE, 0.5F, 1F);
					return;
				case HONEY_BLOCK:
				case HONEYCOMB_BLOCK:
					playSound(player, Sound.ENTITY_BEE_POLLINATE, 0.5F, Method.random(80, 120) / 100.0F);
					return;
				case HONEY_BOTTLE:
					playSound(player, Sound.ITEM_HONEY_BOTTLE_DRINK, 0.5F, Method.random(80, 120) / 100.0F);
					return;
				case HONEYCOMB:
					playSound(player, Sound.BLOCK_HONEY_BLOCK_SLIDE, 0.8F, 1.3F);
					return;
				case CROSSBOW:
					playSound(player, Sound.ITEM_CROSSBOW_LOADING_END, 0.8F, Method.random(80, 120) / 100.0F);
					return;
				default:
					playSound(player, Sound.ENTITY_ITEM_FRAME_ROTATE_ITEM, 0.5F, Method.random(80, 100) / 100.0F);
			}
		}
	}

	@NotNull
	public static String getWorldDisplayName(@NotNull World world)
	{
		return Method.getWorldDisplayName(world.getName());
	}

	@NotNull
	public static String getWorldDisplayName(@NotNull String world)
	{
		String worldName = Cucumbery.config.getString("custom-world-name." + world);
		if (worldName != null)
		{
			return MessageUtil.n2s(worldName);
		}
		return world;
	}

	@Nullable
	public static Player getPlayer(@Nullable CommandSender sender, @NotNull String name)
	{
		return getPlayer(sender, name, true);
	}

	@Nullable
	public static Player getPlayer(@Nullable CommandSender sender, @NotNull String name, boolean notice)
	{
		Player player = Bukkit.getServer().getPlayer(name);
		if (player == null)
		{
			for (Player online : Bukkit.getServer().getOnlinePlayers())
			{
				String display = MessageUtil.stripColor(ComponentUtil.serialize(online.displayName()));
				if (display.toLowerCase().contains(name.toLowerCase()))
				{
					return online;
				}
				String listName = MessageUtil.stripColor(ComponentUtil.serialize(online.playerListName()));
				if (listName.toLowerCase().contains(name.toLowerCase()))
				{
					return online;
				}
			}
		}
		return player;
	}

	@Nullable
	public static OfflinePlayer getOfflinePlayer(@Nullable CommandSender sender, @NotNull String name)
	{
		return Method.getOfflinePlayer(sender, name, true);
	}

	@Nullable
	public static OfflinePlayer getOfflinePlayer(@Nullable CommandSender sender, @NotNull String name, boolean notice)
	{
		if (Method.isUUID(name))
		{
			OfflinePlayer offlinePlayer = Bukkit.getServer().getOfflinePlayer(UUID.fromString(name));
			if (offlinePlayer.getName() == null)
			{
				if (notice)
				{
					if (sender != null)
					{
						MessageUtil.noArg(sender, Prefix.NO_PLAYER, name);
					}
				}
				return null;
			}
			return offlinePlayer;
		}
		else
		{
			OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayerIfCached(name);
			if (offlinePlayer != null)
			{
				return offlinePlayer;
			}
			if (Variable.cachedUUIDs.containsKey(name))
			{
				return Bukkit.getServer().getOfflinePlayer(Variable.cachedUUIDs.get(name));
			}
			for (String cache : Variable.cachedUUIDs.keySet())
			{
				if (cache.toLowerCase().contains(name.toLowerCase()))
				{
					return Bukkit.getServer().getOfflinePlayer(Variable.cachedUUIDs.get(cache));
				}
			}
			if (notice && Variable.userDataUUIDs.size() >= 100)
			{
				if (sender != null)
				{
					MessageUtil.sendWarn(sender, "rg255,204;대량(" + Variable.userDataUUIDs.size() + "개)의 유저 데이터 IO에 접근합니다. &c지연이 발생할 수 있습니다");
				}
			}
			else if (notice)
			{
				if (sender != null)
				{
					MessageUtil.sendWarn(sender, "유저 데이터 IO에 접근합니다. 약간의 지연이 발생할 수 있습니다");
				}
			}
			File folder = new File(Cucumbery.getPlugin().getDataFolder() + "/data/UserData");
			if (!folder.exists())
			{
				if (notice)
				{
					if (sender != null)
					{
						MessageUtil.noArg(sender, Prefix.NO_PLAYER, name);
					}
				}
				return null;
			}
			File[] files = folder.listFiles();
			if (files == null)
			{
				files = new File[] {};
			}
			for (File file : files)
			{
				String fileName = file.getName();
				if (fileName.endsWith(".yml"))
				{
					fileName = fileName.substring(0, fileName.length() - 4);
				}
				if (Method.isUUID(fileName))
				{
					UUID uuid = UUID.fromString(fileName);
					CustomConfig customConfig = CustomConfig.getPlayerConfig(uuid);
					YamlConfiguration config = customConfig.getConfig();
					String id = config.getString(UserData.ID.getKey());
					if (id != null && id.toLowerCase().contains(name.toLowerCase()))
					{
						return Bukkit.getServer().getOfflinePlayer(uuid);
					}
					String display = config.getString(UserData.DISPLAY_NAME.getKey());
					if (display != null && MessageUtil.stripColor(ComponentUtil.serialize(ComponentUtil.create(display))).toLowerCase().contains(name.toLowerCase()))
					{
						return Bukkit.getServer().getOfflinePlayer(uuid);
					}
					String listName = config.getString(UserData.PLAYER_LIST_NAME.getKey());
					if (listName != null && MessageUtil.stripColor(ComponentUtil.serialize(ComponentUtil.create(listName))).toLowerCase().contains(name.toLowerCase()))
					{
						return Bukkit.getServer().getOfflinePlayer(uuid);
					}
				}
			}
			if (notice)
			{
				if (sender != null)
				{
					MessageUtil.noArg(sender, Prefix.NO_PLAYER, name);
				}
			}
			return null;
		}
	}

	@NotNull
	public static String getDisplayName(@NotNull OfflinePlayer player)
	{
		try
		{
			String name = player.getName();
			Player online = player.getPlayer();
			if (player.isOnline() && online != null)
			{
				name = ComponentUtil.serialize(online.displayName());
			}
			else
			{
				File file = new File(Cucumbery.getPlugin().getDataFolder() + "/data/UserData/" + player.getUniqueId() + ".yml");
				if (file.exists())
				{
					name = CustomConfig.getPlayerConfig(player.getUniqueId()).getConfig().getString(UserData.DISPLAY_NAME.getKey());
				}
			}
			if (name == null)
			{
				throw new Exception();
			}
			return name;
		}
		catch (Exception e)
		{
			return Method.getName(player);
		}
	}

	@NotNull
	public static String getName(@NotNull OfflinePlayer player)
	{
		if (player.isOnline())
		{
			return Objects.requireNonNull(player.getPlayer()).getName();
		}
		String name = player.getName();
		if (name == null)
		{
			File file = new File(Cucumbery.getPlugin().getDataFolder() + "/data/UserData/" + player.getUniqueId() + ".yml");
			if (file.exists())
			{
				name = CustomConfig.getPlayerConfig(player.getUniqueId()).getConfig().getString(CustomConfig.UserData.ID.getKey());
			}
		}
		if (name == null)
		{
			name = player.getUniqueId().toString();
		}
		return name;
	}

	@SuppressWarnings({ "all" })
	public static boolean isUUID(String input)
	{
		try
		{
			UUID.fromString(input);
			return true;
		}
		catch (Exception e)
		{
			return false;
		}
	}

	public static String[] listToArray(List<String> list)
	{
		String[] ary = new String[list.size()];
		return list.toArray(ary);
	}

	public static List<String> arrayToList(String... array)
	{
		if (array == null)
		{
			return Collections.emptyList();
		}
		return new ArrayList<>(Arrays.asList(array));
	}

	public static List<String> mapToList(HashMap<String, ?> map)
	{
		if (map == null)
		{
			return Collections.emptyList();
		}
		return new ArrayList<>(map.keySet());
	}

	public static List<String> setToList(Set<String> set)
	{
		if (set == null)
		{
			return Collections.emptyList();
		}
		return new ArrayList<>(set);
	}

	public static List<String> enumToList(Enum<?>[] array)
	{
		List<String> list = new ArrayList<>();
		for (Enum<?> e : array)
		{
			if (!(e instanceof EnumHideable enumHideable) || !enumHideable.isHiddenEnum())
			{
				list.add(e.toString().toLowerCase());
			}
		}
		return list;
	}

	@NotNull
	public static List<String> getPluginCommands(@NotNull Plugin plugin)
	{
		String pluginName = plugin.getName();
		List<String> cmds = new ArrayList<>();
		if (plugin instanceof JavaPlugin javaPlugin)
		{
			for (HelpTopic cmdLabel2 : Bukkit.getServer().getHelpMap().getHelpTopics())
			{
				String cmd2 = cmdLabel2.getName();
				cmd2 = cmd2.substring(1);
				Command command = javaPlugin.getCommand(cmd2);
				if (command == null)
				{
					continue;
				}
				cmds.add(command.getName());
				cmds.add(pluginName.toLowerCase() + ":" + command.getName());
				for (String alias : command.getAliases())
				{
					cmds.add(alias);
					cmds.add(pluginName.toLowerCase() + ":" + alias);
				}
			}
		}
		return cmds;
	}

	public static List<String> getAllServerCommands()
	{
		List<String> cmds = new ArrayList<>();
		for (Plugin plugin : Bukkit.getServer().getPluginManager().getPlugins())
		{
			List<String> pluginCmds = Method.getPluginCommands(plugin);
			cmds.addAll(pluginCmds);
		}
		for (HelpTopic cmdLabel : Bukkit.getServer().getHelpMap().getHelpTopics())
		{
			String cmd2 = cmdLabel.getName();
			cmd2 = cmd2.substring(1);
			cmds.add(cmd2);
		}
		// 왜 이놈들은 추가가 안되는가 그래서 수동으로 집어 넣음
		cmds.add("bukkit:?");
		cmds.add("bukkit:about");
		cmds.add("bukkit:help");
		cmds.add("bukkit:pl");
		cmds.add("bukkit:plugins");
		cmds.add("bukkit:reload");
		cmds.add("bukkit:rl");
		cmds.add("bukkit:timings");
		cmds.add("bukkit:ver");
		cmds.add("bukkit:version");

		for (String vanillaCommand : VANILLA_COMMANDS)
		{
			cmds.add(vanillaCommand);
			cmds.add("minecraft:" + vanillaCommand);
		}

		return cmds;
	}

	public static boolean hasPermission(CommandSender sender, Permission permission, boolean notice)
	{
		return hasPermission(sender, permission.toString(), notice);
	}

	public static boolean hasPermission(CommandSender sender, String permission, boolean notice)
	{
		if (sender.hasPermission(permission) || sender instanceof ConsoleCommandSender || sender instanceof BlockCommandSender)
		{
			return true;
		}
		if (notice)
		{
			sender.sendMessage(ComponentUtil.translate("command.unknown.command", NamedTextColor.RED));
			sender.sendMessage(ComponentUtil.translate("command.context.here", NamedTextColor.RED).decoration(TextDecoration.ITALIC, State.TRUE));
			MessageUtil.consoleSendMessage(Prefix.INFO_WARN, sender, "translate:이(가) 권한이 부족하여 명령어 사용에 실패했습니다");
		}
		return false;
	}

	/**
	 * 명령어의 구문 형식을 반환합니다.
	 *
	 * @param cmd
	 * 		구문 형식을 가져올 명령어
	 * @return 명령어의 구문 형식
	 */
	public static String getUsage(Command cmd)
	{
		return cmd.getUsage().replace("/<command> ", "").replace("/<command>", "");
	}

	/**
	 * 입력한 문자열이 배열에 포함되어 있는 문자열과 일치하는지 확인합니다.
	 *
	 * @param input
	 * 		일치하는지 확일할 문자열
	 * @param args
	 * 		문자열 배열
	 * @return 하나라도 일치하는게 있을 경우 true, 아닐 경우 false
	 */
	public static boolean equals(String input, List<String> args)
	{
		for (String arg : args)
		{
			if (input.equals(arg))
			{
				return true;
			}
		}
		return false;
	}

	/**
	 * 입력한 문자열이 배열에 포함되어 있는 문자열과 일치하는지 확인합니다.
	 *
	 * @param input
	 * 		일치하는지 확일할 문자열
	 * @param args
	 * 		문자열 배열
	 * @return 하나라도 일치하는게 있을 경우 true, 아닐 경우 false
	 */
	public static boolean equals(String input, String... args)
	{
		return Method.equals(input, Method.arrayToList(args));
	}

	/**
	 * 입력한 문자열이 배열에 포함되어 있는 문자열과 일치하는지 확인합니다. 대소문자를 구별하지 않습니다.
	 *
	 * @param input
	 * 		일치하는지 확일할 문자열
	 * @param args
	 * 		문자열 배열
	 * @return 하나라도 일치하는게 있을 경우 true, 아닐 경우 false
	 */
	public static boolean equalsIgnoreCase(String input, List<String> args)
	{
		for (String arg : args)
		{
			if (arg.equalsIgnoreCase(input))
			{
				return true;
			}
		}
		return false;
	}

	public static boolean startsWith(String input, String... args)
	{
		return Method.startsWith(input, false, args);
	}

	public static boolean startsWith(String input, List<String> args)
	{
		return Method.startsWith(input, args, false);
	}

	public static boolean startsWith(String input, boolean reverse, String... args)
	{
		return Method.startsWith(input, Method.arrayToList(args), reverse);
	}

	public static boolean allStartsWith(String input, boolean reverse, String... args)
	{
		if (input == null || args == null)
		{
			return false;
		}
		for (String arg : args)
		{
			if (arg == null)
			{
				continue;
			}
			if ((!reverse && !input.startsWith(arg)) || (reverse && !arg.startsWith(input)))
			{
				return false;
			}
		}
		return true;
	}

	public static boolean startsWith(String input, List<String> args, boolean reverse)
	{
		if (input == null || args == null)
		{
			return false;
		}
		for (String arg : args)
		{
			if (arg == null)
			{
				continue;
			}
			if ((!reverse && input.startsWith(arg)) || (reverse && arg.startsWith(input)))
			{
				return true;
			}
		}
		return false;
	}

	/**
	 * 입력한 문자열이 배열에 포함되어 있는 문자열과 일치하는지 확인합니다. 대소문자를 구별하지 않습니다.
	 *
	 * @param input
	 * 		일치하는지 확일할 문자열
	 * @param args
	 * 		문자열 배열
	 * @return 하나라도 일치하는게 있을 경우 true, 아닐 경우 false
	 */
	public static boolean equalsIgnoreCase(String input, Set<String> args)
	{
		return Method.equalsIgnoreCase(input, Method.setToList(args));
	}

	/**
	 * 입력한 문자열이 배열에 포함되어 있는 문자열과 일치하는지 확인합니다. 대소문자를 구별하지 않습니다.
	 *
	 * @param input
	 * 		일치하는지 확일할 문자열
	 * @param args
	 * 		문자열 배열
	 * @return 하나라도 일치하는게 있을 경우 true, 아닐 경우 false
	 */
	public static boolean equalsIgnoreCase(String input, String... args)
	{
		return Method.equalsIgnoreCase(input, Method.arrayToList(args));
	}

	/**
	 * 입력한 문자열이 배열에 포함되어 있는 문자열에 포함되는지 확인합니다. 대소문자를 구별하지 않습니다.
	 *
	 * @param input
	 * 		일치하는지 확일할 문자열
	 * @param args
	 * 		문자열 배열
	 * @return 하나라도 포함되는게 있을 경우 true, 아닐 경우 false
	 */
	public static boolean containsIgnoreCase(String input, List<String> args)
	{
		for (String arg : args)
		{
			if (input.toLowerCase().contains(arg.toLowerCase()))
			{
				return true;
			}
		}
		return false;
	}

	/**
	 * 입력한 문자열이 배열에 포함되어 있는 문자열에 포함되는지 확인합니다. 대소문자를 구별하지 않습니다.
	 *
	 * @param input
	 * 		일치하는지 확일할 문자열
	 * @param args
	 * 		문자열 배열
	 * @return 하나라도 포함되는게 있을 경우 true, 아닐 경우 false
	 */
	@SuppressWarnings("unused")
	public static boolean containsIgnoreCase(String input, Set<String> args)
	{
		return Method.containsIgnoreCase(input, Method.setToList(args));
	}

	/**
	 * 입력한 문자열이 배열에 포함되어 있는 문자열에 포함되는지 확인합니다. 대소문자를 구별하지 않습니다.
	 *
	 * @param input
	 * 		일치하는지 확일할 문자열
	 * @param args
	 * 		문자열 배열
	 * @return 하나라도 포함되는게 있을 경우 true, 아닐 경우 false
	 */
	@SuppressWarnings("unused")
	public static boolean containsIgnoreCase(String input, HashMap<String, ?> args)
	{
		return Method.containsIgnoreCase(input, Method.mapToList(args));
	}

	/**
	 * 입력한 문자열이 배열에 포함되어 있는 문자열에 포함되는지 확인합니다. 대소문자를 구별하지 않습니다.
	 *
	 * @param input
	 * 		일치하는지 확일할 문자열
	 * @param args
	 * 		문자열 배열
	 * @return 하나라도 포함되는게 있을 경우 true, 아닐 경우 false
	 */
	public static boolean containsIgnoreCase(String input, String... args)
	{
		return Method.containsIgnoreCase(input, Method.arrayToList(args));
	}

	/**
	 * 입력한 문자열이 배열에 포함되어 있는 문자열에 포함되는지 확인합니다.
	 *
	 * @param input
	 * 		일치하는지 확일할 문자열
	 * @param args
	 * 		문자열 배열
	 * @return 하나라도 포함되는게 있을 경우 true, 아닐 경우 false
	 */
	public static boolean contains(String input, List<String> args)
	{
		for (String arg : args)
		{
			if (input.contains(arg))
			{
				return true;
			}
		}
		return false;
	}

	/**
	 * 입력한 문자열이 배열에 포함되어 있는 문자열에 포함되는지 확인합니다.
	 *
	 * @param input
	 * 		일치하는지 확일할 문자열
	 * @param args
	 * 		문자열 배열
	 * @return 하나라도 포함되는게 있을 경우 true, 아닐 경우 false
	 */
	public static boolean contains(String input, String... args)
	{
		return Method.contains(input, Method.arrayToList(args));
	}

	/**
	 * 입력한 문자열에 --noph, --noeval --nocolor 문자열을 감지하여 포함되어 있지 않으면 파싱해서 반환합니다.
	 *
	 * @param input
	 * 		파싱할 문자열
	 * @return 파싱된 문자열
	 */
	public static String parseCommandString(CommandSender sender, String input)
	{
		if (!input.contains("--noph"))
		{
			input = PlaceHolderUtil.placeholder(sender, input, null);
		}
		else
		{
			input = input.replaceFirst("--noph", "");
		}
		if (!input.contains("--nocolor"))
		{
			input = MessageUtil.n2s(input);
		}
		else
		{
			input = input.replaceFirst("--nocolor", "");
		}
		if (input.contains("--strip"))
		{
			input = MessageUtil.stripColor(input.replaceFirst("--strip", ""));
		}
		if (!input.contains("--noeval"))
		{
			input = PlaceHolderUtil.evalString(input);
		}
		else
		{
			input = input.replaceFirst("--noeval", "");
		}
		return input;
	}

	/**
	 * 해당 플레이어가 아이템 설명을 추가해야하는지 제거해야하는지를 반환한다.
	 *
	 * @param player
	 * 		해당 플레이어
	 * @return 추가/제거 여부
	 */
	public static boolean usingLoreFeature(@NotNull Player player)
	{
		return true;
	}

	public static boolean usingLoreFeature(@NotNull Location location)
	{
		return true;
	}

	/**
	 * 아이템의 정보를 갱신하여 CustomName의 값을 올바르게 수정합니다.
	 *
	 * @param itemEntity
	 * 		갱신할 아이템 개체
	 */
	public static void updateItem(@NotNull Item itemEntity)
	{
		updateItem(itemEntity, 0);
	}

	/**
	 * 아이템의 정보를 갱신하여 CustomName의 값을 올바르게 수정합니다.
	 *
	 * @param itemEntity
	 * 		갱신할 아이템 개체
	 * @param mergeAmount
	 * 		ItemMergeEvent에서 개수를 추가
	 */
	public static void updateItem(@NotNull Item itemEntity, int mergeAmount)
	{
		if (!itemEntity.isValid())
		{
			return;
		}
		ItemStack item = itemEntity.getItemStack();
		// 아이템 제거
		{
			if (NBTAPI.isRestricted(item, Constant.RestrictionType.NO_ITEM_EXISTS))
			{
				itemEntity.remove();
				return;
			}
		}
		// 아이템 이름 설정
		{
			Component component = ItemNameUtil.itemName(item);
			int amount = item.getAmount() + mergeAmount;
			if (amount > 1)
			{
				component = ComponentUtil.translate("%s (%s)", component, Constant.THE_COLOR_HEX + amount);
			}
			itemEntity.customName(component);
		}
		NBTCompound itemTag = NBTAPI.getMainCompound(item);
		// 아이템 무적 모드 설정
		{
			NBTList<String> extraTags = NBTAPI.getStringList(itemTag, CucumberyTag.EXTRA_TAGS_KEY);
			if (NBTAPI.arrayContainsValue(extraTags, Constant.ExtraTag.INVULNERABLE))
			{
				itemEntity.setInvulnerable(true);
			}
		}
		// 아이템 설명 업데이트
		{
			if (Method.usingLoreFeature(itemEntity.getLocation()))
			{
				ItemLore.setItemLore(item);
			}
			else
			{
				ItemLore.removeItemLore(item);
			}
		}
		// 이름이 표시되지 않을 아이템이면 표시하지 않고 빠꾸
		{
			NBTList<String> hideFlags = NBTAPI.getStringList(itemTag, CucumberyTag.HIDE_FLAGS_KEY);
			if (NBTAPI.arrayContainsValue(hideFlags, Constant.CucumberyHideFlag.CUSTOM_NAME))
			{
				itemEntity.setCustomNameVisible(false);
				return;
			}
		}
		CustomMaterial customMaterial = CustomMaterial.itemStackOf(item);
		// 일부 커스텀 아이템은 별도의 이름 표기 규칙을 가짐 (적용 후 return)
		{
			if (customMaterial != null)
			{
				switch (customMaterial)
				{
					case CORE_GEMSTONE, CORE_GEMSTONE_EXPERIENCE, CORE_GEMSTONE_MIRROR, CORE_GEMSTONE_MITRA ->
					{
						itemEntity.setCustomNameVisible(false);
						return;
					}
					case RUNE_DESTRUCTION, RUNE_EARTHQUAKE ->
					{
						itemEntity.setCustomNameVisible(true);
						return;
					}
				}
			}
		}
		// ProtocolLib 사용 시 플레이어마다 아이템 이름 표시를 다르게 함
		// 버그로 인해 비활성화
		//    if (Cucumbery.using_ProtocolLib)
		if (false)
		{
			itemEntity.setCustomNameVisible(false);
			Bukkit.getScheduler().runTaskLaterAsynchronously(Cucumbery.getPlugin(), () ->
			{
				ProtocolManager protocolManager = ProtocolLibrary.getProtocolManager();
				try
				{
					for (Player player : Bukkit.getOnlinePlayers())
					{
						if (!player.getWorld().getName().equals(itemEntity.getWorld().getName()))
						{
							continue;
						}
						if (player.getLocation().distance(itemEntity.getLocation()) > 100d)
						{
							continue;
						}
						boolean showDrop = UserData.SHOW_DROPPED_ITEM_CUSTOM_NAME.getBoolean(player);
						if (UserData.FORCE_HIDE_DROPPED_ITEM_CUSTOM_NAME.getBoolean(player))
						{
							showDrop = false;
						}
						PacketContainer updateComponent = protocolManager.createPacket(Play.Server.ENTITY_METADATA);
						StructureModifier<List<WrappedDataValue>> watchableAccessor = updateComponent.getDataValueCollectionModifier();
						List<WrappedDataValue> values = Lists.newArrayList(new WrappedDataValue(3, WrappedDataWatcher.Registry.get(Boolean.class), showDrop));
						watchableAccessor.write(0, values);
						updateComponent.getIntegers().write(0, itemEntity.getEntityId());
						protocolManager.sendServerPacket(player, updateComponent);
					}
				}
				catch (ConcurrentModificationException ignored)
				{
				}
				catch (IllegalArgumentException e)
				{
					if (!e.getMessage().contains("No serializer found for class java.lang.Boolean"))
					{
						Cucumbery.getPlugin().getLogger().warning(e.getMessage());
					}
				}
				catch (Exception e)
				{
					Cucumbery.getPlugin().getLogger().warning(e.getMessage());
				}
			}, 0L);
		}
		// 이외의 경우에는 config 설정에 따른 전역 아이템 표시 여부 결정
		else if (Cucumbery.config.getBoolean("name-tag-on-item-spawn"))
		{
			itemEntity.setCustomNameVisible(true);
		}
	}

	/**
	 * config에 있는 위치 좌표 목록이 주어진 location을 포함하는지 확인합니다.
	 * <p>
	 * 현재 위치가 test_world이고 xyz 좌표가 각각 10 100 1000일 때 config가 다음과 같다면 포함됩니다.
	 * <p>
	 * config-options: - test_world
	 * <p>
	 * 이거나
	 * <p>
	 * config-options: - test_world,-10~100,10~100,1000~1000
	 * <p>
	 * 인 경우입니다.
	 *
	 * @param location
	 * 		확인할 위치값
	 * @return location을 포함하면 true
	 */
	public static boolean configContainsLocation(Location location, List<String> configList)
	{
		if (location == null)
		{
			return false;
		}
		if (configList == null || configList.isEmpty())
		{
			return false;
		}
		String world = location.getWorld().getName();
		double x = location.getX();
		double y = location.getY();
		double z = location.getZ();
		for (String config : configList)
		{
			String[] tokens = config.split(",");
			String configWorld = tokens[0];
			if (world.equals(configWorld))
			{
				if (tokens.length == 1) // 월드 이름만 적었을 경우 월드 이름만 일치하면 true
				{
					return true;
				}
				else
				{
					try
					{
						String[] xSplit = tokens[1].split("~");
						String[] ySplit = tokens[2].split("~");
						String[] zSplit = tokens[3].split("~");
						double xFrom = Double.parseDouble(xSplit[0]);
						double xTo = Double.parseDouble(xSplit[1]);
						double yFrom = Double.parseDouble(ySplit[0]);
						double yTo = Double.parseDouble(ySplit[1]);
						double zFrom = Double.parseDouble(zSplit[0]);
						double zTo = Double.parseDouble(zSplit[1]);
						if (x >= xFrom && x <= xTo && y >= yFrom && y <= yTo && z >= zFrom && z <= zTo)
						{
							return true;
						}
					}
					catch (Exception ignored)
					{
					}
				}
			}
		}
		return false;
	}

	public static boolean configContainsLocation(World world, List<String> configList)
	{
		if (world == null)
		{
			return false;
		}
		if (configList == null || configList.isEmpty())
		{
			return false;
		}
		String worldName = world.getName();
		return configList.contains(worldName);
	}

	/**
	 * 해당 부울의 배열의 모든 값이 true인지 확인합니다.
	 *
	 * @param array
	 * 		확인할 배열
	 * @return 모든 값이 true이면 true 반환, 아닐 경우 false
	 */
	public static boolean allIsTrue(boolean[] array)
	{
		boolean allIsTrue = true;
		for (boolean b : array)
		{
			if (!b)
			{
				allIsTrue = false;
				break;
			}
		}
		return allIsTrue;
	}

	/**
	 * value 문자열 사이사이에 token 문자열을 끼워넣어 반홥합니다. 맨 처음에만 넣고 끝에는 넣지 않습니다. 예시: value : abc, token: z, return : zazbzc
	 *
	 * @param value
	 * 		문자열
	 * @param token
	 * 		토큰
	 * @return 반환값
	 */
	public static String format(String value, String token)
	{
		char[] chars = value.toCharArray();
		StringBuilder builder = new StringBuilder();
		for (char character : chars)
		{
			builder.append(token).append(character);
		}
		return builder.toString();
	}

	/**
	 * value 문자열 사이사이에 있는 token 문자열을 제거하여 반환합니다. 맨 처음 문자가 토큰이 아니라면 그대로 반환합니다.
	 *
	 * @param value
	 * 		문자열
	 * @param token
	 * 		토큰
	 * @return 반환값
	 */
	@NotNull
	public static String deformat(@NotNull String value, @NotNull String token)
	{
		if (!value.startsWith(token))
		{
			return value;
		}
		StringBuilder builder = new StringBuilder();
		char[] chars = value.toCharArray();
		for (int i = 1; i < chars.length; i += 2)
		{
			builder.append(chars[i]);
		}
		return builder.toString();
	}

	@NotNull
	public static List<String> sort(@NotNull List<String> list)
	{
		for (int i = 0; i < list.size(); i++)
		{
			String s = list.get(i);
			// for legacy
			try
			{
				if (s.startsWith("{\""))
				{
					s = ComponentUtil.serialize(GsonComponentSerializer.gson().deserialize(s));
				}
			}
			catch (Exception ignored)
			{

			}
			s = MessageUtil.stripColor(s);
			if (s.length() > 123)
			{
				s = "죄송합니다! 메시지가 너무 길었습니다! : " + s.substring(0, 100) + "...";
			}
			boolean escaped = s.startsWith(Constant.TAB_COMPLETER_QUOTE_ESCAPE);
			if (escaped)
			{
				s = s.replace(Constant.TAB_COMPLETER_QUOTE_ESCAPE, "");
			}
			if (s.contains(" ") && (!(s.startsWith("(") || s.endsWith(")") || s.startsWith("<") || s.endsWith(">") || s.startsWith("[") || s.endsWith("]"))
					|| !escaped))
			{
				if (s.contains("'"))
				{
					s = s.replace("'", "''");
				}
				s = "'" + s + "'";
			}
			list.set(i, s);
		}
		Collections.sort(list);
		return list.stream().distinct().collect(Collectors.toList());
	}

	public static void reinforceSound(Player player, ReinforceSound type, boolean use, ReinforceType reinforceType)
	{
		switch (type)
		{
			case DESTROY:
				if (use)
				{
					playSound(player, "reinforce_destroy", SoundCategory.PLAYERS, 1F, 1F);
				}
				else
				{
					playSound(player, Sound.ENTITY_ZOMBIE_ATTACK_IRON_DOOR, SoundCategory.PLAYERS, 1F, 1F);
					playSound(player, Sound.ENTITY_BLAZE_HURT, SoundCategory.PLAYERS, 1F, 0.5F);
					playSound(player, Sound.ENTITY_SHULKER_BULLET_HIT, SoundCategory.PLAYERS, 1F, 0.8F);
					playSound(player, Sound.ENTITY_SKELETON_DEATH, SoundCategory.PLAYERS, 1F, 2F);
					playSound(player, Sound.ENTITY_SKELETON_DEATH, SoundCategory.PLAYERS, 1F, 1F);
					playSound(player, Sound.ENTITY_FIREWORK_ROCKET_LARGE_BLAST, SoundCategory.PLAYERS, 1F, 0.5F);
					playSound(player, Sound.ENTITY_FIREWORK_ROCKET_LARGE_BLAST, SoundCategory.PLAYERS, 1F, 1F);
					playSound(player, Sound.ENTITY_CREEPER_DEATH, SoundCategory.PLAYERS, 1F, 0.5F);

					Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(Cucumbery.getPlugin(), () -> player.stopSound(Sound.ENTITY_CREEPER_DEATH), 10L);
				}
				break;
			case FAIL:
				if (use)
				{
					playSound(player, "reinforce_fail", SoundCategory.PLAYERS, 1F, 1F);
				}
				else
				{
					for (int i = 5; i < 8; i++)
					{
						playSound(player, Sound.ENTITY_ZOMBIE_ATTACK_IRON_DOOR, SoundCategory.PLAYERS, 1F, i / 10F);
					}

					Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(Cucumbery.getPlugin(), () ->
					{
						for (int i = 5; i < 8; i++)
						{
							playSound(player, Sound.ENTITY_ZOMBIE_ATTACK_IRON_DOOR, SoundCategory.PLAYERS, 1F, i / 10F);
						}
					}, 1L);

					Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(Cucumbery.getPlugin(), () ->
					{
						for (int i = 5; i < 8; i++)
						{
							playSound(player, Sound.ENTITY_ZOMBIE_ATTACK_IRON_DOOR, SoundCategory.PLAYERS, 1F, i / 10F);
						}
					}, 2L);
				}
				break;
			case OPERATION:
				if (use && reinforceType == ReinforceType.SCROLL)
				{
					playSound(player, "reinforce_start_2", SoundCategory.PLAYERS, 1F, 1F);
				}
				else if (use && reinforceType == ReinforceType.COMMAND)
				{
					playSound(player, "reinforce_start", SoundCategory.PLAYERS, 1F, 1F);
				}
				else
				{
					playSound(player, Sound.BLOCK_ANVIL_LAND, SoundCategory.PLAYERS, 1F, 2F);

					Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(Cucumbery.getPlugin(), () -> playSound(player, Sound.BLOCK_ANVIL_LAND, 1F, 1.98F), 1L);

					Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(Cucumbery.getPlugin(), () -> playSound(player, Sound.BLOCK_ANVIL_LAND, 1F, 2F), 4L);

					Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(Cucumbery.getPlugin(), () -> playSound(player, Sound.BLOCK_ANVIL_LAND, 1F, 1.98F), 5L);

					Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(Cucumbery.getPlugin(), () -> playSound(player, Sound.BLOCK_PORTAL_TRIGGER, 1F, 2F), 10L);

					Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(Cucumbery.getPlugin(), () -> player.stopSound(Sound.BLOCK_PORTAL_TRIGGER), 20L);
				}
				break;

			case SUCCESS:
				if (use)
				{
					playSound(player, "reinforce_success", SoundCategory.PLAYERS, 1F, 1F);
				}
				else
				{
					playSound(player, Sound.ENTITY_PLAYER_LEVELUP, SoundCategory.PLAYERS, 1F, 1F);
					playSound(player, Sound.ENTITY_FIREWORK_ROCKET_TWINKLE, SoundCategory.PLAYERS, 1F, 1F);
				}
				break;
			default:
				break;

		}
	}

	/**
	 * 인벤토리가 텅 비었는지 확인합니다.
	 *
	 * @param inventory
	 * 		확일할 인벤토리
	 * @return 인벤토리에 아이템이 아무것도 없으면 true
	 */
	public static boolean inventoryEmpty(Inventory inventory)
	{
		try
		{
			if (inventory == null)
			{
				return true;
			}
			boolean itemExists = false;
			for (int i = 0; i < inventory.getSize(); i++)
			{
				if (ItemStackUtil.itemExists(inventory.getItem(i)))
				{
					itemExists = true;
					break;
				}
			}
			return !itemExists;
		}
		catch (Exception e)
		{
			return true;
		}
	}

	/**
	 * 개체의 attribute 값을 가져와서 보기 좋은 문자열로 반환합니다.
	 *
	 * @param entity
	 * 		값을 가져올 개체
	 * @param attribute
	 * 		값을 가져올 attribute
	 * @return 보기 좋은 문자열
	 */
	public static String attributeString(Attributable entity, Attribute attribute)
	{
		AttributeInstance attr = entity.getAttribute(attribute);
		double attrValue = attr != null ? attr.getValue() : 0;
		double attrBaseValue = attr != null ? attr.getBaseValue() : 0;
		String attrString = Constant.Sosu4.format(attrValue);
		double differ = attrValue - attrBaseValue;
		String differString = (differ >= 0d ? ("&b+" + Constant.Sosu4.format(differ)) : ("&c-" + Constant.Sosu4.format(-differ)));
		if (differ != 0d)
		{
			attrString += " &r(rg255,204;" + Constant.Sosu4.format(attrBaseValue) + "rg255,204; " + differString + "&r)";
		}
		return MessageUtil.n2s(attrString);
	}

	public static String getCurrentTime(Calendar calendar)
	{
		return Method.getCurrentTime(calendar, true, false);
	}

	public static String getCurrentTime(Calendar calendar, boolean all, boolean displayMs)
	{
		int year = calendar.get(Calendar.YEAR), month = calendar.get(Calendar.MONTH) + 1, date = calendar.get(Calendar.DATE), hour = calendar.get(
				Calendar.HOUR_OF_DAY), minute = calendar.get(Calendar.MINUTE), second = calendar.get(Calendar.SECOND), ms = calendar.get(Calendar.MILLISECOND);
		if (all)
		{
			String time =
					year + "년 " + (((month >= 10) ? "" : "0") + month) + "월 " + (((date >= 10) ? "" : "0") + date) + "일 " + (((hour >= 10) ? "" : "0") + hour) + "시 " + (
							((minute >= 10) ? "" : "0") + minute) + "분 " + (((second >= 10) ? "" : "0") + second) + "초 " + (displayMs ? (
							(((ms < 100) ? "0" : "") + ((ms < 10) ? "0" : "") + ms) + "ms") : "");
			if (time.endsWith(" "))
			{
				time = time.substring(0, time.length() - 1);
			}
			return time;
		}
		return year + "-" + (((month >= 10) ? "" : "0") + month) + "-" + (((date >= 10) ? "" : "0") + date);
	}

	public static String getCurrentTime(long currentTimeMillis)
	{
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(currentTimeMillis);
		return getCurrentTime(calendar);
	}

	public static long getCurrentTime(String timeformat)
	{
		try
		{
			long currentTime = 0L;
			String[] split = timeformat.split(" ");
			int year = 0, month = 0, date = 0, hour = 0, minute = 0, second = 0;
			for (String token : split)
			{
				if (token.endsWith("년"))
				{
					year = Integer.parseInt(token.replace("년", ""));
				}
				if (token.endsWith("개월"))
				{
					month = Integer.parseInt(token.replace("개월", ""));
				}
				if (token.endsWith("일"))
				{
					date = Integer.parseInt(token.replace("일", ""));
				}
				if (token.endsWith("시간"))
				{
					hour = Integer.parseInt(token.replace("시간", ""));
				}
				if (token.endsWith("분"))
				{
					minute = Integer.parseInt(token.replace("분", ""));
				}
				if (token.endsWith("초"))
				{
					second = Integer.parseInt(token.replace("초", ""));
				}
			}
			currentTime += second * 1000L;
			currentTime += minute * 60L * 1000L;
			currentTime += hour * 60L * 60L * 1000L;
			currentTime += date * 24L * 60L * 60L * 1000L;
			currentTime += month * 30L * 24L * 60L * 60L * 1000L;
			currentTime += year * 365L * 24L * 60L * 60L * 1000L;
			return currentTime;
		}
		catch (Exception e)
		{
			return System.currentTimeMillis();
		}
	}

	public static long getTimeDifference(Calendar end, String timeformat)
	{
		try
		{
			String[] split = timeformat.split(" ");
			int year = 0, month = 0, date = 0, hour = 0, minute = 0, second = 0;
			for (String token : split)
			{
				if (token.endsWith("년"))
				{
					year = Integer.parseInt(token.replace("년", ""));
				}
				if (token.endsWith("월"))
				{
					month = Integer.parseInt(token.replace("월", ""));
				}
				if (token.endsWith("일"))
				{
					date = Integer.parseInt(token.replace("일", ""));
				}
				if (token.endsWith("시"))
				{
					hour = Integer.parseInt(token.replace("시", ""));
				}
				if (token.endsWith("분"))
				{
					minute = Integer.parseInt(token.replace("분", ""));
				}
				if (token.endsWith("초"))
				{
					second = Integer.parseInt(token.replace("초", ""));
				}
			}
			Calendar cur = Calendar.getInstance();
			cur.set(Calendar.YEAR, year);
			cur.set(Calendar.MONTH, month - 1);
			cur.set(Calendar.DATE, date);
			cur.set(Calendar.HOUR_OF_DAY, hour);
			cur.set(Calendar.MINUTE, minute);
			cur.set(Calendar.SECOND, second);
			return cur.getTimeInMillis() - end.getTimeInMillis();
		}
		catch (Exception e)
		{
			return 0L;
		}
	}

	/**
	 * 명령어를 실행합니다.
	 *
	 * @param sender
	 * 		명령어를 실행하는 주체
	 * @param command
	 * 		실행할 명령어
	 * @param placeholder
	 * 		플레이스 홀더 사용 여부
	 * @param calculate
	 * 		수식 계산 여부
	 */
	public static void performCommand(@NotNull CommandSender sender, @NotNull String command, boolean placeholder, boolean calculate,
			@Nullable Object extraParams)
	{
		if (placeholder)
		{
			if (command.contains("--noph"))
			{
				command = command.replaceFirst("--noph", "");
				command = PlaceHolderUtil.placeholder(sender, command, extraParams, true);
			}
			else
			{
				command = PlaceHolderUtil.placeholder(sender, command, extraParams);
			}
		}
		if (calculate)
		{
			if (command.contains("--noeval") || command.contains("--nocalc"))
			{
				command = command.replaceFirst("--noeval", "");
				command = command.replaceFirst("--nocalc", "");
			}
			else
			{
				command = PlaceHolderUtil.evalString(command);
			}
		}
		boolean senderIsPlayer = sender instanceof Player;
		boolean isOp = !senderIsPlayer || sender.isOp();
		if (command.startsWith("op:"))
		{
			if (senderIsPlayer)
			{
				Player player = (Player) sender;
				if (!isOp)
				{
					player.setOp(true);
				}
				player.performCommand(command.substring(3));
				if (!isOp)
				{
					player.setOp(false);
				}
			}
			else
			{
				Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), command.substring(3));
			}
		}
		else if (command.startsWith("console:"))
		{
			Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), command.substring(8));
		}
		else if (command.startsWith("chat:") && senderIsPlayer)
		{
			((Player) sender).chat(command.substring(5));
		}
		else if (command.startsWith("opchat:") && senderIsPlayer)
		{
			Player player = (Player) sender;
			if (!isOp)
			{
				player.setOp(true);
			}
			player.chat(command.substring(7));
			if (!isOp)
			{
				player.setOp(false);
			}
		}
		else if (command.startsWith("commandpack:"))
		{
			command = command.substring(12);
			String[] split = command.split(" ");
			String packName = MessageUtil.listToString(" ", 1, split.length, split);
			Method.commandPack(sender, split[0], packName);
		}
		else
		{
			if (senderIsPlayer)
			{
				((Player) sender).performCommand(command);
			}
			else
			{
				Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), command);
			}
		}
	}

	public static void commandPack(CommandSender sender, String fileName, String packName)
	{
		try
		{
			YamlConfiguration config = Variable.commandPacks.get(fileName);
			List<String> commands = null;
			boolean configExists = true;
			if (config == null)
			{
				configExists = false;
			}
			else
			{
				commands = config.getStringList(packName);
			}
			configExists = configExists && !commands.isEmpty();
			if (!configExists)
			{
				MessageUtil.sendError(sender, "해당 명령어 팩은 존재하지 않습니다. 관리자에게 문의해주세요. (파일 이름 : rg255,204;" + fileName + ".yml&r, 팩 이름 : rg255,204;" + packName + "&r)");
				return;
			}
			List<String> commandList = new ArrayList<>();
			for (String command : commands)
			{
				if (!command.startsWith("pack:"))
				{
					commandList.add(command);
				}
				else
				{
					List<String> loadPack = config.getStringList(command.substring(5));
					for (String loadCommand : loadPack)
					{
						if (!loadCommand.startsWith("pack:"))
						{
							commandList.add(loadCommand);
						}
						else
						{
							loadPack = config.getStringList(loadCommand.substring(5));
							for (String loadCommand2 : loadPack)
							{
								if (!loadCommand2.startsWith("pack:"))
								{
									commandList.add(loadCommand2);
								}
								else
								{
									loadPack = config.getStringList(loadCommand2.substring(5));
									for (String loadCommand3 : loadPack)
									{
										if (!loadCommand3.startsWith("pack:"))
										{
											commandList.add(loadCommand3);
										}
										else
										{
											loadPack = config.getStringList(loadCommand3.substring(5));

											for (String loadCommand4 : loadPack)
											{
												if (!loadCommand4.startsWith("pack:"))
												{
													commandList.add(loadCommand4);
												}
												else
												{
													loadPack = config.getStringList(loadCommand4.substring(5));
													for (String loadCommand5 : loadPack)
													{
														if (!loadCommand5.startsWith("pack:"))
														{
															commandList.add(loadCommand5);
														}
														else
														{
															loadPack = config.getStringList(loadCommand5.substring(5));
															for (String loadCommand6 : loadPack)
															{
																if (!loadCommand6.startsWith("pack:"))
																{
																	commandList.add(loadCommand6);
																}
															}
														}
													}
												}
											}
										}
									}
								}
							}
						}
					}
				}
			}
			if (commandList.isEmpty())
			{
				MessageUtil.sendError(sender,
						"해당 명령어 팩에는 명령어가 존재하지 않습니다. 관리자에게 문의해주세요. (파일 이름 : rg255,204;" + fileName + ".yml&r, 팩 이름 : rg255,204;" + packName + "&r)");
				return;
			}
			for (String command : commandList)
			{
				Method.performCommand(sender, command, true, true, null);
			}
		}
		catch (Exception e)
		{
			Cucumbery.getPlugin().getLogger().warning(e.getMessage());
		}
	}

	public static List<String> tabCompleterList(String[] args, List<String> list, String key)
	{
		return Method.tabCompleterList(args, list, key, false);
	}

	public static List<String> tabCompleterList(String[] args, List<String> list, String key, boolean ignoreEmpty)
	{
		String tabArg = args[args.length - 1];
		if ((!ignoreEmpty && tabArg.equals("") && (list == null || list.isEmpty())) || (ignoreEmpty && (list == null || list.isEmpty())))
		{
			return Collections.singletonList(key);
		}
		list.remove(null);
		int length = tabArg.length();
		if (!tabArg.equals(""))
		{
			if (length == 1 && tabArg.charAt(0) >= '가' && tabArg.charAt(0) <= '힣')
			{
				length = 3;
			}
			else
			{
				for (char c : tabArg.toCharArray())
				{
					if (c >= '가' && c <= '힣')
					{
						length++;
					}
				}
			}
			List<String> returnValue = new ArrayList<>();
			String replace = tabArg.replace(" ", "").replace("_", "").toLowerCase();
			for (String str : list)
			{
				final boolean contains = str.toLowerCase().replace(" ", "").replace("_", "").contains(replace);
				if ((length <= 2 && str.replace(Constant.TAB_COMPLETER_QUOTE_ESCAPE, "").toLowerCase().startsWith(replace)) || (length >= 3 && contains))
				{
					returnValue.add(str);
				}
			}
			if (returnValue.isEmpty() && !key.equals("") && !ignoreEmpty)
			{
				return Collections.singletonList(
						"'" + tabArg + "'" + MessageUtil.getFinalConsonant(tabArg, MessageUtil.ConsonantType.은는) + " 잘못되거나 알 수 없는 " + key.replace("<", "").replace(">", "")
								.replace("[", "").replace("]", "") + "입니다" + ".");
			}
			returnValue = Method.sort(returnValue);
			if ((ignoreEmpty && returnValue.isEmpty()) || (!key.equals("") && returnValue.size() == 1 && returnValue.get(0).equalsIgnoreCase(tabArg)))
			{
				return Collections.singletonList(key);
			}
			return returnValue;
		}
		return Method.sort(list);
	}

	public static List<String> tabCompleterList(String[] args, String key, String... list)
	{
		return Method.tabCompleterList(args, Method.arrayToList(list), key, false);
	}

	public static List<String> tabCompleterList(String[] args, String key, boolean ignoreEmpty, String... list)
	{
		return Method.tabCompleterList(args, Method.arrayToList(list), key, ignoreEmpty);
	}

	public static List<String> tabCompleterList(String[] args, Set<String> set, String key)
	{
		return Method.tabCompleterList(args, Method.setToList(set), key, false);
	}

	public static List<String> tabCompleterList(String[] args, Set<String> set, String key, boolean ignoreEmpty)
	{
		return Method.tabCompleterList(args, Method.setToList(set), key, ignoreEmpty);
	}

	public static List<String> tabCompleterList(String[] args, Enum<?>[] array, String key)
	{
		return Method.tabCompleterList(args, array, key, false);
	}

	public static List<String> tabCompleterList(String[] args, Enum<?>[] array, String key, boolean ignoreEmpty)
	{
		return Method.tabCompleterList(args, enumToList(array), key, ignoreEmpty);
	}

	@NotNull
	public static List<String> tabCompleterList(@NotNull String[] args, @NotNull Map<?, ?> map, @NotNull Object key)
	{
		return tabCompleterList(args, map, key, null);
	}

	@NotNull
	public static List<String> tabCompleterList(@NotNull String[] args, @NotNull Map<?, ?> map, @NotNull Object key, Predicate<Object> exclude)
	{
		List<String> list = new ArrayList<>();
		for (Object k : map.keySet())
		{
			if (exclude != null && exclude.test(k))
			{
				continue;
			}
			Object v = map.get(k);
			if (v instanceof CustomEffectType customEffectType && customEffectType.isEnumHidden())
			{
				continue;
			}
			list.add(k.toString());
		}
		return Method.tabCompleterList(args, list, key.toString());
	}

	public static List<String> tabCompleterIntegerRadius(String[] args, int from, int to, String key)
	{
		return Method.tabCompleterIntegerRadius(args, from, to, key, "");
	}

	public static List<String> tabCompleterIntegerRadius(String[] args, int from, int to, String key, String... additionalArgs)
	{
		String tabArg = args[args.length - 1];
		if (additionalArgs != null && ((additionalArgs.length >= 1 && !additionalArgs[0].equals("") && (tabArg.equals("")) || Method.startsWith(tabArg, true,
				additionalArgs))))
		{
			List<String> list = new ArrayList<>(Collections.singletonList(Constant.TAB_COMPLETER_QUOTE_ESCAPE + key));
			list.addAll(Method.arrayToList(additionalArgs));
			return Method.tabCompleterList(args, list, key);
		}
		if (tabArg.equals(""))
		{
			return Collections.singletonList(key);
		}
		if (from == Integer.MIN_VALUE && to == Integer.MAX_VALUE)
		{
			return Collections.singletonList(key);
		}
		if (!MessageUtil.isDouble(null, tabArg, false))
		{
			return Collections.singletonList(tabArg + MessageUtil.getFinalConsonant(tabArg, MessageUtil.ConsonantType.은는) + " 정수가 아닙니다");
		}
		if (!MessageUtil.isInteger(null, tabArg, false))
		{
			return Collections.singletonList(tabArg + MessageUtil.getFinalConsonant(tabArg, MessageUtil.ConsonantType.은는) + " 잘못된 정수입니다");
		}
		int argInt = Integer.parseInt(tabArg);
		tabArg = Constant.Sosu15.format(argInt);
		if (!MessageUtil.checkNumberSize(null, argInt, from, Integer.MAX_VALUE))
		{
			return Collections.singletonList(
					"정수는 " + Constant.Sosu15.format(from) + " 이상이어야 하는데, " + tabArg + MessageUtil.getFinalConsonant(tabArg, MessageUtil.ConsonantType.이가) + " 있습니다");
		}
		if (!MessageUtil.checkNumberSize(null, argInt, Integer.MIN_VALUE, to))
		{
			return Collections.singletonList(
					"정수는 " + Constant.Sosu15.format(to) + " 이하여야 하는데, " + tabArg + MessageUtil.getFinalConsonant(tabArg, MessageUtil.ConsonantType.이가) + " 있습니다");
		}
		char keyLast = key.charAt(key.length() - 1);
		key = key.substring(0, key.length() - 1) + "=" + Constant.Sosu15.format(argInt) + keyLast;
		return Collections.singletonList(key);
	}

	public static List<String> tabCompleterLongRadius(String[] args, long from, long to, String key)
	{
		return Method.tabCompleterLongRadius(args, from, to, key, "");
	}

	public static List<String> tabCompleterLongRadius(String[] args, long from, long to, String key, String... additionalArgs)
	{
		String tabArg = args[args.length - 1];
		if (additionalArgs != null && ((additionalArgs.length >= 1 && !additionalArgs[0].equals("") && (tabArg.equals("")) || Method.startsWith(tabArg, true,
				additionalArgs))))
		{
			List<String> list = new ArrayList<>(Collections.singletonList(Constant.TAB_COMPLETER_QUOTE_ESCAPE + key));
			list.addAll(Method.arrayToList(additionalArgs));
			return Method.tabCompleterList(args, list, key);
		}
		if (tabArg.equals(""))
		{
			return Collections.singletonList(key);
		}
		if (!MessageUtil.isDouble(null, tabArg, false))
		{
			return Collections.singletonList(tabArg + MessageUtil.getFinalConsonant(tabArg, MessageUtil.ConsonantType.은는) + " 정수가 아닙니다");
		}
		if (!MessageUtil.isLong(null, tabArg, false))
		{
			return Collections.singletonList(tabArg + MessageUtil.getFinalConsonant(tabArg, MessageUtil.ConsonantType.은는) + " 잘못된 정수입니다");
		}
		long argLong = Long.parseLong(tabArg);
		tabArg = Constant.Sosu15.format(argLong);
		if (!MessageUtil.checkNumberSize(null, argLong, from, Long.MAX_VALUE))
		{
			return Collections.singletonList(
					"정수는 " + Constant.Sosu15.format(from) + " 이상이어야 하는데, " + tabArg + MessageUtil.getFinalConsonant(tabArg, MessageUtil.ConsonantType.이가) + " 있습니다");
		}
		if (!MessageUtil.checkNumberSize(null, argLong, Long.MIN_VALUE, to))
		{
			return Collections.singletonList(
					"정수는 " + Constant.Sosu15.format(to) + " 이하여야 하는데, " + tabArg + MessageUtil.getFinalConsonant(tabArg, MessageUtil.ConsonantType.이가) + " 있습니다");
		}
		char keyLast = key.charAt(key.length() - 1);
		key = key.substring(0, key.length() - 1) + "=" + Constant.Sosu15.format(argLong) + keyLast;
		return Collections.singletonList(key);
	}

	@SuppressWarnings("unused")
	public static List<String> tabCompleterDoubleRadius(String[] args, String key)
	{
		return Method.tabCompleterDoubleRadius(args, -Double.MAX_VALUE, Double.MAX_VALUE, key);
	}

	@SuppressWarnings("unused")
	public static List<String> tabCompleterDoubleRadius(String[] args, String key, String... additionalArgs)
	{
		return Method.tabCompleterDoubleRadius(args, -Double.MAX_VALUE, Double.MAX_VALUE, key, additionalArgs);
	}

	public static List<String> tabCompleterDoubleRadius(String[] args, double from, double to, String key)
	{
		return Method.tabCompleterDoubleRadius(args, from, false, to, false, key, "");
	}

	public static List<String> tabCompleterDoubleRadius(String[] args, double from, double to, String key, String... additionalArgs)
	{
		return Method.tabCompleterDoubleRadius(args, from, false, to, false, key, additionalArgs);
	}

	public static List<String> tabCompleterDoubleRadius(String[] args, double from, boolean excludeFrom, double to, boolean excludeTo, String key,
			String... additionalArgs)
	{
		String tabArg = args[args.length - 1];
		if (additionalArgs.length >= 1 && !additionalArgs[0].equals("") && (tabArg.equals("") || Method.startsWith(tabArg, true, additionalArgs)))
		{
			List<String> list = new ArrayList<>(Collections.singletonList(Constant.TAB_COMPLETER_QUOTE_ESCAPE + key));
			list.addAll(Method.arrayToList(additionalArgs));
			return Method.tabCompleterList(args, list, key);
		}
		if (tabArg.equals(""))
		{
			return Collections.singletonList(key);
		}
		if (from == -Double.MAX_VALUE && to == Double.MAX_VALUE)
		{
			return Collections.singletonList(key);
		}
		if (!MessageUtil.isDouble(null, tabArg, false))
		{
			return Collections.singletonList(tabArg + MessageUtil.getFinalConsonant(tabArg, MessageUtil.ConsonantType.은는) + " 숫자가 아닙니다");
		}
		double argDouble = Double.parseDouble(tabArg);
		tabArg = Constant.Sosu15.format(argDouble);
		if (!MessageUtil.checkNumberSize(null, argDouble, from, Double.MAX_VALUE, excludeFrom, excludeTo, false))
		{
			return Collections.singletonList(
					"숫자는 " + Constant.Sosu15.format(from) + " " + (excludeFrom ? "초과여야" : "이상이어야") + " 하는데, " + tabArg + MessageUtil.getFinalConsonant(tabArg,
							MessageUtil.ConsonantType.이가) + " 있습니다");
		}
		if (!MessageUtil.checkNumberSize(null, argDouble, -Double.MAX_VALUE, to, excludeFrom, excludeTo, false))
		{
			return Collections.singletonList(
					"숫자는 " + Constant.Sosu15.format(to) + " " + (excludeTo ? "미만이어야" : "이하여야") + " 하는데, " + tabArg + MessageUtil.getFinalConsonant(tabArg,
							MessageUtil.ConsonantType.이가) + " 있습니다");
		}

		char keyLast = key.charAt(key.length() - 1);
		key = key.substring(0, key.length() - 1) + "=" + Constant.Sosu15.format(argDouble) + keyLast;
		return Collections.singletonList(key);
	}

	private static List<String> tabCompleterEntity(CommandSender sender, String lastArg)
	{
		return tabCompleterEntity(sender, lastArg, false);
	}

	private static List<String> tabCompleterEntity(CommandSender sender, String lastArg, boolean excludeNonPlayerEntity)
	{
		List<String> list = new ArrayList<>(Arrays.asList("@p", "@r", "@a", "@s"));
		if (!sender.hasPermission("minecraft.command.selector"))
		{
			list.clear();
		}
		for (Player online : Bukkit.getServer().getOnlinePlayers())
		{
			list.add(online.getName());
			String displayName = MessageUtil.stripColor(ComponentUtil.serialize(online.displayName()));
			String playerListName = MessageUtil.stripColor(ComponentUtil.serialize(online.playerListName()));
			list.add(displayName);
			list.add(playerListName);
		}
		if (sender instanceof Player player)
		{
			Location location = player.getLocation();
			location.add(location.getDirection().multiply(1d));
			for (Entity entity : player.getWorld().getNearbyEntities(location, 3d, 2d, 3d))
			{
				if (excludeNonPlayerEntity && !(entity instanceof Player))
				{
					continue;
				}
				if ((!entity.equals(player)))
				{
					String type = MessageUtil.stripColor(ComponentUtil.serialize(ComponentUtil.translate(entity.getType().translationKey())));
					String display;
					if (entity instanceof Player p)
					{
						display = p.getName() + "/" + type;
						String playerDisplay = MessageUtil.stripColor(ComponentUtil.serialize(p.displayName()));
						if (!p.getName().equals(playerDisplay))
						{
							display = playerDisplay + "/" + p.getName() + "/" + type;
						}
					}
					else
					{
						display = MessageUtil.stripColor(ComponentUtil.serialize(SenderComponentUtil.senderComponent(entity)));
						if (!type.equals(display))
						{
							display += "/" + type;
						}
					}
					list.add(entity.getUniqueId().toString());
					if (!Method.isUUID(lastArg))
					{
						list.add(Constant.TAB_COMPLETER_QUOTE_ESCAPE + entity.getUniqueId() + "[" + display + "]");
					}
				}
			}
		}
		if (Bukkit.getServer().getPlayerExact(lastArg) != null)
		{
			return Collections.singletonList(lastArg);
		}
		return list;
	}

	public static List<String> tabCompleterEntity(CommandSender sender, String[] args)
	{
		return tabCompleterEntity(sender, args, "[<개체>]");
	}

	public static List<String> tabCompleterEntity(CommandSender sender, String[] args, String key)
	{
		return tabCompleterEntity(sender, args, key, false);
	}

	public static List<String> tabCompleterEntity(CommandSender sender, String[] args, String key, boolean multiple)
	{
		if (Method.equals(args[args.length - 1], "@a", "@e", "@p", "@r", "@s") && !sender.hasPermission("minecraft.command.selector"))
		{
			return Collections.singletonList(ComponentUtil.serialize(ComponentUtil.translate("argument.entity.selector.not_allowed")));
		}
		List<String> list = new ArrayList<>(tabCompleterEntity(sender, args[args.length - 1]));
		if (sender.hasPermission("minecraft.command.selector"))
		{
			list.add("@e");
		}
		List<String> returnList = Method.tabCompleterList(args, list, key, true);
		if ((Method.equals(args[args.length - 1], "@a", "@e", "@p", "@r", "@s") || !list.contains(args[args.length - 1])) && returnList.size() == 1 && key.equals(
				returnList.get(0)))
		{
			try
			{
				List<Entity> entities = Bukkit.selectEntities(sender, args[args.length - 1]);
				if (entities.isEmpty())
				{
					return Collections.singletonList(ComponentUtil.serialize(ComponentUtil.translate("argument.entity.notfound.entity")));
				}
				if (!multiple && entities.size() > 1)
				{
					if (!sender.hasPermission("minecraft.command.selector"))
					{
						return Collections.singletonList(ComponentUtil.serialize(ComponentUtil.translate("argument.entity.selector.not_allowed")));
					}
					return Collections.singletonList(ComponentUtil.serialize(ComponentUtil.translate("argument.entity.toomany")));
				}
			}
			catch (IllegalArgumentException e)
			{
				if (!sender.hasPermission("minecraft.command.selector"))
				{
					return Collections.singletonList(ComponentUtil.serialize(ComponentUtil.translate("argument.entity.selector.not_allowed")));
				}
				return Collections.singletonList(SelectorUtil.getErrorMessage(args[args.length - 1], e));
			}
		}
		return returnList;
	}

	public static List<String> tabCompleterPlayer(CommandSender sender, String[] args)
	{
		return tabCompleterPlayer(sender, args, "[<플레이어>]");
	}

	public static List<String> tabCompleterPlayer(CommandSender sender, String[] args, String key)
	{
		return tabCompleterPlayer(sender, args, key, false);
	}

	public static List<String> tabCompleterPlayer(CommandSender sender, String[] args, String key, boolean multiple)
	{
		if (Method.equals(args[args.length - 1], "@a", "@e", "@p", "@r", "@s") && !sender.hasPermission("minecraft.command.selector"))
		{
			return Collections.singletonList(ComponentUtil.serialize(ComponentUtil.translate("argument.entity.selector.not_allowed")));
		}
		List<String> list = tabCompleterEntity(sender, args[args.length - 1], true);
		List<String> returnList = Method.tabCompleterList(args, list, key, true);
		if ((Method.equals(args[args.length - 1], "@a", "@e", "@p", "@r", "@s") || !list.contains(args[args.length - 1])) && returnList.size() == 1 && key.equals(
				returnList.get(0)))
		{
			try
			{
				List<Entity> entities = Bukkit.selectEntities(sender, args[args.length - 1]);
				if (entities.isEmpty())
				{
					return Collections.singletonList(ComponentUtil.serialize(ComponentUtil.translate("argument.entity.notfound.player")));
				}
				if (!entities.stream().allMatch(Predicates.instanceOf(Player.class)))
				{
					if (!sender.hasPermission("minecraft.command.selector"))
					{
						return Collections.singletonList(ComponentUtil.serialize(ComponentUtil.translate("argument.entity.selector.not_allowed")));
					}
					return Collections.singletonList(ComponentUtil.serialize(ComponentUtil.translate("argument.player.entities")));
				}
				if (!multiple && entities.size() > 1)
				{
					if (!sender.hasPermission("minecraft.command.selector"))
					{
						return Collections.singletonList(ComponentUtil.serialize(ComponentUtil.translate("argument.entity.selector.not_allowed")));
					}
					return Collections.singletonList(ComponentUtil.serialize(ComponentUtil.translate("argument.player.toomany")));
				}
			}
			catch (IllegalArgumentException e)
			{
				if (!sender.hasPermission("minecraft.command.selector"))
				{
					return Collections.singletonList(ComponentUtil.serialize(ComponentUtil.translate("argument.entity.selector.not_allowed")));
				}
				return Collections.singletonList(SelectorUtil.getErrorMessage(args[args.length - 1], e));
			}
		}
		return returnList;
	}

	public static List<String> tabCompleterOfflinePlayer(CommandSender sender, String[] args)
	{
		return tabCompleterOfflinePlayer(sender, args, "[<플레이어>]");
	}

	public static List<String> tabCompleterOfflinePlayer(CommandSender sender, String[] args, String key)
	{
		return tabCompleterOfflinePlayer(sender, args, key, false);
	}

	public static List<String> tabCompleterOfflinePlayer(CommandSender sender, String[] args, String key, boolean multiple)
	{
		if (Method.equals(args[args.length - 1], "@a", "@e", "@p", "@r", "@s") && !sender.hasPermission("minecraft.command.selector"))
		{
			return Collections.singletonList(ComponentUtil.serialize(ComponentUtil.translate("argument.entity.selector.not_allowed")));
		}
		List<String> list = new ArrayList<>(tabCompleterEntity(sender, args[args.length - 1], true));
		for (String nickName : Variable.nickNames)
		{
			if (!Method.isUUID(nickName) || (Bukkit.getOfflinePlayer(UUID.fromString(nickName)).getName() == null))
			{
				list.add(MessageUtil.stripColor(nickName).replace(" ", "+"));
			}
		}
		List<String> returnList = Method.tabCompleterList(args, list, key, true);
		if ((Method.equals(args[args.length - 1], "@a", "@e", "@p", "@r", "@s") || !list.contains(args[args.length - 1])) && returnList.size() == 1 && key.equals(
				returnList.get(0)))
		{
			try
			{
				List<Entity> entities = Bukkit.selectEntities(sender, args[args.length - 1]);
				if (entities.isEmpty())
				{
					return Collections.singletonList(ComponentUtil.serialize(ComponentUtil.translate("argument.entity.notfound.player")));
				}
				if (!entities.stream().allMatch(Predicates.instanceOf(Player.class)))
				{
					if (!sender.hasPermission("minecraft.command.selector"))
					{
						return Collections.singletonList(ComponentUtil.serialize(ComponentUtil.translate("argument.entity.selector.not_allowed")));
					}
					return Collections.singletonList(ComponentUtil.serialize(ComponentUtil.translate("argument.player.entities")));
				}
				if (!multiple && entities.size() > 1)
				{
					if (!sender.hasPermission("minecraft.command.selector"))
					{
						return Collections.singletonList(ComponentUtil.serialize(ComponentUtil.translate("argument.entity.selector.not_allowed")));
					}
					return Collections.singletonList(ComponentUtil.serialize(ComponentUtil.translate("argument.player.toomany")));
				}
			}
			catch (IllegalArgumentException e)
			{
				if (!sender.hasPermission("minecraft.command.selector"))
				{
					return Collections.singletonList(ComponentUtil.serialize(ComponentUtil.translate("argument.entity.selector.not_allowed")));
				}
				return Collections.singletonList(SelectorUtil.getErrorMessage(args[args.length - 1], e));
			}
		}
		return returnList;
	}

	public static List<String> tabCompleterStatistics(String[] args, String type, String key)
	{
		return Method.tabCompleterStatistics(args, type, key, false);
	}

	public static List<String> tabCompleterStatistics(String[] args, String type, String key, boolean ignoreEmpty)
	{
		List<String> list = new ArrayList<>();
		if (type.equals("entity"))
		{
			list.add("entity_killed_by");
			list.add("kill_entity");
		}
		else if (type.equals("material"))
		{
			list.add("break_item");
			list.add("craft_item");
			list.add("drop");
			list.add("mine_block");
			list.add("pickup");
			list.add("use_item");
		}
		else
		{
			list = new ArrayList<>(Method.enumToList(Statistic.values()));
			if (type.equals("general"))
			{
				list.remove("break_item");
				list.remove("craft_item");
				list.remove("drop");
				list.remove("mine_block");
				list.remove("pickup");
				list.remove("use_item");
				list.remove("entity_killed_by");
				list.remove("kill_entity");
			}
		}
		return Method.tabCompleterList(args, list, key, ignoreEmpty);
	}

	public static List<String> tabCompleterBoolean(String[] args, String key)
	{
		return Method.tabCompleterBoolean(args, key, (String) null);
	}

	public static List<String> tabCompleterBoolean(String[] args, String key, String... extraKeys)
	{
		boolean noExtra = extraKeys == null || extraKeys.length == 0;
		String tag = args[args.length - 1];
		if (!Method.startsWith(tag, true, "true", "false") && !noExtra)
		{
			return Collections.singletonList(
					"잘못된 불입니다. 'true' 또는 'false'가 필요하지만 '" + tag + "'" + MessageUtil.getFinalConsonant(tag, MessageUtil.ConsonantType.이가) + " 입력되었습니다");
		}
		if (tag.equals("true"))
		{
			return Collections.singletonList(key);
		}
		return Method.tabCompleterList(args, Method.addAll(Arrays.asList("true", "false", Constant.TAB_COMPLETER_QUOTE_ESCAPE + key), extraKeys), key);
	}

	public static List<String> listWorlds()
	{
		List<String> worlds = new ArrayList<>();
		for (World world : Bukkit.getServer().getWorlds())
		{
			worlds.add(world.getName());
		}
		return worlds;
	}

	public static List<String> addAll(List<String> list, String... extra)
	{
		List<String> returnValue = new ArrayList<>(list);
		if (extra == null)
		{
			return returnValue;
		}
		returnValue.addAll(Arrays.asList(extra));
		return returnValue;
	}

	public static List<String> addAll(Set<String> list, String... extra)
	{
		List<String> returnValue = new ArrayList<>(list);
		if (extra == null)
		{
			return returnValue;
		}
		returnValue.addAll(Arrays.asList(extra));
		return returnValue;
	}

	public static List<String> addAll(Enum<?>[] list, String... extra)
	{
		List<String> returnValue = new ArrayList<>(Method.enumToList(list));
		if (extra == null)
		{
			return returnValue;
		}
		returnValue.addAll(Arrays.asList(extra));
		return returnValue;
	}

	@SuppressWarnings("all")
	public static boolean isTimeUp(ItemStack item, String deadLine)
	{
		int hourDiff = Cucumbery.config.getInt("adjust-time-difference-value");
		if (deadLine.startsWith("~"))
		{
			try
			{
				deadLine = deadLine.substring(1);
				String[] split = deadLine.split(" ");
				int year = 0, month = 0, date = 0, hour = 0, minute = 0, second = 0;
				for (String token : split)
				{
					if (token.endsWith("년"))
					{
						year = Integer.parseInt(token.replace("년", ""));
					}
					if (token.endsWith("개월"))
					{
						month = Integer.parseInt(token.replace("개월", ""));
					}
					if (token.endsWith("일"))
					{
						date = Integer.parseInt(token.replace("일", ""));
					}
					if (token.endsWith("시간"))
					{
						hour = Integer.parseInt(token.replace("시간", ""));
					}
					if (token.endsWith("분"))
					{
						minute = Integer.parseInt(token.replace("분", ""));
					}
					if (token.endsWith("초"))
					{
						second = Integer.parseInt(token.replace("초", ""));
					}
					Calendar endCal = Calendar.getInstance();
					endCal.add(Calendar.YEAR, year);
					endCal.add(Calendar.MONTH, month);
					endCal.add(Calendar.DATE, date);
					endCal.add(Calendar.HOUR_OF_DAY, hour + hourDiff);
					endCal.add(Calendar.MINUTE, minute);
					endCal.add(Calendar.SECOND, second);
					String expireDate = Method.getCurrentTime(endCal, true, false);
					ItemStack clone = item.clone();
					NBTItem nbtItem = new NBTItem(clone);
					NBTCompound itemTag = nbtItem.getCompound(CucumberyTag.KEY_MAIN);
					itemTag.setString(CucumberyTag.EXPIRE_DATE_KEY, expireDate);
					clone = nbtItem.getItem();
					item.setItemMeta(clone.getItemMeta());
					ItemLore.setItemLore(item);
					deadLine = expireDate;
				}
			}
			catch (Exception e)
			{
				return false;
			}
		}
		int[] end = format(deadLine);
		if (end == null)
		{
			return false;
		}
		Calendar curCal = Calendar.getInstance(), endCal = Calendar.getInstance();
		curCal.set(curCal.get(Calendar.YEAR), curCal.get(Calendar.MONTH) + 1, curCal.get(Calendar.DATE), curCal.get(Calendar.HOUR_OF_DAY),
				curCal.get(Calendar.MINUTE), curCal.get(Calendar.SECOND));
		curCal.add(Calendar.HOUR_OF_DAY, hourDiff);
		endCal.set(end[0], end[1], end[2], end[3], end[4], end[5]);
		return curCal.after(endCal) || curCal.equals(endCal);
	}

	private static int[] format(String value)
	{
		String input = value.replace(" ", "").replace("년", ",").replace("월", ",").replace("일", ",").replace("시", ",").replace("분", ",").replace("초", "");
		String[] cutter = input.split(",");
		int[] tokens = new int[6];
		try
		{
			for (int i = 0; i < cutter.length; i++)
			{
				tokens[i] = Integer.parseInt(cutter[i]);
			}
			return tokens;
		}
		catch (Exception e)
		{
			return null;
		}
	}

	public static boolean useItem(Player player, ItemStack item, EquipmentSlot slot, Action action)
	{
		if (!ItemStackUtil.itemExists(item))
		{
			return false;
		}
		boolean success = false;
		ItemMeta itemMeta = item.getItemMeta();
		NBTCompound itemTag = NBTAPI.getMainCompound(item);
		NBTCompound usageTag = NBTAPI.getCompound(itemTag, CucumberyTag.USAGE_KEY);
		NBTCompound usageRightClickTag = NBTAPI.getCompound(usageTag, CucumberyTag.USAGE_COMMANDS_RIGHT_CLICK_KEY);
		NBTCompound cooldownTag = NBTAPI.getCompound(usageRightClickTag, CucumberyTag.COOLDOWN_KEY);
		String permission = NBTAPI.getString(usageRightClickTag, CucumberyTag.PERMISSION_KEY);
		if (permission != null && !player.hasPermission(permission))
		{
			String itemName = item.toString();
			playWarnSound(player);
			MessageUtil.sendWarn(player, "%s을(를) 우클릭 사용할 권한이 없습니다", item);
			return true;
		}
		if (!UserData.EVENT_EXCEPTION_ACCESS.getBoolean(player.getUniqueId()) && cooldownTag != null)
		{
			try
			{
				long cooldownTime = cooldownTag.getLong(CucumberyTag.TIME_KEY);
				String cooldownTagTag = cooldownTag.getString(CucumberyTag.TAG_KEY);
				UUID uuid = player.getUniqueId();
				YamlConfiguration configPlayerCooldown = Variable.cooldownsItemUsage.get(uuid);
				long nextAvailable = configPlayerCooldown == null ? 0 : configPlayerCooldown.getLong(cooldownTagTag);
				long currentTime = System.currentTimeMillis();
				String remainTime = Method.timeFormatMilli(nextAvailable - currentTime);
				if (currentTime < nextAvailable)
				{
					MessageUtil.sendWarn(player, "아직 %s을(를) 우클릭 사용할 수 없습니다 (남은 시간 : %s)", item, Constant.THE_COLOR_HEX + remainTime);
					return true;
				}
				if (configPlayerCooldown == null)
				{
					configPlayerCooldown = new YamlConfiguration();
				}
				configPlayerCooldown.set(cooldownTagTag, currentTime + cooldownTime);
				Variable.cooldownsItemUsage.put(uuid, configPlayerCooldown);
				success = true;
			}
			catch (Exception e)
			{
				Cucumbery.getPlugin().getLogger().warning(e.getMessage());
				MessageUtil.broadcastDebug("오류");
				// DO NOTHING
			}
		}

		// 명령어 실행
		if (usageRightClickTag != null)
		{
			NBTList<String> commandsTag = NBTAPI.getStringList(usageRightClickTag, CucumberyTag.USAGE_COMMANDS_KEY);
			if (commandsTag != null)
			{
				for (String command : commandsTag)
				{
					Method.performCommand(player, command, true, true, null);
					success = true;
				}
			}
			// 팔 휘두름 처리
			if (slot == EquipmentSlot.HAND)
			{
				player.swingMainHand();
			}
			else
			{
				player.swingOffHand();
			}
		}

		// 폭죽 공중에서 발사
		if (item.getType() == Material.FIREWORK_ROCKET && !player.isGliding())
		{
			double radius = 3.5;
			if (player.getGameMode() == GameMode.CREATIVE)
			{
				radius++;
			}
			if (action == Action.RIGHT_CLICK_AIR)
			{
				if (UserData.FIREWORK_LAUNCH_ON_AIR.getBoolean(player.getUniqueId()))
				{
					Firework firework = (Firework) player.getWorld().spawnEntity(player.getEyeLocation().add(player.getEyeLocation().getDirection().multiply(radius)), EntityType.FIREWORK, SpawnReason.CUSTOM);
          firework.setFireworkMeta(((FireworkMeta) itemMeta));
          if (player.isSneaking())
          {
            firework.setShotAtAngle(true);
            firework.setVelocity(player.getLocation().getDirection());
          }
          firework.setShooter(player);
					// 통계 처리
					player.incrementStatistic(Statistic.USE_ITEM, Material.FIREWORK_ROCKET);
					// 팔 휘두름 처리
					if (slot == EquipmentSlot.HAND)
					{
						player.swingMainHand();
					}
					else
					{
						player.swingOffHand();
					}
					if (player.getGameMode() != GameMode.CREATIVE)
					{
						item.setAmount(item.getAmount() - 1);
						if (slot == EquipmentSlot.HAND)
						{
							player.getInventory().setItemInMainHand(item);
						}
						else
						{
							player.getInventory().setItemInOffHand(item);
						}
					}
					return true;
				}
			}
		}

		// 명령 블록 빠른 실행
		if (UserData.USE_QUICK_COMMAND_BLOCK.getBoolean(player.getUniqueId()) && !player.isSneaking() && player.isOp() && (item.getType() == Material.COMMAND_BLOCK
				|| item.getType() == Material.REPEATING_COMMAND_BLOCK || item.getType() == Material.CHAIN_COMMAND_BLOCK))
		{
			String cmd = ((CommandBlock) ((BlockStateMeta) itemMeta).getBlockState()).getCommand();
			if (!cmd.isEmpty())
			{
				Bukkit.getServer().dispatchCommand(player, (cmd.startsWith("/") ? cmd.substring(1) : cmd));
				// 팔 휘두름 처리
				if (slot == EquipmentSlot.HAND)
				{
					player.swingMainHand();
				}
				else
				{
					player.swingOffHand();
				}
				return true;
			}
		}

		// 휴대용 셜커 상자
		if (Constant.SHULKER_BOXES.contains(item.getType()))
		{
			try
			{
				NBTList<String> extraTags = (itemTag != null ? itemTag.getStringList(CucumberyTag.EXTRA_TAGS_KEY) : null);
				if (NBTAPI.arrayContainsValue(extraTags, Constant.ExtraTag.PORTABLE_SHULKER_BOX.toString()))
				{
					BlockStateMeta boxMeta = (BlockStateMeta) item.getItemMeta();
					ShulkerBox shulker = (ShulkerBox) boxMeta.getBlockState();
					ItemStack[] contents = shulker.getInventory().getContents();
					Component display = ItemNameUtil.itemName(item, NamedTextColor.DARK_GRAY);
					Inventory inv = Bukkit.createInventory(null, InventoryType.SHULKER_BOX, ComponentUtil.translate("%s")
							.args(display, Component.text(Constant.ITEM_PORTABLE_SHULKER_BOX_GUI + (slot == EquipmentSlot.HAND ? "MAIN_HAND" : "OFF_HAND"))));
					inv.setContents(contents);
					playSound(player, Sound.BLOCK_SHULKER_BOX_OPEN, SoundCategory.BLOCKS);
					player.openInventory(inv);
					success = true;
					// 팔 휘두름 처리
					if (slot == EquipmentSlot.HAND)
					{
						player.swingMainHand();
					}
					else
					{
						player.swingOffHand();
					}
				}
			}
			catch (Exception ignored)
			{
			}
		}

		// 아이템 소비 및 슬롯 장착
		if (usageRightClickTag != null)
		{
			if (usageRightClickTag.hasTag(CucumberyTag.USAGE_DISPOSABLE_KEY))
			{
				double disposableChance = 100d;
				if (usageTag.hasTag(CucumberyTag.USAGE_DISPOSABLE_KEY))
				{
					disposableChance = usageTag.getDouble(CucumberyTag.USAGE_DISPOSABLE_KEY);
				}
				if (Math.random() * 100d < disposableChance && player.getGameMode() != GameMode.CREATIVE)
				{
					item.setAmount(item.getAmount() - 1);
					if (slot == EquipmentSlot.HAND)
					{
						player.getInventory().setItemInMainHand(item);
					}
					else
					{
						player.getInventory().setItemInOffHand(item);
					}
				}
				success = true;
				// 팔 휘두름 처리
				if (slot == EquipmentSlot.HAND)
				{
					player.swingMainHand();
				}
				else
				{
					player.swingOffHand();
				}
			}
		}

		if (usageTag != null && usageTag.hasTag(CucumberyTag.USAGE_EQUIPMENT_SLOT_KEY))
		{
			String equipmentSlot = usageTag.getString(CucumberyTag.USAGE_EQUIPMENT_SLOT_KEY);
			switch (equipmentSlot)
			{
				case "HELMET" ->
				{
					ItemStack hat = player.getInventory().getHelmet();
					if (ItemStackUtil.itemExists(hat))
					{
						return false;
					}
					player.getInventory().setHelmet(item);
					if (slot == EquipmentSlot.HAND)
					{
						player.getInventory().setItemInMainHand(null);
					}
					else
					{
						player.getInventory().setItemInOffHand(null);
					}
					// 팔 휘두름 처리
					if (slot == EquipmentSlot.HAND)
					{
						player.swingMainHand();
					}
					else
					{
						player.swingOffHand();
					}
					return true;
				}
				case "CHESTPLATE" ->
				{
					ItemStack chestplate = player.getInventory().getChestplate();
					if (ItemStackUtil.itemExists(chestplate))
					{
						return false;
					}
					player.getInventory().setChestplate(item);
					if (slot == EquipmentSlot.HAND)
					{
						player.getInventory().setItemInMainHand(null);
					}
					else
					{
						player.getInventory().setItemInOffHand(null);
					}
					// 팔 휘두름 처리
					if (slot == EquipmentSlot.HAND)
					{
						player.swingMainHand();
					}
					else
					{
						player.swingOffHand();
					}
					return true;
				}
				case "LEGGINGS" ->
				{
					ItemStack leggings = player.getInventory().getLeggings();
					if (ItemStackUtil.itemExists(leggings))
					{
						return false;
					}
					player.getInventory().setLeggings(item);
					if (slot == EquipmentSlot.HAND)
					{
						player.getInventory().setItemInMainHand(null);
					}
					else
					{
						player.getInventory().setItemInOffHand(null);
					}
					// 팔 휘두름 처리
					if (slot == EquipmentSlot.HAND)
					{
						player.swingMainHand();
					}
					else
					{
						player.swingOffHand();
					}
					return true;
				}
				case "BOOTS" ->
				{
					ItemStack boots = player.getInventory().getBoots();
					if (ItemStackUtil.itemExists(boots))
					{
						return false;
					}
					player.getInventory().setBoots(item);
					if (slot == EquipmentSlot.HAND)
					{
						player.getInventory().setItemInMainHand(null);
					}
					else
					{
						player.getInventory().setItemInOffHand(null);
					}
					// 팔 휘두름 처리
					if (slot == EquipmentSlot.HAND)
					{
						player.swingMainHand();
					}
					else
					{
						player.swingOffHand();
					}
					return true;
				}
			}
		}
		return success;
	}

	public static void itemBreakParticle(Player player, ItemStack item)
	{
		player.getLocation().getWorld()
				.spawnParticle(Particle.ITEM_CRACK, player.getEyeLocation().add(player.getLocation().getDirection().multiply(1D)), 20, 0, 0, 0, 0.1, item);
	}

	public static float getPitchFromNote(@NotNull Note note)
	{
		int octave = note.getOctave();
		Tone tone = note.getTone();
		boolean isSharped = note.isSharped();
		switch (octave)
		{
			case 0 ->
			{
				switch (tone)
				{
					case G ->
					{
						return isSharped ? 0.561231F : 0.529732F;
					}
					case A ->
					{
						return isSharped ? 0.629961F : 0.594604F;
					}
					case B ->
					{
						return 0.667420F;
					}
					case C ->
					{
						return isSharped ? 0.749154F : 0.707107F;
					}
					case D ->
					{
						return isSharped ? 0.840896F : 0.793701F;
					}
					case E ->
					{
						return 0.890899F;
					}
					case F ->
					{
						return isSharped ? 0.5F : 0.943874F;
					}
				}
			}
			case 1 ->
			{
				switch (tone)
				{
					case G ->
					{
						return isSharped ? 1.122462F : 1.059463F;
					}
					case A ->
					{
						return isSharped ? 1.259921F : 1.189207F;
					}
					case B ->
					{
						return 1.334840F;
					}
					case C ->
					{
						return isSharped ? 1.498307F : 1.414214F;
					}
					case D ->
					{
						return isSharped ? 1.681793F : 1.587401F;
					}
					case E ->
					{
						return 1.781797F;
					}
					case F ->
					{
						return isSharped ? 1F : 1.887749F;
					}
				}
			}
			case 2 ->
			{
				return 2F;
			}
		}
		return 0F;
	}

	public enum ReinforceSound
	{
		OPERATION,
		SUCCESS,
		FAIL,
		DESTROY
	}

	public enum ReinforceType
	{
		COMMAND,
		SCROLL
	}
}
