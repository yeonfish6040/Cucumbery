package com.jho5245.cucumbery.customrecipe.recipeinventory;

import com.jho5245.cucumbery.Cucumbery;
import com.jho5245.cucumbery.customrecipe.CustomRecipeUtil;
import com.jho5245.cucumbery.util.ItemSerializer;
import com.jho5245.cucumbery.util.MessageUtil;
import com.jho5245.cucumbery.util.Method;
import com.jho5245.cucumbery.util.nbt.CucumberyTag;
import com.jho5245.cucumbery.util.nbt.NBTAPI;
import com.jho5245.cucumbery.util.storage.ComponentUtil;
import com.jho5245.cucumbery.util.storage.CreateItemStack;
import com.jho5245.cucumbery.util.storage.ItemStackUtil;
import com.jho5245.cucumbery.util.storage.data.Constant;
import com.jho5245.cucumbery.util.storage.data.Variable;
import de.tr7zw.changeme.nbtapi.NBTCompound;
import de.tr7zw.changeme.nbtapi.NBTList;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

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
  public static void openRecipeInventory(Player player, int mainPage, String category, int categoryPage, String recipe, boolean firstOpen)
  {
    String openInventoryTitle = player.getOpenInventory().getTitle();
    YamlConfiguration config = Variable.customRecipes.get(category);
    if (config == null)
    {
      MessageUtil.sendError(player, "커스텀 레시피 목록 &e" + category + "&r" + MessageUtil.getFinalConsonant(category, MessageUtil.ConsonantType.을를) + " 찾을 수 없습니다. 현상이 지속적으로 발생한다면, 관리자에게 문의해주세요.");
      if (openInventoryTitle.startsWith(Constant.CANCEL_STRING))
      {
        player.closeInventory();
      }
      return;
    }
    ConfigurationSection recipeList = config.getConfigurationSection("recipes");
    if (recipeList == null || !recipeList.contains(recipe))
    {
      MessageUtil.sendError(player, "커스텀 레시피 목록 &e" + category + "&r에서 해당 커스텀 레시피(&e" + recipe + "&r)를 찾을 수 없습니다. 현상이 지속적으로 발생한다면, 관리자에게 문의해주세요.");
      if (openInventoryTitle.startsWith(Constant.CANCEL_STRING))
      {
        player.closeInventory();
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
        MessageUtil.sendWarn(player, "해당 레시피에 접근할 권한이 없어 레시피 선택 화면으로 돌아갑니다.");
      }
      else
      {
        MessageUtil.sendError(player, "해당 레시피에 접근할 권한이 없습니다.");
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
      ItemStack ingredient = ItemSerializer.deserialize(ingredientString);
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
    categoryDisplay = MessageUtil.n2s(categoryDisplay);

    String recipeDisplay = config.getString("recipes." + recipe + ".extra.display");
    if (recipeDisplay == null)
    {
      recipeDisplay = recipe;
    }
    recipeDisplay = MessageUtil.n2s(recipeDisplay);
    Inventory menu = Bukkit.createInventory(null, 54, Constant.CANCEL_STRING +
            Method.format("category:" + category, "§") +
            Constant.CUSTOM_RECIPE_CRAFTING_MENU +
            "§8" +
            categoryDisplay +
            " §1- §8" +
            recipeDisplay +
            Method.format("recipe:" + recipe, "§") +
            Method.format("mainpage:" + mainPage, "§") +
            Method.format("categorypage:" + categoryPage, "§"));
    if (firstOpen)
    {
      ItemStack deco1 = CreateItemStack.newItem(Material.WHITE_STAINED_GLASS_PANE, 1, "§와", false);
      ItemStack deco2 = CreateItemStack.newItem(Material.BROWN_STAINED_GLASS_PANE, 1, "§와", false);
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

      menu.setItem(45, CreateItemStack.newItem(Material.BIRCH_BOAT, 1, "&b메인 메뉴로", false));

      menu.setItem(53, CreateItemStack.newItem(Material.BIRCH_BOAT, 1, "§b레시피 목록 메뉴로", false));

      menu.setItem(51, CreateItemStack.newItem(Material.SPRUCE_BOAT, 1, "§b레시피 메뉴로", false));

      menu.setItem(47, CreateItemStack.newItem(Material.SPRUCE_BOAT, 1, "§b레시피 메뉴로", false));
      menu.setItem(49, CreateItemStack.newItem(Material.CLOCK, 1, "&e로딩중...", false));
      player.openInventory(menu);
    }
    else
    {
      menu = player.getOpenInventory().getTopInventory();
      for (int i = 9; i <= 35; i++)
      {
        menu.setItem(i, null);
      }
    }

    String nbtItemStack = config.getString("recipes." + recipe + ".result");
    boolean resultIsNull;
    ItemStack result = ItemSerializer.deserialize(nbtItemStack);
    resultIsNull = !ItemStackUtil.itemExists(result);
    if (resultIsNull)
    {
      MessageUtil.sendError(player, "레시피를 여는데 일시적인 오류가 발생했거나 올바르지 않은 레시피입니다. 현상이 지속적으로 발생한다면, 관리자에게 문의해주세요. (오류 코드 : Result)");
      RecipeInventoryCategory.openRecipeInventory(player, mainPage, category, categoryPage, true);
      return;
    }

    menu.setItem(4, result);

    ItemStack createButton = CreateItemStack.newItem(Material.BARRIER, 1, "§c[재료 부족]", false);
    ItemMeta createButtonMeta = createButton.getItemMeta();
    List<Component> createButtonLore = new ArrayList<>(Arrays.asList(ComponentUtil.create("§e가지고 있는 재료가 부족하여 아이템을 제작할 수 없습니다."), Component.empty()));
    List<String> description = config.getStringList("recipes." + recipe + ".extra.descriptions.crafting");
    if (description.size() > 0)
    {
      for (String str : description)
      {
        str = Method.parseCommandString(player, str);
        createButtonLore.add(ComponentUtil.create(str));
      }
      createButtonLore.add(Component.empty());
    }
    createButtonLore.add(ComponentUtil.create("§b[가지고 있는 재료 개수]"));
    boolean[] ingredientEnoughArray = new boolean[ingredients.size()];
    for (int i = 0; i < ingredients.size(); i++)
    {
      ItemStack ingredient = ingredients.get(i);
      ItemMeta itemMeta = ingredient.getItemMeta();
      List<Component> lore = null;
      if (ItemStackUtil.hasLore(ingredient))
      {
        lore = ingredient.getItemMeta().lore();
      }
      if (lore == null)
      {
        lore = new ArrayList<>();
      }
      int amount = ingredientAmounts.get(i);
      ingredient.setAmount(Math.min(64, amount));
      int playerAmount = ItemStackUtil.countItem(player.getInventory(), ingredient);
      if (playerAmount >= amount)
      {
        ingredientEnoughArray[i] = true;
      }
      String playerAmountColor = CustomRecipeUtil.getPercentColor(playerAmount * 1d / amount);
      lore.addAll(Arrays.asList(Component.empty(),
              ComponentUtil.create("§8--------------------"),
              ComponentUtil.create("rgb238,172,17;[가지고 있는 재료 개수]"),
              ComponentUtil.create(playerAmountColor + playerAmount + " &7/rgb0,255,84; " + amount)));
      NBTCompound itemTag = NBTAPI.getMainCompound(ingredient);
      NBTList<String> extraTags = NBTAPI.getStringList(itemTag, CucumberyTag.EXTRA_TAGS_KEY);

      boolean reusable = NBTAPI.arrayContainsValue(extraTags, Constant.ExtraTag.CUSTOM_RECIPE_REUSABLE.toString()) || config.getBoolean("recipes." + recipe + ".ingredients." + (i + 1) + ".reusable");
      if (reusable)
      {
        lore.addAll(Arrays.asList(Component.empty(),
                ComponentUtil.create("rgb153,217,234;[이 아이템은 제작 시 사라지지 않습니다.]")));
      }
      createButtonLore.add(ComponentUtil.create(ComponentUtil.itemName(ingredient, TextColor.color(255, 255, 255)), "&8 : " + playerAmountColor + playerAmount + " &7/rgb0,255,84; " + amount + (reusable ? " &8[∞]" : "")));
      itemMeta.lore(lore);
      ingredient.setItemMeta(itemMeta);
      menu.addItem(ingredient);

    }

    // 추가 조건 설명 추가
    List<Component> requirementsLore = new ArrayList<>(Arrays.asList(Component.empty(), ComponentUtil.create("rgb255,115,255;[추가 제작 조건]")));
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
          createButtonLore.set(0, ComponentUtil.create("§7아이템을 제작하는 중입니다."));
          menu.setItem(49, CreateItemStack.newItem2(Material.CLOCK, 1, "§e[제작중]", createButtonLore, false));
          if (Cucumbery.using_Vault)
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
                        ComponentUtil.create("§b클릭하면 §e" + Constant.Jeongsu.format(finalCost) + "원§b을 지불하여 아이템 제작 시간을 스킵할 수 있습니다.")));
                menu.setItem(49, CreateItemStack.newItem2(Material.ENDER_PEARL, 1, "§e[제작중]", createButtonLore, false));
              }
            }
          }
        }
        else
        {
          createButtonLore.set(0, ComponentUtil.create("§7클릭하여 아이템울 수령합니다."));
          menu.setItem(49, CreateItemStack.newItem2(Material.HOPPER, 1, "§a[수령하기]", createButtonLore, false));
        }
      }
    }

    // 모든 요구 조건 충족 확인
    if (!timeCraftingIgnoreOthers && Method.allIsTrue(requirements))
    {
      createButtonLore.set(0, ComponentUtil.create("§7클릭하여 아이템을 제작합니다."));
      menu.setItem(49, CreateItemStack.newItem2(Material.CRAFTING_TABLE, 1, "§a[제작하기]", createButtonLore, false));
    }
    else if (!timeCraftingIgnoreOthers)
    {
      createButtonLore.set(0, ComponentUtil.create("§e추가 제작 조건을 만족하지 않아 아이템을 제작할 수 없습니다."));
      menu.setItem(49, CreateItemStack.newItem2(Material.BARRIER, 1, "§c[제작 불가]", createButtonLore, false));
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
              Arrays.asList("§e인벤토리 공간이 부족하여 아이템을 " + (timeCraftingIgnoreOthers ? "수령" : "제작") + "할 수 없습니다.", MessageUtil.n2s("&e필요 인벤토리 공간 : " + playerSpaceColor + countSpace + " &7/rgb0,255,84; " + resultAmount)));
      menu.setItem(49, CreateItemStack.newItem(Material.BARRIER, 1, "§c[인벤토리 공간 부족]", notEnoughInvLore, false));
    }
    else if (!ingredientEnough && !timeCraftingIgnoreOthers)
    {
      menu.setItem(49, createButton);
    }
  }
}
