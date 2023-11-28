package com.jho5245.cucumbery.custom.customrecipe.recipeinventory;

import com.jho5245.cucumbery.Cucumbery;
import com.jho5245.cucumbery.custom.customrecipe.CustomRecipeUtil;
import com.jho5245.cucumbery.util.itemlore.ItemLore;
import com.jho5245.cucumbery.util.itemlore.ItemLoreView;
import com.jho5245.cucumbery.util.nbt.CucumberyTag;
import com.jho5245.cucumbery.util.nbt.NBTAPI;
import com.jho5245.cucumbery.util.no_groups.ItemSerializer;
import com.jho5245.cucumbery.util.no_groups.MessageUtil;
import com.jho5245.cucumbery.util.no_groups.Method;
import com.jho5245.cucumbery.util.storage.component.util.ComponentUtil;
import com.jho5245.cucumbery.util.storage.component.util.ItemNameUtil;
import com.jho5245.cucumbery.util.storage.data.Constant;
import com.jho5245.cucumbery.util.storage.data.CustomMaterial;
import com.jho5245.cucumbery.util.storage.data.Variable;
import com.jho5245.cucumbery.util.storage.no_groups.CreateItemStack;
import com.jho5245.cucumbery.util.storage.no_groups.ItemStackUtil;
import de.tr7zw.changeme.nbtapi.NBTCompound;
import de.tr7zw.changeme.nbtapi.NBTItem;
import de.tr7zw.changeme.nbtapi.NBTList;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RecipeInventoryRecipe
{
  /**
   * 실제로 아이템을 제작하는 메뉴를 열어줍니다.
   *
   * @param player       메뉴를 열어줄 플레이어
   * @param mainPage     메인 페이지
   * @param category     레시피 목록 이름
   * @param categoryPage 이전 레시피 목록 페이지
   * @param recipe       레시피 이름
   * @param firstOpen    처음 메뉴를 여는가?
   */
  public static void openRecipeInventory(@NotNull final Player player, final int mainPage, final String category, final int categoryPage, final String recipe, final boolean firstOpen)
  {
    Bukkit.getScheduler().runTaskLaterAsynchronously(Cucumbery.getPlugin(), () ->
    {
      String openInventoryTitle = player.getOpenInventory().getTitle();
      YamlConfiguration config = Variable.customRecipes.get(category);
      if (config == null)
      {
        MessageUtil.sendError(player, "커스텀 레시피 목록 rg255,204;" + category + "&r" + MessageUtil.getFinalConsonant(category, MessageUtil.ConsonantType.을를) + " 찾을 수 없습니다. 현상이 지속적으로 발생한다면, 관리자에게 문의해주세요.");
        if (openInventoryTitle.startsWith(Constant.CANCEL_STRING))
        {
          Bukkit.getScheduler().runTaskLater(Cucumbery.getPlugin(), () ->
                  player.closeInventory(), 0L);
        }
        return;
      }
      ConfigurationSection recipeList = config.getConfigurationSection("recipes");
      if (recipeList == null || !recipeList.contains(recipe))
      {
        MessageUtil.sendError(player, "커스텀 레시피 목록 rg255,204;" + category + "&r에서 해당 커스텀 레시피(rg255,204;" + recipe + "&r)를 찾을 수 없습니다. 현상이 지속적으로 발생한다면, 관리자에게 문의해주세요.");
        if (openInventoryTitle.startsWith(Constant.CANCEL_STRING))
        {
          Bukkit.getScheduler().runTaskLater(Cucumbery.getPlugin(), () ->
                  player.closeInventory(), 0L);
        }
        return;
      }
      String permission = config.getString("recipes." + recipe + ".extra.permissions.base-permission");
      boolean hideIfNoBase = config.getBoolean("recipes." + recipe + ".extra.permissions.hide-if-no-base");
      String bypassIfHidden = config.getString("recipes." + recipe + ".extra.permissions.bypass-if-hidden-permission");
      if (permission != null && !player.hasPermission(permission) && hideIfNoBase && (bypassIfHidden == null || !player.hasPermission(bypassIfHidden)))
      {
        if (openInventoryTitle.contains(Constant.CANCEL_STRING))
        {
          RecipeInventoryCategory.openRecipeInventory(player, mainPage, category, categoryPage, true);
          MessageUtil.sendWarn(player, "해당 레시피에 접근할 권한이 없어 레시피 선택 화면으로 돌아갑니다");
        }
        else
        {
          MessageUtil.sendError(player, "해당 레시피에 접근할 권한이 없습니다");
        }
        return;
      }

      List<Integer> ingredientAmounts = new ArrayList<>();
      List<ItemStack> ingredients = new ArrayList<>();
      for (int i = 1; i <= 27; i++)
      {
        String ingredientString = config.getString("recipes." + recipe + ".ingredients." + i + ".item");
        if (ingredientString == null)
        {
          break;
        }
        ingredientAmounts.add(config.getInt("recipes." + recipe + ".ingredients." + i + ".amount"));
        ItemStack ingredient = ingredientString.startsWith("predicate:") ? ItemStackUtil.getItemStackPredicate(ingredientString.substring(10)) : ItemSerializer.deserialize(ingredientString);
        if (!ItemStackUtil.itemExists(ingredient))
        {
          MessageUtil.sendError(player, "레시피를 여는데 일시적인 오류가 발생했거나 올바르지 않은 레시피입니다. 현상이 지속적으로 발생한다면, 관리자에게 문의해주세요. (오류 코드 : Material)");
          return;
        }
        ingredients.add(ingredient);
      }

      String categoryDisplay = config.getString("extra.display");
      if (categoryDisplay == null)
      {
        categoryDisplay = category;
      }
      Component categoryDisplayComp = ComponentUtil.create(categoryDisplay).hoverEvent(null).clickEvent(null);
      if (categoryDisplayComp.color() == null)
      {
        categoryDisplayComp = categoryDisplayComp.color(NamedTextColor.DARK_GRAY);
      }
      categoryDisplay = ComponentUtil.serialize(categoryDisplayComp);

      String recipeDisplay = config.getString("recipes." + recipe + ".extra.display");
      if (recipeDisplay == null)
      {
        recipeDisplay = recipe;
      }
      Component recipeDisplayComp = ComponentUtil.create(recipeDisplay).hoverEvent(null).clickEvent(null);
      if (recipeDisplayComp.color() == null)
      {
        recipeDisplayComp = recipeDisplayComp.color(NamedTextColor.DARK_GRAY);
      }
      recipeDisplay = ComponentUtil.serialize(recipeDisplayComp);
      final String title = Constant.CANCEL_STRING +
              Method.format("category:" + category, "§") +
              Constant.CUSTOM_RECIPE_CRAFTING_MENU +
              "§8" +
              categoryDisplay +
              " §1- §8" +
              recipeDisplay +
              Method.format("recipe:" + recipe, "§") +
              Method.format("mainpage:" + mainPage, "§") +
              Method.format("categorypage:" + categoryPage, "§");
      Inventory menu = Bukkit.createInventory(null, 54, title);
      if (firstOpen)
      {
        ItemStack deco1 = CreateItemStack.create(Material.WHITE_STAINED_GLASS_PANE, 1, "§와", false);
        ItemStack deco2 = CreateItemStack.create(Material.BROWN_STAINED_GLASS_PANE, 1, "§와", false);
        menu.setItem(0, deco1);
        menu.setItem(1, deco1);
        menu.setItem(2, deco1);
        menu.setItem(3, deco1);
        menu.setItem(5, deco1);
        menu.setItem(6, deco1);
        menu.setItem(7, deco1);
        menu.setItem(8, deco1);

        menu.setItem(36, deco1);
        menu.setItem(37, deco1);
        menu.setItem(38, deco1);

        menu.setItem(39, deco2);
        menu.setItem(40, deco2);
        menu.setItem(41, deco2);
        menu.setItem(48, deco2);
        menu.setItem(50, deco2);

        menu.setItem(42, deco1);
        menu.setItem(43, deco1);
        menu.setItem(44, deco1);

        menu.setItem(46, deco1);
        menu.setItem(52, deco1);

        // button

        menu.setItem(45, CreateItemStack.create(Material.BIRCH_BOAT, 1, "§b레시피 목록 메뉴로", false));

        menu.setItem(53, CreateItemStack.create(Material.BIRCH_BOAT, 1, "§b레시피 목록 메뉴로", false));

        menu.setItem(51, CreateItemStack.create(Material.SPRUCE_BOAT, 1, "§b레시피 메뉴로", false));

        menu.setItem(47, CreateItemStack.create(Material.SPRUCE_BOAT, 1, "§b레시피 메뉴로", false));
        menu.setItem(49, CreateItemStack.create(Material.CLOCK, 1, "rg255,204;로딩중...", false));
        Inventory finalMenu = menu;
        Bukkit.getScheduler().runTaskLater(Cucumbery.getPlugin(), () ->
                player.openInventory(finalMenu), 0L);

      }
      else
      {
        menu = player.getOpenInventory().getTopInventory();
      }

      String nbtItemStack = config.getString("recipes." + recipe + ".result");
      ItemStack result = ItemSerializer.deserialize(nbtItemStack);
      boolean resultIsNull = !ItemStackUtil.itemExists(result);
      if (resultIsNull)
      {
        MessageUtil.sendError(player, "레시피를 여는데 일시적인 오류가 발생했거나 올바르지 않은 레시피입니다. 현상이 지속적으로 발생한다면, 관리자에게 문의해주세요. (오류 코드 : Result)");
        RecipeInventoryCategory.openRecipeInventory(player, mainPage, category, categoryPage, true);
        return;
      }
      NBTItem resultNBTItem = new NBTItem(result);
      NBTCompound merge = resultNBTItem.getCompound("MergeNBT");
      boolean isMerge = merge != null;
      if (merge != null)
      {
        ItemStack ingredient = ingredients.get(0).clone();
        String ingredientString = config.getString("recipes." + recipe + ".ingredients.1.item") + "";
        boolean isPredicate = ingredientString.startsWith("predicate:");
        if (isPredicate)
        {
          for (int j = 0; j < player.getInventory().getSize(); j++)
          {
            ItemStack invItem = player.getInventory().getItem(j);
            if (invItem == null || invItem.getType().isAir())
            {
              continue;
            }
            if (ItemStackUtil.predicateItem(invItem, ingredientString.substring("predicate:".length())))
            {
              ingredient.setItemMeta(invItem.getItemMeta());
              break;
            }
          }
        }
        NBTItem ingredientNBTItem = new NBTItem(ingredient, true);
        ingredientNBTItem.mergeCompound(merge);
        ItemLore.setItemLore(ingredient, ItemLoreView.of(player));
        ingredientNBTItem = new NBTItem(ingredient, true);
        Long bonusDurability = resultNBTItem.getLong("BonusDurability");
        if (bonusDurability != null)
        {
          NBTCompound itemTag = ingredientNBTItem.getCompound(CucumberyTag.KEY_MAIN);
          if (itemTag != null)
          {
            NBTCompound duraTag = itemTag.getCompound(CucumberyTag.CUSTOM_DURABILITY_KEY);
            if (duraTag != null && duraTag.hasTag(CucumberyTag.CUSTOM_DURABILITY_CURRENT_KEY))
            {
              long cur = duraTag.getLong(CucumberyTag.CUSTOM_DURABILITY_CURRENT_KEY) - bonusDurability;
              duraTag.setLong(CucumberyTag.CUSTOM_DURABILITY_CURRENT_KEY, Math.max(cur, 0));
            }
          }
        }
        String setType = resultNBTItem.getString("SetType");
        if (!setType.equals(""))
        {
          try
          {
            Material material = Material.valueOf(setType);
            result.setType(material);
          }
          catch (Exception ignored)
          {

          }
        }
        result.setItemMeta(ingredient.getItemMeta());
      }
      ItemLore.setItemLore(result, ItemLoreView.of(player));
      try
      {
        menu.setItem(4, result);
      }
      catch (Exception e)
      {
        MessageUtil.sendWarn(player, "일시적인 오류가 발생하여 이전 메뉴로 돌아갑니다");
        RecipeInventoryCategory.openRecipeInventory(player, mainPage, category, categoryPage, true);
        return;
      }

      ItemStack createButton = CreateItemStack.create(Material.BARRIER, 1, "§c[재료 부족]", false);
      ItemMeta createButtonMeta = createButton.getItemMeta();
      List<Component> createButtonLore = new ArrayList<>(Arrays.asList(ComponentUtil.create("§e가지고 있는 재료가 부족하여 아이템을 제작할 수 없습니다"), Component.empty()));
      List<String> description = config.getStringList("recipes." + recipe + ".extra.descriptions.crafting");
      List<Component> descriptionComponent = new ArrayList<>();
      description.forEach(s -> descriptionComponent.add(ComponentUtil.create(Method.parseCommandString(player, s))));
      if (isMerge)
      {
        descriptionComponent.add(ComponentUtil.translate("&7아이템 제작 시 첫 번째 재료(%s)의", ingredients.get(0)));
        descriptionComponent.add(ComponentUtil.translate("&7속성이 제작 결과물에 반영됩니다. (인챈트, 내구도 등)"));
      }
      if (!descriptionComponent.isEmpty())
      {
        createButtonLore.addAll(descriptionComponent);
        createButtonLore.add(Component.empty());
      }

      createButtonLore.add(ComponentUtil.translate("&b[가지고 있는 재료 개수]"));
      boolean[] ingredientEnoughArray = new boolean[ingredients.size()];
      ItemStack[] menuItems = new ItemStack[27];
      for (int i = 0; i < ingredients.size(); i++)
      {
        ItemStack ingredient = ingredients.get(i);
        ItemLore.setItemLore(ingredient, ItemLoreView.of(player));
        ItemMeta itemMeta = ingredient.getItemMeta();
        List<Component> lore = itemMeta.lore();
        int amount = ingredientAmounts.get(i);
        ingredient.setAmount(Math.min(64, amount));
        String ingredientString = config.getString("recipes." + recipe + ".ingredients." + (i + 1) + ".item");
        int playerAmount = ItemStackUtil.countItem(player.getInventory(), ingredient);
        Component display = ItemNameUtil.itemName(ingredient, NamedTextColor.WHITE);
        if (ingredientString != null && ingredientString.startsWith("predicate:"))
        {
          Material type = ingredient.getType();
          if (!type.isItem() || type == Material.AIR)
          {
            ingredient.setType(Material.ENDER_PEARL);
            itemMeta.displayName(ItemNameUtil.itemName(type));
          }
          else
          {
            itemMeta.displayName(null);
            ingredient.setItemMeta(itemMeta);
            ItemLore.setItemLore(ingredient, ItemLoreView.of(player));
            itemMeta = ingredient.getItemMeta();
            lore = itemMeta.lore();
          }
          if (ingredientString.startsWith("predicate:{id:"))
          {
            String id = ingredientString.substring("predicate:{id:".length(), ingredientString.length() - 1);
            try
            {
              CustomMaterial customMaterial = CustomMaterial.valueOf(id);
              ItemStack create = customMaterial.create();
              ItemLore.setItemLore(create, ItemLoreView.of(player));
              ingredient.setItemMeta(create.getItemMeta());
              itemMeta = ingredient.getItemMeta();
              lore = itemMeta.lore();
            }
            catch (Exception e)
            {
              ItemStack itemStack = new ItemStack(Material.STONE);
              NBTItem nbtItem = new NBTItem(itemStack, true);
              nbtItem.setString("id", id);
              ItemLore.setItemLore(itemStack, ItemLoreView.of(player));
              ingredient.setItemMeta(itemStack.getItemMeta());
              itemMeta = ingredient.getItemMeta();
              lore = itemMeta.lore();
            }
          }
          playerAmount = ItemStackUtil.countItem(player.getInventory(), ingredientString.substring(10));
        }
        if (playerAmount >= amount)
        {
          ingredientEnoughArray[i] = true;
        }
        if (lore == null)
        {
          lore = new ArrayList<>();
        }
        String playerAmountColor = CustomRecipeUtil.getPercentColor(playerAmount * 1d / amount);
        lore.addAll(Arrays.asList(ComponentUtil.create("&8" + Constant.SEPARATOR),
                ComponentUtil.translate("rgb238,172,17;[가지고 있는 재료 개수]"),
                ComponentUtil.translate("&7%s / %s", playerAmountColor + playerAmount, "rgb0,255,84;" + amount)));
        NBTCompound itemTag = NBTAPI.getMainCompound(ingredient);
        NBTList<String> extraTags = NBTAPI.getStringList(itemTag, CucumberyTag.EXTRA_TAGS_KEY);

        boolean reusable = NBTAPI.arrayContainsValue(extraTags, Constant.ExtraTag.CUSTOM_RECIPE_REUSABLE.toString()) || config.getBoolean("recipes." + recipe + ".ingredients." + (i + 1) + ".reusable");
        if (reusable)
        {
          lore.addAll(Arrays.asList(Component.empty(),
                  ComponentUtil.create("rgb153,217,234;[이 아이템은 제작 시 사라지지 않습니다]")));
        }
        createButtonLore.add(ComponentUtil.create(display, "&8 : " + playerAmountColor + playerAmount + " &7/rgb0,255,84; " + amount + (reusable ? " &8[∞]" : "")));
        itemMeta.lore(lore);
        ingredient.setItemMeta(itemMeta);
        menuItems[i] = ingredient;
      }
      String invName = menu.getViewers().isEmpty() ? "" : menu.getViewers().get(0).getOpenInventory().getTitle();

      // for asynchronus issue
      if (!invName.equals(title))
      {
        return;
      }
      for (int i = 0; i < menuItems.length; i++)
      {
        menu.setItem(i + 9, menuItems[i]);
      }

      // 추가 조건 설명 추가
      List<Component> requirementsLore = new ArrayList<>(Arrays.asList(Component.empty(), ComponentUtil.translate("rgb255,115,255;[추가 제작 조건]")));
      // 모든 요구 조건을 만족하여 아이템을 제적할 수 있는가?
      boolean[] requirements = new boolean[CustomRecipeUtil.REQUIREMENT_AMOUNT];

      Arrays.fill(requirements, true);

      CustomRecipeUtil.createRequirementLore(requirementsLore, player, config, category, recipe, requirements);

      if (requirementsLore.size() > 2)
      {
        createButtonLore.addAll(requirementsLore);
      }
      createButtonMeta.lore(createButtonLore);
      createButton.setItemMeta(createButtonMeta);

      // 제작하는데 시간이 걸리는 레시피는 추가 조건, 레시피 조건 무시
      boolean timeCraftingIgnoreOthers = false;

      long requireTimeToCraft = config.getLong("recipes." + recipe + ".extra.crafting-time");
      if (requireTimeToCraft > 0)
      {
        YamlConfiguration playerCraftingTimeConfig = Variable.craftingTime.get(player.getUniqueId());
        long playerCraftingTime = playerCraftingTimeConfig == null ? 0 : playerCraftingTimeConfig.getLong("crafting-time." + category + "." + recipe);
        if (playerCraftingTime > 0)
        {
          timeCraftingIgnoreOthers = true;
          long currentTime = System.currentTimeMillis();
          if (currentTime <= playerCraftingTime)
          {
            createButtonLore.set(0, ComponentUtil.create("§7아이템을 제작하는 중입니다"));
            menu.setItem(49, CreateItemStack.create(Material.CLOCK, 1, "§e[제작중]", createButtonLore, false));
            if (Cucumbery.using_Vault_Economy)
            {
              double skipCost = config.getDouble("recipes." + recipe + ".extra.crafting-time-skip.cost");
              if (skipCost > 0)
              {
                boolean timeRelative = config.getBoolean("recipes." + recipe + ".extra.crafting-time-skip.time-relative");
                double finalCost = timeRelative ? skipCost * (playerCraftingTime - currentTime) / requireTimeToCraft : skipCost;
                double playerMoney = Cucumbery.eco.getBalance(player);
                String skipPermission = config.getString("recipes." + recipe + ".extra.crafting-time-skip.permission");
                if (playerMoney >= finalCost && (skipPermission == null || player.hasPermission(skipPermission)))
                {
                  createButtonLore.addAll(Arrays.asList(Component.empty(),
                          ComponentUtil.create("§b클릭하면 §e" + Constant.Jeongsu.format(finalCost) + "원§b을 지불하여 아이템 제작 시간을 스킵할 수 있습니다")));
                  menu.setItem(49, CreateItemStack.create(Material.ENDER_PEARL, 1, "§e[제작중]", createButtonLore, false));
                }
              }
            }
          }
          else
          {
            createButtonLore.set(0, ComponentUtil.create("§7클릭하여 아이템울 수령합니다"));
            menu.setItem(49, CreateItemStack.create(Material.HOPPER, 1, "§a[수령하기]", createButtonLore, false));
          }
        }
      }

      // 모든 요구 조건 충족 확인
      if (!timeCraftingIgnoreOthers && Method.allIsTrue(requirements))
      {
        createButtonLore.set(0, ComponentUtil.create("§7클릭하여 아이템을 제작합니다"));
        menu.setItem(49, CreateItemStack.create(Material.CRAFTING_TABLE, 1, "§a[제작하기]", createButtonLore, false));
      }
      else if (!timeCraftingIgnoreOthers)
      {
        createButtonLore.set(0, ComponentUtil.create("§e추가 제작 조건을 만족하지 않아 아이템을 제작할 수 없습니다"));
        menu.setItem(49, CreateItemStack.create(Material.BARRIER, 1, "§c[제작 불가]", createButtonLore, false));
      }

      // 인벤토리 부족 또는 재료 부족

      boolean ingredientEnough = Method.allIsTrue(ingredientEnoughArray);

      int countSpace = ItemStackUtil.countSpace(player.getInventory(), result);
      int resultAmount = result.getAmount();
      boolean playerHasEnoughSpace = countSpace >= resultAmount;
      if (player.getInventory().firstEmpty() == -1 && !playerHasEnoughSpace)
      {
        String playerSpaceColor = CustomRecipeUtil.getPercentColor(countSpace * 1d / resultAmount);
        List<String> notEnoughInvLore = new ArrayList<>(
                Arrays.asList("§e인벤토리 공간이 부족하여 아이템을 " + (timeCraftingIgnoreOthers ? "수령" : "제작") + "할 수 없습니다", MessageUtil.n2s("rg255,204;필요 인벤토리 공간 : " + playerSpaceColor + countSpace + " &7/rgb0,255,84; " + resultAmount)));
        menu.setItem(49, CreateItemStack.create(Material.BARRIER, 1, "§c[인벤토리 공간 부족]", notEnoughInvLore, false));
      }
      else if (!ingredientEnough && !timeCraftingIgnoreOthers)
      {
        menu.setItem(49, createButton);
      }
    }, 0L);
  }
}
