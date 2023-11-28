package com.jho5245.cucumbery.listeners.addon.noteblockapi;

import com.jho5245.cucumbery.Cucumbery;
import com.jho5245.cucumbery.commands.sound.CommandSong;
import com.jho5245.cucumbery.util.addons.Songs;
import com.jho5245.cucumbery.util.no_groups.MessageUtil;
import com.jho5245.cucumbery.util.no_groups.Scheduler;
import com.jho5245.cucumbery.util.storage.data.Prefix;
import com.jho5245.cucumbery.util.storage.data.Variable;
import com.jho5245.cucumbery.util.storage.no_groups.CustomConfig.UserData;
import com.xxmicloxx.NoteBlockAPI.event.SongEndEvent;
import com.xxmicloxx.NoteBlockAPI.model.SoundCategory;
import com.xxmicloxx.NoteBlockAPI.songplayer.RadioSongPlayer;
import com.xxmicloxx.NoteBlockAPI.utils.NBSDecoder;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class SongEnd implements Listener
{
  @EventHandler
  public void onSongEnd(SongEndEvent event)
  {
    if (CommandSong.autoNext)
    {
      Bukkit.getScheduler().runTaskLaterAsynchronously(Cucumbery.getPlugin(), () ->
      {
        List<String> songs = new ArrayList<>(Variable.songFiles);
        if (songs.isEmpty())
        {
          return;
        }
        CommandSong.isDownloading.add("foo");
        String songName = songs.get((int) (Math.random() * songs.size())) + ".nbs";
        File songFile = new File(Cucumbery.getPlugin().getDataFolder() + "/data/songs/" + songName);
        if (!songFile.exists())
        {
          if (songs.contains(songName.substring(0, songName.length() - 4)))
          {
            try
            {
              songFile = Songs.download(songName, false);
            }
            catch (Exception e)
            {
              CommandSong.isDownloading.clear();
              stop();
              return;
            }
          }
          else
          {
            CommandSong.isDownloading.clear();
            stop();
            return;
          }
        }
        CommandSong.song = NBSDecoder.parse(songFile);
        if (CommandSong.song == null)
        {
          CommandSong.isDownloading.clear();
          stop();
          return;
        }
        CommandSong.song = NBSDecoder.parse(songFile);
        if (CommandSong.song == null)
        {
          CommandSong.isDownloading.clear();
          return;
        }
        SoundCategory soundCategory = CommandSong.radioSongPlayer != null ? CommandSong.radioSongPlayer.getCategory() : SoundCategory.RECORDS;
        boolean disable10Octave = CommandSong.radioSongPlayer != null && !CommandSong.radioSongPlayer.isEnable10Octave();
        if (CommandSong.radioSongPlayer != null)
        {
          CommandSong.radioSongPlayer.setPlaying(false);
          CommandSong.radioSongPlayer.destroy();
        }
        CommandSong.radioSongPlayer = new RadioSongPlayer(CommandSong.song);
        CommandSong.radioSongPlayer.setCategory(soundCategory);
        CommandSong.radioSongPlayer.setEnable10Octave(!disable10Octave);
        CommandSong.isDownloading.clear();
        Scheduler.fileNameLength = songName.length() - 1;
        if (Scheduler.delayTask != null)
        {
          Scheduler.delayTask.cancel();
        }
        Scheduler.delay = false;
        for (Player player : Bukkit.getServer().getOnlinePlayers())
        {
          if (UserData.LISTEN_GLOBAL.getBoolean(player.getUniqueId()) || UserData.LISTEN_GLOBAL_FORCE.getBoolean(player.getUniqueId()))
          {
            CommandSong.radioSongPlayer.addPlayer(player);
            MessageUtil.sendMessage(player, Prefix.INFO_SONG, "노래 자동 재생 - %s", CommandSong.song);
          }
        }
        CommandSong.radioSongPlayer.setPlaying(true);
      }, 0L);
    }
    else
    {
      stop();
    }
  }

  private void stop()
  {
    if (CommandSong.radioSongPlayer != null)
    {
      CommandSong.radioSongPlayer.setPlaying(false);
      CommandSong.radioSongPlayer.destroy();
      CommandSong.radioSongPlayer = null;
    }
    CommandSong.song = null;
  }
}