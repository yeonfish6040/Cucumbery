package com.jho5245.cucumbery.listeners;

import com.destroystokyo.paper.event.server.AsyncTabCompleteEvent;
import com.destroystokyo.paper.event.server.AsyncTabCompleteEvent.Completion;
import com.jho5245.cucumbery.Cucumbery;
import com.jho5245.cucumbery.util.no_groups.AsyncTabCompleter;
import com.jho5245.cucumbery.util.no_groups.CommandArgumentUtil;
import com.jho5245.cucumbery.util.no_groups.MessageUtil;
import com.jho5245.cucumbery.util.no_groups.Method;
import com.jho5245.cucumbery.util.storage.component.util.ComponentUtil;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.*;

public class AsyncTabComplete implements Listener
{
  private HashMap<UUID, Integer> count = new HashMap<>();

  @EventHandler
  public void onAsyncTabComplete(AsyncTabCompleteEvent event)
  {
    if (event.isCancelled())
    {
      return;
    }
    CommandSender sender = event.getSender();
    String buffer = event.getBuffer();
    Location location = event.getLocation();
    if (buffer.length() < 1)
    {
      return;
    }
    try
    {
      String commandString = buffer.substring(1).split(" ")[0];
      PluginCommand command = Bukkit.getPluginCommand(commandString);
      if (command != null && command.getPlugin() instanceof Cucumbery)
      {
        CommandExecutor executor = command.getExecutor();
        if (executor instanceof AsyncTabCompleter tabCompleter)
        {
          boolean endsWithBlank = buffer.endsWith(" ");
          String[] args = buffer.split(" ");
          if (endsWithBlank)
          {
            List<String> list = new ArrayList<>(Method.arrayToList(args));
            list.add("");
            args = Method.listToArray(list);
          }
          String[] temp = new String[args.length - 1];
          System.arraycopy(args, 1, temp, 0, args.length - 1);
          args = temp;
          if (!MessageUtil.checkQuoteIsValidInArgs(sender, args = MessageUtil.wrapWithQuote(true, args), true))
          {
            event.completions(Collections.singletonList(Completion.completion(ComponentUtil.serialize(ComponentUtil.translate(args[0])), ComponentUtil.translate(args[0]))));
            return;
          }
          if (location == null)
          {
            location = CommandArgumentUtil.senderLocation(sender);
          }
/*          if (sender instanceof Player player)
          {
            UUID uuid = player.getUniqueId();
            int count = this.count.getOrDefault(uuid, 0);
            if (CommandTabUtil.selectorErrorMessage.containsKey(uuid))
            {
              count++;
              if (count < 3)
              {
                AsyncTabCompleteEvent asyncTabCompleteEvent = new AsyncTabCompleteEvent(sender, buffer, isCommand, event.getLocation());
                Bukkit.getPluginManager().callEvent(asyncTabCompleteEvent);
              }
            }
            this.count.put(uuid, count);
          }*/
          event.completions(tabCompleter.completion(sender, command, commandString, args, location));
        }
      }
    }
    catch (IndexOutOfBoundsException ignored)
    {

    }
  }
}
