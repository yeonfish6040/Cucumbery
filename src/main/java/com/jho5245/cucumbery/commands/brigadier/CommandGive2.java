package com.jho5245.cucumbery.commands.brigadier;

import com.jho5245.cucumbery.commands.brigadier.base.ArgumentUtil;
import com.jho5245.cucumbery.commands.brigadier.base.CommandBase;
import com.jho5245.cucumbery.util.no_groups.MessageUtil;
import com.jho5245.cucumbery.util.additemmanager.AddItemUtil;
import com.jho5245.cucumbery.util.itemlore.ItemLore;
import dev.jorel.commandapi.CommandAPI;
import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.Argument;
import dev.jorel.commandapi.exceptions.WrapperCommandSyntaxException;
import dev.jorel.commandapi.wrappers.NativeProxyCommandSender;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static com.jho5245.cucumbery.commands.brigadier.base.ArgumentUtil.*;

public class CommandGive2 extends CommandBase
{
  private final List<Argument<?>> list1 = Arrays.asList(MANY_PLAYERS, ITEMSTACK);

  private final List<Argument<?>> list2 = Arrays.asList(MANY_PLAYERS, ITEMSTACK, HIDE_OUTPUT);

  private final List<Argument<?>> list3 = Arrays.asList(MANY_PLAYERS, ITEMSTACK, ArgumentUtil.of(ArgumentType.INTEGER, "개수", 1, 2304));

  private final List<Argument<?>> list4 = Arrays.asList(MANY_PLAYERS, ITEMSTACK, ArgumentUtil.of(ArgumentType.INTEGER, "개수", 1, 2304), HIDE_OUTPUT);

  private final List<Argument<?>> list5 = Arrays.asList(MANY_PLAYERS, ITEMSTACK, ArgumentUtil.of(ArgumentType.LITERAL, "max-stack"));

  private final List<Argument<?>> list6 = Arrays.asList(MANY_PLAYERS, ITEMSTACK, ArgumentUtil.of(ArgumentType.LITERAL, "max-stack"), HIDE_OUTPUT);

  private final List<Argument<?>> list7 = Collections.singletonList(ITEMSTACK);

  private final List<Argument<?>> list8 = Arrays.asList(ITEMSTACK, HIDE_OUTPUT);

  private final List<Argument<?>> list9 = Arrays.asList(ITEMSTACK, ArgumentUtil.of(ArgumentType.INTEGER, "개수", 1, 2304));

  private final List<Argument<?>> list10 = Arrays.asList(ITEMSTACK, ArgumentUtil.of(ArgumentType.INTEGER, "개수", 1, 2304), HIDE_OUTPUT);

  private final List<Argument<?>> list11 = Arrays.asList(ITEMSTACK, ArgumentUtil.of(ArgumentType.LITERAL, "max-stack"));

  private final List<Argument<?>> list12 = Arrays.asList(ITEMSTACK, ArgumentUtil.of(ArgumentType.LITERAL, "max-stack"), HIDE_OUTPUT);

  private void give(@NotNull NativeProxyCommandSender sender, @NotNull Collection<Player> players, @NotNull ItemStack itemStack, int amount, boolean hideOutput) throws WrapperCommandSyntaxException
  {
    CommandSender commandSender = sender.getCallee();
    if (players.isEmpty())
    {
      if (commandSender instanceof BlockCommandSender)
      {
        CommandAPI.fail("개체를 찾을 수 없습니다");
      }
      else
      {
        MessageUtil.sendError(commandSender, "개체를 찾을 수 없습니다");
      }
      return;
    }
    ItemLore.setItemLore(itemStack);
    itemStack.setAmount(amount);
    AddItemUtil.addItemResult2(sender.getCallee(), players, itemStack, amount).sendFeedback(hideOutput);
  }

  @SuppressWarnings("unchecked")
  public void registerCommand(String command, String permission, String... aliases)
  {
    CommandAPICommand commandAPICommand;
    {
      commandAPICommand = getCommandBase(command, permission, aliases);
      commandAPICommand = commandAPICommand.withArguments(list7);
      commandAPICommand = commandAPICommand.executesNative((sender, args) ->
      {
        if (sender.getCallee() instanceof Player player)
        {
          give(sender, Collections.singleton(player), (ItemStack) args[0], 1, false);
        }
        else
        {
          CommandAPI.fail("플레이어만 이 명령어를 사용할 수 있습니다");
        }
      });
      commandAPICommand.register();


      commandAPICommand = getCommandBase(command, permission, aliases);
      commandAPICommand = commandAPICommand.withArguments(list8);
      commandAPICommand = commandAPICommand.executesNative((sender, args) ->
      {
        give(sender, (Collection<Player>) args[0], (ItemStack) args[1], 1, (boolean) args[2]);
        if (sender.getCallee() instanceof Player player)
        {
          give(sender, Collections.singleton(player), (ItemStack) args[0], 1, (Boolean) args[1]);
        }
        else
        {
          CommandAPI.fail("플레이어만 이 명령어를 사용할 수 있습니다");
        }
      });
      commandAPICommand.register();


      commandAPICommand = getCommandBase(command, permission, aliases);
      commandAPICommand = commandAPICommand.withArguments(list9);
      commandAPICommand = commandAPICommand.executesNative((sender, args) ->
      {
        if (sender.getCallee() instanceof Player player)
        {
          give(sender, Collections.singleton(player), (ItemStack) args[0], (Integer) args[1], false);
        }
        else
        {
          CommandAPI.fail("플레이어만 이 명령어를 사용할 수 있습니다");
        }
      });
      commandAPICommand.register();


      commandAPICommand = getCommandBase(command, permission, aliases);
      commandAPICommand = commandAPICommand.withArguments(list10);
      commandAPICommand = commandAPICommand.executesNative((sender, args) ->
      {
        if (sender.getCallee() instanceof Player player)
        {
          give(sender, Collections.singleton(player), (ItemStack) args[0], (Integer) args[1], (Boolean) args[2]);
        }
        else
        {
          CommandAPI.fail("플레이어만 이 명령어를 사용할 수 있습니다");
        }
      });
      commandAPICommand.register();


      commandAPICommand = getCommandBase(command, permission, aliases);
      commandAPICommand = commandAPICommand.withArguments(list11);
      commandAPICommand = commandAPICommand.executesNative((sender, args) ->
      {
        ItemStack itemStack = (ItemStack) args[0];
        if (sender.getCallee() instanceof Player player)
        {
          give(sender, Collections.singleton(player), itemStack, itemStack.getType().getMaxStackSize(), false);
        }
        else
        {
          CommandAPI.fail("플레이어만 이 명령어를 사용할 수 있습니다");
        }
      });
      commandAPICommand.register();

      commandAPICommand = getCommandBase(command, permission, aliases);
      commandAPICommand = commandAPICommand.withArguments(list12);
      commandAPICommand = commandAPICommand.executesNative((sender, args) ->
      {
        ItemStack itemStack = (ItemStack) args[0];
        if (sender.getCallee() instanceof Player player)
        {
          give(sender, Collections.singleton(player), itemStack, itemStack.getType().getMaxStackSize(), (Boolean) args[2]);
        }
        else
        {
          CommandAPI.fail("플레이어만 이 명령어를 사용할 수 있습니다");
        }
      });
      commandAPICommand.register();
    }

    {
      commandAPICommand = getCommandBase(command, permission, aliases);
      commandAPICommand = commandAPICommand.withArguments(list1);
      commandAPICommand = commandAPICommand.executesNative((sender, args) ->
      {
        give(sender, (Collection<Player>) args[0], (ItemStack) args[1], 1, false);
      });
      commandAPICommand.register();


      commandAPICommand = getCommandBase(command, permission, aliases);
      commandAPICommand = commandAPICommand.withArguments(list2);
      commandAPICommand = commandAPICommand.executesNative((sender, args) ->
      {
        give(sender, (Collection<Player>) args[0], (ItemStack) args[1], 1, (boolean) args[2]);
      });
      commandAPICommand.register();


      commandAPICommand = getCommandBase(command, permission, aliases);
      commandAPICommand = commandAPICommand.withArguments(list3);
      commandAPICommand = commandAPICommand.executesNative((sender, args) ->
      {
        give(sender, (Collection<Player>) args[0], (ItemStack) args[1], (int) args[2], false);
      });
      commandAPICommand.register();


      commandAPICommand = getCommandBase(command, permission, aliases);
      commandAPICommand = commandAPICommand.withArguments(list4);
      commandAPICommand = commandAPICommand.executesNative((sender, args) ->
      {
        give(sender, (Collection<Player>) args[0], (ItemStack) args[1], (int) args[2], (boolean) args[3]);
      });
      commandAPICommand.register();


      commandAPICommand = getCommandBase(command, permission, aliases);
      commandAPICommand = commandAPICommand.withArguments(list5);
      commandAPICommand = commandAPICommand.executesNative((sender, args) ->
      {
        ItemStack itemStack = (ItemStack) args[1];
        give(sender, (Collection<Player>) args[0], itemStack, itemStack.getType().getMaxStackSize(), false);
      });
      commandAPICommand.register();

      commandAPICommand = getCommandBase(command, permission, aliases);
      commandAPICommand = commandAPICommand.withArguments(list6);
      commandAPICommand = commandAPICommand.executesNative((sender, args) ->
      {
        ItemStack itemStack = (ItemStack) args[1];
        give(sender, (Collection<Player>) args[0], itemStack, itemStack.getType().getMaxStackSize(), (boolean) args[3]);
      });
      commandAPICommand.register();
    }
  }
}
