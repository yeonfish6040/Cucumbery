package com.jho5245.cucumbery.commands.no_groups;

import com.destroystokyo.paper.event.server.AsyncTabCompleteEvent.Completion;
import com.jho5245.cucumbery.util.itemlore.ItemLore;
import com.jho5245.cucumbery.util.itemlore.ItemLoreView;
import com.jho5245.cucumbery.util.no_groups.CommandTabUtil;
import com.jho5245.cucumbery.util.no_groups.CucumberyCommandExecutor;
import com.jho5245.cucumbery.util.no_groups.MessageUtil;
import com.jho5245.cucumbery.util.no_groups.Method;
import com.jho5245.cucumbery.util.storage.component.util.ComponentUtil;
import com.jho5245.cucumbery.util.storage.data.Permission;
import com.jho5245.cucumbery.util.storage.data.Prefix;
import com.jho5245.cucumbery.util.storage.data.custom_enchant.ultimate.CustomEnchantUltimate;
import com.jho5245.cucumbery.util.storage.no_groups.ItemStackUtil;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.format.TextDecoration.State;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class CommandEnchant implements CucumberyCommandExecutor
{
  @Override
  public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args)
  {
    if (!Method.hasPermission(sender, Permission.CMD_CUSTOM_EFFECT, true))
    {
      return true;
    }
    if (!(sender instanceof Player player))
    {
      MessageUtil.sendError(sender, Prefix.ONLY_PLAYER);
      return true;
    }
    if (!MessageUtil.checkQuoteIsValidInArgs(sender, args = MessageUtil.wrapWithQuote(args)))
    {
      return true;
    }
    ItemStack itemStack = player.getInventory().getItemInMainHand();
    if (!ItemStackUtil.itemExists(itemStack))
    {
      MessageUtil.sendError(player, Prefix.NO_HOLDING_ITEM);
      return true;
    }
    if (args.length < 1)
    {
      MessageUtil.shortArg(sender, 1, args);
      MessageUtil.info(sender, Method.getUsage(command));
      return true;
    }
    String name = args[0];
    if (!name.contains(":"))
    {
      name = "minecraft:" + name;
    }
    String[] split = name.split(":");
    NamespacedKey namespacedKey;
    try
    {
      namespacedKey = new NamespacedKey(split[0], split[1]);
    }
    catch (Exception e)
    {
      MessageUtil.sendError(player, "%s은(는) 잘못된 키입니다.", name);
      return true;
    }
    Enchantment enchantment = Enchantment.getByKey(namespacedKey);
    if (enchantment == null)
    {
      MessageUtil.sendError(player, "%s은(는) 알 수 없는 마법입니다.", name);
      return true;
    }
    int level = enchantment.getMaxLevel();
    if (args.length == 2)
    {
      try
      {
        level = Integer.parseInt(args[1]);
      }
      catch (Exception e)
      {
        MessageUtil.sendError(player, Prefix.ONLY_INTEGER);
        return true;
      }
      if (!MessageUtil.checkNumberSize(player, level, 0, 32767))
      {
        return true;
      }
    }
    else if (args.length > 2)
    {
      MessageUtil.longArg(sender, 2, args);
      MessageUtil.info(sender, Method.getUsage(command));
      return true;
    }
    Material type = itemStack.getType();
    if (type == Material.ENCHANTED_BOOK)
    {
      EnchantmentStorageMeta itemMeta = (EnchantmentStorageMeta) itemStack.getItemMeta();
      if (level == 0)
      {
        if (!itemMeta.hasStoredEnchant(enchantment))
        {
          MessageUtil.sendError(player, "변경 사항이 없습니다. %s에 부여 가능한 마법 %s(이)가 없습니다.", itemStack, enchantment);
          return true;
        }
        itemMeta.removeStoredEnchant(enchantment);
        MessageUtil.sendMessage(player, Prefix.INFO_SETDATA, "%s에서 부여 가능한 마법 %s을(를) 제거했습니다", itemStack, enchantment);
      }
      else
      {
        int alreadyLevel = itemMeta.getStoredEnchantLevel(enchantment);
        if (level == alreadyLevel)
        {
          MessageUtil.sendError(player, "변경 사항이 없습니다. 이미 %s에 부여 가능한 마법 %s의 레벨이 %s입니다.", itemStack, enchantment, level);
          return true;
        }
        boolean change = itemMeta.hasStoredEnchant(enchantment);
        itemMeta.addStoredEnchant(enchantment, level, true);
        MessageUtil.sendMessage(player, Prefix.INFO_SETDATA, "%s에 %s %s 부여 가능한 마법을 " + (change ? "변경" : "추가") + "했습니다", itemStack, enchantment, level);
      }
      itemStack.setItemMeta(itemMeta);
      player.getInventory().setItemInMainHand(ItemLore.setItemLore(itemStack, ItemLoreView.of(player)));
    }
    else
    {
      ItemMeta itemMeta = itemStack.getItemMeta();
      if (level == 0)
      {
        if (!itemMeta.hasEnchant(enchantment))
        {
          MessageUtil.sendError(player, "변경 사항이 없습니다. %s에 부여된 마법 %s(이)가 없습니다.", itemStack, enchantment);
          return true;
        }
        itemMeta.removeEnchant(enchantment);
        MessageUtil.sendMessage(player, Prefix.INFO_SETDATA, "%s에서 부여된 마법 %s을(를) 제거했습니다", itemStack, enchantment);
      }
      else
      {
        int alreadyLevel = itemMeta.getEnchantLevel(enchantment);
        if (level == alreadyLevel)
        {
          MessageUtil.sendError(player, "변경 사항이 없습니다. 이미 %s에 부여된 마법 %s의 레벨이 %s입니다.", itemStack, enchantment, level);
          return true;
        }
        boolean change = itemMeta.hasEnchant(enchantment);
        itemMeta.addEnchant(enchantment, level, true);
        if (!enchantment.canEnchantItem(itemStack))
        {
          MessageUtil.sendWarn(player, "일반적인 상황에서는 %s 마법은 %s에 부여할 수 없거나 부여해도 정상적으로 기능하지 않습니다!", enchantment, itemStack);
        }
        MessageUtil.sendMessage(player, Prefix.INFO_SETDATA, "%s에 %s %s 마법을 " + (change ? "변경" : "추가") + "했습니다", itemStack, enchantment, level);
      }
      itemStack.setItemMeta(itemMeta);
      player.getInventory().setItemInMainHand(ItemLore.setItemLore(itemStack, ItemLoreView.of(player)));
    }
    return true;
  }

  @Override
  public @NotNull List<Completion> completion(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args, @NotNull Location location)
  {
    if (args.length == 1)
    {
      List<Completion> list = new ArrayList<>();
      for (Enchantment enchantment : Enchantment.values())
      {
        Component hover =  ComponentUtil.translate(enchantment.translationKey());
        if (enchantment instanceof CustomEnchantUltimate)
        {
          hover = hover.decoration(TextDecoration.BOLD, State.TRUE).color(NamedTextColor.LIGHT_PURPLE);
        }
        else if (enchantment.isCursed())
        {
          hover = hover.color(NamedTextColor.RED);
        }
        Completion completion = Completion.completion(enchantment.getKey().toString(), hover);
        list.add(completion);
      }
      return CommandTabUtil.tabCompleterList(args, list, "<마법>");
    }
    else if (args.length == 2)
    {
      List<Completion> list1 = CommandTabUtil.tabCompleterIntegerRadius(args, 0, 32767, "[레벨]");
      List<Completion> list2 = new ArrayList<>();
      list2.add(Completion.completion("0"));
      String enchString = args[0];
      if (!enchString.contains(":") && enchString.length() <= 2)
      {
        enchString = "minecraft:" + enchString;
      }
      NamespacedKey namespacedKey;
      try
      {
        String[] split = enchString.split(":");
        namespacedKey = new NamespacedKey(split[0], split[1]);
        Enchantment enchantment = Enchantment.getByKey(namespacedKey);
        if (enchantment == null)
        {
          throw new Exception();
        }
        int maxLevel = enchantment.getMaxLevel();
        list2.add(Completion.completion(maxLevel + "", ComponentUtil.translate("%s의 최대 레벨", enchantment)));
      }
      catch (Exception e)
      {
        return CommandTabUtil.errorMessage("%s은(는) 잘못되거나 알 수 없는 마법입니다.", enchString);
      }
      return CommandTabUtil.sortError(list1, CommandTabUtil.tabCompleterList(args, list2, "[레벨]"));
    }
    return CommandTabUtil.ARGS_LONG;
  }
}
