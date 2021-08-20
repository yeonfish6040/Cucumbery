package com.jho5245.cucumbery.commands.brigadier;

import com.jho5245.cucumbery.Cucumbery;
import com.jho5245.cucumbery.Initializer;
import com.jho5245.cucumbery.commands.brigadier.base.CommandBase;
import com.jho5245.cucumbery.util.MessageUtil;
import com.jho5245.cucumbery.util.Method;
import com.jho5245.cucumbery.util.plugin_support.CustomRecipeSupport;
import com.jho5245.cucumbery.util.plugin_support.QuickShopSupport;
import com.jho5245.cucumbery.util.storage.CustomConfig;
import com.jho5245.cucumbery.util.storage.PluginLoader;
import com.jho5245.cucumbery.util.storage.RecipeChecker;
import com.jho5245.cucumbery.util.storage.Updater;
import com.jho5245.cucumbery.util.storage.data.Constant;
import com.jho5245.cucumbery.util.storage.data.Variable;
import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.Argument;
import dev.jorel.commandapi.arguments.MultiLiteralArgument;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Recipe;
import org.maxgamer.quickshop.QuickShop;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

public class CucumberyMainCommand extends CommandBase
{
  private final List<Argument> arguments = new ArrayList<>();

  {
    arguments.add(new MultiLiteralArgument("reload", "reloaddata", "reloadplugin", "reloadplugin2", "version", "update", "update-quickshop-item", "update-customrecipe-item", "purge-user-data-files"));
  }

  public void registerCommand(String command, String permission, String... aliases)
  {
    CommandAPICommand commandAPICommand = getCommandBase(command, permission, aliases);
    commandAPICommand = commandAPICommand.withArguments(arguments);
    commandAPICommand = commandAPICommand.executesNative((sender, args) ->
    {
      CommandSender commandSender = sender.getCallee();
      try
      {
        commandSender.getName();
      }
      catch (Exception e)
      {
        commandSender = sender.getCaller();
      }
      String arg = (String) args[0];
      switch (arg)
      {
        case "reload":
          MessageUtil.broadcastDebug(commandSender, "이(가) Cucumbery 플러그인 리로드 명령어 사용");
          Cucumbery.getPlugin().registerConfig();
          Cucumbery.getPlugin().reloadConfig();
          Cucumbery.config = (YamlConfiguration) Cucumbery.getPlugin().getConfig();
          Initializer.saveUserData();
          Initializer.saveBlockPlaceData();
          Initializer.saveItemUsageData();
          Initializer.loadCustomConfigs();
          Initializer.loadPlayersConfig();

          if (Cucumbery.using_QuickShop)
          {
            Variable.shops.clear();
            Variable.shops = QuickShop.getInstance().getShopManager().getAllShops();
          }

          RecipeChecker.recipes.clear();
          Iterator<Recipe> recipeIterator = Bukkit.recipeIterator();
          while (recipeIterator.hasNext())
          {
            RecipeChecker.recipes.add(recipeIterator.next());
          }
          for (Player player : Bukkit.getOnlinePlayers())
          {
            Method.updateInventory(player);
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
          MessageUtil.info(commandSender, "모든 콘픽 파일을 리로드하였습니다.");
          break;
        case "reloaddata":
          MessageUtil.broadcastDebug(commandSender, "이(가) Cucumbery 플러그인 리로드데이터 명령어 사용");
          Bukkit.reloadData();
          MessageUtil.info(commandSender, "서버 데이터 파일을 리로드하였습니다.");
          break;
        case "reloadplugin":
          MessageUtil.broadcastDebug(commandSender, "이(가) Cucumbery 플러그인 리로드 플러그인 명령어 사용");
          PluginLoader.unload();
          PluginLoader.load(Cucumbery.file);
          MessageUtil.info(commandSender, "플러그인을 리로드하였습니다.");
          break;
        case "reloadplugin2":
          MessageUtil.broadcastDebug(commandSender, "이(가) Cucumbery 플러그인 리로드 플러그인2 명령어 사용");
          Bukkit.getServer().getPluginManager().disablePlugin(Cucumbery.getPlugin());
          Bukkit.getServer().getPluginManager().enablePlugin(Cucumbery.getPlugin());
          MessageUtil.info(commandSender, "플러그인을 리로드하였습니다. 2");
          break;
        case "version":
          MessageUtil.info(commandSender, "플러그인 버전 : &e" + Cucumbery.getPlugin().getDescription().getVersion());
          break;
        case "update":
          MessageUtil.broadcastDebug(commandSender, "이(가) Cucumbery 플러그인 업데이트 명령어 사용");
          if (Updater.defaultUpdater.updateLatest())
          {
            MessageUtil.info(commandSender, "플러구으누엊데이트 완료");
          }
          else
          {
            MessageUtil.info(commandSender, "플러구으누엊데이트 else싶애다파 이미 최신이겨나 오류터짐");
          }
          break;
        case "update-quickshop-item":
          if (!Cucumbery.using_QuickShop)
          {
            MessageUtil.sendError(commandSender, "&eQuickShop&r 플러그인을 사용하고 있지 않습니다.");
            return;
          }
          int size = QuickShopSupport.updateQuickShopItems();
          MessageUtil.info(sender, "&eQuickShop&r의 모든 상점 아이템을 업데이트 하였습니다. (총 " + size + "개)");
          break;
        case "update-customrecipe-item":
          size = CustomRecipeSupport.updateCustomRecipe();
          MessageUtil.info(sender, "&e특수 조합법&r의 모든 결과물 아이템과 재료 아이템을 업데이트 하였습니다.(총 " + size + "개)");
          break;
        case "purge-user-data-files":
          File userDataFolder = new File(Cucumbery.getPlugin().getDataFolder() + "/data/UserData");
          if (userDataFolder.exists())
          {
            File[] files = userDataFolder.listFiles();
            if (files == null)
            {
              MessageUtil.sendError(sender, "제거할 유저 데이터가 존재하지 않습니다.");
              return;
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
                  if (CustomConfig.UserData.ID.getString(uuid) == null)
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
              MessageUtil.info(sender, "&e" + removeSize + "개&r의 유저 데이터를 제거하고 유저 데이터를 리로드 하였습니다.");
            }
            else
            {
              MessageUtil.info(sender, "제거할 유저 데이터가 존재하지 않습니다.");
            }
          }
          else
          {
            MessageUtil.info(sender, "제거할 유저 데이터가 존재하지 않습니다.");
          }
          break;
      }
    });
    commandAPICommand.register();
  }
}
