package com.jho5245.cucumbery.util.no_groups;

import com.destroystokyo.paper.event.server.AsyncTabCompleteEvent.Completion;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public interface AsyncTabCompleter
{

  @NotNull
  List<Completion> completion(@NotNull CommandSender sender, @NotNull String label, @NotNull String[] args, @NotNull Location location);
}
