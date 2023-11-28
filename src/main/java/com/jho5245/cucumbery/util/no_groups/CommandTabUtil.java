package com.jho5245.cucumbery.util.no_groups;

import com.destroystokyo.paper.event.server.AsyncTabCompleteEvent.Completion;
import com.jho5245.cucumbery.custom.customeffect.type.CustomEffectType;
import com.jho5245.cucumbery.util.nbt.CucumberyTag;
import com.jho5245.cucumbery.util.nbt.NBTAPI;
import com.jho5245.cucumbery.util.no_groups.CommandArgumentUtil.LocationTooltip;
import com.jho5245.cucumbery.util.storage.component.LocationComponent;
import com.jho5245.cucumbery.util.storage.component.util.ComponentUtil;
import com.jho5245.cucumbery.util.storage.component.util.ItemNameUtil;
import com.jho5245.cucumbery.util.storage.component.util.sendercomponent.SenderComponentUtil;
import com.jho5245.cucumbery.util.storage.data.*;
import com.jho5245.cucumbery.util.storage.no_groups.CustomConfig.UserData;
import com.jho5245.cucumbery.util.storage.no_groups.ItemStackUtil;
import de.tr7zw.changeme.nbtapi.NBTCompound;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.TranslatableComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;
import net.kyori.adventure.translation.Translatable;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.command.*;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitTask;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class CommandTabUtil
{
  public static HashMap<UUID, List<Completion>> selectorErrorMessage = new HashMap<>();

  public static HashMap<UUID, BukkitTask> taskHashMap = new HashMap<>();

  public static final List<Completion> ARGS_LONG = Collections.singletonList(Completion.completion(Prefix.ARGS_LONG.toString(), ComponentUtil.translate(Prefix.ARGS_LONG.toString())));
  private static final List<Completion> SELECTORS = List.of(Completion.completion("@p", ComponentUtil.translate("argument.entity.selector.nearestPlayer")),
          Completion.completion("@r", ComponentUtil.translate("argument.entity.selector.randomPlayer")),
          Completion.completion("@a", ComponentUtil.translate("argument.entity.selector.allPlayers")),
          Completion.completion("@s", ComponentUtil.translate("argument.entity.selector.self")));

  @Deprecated
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
                  "laserwidth", "fireworktype", "reverse", "suicide", "particle-type");
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
            case "particle-type":
              return Method.tabCompleterList(args, Particle.values(), "<입자 유형>");
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

  @Deprecated
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

  @NotNull
  public static List<Completion> fromLegacy(@NotNull TabCompleter completer, @NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args)
  {
    List<String> list = completer.onTabComplete(sender, cmd, label, args);
    if (list != null)
    {
      List<Completion> completions = new ArrayList<>();
      list.forEach(s -> completions.add(Completion.completion(s)));
      return completions;
    }
    return Collections.singletonList(Completion.completion("[<인수>]", ComponentUtil.translate("인수")));
  }

  @NotNull
  public static List<Completion> getCommandsTabCompleter2(@NotNull CommandSender sender, @NotNull String[] args, int length, boolean forSudo)
  {
    if (args.length == length)
    {
      if (!forSudo)
      {
        return tabCompleterList(args, Method.getAllServerCommands(), "<명령어>", true);
      }
      List<String> cmds = Method.getAllServerCommands();
      List<String> newCmds = new ArrayList<>();
      for (String cmd2 : cmds)
      {
        newCmds.add(cmd2);
        newCmds.add("op:" + cmd2);
      }
      return tabCompleterList(args, newCmds, "<명령어>", true);
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
        return tabCompleterList(args, Method.getAllServerCommands(), "<명령어>", true);
      }
      PluginCommand command = Bukkit.getPluginCommand(cmdLabel);
      String[] args2 = new String[args.length - length];
      System.arraycopy(args, length, args2, 0, args.length - length);
      if (command != null)
      {
        CommandExecutor executor = command.getExecutor();
        if (executor instanceof AsyncTabCompleter asyncTabCompleter)
        {
          return asyncTabCompleter.completion(sender, command, command.getLabel(), args2, CommandArgumentUtil.senderLocation(sender));
        }
        else
        {
          TabCompleter completer = command.getTabCompleter();
          if (completer != null)
          {
            return fromLegacy(completer, sender, command, command.getLabel(), args2);
          }
        }
      }
/*      String wholeCommand = cmdLabel + " " + MessageUtil.listToString(args); // 실제로 채팅에 입력한 명령어 ig : /give @a diamond
      int start;
      if (wholeCommand.contains(" "))
      {
        start = wholeCommand.length() - wholeCommand.replace(" ", "").length();
      }
      else
      {
        start = 0;
      }
      @SuppressWarnings("unchecked")
      ParseResults<?> parseResults = Brigadier.getCommandDispatcher().parse(cmdLabel, Brigadier.getBrigadierSourceFromCommandSender(sender));
      List<Completion> errors = new ArrayList<>();
      for (CommandSyntaxException exception : parseResults.getExceptions().values())
      {
        errors.add(Completion.completion(exception.getRawMessage().getString(), ComponentUtil.translate(exception.getRawMessage().getString())));
      }
      if (!errors.isEmpty())
      {
        return errors;
      }
      List<Suggestion> suggestionsList = new ArrayList<>();
      Brigadier.getCommandDispatcher().getCompletionSuggestions(parseResults).thenApply((suggestionsObject) -> {
        Suggestions suggestions = (Suggestions) suggestionsObject;
        Suggestions s = new Suggestions(new StringRange(start, start + suggestions.getRange().getLength()), suggestions.getList());
        suggestionsList.addAll(s.getList());
        return s;
      });
      List<Completion> completions = new ArrayList<>();
      suggestionsList.forEach(s -> completions.add(Completion.completion(s.getText(), ComponentUtil.translate(s.getText()))));
      if (!completions.isEmpty())
      {
        return completions;
      }*/
      return Collections.singletonList(Completion.completion("[<인수>]", ComponentUtil.translate("인수")));
    }
  }

  @NotNull
  public static Completion completion(@NotNull Component component)
  {
    return Completion.completion(ComponentUtil.serialize(component), component);
  }

  @NotNull
  public static List<Completion> completions(@NotNull Component component)
  {
    return Collections.singletonList(completion(component));
  }

  @NotNull
  public static List<Completion> errorMessage(@NotNull String key)
  {
    return errorMessage(key, true);
  }

  @NotNull
  public static List<Completion> errorMessage(@NotNull Component component)
  {
    String key = "";
    List<Component> args = new ArrayList<>();
    if (component instanceof TranslatableComponent translatableComponent)
    {
      key = translatableComponent.key();
      args.addAll(translatableComponent.args());
    }
    else if (component instanceof TextComponent textComponent)
    {
      key = textComponent.content();
    }
    return completions(ComponentUtil.translate("%s", ComponentUtil.translate(key, args), "error-text").append(ComponentUtil.create(component.children())));
  }

  @NotNull
  public static List<Completion> errorMessage(@NotNull String key, @NotNull Object... args)
  {
    Component component = ComponentUtil.translate("%s", ComponentUtil.translate(key, args), "error-text");
    return completions(component);
  }

  public static boolean isErrorMessage(@NotNull List<Completion> completions)
  {
    return completions.size() == 1 && completions.get(0).tooltip() instanceof TranslatableComponent translatableComponent && (translatableComponent.args().size() == 2 &&
            translatableComponent.args().get(1) instanceof TextComponent t && t.content().equals("error-text"));
  }

  /**
   * 개체 선택자, 플레이어 이름, 바라보고 있는 개체의 UUID를 반환합니다
   *
   * @param sender                 명령어를 실행하는 주체
   * @param lastArg                명령어의 마지막 인수
   * @param excludeNonPlayerEntity 바라보고 있는 개체의 UUID 중 플레이어가 아닌 개체의 UUID 제외 여부
   * @return 햐당하는 컴플릿션 리스트
   */
  @NotNull
  private static List<Completion> tabCompleterEntity(@NotNull CommandSender sender, @NotNull String lastArg, boolean excludeNonPlayerEntity)
  {
    Player exactPlayer = Bukkit.getServer().getPlayerExact(lastArg);
    if (exactPlayer != null)
    {
      Component hover = SenderComponentUtil.senderComponent(exactPlayer, Constant.THE_COLOR, true);
      return Collections.singletonList(Completion.completion(lastArg, hover));
    }
    List<Completion> list = sender.hasPermission("minecraft.command.selector") ? new ArrayList<>(SELECTORS) : new ArrayList<>();
    for (Player online : Bukkit.getServer().getOnlinePlayers())
    {
      Component hover = SenderComponentUtil.senderComponent(online, Constant.THE_COLOR, true);
      list.add(Completion.completion(online.getName(), hover));
      String displayName = MessageUtil.stripColor(ComponentUtil.serialize(online.displayName()));
      String playerListName = MessageUtil.stripColor(ComponentUtil.serialize(online.playerListName()));
      list.add(Completion.completion(displayName, hover));
      list.add(Completion.completion(playerListName, hover));
    }
    if (!excludeNonPlayerEntity && sender instanceof Player player)
    {
      Location location = player.getLocation();
      location.add(location.getDirection().multiply(2d));
      List<Entity> entities = new ArrayList<>(Arrays.asList(location.getChunk().getEntities()));
      entities.addAll(Arrays.asList(location.clone().add(16, 0, 0).getChunk().getEntities()));
      entities.addAll(Arrays.asList(location.clone().add(0, 0, 16).getChunk().getEntities()));
      entities.addAll(Arrays.asList(location.clone().add(-16, 0, 0).getChunk().getEntities()));
      entities.addAll(Arrays.asList(location.clone().add(0, 0, -16).getChunk().getEntities()));
      entities.addAll(Arrays.asList(location.clone().add(16, 0, 16).getChunk().getEntities()));
      entities.addAll(Arrays.asList(location.clone().add(-16, 0, 16).getChunk().getEntities()));
      entities.addAll(Arrays.asList(location.clone().add(16, 0, -16).getChunk().getEntities()));
      entities.addAll(Arrays.asList(location.clone().add(-16, 0, -16).getChunk().getEntities()));
      entities.removeIf(entity ->
      {
        double distance = entity.getLocation().distance(location);
        return distance > 3d || entity instanceof Player;
      });
      for (Entity entity : entities)
      {
        Component hover = SenderComponentUtil.senderComponent(entity, Constant.THE_COLOR, true);
        String uuid = entity.getUniqueId().toString();
        list.add(Completion.completion(uuid, hover));
      }
    }
    if (sender instanceof Player player && player.getGameMode() == GameMode.SPECTATOR)
    {
      Entity entity = player.getSpectatorTarget();
      if (entity != null && (!excludeNonPlayerEntity || entity instanceof Player))
      {
        Component hover = ComponentUtil.translate("관전 중인 개체 (%s)", SenderComponentUtil.senderComponent(entity, Constant.THE_COLOR, true));
        String uuid = entity.getUniqueId().toString();
        list.add(Completion.completion(uuid, hover));
      }
    }
    if (lastArg.equals(""))
    {
      list.add(Completion.completion("#", ComponentUtil.translate("특정 태그가 있는 개체 (예 : #foo)")));
    }
    return list;
  }

  /**
   * 개체를 반환합니다
   *
   * @param sender 명령어를 실행하는 주체
   * @param args   명령어의 인수
   * @param key    인수의 유형
   * @return 개체 리스트
   */
  @NotNull
  public static List<Completion> tabCompleterEntity(@NotNull CommandSender sender, @NotNull String[] args, @NotNull Object key)
  {
    return tabCompleterEntity(sender, args, key, false);
  }

  /**
   * 개체를 반환합니다
   *
   * @param sender 명령어를 실행하는 주체
   * @param args   명령어의 인수
   * @param key    인수의 유형
   * @return 개체 리스트
   */
  @NotNull
  public static List<Completion> tabCompleterEntity(@NotNull CommandSender sender, @NotNull String[] args, @NotNull Object key, boolean multiple)
  {
    String arg = args[args.length - 1];
    if (Method.equals(arg, "@a", "@e", "@p", "@r", "@s", "@A", "@E", "@R", "@S", "#") && !sender.hasPermission("minecraft.command.selector"))
    {
      return errorMessage("argument.entity.selector.not_allowed");
    }
    List<Completion> list = new ArrayList<>(tabCompleterEntity(sender, arg, false));
    if (sender.hasPermission("minecraft.command.selector"))
    {
      list.add(Completion.completion("@e", ComponentUtil.translate("argument.entity.selector.allEntities")));
      if (arg.startsWith("@"))
      {
        list.add(Completion.completion("@A", ComponentUtil.translate("자신을 제외한 모든 플레이어")));
        list.add(Completion.completion("@E", ComponentUtil.translate("플레이어를 제외한 모든 개체")));
        list.add(Completion.completion("@R", ComponentUtil.translate("무작위 개체")));
        if (arg.toLowerCase().startsWith("@r"))
        {
          list.add(Completion.completion("@rR", ComponentUtil.translate("자신을 제외한 무작위 개체")));
          list.add(Completion.completion("@rr", ComponentUtil.translate("자신을 제외한 무작위 플레이어")));
        }
        list.add(Completion.completion("@S", ComponentUtil.translate("자신을 제외한 모든 개체")));
        if (arg.toLowerCase().startsWith("@p"))
        {
          list.add(Completion.completion("@P", ComponentUtil.translate("자신을 제외한 가장 가까운 플레이어")));
          list.add(Completion.completion("@pp", ComponentUtil.translate("자신을 제외한 가장 가까운 개체")));
        }
      }
      if (arg.startsWith("#"))
      {
        for (World world : Bukkit.getWorlds())
        {
          for (Chunk chunk : world.getLoadedChunks())
          {
            for (Entity entity : chunk.getEntities())
            {
              for (String tag : entity.getScoreboardTags())
              {
                list.add(Completion.completion("#" + tag, ComponentUtil.translate("%s 태그가 있는 개체", tag)));
              }
            }
          }
        }
      }
    }
/*    if (sender instanceof Player player)
    {
      UUID uuid = player.getUniqueId();
      if (selectorErrorMessage.containsKey(uuid))
      {
        List<Completion> completions = selectorErrorMessage.get(uuid);
        taskHashMap.put(uuid, Bukkit.getScheduler().runTaskLater(Cucumbery.getPlugin(), () ->
                selectorErrorMessage.remove(uuid), 2L));
        return completions;
      }
      Bukkit.getScheduler().runTaskLater(Cucumbery.getPlugin(), () ->
      {
        BukkitTask bukkitTask = taskHashMap.get(uuid);
        try
        {
          List<Entity> entities = Bukkit.selectEntities(sender, args[args.length - 1]);
          if (entities.isEmpty())
          {
            if (bukkitTask != null)
            {
              bukkitTask.cancel();
            }
            selectorErrorMessage.put(uuid, errorMessage("argument.entity.notfound.entity"));
            return;
          }
          if (!multiple && entities.size() > 1)
          {
            if (!sender.hasPermission("minecraft.command.selector"))
            {
              if (bukkitTask != null)
              {
                bukkitTask.cancel();
              }
              selectorErrorMessage.put(uuid, errorMessage("argument.entity.selector.not_allowed"));
              return;
            }
            if (bukkitTask != null)
            {
              bukkitTask.cancel();
            }
            selectorErrorMessage.put(uuid, errorMessage("argument.entity.toomany"));
          }
        }
        catch (IllegalArgumentException e)
        {
          if (!sender.hasPermission("minecraft.command.selector"))
          {
            if (bukkitTask != null)
            {
              bukkitTask.cancel();
            }
            selectorErrorMessage.put(uuid, errorMessage("argument.entity.selector.not_allowed"));
            return;
          }
          if (bukkitTask != null)
          {
            bukkitTask.cancel();
          }
          selectorErrorMessage.put(uuid, errorMessage(SelectorUtil.errorMessage(args[args.length - 1], e)));
        }
      }, 0L);
    }*/
    return tabCompleterList(args, list, key, arg.startsWith("@") || arg.startsWith("#"));
  }

  @NotNull
  public static List<Completion> tabCompleterPlayer(@NotNull CommandSender sender, @NotNull String[] args, @NotNull Object key)
  {
    String arg = args[args.length - 1];
    if (Method.equals(arg, "@a", "@e", "@p", "@r", "@s", "@A", "@E", "#") && !sender.hasPermission("minecraft.command.selector"))
    {
      return errorMessage("argument.entity.selector.not_allowed");
    }
    List<Completion> list = new ArrayList<>(tabCompleterEntity(sender, arg, true));
    if (sender.hasPermission("minecraft.command.selector"))
    {
      if (arg.startsWith("@"))
      {
        list.add(Completion.completion("@A", ComponentUtil.translate("자신을 제외한 모든 플레이어")));
        if (arg.toLowerCase().startsWith("@r"))
        {
          list.add(Completion.completion("@rr", ComponentUtil.translate("자신을 제외한 무작위 플레이어")));
        }
        if (arg.toLowerCase().startsWith("@p"))
        {
          list.add(Completion.completion("@P", ComponentUtil.translate("자신을 제외한 가장 가까운 플레이어")));
        }
      }
      if (arg.startsWith("#"))
      {
        for (World world : Bukkit.getWorlds())
        {
          for (Chunk chunk : world.getLoadedChunks())
          {
            for (Entity entity : chunk.getEntities())
            {
              for (String tag : entity.getScoreboardTags())
              {
                list.add(Completion.completion("#" + tag, ComponentUtil.translate("%s 태그가 있는 개체", tag)));
              }
            }
          }
        }
      }
    }
    return tabCompleterList(args, list, key, arg.startsWith("@") || arg.startsWith("#"));
  }

  @NotNull
  public static List<Completion> tabCompleterOfflinePlayer(@NotNull CommandSender sender, @NotNull String[] args, @NotNull Object key)
  {
    String arg = args[args.length - 1];
    if (Method.equals(arg, "@a", "@e", "@p", "@r", "@s", "@A", "@E", "#") && !sender.hasPermission("minecraft.command.selector"))
    {
      return errorMessage("argument.entity.selector.not_allowed");
    }
    List<Completion> list = new ArrayList<>(tabCompleterEntity(sender, arg, true));
    if (sender.hasPermission("minecraft.command.selector"))
    {
      if (arg.startsWith("@"))
      {
        list.add(Completion.completion("@A", ComponentUtil.translate("자신을 제외한 모든 플레이어")));
        if (arg.toLowerCase().startsWith("@r"))
        {
          list.add(Completion.completion("@rr", ComponentUtil.translate("자신을 제외한 무작위 플레이어")));
        }
        if (arg.toLowerCase().startsWith("@p"))
        {
          list.add(Completion.completion("@P", ComponentUtil.translate("자신을 제외한 가장 가까운 플레이어")));
        }
      }
      if (arg.startsWith("#"))
      {
        for (World world : Bukkit.getWorlds())
        {
          for (Chunk chunk : world.getLoadedChunks())
          {
            for (Entity entity : chunk.getEntities())
            {
              for (String tag : entity.getScoreboardTags())
              {
                list.add(Completion.completion("#" + tag, ComponentUtil.translate("%s 태그가 있는 개체", tag)));
              }
            }
          }
        }
      }
    }
    for (String nickName : Variable.nickNames)
    {
      if (!Method.isUUID(nickName) || Bukkit.getOfflinePlayer(UUID.fromString(nickName)).getName() == null)
      {
        nickName = MessageUtil.stripColor(nickName);
        OfflinePlayer offlinePlayer = Method.getOfflinePlayer(sender, nickName, false);
        Component hover = offlinePlayer == null ? null :
                offlinePlayer.getPlayer() != null ? SenderComponentUtil.senderComponent(offlinePlayer.getPlayer(), Constant.THE_COLOR, true) :
                        Component.text(offlinePlayer.getUniqueId().toString());
        list.add(Completion.completion(nickName, hover));
      }
    }
    return tabCompleterList(args, list, key, arg.startsWith("@") || arg.startsWith("#"));
  }

  @NotNull
  public static List<Completion> tabCompleterList(@NotNull String[] args, @NotNull List<?> list, @NotNull Object key)
  {
    return tabCompleterList(args, list, key, false);
  }

  @NotNull
  public static List<Completion> tabCompleterList(@NotNull String[] args, @NotNull List<?> list, @NotNull Object key, boolean ignoreEmpty)
  {
    String tabArg = args[args.length - 1];
    Completion returnKey = null;
    String suggestion;
    if (key instanceof Completion completion)
    {
      suggestion = completion.suggestion();
      if (completion.tooltip() == null)
      {
        returnKey = Completion.completion(completion.suggestion(), ComponentUtil.translate(suggestion.substring(1, suggestion.length() - 1)));
      }
    }
    else
    {
      suggestion = key.toString();
      returnKey = Completion.completion(suggestion, ComponentUtil.translate(suggestion.substring(1, suggestion.length() - 1)));
    }
    if (!ignoreEmpty && tabArg.isEmpty() && list.isEmpty() || ignoreEmpty && list.isEmpty())
    {
      return Collections.singletonList(returnKey);
    }
    list = new ArrayList<>(list);
    list.removeIf(e -> e == null || e.equals("") || (e instanceof Completion completion && completion.suggestion().isEmpty()));
    int length = tabArg.length();
    if (!tabArg.isEmpty())
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
      List<Completion> returnValue = new ArrayList<>();
      String replace = tabArg.replace(" ", "").replace("_", "").toLowerCase();
      for (Object o : list)
      {
        String str = o instanceof Completion completion ? completion.suggestion() : o.toString();
        final boolean contains = str.toLowerCase().replace(" ", "").replace("_", "").contains(replace);
        if ((length <= 2 && str.replace(Constant.TAB_COMPLETER_QUOTE_ESCAPE, "").toLowerCase().startsWith(replace)) || (length >= 3 && contains))
        {
          returnValue.add(o instanceof Completion completion ? completion : Completion.completion(o.toString()));
        }
      }
      if (returnValue.size() == 1)
      {
        String s = returnValue.get(0).suggestion();
        if (s.contains(" ") && s.endsWith(tabArg))
        {
          return Collections.singletonList(returnKey);
        }
      }
      if (returnValue.isEmpty() && !(key instanceof Completion completion ? completion.suggestion() : key.toString()).isEmpty() && !ignoreEmpty)
      {
        String key2 = (key instanceof Completion completion ? completion.suggestion() :
                key.toString()).replace("<", "").replace(">", "").replace("[", "").replace("]", "");
        String msg = getErrorMessage(key2);
        return errorMessage(msg, tabArg, ComponentUtil.translate(key2));
      }
      returnValue = sort(returnValue);
      if ((ignoreEmpty && returnValue.isEmpty()) || (!(key instanceof Completion completion ? completion.suggestion() : key.toString()).isEmpty()
					&& returnValue.size() == 1 && returnValue.get(0).suggestion().equalsIgnoreCase(tabArg)))
      {
        return Collections.singletonList(returnKey);
      }
      return returnValue;
    }
    List<Completion> completions = new ArrayList<>();
    for (Object o : list)
    {
      completions.add(o instanceof Completion completion ? completion : Completion.completion(o.toString()));
    }
    return sort(completions);
  }

  @NotNull
  private static String getErrorMessage(String key2)
  {
    String msg = "'%s'은(는) 잘못되거나 알 수 없는 %s입니다";
    if (key2.contains("개체"))
    {
      msg = "개체를 찾을 수 없습니다 (%s)";
    }
    if (key2.contains("플레이어") || key2.contains("관전자"))
    {
      msg = "플레이어를 찾을 수 없습니다 (%s)";
    }
    switch (key2)
    {
      case "아이템" -> msg = "argument.item.id.invalid";
      case "블록" -> msg = "argument.block.id.invalid";
      case "인수" -> msg = "명령어에 잘못된 인수가 있습니다: %s";
      case "개체 유형" -> msg = "'%s'은(는) 잘못되거나 알 수 없는 %s입니다";
    }
    return msg;
  }

  @NotNull
  public static List<Completion> tabCompleterList(@NotNull String[] args, @NotNull Set<?> set, @NotNull Object key)
  {
    return tabCompleterList(args, set, key, false);
  }

  @NotNull
  public static List<Completion> tabCompleterList(@NotNull String[] args, @NotNull Set<?> set, @NotNull Object key, boolean ignoreEmpty)
  {
    return tabCompleterList(args, new ArrayList<>(set), key, ignoreEmpty);
  }

  @NotNull
  public static <T extends Enum<T>> List<Completion> tabCompleterList(@NotNull String[] args, @NotNull Enum<T>[] array, @NotNull Object key)
  {
    return tabCompleterList(args, array, key, false);
  }

  @NotNull
  public static <T extends Enum<T>> List<Completion> tabCompleterList(@NotNull String[] args, @NotNull Enum<T>[] array, @NotNull Object key, boolean ignoreEmpty)
  {
    return tabCompleterList(args, array, key, ignoreEmpty, null);
  }

  @NotNull
  public static <T extends Enum<T>> List<Completion> tabCompleterList(@NotNull String[] args, @NotNull Enum<T>[] array, @NotNull Object key, @Nullable Predicate<T> exclude)
  {
    return tabCompleterList(args, array, key, false, exclude);
  }

  @NotNull
  @SuppressWarnings("unchecked")
  public static <T extends Enum<T>> List<Completion> tabCompleterList(@NotNull String[] args, @NotNull Enum<T>[] array, @NotNull Object key, boolean ignoreEmpty, @Nullable Predicate<T> exclude)
  {
    List<Object> list = new ArrayList<>();
    String arg = args[args.length - 1];
    if (arg.equals(arg.toLowerCase()))
    {
      for (Enum<T> e : array)
      {
        if (exclude != null && exclude.test((T) e))
        {
          continue;
        }
        if (!(e instanceof EnumHideable enumHideable) || !enumHideable.isHiddenEnum())
        {
          Component hover = null;
          if (e instanceof Material material)
          {
            hover = ItemNameUtil.itemName(material);
          }
          else if (e instanceof Statistic statistic)
          {
            hover = switch (statistic)
                    {
                      case ENTITY_KILLED_BY -> ComponentUtil.translate("개체에게 죽은 횟수");
                      case KILL_ENTITY -> ComponentUtil.translate("개체를 죽인 횟수");
                      default -> ComponentUtil.translate(TranslatableKeyParser.getKey(statistic));
                    };
          }
          else if (e instanceof UserData userData)
          {
            hover = ComponentUtil.translate(userData.getKey().replace("-", " "));
          }
          else if (e instanceof Sound sound)
          {
            hover = ComponentUtil.translate("key:subtitles." + sound.key().value() + "|" + sound.key().value());
          }
          else if (e instanceof Translatable translatable)
          {
            hover = ComponentUtil.translate(translatable.translationKey());
          }
          list.add(Completion.completion(e.toString().toLowerCase(), hover));
        }
      }
    }
    return tabCompleterList(args, list, key, ignoreEmpty);
  }

  @NotNull
  public static List<Completion> tabCompleterList(@NotNull String[] args, @NotNull Map<?, ?> map, @NotNull Object key)
  {
    return tabCompleterList(args, map, key, null);
  }

  @NotNull
  public static List<Completion> tabCompleterList(@NotNull String[] args, @NotNull Map<?, ?> map, @NotNull Object key, Predicate<Object> exclude)
  {
    List<Object> list = new ArrayList<>();
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
      String s = k.toString();
/*      if (k instanceof NamespacedKey && !args[args.length - 1].equals("") && !args[args.length - 1].contains(":"))
      {
        args[args.length - 1] = "cucumbery:" + args[args.length - 1];
      }*/
      String lastArg = args[args.length - 1];
      if (k instanceof NamespacedKey && !lastArg.isEmpty() && lastArg.length() <= 2)
      {
        args[args.length - 1] = "cucumbery:" + lastArg;
      }
      Component hover = null;
      if (v instanceof CustomEffectType customEffectType)
      {
        hover = ComponentUtil.translate(customEffectType.translationKey()).color(customEffectType.isNegative() ? NamedTextColor.RED : NamedTextColor.GREEN);
      }
      list.add(Completion.completion(s, hover));
    }
    return tabCompleterList(args, list, key);
  }

  @NotNull
  public static List<Completion> tabCompleterList(@NotNull String[] args, @NotNull Object key, boolean ignoreEmpty, @NotNull Object... list)
  {
    return tabCompleterList(args, Arrays.asList(list), key, ignoreEmpty);
  }

  @NotNull
  public static List<Completion> tabCompleterList(@NotNull String[] args, @NotNull Object key, @NotNull List<String> list)
  {
    return tabCompleterList(args, list, key, false);
  }

  @NotNull
  public static List<Completion> tabCompleterList(@NotNull String[] args, @NotNull Object key, boolean ignoreEmpty, @NotNull List<String> list)
  {
    return tabCompleterList(args, list, key, ignoreEmpty);
  }

  @NotNull
  public static List<Completion> tabCompleterBoolean(@NotNull String[] args, @NotNull Object key)
  {
    String tag = args[args.length - 1];
    if (!Method.startsWith(tag, true, "true", "false"))
    {
      return errorMessage("parsing.bool.invalid", tag);
    }
    if (tag.equals("true"))
    {
      return Collections.singletonList(key instanceof Completion completion ? completion : Completion.completion(key.toString()));
    }
    String hover = key instanceof Completion completion ? completion.suggestion() : key.toString();
    // '[명령어 출력 숨김 여부]' 일 경우 앞뒤 끝의 '['와 ']' 제거
    hover = hover.substring(1, hover.length() - 1);
    return CommandTabUtil.tabCompleterList(args, key, false, Completion.completion("true", ComponentUtil.translate(hover)), Completion.completion("false", ComponentUtil.translate(hover)));
  }

  @NotNull
  public static List<Completion> tabCompleterDoubleRadius(
          @NotNull String[] args, double from, double to, @NotNull Object key)
  {
    return tabCompleterDoubleRadius(args, from, false, to, false, key);
  }

  @NotNull
  public static List<Completion> tabCompleterDoubleRadius(
          @NotNull String[] args, double from, boolean excludeFrom, double to, boolean excludeTo, @NotNull Object key)
  {
    String tabArg = args[args.length - 1];
    Completion completion = key instanceof Completion c ? c : Completion.completion(key.toString(), ComponentUtil.translate("범위 제한 : %s %s %s %s",
            Constant.Sosu2.format(from), excludeFrom ? "초과" : "이상", Constant.Sosu2.format(to), excludeTo ? "미만" : "이하"));
    if (tabArg.equals(""))
    {
      return Collections.singletonList(completion);
    }
    if (from == -Double.MAX_VALUE && to == Double.MAX_VALUE)
    {
      return Collections.singletonList(objectToCompletion(key));
    }
    if (!MessageUtil.isDouble(null, tabArg, false))
    {
      return errorMessage("parsing.double.invalid", tabArg);
    }
    double argDouble = Double.parseDouble(tabArg);
    tabArg = Constant.Sosu15.format(argDouble);
    if (!MessageUtil.checkNumberSize(null, argDouble, from, Double.MAX_VALUE, excludeFrom, excludeTo, false))
    {
      return errorMessage(excludeFrom ? "double은 %s 초과이어야 하는데, %s이(가) 있습니다" : "argument.double.low", Constant.Sosu15.format(from), tabArg);
    }
    if (!MessageUtil.checkNumberSize(null, argDouble, -Double.MAX_VALUE, to, excludeFrom, excludeTo, false))
    {
      return errorMessage(excludeTo ? "double은 %s 미만이어야 하는데, %s이(가) 있습니다" : "argument.double.big", Constant.Sosu15.format(to), tabArg);
    }
    String keyString = completion.suggestion();
    char keyLast = keyString.charAt(keyString.length() - 1);
    keyString = keyString.substring(0, keyString.length() - 1) + "=" + Constant.Sosu15.format(argDouble) + keyLast;
    return Collections.singletonList(Completion.completion(keyString, completion.tooltip()));
  }

  @NotNull
  public static List<Completion> tabCompleterLongRadius(@NotNull String[] args, long from, long to, @NotNull Object key)
  {
    String tabArg = args[args.length - 1];
    Completion completion = key instanceof Completion c ? c : Completion.completion(key.toString(), ComponentUtil.translate("범위 제한 : %s 이상 %s 이하", Constant.Sosu2.format(from), Constant.Sosu2.format(to)));
    if (tabArg.equals(""))
    {
      return Collections.singletonList(completion);
    }
    if (!MessageUtil.isDouble(null, tabArg, false))
    {
      return errorMessage("parsing.long.invalid", tabArg);
    }
    if (!MessageUtil.isLong(null, tabArg, false))
    {
      return errorMessage("parsing.long.invalid", tabArg);
    }
    long argLong = Long.parseLong(tabArg);
    tabArg = Constant.Sosu15.format(argLong);
    if (!MessageUtil.checkNumberSize(null, argLong, from, Long.MAX_VALUE))
    {
      return errorMessage("argument.long.low", Constant.Sosu15.format(from), tabArg);
    }
    if (!MessageUtil.checkNumberSize(null, argLong, Long.MIN_VALUE, to))
    {
      return errorMessage("argument.long.big", Constant.Sosu15.format(from), tabArg);
    }
    String keyString = completion.suggestion();
    char keyLast = keyString.charAt(keyString.length() - 1);
    keyString = keyString.substring(0, keyString.length() - 1) + "=" + Constant.Sosu15.format(argLong) + keyLast;
    return Collections.singletonList(Completion.completion(keyString, completion.tooltip()));
  }

  @NotNull
  public static List<Completion> tabCompleterIntegerRadius(@NotNull String[] args, int from, int to, @NotNull Object key)
  {
    String tabArg = args[args.length - 1];
    Completion completion = key instanceof Completion c ? c : Completion.completion(key.toString(), ComponentUtil.translate("범위 제한 : %s 이상 %s 이하", Constant.Sosu2.format(from), Constant.Sosu2.format(to)));
    if (tabArg.equals(""))
    {
      return Collections.singletonList(completion);
    }
    if (!MessageUtil.isDouble(null, tabArg, false))
    {
      return errorMessage("parsing.int.invalid", tabArg);
    }
    if (!MessageUtil.isInteger(null, tabArg, false))
    {
      return errorMessage("parsing.int.invalid", tabArg);
    }
    int argInteger = Integer.parseInt(tabArg);
    tabArg = Constant.Sosu15.format(argInteger);
    if (!MessageUtil.checkNumberSize(null, argInteger, from, Integer.MAX_VALUE))
    {
      return errorMessage("argument.integer.low", Constant.Sosu15.format(from), tabArg);
    }
    if (!MessageUtil.checkNumberSize(null, argInteger, Integer.MIN_VALUE, to))
    {
      return errorMessage("argument.integer.big", Constant.Sosu15.format(to), tabArg);
    }

    String keyString = completion.suggestion();
    char keyLast = keyString.charAt(keyString.length() - 1);
    keyString = keyString.substring(0, keyString.length() - 1) + "=" + Constant.Sosu15.format(argInteger) + keyLast;
    return Collections.singletonList(Completion.completion(keyString, completion.tooltip()));
  }

  @NotNull
  public static List<Completion> locationArgument(@NotNull CommandSender sender, @NotNull String[] args, @NotNull Object key, @Nullable Location location, boolean isInteger)
  {
    return locationArgument(sender, args, key, location, isInteger, null);
  }

  @NotNull
  public static List<Completion> locationArgument(@NotNull CommandSender sender, @NotNull String[] args, @NotNull Object key, @Nullable Location location, boolean isInteger, @Nullable List<LocationTooltip> extraSuggestions)
  {
    if (location == null)
    {
      location = CommandArgumentUtil.senderLocation(sender);
    }
    if (isInteger)
    {
      location.setX(location.getBlockX());
      location.setY(location.getBlockY());
      location.setZ(location.getBlockZ());
    }
    @Nullable Block targetBlock = null;
    if (sender instanceof Player player)
    {
      targetBlock = player.getTargetBlockExact(6);
    }
    List<Object> list = new ArrayList<>(Arrays.asList(
            Completion.completion("~ ~ ~"),
            Completion.completion("~ ~"),
            Completion.completion("~")
    ));
    Component hover = ComponentUtil.translate("현재 위치 (%s)", location);
    String x = isInteger ? location.getBlockX() + "" : Constant.Sosu2rawFormat.format(location.getX());
    String y = isInteger ? location.getBlockY() + "" : Constant.Sosu2rawFormat.format(location.getY());
    String z = isInteger ? location.getBlockZ() + "" : Constant.Sosu2rawFormat.format(location.getZ());
    list.add(Completion.completion(x + " " + y + " " + z, hover));
    if (extraSuggestions != null)
    {
      for (LocationTooltip tooltip : extraSuggestions)
      {
        Location loc = tooltip.location();
        Component h = tooltip.hover();
        if (h == null)
        {
          h = LocationComponent.locationComponent(loc);
        }
        String X = isInteger ? loc.getBlockX() + "" : Constant.Sosu2rawFormat.format(loc.getX());
        String Y = isInteger ? loc.getBlockY() + "" : Constant.Sosu2rawFormat.format(loc.getY());
        String Z = isInteger ? loc.getBlockZ() + "" : Constant.Sosu2rawFormat.format(loc.getZ());
        list.add(Completion.completion(X + " " + Y + " " + Z, h));
      }
    }
    if (targetBlock != null)
    {
      hover = ComponentUtil.translate("바라보고 있는 블록 (%s)", targetBlock.getType());
      int x2 = targetBlock.getX(), y2 = targetBlock.getY(), z2 = targetBlock.getZ();
      list.add(Completion.completion(x2 + " " + y2 + " " + z2, hover));
      list.add(Completion.completion("$target_block", hover));
    }
    String arg = args[args.length - 1];
    if (targetBlock == null || !"$target_block".startsWith(arg))
    {
      if (!arg.equals(""))
      {
        if (arg.equals("~ ~ ~") || arg.equals("^ ^ ^"))
        {
          return Collections.singletonList(objectToCompletion(key));
        }
        if (arg.contains("~") && arg.contains("^") || (arg.contains("^") && !Method.allStartsWith("^", true, arg.split(" "))))
        {
          return errorMessage("argument.pos.mixed");
        }
        else if (arg.startsWith("^"))
        {
          list.removeIf(c -> c instanceof Completion completion && completion.suggestion().startsWith("~"));
          list.addAll(Arrays.asList(
                  Completion.completion("^ ^ ^"),
                  Completion.completion("^ ^"),
                  Completion.completion("^")));
        }
        String[] split = arg.split(" ");
        x = split[0];
        y = split.length > 1 ? split[1] : y;
        z = split.length > 2 ? split[2] : z;
        if (isInteger)
        {
          if (!MessageUtil.isInteger(sender, x, false))
          {
            if (x.startsWith("~") || x.startsWith("^"))
            {
              if (!x.equals("~") && !x.equals("^"))
              {
                x = x.substring(1);
                if (!MessageUtil.isInteger(sender, x, false))
                {
                  return errorMessage("argument.pos.missing.int");
                }
              }
            }
            else
            {
              return errorMessage("argument.pos.missing.int");
            }
          }
          if (!MessageUtil.isInteger(sender, y, false))
          {
            if (y.startsWith("~") || y.startsWith("^"))
            {
              if (!y.equals("~") && !y.equals("^"))
              {
                y = y.substring(1);
                if (!MessageUtil.isInteger(sender, y, false))
                {
                  return errorMessage("argument.pos.missing.int");
                }
              }
            }
            else
            {
              return errorMessage("argument.pos.missing.int");
            }
          }
          if (!MessageUtil.isInteger(sender, z, false))
          {
            if (z.startsWith("~") || z.startsWith("^"))
            {
              if (!z.equals("~") && !z.equals("^"))
              {
                z = z.substring(1);
                if (!MessageUtil.isInteger(sender, z, false))
                {
                  return errorMessage("argument.pos.missing.int");
                }
              }
            }
            else
            {
              return errorMessage("argument.pos.missing.int");
            }
          }
        }
        else
        {
          if (!MessageUtil.isDouble(sender, x, false))
          {
            if (x.startsWith("~") || x.startsWith("^"))
            {
              if (!x.equals("~") && !x.equals("^"))
              {
                x = x.substring(1);
                if (!MessageUtil.isDouble(sender, x, false))
                {
                  return errorMessage("argument.pos.missing.double");
                }
              }
            }
            else
            {
              return errorMessage("argument.pos.missing.double");
            }
          }
          if (!MessageUtil.isDouble(sender, y, false))
          {
            if (y.startsWith("~") || y.startsWith("^"))
            {
              if (!y.equals("~") && !y.equals("^"))
              {
                y = y.substring(1);
                if (!MessageUtil.isDouble(sender, y, false))
                {
                  return errorMessage("argument.pos.missing.double");
                }
              }
            }
            else
            {
              return errorMessage("argument.pos.missing.double");
            }
          }
          if (!MessageUtil.isDouble(sender, z, false))
          {
            if (z.startsWith("~") || z.startsWith("^"))
            {
              if (!z.equals("~") && !z.equals("^"))
              {
                z = z.substring(1);
                if (!MessageUtil.isDouble(sender, z, false))
                {
                  return errorMessage("argument.pos.missing.double");
                }
              }
            }
            else
            {
              return errorMessage("argument.pos.missing.double");
            }
          }
        }
        if (split.length > 3)
        {
          return errorMessage("좌표값은 3개이어야 합니다");
        }
        split = arg.split(" ");
        if (arg.startsWith("^"))
        {
          y = split.length > 1 ? split[1] : y;
          z = split.length > 2 ? split[2] : z;
          if (y.startsWith("^"))
          {
            if (z.startsWith("^"))
            {
              list.add(Completion.completion(split[0] + " " + y + " " + z));
            }
            else
            {
              list.add(Completion.completion(split[0] + " " + y + " ^"));
            }
            list.add(Completion.completion(split[0] + " " + y));
          }
          else
          {
            list.add(Completion.completion(split[0] + " ^ ^"));
            list.add(Completion.completion(split[0] + " ^"));
          }
        }
        else
        {
          list.add(Completion.completion(split[0] + " " + (split.length > 1 ? split[1] : y) + " " + (split.length > 2 ? split[2] : z)));
          list.add(Completion.completion(split[0] + " " + (split.length > 1 ? split[1] : y)));
          list.add(Completion.completion(split[0] + " ~ ~"));
          list.add(Completion.completion(split[0] + " ~"));
        }
        list.add(Completion.completion(split[0]));
        if (isErrorMessage(tabCompleterList(args, list, key)) && split.length < 3)
        {
          return errorMessage("argument.pos3d.incomplete");
        }
        list.removeIf(c -> c instanceof Completion completion && completion.suggestion().equals(arg));
      }
    }
    return tabCompleterList(args, list, key, true);
  }

  @NotNull
  public static List<Completion> rotationArgument(@NotNull CommandSender sender, @NotNull String[] args, @NotNull Object key, @Nullable Location location)
  {
    if (location == null)
    {
      location = CommandArgumentUtil.senderLocation(sender);
    }
    String yaw = Constant.Sosu2rawFormat.format(location.getYaw()), pitch = Constant.Sosu2rawFormat.format(location.getPitch());
    List<Completion> list = new ArrayList<>(Arrays.asList(
            Completion.completion("~ ~"),
            Completion.completion("~"),
            Completion.completion(yaw + " " + pitch, ComponentUtil.translate("현재 바라보고 있는 방향"))
    ));
    String arg = args[args.length - 1];
    if (!arg.equals(""))
    {
      if (arg.equals("~ ~"))
      {
        return Collections.singletonList(objectToCompletion(key));
      }
      String[] split = arg.split(" ");
      yaw = split[0];
      pitch = split.length > 1 ? split[1] : pitch;
      if (!MessageUtil.isDouble(sender, yaw, false))
      {
        if (yaw.startsWith("~"))
        {
          if (!yaw.equals("~"))
          {
            yaw = yaw.substring(1);
            if (!MessageUtil.isDouble(sender, yaw, false))
            {
              return errorMessage("argument.pos.missing.double");
            }
          }
        }
        else
        {
          return errorMessage("argument.pos.missing.double");
        }
      }
      if (!MessageUtil.isDouble(sender, pitch, false))
      {
        if (pitch.startsWith("~"))
        {
          if (!pitch.equals("~"))
          {
            pitch = pitch.substring(1);
            if (!MessageUtil.isDouble(sender, pitch, false))
            {
              return errorMessage("argument.pos.missing.double");
            }
          }
        }
        else
        {
          return errorMessage("argument.pos.missing.double");
        }
      }
      if (split.length > 2)
      {
        return errorMessage("좌표값은 2개이어야 합니다");
      }
      list.add(Completion.completion(split[0] + " " + (split.length > 1 ? split[1] : pitch)));
      list.add(Completion.completion(split[0] + " ~"));
      list.add(Completion.completion(split[0]));
      if (isErrorMessage(tabCompleterList(args, list, key)) && split.length < 2)
      {
        return errorMessage("argument.pos2d.incomplete");
      }
      list.removeIf(c -> c.suggestion().equals(arg));
    }
    return tabCompleterList(args, list, key, true);
  }

  @NotNull
  public static List<Completion> worldArgument(@NotNull CommandSender sender, @NotNull String[] args, @NotNull Object key)
  {
    List<World> worlds = Bukkit.getWorlds();
    List<Completion> list = new ArrayList<>();
    for (World world : worlds)
    {
      String tooltipString = Method.getWorldDisplayName(world);
      Component tooltip = tooltipString.equals(world.getName()) ? null : ComponentUtil.create(tooltipString);
      list.add(Completion.completion(world.getName(), tooltip));
    }
    World currentWorld = CommandArgumentUtil.senderLocation(sender).getWorld();
    String tooltipString = Method.getWorldDisplayName(currentWorld);
    Component tooltip = tooltipString.equals(currentWorld.getName()) ? ComponentUtil.translate("현재 월드 (%s)", currentWorld.getName())
            : ComponentUtil.translate("현재 월드 (%s, %s)", currentWorld.getName(), ComponentUtil.create(tooltipString));
    list.add(Completion.completion("$current_world", tooltip));
    return tabCompleterList(args, list, key);
  }

  @NotNull
  public static List<Completion> itemStackArgument(@NotNull CommandSender sender, @NotNull String[] args, @NotNull Object key)
  {
    List<Completion> list = new ArrayList<>(tabCompleterList(args, Material.values(), key, material -> material.isAir() || !material.isItem()));
    String arg = args[args.length - 1];
    boolean regex = arg.equals("") || (arg.matches("(.*[a-z0-9_-])") && !arg.contains("{"));
    if (regex)
    {
      try
      {
        if (arg.toLowerCase().equals(arg))
        {
          Material material = Material.valueOf(arg.toUpperCase());
          if (!(material.isAir() || !material.isItem()))
          {
            return List.of(Completion.completion(arg + "{"));
          }
        }
      }
      catch (Exception ignored)
      {

      }
      return list;
    }
    try
    {
      Bukkit.getItemFactory().createItemStack(args[args.length - 1]);
      return tabCompleterList(args, key, true);
    }
    catch (Exception e)
    {
      TranslatableComponent component = ItemStackUtil.getErrorCreateItemStack(e.getCause());
      return completions(ComponentUtil.translate("%s", component, "error-text"));
    }
  }

  @NotNull
  public static List<Completion> nbtArgument(@NotNull CommandSender sender, @NotNull String[] args, @NotNull Object key)
  {
    String nbt = "{" + args[args.length - 1] + "}";
    try
    {
      Bukkit.getItemFactory().createItemStack("stone" + nbt);
      return tabCompleterList(args, key, true);
    }
    catch (Exception e)
    {
      TranslatableComponent component = ItemStackUtil.getErrorCreateItemStack(e.getCause());
      return completions(ComponentUtil.translate("%s", component, "error-text"));
    }
  }

  @NotNull
  public static Completion objectToCompletion(@NotNull Object object)
  {
    return object instanceof Completion completion ? completion : Completion.completion(object.toString());
  }

  @NotNull
  public static List<Completion> sort(@NotNull List<Completion> list)
  {
    list = new ArrayList<>(list);
    for (int i = 0; i < list.size(); i++)
    {
      String s = list.get(i).suggestion();
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
      if (s.contains(" ") &&
              (!(s.startsWith("(") || s.endsWith(")") ||
                      s.startsWith("<") || s.endsWith(">") ||
                      s.startsWith("[") || s.endsWith("]")
              ) || !escaped)
      )
      {
        if (s.contains("'"))
        {
          s = s.replace("'", "''");
        }
        s = "'" + s + "'";
      }
      list.set(i, Completion.completion(s, list.get(i).tooltip()));
    }
    return list.stream().distinct().collect(Collectors.toList());
  }

  @NotNull
  public static List<Completion> removeDupe(@NotNull List<Completion> list)
  {
    return list.stream().distinct().collect(Collectors.toList());
  }

  /**
   * 여러 처리 방식을 가진 명령어에서 에러가 발생한 명령어 처리 메시지 관리자
   *
   * @param completions 처리 방식이 다른 여러 명령어들
   * @return 모든 명령어에 오류가 발생하면 모든 오류를 출력하고 아닐 경우 오류가 없는 메시지만 출력
   */
  @SafeVarargs
  @NotNull
  public static List<Completion> sortError(@NotNull List<Completion>... completions)
  {
    List<Completion> list = new ArrayList<>();
    boolean[] errors = new boolean[completions.length];
    boolean allIsError = true;
    for (int i = 0; i < errors.length; i++)
    {
      boolean b = isErrorMessage(completions[i]);
      errors[i] = b;
      if (!b)
      {
        allIsError = false;
      }
    }
    if (allIsError)
    {
      for (int i = 0; i < errors.length; i++)
      {
        list.addAll(completions[i]);
      }
    }
    else
    {
      for (int i = 0; i < errors.length; i++)
      {
        if (!errors[i])
        {
          list.addAll(completions[i]);
        }
      }
    }

    return list.stream().distinct().collect(Collectors.toList());
  }
}
