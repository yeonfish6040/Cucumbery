package com.jho5245.cucumbery.commands.no_groups;

import com.destroystokyo.paper.event.server.AsyncTabCompleteEvent.Completion;
import com.jho5245.cucumbery.util.gui.GUIManager;
import com.jho5245.cucumbery.util.gui.GUIManager.GUIType;
import com.jho5245.cucumbery.util.itemlore.ItemLore;
import com.jho5245.cucumbery.util.itemlore.ItemLoreUtil;
import com.jho5245.cucumbery.util.itemlore.ItemLoreView;
import com.jho5245.cucumbery.util.no_groups.AsyncTabCompleter;
import com.jho5245.cucumbery.util.no_groups.CommandTabUtil;
import com.jho5245.cucumbery.util.no_groups.MessageUtil;
import com.jho5245.cucumbery.util.no_groups.Method;
import com.jho5245.cucumbery.util.storage.component.util.ComponentUtil;
import com.jho5245.cucumbery.util.storage.data.Permission;
import com.jho5245.cucumbery.util.storage.data.Prefix;
import com.jho5245.cucumbery.util.storage.data.Variable;
import com.jho5245.cucumbery.util.storage.no_groups.CreateItemStack;
import com.jho5245.cucumbery.util.storage.no_groups.ItemStackUtil;
import net.kyori.adventure.text.Component;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class CommandStash implements CommandExecutor, TabCompleter, AsyncTabCompleter
{
  public static void openStash(@NotNull Player player, boolean firstOpen)
  {
    UUID uuid = player.getUniqueId();
    List<ItemStack> stash = Variable.itemStash.getOrDefault(uuid, Collections.emptyList());
    if (stash.isEmpty())
    {
      MessageUtil.sendMessage(player, Prefix.INFO_STASH, "보관된 아이템이 하나도 없습니다");
      return;
    }
    Inventory inventory = firstOpen ? GUIManager.create(6, ComponentUtil.translate("&8아이템 저장고"), "stash-" + uuid) : player.getOpenInventory().getTopInventory();
    if (firstOpen)
    {
      final ItemStack deco = CreateItemStack.create(Material.WHITE_STAINED_GLASS_PANE, Component.empty());
      inventory.setItem(45, deco);
      inventory.setItem(46, deco);
      inventory.setItem(47, deco);
      inventory.setItem(48, deco);
      inventory.setItem(50, deco);
      inventory.setItem(51, deco);
      inventory.setItem(52, deco);
      inventory.setItem(53, deco);
    }
    else {
      for (int i = 0; i < 45; i++)
      {
        inventory.setItem(i, null);
      }
    }
    if (player.getInventory().firstEmpty() == -1)
    {
      inventory.setItem(49, CreateItemStack.create(Material.BARRIER, 1, ComponentUtil.translate("&c인벤토리 가득참!"), ComponentUtil.translate("rg255,204;인벤토리가 가득 차서 전부 수령할 수 없습니다!", stash.size()), false));
    }
    else
    {
      inventory.setItem(49, CreateItemStack.create(Material.HOPPER, 1, ComponentUtil.translate("&a전부 수령하기"), ComponentUtil.translate("&7클릭하여 아이템 %s개를 전부 수령합니다", stash.size()), false));
    }
    for (int i = 0; i < Math.min(45, stash.size()); i++)
    {
      ItemStack itemStack = stash.get(i).clone();
      itemStack = itemStack.clone();
      ItemLore.setItemLore(itemStack, ItemLoreView.of(player));
      if (ItemLoreUtil.isCucumberyTMIFood(itemStack))
      {
        ItemLoreUtil.removeCucumberyTMIFood(itemStack);
      }
      ItemMeta itemMeta = itemStack.getItemMeta();
      List<Component> lore = itemMeta.lore();
      if (lore == null)
      {
        lore = new ArrayList<>();
      }
      lore.add(Component.empty());
      int space = ItemStackUtil.countSpace(player, stash.get(i)), amount = itemStack.getAmount();
      if (space < amount)
      {
        lore.add(ComponentUtil.translate("&c이 아이템을 수령하기 위한"));
        lore.add(ComponentUtil.translate("&c충분한 인벤토리 공간이 부족합니다!"));
      }
      else
      {
        lore.add(ComponentUtil.translate("rg255,204;클릭하여 수령하기!"));
      }
      itemMeta.lore(lore);
      itemStack.setItemMeta(itemMeta);
      inventory.setItem(i, itemStack);
    }
    if (firstOpen)
    player.openInventory(inventory);
  }

  /**
   * Executes the given command, returning its success.
   * <br>
   * If false is returned, then the "usage" plugin.yml entry for this command
   * (if defined) will be sent to the player.
   *
   * @param sender  Source of the command
   * @param command Command which was executed
   * @param label   Alias of the command which was used
   * @param args    Passed command arguments
   * @return true if a valid command, otherwise false
   */
  @Override
  public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args)
  {
    if (!Method.hasPermission(sender, Permission.CMD_STASH, true))
    {
      return true;
    }
    if (!(sender instanceof Player player))
    {
      return true;
    }
    if (args.length == 0)
    {
      GUIManager.openGUI(player, GUIType.ITEM_STASH);
    }
    else
    {
      MessageUtil.longArg(sender, 0, args);
      MessageUtil.commandInfo(sender, label, "");
      return true;
    }
    return true;
  }

  /**
   * Requests a list of possible completions for a command argument.
   *
   * @param sender   Source of the command.  For players tab-completing a
   *                 command inside a command block, this will be the player, not
   *                 the command block.
   * @param cmd      the command to be executed.
   * @param label    Alias of the command which was used
   * @param args     The arguments passed to the command, including final
   *                 partial argument to be completed
   * @param location The location of this command was executed.
   * @return A List of possible completions for the final argument, or an empty list.
   */
  @Override
  public @NotNull List<Completion> completion(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args, @NotNull Location location)
  {
    return CommandTabUtil.ARGS_LONG;
  }

  /**
   * Requests a list of possible completions for a command argument.
   *
   * @param sender  Source of the command.  For players tab-completing a
   *                command inside of a command block, this will be the player, not
   *                the command block.
   * @param command Command which was executed
   * @param label   Alias of the command which was used
   * @param args    The arguments passed to the command, including final
   *                partial argument to be completed
   * @return A List of possible completions for the final argument, or null
   * to default to the command executor
   */
  @Override
  public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args)
  {
    return Collections.singletonList(Prefix.ARGS_LONG.toString());
  }
}
