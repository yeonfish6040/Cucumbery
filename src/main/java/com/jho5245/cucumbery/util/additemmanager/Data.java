package com.jho5245.cucumbery.util.additemmanager;

import com.jho5245.cucumbery.Cucumbery;
import com.jho5245.cucumbery.util.no_groups.MessageUtil;
import com.jho5245.cucumbery.util.storage.component.util.ComponentUtil;
import com.jho5245.cucumbery.util.storage.data.Constant;
import com.jho5245.cucumbery.util.storage.data.Prefix;
import dev.jorel.commandapi.wrappers.NativeProxyCommandSender;
import net.kyori.adventure.text.Component;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class Data
{
  private final CommandSender sender;

  private final Collection<UUID> targets;

  private final HashMap<UUID, Integer> lostAmounts;

  private final List<UUID> failure;

  private final ItemStack item;

  private final int amount;

  private final Component amountComponent;

  public Data(@NotNull CommandSender sender, @NotNull Collection<UUID> targets, HashMap<UUID, Integer> lostAmounts,
              @NotNull List<UUID> failure, @NotNull ItemStack given, int amount)
  {
    this.sender = sender;
    this.targets = targets;
    this.lostAmounts = lostAmounts;
    this.failure = failure;
    this.amount = amount;
    this.amountComponent = Component.text(amount, Constant.THE_COLOR);
    this.item = given;
  }

  public void sendFeedback(boolean hideOutput)
  {
    if (hideOutput)
    {
      return;
    }
    if (targets.isEmpty())
    {
      MessageUtil.sendError(sender, Prefix.NO_PLAYER);
      return;
    }
    MessageUtil.sendAdminMessage(sender, null, "commands.give.success.single", amountComponent, item, targets);

    if (!failure.isEmpty())
    {
      MessageUtil.sendWarn(sender, "%s이(가) 인벤토리가 가득 차서 %s %s개를 제대로 지급하지 못했습니다", failure, item, amountComponent);
      Component hover = Component.empty();
      for (int i = 0; i < failure.size(); i++)
      {
        UUID target = failure.get(i);
        int lostAmount = lostAmounts.get(target);
        hover = hover.append(ComponentUtil.translate("%s : %s개 (%s - %s)",
                target, Component.text(amount - lostAmount).color(Constant.THE_COLOR), amountComponent, Component.text(lostAmount, Constant.THE_COLOR)));
        if (i + 1 != failure.size())
        {
          hover = hover.append(Component.text("\n"));
        }
        if (sender instanceof Player player && target.equals(player.getUniqueId()))
        {
          continue;
        }
        MessageUtil.sendWarn(target, "인벤토리가 가득 차서 %s이(가) 보낸 %s %s개 중 %s개를 지급받지 못했습니다", sender, item, amountComponent, Component.text(lostAmount, Constant.THE_COLOR));
      }
      if (sender instanceof Player player)
      {
        Component message = ComponentUtil.translate("받은 아이템 개수 상태 (마우스를 올려서 보기)");
        MessageUtil.info(player, message.hoverEvent(hover));
      }
      else if (Cucumbery.using_CommandAPI && sender instanceof NativeProxyCommandSender proxyCommandSender && proxyCommandSender.getCallee() instanceof Player player)
      {
        Component message = ComponentUtil.translate("받은 아이템 개수 상태 (마우스를 올려서 보기)");
        MessageUtil.info(player, message.hoverEvent(hover));
      }
    }
    MessageUtil.sendMessage(sender, Prefix.INFO_HANDGIVE, "commands.give.success.single", amountComponent, item, targets);
    for (UUID target : targets)
    {
      if (sender instanceof Player player && target.equals(player.getUniqueId()))
      {
        continue;
      }
      MessageUtil.sendMessage(target, Prefix.INFO_HANDGIVE, "%s(으)로부터 %s을(를) %s개 지급받았습니다", sender, item, amountComponent);
    }
  }
}


















