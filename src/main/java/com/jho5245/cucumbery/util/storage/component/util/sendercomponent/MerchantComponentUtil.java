package com.jho5245.cucumbery.util.storage.component.util.sendercomponent;

import com.jho5245.cucumbery.util.storage.no_groups.ItemStackUtil;
import com.jho5245.cucumbery.util.storage.component.ItemStackComponent;
import com.jho5245.cucumbery.util.storage.component.util.ComponentUtil;
import com.jho5245.cucumbery.util.storage.data.Constant;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Merchant;
import org.bukkit.inventory.MerchantRecipe;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class MerchantComponentUtil
{
  @NotNull
  public static Component hoverMerchantComponent(@NotNull Merchant merchant, @NotNull Component hover)
  {
    List<MerchantRecipe> recipes = merchant.getRecipes();
    if (!recipes.isEmpty())
    {
      hover = hover.append(Component.text("\n"));
      hover = hover.append(ComponentUtil.create(Constant.SEPARATOR));
      hover = hover.append(Component.text("\n"));
      hover = hover.append(ComponentUtil.translate("거래 목록 : %s개", recipes.size()));
      for (int i = 0; i < recipes.size(); i++)
      {
        hover = hover.append(Component.text("\n"));
        if (i == 20)
        {
          hover = hover.append(ComponentUtil.translate("&7&ocontainer.shulkerBox.more", Component.text(recipes.size() - 20)));
          break;
        }
        MerchantRecipe recipe = recipes.get(i);
        int use = recipe.getUses(), maxUse = recipe.getMaxUses();
        boolean unavailable = use >= maxUse;
        ItemStack result = recipe.getResult();
        List<ItemStack> ingredients = recipe.getIngredients();
        String recipeDisplay = "&8%s ➜ %s (%s / %s)";
        List<Component> recipeArgs = new ArrayList<>();
        StringBuilder ingredientDisplay = new StringBuilder();
        List<Component> ingredientArgs = new ArrayList<>();
        for (ItemStack ingredient : ingredients)
        {
          if (!ItemStackUtil.itemExists(ingredient))
          {
            continue;
          }
          ingredientDisplay.append("%s, ");
          ingredientArgs.add(ItemStackComponent.itemStackComponent(ingredient));
        }
        ingredientDisplay = new StringBuilder(ingredientDisplay.substring(0, ingredientDisplay.length() - 2));
        recipeArgs.add(ComponentUtil.translate(ingredientDisplay.toString()).args(ingredientArgs));
        recipeArgs.add(ItemStackComponent.itemStackComponent(result));
        recipeArgs.add(Component.text(use));
        recipeArgs.add(Component.text(maxUse));
        Component component = ComponentUtil.translate(recipeDisplay).args(recipeArgs);
        ItemMeta itemMeta = result.getItemMeta();
        if (itemMeta.hasEnchants())
        {
          StringBuilder enchantDisplay = new StringBuilder("&b ");
          List<Component> enchantArgs = new ArrayList<>();
          for (Enchantment enchantment : itemMeta.getEnchants().keySet())
          {
            int level = itemMeta.getEnchantLevel(enchantment);
            Component enchant = ComponentUtil.translate(enchantment.translationKey());
            if (level > 255)
            {
              level = 255;
            }
            if (level < 1)
            {
              level = 1;
            }
            if (level > 1 || enchantment.getMaxLevel() != 1)
            {
              enchant = ComponentUtil.translate("%s %s", enchant, level);
            }
            enchantArgs.add(enchant);
            enchantDisplay.append("%s, ");
          }
          enchantDisplay = new StringBuilder(enchantDisplay.substring(0, enchantDisplay.length() - 2));
          component = component.append(ComponentUtil.translate(enchantDisplay.toString()).args(enchantArgs));
        }
        if (itemMeta instanceof EnchantmentStorageMeta storageMeta)
        {
          StringBuilder enchantDisplay = new StringBuilder("&b ");
          List<Component> enchantArgs = new ArrayList<>();
          for (Enchantment enchantment : storageMeta.getStoredEnchants().keySet())
          {
            int level = storageMeta.getStoredEnchantLevel(enchantment);
            Component enchant = ComponentUtil.translate(enchantment.translationKey());
            if (level > 255)
            {
              level = 255;
            }
            if (level < 1)
            {
              level = 1;
            }
            if (level > 1 || enchantment.getMaxLevel() != 1)
            {
              enchant = ComponentUtil.translate("%s %s", enchant, level);
            }
            enchantArgs.add(enchant);
            enchantDisplay.append("%s, ");
          }
          enchantDisplay = new StringBuilder(enchantDisplay.substring(0, enchantDisplay.length() - 2));
          component = component.append(ComponentUtil.translate(enchantDisplay.toString()).args(enchantArgs));
        }
        if (unavailable)
        {
          component = component.color(NamedTextColor.RED);
        }
        hover = hover.append(component);
      }
      hover = hover.append(Component.text("\n"));
      hover = hover.append(ComponentUtil.create(Constant.SEPARATOR));
    }
    return hover;
  }
}
