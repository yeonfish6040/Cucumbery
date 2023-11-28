package com.jho5245.cucumbery;

import com.comphenix.protocol.ProtocolLib;
import com.jho5245.cucumbery.commands.addon.CommandQuickShopAddon;
import com.jho5245.cucumbery.commands.air.CommandAirPoint;
import com.jho5245.cucumbery.commands.brigadier.*;
import com.jho5245.cucumbery.commands.brigadier.hp.CommandHealthPoint;
import com.jho5245.cucumbery.commands.customrecipe.CommandCustomRecipe;
import com.jho5245.cucumbery.commands.customrecipe.CommandCustomRecipeTabCompleter;
import com.jho5245.cucumbery.commands.debug.*;
import com.jho5245.cucumbery.commands.hp.CommandHealthScale;
import com.jho5245.cucumbery.commands.hp.CommandMaxHealthPoint;
import com.jho5245.cucumbery.commands.itemtag.CommandItemTag;
import com.jho5245.cucumbery.commands.itemtag.CommandItemTagTabCompleter;
import com.jho5245.cucumbery.commands.msg.*;
import com.jho5245.cucumbery.commands.no_groups.*;
import com.jho5245.cucumbery.commands.reinforce.CommandReinforce;
import com.jho5245.cucumbery.commands.sound.CommandPlaySound;
import com.jho5245.cucumbery.commands.sound.CommandSong;
import com.jho5245.cucumbery.commands.teleport.*;
import com.jho5245.cucumbery.custom.customeffect.CustomEffectManager;
import com.jho5245.cucumbery.custom.customeffect.type.CustomEffectType;
import com.jho5245.cucumbery.custom.recipe.RecipeManager;
import com.jho5245.cucumbery.listeners.AsyncTabComplete;
import com.jho5245.cucumbery.listeners.UnknownCommand;
import com.jho5245.cucumbery.listeners.addon.gsit.PreEntitySit;
import com.jho5245.cucumbery.listeners.addon.noteblockapi.SongEnd;
import com.jho5245.cucumbery.listeners.addon.quickshop.*;
import com.jho5245.cucumbery.listeners.addon.ultimatetimber.TreeFell;
import com.jho5245.cucumbery.listeners.addon.worldguard.DisallowedPVP;
import com.jho5245.cucumbery.listeners.block.*;
import com.jho5245.cucumbery.listeners.block.piston.BlockPistonExtend;
import com.jho5245.cucumbery.listeners.block.piston.BlockPistonRetract;
import com.jho5245.cucumbery.listeners.enchantment.EnchantItem;
import com.jho5245.cucumbery.listeners.enchantment.PrepareItemEnchant;
import com.jho5245.cucumbery.listeners.entity.customeffect.*;
import com.jho5245.cucumbery.listeners.entity.damage.EntityDamage;
import com.jho5245.cucumbery.listeners.entity.damage.EntityDamageByBlock;
import com.jho5245.cucumbery.listeners.entity.damage.EntityDamageByEntity;
import com.jho5245.cucumbery.listeners.entity.item.ItemMerge;
import com.jho5245.cucumbery.listeners.entity.item.ItemSpawn;
import com.jho5245.cucumbery.listeners.entity.no_groups.*;
import com.jho5245.cucumbery.listeners.hanging.HangingBreak;
import com.jho5245.cucumbery.listeners.hanging.HangingBreakByEntity;
import com.jho5245.cucumbery.listeners.hanging.HangingPlace;
import com.jho5245.cucumbery.listeners.inventory.*;
import com.jho5245.cucumbery.listeners.player.bucket.PlayerBucketEmpty;
import com.jho5245.cucumbery.listeners.player.bucket.PlayerBucketEntity;
import com.jho5245.cucumbery.listeners.player.bucket.PlayerBucketFill;
import com.jho5245.cucumbery.listeners.player.interact.PlayerInteract;
import com.jho5245.cucumbery.listeners.player.interact.PlayerInteractAtEntity;
import com.jho5245.cucumbery.listeners.player.interact.PlayerInteractEntity;
import com.jho5245.cucumbery.listeners.player.item.*;
import com.jho5245.cucumbery.listeners.player.no_groups.*;
import com.jho5245.cucumbery.listeners.server.ServerCommand;
import com.jho5245.cucumbery.listeners.server.ServerListPing;
import com.jho5245.cucumbery.listeners.vehicle.VehicleDamage;
import com.jho5245.cucumbery.listeners.vehicle.VehicleDestroy;
import com.jho5245.cucumbery.listeners.vehicle.VehicleExit;
import com.jho5245.cucumbery.listeners.world.EntitiesLoad;
import com.jho5245.cucumbery.listeners.world.chunk.PlayerChunkLoad;
import com.jho5245.cucumbery.listeners.world.chunk.PlayerChunkUnload;
import com.jho5245.cucumbery.util.addons.ProtocolLibManager;
import com.jho5245.cucumbery.util.addons.Songs;
import com.jho5245.cucumbery.util.blockplacedata.BlockPlaceDataConfig;
import com.jho5245.cucumbery.util.gui.GUIManager;
import com.jho5245.cucumbery.util.itemlore.ItemLore;
import com.jho5245.cucumbery.util.no_groups.*;
import com.jho5245.cucumbery.util.storage.component.util.ComponentUtil;
import com.jho5245.cucumbery.util.storage.data.Constant;
import com.jho5245.cucumbery.util.storage.data.Prefix;
import com.jho5245.cucumbery.util.storage.data.Variable;
import com.jho5245.cucumbery.util.storage.data.custom_enchant.CustomEnchant;
import com.jho5245.cucumbery.util.storage.no_groups.CustomConfig.UserData;
import com.jho5245.cucumbery.util.storage.no_groups.ItemStackUtil;
import com.jho5245.cucumbery.util.storage.no_groups.RecipeChecker;
import com.jho5245.cucumbery.util.storage.no_groups.SoundPlay;
import com.jho5245.cucumbery.util.storage.no_groups.Updater;
import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.songoda.ultimatetimber.UltimateTimber;
import com.xxmicloxx.NoteBlockAPI.NoteBlockAPI;
import com.xxmicloxx.NoteBlockAPI.songplayer.RadioSongPlayer;
import de.tr7zw.changeme.nbtapi.NBTContainer;
import dev.geco.gsit.GSitMain;
import dev.jorel.commandapi.CommandAPI;
import dev.jorel.commandapi.CommandAPIBukkitConfig;
import dev.jorel.commandapi.Converter;
import io.lumine.mythic.bukkit.BukkitAPIHelper;
import me.clip.placeholderapi.PlaceholderAPIPlugin;
import net.coreprotect.CoreProtect;
import net.milkbowl.vault.chat.Chat;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.AreaEffectCloud;
import org.bukkit.entity.Entity;
import org.bukkit.entity.ItemFrame;
import org.bukkit.entity.Player;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.maxgamer.quickshop.QuickShop;
import org.maxgamer.quickshop.api.QuickShopAPI;
import org.maxgamer.quickshop.api.shop.Shop;

import java.io.File;
import java.util.List;
import java.util.Timer;
import java.util.UUID;

public class Cucumbery extends JavaPlugin
{
	public static final int CONFIG_VERSION = 46, DEATH_MESSAGES_CONFIG_VERSION = 12, LANG_CONFIG_VERSION = 6;

	//  private static final ExecutorService brigadierService = Executors.newFixedThreadPool(1);
	public static YamlConfiguration config;

	/**
	 * Shaded since 2022.07.04 so always true
	 */
	public static boolean using_CommandAPI = true;

	public static boolean using_Vault_Economy;

	public static boolean using_Vault_Chat;

	public static boolean using_NoteBlockAPI;

	public static boolean using_QuickShop;

	public static boolean using_PlaceHolderAPI;

	public static boolean using_mcMMO;

	public static boolean using_MythicMobs;

	public static boolean using_ProtocolLib;

	public static boolean using_WorldEdit;

	public static boolean using_WorldGuard;

	public static boolean using_GSit;

	public static boolean using_UltimateTimber;

	public static boolean using_Residence;

	public static boolean using_CoreProtect;

	/**
	 * MythicMobs API
	 */
	public static BukkitAPIHelper bukkitAPIHelper;

	public static Economy eco;

	public static Chat chat;

	public static WorldEditPlugin worldEditPlugin;

	public static File file;

	public static File dataFolder;

	public static long runTime;

	public static boolean isPaper;

	private boolean isLoaded;

	private static Cucumbery cucumbery;

	public PluginDescriptionFile pluginDescriptionFile;

	private PluginManager pluginManager;

	public static Cucumbery getPlugin()
	{
		return Cucumbery.cucumbery;
	}

	public PluginManager getPluginManager()
	{
		return pluginManager;
	}

	@Override
	public void onLoad()
	{
		if (isLoaded)
		{
			return;
		}
		isLoaded = true;
		try
		{
			CommandAPI.onLoad(new CommandAPIBukkitConfig(this).verboseOutput(false).silentLogs(true).initializeNBTAPI(NBTContainer.class, NBTContainer::new));
		}
		catch (Throwable e)
		{
			Cucumbery.getPlugin().getLogger().warning(e.getMessage());
		}
	}

	@Override
	public void onEnable()
	{
		try
		{
			this.init();
		}
		catch (Exception e)
		{
			MessageUtil.consoleSendMessage("&c플러그인을 활성화하는 도중 오류가 발생했습니다");
			Cucumbery.getPlugin().getLogger().warning(e.getMessage());
		}
		if (Cucumbery.config.getBoolean("console-messages.plugin"))
		{
			MessageUtil.consoleSendMessage(
					"&2[#52ee52;활성화&2] rg255,204;" + pluginDescriptionFile.getName() + "&r version : rg255,204;" + pluginDescriptionFile.getVersion()
							+ "&r 플러그인이 활성화 되었습니다");
		}
		MessageUtil.broadcastDebug("Cucumbery 플러그인 활성화");
		if (config.getBoolean("use-custom-enchant-features") && !CustomEnchant.isEnabled())
		{
			MessageUtil.broadcastDebug("커스텀 인챈트 등록 도중 오류가 발생했습니다. 원활한 처리를 위해 서버를 껐다 키는 것을 권장합니다 (플러그인을 업데이트 했나요?)");
		}
		for (Player online : Bukkit.getOnlinePlayers())
		{
			if (UserData.SHOW_PLUGIN_DEV_DEBUG_MESSAGE.getBoolean(online))
			{
				SoundPlay.playSound(online, Sound.BLOCK_WOODEN_DOOR_OPEN);
			}
		}
	}

	@Override
	public void onDisable()
	{
		try
		{
			this.disableOperation();
		}
		catch (Exception e)
		{
			MessageUtil.consoleSendMessage("&c플러그인을 비활성화하는 도중 오류가 발생했습니다");
			Cucumbery.getPlugin().getLogger().warning(e.getMessage());
		}
		if (Cucumbery.config.getBoolean("console-messages.plugin"))
		{
			MessageUtil.consoleSendMessage(
					"&5[&d비활성화&5] rg255,204;" + pluginDescriptionFile.getName() + "&r version : rg255,204;" + pluginDescriptionFile.getVersion() + "&r 플러그인이 비활성화 되었습니다");
		}
	}

	// 플러그인 활성화 시 초기화 과정
	@SuppressWarnings("deprecation")
	private void init()
	{
		String version = Bukkit.getServer().getVersion().toLowerCase();
		isPaper = version.contains("paper") || version.contains("pufferfish");
		cucumbery = this;
		file = this.getFile();
		dataFolder = this.getDataFolder();
		config = (YamlConfiguration) this.getConfig();
		this.pluginDescriptionFile = this.getDescription();
		this.pluginManager = Bukkit.getServer().getPluginManager();
		try
		{
			this.registerItems();
		}
		catch (Throwable e)
		{
			Cucumbery.getPlugin().getLogger().warning(e.getMessage());
		}
		if (config.getBoolean("console-messages.outdated-config"))
		{
			int currentConfigVersion = config.getInt("config-version");
			if (currentConfigVersion != CONFIG_VERSION)
			{
				MessageUtil.consoleSendMessage(Prefix.INFO_WARN, "%s 파일의 버전이 최신 버전과 일치하지 않습니다! 현재 버전 : %s, 최신 버전 : %s", "rg255,204;config.yml", currentConfigVersion,
						CONFIG_VERSION);
				MessageUtil.consoleSendMessage(Prefix.INFO, "%1$s 파일을 삭제하고 플러그인을 리로드하여 %1$s 파일을 재생성하시거나 플러그인 파일에 있는 %1$s에서 직접 값을 붙여넣어 주세요.", "rg255,204;config.yml");
			}
			int currentDeathMessagesVersion = Variable.deathMessages.getInt("config-version");
			if (currentDeathMessagesVersion != DEATH_MESSAGES_CONFIG_VERSION)
			{
				MessageUtil.consoleSendMessage(Prefix.INFO_WARN, "%s 파일의 버전이 최신 버전과 일치하지 않습니다! 현재 버전 : %s, 최신 버전 : %s", "rg255,204;DeathMessages.yml",
						currentDeathMessagesVersion, DEATH_MESSAGES_CONFIG_VERSION);
				MessageUtil.consoleSendMessage(Prefix.INFO, "%1$s 파일을 삭제하고 플러그인을 리로드하여 %1$s 파일을 재생성하시거나 플러그인 파일에 있는 %1$s에서 직접 값을 붙여넣어 주세요.",
						"rg255,204;DeathMessages.yml");
			}
			int currentLangVersion = Variable.lang.getInt("config-version");
			if (currentLangVersion != LANG_CONFIG_VERSION)
			{
				MessageUtil.consoleSendMessage(Prefix.INFO_WARN, "%s 파일의 버전이 최신 버전과 일치하지 않습니다! 현재 버전 : %s, 최신 버전 : %s", "rg255,204;lang.yml", currentLangVersion,
						LANG_CONFIG_VERSION);
				MessageUtil.consoleSendMessage(Prefix.INFO, "%1$s 파일을 삭제하고 플러그인을 리로드하여 %1$s 파일을 재생성하시거나 플러그인 파일에 있는 %1$s에서 직접 값을 붙여넣어 주세요.", "rg255,204;lang.yml");
			}
		}
		Scheduler.Schedule(this);
		Updater.onEnable();
		Bukkit.getScheduler().scheduleSyncRepeatingTask(cucumbery, new TPSMeter(), 100L, 1L);
		if (using_NoteBlockAPI)
		{
			Songs.onEnable();
		}
		for (Player player : Bukkit.getServer().getOnlinePlayers())
		{
			ItemStackUtil.updateInventory(player);
			if (using_ProtocolLib)
			{
				Bukkit.getScheduler().runTaskLaterAsynchronously(cucumbery, () -> BlockPlaceDataConfig.display(player, player.getLocation()), 0L);
			}
		}
		for (World world : Bukkit.getWorlds())
		{
			for (Entity entity : world.getEntities())
			{
				if (entity.getScoreboardTags().contains("damage_indicator"))
				{
					entity.remove();
					continue;
				}
				if (entity instanceof ItemFrame itemFrame)
				{
					ItemStack itemStack = itemFrame.getItem();
					itemFrame.setItem(ItemLore.setItemLore(itemStack));
				}
			}
		}
	}

	// 플러그인 비활성화 시 처리 과정
	private void disableOperation()
	{
		Initializer.saveUserData();
		BlockPlaceDataConfig.saveAll();
		Initializer.saveItemUsageData();
		Initializer.saveItemStashData();
		CustomEffectManager.saveAll();
		Initializer.loadBrigadierTabListConfig();
		if (Cucumbery.using_NoteBlockAPI)
		{
			if (CommandSong.radioSongPlayer != null)
			{
				CommandSong.radioSongPlayer.setPlaying(false);
				CommandSong.radioSongPlayer.destroy();
			}
			if (!CommandSong.playerRadio.isEmpty())
			{
				for (RadioSongPlayer playerRadio : CommandSong.playerRadio.values())
				{
					playerRadio.setPlaying(false);
					playerRadio.destroy();
				}
			}
			Songs.onDisable();
		}
		for (Player player : Bukkit.getOnlinePlayers())
		{
			CustomEffectManager.addEffect(player, CustomEffectType.INVINCIBLE_PLUGIN_RELOAD);
			player.hideBossBar(Scheduler.serverRadio);
			InventoryView inventoryView = player.getOpenInventory();
			String title = ComponentUtil.serialize(inventoryView.title());
			if (title.contains(Constant.GUI_SUFFIX) || GUIManager.isGUITitle(inventoryView.title()))
			{
				player.closeInventory();
				MessageUtil.sendWarn(player, "플러그인이 비활성화되어 GUI 창이 닫힙니다");
			}
			for (Entity passenger : player.getPassengers())
			{
				while (passenger instanceof AreaEffectCloud && passenger.getScoreboardTags().contains("cucumbery-command-ride"))
				{
					Entity e = passenger;
					passenger = passenger.getPassengers().isEmpty() ? null : passenger.getPassengers().get(0);
					if (e.getVehicle() == null || e.getPassengers().isEmpty())
						e.remove();
				}
			}
		}
		if (using_ProtocolLib)
		{
			BlockPlaceDataConfig.ITEM_DISPLAY_MAP.keySet().forEach(location ->
			{
				String[] split = location.split("\\|");
				try
				{
					Location location1 = new Location(Bukkit.getWorld(split[0]), Integer.parseInt(split[1]), Integer.parseInt(split[2]), Integer.parseInt(split[3]));
					BlockPlaceDataConfig.despawnItemDisplay(location1);
				}
				catch (Exception e)
				{
					System.out.println("왜 : " + location);
					Cucumbery.getPlugin().getLogger().warning(e.getMessage());
				}
			});
			BlockPlaceDataConfig.TIMERS.forEach(Timer::cancel);
		}
		for (UUID uuid : Variable.customEffectBossBarMap.keySet())
		{
			Player player = Bukkit.getPlayer(uuid);
			if (player != null)
			{
				player.hideBossBar(Variable.customEffectBossBarMap.get(uuid));
			}
		}
		for (UUID uuid : Variable.sendBossBarMap.keySet())
		{
			Player player = Bukkit.getPlayer(uuid);
			if (player != null)
			{
				List<BossBarMessage> bossBarMessages = Variable.sendBossBarMap.get(uuid);
				for (BossBarMessage bossBarMessage : bossBarMessages)
				{
					player.hideBossBar(bossBarMessage.getBossBar());
				}
			}
		}
		Updater.onDisable();
		if (config.getBoolean("use-custom-enchant-features"))
		{
			try
			{
				CustomEnchant.onDisable();
			}
			catch (Exception e)
			{
				Cucumbery.getPlugin().getLogger().warning(e.getMessage());
			}
		}
		// 채광 모드 2 블록 복구 모드
		if (Cucumbery.config.getBoolean("custom-mining.restore-mining-mode-2-blocks-on-plugin-disable"))
		{
			for (Location location : Variable.customMiningMode2BlockData.keySet())
			{
				location.getBlock().setBlockData(Variable.customMiningMode2BlockData.get(location), false);
			}
		}
		for (Location location : Variable.customMiningCooldown.keySet())
		{
			location.getBlock().getState().update();
		}
		for (Location location : Variable.fakeBlocks.keySet())
		{
			location.getBlock().getState().update();
		}
		for (UUID uuid : CommandRide.RIDE_AREA_EFFECT_CLOUDS)
		{
			Entity entity = Bukkit.getEntity(uuid);
			if (entity != null)
			{
				if (entity.getPassengers().isEmpty() || entity.getVehicle() == null)
				{
					entity.remove();
				}
			}
		}
		RecipeManager.unload();
		CommandAPI.onDisable();
	}

	private void registerItems()
	{
		this.registerConfig();
		try
		{
			RecipeChecker.setRecipes();
			this.checkUsingAddons();
			this.registerCustomConfig();
		}
		catch (Throwable e)
		{
			Cucumbery.getPlugin().getLogger().warning(e.getMessage());
		}
		if (config.getBoolean("use-custom-enchant-features"))
		{
			try
			{
				CustomEnchant.onEnable();
			}
			catch (Exception ignored)
			{

			}
		}
		if (using_CommandAPI)
		{
			try
			{
				CommandAPI.onEnable();
				new ExtraExecuteArgument().registerArgument();
				new CommandRide().registerCommand("ride2", "cucumbery.command.ride", "cride");
				new CommandSudo2().registerCommand("sudo2", "cucumbery.command.sudo2", "csudo2");
				new CommandGive2().registerCommand("cgive", "cucumbery.command.cgive", "cgive", "give2");
				new CommandVelocity().registerCommand("velocity", "cucumbery.command.velocity2", "velo", "날리기", "cvelo", "cvelocity");
				new CommandHealthPoint().registerCommand("healthpoint", "cucumbery.command.healthpoint", "hp", "chp");
				new CommandKill2().registerCommand("ckill", "cucumbery.command.ckill", "ckill", "kill2");
				new CommandSetItem().registerCommand("setitem", "cucumbery.command.setitem", "csetitem");
				new CommandConsoleSudo2().registerCommand("consolesudo2", "cucumbery.command.consolesudo", "consolesudo2");
				new CommandSendActionbar().registerCommand("sendactionbar", "cucumbery.command.sendactionbar", "csendactionbar");
				new CommandSendTitle().registerCommand("sendtitle", "cucumbery.command.sendtitle", "csendtitle");
				new CommandUpdateItem().registerCommand("updateitem", "cucumbery.command.updateitem", "cupdateitem");
				new CommandEffect2().registerCommand("ceffect", "cucumbery.command.effect", "ceffect", "effect2");
				new CommandDamage().registerCommand("damage2", "cucumbery.command.damage", "cdamage");
				new CommandSummon2().registerCommand("csummon", "cucumbery.command.summon", "csummon", "summon2");
				new CommandSetBlock2().registerCommand("csetblock", "cucumbery.command.setblock", "csetblock", "setblock2");
				new CommandReplaceEntity().registerCommand("replaceentity", "cucumbery.command.replaceentity", "creplaceentity");
				new CommandRepeat2().registerCommand("crepeat", "cucumbery.command.repeat", "repeat2");
				new CommandData2().registerCommand("cdata", "cucumbery.command.data", "data2");
				new CommandTeleport2().registerCommand("teleport2", "cucumbery.command.teleport", "tp2");
				new CommandExplode().registerCommand("explode", "cucumbery.command.explode", "cexplode");
				new CommandVanillaTeleport().registerCommand("teleport", "minecraft.command.teleport", "tp");
				new CommandSellItem().registerCommand("sellitem", "cucumbery.command.sellitem", "csellitem");
				new CommandClear2().registerCommand("clear2", "cucumbery.command.clear2", "cclear");
				new CommandSearchChestItem().registerCommand("search-container-item", "cucumbey.command.search_container_item", "search-container-item");
				new CommandBreak().registerCommand("cbreak", "cucumbery.command.break", "cbreak");
				new CommandFakeBlock().registerCommand("fakeblock", "cucumbery.command.fakeblock", "cfakeblock");
				new CommandTag().registerCommand("tag", "minecraft.command.tag", "tag");
				//        brigadierService.submit(this::registerBrigadierCommands);
			}
			catch (Throwable e)
			{
				Cucumbery.getPlugin().getLogger().warning(e.getMessage());
			}
		}
		try
		{
			this.registerCommands();
		}
		catch (Throwable e)
		{
			Cucumbery.getPlugin().getLogger().warning(e.getMessage());
		}
		try
		{
			this.registerEvents();
		}
		catch (Throwable e)
		{
			Cucumbery.getPlugin().getLogger().warning(e.getMessage());
		}
		try
		{
			RecipeManager.registerRecipe();
		}
		catch (Throwable t)
		{
			Cucumbery.getPlugin().getLogger().warning(t.getMessage());
		}
	}

	private void checkUsingAddons()
	{
		Cucumbery.using_Vault_Economy = Cucumbery.config.getBoolean("use-hook-plugins.Vault-Economy") && Initializer.setupEconomy() && eco != null;
		Cucumbery.using_Vault_Chat = Cucumbery.config.getBoolean("use-hook-plugins.Vault-Chat") && Initializer.setupChat() && chat != null;
		Cucumbery.using_NoteBlockAPI =
				Cucumbery.config.getBoolean("use-hook-plugins.NoteBlockAPI") && this.pluginManager.getPlugin("NoteBlockAPI") instanceof NoteBlockAPI;
		Cucumbery.using_QuickShop = Cucumbery.config.getBoolean("use-hook-plugins.QuickShop") && this.pluginManager.getPlugin("QuickShop") instanceof QuickShop;
		Cucumbery.using_PlaceHolderAPI =
				Cucumbery.config.getBoolean("use-hook-plugins.PlaceHolderAPI") && this.pluginManager.getPlugin("PlaceHolderAPI") instanceof PlaceholderAPIPlugin;
		Cucumbery.using_mcMMO = Cucumbery.config.getBoolean("use-hook-plugins.mcMMO") && this.pluginManager.getPlugin("mcMMO") != null;
		Cucumbery.using_MythicMobs = Cucumbery.config.getBoolean("use-hook-plugins.MythicMobs") && this.pluginManager.getPlugin("MythicMobs") != null;
		Cucumbery.using_ProtocolLib =
				Cucumbery.config.getBoolean("use-hook-plugins.ProtocolLib") && this.pluginManager.getPlugin("ProtocolLib") instanceof ProtocolLib;
		Cucumbery.using_WorldEdit =
				Cucumbery.config.getBoolean("use-hook-plugins.WorldEdit") && this.pluginManager.getPlugin("WorldEdit") instanceof WorldEditPlugin;
		Cucumbery.using_WorldGuard =
				Cucumbery.config.getBoolean("use-hook-plugins.WorldGuard") && this.pluginManager.getPlugin("WorldGuard") instanceof WorldGuardPlugin;
		Cucumbery.using_GSit = Cucumbery.config.getBoolean("use-hook-plugins.GSit") && this.pluginManager.getPlugin("GSit") instanceof GSitMain;
		Cucumbery.using_UltimateTimber =
				Cucumbery.config.getBoolean("use-hook-plugins.UltimateTimber") && this.pluginManager.getPlugin("UltimateTimber") instanceof UltimateTimber;
		Cucumbery.using_Residence = Cucumbery.config.getBoolean("use-hook-plugins.Residence");
		Cucumbery.using_CoreProtect =
				Cucumbery.config.getBoolean("use-hook-plugins.CoreProtect") && this.pluginManager.getPlugin("CoreProtect") instanceof CoreProtect;

		if (using_Residence)
		{
			Plugin plugin = pluginManager.getPlugin("Residence");
			using_Residence = plugin != null && plugin.getClass().getName().equals("com.bekvon.bukkit.residence.Residence");
		}
		if (using_WorldEdit)
		{
			Plugin plugin = this.pluginManager.getPlugin("WorldEdit");
			if (plugin instanceof WorldEditPlugin worldEdit)
			{
				worldEditPlugin = worldEdit;
			}
			else
			{
				using_WorldEdit = false;
			}
		}
		if (Cucumbery.config.getBoolean("console-messages.hook-plugins"))
		{
			if (using_Vault_Economy)
			{
				MessageUtil.consoleSendMessage(Prefix.INFO, "Vault-Economy 플러그인을 연동했습니다");
			}
			if (using_Vault_Chat)
			{
				MessageUtil.consoleSendMessage(Prefix.INFO, "Vault-Chat 플러그인을 연동했습니다");
			}
			if (using_NoteBlockAPI)
			{
				MessageUtil.consoleSendMessage(Prefix.INFO, "NoteBlockAPI 플러그인을 연동했습니다");
			}
			if (using_QuickShop)
			{
				MessageUtil.consoleSendMessage(Prefix.INFO, "QuickShop 플러그인을 연동했습니다");
			}
			if (using_PlaceHolderAPI)
			{
				MessageUtil.consoleSendMessage(Prefix.INFO, "PlaceHolderAPI 플러그인을 연동했습니다");
			}
			if (using_mcMMO)
			{
				MessageUtil.consoleSendMessage(Prefix.INFO, "mcMMO 플러그인을 연동했습니다");
			}
			if (using_MythicMobs)
			{
				MessageUtil.consoleSendMessage(Prefix.INFO, "MythicMobs 플러그인을 연동했습니다");
			}
			if (using_ProtocolLib)
			{
				MessageUtil.consoleSendMessage(Prefix.INFO, "ProtocolLib 플러그인을 연동했습니다");
			}
			if (using_WorldEdit)
			{
				MessageUtil.consoleSendMessage(Prefix.INFO, "WorldEdit 플러그인을 연동했습니다");
			}
			if (using_WorldGuard)
			{
				MessageUtil.consoleSendMessage(Prefix.INFO, "WorldGuard 플러그인을 연동했습니다");
			}
			if (using_GSit)
			{
				MessageUtil.consoleSendMessage(Prefix.INFO, "GSit 플러그인을 연동했습니다");
			}
			if (using_UltimateTimber)
			{
				MessageUtil.consoleSendMessage(Prefix.INFO, "UltimateTimber 플러그인을 연동했습니다");
			}
			if (using_Residence)
			{
				MessageUtil.consoleSendMessage(Prefix.INFO, "Residence 플러그인을 연동했습니다");
			}
			if (using_CoreProtect)
			{
				MessageUtil.consoleSendMessage(Prefix.INFO, "CoreProtect 플러그인을 연동했습니다");
			}
		}
		if (using_QuickShop)
		{
			Bukkit.getServer().getScheduler().runTaskLater(this, () ->
			{
				try
				{
					Plugin plugin = this.pluginManager.getPlugin("QuickShop");
					if (plugin instanceof QuickShopAPI shopAPI)
					{
						List<Shop> shopList = shopAPI.getShopManager().getAllShops();
						Variable.shops.clear();
						Variable.shops.addAll(shopList);
					}
				}
				catch (Exception e)
				{
					Cucumbery.getPlugin().getLogger().warning(e.getMessage());
				}
			}, 100L);
		}
		if (using_MythicMobs)
		{
			bukkitAPIHelper = new BukkitAPIHelper();
		}
		if (using_ProtocolLib)
		{
			try
			{
				ProtocolLibManager.manage();
			}
			catch (Throwable throwable)
			{
				Cucumbery.getPlugin().getLogger().warning(throwable.getMessage());
			}
		}
		if (using_CoreProtect)
		{
			if (CoreProtect.getInstance() == null || !CoreProtect.getInstance().isEnabled() || CoreProtect.getInstance().getAPI().APIVersion() < 9)
			{
				getLogger().warning("CoreProtect가 연동되었으나 망했거나 플러그인이 비활성화되어 있거나 CoreProtect의 API 버전이 낮습니다. 예기치 못한 오류가 발생할 수 있습니다.");
			}
		}
	}

	@SuppressWarnings("unused")
	private void registerBrigadierCommands()
	{
		try
		{
			for (Plugin plugin : pluginManager.getPlugins())
			{
				if (plugin instanceof JavaPlugin javaPlugin)
				{
					try
					{
						Converter.convert(javaPlugin);
					}
					catch (Exception ignored)
					{

					}
				}
			}
		}
		catch (Exception e)
		{
			Cucumbery.getPlugin().getLogger().warning(e.getMessage());
		}
	}

	private void registerCommands()
	{
		Initializer.registerCommand("cucumbery", new CommandCucumbery());
		CommandItemData itemData = new CommandItemData();
		Initializer.registerCommand("itemdata", itemData);
		Initializer.registerCommand("setdata", new CommandSetData());
		CommandSetItemMeta setItemMeta = new CommandSetItemMeta();
		Initializer.registerCommand("setname", setItemMeta);
		Initializer.registerCommand("setname2", setItemMeta);
		Initializer.registerCommand("setlore", setItemMeta);
		Initializer.registerCommand("setlore2", setItemMeta);
		Initializer.registerCommand("addlore", setItemMeta);
		Initializer.registerCommand("deletelore", setItemMeta);
		Initializer.registerCommand("insertlore", setItemMeta);
		Initializer.registerCommand("userdata", new CommandUserData());
		CommandNickName nickName = new CommandNickName();
		Initializer.registerCommand("nickname", nickName);
		Initializer.registerCommand("nicknameothers", nickName);
		Initializer.registerCommand("healthbar", new CommandHealthScale());
		Initializer.registerCommand("maxhealthpoint", new CommandMaxHealthPoint());
		Initializer.registerCommand("whois", new CommandWhoIs());
		Initializer.registerCommand("whatis", new CommandWhatIs());
		Initializer.registerCommand("sudo", new CommandSudo());
		Initializer.registerCommand("handgive", new CommandHandGive());
		Initializer.registerCommand("broadcastitem", new CommandBroadcastItem());
		Initializer.registerCommand("call", new CommandCall());
		Initializer.registerCommand("enderchest", new CommandEnderChest());
		Initializer.registerCommand("workbench", new CommandWorkbench());
		Initializer.registerCommand("trashcan", new CommandTrashCan());
		Initializer.registerCommand("cspectate", new CommandSpectate2());
		Initializer.registerCommand("citemstorage", new CommandItemStorage());
		Initializer.registerCommand("itemflag", new CommandItemFlag());
		CommandHeal healAndFeed = new CommandHeal();
		Initializer.registerCommand("heal", healAndFeed);
		Initializer.registerCommand("feed", healAndFeed);
		Initializer.registerCommand("advancedfeed", healAndFeed);
		Initializer.registerCommand("broadcast", new CommandBroadcast());
		Initializer.registerCommand("sendmessage", new CommandSendMessage());
		Initializer.registerCommand("clearchat", new CommandClearChat());
		Initializer.registerCommand("advancedteleport", new CommandAdvancedTeleport());
		CommandWarp warp = new CommandWarp();
		Initializer.registerCommand("cwarps", warp);
		Initializer.registerCommand("cwarp", warp);
		Initializer.registerCommand("csetwarp", warp);
		Initializer.registerCommand("cdelwarp", warp);
		CommandPlaySound playSound = new CommandPlaySound();
		Initializer.registerCommand("playsoundall", playSound);
		Initializer.registerCommand("playsoundall2", playSound);
		Initializer.registerCommand("cplaysound", playSound);
		Initializer.registerCommand("airpoint", new CommandAirPoint());
		Initializer.registerCommand("reinforce", new CommandReinforce());
		Initializer.registerCommand("menu", new CommandMenu());
		Initializer.registerCommand("forcechat", new CommandForceChat());
		Initializer.registerCommand("setrepaircost", setItemMeta);
		Initializer.registerCommand("sethelditemslot", new CommandSetHeldItemSlot());
		Initializer.registerCommand("swaphelditem", new CommandSwapHeldItem());
		Initializer.registerCommand("itemdata2", itemData);
		Initializer.registerCommand("itemdata3", itemData);
		Initializer.registerCommand("getpositions", new CommandGetPositions());
		Initializer.registerCommand("checkpermission", new CommandCheckPermission());
		Initializer.registerCommand("allplayer", new CommandAllPlayer());
		Initializer.registerCommand("commandpack", new CommandCommandPack());
		Initializer.registerCommand("testcommand", new TestCommand());
		Initializer.registerCommand("swapteleport", new CommandSwapTeleport());
		Initializer.registerCommand("calcdistance", new CommandCalcDistance());
		Initializer.registerCommand("checkamount", new CommandCheckAmount());
		Initializer.registerCommand("updateinventory", new CommandUpdateInventory());
		Initializer.registerCommand("yunnori", new CommandYunnori());
		Initializer.registerCommand("hat", new CommandHat());
		Initializer.registerCommand("updatecommands", new CommandUpdateCommands());
		CommandSong songCommand = new CommandSong();
		Initializer.registerCommand("csong", songCommand);
		Initializer.registerCommand("csong2", songCommand);
		Initializer.registerCommand("citemtag", new CommandItemTag(), new CommandItemTagTabCompleter());
		Initializer.registerCommand("howis", new CommandHowIs());
		Initializer.registerCommand("customrecipe", new CommandCustomRecipe(), new CommandCustomRecipeTabCompleter());
		CommandVirtualChest virtualChest = new CommandVirtualChest();
		Initializer.registerCommand("virtualchest", virtualChest);
		Initializer.registerCommand("virtualchestadd", virtualChest);
		Initializer.registerCommand("virtualchestadmin", virtualChest);
		Initializer.registerCommand("customfix", new CommandCustomFix());
		Initializer.registerCommand("economy", new CommandEconomy());
		Initializer.registerCommand("respawn", new CommandRespawn());
		Initializer.registerCommand("removebedspawnlocation", new CommandRemoveBedSpawnLocation());
		Initializer.registerCommand("checkconfig", new CommandCheckConfig());
		Initializer.registerCommand("cteleport", new CommandTeleport());
		Initializer.registerCommand("editcommandblock", new CommandEditCommandBlock());
		Initializer.registerCommand("editblockdata", new CommandEditBlockData());
		Initializer.registerCommand("consolesudo", new CommandConsoleSudo());
		Initializer.registerCommand("velocity2", new CommandVelocity2());
		Initializer.registerCommand("repeat", new CommandRepeat());
		Initializer.registerCommand("socialmenu", new CommandSocialMenu());
		Initializer.registerCommand("ckill2", new CommandKill3());
		Initializer.registerCommand("viewinventory", new CommandViewInventory());
		Initializer.registerCommand("custommerchant", new CommandCustomMerchant());
		Initializer.registerCommand("customeffect", new CommandCustomEffect());
		Initializer.registerCommand("quickshopaddon", new CommandQuickShopAddon());
		Initializer.registerCommand("modifyexplosive", new CommandModifyExplosive());
		Initializer.registerCommand("sendtoast", new CommandSendToast());
		Initializer.registerCommand("sendbossbar", new CommandSendBossbar());
		Initializer.registerCommand("delay", new CommandDelay());
		Initializer.registerCommand("setnodamageticks", new CommandSetNoDamageTicks());
		Initializer.registerCommand("setaggro", new CommandSetAggro());
		Initializer.registerCommand("stash", new CommandStash());
		Initializer.registerCommand("blockplacedata", new CommandBlockPlaceData());
		Initializer.registerCommand("splash", new CommandSplash());
		Initializer.registerCommand("swingarm", new CommandSwingArm());
		Initializer.registerCommand("shakevillagerhead", new CommandShakeVillagerHead());
		Initializer.registerCommand("setrotation", new CommandSetRotation());
		Initializer.registerCommand("lookat", new CommandLookAt());
		Initializer.registerCommand("cenchant", new CommandEnchant());
	}

	private void registerEvents()
	{
		// listener
		Initializer.registerEvent(new AsyncTabComplete());
		Initializer.registerEvent(new UnknownCommand());

		// listener.block
		Initializer.registerEvent(new BeaconEffect());
		Initializer.registerEvent(new BlockBreak());
		Initializer.registerEvent(new BlockBurn());
		Initializer.registerEvent(new BlockDamage());
		Initializer.registerEvent(new BlockDamageAbort());
		Initializer.registerEvent(new BlockDestroy());
		Initializer.registerEvent(new BlockDispense());
		Initializer.registerEvent(new BlockDispenseArmor());
		Initializer.registerEvent(new BlockExplode());
		Initializer.registerEvent(new BlockFromTo());
		Initializer.registerEvent(new BlockIgnite());
		Initializer.registerEvent(new BlockPhysics());
		Initializer.registerEvent(new BlockPlace());
		Initializer.registerEvent(new BlockPreDispense());
		Initializer.registerEvent(new CustomBlockBreak());
		Initializer.registerEvent(new EntityBlockForm());
		Initializer.registerEvent(new NotePlay());
		Initializer.registerEvent(new PreCustomBlockBreak());
		Initializer.registerEvent(new TNTPrime());

		// listener.block.piston
		Initializer.registerEvent(new BlockPistonExtend());
		Initializer.registerEvent(new BlockPistonRetract());

		// listener.enchantment
		Initializer.registerEvent(new EnchantItem());
		Initializer.registerEvent(new PrepareItemEnchant());

		// listener.entity
		Initializer.registerEvent(new AreaEffectCloudApply());
		Initializer.registerEvent(new EntityAddToWorld());
		Initializer.registerEvent(new EntityBreed());
		Initializer.registerEvent(new EntityChangeBlock());
		Initializer.registerEvent(new EntityDeath());
		Initializer.registerEvent(new EntityDismount());
		Initializer.registerEvent(new EntityExplode());
		Initializer.registerEvent(new EntityJump());
		Initializer.registerEvent(new EntityLandOnGround());
		Initializer.registerEvent(new EntityLoadCrossbow());
		Initializer.registerEvent(new EntityMount());
		Initializer.registerEvent(new EntityMove());
		Initializer.registerEvent(new EntityPickupItem());
		Initializer.registerEvent(new EntityPotionEffect());
		Initializer.registerEvent(new EntityRegainHealth());
		Initializer.registerEvent(new EntityRemoveFromWorld());
		Initializer.registerEvent(new EntityResurrect());
		Initializer.registerEvent(new EntityShootBow());
		Initializer.registerEvent(new EntitySpawn());
		Initializer.registerEvent(new EntityTarget());
		Initializer.registerEvent(new ExpBottle());
		Initializer.registerEvent(new ExperienceOrbMerge());
		Initializer.registerEvent(new ExplosionPrime());
		Initializer.registerEvent(new FireworkExplode());
		Initializer.registerEvent(new LingeringPotionSplash());
		Initializer.registerEvent(new PotionSplash());
		Initializer.registerEvent(new ProjectileHit());
		Initializer.registerEvent(new ProjectileLaunch());
		Initializer.registerEvent(new TameableDeathMessage());
		Initializer.registerEvent(new VillagerAcquireTrade());
		Initializer.registerEvent(new WitchThrowPotion());
		// listener.entity.customeffect
		Initializer.registerEvent(new EntityCustomEffectApply());
		Initializer.registerEvent(new EntityCustomEffectPostApply());
		Initializer.registerEvent(new EntityCustomEffectPreApply());
		Initializer.registerEvent(new EntityCustomEffectPreRemove());
		Initializer.registerEvent(new EntityCustomEffectRemove());
		// listener.entity.damage
		Initializer.registerEvent(new EntityDamage());
		Initializer.registerEvent(new EntityDamageByBlock());
		Initializer.registerEvent(new EntityDamageByEntity());

		// listener.item
		Initializer.registerEvent(new ItemMerge());
		Initializer.registerEvent(new ItemSpawn());

		// listener.hanging
		Initializer.registerEvent(new HangingBreak());
		Initializer.registerEvent(new HangingBreakByEntity());
		Initializer.registerEvent(new HangingPlace());

		// listener.inventory
		Initializer.registerEvent(new Brew());
		Initializer.registerEvent(new CraftItem());
		Initializer.registerEvent(new FurnaceBurn());
		Initializer.registerEvent(new FurnaceSmelt());
		Initializer.registerEvent(new InventoryClick());
		Initializer.registerEvent(new InventoryClose());
		Initializer.registerEvent(new InventoryMoveItem());
		Initializer.registerEvent(new InventoryOpen());
		Initializer.registerEvent(new InventoryPickupItem());
		Initializer.registerEvent(new PrepareAnvil());
		Initializer.registerEvent(new PrepareItemCraft());
		Initializer.registerEvent(new PrepareResult());
		Initializer.registerEvent(new PrepareSmithing());

		// listener.player
		Initializer.registerEvent(new PlayerAdvancementDone());
		Initializer.registerEvent(new PlayerArmorChange());
		Initializer.registerEvent(new PlayerChangeBeaconEffect());
		Initializer.registerEvent(new PlayerChangedWorld());
		Initializer.registerEvent(new PlayerChat());
		Initializer.registerEvent(new PlayerChatPreview());
		Initializer.registerEvent(new PlayerCommandPreprocess());
		Initializer.registerEvent(new PlayerCommandSend());
		Initializer.registerEvent(new PlayerDeath());
		Initializer.registerEvent(new PlayerEditBook());
		Initializer.registerEvent(new PlayerElytraBoost());
		Initializer.registerEvent(new PlayerExpChange());
		Initializer.registerEvent(new PlayerFish());
		Initializer.registerEvent(new PlayerFlowerPotManipulate());
		Initializer.registerEvent(new PlayerGameModeChange());
		Initializer.registerEvent(new PlayerJoin());
		Initializer.registerEvent(new PlayerJump());
		Initializer.registerEvent(new PlayerLaunchProjectile());
		Initializer.registerEvent(new PlayerLecternPageChange());
		Initializer.registerEvent(new PlayerLoomPatternSelect());
		Initializer.registerEvent(new PlayerMove());
		Initializer.registerEvent(new PlayerPickupArrow());
		Initializer.registerEvent(new PlayerPickupExperience());
		Initializer.registerEvent(new PlayerPostRespawn());
		Initializer.registerEvent(new PlayerPreLogin());
		Initializer.registerEvent(new PlayerQuit());
		Initializer.registerEvent(new PlayerRecipeBookClick());
		Initializer.registerEvent(new PlayerRespawn());
		Initializer.registerEvent(new PlayerStoneCutterRecipeSelect());
		Initializer.registerEvent(new PlayerStopSpectatingEntity());
		Initializer.registerEvent(new PlayerTakeLecternBook());
		Initializer.registerEvent(new PlayerTeleport());
		Initializer.registerEvent(new PlayerToggleSneak());
		Initializer.registerEvent(new PlayerToggleSprint());
		// listener.player.bucket
		Initializer.registerEvent(new PlayerBucketEmpty());
		Initializer.registerEvent(new PlayerBucketEntity());
		Initializer.registerEvent(new PlayerBucketFill());
		// listener.player.interact
		Initializer.registerEvent(new PlayerInteract());
		Initializer.registerEvent(new PlayerInteractAtEntity());
		Initializer.registerEvent(new PlayerInteractEntity());
		// listener.player.item
		Initializer.registerEvent(new PlayerAttemptPickupItem());
		Initializer.registerEvent(new PlayerDropItem());
		Initializer.registerEvent(new PlayerItemBreak());
		Initializer.registerEvent(new PlayerItemConsume());
		Initializer.registerEvent(new PlayerItemDamage());
		Initializer.registerEvent(new PlayerItemHeld());
		Initializer.registerEvent(new PlayerItemMend());
		Initializer.registerEvent(new PlayerPickItem());
		Initializer.registerEvent(new PlayerStopUsingItem());
		Initializer.registerEvent(new PlayerSwapHandItems());
		// listener.server
		Initializer.registerEvent(new ServerCommand());
		Initializer.registerEvent(new ServerListPing());
		// listener.vehicle
		Initializer.registerEvent(new VehicleDamage());
		Initializer.registerEvent(new VehicleDestroy());
		Initializer.registerEvent(new VehicleExit());
		// listener.world
		Initializer.registerEvent(new EntitiesLoad());
		// listener.world.chunk
		Initializer.registerEvent(new PlayerChunkLoad());
		Initializer.registerEvent(new PlayerChunkUnload());
		// listener.addon.quickshop
		if (using_QuickShop)
		{
			Initializer.registerEvent(new PlayerShopClick());
			Initializer.registerEvent(new ShopDelete());
			Initializer.registerEvent(new ShopItemChange());
			Initializer.registerEvent(new ShopPreCreate());
			Initializer.registerEvent(new ShopPriceChange());
			Initializer.registerEvent(new ShopSuccessPurchase());
		}
		// listener.addon.noteblockapi
		if (using_NoteBlockAPI)
		{
			Initializer.registerEvent(new SongEnd());
		}
		// listener.addon.worldguard
		if (using_WorldGuard)
		{
			Initializer.registerEvent(new DisallowedPVP());
		}
		// listener.addon.gsit
		if (using_GSit)
		{
			Initializer.registerEvent(new PreEntitySit());
		}
		// listener.addon.ultimatetimber
		if (using_UltimateTimber)
		{
			Initializer.registerEvent(new TreeFell());
		}
	}

	public void registerConfig()
	{
		File config = new File(this.getDataFolder(), "config.yml");
		if (!config.exists())
		{
			this.getConfig().options().copyDefaults(true);
			this.saveDefaultConfig();
		}
	}

	private void registerCustomConfig()
	{
		Initializer.loadCustomConfigs();
		Initializer.loadPlayersConfig();
	}
}
