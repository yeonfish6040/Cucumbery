package com.jho5245.cucumbery;

import com.jho5245.cucumbery.commands.*;
import com.jho5245.cucumbery.commands.addon.QuickShopAddon;
import com.jho5245.cucumbery.commands.air.AirPoint;
import com.jho5245.cucumbery.commands.brigadier.*;
import com.jho5245.cucumbery.commands.debug.*;
import com.jho5245.cucumbery.commands.hp.HealthBar;
import com.jho5245.cucumbery.commands.hp.MaxHealthPoint;
import com.jho5245.cucumbery.commands.msg.Broadcast;
import com.jho5245.cucumbery.commands.msg.ClearChat;
import com.jho5245.cucumbery.commands.msg.SendMessage;
import com.jho5245.cucumbery.commands.sound.NoteBlockAPISong;
import com.jho5245.cucumbery.commands.sound.PlaySound;
import com.jho5245.cucumbery.commands.teleport.AdvancedTeleport;
import com.jho5245.cucumbery.commands.teleport.SwapTeleport;
import com.jho5245.cucumbery.commands.teleport.Teleport;
import com.jho5245.cucumbery.commands.teleport.Warp;
import com.jho5245.cucumbery.listeners.UnknownCommand;
import com.jho5245.cucumbery.listeners.addon.quickshop.ShopDelete;
import com.jho5245.cucumbery.listeners.addon.quickshop.ShopItemChange;
import com.jho5245.cucumbery.listeners.addon.quickshop.ShopPreCreate;
import com.jho5245.cucumbery.listeners.addon.quickshop.ShopPriceChange;
import com.jho5245.cucumbery.listeners.block.*;
import com.jho5245.cucumbery.listeners.block.piston.BlockPistonExtend;
import com.jho5245.cucumbery.listeners.block.piston.BlockPistonRetract;
import com.jho5245.cucumbery.listeners.enchantment.EnchantItem;
import com.jho5245.cucumbery.listeners.enchantment.PrepareItemEnchant;
import com.jho5245.cucumbery.listeners.entity.*;
import com.jho5245.cucumbery.listeners.entity.item.ItemMerge;
import com.jho5245.cucumbery.listeners.entity.item.ItemSpawn;
import com.jho5245.cucumbery.listeners.hanging.HangingBreak;
import com.jho5245.cucumbery.listeners.hanging.HangingPlace;
import com.jho5245.cucumbery.listeners.inventory.*;
import com.jho5245.cucumbery.listeners.player.*;
import com.jho5245.cucumbery.listeners.player.bucket.PlayerBucketEmpty;
import com.jho5245.cucumbery.listeners.player.bucket.PlayerBucketEntity;
import com.jho5245.cucumbery.listeners.player.bucket.PlayerBucketFill;
import com.jho5245.cucumbery.listeners.player.interact.PlayerInteract;
import com.jho5245.cucumbery.listeners.player.interact.PlayerInteractAtEntity;
import com.jho5245.cucumbery.listeners.player.interact.PlayerInteractEntity;
import com.jho5245.cucumbery.listeners.player.item.*;
import com.jho5245.cucumbery.listeners.server.ServerCommand;
import com.jho5245.cucumbery.listeners.server.ServerListPing;
import com.jho5245.cucumbery.rpg.DamageDebugCommand;
import com.jho5245.cucumbery.util.*;
import com.jho5245.cucumbery.util.addons.Songs;
import com.jho5245.cucumbery.util.plugin_support.QuickShopTabCompleter;
import com.jho5245.cucumbery.util.storage.ComponentUtil;
import com.jho5245.cucumbery.util.storage.CustomConfig;
import com.jho5245.cucumbery.util.storage.SoundPlay;
import com.jho5245.cucumbery.util.storage.Updater;
import com.jho5245.cucumbery.util.storage.data.Constant;
import com.jho5245.cucumbery.util.storage.data.Variable;
import com.xxmicloxx.NoteBlockAPI.songplayer.RadioSongPlayer;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.InventoryView;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.maxgamer.quickshop.QuickShop;

import java.io.File;

public class Cucumbery extends JavaPlugin
{
  private static Cucumbery cucumbery;

  public static YamlConfiguration config;

  public PluginDescriptionFile pluginDescriptionFile;

  private PluginManager pluginManager;

  public int currentConfigVersion;

  public static int CONFIG_VERSION = 1;

  public static boolean using_CommandAPI;

  public static boolean using_Vault;

  public static boolean using_NoteBlockAPI;

  public static boolean using_QuickShop;

  public static boolean using_PlaceHolderAPI;

  public static boolean using_ItemEdit;

  public static boolean using_mcMMO;

  public static Economy eco;

  public static File file;

  public static File dataFolder;

  public static long runTime;

  private static TabCompleter tabCompleter;

  public static Cucumbery getPlugin()
  {
    return Cucumbery.cucumbery;
  }

  public PluginManager getPluginManager()
  {
    return pluginManager;
  }

  public TabCompleter getTabCompleter()
  {
    return tabCompleter;
  }

  public void onEnable()
  {
    try
    {
      this.init();
    }
    catch (Exception e)
    {
      MessageUtil.consoleSendMessage("&c플러그인을 활성화하는 도중 오류가 발생하였습니다.");
      e.printStackTrace();
    }
    if (Cucumbery.config.getBoolean("console-messages.plugin"))
    {
      MessageUtil.consoleSendMessage("&2[#52ee52;활성화&2] &e" + pluginDescriptionFile.getName() + "&r version : &e" + pluginDescriptionFile.getVersion() + "&r 플러그인이 활성화 되었습니다.");
    }
    MessageUtil.broadcastDebug("Cucumbery 플러그인 활성화");
    for (Player online : Bukkit.getOnlinePlayers())
    {
      if (CustomConfig.UserData.SHOW_PLUGIN_DEV_DEBUG_MESSAGE.getBoolean(online))
      {
        SoundPlay.playSound(online, Sound.BLOCK_WOODEN_DOOR_OPEN);
      }
    }
  }

  public void onDisable()
  {
    try
    {
      this.disableOperation();
    }
    catch (Exception e)
    {
      MessageUtil.consoleSendMessage("&c플러그인을 비활성화하는 도중 오류가 발생하였습니다.");
      e.printStackTrace();
    }
    if (Cucumbery.config.getBoolean("console-messages.plugin"))
    {
      MessageUtil.consoleSendMessage("&5[&d비활성화&5] &e" + pluginDescriptionFile.getName() + "&r version : &e" + pluginDescriptionFile.getVersion() + "&r 플러그인이 비활성화 되었습니다.");
    }
  }

  // 플러그인 활성화 시 초기화 과정
  private void init()
  {
    Cucumbery.cucumbery = this;
    dataFolder = this.getDataFolder();
    Cucumbery.config = (YamlConfiguration) this.getConfig();
    this.pluginDescriptionFile = this.getDescription();
    this.pluginManager = Bukkit.getServer().getPluginManager();
    this.currentConfigVersion = Cucumbery.config.getInt("config-version");
    this.registerItems();
    Scheduler.Schedule(this);
    dataFolder = new File(new File(Cucumbery.getPlugin().getDataFolder().getAbsoluteFile().getParent()).getParent());
    file = this.getFile();
    Updater.onEnable();
    if (using_NoteBlockAPI)
    {
      Songs.onEnable();
    }
    for (Player player : Bukkit.getServer().getOnlinePlayers())
    {
      Method.updateInventory(player);
    }
  }

  // 플러그인 비활성화 시 처리 과정
  private void disableOperation()
  {
    Initializer.saveUserData();
    Initializer.saveBlockPlaceData();
    Initializer.saveItemUsageData();
    Initializer.loadBrigadierTabListConfig();
    for (Player player : Bukkit.getOnlinePlayers())
    {
      InventoryView inventoryView = player.getOpenInventory();
      String title = ComponentUtil.serialize(inventoryView.title());
      if (title.contains(Constant.GUI_SUFFIX))
      {
        player.closeInventory();
        MessageUtil.sendWarn(player, "플러그인이 비활성화되어 GUI 창이 닫힙니다.");
      }
    }
    Updater.onDisable();
    if (Cucumbery.using_NoteBlockAPI)
    {
      if (NoteBlockAPISong.radioSongPlayer != null)
      {
        NoteBlockAPISong.radioSongPlayer.setPlaying(false);
        NoteBlockAPISong.radioSongPlayer.destroy();
      }
      if (NoteBlockAPISong.playerRadio.size() > 0)
      {
        for (RadioSongPlayer playerRadio : NoteBlockAPISong.playerRadio.values())
        {
          playerRadio.setPlaying(false);
          playerRadio.destroy();
        }
      }
    }
    Songs.onDisable();
  }

  private void registerItems()
  {
    this.registerConfig();
    this.checkUsingAddons();
    this.registerCustomConfig();
    if (using_CommandAPI)
    {
      this.registerBrigadierCommands();
    }
    this.registerCommands();
    this.registerEvents();
  }

  private void checkUsingAddons()
  {
    Cucumbery.using_CommandAPI = Cucumbery.config.getBoolean("use-hook-plugins.CommandAPI") && this.pluginManager.getPlugin("CommandAPI") != null;
    Cucumbery.using_Vault = Cucumbery.config.getBoolean("use-hook-plugins.Vault") && Initializer.setupEconomy();
    Cucumbery.using_NoteBlockAPI = Cucumbery.config.getBoolean("use-hook-plugins.NoteBlockAPI") && this.pluginManager.getPlugin("NoteBlockAPI") != null;
    Cucumbery.using_QuickShop = Cucumbery.config.getBoolean("use-hook-plugins.QuickShop") && this.pluginManager.getPlugin("QuickShop") != null;
    Cucumbery.using_PlaceHolderAPI = Cucumbery.config.getBoolean("use-hook-plugins.PlaceHolderAPI") && this.pluginManager.getPlugin("PlaceHolderAPI") != null;
    Cucumbery.using_ItemEdit = Cucumbery.config.getBoolean("use-hook-plugins.ItemEdit") && this.pluginManager.getPlugin("ItemEdit") != null;
    Cucumbery.using_mcMMO = Cucumbery.config.getBoolean("use-hook-plugins.mcMMO") && this.pluginManager.getPlugin("mcMMO") != null;
    if (using_QuickShop)
    {
      Variable.shops.clear();
      Variable.shops = QuickShop.getInstance().getShopManager().getAllShops();
    }
  }

  private void registerBrigadierCommands()
  {
    new ExtraExecuteArgument().registerArgument();
    new CucumberyMainCommand().registerCommand("cucumbery", "cucumbery.command.cucumbery", "ccucumbery");
    new Ride().registerCommand("ride", "cucumbery.command.ride", "cride");
    new Sudo2().registerCommand("sudo2", "cucumbery.command.sudo2", "csudo2");
    new Give().registerCommand("cgive", "cucumbery.command.cgive", "cgive", "give2");
    new Velocity().registerCommand("velocity", "cucumbery.command.velocity2", "velo", "날리기", "cvelo", "cvelocity");
    new com.jho5245.cucumbery.commands.brigadier.hp.HealthPoint().registerCommand("healthpoint", "cucumbery.command.healthpoint", "hp", "chp");
    new Kill().registerCommand("ckill", "cucumbery.command.ckill", "ckill", "kill2");
    new SetItem().registerCommand("setitem", "cucumbery.command.setitem", "csetitem");
    new ConsoleSudo2().registerCommand("consolesudo2", "cucumbery.command.consolesudo", "consolesudo2");
    new SendActionbar().registerCommand("sendactionbar", "cucumbery.command.sendactionbar", "csendactionbar");
    new SendTitle().registerCommand("sendtitle", "cucumbery.command.sendtitle", "csendtitle");
    new UpdateItem().registerCommand("updateitem", "cucumbery.command.updateitem", "cupdateitem");
    new Effect().registerCommand("ceffect", "cucumbery.command.effect", "ceffect", "effect2");
    new Damage().registerCommand("damage", "cucumbery.command.damage", "cdamage");
    new Summon().registerCommand("csummon", "cucumbery.command.summon", "csummon", "summon2");
    new Setblock().registerCommand("csetblock", "cucumbery.command.setblock", "csetblock", "setblock2");
    new ReplaceEntity().registerCommand("replaceentity", "cucumbery.command.replaceentity", "creplaceentity");
    new Repeat2().registerCommand("crepeat", "cucumbery.command.repeat", "repeat2");
  }

  private void registerCommands()
  {
    tabCompleter = new TabCompleter();
    ItemData itemData = new ItemData();
    Initializer.registerCommand("itemdata", itemData);
    Initializer.registerCommand("setdata", new SetData());
    SetItemMeta setItemMeta = new SetItemMeta();
    Initializer.registerCommand("setname", setItemMeta);
    Initializer.registerCommand("setname2", setItemMeta);
    Initializer.registerCommand("setlore", setItemMeta);
    Initializer.registerCommand("setlore2", setItemMeta);
    Initializer.registerCommand("addlore", setItemMeta);
    Initializer.registerCommand("deletelore", setItemMeta);
    Initializer.registerCommand("insertlore", setItemMeta);
    Initializer.registerCommand("setuserdata", new SetUserData());
    NickName nickName = new NickName();
    Initializer.registerCommand("nickname", nickName);
    Initializer.registerCommand("nicknameothers", nickName);
    Initializer.registerCommand("healthbar", new HealthBar());
    Initializer.registerCommand("maxhealthpoint", new MaxHealthPoint());
    Initializer.registerCommand("whois", new WHOIS());
    Initializer.registerCommand("whatis", new WHATIS());
    Initializer.registerCommand("sudo", new Sudo());
    HandGive handGive = new HandGive();
    Initializer.registerCommand("handgive", handGive);
    Initializer.registerCommand("handgiveall", handGive);
    BroadcastAndMessageItem broadcastAndMessageItem = new BroadcastAndMessageItem();
    Initializer.registerCommand("broadcastitem", broadcastAndMessageItem);
    Initializer.registerCommand("messageitem", broadcastAndMessageItem);
    Initializer.registerCommand("call", new Call());
    Initializer.registerCommand("enderchest", new EnderChest());
    Initializer.registerCommand("workbench", new Workbench());
    Initializer.registerCommand("trashcan", new TrashCan());
    Initializer.registerCommand("cspectate", new Spectate());
    Initializer.registerCommand("citemstorage", new ItemStorage());
    Initializer.registerCommand("itemflag", new ItemFlags());
    HealAndFeed healAndFeed = new HealAndFeed();
    Initializer.registerCommand("heal", healAndFeed);
    Initializer.registerCommand("feed", healAndFeed);
    Initializer.registerCommand("advancedfeed", healAndFeed);
    Initializer.registerCommand("copystring", new CopyString());
    Initializer.registerCommand("viewdamage", new ViewDamage());
    Initializer.registerCommand("uuid", new com.jho5245.cucumbery.commands.debug.UUID());
    Initializer.registerCommand("broadcast", new Broadcast());
    Initializer.registerCommand("sendmessage", new SendMessage());
    Initializer.registerCommand("clearchat", new ClearChat());
    Initializer.registerCommand("advancedteleport", new AdvancedTeleport());
    Warp warp = new Warp();
    Initializer.registerCommand("cwarps", warp);
    Initializer.registerCommand("cwarp", warp);
    Initializer.registerCommand("csetwarp", warp);
    Initializer.registerCommand("cdelwarp", warp);
    PlaySound playSound = new PlaySound();
    Initializer.registerCommand("playsoundall", playSound);
    Initializer.registerCommand("playsoundall2", playSound);
    Initializer.registerCommand("cplaysound", playSound);
    Initializer.registerCommand("airpoint", new AirPoint());
    Initializer.registerCommand("getuserdata", new GetUserData());
    Initializer.registerCommand("reinforce", new Reinforce());
    Initializer.registerCommand("debugdamage", new DamageDebugCommand());
    Initializer.registerCommand("menu", new Menu());
    Initializer.registerCommand("forcechat", new ForceChat());
    Initializer.registerCommand("setrepaircost", setItemMeta);
    Initializer.registerCommand("sethelditemslot", new SetHeldItemSlot());
    Initializer.registerCommand("swaphelditem", new SwapHeldItem());
    Initializer.registerCommand("itemdata2", itemData);
    Initializer.registerCommand("itemdata3", itemData);
    Initializer.registerCommand("getpositions", new GetPositions());
    Initializer.registerCommand("checkpermission", new CheckPermission());
    Initializer.registerCommand("allplayer", new AllPlayer());
    Initializer.registerCommand("commandpack", new CommandPack());
    Initializer.registerCommand("testcommand", new TestCommand());
    Initializer.registerCommand("swapteleport", new SwapTeleport());
    Initializer.registerCommand("calcdistance", new CalcDistance());
    Initializer.registerCommand("checkamount", new CheckAmount());
    Initializer.registerCommand("setattribute", new SetAttribute());
    Initializer.registerCommand("updateinventory", new UpdateInventory());
    Initializer.registerCommand("yunnori", new Yunnori());
    Initializer.registerCommand("hat", new Hat());
    Initializer.registerCommand("updatecommands", new UpdateCommands());
    NoteBlockAPISong songCommand = new NoteBlockAPISong();
    Initializer.registerCommand("csong", songCommand);
    Initializer.registerCommand("csong2", songCommand);
    Initializer.registerCommand("citemtag", new CucumberyItemTag());
    Initializer.registerCommand("howis", new HOWIS());
    Initializer.registerCommand("customrecipe", new CustomRecipe());
    VirtualChest virtualChest = new VirtualChest();
    Initializer.registerCommand("virtualchest", virtualChest);
    Initializer.registerCommand("virtualchestadd", virtualChest);
    Initializer.registerCommand("virtualchestadmin", virtualChest);
    Initializer.registerCommand("customfix", new CustomFix());
    Initializer.registerCommand("economy", new CucumberyEconomyCommand());
    Initializer.registerCommand("respawn", new Respawn());
    Initializer.registerCommand("removebedspawnlocation", new RemoveBedSpawnLocation());
    Initializer.registerCommand("checkconfig", new CheckConfig());
    Initializer.registerCommand("cteleport", new Teleport());
    Initializer.registerCommand("editcommandblock", new EditCommandBlock());
    Initializer.registerCommand("editblockdata", new EditBlockData());
    Initializer.registerCommand("consolesudo", new ConsoleSudo());
    Initializer.registerCommand("velocity2", new Velocity2());
    Initializer.registerCommand("repeat", new Repeat());
    Initializer.registerCommand("socialmenu", new SocialMenu());
    Initializer.registerCommand("ckill2", new CommandCKill2());

    if (Cucumbery.using_QuickShop)
    {
      Initializer.registerCommand("quickshopaddon", new QuickShopAddon(), new QuickShopTabCompleter());
    }
    else
    {
      Initializer.registerCommand("quickshopaddon", new QuickShopAddon());
    }
  }

  private void registerEvents()
  {
    // event
    Initializer.registerEvent(new UnknownCommand());
    // event.block
    Initializer.registerEvent(new BlockBreak());
    Initializer.registerEvent(new BlockBurn());
    Initializer.registerEvent(new BlockDestroy());
    Initializer.registerEvent(new BlockDispense());
    Initializer.registerEvent(new BlockDispenseArmor());
    Initializer.registerEvent(new BlockExplode());
    Initializer.registerEvent(new BlockFromTo());
    Initializer.registerEvent(new BlockIgnite());
    Initializer.registerEvent(new BlockPhysics());
    Initializer.registerEvent(new BlockPlace());
    Initializer.registerEvent(new BlockPreDispense());
    Initializer.registerEvent(new EntityBlockForm());
    Initializer.registerEvent(new TNTPrime());
    // event.block.piston
    Initializer.registerEvent(new BlockPistonExtend());
    Initializer.registerEvent(new BlockPistonRetract());
    // event.enchantment
    Initializer.registerEvent(new EnchantItem());
    Initializer.registerEvent(new PrepareItemEnchant());
    // event.entity
    Initializer.registerEvent(new EntityAddToWorld());
    Initializer.registerEvent(new EntityChangeBlock());
    Initializer.registerEvent(new EntityDamage());
    Initializer.registerEvent(new EntityDamageByEntity());
    Initializer.registerEvent(new EntityDeath());
    Initializer.registerEvent(new EntityExplode());
    Initializer.registerEvent(new EntityLoadCrossbow());
    Initializer.registerEvent(new EntityMove());
    Initializer.registerEvent(new EntityPickupItem());
    Initializer.registerEvent(new EntityResurrect());
    Initializer.registerEvent(new EntityShootBow());
    Initializer.registerEvent(new EntitySpawn());
    Initializer.registerEvent(new EntityTarget());
    Initializer.registerEvent(new ExperienceOrbMerge());
    Initializer.registerEvent(new ExplosionPrime());
    Initializer.registerEvent(new LingeringPotionSplash());
    Initializer.registerEvent(new VillagerAcquireTrade());
    Initializer.registerEvent(new WitchThrowPotion());
    // event.item
    Initializer.registerEvent(new ItemMerge());
    Initializer.registerEvent(new ItemSpawn());
    // hanging
    Initializer.registerEvent(new HangingBreak());
    Initializer.registerEvent(new HangingPlace());
    // event.inventory
    Initializer.registerEvent(new Brew());
    Initializer.registerEvent(new CraftItem());
    Initializer.registerEvent(new FurnaceBurn());
    Initializer.registerEvent(new FurnaceSmelt());
    Initializer.registerEvent(new InventoryClick());
    Initializer.registerEvent(new InventoryClose());
    Initializer.registerEvent(new InventoryMoveItem());
    Initializer.registerEvent(new InventoryOpen());
    Initializer.registerEvent(new PrepareAnvil());
    Initializer.registerEvent(new PrepareItemCraft());
    Initializer.registerEvent(new PrepareResult());
    Initializer.registerEvent(new PrepareSmithing());
    // event.player.bucket
    Initializer.registerEvent(new PlayerBucketEmpty());
    Initializer.registerEvent(new PlayerBucketEntity());
    Initializer.registerEvent(new PlayerBucketFill());
    // event.player.interact
    Initializer.registerEvent(new PlayerInteract());
    Initializer.registerEvent(new PlayerInteractAtEntity());
    Initializer.registerEvent(new PlayerInteractEntity());
    // event.player.item
    Initializer.registerEvent(new PlayerDropItem());
    Initializer.registerEvent(new PlayerItemBreak());
    Initializer.registerEvent(new PlayerItemConsume());
    Initializer.registerEvent(new PlayerItemDamage());
    Initializer.registerEvent(new PlayerItemHeld());
    Initializer.registerEvent(new PlayerItemMend());
    Initializer.registerEvent(new PlayerSwapHandItems());
    // event.player
    Initializer.registerEvent(new PlayerAdvancementDone());
    Initializer.registerEvent(new PlayerChangeBeaconEffect());
    Initializer.registerEvent(new PlayerChangedWorld());
    Initializer.registerEvent(new PlayerChat());
    Initializer.registerEvent(new PlayerCommandPreprocess());
    Initializer.registerEvent(new PlayerCommandSend());
    Initializer.registerEvent(new PlayerDeath());
    Initializer.registerEvent(new PlayerEditBook());
    Initializer.registerEvent(new PlayerFish());
    Initializer.registerEvent(new PlayerGameModeChange());
    Initializer.registerEvent(new PlayerJoin());
    Initializer.registerEvent(new PlayerLaunchProjectile());
    Initializer.registerEvent(new PlayerLecternPageChange());
    Initializer.registerEvent(new PlayerLoomPatternSelect());
    Initializer.registerEvent(new PlayerMove());
    Initializer.registerEvent(new PlayerPickupArrow());
    Initializer.registerEvent(new PlayerPreLogin());
    Initializer.registerEvent(new PlayerQuit());
    Initializer.registerEvent(new PlayerRecipeBookClick());
    Initializer.registerEvent(new PlayerStoneCutterRecipeSelect());
    Initializer.registerEvent(new PlayerStopSpectatingEntity());
    Initializer.registerEvent(new PlayerTakeLecternBook());
    Initializer.registerEvent(new PlayerTeleport());
    Initializer.registerEvent(new PlayerToggleSneak());
    // event.server
    Initializer.registerEvent(new ServerCommand());
    Initializer.registerEvent(new ServerListPing());

    if (using_QuickShop)
    {
      Initializer.registerEvent(new ShopDelete());
      Initializer.registerEvent(new ShopItemChange());
      Initializer.registerEvent(new ShopPreCreate());
      Initializer.registerEvent(new ShopPriceChange());
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
    this.currentConfigVersion = this.getConfig().getInt("config-version");
  }

  private void registerCustomConfig()
  {
    Initializer.loadCustomConfigs();
  }
}
