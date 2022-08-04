package com.jho5245.cucumbery.commands.brigadier;

import com.jho5245.cucumbery.commands.brigadier.base.ArgumentUtil;
import com.jho5245.cucumbery.commands.brigadier.base.CommandBase;
import com.jho5245.cucumbery.util.additemmanager.AddItemUtil;
import com.jho5245.cucumbery.util.itemlore.ItemLore;
import com.jho5245.cucumbery.util.no_groups.MessageUtil;
import com.jho5245.cucumbery.util.no_groups.Method;
import com.jho5245.cucumbery.util.storage.data.CustomMaterial;
import com.jho5245.cucumbery.util.storage.data.Variable;
import de.tr7zw.changeme.nbtapi.NBTItem;
import dev.jorel.commandapi.CommandAPI;
import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.Argument;
import dev.jorel.commandapi.arguments.MultiLiteralArgument;
import dev.jorel.commandapi.wrappers.NativeProxyCommandSender;
import org.bukkit.Material;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
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
  private final MultiLiteralArgument CUSTOM_ITEM = new MultiLiteralArgument(Method.listToArray(list));

  private static final List<String> list = Method.enumToList(CustomMaterial.values());

  static
  {
    ConfigurationSection root = Variable.customItemsConfig.getConfigurationSection("");
    if (root != null)
    {
      list.addAll(root.getKeys(false));
    }
  }

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

  private final Argument<?>[] list13 = new Argument[] {MANY_PLAYERS, CUSTOM_ITEM};

  private final Argument<?>[] list14 = new Argument[] {MANY_PLAYERS, CUSTOM_ITEM, HIDE_OUTPUT};

  private final Argument<?>[] list15 = new Argument[] {MANY_PLAYERS, CUSTOM_ITEM, ArgumentUtil.of(ArgumentType.INTEGER, "개수", 1, 2304)};

  private final Argument<?>[] list16 = new Argument[] {MANY_PLAYERS, CUSTOM_ITEM, ArgumentUtil.of(ArgumentType.INTEGER, "개수", 1, 2304), HIDE_OUTPUT};

  private final Argument<?>[] list17 = new Argument[] {MANY_PLAYERS, CUSTOM_ITEM, ArgumentUtil.of(ArgumentType.LITERAL, "max-stack")};

  private final Argument<?>[] list18 = new Argument[] {MANY_PLAYERS, CUSTOM_ITEM, ArgumentUtil.of(ArgumentType.LITERAL, "max-stack"), HIDE_OUTPUT};

  private final Argument<?>[] list19 = new Argument[] {CUSTOM_ITEM};

  private final Argument<?>[] list20 = new Argument[] {CUSTOM_ITEM, HIDE_OUTPUT};

  private final Argument<?>[] list21 = new Argument[] {CUSTOM_ITEM, ArgumentUtil.of(ArgumentType.INTEGER, "개수", 1, 2304)};

  private final Argument<?>[] list22 = new Argument[] {CUSTOM_ITEM, ArgumentUtil.of(ArgumentType.INTEGER, "개수", 1, 2304), HIDE_OUTPUT};

  private final Argument<?>[] list23 = new Argument[] {CUSTOM_ITEM, ArgumentUtil.of(ArgumentType.LITERAL, "max-stack")};

  private final Argument<?>[] list24 = new Argument[] {CUSTOM_ITEM, ArgumentUtil.of(ArgumentType.LITERAL, "max-stack"), HIDE_OUTPUT};

  private ItemStack convert(String customType)
  {
    try
    {
      CustomMaterial customMaterial = CustomMaterial.valueOf(customType.toUpperCase());
      ItemStack itemStack = new ItemStack(customMaterial.getDisplayMaterial());
      NBTItem nbtItem = new NBTItem(itemStack, true);
      nbtItem.setString("id", customMaterial.toString().toLowerCase());
      return ItemLore.setItemLore(itemStack);
    }
    catch (IllegalArgumentException e)
    {
      ItemStack itemStack = new ItemStack(Material.STONE);
      NBTItem nbtItem = new NBTItem(itemStack, true);
      nbtItem.setString("id", customType);
      return ItemLore.setItemLore(itemStack);
    }
    catch (Exception e)
    {
      return null;
    }
  }

  private void give(@NotNull NativeProxyCommandSender sender, @NotNull Collection<Player> players, @NotNull ItemStack itemStack, int amount, boolean hideOutput)
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
    itemStack.setAmount(amount);
    AddItemUtil.addItemResult2(sender.getCallee(), players, itemStack, amount).stash().sendFeedback(hideOutput);
  }

  @SuppressWarnings("unchecked")
  public void registerCommand(String command, String permission, String... aliases)
  {
    CommandAPICommand commandAPICommand;
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
      commandAPICommand = commandAPICommand.withArguments(list13);
      commandAPICommand = commandAPICommand.executesNative((sender, args) ->
      {
        ItemStack itemStack = convert((String) args[1]);
        if (itemStack == null)
        {
          return;
        }
        give(sender, (Collection<Player>) args[0], itemStack, 1, false);
      });
      commandAPICommand.register();


      commandAPICommand = getCommandBase(command, permission, aliases);
      commandAPICommand = commandAPICommand.withArguments(list14);
      commandAPICommand = commandAPICommand.executesNative((sender, args) ->
      {
        ItemStack itemStack = convert((String) args[1]);
        if (itemStack == null)
        {
          return;
        }
        give(sender, (Collection<Player>) args[0], itemStack, 1, (boolean) args[2]);
      });
      commandAPICommand.register();


      commandAPICommand = getCommandBase(command, permission, aliases);
      commandAPICommand = commandAPICommand.withArguments(list15);
      commandAPICommand = commandAPICommand.executesNative((sender, args) ->
      {
        ItemStack itemStack = convert((String) args[1]);
        if (itemStack == null)
        {
          return;
        }
        give(sender, (Collection<Player>) args[0], itemStack, (int) args[2], false);
      });
      commandAPICommand.register();


      commandAPICommand = getCommandBase(command, permission, aliases);
      commandAPICommand = commandAPICommand.withArguments(list16);
      commandAPICommand = commandAPICommand.executesNative((sender, args) ->
      {

        ItemStack itemStack = convert((String) args[1]);
        if (itemStack == null)
        {
          return;
        }
        give(sender, (Collection<Player>) args[0], itemStack, (int) args[2], (boolean) args[3]);
      });
      commandAPICommand.register();


      commandAPICommand = getCommandBase(command, permission, aliases);
      commandAPICommand = commandAPICommand.withArguments(list17);
      commandAPICommand = commandAPICommand.executesNative((sender, args) ->
      {
        ItemStack itemStack = convert((String) args[1]);
        if (itemStack == null)
        {
          return;
        }
        give(sender, (Collection<Player>) args[0], itemStack, itemStack.getType().getMaxStackSize(), false);
      });
      commandAPICommand.register();

      commandAPICommand = getCommandBase(command, permission, aliases);
      commandAPICommand = commandAPICommand.withArguments(list18);
      commandAPICommand = commandAPICommand.executesNative((sender, args) ->
      {
        ItemStack itemStack = convert((String) args[1]);
        if (itemStack == null)
        {
          return;
        }
        give(sender, (Collection<Player>) args[0], itemStack, itemStack.getType().getMaxStackSize(), (boolean) args[3]);
      });
      commandAPICommand.register();
    }
    {
      commandAPICommand = getCommandBase(command, permission, aliases);
      commandAPICommand = commandAPICommand.withArguments(list19);
      commandAPICommand = commandAPICommand.executesNative((sender, args) ->
      {
        if (sender.getCallee() instanceof Player player)
        {
          ItemStack itemStack = convert((String) args[0]);
          if (itemStack == null)
          {
            return;
          }
          give(sender, Collections.singleton(player), itemStack, 1, false);
        }
        else
        {
          CommandAPI.fail("플레이어만 이 명령어를 사용할 수 있습니다");
        }
      });
      commandAPICommand.register();


      commandAPICommand = getCommandBase(command, permission, aliases);
      commandAPICommand = commandAPICommand.withArguments(list20);
      commandAPICommand = commandAPICommand.executesNative((sender, args) ->
      {
        if (sender.getCallee() instanceof Player player)
        {

          ItemStack itemStack = convert((String) args[0]);
          if (itemStack == null)
          {
            return;
          }
          give(sender, Collections.singleton(player), itemStack, 1, (Boolean) args[1]);
        }
        else
        {
          CommandAPI.fail("플레이어만 이 명령어를 사용할 수 있습니다");
        }
      });
      commandAPICommand.register();


      commandAPICommand = getCommandBase(command, permission, aliases);
      commandAPICommand = commandAPICommand.withArguments(list21);
      commandAPICommand = commandAPICommand.executesNative((sender, args) ->
      {
        if (sender.getCallee() instanceof Player player)
        {
          ItemStack itemStack = convert((String) args[0]);
          if (itemStack == null)
          {
            return;
          }
          give(sender, Collections.singleton(player), itemStack, (Integer) args[1], false);
        }
        else
        {
          CommandAPI.fail("플레이어만 이 명령어를 사용할 수 있습니다");
        }
      });
      commandAPICommand.register();


      commandAPICommand = getCommandBase(command, permission, aliases);
      commandAPICommand = commandAPICommand.withArguments(list22);
      commandAPICommand = commandAPICommand.executesNative((sender, args) ->
      {
        if (sender.getCallee() instanceof Player player)
        {
          ItemStack itemStack = convert((String) args[0]);
          if (itemStack == null)
          {
            return;
          }
          give(sender, Collections.singleton(player), itemStack, (Integer) args[1], (Boolean) args[2]);
        }
        else
        {
          CommandAPI.fail("플레이어만 이 명령어를 사용할 수 있습니다");
        }
      });
      commandAPICommand.register();


      commandAPICommand = getCommandBase(command, permission, aliases);
      commandAPICommand = commandAPICommand.withArguments(list23);
      commandAPICommand = commandAPICommand.executesNative((sender, args) ->
      {
        if (sender.getCallee() instanceof Player player)
        {
          ItemStack itemStack = convert((String) args[0]);
          if (itemStack == null)
          {
            return;
          }
          give(sender, Collections.singleton(player), itemStack, itemStack.getType().getMaxStackSize(), false);
        }
        else
        {
          CommandAPI.fail("플레이어만 이 명령어를 사용할 수 있습니다");
        }
      });
      commandAPICommand.register();

      commandAPICommand = getCommandBase(command, permission, aliases);
      commandAPICommand = commandAPICommand.withArguments(list24);
      commandAPICommand = commandAPICommand.executesNative((sender, args) ->
      {
        if (sender.getCallee() instanceof Player player)
        {
          ItemStack itemStack = convert((String) args[0]);
          if (itemStack == null)
          {
            return;
          }
          give(sender, Collections.singleton(player), itemStack, itemStack.getType().getMaxStackSize(), (Boolean) args[2]);
        }
        else
        {
          CommandAPI.fail("플레이어만 이 명령어를 사용할 수 있습니다");
        }
      });
      commandAPICommand.register();
    }
  }
}
