package com.jho5245.cucumbery.util.additemmanager;

import com.jho5245.cucumbery.util.MessageUtil;
import com.jho5245.cucumbery.util.storage.ComponentUtil;
import com.jho5245.cucumbery.util.storage.data.Prefix;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.command.CommandSender;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;

public class Data
{
  private final CommandSender sender;

  private final Collection<? extends InventoryHolder> targets;

  private final HashMap<InventoryHolder, Integer> lostAmounts;

  private final List<? extends InventoryHolder> success;

  private final List<? extends InventoryHolder> failure;

  private final ItemStack given;

  private final int amount;

  private final Component item;

  public Data(@NotNull CommandSender sender, @NotNull Collection<? extends InventoryHolder> targets, HashMap<InventoryHolder, Integer> lostAmounts,
              @NotNull List<? extends InventoryHolder> success, @NotNull List<? extends InventoryHolder> failure, @NotNull ItemStack given, int amount)
  {
    this.sender = sender;
    this.targets = targets;
    this.lostAmounts = lostAmounts;
    this.success = success;
    this.failure = failure;
    this.given = given;
    this.item = ComponentUtil.itemName(given, TextColor.color(255, 204, 0)).hoverEvent(given.asHoverEvent());
    this.amount = amount;
  }

  public void sendFeedback(boolean hideOutput)
  {
    if (hideOutput)
    {
      return;
    }
    if (targets.size() == 0)
    {
      return;
    }
    if (success.size() == 0)
    {
      MessageUtil.sendWarn(sender, ComponentUtil.createTranslate(targets.size() == 1 ? "%s이(가) %s %s를 지급받지 못했습니다." : "%s 전부 %s %s를 지급받지 못했습니다.",
              targets, item, ComponentUtil.createTranslate("%s개", amount)));
      for (InventoryHolder target : targets)
      {
        if (sender.equals(target))
        {
          continue;
        }
        MessageUtil.sendWarn(target, ComponentUtil.createTranslate("인벤토리가 가득 차서 %s이(가) 보낸 %s %s를 지급받지 못했습니다.", sender, given,
                ComponentUtil.createTranslate("%s개", lostAmounts.get(target))));
      }
      return;
    }
    MessageUtil.sendMessage(sender, Prefix.INFO_HANDGIVE, success, "에게", given, "을(를) 성공적으로 지급하였습니다.");
    if (failure.size() > 0)
    {
      MessageUtil.sendWarn(sender, failure, "이(가) 인본토리가 가득 차서 ");
    }
    for (InventoryHolder target : success)
    {
      if (sender.equals(target))
      {
        continue;
      }
      MessageUtil.sendMessage(target, Prefix.INFO_HANDGIVE, sender, "으로부터", given, "을(를) 성공적으로 지급받았습니다.");
    }
    for (InventoryHolder target : failure)
    {
      if (sender.equals(target))
      {
        continue;
      }
      MessageUtil.sendWarn(target, "인벤토리가 가득 차서", sender, "이(가) 보낸 ", given, "의 일부 혹은 전부를 지급받지 못햇습니다.");
    }
  }
}

















