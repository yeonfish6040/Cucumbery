package com.jho5245.cucumbery;

import com.jho5245.cucumbery.commands.sound.CommandSong;
import com.jho5245.cucumbery.custom.customeffect.CustomEffectManager;
import com.jho5245.cucumbery.custom.customeffect.type.CustomEffectType;
import com.jho5245.cucumbery.custom.custommerchant.MerchantData;
import com.jho5245.cucumbery.deathmessages.CustomDeathMessage;
import com.jho5245.cucumbery.util.itemlore.ItemLore;
import com.jho5245.cucumbery.util.no_groups.*;
import com.jho5245.cucumbery.util.storage.component.util.ComponentUtil;
import com.jho5245.cucumbery.util.storage.component.util.sendercomponent.SenderComponentUtil;
import com.jho5245.cucumbery.util.storage.data.Variable;
import com.jho5245.cucumbery.util.storage.no_groups.CustomConfig;
import com.jho5245.cucumbery.util.storage.no_groups.CustomConfig.UserData;
import net.kyori.adventure.text.Component;
import net.milkbowl.vault.chat.Chat;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.TabCompleter;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import static com.jho5245.cucumbery.Cucumbery.getPlugin;

public class Initializer
{
  public static void registerCommand(@NotNull String command, @NotNull Object object)
  {
    PluginCommand pluginCommand = getPlugin().getCommand(command);
    if (pluginCommand != null)
    {
      if (object instanceof CommandExecutor commandExecutor)
      {
        pluginCommand.setExecutor(commandExecutor);
      }
      if (object instanceof TabCompleter tabCompleter)
      {
        pluginCommand.setTabCompleter(tabCompleter);
      }
    }
  }

  public static void registerCommand(@NotNull String command, @NotNull CommandExecutor executor, @NotNull TabCompleter tabCompleter)
  {
    PluginCommand pluginCommand = getPlugin().getCommand(command);
    if (pluginCommand != null)
    {
      pluginCommand.setExecutor(executor);
      pluginCommand.setTabCompleter(tabCompleter);
    }
  }

  public static void registerEvent(Listener listener)
  {
    getPlugin().getPluginManager().registerEvents(listener, getPlugin());
  }

  public static boolean setupEconomy()
  {
    if (getPlugin().getPluginManager().getPlugin("Vault") == null)
    {
      return false;
    }
    RegisteredServiceProvider<Economy> rsp = Bukkit.getServer().getServicesManager().getRegistration(Economy.class);
    if (rsp == null)
    {
      return false;
    }
    Cucumbery.eco = rsp.getProvider();
    return true;
  }

  public static boolean setupChat()
  {
    if (getPlugin().getPluginManager().getPlugin("Vault") == null)
    {
      return false;
    }
    RegisteredServiceProvider<Chat> rsp = Bukkit.getServer().getServicesManager().getRegistration(Chat.class);
    if (rsp != null)
    {
      Cucumbery.chat = rsp.getProvider();
    }
    return Cucumbery.chat != null;
  }

  public static void loadDeathMessagesConfig()
  {
    File file = new File(Cucumbery.getPlugin().getDataFolder() + "/DeathMessages.yml");
    if (!file.exists())
    {
      Cucumbery.getPlugin().saveResource("DeathMessages.yml", false);
    }
    CustomConfig deathMessagesConfig = CustomConfig.getCustomConfig("DeathMessages.yml");
    Variable.deathMessages = deathMessagesConfig.getConfig();
    CustomDeathMessage.register();
  }

  public static void loadLang()
  {
    File file = new File(Cucumbery.getPlugin().getDataFolder() + "/lang.yml");
    if (!file.exists())
    {
      Cucumbery.getPlugin().saveResource("lang.yml", false);
    }
    CustomConfig langConfig = CustomConfig.getCustomConfig("lang.yml");
    ConfigurationSection root = langConfig.getConfig().getConfigurationSection("");
    if (root != null)
    {
      for (String key : root.getKeys(true))
      {
        Variable.lang.set(key, root.get(key));
      }
    }
  }

  public static void loadCustomItems()
  {
    File file = new File(Cucumbery.getPlugin().getDataFolder() + "/CustomItems.yml");
    if (!file.exists())
    {
      Cucumbery.getPlugin().saveResource("CustomItems.yml", false);
    }
    CustomConfig customItemsConfig = CustomConfig.getCustomConfig("CustomItems.yml");
    ConfigurationSection root = customItemsConfig.getConfig().getConfigurationSection("");
    if (root != null)
    {
      for (String key : root.getKeys(true))
      {
        Variable.customItemsConfig.set(key, root.get(key));
      }
    }
  }

  public static void loadCustomEffects()
  {
    CustomEffectType.unregister();
    CustomEffectType.register();
    CustomEffectManager.effectMap.clear();
    File customEffectsFolder = new File(getPlugin().getDataFolder() + "/data/CustomEffects");
    if (customEffectsFolder.exists())
    {
      File[] customEffectFiles = customEffectsFolder.listFiles();
      if (customEffectFiles == null)
      {
        customEffectFiles = new File[]{};
      }
      for (File folder : customEffectFiles)
      {
        if (folder.isDirectory())
        {
          File[] files = folder.listFiles();
          if (files != null)
          {
            for (File file : files)
            {
              String fileName7 = file.getName();
              if (fileName7.endsWith(".yml"))
              {
                fileName7 = fileName7.substring(0, fileName7.length() - 4);
                if (Method.isUUID(fileName7))
                {
                  UUID uuid = UUID.fromString(fileName7);
                  if (Method2.getEntityAsync(uuid) == null && !Bukkit.getOfflinePlayer(uuid).hasPlayedBefore())
                  {
                    if (!file.delete())
                    {
                      Bukkit.getLogger().severe("Could not delete " + file + " file!");
                    }
                    continue;
                  }
                  CustomConfig customConfig = CustomConfig.getCustomConfig(file);
                  YamlConfiguration config = customConfig.getConfig();
                  CustomEffectManager.load(uuid, config);
                }
              }
            }
          }
        }
        else
        {
          String fileName7 = folder.getName();
          if (fileName7.endsWith(".yml"))
          {
            fileName7 = fileName7.substring(0, fileName7.length() - 4);
            if (Method.isUUID(fileName7))
            {
              UUID uuid = UUID.fromString(fileName7);
              if (Method2.getEntityAsync(uuid) == null && !Bukkit.getOfflinePlayer(uuid).hasPlayedBefore())
              {
                if (!folder.delete())
                {
                  Bukkit.getLogger().severe("Could not delete " + folder + " file!");
                }
                continue;
              }
              CustomConfig customConfig = CustomConfig.getCustomConfig(folder);
              YamlConfiguration config = customConfig.getConfig();
              CustomEffectManager.load(uuid, config);
            }
          }
        }
      }
    }
  }

  public static void loadCustomRecipes()
  {
    Variable.customRecipes.clear();
    File recipesFolder = new File(getPlugin().getDataFolder() + "/data/CustomRecipe");
    if (recipesFolder.exists())
    {
      File[] recipeFiles = recipesFolder.listFiles();
      if (recipeFiles == null)
      {
        recipeFiles = new File[]{};
      }
      for (File file : recipeFiles)
      {
        String fileName3 = file.getName();
        boolean hasColorCode = !fileName3.equals(MessageUtil.stripColor(fileName3));
        if (fileName3.endsWith(".yml") && !fileName3.contains(" ") && !hasColorCode)
        {
          fileName3 = fileName3.substring(0, fileName3.length() - 4);
          Variable.customRecipes.put(fileName3, CustomConfig.getCustomConfig(file).getConfig());
        }
      }
      File craftingTimeFolder = new File(getPlugin().getDataFolder() + "/data/CustomRecipe/CraftingTime");
      if (craftingTimeFolder.exists())
      {
        File[] craftingTimeFiles = craftingTimeFolder.listFiles();
        if (craftingTimeFiles == null)
        {
          craftingTimeFiles = new File[]{};
        }
        for (File file : craftingTimeFiles)
        {
          String fileName4 = file.getName();
          if (fileName4.endsWith(".yml"))
          {
            fileName4 = fileName4.substring(0, fileName4.length() - 4);
            Variable.craftingTime.put(UUID.fromString(fileName4), CustomConfig.getCustomConfig(file).getConfig());
          }
        }
      }
      File craftsLogFolder = new File(getPlugin().getDataFolder() + "/data/CustomRecipe/CraftsLog");
      if (craftsLogFolder.exists())
      {
        File[] craftsLogFiles = craftsLogFolder.listFiles();
        if (craftsLogFiles == null)
        {
          craftsLogFiles = new File[]{};
        }
        for (File file : craftsLogFiles)
        {
          String fileName5 = file.getName();
          if (fileName5.endsWith(".yml"))
          {
            fileName5 = fileName5.substring(0, fileName5.length() - 4);
            Variable.craftsLog.put(UUID.fromString(fileName5), CustomConfig.getCustomConfig(file).getConfig());
          }
        }
      }
      File lastCraftsLogFolder = new File(getPlugin().getDataFolder() + "/data/CustomRecipe/LastCraftsLog");
      if (lastCraftsLogFolder.exists())
      {
        File[] lastCraftsLogFiles = lastCraftsLogFolder.listFiles();
        if (lastCraftsLogFiles == null)
        {
          lastCraftsLogFiles = new File[]{};
        }
        for (File file : lastCraftsLogFiles)
        {
          String fileName6 = file.getName();
          if (fileName6.endsWith(".yml"))
          {
            fileName6 = fileName6.substring(0, fileName6.length() - 4);
            Variable.lastCraftsLog.put(UUID.fromString(fileName6), CustomConfig.getCustomConfig(file).getConfig());
          }
        }
      }
    }
  }

  public static void loadCustomConfigs()
  {
    loadDeathMessagesConfig();
    loadLang();
    loadCustomItems();
    loadCustomEffects();
    loadCustomRecipes();
    Variable.craftingTime.clear();
    Variable.craftsLog.clear();
    Variable.lastCraftsLog.clear();

    Variable.commandPacks.clear();
    File commandPacksFolder = new File(getPlugin().getDataFolder() + "/data/CommandPacks");
    if (commandPacksFolder.exists())
    {
      File[] commandPackFiles = commandPacksFolder.listFiles();
      if (commandPackFiles == null)
      {
        commandPackFiles = new File[]{};
      }
      for (File file : commandPackFiles)
      {
        String fileName7 = file.getName();
        if (fileName7.endsWith(".yml"))
        {
          fileName7 = fileName7.substring(0, fileName7.length() - 4);
          Variable.commandPacks.put(fileName7, CustomConfig.getCustomConfig(file).getConfig());
        }
      }
    }
    Variable.itemStorage.clear();
    File itemStorageFolder = new File(getPlugin().getDataFolder() + "/data/ItemStorage");
    if (itemStorageFolder.exists())
    {
      File[] itemStorageFiles = itemStorageFolder.listFiles();
      if (itemStorageFiles == null)
      {
        itemStorageFiles = new File[]{};
      }
      for (File file : itemStorageFiles)
      {
        String fileName4 = file.getName();
        if (fileName4.endsWith(".yml"))
        {
          fileName4 = fileName4.substring(0, fileName4.length() - 4);
          Variable.itemStorage.put(fileName4, CustomConfig.getCustomConfig(file).getConfig());
        }
      }
    }
    Variable.customItems.clear();
    File customItemFolder = new File(getPlugin().getDataFolder() + "/data/CustomItem");
    if (customItemFolder.exists())
    {
      File[] customItemFiles = customItemFolder.listFiles();
      if (customItemFiles == null)
      {
        customItemFiles = new File[]{};
      }
      for (File file : customItemFiles)
      {
        String fileName4 = file.getName();
        if (fileName4.endsWith(".yml"))
        {
          fileName4 = fileName4.substring(0, fileName4.length() - 4);
          Variable.customItems.put(fileName4, CustomConfig.getCustomConfig(file).getConfig());
        }
      }
    }
    Variable.songFiles.clear();
    File songsFolder = new File(Cucumbery.getPlugin().getDataFolder() + "/data/songs");
    if (!songsFolder.exists())
    {
      if (!songsFolder.mkdirs())
      {
        return;
      }
    }
    else
    {
      File[] songFiles = songsFolder.listFiles();
      if (songFiles == null)
      {
        songFiles = new File[]{};
      }
      for (File file : songFiles)
      {
        String fileName5 = file.getName();
        if (fileName5.endsWith(".nbs"))
        {
          Variable.songFiles.add(fileName5.substring(0, fileName5.length() - 4));
        }
      }
    }

    Variable.allPlayerConfig = CustomConfig.getCustomConfig(CustomConfig.CustomConfigType.ALLPLAYER).getConfig();
    Variable.warps.clear();
    File warpsFolder = new File(getPlugin().getDataFolder() + "/data/Warps");
    if (warpsFolder.exists())
    {
      File[] warpFiles = warpsFolder.listFiles();
      if (warpFiles == null)
      {
        warpFiles = new File[]{};
      }
      for (File file : warpFiles)
      {
        String fileName2 = file.getName();
        if (fileName2.endsWith(".yml"))
        {
          fileName2 = fileName2.substring(0, fileName2.length() - 4);
          Variable.warps.put(fileName2, CustomConfig.getCustomConfig(file).getConfig());
        }
      }
    }

    loadNicknamesConfig();

//    Variable.blockPlaceData.clear();
//    File blockPlaceDataFolder = new File(getPlugin().getDataFolder() + "/data/BlockPlaceData");
//    if (blockPlaceDataFolder.exists())
//    {
//      File[] dataFiles = blockPlaceDataFolder.listFiles();
//      if (dataFiles != null)
//      {
//        for (File file : dataFiles)
//        {
//          String fileName = file.getName();
//          if (fileName.endsWith(".yml"))
//          {
//            fileName = fileName.substring(0, fileName.length() - 4);
//            Variable.blockPlaceData.put(fileName, CustomConfig.getCustomConfig(file).getConfig());
//          }
//        }
//      }
//    }

    MerchantData.merchantDataHashMap.clear();
    File customMerchantFolder = new File(getPlugin().getDataFolder() + "/data/CustomMerchants");
    if (customMerchantFolder.exists())
    {
      File[] dataFiles = customMerchantFolder.listFiles();
      if (dataFiles != null)
      {
        for (File file : dataFiles)
        {
          String fileName = file.getName();
          if (fileName.endsWith(".yml"))
          {
            fileName = fileName.substring(0, fileName.length() - 4);
            CustomConfig customConfig = CustomConfig.getCustomConfig(file);
            YamlConfiguration configuration = customConfig.getConfig();
            String display = configuration.getString("display");
            if (display == null)
            {
              display = fileName;
            }
            new MerchantData(fileName, display, configuration);
          }
        }
      }
    }

    Variable.itemStash.clear();
    File itemStashFolder = new File(getPlugin().getDataFolder() + "/data/ItemStash");
    if (itemStashFolder.exists())
    {
      File[] dataFiles = itemStashFolder.listFiles();
      if (dataFiles != null)
      {
        for (File file : dataFiles)
        {
          String fileName = file.getName();
          if (fileName.endsWith(".yml"))
          {
            fileName = fileName.substring(0, fileName.length() - 4);
            if (Method.isUUID(fileName))
            {
              UUID uuid = UUID.fromString(fileName);
              CustomConfig customConfig = CustomConfig.getCustomConfig(file);
              YamlConfiguration configuration = customConfig.getConfig();
              List<String> list = configuration.getStringList("items");
              List<ItemStack> itemStacks = new ArrayList<>();
              list.forEach(s ->
              {
                ItemStack itemStack = ItemSerializer.deserialize(s);
                if (!itemStack.getType().isAir())
                {
                  itemStacks.add(itemStack);
                }
              });
              Variable.itemStash.put(uuid, itemStacks);
            }
          }
        }
      }
    }
  }

  public static void loadBrigadierTabListConfig()
  {
    CustomConfig brigadierTabListCustomConfig = CustomConfig.getCustomConfig("data/brigadier_tab_list.yml");
    YamlConfiguration brigadierTabListConfig = brigadierTabListCustomConfig.getConfig();
    List<String> worlds = new ArrayList<>();
    for (World world : Bukkit.getWorlds())
    {
      worlds.add(world.getName());
    }
    brigadierTabListConfig.set("worldNames", worlds);
    List<String> commands = Method.getAllServerCommands();
    brigadierTabListConfig.set("commands", commands);

    brigadierTabListCustomConfig.saveConfig();
  }

  public static void loadNicknamesConfig()
  {
    Variable.nickNames.clear();
    File userDataFolder = new File(getPlugin().getDataFolder() + "/data/UserData");
    if (userDataFolder.exists())
    {
      File nickNamesFile = new File(getPlugin().getDataFolder() + "/data/Nicknames.yml");
      if (!nickNamesFile.exists())
      {
        boolean success = nickNamesFile.getParentFile().mkdirs();
        if (!success)
        {
          System.err.println("[Cucumbery] Could not create data folder!");
        }
      }
      CustomConfig nickNamesCustonConfig = CustomConfig.getCustomConfig(nickNamesFile);
      YamlConfiguration nickNamesConfig = nickNamesCustonConfig.getConfig();
      List<String> nickNames = new ArrayList<>();
      File[] userDataFiles = userDataFolder.listFiles();
      if (userDataFiles == null)
      {
        userDataFiles = new File[]{};
      }
      for (File userDataFile : userDataFiles)
      {
        String fileName = userDataFile.getName();
        if (fileName.endsWith(".yml"))
        {
          CustomConfig userDataCustomConfig = CustomConfig.getCustomConfig(userDataFile);
          YamlConfiguration userDataConfig = userDataCustomConfig.getConfig();
          String uuid = fileName.substring(0, fileName.length() - 4);
          String id = userDataConfig.getString(CustomConfig.UserData.ID.getKey());
          String display = userDataConfig.getString(CustomConfig.UserData.DISPLAY_NAME.getKey());
          String listName = userDataConfig.getString(CustomConfig.UserData.PLAYER_LIST_NAME.getKey());
          if (!nickNames.contains(uuid))
          {
            nickNames.add(uuid);
          }
          if (id != null && !nickNames.contains(id))
          {
            nickNames.add(id);
          }
          if (display != null)
          {
            display = MessageUtil.stripColor(ComponentUtil.serialize(ComponentUtil.create(display)));
            if (!nickNames.contains(display))
            {
              nickNames.add(MessageUtil.stripColor(display));
            }
          }
          if (listName != null)
          {
            listName = MessageUtil.stripColor(ComponentUtil.serialize(ComponentUtil.create(listName)));
            if (!nickNames.contains(listName))
            {
              nickNames.add(MessageUtil.stripColor(listName));
            }
          }
        }
      }
      nickNamesConfig.set("nicks", nickNames);
      nickNamesCustonConfig.saveConfig();
      Variable.nickNames.addAll(nickNames);
    }
    cacheUUID();
  }

  public static void cacheUUID()
  {
    for (String nickName : Variable.nickNames)
    {
      if (Method.isUUID(nickName))
      {
        continue;
      }
      if (nickName.startsWith("@"))
      {
        continue;
      }
      OfflinePlayer offlinePlayer = SelectorUtil.getOfflinePlayer(null, nickName, false);
      if (offlinePlayer == null)
      {
        continue;
      }
      Variable.cachedUUIDs.put(nickName, offlinePlayer.getUniqueId());
    }
  }

  public static void loadPlayerConfig(Player player)
  {
    loadUserDataConfig(player);
    loadCooldownConfig(player);
  }

  public static void loadPlayersConfig()
  {
    Variable.userData.clear();
    Variable.userDataUUIDs.clear();
    Variable.cooldownsItemUsage.clear();
    for (Player player : Bukkit.getServer().getOnlinePlayers())
    {
      loadPlayerConfig(player);
    }
  }

  public static void loadCooldownConfig(Player player)
  {
    UUID playerUUID = player.getUniqueId();
    File file = new File(getPlugin().getDataFolder() + "/data/ItemUsage/Cooldown/" + playerUUID + ".yml");
    if (file.exists())
    {
      CustomConfig cooldownCustomConfig = CustomConfig.getCustomConfig(file);
      Variable.cooldownsItemUsage.put(playerUUID, cooldownCustomConfig.getConfig());
    }
  }

  private static void loadUserDataConfig(Player player)
  {
    UUID uuid = player.getUniqueId();
    CustomConfig customConfig = CustomConfig.getPlayerConfig(player.getUniqueId());
    YamlConfiguration userCfg = customConfig.getConfig();
    Variable.userData.put(uuid, userCfg);
    Variable.userDataUUIDs.add(uuid.toString());
    Variable.nickNames.add(player.getName());
    Variable.cachedUUIDs.put(player.getName(), uuid);
    String displayname = UserData.DISPLAY_NAME.getString(player.getUniqueId());
    String playerListName = UserData.PLAYER_LIST_NAME.getString(player.getUniqueId());
    if (displayname != null)
    {
      displayname = MessageUtil.stripColor(displayname);
      Variable.nickNames.add(displayname);
      Variable.cachedUUIDs.put(displayname, uuid);
    }
    if (playerListName != null)
    {
      playerListName = MessageUtil.stripColor(playerListName);
      Variable.nickNames.add(playerListName);
      Variable.cachedUUIDs.put(playerListName, uuid);
    }
    int invincibleTime = UserData.INVINCIBLE_TIME.getInt(uuid), loginInvincibleTime = UserData.INVINCIBLE_TIME_JOIN.getInt(uuid);
    if (invincibleTime >= 0)
    {
      player.setMaximumNoDamageTicks(invincibleTime);
    }
    if (loginInvincibleTime >= 0)
    {
      player.setNoDamageTicks(loginInvincibleTime);
    }
    if (Cucumbery.using_NoteBlockAPI)
    {
      if (CommandSong.playerRadio.containsKey(uuid))
      {
        if (UserData.LISTEN_GLOBAL.getBoolean(uuid) || UserData.LISTEN_GLOBAL_FORCE.getBoolean(uuid))
        {
          CommandSong.playerRadio.get(uuid).addPlayer(player);
        }
        else
        {
          CommandSong.playerRadio.get(uuid).removePlayer(player);
        }
      }
      else if (CommandSong.radioSongPlayer != null)
      {
        if (UserData.LISTEN_GLOBAL.getBoolean(uuid) || UserData.LISTEN_GLOBAL_FORCE.getBoolean(uuid))
        {
          CommandSong.radioSongPlayer.addPlayer(player);
        }
        else
        {
          CommandSong.radioSongPlayer.removePlayer(player);
        }
      }
    }
    boolean healthScaled = UserData.HEALTH_SCALED.getBoolean(uuid);
    if (healthScaled)
    {
      player.setHealthScaled(false);
    }
    else
    {
      double healthbar = UserData.HEALTH_BAR.getDouble(uuid);
      if (healthbar <= 0D)
      {
        healthbar = 20D;
      }
      player.setHealthScale(healthbar);
    }
    YamlConfiguration cfg = Cucumbery.config;
    if (cfg.getBoolean("use-nickname-feature"))
    {
      displayname = UserData.DISPLAY_NAME.getString(uuid);
      playerListName = UserData.PLAYER_LIST_NAME.getString(uuid);
    }
    Component senderComponent = SenderComponentUtil.senderComponent(player, player, null);
    if (displayname == null)
    {
      displayname = player.getName();
    }
    Component finalDislay = ComponentUtil.create(displayname).hoverEvent(senderComponent.hoverEvent()).clickEvent(senderComponent.clickEvent());
    player.displayName(finalDislay);
    senderComponent = SenderComponentUtil.senderComponent(player, player, null);
    player.displayName(senderComponent);
    if (playerListName == null)
    {
      playerListName = player.getName();
    }
    finalDislay = ComponentUtil.create(playerListName);
    final Component originalDisplayName = player.displayName();
    player.displayName(finalDislay.hoverEvent(null).clickEvent(null).insertion(null));
    senderComponent = SenderComponentUtil.senderComponent(player, player, null);
    player.playerListName(senderComponent.hoverEvent(null).clickEvent(null).insertion(null));
    player.displayName(originalDisplayName);
    boolean isSpectator = UserData.SPECTATOR_MODE.getBoolean(player);
    if (isSpectator && UserData.SPECTATOR_MODE_ON_JOIN.getBoolean(player))
    {
      if (player.getGameMode() != GameMode.SPECTATOR)
      {
        player.setGameMode(GameMode.SPECTATOR);
        MessageUtil.info(player, ComponentUtil.translate("관전자여서 게임 모드가 자동으로 관전 모드로 전환되었습니다"));
      }
    }
  }

  public static void setNickName(@NotNull Player player)
  {
    YamlConfiguration cfg = Cucumbery.config;
    if (!cfg.getBoolean("use-nickname-feature"))
    {
      return;
    }
    String displayName = UserData.DISPLAY_NAME.getString(player), listName = UserData.PLAYER_LIST_NAME.getString(player);
    if (displayName == null)
    {
      displayName = player.getName();
    }
    if (listName == null)
    {
      listName = player.getName();
    }
    Component display = ComponentUtil.create(displayName);
    player.displayName(display);
    Component list = ComponentUtil.create(listName);
    player.playerListName(list);
  }

  public static void saveUserData()
  {
    for (UUID uuid : Variable.userData.keySet())
    {
      saveUserData(uuid);
    }
    Variable.userData.keySet().removeIf(uuid -> Bukkit.getPlayer(uuid) == null);
  }

  public static void saveUserData(@NotNull UUID uuid)
  {
    if (Variable.userData.containsKey(uuid))
    {
      YamlConfiguration cacheConfig = Variable.userData.get(uuid);
      ConfigurationSection cacheSection = cacheConfig.getConfigurationSection("");
      if (cacheSection != null)
      {
        CustomConfig customConfig = CustomConfig.getPlayerConfig(uuid);
        YamlConfiguration config = customConfig.getConfig();
        ConfigurationSection section = config.getConfigurationSection("");
        if (section != null)
        {
          for (String key : section.getKeys(true))
          {
            config.set(key, null);
          }
        }
        for (String key : cacheSection.getKeys(true))
        {
          config.set(key, cacheSection.get(key));
        }
        customConfig.saveConfig();
      }
    }
  }

  public static void saveItemUsageData()
  {
    HashMap<UUID, YamlConfiguration> removal = new HashMap<>();
    for (UUID uuid : Variable.cooldownsItemUsage.keySet())
    {
      YamlConfiguration cacheConfig = Variable.cooldownsItemUsage.get(uuid);
      ConfigurationSection cacheSection = cacheConfig.getConfigurationSection("");
      if (cacheSection != null)
      {
        CustomConfig customConfig = CustomConfig.getCustomConfig("data/ItemUsage/Cooldown/" + uuid.toString() + ".yml");
        YamlConfiguration config = customConfig.getConfig();
        for (String key : cacheSection.getKeys(true))
        {
          config.set(key, cacheSection.get(key));
        }
        customConfig.saveConfig();
      }
      if (Bukkit.getPlayer(uuid) == null)
      {
        removal.put(uuid, Variable.cooldownsItemUsage.get(uuid));
      }
    }
    for (UUID uuid : removal.keySet())
    {
      Variable.cooldownsItemUsage.remove(uuid);
    }
  }

  public static void saveItemStashData()
  {
    for (UUID uuid : Variable.itemStash.keySet())
    {
      List<ItemStack> itemStacks = Variable.itemStash.get(uuid);
      CustomConfig customConfig = CustomConfig.getCustomConfig("data/ItemStash/" + uuid + ".yml");
      YamlConfiguration config = customConfig.getConfig();
      List<String> list = new ArrayList<>();
      itemStacks.forEach(itemStack -> list.add(ItemSerializer.serialize(ItemLore.removeItemLore(itemStack))));
      config.set("items", list);
      customConfig.saveConfig();
    }
  }
}
