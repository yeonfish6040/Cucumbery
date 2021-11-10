package com.jho5245.cucumbery.commands.sound;

import com.jho5245.cucumbery.Cucumbery;
import com.jho5245.cucumbery.util.MessageUtil;
import com.jho5245.cucumbery.util.Method;
import com.jho5245.cucumbery.util.SelectorUtil;
import com.jho5245.cucumbery.util.addons.Songs;
import com.jho5245.cucumbery.util.storage.CustomConfig.UserData;
import com.jho5245.cucumbery.util.storage.component.util.ComponentUtil;
import com.jho5245.cucumbery.util.storage.data.Permission;
import com.jho5245.cucumbery.util.storage.data.Prefix;
import com.jho5245.cucumbery.util.storage.data.Variable;
import com.xxmicloxx.NoteBlockAPI.model.RepeatMode;
import com.xxmicloxx.NoteBlockAPI.model.Song;
import com.xxmicloxx.NoteBlockAPI.model.SoundCategory;
import com.xxmicloxx.NoteBlockAPI.songplayer.RadioSongPlayer;
import com.xxmicloxx.NoteBlockAPI.utils.NBSDecoder;
import org.bukkit.Bukkit;
import org.bukkit.command.*;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.util.*;

public class CommandSong implements CommandExecutor, TabCompleter
{
  public static @Nullable RadioSongPlayer radioSongPlayer;

  public static @Nullable Song song;

  public static HashMap<UUID, RadioSongPlayer> playerRadio = new HashMap<>();

  public static HashMap<UUID, Song> playerSong = new HashMap<>();

  public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args)
  {
    if (!Method.hasPermission(sender, Permission.CMD_SONG, true))
    {
      return true;
    }
    // for console disable quote command feature
    if (sender instanceof Player)
    {
      if (!MessageUtil.checkQuoteIsValidInArgs(sender, args = MessageUtil.wrapWithQuote(args)))
      {
        return !(sender instanceof BlockCommandSender);
      }
    }
    if (!Cucumbery.using_NoteBlockAPI)
    {
      MessageUtil.sendError(sender, "&eNoteBlockAPI&r 플러그인을 사용하고 있지 않습니다.");
      return true;
    }
    switch (cmd.getName())
    {
      case "csong":
        if (args.length == 0)
        {
          MessageUtil.shortArg(sender, 1, args);
          MessageUtil.commandInfo(sender, label, Method.getUsage(cmd));
          return true;
        }
        else if (args.length == 1)
        {
          switch (args[0])
          {
            case "stop":
              try
              {
                if (radioSongPlayer == null || song == null)
                {
                  MessageUtil.sendError(sender, "노래를 재생하고 있지 않습니다.");
                  return true;
                }
                radioSongPlayer.setPlaying(false);
                radioSongPlayer.destroy();
                MessageUtil.sendMessage(sender, Prefix.INFO_SONG, ComponentUtil.createTranslate("%s의 재생을 멈췄습니다.", song));
                for (Player player : Bukkit.getServer().getOnlinePlayers())
                {
                  if (UserData.LISTEN_GLOBAL.getBoolean(player.getUniqueId()) || UserData.LISTEN_GLOBAL_FORCE.getBoolean(player.getUniqueId()))
                  {
                    if (!sender.equals(player))
                    {
                      MessageUtil.sendMessage(player, Prefix.INFO_SONG, ComponentUtil.createTranslate("%s이(가) %s의 재생을 멈췄습니다.", sender, song));
                    }
                  }
                }
                song = null;
                radioSongPlayer = null;
                return true;
              }
              catch (Exception e)
              {
                MessageUtil.sendError(sender, "노래의 재생을 멈추는 도중에 알 수 없는 오류가 발생하였습니다.");
                e.printStackTrace();
                return true;
              }
            case "pause":
            {
              if (radioSongPlayer == null || song == null)
              {
                MessageUtil.sendError(sender, "노래를 재생하고 있지 않습니다.");
                return true;
              }
              boolean playing = radioSongPlayer.isPlaying();
              radioSongPlayer.setPlaying(!playing);
              String display = playing ? "중지" : "재개";
              MessageUtil.sendMessage(sender, Prefix.INFO_SONG, ComponentUtil.createTranslate("%s의 재생을 " + display + "하였습니다.", song));
              for (Player player : Bukkit.getServer().getOnlinePlayers())
              {
                if (UserData.LISTEN_GLOBAL.getBoolean(player.getUniqueId()) || UserData.LISTEN_GLOBAL_FORCE.getBoolean(player.getUniqueId()))
                {
                  if (!sender.equals(player))
                  {
                    MessageUtil.sendMessage(player, Prefix.INFO_SONG, ComponentUtil.createTranslate("%s이(가) %s의 재생을 " + display + "하였습니다.", sender, song));
                  }
                }
              }
              return true;
            }
            case "info":
              if (radioSongPlayer == null || song == null)
              {
                MessageUtil.sendMessage(sender, Prefix.INFO_SONG, "노래를 재생하고 있지 않습니다.");
                return true;
              }
              MessageUtil.sendMessage(sender, Prefix.INFO_SONG, "--------------------------------------------");
              MessageUtil.sendMessage(sender, Prefix.INFO_SONG, "파일 이름 : &e" + radioSongPlayer.getSong().getPath().getName());
              MessageUtil.sendMessage(sender, Prefix.INFO_SONG, "길이 : &e" + radioSongPlayer.getSong().getLength());
              return true;
            case "play":
              MessageUtil.shortArg(sender, 2, args);
              MessageUtil.info(sender, "/" + label + " play <파일 이름(.nbs 확장자 제외)>");
              return true;
            case "playist":
              MessageUtil.shortArg(sender, 2, args);
              MessageUtil.info(sender, "/" + label + " <create|remove|list|modify|info> ...");
              return true;
            case "listening":
              if (radioSongPlayer == null || song == null)
              {
                MessageUtil.sendMessage(sender, Prefix.INFO_SONG, "노래를 재생하고 있지 않습니다.");
                return true;
              }
              List<Player> players = new ArrayList<>();
              song.getLength();
              for (Player player : Bukkit.getServer().getOnlinePlayers())
              {
                if (UserData.LISTEN_GLOBAL.getBoolean(player.getUniqueId()) || UserData.LISTEN_GLOBAL_FORCE.getBoolean(player.getUniqueId()))
                {
                  players.add(player);
                }
              }
              if (players.isEmpty())
              {
                MessageUtil.sendMessage(sender, Prefix.INFO_SONG, "아무도 노래를 듣고 있지 않습니다.");
                return true;
              }
              MessageUtil.sendMessage(sender, Prefix.INFO_SONG, "--------------------------------------------");
              MessageUtil.sendMessage(sender, Prefix.INFO_SONG, "듣고 있는 사람 : &e" + players.size() + "명");
              for (Player player : players)
              {
                MessageUtil.sendMessage(sender, Prefix.INFO_SONG, player);
              }
              return true;
            default:
              MessageUtil.wrongArg(sender, 1, args);
              return true;
          }
        }
        else
        {
          switch (args[0])
          {
            case "stop", "info", "listening" -> {
              MessageUtil.longArg(sender, 1, args);
              MessageUtil.info(sender, "/" + label + " " + args[0]);
              return true;
            }
            case "play" -> {
              String fileName = MessageUtil.listToString(" ", 1, args.length, args);

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
              boolean silent = fileName.contains("--silent");
              if (silent)
              {
                fileName = fileName.replace("--silent", "");
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
              if (!fileName.endsWith(".nbs"))
              {
                fileName += ".nbs";
              }
              try
              {
                File songFile = new File(Cucumbery.getPlugin().getDataFolder() + "/data/songs/" + fileName);

                if (!songFile.exists())
                {
                  List<String> songs = Songs.list;
                  if (songs.contains(fileName))
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
                if (radioSongPlayer != null && song != null)
                {
                  radioSongPlayer.setPlaying(false);
                  radioSongPlayer.destroy();
                }
                song = NBSDecoder.parse(songFile);
                if (song == null)
                {
                  MessageUtil.sendError(sender, "파일이 손상되어 재생할 수 없습니다.");
                  return true;
                }
                radioSongPlayer = new RadioSongPlayer(song);
                radioSongPlayer.setCategory(category);
                radioSongPlayer.setEnable10Octave(!disable10Octave);
                if (enableRepeat)
                {
                  radioSongPlayer.setRepeatMode(RepeatMode.ONE);
                }
                radioSongPlayer.setAutoDestroy(true);
                if (!silent)
                {
                  MessageUtil.sendMessage(sender, Prefix.INFO_SONG, ComponentUtil.createTranslate("%s을(를) 재생합니다.", song));
                }
                for (Player player : Bukkit.getServer().getOnlinePlayers())
                {
                  if (UserData.LISTEN_GLOBAL.getBoolean(player.getUniqueId()) || UserData.LISTEN_GLOBAL_FORCE.getBoolean(player.getUniqueId()))
                  {
                    radioSongPlayer.addPlayer(player);
                    if (!silent && !sender.equals(player))
                    {
                      MessageUtil.sendMessage(player, Prefix.INFO_SONG, ComponentUtil.createTranslate("%s이(가) %s을(를) 재생합니다.", sender, song));
                    }
                  }
                }
                radioSongPlayer.setPlaying(true);
              }
              catch (Exception e)
              {
                MessageUtil.sendError(sender, "오류 뭐");
                e.printStackTrace();
              }
              return true;
            }
            default -> {
              MessageUtil.wrongArg(sender, 1, args);
              MessageUtil.commandInfo(sender, label, "<stop|play|info|listening> ...");
              return true;
            }
          }
        }
      case "csong2":
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
          case "play" -> {
            if (args.length < 4)
            {
              MessageUtil.shortArg(sender, 4, args);
              MessageUtil.commandInfo(sender, label, player.getName() + " play <명령어 출력 숨김 여부> <노래(.nbs 확장자 제외)>");
              return true;
            }
            if (!MessageUtil.isBoolean(sender, args, 3, true))
            {
              return true;
            }
            boolean hideOutput = Boolean.parseBoolean(args[2]);
            String fileName = MessageUtil.listToString(" ", 3, args.length, args);
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
            try
            {
              File songFile = new File(Cucumbery.getPlugin().getDataFolder() + "/data/songs/" + fileName + ".nbs");
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
                MessageUtil.sendError(sender, "파일이 손상되어 재생할 수 없습니다.");
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
                MessageUtil.sendMessage(sender, Prefix.INFO_SONG, ComponentUtil.createTranslate("%s에게 %s을(를) 재생합니다.", player, playerSong));
              }
              if (UserData.LISTEN_GLOBAL.getBoolean(uuid) || UserData.LISTEN_GLOBAL_FORCE.getBoolean(uuid))
              {
                playerRadio.addPlayer(player);
                if (!hideOutput && !player.equals(sender))
                {
                  MessageUtil.sendMessage(player, Prefix.INFO_SONG, ComponentUtil.createTranslate("%s이(가) 당신에게 %s을(를) 재생합니다.", sender, playerSong));
                }
              }
              playerRadio.setPlaying(true);
              CommandSong.playerRadio.put(uuid, playerRadio);
              CommandSong.playerSong.put(uuid, playerSong);
            }
            catch (Exception e)
            {
              MessageUtil.sendError(sender, "오류 뭐");
              e.printStackTrace();
            }
          }
          case "stop" -> {
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
                MessageUtil.sendError(sender, "노래를 재생하고 있지 않습니다.");
                return true;
              }
              Song playerSong = playerRadio.getSong();
              playerRadio.setPlaying(false);
              playerRadio.destroy();
              if (!hideOutput)
              {
                MessageUtil.sendMessage(sender, Prefix.INFO_SONG, ComponentUtil.createTranslate("%s의 %s의 재생을 멈췄습니다.", player, playerSong));
              }
              if (UserData.LISTEN_GLOBAL.getBoolean(uuid) || UserData.LISTEN_GLOBAL_FORCE.getBoolean(uuid))
              {
                if (!hideOutput && !player.equals(sender))
                {
                  MessageUtil.sendMessage(player, Prefix.INFO_SONG, ComponentUtil.createTranslate("%s이(가) 당신의 %s의 재생을 멈췄습니다.", sender, playerSong));
                }
              }
              CommandSong.playerRadio.remove(uuid);
              CommandSong.playerSong.remove(uuid);
            }
            catch (Exception e)
            {
              MessageUtil.sendError(sender, "노래의 재생을 멈추는 도중에 알 수 없는 오류가 발생하였습니다.");
              e.printStackTrace();
            }
          }
          case "info" -> {
            RadioSongPlayer playerRadio = CommandSong.playerRadio.get(uuid);
            if (playerRadio == null)
            {
              MessageUtil.sendError(sender, ComponentUtil.createTranslate("%s은(는) 노래를 재생하고 있지 않습니다.", player));
              return true;
            }
            Song playerSong = playerRadio.getSong();
            MessageUtil.sendMessage(sender, Prefix.INFO_SONG, "--------------------------------------------");
            MessageUtil.sendMessage(sender, Prefix.INFO_SONG, ComponentUtil.createTranslate("파일 이름 : %s", playerSong));
            MessageUtil.sendMessage(sender, Prefix.INFO_SONG, ComponentUtil.createTranslate("길이 : %s", playerSong.getLength()));
          }
          default -> {
            MessageUtil.wrongArg(sender, 2, args);
            return true;
          }
        }
        break;
    }
    return true;
  }

  public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args)
  {
    if (!MessageUtil.checkQuoteIsValidInArgs(sender, args = MessageUtil.wrapWithQuote(true, args), true))
    {
      return Collections.singletonList(args[0]);
    }
    int length = args.length;
    String name = cmd.getName();
    if (name.equals("csong") && Method.hasPermission(sender, Permission.CMD_SONG, false))
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

    return Collections.singletonList(Prefix.ARGS_LONG.toString());
  }
}
