package com.jho5245.cucumbery.commands.no_groups;

import com.jho5245.cucumbery.custom.custommerchant.MerchantData;
import com.jho5245.cucumbery.custom.custommerchant.MerchantManager;
import com.jho5245.cucumbery.util.no_groups.MessageUtil;
import com.jho5245.cucumbery.util.no_groups.Method;
import com.jho5245.cucumbery.util.no_groups.SelectorUtil;
import com.jho5245.cucumbery.util.storage.no_groups.CustomConfig;
import com.jho5245.cucumbery.util.storage.no_groups.ItemStackUtil;
import com.jho5245.cucumbery.util.storage.component.ItemStackComponent;
import com.jho5245.cucumbery.util.storage.component.util.ComponentUtil;
import com.jho5245.cucumbery.util.storage.data.Constant;
import com.jho5245.cucumbery.util.storage.data.Permission;
import com.jho5245.cucumbery.util.storage.data.Prefix;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import org.bukkit.Bukkit;
import org.bukkit.command.*;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Merchant;
import org.bukkit.inventory.MerchantRecipe;
import org.bukkit.inventory.PlayerInventory;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class CommandCustomMerchant implements CommandExecutor, TabCompleter
{
  public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, String[] args)
  {
    if (!Method.hasPermission(sender, Permission.CMD_CUSTOM_MERCHANT_ADMIN, true))
    {
      return !(sender instanceof BlockCommandSender);
    }
    if (!MessageUtil.checkQuoteIsValidInArgs(sender, args = MessageUtil.wrapWithQuote(args)))
    {
      return !(sender instanceof BlockCommandSender);
    }
    if (args.length == 0)
    {
      MessageUtil.sendError(sender, Prefix.ARGS_SHORT);
      MessageUtil.commandInfo(sender, label, Method.getUsage(cmd));
      return !(sender instanceof BlockCommandSender);
    }
    if (args.length == 1 && args[0].equals("list"))
    {
      HashMap<String, MerchantData> merchants = MerchantData.merchantDataHashMap;
      if (merchants.isEmpty())
      {
        MessageUtil.sendMessage(sender, Prefix.INFO_CUSTOM_MERCHANT, ComponentUtil.translate("유효한 상점이 없습니다"));
        return true;
      }
      MessageUtil.sendMessage(sender, Prefix.INFO_CUSTOM_MERCHANT, ComponentUtil.translate("상점 개수 : %s개", merchants.size()));
      List<Component> arguments = new ArrayList<>();
      StringBuilder key = new StringBuilder("&7");
      for (String id : merchants.keySet())
      {
        key.append("%s, ");
        Component argument = Component.text(id).color(Constant.THE_COLOR);
        MerchantData merchantData = merchants.get(id);
        List<MerchantRecipe> merchantRecipes = MerchantManager.getRecipes(merchantData.getConfiguration());
        String display = merchantData.getDisplay();
        Component hover = Component.text(id);
        if (!display.equals(id))
        {
          hover = hover.append(Component.text("\n"));
          hover = hover.append(ComponentUtil.translate("이름 : %s", ComponentUtil.create(display)));
        }
        hover = hover.append(Component.text("\n"));
        hover = hover.append(ComponentUtil.translate("레시피 개수 : %s개", merchantRecipes.size()));
        for (int i = 0; i < merchantRecipes.size(); i++)
        {
          if (i + 1 != merchantRecipes.size())
          {
            hover = hover.append(Component.text("\n"));
          }
          if (i == 20)
          {
            hover = hover.append(ComponentUtil.translate("&7&ocontainer.shulkerBox.more", Component.text(merchantRecipes.size() - 20)));
            break;
          }
          MerchantRecipe merchantRecipe = merchantRecipes.get(i);
          ItemStack result = merchantRecipe.getResult();
          List<ItemStack> ingredients = merchantRecipe.getIngredients();
          StringBuilder recipeKey = new StringBuilder("&7");
          List<Component> ingredientsComponent = new ArrayList<>();
          for (ItemStack ingredient : ingredients)
          {
            recipeKey.append("%s, ");
            ingredientsComponent.add(ItemStackComponent.itemStackComponent(ingredient, Constant.THE_COLOR));
          }
          recipeKey = new StringBuilder(recipeKey.substring(0, recipeKey.length() - 2));
          hover = hover.append(ComponentUtil.translate("[%s] 재료 : %s", ItemStackComponent.itemStackComponent(result, Constant.THE_COLOR), ComponentUtil.translate(recipeKey.toString()).args(ingredientsComponent)));
        }
        arguments.add(argument.hoverEvent(hover).clickEvent(ClickEvent.suggestCommand("/cmerchant open " + id)));
      }
      key = new StringBuilder(key.substring(0, key.length() - 2));
      MessageUtil.sendMessage(sender, Prefix.INFO_CUSTOM_MERCHANT, ComponentUtil.translate(key.toString(), arguments));
      return true;
    }
    else
    {
      if (args.length >= 2)
      {
        switch (args[0])
        {
          case "create" -> {
            YamlConfiguration configuration = CustomConfig.getCustomConfig("data/CustomMerchants/" + args[1] + ".yml").getConfig();
            new MerchantData(args[1], args.length == 3 ? args[2] : null, configuration);
          }
          case "remove" -> {
//
          }
          case "edit" -> {
            String id = args[1];
            boolean remove = args.length == 3 && args[2].equals("remove");
            CustomConfig customConfig = CustomConfig.getCustomConfig("data/CustomMerchants/" + id + ".yml");
            if (remove)
            {
              customConfig.delete();
            }
            if (!(sender instanceof Player player))
            {
              return !(sender instanceof BlockCommandSender);
            }
            MerchantData merchantData = MerchantData.merchantDataHashMap.get(args[1]);
            if (merchantData != null)
            {
              List<MerchantRecipe> merchantRecipes = MerchantManager.getRecipes(customConfig.getConfig());
              PlayerInventory inventory = player.getInventory();
              ItemStack item1 = inventory.getItem(0);
              ItemStack item2 = inventory.getItem(1);
              ItemStack item3 = inventory.getItem(2);
              if (ItemStackUtil.itemExists(item1))
              {
                MerchantRecipe recipe = new MerchantRecipe(item1, 0, Integer.MAX_VALUE, false, 0, 0, true);
                List<ItemStack> ingredients = new ArrayList<>();
                if (ItemStackUtil.itemExists(item2))
                {
                  ingredients.add(item2);
                }
                if (ItemStackUtil.itemExists(item3))
                {
                  ingredients.add(item3);
                }
                recipe.setIngredients(ingredients);
                merchantRecipes.add(recipe);
              }
              MerchantManager.setRecipes(customConfig, merchantRecipes);
              merchantData.setMerchantRecipes(customConfig.getConfig());
            }
            MerchantData.merchantDataHashMap.put(id, merchantData);
          }
          case "open" -> {
            List<Player> targets = null;
            if (args.length == 2 && sender instanceof Player player)
            {
              targets = List.of(player);
            }
            else if (args.length >= 3)
            {
              targets = SelectorUtil.getPlayers(sender, args[2]);
            }
            if (targets == null)
            {
              return !(sender instanceof BlockCommandSender);
            }
            MerchantData merchantData = MerchantData.merchantDataHashMap.get(args[1]);
            if (merchantData != null)
            {
              List<MerchantRecipe> merchantRecipes = MerchantManager.getRecipes(merchantData.getConfiguration());
              String display = merchantData.getDisplay();
              Merchant merchant = Bukkit.createMerchant(ComponentUtil.create(display));
              merchant.setRecipes(merchantRecipes);
              for (Player target : targets)
              {
                target.openMerchant(merchant, true);
              }
            }
          }
        }
      }
    }
    return !(sender instanceof BlockCommandSender);
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
      return Method.tabCompleterList(args, "<인수>", "create", "remove", "list", "edit");
    }
    else if (length == 2)
    {
      Set<String> keys = MerchantData.merchantDataHashMap.keySet();
      switch (args[0])
      {
        case "create" -> {
          if (keys.contains(args[1]))
          {
            return Collections.singletonList("해당 id(" + args[1] + ")는 이미 존재합니다. /" + label + " edit " + args[1] + " 명령어를 사용해주세요.");
          }
          return Method.tabCompleterList(args, keys, "<id>", true);
        }
        case "remove", "edit" -> {
          return Method.tabCompleterList(args, keys, "<id>");
        }
      }
    }

    return Collections.singletonList(Prefix.ARGS_LONG.toString());
  }
}
