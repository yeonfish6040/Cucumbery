package com.jho5245.cucumbery.commands.no_groups;

import com.destroystokyo.paper.event.server.AsyncTabCompleteEvent.Completion;
import com.jho5245.cucumbery.Cucumbery;
import com.jho5245.cucumbery.Initializer;
import com.jho5245.cucumbery.custom.customeffect.CustomEffectManager;
import com.jho5245.cucumbery.custom.customeffect.custom_mining.MiningScheduler;
import com.jho5245.cucumbery.util.blockplacedata.BlockPlaceDataConfig;
import com.jho5245.cucumbery.util.no_groups.*;
import com.jho5245.cucumbery.util.plugin_support.CustomRecipeSupport;
import com.jho5245.cucumbery.util.plugin_support.QuickShopSupport;
import com.jho5245.cucumbery.util.storage.component.util.ComponentUtil;
import com.jho5245.cucumbery.util.storage.data.Constant;
import com.jho5245.cucumbery.util.storage.data.Permission;
import com.jho5245.cucumbery.util.storage.data.Variable;
import com.jho5245.cucumbery.util.storage.data.custom_enchant.CustomEnchant;
import com.jho5245.cucumbery.util.storage.no_groups.CustomConfig.UserData;
import com.jho5245.cucumbery.util.storage.no_groups.ItemStackUtil;
import com.jho5245.cucumbery.util.storage.no_groups.PluginLoader;
import com.jho5245.cucumbery.util.storage.no_groups.RecipeChecker;
import com.jho5245.cucumbery.util.storage.no_groups.Updater;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.maxgamer.quickshop.api.QuickShopAPI;

import java.io.File;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@SuppressWarnings("deprecation")
public class CommandCucumbery implements CucumberyCommandExecutor
{
  private final Set<String> isTryingUpdating = new HashSet<>();
  @Override
  public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args)
  {
    if (!Method.hasPermission(sender, Permission.CMD_MAINCOMMAND, true))
    {
      return true;
    }
    if (!MessageUtil.checkQuoteIsValidInArgs(sender, args = MessageUtil.wrapWithQuote(args)))
    {
      return !(sender instanceof BlockCommandSender);
    }
    if (args.length == 0)
    {
      MessageUtil.shortArg(sender, 1, args);
      MessageUtil.commandInfo(sender, label, Method.getUsage(cmd));
      return true;
    }
    switch (args[0])
    {
      case "reload" ->
      {
        MessageUtil.broadcastDebug(ComponentUtil.translate("%s이(가) /cucumbery reload 명령어 사용", sender));
        Cucumbery.getPlugin().registerConfig();
        Cucumbery.getPlugin().reloadConfig();
        Cucumbery.config = (YamlConfiguration) Cucumbery.getPlugin().getConfig();
        Initializer.saveUserData();
        BlockPlaceDataConfig.saveAll();
        Initializer.saveItemUsageData();
        Initializer.saveItemStashData();
        CustomEffectManager.saveAll();
        Initializer.loadCustomConfigs();
        Initializer.loadPlayersConfig();
        if (Cucumbery.using_QuickShop)
        {
          Variable.shops.clear();
          try
          {
            QuickShopAPI shopAPI = (QuickShopAPI) Cucumbery.getPlugin().getPluginManager().getPlugin("QuickShop");
            if (shopAPI != null)
            {
              Variable.shops.addAll(shopAPI.getShopManager().getAllShops());
            }
          }
          catch (Exception ignored)
          {

          }
        }
        RecipeChecker.setRecipes();
        for (Player player : Bukkit.getOnlinePlayers())
        {
          ItemStackUtil.updateInventory(player);
        }
        try
        {
          Constant.WARNING_SOUND = Sound.valueOf(Cucumbery.config.getString("sound-const.warning-sound.sound"));
        }
        catch (Exception e)
        {
          Constant.WARNING_SOUND = Sound.ENTITY_ENDERMAN_TELEPORT;
        }
        try
        {
          Constant.ERROR_SOUND = Sound.valueOf(Cucumbery.config.getString("sound-const.error-sound.sound"));
        }
        catch (Exception e)
        {
          Constant.ERROR_SOUND = Sound.BLOCK_ANVIL_LAND;
        }
        Constant.WARNING_SOUND_PITCH = Cucumbery.config.getDouble("sound-const.warning-sound.pitch");
        Constant.WARNING_SOUND_VOLUME = Cucumbery.config.getDouble("sound-const.warning-sound.volume");
        Constant.ERROR_SOUND_PITCH = Cucumbery.config.getDouble("sound-const.error-sound.pitch");
        Constant.ERROR_SOUND_VOLUME = Cucumbery.config.getDouble("sound-const.error-sound.volume");
        MessageUtil.info(sender, "모든 콘픽 파일을 리로드했습니다");
      }
      case "reload-configs" ->
      {
        MessageUtil.broadcastDebug(ComponentUtil.translate("%s이(가) /cucumbery reload-configs 명령어 사용", sender));
        Cucumbery.getPlugin().registerConfig();
        Cucumbery.getPlugin().reloadConfig();
        Cucumbery.config = (YamlConfiguration) Cucumbery.getPlugin().getConfig();
        try
        {
          Constant.WARNING_SOUND = Sound.valueOf(Cucumbery.config.getString("sound-const.warning-sound.sound"));
        }
        catch (Exception e)
        {
          Constant.WARNING_SOUND = Sound.ENTITY_ENDERMAN_TELEPORT;
        }
        try
        {
          Constant.ERROR_SOUND = Sound.valueOf(Cucumbery.config.getString("sound-const.error-sound.sound"));
        }
        catch (Exception e)
        {
          Constant.ERROR_SOUND = Sound.BLOCK_ANVIL_LAND;
        }
        Constant.WARNING_SOUND_PITCH = Cucumbery.config.getDouble("sound-const.warning-sound.pitch");
        Constant.WARNING_SOUND_VOLUME = Cucumbery.config.getDouble("sound-const.warning-sound.volume");
        Constant.ERROR_SOUND_PITCH = Cucumbery.config.getDouble("sound-const.error-sound.pitch");
        Constant.ERROR_SOUND_VOLUME = Cucumbery.config.getDouble("sound-const.error-sound.volume");
        Initializer.loadLang();
        Initializer.loadDeathMessagesConfig();
        Initializer.loadCustomItems();
        for (Player player : Bukkit.getOnlinePlayers())
        {
          ItemStackUtil.updateInventory(player);
        }
        MessageUtil.info(sender, "config.yml 파일을 리로드했습니다");
      }
      case "reload-config" ->
      {
        MessageUtil.broadcastDebug(ComponentUtil.translate("%s이(가) /cucumbery reload-config 명령어 사용", sender));
        Cucumbery.getPlugin().registerConfig();
        Cucumbery.getPlugin().reloadConfig();
        Cucumbery.config = (YamlConfiguration) Cucumbery.getPlugin().getConfig();
        try
        {
          Constant.WARNING_SOUND = Sound.valueOf(Cucumbery.config.getString("sound-const.warning-sound.sound"));
        }
        catch (Exception e)
        {
          Constant.WARNING_SOUND = Sound.ENTITY_ENDERMAN_TELEPORT;
        }
        try
        {
          Constant.ERROR_SOUND = Sound.valueOf(Cucumbery.config.getString("sound-const.error-sound.sound"));
        }
        catch (Exception e)
        {
          Constant.ERROR_SOUND = Sound.BLOCK_ANVIL_LAND;
        }
        Constant.WARNING_SOUND_PITCH = Cucumbery.config.getDouble("sound-const.warning-sound.pitch");
        Constant.WARNING_SOUND_VOLUME = Cucumbery.config.getDouble("sound-const.warning-sound.volume");
        Constant.ERROR_SOUND_PITCH = Cucumbery.config.getDouble("sound-const.error-sound.pitch");
        Constant.ERROR_SOUND_VOLUME = Cucumbery.config.getDouble("sound-const.error-sound.volume");
        for (Player player : Bukkit.getOnlinePlayers())
        {
          ItemStackUtil.updateInventory(player);
        }
        MessageUtil.info(sender, "config.yml 파일을 리로드했습니다");
      }
      case "reload-data" ->
      {
        MessageUtil.broadcastDebug(ComponentUtil.translate("%s이(가) /cucumbery reload-data 명령어 사용", sender));
        Bukkit.reloadData();
        MessageUtil.info(sender, "서버 데이터 파일을 리로드했습니다");
      }
      case "reload-custom-recipes" -> {
        MessageUtil.broadcastDebug(ComponentUtil.translate("%s이(가) /cucumbery reload-custom-recipes 명령어 사용", sender));
        Initializer.loadCustomRecipes();
        MessageUtil.info(sender, "특수 조합법 파일을 리로드했습니다");
      }
      case "reload-plugin" ->
      {
        MessageUtil.broadcastDebug(ComponentUtil.translate("%s이(가) /cucumbery reload-plugin 명령어 사용", sender));
        PluginLoader.unload();
        PluginLoader.load(Cucumbery.file);
        MessageUtil.info(sender, "플러그인을 리로드했습니다");
      }
      case "reload-custom-enchants" -> {
        MessageUtil.broadcastDebug(ComponentUtil.translate("%s이(가) /cucumbery reload-custom-enchants 명령어 사용", sender));
        CustomEnchant.onDisable();
        CustomEnchant.onEnable();
        MessageUtil.info(sender, "커스텀 인챈트를 리로드했습니다");
      }
//      case "reloadplugin2" ->
//      {
//        MessageUtil.broadcastDebug(ComponentUtil.translate("%s이(가) /cucumbery reloadplugin2 명령어 사용", sender));
//        Bukkit.getServer().getPluginManager().disablePlugin(Cucumbery.getPlugin());
//        Bukkit.getServer().getPluginManager().enablePlugin(Cucumbery.getPlugin());
//        MessageUtil.info(sender, "플러그인을 리로드했습니다. 2");
//      }
      case "version" ->
      {
        String version = Cucumbery.getPlugin().getDescription().getVersion();
        Component messasge = ComponentUtil.translate("플러그인 버전 : %s", Component.text(version, Constant.THE_COLOR)
                        .clickEvent(ClickEvent.copyToClipboard(version)))
                .hoverEvent(ComponentUtil.translate("chat.copy.click"));
        MessageUtil.info(sender, messasge);
      }
      case "update" ->
      {
        MessageUtil.broadcastDebug(ComponentUtil.translate("%s이(가) /cucumbery update 명령어 사용", sender));
        if (!isTryingUpdating.isEmpty())
        {
          MessageUtil.sendError(sender, "이미 업데이트를 진행하는 중입니다");
          return true;
        }
        isTryingUpdating.add("");
        MessageUtil.info(sender, "플러그인이 최신 버전인지 확인합니다...");
        ExecutorService executor = Executors.newFixedThreadPool(1);
        executor.submit(() ->
        {
          String version;
          try
          {
            version = Updater.getLatest();
          }
          catch (Exception e)
          {
            // 오류: 버전 확인 실패
            MessageUtil.sendError(sender, "버전 확인을 실패하였습니다 (%s)", e.getMessage());
            executor.shutdown();
            isTryingUpdating.clear();
            return true;
          }
          if (Updater.isLatest())
          {
            // 경고: 이미 최신 버전임
            MessageUtil.info(sender, "이미 플러그인이 최신 버전입니다");
            executor.shutdown();
            isTryingUpdating.clear();
            return true;
          }
          // 정보: 플러그인 버전
          MessageUtil.info(sender, "플러그인의 최신 버전을 발견했습니다");
          MessageUtil.info(sender, "  현재 버전 : " + Cucumbery.getPlugin().getDescription().getName() + " v" + Cucumbery.getPlugin().getDescription().getVersion());
          MessageUtil.info(sender, "  최신 버전 : " + Cucumbery.getPlugin().getDescription().getName() + " v" + version);
          // 정보: 파일 다운로드 시작
          MessageUtil.info(sender, "파일 다운로드 중...");
          File file;
          try
          {
            file = Updater.download(version);
          }
          catch (Exception e)
          {
            // 오류: 파일 다운로드 실패
            MessageUtil.sendError(sender, "파일 다운로드 실패 (%s)", e.getMessage());
            executor.shutdown();
            isTryingUpdating.clear();
            return true;
          }
          // 정보: 파일 다운로드 완료
          MessageUtil.info(sender, "파일 다운로드 완료");
          // 정보: 플러그인 업데이트 시작
          MessageUtil.info(sender, "플러그인 업데이트 중...");
          try
          {
            Updater.update(file, version);
          }
          catch (Exception e)
          {
            // 오류: 업데이트 실패
            MessageUtil.sendError(sender, "플러그인 업데이트 실패 (%s)", e.getMessage());
            executor.shutdown();
            isTryingUpdating.clear();
            return true;
          }
          // 정보: 업데이트 완료
          MessageUtil.info(sender, "업데이트 완료");
          executor.shutdown();
          return true;
        });
        executor.shutdown();
      }
      case "update-quickshop-item" ->
      {
        if (!Cucumbery.using_QuickShop)
        {
          MessageUtil.sendError(sender, "rg255,204;QuickShop&r 플러그인을 사용하고 있지 않습니다");
          return true;
        }
        int size = QuickShopSupport.updateQuickShopItems();
        MessageUtil.info(sender, "rg255,204;QuickShop&r의 모든 상점 아이템을 업데이트 했습니다 (총 " + size + "개)");
      }
      case "update-customrecipe-item" ->
      {
        int size = CustomRecipeSupport.updateCustomRecipe();
        MessageUtil.info(sender, "rg255,204;특수 조합법&r의 모든 결과물 아이템과 재료 아이템을 업데이트 했습니다.(총 " + size + "개)");
      }
      case "purge-user-data-files" ->
      {
        File userDataFolder = new File(Cucumbery.getPlugin().getDataFolder() + "/data/UserData");
        if (userDataFolder.exists())
        {
          File[] files = userDataFolder.listFiles();
          if (files == null)
          {
            MessageUtil.sendError(sender, "제거할 유저 데이터가 존재하지 않습니다");
            return true;
          }
          int removeSize = 0;
          for (File file : files)
          {
            String fileName = file.getName();

            if (fileName.endsWith(".yml"))
            {
              fileName = fileName.substring(0, fileName.length() - 4);
              if (Method.isUUID(fileName))
              {
                UUID uuid = UUID.fromString(fileName);
                if (UserData.ID.getString(uuid) == null)
                {
                  if (file.delete())
                  {
                    removeSize++;
                  }
                  else
                  {
                    System.err.println("[Cucumbery] could not delete " + file.getName() + " file!");
                  }
                }
              }
            }
          }
          if (removeSize > 0)
          {
            Initializer.saveUserData();
            Initializer.loadNicknamesConfig();
            Initializer.loadPlayersConfig();
            MessageUtil.info(sender, Constant.THE_COLOR_HEX + removeSize + "개&r의 유저 데이터를 제거하고 유저 데이터를 리로드 했습니다");
          }
          else
          {
            MessageUtil.info(sender, "제거할 유저 데이터가 존재하지 않습니다");
          }
        }
        else
        {
          MessageUtil.info(sender, "제거할 유저 데이터가 존재하지 않습니다");
        }
      }
      case "reset-custom-mining-cooldowns" ->
      {
        if (args.length == 1)
        {
          MessageUtil.shortArg(sender, 2, args);
          MessageUtil.commandInfo(sender, label, "<위치 1> <위치 2>");
          MessageUtil.commandInfo(sender, label, "<월드>");
          return true;
        }
        World world = CommandArgumentUtil.world(sender, args[1], false);
        Location location1 = CommandArgumentUtil.location(sender, args[1], true, false);
        if (world == null && location1 == null)
        {
          MessageUtil.wrongArg(sender, 2, args);
          return true;
        }
        if (location1 != null)
        {
          if (args.length < 3)
          {
            MessageUtil.shortArg(sender, 3, args);
            MessageUtil.commandInfo(sender, label, "<위치 1> <위치 2>");
            return true;
          }
          if (args.length > 3)
          {
            MessageUtil.longArg(sender, 3, args);
            MessageUtil.commandInfo(sender, label, "<위치 1> <위치 2>");
            return true;
          }
          Location location2 = CommandArgumentUtil.location(sender, args[2], true, true);
          if (location2 == null)
          {
            return true;
          }
          MiningScheduler.resetCooldowns(location1, location2);
          MessageUtil.info(sender, "%s 월드의 커스텀 채광 블록 쿨타임을 초기화했습니다 (%s ~ %s)", location1.getWorld(), location1, location2);
        }
        else
        {
          if (args.length > 2)
          {
            MessageUtil.longArg(sender, 2, args);
            MessageUtil.commandInfo(sender, label, "<월드>");
            return true;
          }
          MiningScheduler.resetCooldowns(world);
          MessageUtil.info(sender, "%s 월드의 커스텀 채광 블록 쿨타임을 초기화했습니다", world);
        }
      }
      default ->
      {
        MessageUtil.wrongArg(sender, 1, args);
        MessageUtil.commandInfo(sender, label, Method.getUsage(cmd));
        return true;
      }
    }
    return true;
  }

  @Override
  public @NotNull List<Completion> completion(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args, @NotNull Location location)
  {
    int length = args.length;
    if (length == 1)
    {
      return CommandTabUtil.tabCompleterList(args, "<인수>", false,
              "reload", "reload-data", "reload-plugin", "reload-custom-enchants", "version", "update", "update-quickshop-item", "update-customrecipe-item", "purge-user-data-files", "reset-custom-mining-cooldowns",
      "reload-configs", "reload-config", "reload-custom-recipes");
    }
    if (length == 2)
    {
      if (args[0].equals("reset-custom-mining-cooldowns"))
      {
        List<Completion> list = CommandTabUtil.worldArgument(sender, args, "<월드>"), list2 = CommandTabUtil.locationArgument(sender, args, "<위치 1>", null, true);
        return CommandTabUtil.sortError(list, list2);
      }
    }
    if (length == 3)
    {
      String[] args2 = new String[length - 1];
      System.arraycopy(args, 0, args2, 0, args2.length);
      List<Completion> beforeList = CommandTabUtil.locationArgument(sender, args2, "<위치>", null, false, null);
      List<String> beforeStringList = CucumberyCommandExecutor.legacy(beforeList);
      if (beforeStringList.size() == 1 && beforeStringList.get(0).equals("<위치>"))
      {
        return CommandTabUtil.locationArgument(sender, args, "<위치 2>", null, true);
      }
    }
    return CommandTabUtil.ARGS_LONG;
  }
}
