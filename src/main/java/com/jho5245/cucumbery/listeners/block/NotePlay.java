package com.jho5245.cucumbery.listeners.block;

import com.jho5245.cucumbery.Cucumbery;
import com.jho5245.cucumbery.util.no_groups.Method;
import com.jho5245.cucumbery.util.no_groups.PlaceHolderUtil;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.data.type.NoteBlock;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.NotePlayEvent;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NotePlay implements Listener
{
  public static boolean customNoteBlockSound(@NotNull Block block)
  {
    Location location = block.getLocation();
    if (Cucumbery.config.getBoolean("custom-note-block-sound.enabled"))
    {
      Block belowBlock = location.add(0, -1, 0).getBlock();
      String newSoundString = Cucumbery.config.getString("custom-note-block-sound.sounds." + belowBlock.getType());
      if (newSoundString != null)
      {
        if (newSoundString.startsWith("placeholder:"))
        {
          newSoundString = newSoundString.substring("placeholder:".length());
          newSoundString = PlaceHolderUtil.placeholder(Bukkit.getConsoleSender(), newSoundString, null);
        }
        boolean vaillnaSound = false;
        Sound newSound = null;
        try
        {
          newSound = Sound.valueOf(newSoundString);
          vaillnaSound = true;
        }
        catch (Exception ignored)
        {
        }
        NoteBlock noteBlock = (NoteBlock) block.getBlockData();
        Note note = noteBlock.getNote();
        int pitchNum = 0;
        String bds = noteBlock.getAsString();
        String pattern = "note=([0-9]{1,2})";
        Pattern r = Pattern.compile(pattern);
        Matcher m = r.matcher(bds);
        if (m.find())
        {
          pitchNum = Integer.parseInt(m.group(1));
        }
        double colorNum = 0.041667 * pitchNum;
        block.getWorld().spawnParticle(Particle.NOTE, location.add(0.5, 2.25, 0.5), 0, colorNum, 0, 0, 1);
        Collection<Player> players = location.getWorld().getPlayers();
        for (Player player : players)
        {
          try
          {
            if (vaillnaSound)
            {
              player.playSound(location, newSound, SoundCategory.RECORDS, 3F, Method.getPitchFromNote(note));
            }
            else
            {
              player.playSound(location, newSoundString, SoundCategory.RECORDS, 3F, Method.getPitchFromNote(note));
            }
          }
          catch (Exception e)
          {
            break;
          }

        }
        return true;
      }
    }
    return false;
  }

  @EventHandler
  public void onNotePlay(NotePlayEvent event)
  {
    if (event.isCancelled())
    {
      return;
    }
    Block block = event.getBlock();
    if (customNoteBlockSound(block))
    {
      event.setCancelled(true);
    }
  }
}
