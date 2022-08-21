package com.jho5245.cucumbery.commands.msg;

import com.destroystokyo.paper.event.server.AsyncTabCompleteEvent.Completion;
import com.jho5245.cucumbery.util.no_groups.*;
import com.jho5245.cucumbery.util.storage.component.util.ComponentUtil;
import com.jho5245.cucumbery.util.storage.data.Permission;
import com.jho5245.cucumbery.util.storage.no_groups.ItemStackUtil;
import io.papermc.paper.advancement.AdvancementDisplay.Frame;
import net.kyori.adventure.text.Component;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class CommandSendToast implements CucumberyCommandExecutor
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
    ItemStack itemStack = new ItemStack(Material.KNOWLEDGE_BOOK);
    Frame frame = Frame.GOAL;
    if (length >= 3)
    {
      itemStack = ItemStackUtil.createItemStack(sender, args[2]);
      if (itemStack == null)
      {
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

    MessageUtil.sendToast(players, message, itemStack, frame);

    if (length > 4)
    {
      MessageUtil.longArg(sender, 4, args);
      MessageUtil.commandInfo(sender, label, Method.getUsage(cmd));
      return !(sender instanceof BlockCommandSender);
    }
    return true;
  }

  @Override
  public @NotNull List<Completion> completion(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args, @NotNull Location location)
  {
    int length = args.length;
    if (length == 1)
    {
      return CommandTabUtil.tabCompleterPlayer(sender, args, "<플레이어>");
    }
    else if (length == 2)
    {
      return CommandTabUtil.tabCompleterList(args, "<메시지>", true);
    }
    else if (length == 3)
    {
      return CommandTabUtil.itemStackArgument(sender, args, "[아이템]");
    }
    else if (length == 4)
    {
      return CommandTabUtil.tabCompleterList(args, Frame.values(), "[표시 유형]");
    }
    return CommandTabUtil.ARGS_LONG;
  }
}
