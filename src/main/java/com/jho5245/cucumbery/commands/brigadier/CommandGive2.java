package com.jho5245.cucumbery.commands.brigadier;

import com.jho5245.cucumbery.commands.brigadier.base.ArgumentUtil;
import com.jho5245.cucumbery.commands.brigadier.base.CommandBase;
import com.jho5245.cucumbery.util.additemmanager.AddItemUtil;
import com.jho5245.cucumbery.util.itemlore.ItemLore;
import com.jho5245.cucumbery.util.no_groups.MessageUtil;
import com.jho5245.cucumbery.util.storage.component.util.ComponentUtil;
import com.jho5245.cucumbery.util.storage.data.CustomMaterial;
import com.jho5245.cucumbery.util.storage.data.Variable;
import de.tr7zw.changeme.nbtapi.NBTItem;
import dev.jorel.commandapi.CommandAPI;
import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.IStringTooltip;
import dev.jorel.commandapi.StringTooltip;
import dev.jorel.commandapi.arguments.Argument;
import dev.jorel.commandapi.arguments.ArgumentSuggestions;
import dev.jorel.commandapi.arguments.CustomArgument;
import dev.jorel.commandapi.arguments.CustomArgument.CustomArgumentException;
import dev.jorel.commandapi.arguments.StringArgument;
import dev.jorel.commandapi.exceptions.WrapperCommandSyntaxException;
import dev.jorel.commandapi.wrappers.NativeProxyCommandSender;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static com.jho5245.cucumbery.commands.brigadier.base.ArgumentUtil.*;

public class CommandGive2 extends CommandBase
{
  private final Argument<ItemStack> CUSTOM_ITEM = customMaterialArgument();

  private Argument<ItemStack> customMaterialArgument()
  {
    return new CustomArgument<>(new StringArgument("커스텀 아이템"), info ->
    {
      try
      {
        return convert(info.input());
      }
      catch (NullPointerException e)
      {
        throw new CustomArgumentException(ComponentUtil.serialize(ComponentUtil.translate("%s은(는) 알 수 없는 아이템입니다", info.input())));
      }
    }).replaceSuggestions(ArgumentSuggestions.stringsWithTooltips(of()));
  }

  private IStringTooltip[] of()
  {
    List<IStringTooltip> list = new ArrayList<>();
    Arrays.stream(CustomMaterial.values()).toList().forEach(customMaterial -> list.add(StringTooltip.ofAdventureComponent(customMaterial.toString().toLowerCase(), customMaterial.getDisplayName())));
    ConfigurationSection root = Variable.customItemsConfig.getConfigurationSection("");
    if (root != null)
    {
      for (String key : root.getKeys(false))
      {
        String displayName = root.getString(key + ".display-name");
        if (displayName != null)
        {
          list.add(StringTooltip.ofAdventureComponent(key, ComponentUtil.create(MessageUtil.n2s(displayName))));
        }
      }
    }
    return list.toArray(IStringTooltip[]::new);
  }

  private final Argument<?>[] list1 = new Argument[]{MANY_PLAYERS, ITEMSTACK};

  private final Argument<?>[] list2 = new Argument[]{MANY_PLAYERS, ITEMSTACK, HIDE_OUTPUT};

  private final Argument<?>[] list3 = new Argument[]{MANY_PLAYERS, ITEMSTACK, ArgumentUtil.of(ArgumentType.INTEGER, "개수", 1, 2304)};

  private final Argument<?>[] list4 = new Argument[]{MANY_PLAYERS, ITEMSTACK, ArgumentUtil.of(ArgumentType.INTEGER, "개수", 1, 2304), HIDE_OUTPUT};

  private final Argument<?>[] list5 = new Argument[]{MANY_PLAYERS, ITEMSTACK, ArgumentUtil.of(ArgumentType.LITERAL, "max-stack")};

  private final Argument<?>[] list6 = new Argument[]{MANY_PLAYERS, ITEMSTACK, ArgumentUtil.of(ArgumentType.LITERAL, "max-stack"), HIDE_OUTPUT};

  private final Argument<?>[] list7 = new Argument[]{MANY_PLAYERS, CUSTOM_ITEM};

  private final Argument<?>[] list8 = new Argument[]{MANY_PLAYERS, CUSTOM_ITEM, HIDE_OUTPUT};

  private final Argument<?>[] list9 = new Argument[]{MANY_PLAYERS, CUSTOM_ITEM, ArgumentUtil.of(ArgumentType.INTEGER, "개수", 1, 2304)};

  private final Argument<?>[] list10 = new Argument[]{MANY_PLAYERS, CUSTOM_ITEM, ArgumentUtil.of(ArgumentType.INTEGER, "개수", 1, 2304), HIDE_OUTPUT};

  private final Argument<?>[] list11 = new Argument[]{MANY_PLAYERS, CUSTOM_ITEM, ArgumentUtil.of(ArgumentType.LITERAL, "max-stack")};

  private final Argument<?>[] list12 = new Argument[]{MANY_PLAYERS, CUSTOM_ITEM, ArgumentUtil.of(ArgumentType.LITERAL, "max-stack"), HIDE_OUTPUT};

  private ItemStack convert(String customType) throws NullPointerException
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
      if (Variable.customItemsConfig.getConfigurationSection(customType) == null)
      {
        throw new NullPointerException();
      }
      ItemStack itemStack = new ItemStack(Material.STONE);
      NBTItem nbtItem = new NBTItem(itemStack, true);
      nbtItem.setString("id", customType);
      return ItemLore.setItemLore(itemStack);
    }
  }

  private void give(@NotNull NativeProxyCommandSender sender, @NotNull Collection<Player> players, @NotNull ItemStack itemStack, int amount, boolean hideOutput) throws WrapperCommandSyntaxException
  {
    if (players.isEmpty())
    {
      throw CommandAPI.failWithAdventureComponent(Component.translatable("argument.entity.notfound.entity"));
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
        ItemStack itemStack = (ItemStack) args[1];
        give(sender, (Collection<Player>) args[0], itemStack, 1, false);
      });
      commandAPICommand.register();


      commandAPICommand = getCommandBase(command, permission, aliases);
      commandAPICommand = commandAPICommand.withArguments(list8);
      commandAPICommand = commandAPICommand.executesNative((sender, args) ->
      {
        ItemStack itemStack = (ItemStack) args[1];
        give(sender, (Collection<Player>) args[0], itemStack, 1, (boolean) args[2]);
      });
      commandAPICommand.register();


      commandAPICommand = getCommandBase(command, permission, aliases);
      commandAPICommand = commandAPICommand.withArguments(list9);
      commandAPICommand = commandAPICommand.executesNative((sender, args) ->
      {
        ItemStack itemStack = (ItemStack) args[1];
        give(sender, (Collection<Player>) args[0], itemStack, (int) args[2], false);
      });
      commandAPICommand.register();


      commandAPICommand = getCommandBase(command, permission, aliases);
      commandAPICommand = commandAPICommand.withArguments(list10);
      commandAPICommand = commandAPICommand.executesNative((sender, args) ->
      {

        ItemStack itemStack = (ItemStack) args[1];
        give(sender, (Collection<Player>) args[0], itemStack, (int) args[2], (boolean) args[3]);
      });
      commandAPICommand.register();


      commandAPICommand = getCommandBase(command, permission, aliases);
      commandAPICommand = commandAPICommand.withArguments(list11);
      commandAPICommand = commandAPICommand.executesNative((sender, args) ->
      {
        ItemStack itemStack = (ItemStack) args[1];
        give(sender, (Collection<Player>) args[0], itemStack, itemStack.getType().getMaxStackSize(), false);
      });
      commandAPICommand.register();

      commandAPICommand = getCommandBase(command, permission, aliases);
      commandAPICommand = commandAPICommand.withArguments(list12);
      commandAPICommand = commandAPICommand.executesNative((sender, args) ->
      {
        ItemStack itemStack = (ItemStack) args[1];
        give(sender, (Collection<Player>) args[0], itemStack, itemStack.getType().getMaxStackSize(), (boolean) args[3]);
      });
      commandAPICommand.register();
    }
  }
}
