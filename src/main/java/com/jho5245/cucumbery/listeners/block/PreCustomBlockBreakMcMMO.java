package com.jho5245.cucumbery.listeners.block;

import com.gmail.nossr50.datatypes.skills.PrimarySkillType;
import com.gmail.nossr50.events.skills.abilities.McMMOPlayerAbilityActivateEvent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class PreCustomBlockBreakMcMMO
{
  protected static void skill(Player player)
  {
    // TODO: Skill doesn't work 2023.05.14
    if (true)
      return;
    McMMOPlayerAbilityActivateEvent event = new McMMOPlayerAbilityActivateEvent(player, PrimarySkillType.MINING);
    Bukkit.getPluginManager().callEvent(event);
  }
}
