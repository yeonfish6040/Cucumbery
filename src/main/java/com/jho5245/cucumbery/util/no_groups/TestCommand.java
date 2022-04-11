package com.jho5245.cucumbery.util.no_groups;

import com.destroystokyo.paper.event.server.AsyncTabCompleteEvent.Completion;
import com.jho5245.cucumbery.custom.customeffect.CustomEffect;
import com.jho5245.cucumbery.custom.customeffect.CustomEffectManager;
import com.jho5245.cucumbery.custom.customeffect.CustomEffectType;
import com.jho5245.cucumbery.custom.customeffect.children.group.ItemStackCustomEffect;
import com.jho5245.cucumbery.util.no_groups.LocationUtil.LocationTooltip;
import com.jho5245.cucumbery.util.no_groups.LocationUtil.Rotation;
import com.jho5245.cucumbery.util.storage.component.ItemStackComponent;
import com.jho5245.cucumbery.util.storage.data.Constant;
import com.jho5245.cucumbery.util.storage.data.Prefix;
import net.kyori.adventure.text.Component;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@SuppressWarnings("all")
public class TestCommand implements CommandExecutor, AsyncTabCompleter
{
  private static final NamespacedKey test = NamespacedKey.fromString("test");

  public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args)
  {
    args = MessageUtil.wrapWithQuote(args);
    if (!Method.hasPermission(sender, "asdf", true))
    {
      return true;
    }
    try
    {
      if (sender instanceof Player player)
      {
        if (CustomEffectManager.hasEffect(player, CustomEffectType.NOTIFY_NO_TRADE_ITEM_DROP))
        {
          CustomEffect customEffect = CustomEffectManager.getEffect(player, CustomEffectType.NOTIFY_NO_TRADE_ITEM_DROP);
          if (customEffect instanceof ItemStackCustomEffect itemStackCustomEffect)
          {
            ItemStack itemStack = itemStackCustomEffect.getItemStack();
            Component itemDisplay = ItemStackComponent.itemStackComponent(itemStack, Constant.THE_COLOR);
            MessageUtil.info(player, "%s을(를) 인벤토리에서 제거하였습니다", itemDisplay);
            player.getInventory().removeItem(itemStack);
            CustomEffectManager.removeEffect(player, CustomEffectType.NOTIFY_NO_TRADE_ITEM_DROP);
          }
        }
        if (args.length == 1)
        {
          Entity entity = SelectorUtil.getEntity(sender, args[0]);
          MessageUtil.info(sender, entity);
        }
        if (args.length == 3)
        {
          World world = LocationUtil.world(sender, args[0], true);
          List<LocationTooltip> tooltips = new ArrayList<>();
          Location location = LocationUtil.location(sender, args[1], false, true);
          Rotation rotation = LocationUtil.rotation(sender, args[2], true);
          location = new Location(world, location.getX(), location.getY(), location.getZ(), rotation.yaw(), rotation.pitch());
          MessageUtil.info(sender, "current : %s", player.getLocation());
          MessageUtil.info(sender, "new : %s", location);
        }
        /*
        ItemStack itemStack = player.getInventory().getItemInMainHand();
        switch (args[0])
        {
          case "add" -> {
            ItemStack result = new ItemStack(Material.TNT);
            ItemMeta itemMeta = result.getItemMeta();
            itemMeta.displayName(ComponentUtil.translate("&iitem.minecraft.diamond"));
            result.setItemMeta(itemMeta);
            ItemStack ingredient = new ItemStack(Material.DIAMOND);
            ShapedRecipe shapedRecipe = new ShapedRecipe(test, result);
            shapedRecipe = shapedRecipe.shape("aaa", "a#a", "aaa").setIngredient('a', ingredient);
              MessageUtil.sendMessage(player, Bukkit.addRecipe(shapedRecipe) ? "recipe added!" : "&ccould not add that recipe!");
          }
          case "remove" -> {
            MessageUtil.sendMessage(player, Bukkit.removeRecipe(test) ? "recipe removed!" : "could not reipce that recipe!");
          }
        }*/
      }
      if (args.length >= 2)
      {
        switch (args[0])
        {
          case "entities" -> {
            List<Entity> entities = SelectorUtil.getEntities(sender, args[1], true);
            MessageUtil.sendMessage(sender, entities != null ? entities : "null");
          }
          case "entity" -> {
            Entity entity = SelectorUtil.getEntity(sender, args[1], true);
            MessageUtil.sendMessage(sender, entity != null ? entity : "null");
          }
          case "players" -> {
            List<Player> players = SelectorUtil.getPlayers(sender, args[1], true);
            MessageUtil.sendMessage(sender, players != null ? players : "null");
          }
          case "player" -> {
            Player p = SelectorUtil.getPlayer(sender, args[1], true);
            MessageUtil.sendMessage(sender, p != null ? p : "null");
          }
        }
      }
    }
    catch (Exception e)
    {

    }
    return true;
  }

  public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args)
  {
    if (!MessageUtil.checkQuoteIsValidInArgs(sender, args = MessageUtil.wrapWithQuote(true, args), true))
    {
      return Collections.singletonList(args[0]);
    }
    int length = args.length;
    if (length == 1)
    {
      return Method.tabCompleterList(args, "<뭐 왜 왓>", "entities", "entity", "players", "player");
    }
    else if (length == 2)
    {
      switch (args[0])
      {
        case "entities" -> {
          return Method.tabCompleterEntity(sender, args, "<개체>", true);
        }
        case "entity" -> {
          return Method.tabCompleterEntity(sender, args, "<개체>");
        }
        case "players" -> {
          return Method.tabCompleterPlayer(sender, args, "<플레이어>", true);
        }
        case "player" -> {
          return Method.tabCompleterPlayer(sender, args, "<플레이어>");
        }
      }
      return Collections.EMPTY_LIST;
    }
    return Collections.singletonList(Prefix.ARGS_LONG.toString());
  }

  @NotNull
  public List<Completion> completion(@NotNull CommandSender sender, @NotNull String label, @NotNull String[] args, @NotNull Location location)
  {
    int length = args.length;
    if (length == 1)
    {
      List<Completion> list1 = TabCompleterUtil.tabCompleterEntity(sender, args, "<개체>"), list2 = TabCompleterUtil.worldArgument(sender, args, "<월드>");
      return TabCompleterUtil.sortError(list1, list2);
    }
    if (length == 2)
    {
      return TabCompleterUtil.locationArgument(sender, args, "<위치>", location, false);
    }
    if (length == 3)
    {
      return TabCompleterUtil.rotationArgument(sender, args, "<방향>", location);
    }
    return Collections.singletonList(TabCompleterUtil.ARGS_LONG);
  }
}

