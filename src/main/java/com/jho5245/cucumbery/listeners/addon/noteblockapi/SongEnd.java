package com.jho5245.cucumbery.listeners.addon.noteblockapi;

import com.jho5245.cucumbery.commands.sound.CommandSong;
import com.xxmicloxx.NoteBlockAPI.event.SongEndEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class SongEnd implements Listener
{
  @EventHandler
  public void onSongEnd(SongEndEvent event)
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