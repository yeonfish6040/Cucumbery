package com.jho5245.cucumbery.customrecipe.recipeinventory;

import com.jho5245.cucumbery.Cucumbery;
import com.jho5245.cucumbery.customrecipe.CustomRecipeUtil;
import com.jho5245.cucumbery.util.ItemSerializer;
import com.jho5245.cucumbery.util.MessageUtil;
import com.jho5245.cucumbery.util.Method;
import com.jho5245.cucumbery.util.nbt.CucumberyTag;
import com.jho5245.cucumbery.util.nbt.NBTAPI;
import com.jho5245.cucumbery.util.storage.CreateItemStack;
import com.jho5245.cucumbery.util.storage.ItemStackUtil;
import com.jho5245.cucumbery.util.storage.component.util.ComponentUtil;
import com.jho5245.cucumbery.util.storage.component.util.ItemNameUtil;
import com.jho5245.cucumbery.util.storage.data.Constant;
import com.jho5245.cucumbery.util.storage.data.Variable;
import de.tr7zw.changeme.nbtapi.NBTCompound;
import de.tr7zw.changeme.nbtapi.NBTItem;
import de.tr7zw.changeme.nbtapi.NBTList;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.format.TextDecoration.State;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;

import java.util.*;

public class RecipeInventoryCategory
{
  /**
   * 레시피 목록에 있는 레시피 목록이 있는 메뉴를 열어줍니다.
   *
   * @param player   메뉴를 열어줄 플레이어
   * @param mainPage 메인페이지
   * @param category 레시피 목록 이름
   * @param page     메뉴 페이지
   */
  public static void openRecipeInventory(Player player, int mainPage, String category, int page, boolean firstOpen)
  {
    String openInventoryTitle = player.getOpenInventory().getTitle();
    YamlConfiguration config = Variable.customRecipes.get(category);
    boolean exists = config != null;
    ConfigurationSection recipeList = null;
    if (exists)
    {
      recipeList = config.getConfigurationSection("recipes");
    }
    exists = exists && recipeList != null && !recipeList.getKeys(false).isEmpty();
    if (!exists)
    {
      MessageUtil.sendError(player, "커스텀 레시피 목록 &e" + category + "&r에는 레시피가 1개도 없습니다. 현상이 지속적으로 발생한다면, 관리자에게 문의해주세요.");
      if (openInventoryTitle.startsWith(Constant.CANCEL_STRING))
      {
        player.closeInventory();
      }
      return;
    }

    // 1초 마다 업데이트 되었을 때 해당 카테고리를 볼 권한이 없으면 닫아버리기.

    boolean[] requirementsCheck = new boolean[CustomRecipeUtil.REQUIREMENT_AMOUNT];

    Arrays.fill(requirementsCheck, true);

    CustomRecipeUtil.createRequirementLore(null, player, config, category, null, requirementsCheck);

    if (!Method.allIsTrue(requirementsCheck))
    {
      if (openInventoryTitle.contains(Constant.CANCEL_STRING))
      {
        RecipeInventoryMainMenu.openRecipeInventory(player, page, true);
      }
      MessageUtil.sendWarn(player, "해당 레시피 목록에 접근할 권한이 없어 레시피 목록 선택 화면으로 돌아갑니다");
      return;
    }

    int recipeAmount = recipeList.getKeys(false).size();
    // 페이지 당 최대 레시피 배치 개수
    int maxRecipesPerPage = 21;
    int maxPage = recipeAmount / maxRecipesPerPage + 1;
    if (recipeAmount % maxRecipesPerPage == 0)
    {
      maxPage--;
    }
    if (page > maxPage)
    {
      page = 1;
    }
    if (page < 1)
    {
      page = maxPage;
    }

    String categoryDisplay = config.getString("extra.display");
    if (categoryDisplay == null)
    {
      categoryDisplay = category;
    }
    Component categoryDisplayComp = ComponentUtil.create(categoryDisplay).hoverEvent(null).clickEvent(null);
    if (categoryDisplayComp.color() == null)
    {
      categoryDisplayComp = categoryDisplayComp.color(NamedTextColor.DARK_GRAY).decoration(TextDecoration.ITALIC, State.FALSE);
    }
    categoryDisplay = ComponentUtil.serialize(categoryDisplayComp);
    Inventory menu = Bukkit.createInventory(
            null, 45, Constant.CANCEL_STRING +
                    Constant.CUSTOM_RECIPE_MENU +
                    MessageUtil.n2s(categoryDisplay) +
                    (maxPage == 1 ? "" : (" §3[" + page + "/" + maxPage + "]")) +
                    Method.format("page:" + page, "§") +
                    Method.format("category:" + category, "§") +
                    Method.format("mainpage:" + mainPage, "§"));
    if (firstOpen)
    {
      // deco template

      ItemStack deco1 = CreateItemStack.newItem(Material.WHITE_STAINED_GLASS_PANE, 1, "§와", false);

      menu.setItem(0, deco1);
      menu.setItem(1, deco1);
      menu.setItem(2, deco1);
      menu.setItem(3, deco1);
      menu.setItem(4, deco1);
      menu.setItem(5, deco1);
      menu.setItem(6, deco1);
      menu.setItem(7, deco1);
      menu.setItem(8, deco1);

      menu.setItem(9, deco1);
      menu.setItem(17, deco1);

      menu.setItem(18, deco1);
      menu.setItem(26, deco1);

      menu.setItem(27, deco1);
      menu.setItem(35, deco1);

      menu.setItem(37, deco1);
      menu.setItem(38, deco1);
      menu.setItem(42, deco1);
      menu.setItem(43, deco1);
      // buttons

      menu.setItem(36, CreateItemStack.newItem(Material.BIRCH_BOAT, 1, "§b메인 메뉴로", false));

      if (maxPage == 1)
      {
        menu.setItem(39, deco1);
        menu.setItem(41, deco1);
      }
      else
      {
        menu.setItem(39, CreateItemStack.newItem(Material.SPRUCE_BOAT, Math.max(1, page == 1 ? maxPage : page - 1), page == 1 ? "§e마지막 페이지로" : "§e이전 페이지로", "§a현재 페이지 : " + page + " / " + maxPage, false));
        menu.setItem(
                41, CreateItemStack.newItem(Material.SPRUCE_BOAT, Math.min(maxPage, page == maxPage ? 1 : page + 1), page == maxPage ? "§b처음 페이지로" : "§b다음 페이지로", "§a현재 페이지 : " + page + " / " + maxPage, false));
      }

      menu.setItem(44, CreateItemStack.newItem(Material.BIRCH_BOAT, 1, "§b레시피 목록 메뉴로", false));
      menu.setItem(40, CreateItemStack.newItem(Material.CLOCK, 1, "&e로딩중...", false));
      player.openInventory(menu);
    }
    else
    {
      menu = player.getOpenInventory().getTopInventory();
      for (int i = 10; i <= 16; i++)
      {
        menu.setItem(i, null);
      }
      for (int i = 19; i <= 25; i++)
      {
        menu.setItem(i, null);
      }
      for (int i = 28; i <= 34; i++)
      {
        menu.setItem(i, null);
      }
    }
    List<String> recipes = new ArrayList<>(recipeList.getKeys(false));
    for (int i = (page - 1) * maxRecipesPerPage; i < page * maxRecipesPerPage; i++)
    {
      if (i >= recipes.size())
      {
        break;
      }
      String recipe = recipes.get(i);
      ItemStack result = ItemSerializer.deserialize(config.getString("recipes." + recipe + ".result"));
      if (!ItemStackUtil.itemExists(result))
      {
        result = CreateItemStack.newItem(Material.BAKED_POTATO, 1, "&e알 수 없는 아이템", "&c&o손상된 레시피", true);
      }
      NBTItem nbtItem = new NBTItem(result);
      nbtItem.setString("recipe", recipe);
      result = nbtItem.getItem();
      ItemMeta resultMeta = result.getItemMeta();
      resultMeta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
      PersistentDataContainer dataContainer = resultMeta.getPersistentDataContainer();
      Set<NamespacedKey> dataKeys = dataContainer.getKeys();
      if (!dataKeys.isEmpty())
      {
        for (NamespacedKey dataKey : dataKeys)
        {
          dataContainer.remove(dataKey);
        }
      }
      List<Component> resultLore = resultMeta.lore();
      if (resultLore == null)
      {
        resultLore = new ArrayList<>();
      }
      String display = config.getString("recipes." + recipe + ".extra.display");
      if (display == null)
      {
        display = recipe;
      }
      Component displayComp = ComponentUtil.create(display).hoverEvent(null).clickEvent(null);
      if (displayComp.color() == null)
      {
        displayComp = displayComp.color(TextColor.color(93, 244, 255)).decoration(TextDecoration.ITALIC, State.FALSE);
      }
      resultLore.addAll(
              Arrays.asList(ComponentUtil.create(Constant.SEPARATOR),
                      ComponentUtil.translate("gb210,255;레시피 이름 : %s", displayComp),
                      Component.empty()));

      List<String> description = config.getStringList("recipes." + recipe + ".extra.descriptions.preview");
      if (!description.isEmpty())
      {
        for (String str : description)
        {
          str = Method.parseCommandString(player, str);
          resultLore.add(ComponentUtil.create(str));
        }
        resultLore.add(Component.empty());
      }

      resultLore.add(ComponentUtil.create("rgb238,172,17;[가지고 있는 재료 개수]"));
      List<Integer> ingredientAmounts = new ArrayList<>();
      List<ItemStack> ingredients = new ArrayList<>();
      for (int j = 1; j <= 27; j++)
      {
        String ingredientString = config.getString("recipes." + recipe + ".ingredients." + j + ".item");
        if (ingredientString == null)
        {
          break;
        }
        ingredientAmounts.add(config.getInt("recipes." + recipe + ".ingredients." + j + ".amount"));
        ItemStack ingredient = ItemSerializer.deserialize(ingredientString);
        if (!ItemStackUtil.itemExists(ingredient) && !ingredientString.startsWith("predicate:"))
        {
          result = CreateItemStack.newItem(Material.MUSIC_DISC_11, 1, "&e알 수 없는 아이템", "&c&o손상된 레시피", true);
          break;
        }
        ingredients.add(ingredient);
      }
      boolean[] ingredientEnoughArray = new boolean[ingredients.size()];
      for (int j = 1; j <= 27; j++)
      {
        String ingredientString = config.getString("recipes." + recipe + ".ingredients." + j + ".item");
        if (ingredientString == null)
        {
          break;
        }
        ingredientAmounts.add(config.getInt("recipes." + recipe + ".ingredients." + j + ".amount"));
        boolean isPredicate = ingredientString.startsWith("predicate:");
        ItemStack ingredient = ItemSerializer.deserialize(ingredientString);
        int playerAmount = ItemStackUtil.countItem(player.getInventory(), ingredient);
        if (isPredicate)
        {
          ingredientString = ingredientString.substring(10);
          ingredient = ItemStackUtil.getItemStackPredicate(ingredientString);
          playerAmount = ItemStackUtil.countItem(player.getInventory(), ingredientString);
        }
        int amount = ingredientAmounts.get(j - 1);
        Component itemName = ItemNameUtil.itemName(ingredient, TextColor.color(255, 255, 255));

        if (playerAmount >= amount)
        {
          ingredientEnoughArray[j - 1] = true;
        }
        String playerAmountColor = CustomRecipeUtil.getPercentColor(playerAmount * 1d / amount);
        NBTCompound itemTag = NBTAPI.getMainCompound(ingredient);
        NBTList<String> extraTags = NBTAPI.getStringList(itemTag, CucumberyTag.EXTRA_TAGS_KEY);
        boolean reusable = NBTAPI.arrayContainsValue(extraTags, Constant.ExtraTag.CUSTOM_RECIPE_REUSABLE) || config.getBoolean("recipes." + recipe + ".ingredients." + j + "." + "reusable");
        resultLore.add(ComponentUtil.create("&f", itemName, "&8 : " + playerAmountColor + playerAmount + " §7/rgb0,255,84; " + amount + (reusable ? " §8[∞]" : "")));
      }
      if (ingredients.isEmpty())
      {
        result = CreateItemStack.newItem(Material.MUSIC_DISC_11, 1, "&e알 수 없는 아이템", "&c&o손상된 레시피", true);
      }
      if (!ingredients.isEmpty())
      {
        // 추가 조건 설명 추가
        List<Component> requirementsLore = new ArrayList<>(Arrays.asList(Component.empty(), ComponentUtil.create("rgb255,115,255;[추가 제작 조건]")));
        // 모든 요구 조건을 만족하여 아이템을 제적할 수 있는가?
        boolean[] requirements = new boolean[CustomRecipeUtil.REQUIREMENT_AMOUNT];

        Arrays.fill(requirements, true);

        CustomRecipeUtil.createRequirementLore(requirementsLore, player, config, category, recipe, requirements);

        if (requirementsLore.size() > 2)
        {
          resultLore.addAll(requirementsLore);
        }
        resultLore.addAll(Arrays.asList(Component.empty(), ComponentUtil.create("§e시프트 클릭§7으로 빠르게 아이템 제작 가능")));

        // 제작하는데 시간이 걸리는 레시피를 제작중일 때는 추가 조건, 레시피 조건 무시
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
              resultLore.remove(resultLore.size() - 1);
              resultLore.remove(resultLore.size() - 1);
              resultLore.addAll(Arrays.asList(Component.empty(), ComponentUtil.create("§b[아이템을 제작하는 중입니다]")));
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
                    resultLore.addAll(Arrays.asList(Component.empty(), ComponentUtil.create("§e시프트 클릭§7으로 빠르게 아이템 제작 시간 스킵 가능")));
                  }
                }
              }
            }
            else
            {
              resultLore.set(resultLore.size() - 1, ComponentUtil.create("§e시프트 클릭§7으로 빠르게 아이템 수령 가능"));
            }
          }
        }

        if (!Method.allIsTrue(requirements) && !timeCraftingIgnoreOthers)
        {
          resultLore.remove(resultLore.size() - 1);
          resultLore.remove(resultLore.size() - 1);
          resultLore.addAll(Arrays.asList(Component.empty(), ComponentUtil.create("§c[추가 제작 조건 불충족]")));
        }

        // 인벤토리 부족 또는 재료 부족
        boolean ingredientEnough = Method.allIsTrue(ingredientEnoughArray);
        int countSpace = ItemStackUtil.countSpace(player.getInventory(), result);
        int resultAmount = result.getAmount();
        boolean playerHasEnoughSpace = countSpace >= resultAmount;
        if (player.getInventory().firstEmpty() == -1 && !playerHasEnoughSpace)
        {
          String playerSpaceColor = CustomRecipeUtil.getPercentColor(countSpace * 1d / resultAmount);
          resultLore.remove(resultLore.size() - 1);
          resultLore.remove(resultLore.size() - 1);
          resultLore.addAll(
                  Arrays.asList(Component.empty(),
                          ComponentUtil.create("§c[인벤토리 공간 부족]"),
                          ComponentUtil.create("&e필요 인벤토리 공간 : " + playerSpaceColor + countSpace + " &7/rgb0,255,84; " + resultAmount)));
        }
        else if (!ingredientEnough && !timeCraftingIgnoreOthers)
        {
          resultLore.remove(resultLore.size() - 1);
          resultLore.remove(resultLore.size() - 1);
          resultLore.addAll(Arrays.asList(Component.empty(), ComponentUtil.create("§c[재료 부족]")));
        }

        String permission = config.getString("recipes." + recipe + ".extra.permissions.base-permission");
        boolean hideIfNoBase = config.getBoolean("recipes." + recipe + ".extra.permissions.hide-if-no-base");
        String bypassIfHidden = config.getString("recipes." + recipe + ".extra.permissions.bypass-if-hidden-permission");
        if (permission != null && !player.hasPermission(permission) && hideIfNoBase && (bypassIfHidden == null || !player.hasPermission(bypassIfHidden)))
        {
          resultLore = new ArrayList<>(Collections.singletonList(ComponentUtil.create("§c해당 레시피의 정보를 볼 권한이 없습니다")));
          resultMeta.displayName(ComponentUtil.create("§c[비공개 레시피]"));
          result.setType(Material.BARRIER);
        }

        resultMeta.lore(resultLore);
        result.setItemMeta(resultMeta);
        menu.addItem(result);
      }
    }
    menu.setItem(
            40, INFO_ITEM);
  }

  private final static ItemStack INFO_ITEM = CreateItemStack.newItem(Material.ACACIA_SIGN, 1, "&a여기에서는 무엇을 할 수 있나요?", Arrays
          .asList("&7제작을 원하는 아이템에 마우스를 올리면", "&7해당 아이템을 제작하는데 필요한 아이템들을 나열해줍니다", "&e클릭&7을 하면 해당 재료에 대한 자세한 정보를 알려줍니다", "&7제작 조건을 전부 충족시켰다면 &e시프트 클릭", "&7으로도 빠르게 아이템을 제작할 수 있습니다",
                  "&b제작하고 싶은 아이템을 선택해서 제작해보세요!"), false);
}
