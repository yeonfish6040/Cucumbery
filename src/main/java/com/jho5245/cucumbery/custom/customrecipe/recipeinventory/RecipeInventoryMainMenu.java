package com.jho5245.cucumbery.custom.customrecipe.recipeinventory;

import com.jho5245.cucumbery.Cucumbery;
import com.jho5245.cucumbery.custom.customrecipe.CustomRecipeUtil;
import com.jho5245.cucumbery.util.gui.GUIManager;
import com.jho5245.cucumbery.util.no_groups.ItemSerializer;
import com.jho5245.cucumbery.util.no_groups.MessageUtil;
import com.jho5245.cucumbery.util.no_groups.Method;
import com.jho5245.cucumbery.util.storage.component.util.ComponentUtil;
import com.jho5245.cucumbery.util.storage.data.Constant;
import com.jho5245.cucumbery.util.storage.data.Variable;
import com.jho5245.cucumbery.util.storage.no_groups.CreateItemStack;
import com.jho5245.cucumbery.util.storage.no_groups.ItemStackUtil;
import de.tr7zw.changeme.nbtapi.NBTItem;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.format.TextDecoration.State;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.jho5245.cucumbery.custom.customrecipe.recipeinventory.RecipeInventoryCategory.convert;

public class RecipeInventoryMainMenu
{
  /**
   * 레시피 목록 목록이 있는 메뉴를 열어줍니다.
   *
   * @param player 메류를 열어줄 플레이어
   * @param page   메뉴 페이지
   */
  public static void openRecipeInventory(@NotNull final Player player, final int page, final boolean firstOpen)
  {
    int categoryAmount = Variable.customRecipes.size();
    if (categoryAmount == 0)
    {
      MessageUtil.sendError(player, "제작 가능한 커스텀 레시피가 하나도 없습니다");
      String openInventoryTitle = player.getOpenInventory().getTitle();
      if (openInventoryTitle.startsWith(Constant.CANCEL_STRING))
      {
        Bukkit.getScheduler().runTaskLater(Cucumbery.getPlugin(), () ->
                player.closeInventory(), 0L);
      }
      return;
    }
    // 페이지 당 최대 레시피 목록 배치 개수
    int maxCategoriesPerPage = 21;
    int maxPage = categoryAmount / maxCategoriesPerPage + 1;
    if (categoryAmount % maxCategoriesPerPage == 0)
    {
      maxPage--;
    }
    int PAGE = page;
    if (PAGE > maxPage)
    {
      PAGE = 1;
    }
    if (PAGE < 1)
    {
      PAGE = maxPage;
    }
    final String title = Constant.CANCEL_STRING + Constant.CUSTOM_RECIPE_RECIPE_LIST_MENU + (maxPage == 1 ? "" : (" §3[" + PAGE + "/" + maxPage + "]")) + Method.format("page:" + PAGE, "§");
    Inventory menu = Bukkit.createInventory(null, 45, title);
    if (firstOpen)
    {
      // deco template
      ItemStack deco1 = CreateItemStack.create(Material.WHITE_STAINED_GLASS_PANE, 1, "§와", false);
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
      menu.setItem(44, deco1);

      // buttons

      if (maxPage == 1)
      {
        menu.setItem(39, deco1);
        menu.setItem(41, deco1);
      }
      else
      {
        menu.setItem(39, CreateItemStack.create(Material.SPRUCE_BOAT, Math.max(1, PAGE == 1 ? maxPage : PAGE - 1), PAGE == 1 ? "§e마지막 페이지로" : "§e이전 페이지로", "§a현재 페이지 : " + PAGE + " / " + maxPage, false));
        menu.setItem(
                41, CreateItemStack.create(Material.SPRUCE_BOAT, Math.min(maxPage, PAGE == maxPage ? 1 : PAGE + 1), PAGE == maxPage ? "§b처음 페이지로" : "§b다음 페이지로", "§a현재 페이지 : " + PAGE + " / " + maxPage, false));
      }
      menu.setItem(40, CreateItemStack.create(Material.CLOCK, 1, "rg255,204;로딩중...", false));
      Inventory finalMenu = menu;
      Bukkit.getScheduler().runTaskLater(Cucumbery.getPlugin(), () ->
              player.openInventory(finalMenu), 0L);
      InventoryView lastInventory = GUIManager.getLastInventory(player.getUniqueId());
      if (lastInventory != null)
      {
        menu.setItem(36, CreateItemStack.getPreviousButton(lastInventory.title()));
      }
      else
      {
        menu.setItem(36, deco1);
      }
    }
    else
    {
      menu = player.getOpenInventory().getTopInventory();
    }
    List<String> categoryNames = new ArrayList<>(Variable.customRecipes.keySet());
    Method.sort(categoryNames);
    ItemStack[] menuItems = new ItemStack[45];
    for (int i = (PAGE - 1) * maxCategoriesPerPage; i < PAGE * maxCategoriesPerPage; i++)
    {
      if (i >= Variable.customRecipes.size())
      {
        menuItems[convert(i)] = new ItemStack(Material.AIR);
        continue;
      }
      String category = categoryNames.get(i);
      YamlConfiguration config = Variable.customRecipes.get(category);
      ItemStack categoryItem = ItemSerializer.deserialize(config.getString("extra.displayitem"));
      if (!ItemStackUtil.itemExists(categoryItem))
      {
        try
        {
          categoryItem = new ItemStack(Material.valueOf(config.getString("extra.displayitem")));
        }
        catch (Exception e)
        {
          categoryItem = new ItemStack(Material.CHEST_MINECART);
        }
      }
      if (!categoryItem.getType().isItem())
      {
        categoryItem.setType(Material.CHEST_MINECART);
      }
      ItemMeta itemMeta = categoryItem.getItemMeta();
      itemMeta.addItemFlags(ItemFlag.values());
      categoryItem.setItemMeta(itemMeta);
      boolean isDecorative = config.getBoolean("extra.decorative");
      if (isDecorative)
      {
        menuItems[convert(i)] = categoryItem;
        continue;
      }
      NBTItem nbtItem = new NBTItem(categoryItem, true);
      nbtItem.setString("category", category);
      ItemMeta categoryItemMeta = categoryItem.getItemMeta();
      String categoryDisplay = config.getString("extra.display");
      if (categoryDisplay == null)
      {
        categoryDisplay = category;
      }
      Component categoryDisplayComp = ComponentUtil.create(categoryDisplay).hoverEvent(null).clickEvent(null);
      if (categoryDisplayComp.color() == null)
      {
        categoryDisplayComp = categoryDisplayComp.color(Constant.THE_COLOR).decoration(TextDecoration.ITALIC, State.FALSE);
      }
      categoryItemMeta.displayName(categoryDisplayComp);
      final List<Component> categoryItemLore = new ArrayList<>();
      ConfigurationSection recipeList = config.getConfigurationSection("recipes");
      if (recipeList == null || recipeList.getKeys(false).isEmpty())
      {
        categoryItemLore.add(ComponentUtil.create("§c§o레시피 없음 (혹은 오류 발생)"));
      }
      else
      {
        if (player.hasPermission("asdf"))
        {
          categoryItemLore.add(ComponentUtil.translate("gb210,255;카테고리 ID : %s", "rgb93,244,255;" + category));
        }
        int recipeAmount = recipeList.getKeys(false).size();
        // 장식용 레시피는 제외하기
        for (String key : recipeList.getKeys(false))
        {
          if (config.getBoolean("recipes." + key + ".extra.decorative"))
          {
            recipeAmount--;
          }
        }
        int maxRecipesPerCategory = Cucumbery.config.getInt("customrecipe.max-recipes-per-category");
        int counter = 0;
        List<Component> categoryDescription = new ArrayList<>();
        List<String> descrption = config.getStringList("extra.description");
        for (String d : descrption)
        {
          categoryDescription.add(ComponentUtil.create(d));
        }
        if (!categoryDescription.isEmpty())
        {
          categoryItemLore.addAll(categoryDescription);
        }
        categoryItemLore.addAll(Arrays.asList(Component.empty(), ComponentUtil.translate("gb210,255;레시피 개수 : %s", ComponentUtil.translate("rgb93,244,255;%s개", recipeAmount))));
        for (String recipe : recipeList.getKeys(false))
        {
          if (config.getBoolean("recipes." + recipe + ".extra.decorative"))
          {
            continue;
          }
          if (counter >= maxRecipesPerCategory)
          {
            int difference = recipeAmount - counter;
            categoryItemLore.add(ComponentUtil.create("rgb128,215,255;&o외 " + difference + "개 더..."));
            break;
          }
          counter++;
          String display = config.getString("recipes." + recipe + ".extra.display");
          String basePermission = config.getString("recipes." + recipe + ".extra.permissions.base-permission");
          String hidePermission = config.getString("recipes." + recipe + ".extra.permissions.bypass-if-hidden-permission");
          boolean hide = config.getBoolean("recipes." + recipe + ".extra.permissions.hide-if-no-base");
          if (hide && basePermission != null && !player.hasPermission(basePermission) && (hidePermission == null || !player.hasPermission(hidePermission)))
          {
            display = "&c[비공개 레시피]";
          }
          else if (display == null)
          {
            display = recipe;
          }
          Component displayComp = ComponentUtil.create(display).hoverEvent(null).clickEvent(null);
          if (displayComp.color() == null)
          {
            displayComp = displayComp.color(TextColor.color(93, 244, 255)).decoration(TextDecoration.ITALIC, State.FALSE);
          }
          categoryItemLore.add(ComponentUtil.create("§b - ", displayComp));
        }
      }
      // 접근 조건 설명 추가
      List<Component> requirementsLore = new ArrayList<>(Arrays.asList(Component.empty(), ComponentUtil.translate("rgb255,115,255;[접근 조건]")));
      // 모든 요구 조건을 만족하여 레시피 목록에 접근할 수 있는가?
      boolean[] requirements = new boolean[5];
      Arrays.fill(requirements, true);

      CustomRecipeUtil.createRequirementLore(requirementsLore, player, config, category, null, requirements);

      if (requirementsLore.size() > 2)
      {
        categoryItemLore.addAll(requirementsLore);
      }

      boolean passed = Method.allIsTrue(requirements);
      if (!passed)
      {
        categoryItemLore.addAll(Arrays.asList(Component.empty(), ComponentUtil.translate("&c[접근 조건 불충족]")));
      }
      // 레시피 목록에 접근할 권한도 없고 비공개 레시피이고 비공개 레시피 우회 권한도 없을 때
      String permission = config.getString("extra.permissions.base-permission");
      String bypassIfHidden = config.getString("extra.permissions.bypass-if-hidden-permission");
      boolean hideIfNoBase = config.getBoolean("extra.permissions.hide-if-no-base");
      if (permission != null && !player.hasPermission(permission) && hideIfNoBase && (bypassIfHidden == null || !player.hasPermission(bypassIfHidden)))
      {
        categoryItemLore.clear();
        categoryItemLore.add(ComponentUtil.translate("&c해당 레시피 목록의 정보를 볼 권한이 없습니다"));
        categoryItemMeta.displayName(ComponentUtil.translate("&c[비공개 레시피 목록]"));
        categoryItem.setType(Material.BARRIER);
      }

      categoryItemMeta.lore(categoryItemLore);
      categoryItem.setItemMeta(categoryItemMeta);
      menuItems[convert(i)] = categoryItem;
    }
    String invName = menu.getViewers().isEmpty() ? "" : menu.getViewers().get(0).getOpenInventory().getTitle();
    // for asynchronus issue
    if (!invName.equals(title))
    {
      return;
    }
    for (int i = 0; i < menuItems.length; i++)
    {
      ItemStack itemStack = menuItems[i];
      if (itemStack != null)
      {
        menu.setItem(i, itemStack);
      }
    }
    menu.setItem(
            40, CreateItemStack.create(Material.ACACIA_SIGN, 1, "&a여기에서는 무엇을 할 수 있나요?",
                    Arrays.asList("&73×3 제작대처럼 복잡하게 재료의", "&7위치를 외울 필요 없이 재료만 들고 있으면", "&7클릭 한 번으로 아이템을 제작할 수 있는 곳입니다", "&b제작하고 싶은 아이템의 종류를 목록 위에서 골라보세요!"), false));
  }
}
