package com.jho5245.cucumbery.commands.brigadier;

import com.jho5245.cucumbery.commands.brigadier.base.ArgumentUtil;
import com.jho5245.cucumbery.commands.brigadier.base.CommandBase;
import com.jho5245.cucumbery.util.additemmanager.AddItemUtil;
import com.jho5245.cucumbery.util.itemlore.ItemLore;
import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.Argument;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static com.jho5245.cucumbery.commands.brigadier.base.ArgumentUtil.*;

public class Give extends CommandBase
{
  private final List<Argument> list1 = Arrays.asList(MANY_PLAYERS, ITEMSTACK);

  private final List<Argument> list2 = Arrays.asList(MANY_PLAYERS, ITEMSTACK, ArgumentUtil.of(ArgumentType.INTEGER, "개수", 1, 2304));

  private final List<Argument> list3 = Arrays.asList(MANY_PLAYERS, ITEMSTACK, ArgumentUtil.of(ArgumentType.LITERAL, "-1"));

  private final List<Argument> list4 = Arrays.asList(MANY_PLAYERS, ITEMSTACK, HIDE_OUTPUT);

  private final List<Argument> list5 = Arrays.asList(MANY_PLAYERS, ITEMSTACK, ArgumentUtil.of(ArgumentType.INTEGER, "개수", 1, 2304), HIDE_OUTPUT);

  private final List<Argument> list6 = Arrays.asList(MANY_PLAYERS, ITEMSTACK, ArgumentUtil.of(ArgumentType.LITERAL, "-1"), HIDE_OUTPUT);

  @SuppressWarnings("unchecked")
  public void registerCommand(String command, String permission, String... aliases)
  {
    CommandAPICommand commandAPICommand = getCommandBase(command, permission, aliases);
    commandAPICommand = commandAPICommand.withArguments(list1);
    commandAPICommand = commandAPICommand.executesNative((sender, args) ->
    {
      Collection<Player> players = (Collection<Player>) args[0];
      ItemStack itemStack = (ItemStack) args[1];
      ItemLore.setItemLore(itemStack);
      int amount = itemStack.getAmount();
      AddItemUtil.addItemResult2(sender.getCallee(), players, itemStack, amount).sendFeedback(false);
    });
    commandAPICommand.register();

    commandAPICommand = getCommandBase(command, permission, aliases);
    commandAPICommand = commandAPICommand.withArguments(list2);
    commandAPICommand = commandAPICommand.executesNative((sender, args) ->
    {
      Collection<Player> players = (Collection<Player>) args[0];
      ItemStack itemStack = (ItemStack) args[1];
      ItemLore.setItemLore(itemStack);
      int amount = (int) args[2];
      itemStack.setAmount(amount);
      AddItemUtil.addItemResult2(sender.getCallee(), players, itemStack, amount).sendFeedback(false);
    });
    commandAPICommand.register();

    commandAPICommand = getCommandBase(command, permission, aliases);
    commandAPICommand = commandAPICommand.withArguments(list3);
    commandAPICommand = commandAPICommand.executesNative((sender, args) ->
    {
      Collection<Player> players = (Collection<Player>) args[0];
      ItemStack itemStack = (ItemStack) args[1];
      ItemLore.setItemLore(itemStack);
      int amount = itemStack.getType().getMaxStackSize();
      itemStack.setAmount(amount);
      AddItemUtil.addItemResult2(sender.getCallee(), players, itemStack, amount).sendFeedback(false);
    });
    commandAPICommand.register();

    commandAPICommand = getCommandBase(command, permission, aliases);
    commandAPICommand = commandAPICommand.withArguments(list4);
    commandAPICommand = commandAPICommand.executesNative((sender, args) ->
    {
      Collection<Player> players = (Collection<Player>) args[0];
      ItemStack itemStack = (ItemStack) args[1];
      ItemLore.setItemLore(itemStack);
      int amount = itemStack.getAmount();
      boolean hideOutput = (boolean) args[2];
      itemStack.setAmount(amount);
      AddItemUtil.addItemResult2(sender.getCallee(), players, itemStack, amount).sendFeedback(hideOutput);
    });
    commandAPICommand.register();

    commandAPICommand = getCommandBase(command, permission, aliases);
    commandAPICommand = commandAPICommand.withArguments(list5);
    commandAPICommand = commandAPICommand.executesNative((sender, args) ->
    {
      Collection<Player> players = (Collection<Player>) args[0];
      ItemStack itemStack = (ItemStack) args[1];
      ItemLore.setItemLore(itemStack);
      int amount = (int) args[2];
      boolean hideOutput = (boolean) args[3];
      itemStack.setAmount(amount);
      AddItemUtil.addItemResult2(sender.getCallee(), players, itemStack, amount).sendFeedback(hideOutput);
    });
    commandAPICommand.register();

    commandAPICommand = getCommandBase(command, permission, aliases);
    commandAPICommand = commandAPICommand.withArguments(list6);
    commandAPICommand = commandAPICommand.executesNative((sender, args) ->
    {
      Collection<Player> players = (Collection<Player>) args[0];
      ItemStack itemStack = (ItemStack) args[1];
      ItemLore.setItemLore(itemStack);
      int amount = itemStack.getType().getMaxStackSize();
      boolean hideOutput = (boolean) args[3];
      itemStack.setAmount(amount);
      AddItemUtil.addItemResult2(sender.getCallee(), players, itemStack, amount).sendFeedback(hideOutput);
    });
    commandAPICommand.register();
  }
}
