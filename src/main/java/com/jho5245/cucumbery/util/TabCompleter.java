package com.jho5245.cucumbery.util;

import com.jho5245.cucumbery.Cucumbery;
import com.jho5245.cucumbery.util.addons.Songs;
import com.jho5245.cucumbery.util.nbt.CucumberyTag;
import com.jho5245.cucumbery.util.nbt.NBTAPI;
import com.jho5245.cucumbery.util.storage.BlockDataInfo;
import com.jho5245.cucumbery.util.storage.ComponentUtil;
import com.jho5245.cucumbery.util.storage.CustomConfig.UserData;
import com.jho5245.cucumbery.util.storage.ItemCategory;
import com.jho5245.cucumbery.util.storage.ItemStackUtil;
import com.jho5245.cucumbery.util.storage.data.Constant;
import com.jho5245.cucumbery.util.storage.data.Constant.ItemUsageType;
import com.jho5245.cucumbery.util.storage.data.Constant.RestrictionType;
import com.jho5245.cucumbery.util.storage.data.Permission;
import com.jho5245.cucumbery.util.storage.data.Prefix;
import com.jho5245.cucumbery.util.storage.data.Variable;
import de.tr7zw.changeme.nbtapi.*;
import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.block.Biome;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.*;

public class TabCompleter implements org.bukkit.command.TabCompleter
{
  public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args)
  {
    String name = cmd.getName();
    int length = args.length;
    String lastArg = length >= 1 ? args[length - 1] : "";
    if (name.equals("menu") && Method.hasPermission(sender, Permission.CMD_GUICOMMANDS, false))
    {
      if (length == 1)
      {
        return Method.tabCompleterPlayer(sender, args);
      }
      else if (length == 2)
      {
        return Method.tabCompleterBoolean(args, "[명령어 출력 숨김 여부]");
      }
    }
    else if (name.equals("itemdata") && Method.hasPermission(sender, Permission.CMD_ITEMDATA, false))
    {
      if (length == 1)
      {
        return Method.tabCompleterPlayer(sender, args);
      }
    }
    else if (name.equals("setdata") && Method.hasPermission(sender, Permission.CMD_ITEMDATA, false))
    {
      if (length == 1)
      {
        return Method.tabCompleterList(args, "<인수>", "amount", "durability", "material");
      }
      else if (length == 2)
      {
        switch (args[0])
        {
          case "material":
            List<String> list = new ArrayList<>();
            for (Material material : Material.values())
            {
              if (material.isItem() && material != Material.AIR)
              {
                list.add(material.toString().toLowerCase());
              }
            }
            return Method.tabCompleterList(args, list, "<아이템>");
          case "amount":
            return Method.tabCompleterIntegerRadius(args, 0, 127, "<개수>");
          case "durability":
            return Method.tabCompleterIntegerRadius(args, 0, 32767, "<내구도>");
        }
      }
      else if (length == 3)
      {
        return Method.tabCompleterPlayer(sender, args);
      }
      else if (length == 4)
      {
        return Method.tabCompleterBoolean(args, "[명령어 출력 숨김 여부]");
      }
    }
    else if (name.equals("setuserdata") && Method.hasPermission(sender, Permission.CMD_SETUSERDATA, false))
    {
      if (length == 1)
      {
        if (label.equals("sud"))
        {
          return Method.tabCompleterPlayer(sender, args);
        }
        return Method.tabCompleterOfflinePlayer(sender, args);
      }
      else if (length == 2)
      {
        if (Method.equals(args[1], "id", "uuid"))
        {
          return Collections.singletonList(args[1] + "("+UserData.valueOf(args[1].toUpperCase()).getKey().replace("-", " ")+")" + " 키의 값은 변경할 수 없습니다.");
        }
        List<String> list = Method.enumToList(UserData.values());
        list.remove("id");
        list.remove("uuid");
        return Method.tabCompleterList(args, list, "<데이터 키>");
      }
      else if (length == 3)
      {
        UserData key;
        try
        {
          key = UserData.valueOf(args[1].toUpperCase());
        }
        catch (Exception e)
        {
          return Collections.singletonList(args[1] + MessageUtil.getFinalConsonant(args[1], MessageUtil.ConsonantType.은는) + "잘못된 데이터 키입니다.");
        }
        switch (key)
        {
          case ITEM_DROP_MODE:
          case ITEM_PICKUP_MODE:
            return Method.tabCompleterList(args, "<인수>", "normal", "sneak", "disabled");
          case LISTEN_CHAT:
          case EVENT_EXCEPTION_ACCESS:
          case LISTEN_CHAT_FORCE:
          case LISTEN_COMMAND:
          case LISTEN_CONTAINER:
          case LISTEN_GLOBAL:
          case LISTEN_GLOBAL_FORCE:
          case LISTEN_HELDITEM:
          case LISTEN_ITEM_DROP:
          case LISTEN_JOIN:
          case LISTEN_JOIN_FORCE:
          case LISTEN_QUIT:
          case LISTEN_QUIT_FORCE:
          case OUTPUT_JOIN_MESSAGE_FORCE:
          case OUTPUT_JOIN_MESSAGE:
          case OUTPUT_QUIT_MESSAGE:
          case OUTPUT_QUIT_MESSAGE_FORCE:
          case PLAY_CHAT:
          case PLAY_CHAT_FORCE:
          case PLAY_JOIN:
          case PLAY_JOIN_FORCE:
          case PLAY_QUIT:
          case PLAY_QUIT_FORCE:
          case SHOW_ACTIONBAR_ON_ATTACK:
          case SHOW_ACTIONBAR_ON_ITEM_DROP:
          case SHOW_ACTIONBAR_ON_ITEM_PICKUP:
          case SHOW_JOIN_MESSAGE:
          case SHOW_JOIN_MESSAGE_FORCE:
          case SHOW_QUIT_MESSAGE:
          case SHOW_QUIT_MESSAGE_FORCE:
          case SERVER_RESOURCEPACK:
          case TRAMPLE_SOIL:
          case TRAMPLE_SOIL_ALERT:
          case SHOW_ITEM_BREAK_TITLE:
          case SHOW_JOIN_TITLE:
          case SHOW_ACTIONBAR_ON_ATTACK_PVP:
          case HIDE_ACTIONBAR_ON_ATTACK_PVP_TO_OTHERS:
          case SHOW_ACTIONBAR_ON_ATTACK_FORCE:
          case SHOW_ACTIONBAR_ON_ATTACK_PVP_FORCE:
          case SHOW_ACTIONBAR_ON_ITEM_DROP_FORCE:
          case SHOW_ACTIONBAR_ON_ITEM_PICKUP_FORCE:
          case TRAMPLE_SOIL_FORCE:
          case TRAMPLE_SOIL_NO_ALERT_FORCE:
          case ENTITY_AGGRO:
          case FIREWORK_LAUNCH_ON_AIR:
          case USE_QUICK_COMMAND_BLOCK:
          case ITEM_DROP_DELAY_ALERT:
          case COPY_NOTE_BLOCK_INSTRUMENT:
          case COPY_NOTE_BLOCK_PITCH:
          case COPY_NOTE_BLOCK_VALUE_WHEN_SNEAKING:
          case INVERT_NOTE_BLOCK_PITCH_WHEN_SNEAKING_IN_CREATIVE_MODE:
          case PLAY_NOTE_BLOCK_WHEN_SNEAKING_IN_CREATIVE_MODE:
          case SHORTEND_DEBUG_MESSAGE:
          case USE_HELPFUL_LORE_FEATURE:
          case SAVE_EXPERIENCE_UPON_DEATH:
          case SAVE_INVENTORY_UPON_DEATH:
          case SHOW_PLUGIN_DEV_DEBUG_MESSAGE:
          case DISABLE_HELPFUL_FEATURE_WHEN_CREATIVE:
          case SHOW_PREVIEW_COMMAND_BLOCK_COMMAND:
          case NOTIFY_IF_INVENTORY_IS_FULL:
          case NOTIFY_IF_INVENTORY_IS_FULL_FORCE_DISABLE:
          case SHOW_ACTIONBAR_WHEN_ITEM_IS_COOLDOWN:
          case FORCE_HIDE_ACTIONBAR_WHEN_ITEM_IS_COOLDOWN:
          case SHOW_COMMAND_BLOCK_EXECUTION_LOCATION:
          case HEALTH_SCALED:
          case COPY_BLOCK_DATA:
          case COPY_BLOCK_DATA_WHEN_SNEAKING:
          case COPY_BLOCK_DATA_FACING:
          case COPY_BLOCK_DATA_WATERLOGGED:
          case DISABLE_COMMAND_BLOCK_BREAK_WHEN_SNEAKING:
          case SHOW_SPECTATOR_TARGET_INFO_IN_ACTIONBAR:
          case SHOW_SPECTATOR_TARGET_INFO_IN_ACTIONBAR_TMI_MODE:
          case GOD_MODE:
            return Method.tabCompleterBoolean(args, "<값>");
          case DISPLAY_NAME:
          case PLAYER_LIST_NAME:
            return Collections.singletonList("닉네임은 닉네임 명령어(/nick, /nickothers)를 사용하여 변경해주세요.");
          case HEALTH_BAR:
            return Collections.singletonList("HP바는 hp바 명령어(/shp)를 사용하여 변겨해주세요.");
          case ID:
          case UUID:
            return Collections.singletonList(args[1] + "("+key.getKey().replace("-", " ")+")" + " 키의 값은 변경할 수 없습니다.");
          case ITEM_USE_DELAY:
          case ITEM_DROP_DELAY:
            return Method.tabCompleterIntegerRadius(args, 0, 200, "<틱>");
        }
      }
      else if (length == 4)
      {
        return Method.tabCompleterBoolean(args, "[명령어 출력 숨김 여부]");
      }
    }
    else if (name.equals("nickname") && Method.hasPermission(sender, Permission.CMD_NICK, false))
    {
      if (!Cucumbery.config.getBoolean("use-nickname-feature"))
      {
        return Collections.singletonList("닉네임 기능이 비활성화 되어 있습니다.");
      }
      if (!(sender instanceof Player))
      {
        return Collections.singletonList(Prefix.ONLY_PLAYER.toString());
      }
      if (length == 1)
      {
        return Method.tabCompleterList(args, "<닉네임 유형>", "all", "display", "list");
      }
      else
      {
        if (length == 2)
        {
          return Method.tabCompleterList(args, "<닉네임>", true, "<닉네임>", "--off");
        }
        return Method.tabCompleterList(args, "[닉네임]", true);
      }
    }
    else if (name.equals("nicknameothers") && Method.hasPermission(sender, Permission.CMD_NICK, false))
    {
      if (!Cucumbery.config.getBoolean("use-nickname-feature"))
      {
        return Collections.singletonList("닉네임 기능이 비활성화 되어 있습니다.");
      }
      if (length == 1)
      {
        if (label.equals("cnicks"))
        {
          return Method.tabCompleterPlayer(sender, args);
        }
        return Method.tabCompleterOfflinePlayer(sender, args);
      }
      else if (length == 2)
      {
        return Method.tabCompleterList(args, "<닉네임 유형>", "all", "display", "list");
      }
      else if (length == 3)
      {
        return Method.tabCompleterBoolean(args, "<명령어 출력 숨김 여부>");
      }
      else
      {
        if (length == 4)
        {
          return Method.tabCompleterList(args, "<닉네임>", true, "<닉네임>", "--off");
        }
        return Method.tabCompleterList(args, "[닉네임]", true);
      }
    }
    else if (name.equals("healthbar") && Method.hasPermission(sender, Permission.CMD_SHP, false))
    {
      if (length == 1)
      {
        return Method.tabCompleterList(args, "<인수>", "set", "give", "take");
      }
      else if (length == 2 && (Method.equals(args[0], "<인수>", "set", "give", "take")))
      {
        if (label.equals("shp"))
        {
          return Method.tabCompleterPlayer(sender, args);
        }
        return Method.tabCompleterOfflinePlayer(sender, args);
      }
      else if (length == 3)
      {
        return Method.tabCompleterDoubleRadius(args, 1, Double.MAX_VALUE, "<수치>");
      }
      else if (length == 4 && (Method.equals(args[0], "<인수>", "set", "give", "take")))
      {
        return Method.tabCompleterBoolean(args, "[명령어 출력 숨김 여부]");
      }
    }
    else if (name.equals("maxhealthpoint") && Method.hasPermission(sender, Permission.CMD_MHP, false))
    {
      if (length == 1)
      {
        return Method.tabCompleterList(args, "<인수>", "set", "give", "take");
      }
      else if (length == 2 && (Method.equals(args[0], "<인수>", "set", "give", "take")))
      {
        return Method.tabCompleterEntity(sender, args);
      }
      else if (length == 3)
      {
        return Method.tabCompleterDoubleRadius(args, 1, Double.MAX_VALUE, "<수치>");
      }
      else if (length == 4 && (Method.equals(args[0], "<인수>", "set", "give", "take")))
      {
        return Method.tabCompleterBoolean(args, "[HP를 최대 HP로 설정]");
      }
      else if (length == 5 && (Method.equals(args[0], "<인수>", "set", "give", "take")))
      {
        return Method.tabCompleterBoolean(args, "[명령어 출력 숨김 여부]");
      }
    }
    else if (name.equals("whois") && Method.hasPermission(sender, Permission.CMD_WHOIS, false))
    {
      if (length == 1)
      {
        if (label.equals("whois"))
        {
          return Method.tabCompleterPlayer(sender, args);
        }
        return Method.tabCompleterOfflinePlayer(sender, args);
      }
      else if (length == 2)
      {
        Player player = SelectorUtil.getPlayer(sender, args[0], false);
        OfflinePlayer offlinePlayer = null;
        if (Method.isUUID(args[0]))
        {
          offlinePlayer = Bukkit.getOfflinePlayer(UUID.fromString(args[0]));
        }
        else if (!args[0].equals(""))
        {
          offlinePlayer = Bukkit.getOfflinePlayerIfCached(args[0]);
        }
        if (args[0].equals("") || (player == null && (offlinePlayer == null || !offlinePlayer.isOnline())))
        {
          if (Method.equals(args[1], "pos", "state", "effect"))
          {
            return Collections.singletonList("해당 정보 유형은 온라인 상태의 플레이어에게만 사용할 수 있습니다.");
          }
          return Method.tabCompleterList(args, "[정보 유형]", "name", "stats", "stats_general", "stats_entity", "stats_material", "offline");
        }
        boolean hasPotionEffects = player.getActivePotionEffects().size() > 0;
        return Method.tabCompleterList(args, "[정보 유형]", "pos", "name", "state", "stats",
                "effect" + (!hasPotionEffects ? "(적용 중인 효과 없음)" : ""), "stats_general", "stats_entity", "stats_material", "offline");
      }
      else if (length == 3)
      {
        if (args[1].equals("stats"))
        {
          return Method.tabCompleterIntegerRadius(args, 1, 4, "[페이지]");
        }
        if (Method.equals(args[1], "stats_general", "stats_entity", "stats_material"))
        {
          return Method.tabCompleterStatistics(args, args[1].replace("stats_", ""), "<통계>");
        }
      }
      else if (length == 4)
      {
        if (args[1].equals("stats_entity"))
        {
          List<String> list = new ArrayList<>();
          for (EntityType entityType : EntityType.values())
          {
            if (entityType.isAlive())
            {
              list.add(entityType.toString().toLowerCase());
            }
          }
          return Method.tabCompleterList(args, list, "<개체>");
        }
        else if (args[1].equals("stats_material"))
        {
          if (args[2].equals("mine_block"))
          {
            List<String> list = new ArrayList<>();
            for (Material material : Material.values())
            {
              if (material.isBlock())
              {
                list.add(material.toString().toLowerCase());
              }
            }
            return Method.tabCompleterList(args, list, "<블록>");
          }
          else if (args[2].equals("break_item"))
          {
            List<String> list = new ArrayList<>();
            for (Material material : Constant.DURABLE_ITEMS)
            {
              list.add(material.toString().toLowerCase());
            }
            return Method.tabCompleterList(args, list, "<내구도가 있는 아이템>");
          }
          else if (Method.equals(args[2], "use_item", "drop", "pickup", "craft_item"))
          {
            List<String> list = new ArrayList<>();
            for (Material material : Material.values())
            {
              if (material.isItem() && material != Material.AIR)
              {
                list.add(material.toString().toLowerCase());
              }
            }
            return Method.tabCompleterList(args, list, "<아이템>");
          }
        }
      }
    }
    else if (name.equals("whatis") && Method.hasPermission(sender, Permission.CMD_WHATIS, false))
    {
      if (length == 1)
      {
        return Method.tabCompleterList(args, Method.listWorlds(), "[월드]");
      }
      else if (length == 2)
      {
        return Method.tabCompleterList(args, "[인수]", "forecast");
      }
    }
    else if (name.equals("sudo") && Method.hasPermission(sender, Permission.CMD_SUDO, false))
    {
      if (length == 1)
      {
        return Method.tabCompleterPlayer(sender, args);
      }
      else if (length == 2)
      {
        return Method.tabCompleterBoolean(args, "<명령어 출력 숨김 여부>");
      }
      else
      {
        return this.getCommandsTabCompleter(sender, args, 3, true);
      }
    }
    else if (name.equals("handgive") && Method.hasPermission(sender, Permission.CMD_HANDGIVE, false))
    {
      Player player = (Player) sender;
      ItemStack item = player.getInventory().getItemInMainHand();
      if (!ItemStackUtil.itemExists(item))
      {
        return Collections.singletonList(Prefix.NO_HOLDING_ITEM.toString());
      }
      if (length == 1)
      {
        return Method.tabCompleterPlayer(sender, args);
      }
      else if (length == 2)
      {
        return Method.tabCompleterIntegerRadius(args, 1, 2304, "[개수]");
      }
      else if (length == 3)
      {
        return Method.tabCompleterBoolean(args, "[명령어 출력 숨김 여부]");
      }
    }
    else if (name.equals("handgiveall") && Method.hasPermission(sender, Permission.CMD_HANDGIVEALL, false))
    {
      Player player = (Player) sender;
      ItemStack item = player.getInventory().getItemInMainHand();
      if (!ItemStackUtil.itemExists(item))
      {
        return Collections.singletonList(Prefix.NO_HOLDING_ITEM.toString());
      }
      if (length == 1)
      {
        return Method.tabCompleterIntegerRadius(args, 1, 2304, "[개수]", "R#~#");
      }
      else if (length == 2)
      {
        return Method.tabCompleterList(args, "[추가 조건]", "world", "distance", "permission", "level", "hp", "maxhp", "healthbar", "gamemode");
      }
      else if (length == 3)
      {
        switch (args[1])
        {
          case "world":
            return Method.tabCompleterList(args, Method.listWorlds(), "<월드>");
          case "gamemode":
            return Method.tabCompleterList(args, GameMode.values(), "<게임 모드>");
          case "distance":
            return Method.tabCompleterDoubleRadius(args, 0, Double.MAX_VALUE, "<거리>");
          case "permission":
            return Method.tabCompleterList(args, "<퍼미션 노드>", true);
          case "level":
            return Method.tabCompleterIntegerRadius(args, 0, Integer.MAX_VALUE, "<레벨>");
        }
      }
      else if (length == 4)
      {
        return Method.tabCompleterBoolean(args, "[명령어 출력 숨김 여부]");
      }
    }
    else if (name.equals("messageitem") && sender instanceof Player player)
    {
      ItemStack item = player.getInventory().getItemInMainHand();
      if (!ItemStackUtil.itemExists(item))
      {
        return Collections.singletonList(Prefix.NO_HOLDING_ITEM.toString());
      }
      if (length == 1)
      {
        return Method.tabCompleterPlayer(sender, args);
      }
    }
    else if (name.equals("call") && Method.hasPermission(sender, Permission.CMD_CALL, false))
    {
      if (length == 1)
      {
        return Method.tabCompleterPlayer(sender, args);
      }
    }
    else if (name.equals("enderchest") && Method.hasPermission(sender, Permission.CMD_ENDERCHEST, false))
    {
      if (length == 1)
      {
        return Method.tabCompleterPlayer(sender, args);
      }
      else if (length == 2)
      {
        return Method.tabCompleterPlayer(sender, args);
      }
      else if (length == 3)
      {
        return Method.tabCompleterBoolean(args, "[명령어 출력 숨김 여부]");
      }
    }
    else if (name.equals("workbench") && Method.hasPermission(sender, Permission.CMD_WORKBENCH, false))
    {
      if (length == 1)
      {
        return Method.tabCompleterPlayer(sender, args);
      }
      else if (length == 2)
      {
        return Method.tabCompleterBoolean(args, "[명령어 출력 숨김 여부]");
      }
    }
    else if (name.equals("trashcan") && Method.hasPermission(sender, Permission.CMD_TRASHCAN, false))
    {
      if (length == 1)
      {
        return Method.tabCompleterPlayer(sender, args);
      }
      else if (length == 2)
      {
        return Method.tabCompleterBoolean(args, "[명령어 출력 숨김 여부]");
      }
      else
      {
        if (length == 3)
        {
          return Method.tabCompleterList(args, "<쓰레기통 이름>", true);
        }
        return Method.tabCompleterList(args, "[쓰레기통 이름]", true);
      }
    }
    else if (name.equals("cspectate") && Method.hasPermission(sender, Permission.CMD_SPECTATE, false))
    {
      if (length == 1)
      {
        return Method.tabCompleterPlayer(sender, args);
      }
      else if (length == 2)
      {
        return Method.tabCompleterPlayer(sender, args);
      }
      else if (length == 3)
      {
        return Method.tabCompleterBoolean(args, "[게임모드 자동 변경]");
      }
      else if (length == 4)
      {
        return Method.tabCompleterBoolean(args, "[권한 부족 우회]");
      }
      else if (length == 5)
      {
        return Method.tabCompleterBoolean(args, "[명령어 출력 숨김 여부]");
      }
    }
    else if (name.equals("citemstorage") && Method.hasPermission(sender, Permission.CMD_ITEMSTORAGE, false))
    {
      if (length == 1)
      {
        return Method.tabCompleterList(args, "<인수>", "list", "store", "remove", "get", "info", "give", "setitem");
      }
      else if (length == 2)
      {
        if (Method.equals(args[0], "store") && sender instanceof Player player)
        {
          ItemStack item = player.getInventory().getItemInMainHand();
          if (!ItemStackUtil.itemExists(item))
          {
            return Collections.singletonList(Prefix.NO_HOLDING_ITEM.toString());
          }
          if (Variable.itemStorage.size() == 0)
          {
            return Method.tabCompleterList(args, "<새로운 아이템 목록>", true);
          }
          return Method.tabCompleterList(args, Variable.itemStorage.keySet(), (Variable.itemStorage.containsKey(lastArg) ? "<아이템 목록>" : "<새로운 아이템 목록>"), true);
        }
        else if (args[0].equals("list"))
        {
          if (Variable.itemStorage.size() == 0)
          {
            return Collections.singletonList("아이템 목록이 하나도 존재하지 않습니다.");
          }
          return Method.tabCompleterList(args, Variable.itemStorage.keySet(), "[아이템 목록]");
        }
        else if (Method.equals(args[0], "remove", "get", "info"))
        {
          if (Variable.itemStorage.size() == 0)
          {
            return Collections.singletonList("아이템 목록이 하나도 존재하지 않습니다.");
          }
          return Method.tabCompleterList(args, Variable.itemStorage.keySet(), "<아이템 목록>");
        }
        else if (Method.equals(args[0], "give", "checkamount", "setitem"))
        {
          return Method.tabCompleterPlayer(sender, args);
        }
      }
      else if (length == 3)
      {
        switch (args[0])
        {
          case "remove":
          case "info":
          {
            FileConfiguration config = Variable.itemStorage.get(args[1]);
            if (config == null)
            {
              return Collections.singletonList(args[1] + MessageUtil.getFinalConsonant(args[1], MessageUtil.ConsonantType.은는) + " 잘못되거나 알 수 없는 아이템 목록입니다.");
            }
            ConfigurationSection itemList = config.getConfigurationSection("items");
            if (itemList == null)
            {
              return Collections.singletonList(args[1] + " 아이템 목록에는 유효한 아이템이 없습니다.");
            }
            return Method.tabCompleterList(args, itemList.getKeys(false), "<아이템>");

          }
          case "get":
          {
            FileConfiguration config = Variable.itemStorage.get(args[1]);
            if (config == null)
            {
              return Collections.singletonList(args[1] + MessageUtil.getFinalConsonant(args[1], MessageUtil.ConsonantType.은는) + " 잘못되거나 알 수 없는 아이템 목록입니다.");
            }
            ConfigurationSection itemList = config.getConfigurationSection("items");
            if (itemList == null)
            {
              return Collections.singletonList(args[1] + " 아이템 목록에는 유효한 아이템이 없습니다.");
            }
            return Method.tabCompleterList(args, Method.addAll(itemList.getKeys(false), "--all"), "<아이템>");
          }
          case "store":
            Player player = (Player) sender;
            ItemStack item = player.getInventory().getItemInMainHand();
            if (!ItemStackUtil.itemExists(item))
            {
              return Collections.singletonList(Prefix.NO_HOLDING_ITEM.toString());
            }
            FileConfiguration config = Variable.itemStorage.get(args[1]);
            if (config == null)
            {
              return Collections.singletonList("<새로운 아이템>");
            }
            ConfigurationSection itemList = config.getConfigurationSection("items");
            if (itemList == null)
            {
              return Collections.singletonList("<새로운 아이템>");
            }
            return Method.tabCompleterList(args, itemList.getKeys(false), itemList.getKeys(false).contains(args[2]) ? "<덮어씌울 아이템>" : "<새로운 아이템>", true);
          case "give":
          case "checkamount":
            if (Variable.itemStorage.size() == 0)
            {
              return Collections.singletonList("아이템 목록이 하나도 존재하지 않습니다.");
            }
            return Method.tabCompleterList(args, Variable.itemStorage.keySet(), "<아이템 목록>");
          case "setitem":
            List<String> list = new ArrayList<>();
            for (int i = 1; i <= 27; i++)
            {
              list.add("inventory." + i);
            }
            for (int i = 1; i <= 9; i++)
            {
              list.add("hotbar." + i);
            }
            list.addAll(Arrays.asList("weapon", "weapon.mainhand", "weapon.offhand", "armor.head", "armor.chest", "armor.legs", "armor.feet"));
            return Method.tabCompleterList(args, list, "<슬롯>");
          default:
            if (!args[length - 1].equals(""))
            {
              return Collections.singletonList(Prefix.ARGS_LONG.toString());
            }
        }
      }
      else if (length == 4)
      {
        switch (args[0])
        {
          case "get":
            return Method.tabCompleterIntegerRadius(args, 1, 2304, "[개수]");
          case "checkamount":
          {
            FileConfiguration config = Variable.itemStorage.get(args[2]);
            if (config == null)
            {
              return Collections.singletonList(args[2] + MessageUtil.getFinalConsonant(args[2], MessageUtil.ConsonantType.은는) + " 잘못되거나 알 수 없는 아이템 목록입니다.");
            }
            ConfigurationSection itemList = config.getConfigurationSection("items");
            if (itemList == null)
            {
              return Collections.singletonList(args[2] + " 아이템 목록에는 유효한 아이템이 없습니다.");
            }
            return Method.tabCompleterList(args, itemList.getKeys(false), "<아이템>");
          }
          case "give":
          {
            FileConfiguration config = Variable.itemStorage.get(args[2]);
            if (config == null)
            {
              return Collections.singletonList(args[2] + MessageUtil.getFinalConsonant(args[2], MessageUtil.ConsonantType.은는) + " 잘못되거나 알 수 없는 아이템 목록입니다.");
            }
            ConfigurationSection itemList = config.getConfigurationSection("items");
            if (itemList == null)
            {
              return Collections.singletonList(args[2] + " 아이템 목록에는 유효한 아이템이 없습니다.");
            }
            return Method.tabCompleterList(args, Method.addAll(itemList.getKeys(false), "--all"), "<아이템>");
          }
          case "setitem":
            if (Variable.itemStorage.size() == 0)
            {
              return Collections.singletonList("아이템 목록이 하나도 존재하지 않습니다.");
            }
            return Method.tabCompleterList(args, Variable.itemStorage.keySet(), "<아이템 목록>");
          case "store":
            return Method.tabCompleterBoolean(args, "[기존 아이템 덮어씌움]");
          case "remove":
            return Method.tabCompleterBoolean(args, "[명령어 출력 숨김 여부]");
          default:
            if (!args[length - 1].equals(""))
            {
              return Collections.singletonList(Prefix.ARGS_LONG.toString());
            }
        }
      }
      else if (length == 5)
      {
        switch (args[0])
        {
          case "give":
            return Method.tabCompleterIntegerRadius(args, 1, 2304, "[개수]", "-1");
          case "store":
            return Method.tabCompleterBoolean(args, "[명령어 출력 숨김 여부]");
          case "setitem":
            FileConfiguration config = Variable.itemStorage.get(args[3]);
            if (config == null)
            {
              return Collections.singletonList(args[3] + MessageUtil.getFinalConsonant(args[3], MessageUtil.ConsonantType.은는) + " 잘못되거나 알 수 없는 아이템 목록입니다.");
            }
            ConfigurationSection itemList = config.getConfigurationSection("items");
            if (itemList == null)
            {
              return Collections.singletonList(args[3] + " 아이템 목록에는 유효한 아이템이 없습니다.");
            }
            return Method.tabCompleterList(args, itemList.getKeys(false), "<아이템>");
          case "checkamount":
            return Method.tabCompleterIntegerRadius(args, 0, 32767, "[내구도 조건]", "-1");
          default:
            if (!args[length - 1].equals(""))
            {
              return Collections.singletonList(Prefix.ARGS_LONG.toString());
            }
        }
      }
      else if (length == 6)
      {
        if (args[0].equals("setitem"))
        {
          return Method.tabCompleterIntegerRadius(args, 1, 127, "[개수]");
        }
        else if (args[0].equals("give"))
        {
          return Method.tabCompleterBoolean(args, "[명령어 출력 숨김 여부]");
        }
      }
      else if (length == 7)
      {
        if (args[0].equals("setitem"))
        {
          return Method.tabCompleterBoolean(args, "[기존 아이템 덮어씌움]");
        }
      }
      else if (length == 8)
      {
        if (args[0].equals("setitem"))
        {
          return Method.tabCompleterBoolean(args, "[명령어 출력 숨김 여부]");
        }
      }

    }
    else if (name.equals("itemflag") && sender instanceof Player player)
    {
      ItemStack item = player.getInventory().getItemInMainHand();
      if (!ItemStackUtil.itemExists(item))
      {
        return Collections.singletonList(Prefix.NO_HOLDING_ITEM.toString());
      }
      if (length == 1)
      {
        return Method.tabCompleterList(args, "<인수>", "add", "remove");
      }
      else if (length == 2)
      {
        return Method.tabCompleterList(args, Method.addAll(ItemFlag.values(), "all", "내구성"), "<아이템 플래그>");
      }
    }
    else if (name.equals("heal") && Method.hasPermission(sender, Permission.CMD_HEAL, false))
    {
      if (length == 1)
      {
        return Method.tabCompleterPlayer(sender, args);
      }
      else if (length == 2)
      {
        return Method.tabCompleterBoolean(args, "[명령어 출력 숨김 여부]");
      }
    }
    else if (name.equals("feed") && Method.hasPermission(sender, Permission.CMD_FEED, false))
    {
      if (length == 1)
      {
        return Method.tabCompleterPlayer(sender, args);
      }
      else if (length == 2)
      {
        return Method.tabCompleterBoolean(args, "[명령어 출력 숨김 여부]");
      }
    }
    else if (name.equals("advancedfeed") && Method.hasPermission(sender, Permission.CMD_AFEED, false))
    {
      if (length == 1)
      {
        return Method.tabCompleterList(args, "<인수>", "set", "give", "take");
      }
      else if (length == 2)
      {
        return Method.tabCompleterPlayer(sender, args);
      }
      else if (length == 3)
      {
        return Method.tabCompleterList(args, "<인수>", "all", "foodlevel", "saturation");
      }
      else if (length == 4)
      {
        if (args[0].equals("set"))
        {
          return Method.tabCompleterDoubleRadius(args, 0, 20, "<수치>");
        }
        return Method.tabCompleterDoubleRadius(args, 0, true, 20, false, "<수치>");
      }
      else if (length == 5)
      {
        return Method.tabCompleterBoolean(args, "[명령어 출력 숨김 여부]");
      }
    }
    else if (name.equals("copystring") && sender instanceof Player)
    {
      Player player = (Player) sender;
      ItemStack item = player.getInventory().getItemInMainHand();
      if (!ItemStackUtil.itemExists(item))
      {
        return Collections.singletonList(Prefix.NO_HOLDING_ITEM.toString());
      }
      if (length == 1)
      {
        return Method.tabCompleterList(args, "<복사할 값>", "name", "lore");
      }
      else if (length == 2)
      {
        if (args[0].equals("name"))
        {
          return Method.tabCompleterBoolean(args, "[색깔 없앰 해체 여부]");
        }
        else if (args[0].equals("lore"))
        {
          if (!ItemStackUtil.hasLore(item))
          {
            return Collections.singletonList("복사할 수 있는 설명이 없습니다.");
          }
          return Method.tabCompleterIntegerRadius(args, 1, Objects.requireNonNull(item.getItemMeta().getLore()).size(), "<줄>");
        }
      }
      else if (length == 3)
      {
        if (args[0].equals("lore"))
        {
          return Method.tabCompleterBoolean(args, "[색깔 없앰 해제 여부]");
        }
      }
    }
    else if (name.equals("viewdamage") && Method.hasPermission(sender, Permission.CMD_VIEWDAMAGE, false))
    {
      if (length == 1)
      {
        return Method.tabCompleterOnOff(args, "[토글]");
      }
    }
    else if (name.equals("uuid") && Method.hasPermission(sender, Permission.CMD_UUID, false))
    {
      if (length == 1)
      {
        List<String> list = new ArrayList<>(label.equals("uuid") ? Method.tabCompleterPlayer(sender, args) : Method.tabCompleterOfflinePlayer(sender, args));
        list.removeIf(Method::isUUID);
        return list;
      }
    }
    else if (name.equals("sendmessage") && Method.hasPermission(sender, Permission.CMD_SENDMESSAGE, false))
    {
      if (length == 1)
      {
        return Method.tabCompleterPlayer(sender, args);
      }
      else
      {
        return Method.tabCompleterList(args, length == 2 ? "<메시지>" : "[메시지]", true);
      }
    }
    else if (name.equals("clearchat") && Method.hasPermission(sender, Permission.CMD_CLEARCHAT, false))
    {
      if (length == 1)
      {
        return Method.tabCompleterPlayer(sender, args);
      }
      else if (length == 2)
      {
        return Method.tabCompleterBoolean(args, "[명령어 출력 숨김 여부]");
      }
    }
    else if (name.equals("advancedteleport") && Method.hasPermission(sender, Permission.CMD_ADVANCED_TELEPORT, false))
    {
      if (length == 1)
      {
        return Method.tabCompleterEntity(sender, args);
      }
      else if (length == 2)
      {
        return Method.tabCompleterList(args, Method.addAll(Method.listWorlds(), "~"), "<월드>");
      }
      else if (length >= 3 && length <= 7)
      {
        switch (length)
        {
          case 3:
            return Method.tabCompleterDoubleRadius(args, -Double.MAX_VALUE, Double.MAX_VALUE, "<x좌표> <y좌표> <z좌표> [x축 회전] [y축 회전]", "~ ~ ~", "~ ~", "~");
          case 4:
            return Method.tabCompleterDoubleRadius(args, -Double.MAX_VALUE, Double.MAX_VALUE, "<y좌표> <z좌표> [x축 회전] [y축 회전]", "~ ~", "~");
          case 5:
            return Method.tabCompleterDoubleRadius(args, -Double.MAX_VALUE, Double.MAX_VALUE, "<z좌표> [x축 회전] [y축 회전]", "~");
          case 6:
            return Method.tabCompleterDoubleRadius(args, -Double.MAX_VALUE, Double.MAX_VALUE, "[x축 회전] [y축 회전]", "~ ~", "~");
          case 7:
            return Method.tabCompleterDoubleRadius(args, -Double.MAX_VALUE, Double.MAX_VALUE, "[y축 회전]", "~");
        }
      }
      else if (length == 8)
      {
        return Method.tabCompleterBoolean(args, "[운동 에너지 보존]");
      }
      else if (length == 9)
      {
        return Method.tabCompleterBoolean(args, "[위치 에너지 보존]");
      }
      else if (length == 10)
      {
        return Method.tabCompleterBoolean(args, "[명령어 출력 숨김 여부]");
      }
    }
    else if (name.equals("cwarp") && Method.hasPermission(sender, Permission.CMD_WARP, false))
    {
      if (length == 1)
      {
        if (Variable.warps.size() == 0)
        {
          return Collections.singletonList("유효한 워프가 존재하지 않습니다.");
        }
        return Method.tabCompleterList(args, Variable.warps.keySet(), "<워프>");
      }
      else if (length == 2)
      {
        return Method.tabCompleterPlayer(sender, args);
      }
      else if (length == 3)
      {
        return Method.tabCompleterBoolean(args, "[운동 에너지 보존]");
      }
      else if (length == 4)
      {
        return Method.tabCompleterBoolean(args, "[위치 에너지 보존]");
      }
      else if (length == 5)
      {
        return Method.tabCompleterBoolean(args, "[명령어 출력 숨김 여부]");
      }
    }
    else if (name.equals("cdelwarp") && Method.hasPermission(sender, Permission.CMD_WARP, false))
    {
      if (length == 1)
      {
        if (Variable.warps.size() == 0)
        {
          return Collections.singletonList("유효한 워프가 존재하지 않습니다.");
        }
        return Method.tabCompleterList(args, Variable.warps.keySet(), "<워프>");
      }
      else if (length == 2)
      {
        return Method.tabCompleterBoolean(args, "[명령어 출력 숨김 여부]");
      }
    }
    else if (name.equals("csetwarp") && Method.hasPermission(sender, Permission.CMD_WARP, false))
    {
      if (length == 1)
      {
        return Method.tabCompleterList(args, Variable.warps.containsKey(lastArg) ? "<덮어씌울 워프 이름>" : "<새로운 워프 이름>", true);
      }
      if (length == 2)
      {
        return Method.tabCompleterList(args, "[워프 표시 이름]", true, args[0]);
      }
      if (length == 3)
      {
        return Method.tabCompleterBoolean(args, "[기존 워프 덮어 씌움]");
      }
      else if (length == 4)
      {
        return Method.tabCompleterBoolean(args, "[명령어 출력 숨김 여부]");
      }
    }
    else if (name.equals("playsoundall") || name.equals("playsoundall2"))
    {
      if (length == 1)
      {
        return Method.tabCompleterList(args, Sound.values(), "<소리>", true);
      }
      else if (length == 2)
      {
        return Method.tabCompleterList(args, SoundCategory.values(), "[소리 유형]");
      }
      else if (length == 3)
      {
        return Method.tabCompleterDoubleRadius(args, 0, Float.MAX_VALUE, "[음량]");
      }
      else if (length == 4)
      {
        return Method.tabCompleterDoubleRadius(args, 0.5, 2, "[음색]");
      }
      else if (length == 5)
      {
        return Method.tabCompleterBoolean(args, "[명령어 출력 숨김 여부]");
      }
    }
    else if (name.equals("cplaysound") && Method.hasPermission(sender, Permission.CMD_PLAYSOUNDALL, false))
    {
      if (length == 1)
      {
        return Method.tabCompleterPlayer(sender, args);
      }
      else if (length == 2)
      {
        return Method.tabCompleterList(args, Sound.values(), "<소리>", true);
      }
      else if (length == 3)
      {
        return Method.tabCompleterList(args, SoundCategory.values(), "[소리 유형]");
      }
      else if (length == 4)
      {
        return Method.tabCompleterDoubleRadius(args, 0, Float.MAX_VALUE, "[음량]");
      }
      else if (length == 5)
      {
        return Method.tabCompleterDoubleRadius(args, 0.5, 2, "[음색]");
      }
      else if (length == 6)
      {
        return Method.tabCompleterBoolean(args, "[명령어 출력 숨김 여부]");
      }
    }
    else if (name.equals("airpoint") && Method.hasPermission(sender, Permission.CMD_AIRPOINT, false))
    {
      if (length == 1)
      {
        return Method.tabCompleterList(args, "<인수>", "set", "give", "take");
      }
      else if (length == 2)
      {
        return Method.tabCompleterPlayer(sender, args);
      }
      else if (length == 3)
      {
        return Method.tabCompleterIntegerRadius(args, 1, 300, "<수치>");
      }
      else if (length == 4)
      {
        return Method.tabCompleterBoolean(args, "[명령어 출력 숨김 여부]");
      }
    }
    else if (name.equals("getuserdata") && Method.hasPermission(sender, Permission.CMD_GETUSERDATA, false))
    {
      if (length == 1)
      {
        if (label.equals("gud"))
        {
          return Method.tabCompleterPlayer(sender, args);
        }
        return Method.tabCompleterOfflinePlayer(sender, args);
      }
      else if (length == 2)
      {
        return Method.tabCompleterList(args, UserData.values(), "<유저 데이터>");
      }
    }
    else if (name.equals("reinforce") && Method.hasPermission(sender, Permission.CMD_REINFORCE, false))
    {
      if (length == 1)
      {
        return Method.tabCompleterList(args, "<인수>", "상태", "시작");
      }
    }
    else if (name.equals("forcechat") && Method.hasPermission(sender, Permission.CMD_FORCECHAT, false))
    {
      if (length == 1)
      {
        return Method.tabCompleterPlayer(sender, args);
      }
      else if (length == 2)
      {
        return Method.tabCompleterBoolean(args, "[명령어 출력 숨김 여부]");
      }
      else
      {
        if (args.length == 3)
        {
          return Method.tabCompleterList(args, "<메시지>", true, "<메시지>", "op:");
        }
        return Method.tabCompleterList(args, "[메시지]", true);
      }
    }
    else if (name.equals("sethelditemslot") && Method.hasPermission(sender, Permission.CMD_SETHELDITEMSLOT, false))
    {
      if (length == 1)
      {
        return Method.tabCompleterIntegerRadius(args, 1, 9, "<슬롯>");
      }
      else if (length == 2)
      {
        return Method.tabCompleterPlayer(sender, args);
      }
      else if (length == 3)
      {
        return Method.tabCompleterBoolean(args, "[명령어 출력 숨김 여부]");
      }
    }
    else if (name.equals("swaphelditem") && Method.hasPermission(sender, Permission.CMD_SWAPHELDITEM, false))
    {
      if (length == 1)
      {
        return Method.tabCompleterPlayer(sender, args);
      }
      else if (length == 2)
      {
        return Method.tabCompleterBoolean(args, "[명령어 출력 숨김 여부]");
      }
    }
    else if (name.equals("itemdata2") && Method.hasPermission(sender, Permission.CMD_ITEMDATA, false))
    {
      if (length == 1)
      {
        return Method.tabCompleterIntegerRadius(args, 1, 36, "[슬롯]", "helmet", "chestplate", "leggings", "boots", "offhand", "cursor");
      }
      else if (length == 2)
      {
        return Method.tabCompleterPlayer(sender, args);
      }
    }
    else if (name.equals("itemdata3") && Method.hasPermission(sender, Permission.CMD_ITEMDATA, false))
    {
      if (length == 1)
      {
        return Method.tabCompleterIntegerRadius(args, 1, 27, "[슬롯]");
      }
      if (length == 2)
      {
        return Method.tabCompleterPlayer(sender, args);
      }
    }
    else if (name.equals("getpositions") && Method.hasPermission(sender, Permission.CMD_GETPOSITIONS, false))
    {
      if (length == 1)
      {
        return Method.tabCompleterList(args, Method.listWorlds(), "[월드]");
      }
    }
    else if (name.equals("checkpermission") && Method.hasPermission(sender, Permission.CMD_CHECKPERMISSION, false))
    {
      if (length == 1)
      {
        return Method.tabCompleterPlayer(sender, args);
      }
      else if (length == 2)
      {
        List<String> list = new ArrayList<>();
        for (org.bukkit.permissions.Permission permission : Cucumbery.getPlugin().getPluginManager().getPermissions())
        {
          list.add(permission.getName());
        }
        return Method.tabCompleterList(args, list, "<퍼미션 노드>", true);
      }
    }
    else if (name.equals("allplayer") && Method.hasPermission(sender, Permission.CMD_ALLPLAYER, false))
    {
      if (length == 1)
      {
        return Method.tabCompleterList(args, Constant.AllPlayer.values(), "<통제 범주>");
      }
      else if (length == 2)
      {
        return Method.tabCompleterList(args, "<인수>", "true", "false", "check");
      }
    }
    else if (name.equals("commandpack") && Method.hasPermission(sender, Permission.CMD_COMMANDPACK, false))
    {
      if (length == 1)
      {
        return Method.tabCompleterList(args, "<인수>", "edit", "remove", "execute");
      }
      if (length == 2)
      {
        switch (args[0])
        {
          case "execute":
            return Method.tabCompleterList(args, Method.addAll(Method.tabCompleterPlayer(sender, args), "--console"), "[<플레이어>]", true);
          case "edit":
            if (Variable.commandPacks.size() == 0)
            {
              return Method.tabCompleterList(args, "<새로운 명령어 팩 파일 이름>", true);
            }
            return Method.tabCompleterList(args, Variable.commandPacks.keySet(), (Variable.commandPacks.containsKey(lastArg) ? "<편집할 명령어 팩 파일 이름>" : "<새로운 명령어 팩 파일 이름>"), true);
          case "remove":
            if (Variable.commandPacks.size() == 0)
            {
              return Collections.singletonList("유효한 명령어 팩 파일이 존재하지 않습니다.");
            }
            return Method.tabCompleterList(args, Variable.commandPacks.keySet(), "<명령어 팩 파일 이름>");
        }
      }
      else
      {
        switch (args[0])
        {
          case "execute":
          {
            if (args.length == 3)
            {
              if (Variable.commandPacks.size() == 0)
              {
                return Collections.singletonList("유효한 명령어 팩 파일이 존재하지 않습니다.");
              }
              return Method.tabCompleterList(args, Variable.commandPacks.keySet(), "<명령어 팩 파일 이름>");
            }
            if (args.length == 4)
            {
              if (Variable.commandPacks.size() == 0)
              {
                return Collections.singletonList("유효한 명령어 팩 파일이 존재하지 않습니다.");
              }
              YamlConfiguration config = Variable.commandPacks.get(args[2]);
              if (config == null)
              {
                return Collections.singletonList(args[2] + MessageUtil.getFinalConsonant(args[2], MessageUtil.ConsonantType.은는) + " 잘못되거나 알 수 없는 명령어 팩 파일입니다.");
              }
              if (args.length == 4)
              {
                return Method.tabCompleterList(args, config.getKeys(false), "<명령어 팩>");
              }
              else
              {
                return Method.tabCompleterList(args, "[명령어 팩]", true);
              }
            }
          }
          case "edit":
          {
            YamlConfiguration config = Variable.commandPacks.get(args[1]);
            if (args.length == 3)
            {
              if (Variable.commandPacks.size() == 0)
              {
                return Method.tabCompleterList(args, "<새로운 명령어 팩 이름>", true);
              }
              if (config == null)
              {
                return Method.tabCompleterList(args, "<새로운 명령어 팩 이름>", true);
              }
              return Method.tabCompleterList(args, config.getKeys(false), (config.getKeys(false).contains(lastArg) ? "<편집할 명령어 팩 이름>" : "<새로운 명령어 팩 이름>"), true);
            }
            if (args.length == 4)
            {
              return Method.tabCompleterList(args, "<인수>", "list", "add", "remove", "set", "insert");
            }
            if (args.length == 5)
            {
              switch (args[3])
              {
                case "set":
                  return Method.tabCompleterIntegerRadius(args, 1, Integer.MAX_VALUE, "<줄>");
                case "insert":
                {
                  if (config == null)
                  {
                    return Collections.singletonList("명령어를 들여쓸 수 없습니다.");
                  }
                  List<String> commands = config.getStringList(args[2].replace("__", " "));
                  if (commands.size() == 0)
                  {
                    return Collections.singletonList("명령어를 들여쓸 수 없습니다.");
                  }
                  return Method.tabCompleterIntegerRadius(args, 1, commands.size(), "<줄>");
                }
                case "remove":
                {
                  if (config == null)
                  {
                    return Collections.singletonList("제거할 명령어가 없습니다.");
                  }
                  List<String> commands = config.getStringList(args[2].replace("__", " "));
                  if (commands.size() == 0)
                  {
                    return Collections.singletonList("제거할 명령어가 없습니다.");
                  }
                  return Method.tabCompleterIntegerRadius(args, 1, commands.size(), "[줄]", "--all");
                }
              }
            }

            if ((args.length >= 5 && Method.equals(args[3], "add")) || (args.length >= 6 && Method.equals(args[3], "set", "insert")))
            {
              int argLength = 5;
              boolean insertOrSet = args[3].equals("insert") || args[3].equals("set");
              if (insertOrSet)
              {
                argLength++;
              }
              if (length == argLength)
              {
                List<String> cmds = Method.getAllServerCommands();
                List<String> newCmds = new ArrayList<>();
                newCmds.add("chat:" + "<채팅 메시지>");
                newCmds.add("opchat:" + "<오피 권한으로 채팅 메시지>");
                newCmds.add("chat:/" + "<채팅 명령어>");
                newCmds.add("opchat:/" + "<오피 권한으로 채팅 명령어>");
                for (String cmd2 : cmds)
                {
                  newCmds.add(cmd2);
                  newCmds.add("chat:/" + cmd2);
                  newCmds.add("op:" + cmd2);
                  newCmds.add("opchat:/" + cmd2);
                  newCmds.add("console:" + cmd2);
                }
                List<String> list = new ArrayList<>(Method.tabCompleterList(args, Variable.commandPacks.keySet(), "<명령어 팩 파일>", true));
                for (int i = 0; i < list.size(); i++)
                {
                  String fileName = list.get(i);
                  newCmds.add("commandpack:" + fileName);
                  list.set(i, "commandpack:" + fileName);
                }
                return Method.tabCompleterList(args, newCmds, "<명령어>", true);
              }
              else
              {
                String cmdLabel = args[argLength - 1];
                if (cmdLabel.startsWith("op:"))
                {
                  cmdLabel = cmdLabel.substring(3);
                }
                if (cmdLabel.startsWith("chat:/"))
                {
                  cmdLabel = cmdLabel.substring(6);
                }
                else if (cmdLabel.startsWith("chat:"))
                {
                  return Collections.singletonList("[메시지]");
                }
                if (cmdLabel.startsWith("opchat:/"))
                {
                  cmdLabel = cmdLabel.substring(8);
                }
                else if (cmdLabel.startsWith("opchat:"))
                {
                  return Collections.singletonList("[메시지]");
                }
                if (cmdLabel.startsWith("console:"))
                {
                  cmdLabel = cmdLabel.substring(8);
                }
                if (length == argLength + 1 && (cmdLabel.equals("?") || cmdLabel.equals("bukkit:?") || cmdLabel.equals("bukkit:help")))
                {
                  return Method.tabCompleterList(args, Method.getAllServerCommands(), "<명령어>");
                }
                PluginCommand command = Bukkit.getServer().getPluginCommand(cmdLabel);
                String[] args2 = new String[length - argLength];
                System.arraycopy(args, argLength, args2, 0, length - argLength);
                if (command != null)
                {
                  org.bukkit.command.TabCompleter completer = command.getTabCompleter();
                  if (completer != null)
                  {
                    return completer.onTabComplete(sender, command, command.getLabel(), args2);
                  }
                }
                return Collections.singletonList("[<인수>]");
              }
            }
          }
        }
      }
    }
    else if (name.equals("swapteleport") && Method.hasPermission(sender, Permission.CMD_SWAP_TELEPORT, false))
    {
      if (length == 1)
      {
        return Method.tabCompleterPlayer(sender, args);
      }
      else if (length == 2)
      {
        return Method.tabCompleterPlayer(sender, args);
      }
      else if (length == 3)
      {
        return Method.tabCompleterBoolean(args, "[운동 에너지 보존]");
      }
      else if (length == 4)
      {
        return Method.tabCompleterBoolean(args, "[위치 에너지 보존]");
      }
      else if (length == 5)
      {
        return Method.tabCompleterBoolean(args, "[명령어 출력 숨김 여부]");
      }
    }
    else if (name.equals("calcdistance") && Method.hasPermission(sender, Permission.CMD_CALC_DISTANCE, false))
    {
      if (length == 1)
      {
        return Method.tabCompleterPlayer(sender, args);
      }
      else if (length == 2)
      {
        return Method.tabCompleterPlayer(sender, args);
      }
      else if (length == 3)
      {
        return Method.tabCompleterBoolean(args, "[다른 월드여도 좌표 거리만 측정]");
      }
    }
    else if (name.equals("checkamount") && Method.hasPermission(sender, Permission.CMD_CHECK_AMOUNT, false))
    {
      if (length == 1)
      {
        return Method.tabCompleterPlayer(sender, args);
      }
      else if (length == 2)
      {
        return Method.tabCompleterList(args, "<인수>", "space", "amount");
      }
      else if (length == 3)
      {
        List<String> list = new ArrayList<>();
        for (Material material : Material.values())
        {
          if (material.isItem() && material != Material.AIR)
          {
            list.add(material.toString().toLowerCase());
          }
        }
        return Method.tabCompleterList(args, list, "<아이템>");
      }
      else if (length == 4)
      {
        return Method.tabCompleterIntegerRadius(args, 0, 32767, "[내구도 조건]", "*");
      }
    }
    else if (name.equals("setattribute") && Method.hasPermission(sender, Permission.CMD_SET_ATTRIBUTE, false))
    {
      if (length == 1)
      {
        return Method.tabCompleterPlayer(sender, args);
      }
      else if (length == 2)
      {
        return Method.tabCompleterList(args, Attribute.values(), "<속성 값>");
      }
      else if (length == 3)
      {
        return Method.tabCompleterDoubleRadius(args, 0, Double.MAX_VALUE, "<수치>", "reset");
      }
      else if (length == 4)
      {
        return Method.tabCompleterBoolean(args, "[명령어 출력 숨김 여부]");
      }
    }
    else if (name.equals("updateinventory") && Method.hasPermission(sender, Permission.CMD_UPDATE_INVENTORY, false))
    {
      if (length == 1)
      {
        return Method.tabCompleterPlayer(sender, args);
      }
      else if (length == 2)
      {
        return Method.tabCompleterBoolean(args, "[명령어 출력 숨김 여부]");
      }
    }
    else if (name.equals("updatecommands") && Method.hasPermission(sender, Permission.CMD_UPDATE_COMMANDS, false))
    {
      if (length == 1)
      {
        return Method.tabCompleterPlayer(sender, args);
      }
      else if (length == 2)
      {
        return Method.tabCompleterBoolean(args, "[명령어 출력 숨김 여부]");
      }
    }
    else if (name.equals("csong") && Method.hasPermission(sender, Permission.CMD_SONG, false))
    {
      if (!Cucumbery.using_NoteBlockAPI)
      {
        return Collections.singletonList("NoteBlockAPI 플러그인을 사용하고 있지 않습니다.");
      }
      if (length == 1)
      {
        return Method.tabCompleterList(args, "<인수>", "play", "stop", "info", "listening", "pause");
      }
      else if (args[0].equals("play"))
      {
        if (args.length == 2)
        {
          Variable.songFiles.addAll(Songs.list);
          return Method.tabCompleterList(args, Variable.songFiles, "<노래 파일>", true);
        }
        return Method.tabCompleterList(args, "<노래 파일>", true);
      }
    }
    else if (name.equals("csong2") && Method.hasPermission(sender, Permission.CMD_SONG, false))
    {
      if (!Cucumbery.using_NoteBlockAPI)
      {
        return Collections.singletonList("NoteBlockAPI 플러그인을 사용하고 있지 않습니다.");
      }
      if (length == 1)
      {
        return Method.tabCompleterPlayer(sender, args);
      }
      else if (length == 2)
      {
        return Method.tabCompleterList(args, "<인수>", "play", "stop", "info");
      }
      else if (length == 3)
      {
        switch (args[1])
        {
          case "play":
            return Method.tabCompleterBoolean(args, "<명령어 출력 숨김 여부>");
          case "stop":
            return Method.tabCompleterBoolean(args, "[명령어 출력 숨김 여부]");
        }
      }
      else if (args[1].equals("play"))
      {
        if (args.length == 4)
        {
          return Method.tabCompleterList(args, Variable.songFiles, "<노래 파일>", true);
        }
        return Method.tabCompleterList(args, "<노래 파일>", true);
      }
    }
    else if (name.equals("citemtag") && sender instanceof Player player)
    {
      ItemStack item = player.getInventory().getItemInMainHand();
      if (!ItemStackUtil.itemExists(item))
      {
        return Collections.singletonList(Prefix.NO_HOLDING_ITEM.toString());
      }
      Material material = item.getType();
      switch (length)
      {
        case 1:
        {
          List<String> list = Method.tabCompleterList(args, "<태그>", "restriction", "customlore", "extratag", "customdurability" + (!Constant.DURABLE_ITEMS.contains(material) ? "(내구도가 있는 아이템 전용)" : ""),
                  "customitemtype", "hideflag", "customrarity", "usage", "expiredate", "tnt" + (material != Material.TNT ? "(TNT 전용)" : ""), "abovecustomlore",
                  "customenchant", "customitem", "food" + (ItemStackUtil.isEdible(material) ? "" : "(먹을 수 있는 아이템 전용)"), "id", "nbt");
          if (args[0].equals("tnt") && material != Material.TNT)
          {
            return Collections.singletonList("해당 태그는 TNT에만 사용할 수 있습니다.");
          }
          if (args[0].equals("customdurability") && !Constant.DURABLE_ITEMS.contains(material))
          {
            return Collections.singletonList("해당 태그는 내구도가 있는 아이템에만 사용할 수 있습니다.");
          }
          if (args[0].equals("food") && !ItemStackUtil.isEdible(material))
          {
            return Collections.singletonList("해당 태그는 먹을 수 있는 아이템에만 사용할 수 있습니다.");
          }
          return list;
        }
        case 2:
          switch (args[0])
          {
            case "restriction":
              return Method.tabCompleterList(args, "<인수>", "add", "remove", "modify");
            case "customlore":
            case "abovecustomlore":
              return Method.tabCompleterList(args, "<인수>", "add", "remove", "set", "insert", "list");
            case "customdurability":
              if (!Constant.DURABLE_ITEMS.contains(material))
              {
                return Collections.singletonList("해당 태그는 내구도가 있는 아이템에만 사용할 수 있습니다.");
              }
              return Method.tabCompleterList(args, "<인수>", "durability", "chance");
            case "hideflag":
            case "extratag":
              return Method.tabCompleterList(args, "<인수>", "add", "remove");
            case "customrarity":
              return Method.tabCompleterList(args, "<인수>", "base", "value", "set");
            case "usage":
              return Method.tabCompleterList(args, "<인수>", "disposable", "command", "equip", "cooldown", "permission");
            case "tnt":
              if (material != Material.TNT)
              {
                return Collections.singletonList("해당 태그는 TNT에만 사용할 수 있습니다.");
              }
              return Method.tabCompleterList(args, "<인수>", "unstable", "ignite", "fuse");
            case "customenchant":
              return Method.tabCompleterList(args, "<인수>", "list", "add", "remove");
            case "customitem":
              return Method.tabCompleterList(args, "<인수>", "setid", "modify");
            case "food":
              if (!ItemStackUtil.isEdible(material))
              {
                return Collections.singletonList("해당 태그는 먹을 수 있는 아이템에만 사용할 수 있습니다.");
              }
              boolean hasEffects = ItemStackUtil.hasStatusEffect(material);
              return Method.tabCompleterList(args, "<인수>", "disable-status-effect" + (hasEffects ? "" : "(상태 효과에 영향을 줄 수 있는 아이템 전용)"), "food-level", "saturation", "nourishment");
            case "nbt":
              return Method.tabCompleterList(args, "<인수>", "set", "remove", "merge");
          }
          break;
        case 3:
          switch (args[0])
          {
            case "restriction":
              switch (args[1])
              {
                case "add":
                case "remove":
                case "modify":
                  return Method.tabCompleterList(args, RestrictionType.values(), "<사용 제한 태그>");
              }
              break;
            case "extratag":
              switch (args[1])
              {
                case "add":
                case "remove":
                  boolean shulkerBoxExclusive = !Constant.SHULKER_BOXES.contains(material);
                  boolean enderPearlExclusive = material != Material.ENDER_PEARL;
                  List<String> list = new ArrayList<>(Method.tabCompleterList(args, Constant.ExtraTag.values(), "<태그>"));
                  for (int i = 0; i < list.size(); i++)
                  {
                    if (shulkerBoxExclusive && list.get(i).equalsIgnoreCase(Constant.ExtraTag.PORTABLE_SHULKER_BOX.toString()))
                    {
                      list.set(i, Constant.ExtraTag.PORTABLE_SHULKER_BOX.toString().toLowerCase() + "(셜커 상자 전용)");
                      break;
                    }
                    if (enderPearlExclusive && list.get(i).equalsIgnoreCase(Constant.ExtraTag.NO_COOLDOWN_ENDER_PEARL.toString()))
                    {
                      list.set(i, Constant.ExtraTag.NO_COOLDOWN_ENDER_PEARL.toString().toLowerCase() + "(엔더 진주 전용)");
                      break;
                    }
                  }
                  if (shulkerBoxExclusive && args[2].equalsIgnoreCase(Constant.ExtraTag.PORTABLE_SHULKER_BOX.toString()))
                  {
                    return Collections.singletonList("해당 태그는 셜커 상자에만 사용할 수 있습니다.");
                  }
                  if (enderPearlExclusive && args[2].equalsIgnoreCase(Constant.ExtraTag.NO_COOLDOWN_ENDER_PEARL.toString()))
                  {
                    return Collections.singletonList("해당 태그는 엔더 진주에만 사용할 수 있습니다.");
                  }
                  return list;
              }
            case "hideflag":
              switch (args[1])
              {
                case "add":
                case "remove":
                  return Method.tabCompleterList(args, Method.addAll(Constant.CucumberyHideFlag.values()
                          , "--all", "--모두", "--ables", "--가능충", "--dura", "--내구도"), "<태그>");
              }
              break;
            case "customrarity":
              switch (args[1])
              {
                case "base":
                case "set":
                  return Method.tabCompleterList(args, Method.addAll(ItemCategory.Rarity.values(), "--remove"), "<아이템 등급>");
                case "value":
                  return Method.tabCompleterIntegerRadius(args, -2_000_000_000, 2_000_000_000, "<수치>");
              }
              break;
            case "usage":
              switch (args[1])
              {
                case "command":
                case "cooldown":
                case "permission":
                  return Method.tabCompleterList(args, Constant.ItemUsageType.values(), "<실행 유형>");
                case "disposable":
                  List<String> usageTypes = Method.enumToList(Constant.ItemUsageType.values());
                  if (args[2].contains("attack") && usageTypes.contains(args[2]))
                  {
                    return Collections.singletonList(ItemUsageType.valueOf(args[2].toUpperCase()).getDisplay() + " 태그에는 소비 확률을 적용할 수 없습니다.");
                  }
                  usageTypes.removeIf(s -> s.contains("attack"));
                  return Method.tabCompleterList(args, usageTypes, "<실행 유형>");
                case "equip":
                  return Method.tabCompleterList(args, "<슬롯>", "--remove", "helmet", "chestplate", "leggings", "boots");
              }
              break;
            case "tnt":
              switch (args[1])
              {
                case "unstable":
                  return Method.tabCompleterBoolean(args, "<건드리면 점화 여부>");
                case "ignite":
                  return Method.tabCompleterBoolean(args, "<설치 즉시 점화 여부>");
                case "fuse":
                  return Method.tabCompleterIntegerRadius(args, 0, Integer.MAX_VALUE, "<점화 시간(틱)>", "-1");
              }
              break;
            case "customenchant":
              switch (args[1])
              {
                case "add":
                case "remove":
                  return Method.tabCompleterList(args, Constant.CustomEnchant.values(), "<커스텀 마법>");
              }
              break;
            case "customitem":
              switch (args[1])
              {
                case "setid":
                  List<String> list = Method.tabCompleterList(args, "<아이디>", "railgun", "fishingrod" + (material != Material.FISHING_ROD ? "(낚싯대 전용)" : ""), "--remove");
                  if (args[2].equals("fishingrod") && material != Material.FISHING_ROD)
                  {
                    return Collections.singletonList("해당 태그는 낚싯대에만 사용할 수 있습니다.");
                  }
                  return list;
                case "modify":
                  return this.customItemTabCompleter(player, args);
              }
              break;
            case "customdurability":
              if (!Constant.DURABLE_ITEMS.contains(material))
              {
                return Collections.singletonList("해당 태그는 내구도가 있는 아이템에만 사용할 수 있습니다.");
              }
              switch (args[1])
              {
                case "chance":
                  return Method.tabCompleterDoubleRadius(args, 0, 100, "<내구도 감소 무효 확률(%)>");
                case "durability":
                  return Method.tabCompleterLongRadius(args, 0, Long.MAX_VALUE, "<내구도>", "0");
              }
              break;
            case "food":
              if (!ItemStackUtil.isEdible(material))
              {
                return Collections.singletonList("해당 태그는 먹을 수 있는 아이템에만 사용할 수 있습니다.");
              }
              switch (args[1])
              {
                case "disable-status-effect":
                  boolean hasEffects = ItemStackUtil.hasStatusEffect(material);
                  if (!hasEffects)
                  {
                    return Collections.singletonList("해당 태그는 상태 효과에 영향을 줄 수 있는 아이템에만 사용할 수 있습니다.");
                  }
                  return Method.tabCompleterBoolean(args, "<섭취 시 상태 효과 미적용 여부>");
                case "food-level":
                  return Method.tabCompleterIntegerRadius(args, -20, 20, "<음식 포인트>", "--remove");
                case "saturation":
                  return Method.tabCompleterDoubleRadius(args, -20, 20, "<포화도>", "--remove");
              }
              break;
            case "nbt":
              switch (args[1])
              {
                case "set":
                  return Method.tabCompleterList(args, "<자료형>",
                          "boolean", "byte", "byte-array", "short", "int", "int-list", "int-array", "long", "long-list",
                          "float", "float-list", "double", "double-list", "uuid", "string", "string-list",
                          "compound", "compound-list");
                case "remove":
                  NBTItem nbtItem = new NBTItem(item);
                  List<String> returnValue = new ArrayList<>(nbtItem.getKeys());
                  for (String key1 : nbtItem.getKeys())
                  {
                    NBTCompound compound2 = nbtItem.getCompound(key1);
                    if (compound2 != null)
                    {
                      for (String key2 : compound2.getKeys())
                      {
                        returnValue.add(key1 + "." + key2);
                        NBTCompound compound3 = compound2.getCompound(key2);
                        if (compound3 != null)
                        {
                          for (String key3 : compound3.getKeys())
                          {
                            returnValue.add(key1 + "." + key2 + "." + key3);
                            NBTCompound compound4 = compound3.getCompound(key3);
                            if (compound4 != null)
                            {
                              for (String key4 : compound4.getKeys())
                              {
                                returnValue.add(key1 + "." + key2 + "." + key3 + "." + key4);
                                NBTCompound compound5 = compound4.getCompound(key4);
                                if (compound5 != null)
                                {
                                  for (String key5 : compound5.getKeys())
                                  {
                                    returnValue.add(key1 + "." + key2 + "." + key3 + "." + key4 + "." + key5);
                                    NBTCompound compound6 = compound5.getCompound(key5);
                                    if (compound6 != null)
                                    {
                                      for (String key6 : compound6.getKeys())
                                      {
                                        returnValue.add(key1 + "." + key2 + "." + key3 + "." + key4 + "." + key5 + "." + key6);
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
                  return Method.tabCompleterList(args, returnValue, "<태그>");
              }
              break;
          }
          break;
        case 4:
          switch (args[0])
          {
            case "restriction":
              switch (args[1])
              {
                case "add":
                  return Method.tabCompleterBoolean(args, "[설명 숨김 여부]");
                case "modify":
                  return Method.tabCompleterList(args, "<인수>", "hide", "permission");
              }
              break;
            case "customitem":
              if ("modify".equals(args[1]))
              {
                return this.customItemTabCompleter(player, args);
              }
              break;
            case "customenchant":
              switch (args[1])
              {
                case "add":
                case "remove":
                  return Method.tabCompleterIntegerRadius(args, 1, Integer.MAX_VALUE, "[레벨]");
              }
              break;
            case "customdurability":
              if (!Constant.DURABLE_ITEMS.contains(material))
              {
                return Collections.singletonList("해당 태그는 내구도가 있는 아이템에만 사용할 수 있습니다.");
              }
              if ("durability".equals(args[1]))
              {
                return Method.tabCompleterLongRadius(args, 1, Long.MAX_VALUE, "[최대 내구도]");
              }
              break;
            case "usage":
              switch (args[1])
              {
                case "command":
                  try
                  {
                    ItemUsageType.valueOf(args[2].toUpperCase());
                  }
                  catch (Exception e)
                  {
                    return Collections.singletonList(args[2] + MessageUtil.getFinalConsonant(args[2], MessageUtil.ConsonantType.은는) + " 잘못되거나 알 수 없는 실행 유형입니다.");
                  }
                  return Method.tabCompleterList(args, "<인수>", "add", "remove", "list", "set", "insert");
                case "cooldown":
                  try
                  {
                    ItemUsageType.valueOf(args[2].toUpperCase());
                  }
                  catch (Exception e)
                  {
                    return Collections.singletonList(args[2] + MessageUtil.getFinalConsonant(args[2], MessageUtil.ConsonantType.은는) + " 잘못되거나 알 수 없는 실행 유형입니다.");
                  }
                  return Method.tabCompleterList(args, "<인수>", "tag", "time");
                case "disposable":
                  String display;
                  try
                  {
                    ItemUsageType itemUsageType = ItemUsageType.valueOf(args[2].toUpperCase());
                    display = itemUsageType.getDisplay();
                  }
                  catch (Exception e)
                  {
                    return Collections.singletonList(args[2] + MessageUtil.getFinalConsonant(args[2], MessageUtil.ConsonantType.은는) + " 잘못되거나 알 수 없는 실행 유형입니다.");
                  }
                  if (args[2].contains("attack"))
                  {
                    return Collections.singletonList(display + " 태그에는 소비 확률을 적용할 수 없습니다.");
                  }
                  return Method.tabCompleterDoubleRadius(args, 0, 100, "<" + display + " 시 소비 확률(%)>", "-1");
                case "permission":
                  try
                  {
                    ItemUsageType.valueOf(args[2].toUpperCase());
                  }
                  catch (Exception e)
                  {
                    return Collections.singletonList(args[2] + MessageUtil.getFinalConsonant(args[2], MessageUtil.ConsonantType.은는) + " 잘못되거나 알 수 없는 실행 유형입니다.");
                  }
                  return Method.tabCompleterList(args, "<퍼미션 노드>", true, "--remove", "<퍼미션 노드>");
              }
              break;
            case "nbt":
              if (args[1].equals("set"))
              {
                switch (args[2])
                {
                  case "boolean":
                  case "byte":
                  case "byte-array":
                  case "short":
                  case "int":
                  case "int-array":
                  case "int-list":
                  case "long":
                  case "long-list":
                  case "float":
                  case "float-list":
                  case "double":
                  case "double-list":
                  case "uuid":
                  case "string":
                  case "string-list":
                  case "compound":
                  case "compound-list":
                    NBTItem nbtItem = new NBTItem(item);
                    Set<String> keys = nbtItem.getKeys();
                    if (keys.contains(args[3]))
                    {
                      return Method.tabCompleterList(args, Method.addAll(keys, "<변경할 키>"), "<변경할 키>", true);
                    }
                    return Method.tabCompleterList(args, Method.addAll(keys, "<키|변경할 키>"), "<키>", true);
                  default:
                    return Collections.singletonList(args[2] + MessageUtil.getFinalConsonant(args[2], MessageUtil.ConsonantType.은는) + " 잘못되거나 알 수 없는 자료형입니다.");
                }
              }
              break;
          }
          break;
        case 5:
          switch (args[0])
          {
            case "restriction":
              switch (args[1])
              {
                case "add":
                  return Method.tabCompleterList(args, "[우회 퍼미션 노드]", true);
                case "modify":
                  if ("hide".equals(args[3]))
                  {
                    return Method.tabCompleterBoolean(args, "<설명 숨김 여부>");
                  }
                  else if ("permission".equals(args[3]))
                  {
                    return Method.tabCompleterList(args, "<우회 퍼미션 노드>", true, "--remove", "<우회 퍼미션 노드>");
                  }
              }
              break;
            case "usage":
              if (args[1].equals("cooldown"))
              {
                try
                {
                  ItemUsageType.valueOf(args[2].toUpperCase());
                }
                catch (Exception e)
                {
                  return Collections.singletonList(args[2] + MessageUtil.getFinalConsonant(args[2], MessageUtil.ConsonantType.은는) + " 잘못되거나 알 수 없는 실행 유형입니다.");
                }
                switch (args[3])
                {
                  case "tag":
                    return Method.tabCompleterList(args, "<태그>", true, "<태그>", "default-" + args[2].toLowerCase());
                  case "time":
                    return Method.tabCompleterDoubleRadius(args, 0, Double.MAX_VALUE, "<재사용 대기 시간(초)>", "0");
                }
              }
              break;
            case "nbt":
              if (args[1].equals("set"))
              {
                String key = args[3];
                String input = args[4];
                NBTItem nbtItem = new NBTItem(item);
                switch (args[2])
                {
                  case "boolean":
                    boolean exists = nbtItem.hasKey(key) && nbtItem.getType(key) == NBTType.NBTTagByte;
                    byte b = nbtItem.getByte(key);
                    exists = exists && (b == 0 || b == 1);
                    boolean bool = nbtItem.getBoolean(key);
                    return Method.tabCompleterBoolean(args, "<값>", exists ? bool + "(기존값)" : null);
                  case "byte":
                    exists = nbtItem.hasKey(key) && nbtItem.getType(key) == NBTType.NBTTagByte;
                    b = nbtItem.getByte(key);
                    return Method.tabCompleterIntegerRadius(args, Byte.MIN_VALUE, Byte.MAX_VALUE, "<값>", exists ? b + "(기존값)" : "");
                  case "byte-array":
                    if (input.startsWith(","))
                    {
                      return Collections.singletonList("정수가 필요합니다. (" + input + ")");
                    }
                    String[] split = input.split(",");
                    if (!input.equals(""))
                    {
                      for (String s : split)
                      {
                        if (!MessageUtil.isInteger(sender, s, false))
                        {
                          return Collections.singletonList("정수가 필요합니다. (" + s + ")");
                        }
                        int j = Integer.parseInt(s);
                        if (j > Byte.MAX_VALUE)
                        {
                          String valueString = Constant.Sosu15.format(j);
                          valueString += MessageUtil.getFinalConsonant(valueString, MessageUtil.ConsonantType.이가);
                          return Collections.singletonList("정수는 " + Constant.Sosu15.format(Byte.MAX_VALUE) + " 이하여야 하는데, " + valueString + " 있습니다.");
                        }
                        else if (j < Byte.MIN_VALUE)
                        {
                          String valueString = Constant.Sosu15.format(j);
                          valueString += MessageUtil.getFinalConsonant(valueString, MessageUtil.ConsonantType.이가);
                          return Collections.singletonList("정수는 " + Constant.Sosu15.format(Byte.MIN_VALUE) + " 이상이여야 하는데, " + valueString + " 있습니다.");
                        }
                      }
                    }
                    exists = nbtItem.hasKey(key) && nbtItem.getType(key) == NBTType.NBTTagByteArray;
                    if (exists)
                    {
                      byte[] originValue = nbtItem.getByteArray(key);
                      StringBuilder originValueStringBuilder = new StringBuilder();
                      for (byte originValue2 : originValue)
                      {
                        originValueStringBuilder.append(originValue2).append(",");
                      }
                      String originValueString = originValueStringBuilder.toString();
                      originValueString = originValueString.substring(0, originValueString.length() - 1);
                      return Method.tabCompleterList(args, "<값>", true, "<값>", "예시 : 1,2,3", originValueString, originValueString + "(기본값)");
                    }
                    return Method.tabCompleterList(args, "<값>", true, "<값>", "예시 : 1,2,3");
                  case "short":
                    exists = nbtItem.hasKey(key) && nbtItem.getType(key) == NBTType.NBTTagShort;
                    short s = nbtItem.getShort(key);
                    return Method.tabCompleterIntegerRadius(args, Short.MIN_VALUE, Short.MAX_VALUE, "<값>", exists ? s + "(기존값)" : "");
                  case "int":
                    exists = nbtItem.hasKey(key) && nbtItem.getType(key) == NBTType.NBTTagInt;
                    int i = nbtItem.getInteger(key);
                    return Method.tabCompleterIntegerRadius(args, Integer.MIN_VALUE, Integer.MAX_VALUE, "<값>", exists ? i + "(기존값)" : "");
                  case "int-array":
                    if (input.startsWith(","))
                    {
                      return Collections.singletonList("정수가 필요합니다. (" + input + ")");
                    }
                    split = input.split(",");
                    if (!input.equals(""))
                    {
                      for (String s2 : split)
                      {
                        if (!MessageUtil.isInteger(sender, s2, false))
                        {
                          return Collections.singletonList("정수가 필요합니다. (" + s2 + ")");
                        }
                      }
                    }
                    exists = nbtItem.hasKey(key) && nbtItem.getType(key) == NBTType.NBTTagIntArray;
                    if (exists)
                    {
                      int[] originValue = nbtItem.getIntArray(key);
                      StringBuilder originValueStringBuilder = new StringBuilder();
                      for (int originValue2 : originValue)
                      {
                        originValueStringBuilder.append(originValue2).append(",");
                      }
                      String originValueString = originValueStringBuilder.toString();
                      originValueString = originValueString.substring(0, originValueString.length() - 1);
                      return Method.tabCompleterList(args, "<값>", true, "<값>", "예시 : 1,2,3", originValueString, originValueString + "(기본값)");
                    }
                    return Method.tabCompleterList(args, "<값>", true, "<값>", "예시 : 1,2,3");
                  case "int-list":
                    if (input.startsWith(","))
                    {
                      return Collections.singletonList("정수가 필요합니다. (" + input + ")");
                    }
                    split = input.split(",");
                    if (!input.equals(""))
                    {
                      for (String s2 : split)
                      {
                        if (!MessageUtil.isInteger(sender, s2, false))
                        {
                          return Collections.singletonList("정수가 필요합니다. (" + s2 + ")");
                        }
                      }
                    }
                    NBTList<Integer> nbtIntegerList = nbtItem.getIntegerList(key);
                    if (nbtIntegerList != null && nbtIntegerList.size() > 0)
                    {
                      StringBuilder originValueStringBuilder = new StringBuilder();
                      for (int originValue2 : nbtIntegerList)
                      {
                        originValueStringBuilder.append(originValue2).append(",");
                      }
                      String originValueString = originValueStringBuilder.toString();
                      originValueString = originValueString.substring(0, originValueString.length() - 1);
                      return Method.tabCompleterList(args, "<값>", true, "<값>", "예시 : 1,2,3", originValueString, originValueString + "(기본값)");
                    }
                    return Method.tabCompleterList(args, "<값>", true, "<값>", "예시 : 1,2,3");
                  case "long":
                    if (input.startsWith(","))
                    {
                      return Collections.singletonList("정수가 필요합니다. (" + input + ")");
                    }
                    split = input.split(",");
                    if (!input.equals(""))
                    {
                      for (String s2 : split)
                      {
                        if (!MessageUtil.isLong(sender, s2, false))
                        {
                          return Collections.singletonList("정수가 필요합니다. (" + s2 + ")");
                        }
                      }
                    }
                    exists = nbtItem.hasKey(key) && nbtItem.getType(key) == NBTType.NBTTagLong;
                    long l = nbtItem.getLong(key);
                    return Method.tabCompleterLongRadius(args, Long.MIN_VALUE, Long.MAX_VALUE, "<값>", exists ? l + "(기존값)" : "");
                  case "long-list":
                    if (input.startsWith(","))
                    {
                      return Collections.singletonList("정수가 필요합니다. (" + input + ")");
                    }
                    split = input.split(",");
                    if (!input.equals(""))
                    {
                      for (String s2 : split)
                      {
                        if (!MessageUtil.isLong(sender, s2, false))
                        {
                          return Collections.singletonList("정수가 필요합니다. (" + s2 + ")");
                        }
                      }
                    }
                    NBTList<Long> nbtLongList = nbtItem.getLongList(key);
                    if (nbtLongList != null && nbtLongList.size() > 0)
                    {
                      StringBuilder originValueStringBuilder = new StringBuilder();
                      for (long originValue2 : nbtLongList)
                      {
                        originValueStringBuilder.append(originValue2).append(",");
                      }
                      String originValueString = originValueStringBuilder.toString();
                      originValueString = originValueString.substring(0, originValueString.length() - 1);
                      return Method.tabCompleterList(args, "<값>", true, "<값>", "예시 : 1,2,3", originValueString, originValueString + "(기본값)");
                    }
                    return Method.tabCompleterList(args, "<값>", true, "<값>", "예시 : 1,2,3");
                  case "float":
                    exists = nbtItem.hasKey(key) && nbtItem.getType(key) == NBTType.NBTTagFloat;
                    float f = nbtItem.getFloat(key);
                    return Method.tabCompleterDoubleRadius(args, -Float.MAX_VALUE, Float.MAX_VALUE, "<값>", exists ? f + "(기존값)" : "");
                  case "float-list":
                    if (input.startsWith(","))
                    {
                      return Collections.singletonList("숫자가 필요합니다. (" + input + ")");
                    }
                    split = input.split(",");
                    if (!input.equals(""))
                    {
                      for (String s2 : split)
                      {
                        if (!MessageUtil.isDouble(sender, s2, false))
                        {
                          return Collections.singletonList("숫자가 필요합니다. (" + s2 + ")");
                        }
                        double j = Double.parseDouble(s2);
                        if (j > Float.MAX_VALUE)
                        {
                          String valueString = Constant.Sosu15.format(j);
                          valueString += MessageUtil.getFinalConsonant(valueString, MessageUtil.ConsonantType.이가);
                          return Collections.singletonList("숫자는 " + Constant.Sosu15.format(Byte.MAX_VALUE) + " 이하여야 하는데, " + valueString + " 있습니다.");
                        }
                        else if (j < -Float.MAX_VALUE)
                        {
                          String valueString = Constant.Sosu15.format(j);
                          valueString += MessageUtil.getFinalConsonant(valueString, MessageUtil.ConsonantType.이가);
                          return Collections.singletonList("숫자는 " + Constant.Sosu15.format(Byte.MIN_VALUE) + " 이상이여야 하는데, " + valueString + " 있습니다.");
                        }
                      }
                    }
                    NBTList<Float> nbtFloatList = nbtItem.getFloatList(key);
                    if (nbtFloatList != null && nbtFloatList.size() > 0)
                    {
                      StringBuilder originValueStringBuilder = new StringBuilder();
                      for (float originValue2 : nbtFloatList)
                      {
                        originValueStringBuilder.append(originValue2).append(",");
                      }
                      String originValueString = originValueStringBuilder.toString();
                      originValueString = originValueString.substring(0, originValueString.length() - 1);
                      return Method.tabCompleterList(args, "<값>", true, "<값>", "예시 : 1.5,2.5,3.5", originValueString, originValueString + "(기본값)");
                    }
                    return Method.tabCompleterList(args, "<값>", true, "<값>", "예시 : 1.5,2.5,3.5");
                  case "double":
                    exists = nbtItem.hasKey(key) && nbtItem.getType(key) == NBTType.NBTTagDouble;
                    double d = nbtItem.getDouble(key);
                    return Method.tabCompleterDoubleRadius(args, -Double.MAX_VALUE, Double.MAX_VALUE, "<값>", exists ? d + "(기존값)" : "");
                  case "double-list":
                    if (input.startsWith(","))
                    {
                      return Collections.singletonList("숫자가 필요합니다. (" + input + ")");
                    }
                    split = input.split(",");
                    if (!input.equals(""))
                    {
                      for (String s2 : split)
                      {
                        if (!MessageUtil.isDouble(sender, s2, false))
                        {
                          return Collections.singletonList("숫자가 필요합니다. (" + s2 + ")");
                        }
                      }
                    }
                    NBTList<Double> nbtDoubleList = nbtItem.getDoubleList(key);
                    if (nbtDoubleList != null && nbtDoubleList.size() > 0)
                    {
                      StringBuilder originValueStringBuilder = new StringBuilder();
                      for (double originValue2 : nbtDoubleList)
                      {
                        originValueStringBuilder.append(originValue2).append(",");
                      }
                      String originValueString = originValueStringBuilder.toString();
                      originValueString = originValueString.substring(0, originValueString.length() - 1);
                      return Method.tabCompleterList(args, "<값>", true, "<값>", "예시 : 1.5,2.5,3.5", originValueString, originValueString + "(기본값)");
                    }
                    return Method.tabCompleterList(args, "<값>", true, "<값>", "예시 : 1.5,2.5,3.5");
                  case "uuid":
                    exists = nbtItem.hasKey(key) && nbtItem.getType(key) == NBTType.NBTTagIntArray;
                    try
                    {
                      UUID uuid = nbtItem.getUUID(key);
                      return Method.tabCompleterList(args, "<값>", true, exists ?
                              new String[]{uuid.toString(), uuid.toString() + "(기존값)", "<값>", player.getUniqueId().toString(), player.getUniqueId().toString() + "(플레이어 UUID)"}
                              : new String[]{"<값>", player.getUniqueId().toString() + "(플레이어 UUID)"});
                    }
                    catch (Exception e)
                    {
                      return Method.tabCompleterList(args, "<값>", true, "<값>", player.getUniqueId().toString() + "(플레이어 UUID)");
                    }
                  case "string":
                    exists = nbtItem.hasKey(key) && nbtItem.getType(key) == NBTType.NBTTagString;
                    String str = nbtItem.getString(key);
                    return Method.tabCompleterList(args, "<값>", true, exists ? new String[]{str.replace("§", "&"), "<값>"} : new String[]{"<값>"});
                  case "string-list":
                    NBTList<String> nbtStringList = nbtItem.getStringList(key);
                    if (nbtStringList != null && nbtStringList.size() > 0)
                    {
                      StringBuilder originValueStringBuilder = new StringBuilder();
                      for (String originValue2 : nbtStringList)
                      {
                        originValueStringBuilder.append(originValue2).append(";;");
                      }
                      String originValueString = originValueStringBuilder.toString();
                      originValueString = originValueString.substring(0, originValueString.length() - 2);
                      return Method.tabCompleterList(args, "<값>", true, "<값>", "예시 : 문장1;;문장2;;문장3", originValueString, originValueString + "(기본값)");
                    }
                    return Method.tabCompleterList(args, "<값>", true, "<값>", "예시 : 문장1;;문장2;;문장3");
                  case "compound":
                  case "compound-list":
                    break;
                  default:
                    return Collections.singletonList(args[2] + MessageUtil.getFinalConsonant(args[2], MessageUtil.ConsonantType.은는) + " 잘못되거나 알 수 없는 자료형입니다.");
                }
              }
              break;
          }
          break;
      }

      switch (args[0])
      {
        case "customitemtype":
          if (length == 2)
          {
            return Method.tabCompleterList(args, "<아이템 종류>", true, "<아이템 종류>", "--remove");
          }
          return Method.tabCompleterList(args, "[아이템 종류]", true);
        case "customlore":
          switch (args[1])
          {
            case "add":
              if (length == 3)
              {
                return Method.tabCompleterList(args, "<커스텀 설명>", true, "<커스텀 설명>", "--empty");
              }
              return Method.tabCompleterList(args, "[커스텀 설명]", true);
            case "set":
            {
              if (length == 3)
              {
                return Method.tabCompleterIntegerRadius(args, 1, Integer.MAX_VALUE, "<줄>");
              }
              NBTList<String> customLore = NBTAPI.getStringList(NBTAPI.getMainCompound(item), CucumberyTag.CUSTOM_LORE_KEY);
              try
              {
                int input = Integer.parseInt(args[2]);
                String lore = customLore.get(input - 1);
                if (args[3].equals("") && customLore.size() >= input && input > 0)
                {
                  return Method.tabCompleterList(args, "<커스텀 설명>", true, "<커스텀 설명>", "--empty", lore.replace("§", "&"));
                }
              }
              catch (Exception ignored)
              {

              }
              if (length == 4)
              {
                return Method.tabCompleterList(args, "<커스텀 설명>", true, "<커스텀 설명>", "--empty");
              }
              return Method.tabCompleterList(args, "[커스텀 설명]", true);
            }
            case "remove":
            {
              {
                if (length == 3)
                {
                  NBTList<String> customLore = NBTAPI.getStringList(NBTAPI.getMainCompound(item), CucumberyTag.CUSTOM_LORE_KEY);
                  if (customLore == null || customLore.size() == 0)
                  {
                    return Collections.singletonList("더 이상 제거할 수 있는 커스텀 설명이 없습니다.");
                  }
                  return Method.tabCompleterIntegerRadius(args, 1, customLore.size(), "[줄]", "--all");
                }
              }
              break;
            }
            case "insert":
            {
              NBTList<String> customLore = NBTAPI.getStringList(NBTAPI.getMainCompound(item), CucumberyTag.CUSTOM_LORE_KEY);
              if (customLore == null || customLore.size() == 0)
              {
                return Collections.singletonList("커스텀 설명을 들여쓸 수 없습니다.");
              }
              if (length == 3)
              {
                return Method.tabCompleterIntegerRadius(args, 1, customLore.size(), "<줄>");
              }
              if (length == 4)
              {
                return Method.tabCompleterList(args, "<커스텀 설명>", true, "<커스텀 설명>", "--empty");
              }
              return Method.tabCompleterList(args, "[커스텀 설명]", true);
            }
          }
          break;
        case "abovecustomlore":
          switch (args[1])
          {
            case "add":
              if (length == 3)
              {
                return Method.tabCompleterList(args, "<상단 커스텀 설명>", true, "<상단 커스텀 설명>", "--empty");
              }
              return Method.tabCompleterList(args, "[상단 커스텀 설명]", true);
            case "set":
            {
              if (length == 3)
              {
                return Method.tabCompleterIntegerRadius(args, 1, Integer.MAX_VALUE, "<줄>");
              }
              NBTList<String> customLore = NBTAPI.getStringList(NBTAPI.getMainCompound(item), CucumberyTag.ABOVE_CUSTOM_LORE_KEY);
              try
              {
                int input = Integer.parseInt(args[2]);
                String lore = customLore.get(input - 1);
                if (args[3].equals("") && customLore.size() >= input && input > 0)
                {
                  return Method.tabCompleterList(args, "<상단 커스텀 설명>", true, "<상단 커스텀 설명>", "--empty", lore.replace("§", "&"));
                }
              }
              catch (Exception ignored)
              {

              }
              if (length == 4)
              {
                return Method.tabCompleterList(args, "<상단 커스텀 설명>", true, "<상단 커스텀 설명>", "--empty");
              }
              return Method.tabCompleterList(args, "[상단 커스텀 설명]", true);
            }
            case "remove":
            {
              {
                if (length == 3)
                {
                  NBTList<String> customLore = NBTAPI.getStringList(NBTAPI.getMainCompound(item), CucumberyTag.ABOVE_CUSTOM_LORE_KEY);
                  if (customLore == null || customLore.size() == 0)
                  {
                    return Collections.singletonList("더 이상 제거할 수 있는 상단 커스텀 설명이 없습니다.");
                  }
                  return Method.tabCompleterIntegerRadius(args, 1, customLore.size(), "[줄]", "--all");
                }
              }
              break;
            }
            case "insert":
            {
              NBTList<String> customLore = NBTAPI.getStringList(NBTAPI.getMainCompound(item), CucumberyTag.ABOVE_CUSTOM_LORE_KEY);
              if (customLore == null || customLore.size() == 0)
              {
                return Collections.singletonList("상단 커스텀 설명을 들여쓸 수 없습니다.");
              }
              if (length == 3)
              {
                return Method.tabCompleterIntegerRadius(args, 1, customLore.size(), "<줄>");
              }
              if (length == 4)
              {
                return Method.tabCompleterList(args, "<상단 커스텀 설명>", true, "<상단 커스텀 설명>", "--empty");
              }
              return Method.tabCompleterList(args, "[상단 커스텀 설명]", true);
            }
          }
          break;
        case "expiredate":
          if (args.length == 2)
          {
            Calendar calendar = Calendar.getInstance();
            List<String> list = new ArrayList<>();
            list.add("--remove");
            list.addAll(Arrays.asList("~1분", "~10분", "~1시간", "~1일", "~7일", "~14일", "~21일", "~30일", "~1년"));
            calendar.add(Calendar.MINUTE, 1);
            list.add(Method.getCurrentTime(calendar, true, false));
            calendar.add(Calendar.MINUTE, 10);
            list.add(Method.getCurrentTime(calendar, true, false));
            calendar.add(Calendar.HOUR, 1);
            list.add(Method.getCurrentTime(calendar, true, false));
            calendar.add(Calendar.DATE, 1);
            list.add(Method.getCurrentTime(calendar, true, false));
            calendar.add(Calendar.DATE, 7);
            list.add(Method.getCurrentTime(calendar, true, false));
            calendar.add(Calendar.DATE, 14);
            list.add(Method.getCurrentTime(calendar, true, false));
            calendar.add(Calendar.DATE, 21);
            list.add(Method.getCurrentTime(calendar, true, false));
            calendar.add(Calendar.DATE, 30);
            list.add(Method.getCurrentTime(calendar, true, false));
            calendar.add(Calendar.DATE, 365);
            list.add(Method.getCurrentTime(calendar, true, false));
            return Method.tabCompleterList(args, list, "<기간>", true);
          }
          return Method.tabCompleterList(args, "[<기간>]", true);
        case "usage":
        {
          if ("command".equals(args[1]))
          {
            ItemUsageType itemUsageType;
            try
            {
              itemUsageType = ItemUsageType.valueOf(args[2].toUpperCase());
            }
            catch (Exception e)
            {
              return Collections.singletonList(args[2] + MessageUtil.getFinalConsonant(args[2], MessageUtil.ConsonantType.은는) + " 잘못되거나 알 수 없는 실행 유형입니다.");
            }
            NBTCompound usageTag = NBTAPI.getCompound(NBTAPI.getMainCompound(item), CucumberyTag.USAGE_KEY);
            NBTList<String> commands = NBTAPI.getStringList(NBTAPI.getCompound(usageTag, itemUsageType.getKey()), CucumberyTag.USAGE_COMMANDS_KEY);
            switch (args[3])
            {
              case "set":
                if (length == 5)
                {
                  return Method.tabCompleterIntegerRadius(args, 1, Integer.MAX_VALUE, "<줄>");
                }
                break;
              case "remove":
              {
                if (length == 5)
                {
                  if (commands == null || commands.size() == 0)
                  {
                    return Collections.singletonList("아이템에 " + itemUsageType.getDisplay() + " 시 명령어 실행 태그 값이 없습니다.");
                  }
                  return Method.tabCompleterIntegerRadius(args, 1, commands.size(), "[줄]", "--all");
                }
                break;
              }
              case "insert":
              {
                if (commands == null || commands.size() == 0)
                {
                  return Collections.singletonList("아이템에 " + itemUsageType.getDisplay() + " 시 명령어 실행 태그 값이 없습니다.");
                }
                if (length == 5)
                {
                  return Method.tabCompleterIntegerRadius(args, 1, commands.size(), "<줄>");
                }
              }
              break;
            }
            break;
          }
          break;
        }
        case "nbt":
          if (args.length >= 3 && args[1].equals("merge"))
          {
            String input = MessageUtil.listToString(" ", 2, args.length, args);
            if (!input.equals(""))
            {
              try
              {
                new NBTContainer(input);
              }
              catch (Exception e)
              {
                return Collections.singletonList(input + MessageUtil.getFinalConsonant(input, MessageUtil.ConsonantType.은는) + " 잘못된 nbt입니다.");
              }
            }
            if (args.length == 3)
            {
              return Method.tabCompleterList(args, "<nbt>", true, "<nbt>", "예시 : {foo:bar,nbt:{extra:\"nbts\"}}");
            }
            return Method.tabCompleterList(args, "[nbt]", true);
          }
          if (args.length >= 5 && args[1].equals("set"))
          {
            switch (args[2])
            {
              case "string":
              case "string-list":
                return Method.tabCompleterList(args, "[값]", true);
              case "compound":
                String input = MessageUtil.listToString(" ", 4, args.length, args);
                if (!input.equals(""))
                {
                  try
                  {
                    new NBTContainer(input);
                  }
                  catch (Exception e)
                  {
                    return Collections.singletonList(input + MessageUtil.getFinalConsonant(input, MessageUtil.ConsonantType.은는) + " 잘못된 nbt입니다.");
                  }
                }
                String key = args[3];
                NBTItem nbtItem = new NBTItem(item);
                boolean exists = nbtItem.hasKey(key) && nbtItem.getType(key) == NBTType.NBTTagCompound;
                NBTCompound nbtCompound = nbtItem.getCompound(key);
                if (exists && nbtCompound.toString().length() < 100)
                {
                  return Method.tabCompleterList(args, "<nbt>", true, "<nbt>", "예시 : {foo:bar,nbt:{extra:\"nbts\"}}", nbtCompound.toString(), nbtCompound.toString() + "(기본값)");
                }
                if (args.length == 5)
                {
                  return Method.tabCompleterList(args, "<nbt>", true, "<nbt>", "예시 : {foo:bar,nbt:{extra:\"nbts\"}}");
                }
                return Method.tabCompleterList(args, "[nbt]", true);
              case "compound-list":
                input = MessageUtil.listToString(" ", 4, args.length, args);
                String[] split = input.split(";;");
                if (!input.equals(""))
                {
                  for (String compoundString : split)
                  {
                    try
                    {
                      new NBTContainer(compoundString);
                    }
                    catch (Exception e)
                    {
                      return Collections.singletonList(compoundString + MessageUtil.getFinalConsonant(compoundString, MessageUtil.ConsonantType.은는) + " 잘못된 nbt입니다.");
                    }
                  }
                }
                key = args[3];
                nbtItem = new NBTItem(item);
                NBTCompoundList nbtCompoundList = nbtItem.getCompoundList(key);
                if (nbtCompoundList != null && nbtCompoundList.size() > 0)
                {
                  StringBuilder originValueStringBuilder = new StringBuilder();
                  for (NBTCompound originValue2 : nbtCompoundList)
                  {
                    originValueStringBuilder.append(originValue2.toString()).append(";;");
                  }
                  String originValueString = originValueStringBuilder.toString();
                  originValueString = originValueString.substring(0, originValueString.length() - 2);
                  return Method.tabCompleterList(args, "<nbt>", true, "<nbt>", "예시 : {foo:bar};;{wa:sans};;{third:list}", originValueString, originValueString + "(기본값)");
                }
                if (args.length == 5)
                {
                  return Method.tabCompleterList(args, "<nbt>", true, "<nbt>", "예시 : {foo:bar};;{wa:sans};;{third:list}");
                }
                return Method.tabCompleterList(args, "[nbt]", true);
            }
          }
      }

      if (length >= 3 && args[0].equals("food") && args[1].equals("nourishment"))
      {
        if (args.length == 3)
        {
          return Method.tabCompleterList(args, "<든든함>", true, "<든든함>", "--remove", "#57B6F0;공허", "#47B6F0;허-전", "#16F06C;낮음", "#F0CA4F;보통", "#F05C48;높음", "#E553F0;든-든");
        }
        return Method.tabCompleterList(args, "[든든함]", true);
      }

      if (length >= 5 && args[0].equals("usage") && args[1].equals("command"))
      {
        if (args[3].equals("add") || args[3].equals("insert") || args[3].equals("set"))
        {
          int argLength = 5;
          boolean insertOrSet = args[3].equals("insert") || args[3].equals("set");
          if (insertOrSet)
          {
            argLength++;
          }
          if (insertOrSet)
          {
            if (!MessageUtil.isInteger(sender, args[4], false))
            {
              return Collections.singletonList(args[4] + MessageUtil.getFinalConsonant(args[4], MessageUtil.ConsonantType.은는) + " 정수가 아닙니다.");
            }
            else if (!MessageUtil.checkNumberSize(sender, Integer.parseInt(args[4]), 1, Integer.MAX_VALUE, false))
            {
              return Collections.singletonList("정수는 1 이상이여야 하는데, " + args[4] + MessageUtil.getFinalConsonant(args[4], MessageUtil.ConsonantType.이가) + " 있습니다.");
            }
          }
          if (length == argLength)
          {
            List<String> cmds = Method.getAllServerCommands();
            List<String> newCmds = new ArrayList<>();
            ItemUsageType itemUsageType;
            try
            {
              itemUsageType = ItemUsageType.valueOf(args[2].toUpperCase());
              NBTCompound usageTag = NBTAPI.getCompound(NBTAPI.getMainCompound(item), CucumberyTag.USAGE_KEY);
              NBTList<String> commands = NBTAPI.getStringList(NBTAPI.getCompound(usageTag, itemUsageType.getKey()), CucumberyTag.USAGE_COMMANDS_KEY);
              int input = Integer.parseInt(args[4]);
              String command = commands.get(input - 1);
              if (args[5].equals("") && commands.size() >= input && input > 0)
              {
                newCmds.add(command);
                return Method.tabCompleterList(args, newCmds, "<명령어>", true);
              }
            }
            catch (Exception ignored)
            {

            }
            newCmds.add("chat:" + "<채팅 메시지>");
            newCmds.add("opchat:" + "<오피 권한으로 채팅 메시지>");
            newCmds.add("chat:/" + "<채팅 명령어>");
            newCmds.add("opchat:/" + "<오피 권한으로 채팅 명령어>");
            for (String cmd2 : cmds)
            {
              newCmds.add(cmd2);
              newCmds.add("chat:/" + cmd2);
              newCmds.add("op:" + cmd2);
              newCmds.add("opchat:/" + cmd2);
              newCmds.add("console:" + cmd2);
            }
            List<String> list = new ArrayList<>(Method.tabCompleterList(args, Variable.commandPacks.keySet(), "<명령어 팩 파일>", true));
            for (int i = 0; i < list.size(); i++)
            {
              String fileName = list.get(i);
              newCmds.add("commandpack:" + fileName);
              list.set(i, "commandpack:" + fileName);
            }
            if (lastArg.startsWith("commandpack:"))
            {
              if (Variable.commandPacks.size() == 0)
              {
                return Collections.singletonList("유효한 명령어 팩 파일이 존재하지 않습니다.");
              }
              args[args.length - 1] = lastArg.substring(12);
              return Method.tabCompleterList(args, list, "<명령어 팩 파일>");
            }
            return Method.tabCompleterList(args, newCmds, "<명령어>", true);
          }
          else
          {
            String cmdLabel = args[argLength - 1];
            if (cmdLabel.startsWith("commandpack:"))
            {
              cmdLabel = cmdLabel.substring(12);
              if (Variable.commandPacks.size() == 0)
              {
                return Collections.singletonList("유효한 명령어 팩 파일이 존재하지 않습니다.");
              }
              YamlConfiguration config = Variable.commandPacks.get(cmdLabel);
              if (config == null)
              {
                return Collections.singletonList(cmdLabel + MessageUtil.getFinalConsonant(cmdLabel, MessageUtil.ConsonantType.은는) + " 잘못되거나 알 수 없는 명령어 팩 파일입니다.");
              }
              return Method.tabCompleterList(args, config.getKeys(false), "<명령어 팩>");
            }
            if (cmdLabel.startsWith("op:"))
            {
              cmdLabel = cmdLabel.substring(3);
            }
            if (cmdLabel.startsWith("chat:/"))
            {
              cmdLabel = cmdLabel.substring(6);
            }
            else if (cmdLabel.startsWith("chat:"))
            {
              return Collections.singletonList("[메시지]");
            }
            if (cmdLabel.startsWith("opchat:/"))
            {
              cmdLabel = cmdLabel.substring(8);
            }
            else if (cmdLabel.startsWith("opchat:"))
            {
              return Collections.singletonList("[메시지]");
            }
            if (cmdLabel.startsWith("console:"))
            {
              cmdLabel = cmdLabel.substring(8);
            }
            if (length == argLength + 1 && (cmdLabel.equals("?") || cmdLabel.equals("bukkit:?") || cmdLabel.equals("bukkit:help")))
            {
              return Method.tabCompleterList(args, Method.getAllServerCommands(), "<명령어>");
            }
            PluginCommand command = Bukkit.getServer().getPluginCommand(cmdLabel);
            String[] args2 = new String[length - argLength];
            System.arraycopy(args, argLength, args2, 0, length - argLength);
            if (command != null)
            {
              org.bukkit.command.TabCompleter completer = command.getTabCompleter();
              if (completer != null)
              {
                return completer.onTabComplete(sender, command, command.getLabel(), args2);
              }
            }
            return Collections.singletonList("[<인수>]");
          }
        }
      }
    }
    else if (name.equals("customrecipe") && Method.hasPermission(sender, Permission.CMD_CUSTOMRECIPE_ADMIN, false))
    {
      switch (length)
      {
        case 1:
          return Method.tabCompleterList(args, "<인수>", "open", "create", "remove", "edit");
        case 2:
          switch (args[0])
          {
            case "open":
            case "remove":
              if (Variable.customRecipes.size() == 0)
              {
                return Collections.singletonList((args[0].equals("open") ? "열" : "제거할") + " 수 있는 유효한 레시피 목록이 존재하지 않습니다.");
              }
              return Method.tabCompleterList(args, Variable.customRecipes.keySet(), "<레시피 목록>");
            case "create":
              if (Method2.isInvalidFileName(args[1]))
              {
                return Collections.singletonList(args[1] + MessageUtil.getFinalConsonant(args[1], MessageUtil.ConsonantType.은는) + " 유효하지 않은 레시피 목록 이름입니다.");
              }
              if (Variable.customRecipes.size() == 0)
              {
                return Method.tabCompleterList(args, "<새로운 레시피 목록>", true);
              }
              return Method.tabCompleterList(args, Variable.customRecipes.keySet(), (Variable.customRecipes.containsKey(lastArg) ? "<편집할 레시피 목록>" : "<새로운 레시피 목록>"), true);
            case "edit":
              return Method.tabCompleterList(args, "<인수>", "category", "recipe");
          }
          break;
        case 3:
          switch (args[0])
          {
            case "open":
            case "remove":
            {
              YamlConfiguration config = Variable.customRecipes.get(args[1]);
              if (config == null)
              {
                return Collections.singletonList(args[1] + MessageUtil.getFinalConsonant(args[1], MessageUtil.ConsonantType.은는) + " 잘못되거나 알 수 없는 레시피 목록입니다.");
              }
              ConfigurationSection recipes = config.getConfigurationSection("recipes");
              if (recipes == null)
              {
                return Collections.singletonList((args[0].equals("open") ? "열" : "제거할") + " 수 있는 유효한 레시피가 존재하지 않습니다.");
              }
              return Method.tabCompleterList(args, recipes.getKeys(false), "<레시피>");
            }
            case "create":
            {
              YamlConfiguration config = Variable.customRecipes.get(args[1]);
              if (config == null)
              {
                return Method.tabCompleterList(args, "<새로운 레시피>", true);
              }
              ConfigurationSection recipes = config.getConfigurationSection("recipes");
              if (recipes == null || recipes.getKeys(false).size() == 0)
              {
                return Method.tabCompleterList(args, "<새로운 레시피>", true);
              }
              return Method.tabCompleterList(args, recipes.getKeys(false), (recipes.getKeys(false).contains(lastArg) ? "<편집할 레시피>" : "<새로운 레시피>"), true);
            }
            case "edit":
              switch (args[1])
              {
                case "category":
                case "recipe":
                  if (Variable.customRecipes.size() == 0)
                  {
                    return Collections.singletonList("유효한 레시피 목록이 없습니다.");
                  }
                  return Method.tabCompleterList(args, Variable.customRecipes.keySet(), "<레시피 목록>");
              }
              break;
          }
          break;
        case 4:
          switch (args[0])
          {
            case "open":
              return Method.tabCompleterPlayer(sender, args);
            case "edit":
              switch (args[1])
              {
                case "category":
                  return Method.tabCompleterList(
                          args, "<인수>", "permission", "wealth", "level", "require", "statistic", "hp", "mhp", "maxplayer", "foodlevel", "saturation", "display", "displayitem", "biome", "belowblock");
                case "recipe":
                  YamlConfiguration config = Variable.customRecipes.get(args[2]);
                  if (config == null)
                  {
                    return Collections.singletonList(args[2] + MessageUtil.getFinalConsonant(args[2], MessageUtil.ConsonantType.은는) + " 잘못되거나 알 수 없는 레시피 목록입니다.");
                  }
                  ConfigurationSection recipes = config.getConfigurationSection("recipes");
                  if (recipes == null)
                  {
                    return Collections.singletonList(args[2] + " 레시피 목록에는 유효한 레시피가 없습니다.");
                  }
                  return Method.tabCompleterList(args, recipes.getKeys(false), "<레시피>");
              }
              break;
          }
          break;
        case 5:
          if ("edit".equals(args[0]))
          {
            switch (args[1])
            {
              case "category":
                switch (args[3])
                {
                  case "permission":
                    return Method.tabCompleterList(args, "<인수>", "base", "bypass", "hide");
                  case "require":
                    return Method.tabCompleterList(args, "<제작 횟수 유형>", "recipe", "category");
                  case "statistic":
                    return Method.tabCompleterList(args, "<통계 유형>", "general", "entity", "material");
                  case "displayitem":
                    boolean holdingItem = false;
                    String hand = "hand";
                    if (!(sender instanceof Player))
                    {
                      hand = "hand(플레이어만 사용 가능)";
                    }
                    else
                    {
                      Player player = (Player) sender;
                      holdingItem = ItemStackUtil.itemExists(player.getInventory().getItemInMainHand());
                      if (!holdingItem)
                      {
                        hand = "hand(손에 아이템 없음)";
                      }
                    }
                    if (!holdingItem && lastArg.equals("hand"))
                    {
                      return Collections.singletonList(Prefix.NO_HOLDING_ITEM.toString());
                    }
                    return Method.tabCompleterList(args, "<표시 아이템 출처>", hand, "material", "remove");
                  case "biome":
                    return Method.tabCompleterList(args, Method.addAll(Biome.values(), "--remove"), "<생물 군계>");
                  case "belowblock":
                    List<String> list = new ArrayList<>();
                    for (Material material : Material.values())
                    {
                      if (material.isBlock())
                      {
                        list.add(material.toString().toLowerCase());
                      }
                    }
                    return Method.tabCompleterList(args, Method.addAll(list, "--remove"), "<블록>");
                  case "display":
                    if (length == 5)
                    {
                      return Method.tabCompleterList(args, "<레시피 목록 표시 이름>", true, "<레시피 목록 표시 이름>", "--remove");
                    }
                    return Method.tabCompleterList(args, "<레시피 목록 표시 이름>", true);
                  case "foodlevel":
                    return Method.tabCompleterIntegerRadius(args, 0, 20, "<현재 음식 포인트의 최소 조건>", "-1");
                  case "level":
                    return Method.tabCompleterIntegerRadius(args, 0, Integer.MAX_VALUE, "<현재 레벨의 최소 조건>", "-1");
                  case "maxplayer":
                    return Method.tabCompleterIntegerRadius(args, 0, Integer.MAX_VALUE, "<현재 접속 중인 플레이어 수의 최소 조건>", "-1");
                  case "hp":
                    return Method.tabCompleterDoubleRadius(args, 0, Double.MAX_VALUE, "<현재 HP의 최소 조건>", "-1");
                  case "mhp":
                    return Method.tabCompleterDoubleRadius(args, 0, Double.MAX_VALUE, "<현재 순수 최대 HP의 최소 조건>", "-1");
                  case "saturation":
                    return Method.tabCompleterDoubleRadius(args, 0, 20, "<현재 포화도의 최소 조건>", "-1");
                  case "wealth":
                    if (!Cucumbery.using_Vault)
                    {
                      return Collections.singletonList("Vault 플러그인을 사용하고 있지 않습니다.");
                    }
                    return Method.tabCompleterDoubleRadius(args, 0, Double.MAX_VALUE, "<현재 소지 금액의 최소 조건>", "-1");
                }
                break;
              case "recipe":
                List<String> list = new ArrayList<>(Method.tabCompleterList(args, "<인수>", "permission", "chance", "reusable", "cost", "wealth", "levelcost", "level", "display", "require", "statistic",
                        "hp", "mhp", "maxplayer", "foodlevel", "saturation", "hpcost", "mhpcost", "foodlevelcost", "saturationcost", "craftingtime",
                        "command", "biome", "belowblock"));
                if (!Cucumbery.using_Vault)
                {
                  list.remove("cost");
                  list.remove("wealth");
                  if (Method.equals(lastArg, "cost", "wealth"))
                  {
                    return Collections.singletonList("Vault 플러그인을 사용하고 있지 않습니다.");
                  }
                }
                return list;
            }
          }
          break;
        case 6:
          if ("edit".equals(args[0]))
          {
            switch (args[1])
            {
              case "category":
                switch (args[3])
                {
                  case "permission":
                    switch (args[4])
                    {
                      case "hide":
                        return Method.tabCompleterBoolean(args, "<권한 부족 시 숨김 여부>");
                      case "bypass":
                      case "base":
                        return Method.tabCompleterList(args, "<퍼미션 노드>", true, "<퍼미션 노드>", "--remove");
                    }
                    break;
                  case "require":
                    switch (args[4])
                    {
                      case "recipe":
                      case "category":
                        return Method.tabCompleterList(args, Variable.customRecipes.keySet(), "<레시피 목록>");
                    }
                    break;
                  case "statistic":
                    return Method.tabCompleterStatistics(args, args[4], "<통계>");
                  case "displayitem":
                    if ("material".equals(args[4]))
                    {
                      List<String> list = new ArrayList<>();
                      for (Material material : Material.values())
                      {
                        if (material.isItem() && material != Material.AIR)
                        {
                          list.add(material.toString().toLowerCase());
                        }
                      }
                      return Method.tabCompleterList(args, list, "<아이템>");
                    }
                    break;
                  case "foodlevel":
                  {
                    int min = 0;
                    String minStr = args[4];
                    if (MessageUtil.isInteger(sender, minStr, false))
                    {
                      min = Math.max(0, Integer.parseInt(minStr));
                    }
                    return Method.tabCompleterIntegerRadius(args, min, 20, "[현재 음식 포인트의 최대 조건]", "-1", min + "");
                  }
                  case "level":
                  {
                    int min = 0;
                    String minStr = args[4];
                    if (MessageUtil.isInteger(sender, minStr, false))
                    {
                      min = Math.max(0, Integer.parseInt(minStr));
                    }
                    return Method.tabCompleterIntegerRadius(args, min, Integer.MAX_VALUE, "[현재 레벨의 최대 조건]", "-1", min + "");
                  }
                  case "maxplayer":
                  {
                    int min = 0;
                    String minStr = args[4];
                    if (MessageUtil.isInteger(sender, minStr, false))
                    {
                      min = Math.max(0, Integer.parseInt(minStr));
                    }
                    return Method.tabCompleterIntegerRadius(args, min, Integer.MAX_VALUE, "[현재 접속 중인 플레이어 수의 최대 조건]", "-1", min + "");
                  }
                  case "hp":
                  {
                    double min = 0;
                    String minStr = args[4];
                    if (MessageUtil.isDouble(sender, minStr, false))
                    {
                      min = Math.max(0, Double.parseDouble(minStr));
                    }
                    return Method.tabCompleterDoubleRadius(args, min, Double.MAX_VALUE, "[현재 HP의 최대 조건]", "-1", min + "");
                  }
                  case "mhp":
                  {
                    double min = 0;
                    String minStr = args[4];
                    if (MessageUtil.isDouble(sender, minStr, false))
                    {
                      min = Math.max(0, Double.parseDouble(minStr));
                    }
                    return Method.tabCompleterDoubleRadius(args, min, Double.MAX_VALUE, "[현재 순수 최대 HP의 최대 조건]", "-1", min + "");
                  }
                  case "saturation":
                  {
                    double min = 0;
                    String minStr = args[4];
                    if (MessageUtil.isDouble(sender, minStr, false))
                    {
                      min = Math.max(0, Double.parseDouble(minStr));
                    }
                    return Method.tabCompleterDoubleRadius(args, min, 20, "[현재 포화도의 최대 조건]", "-1", min + "");
                  }
                  case "wealth":
                  {
                    if (!Cucumbery.using_Vault)
                    {
                      return Collections.singletonList("Vault 플러그인을 사용하고 있지 않습니다.");
                    }
                    double min = 0;
                    String minStr = args[4];
                    if (MessageUtil.isDouble(sender, minStr, false))
                    {
                      min = Math.max(0, Double.parseDouble(minStr));
                    }
                    return Method.tabCompleterDoubleRadius(args, min, Double.MAX_VALUE, "[현재 소지 금액의 최대 조건]", "-1", min + "");
                  }
                }
                break;
              case "recipe":
                switch (args[4])
                {
                  case "permission":
                    return Method.tabCompleterList(args, "<인수>", "base", "bypass", "hide");
                  case "reusable":
                  {
                    YamlConfiguration config = Variable.customRecipes.get(args[2]);
                    if (config == null)
                    {
                      return Collections.singletonList(args[2] + MessageUtil.getFinalConsonant(args[2], MessageUtil.ConsonantType.은는) + " 잘못되거나 알 수 없는 레시피입니다.");
                    }
                    ConfigurationSection ingredients = config.getConfigurationSection("recipes." + args[3] + ".ingredients");
                    if (ingredients == null)
                    {
                      return Collections.singletonList(args[2] + " 레시피는 손상된 레시피입니다.");
                    }
                    Set<String> keys = ingredients.getKeys(false);
                    List<String> list = new ArrayList<>();
                    int i = 1;
                    for (String key : keys)
                    {
                      String ingredientName = ComponentUtil.itemName(ItemSerializer.deserialize(ingredients.getString(key + ".item"))).toString();
                      list.add(i + " : " + MessageUtil.stripColor(ingredientName));
                      i++;
                    }
                    return Method.tabCompleterList(args, list, "<재료(숫자)>");
                  }
                  case "require":
                    return Method.tabCompleterList(args, "<제작 횟수 유형>", "recipe", "category");
                  case "statistic":
                    return Method.tabCompleterList(args, "<통계 유형>", "general", "entity", "material");
                  case "craftingtime":
                    return Method.tabCompleterList(args, "<인수>", "time", "skip", "interval");
                  case "command":
                    return Method.tabCompleterList(args, "<실행 시점>", "craft", "failure", "success");
                  case "biome":
                    return Method.tabCompleterList(args, Method.addAll(Biome.values(), "--remove"), "<생물 군계>");
                  case "belowblock":
                  {
                    List<String> list = new ArrayList<>();
                    for (Material material : Material.values())
                    {
                      if (material.isBlock())
                      {
                        list.add(material.toString().toLowerCase());
                      }
                    }
                    return Method.tabCompleterList(args, Method.addAll(list, "--remove"), "<블록>");
                  }
                  case "display":
                    if (length == 6)
                    {
                      return Method.tabCompleterList(args, "<레시피 표시 이름>", true, "<레시피 표시 이름>", "--remove");
                    }
                    return Method.tabCompleterList(args, "<레시피 표시 이름>", true);
                  case "chance":
                    return Method.tabCompleterDoubleRadius(args, 0, 100, "<제작 성공 확률(%)>");
                  case "cost":
                    if (!Cucumbery.using_Vault)
                    {
                      return Collections.singletonList("Vault 플러그인을 사용하고 있지 않습니다.");
                    }
                    return Method.tabCompleterDoubleRadius(args, 0, 100, "<제작 비용>");
                  case "foodlevelcost":
                    return Method.tabCompleterIntegerRadius(args, 0, 20, "<제작 요구 음식 포인트 비용>");
                  case "levelcost":
                    return Method.tabCompleterIntegerRadius(args, 0, Integer.MAX_VALUE, "<제작 요구 레벨>");
                  case "hpcost":
                    return Method.tabCompleterDoubleRadius(args, 0, Double.MAX_VALUE, "<제작 요구 HP>");
                  case "mhpcost":
                    return Method.tabCompleterDoubleRadius(args, 0, Double.MAX_VALUE, "<제작 요구 순수 최대 HP>");
                  case "saturationcost":
                    return Method.tabCompleterDoubleRadius(args, 0, 20, "<제작 요구 포화도 비용>");
                  case "foodlevel":
                    return Method.tabCompleterIntegerRadius(args, 0, 20, "<현재 음식 포인트의 최소 조건>", "-1");
                  case "level":
                    return Method.tabCompleterIntegerRadius(args, 0, Integer.MAX_VALUE, "<현재 레벨의 최소 조건>", "-1");
                  case "maxplayer":
                    return Method.tabCompleterIntegerRadius(args, 0, Integer.MAX_VALUE, "<현재 접속 중인 플레이어 수의 최소 조건>", "-1");
                  case "hp":
                    return Method.tabCompleterDoubleRadius(args, 0, Double.MAX_VALUE, "<현재 HP의 최소 조건>", "-1");
                  case "mhp":
                    return Method.tabCompleterDoubleRadius(args, 0, Double.MAX_VALUE, "<현재 순수 최대 HP의 최소 조건>", "-1");
                  case "saturation":
                    return Method.tabCompleterDoubleRadius(args, 0, 20, "<현재 포화도의 최소 조건>", "-1");
                  case "wealth":
                    if (!Cucumbery.using_Vault)
                    {
                      return Collections.singletonList("Vault 플러그인을 사용하고 있지 않습니다.");
                    }
                    return Method.tabCompleterDoubleRadius(args, 0, Double.MAX_VALUE, "<현재 소지 금액의 최소 조건>", "-1");
                }
                break;
            }
          }
          break;
        case 7:
          if ("edit".equals(args[0]))
          {
            switch (args[1])
            {
              case "recipe":
                switch (args[4])
                {
                  case "permission":
                    switch (args[5])
                    {
                      case "hide":
                        return Method.tabCompleterBoolean(args, "<권한 부족 시 숨김 여부>");
                      case "bypass":
                      case "base":
                        return Method.tabCompleterList(args, "<퍼미션 노드>", true, "<퍼미션 노드>", "--remove");
                    }
                    break;
                  case "reusable":
                    return Method.tabCompleterBoolean(args, "<제작 시 사라지지 않는 아이템 태그>");
                  case "require":
                    switch (args[5])
                    {
                      case "recipe":
                      case "category":
                        return Method.tabCompleterList(args, Variable.customRecipes.keySet(), "<레시피 목록>");
                    }
                    break;
                  case "statistic":
                    return Method.tabCompleterStatistics(args, args[5], "<통계>");
                  case "craftingtime":
                    switch (args[5])
                    {
                      case "skip":
                        if (!Cucumbery.using_Vault)
                        {
                          return Collections.singletonList("Vault 플러그인을 사용하고 있지 않습니다.");
                        }
                        return Method.tabCompleterList(args, "<인수>", "cost", "permission", "relative");
                      case "time":
                        return Method.tabCompleterIntegerRadius(args, 0, Integer.MAX_VALUE, "<제작에 필요한 시간(초)>");
                      case "interval":
                        return Method.tabCompleterIntegerRadius(args, 0, Integer.MAX_VALUE, "<제작 주기(초)>");
                    }
                    break;
                  case "command":
                    if (Method.equals(args[5], "<인수>", "craft", "failure", "success"))
                    {
                      return Method.tabCompleterList(args, "<인수>", "list", "add", "remove", "set", "insert");
                    }
                    break;
                  case "foodlevel":
                  {
                    int min = 0;
                    String minStr = args[5];
                    if (MessageUtil.isInteger(sender, minStr, false))
                    {
                      min = Math.max(0, Integer.parseInt(minStr));
                    }
                    return Method.tabCompleterIntegerRadius(args, min, 20, "[현재 음식 포인트의 최대 조건]", "-1", min + "");
                  }
                  case "level":
                  {
                    int min = 0;
                    String minStr = args[5];
                    if (MessageUtil.isInteger(sender, minStr, false))
                    {
                      min = Math.max(0, Integer.parseInt(minStr));
                    }
                    return Method.tabCompleterIntegerRadius(args, min, Integer.MAX_VALUE, "[현재 레벨의 최대 조건]", "-1", min + "");
                  }
                  case "maxplayer":
                  {
                    int min = 0;
                    String minStr = args[5];
                    if (MessageUtil.isInteger(sender, minStr, false))
                    {
                      min = Math.max(0, Integer.parseInt(minStr));
                    }
                    return Method.tabCompleterIntegerRadius(args, min, Integer.MAX_VALUE, "[현재 접속 중인 플레이어 수의 최대 조건]", "-1", min + "");
                  }
                  case "hp":
                  {
                    double min = 0;
                    String minStr = args[5];
                    if (MessageUtil.isDouble(sender, minStr, false))
                    {
                      min = Math.max(0, Double.parseDouble(minStr));
                    }
                    return Method.tabCompleterDoubleRadius(args, min, Double.MAX_VALUE, "[현재 HP의 최대 조건]", "-1", min + "");
                  }
                  case "mhp":
                  {
                    double min = 0;
                    String minStr = args[5];
                    if (MessageUtil.isDouble(sender, minStr, false))
                    {
                      min = Math.max(0, Double.parseDouble(minStr));
                    }
                    return Method.tabCompleterDoubleRadius(args, min, Double.MAX_VALUE, "[현재 순수 최대 HP의 최대 조건]", "-1", min + "");
                  }
                  case "saturation":
                  {
                    double min = 0;
                    String minStr = args[5];
                    if (MessageUtil.isDouble(sender, minStr, false))
                    {
                      min = Math.max(0, Double.parseDouble(minStr));
                    }
                    return Method.tabCompleterDoubleRadius(args, min, 20, "[현재 포화도의 최대 조건]", "-1", min + "");
                  }
                  case "wealth":
                  {
                    if (!Cucumbery.using_Vault)
                    {
                      return Collections.singletonList("Vault 플러그인을 사용하고 있지 않습니다.");
                    }
                    double min = 0;
                    String minStr = args[5];
                    if (MessageUtil.isDouble(sender, minStr, false))
                    {
                      min = Math.max(0, Double.parseDouble(minStr));
                    }
                    return Method.tabCompleterDoubleRadius(args, min, Double.MAX_VALUE, "[현재 소지 금액의 최대 조건]", "-1", min + "");
                  }
                }
                break;
              case "category":
                switch (args[3])
                {
                  case "require":
                    if ("recipe".equals(args[4]))
                    {
                      YamlConfiguration config = Variable.customRecipes.get(args[5]);
                      if (config == null)
                      {
                        return Collections.singletonList(args[5] + MessageUtil.getFinalConsonant(args[5], MessageUtil.ConsonantType.은는) + " 잘못되거나 알 수 없는 레시피 목록입니다.");
                      }
                      ConfigurationSection recipes = config.getConfigurationSection("recipes");
                      if (recipes == null)
                      {
                        return Collections.singletonList(args[5] + " 레시피 목록에는 유효한 레시피가 없습니다.");
                      }
                      return Method.tabCompleterList(args, recipes.getKeys(false), "<레시피>");
                    }
                    if ("category".equals(args[4]))
                    {
                      YamlConfiguration config = Variable.customRecipes.get(args[5]);
                      if (config == null)
                      {
                        return Collections.singletonList(args[5] + MessageUtil.getFinalConsonant(args[5], MessageUtil.ConsonantType.은는) + " 잘못되거나 알 수 없는 레시피 목록입니다.");
                      }
                      String requireCategoryDisplay = config.getString("extra.display");
                      if (requireCategoryDisplay == null)
                      {
                        requireCategoryDisplay = args[5];
                      }
                      requireCategoryDisplay = MessageUtil.stripColor(requireCategoryDisplay);
                      return Method.tabCompleterIntegerRadius(args, 0, Integer.MAX_VALUE, "<" + requireCategoryDisplay + " 레시피 목록에 있는 레시피의 최소 제작 횟수 조건>", "-1");
                    }
                    break;
                  case "statistic":
                    switch (args[4])
                    {
                      case "material":
                        if (args[5].equals("mine_block"))
                        {
                          List<String> list = new ArrayList<>();
                          for (Material material : Material.values())
                          {
                            if (material.isBlock())
                            {
                              list.add(material.toString().toLowerCase());
                            }
                          }
                          return Method.tabCompleterList(args, list, "<블록>");
                        }
                        else if (args[5].equals("break_item"))
                        {
                          List<String> list = new ArrayList<>();
                          for (Material material : Constant.DURABLE_ITEMS)
                          {
                            list.add(material.toString().toLowerCase());
                          }
                          return Method.tabCompleterList(args, list, "<내구도가 있는 아이템>");
                        }
                        else if (Method.equals(args[5], "use_item", "drop", "pickup", "craft_item"))
                        {
                          List<String> list = new ArrayList<>();
                          for (Material material : Material.values())
                          {
                            if (material.isItem() && material != Material.AIR)
                            {
                              list.add(material.toString().toLowerCase());
                            }
                          }
                          return Method.tabCompleterList(args, list, "<아이템>");
                        }
                        break;
                      case "entity":
                        List<String> list = new ArrayList<>();
                        for (EntityType entityType : EntityType.values())
                        {
                          if (entityType.isAlive())
                          {
                            list.add(entityType.toString().toLowerCase());
                          }
                        }
                        return Method.tabCompleterList(args, list, "<개체>");
                      case "general":
                        String typeValue = args[5];
                        try
                        {
                          typeValue = Statistic.valueOf(typeValue.toUpperCase()).toString();
                        }
                        catch (Exception ignored)
                        {
                        }
                        return Method.tabCompleterIntegerRadius(args, 0, Integer.MAX_VALUE, "<" + typeValue + "의 최솟값 조건>", "-1");
                    }
                    break;
                }
                break;
            }
          }
          break;
        case 8:
          if ("edit".equals(args[0]))
          {
            if ("category".equals(args[1]))
            {
              if ("statistic".equals(args[3]))
              {
                String typeValue = args[6];
                Material material = null;
                switch (args[4])
                {
                  case "entity":
                    try
                    {
                      typeValue = MessageUtil.stripColor((EntityType.valueOf(typeValue.toUpperCase())).toString());
                    }
                    catch (Exception ignored)
                    {

                    }
                    break;
                  case "material":
                    try
                    {
                      material = Material.valueOf(typeValue.toUpperCase());
                      typeValue = MessageUtil.stripColor(ComponentUtil.itemName(material).toString());
                    }
                    catch (Exception ignored)
                    {

                    }
                    break;
                }
                switch (args[4])
                {
                  case "material":
                    switch (args[5])
                    {
                      case "break_item":
                        return Method.tabCompleterIntegerRadius(args, 0, Integer.MAX_VALUE, "<" + typeValue + MessageUtil.getFinalConsonant(typeValue, MessageUtil.ConsonantType.을를) + " 망가뜨린 최소 횟수 조건>", "-1");
                      case "craft_item":
                        return Method.tabCompleterIntegerRadius(args, 0, Integer.MAX_VALUE, "<" + typeValue + MessageUtil.getFinalConsonant(typeValue, MessageUtil.ConsonantType.을를) + " 제작한 최소 횟수 조건>", "-1");
                      case "drop":
                        return Method.tabCompleterIntegerRadius(args, 0, Integer.MAX_VALUE, "<" + typeValue + MessageUtil.getFinalConsonant(typeValue, MessageUtil.ConsonantType.을를) + " 떨어뜨린 최소 횟수 조건>", "-1");
                      case "pickup":
                        return Method.tabCompleterIntegerRadius(args, 0, Integer.MAX_VALUE, "<" + typeValue + MessageUtil.getFinalConsonant(typeValue, MessageUtil.ConsonantType.을를) + " 주운 최소 횟수 조건>", "-1");
                      case "mine_block":
                        return Method.tabCompleterIntegerRadius(args, 0, Integer.MAX_VALUE, "<" + typeValue + MessageUtil.getFinalConsonant(typeValue, MessageUtil.ConsonantType.을를) + " 채굴한 최소 횟수 조건>", "-1");
                      case "use_item":
                        String useType = "사용";
                        if (material.isBlock())
                        {
                          useType = "설치";
                        }
                        if (ItemStackUtil.isEdible(material))
                        {
                          useType = "섭취";
                        }
                        return Method.tabCompleterIntegerRadius(
                                args, 0, Integer.MAX_VALUE, "<" + typeValue + MessageUtil.getFinalConsonant(typeValue, MessageUtil.ConsonantType.을를) + " " + useType + "한 최소 횟수 조건>", "-1");
                    }
                    break;
                  case "entity":
                    switch (args[5])
                    {
                      case "entity_killed_by":
                        return Method.tabCompleterIntegerRadius(args, 0, Integer.MAX_VALUE, "<" + typeValue + "에게 죽은 최소 횟수 조건>", "-1");
                      case "kill_entity":
                        return Method.tabCompleterIntegerRadius(args, 0, Integer.MAX_VALUE, "<" + typeValue + MessageUtil.getFinalConsonant(typeValue, MessageUtil.ConsonantType.을를) + " 죽인 최소 횟수 조건>", "-1");
                    }
                    break;
                  case "general":
                    int min = 0;
                    if (MessageUtil.isInteger(sender, args[6], false))
                    {
                      min = Integer.parseInt(args[6]);
                    }
                    typeValue = args[5];
                    try
                    {
                      typeValue = Statistic.valueOf(typeValue.toUpperCase()).toString();
                    }
                    catch (Exception ignored)
                    {
                    }
                    return Method.tabCompleterIntegerRadius(args, min, Integer.MAX_VALUE, "[" + typeValue + "의 최댓값 조건]", "-1", min + "");
                }
              }
              if ("require".equals(args[3]))
              {
                if ("category".equals(args[4]))
                {
                  YamlConfiguration config = Variable.customRecipes.get(args[5]);
                  if (config == null)
                  {
                    return Collections.singletonList(args[5] + MessageUtil.getFinalConsonant(args[5], MessageUtil.ConsonantType.은는) + " 잘못되거나 알 수 없는 레시피 목록입니다.");
                  }
                  String requireCategoryDisplay = config.getString("extra.display");
                  if (requireCategoryDisplay == null)
                  {
                    requireCategoryDisplay = args[5];
                  }
                  requireCategoryDisplay = MessageUtil.stripColor(requireCategoryDisplay);
                  int min = 0;
                  if (MessageUtil.isInteger(sender, args[6], false))
                  {
                    min = Integer.parseInt(args[6]);
                  }
                  return Method.tabCompleterIntegerRadius(args, min, Integer.MAX_VALUE, "[" + requireCategoryDisplay + " 레시피 목록에 있는 레시피의 최대 제작 횟수 조건]", "-1", min + "");
                }
                if ("recipe".equals(args[4]))
                {
                  YamlConfiguration config = Variable.customRecipes.get(args[5]);
                  if (config == null)
                  {
                    return Collections.singletonList(args[5] + MessageUtil.getFinalConsonant(args[5], MessageUtil.ConsonantType.은는) + " 잘못되거나 알 수 없는 레시피 목록입니다.");
                  }
                  ConfigurationSection recipes = config.getConfigurationSection("recipes");
                  if (recipes == null)
                  {
                    return Collections.singletonList(args[5] + " 레시피 목록에는 유효한 레시피가 없습니다.");
                  }
                  if (config.getConfigurationSection("recipes." + args[6]) == null)
                  {
                    return Collections.singletonList(args[6] + MessageUtil.getFinalConsonant(args[6], MessageUtil.ConsonantType.은는) + " 잘못되거나 알 수 없는 레시피입니다.");
                  }
                  String requireCategoryDisplay = config.getString("extra.display");
                  if (requireCategoryDisplay == null)
                  {
                    requireCategoryDisplay = args[5];
                  }
                  requireCategoryDisplay = MessageUtil.stripColor(requireCategoryDisplay);
                  String requireRecipeDisplay = config.getString("recipes." + args[6] + ".extra.display");
                  if (requireRecipeDisplay == null)
                  {
                    requireRecipeDisplay = args[6];
                  }
                  requireRecipeDisplay = MessageUtil.stripColor(requireRecipeDisplay);
                  return Method.tabCompleterIntegerRadius(args, 0, Integer.MAX_VALUE, "<" + requireCategoryDisplay + " 레시피 목록에 있는 " + requireRecipeDisplay + " 레시피의 최소 제작 횟수 조건>", "-1");
                }
              }
            }
            if ("recipe".equals(args[1]))
            {
              switch (args[4])
              {
                case "require":
                  if ("recipe".equals(args[5]))
                  {
                    FileConfiguration config = Variable.customRecipes.get(args[6]);
                    if (config == null)
                    {
                      return Collections.singletonList(args[6] + MessageUtil.getFinalConsonant(args[6], MessageUtil.ConsonantType.은는) + " 잘못되거나 알 수 없는 레시피 목록입니다.");
                    }
                    ConfigurationSection recipes = config.getConfigurationSection("recipes");
                    if (recipes == null)
                    {
                      return Collections.singletonList(args[6] + " 레시피 목록에는 유효한 레시피가 없습니다.");
                    }
                    return Method.tabCompleterList(args, recipes.getKeys(false), "<레시피>");
                  }
                  if ("category".equals(args[5]))
                  {
                    YamlConfiguration config = Variable.customRecipes.get(args[6]);
                    if (config == null)
                    {
                      return Collections.singletonList(args[5] + MessageUtil.getFinalConsonant(args[6], MessageUtil.ConsonantType.은는) + " 잘못되거나 알 수 없는 레시피 목록입니다.");
                    }
                    String requireCategoryDisplay = config.getString("extra.display");
                    if (requireCategoryDisplay == null)
                    {
                      requireCategoryDisplay = args[6];
                    }
                    requireCategoryDisplay = MessageUtil.stripColor(requireCategoryDisplay);
                    return Method.tabCompleterIntegerRadius(args, 0, Integer.MAX_VALUE, "<" + requireCategoryDisplay + " 레시피 목록에 있는 레시피의 최소 제작 횟수 조건>", "-1");
                  }
                  break;
                case "statistic":
                  switch (args[5])
                  {
                    case "material":
                      if (args[6].equals("mine_block"))
                      {
                        List<String> list = new ArrayList<>();
                        for (Material material : Material.values())
                        {
                          if (material.isBlock())
                          {
                            list.add(material.toString().toLowerCase());
                          }
                        }
                        return Method.tabCompleterList(args, list, "<블록>");
                      }
                      else if (args[6].equals("break_item"))
                      {
                        List<String> list = new ArrayList<>();
                        for (Material material : Constant.DURABLE_ITEMS)
                        {
                          list.add(material.toString().toLowerCase());
                        }
                        return Method.tabCompleterList(args, list, "<내구도가 있는 아이템>");
                      }
                      else if (Method.equals(args[6], "use_item", "drop", "pickup", "craft_item"))
                      {
                        List<String> list = new ArrayList<>();
                        for (Material material : Material.values())
                        {
                          if (material.isItem() && material != Material.AIR)
                          {
                            list.add(material.toString().toLowerCase());
                          }
                        }
                        return Method.tabCompleterList(args, list, "<아이템>");
                      }
                      return Method.tabCompleterList(args, Material.values(), "<물질>");
                    case "entity":
                      List<String> list = new ArrayList<>();
                      for (EntityType entityType : EntityType.values())
                      {
                        if (entityType.isAlive())
                        {
                          list.add(entityType.toString().toLowerCase());
                        }
                      }
                      return Method.tabCompleterList(args, list, "<개체>");
                    case "general":
                      String typeValue = args[6];
                      try
                      {
                        typeValue = Statistic.valueOf(typeValue.toUpperCase()).toString();
                      }
                      catch (Exception ignored)
                      {
                      }
                      return Method.tabCompleterIntegerRadius(args, 0, Integer.MAX_VALUE, "<" + typeValue + "의 최솟값 조건>", "-1");
                  }
                case "craftingtime":
                  if ("skip".equals(args[5]))
                  {
                    if (!Cucumbery.using_Vault)
                    {
                      return Collections.singletonList("Vault 플러그인을 사용하고 있지 않습니다.");
                    }
                    switch (args[6])
                    {
                      case "cost":
                        return Method.tabCompleterDoubleRadius(args, 0, Double.MAX_VALUE, "<제작 시간 스킵 비용>");
                      case "relative":
                        return Method.tabCompleterBoolean(args, "<스킵 비용 시간 비례 적용 여부>");
                      case "permission":
                        return Method.tabCompleterList(args, "<제작 시간 스킵 요구 퍼미션 노드>", true, "<제작 시간 스킵 요구 퍼미션 노드>", "--remove");
                    }
                  }
                  break;
                case "command":
                  if (Method.equals(args[5], "craft", "failure", "success"))
                  {
                    String commandTypeStr = "";
                    FileConfiguration config = Variable.customRecipes.get(args[2]);
                    if (config == null)
                    {
                      return Collections.singletonList(args[2] + MessageUtil.getFinalConsonant(args[2], MessageUtil.ConsonantType.은는) + " 잘못되거나 알 수 없는 레시피 목록입니다.");
                    }
                    List<String> commands = config == null ? null : config.getStringList("recipes." + args[3] + ".extra.commands." + args[5]);
                    switch (args[5])
                    {
                      case "craft":
                        commandTypeStr = "제작 완료";
                        break;
                      case "success":
                        commandTypeStr = "제작 성공";
                        break;
                      case "failure":
                        commandTypeStr = "제작 실패";
                        break;
                    }
                    switch (args[6])
                    {
                      case "remove":
                        if (commands.size() == 0)
                        {
                          return Collections.singletonList(args[2] + " 레시피 목록의 " + args[3] + " 레시피에는 " + commandTypeStr + " 시 실행되는 명령어가 없습니다.");
                        }
                        return Method.tabCompleterIntegerRadius(args, 1, commands.size(), "[줄]", "--all");
                      case "set":
                        if (length == 8)
                        {
                          if (commands.size() == 0)
                          {
                            return Collections.singletonList(args[2] + " 레시피 목록의 " + args[3] + " 레시피에는 " + commandTypeStr + " 시 실행되는 명령어가 없습니다.");
                          }
                        }
                        return Method.tabCompleterIntegerRadius(args, 1, commands.size(), "<줄>");
                      case "insert":
                        if (length == 8)
                        {
                          if (commands.size() == 0)
                          {
                            return Collections.singletonList(args[2] + " 레시피 목록의 " + args[3] + " 레시피에는 " + commandTypeStr + " 시 실행되는 명령어가 없습니다.");
                          }
                          return Method.tabCompleterIntegerRadius(args, 1, commands.size(), "[줄]", "--all");
                        }
                    }
                  }
                  break;
              }
            }
          }
          break;
        case 9:
          if ("edit".equals(args[0]))
          {
            if ("category".equals(args[1]))
            {
              if ("statistic".equals(args[3]))
              {
                String typeValue = args[6];
                Material material = null;
                switch (args[4])
                {
                  case "entity":
                    try
                    {
                      typeValue = MessageUtil.stripColor((EntityType.valueOf(typeValue.toUpperCase())).toString());
                    }
                    catch (Exception ignored)
                    {

                    }
                    break;
                  case "material":
                    try
                    {
                      material = Material.valueOf(typeValue.toUpperCase());
                      typeValue = MessageUtil.stripColor(ComponentUtil.itemName(material).toString());
                    }
                    catch (Exception ignored)
                    {

                    }
                    break;
                }
                int min = 0;
                if (MessageUtil.isInteger(sender, args[7], false))
                {
                  min = Integer.parseInt(args[7]);
                }
                switch (args[4])
                {
                  case "material":
                    switch (args[5])
                    {
                      case "break_item":
                        return Method.tabCompleterIntegerRadius(
                                args, min, Integer.MAX_VALUE, "[" + typeValue + MessageUtil.getFinalConsonant(typeValue, MessageUtil.ConsonantType.을를) + " 망가뜨린 최대 횟수 조건]", "-1", min + "");
                      case "craft_item":
                        return Method.tabCompleterIntegerRadius(
                                args, min, Integer.MAX_VALUE, "[" + typeValue + MessageUtil.getFinalConsonant(typeValue, MessageUtil.ConsonantType.을를) + " 제작한 최대 횟수 조건]", "-1", min + "");
                      case "drop":
                        return Method.tabCompleterIntegerRadius(
                                args, min, Integer.MAX_VALUE, "[" + typeValue + MessageUtil.getFinalConsonant(typeValue, MessageUtil.ConsonantType.을를) + " 떨어뜨린 최대 횟수 조건]", "-1", min + "");
                      case "pickup":
                        return Method.tabCompleterIntegerRadius(
                                args, min, Integer.MAX_VALUE, "[" + typeValue + MessageUtil.getFinalConsonant(typeValue, MessageUtil.ConsonantType.을를) + " 주운 최대 횟수 조건]", "-1", min + "");
                      case "mine_block":
                        return Method.tabCompleterIntegerRadius(
                                args, min, Integer.MAX_VALUE, "[" + typeValue + MessageUtil.getFinalConsonant(typeValue, MessageUtil.ConsonantType.을를) + " 채굴한 최대 횟수 조건]", "-1", min + "");
                      case "use_item":
                        String useType = "사용";
                        if (material.isBlock())
                        {
                          useType = "설치";
                        }
                        if (ItemStackUtil.isEdible(material))
                        {
                          useType = "섭취";
                        }
                        return Method.tabCompleterIntegerRadius(
                                args, min, Integer.MAX_VALUE, "[" + typeValue + MessageUtil.getFinalConsonant(typeValue, MessageUtil.ConsonantType.을를) + " " + useType + "한 최대 횟수 조건]", "-1", min + "");
                    }
                    break;
                  case "entity":
                    switch (args[5])
                    {
                      case "entity_killed_by":
                        return Method.tabCompleterIntegerRadius(args, min, Integer.MAX_VALUE, "[" + typeValue + "에게 죽은 최대 횟수 조건]", "-1", min + "");
                      case "kill_entity":
                        return Method.tabCompleterIntegerRadius(args, min, Integer.MAX_VALUE, "[" + typeValue + MessageUtil.getFinalConsonant(typeValue, MessageUtil.ConsonantType.을를) + " 죽인 최대 횟수 조건>", "-1");
                    }
                    break;
                }
              }
              if ("require".equals(args[3]))
              {
                if ("recipe".equals(args[4]))
                {
                  YamlConfiguration config = Variable.customRecipes.get(args[5]);
                  if (config == null)
                  {
                    return Collections.singletonList(args[5] + MessageUtil.getFinalConsonant(args[5], MessageUtil.ConsonantType.은는) + " 잘못되거나 알 수 없는 레시피 목록입니다.");
                  }
                  ConfigurationSection recipes = config.getConfigurationSection("recipes");
                  if (recipes == null)
                  {
                    return Collections.singletonList(args[5] + " 레시피 목록에는 유효한 레시피가 없습니다.");
                  }
                  if (config.getConfigurationSection("recipes." + args[6]) == null)
                  {
                    return Collections.singletonList(args[6] + MessageUtil.getFinalConsonant(args[6], MessageUtil.ConsonantType.은는) + " 잘못되거나 알 수 없는 레시피입니다.");
                  }
                  String requireCategoryDisplay = config.getString("extra.display");
                  if (requireCategoryDisplay == null)
                  {
                    requireCategoryDisplay = args[5];
                  }
                  requireCategoryDisplay = MessageUtil.stripColor(requireCategoryDisplay);
                  String requireRecipeDisplay = config.getString("recipes." + args[6] + ".extra.display");
                  if (requireRecipeDisplay == null)
                  {
                    requireRecipeDisplay = args[6];
                  }
                  requireRecipeDisplay = MessageUtil.stripColor(requireRecipeDisplay);
                  int min = 0;
                  if (MessageUtil.isInteger(sender, args[7], false))
                  {
                    min = Integer.parseInt(args[7]);
                  }
                  return Method.tabCompleterIntegerRadius(args, min, Integer.MAX_VALUE, "[" + requireCategoryDisplay + " 레시피 목록에 있는 " + requireRecipeDisplay + " 레시피의 최대 제작 횟수 조건]", "-1", min + "");
                }
              }
            }
            if ("recipe".equals(args[1]))
            {
              if ("statistic".equals(args[4]))
              {
                String typeValue = args[7];
                Material material = null;
                switch (args[5])
                {
                  case "entity":
                    try
                    {
                      typeValue = MessageUtil.stripColor((EntityType.valueOf(typeValue.toUpperCase())).toString());
                    }
                    catch (Exception ignored)
                    {

                    }
                    break;
                  case "material":
                    try
                    {
                      material = Material.valueOf(typeValue.toUpperCase());
                      typeValue = MessageUtil.stripColor(ComponentUtil.itemName(material).toString());
                    }
                    catch (Exception ignored)
                    {

                    }
                    break;
                }
                switch (args[5])
                {
                  case "material":
                    switch (args[6])
                    {
                      case "break_item":
                        return Method.tabCompleterIntegerRadius(args, 0, Integer.MAX_VALUE, "<" + typeValue + MessageUtil.getFinalConsonant(typeValue, MessageUtil.ConsonantType.을를) + " 망가뜨린 최소 횟수 조건>", "-1");
                      case "craft_item":
                        return Method.tabCompleterIntegerRadius(args, 0, Integer.MAX_VALUE, "<" + typeValue + MessageUtil.getFinalConsonant(typeValue, MessageUtil.ConsonantType.을를) + " 제작한 최소 횟수 조건>", "-1");
                      case "drop":
                        return Method.tabCompleterIntegerRadius(args, 0, Integer.MAX_VALUE, "<" + typeValue + MessageUtil.getFinalConsonant(typeValue, MessageUtil.ConsonantType.을를) + " 떨어뜨린 최소 횟수 조건>", "-1");
                      case "pickup":
                        return Method.tabCompleterIntegerRadius(args, 0, Integer.MAX_VALUE, "<" + typeValue + MessageUtil.getFinalConsonant(typeValue, MessageUtil.ConsonantType.을를) + " 주운 최소 횟수 조건>", "-1");
                      case "mine_block":
                        return Method.tabCompleterIntegerRadius(args, 0, Integer.MAX_VALUE, "<" + typeValue + MessageUtil.getFinalConsonant(typeValue, MessageUtil.ConsonantType.을를) + " 채굴한 최소 횟수 조건>", "-1");
                      case "use_item":
                        String useType = "사용";
                        if (material.isBlock())
                        {
                          useType = "설치";
                        }
                        if (ItemStackUtil.isEdible(material))
                        {
                          useType = "섭취";
                        }
                        return Method.tabCompleterIntegerRadius(
                                args, 0, Integer.MAX_VALUE, "<" + typeValue + MessageUtil.getFinalConsonant(typeValue, MessageUtil.ConsonantType.을를) + " " + useType + "한 최소 횟수 조건>", "-1");
                    }
                    break;
                  case "entity":
                    switch (args[6])
                    {
                      case "entity_killed_by":
                        return Method.tabCompleterIntegerRadius(args, 0, Integer.MAX_VALUE, "<" + typeValue + "에게 죽은 최소 횟수 조건>", "-1");
                      case "kill_entity":
                        return Method.tabCompleterIntegerRadius(args, 0, Integer.MAX_VALUE, "<" + typeValue + MessageUtil.getFinalConsonant(typeValue, MessageUtil.ConsonantType.을를) + " 죽인 최소 횟수 조건>", "-1");
                    }
                    break;
                  case "general":
                    int min = 0;
                    if (MessageUtil.isInteger(sender, args[7], false))
                    {
                      min = Integer.parseInt(args[7]);
                    }
                    typeValue = args[6];
                    try
                    {
                      typeValue = Statistic.valueOf(typeValue.toUpperCase()).toString();
                    }
                    catch (Exception ignored)
                    {
                    }
                    return Method.tabCompleterIntegerRadius(args, min, Integer.MAX_VALUE, "[" + typeValue + "의 최댓값 조건]", "-1", min + "");
                }
              }
              if ("require".equals(args[4]))
              {
                if ("category".equals(args[5]))
                {
                  YamlConfiguration config = Variable.customRecipes.get(args[6]);
                  if (config == null)
                  {
                    return Collections.singletonList(args[6] + MessageUtil.getFinalConsonant(args[6], MessageUtil.ConsonantType.은는) + " 잘못되거나 알 수 없는 레시피 목록입니다.");
                  }
                  String requireCategoryDisplay = config.getString("extra.display");
                  if (requireCategoryDisplay == null)
                  {
                    requireCategoryDisplay = args[6];
                  }
                  requireCategoryDisplay = MessageUtil.stripColor(requireCategoryDisplay);
                  int min = 0;
                  if (MessageUtil.isInteger(sender, args[7], false))
                  {
                    min = Integer.parseInt(args[7]);
                  }
                  return Method.tabCompleterIntegerRadius(args, min, Integer.MAX_VALUE, "[" + requireCategoryDisplay + " 레시피 목록에 있는 레시피의 최대 제작 횟수 조건]", "-1", min + "");
                }
                if ("recipe".equals(args[5]))
                {
                  YamlConfiguration config = Variable.customRecipes.get(args[6]);
                  if (config == null)
                  {
                    return Collections.singletonList(args[6] + MessageUtil.getFinalConsonant(args[6], MessageUtil.ConsonantType.은는) + " 잘못되거나 알 수 없는 레시피 목록입니다.");
                  }
                  ConfigurationSection recipes = config.getConfigurationSection("recipes");
                  if (recipes == null)
                  {
                    return Collections.singletonList(args[6] + " 레시피 목록에는 유효한 레시피가 없습니다.");
                  }
                  if (config.getConfigurationSection("recipes." + args[7]) == null)
                  {
                    return Collections.singletonList(args[7] + MessageUtil.getFinalConsonant(args[7], MessageUtil.ConsonantType.은는) + " 잘못되거나 알 수 없는 레시피입니다.");
                  }
                  String requireCategoryDisplay = config.getString("extra.display");
                  if (requireCategoryDisplay == null)
                  {
                    requireCategoryDisplay = args[6];
                  }
                  requireCategoryDisplay = MessageUtil.stripColor(requireCategoryDisplay);
                  String requireRecipeDisplay = config.getString("recipes." + args[7] + ".extra.display");
                  if (requireRecipeDisplay == null)
                  {
                    requireRecipeDisplay = args[7];
                  }
                  requireRecipeDisplay = MessageUtil.stripColor(requireRecipeDisplay);
                  return Method.tabCompleterIntegerRadius(args, 0, Integer.MAX_VALUE, "<" + requireCategoryDisplay + " 레시피 목록에 있는 " + requireRecipeDisplay + " 레시피의 최소 제작 횟수 조건>", "-1");
                }
              }
            }
          }
          break;
        case 10:
          if ("edit".equals(args[0]))
          {
            if ("recipe".equals(args[1]))
            {
              if ("statistic".equals(args[4]))
              {
                String typeValue = args[7];
                Material material = null;
                switch (args[5])
                {
                  case "entity":
                    try
                    {
                      typeValue = MessageUtil.stripColor((EntityType.valueOf(typeValue.toUpperCase())).toString());
                    }
                    catch (Exception ignored)
                    {

                    }
                    break;
                  case "material":
                    try
                    {
                      material = Material.valueOf(typeValue.toUpperCase());
                      typeValue = MessageUtil.stripColor(ComponentUtil.itemName(material).toString());
                    }
                    catch (Exception ignored)
                    {

                    }
                    break;
                }
                int min = 0;
                if (MessageUtil.isInteger(sender, args[8], false))
                {
                  min = Integer.parseInt(args[8]);
                }
                switch (args[5])
                {
                  case "material":
                    switch (args[6])
                    {
                      case "break_item":
                        return Method.tabCompleterIntegerRadius(
                                args, min, Integer.MAX_VALUE, "[" + typeValue + MessageUtil.getFinalConsonant(typeValue, MessageUtil.ConsonantType.을를) + " 망가뜨린 최대 횟수 조건]", "-1", min + "");
                      case "craft_item":
                        return Method.tabCompleterIntegerRadius(
                                args, min, Integer.MAX_VALUE, "[" + typeValue + MessageUtil.getFinalConsonant(typeValue, MessageUtil.ConsonantType.을를) + " 제작한 최대 횟수 조건]", "-1", min + "");
                      case "drop":
                        return Method.tabCompleterIntegerRadius(
                                args, min, Integer.MAX_VALUE, "[" + typeValue + MessageUtil.getFinalConsonant(typeValue, MessageUtil.ConsonantType.을를) + " 떨어뜨린 최대 횟수 조건]", "-1", min + "");
                      case "pickup":
                        return Method.tabCompleterIntegerRadius(
                                args, min, Integer.MAX_VALUE, "[" + typeValue + MessageUtil.getFinalConsonant(typeValue, MessageUtil.ConsonantType.을를) + " 주운 최대 횟수 조건]", "-1", min + "");
                      case "mine_block":
                        return Method.tabCompleterIntegerRadius(
                                args, min, Integer.MAX_VALUE, "[" + typeValue + MessageUtil.getFinalConsonant(typeValue, MessageUtil.ConsonantType.을를) + " 채굴한 최대 횟수 조건]", "-1", min + "");
                      case "use_item":
                        String useType = "사용";
                        if (material.isBlock())
                        {
                          useType = "설치";
                        }
                        if (ItemStackUtil.isEdible(material))
                        {
                          useType = "섭취";
                        }
                        return Method.tabCompleterIntegerRadius(
                                args, min, Integer.MAX_VALUE, "[" + typeValue + MessageUtil.getFinalConsonant(typeValue, MessageUtil.ConsonantType.을를) + " " + useType + "한 최대 횟수 조건]", "-1", min + "");
                    }
                    break;
                  case "entity":
                    switch (args[6])
                    {
                      case "entity_killed_by":
                        return Method.tabCompleterIntegerRadius(args, min, Integer.MAX_VALUE, "[" + typeValue + "에게 죽은 최대 횟수 조건]", "-1", min + "");
                      case "kill_entity":
                        return Method.tabCompleterIntegerRadius(args, min, Integer.MAX_VALUE, "[" + typeValue + MessageUtil.getFinalConsonant(typeValue, MessageUtil.ConsonantType.을를) + " 죽인 최대 횟수 조건>", "-1");
                    }
                    break;
                }
              }
              if ("require".equals(args[4]))
              {
                if ("recipe".equals(args[5]))
                {
                  YamlConfiguration config = Variable.customRecipes.get(args[6]);
                  if (config == null)
                  {
                    return Collections.singletonList(args[6] + MessageUtil.getFinalConsonant(args[6], MessageUtil.ConsonantType.은는) + " 잘못되거나 알 수 없는 레시피 목록입니다.");
                  }
                  ConfigurationSection recipes = config.getConfigurationSection("recipes");
                  if (recipes == null)
                  {
                    return Collections.singletonList(args[6] + " 레시피 목록에는 유효한 레시피가 없습니다.");
                  }
                  if (config.getConfigurationSection("recipes." + args[7]) == null)
                  {
                    return Collections.singletonList(args[7] + MessageUtil.getFinalConsonant(args[7], MessageUtil.ConsonantType.은는) + " 잘못되거나 알 수 없는 레시피입니다.");
                  }
                  String requireCategoryDisplay = config.getString("extra.display");
                  if (requireCategoryDisplay == null)
                  {
                    requireCategoryDisplay = args[6];
                  }
                  requireCategoryDisplay = MessageUtil.stripColor(requireCategoryDisplay);
                  String requireRecipeDisplay = config.getString("recipes." + args[7] + ".extra.display");
                  if (requireRecipeDisplay == null)
                  {
                    requireRecipeDisplay = args[7];
                  }
                  requireRecipeDisplay = MessageUtil.stripColor(requireRecipeDisplay);
                  int min = 0;
                  if (MessageUtil.isInteger(sender, args[8], false))
                  {
                    min = Integer.parseInt(args[8]);
                  }
                  return Method.tabCompleterIntegerRadius(args, min, Integer.MAX_VALUE, "[" + requireCategoryDisplay + " 레시피 목록에 있는 " + requireRecipeDisplay + " 레시피의 최대 제작 횟수 조건]", "-1", min + "");
                }
              }
            }
          }
          break;
      }
      if (length >= 8)
      {
        if (args[0].equals("edit") && args[1].equals("recipe") && args[4].equals("command") && Method.equals(args[5], "craft", "failure", "success"))
        {
          if (Method.equals(args[6], "set", "insert"))
          {
            return this.getCommandsTabCompleter(sender, args, 9, true);
          }
          else if (args[6].equals("add"))
          {
            return this.getCommandsTabCompleter(sender, args, 8, true);
          }
        }
      }
    }
    else if (Method.equals(name, "virtualchest", "virtualchestadd") && Method.hasPermission(sender, Permission.CMD_VIRTUAL_CHEST, false))
    {
      if (sender instanceof Player)
      {
        if (length == 1)
        {
          if (Method2.isInvalidFileName(args[0]))
          {
            return Collections.singletonList(args[0] + MessageUtil.getFinalConsonant(args[0], MessageUtil.ConsonantType.은는) + " 유효하지 않은 상자 이름입니다.");
          }
          Player player = (Player) sender;
          File folder = new File(Cucumbery.getPlugin().getDataFolder() + "/data/VirtualChest/" + player.getUniqueId().toString());
          if (folder.exists())
          {
            List<String> list = new ArrayList<>();
            File[] files = folder.listFiles();
            if (files != null)
            {
              for (File file : files)
              {
                String fileName = file.getName();
                if (fileName.endsWith(".yml"))
                {
                  fileName = fileName.substring(0, fileName.length() - 4);
                  list.add(fileName);
                }
              }
            }
            return Method.tabCompleterList(args, list, "[상자 이름]", true);
          }
          return Method.tabCompleterList(args, "[상자 이름]", true, "default");
        }
      }
    }
    else if (name.equals("virtualchestadmin") && Method.hasPermission(sender, Permission.CMD_VIRTUAL_CHEST_ADMIN, false))
    {
      if (sender instanceof Player)
      {
        if (length == 1)
        {
          if (label.equals("chestadmin"))
          {
            return Method.tabCompleterPlayer(sender, args);
          }
          return Method.tabCompleterOfflinePlayer(sender, args);
        }
        else if (length == 2)
        {
          OfflinePlayer target = SelectorUtil.getOfflinePlayer(sender, args[0], false);
          if (target != null)
          {
            if (Method2.isInvalidFileName(args[1]))
            {
              return Collections.singletonList(args[1] + MessageUtil.getFinalConsonant(args[1], MessageUtil.ConsonantType.은는) + " 유효하지 않은 상자 이름입니다.");
            }
            File folder = new File(Cucumbery.getPlugin().getDataFolder() + "/data/VirtualChest/" + target.getUniqueId().toString());
            if (folder.exists())
            {
              List<String> list = new ArrayList<>();
              File[] files = folder.listFiles();
              if (files != null)
              {
                for (File file : files)
                {
                  String fileName = file.getName();
                  if (fileName.endsWith(".yml"))
                  {
                    fileName = fileName.substring(0, fileName.length() - 4);
                    list.add(fileName);
                  }
                }
              }
              return Method.tabCompleterList(args, list, "[상자 이름]");
            }
            return Method.tabCompleterList(args, "[상자 이름]");
          }
        }
      }
    }
    else if (name.equals("setrepaircost") && sender instanceof Player player)
    {
      ItemStack item = player.getInventory().getItemInMainHand();
      if (!ItemStackUtil.itemExists(item))
      {
        return Collections.singletonList(Prefix.NO_HOLDING_ITEM.toString());
      }
      if (length == 1)
      {
        return Method.tabCompleterIntegerRadius(args, 0, 30, "<누적 모루 합성 횟수>");
      }
      else if (length == 2)
      {
        return Method.tabCompleterBoolean(args, "[명령어 출력 숨김 여부]");
      }
    }
    else if (name.equals("economy") && Method.hasPermission(sender, Permission.CMD_ECONOMY, false))
    {
      if (!Cucumbery.using_Vault)
      {
        return Collections.singletonList("Vault 플러그인을 사용하고 있지 않습니다.");
      }
      if (length == 1)
      {
        return Method.tabCompleterList(args, "<인수>", "give", "set", "take");
      }
      else if (length == 2)
      {
        if (label.equals("ceco"))
        {
          return Method.tabCompleterPlayer(sender, args);
        }
        return Method.tabCompleterOfflinePlayer(sender, args);
      }
      else if (length == 3)
      {
        switch (args[0])
        {
          case "set":
            return Method.tabCompleterDoubleRadius(args, 0, 10_000_000_000_000d, "<금액>");
          case "give":
          case "take":
            return Method.tabCompleterDoubleRadius(args, 0, true, 10_000_000_000_000d, false, "<금액>");
        }
      }
      else if (length == 4)
      {
        return Method.tabCompleterBoolean(args, "[명령어 출력 숨김 여부]");
      }
    }
    else if (name.equals("respawn") && Method.hasPermission(sender, Permission.CMD_RESPAWN, false))
    {
      if (length == 1)
      {
        return Method.tabCompleterPlayer(sender, args);
      }
      else if (length == 2)
      {
        return Method.tabCompleterBoolean(args, "[명령어 출력 숨김 여부]");
      }
    }
    else if (name.equals("removebedspawnlocation") && Method.hasPermission(sender, Permission.CMD_REMOVE_BED_SPAWN_LOCATION, false))
    {
      if (length == 1)
      {
        return Method.tabCompleterPlayer(sender, args);
      }
      else if (length == 2)
      {
        return Method.tabCompleterBoolean(args, "[명령어 출력 숨김 여부]");
      }
    }
    else if (name.equals("checkconfig") && Method.hasPermission(sender, Permission.CMD_CHECK_CONFIG, false))
    {
      if (length == 1)
      {
        ConfigurationSection section = Cucumbery.config.getConfigurationSection("");
        if (section != null)
        {
          return Method.tabCompleterList(args, section.getKeys(true), "<키>", true);
        }
      }
      else
      {
        return Method.tabCompleterList(args, "[키]", true);
      }
    }
    else if (name.equals("cteleport") && Method.hasPermission(sender, Permission.CMD_TELEPORT, false))
    {
      if (length == 1)
      {
        return Method.tabCompleterEntity(sender, args, "(<개체>|<여러 개체> [다른 개체])", true);
      }
      else if (length == 2)
      {
        return Method.tabCompleterEntity(sender, args, "<다른 개체>");
      }
    }
    else if (name.equals("setname") && sender instanceof Player player)
    {
      ItemStack item = player.getInventory().getItemInMainHand();
      if (!ItemStackUtil.itemExists(item))
      {
        return Collections.singletonList(Prefix.NO_HOLDING_ITEM.toString());
      }
      if (length == 1)
      {
        return Method.tabCompleterList(args, "<아이템 이름>", true, "<아이템 이름>", "--remove");
      }
      return Method.tabCompleterList(args, "[아이템 이름]", true);
    }
    else if (name.equals("setname2") && sender instanceof Player player)
    {
      ItemStack item = player.getInventory().getItemInMainHand();
      if (!ItemStackUtil.itemExists(item))
      {
        return Collections.singletonList(Prefix.NO_HOLDING_ITEM.toString());
      }
      if (length == 1)
      {
        return Method.tabCompleterList(args, "<추가로 입력할 이름|--지울 글자 수>", true, "<추가로 입력할 이름>", "--");
      }
      return Method.tabCompleterList(args, "[추가로 입력할 이름]", true);
    }
    else if (name.equals("setlore") && sender instanceof Player player)
    {
      ItemStack item = player.getInventory().getItemInMainHand();
      if (!ItemStackUtil.itemExists(item))
      {
        return Collections.singletonList(Prefix.NO_HOLDING_ITEM.toString());
      }
      if (length == 1)
      {
        return Method.tabCompleterIntegerRadius(args, 1, Integer.MAX_VALUE, "<줄>");
      }
      else
      {
        try
        {
          if (args[1].equals(""))
          {
            return Method.tabCompleterList(args, "<설명>", true, "<설명>", "--empty", item.getItemMeta().getLore().get(Integer.parseInt(args[0]) - 1).replace("§", "&"));
          }
        }
        catch (Exception ignored)
        {

        }
        if (length == 2)
        {
          return Method.tabCompleterList(args, "<설명>", true, "<설명>", "--empty");
        }
        return Method.tabCompleterList(args, "[설명]", true);
      }
    }
    else if (name.equals("setlore2") && sender instanceof Player player)
    {
      ItemStack item = player.getInventory().getItemInMainHand();
      if (!ItemStackUtil.itemExists(item))
      {
        return Collections.singletonList(Prefix.NO_HOLDING_ITEM.toString());
      }
      if (length == 1)
      {
        return Method.tabCompleterIntegerRadius(args, 1, Integer.MAX_VALUE, "<줄>");
      }
      else
      {
        if (length == 2)
        {
          return Method.tabCompleterList(args, "<추가로 입력할 설명|--지울 글자 수>", true, "<추가로 입력할 설명>", "--");
        }
        return Method.tabCompleterList(args, "[추가로 입력할 설명]", true);
      }
    }
    else if (name.equals("addlore") && sender instanceof Player player)
    {
      ItemStack item = player.getInventory().getItemInMainHand();
      if (!ItemStackUtil.itemExists(item))
      {
        return Collections.singletonList(Prefix.NO_HOLDING_ITEM.toString());
      }
      if (length == 1)
      {
        return Method.tabCompleterList(args, "<설명>", true, "<설명>", "--empty");
      }
      return Method.tabCompleterList(args, "[설명]", true);
    }
    else if (name.equals("deletelore") && sender instanceof Player)
    {
      if (length == 1)
      {
        Player player = (Player) sender;
        ItemStack item = player.getInventory().getItemInMainHand();
        if (!ItemStackUtil.itemExists(item))
        {
          return Collections.singletonList(Prefix.NO_HOLDING_ITEM.toString());
        }
        else if (!ItemStackUtil.hasLore(item, true))
        {
          return Collections.singletonList("더 이상 제거할 수 있는 설명이 없습니다.");
        }
        List<String> lore = item.getItemMeta().getLore();
        return Method.tabCompleterIntegerRadius(args, 1, lore == null ? 1 : lore.size(), "[줄]", "--all");
      }
    }
    else if (name.equals("insertlore") && sender instanceof Player player)
    {
      ItemStack item = player.getInventory().getItemInMainHand();
      if (!ItemStackUtil.itemExists(item))
      {
        return Collections.singletonList(Prefix.NO_HOLDING_ITEM.toString());
      }
      else if (!ItemStackUtil.hasLore(item, true))
      {
        return Collections.singletonList("설명을 들여쓸 수 없습니다.");
      }
      if (length == 1)
      {
        List<String> lore = item.getItemMeta().getLore();
        return Method.tabCompleterIntegerRadius(args, 1, lore == null ? 1 : lore.size(), "<줄>");
      }
      else
      {
        if (length == 2)
        {
          return Method.tabCompleterList(args, "<설명>", true, "<설명>", "--empty");
        }
        return Method.tabCompleterList(args, "[설명]", true);
      }
    }
    else if (name.equals("editcommandblock") && sender instanceof Player player)
    {
      ItemStack item = player.getInventory().getItemInMainHand();
      if (!ItemStackUtil.itemExists(item))
      {
        return Collections.singletonList(Prefix.NO_HOLDING_ITEM.toString());
      }
      switch (item.getType())
      {
        case COMMAND_BLOCK:
        case CHAIN_COMMAND_BLOCK:
        case REPEATING_COMMAND_BLOCK:
          break;
        default:
          return Collections.singletonList("해당 명령어는 명령 블록에만 사용할 수 있습니다.");
      }
      if (length == 1)
      {
        return Method.tabCompleterList(args, "<인수>", "name", "command", "auto", "conditional");
      }
      else
      {
        switch (args[0])
        {
          case "command":
            return this.getCommandsTabCompleter(sender, args, 2, false);
          case "name":
            if (args.length == 2)
            {
              return Method.tabCompleterList(args, "<이름>", true, "<이름>", "@");
            }
            return Method.tabCompleterList(args, "[이름]", true);
          case "auto":
            if (length == 2)
            {
              return Method.tabCompleterBoolean(args, "<항상 활성화>");
            }
          case "conditional":
            if (length == 2)
            {
              return Method.tabCompleterBoolean(args, "<조건적>");
            }
        }
      }
    }
    else if (name.equals("broadcast"))
    {
      return Method.tabCompleterList(args, length == 1 ? "<메시지>" : "[메시지]", true);
    }
    else if (name.equals("broadcastitem") && sender instanceof Player player)
    {
      List<String> list = new ArrayList<>();
      String arg = MessageUtil.listToString(args);
      if (arg.contains("[i1]"))
      {
        ItemStack item = player.getInventory().getItem(0);
        if (!ItemStackUtil.itemExists(item))
        {
          return Collections.singletonList("1번째 단축바 슬롯에 아이템을 들고 있지 않습니다.");
        }
      }
      else
      {
        list.add("[i1]");
      }
      if (arg.contains("[i2]"))
      {
        ItemStack item = player.getInventory().getItem(1);
        if (!ItemStackUtil.itemExists(item))
        {
          return Collections.singletonList("2번째 단축바 슬롯에 아이템을 들고 있지 않습니다.");
        }
      }
      else
      {
        list.add("[i2]");
      }
      if (arg.contains("[i3]"))
      {
        ItemStack item = player.getInventory().getItem(2);
        if (!ItemStackUtil.itemExists(item))
        {
          return Collections.singletonList("3번째 단축바 슬롯에 아이템을 들고 있지 않습니다.");
        }
      }
      else
      {
        list.add("[i3]");
      }
      if (arg.contains("[i4]"))
      {
        ItemStack item = player.getInventory().getItem(3);
        if (!ItemStackUtil.itemExists(item))
        {
          return Collections.singletonList("4번째 단축바 슬롯에 아이템을 들고 있지 않습니다.");
        }
      }
      else
      {
        list.add("[i4]");
      }
      if (arg.contains("[i5]"))
      {
        ItemStack item = player.getInventory().getItem(4);
        if (!ItemStackUtil.itemExists(item))
        {
          return Collections.singletonList("5번째 단축바 슬롯에 아이템을 들고 있지 않습니다.");
        }
      }
      else
      {
        list.add("[i5]");
      }
      if (arg.contains("[i6]"))
      {
        ItemStack item = player.getInventory().getItem(5);
        if (!ItemStackUtil.itemExists(item))
        {
          return Collections.singletonList("6번째 단축바 슬롯에 아이템을 들고 있지 않습니다.");
        }
      }
      else
      {
        list.add("[i6]");
      }
      if (arg.contains("[i7]"))
      {
        ItemStack item = player.getInventory().getItem(6);
        if (!ItemStackUtil.itemExists(item))
        {
          return Collections.singletonList("7번째 단축바 슬롯에 아이템을 들고 있지 않습니다.");
        }
      }
      else
      {
        list.add("[i7]");
      }
      if (arg.contains("[i8]"))
      {
        ItemStack item = player.getInventory().getItem(7);
        if (!ItemStackUtil.itemExists(item))
        {
          return Collections.singletonList("8번째 단축바 슬롯에 아이템을 들고 있지 않습니다.");
        }
      }
      else
      {
        list.add("[i8]");
      }
      if (arg.contains("[i9]"))
      {
        ItemStack item = player.getInventory().getItem(8);
        if (!ItemStackUtil.itemExists(item))
        {
          return Collections.singletonList("9번째 단축바 슬롯에 아이템을 들고 있지 않습니다.");
        }
      }
      else
      {
        list.add("[i9]");
      }
      if (arg.contains("[i]"))
      {
        ItemStack item = player.getInventory().getItemInMainHand();
        if (!ItemStackUtil.itemExists(item))
        {
          return Collections.singletonList(Prefix.NO_HOLDING_ITEM.toString());
        }
      }
      else
      {
        list.add("[i]");
      }
      list.add("[메시지]");
      return Method.tabCompleterList(args, list, "[메시지]", true);
    }
    else if (name.equals("customfix"))
    {
      if (args.length == 1)
      {
        return Method.tabCompleterBoolean(args, "[명령어 출력 숨김 여부]");
      }
    }
    else if (name.equals("editblockdata") && sender instanceof Player player)
    {
      ItemStack item = player.getInventory().getItemInMainHand();
      if (!ItemStackUtil.itemExists(item))
      {
        return Collections.singletonList(Prefix.NO_HOLDING_ITEM.toString());
      }
      String itemName = MessageUtil.stripColor(ComponentUtil.itemName(item).toString());
      String[] keys = BlockDataInfo.getBlockDataKeys(item.getType());
      if (keys == null)
      {
        return Collections.singletonList(itemName + "에는 속성이 없습니다.");
      }
      if (length == 1)
      {
        return Method.tabCompleterList(args, "<속성>", keys);
      }
      else if (length == 2)
      {
        String[] values = BlockDataInfo.getBlockDataValues(item.getType(), args[0]);
        if (!args[1].equals("--remove") && values == null)
        {
          if (!Method.equals(args[0], keys))
          {
            return Collections.singletonList(args[0] + MessageUtil.getFinalConsonant(args[0], MessageUtil.ConsonantType.은는) + " 잘못되거나 알 수 없는 속성입니다.");
          }
          return Collections.singletonList(args[1] + MessageUtil.getFinalConsonant(args[1], MessageUtil.ConsonantType.은는) + " 잘못되거나 알 수 없는 값입니다.");
        }
        return Method.tabCompleterList(args, Method.addAll(Method.arrayToList(values), "--remove"), "<값>");
      }
    }
    else if (name.equals("consolesudo"))
    {
      return this.getCommandsTabCompleter(sender, args, 1, false);
    }
    else if (name.equals("velocity2"))
    {
      if (args.length == 1)
      {
        return Method.tabCompleterEntity(sender, args, "<개체>", true);
      }

      Entity entity = SelectorUtil.getEntity(sender, args[0], false);

      String x = "~", y = "~", z = "~";
      if (entity != null)
      {
        Vector vector = entity.getVelocity();
        x = Constant.Sosu2.format(vector.getX());
        y = Constant.Sosu2.format(vector.getY());
        z = Constant.Sosu2.format(vector.getZ());
      }

      switch (length)
      {
        case 2:
          return Method.tabCompleterDoubleRadius(args, -Double.MAX_VALUE, Double.MAX_VALUE, "<x축 속력> <y축 속력> <z축 속력>", "~ ~ ~", "~ ~", "~", x + " " + y + " " + z, x + " " + y, x);
        case 3:
          return Method.tabCompleterDoubleRadius(args, -Double.MAX_VALUE, Double.MAX_VALUE, "<y축 속력> <z축 속력>", "~ ~", "~", y + " " + z, y);
        case 4:
          return Method.tabCompleterDoubleRadius(args, -Double.MAX_VALUE, Double.MAX_VALUE, "<z축 속력>", "~", z);
      }
    }
    else if (name.equals("quickshopaddon"))
    {
      return Collections.singletonList("QuickShop 플러그인을 사용하고 있지 않습니다.");
    }
    else if (name.equals("repeat"))
    {
      if (args.length == 1)
      {
        return Method.tabCompleterIntegerRadius(args, 1, 10000, "<반복 횟수>");
      }
      else if (args.length == 2)
      {
        return Method.tabCompleterIntegerRadius(args, 0, 20 * 60 * 60, "<딜레이(틱)>");
      }
      else
      {
        return this.getCommandsTabCompleter(sender, args, 3, true);
      }
    }
    else if (name.equals("socialmenu"))
    {
      if (args.length == 1)
      {
        return Method.tabCompleterPlayer(sender, args);
      }
    }
    else if (name.equals("ckill2"))
    {
      if (length == 1)
      {
        return Method.tabCompleterEntity(sender, args, "<죽일 개체>", true);
      }
      else if (length == 2)
      {
        return Method.tabCompleterBoolean(args, "[명령어 출력 숨김 여부]");
      }
    }
    else if (name.equals("testcommand"))
    {
      if (length == 1)
      {
        return Method.tabCompleterList(args, "asdfasdf", "entities", "entity", "players", "player");
      }
      else if (length == 2)
      {
        switch (args[0])
        {
          case "entities", "entity" ->
                  {
                    return Method.tabCompleterEntity(sender, args);
                  }
                  case "players", "player" ->
                          {
                            return Method.tabCompleterPlayer(sender, args);
                          }
        }
      }
    }
    return Collections.singletonList(Prefix.ARGS_LONG.toString());
  }

  private List<String> customItemTabCompleter(Player player, String[] args)
  {
    ItemStack item = player.getInventory().getItemInMainHand();
    int length = args.length;
    if (!ItemStackUtil.itemExists(item))
    {
      return Collections.singletonList(Prefix.NO_HOLDING_ITEM.toString());
    }
    NBTCompound itemTag = NBTAPI.getMainCompound(item);
    NBTCompound customItemTag = NBTAPI.getCompound(itemTag, CucumberyTag.CUSTOM_ITEM_TAG_KEY);
    String customItemType = NBTAPI.getString(customItemTag, CucumberyTag.ID_KEY);
    if (customItemType == null)
    {
      return Collections.singletonList("주로 사용하는 손에 들고 있는 아이템에 커스텀 아이템 태그가 없습니다.");
    }
    switch (customItemType)
    {
      case CucumberyTag.CUSTOM_ITEM_RAILGUN_ID:
        if (length == 3)
        {
          return Method.tabCompleterList(args, "<태그>", "range", "sortparticle", "ignoreinvincible", "density", "damage", "blockpenetrate", "fireworkrocketrequired", "cooldown", "cooldowntag", "piercing",
                  "laserwidth", "fireworktype", "reverse", "suicide");
        }
        else if (length == 4)
        {
          switch (args[2])
          {
            case "range":
              return Method.tabCompleterIntegerRadius(args, 1, 256, "<사정 거리(m)>");
            case "sortparticle":
              return Method.tabCompleterBoolean(args, "<입자 정렬>");
            case "ignoreinvincible":
              return Method.tabCompleterBoolean(args, "<피해 무효 무시>");
            case "blockpenetrate":
              return Method.tabCompleterBoolean(args, "<블록 관통>");
            case "fireworkrocketrequired":
              return Method.tabCompleterBoolean(args, "<탄환용 폭죽 필요>");
            case "reverse":
              return Method.tabCompleterBoolean(args, "<뒤로 발사>");
            case "suicide":
              return Method.tabCompleterBoolean(args, "<자살 모드>");
            case "density":
              return Method.tabCompleterIntegerRadius(args, 0, 10, "<블록당 폭죽 입자 밀도>");
            case "piercing":
              return Method.tabCompleterIntegerRadius(args, 0, 100, "<관통 횟수>");
            case "fireworktype":
              return Method.tabCompleterIntegerRadius(args, 1, 10, "<폭죽 타입>");
            case "cooldown":
              return Method.tabCompleterDoubleRadius(args, 0, 3600, "<재발사 대기 시간(초)>");
            case "cooldowntag":
              return Method.tabCompleterList(args, "<재발사 대기 시간 태그>", true);
            case "damage":
              return Method.tabCompleterDoubleRadius(args, 0, Double.MAX_VALUE, "<대미지>");
            case "laserwidth":
              return Method.tabCompleterDoubleRadius(args, 0, 100, "<레이저 두께>");
          }
        }
        break;
      case CucumberyTag.CUSTOM_ITEM_FISHING_LOD_ID:
        if (item.getType() != Material.FISHING_ROD)
        {
          return Collections.singletonList("해당 태그는 낚싯대에만 사용할 수 있습니다.");
        }
        if (length == 3)
        {
          return Method.tabCompleterList(args, "<태그>", "multiplier", "x", "y", "z");
        }
        else if (length == 4)
        {
          switch (args[2])
          {
            case "multiplier":
              return Method.tabCompleterDoubleRadius(args, 0d, 5d, "<반동 비율>");
            case "x":
            case "y":
            case "z":
              return Method.tabCompleterDoubleRadius(args, 0d, 5d, "<" + args[2] + "축 최대 반동값>");
          }
        }
        break;
    }
    return Collections.singletonList(Prefix.ARGS_LONG.toString());
  }

  private List<String> getCommandsTabCompleter(CommandSender sender, String[] args, int length, boolean forSudo)
  {
    if (args.length == length)
    {
      if (!forSudo)
      {
        return Method.tabCompleterList(args, Method.getAllServerCommands(), "<명령어>", true);
      }
      List<String> cmds = Method.getAllServerCommands();
      List<String> newCmds = new ArrayList<>();
      for (String cmd2 : cmds)
      {
        newCmds.add(cmd2);
        newCmds.add("op:" + cmd2);
      }
      return Method.tabCompleterList(args, newCmds, "<명령어>", true);
    }
    else
    {
      String cmdLabel = args[length - 1];
      if (forSudo)
      {
        if (cmdLabel.startsWith("op:"))
        {
          cmdLabel = cmdLabel.substring(3);
        }
      }
      if (args.length == length + 1 && Method.equals(cmdLabel, "?", "bukkit:?", "bukkit:help"))
      {
        return Method.tabCompleterList(args, Method.getAllServerCommands(), "<명령어>", true);
      }
      PluginCommand command = Bukkit.getServer().getPluginCommand(cmdLabel);
      String[] args2 = new String[args.length - length];
      System.arraycopy(args, length, args2, 0, args.length - length);
      if (command != null)
      {
        org.bukkit.command.TabCompleter completer = command.getTabCompleter();
        if (completer != null)
        {
          return completer.onTabComplete(sender, command, command.getLabel(), args2);
        }
      }
      return Collections.singletonList("[<인수>]");
    }
  }
}