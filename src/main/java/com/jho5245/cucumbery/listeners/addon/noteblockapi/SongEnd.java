package com.jho5245.cucumbery.listeners.addon.noteblockapi;

import com.jho5245.cucumbery.commands.sound.CommandSong;
import com.jho5245.cucumbery.util.MessageUtil;
import com.xxmicloxx.NoteBlockAPI.event.SongEndEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class SongEnd implements Listener
{
  @EventHandler
  public void onSongEnd(SongEndEvent event)
  {
    MessageUtil.broadcastDebug("노래 끝남 : ", event.getSongPlayer().getSong());
    if (CommandSong.radioSongPlayer != null)
    {
      CommandSong.radioSongPlayer.setPlaying(false);
      CommandSong.radioSongPlayer.destroy();
      CommandSong.radioSongPlayer = null;
    }
    CommandSong.song = null;
  }
}
