package com.jho5245.cucumbery.commands.itemtag;

import com.jho5245.cucumbery.custom.customeffect.CustomEffect.DisplayType;
import com.jho5245.cucumbery.custom.customeffect.type.CustomEffectType;
import com.jho5245.cucumbery.util.nbt.CucumberyTag;
import com.jho5245.cucumbery.util.nbt.NBTAPI;
import com.jho5245.cucumbery.util.no_groups.CommandTabUtil;
import com.jho5245.cucumbery.util.no_groups.MessageUtil;
import com.jho5245.cucumbery.util.no_groups.Method;
import com.jho5245.cucumbery.util.storage.component.util.ComponentUtil;
import com.jho5245.cucumbery.util.storage.component.util.ItemNameUtil;
import com.jho5245.cucumbery.util.storage.data.Constant;
import com.jho5245.cucumbery.util.storage.data.Constant.ItemUsageType;
import com.jho5245.cucumbery.util.storage.data.Constant.RestrictionType;
import com.jho5245.cucumbery.util.storage.data.Prefix;
import com.jho5245.cucumbery.util.storage.data.Variable;
import com.jho5245.cucumbery.util.storage.no_groups.ItemCategory;
import com.jho5245.cucumbery.util.storage.no_groups.ItemStackUtil;
import de.tr7zw.changeme.nbtapi.*;
import de.tr7zw.changeme.nbtapi.iface.ReadWriteNBT;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.TabCompleter;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class CommandItemTagTabCompleter implements TabCompleter
{
  public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args)
  {
    if (!(sender instanceof Player player))
    {
      return Collections.emptyList();
    }
    if (!MessageUtil.checkQuoteIsValidInArgs(sender, args = MessageUtil.wrapWithQuote(true, args), true))
    {
      return Collections.singletonList(args[0]);
    }
    int length = args.length;
    String lastArg = args[length - 1];
    ItemStack item = player.getInventory().getItemInMainHand();
    if (!ItemStackUtil.itemExists(item))
    {
      return Collections.singletonList(Prefix.NO_HOLDING_ITEM.toString());
    }
    Material material = item.getType();
    final boolean isPotionType = material == Material.POTION || material == Material.SPLASH_POTION || material == Material.LINGERING_POTION || material == Material.TIPPED_ARROW || ItemStackUtil.isEdible(material);
    switch (length)
    {
      case 1:
      {
        List<String> list = Method.tabCompleterList(args, "<태그>", "restriction", "customlore", "extratag", Constant.TAB_COMPLETER_QUOTE_ESCAPE + "customdurability",
                "customitemtype", "hideflag", "customrarity", "usage", "expiredate", Constant.TAB_COMPLETER_QUOTE_ESCAPE + "tnt" + (material != Material.TNT ? "(TNT 전용)" : ""), "abovecustomlore",
                "customitem", Constant.TAB_COMPLETER_QUOTE_ESCAPE + "food" + (ItemStackUtil.isEdible(material) ? "" : "(먹을 수 있는 아이템 전용)"), "id", "nbt", "customtag", Constant.TAB_COMPLETER_QUOTE_ESCAPE + "potion" +
                        (isPotionType ? "" : "(음식 또는 포션 유형의 아이템 전용)"), "customdisplayname");
        if (args[0].equals("tnt") && material != Material.TNT)
        {
          return Collections.singletonList("해당 태그는 TNT에만 사용할 수 있습니다");
        }
        if (args[0].equals("food") && !ItemStackUtil.isEdible(material))
        {
          return Collections.singletonList("해당 태그는 먹을 수 있는 아이템에만 사용할 수 있습니다");
        }
        if (args[0].equals("potion") && !isPotionType)
        {
          return Collections.singletonList("해당 태그는 음식 또는 포션 유형의 아이템에만 사용할 수 있습니다");
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
              return Collections.singletonList("해당 태그는 TNT에만 사용할 수 있습니다");
            }
            return Method.tabCompleterList(args, "<인수>", "unstable", "ignite", "fuse", "fire", "explode-power");
          case "customitem":
            return Method.tabCompleterList(args, "<인수>", "setid", "modify");
          case "food":
            if (!ItemStackUtil.isEdible(material))
            {
              return Collections.singletonList("해당 태그는 먹을 수 있는 아이템에만 사용할 수 있습니다");
            }
            boolean hasEffects = ItemStackUtil.hasStatusEffect(material);
            return Method.tabCompleterList(args, "<인수>", "disable-status-effect" + (hasEffects ? "" : "(상태 효과에 영향을 줄 수 있는 아이템 전용)"), "food-level", "saturation", "nourishment");
          case "nbt":
            return Method.tabCompleterList(args, "<인수>", "set", "remove", "merge");
          case "potion":
            if (!isPotionType)
            {
              return Collections.singletonList("해당 태그는 음식 또는 포션 유형의 아이템에만 사용할 수 있습니다");
            }
            return Method.tabCompleterList(args, "<인수>", "list", "add", "remove", "set");
          case "customdisplayname":
            return Method.tabCompleterList(args, "<인수>", "prefix", "suffix", "name", "remove");
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
              case "add", "remove" -> {
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
                  return Collections.singletonList("해당 태그는 셜커 상자에만 사용할 수 있습니다");
                }
                if (enderPearlExclusive && args[2].equalsIgnoreCase(Constant.ExtraTag.NO_COOLDOWN_ENDER_PEARL.toString()))
                {
                  return Collections.singletonList("해당 태그는 엔더 진주에만 사용할 수 있습니다");
                }
                return list;
              }
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
                  return Collections.singletonList(ItemUsageType.valueOf(args[2].toUpperCase()).getDisplay() + " 태그에는 소비 확률을 적용할 수 없습니다");
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
              case "fire":
                return Method.tabCompleterBoolean(args, "<폭발 시 불 번짐 여부>");
              case "explode-power":
                return Method.tabCompleterDoubleRadius(args, 0, 500, "<폭발 강도>", "-1", "-1(초기화)");
            }
            break;
          case "customitem":
            switch (args[1])
            {
              case "setid":
                List<String> list = Method.tabCompleterList(args, "<아이디>", "railgun", "fishingrod" + (material != Material.FISHING_ROD ? "(낚싯대 전용)" : ""), "--remove");
                if (args[2].equals("fishingrod") && material != Material.FISHING_ROD)
                {
                  return Collections.singletonList("해당 태그는 낚싯대에만 사용할 수 있습니다");
                }
                return list;
              case "modify":
                return CommandTabUtil.customItemTabCompleter(player, args);
            }
            break;
          case "customdurability":
            switch (args[1])
            {
              case "chance":
                return Method.tabCompleterDoubleRadius(args, 0, 100, "<내구도 감소 무효 확률(%)>");
              case "durability":
                return Method.tabCompleterLongRadius(args, 0, Long.MAX_VALUE, "(<내구도>|<현재 내구도> <최대 내구도>)", "0", "0 (삭제)");
            }
            break;
          case "food":
            if (!ItemStackUtil.isEdible(material))
            {
              return Collections.singletonList("해당 태그는 먹을 수 있는 아이템에만 사용할 수 있습니다");
            }
            switch (args[1])
            {
              case "disable-status-effect":
                boolean hasEffects = ItemStackUtil.hasStatusEffect(material);
                if (!hasEffects)
                {
                  return Collections.singletonList("해당 태그는 상태 효과에 영향을 줄 수 있는 아이템에만 사용할 수 있습니다");
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
          case "potion":
            if (!isPotionType)
            {
              return Collections.singletonList("해당 태그는 음식 또는 포션 유형의 아이템에만 사용할 수 있습니다");
            }
            switch (args[1])
            {
              case "remove" -> {
                NBTCompoundList nbtCompoundList = NBTAPI.getCompoundList(NBTAPI.getMainCompound(item), CucumberyTag.CUSTOM_EFFECTS);
                if (nbtCompoundList == null || nbtCompoundList.isEmpty())
                {
                  return Collections.singletonList(MessageUtil.stripColor(ComponentUtil.serialize(ItemNameUtil.itemName(item))) + "에는 효과가 없습니다");
                }
                return Method.tabCompleterIntegerRadius(args, 1, nbtCompoundList.size(), "[줄]");
              }
              case "set" -> {
                NBTCompoundList nbtCompoundList = NBTAPI.getCompoundList(NBTAPI.getMainCompound(item), CucumberyTag.CUSTOM_EFFECTS);
                if (nbtCompoundList == null || nbtCompoundList.isEmpty())
                {
                  return Collections.singletonList(MessageUtil.stripColor(ComponentUtil.serialize(ItemNameUtil.itemName(item))) + "에는 효과가 없습니다");
                }
                return Method.tabCompleterIntegerRadius(args, 1, nbtCompoundList.size(), "<줄>");
              }
              case "add" -> {
                return Method.tabCompleterList(args, CustomEffectType.getEffectTypeMap(), "<효과>");
              }
            }
            break;
          case "customdisplayname":
            switch (args[1])
            {
              case "prefix":
              case "suffix":
                return Method.tabCompleterList(args, "<인수>", "add", "remove", "list", "set");
              case "name":
                return Method.tabCompleterList(args, "<이름|--remove>", true, "--remove");
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
              return CommandTabUtil.customItemTabCompleter(player, args);
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
            if ("durability".equals(args[1]))
            {
              return Method.tabCompleterLongRadius(args, 1, Long.MAX_VALUE, "[최대 내구도]");
            }
            break;
          case "usage":
            switch (args[1])
            {
              case "command" -> {
                try
                {
                  ItemUsageType.valueOf(args[2].toUpperCase());
                }
                catch (Exception e)
                {
                  return Collections.singletonList(args[2] + MessageUtil.getFinalConsonant(args[2], MessageUtil.ConsonantType.은는) + " 잘못되거나 알 수 없는 실행 유형입니다");
                }
                return Method.tabCompleterList(args, "<인수>", "add", "remove", "list", "set", "insert");
              }
              case "cooldown" -> {
                try
                {
                  ItemUsageType.valueOf(args[2].toUpperCase());
                }
                catch (Exception e)
                {
                  return Collections.singletonList(args[2] + MessageUtil.getFinalConsonant(args[2], MessageUtil.ConsonantType.은는) + " 잘못되거나 알 수 없는 실행 유형입니다");
                }
                return Method.tabCompleterList(args, "<인수>", "tag", "time");
              }
              case "disposable" -> {
                String display;
                try
                {
                  ItemUsageType itemUsageType = ItemUsageType.valueOf(args[2].toUpperCase());
                  display = itemUsageType.getDisplay();
                }
                catch (Exception e)
                {
                  return Collections.singletonList(args[2] + MessageUtil.getFinalConsonant(args[2], MessageUtil.ConsonantType.은는) + " 잘못되거나 알 수 없는 실행 유형입니다");
                }
                if (args[2].contains("attack"))
                {
                  return Collections.singletonList(display + " 태그에는 소비 확률을 적용할 수 없습니다");
                }
                return Method.tabCompleterDoubleRadius(args, 0, 100, "<" + display + " 시 소비 확률(%)>", "-1");
              }
              case "permission" -> {
                try
                {
                  ItemUsageType.valueOf(args[2].toUpperCase());
                }
                catch (Exception e)
                {
                  return Collections.singletonList(args[2] + MessageUtil.getFinalConsonant(args[2], MessageUtil.ConsonantType.은는) + " 잘못되거나 알 수 없는 실행 유형입니다");
                }
                return Method.tabCompleterList(args, "<퍼미션 노드>", true, "--remove", "<퍼미션 노드>");
              }
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
                  return Collections.singletonList(args[2] + MessageUtil.getFinalConsonant(args[2], MessageUtil.ConsonantType.은는) + " 잘못되거나 알 수 없는 자료형입니다");
              }
            }
            break;
          case "potion":
            if (!isPotionType)
            {
              return Collections.singletonList("해당 태그는 음식 또는 포션 유형의 아이템에만 사용할 수 있습니다");
            }
            switch (args[1])
            {
              case "set" -> {
                return Method.tabCompleterList(args, CustomEffectType.getEffectTypeMap(), "<효과>");
              }
              case "add" -> {
                return Method.tabCompleterDoubleRadius(args, 0.05, Integer.MAX_VALUE / 20d, "[지속 시간(초)]", "infinite", "default");
              }
            }
            break;
          case "customdisplayname":
            switch (args[1])
            {
              case "prefix", "suffix" -> {
                NBTCompound displayCompound = NBTAPI.getCompound(NBTAPI.getMainCompound(item), CucumberyTag.ITEMSTACK_DISPLAY_KEY);
                boolean isPrefix = args[1].equals("prefix");
                switch (args[2])
                {
                  case "add" -> {
                    return Method.tabCompleterList(args, isPrefix ? "<접두어>" : "<접미어>", true);
                  }
                  case "remove" -> {
                    if (isPrefix)
                    {
                      NBTCompoundList prefixList = NBTAPI.getCompoundList(displayCompound, CucumberyTag.ITEMSTACK_DISPLAY_PREFIX);
                      if (prefixList == null || prefixList.isEmpty())
                      {
                        return Collections.singletonList(MessageUtil.stripColor(ComponentUtil.serialize(ComponentUtil.translate("%s에는 접두어가 없습니다", item))));
                      }
                      return Method.tabCompleterIntegerRadius(args, 1, prefixList.size(), "[줄(1~" + prefixList.size() + ")|--all]", "--all");
                    }
                    else
                    {
                      NBTCompoundList suffixList = NBTAPI.getCompoundList(displayCompound, CucumberyTag.ITEMSTACK_DISPLAY_SUFFIX);
                      if (suffixList == null || suffixList.isEmpty())
                      {
                        return Collections.singletonList(MessageUtil.stripColor(ComponentUtil.serialize(ComponentUtil.translate("%s에는 접미어가 없습니다", item))));
                      }
                      return Method.tabCompleterIntegerRadius(args, 1, suffixList.size(), "[줄(1~" + suffixList.size() + ")|--all]", "--all");
                    }
                  }
                  case "set" -> {
                    return Method.tabCompleterIntegerRadius(args, 1, Integer.MAX_VALUE, "<줄>");
                  }
                }
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
                return Collections.singletonList(args[2] + MessageUtil.getFinalConsonant(args[2], MessageUtil.ConsonantType.은는) + " 잘못되거나 알 수 없는 실행 유형입니다");
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
                  boolean exists = nbtItem.hasTag(key) && nbtItem.getType(key) == NBTType.NBTTagByte;
                  byte b = nbtItem.getByte(key);
                  exists = exists && (b == 0 || b == 1);
                  boolean bool = nbtItem.getBoolean(key);
                  return Method.tabCompleterBoolean(args, "<값>", exists ? bool + "(기존값)" : null);
                case "byte":
                  exists = nbtItem.hasTag(key) && nbtItem.getType(key) == NBTType.NBTTagByte;
                  b = nbtItem.getByte(key);
                  return Method.tabCompleterIntegerRadius(args, Byte.MIN_VALUE, Byte.MAX_VALUE, "<값>", exists ? b + "(기존값)" : "");
                case "byte-array":
                  if (input.startsWith(","))
                  {
                    return Collections.singletonList("정수가 필요합니다 (" + input + ")");
                  }
                  String[] split = input.split(",");
                  if (!input.equals(""))
                  {
                    for (String s : split)
                    {
                      if (!MessageUtil.isInteger(sender, s, false))
                      {
                        return Collections.singletonList("정수가 필요합니다 (" + s + ")");
                      }
                      int j = Integer.parseInt(s);
                      if (j > Byte.MAX_VALUE)
                      {
                        String valueString = Constant.Sosu15.format(j);
                        valueString += MessageUtil.getFinalConsonant(valueString, MessageUtil.ConsonantType.이가);
                        return Collections.singletonList("정수는 " + Constant.Sosu15.format(Byte.MAX_VALUE) + " 이하여야 하는데, " + valueString + " 있습니다");
                      }
                      else if (j < Byte.MIN_VALUE)
                      {
                        String valueString = Constant.Sosu15.format(j);
                        valueString += MessageUtil.getFinalConsonant(valueString, MessageUtil.ConsonantType.이가);
                        return Collections.singletonList("정수는 " + Constant.Sosu15.format(Byte.MIN_VALUE) + " 이상이여야 하는데, " + valueString + " 있습니다");
                      }
                    }
                  }
                  exists = nbtItem.hasTag(key) && nbtItem.getType(key) == NBTType.NBTTagByteArray;
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
                  exists = nbtItem.hasTag(key) && nbtItem.getType(key) == NBTType.NBTTagShort;
                  short s = nbtItem.getShort(key);
                  return Method.tabCompleterIntegerRadius(args, Short.MIN_VALUE, Short.MAX_VALUE, "<값>", exists ? s + "(기존값)" : "");
                case "int":
                  exists = nbtItem.hasTag(key) && nbtItem.getType(key) == NBTType.NBTTagInt;
                  int i = nbtItem.getInteger(key);
                  return Method.tabCompleterIntegerRadius(args, Integer.MIN_VALUE, Integer.MAX_VALUE, "<값>", exists ? i + "(기존값)" : "");
                case "int-array":
                  if (input.startsWith(","))
                  {
                    return Collections.singletonList("정수가 필요합니다 (" + input + ")");
                  }
                  split = input.split(",");
                  if (!input.equals(""))
                  {
                    for (String s2 : split)
                    {
                      if (!MessageUtil.isInteger(sender, s2, false))
                      {
                        return Collections.singletonList("정수가 필요합니다 (" + s2 + ")");
                      }
                    }
                  }
                  exists = nbtItem.hasTag(key) && nbtItem.getType(key) == NBTType.NBTTagIntArray;
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
                    return Collections.singletonList("정수가 필요합니다 (" + input + ")");
                  }
                  split = input.split(",");
                  if (!input.equals(""))
                  {
                    for (String s2 : split)
                    {
                      if (!MessageUtil.isInteger(sender, s2, false))
                      {
                        return Collections.singletonList("정수가 필요합니다 (" + s2 + ")");
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
                    return Collections.singletonList("정수가 필요합니다 (" + input + ")");
                  }
                  split = input.split(",");
                  if (!input.equals(""))
                  {
                    for (String s2 : split)
                    {
                      if (!MessageUtil.isLong(sender, s2, false))
                      {
                        return Collections.singletonList("정수가 필요합니다 (" + s2 + ")");
                      }
                    }
                  }
                  exists = nbtItem.hasTag(key) && nbtItem.getType(key) == NBTType.NBTTagLong;
                  long l = nbtItem.getLong(key);
                  return Method.tabCompleterLongRadius(args, Long.MIN_VALUE, Long.MAX_VALUE, "<값>", exists ? l + "(기존값)" : "");
                case "long-list":
                  if (input.startsWith(","))
                  {
                    return Collections.singletonList("정수가 필요합니다 (" + input + ")");
                  }
                  split = input.split(",");
                  if (!input.equals(""))
                  {
                    for (String s2 : split)
                    {
                      if (!MessageUtil.isLong(sender, s2, false))
                      {
                        return Collections.singletonList("정수가 필요합니다 (" + s2 + ")");
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
                  exists = nbtItem.hasTag(key) && nbtItem.getType(key) == NBTType.NBTTagFloat;
                  float f = nbtItem.getFloat(key);
                  return Method.tabCompleterDoubleRadius(args, -Float.MAX_VALUE, Float.MAX_VALUE, "<값>", exists ? f + "(기존값)" : "");
                case "float-list":
                  if (input.startsWith(","))
                  {
                    return Collections.singletonList("숫자가 필요합니다 (" + input + ")");
                  }
                  split = input.split(",");
                  if (!input.equals(""))
                  {
                    for (String s2 : split)
                    {
                      if (!MessageUtil.isDouble(sender, s2, false))
                      {
                        return Collections.singletonList("숫자가 필요합니다 (" + s2 + ")");
                      }
                      double j = Double.parseDouble(s2);
                      if (j > Float.MAX_VALUE)
                      {
                        String valueString = Constant.Sosu15.format(j);
                        valueString += MessageUtil.getFinalConsonant(valueString, MessageUtil.ConsonantType.이가);
                        return Collections.singletonList("숫자는 " + Constant.Sosu15.format(Byte.MAX_VALUE) + " 이하여야 하는데, " + valueString + " 있습니다");
                      }
                      else if (j < -Float.MAX_VALUE)
                      {
                        String valueString = Constant.Sosu15.format(j);
                        valueString += MessageUtil.getFinalConsonant(valueString, MessageUtil.ConsonantType.이가);
                        return Collections.singletonList("숫자는 " + Constant.Sosu15.format(Byte.MIN_VALUE) + " 이상이여야 하는데, " + valueString + " 있습니다");
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
                  exists = nbtItem.hasTag(key) && nbtItem.getType(key) == NBTType.NBTTagDouble;
                  double d = nbtItem.getDouble(key);
                  return Method.tabCompleterDoubleRadius(args, -Double.MAX_VALUE, Double.MAX_VALUE, "<값>", exists ? d + "(기존값)" : "");
                case "double-list":
                  if (input.startsWith(","))
                  {
                    return Collections.singletonList("숫자가 필요합니다 (" + input + ")");
                  }
                  split = input.split(",");
                  if (!input.equals(""))
                  {
                    for (String s2 : split)
                    {
                      if (!MessageUtil.isDouble(sender, s2, false))
                      {
                        return Collections.singletonList("숫자가 필요합니다 (" + s2 + ")");
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
                  exists = nbtItem.hasTag(key) && nbtItem.getType(key) == NBTType.NBTTagIntArray;
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
                  exists = nbtItem.hasTag(key) && nbtItem.getType(key) == NBTType.NBTTagString;
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
                  return Collections.singletonList(args[2] + MessageUtil.getFinalConsonant(args[2], MessageUtil.ConsonantType.은는) + " 잘못되거나 알 수 없는 자료형입니다");
              }
            }
            break;
          case "potion":
            if (!isPotionType)
            {
              return Collections.singletonList("해당 태그는 음식 또는 포션 유형의 아이템에만 사용할 수 있습니다");
            }
            switch (args[1])
            {
              case "set" -> {
                return Method.tabCompleterDoubleRadius(args, 0.05, Integer.MAX_VALUE / 20d, "[지속 시간(초)]", "infinite", "default");
              }
              case "add" -> {
                String effect = args[2];
                CustomEffectType customEffectType;
                try
                {
                  customEffectType = CustomEffectType.valueOf(effect);
                }
                catch (Exception e)
                {
                  return Collections.singletonList(effect + MessageUtil.getFinalConsonant(effect, MessageUtil.ConsonantType.은는) + " 잘못되거나 알 수 없는 효과입니다");
                }
                return Method.tabCompleterIntegerRadius(args, 0, customEffectType.getMaxAmplifier(), "[농도 레벨]", "max");
              }
            }
            break;

          case "customdisplayname":
            switch (args[1])
            {
              case "prefix", "suffix" -> {
                boolean isPrefix = args[1].equals("prefix");
                switch (args[2])
                {
                  case "add" -> {
                    String nbt = args[4];
                    if (!nbt.equals(""))
                    try
                    {
                      new NBTContainer(nbt);
                    }
                    catch (Exception exception)
                    {
                      return Collections.singletonList("잘못된 NBT입니다:" + nbt);
                    }
                    return Method.tabCompleterList(args, "[nbt]", true);
                  }
                  case "set" -> {
                    return Method.tabCompleterList(args, isPrefix ? "<접두어>" : "<접미어>", true);
                  }
                }
              }
            }
            break;
        }
        break;
      case 6:
      {
        if (args[0].equals("potion"))
        {
          if (!isPotionType)
          {
            return Collections.singletonList("해당 태그는 음식 또는 포션 유형의 아이템에만 사용할 수 있습니다");
          }
          switch (args[1])
          {
            case "set" -> {
              String effect = args[3];
              CustomEffectType customEffectType;
              try
              {
                customEffectType = CustomEffectType.valueOf(effect.toUpperCase());
              }
              catch (Exception e)
              {
                return Collections.singletonList(effect + MessageUtil.getFinalConsonant(effect, MessageUtil.ConsonantType.은는) + " 잘못되거나 알 수 없는 효과입니다");
              }
              return Method.tabCompleterIntegerRadius(args, 0, customEffectType.getMaxAmplifier(), "[농도 레벨]", "max");
            }
            case "add" -> {
              String effect = args[2];
              CustomEffectType customEffectType;
              try
              {
                customEffectType = CustomEffectType.valueOf(effect.toUpperCase());
              }
              catch (Exception e)
              {
                return Collections.singletonList(effect + MessageUtil.getFinalConsonant(effect, MessageUtil.ConsonantType.은는) + " 잘못되거나 알 수 없는 효과입니다");
              }
              List<String> list = new ArrayList<>(Method.enumToList(DisplayType.values()));
              list.add(customEffectType.getDefaultDisplayType().toString().toLowerCase() + "(기본값)");
              return Method.tabCompleterList(args, list, "[표시 유형]");
            }
          }
        }
        if (args[0].equals("customdisplayname"))
        {
          switch (args[1])
          {
            case "prefix", "suffix" -> {
              if ("set".equals(args[2]))
              {
                String nbt = args[5];
                if (!nbt.equals(""))
                  try
                  {
                    new NBTContainer(nbt);
                  }
                  catch (Exception exception)
                  {
                    return Collections.singletonList("잘못된 NBT입니다:" + nbt);
                  }
                return Method.tabCompleterList(args, "[nbt]", true);
              }
            }
          }
        }
        break;
      }
      case 7:
      {
        if (args[0].equals("potion"))
        {
          if (!isPotionType)
          {
            return Collections.singletonList("해당 태그는 음식 또는 포션 유형의 아이템에만 사용할 수 있습니다");
          }
          if (args[1].equals("set"))
          {
            String effect = args[3];
            CustomEffectType customEffectType;
            try
            {
              customEffectType = CustomEffectType.valueOf(effect.toUpperCase());
            }
            catch (Exception e)
            {
              return Collections.singletonList(effect + MessageUtil.getFinalConsonant(effect, MessageUtil.ConsonantType.은는) + " 잘못되거나 알 수 없는 효과입니다");
            }
            List<String> list = new ArrayList<>(Method.enumToList(DisplayType.values()));
            list.add(customEffectType.getDefaultDisplayType().toString().toLowerCase() + "(기본값)");
            return Method.tabCompleterList(args, list, "[표시 유형]");
          }
        }
      }
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
          case "add" -> {
            if (length == 3)
            {
              return Method.tabCompleterList(args, "<커스텀 설명>", true, "<커스텀 설명>", "--empty");
            }
            return Method.tabCompleterList(args, "[커스텀 설명]", true);
          }
          case "set" -> {
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
          case "remove" -> {
            {
              if (length == 3)
              {
                NBTList<String> customLore = NBTAPI.getStringList(NBTAPI.getMainCompound(item), CucumberyTag.CUSTOM_LORE_KEY);
                if (customLore == null || customLore.size() == 0)
                {
                  return Collections.singletonList("더 이상 제거할 수 있는 커스텀 설명이 없습니다");
                }
                return Method.tabCompleterIntegerRadius(args, 1, customLore.size(), "[줄]", "--all");
              }
            }
          }
          case "insert" -> {
            NBTList<String> customLore = NBTAPI.getStringList(NBTAPI.getMainCompound(item), CucumberyTag.CUSTOM_LORE_KEY);
            if (customLore == null || customLore.size() == 0)
            {
              return Collections.singletonList("커스텀 설명을 들여쓸 수 없습니다");
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
          case "add" -> {
            if (length == 3)
            {
              return Method.tabCompleterList(args, "<상단 커스텀 설명>", true, "<상단 커스텀 설명>", "--empty");
            }
            return Method.tabCompleterList(args, "[상단 커스텀 설명]", true);
          }
          case "set" -> {
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
          case "remove" -> {
            {
              if (length == 3)
              {
                NBTList<String> customLore = NBTAPI.getStringList(NBTAPI.getMainCompound(item), CucumberyTag.ABOVE_CUSTOM_LORE_KEY);
                if (customLore == null || customLore.size() == 0)
                {
                  return Collections.singletonList("더 이상 제거할 수 있는 상단 커스텀 설명이 없습니다");
                }
                return Method.tabCompleterIntegerRadius(args, 1, customLore.size(), "[줄]", "--all");
              }
            }
            break;
          }
          case "insert" -> {
            NBTList<String> customLore = NBTAPI.getStringList(NBTAPI.getMainCompound(item), CucumberyTag.ABOVE_CUSTOM_LORE_KEY);
            if (customLore == null || customLore.size() == 0)
            {
              return Collections.singletonList("상단 커스텀 설명을 들여쓸 수 없습니다");
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
//          Calendar calendar = Calendar.getInstance();
//          calendar.add(Calendar.HOUR_OF_DAY, Cucumbery.config.getInt("adjust-time-difference-value"));
          List<String> list = new ArrayList<>();
          list.add("--remove");
          list.addAll(Arrays.asList("~1분", "~2분", "~5분", "~10분", "~30분", "~1시간", "~1일", "~7일", "~14일", "~21일", "~30일", "~1년"));
//          calendar.add(Calendar.MINUTE, 1);
//          list.add(Method.getCurrentTime(calendar, true, false));
//          calendar.add(Calendar.MINUTE, 10);
//          list.add(Method.getCurrentTime(calendar, true, false));
//          calendar.add(Calendar.HOUR, 1);
//          list.add(Method.getCurrentTime(calendar, true, false));
//          calendar.add(Calendar.DATE, 1);
//          list.add(Method.getCurrentTime(calendar, true, false));
//          calendar.add(Calendar.DATE, 7);
//          list.add(Method.getCurrentTime(calendar, true, false));
//          calendar.add(Calendar.DATE, 14);
//          list.add(Method.getCurrentTime(calendar, true, false));
//          calendar.add(Calendar.DATE, 21);
//          list.add(Method.getCurrentTime(calendar, true, false));
//          calendar.add(Calendar.DATE, 30);
//          list.add(Method.getCurrentTime(calendar, true, false));
//          calendar.add(Calendar.DATE, 365);
//          list.add(Method.getCurrentTime(calendar, true, false));
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
            return Collections.singletonList(args[2] + MessageUtil.getFinalConsonant(args[2], MessageUtil.ConsonantType.은는) + " 잘못되거나 알 수 없는 실행 유형입니다");
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
                  return Collections.singletonList("아이템에 " + itemUsageType.getDisplay() + " 시 명령어 실행 태그 값이 없습니다");
                }
                return Method.tabCompleterIntegerRadius(args, 1, commands.size(), "[줄]", "--all");
              }
              break;
            }
            case "insert":
            {
              if (commands == null || commands.size() == 0)
              {
                return Collections.singletonList("아이템에 " + itemUsageType.getDisplay() + " 시 명령어 실행 태그 값이 없습니다");
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
        if (args[1].equals("merge"))
        {
          String input = MessageUtil.listToString(" ", 2, args.length, args);
          if (!input.equals(""))
          {
            try
            {
              new NBTContainer("{" + input + "}");
            }
            catch (Exception e)
            {
              return Collections.singletonList(input + MessageUtil.getFinalConsonant(input, MessageUtil.ConsonantType.은는) + " 잘못된 nbt입니다");
            }
          }
          if (args.length == 3)
          {
            return Method.tabCompleterList(args, "<nbt>", true, "<nbt>", "예시 : foo:bar,nbt:{extra:\"nbts\"}");
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
                  return Collections.singletonList(input + MessageUtil.getFinalConsonant(input, MessageUtil.ConsonantType.은는) + " 잘못된 nbt입니다");
                }
              }
              String key = args[3];
              NBTItem nbtItem = new NBTItem(item);
              boolean exists = nbtItem.hasTag(key) && nbtItem.getType(key) == NBTType.NBTTagCompound;
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
                    return Collections.singletonList(compoundString + MessageUtil.getFinalConsonant(compoundString, MessageUtil.ConsonantType.은는) + " 잘못된 nbt입니다");
                  }
                }
              }
              key = args[3];
              nbtItem = new NBTItem(item);
              NBTCompoundList nbtCompoundList = nbtItem.getCompoundList(key);
              if (nbtCompoundList != null && nbtCompoundList.size() > 0)
              {
                StringBuilder originValueStringBuilder = new StringBuilder();
                for (ReadWriteNBT originValue2 : nbtCompoundList)
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
        break;
      case "customtag":
        if (length == 2)
        {
          return Method.tabCompleterList(args, "args", "list", "add", "remove");
        }
        else if (length == 3)
        {
          List<String> keys = new ArrayList<>();
          NBTItem nbtItem = new NBTItem(item);
          NBTCompound customTags = NBTAPI.getCompound(NBTAPI.getCompound(nbtItem, CucumberyTag.KEY_TMI), CucumberyTag.TMI_CUSTOM_TAGS);
          if (customTags != null)
          {
            for (String key : customTags.getKeys())
            {
              if (customTags.getBoolean(key))
              {
                keys.add(key);
              }
            }
          }
          switch (args[1])
          {
            case "add" -> {
              return Method.tabCompleterList(args, "new key", true);
            }
            case "remove" -> {
              return Method.tabCompleterList(args, keys, "key");
            }
          }
        }
        break;
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
            return Collections.singletonList(args[4] + MessageUtil.getFinalConsonant(args[4], MessageUtil.ConsonantType.은는) + " 정수가 아닙니다");
          }
          else if (!MessageUtil.checkNumberSize(sender, Integer.parseInt(args[4]), 1, Integer.MAX_VALUE, false))
          {
            return Collections.singletonList("정수는 1 이상이여야 하는데, " + args[4] + MessageUtil.getFinalConsonant(args[4], MessageUtil.ConsonantType.이가) + " 있습니다");
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
              return Collections.singletonList("유효한 명령어 팩 파일이 존재하지 않습니다");
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
              return Collections.singletonList("유효한 명령어 팩 파일이 존재하지 않습니다");
            }
            YamlConfiguration config = Variable.commandPacks.get(cmdLabel);
            if (config == null)
            {
              return Collections.singletonList(cmdLabel + MessageUtil.getFinalConsonant(cmdLabel, MessageUtil.ConsonantType.은는) + " 잘못되거나 알 수 없는 명령어 팩 파일입니다");
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
    return Collections.singletonList(Prefix.ARGS_LONG.toString());
  }
}
