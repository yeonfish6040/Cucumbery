package com.jho5245.cucumbery.commands.sound;

import com.destroystokyo.paper.event.server.AsyncTabCompleteEvent.Completion;
import com.jho5245.cucumbery.Cucumbery;
import com.jho5245.cucumbery.util.addons.Songs;
import com.jho5245.cucumbery.util.no_groups.*;

import com.jho5245.cucumbery.util.storage.component.util.ComponentUtil;
import com.jho5245.cucumbery.util.storage.data.Constant;
import com.jho5245.cucumbery.util.storage.data.Permission;
import com.jho5245.cucumbery.util.storage.data.Prefix;
import com.jho5245.cucumbery.util.storage.data.Variable;
import com.jho5245.cucumbery.util.storage.no_groups.CustomConfig.UserData;
import com.xxmicloxx.NoteBlockAPI.model.Playlist;
import com.xxmicloxx.NoteBlockAPI.model.RepeatMode;
import com.xxmicloxx.NoteBlockAPI.model.Song;
import com.xxmicloxx.NoteBlockAPI.model.SoundCategory;
import com.xxmicloxx.NoteBlockAPI.songplayer.RadioSongPlayer;
import com.xxmicloxx.NoteBlockAPI.utils.NBSDecoder;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class CommandSong implements CucumberyCommandExecutor
{
  public static @Nullable RadioSongPlayer radioSongPlayer;

  public static final Set<String> isDownloading = new HashSet<>();

  public static @Nullable Song song;

  public static HashMap<UUID, RadioSongPlayer> playerRadio = new HashMap<>();

  public static HashMap<UUID, Song> playerSong = new HashMap<>();

  private static final List<BukkitTask> songChangeFade = new ArrayList<>();

  private static boolean changeSongCooldown = false;

  private double TICK_PERCENTAGE = 0;

  public static boolean autoNext = false;

  @Override
  public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args)
  {
    if (!Method.hasPermission(sender, Permission.CMD_SONG, true))
    {
      return true;
    }
    if (!MessageUtil.checkQuoteIsValidInArgs(sender, args = MessageUtil.wrapWithQuote(args)))
    {
      return !(sender instanceof BlockCommandSender);
    }
    if (!Cucumbery.using_NoteBlockAPI)
    {
      MessageUtil.sendError(sender, "rg255,204;NoteBlockAPI&r 플러그인을 사용하고 있지 않습니다");
      return !(sender instanceof BlockCommandSender);
    }
    int length = args.length;
    switch (cmd.getName())
    {
      case "csong" ->
      {
        if (length == 0)
        {
          MessageUtil.shortArg(sender, 1, args);
          MessageUtil.commandInfo(sender, label, Method.getUsage(cmd));
          return true;
        }
        switch (args[0])
        {
          case "play" ->
          {
            if (length < 2)
            {
              Bukkit.dispatchCommand(sender, "csong play --random");
              return true;
            }
            if (length > 2)
            {
              MessageUtil.longArg(sender, 2, args);
              MessageUtil.commandInfo(sender, label, "play <파일 이름>");
              return !(sender instanceof BlockCommandSender);
            }
            String fileName = args[1];
            boolean console = sender instanceof ConsoleCommandSender && fileName.contains("--console");
            if (console)
            {
              fileName = fileName.replace("--console", "");
            }
            boolean force = fileName.contains("--force");
            if (force)
            {
              fileName = fileName.replace("--force", "");
            }
            boolean stop = fileName.contains("--stop");
            if (stop)
            {
              fileName = fileName.replace("--stop", "");
            }
            stop = stop || (sender instanceof Player player && UserData.FORCE_PLAY_SERVER_RADIO.getBoolean(player));
            boolean disable10Octave = fileName.contains("--no10");
            if (disable10Octave)
            {
              fileName = fileName.replace("--no10", "");
            }
            boolean enableRepeat = fileName.contains("--repeat");
            if (enableRepeat)
            {
              fileName = fileName.replace("--repeat", "");
            }
            boolean silent = fileName.contains("--silent");
            if (silent)
            {
              fileName = fileName.replace("--silent", "");
            }
            boolean auto = fileName.contains("--auto");
            if (auto)
            {
              fileName = fileName.replace("--auto", "");
            }
            SoundCategory category;
            try
            {
              String categoryString = fileName.split("--c:")[1];
              category = SoundCategory.valueOf(categoryString.toUpperCase());
              fileName = fileName.replace("--c:" + categoryString, "");
            }
            catch (Exception e)
            {
              category = SoundCategory.RECORDS;
            }
            boolean random = fileName.startsWith("--random");
            String contain = random && fileName.startsWith("--random/") ? fileName.substring("--random/".length()) : "";
            List<String> list = new ArrayList<>(Variable.songFiles);
            if (!contain.equals(""))
            {
              list.removeIf(s -> !s.toLowerCase().replace(" ", "").contains(contain.toLowerCase()));
            }
            if (random && !list.isEmpty())
            {
              fileName = list.get((int) (list.size() * Math.random()));
            }
            if (!isDownloading.isEmpty())
            {
              MessageUtil.sendError(sender, "이미 재생할 노래를 불러오는 중입니다");
              return true;
            }
            if (!stop && radioSongPlayer != null && song != null)
            {
              if (!console)
              {
                MessageUtil.sendError(sender, "노래가 이미 재생중이여서 재생할 수 없습니다 (%s)", song);
                MessageUtil.info(sender, "'/csong stop' 명령어로 노래를 멈추거나 '노래이름--stop'을 입력하면 이미 재생중인 노래를 멈추고 재생할 수 있습니다");
                if (sender instanceof Player player)
                {
                  fileName += "--stop";
                  if (enableRepeat)
                  {
                    fileName += "--repeat";
                  }
                  if (disable10Octave)
                  {
                    fileName += "--no10";
                  }
                  if (silent)
                  {
                    fileName += "--silent";
                  }
                  if (force)
                  {
                    fileName += "--force";
                  }
                  if (fileName.contains(" "))
                  {
                    fileName = "'" + fileName.replace("'", "''") + "'";
                  }
                  String command = "/csong play " + fileName;
                  Component component = ComponentUtil.translate("혹은 이 메시지를 클릭하여 노래를 재생할 수 있습니다", Constant.THE_COLOR);
                  component = component.hoverEvent(Component.text(command)).clickEvent(ClickEvent.suggestCommand(command));
                  MessageUtil.info(player, component);
                }
              }
              return true;
            }
            if (!fileName.endsWith(".nbs"))
            {
              fileName += ".nbs";
            }
            try
            {
              isDownloading.add("foo");
              String finalFileName = fileName;
              SoundCategory finalCategory = category;
              Bukkit.getScheduler().runTaskLaterAsynchronously(Cucumbery.getPlugin(), () ->
              {
                File songFile = new File(Cucumbery.getPlugin().getDataFolder() + "/data/songs/" + finalFileName);
                if (force || !songFile.exists())
                {
                  List<String> songs = Songs.list;
                  if (songs.contains(finalFileName.substring(0, finalFileName.length() - 4)))
                  {
                    try
                    {
                      songFile = Songs.download(finalFileName, force);
                    }
                    catch (Exception e)
                    {
                      isDownloading.clear();
                      MessageUtil.noArg(sender, Prefix.NO_FILE, finalFileName);
                      return;
                    }
                  }
                  else
                  {
                    isDownloading.clear();
                    MessageUtil.noArg(sender, Prefix.NO_FILE, finalFileName);
                    return;
                  }
                }
                song = NBSDecoder.parse(songFile);
                if (song == null)
                {
                  isDownloading.clear();
                  MessageUtil.sendError(sender, "파일이 손상되어 재생할 수 없습니다 (%s)", finalFileName);
                  return;
                }
                if (radioSongPlayer != null)
                {
                  radioSongPlayer.setPlaying(false);
                  radioSongPlayer.destroy();
                }
                radioSongPlayer = new RadioSongPlayer(song);
                radioSongPlayer.setCategory(finalCategory);
                radioSongPlayer.setEnable10Octave(!disable10Octave);
                if (enableRepeat)
                {
                  radioSongPlayer.setRepeatMode(RepeatMode.ONE);
                }
                isDownloading.clear();
                autoNext = auto;
                if (!silent)
                {
                  MessageUtil.sendMessage(sender, Prefix.INFO_SONG, "%s을(를) 재생합니다", song);
                }
                Scheduler.fileNameLength = finalFileName.length() - 1;
                if (Scheduler.delayTask != null)
                {
                  Scheduler.delayTask.cancel();
                }
                Scheduler.delay = false;
                for (Player player : Bukkit.getServer().getOnlinePlayers())
                {
                  if (UserData.LISTEN_GLOBAL.getBoolean(player.getUniqueId()) || UserData.LISTEN_GLOBAL_FORCE.getBoolean(player.getUniqueId()))
                  {
                    radioSongPlayer.addPlayer(player);
                    if (!silent && !sender.equals(player))
                    {
                      MessageUtil.sendMessage(player, Prefix.INFO_SONG, "%s이(가) %s을(를) 재생합니다", sender, song);
                    }
                  }
                }
                radioSongPlayer.setPlaying(true);
              }, 0L);
            }
            catch (Exception e)
            {
              MessageUtil.sendError(sender, "오류 뭐");
Cucumbery.getPlugin().getLogger().warning(              e.getMessage());
            }
            return true;
          }
          case "stop" ->
          {
            if (length > 2)
            {
              MessageUtil.longArg(sender, 2, args);
              MessageUtil.commandInfo(sender, label, "stop [명령어 출력 숨김 여부]");
              return !(sender instanceof BlockCommandSender);
            }
            if (radioSongPlayer == null || song == null)
            {
              MessageUtil.sendError(sender, "노래를 재생하고 있지 않습니다");
              return !(sender instanceof BlockCommandSender);
            }
            boolean hideOutput = false;
            if (length == 2)
            {
              if (!MessageUtil.isBoolean(sender, args, 2, true))
              {
                return !(sender instanceof BlockCommandSender);
              }
              hideOutput = Boolean.parseBoolean(args[1]);
            }
            radioSongPlayer.setPlaying(false);
            radioSongPlayer.destroy();
            if (!hideOutput)
            {
              MessageUtil.sendMessage(sender, Prefix.INFO_SONG, "%s의 재생을 멈췄습니다", song);
              for (Player player : Bukkit.getServer().getOnlinePlayers())
              {
                if (UserData.LISTEN_GLOBAL.getBoolean(player.getUniqueId()) || UserData.LISTEN_GLOBAL_FORCE.getBoolean(player.getUniqueId()))
                {
                  if (!sender.equals(player))
                  {
                    MessageUtil.sendMessage(player, Prefix.INFO_SONG, "%s이(가) %s의 재생을 멈췄습니다", sender, song);
                  }
                }
              }
            }
            song = null;
            radioSongPlayer = null;
            return true;
          }
          case "pause" ->
          {
            if (length > 3)
            {
              MessageUtil.longArg(sender, 1, args);
              MessageUtil.info(sender, "/" + label + " pause [toggle|on|off] [명령어 출력 숨김 여부]");
              return !(sender instanceof BlockCommandSender);
            }
            if (radioSongPlayer == null || song == null)
            {
              MessageUtil.sendError(sender, "노래를 재생하고 있지 않습니다");
              return !(sender instanceof BlockCommandSender);
            }
            boolean playing = radioSongPlayer.isPlaying();
            if (length >= 2)
            {
              switch (args[1])
              {
                case "on" -> playing = true;
                case "off" -> playing = false;
                case "toggle" -> playing = !playing;
                default ->
                {
                  MessageUtil.wrongArg(sender, 2, args);
                  return true;
                }
              }
            }
            else
            {
              playing = !playing;
            }
            boolean hideOutput = false;
            if (length == 3)
            {
              if (!MessageUtil.isBoolean(sender, args, 3, true))
              {
                return !(sender instanceof BlockCommandSender);
              }
              hideOutput = Boolean.parseBoolean(args[2]);
            }
            radioSongPlayer.setPlaying(playing);
            if (!hideOutput)
            {
              String display = !playing ? "중지" : "재개";
              MessageUtil.sendMessage(sender, Prefix.INFO_SONG, "%s의 재생을 " + display + "했습니다", song);
              for (Player player : Bukkit.getServer().getOnlinePlayers())
              {
                if (UserData.LISTEN_GLOBAL.getBoolean(player.getUniqueId()) || UserData.LISTEN_GLOBAL_FORCE.getBoolean(player.getUniqueId()))
                {
                  if (!sender.equals(player))
                  {
                    MessageUtil.sendMessage(player, Prefix.INFO_SONG, "%s이(가) %s의 재생을 " + display + "했습니다", sender, song);
                  }
                }
              }
            }
            return true;
          }
          case "repeating" ->
          {
            if (length > 3)
            {
              MessageUtil.longArg(sender, 1, args);
              MessageUtil.info(sender, "/" + label + " repeating [toggle|on|off] [명령어 출력 숨김 여부]");
              return !(sender instanceof BlockCommandSender);
            }
            if (radioSongPlayer == null || song == null)
            {
              MessageUtil.sendError(sender, "노래를 재생하고 있지 않습니다");
              return !(sender instanceof BlockCommandSender);
            }
            boolean repeating = radioSongPlayer.getRepeatMode() == RepeatMode.ONE;
            if (length >= 2)
            {
              switch (args[1])
              {
                case "on" -> repeating = true;
                case "off" -> repeating = false;
                case "toggle" -> repeating = !repeating;
                default ->
                {
                  MessageUtil.wrongArg(sender, 2, args);
                  return true;
                }
              }
            }
            else
            {
              repeating = !repeating;
            }
            boolean hideOutput = false;
            if (length == 3)
            {
              if (!MessageUtil.isBoolean(sender, args, 3, true))
              {
                return !(sender instanceof BlockCommandSender);
              }
              hideOutput = Boolean.parseBoolean(args[2]);
            }
            radioSongPlayer.setRepeatMode(repeating ? RepeatMode.ONE : RepeatMode.NO);
            if (!hideOutput)
            {
              String display = repeating ? "활성화" : "비활성화";
              MessageUtil.sendMessage(sender, Prefix.INFO_SONG, "%s의 반복 재생 모드를 " + display + " 했습니다", song);
              for (Player player : Bukkit.getServer().getOnlinePlayers())
              {
                if (UserData.LISTEN_GLOBAL.getBoolean(player.getUniqueId()) || UserData.LISTEN_GLOBAL_FORCE.getBoolean(player.getUniqueId()))
                {
                  if (!sender.equals(player))
                  {
                    MessageUtil.sendMessage(player, Prefix.INFO_SONG, "%s이(가) %s의 반복 재생 모드를 " + display + " 했습니다", sender, song);
                  }
                }
              }
            }
            return true;
          }
          case "sound-category" ->
          {
            if (length < 2)
            {
              MessageUtil.shortArg(sender, 3, args);
              MessageUtil.commandInfo(sender, label, "sound-category <소리 범주> [명령어 출력 숨김 여부]");
              return !(sender instanceof BlockCommandSender);
            }
            if (length > 3)
            {
              MessageUtil.longArg(sender, 3, args);
              MessageUtil.commandInfo(sender, label, "sound-category <소리 범주> [명령어 출력 숨김 여부]");
              return !(sender instanceof BlockCommandSender);
            }
            if (radioSongPlayer == null || song == null)
            {
              MessageUtil.sendError(sender, "노래를 재생하고 있지 않습니다");
              return !(sender instanceof BlockCommandSender);
            }
            SoundCategory soundCategory = Method2.valueOf(args[1], SoundCategory.class);
            if (soundCategory == null)
            {
              MessageUtil.wrongArg(sender, 2, args);
              return !(sender instanceof BlockCommandSender);
            }
            boolean hideOutput = false;
            if (length == 3)
            {
              if (!MessageUtil.isBoolean(sender, args, 3, true))
              {
                return !(sender instanceof BlockCommandSender);
              }
              hideOutput = Boolean.parseBoolean(args[2]);
            }
            radioSongPlayer.setCategory(soundCategory);
            if (!hideOutput)
            {
              String s = soundCategory.toString().toLowerCase();
              if (s.endsWith("s"))
              {
                s = s.substring(0, s.length() - 1);
              }
              Component category = ComponentUtil.translate(Constant.THE_COLOR_HEX + "%s(%s)", ComponentUtil.translate("soundCategory." + s), soundCategory);
              MessageUtil.sendMessage(sender, Prefix.INFO_SONG, "%s의 재생 범주를 %s(으)로 변경했습니다", song, category);
              for (Player player : Bukkit.getServer().getOnlinePlayers())
              {
                if (UserData.LISTEN_GLOBAL.getBoolean(player.getUniqueId()) || UserData.LISTEN_GLOBAL_FORCE.getBoolean(player.getUniqueId()))
                {
                  if (!sender.equals(player))
                  {
                    MessageUtil.sendMessage(player, Prefix.INFO_SONG, "%s이(가) %s의 재생 범주를 %s(으)로 변경했습니다", sender, song, category);
                  }
                }
              }
            }
            return true;
          }
          case "octave-limit" ->
          {
            if (length > 3)
            {
              MessageUtil.longArg(sender, 1, args);
              MessageUtil.info(sender, "/" + label + " octave-limit [toggle|on|off] [명령어 출력 숨김 여부]");
              return !(sender instanceof BlockCommandSender);
            }
            if (radioSongPlayer == null || song == null)
            {
              MessageUtil.sendError(sender, "노래를 재생하고 있지 않습니다");
              return !(sender instanceof BlockCommandSender);
            }
            boolean octaveLimit = !radioSongPlayer.isEnable10Octave();
            if (length >= 2)
            {
              switch (args[1])
              {
                case "on" -> octaveLimit = true;
                case "off" -> octaveLimit = false;
                case "toggle" -> octaveLimit = !octaveLimit;
                default ->
                {
                  MessageUtil.wrongArg(sender, 2, args);
                  return true;
                }
              }
            }
            else
            {
              octaveLimit = !octaveLimit;
            }
            boolean hideOutput = false;
            if (length == 3)
            {
              if (!MessageUtil.isBoolean(sender, args, 3, true))
              {
                return !(sender instanceof BlockCommandSender);
              }
              hideOutput = Boolean.parseBoolean(args[2]);
            }
            radioSongPlayer.setEnable10Octave(!octaveLimit);
            if (!hideOutput)
            {
              String display = octaveLimit ? "활성화" : "비활성화";
              MessageUtil.sendMessage(sender, Prefix.INFO_SONG, "%s의 옥타브 제한 모드를 " + display + " 했습니다", song);
              for (Player player : Bukkit.getServer().getOnlinePlayers())
              {
                if (UserData.LISTEN_GLOBAL.getBoolean(player.getUniqueId()) || UserData.LISTEN_GLOBAL_FORCE.getBoolean(player.getUniqueId()))
                {
                  if (!sender.equals(player))
                  {
                    MessageUtil.sendMessage(player, Prefix.INFO_SONG, "%s이(가) %s의 옥타브 제한 모드를 " + display + " 했습니다", sender, song);
                  }
                }
              }
            }
            return true;
          }
          case "change-tick" ->
          {
            if (length < 2)
            {
              MessageUtil.shortArg(sender, 3, args);
              MessageUtil.commandInfo(sender, label, "change-tick <재생 비율(%)> [명령어 출력 숨김 여부]");
              return !(sender instanceof BlockCommandSender);
            }
            if (length > 3)
            {
              MessageUtil.longArg(sender, 3, args);
              MessageUtil.commandInfo(sender, label, "change-tick <재생 비율(%)> [명령어 출력 숨김 여부]");
              return !(sender instanceof BlockCommandSender);
            }
            if (radioSongPlayer == null || song == null)
            {
              MessageUtil.sendError(sender, "노래를 재생하고 있지 않습니다");
              return !(sender instanceof BlockCommandSender);
            }
            if (!MessageUtil.isDouble(sender, args[1], true))
            {
              return !(sender instanceof BlockCommandSender);
            }
            double ratio = Double.parseDouble(args[1]);
            if (!MessageUtil.checkNumberSize(sender, ratio, 0, 100, false, true))
            {
              return !(sender instanceof BlockCommandSender);
            }
            boolean hideOutput = false;
            if (length == 3)
            {
              if (!MessageUtil.isBoolean(sender, args, 3, true))
              {
                return !(sender instanceof BlockCommandSender);
              }
              hideOutput = Boolean.parseBoolean(args[2]);
            }
            radioSongPlayer.setTick((short) (song.getLength() * ratio / 100));
            if (!hideOutput)
            {
              MessageUtil.sendMessage(sender, Prefix.INFO_SONG, "%s의 재생 비율을 %s(으)로 설정했습니다 (%s)", song, Constant.Sosu2.format(ratio) + "%",
                      Method.timeFormatMilli((long) (radioSongPlayer.getTick() / song.getSpeed() * 20L * 50L), true, 1));
              for (Player player : Bukkit.getServer().getOnlinePlayers())
              {
                if (UserData.LISTEN_GLOBAL.getBoolean(player.getUniqueId()) || UserData.LISTEN_GLOBAL_FORCE.getBoolean(player.getUniqueId()))
                {
                  if (!sender.equals(player))
                  {
                    MessageUtil.sendMessage(player, Prefix.INFO_SONG, "%s이(가) %s의 재생 비율을 %s(으)로 설정했습니다 (%s)", sender, song, Constant.Sosu2.format(ratio) + "%",
                            Method.timeFormatMilli((long) (radioSongPlayer.getTick() / song.getSpeed() * 20L * 50L), true, 1));
                  }
                }
              }
            }
            return true;
          }
          case "change-song" ->
          {
            if (length < 2)
            {
              MessageUtil.shortArg(sender, 2, args);
              MessageUtil.commandInfo(sender, label, "change-song <파일 이름> [처음부터 재생] [페이드] [명령어 출력 숨김 여부]");
              return !(sender instanceof BlockCommandSender);
            }
            if (length > 5)
            {
              MessageUtil.longArg(sender, 3, args);
              MessageUtil.commandInfo(sender, label, "change-song <파일 이름> [처음부터 재생] [페이드] [명령어 출력 숨김 여부]");
              return !(sender instanceof BlockCommandSender);
            }
            if (radioSongPlayer == null || song == null)
            {
              MessageUtil.sendError(sender, "노래를 재생하고 있지 않습니다");
              return !(sender instanceof BlockCommandSender);
            }
            if (changeSongCooldown)
            {
              MessageUtil.sendError(sender, "아직 노래를 변경할 수 없습니다");
              return true;
            }
            String fileName = args[1];
            boolean reset = false;
            if (length >= 3)
            {
              if (!MessageUtil.isBoolean(sender, args, 3, true))
              {
                return !(sender instanceof BlockCommandSender);
              }
              reset = Boolean.parseBoolean(args[2]);
            }
            boolean fade = true;
            if (length >= 4)
            {
              if (!MessageUtil.isBoolean(sender, args, 4, true))
              {
                return !(sender instanceof BlockCommandSender);
              }
              fade = Boolean.parseBoolean(args[3]);
            }
            boolean hideOutput = false;
            if (length == 5)
            {
              if (!MessageUtil.isBoolean(sender, args, 5, true))
              {
                return !(sender instanceof BlockCommandSender);
              }
              hideOutput = Boolean.parseBoolean(args[4]);
            }
            boolean force = fileName.contains("--force");
            if (force)
            {
              fileName = fileName.replace("--force", "");
            }
            boolean random = fileName.startsWith("--random");
            String contain = random && fileName.startsWith("--random/") ? fileName.substring("--random/".length()) : "";
            List<String> list = new ArrayList<>(Variable.songFiles);
            if (!contain.equals(""))
            {
              list.removeIf(s -> !s.toLowerCase().replace(" ", "").contains(contain.toLowerCase()));
            }
            if (random && !list.isEmpty())
            {
              fileName = list.get((int) (list.size() * Math.random()));
            }
            if (!fileName.endsWith(".nbs"))
            {
              fileName += ".nbs";
            }
            try
            {
              File songFile = new File(Cucumbery.getPlugin().getDataFolder() + "/data/songs/" + fileName);
              if (force || !songFile.exists())
              {
                List<String> songs = Songs.list;
                if (songs.contains(fileName.substring(0, fileName.length() - 4)))
                {
                  try
                  {
                    songFile = Songs.download(fileName, force);
                  }
                  catch (Exception e)
                  {
                    MessageUtil.noArg(sender, Prefix.NO_FILE, fileName);
                    return true;
                  }
                }
                else
                {
                  MessageUtil.noArg(sender, Prefix.NO_FILE, fileName);
                  return true;
                }
              }
              song = NBSDecoder.parse(songFile);
              if (song == null)
              {
                MessageUtil.sendError(sender, "파일이 손상되어 재생할 수 없습니다 (%s)", fileName);
                return true;
              }
              int songLength = radioSongPlayer.getSong().getLength();
              radioSongPlayer.setPlaylist(new Playlist(song));
              if (!hideOutput)
              {
                MessageUtil.sendMessage(sender, Prefix.INFO_SONG, "%s을(를) 재생합니다", song);
                for (Player player : Bukkit.getServer().getOnlinePlayers())
                {
                  if (UserData.LISTEN_GLOBAL.getBoolean(player.getUniqueId()) || UserData.LISTEN_GLOBAL_FORCE.getBoolean(player.getUniqueId()))
                  {
                    radioSongPlayer.addPlayer(player);
                    if (!sender.equals(player))
                    {
                      MessageUtil.sendMessage(player, Prefix.INFO_SONG, "%s이(가) %s을(를) 재생합니다", sender, song);
                    }
                  }
                }
              }
              Scheduler.fileNameLength = fileName.length() - 1;
              if (Scheduler.delayTask != null)
              {
                Scheduler.delayTask.cancel();
              }
              Scheduler.delay = false;
              radioSongPlayer.setPlaying(true);
              if (fade)
              {
                changeSongCooldown = true;
                Bukkit.getScheduler().runTaskLater(Cucumbery.getPlugin(), () -> changeSongCooldown = false, 30);
                if (!songChangeFade.isEmpty())
                {
                  for (BukkitTask bukkitTask : songChangeFade)
                  {
                    bukkitTask.cancel();
                  }
                  songChangeFade.clear();
                }
                int volume = radioSongPlayer.getVolume();
                for (int i = 1; i <= 15; i++)
                {
                  songChangeFade.add(Bukkit.getScheduler().runTaskLater(Cucumbery.getPlugin(), () -> radioSongPlayer.setVolume((byte) (radioSongPlayer.getVolume() * 0.9)), i));
                }
                songChangeFade.add(Bukkit.getScheduler().runTaskLater(Cucumbery.getPlugin(), () -> TICK_PERCENTAGE = 1d * radioSongPlayer.getTick() / songLength, 16));
                songChangeFade.add(Bukkit.getScheduler().runTaskLater(Cucumbery.getPlugin(), () -> radioSongPlayer.playSong(0), 16));
                boolean finalReset = reset;
                songChangeFade.add(Bukkit.getScheduler().runTaskLater(Cucumbery.getPlugin(), () -> radioSongPlayer.setTick(finalReset ? 0 : (short) (TICK_PERCENTAGE * radioSongPlayer.getSong().getLength())), 16));
                for (int i = 17; i <= 30; i += 1)
                {
                  int finalI = i;
                  songChangeFade.add(Bukkit.getScheduler().runTaskLater(Cucumbery.getPlugin(), () -> radioSongPlayer.setVolume((byte) (volume - (60 - finalI * 2))), i));
                }
              }
              else
              {
                radioSongPlayer.playSong(0);
                radioSongPlayer.setTick(reset ? 0 : (short) (TICK_PERCENTAGE * radioSongPlayer.getSong().getLength()));
                TICK_PERCENTAGE = 0;
              }
            }
            catch (Exception e)
            {
              MessageUtil.sendError(sender, "오류 뭐");
Cucumbery.getPlugin().getLogger().warning(              e.getMessage());
            }
            return true;
          }
          case "volume" ->
          {
            if (length < 2)
            {
              MessageUtil.shortArg(sender, 3, args);
              MessageUtil.commandInfo(sender, label, "volume <볼륨> [명령어 출력 숨김 여부]");
              return !(sender instanceof BlockCommandSender);
            }
            if (length > 3)
            {
              MessageUtil.longArg(sender, 3, args);
              MessageUtil.commandInfo(sender, label, "volume <볼륨> [명령어 출력 숨김 여부]");
              return !(sender instanceof BlockCommandSender);
            }
            if (radioSongPlayer == null || song == null)
            {
              MessageUtil.sendError(sender, "노래를 재생하고 있지 않습니다");
              return !(sender instanceof BlockCommandSender);
            }
            if (!MessageUtil.isInteger(sender, args[1], true))
            {
              return !(sender instanceof BlockCommandSender);
            }
            int volume = Integer.parseInt(args[1]);
            if (!MessageUtil.checkNumberSize(sender, volume, 0, 100))
            {
              return !(sender instanceof BlockCommandSender);
            }
            boolean hideOutput = false;
            if (length == 3)
            {
              if (!MessageUtil.isBoolean(sender, args, 3, true))
              {
                return !(sender instanceof BlockCommandSender);
              }
              hideOutput = Boolean.parseBoolean(args[2]);
            }
            radioSongPlayer.setVolume((byte) volume);
            if (!hideOutput)
            {
              MessageUtil.sendMessage(sender, Prefix.INFO_SONG, "%s의 볼륨을 %s(으)로 설정했습니다", song, volume);
              for (Player player : Bukkit.getServer().getOnlinePlayers())
              {
                if (UserData.LISTEN_GLOBAL.getBoolean(player.getUniqueId()) || UserData.LISTEN_GLOBAL_FORCE.getBoolean(player.getUniqueId()))
                {
                  if (!sender.equals(player))
                  {
                    MessageUtil.sendMessage(player, Prefix.INFO_SONG, "%s이(가) %s의 재생 볼륨을 %s(으)로 설정했습니다", sender, song, volume);
                  }
                }
              }
            }
            return true;
          }
          case "listening" ->
          {
            if (length > 1)
            {
              MessageUtil.longArg(sender, 1, args);
              MessageUtil.commandInfo(sender, label, "listening");
              return !(sender instanceof BlockCommandSender);
            }
            if (radioSongPlayer == null || song == null)
            {
              MessageUtil.sendError(sender, "노래를 재생하고 있지 않습니다");
              return !(sender instanceof BlockCommandSender);
            }
            List<Player> players = new ArrayList<>();
            for (Player player : Bukkit.getServer().getOnlinePlayers())
            {
              if (UserData.LISTEN_GLOBAL.getBoolean(player.getUniqueId()) || UserData.LISTEN_GLOBAL_FORCE.getBoolean(player.getUniqueId()))
              {
                players.add(player);
              }
            }
            if (players.isEmpty())
            {
              MessageUtil.sendMessage(sender, Prefix.INFO_SONG, "아무도 노래를 듣고 있지 않습니다");
              return true;
            }
            MessageUtil.sendMessage(sender, Prefix.INFO_SONG, Constant.SEPARATOR);
            MessageUtil.sendMessage(sender, Prefix.INFO_SONG, "듣고 있는 사람 : %s명", Constant.THE_COLOR_HEX + players.size());
            for (Player player : players)
            {
              MessageUtil.sendMessage(sender, Prefix.INFO_SONG, player);
            }
            return true;
          }
          case "info" ->
          {
            if (length > 1)
            {
              MessageUtil.longArg(sender, 1, args);
              MessageUtil.commandInfo(sender, label, "info");
              return !(sender instanceof BlockCommandSender);
            }
            if (radioSongPlayer == null || song == null)
            {
              MessageUtil.sendError(sender, "노래를 재생하고 있지 않습니다");
              return !(sender instanceof BlockCommandSender);
            }

            MessageUtil.sendMessage(sender, Prefix.INFO_SONG, Constant.SEPARATOR);
            MessageUtil.sendMessage(sender, Prefix.INFO_SONG, "파일 이름 : %s", Constant.THE_COLOR_HEX + song.getPath().getName());
            MessageUtil.sendMessage(sender, Prefix.INFO_SONG, "제작자 : %s", Constant.THE_COLOR_HEX + song.getAuthor());
            MessageUtil.sendMessage(sender, Prefix.INFO_SONG, "원 제작자 : %s", Constant.THE_COLOR_HEX + song.getOriginalAuthor());
            MessageUtil.sendMessage(sender, Prefix.INFO_SONG, "노래 제목 : %s", Constant.THE_COLOR_HEX + song.getTitle());
            MessageUtil.sendMessage(sender, Prefix.INFO_SONG, "노래 설명 : %s", song.getDescription());

            MessageUtil.sendMessage(sender, Prefix.INFO_SONG, "속도 : %sTPS", Constant.THE_COLOR_HEX + Constant.Sosu2.format(song.getSpeed()));
            MessageUtil.sendMessage(sender, Prefix.INFO_SONG, "길이 : %s", Constant.THE_COLOR_HEX + Method.timeFormatMilli((long) ((song.getLength() / song.getSpeed()) * 1000L), true, 1));
            MessageUtil.sendMessage(sender, Prefix.INFO_SONG, Constant.SEPARATOR);
            return true;
          }
          default ->
          {
            MessageUtil.wrongArg(sender, 1, args);
            MessageUtil.commandInfo(sender, label, Method.getUsage(cmd));
            return !(sender instanceof BlockCommandSender);
          }
        }
      }
      case "csong2" ->
      {
        if (args.length < 2)
        {
          MessageUtil.shortArg(sender, 2, args);
          MessageUtil.commandInfo(sender, label, Method.getUsage(cmd));
          return true;
        }
        Player player = SelectorUtil.getPlayer(sender, args[0]);
        if (player == null)
        {
          return true;
        }
        UUID uuid = player.getUniqueId();
        switch (args[1])
        {
          case "play" ->
          {
            if (args.length < 4)
            {
              MessageUtil.shortArg(sender, 4, args);
              MessageUtil.commandInfo(sender, label, player.getName() + " play <명령어 출력 숨김 여부> <노래 파일 이름>");
              return true;
            }
            if (!MessageUtil.isBoolean(sender, args, 3, true))
            {
              return true;
            }
            boolean hideOutput = Boolean.parseBoolean(args[2]);
            String fileName = MessageUtil.listToString(" ", 3, args.length, args);
            boolean force = fileName.contains("--force");
            if (force)
            {
              fileName = fileName.replace("--force", "");
            }
            boolean disable10Octave = fileName.contains("--no10");
            if (disable10Octave)
            {
              fileName = fileName.replace("--no10", "");
            }
            boolean enableRepeat = fileName.contains("--repeat");
            if (enableRepeat)
            {
              fileName = fileName.replace("--repeat", "");
            }
            SoundCategory category;
            try
            {
              String categoryString = fileName.split("--c:")[1];
              category = SoundCategory.valueOf(categoryString.toUpperCase());
              fileName = fileName.replace("--c:" + categoryString, "");
            }
            catch (Exception e)
            {
              category = SoundCategory.RECORDS;
            }
            boolean random = fileName.startsWith("--random");
            String contain = random && fileName.startsWith("--random/") ? fileName.substring("--random/".length()) : "";
            List<String> list = new ArrayList<>(Variable.songFiles);
            if (!contain.equals(""))
            {
              list.removeIf(s -> !s.toLowerCase().replace(" ", "").contains(contain.toLowerCase()));
            }
            if (random && !list.isEmpty())
            {
              fileName = list.get((int) (list.size() * Math.random()));
            }
            if (!fileName.endsWith(".nbs"))
            {
              fileName += ".nbs";
            }
            try
            {
              File songFile = Songs.download(fileName, force);
              if (!songFile.exists())
              {
                MessageUtil.noArg(sender, Prefix.NO_FILE, fileName);
                return true;
              }
              RadioSongPlayer playerRadio = CommandSong.playerRadio.get(uuid);
              Song playerSong = CommandSong.playerSong.get(player.getUniqueId());
              if (playerRadio != null && playerSong != null)
              {
                playerRadio.setPlaying(false);
                playerRadio.destroy();
              }
              playerSong = NBSDecoder.parse(songFile);
              if (playerSong == null)
              {
                MessageUtil.sendError(sender, "파일이 손상되어 재생할 수 없습니다 (%s)", fileName);
                return true;
              }
              playerRadio = new RadioSongPlayer(playerSong);
              playerRadio.setCategory(category);
              playerRadio.setEnable10Octave(!disable10Octave);
              if (enableRepeat)
              {
                playerRadio.setRepeatMode(RepeatMode.ONE);
              }
              playerRadio.setAutoDestroy(true);
              if (!hideOutput)
              {
                MessageUtil.sendMessage(sender, Prefix.INFO_SONG, ComponentUtil.translate("%s에게 %s을(를) 재생합니다", player, playerSong));
              }
              if (UserData.LISTEN_GLOBAL.getBoolean(uuid) || UserData.LISTEN_GLOBAL_FORCE.getBoolean(uuid))
              {
                playerRadio.addPlayer(player);
                if (!hideOutput && !player.equals(sender))
                {
                  MessageUtil.sendMessage(player, Prefix.INFO_SONG, ComponentUtil.translate("%s이(가) 당신에게 %s을(를) 재생합니다", sender, playerSong));
                }
              }
              playerRadio.setPlaying(true);
              CommandSong.playerRadio.put(uuid, playerRadio);
              CommandSong.playerSong.put(uuid, playerSong);
            }
            catch (IOException e)
            {
              MessageUtil.noArg(sender, Prefix.NO_FILE, fileName);
              return true;
            }
            catch (Exception e)
            {
              MessageUtil.sendError(sender, "오류 뭐");
              MessageUtil.info(sender, e.getMessage());
              MessageUtil.info(sender, e.getStackTrace()[0]);
            }
          }
          case "stop" ->
          {
            if (args.length > 3)
            {
              MessageUtil.longArg(sender, 3, args);
              MessageUtil.commandInfo(sender, label, player.getName() + " stop [명령어 출력 숨김 여부]");
              return true;
            }
            boolean hideOutput = false;
            if (args.length == 3)
            {
              if (!MessageUtil.isBoolean(sender, args, 3, true))
              {
                return true;
              }
              hideOutput = Boolean.parseBoolean(args[2]);
            }
            try
            {
              RadioSongPlayer playerRadio = CommandSong.playerRadio.get(uuid);
              if (playerRadio == null)
              {
                MessageUtil.sendError(sender, "노래를 재생하고 있지 않습니다");
                return true;
              }
              Song playerSong = playerRadio.getSong();
              playerRadio.setPlaying(false);
              playerRadio.destroy();
              if (!hideOutput)
              {
                MessageUtil.sendMessage(sender, Prefix.INFO_SONG, ComponentUtil.translate("%s의 %s의 재생을 멈췄습니다", player, playerSong));
              }
              if (UserData.LISTEN_GLOBAL.getBoolean(uuid) || UserData.LISTEN_GLOBAL_FORCE.getBoolean(uuid))
              {
                if (!hideOutput && !player.equals(sender))
                {
                  MessageUtil.sendMessage(player, Prefix.INFO_SONG, ComponentUtil.translate("%s이(가) 당신의 %s의 재생을 멈췄습니다", sender, playerSong));
                }
              }
              CommandSong.playerRadio.remove(uuid);
              CommandSong.playerSong.remove(uuid);
            }
            catch (Exception e)
            {
              MessageUtil.sendError(sender, "노래의 재생을 멈추는 도중에 알 수 없는 오류가 발생했습니다");
Cucumbery.getPlugin().getLogger().warning(              e.getMessage());
            }
          }
          case "info" ->
          {
            RadioSongPlayer playerRadio = CommandSong.playerRadio.get(uuid);
            if (playerRadio == null)
            {
              MessageUtil.sendError(sender, ComponentUtil.translate("%s은(는) 노래를 재생하고 있지 않습니다", player));
              return true;
            }
            Song playerSong = playerRadio.getSong();
            MessageUtil.sendMessage(sender, Prefix.INFO_SONG, Constant.SEPARATOR);
            MessageUtil.sendMessage(sender, Prefix.INFO_SONG, ComponentUtil.translate("파일 이름 : %s", playerSong));
            MessageUtil.sendMessage(sender, Prefix.INFO_SONG, ComponentUtil.translate("길이 : %s", playerSong.getLength()));
          }
          default ->
          {
            MessageUtil.wrongArg(sender, 2, args);
            return true;
          }
        }
      }
    }
    return true;
  }

  @Override
  @NotNull
  @SuppressWarnings({"unchecked", "rawtypes"})
  public List<Completion> completion(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args, @NotNull Location location)
  {
    if (!Cucumbery.using_NoteBlockAPI)
    {
      return CommandTabUtil.errorMessage("NoteBlockAPI 플러그인을 사용하고 있지 않습니다");
    }
    int length = args.length;
    switch (cmd.getName())
    {
      case "csong" ->
      {
        if (length == 1)
        {
          return CommandTabUtil.tabCompleterList(args, "<인수>", false,
                  Completion.completion("play", ComponentUtil.translate("서버 전체에 노래를 재생함니다")),
                  Completion.completion("stop", ComponentUtil.translate("재생 중인 노래를 멈춥니다")),
                  Completion.completion("info", ComponentUtil.translate("재생 중인 노래의 정보를 참조합니다")),
                  Completion.completion("listening", ComponentUtil.translate("재생 중인 노래를 듣오 있는 플레이어 목록을 참조합니다")),
                  Completion.completion("pause", ComponentUtil.translate("재생 중인 노래를 일시 중지합니다")),
                  Completion.completion("repeating", ComponentUtil.translate("재생 중인 노래를 반복 모드로 전환합니다")),
                  Completion.completion("sound-category", ComponentUtil.translate("재생 중인 노래의 재생 범주를 변경합니다")),
                  Completion.completion("octave-limit", ComponentUtil.translate("재생 중인 노래의 음역대 제한 모드를 전환합니다")),
                  Completion.completion("change-tick", ComponentUtil.translate("재생 중인 노래의 비율을 변경합니다")),
                  Completion.completion("change-song", ComponentUtil.translate("재생 중인 노래의 파일을 변경합니다")),
                  Completion.completion("volume", ComponentUtil.translate("재생 중인 노래의 볼륨을 변경합니다")));
        }
        if (length == 2)
        {
          switch (args[0])
          {
            case "pause", "repeating", "octave-limit" ->
            {
              return CommandTabUtil.tabCompleterList(args, "[인수]", false, "on", "toggle", "off");
            }
            case "sound-category" ->
            {
//            return CommandTabUtil.tabCompleterList(args, SoundCategory.values(), "<소리 범주>");
              try
              {
                Class<?> clazz = Class.forName("com.xxmicloxx.NoteBlockAPI.model.SoundCategory");
                return CommandTabUtil.tabCompleterList(args, (Enum[]) clazz.getMethod("values").invoke(clazz), "<소리 범주>");
              }
              catch (Exception ignored)
              {
              }
            }
            case "play", "change-song" ->
            {
              Variable.songFiles.addAll(Songs.list);
              List<Completion> list = new ArrayList<>();
              Variable.songFiles.forEach(s -> list.add(Completion.completion(s)));
              if (!Variable.songFiles.isEmpty())
              {
                list.add(Completion.completion("--random", ComponentUtil.translate("무작위 노래; '--random/(문자열)'으로 필터 가능")));
              }
              return CommandTabUtil.tabCompleterList(args, list, "<노래 파일>", true);
            }
            case "change-tick" ->
            {
              return CommandTabUtil.tabCompleterDoubleRadius(args, 0, false, 100, true, "<재생 비율(%)>");
            }
            case "volume" ->
            {
              return CommandTabUtil.tabCompleterIntegerRadius(args, 0, 100, "<볼륨>");
            }
          }
        }
        if (length == 3)
        {
          switch (args[0])
          {
            case "pause", "repeating", "octave-limit", "change-tick", "volume", "sound-category" ->
            {
              return CommandTabUtil.tabCompleterBoolean(args, "[명령어 출력 숨김 여부]");
            }
            case "change-song" ->
            {
              return CommandTabUtil.tabCompleterBoolean(args, "[처음부터 재생]");
            }
          }
        }
        if (length == 4)
        {
          if ("change-song".equals(args[0]))
          {
            return CommandTabUtil.tabCompleterBoolean(args, "[페이드]");
          }
        }
        if (length == 5)
        {
          if ("change-song".equals(args[0]))
          {
            return CommandTabUtil.tabCompleterBoolean(args, "[명령어 출력 숨김 여부]");
          }
        }
      }
      case "csong2" ->
      {
        if (length == 1)
        {
          return CommandTabUtil.tabCompleterPlayer(sender, args, "<플레이어>");
        }
        else if (length == 2)
        {
          return CommandTabUtil.tabCompleterList(args, "<인수>", false, "play", "stop", "info");
        }
        else if (length == 3)
        {
          switch (args[1])
          {
            case "play":
              return CommandTabUtil.tabCompleterBoolean(args, "<명령어 출력 숨김 여부>");
            case "stop":
              return CommandTabUtil.tabCompleterBoolean(args, "[명령어 출력 숨김 여부]");
          }
        }
        else if (args[1].equals("play"))
        {
          if (args.length == 4)
          {
            Variable.songFiles.addAll(Songs.list);
            List<Completion> list = new ArrayList<>();
            Variable.songFiles.forEach(s -> list.add(Completion.completion(s)));
            if (!Variable.songFiles.isEmpty())
            {
              list.add(Completion.completion("--random", ComponentUtil.translate("무작위 노래; '--random/(문자열)'으로 필터 가능")));
            }
            return CommandTabUtil.tabCompleterList(args, list, "<노래 파일>", true);
          }
        }
      }
    }
    return CommandTabUtil.ARGS_LONG;
  }
}
