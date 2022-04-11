package com.jho5245.cucumbery.util.no_groups;

import com.destroystokyo.paper.event.server.AsyncTabCompleteEvent.Completion;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public interface AsyncTabCompleter
{
  /**
   * Requests a list of possible completions for a command argument.
   *
   * @param sender Source of the command.  For players tab-completing a
   *     command inside a command block, this will be the player, not
   *     the command block.
   * @param label Alias of the command which was used
   * @param args The arguments passed to the command, including final
   *     partial argument to be completed
   * @param location The location of this command was executed.
   * @return A List of possible completions for the final argument, or an empty list.
   */
  @NotNull
  List<Completion> completion(@NotNull CommandSender sender, @NotNull String label, @NotNull String[] args, @NotNull Location location);
}
