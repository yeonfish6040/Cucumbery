package com.jho5245.cucumbery.commands.brigadier;

import com.jho5245.cucumbery.Cucumbery;
import com.jho5245.cucumbery.commands.brigadier.base.CommandBase;
import com.jho5245.cucumbery.util.no_groups.MessageUtil;
import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.Argument;
import dev.jorel.commandapi.arguments.DoubleArgument;
import dev.jorel.commandapi.arguments.IntegerRangeArgument;
import dev.jorel.commandapi.arguments.ItemStackPredicateArgument;
import dev.jorel.commandapi.wrappers.IntegerRange;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.function.Predicate;

import static com.jho5245.cucumbery.commands.brigadier.base.ArgumentUtil.ONE_PLAYER;

public class CommandSellItem extends CommandBase
{
  private final Argument ITEMSTACK = new ItemStackPredicateArgument("items to sell");

  private final Argument PRICE = new DoubleArgument("price per item", 0);

  private final Argument AMOUNT = new IntegerRangeArgument("amount");

  private final Argument FEE = new DoubleArgument("fee", 0, 1);

  private final List<Argument<?>> list1 =  List.of(ONE_PLAYER, ITEMSTACK, PRICE);

  private final List<Argument<?>> list2 =  List.of(ONE_PLAYER, ITEMSTACK, PRICE, AMOUNT);

  private void sellItem(@NotNull Player player, @NotNull Predicate<ItemStack> item, double price, int amount, int maxAmount, double fee)
  {
    if (!Cucumbery.using_Vault_Economy)
    {
      MessageUtil.sendError(player, "no vault found");
      return;
    }

    int foundAmount = 0;
    for (ItemStack itemStack : player.getInventory())
    {
      if (item.test(itemStack))
      {
        foundAmount += itemStack.getAmount();
      }
    }

    if (foundAmount == 0)
    {
      MessageUtil.sendError(player, "no ITEMS");
      return;
    }

    final int sellingAmount = foundAmount;

    if (!(amount == 0 && maxAmount == 0) && (foundAmount < amount || foundAmount > maxAmount))
    {
      MessageUtil.sendError(player, "you cannot sell ", foundAmount, " because the shop wants ", amount, "~", maxAmount);
      return;
    }

    ItemStack preview = new ItemStack(Material.CHEST);

    for (ItemStack itemStack : player.getInventory())
    {
      if (foundAmount == 0)
        break;
      if (item.test(itemStack))
      {
        itemStack = itemStack.clone();
        preview = itemStack.clone();
        int invAmount = itemStack.getAmount();
        if (foundAmount < invAmount)
        {
          itemStack.setAmount(foundAmount);
        }
        foundAmount -= itemStack.getAmount();
        player.getInventory().removeItem(itemStack);
      }
    }

    double cost = sellingAmount * price;
    Cucumbery.eco.depositPlayer(player, cost);
    MessageUtil.info(player, "you've sold ", preview, "x", sellingAmount, " for ", cost, "(", price, " per each)");
    if (fee > 0d)
    {
      MessageUtil.info(player, "you've paid ", fee, " for transaction fee");
      Cucumbery.eco.withdrawPlayer(player, cost * fee);
    }
  }

  @Override
  @SuppressWarnings("unchecked")
  public void registerCommand(String command, String permission, String... aliases)
  {
    CommandAPICommand commandAPICommand = getCommandBase(command, permission, aliases);
    commandAPICommand = commandAPICommand.withArguments(list1);
    commandAPICommand = commandAPICommand.executesNative((sender, args) -> {
      sellItem((Player) args.get(0), (Predicate<ItemStack>) args.get(1), (double) args.get(2),0, 0, 0);
    });
    commandAPICommand.register();

    commandAPICommand = getCommandBase(command, permission, aliases);
    commandAPICommand = commandAPICommand.withArguments(list2);
    commandAPICommand = commandAPICommand.executesNative((sender, args) -> {
      IntegerRange integerRange = (IntegerRange) args.get(3);
      sellItem((Player) args.get(0), (Predicate<ItemStack>) args.get(1), (double) args.get(2), integerRange.getLowerBound(), integerRange.getUpperBound(), 0);
    });
    commandAPICommand.register();
  }
}
