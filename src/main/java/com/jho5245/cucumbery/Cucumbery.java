package com.jho5245.cucumbery;

import com.jho5245.cucumbery.commands.*;
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
import com.jho5245.cucumbery.commands.sound.CommandPlaySound;
import com.jho5245.cucumbery.commands.sound.CommandSong;
import com.jho5245.cucumbery.commands.teleport.CommandAdvancedTeleport;
import com.jho5245.cucumbery.commands.teleport.CommandSwapTeleport;
import com.jho5245.cucumbery.commands.teleport.CommandTeleport;
import com.jho5245.cucumbery.commands.teleport.CommandWarp;
import com.jho5245.cucumbery.customeffect.CustomEffectManager;
import com.jho5245.cucumbery.listeners.UnknownCommand;
import com.jho5245.cucumbery.listeners.addon.noteblockapi.SongEnd;
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
import com.jho5245.cucumbery.listeners.entity.customeffect.EntityCustomEffectApply;
import com.jho5245.cucumbery.listeners.entity.customeffect.EntityCustomEffectPreApply;
import com.jho5245.cucumbery.listeners.entity.customeffect.EntityCustomEffectPreRemove;
import com.jho5245.cucumbery.listeners.entity.customeffect.EntityCustomEffectRemove;
import com.jho5245.cucumbery.listeners.entity.damage.EntityDamage;
import com.jho5245.cucumbery.listeners.entity.damage.EntityDamageByBlock;
import com.jho5245.cucumbery.listeners.entity.damage.EntityDamageByEntity;
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
import com.jho5245.cucumbery.util.*;
import com.jho5245.cucumbery.util.addons.Songs;
import com.jho5245.cucumbery.util.storage.CustomConfig.UserData;
import com.jho5245.cucumbery.util.storage.SoundPlay;
import com.jho5245.cucumbery.util.storage.Updater;
import com.jho5245.cucumbery.util.storage.component.util.ComponentUtil;
import com.jho5245.cucumbery.util.storage.data.Constant;
import com.jho5245.cucumbery.util.storage.data.Prefix;
import com.jho5245.cucumbery.util.storage.data.Variable;
import com.jho5245.cucumbery.util.storage.data.custom_enchant.CustomEnchant;
import com.xxmicloxx.NoteBlockAPI.songplayer.RadioSongPlayer;
import net.milkbowl.vault.chat.Chat;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.InventoryView;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.maxgamer.quickshop.api.QuickShopAPI;
import org.maxgamer.quickshop.api.shop.Shop;

import java.io.File;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Cucumbery extends JavaPlugin
{
  public static final int CONFIG_VERSION = 14;
  private static final ExecutorService brigadierService = Executors.newFixedThreadPool(1);
  public static YamlConfiguration config;
  public static boolean using_CommandAPI;
  public static boolean using_Vault_Economy;
  public static boolean using_Vault_Chat;
  public static boolean using_NoteBlockAPI;
  public static boolean using_QuickShop;
  public static boolean using_PlaceHolderAPI;
  public static boolean using_ItemEdit;
  public static boolean using_mcMMO;
  public static Economy eco;
  public static Chat chat;
  public static File file;
  public static File dataFolder;
  public static long runTime;
  private static Cucumbery cucumbery;
  public PluginDescriptionFile pluginDescriptionFile;
  public int currentConfigVersion;
  private PluginManager pluginManager;

  public static Cucumbery getPlugin()
  {
    return Cucumbery.cucumbery;
  }

  public PluginManager getPluginManager()
  {
    return pluginManager;
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
      if (UserData.SHOW_PLUGIN_DEV_DEBUG_MESSAGE.getBoolean(online))
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
    file = this.getFile();
    dataFolder = this.getDataFolder();
    Cucumbery.config = (YamlConfiguration) this.getConfig();
    this.pluginDescriptionFile = this.getDescription();
    this.pluginManager = Bukkit.getServer().getPluginManager();
    this.currentConfigVersion = Cucumbery.config.getInt("config-version");
    if (currentConfigVersion != CONFIG_VERSION)
    {
      MessageUtil.consoleSendMessage(Prefix.INFO_WARN, "&econfig 파일의 버전이 최신 버전과 일치하지 않습니다! 현재 버전 : " + currentConfigVersion + ", 최신 버전 : " + CONFIG_VERSION);
      MessageUtil.consoleSendMessage(Prefix.INFO, "config 파일을 삭제하고 플러그인을 리로드하여 config 파일을 재생성하시거나 플러그인 파일에 있는 config에서 직접 값을 붙여넣어 주세요.");
    }
    try
    {
      this.registerItems();
    }
    catch (Throwable e)
    {
      e.printStackTrace();
    }
    Scheduler.Schedule(this);
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
    CustomEffectManager.save();
    Initializer.loadBrigadierTabListConfig();
    for (Player player : Bukkit.getOnlinePlayers())
    {
      InventoryView inventoryView = player.getOpenInventory();
      String title = ComponentUtil.serialize(inventoryView.title());
      if (title.contains(Constant.GUI_SUFFIX) || CreateGUI.isGUITitle(inventoryView.title()))
      {
        player.closeInventory();
        MessageUtil.sendWarn(player, "플러그인이 비활성화되어 GUI 창이 닫힙니다.");
      }
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
    CustomEnchant.onDisable();
    if (Cucumbery.using_CommandAPI)
    {
      brigadierService.shutdownNow();
    }
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
    for (Player onlone : Bukkit.getOnlinePlayers())
    {
      onlone.hideBossBar(Scheduler.serverRadio);
    }
  }

  private void registerItems()
  {
    this.registerConfig();
    try
    {
      this.checkUsingAddons();
      this.registerCustomConfig();
    }
    catch (Throwable e)
    {
      e.printStackTrace();
    }
    CustomEnchant.onEnable();
    if (using_CommandAPI)
    {
      try
      {
        brigadierService.submit(this::registerBrigadierCommands);
      }
      catch (Throwable e)
      {
        e.printStackTrace();
      }
    }
    try
    {
      this.registerCommands();
    }
    catch (Throwable e)
    {
      e.printStackTrace();
    }
    try
    {
      this.registerEvents();
    }
    catch (Throwable e)
    {
      e.printStackTrace();
    }
  }

  private void checkUsingAddons()
  {
    Cucumbery.using_CommandAPI = Cucumbery.config.getBoolean("use-hook-plugins.CommandAPI") && this.pluginManager.getPlugin("CommandAPI") != null;
    Cucumbery.using_Vault_Economy = Cucumbery.config.getBoolean("use-hook-plugins.Vault") && Initializer.setupEconomy() && eco != null;
    Cucumbery.using_Vault_Chat = Cucumbery.config.getBoolean("use-hook-plugins.Vault") && Initializer.setupChat() && chat != null;
    Cucumbery.using_NoteBlockAPI = Cucumbery.config.getBoolean("use-hook-plugins.NoteBlockAPI") && this.pluginManager.getPlugin("NoteBlockAPI") != null;
    Cucumbery.using_QuickShop = Cucumbery.config.getBoolean("use-hook-plugins.QuickShop") && this.pluginManager.getPlugin("QuickShop") != null;
    Cucumbery.using_PlaceHolderAPI = Cucumbery.config.getBoolean("use-hook-plugins.PlaceHolderAPI") && this.pluginManager.getPlugin("PlaceHolderAPI") != null;
    Cucumbery.using_ItemEdit = Cucumbery.config.getBoolean("use-hook-plugins.ItemEdit") && this.pluginManager.getPlugin("ItemEdit") != null;
    Cucumbery.using_mcMMO = Cucumbery.config.getBoolean("use-hook-plugins.mcMMO") && this.pluginManager.getPlugin("mcMMO") != null;
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
          e.printStackTrace();
        }
      }, 100L);
    }
  }

  private void registerBrigadierCommands()
  {
    new ExtraExecuteArgument().registerArgument();
    new CommandRide().registerCommand("ride", "cucumbery.command.ride", "cride");
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
    new CommandDamage().registerCommand("damage", "cucumbery.command.damage", "cdamage");
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
    Initializer.registerCommand("setuserdata", new CommandSetUserData());
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
    Initializer.registerCommand("getuserdata", new CommandGetUserData());
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
  }

  private void registerEvents()
  {
    // listener
    Initializer.registerEvent(new UnknownCommand());

    // listener.block
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
    Initializer.registerEvent(new NotePlay());
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
    Initializer.registerEvent(new EntityExplode());
    Initializer.registerEvent(new EntityJump());
    Initializer.registerEvent(new EntityLoadCrossbow());
    Initializer.registerEvent(new EntityMount());
    Initializer.registerEvent(new EntityMove());
    Initializer.registerEvent(new EntityPickupItem());
    Initializer.registerEvent(new EntityPotionEffect());
    Initializer.registerEvent(new EntityRemoveFromWorld());
    Initializer.registerEvent(new EntityResurrect());
    Initializer.registerEvent(new EntityShootBow());
    Initializer.registerEvent(new EntitySpawn());
    Initializer.registerEvent(new EntityTarget());
    Initializer.registerEvent(new ExperienceOrbMerge());
    Initializer.registerEvent(new ExplosionPrime());
    Initializer.registerEvent(new LingeringPotionSplash());
    Initializer.registerEvent(new PotionSplash());
    Initializer.registerEvent(new ProjectileLaunch());
    Initializer.registerEvent(new VillagerAcquireTrade());
    Initializer.registerEvent(new WitchThrowPotion());
    // listener.entity.customeffect
    Initializer.registerEvent(new EntityCustomEffectApply());
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
    Initializer.registerEvent(new PlayerCommandPreprocess());
    Initializer.registerEvent(new PlayerCommandSend());
    Initializer.registerEvent(new PlayerDeath());
    Initializer.registerEvent(new PlayerEditBook());
    Initializer.registerEvent(new PlayerElytraBoost());
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
    Initializer.registerEvent(new PlayerPreLogin());
    Initializer.registerEvent(new PlayerQuit());
    Initializer.registerEvent(new PlayerRecipeBookClick());
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
    Initializer.registerEvent(new PlayerSwapHandItems());
    // listener.server
    Initializer.registerEvent(new ServerCommand());
    Initializer.registerEvent(new ServerListPing());
    // listener.addon.quickshop
    if (using_QuickShop)
    {
      Initializer.registerEvent(new ShopDelete());
      Initializer.registerEvent(new ShopItemChange());
      Initializer.registerEvent(new ShopPreCreate());
      Initializer.registerEvent(new ShopPriceChange());
    }
    // listener.addon.noteblockapi
    if (using_NoteBlockAPI)
    {
      Initializer.registerEvent(new SongEnd());
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
    Initializer.loadPlayersConfig();
  }
}
