package com.jho5245.cucumbery.listeners;

import com.destroystokyo.paper.event.server.AsyncTabCompleteEvent;
import com.destroystokyo.paper.event.server.AsyncTabCompleteEvent.Completion;
import com.jho5245.cucumbery.Cucumbery;
import com.jho5245.cucumbery.util.no_groups.AsyncTabCompleter;
import com.jho5245.cucumbery.util.no_groups.MessageUtil;
import com.jho5245.cucumbery.util.no_groups.Method;
import com.jho5245.cucumbery.util.storage.component.util.ComponentUtil;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AsyncTabComplete implements Listener
{
  @EventHandler
  public void onAsyncTabComplete(AsyncTabCompleteEvent event)
  {
    if (event.isCancelled())
    {
      return;
    }
    CommandSender sender = event.getSender();
    String buffer = event.getBuffer();
    List<Completion> completions = new ArrayList<>(event.completions());
    Location location = event.getLocation();
    String commandString = buffer.substring(1).split(" ")[0];
    PluginCommand command = Bukkit.getPluginCommand(commandString);
    if (command != null && command.getPlugin() instanceof Cucumbery && completions.isEmpty())
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
      if (!MessageUtil.checkQuoteIsValidInArgs2(sender, args = MessageUtil.wrapWithQuote2(true, args)))
      {
        event.completions(Collections.singletonList(Completion.completion(ComponentUtil.serialize(Component.translatable(args[0])), Component.translatable(args[0]))));
        return;
      }
      if (location == null)
      {
        if (sender instanceof Entity entity)
        {
          location = entity.getLocation();
        }
        else if (sender instanceof BlockCommandSender blockCommandSender)
        {
          location = blockCommandSender.getBlock().getLocation();
        }
        else
        {
          location = new Location(Bukkit.getWorlds().get(0), 0, 0, 0);
        }
      }
      //MessageUtil.info(sender, System.currentTimeMillis() + " command : %s, args : %s", commandString, Arrays.toString(args));
      CommandExecutor executor = command.getExecutor();
      if (executor instanceof AsyncTabCompleter tabCompleter)
      {
        event.completions(tabCompleter.completion(sender, commandString, args, location));
      }
    }
  }
}
