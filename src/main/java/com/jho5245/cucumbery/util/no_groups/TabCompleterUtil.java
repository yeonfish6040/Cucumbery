package com.jho5245.cucumbery.util.no_groups;

import com.jho5245.cucumbery.util.nbt.CucumberyTag;
import com.jho5245.cucumbery.util.nbt.NBTAPI;
import com.jho5245.cucumbery.util.storage.no_groups.ItemStackUtil;
import com.jho5245.cucumbery.util.storage.data.Prefix;
import de.tr7zw.changeme.nbtapi.NBTCompound;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TabCompleterUtil
{
  public static List<String> customItemTabCompleter(Player player, String[] args)
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
      return Collections.singletonList("주로 사용하는 손에 들고 있는 아이템에 커스텀 아이템 태그가 없습니다");
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
          return Collections.singletonList("해당 태그는 낚싯대에만 사용할 수 있습니다");
        }
        if (length == 3)
        {
          return Method.tabCompleterList(args, "<태그>", "multiplier", "x", "y", "z", "allow-on-air");
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
            case "allow-on-air":
              return Method.tabCompleterBoolean(args, "<공중으로 도약 가능 여부>");
          }
        }
        break;
    }
    return Collections.singletonList(Prefix.ARGS_LONG.toString());
  }

  public static List<String> getCommandsTabCompleter(CommandSender sender, String[] args, int length, boolean forSudo)
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
