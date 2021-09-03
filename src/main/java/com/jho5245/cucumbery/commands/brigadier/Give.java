package com.jho5245.cucumbery.commands.brigadier;

import com.jho5245.cucumbery.commands.brigadier.base.CommandBase;
import com.jho5245.cucumbery.util.Method;
import com.jho5245.cucumbery.util.additemmanager.AddItemUtil;
import com.jho5245.cucumbery.util.itemlore.ItemLore;
import com.jho5245.cucumbery.util.storage.ComponentUtil;
import com.jho5245.cucumbery.util.storage.SoundPlay;
import com.jho5245.cucumbery.util.storage.data.Constant;
import dev.jorel.commandapi.CommandAPI;
import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.*;
import dev.jorel.commandapi.arguments.EntitySelectorArgument.EntitySelector;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Give extends CommandBase
{
  private final List<Argument> arguments1 = new ArrayList<>();

  {
    arguments1.add(new EntitySelectorArgument("플레이어", EntitySelector.MANY_PLAYERS));
    arguments1.add(new ItemStackArgument("아이템"));
  }

  private final List<Argument> arguments2 = new ArrayList<>();

  {
    arguments2.add(new EntitySelectorArgument("플레이어", EntitySelector.MANY_PLAYERS));
    arguments2.add(new ItemStackArgument("아이템"));
    arguments2.add(new IntegerArgument("개수", 1, 2304));
  }

  private final List<Argument> arguments3 = new ArrayList<>();

  {
    arguments3.add(new EntitySelectorArgument("플레이어", EntitySelector.MANY_PLAYERS));
    arguments3.add(new ItemStackArgument("아이템"));
    arguments3.add(new IntegerArgument("개수", 1, 2304));
    arguments3.add(new BooleanArgument("명령어 출력 숨김 여부"));
  }

  @SuppressWarnings("unchecked")
  public void registerCommand(String command, String permission, String... aliases)
  {
    /*
     * /cgive 플레이어 아이템 1 false
     */
    CommandAPICommand commandAPICommand = getCommandBase(command, permission, aliases);
    commandAPICommand = commandAPICommand.withArguments(arguments1);
    commandAPICommand = commandAPICommand.executesNative((sender, args) ->
    {
      CommandSender commandSender = sender.getCallee();
      try
      {
        commandSender.getName();
      }
      catch (Exception e)
      {
        commandSender = sender.getCaller();
      }
      Collection<Player> players = (Collection<Player>) args[0];
      int total = players.size();
      if (total == 0)
      {
        CommandAPI.fail("플레이어를 찾을 수 없습니다.");
        return;
      }
      ItemStack itemStack = (ItemStack) args[1];
      ItemLore.setItemLore(itemStack);
      int amount = itemStack.getAmount();
      AddItemUtil.addItemResult2(commandSender, (List<? extends InventoryHolder>) players, itemStack, amount).sendFeedback(false);
    });
    commandAPICommand.register();

    /*
     * /cgive 플레이어 아이템 개수 false
     */

    commandAPICommand = getCommandBase(command, permission, aliases);
    commandAPICommand = commandAPICommand.withArguments(arguments2);
    commandAPICommand = commandAPICommand.executesNative((sender, args) ->
    {
      CommandSender commandSender = sender.getCallee();
      try
      {
        commandSender.getName();
      }
      catch (Exception e)
      {
        commandSender = sender.getCaller();
      }
      Collection<Player> players = (Collection<Player>) args[0];
      int total = players.size();
      if (total == 0)
      {
        CommandAPI.fail("플레이어를 찾을 수 없습니다.");
        return;
      }
      ItemStack itemStack = (ItemStack) args[1];
      itemStack.setAmount((int) args[2]);
      int amount = itemStack.getAmount();
      for (Player player : players)
      {
        if (Method.usingLoreFeature(player))
        {
          ItemLore.setItemLore(itemStack, player);
        }
        else
        {
          ItemLore.removeItemLore(itemStack);
        }
        Collection<ItemStack> lostItemMap = player.getInventory().addItem(itemStack).values();
        ItemStack lostItem = null;
        int lostAmount = 0;
        if (lostItemMap.size() != 0)
        {
          for (ItemStack lost : lostItemMap)
          {
            lostItem = lost;
            lostAmount = lostItem.getAmount();
          }
        }
        String targetName = "§e" + ComponentUtil.senderComponent(player);
        if (lostItem != null)
        {
          lostAmount = lostItem.getAmount();
          String lostDisplay = (lostAmount == amount ? "§c전부§r(§e총 " + lostAmount + "개§r)" : lostAmount + "개");
          SoundPlay.playSound(commandSender, Constant.WARNING_SOUND);
          amount -= lostAmount;
        }
        if (amount != 0)
        {
          String amountDisplay = (lostAmount == 0 ? amount + "" : amount + "§r(§e" + (amount + lostAmount) + "§r - §e" + lostAmount + "§r)§e");
        }
        itemStack.setAmount((int) args[2]);
        amount = itemStack.getAmount();
      }
    });
    commandAPICommand.register();

    /*
     * /cgive 플레이어 아이템 개수 [명령어 출력 숨김 여부]
     */

    commandAPICommand = getCommandBase(command, permission, aliases);
    commandAPICommand = commandAPICommand.withArguments(arguments3);
    commandAPICommand = commandAPICommand.executesNative((sender, args) ->
    {
      CommandSender commandSender = sender.getCallee();
      try
      {
        commandSender.getName();
      }
      catch (Exception e)
      {
        commandSender = sender.getCaller();
      }
      Collection<Player> players = (Collection<Player>) args[0];
      boolean hideOutput = (boolean) args[3];
      int total = players.size();
      if (total == 0)
      {
        CommandAPI.fail("플레이어를 찾을 수 없습니다.");
        return;
      }
      ItemStack itemStack = (ItemStack) args[1];
      itemStack.setAmount((int) args[2]);
      int amount = itemStack.getAmount();
      for (Player player : players)
      {
        if (Method.usingLoreFeature(player))
        {
          ItemLore.setItemLore(itemStack, player);
        }
        else
        {
          ItemLore.removeItemLore(itemStack);
        }
        player.getInventory().addItem(itemStack);
        itemStack.setAmount((int) args[2]);
      }
    });
    commandAPICommand.register();
  }
}
