package com.jho5245.cucumbery.commands.msg;

import com.jho5245.cucumbery.util.no_groups.MessageUtil;
import com.jho5245.cucumbery.util.no_groups.Method;
import com.jho5245.cucumbery.util.no_groups.SelectorUtil;
import com.jho5245.cucumbery.util.storage.component.util.ComponentUtil;
import com.jho5245.cucumbery.util.storage.data.Permission;
import com.jho5245.cucumbery.util.storage.data.Prefix;
import io.papermc.paper.advancement.AdvancementDisplay.Frame;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.command.*;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class CommandSendToast implements CommandExecutor, TabCompleter
{
  public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args)
  {
    if (!Method.hasPermission(sender, Permission.CMD_SEND_TOAST, true))
    {
      return true;
    }
    if (!MessageUtil.checkQuoteIsValidInArgs(sender, args = MessageUtil.wrapWithQuote(args)))
    {
      return !(sender instanceof BlockCommandSender);
    }
    int length = args.length;
    if (length < 2)
    {
      MessageUtil.shortArg(sender, 2, args);
      MessageUtil.commandInfo(sender, label, Method.getUsage(cmd));
      return !(sender instanceof BlockCommandSender);
    }
    List<Player> players = SelectorUtil.getPlayers(sender, args[0], true);
    if (players == null)
    {
      return !(sender instanceof BlockCommandSender);
    }
    Component message = ComponentUtil.create(false, args[1]);
    Material type = Material.KNOWLEDGE_BOOK;
    Frame frame = Frame.GOAL;
    if (length >= 3)
    {
      try
      {
        type = Material.valueOf(args[2].toUpperCase());
      }
      catch (Exception e)
      {
        MessageUtil.wrongArg(sender, 3, args);
        return !(sender instanceof BlockCommandSender);
      }
    }
    if (length == 4)
    {
      try
      {
        frame = Frame.valueOf(args[3].toUpperCase());
      }
      catch (Exception e)
      {
        MessageUtil.wrongArg(sender, 4, args);
        return !(sender instanceof BlockCommandSender);
      }
    }

    MessageUtil.sendToast(players, message, type, frame);

    if (length > 4)
    {
      MessageUtil.longArg(sender, 4, args);
      MessageUtil.commandInfo(sender, label, Method.getUsage(cmd));
      return !(sender instanceof BlockCommandSender);
    }
    return true;
  }

  @NotNull
  public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args)
  {
    if (!MessageUtil.checkQuoteIsValidInArgs(sender, args = MessageUtil.wrapWithQuote(true, args), true))
    {
      return Collections.singletonList(args[0]);
    }
    int length = args.length;
    if (length == 1)
    {
      return Method.tabCompleterPlayer(sender, args, "<플레이어>", true);
    }
    else if (length == 2)
    {
      return Method.tabCompleterList(args, "<메시지>", true);
    }
    else if (length == 3)
    {
      List<Material> items = new ArrayList<>(List.of(Material.values()));
      items.removeIf(e -> e == Material.AIR || !e.isItem());
      List<String> list = new ArrayList<>();
      for (Material m : items)
      {
        list.add(m.toString().toLowerCase());
      }
      return Method.tabCompleterList(args, list, "[아이템]");
    }
    else if (length == 4)
    {
      return Method.tabCompleterList(args, Frame.values(), "[표시 유형]");
    }
    return Collections.singletonList(Prefix.ARGS_LONG.toString());
  }
}
