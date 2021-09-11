package com.jho5245.cucumbery.util.additemmanager;

import com.jho5245.cucumbery.util.MessageUtil;
import com.jho5245.cucumbery.util.storage.ComponentUtil;
import com.jho5245.cucumbery.util.storage.data.Constant;
import com.jho5245.cucumbery.util.storage.data.Prefix;
import net.kyori.adventure.text.Component;
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

  private final Component item;

  public Data(@NotNull CommandSender sender, @NotNull Collection<? extends InventoryHolder> targets, HashMap<InventoryHolder, Integer> lostAmounts,
              @NotNull List<? extends InventoryHolder> success, @NotNull List<? extends InventoryHolder> failure, @NotNull ItemStack given, int amount)
  {
    this.sender = sender;
    this.targets = targets;
    this.lostAmounts = lostAmounts;
    this.success = success;
    this.failure = failure;
    this.item = ComponentUtil.itemStackComponent(given, amount, Constant.THE_COLOR);
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
      MessageUtil.sendWarn(sender, ComponentUtil.createTranslate(targets.size() == 1 ? "%s이(가) %s을(를) 지급받지 못했습니다." : "%s 전부 %s을(를) 지급받지 못했습니다.",
              targets, item));
      for (InventoryHolder target : targets)
      {
        if (sender.equals(target))
        {
          continue;
        }
        MessageUtil.sendWarn(target, ComponentUtil.createTranslate("인벤토리가 가득 차서 %s이(가) 보낸 %s을(를) 지급받지 못했습니다.", sender, item));
      }
      return;
    }
    MessageUtil.sendMessage(sender, Prefix.INFO_HANDGIVE, ComponentUtil.createTranslate("%s에게 %s을(를) 성공적으로 지급하였습니다.", success, item));
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
      MessageUtil.sendMessage(target, Prefix.INFO_HANDGIVE, ComponentUtil.createTranslate("%s(으)로부터 %s을(를) 성공적으로 지급받았습니다.", sender, item));
    }
    for (InventoryHolder target : failure)
    {
      if (sender.equals(target))
      {
        continue;
      }
      MessageUtil.sendWarn(target, ComponentUtil.createTranslate("인벤토리가 가득 차서 %s이(가) 보낸 %s의 %s을(를) 지급받지 못했습니다.", sender, item, lostAmounts.get(target)));
    }
  }
}


















