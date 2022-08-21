package com.jho5245.cucumbery.listeners.server;

import com.jho5245.cucumbery.Cucumbery;
import com.jho5245.cucumbery.util.no_groups.MessageUtil;
import com.jho5245.cucumbery.util.no_groups.Method;
import com.jho5245.cucumbery.util.storage.component.util.ComponentUtil;
import com.jho5245.cucumbery.util.storage.no_groups.CustomConfig.UserData;
import com.jho5245.cucumbery.util.storage.component.util.sendercomponent.SenderComponentUtil;
import com.jho5245.cucumbery.util.storage.data.Constant;
import com.jho5245.cucumbery.util.storage.data.Prefix;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.data.type.CommandBlock;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.ServerCommandEvent;

import java.util.List;
import java.util.UUID;

public class ServerCommand implements Listener
{
  @EventHandler
  public void onServerCommand(ServerCommandEvent event)
  {
    String command = event.getCommand();
    // cucumberify
    if (Cucumbery.using_CommandAPI && command.startsWith("give "))
    {
      command = "cgive " + command.substring(5);
      event.setCommand(command);
    }
    // cucumberify
    if (Cucumbery.using_CommandAPI && command.startsWith("/give "))
    {
      command = "/cgive " + command.substring(6);
      event.setCommand(command);
    }
    if (command.contains("--cucumbery"))
    {
      if (event.getSender() instanceof BlockCommandSender)
      {
        List<String> blackListWorlds = Cucumbery.config.getStringList("disable-command-block-worlds");
        if (Method.configContainsLocation(((BlockCommandSender) event.getSender()).getBlock().getLocation(), blackListWorlds))
        {
          event.setCancelled(true);
          return;
        }
      }

      command = command.replaceFirst("--cucumbery", "");
      command = Method.parseCommandString(Bukkit.getServer().getConsoleSender(), command);
      event.setCommand(command);
    }

    if (!command.contains("--nocolor"))
    {
      command = MessageUtil.n2s(command);
    }
    else
    {
      command = command.replaceFirst("--nocolor", "");
    }
    event.setCommand(command);

    // cucumberify
    if (Cucumbery.using_CommandAPI && command.startsWith("/give"))
    {
      command = "/cgive" + command.substring(5);
      event.setCommand(command);
    }

    boolean debuggerExists = false;
    for (Player player : Bukkit.getOnlinePlayers())
    {
      if (UserData.SHOW_COMMAND_BLOCK_EXECUTION_LOCATION.getBoolean(player))
      {
        debuggerExists = true;
        break;
      }
    }

    if (debuggerExists)
    {
      CommandSender sender = event.getSender();
      if (event.getSender() instanceof BlockCommandSender)
      {
        Block block = ((BlockCommandSender) sender).getBlock();
        CommandBlock commandBlock = (CommandBlock) block.getBlockData();

        String extraInfo = "";
        if (commandBlock.isConditional())
        {
          extraInfo += "(조건적)";
        }

        Location loc = block.getLocation();
        World world = loc.getWorld();
        String worldDisplayName = Method.getWorldDisplayName(world);
        String worldName = world.getName();
        Component blockName = SenderComponentUtil.senderComponent(sender);
        String blockOriginName = switch (block.getType())
                {
                  case COMMAND_BLOCK -> "rgb215,180,157;&l반응형 명령 블록";
                  case REPEATING_COMMAND_BLOCK -> "rgb169,153,214;&l반복형 명령 블록";
                  case CHAIN_COMMAND_BLOCK -> "rgb168,209,191;&l연쇄형 명령 블록";
                  default -> null;
                };
        blockName = blockName.append(ComponentUtil.create("&r(" + blockOriginName + "&r)"));
        int x = loc.getBlockX(), y = loc.getBlockY(), z = loc.getBlockZ();
        String xDis = Constant.Jeongsu.format(x);
        String yDis = Constant.Jeongsu.format(y);
        String zDis = Constant.Jeongsu.format(z);
        if (command.length() > 100)
        {
          command = command.substring(0, 99) + " ...";
        }
        for (Player player : Bukkit.getServer().getOnlinePlayers())
        {
          UUID uuid = player.getUniqueId();
          if (UserData.SHOW_COMMAND_BLOCK_EXECUTION_LOCATION.getBoolean(uuid))
          {
            Component msg = ComponentUtil.create(Prefix.INFO, blockName, "&r(rg255,204;" + worldDisplayName + "&r : rg255,204;" + xDis + "&r : rg255,204;" + yDis + "&r : rg255,204;" + zDis + "&r)" + extraInfo + " 명령어 : rg255,204;" + command);
            String quickTpCommand = "/atp @s " + worldName + " " + (x + 0.5) + " " + (y + 1) + " " + (z + 0.5) + " ~ 90 false false true";
            String hoverText = "클릭하여 즉시 이동";
            MessageUtil.sendMessage(player, msg.hoverEvent(HoverEvent.showText(ComponentUtil.create(hoverText))).clickEvent(ClickEvent.runCommand(quickTpCommand)));
          }
        }
      }
    }
  }
}
