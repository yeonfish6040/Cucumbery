package com.jho5245.cucumbery.commands.brigadier;

import com.jho5245.cucumbery.commands.brigadier.base.CommandBase;
import com.jho5245.cucumbery.util.no_groups.MessageUtil;
import com.jho5245.cucumbery.util.storage.no_groups.ItemStackUtil;
import com.jho5245.cucumbery.util.storage.component.util.ComponentUtil;
import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.Argument;
import dev.jorel.commandapi.arguments.IntegerArgument;
import dev.jorel.commandapi.arguments.ItemStackPredicateArgument;
import dev.jorel.commandapi.wrappers.NativeProxyCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Predicate;

import static com.jho5245.cucumbery.commands.brigadier.base.ArgumentUtil.MANY_PLAYERS;

public class CommandClear2 extends CommandBase
{
  private final Argument ITEMSTACK = new ItemStackPredicateArgument("items no to remove");

  private final Argument MAX_AMOUNT = new IntegerArgument("max amount", 1, 2304);

  private final List<Argument<?>> list1 = List.of(MANY_PLAYERS, ITEMSTACK);

  private final List<Argument<?>> list2 = List.of(MANY_PLAYERS, ITEMSTACK, MAX_AMOUNT);

  private void clear(@NotNull NativeProxyCommandSender sender, @NotNull Collection<Player> players, @NotNull Predicate<ItemStack> predicate, int max)
  {
    int total = 0;
    for (Player player : players)
    {
      int current = max;
      for (ItemStack itemStack : player.getInventory())
      {
        if (current == 0)
          break;
        if (!predicate.test(itemStack))
        {
          if (!ItemStackUtil.itemExists(itemStack))
            continue;
          if (itemStack.getAmount() > current)
          {
            itemStack = itemStack.clone();
            itemStack.setAmount(current);
          }
          current -= itemStack.getAmount();
          total += itemStack.getAmount();
          player.getInventory().removeItem(itemStack);
        }
      }
    }
    if (total == 0)
    {
      MessageUtil.sendError(sender, ComponentUtil.translate("%s에게 제거할 수 있는 아이템이 없습니다", players));
    }
    else
    {
      MessageUtil.info(sender, ComponentUtil.translate("commands.clear.success.single", total, players));
      MessageUtil.sendAdminMessage(sender, new ArrayList<>(players), "commands.clear.success.single", total, players);
    }
  }

  @SuppressWarnings("unchecked")
  public void registerCommand(String command, String permission, String... aliases)
  {
    CommandAPICommand commandAPICommand = getCommandBase(command, permission, aliases);
    commandAPICommand = commandAPICommand.withArguments(list1);
    commandAPICommand = commandAPICommand.executesNative((sender, args) ->
    {
      clear(sender, (Collection<Player>) args.get(0), (Predicate<ItemStack>) args.get(1), 2304);
    });
    commandAPICommand.register();
    commandAPICommand = getCommandBase(command, permission, aliases);
    commandAPICommand = commandAPICommand.withArguments(list2);
    commandAPICommand = commandAPICommand.executesNative((sender, args) ->
    {
      clear(sender, (Collection<Player>) args.get(0), (Predicate<ItemStack>) args.get(1), (Integer) args.get(2));
    });
    commandAPICommand.register();
  }
}
