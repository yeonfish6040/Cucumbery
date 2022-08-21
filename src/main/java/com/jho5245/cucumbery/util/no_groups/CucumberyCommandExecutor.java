package com.jho5245.cucumbery.util.no_groups;

import com.destroystokyo.paper.event.server.AsyncTabCompleteEvent.Completion;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a {@link TabExecutor} which extends {@link AsyncTabCompleter}.
 */
public interface CucumberyCommandExecutor extends TabExecutor, AsyncTabCompleter
{
  @NotNull
  static List<String> legacy(@NotNull List<Completion> completions)
  {
    List<String> list = new ArrayList<>();
    completions.forEach(completion -> list.add(completion.suggestion()));
    return list;
  }

  @Override
  default List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args)
  {
    return legacy(completion(sender, cmd, label, args, CommandArgumentUtil.senderLocation(sender)));
  }
}
