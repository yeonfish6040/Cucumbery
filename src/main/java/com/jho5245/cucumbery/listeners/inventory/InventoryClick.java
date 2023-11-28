package com.jho5245.cucumbery.listeners.inventory;

import com.jho5245.cucumbery.Cucumbery;
import com.jho5245.cucumbery.custom.customeffect.CustomEffectManager;
import com.jho5245.cucumbery.custom.customeffect.type.CustomEffectType;
import com.jho5245.cucumbery.custom.customeffect.type.CustomEffectTypeCooldown;
import com.jho5245.cucumbery.custom.customrecipe.CustomRecipeUtil;
import com.jho5245.cucumbery.custom.customrecipe.recipeinventory.RecipeInventoryCategory;
import com.jho5245.cucumbery.custom.customrecipe.recipeinventory.RecipeInventoryMainMenu;
import com.jho5245.cucumbery.custom.customrecipe.recipeinventory.RecipeInventoryRecipe;
import com.jho5245.cucumbery.events.entity.EntityCustomEffectRemoveEvent.RemoveReason;
import com.jho5245.cucumbery.util.additemmanager.AddItemUtil;
import com.jho5245.cucumbery.util.gui.GUIManager;
import com.jho5245.cucumbery.util.gui.GUIManager.GUIType;
import com.jho5245.cucumbery.util.itemlore.ItemLore;
import com.jho5245.cucumbery.util.itemlore.ItemLoreView;
import com.jho5245.cucumbery.util.nbt.CucumberyTag;
import com.jho5245.cucumbery.util.nbt.NBTAPI;
import com.jho5245.cucumbery.util.blockplacedata.BlockPlaceDataConfig;
import com.jho5245.cucumbery.util.no_groups.ItemSerializer;
import com.jho5245.cucumbery.util.no_groups.MessageUtil;
import com.jho5245.cucumbery.util.no_groups.Method;
import com.jho5245.cucumbery.util.storage.component.util.ComponentUtil;
import com.jho5245.cucumbery.util.storage.component.util.ItemNameUtil;
import com.jho5245.cucumbery.util.storage.data.*;
import com.jho5245.cucumbery.util.storage.data.Constant.ExtraTag;
import com.jho5245.cucumbery.util.storage.data.Constant.RestrictionType;
import com.jho5245.cucumbery.util.storage.no_groups.BlockDataInfo;
import com.jho5245.cucumbery.util.storage.no_groups.CustomConfig;
import com.jho5245.cucumbery.util.storage.no_groups.CustomConfig.UserData;
import com.jho5245.cucumbery.util.storage.no_groups.ItemStackUtil;
import com.jho5245.cucumbery.util.storage.no_groups.SoundPlay;
import de.tr7zw.changeme.nbtapi.NBTCompound;
import de.tr7zw.changeme.nbtapi.NBTItem;
import de.tr7zw.changeme.nbtapi.NBTList;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.TranslatableComponent;
import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.block.Block;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.inventory.InventoryType.SlotType;
import org.bukkit.inventory.*;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.util.*;

public class InventoryClick implements Listener
{
  public static List<Player> check = new ArrayList<>();

  @Nullable
  public static List<ItemStack> removeItem(final Player player, String category, FileConfiguration config, String recipe, List<String> ingredientsString, List<ItemStack> ingredients, List<Integer> ingredientAmounts, boolean checkOnly)
  {
    List<ItemStack> itemStacks = new ArrayList<>();
    long requireTimeToCraft = config.getLong("recipes." + recipe + ".extra.crafting-time");
    if (requireTimeToCraft > 0)
    {
      YamlConfiguration playerCraftingTimeConfig = Variable.craftingTime.get(player.getUniqueId());
      long playerCraftingTime = playerCraftingTimeConfig == null ? 0 : playerCraftingTimeConfig.getLong("crafting-time." + category + "." + recipe);
      if (playerCraftingTime > 0)
      {
        return null;
      }
    }
    for (int i = 0; i < ingredients.size(); i++)
    {
      ItemStack ingredient = ingredients.get(i);
      NBTCompound itemTag = NBTAPI.getMainCompound(ingredient);
      NBTList<String> extraTags = NBTAPI.getStringList(itemTag, CucumberyTag.EXTRA_TAGS_KEY);
      // 재사용 가능 재료는 사라지지 않게 함
      boolean reusable = NBTAPI.arrayContainsValue(extraTags, ExtraTag.CUSTOM_RECIPE_REUSABLE.toString()) || config.getBoolean("recipes." + recipe + ".ingredients." + (i + 1) + ".reusable");
      {
        int amount = ingredientAmounts.get(i);
        String ingredientString = ingredientsString.get(i);
        for (ItemStack itemStack : player.getInventory())
        {
          if (amount == 0)
          {
            break;
          }
          if (ingredientString.startsWith("predicate:") && ItemStackUtil.predicateItem(itemStack, ingredientString.substring(10)))
          {
            itemStack = itemStack.clone();
            if (amount < itemStack.getAmount())
            {
              itemStack.setAmount(amount);
            }
            amount -= itemStack.getAmount();
            if (!checkOnly)
            {
              if (!reusable && !player.getInventory().removeItem(itemStack).isEmpty())
              {
                return null;
              }
            }
            itemStacks.add(itemStack);
          }
          else if (ItemStackUtil.itemEquals(ItemLore.setItemLore(ingredient, ItemLoreView.of(player)), itemStack))
          {
            ItemLore.setItemLore(ingredient, ItemLoreView.of(player));
            int maxStackSize = ingredient.getMaxStackSize();
            ingredient.setAmount(maxStackSize);
            int stack = amount / maxStackSize;
            for (int j = 0; j < stack; j++)
            {
              if (!checkOnly)
              {
                if (!reusable && !player.getInventory().removeItem(ingredient).isEmpty())
                {
                  itemStacks.clear();
                  return null;
                }
              }
              itemStacks.add(ingredient);
              amount -= maxStackSize;
            }
            if (amount > 0)
            {
              ingredient.setAmount(amount);
              if (!checkOnly)
              {
                HashMap<Integer, ItemStack> result = reusable ? new HashMap<>() : player.getInventory().removeItem(ingredient.clone());
                if (!result.isEmpty())
                {
                  itemStacks.clear();
                  return null;
                }
              }
              itemStacks.add(ingredient);
              amount = 0;
            }
          }
        }
      }
    }
    if (!checkOnly)
    {
      if (Cucumbery.using_Vault_Economy)
      {
        double cost = config.getDouble("recipes." + recipe + ".extra.cost");
        if (cost > 0)
        {
          if (!UserData.EVENT_EXCEPTION_ACCESS.getBoolean(player))
          {
            Cucumbery.eco.withdrawPlayer(player, cost);
          }
        }
      }
      int levelCost = config.getInt("recipes." + recipe + ".extra.levelcost");
      if (levelCost > 0)
      {
        player.setLevel(Math.max(0, player.getLevel() - levelCost));
      }
      int foodLevelCost = config.getInt("recipes." + recipe + ".extra.foodlevelcost");
      if (foodLevelCost > 0)
      {
        player.setFoodLevel(Math.max(0, player.getFoodLevel() - foodLevelCost));
      }
      double healthCost = config.getDouble("recipes." + recipe + ".extra.hpcost");
      if (healthCost > 0)
      {
        player.setHealth(Math.max(0.01, player.getHealth() - healthCost));
      }
      double saturationCost = config.getDouble("recipes." + recipe + ".extra.saturationcost");
      if (saturationCost > 0)
      {
        player.setSaturation((float) Math.max(0, player.getSaturation() - saturationCost));
      }
      double maxHealthCost = config.getDouble("recipes." + recipe + ".extra.mhpcost");
      if (maxHealthCost > 0)
      {
        double playerMaxHealth = Objects.requireNonNull(player.getAttribute(Attribute.GENERIC_MAX_HEALTH)).getBaseValue();
        Objects.requireNonNull(player.getAttribute(Attribute.GENERIC_MAX_HEALTH)).setBaseValue(Math.max(0.01, maxHealthCost - playerMaxHealth));
      }
    }
    return itemStacks;
  }

  private static void chanceGiveItem(Player player, String recipeListName, FileConfiguration recipeList, String recipe, ItemStack result)
  {
    List<String> commands = Cucumbery.config.getStringList("customrecipe.commands-on-craft.commands");
    for (String command : commands)
    {
      placeHolder(command, player, recipeListName, recipeList, recipe, result);
      Method.performCommand(player, command, true, true, null);
    }
    giveItem(player, recipeListName, recipeList, recipe, result);
  }

  private static void giveItem(Player player, String recipeListName, FileConfiguration recipeList, String recipe, ItemStack result)
  {
    UUID uuid = player.getUniqueId();
    long requireTimeToCraft = recipeList.getLong("recipes." + recipe + ".extra.crafting-time");
    if (requireTimeToCraft > 0)
    {
      YamlConfiguration playerCraftingTimeConfig = Variable.craftingTime.get(uuid);
      long playerCraftingTime = playerCraftingTimeConfig == null ? 0 : playerCraftingTimeConfig.getLong("crafting-time." + recipeListName + "." + recipe);
      if (playerCraftingTime > 0)
      {
        long currentTime = System.currentTimeMillis();
        if (currentTime <= playerCraftingTime)
        {
          return;
        }
        else
        {
          CustomConfig playerCraftingTimeConfigFile = CustomConfig.getCustomConfig("data/CustomRecipe/CraftingTime/" + player.getUniqueId().toString() + ".yml");
          playerCraftingTimeConfigFile.getConfig().set("crafting-time." + recipeListName + "." + recipe, null);
          playerCraftingTimeConfig.set("crafting-time." + recipeListName + "." + recipe, null);
          ConfigurationSection categorySection = playerCraftingTimeConfig.getConfigurationSection("crafting-time." + recipeListName);
          if (categorySection == null || categorySection.getKeys(false).isEmpty())
          {
            playerCraftingTimeConfigFile.getConfig().set("crafting-time." + recipeListName, null);
            playerCraftingTimeConfig.set("crafting-time." + recipeListName, null);
          }
          playerCraftingTimeConfigFile.saveConfig();
          Variable.craftingTime.put(uuid, playerCraftingTimeConfig);
        }
      }
      else
      {
        CustomConfig playerCraftingTimeConfigFile = CustomConfig.getCustomConfig("data/CustomRecipe/CraftingTime/" + player.getUniqueId().toString() + ".yml");
        if (playerCraftingTimeConfig == null)
        {
          playerCraftingTimeConfig = playerCraftingTimeConfigFile.getConfig();
        }
        playerCraftingTimeConfigFile.getConfig().set("crafting-time." + recipeListName + "." + recipe, System.currentTimeMillis() + requireTimeToCraft);
        playerCraftingTimeConfig.set("crafting-time." + recipeListName + "." + recipe, System.currentTimeMillis() + requireTimeToCraft);
        playerCraftingTimeConfigFile.saveConfig();
        Variable.craftingTime.put(uuid, playerCraftingTimeConfig);
        return;
      }
    }
    ItemStack trueResult = null;
    List<String> randomResult = recipeList.getStringList("recipes." + recipe + ".extra.random-result");
    if (!randomResult.isEmpty())
    {
      double randomStart = 0d;
      double random = Math.random() * 100d;
      for (String randomResultString : randomResult)
      {
        try
        {
          String[] split = randomResultString.split(";;");
          String randomResultCategory = split[0];
          String randomResultRecipe = split[1];
          double chance = Double.parseDouble(split[2]);
          File randomResultCategoryFile = new File(Cucumbery.getPlugin().getDataFolder() + "/data/CustomRecipe/" + randomResultCategory + ".yml");
          if (!randomResultCategoryFile.exists())
          {
            continue;
          }
          YamlConfiguration randomResultRecipeConfig = Variable.customRecipes.get(randomResultCategory);
          ConfigurationSection randomResultRecipeSection = randomResultRecipeConfig.getConfigurationSection("recipes." + randomResultRecipe);
          if (randomResultRecipeSection == null || randomResultRecipeSection.getKeys(false).isEmpty())
          {
            continue;
          }
          if (random >= randomStart && random <= randomStart + chance)
          {
            trueResult = ItemSerializer.deserialize(randomResultRecipeSection.getString("result"));
          }
          randomStart += chance;
        }
        catch (Exception ignored)
        {
        }
      }
    }
    if (!ItemStackUtil.itemExists(trueResult) && !recipeList.getBoolean("recipes." + recipe + ".extra.no-give-item"))
    {
      trueResult = result;
    }
    double chance = recipeList.getDouble("recipes." + recipe + ".extra.chance");
    if (chance > 0d)
    {
      if (Math.random() * 100d < chance)
      {
        if (ItemStackUtil.itemExists(trueResult))
        {
          AddItemUtil.addItem(player, trueResult);
        }
        String message = Cucumbery.config.getString("customrecipe.chance-items.success.message");
        if (message != null && !message.equals("none"))
        {
          MessageUtil.sendMessage(player, Prefix.INFO_CUSTOM_RECIPE, ComponentUtil.translate(message, chance, result));
        }
        List<String> successSounds = Cucumbery.config.getStringList("customrecipe.chance-items.success.sounds");
        for (String successSound : successSounds)
        {
          try
          {
            String[] split = successSound.split(",");
            Method.playSound(player, Sound.valueOf(split[0]), Float.parseFloat(split[1]), Float.parseFloat(split[2]));
          }
          catch (Exception ignored)
          {
          }
        }
        List<String> successCommands = Cucumbery.config.getStringList("customrecipe.chance-items.success.commands");
        for (String command : successCommands)
        {
          command = placeHolder(command, player, recipeListName, recipeList, recipe, result);
          Method.performCommand(player, command, true, true, null);
        }
        List<String> craftCommands = recipeList.getStringList("recipes." + recipe + ".extra.commands.success");
        for (String craftCommand : craftCommands)
        {
          craftCommand = placeHolder(craftCommand, player, recipeListName, recipeList, recipe, result);
          Method.performCommand(player, craftCommand, true, true, null);
        }
      }
      else
      {
        String message = Cucumbery.config.getString("customrecipe.chance-items.failure.message");
        if (message != null && !message.equals("none"))
        {
          MessageUtil.sendMessage(player, Prefix.INFO_CUSTOM_RECIPE, ComponentUtil.translate(message, 100d - chance, result));
        }
        List<String> failureSounds = Cucumbery.config.getStringList("customrecipe.chance-items.failure.sounds");
        for (String failureSound : failureSounds)
        {
          try
          {
            String[] split = failureSound.split(",");
            Method.playSound(player, Sound.valueOf(split[0]), Float.parseFloat(split[1]), Float.parseFloat(split[2]));
          }
          catch (Exception ignored)
          {
          }
        }
        List<String> failureCommands = Cucumbery.config.getStringList("customrecipe.chance-items.failure.commands");
        for (String command : failureCommands)
        {
          command = placeHolder(command, player, recipeListName, recipeList, recipe, result);
          Method.performCommand(player, command, true, true, null);
        }
        List<String> craftCommands = recipeList.getStringList("recipes." + recipe + ".extra.commands.failure");
        for (String craftCommand : craftCommands)
        {
          craftCommand = placeHolder(craftCommand, player, recipeListName, recipeList, recipe, result);
          Method.performCommand(player, craftCommand, true, true, null);
        }
      }
    }
    else if (ItemStackUtil.itemExists(trueResult))
    {
      AddItemUtil.addItem(player, trueResult);
    }
    List<String> craftCommands = recipeList.getStringList("recipes." + recipe + ".extra.commands.craft");
    for (String craftCommand : craftCommands)
    {
      craftCommand = placeHolder(craftCommand, player, recipeListName, recipeList, recipe, result);
      Method.performCommand(player, craftCommand, true, true, null);
    }
    logCraft(player, recipeListName, recipe);
  }

  private static String placeHolder(
          @NotNull String command, @NotNull Player player, @NotNull String recipeListName, @NotNull FileConfiguration recipeList, @NotNull String recipe, @NotNull ItemStack result)
  {
    command = command.replace("%result%", result.toString());
    String categoryDisplay = recipeList.getString("extra.display");
    if (categoryDisplay == null)
    {
      categoryDisplay = recipeListName;
    }
    command = command.replace("%category%", categoryDisplay);
    String recipeDisplay = recipeList.getString("recipes." + recipe + ".extra.display");
    if (recipeDisplay == null)
    {
      recipeDisplay = recipe;
    }
    command = command.replace("%recipe%", recipeDisplay);
    FileConfiguration craftsLog = Variable.craftsLog.get(player.getUniqueId());
    if (craftsLog != null)
    {
      command = command.replace("%category_" + recipeListName + "_crafted%", craftsLog.getInt("crafts.categories." + recipeListName) + "");
      command = command.replace("%recipe_" + recipeListName + "_" + recipe + "_crafted%", craftsLog.getInt("crafts.recipes." + recipeListName + "." + recipe) + "");
    }
    command = Method.parseCommandString(player, command);
    return command;
  }

  /**
   * 아이템 제작 기록을 저장합니다.
   *
   * @param player   저장할 플레이어
   * @param category 제작한 아이템의 레시피 목록 이름
   * @param recipe   제작한 아이템의 레시피 이름
   */
  private static void logCraft(Player player, String category, String recipe)
  {
    UUID uuid = player.getUniqueId();
    CustomConfig craftLogConfig = CustomConfig.getCustomConfig("data/CustomRecipe/CraftsLog/" + player.getUniqueId().toString() + ".yml");
    YamlConfiguration logConfig = Variable.craftsLog.get(uuid), logConfig2 = craftLogConfig.getConfig();
    if (logConfig == null)
    {
      logConfig = logConfig2;
    }
    int categoryAmount = logConfig.getInt("crafts.categories." + category);
    logConfig2.set("crafts.categories." + category, categoryAmount + 1);
    logConfig.set("crafts.categories." + category, categoryAmount + 1);
    int craftAmount = logConfig.getInt("crafts.recipes." + category + "." + recipe);
    logConfig2.set("crafts.recipes." + category + "." + recipe, craftAmount + 1);
    logConfig.set("crafts.recipes." + category + "." + recipe, craftAmount + 1);
    craftLogConfig.saveConfig();
    Variable.craftsLog.put(uuid, logConfig);
    CustomConfig customConfigLastCraft = CustomConfig.getCustomConfig("data/CustomRecipe/LastCraftsLog/" + player.getUniqueId().toString() + ".yml");
    YamlConfiguration configLastCraft = customConfigLastCraft.getConfig();
    configLastCraft.set("last-crafts." + category + "." + recipe, System.currentTimeMillis());
    customConfigLastCraft.saveConfig();
    Variable.lastCraftsLog.put(uuid, configLastCraft);
  }

  @EventHandler
  public void onInventoryClick(InventoryClickEvent event)
  {
    if (event.isCancelled())
    {
      return;
    }
    InventoryView view = event.getView();
    Player player = (Player) view.getPlayer();
    UUID uuid = player.getUniqueId();
    if (Variable.scrollReinforcing.contains(uuid))
    {
      event.setCancelled(true);
      return;
    }
    Component title = view.title();
    GameMode gameMode = player.getGameMode();
    PlayerInventory playerInventory = player.getInventory();
    ItemStack helmet = playerInventory.getHelmet();
    ItemStack chestplate = playerInventory.getHelmet();
    ItemStack leggings = playerInventory.getHelmet();
    ItemStack boots = playerInventory.getHelmet();
    //ItemStack mainHand = playerInventory.getItemInMainHand();
    ItemStack offHand = playerInventory.getItemInOffHand();
    boolean helmetExists = ItemStackUtil.itemExists(helmet);
    boolean chestplateExists = ItemStackUtil.itemExists(chestplate);
    boolean leggingsExists = ItemStackUtil.itemExists(leggings);
    boolean bootsExists = ItemStackUtil.itemExists(boots);
    InventoryView openInventory = player.getOpenInventory();
    InventoryType openInventoryType = openInventory.getType();
    String openInventoryTitle = openInventory.getTitle();
    SlotType slotType = event.getSlotType();
    ClickType clickType = event.getClick();
    ItemStack current = event.getCurrentItem(), cursor = event.getCursor();
    Inventory inventory = event.getInventory();
    Location inventoryLocation = inventory.getLocation();
    ItemStack placedBlockDataItemStack = inventoryLocation == null ? null : BlockPlaceDataConfig.getItem(inventoryLocation);
    Inventory clickedIvnentory = event.getClickedInventory();
    String key = GUIManager.isGUITitle(title) ? GUIManager.getGUIKey(title) : "";
    InventoryType clickedInventoryType = null;
    if (clickedIvnentory != null)
    {
      clickedInventoryType = clickedIvnentory.getType();
    }
    int hotbarButton = event.getHotbarButton();
    if (ItemStackUtil.hasDisplayName(current))
    {
      assert current != null;
      String currentDisplayName = current.getItemMeta().getDisplayName();
      if (currentDisplayName.equals(MessageUtil.n2s(Constant.NO_CRAFT_ITEM_DISPLAYNAME)) || currentDisplayName.equals(MessageUtil.n2s(Constant.NO_ANVIL_ITEM_DISPLAYNAME)) || currentDisplayName.equals(
              MessageUtil.n2s(Constant.NO_SMITHING_ITEM_DISPLAYNAME)))
      {
        event.setCancelled(true);
        // 모루에서 이벤트가 캔슬되더라도 캐릭터의 레벨이 줄어드는 것처럼 보이는 버그가 있어서 새로고침 해줌 (실제 레벨이 깎이지는 않음)
        if (openInventoryType == InventoryType.ANVIL)
        {
          player.sendExperienceChange(player.getExp(), player.getLevel());
        }
        return;
      }
    }
    if ((gameMode == GameMode.SURVIVAL || gameMode == GameMode.ADVENTURE) && openInventoryType == InventoryType.CRAFTING)
    {
      if (slotType == SlotType.ARMOR)
      {
        if (ItemStackUtil.itemExists(cursor))
        {
          if (NBTAPI.isRestricted(player, cursor, RestrictionType.NO_EQUIP))
          {
            event.setCancelled(true);
            return;
          }
        }
      }
      else if (clickType == ClickType.SHIFT_LEFT || clickType == ClickType.SHIFT_RIGHT)
      {
        if (NBTAPI.isRestricted(player, current, RestrictionType.NO_EQUIP))
        {
          Material currentType = Objects.requireNonNull(current).getType();
          boolean isHelmetType = (Constant.HELMETS.contains(currentType) || currentType == Material.TURTLE_HELMET || currentType == Material.CARVED_PUMPKIN || Constant.EQUIPABLE_HEADS.contains(
                  currentType)) && !helmetExists;
          boolean isChestplateType = (Constant.CHESTPLATES.contains(currentType) || currentType == Material.ELYTRA) && !chestplateExists;
          boolean isLeggingsType = Constant.LEGGINGSES.contains(currentType) && !leggingsExists;
          boolean isBootsType = Constant.BOOTSES.contains(currentType) && !bootsExists;
          if (isHelmetType || isChestplateType || isLeggingsType || isBootsType)
          {
            event.setCancelled(true);
            return;
          }
        }
      }
      int hotbar = event.getHotbarButton();
      if (hotbar != -1)
      {
        ItemStack hotbarItem = playerInventory.getItem(hotbar);
        if (NBTAPI.isRestricted(player, hotbarItem, RestrictionType.NO_EQUIP))
        {
          event.setCancelled(true);
          return;
        }
      }
    }
    int slotNumber = event.getSlot(); // 39 = helmet, 38 = chestplate, 37 = leggings, 36 = boots
    NBTCompound currentUsageTag = NBTAPI.getCompound(NBTAPI.getMainCompound(current), CucumberyTag.USAGE_KEY);
    if (currentUsageTag != null)
    {
      String equipmentSlot = currentUsageTag.getString(CucumberyTag.USAGE_EQUIPMENT_SLOT_KEY);
      if (equipmentSlot != null)
      {
        if (openInventoryType == InventoryType.CRAFTING)
        {
          if (clickType == ClickType.SHIFT_LEFT || clickType == ClickType.SHIFT_RIGHT)
          {
            if (slotNumber == 40 || (slotNumber <= 35))
            {
              switch (equipmentSlot)
              {
                case "HELMET":
                  if (!helmetExists)
                  {
                    ItemStack currentClone = Objects.requireNonNull(current).clone();
                    {
                      event.setCurrentItem(null);
                      playerInventory.setHelmet(currentClone);
                      player.updateInventory();
                    }
                  }
                  break;
                case "CHESTPLATE":
                  if (!chestplateExists)
                  {
                    ItemStack currentClone = Objects.requireNonNull(current).clone();
                    {
                      event.setCurrentItem(null);
                      playerInventory.setChestplate(currentClone);
                      player.updateInventory();
                    }
                  }
                  break;
                case "LEGGINGS":
                  if (!leggingsExists)
                  {
                    ItemStack currentClone = Objects.requireNonNull(current).clone();
                    {
                      event.setCurrentItem(null);
                      playerInventory.setLeggings(currentClone);
                      player.updateInventory();
                    }
                  }
                  break;
                case "BOOTS":
                  if (!bootsExists)
                  {
                    ItemStack currentClone = Objects.requireNonNull(current).clone();
                    {
                      event.setCurrentItem(null);
                      playerInventory.setBoots(currentClone);
                      player.updateInventory();
                    }
                  }
                  break;
              }
            }
          }
        }
      }
    }
    NBTCompound cursorUsageTag = NBTAPI.getCompound(NBTAPI.getMainCompound(cursor), CucumberyTag.USAGE_KEY);
    if (cursorUsageTag != null)
    {
      String equipmentSlot = cursorUsageTag.getString(CucumberyTag.USAGE_EQUIPMENT_SLOT_KEY);
      if (equipmentSlot != null)
      {
        if (openInventoryType == InventoryType.CRAFTING)
        {
          if (clickType == ClickType.LEFT || clickType == ClickType.RIGHT)
          {
            if (slotNumber == 39 && equipmentSlot.equals("HELMET"))
            {
              boolean currentExists = ItemStackUtil.itemExists(current), isBound = currentExists && current.getItemMeta().hasEnchant(Enchantment.BINDING_CURSE) && current.getItemMeta().getEnchantLevel(
                      Enchantment.BINDING_CURSE) > 0;
              if (!isBound)
              {
                {
                  event.setCancelled(true);
                  ItemStack cursorClone = Objects.requireNonNull(cursor).clone();
                  if (currentExists)
                  {
                    ItemStack currentClone = current.clone();
                    player.setItemOnCursor(currentClone);
                  }
                  else
                  {
                    player.setItemOnCursor(null);
                  }
                  playerInventory.setHelmet(cursorClone);
                }
              }
            }
            if (slotNumber == 38 && equipmentSlot.equals("CHESTPLATE"))
            {
              boolean currentExists = ItemStackUtil.itemExists(current), isBound = currentExists && current.getItemMeta().hasEnchant(Enchantment.BINDING_CURSE) && current.getItemMeta().getEnchantLevel(
                      Enchantment.BINDING_CURSE) > 0;
              if (!isBound)
              {
                {
                  event.setCancelled(true);
                  ItemStack cursorClone = Objects.requireNonNull(cursor).clone();
                  if (currentExists)
                  {
                    ItemStack currentClone = current.clone();
                    player.setItemOnCursor(currentClone);
                  }
                  else
                  {
                    player.setItemOnCursor(null);
                  }
                  playerInventory.setChestplate(cursorClone);
                }
              }
            }
            if (slotNumber == 37 && equipmentSlot.equals("LEGGINGS"))
            {
              boolean currentExists = ItemStackUtil.itemExists(current), isBound = currentExists && current.getItemMeta().hasEnchant(Enchantment.BINDING_CURSE) && current.getItemMeta().getEnchantLevel(
                      Enchantment.BINDING_CURSE) > 0;
              if (!isBound)
              {
                {
                  event.setCancelled(true);
                  ItemStack cursorClone = Objects.requireNonNull(cursor).clone();
                  if (currentExists)
                  {
                    ItemStack currentClone = current.clone();
                    player.setItemOnCursor(currentClone);
                  }
                  else
                  {
                    player.setItemOnCursor(null);
                  }
                  playerInventory.setLeggings(cursorClone);
                }
              }
            }
            if (slotNumber == 36 && equipmentSlot.equals("BOOTS"))
            {
              boolean currentExists = ItemStackUtil.itemExists(current), isBound = currentExists && current.getItemMeta().hasEnchant(Enchantment.BINDING_CURSE) && current.getItemMeta().getEnchantLevel(
                      Enchantment.BINDING_CURSE) > 0;
              if (!isBound)
              {
                {
                  event.setCancelled(true);
                  ItemStack cursorClone = Objects.requireNonNull(cursor).clone();
                  if (currentExists)
                  {
                    ItemStack currentClone = current.clone();
                    player.setItemOnCursor(currentClone);
                  }
                  else
                  {
                    player.setItemOnCursor(null);
                  }
                  playerInventory.setBoots(cursorClone);
                }
              }
            }
          }
        }
      }
    }
    if (clickType == ClickType.SWAP_OFFHAND)
    {
      if (openInventoryType == InventoryType.CRAFTING)
      {
        String equipmentSlot = NBTAPI.getString(NBTAPI.getCompound(NBTAPI.getMainCompound(offHand), CucumberyTag.USAGE_KEY), CucumberyTag.USAGE_EQUIPMENT_SLOT_KEY);
        if (equipmentSlot != null)
        {
          if (slotNumber == 39 && equipmentSlot.equals("HELMET"))
          {
            boolean isBound = helmetExists && helmet.getItemMeta().hasEnchant(Enchantment.BINDING_CURSE) && helmet.getItemMeta().getEnchantLevel(
                    Enchantment.BINDING_CURSE) > 0;
            if (!isBound)
            {
              {
                event.setCancelled(true);
                ItemStack offHandClone = offHand.clone();
                if (helmetExists)
                {
                  ItemStack helmetClone = helmet.clone();
                  playerInventory.setItemInOffHand(helmetClone);
                }
                else
                {
                  playerInventory.setItemInOffHand(null);
                }
                playerInventory.setHelmet(offHandClone);
              }
            }
          }
          else if (slotNumber == 38 && equipmentSlot.equals("CHESTPLATE"))
          {
            boolean isBound = chestplateExists && chestplate.getItemMeta().hasEnchant(Enchantment.BINDING_CURSE) && chestplate.getItemMeta().getEnchantLevel(
                    Enchantment.BINDING_CURSE) > 0;
            if (!isBound)
            {
              {
                event.setCancelled(true);
                ItemStack offHandClone = offHand.clone();
                if (chestplateExists)
                {
                  ItemStack chestplateClone = chestplate.clone();
                  playerInventory.setItemInOffHand(chestplateClone);
                }
                else
                {
                  playerInventory.setItemInOffHand(null);
                }
                playerInventory.setChestplate(offHandClone);
              }
            }
          }
          else if (slotNumber == 37 && equipmentSlot.equals("LEGGINGS"))
          {
            boolean isBound = leggingsExists && leggings.getItemMeta().hasEnchant(Enchantment.BINDING_CURSE) && leggings.getItemMeta().getEnchantLevel(
                    Enchantment.BINDING_CURSE) > 0;
            if (!isBound)
            {
              {
                event.setCancelled(true);
                ItemStack offHandClone = offHand.clone();
                if (leggingsExists)
                {
                  ItemStack leggingsClone = leggings.clone();
                  playerInventory.setItemInOffHand(leggingsClone);
                }
                else
                {
                  playerInventory.setItemInOffHand(null);
                }
                playerInventory.setLeggings(offHandClone);
              }
            }
          }
          else if (slotNumber == 36 && equipmentSlot.equals("BOOTS"))
          {
            boolean isBound = bootsExists && boots.getItemMeta().hasEnchant(Enchantment.BINDING_CURSE) && boots.getItemMeta().getEnchantLevel(
                    Enchantment.BINDING_CURSE) > 0;
            if (!isBound)
            {
              {
                event.setCancelled(true);
                ItemStack offHandClone = offHand.clone();
                if (bootsExists)
                {
                  ItemStack bootsClone = boots.clone();
                  playerInventory.setItemInOffHand(bootsClone);
                }
                else
                {
                  playerInventory.setItemInOffHand(null);
                }
                playerInventory.setBoots(offHandClone);
              }
            }
          }
        }
      }
    }
    if (clickType == ClickType.NUMBER_KEY)
    {
      ItemStack hotbarItem = playerInventory.getItem(hotbarButton);
      if (openInventoryType == InventoryType.CRAFTING)
      {
        String equipmentSlot = NBTAPI.getString(NBTAPI.getCompound(NBTAPI.getMainCompound(hotbarItem), CucumberyTag.USAGE_KEY), CucumberyTag.USAGE_EQUIPMENT_SLOT_KEY);
        if (equipmentSlot != null)
        {
          if (slotNumber == 39 && equipmentSlot.equals("HELMET"))
          {
            boolean isBound = helmetExists && helmet.getItemMeta().hasEnchant(Enchantment.BINDING_CURSE) && helmet.getItemMeta().getEnchantLevel(
                    Enchantment.BINDING_CURSE) > 0;
            if (!isBound)
            {
              {
                event.setCancelled(true);
                ItemStack hotbarItemClone = Objects.requireNonNull(hotbarItem).clone();
                if (helmetExists)
                {
                  ItemStack helmetClone = helmet.clone();
                  playerInventory.setItem(hotbarButton, helmetClone);
                }
                else
                {
                  playerInventory.setItem(hotbarButton, null);
                }
                playerInventory.setHelmet(hotbarItemClone);
              }
            }
          }
          if (slotNumber == 38 && equipmentSlot.equals("CHESTPLATE"))
          {
            boolean isBound = chestplateExists && chestplate.getItemMeta().hasEnchant(Enchantment.BINDING_CURSE) && chestplate.getItemMeta().getEnchantLevel(
                    Enchantment.BINDING_CURSE) > 0;
            if (!isBound)
            {
              {
                event.setCancelled(true);
                ItemStack hotbarItemClone = Objects.requireNonNull(hotbarItem).clone();
                if (chestplateExists)
                {
                  ItemStack chestplateClone = chestplate.clone();
                  playerInventory.setItem(hotbarButton, chestplateClone);
                }
                else
                {
                  playerInventory.setItem(hotbarButton, null);
                }
                playerInventory.setChestplate(hotbarItemClone);
              }
            }
          }
          if (slotNumber == 37 && equipmentSlot.equals("LEGGINGS"))
          {
            boolean isBound = leggingsExists && leggings.getItemMeta().hasEnchant(Enchantment.BINDING_CURSE) && leggings.getItemMeta().getEnchantLevel(
                    Enchantment.BINDING_CURSE) > 0;
            if (!isBound)
            {
              {
                event.setCancelled(true);
                ItemStack hotbarItemClone = Objects.requireNonNull(hotbarItem).clone();
                if (leggingsExists)
                {
                  ItemStack leggingsClone = leggings.clone();
                  playerInventory.setItem(hotbarButton, leggingsClone);
                }
                else
                {
                  playerInventory.setItem(hotbarButton, null);
                }
                playerInventory.setLeggings(hotbarItemClone);
              }
            }
          }
          if (slotNumber == 36 && equipmentSlot.equals("BOOTS"))
          {
            boolean isBound = bootsExists && boots.getItemMeta().hasEnchant(Enchantment.BINDING_CURSE) && boots.getItemMeta().getEnchantLevel(
                    Enchantment.BINDING_CURSE) > 0;
            if (!isBound)
            {
              {
                event.setCancelled(true);
                ItemStack hotbarItemClone = Objects.requireNonNull(hotbarItem).clone();
                if (bootsExists)
                {
                  ItemStack bootsClone = boots.clone();
                  playerInventory.setItem(hotbarButton, bootsClone);
                }
                else
                {
                  playerInventory.setItem(hotbarButton, null);
                }
                playerInventory.setBoots(hotbarItemClone);
              }
            }
          }
        }
      }
    }
    // stash 메뉴에서는 사용 제한 태그로 인해 event가 cancel 되는것 우회
    if (!key.startsWith("stash-") && !UserData.EVENT_EXCEPTION_ACCESS.getBoolean(player.getUniqueId()))
    {
      if (slotNumber == -999)
      {
        return;
      }
      if (slotType == SlotType.OUTSIDE)
      {
        return;
      }
      if (clickType == ClickType.NUMBER_KEY)
      {
        ItemStack item = playerInventory.getItem(hotbarButton);
        if (ItemStackUtil.itemExists(item))
        {
          if (NBTAPI.isRestricted(player, item, RestrictionType.NO_CLICK_INVENTORY) || NBTAPI.isRestricted(player, placedBlockDataItemStack, RestrictionType.NO_BLOCK_CLICK_INVENTORY))
          {
            event.setCancelled(true);
            return;
          }
          if (NBTAPI.isRestricted(player, item, RestrictionType.NO_TRADE) || NBTAPI.isRestricted(player, placedBlockDataItemStack, RestrictionType.NO_BLOCK_TRADE))
          {
            if (clickedInventoryType == InventoryType.FURNACE)
            {
              if (slotType == SlotType.CRAFTING || slotType == SlotType.FUEL)
              {
                event.setCancelled(true);
                return;
              }
            }
            if (clickedInventoryType == InventoryType.BLAST_FURNACE)
            {
              if (slotType == SlotType.CRAFTING || slotType == SlotType.FUEL)
              {
                event.setCancelled(true);
                return;
              }
            }
            if (clickedInventoryType == InventoryType.SMOKER)
            {
              if (slotType == SlotType.CRAFTING || slotType == SlotType.FUEL)
              {
                event.setCancelled(true);
                return;
              }
            }
            if (event.getClickedInventory().getType() == InventoryType.BREWING)
            {
              if (slotType == SlotType.CRAFTING || slotType == SlotType.FUEL)
              {
                event.setCancelled(true);
                return;
              }
            }
            if (clickedInventoryType == InventoryType.SHULKER_BOX ||
                    clickedInventoryType == InventoryType.CHEST ||
                    clickedInventoryType == InventoryType.DISPENSER ||
                    clickedInventoryType == InventoryType.DROPPER ||
                    clickedInventoryType == InventoryType.HOPPER ||
                    clickedInventoryType == InventoryType.BARREL)
            {
              if (!openInventoryTitle.startsWith(Constant.TRASH_CAN) &&
                      !openInventoryTitle.startsWith(Constant.VIRTUAL_CHEST_ADMIN_MENU_PREFIX) &&
                      !openInventoryTitle.startsWith(Constant.VIRTUAL_CHEST_MENU_PREFIX) &&
                      !openInventoryTitle.contains(Constant.CUSTOM_RECIPE_CREATE_GUI) &&
                      !openInventoryTitle.contains(Constant.CUSTOM_RECIPE_MENU) &&
                      !openInventoryTitle.contains(Constant.CUSTOM_RECIPE_CRAFTING_MENU))
              {
                event.setCancelled(true);
                return;
              }
            }
          }
          if (NBTAPI.isRestricted(player, item, RestrictionType.NO_STORE) || NBTAPI.isRestricted(player, placedBlockDataItemStack, RestrictionType.NO_BLOCK_STORE))
          {
            if (clickedInventoryType == InventoryType.FURNACE)
            {
              if (event.getSlotType() == SlotType.CRAFTING || event.getSlotType() == SlotType.FUEL)
              {
                event.setCancelled(true);
                return;
              }
            }
            if (clickedInventoryType == InventoryType.BREWING)
            {
              if (event.getSlotType() == SlotType.CRAFTING || event.getSlotType() == SlotType.FUEL)
              {
                event.setCancelled(true);
                return;
              }
            }
            if (clickedInventoryType == InventoryType.BLAST_FURNACE)
            {
              if (event.getSlotType() == SlotType.CRAFTING || event.getSlotType() == SlotType.FUEL)
              {
                event.setCancelled(true);
                return;
              }
            }
            if (clickedInventoryType == InventoryType.SMOKER)
            {
              if (event.getSlotType() == SlotType.CRAFTING || event.getSlotType() == SlotType.FUEL)
              {
                event.setCancelled(true);
                return;
              }
            }
            if (clickedInventoryType == InventoryType.SHULKER_BOX ||
                    clickedInventoryType == InventoryType.CHEST ||
                    clickedInventoryType == InventoryType.DISPENSER ||
                    clickedInventoryType == InventoryType.DROPPER ||
                    clickedInventoryType == InventoryType.HOPPER ||
                    clickedInventoryType == InventoryType.BARREL ||
                    clickedInventoryType == InventoryType.ENDER_CHEST)
            {
              if (!openInventoryTitle.contains(Constant.TRASH_CAN) &&
                      !openInventoryTitle.startsWith(Constant.CUSTOM_RECIPE_CREATE_GUI) &&
                      !openInventoryTitle.contains(Constant.CUSTOM_RECIPE_MENU) &&
                      !openInventoryTitle.contains(Constant.CUSTOM_RECIPE_CRAFTING_MENU))
              {
                event.setCancelled(true);
                return;
              }
            }
          }
          if (NBTAPI.isRestricted(player, item, RestrictionType.NO_BREW))
          {
            if (clickedInventoryType == InventoryType.BREWING)
            {
              event.setCancelled(true);
              return;
            }
          }
          if (NBTAPI.isRestricted(player, item, RestrictionType.NO_DISPENSER))
          {
            if (clickedInventoryType == InventoryType.DISPENSER)
            {
              event.setCancelled(true);
              return;
            }
          }
          if (NBTAPI.isRestricted(player, item, RestrictionType.NO_DROPPER))
          {
            if (clickedInventoryType == InventoryType.DROPPER)
            {
              event.setCancelled(true);
              return;
            }
          }
          if (NBTAPI.isRestricted(player, item, RestrictionType.NO_HOPPER))
          {
            if (clickedInventoryType == InventoryType.HOPPER)
            {
              event.setCancelled(true);
              return;
            }
          }
          if (NBTAPI.isRestricted(player, item, RestrictionType.NO_GRINDSTONE))
          {
            if (clickedInventoryType == InventoryType.GRINDSTONE)
            {
              event.setCancelled(true);
              return;
            }
          }
          if (NBTAPI.isRestricted(player, item, RestrictionType.NO_STONECUTTER))
          {
            if (clickedInventoryType == InventoryType.STONECUTTER)
            {
              event.setCancelled(true);
              return;
            }
          }
          if (NBTAPI.isRestricted(player, item, RestrictionType.NO_CARTOGRAPHY_TABLE))
          {
            if (clickedInventoryType == InventoryType.CARTOGRAPHY)
            {
              event.setCancelled(true);
              return;
            }
          }
          if (NBTAPI.isRestricted(player, item, RestrictionType.NO_LOOM))
          {
            if (clickedInventoryType == InventoryType.LOOM)
            {
              event.setCancelled(true);
              return;
            }
          }
          if (NBTAPI.isRestricted(player, item, RestrictionType.NO_TRADE_VILLAGER))
          {
            if (clickedInventoryType == InventoryType.MERCHANT)
            {
              if (clickedIvnentory.getHolder() != null)
              {
                event.setCancelled(true);
                return;
              }
            }
          }
        }
      }
      else
      {
        if (event.getSlotType() != SlotType.OUTSIDE)
        {
          if (NBTAPI.isRestricted(player, current, RestrictionType.NO_CLICK_INVENTORY) || NBTAPI.isRestricted(player, placedBlockDataItemStack, RestrictionType.NO_BLOCK_CLICK_INVENTORY))
          {
            if (openInventoryType != InventoryType.CREATIVE)
            {
              event.setCancelled(true);
              return;
            }
          }
          if (NBTAPI.isRestricted(player, current, RestrictionType.NO_LOOT) || NBTAPI.isRestricted(player, placedBlockDataItemStack, RestrictionType.NO_BLOCK_LOOT))
          {
            if (openInventoryType != InventoryType.PLAYER && openInventoryType != InventoryType.CREATIVE && openInventoryType != InventoryType.CRAFTING)
            {
              event.setCancelled(true);
              return;
            }
          }
          if (NBTAPI.isRestricted(player, current, RestrictionType.NO_BREW))
          {
            if (openInventoryType == InventoryType.BREWING)
            {
              event.setCancelled(true);
              return;
            }
          }
          if (NBTAPI.isRestricted(player, current, RestrictionType.NO_DISPENSER))
          {
            if (player.getOpenInventory().getType() == InventoryType.DISPENSER)
            {
              event.setCancelled(true);
              return;
            }
          }
          if (NBTAPI.isRestricted(player, current, RestrictionType.NO_HOPPER))
          {
            if (player.getOpenInventory().getType() == InventoryType.HOPPER)
            {
              event.setCancelled(true);
              return;
            }
          }
          if (NBTAPI.isRestricted(player, current, RestrictionType.NO_DROPPER))
          {
            if (player.getOpenInventory().getType() == InventoryType.DROPPER)
            {
              event.setCancelled(true);
              return;
            }
          }
          if (NBTAPI.isRestricted(player, current, RestrictionType.NO_GRINDSTONE))
          {
            if (player.getOpenInventory().getType() == InventoryType.GRINDSTONE)
            {
              event.setCancelled(true);
              return;
            }
          }
          if (NBTAPI.isRestricted(player, current, RestrictionType.NO_STONECUTTER))
          {
            if (player.getOpenInventory().getType() == InventoryType.STONECUTTER)
            {
              event.setCancelled(true);
              return;
            }
          }
          if (NBTAPI.isRestricted(player, current, RestrictionType.NO_CARTOGRAPHY_TABLE))
          {
            if (player.getOpenInventory().getType() == InventoryType.CARTOGRAPHY)
            {
              event.setCancelled(true);
              return;
            }
          }
          if (NBTAPI.isRestricted(player, current, RestrictionType.NO_LOOM) || NBTAPI.isRestricted(player, placedBlockDataItemStack, RestrictionType.NO_BLOCK_LOOM))
          {
            if (player.getOpenInventory().getType() == InventoryType.LOOM)
            {
              event.setCancelled(true);
              return;
            }
          }
          if (NBTAPI.isRestricted(player, current, RestrictionType.NO_TRADE_VILLAGER))
          {
            if (player.getOpenInventory().getType() == InventoryType.MERCHANT)
            {
              if (player.getOpenInventory().getTopInventory().getHolder() != null)
              {
                event.setCancelled(true);
                return;
              }
            }
          }
          if (NBTAPI.isRestricted(player, current, RestrictionType.NO_TRADE) || NBTAPI.isRestricted(player, placedBlockDataItemStack, RestrictionType.NO_BLOCK_TRADE))
          {
            if ((player.getOpenInventory().getType() == InventoryType.BREWING) ||
                    (player.getOpenInventory().getType() == InventoryType.CHEST) ||
                    (player.getOpenInventory().getType() == InventoryType.DISPENSER) ||
                    (player.getOpenInventory().getType() == InventoryType.DROPPER) ||
                    (player.getOpenInventory().getType() == InventoryType.FURNACE) ||
                    (player.getOpenInventory().getType() == InventoryType.HOPPER) ||
                    (player.getOpenInventory().getType() == InventoryType.SHULKER_BOX) ||
                    (player.getOpenInventory().getType() == InventoryType.BARREL) ||
                    (player.getOpenInventory().getType() == InventoryType.BLAST_FURNACE) ||
                    (player.getOpenInventory().getType() == InventoryType.SMOKER))
            {
              if (!player.getOpenInventory().getTitle().startsWith(Constant.TRASH_CAN) &&
                      !player.getOpenInventory().getTitle().startsWith(Constant.VIRTUAL_CHEST_MENU_PREFIX) &&
                      !player.getOpenInventory().getTitle().startsWith(Constant.VIRTUAL_CHEST_ADMIN_MENU_PREFIX) &&
                      !player.getOpenInventory().getTitle().startsWith(Constant.CUSTOM_RECIPE_CREATE_GUI) &&
                      !player.getOpenInventory().getTitle().contains(Constant.CUSTOM_RECIPE_MENU) &&
                      !player.getOpenInventory().getTitle().contains(Constant.CUSTOM_RECIPE_CRAFTING_MENU))
              {
                event.setCancelled(true);
                return;
              }
            }
          }
          if (NBTAPI.isRestricted(player, current, RestrictionType.NO_STORE) || NBTAPI.isRestricted(player, placedBlockDataItemStack, RestrictionType.NO_BLOCK_STORE))
          {
            if ((player.getOpenInventory().getType() == InventoryType.BREWING) ||
                    (player.getOpenInventory().getType() == InventoryType.CHEST) ||
                    (player.getOpenInventory().getType() == InventoryType.DISPENSER) ||
                    (player.getOpenInventory().getType() == InventoryType.DROPPER) ||
                    (player.getOpenInventory().getType() == InventoryType.FURNACE) ||
                    (player.getOpenInventory().getType() == InventoryType.HOPPER) ||
                    (player.getOpenInventory().getType() == InventoryType.ENDER_CHEST) ||
                    (player.getOpenInventory().getType() == InventoryType.SHULKER_BOX) ||
                    (player.getOpenInventory().getType() == InventoryType.BARREL) ||
                    (player.getOpenInventory().getType() == InventoryType.BLAST_FURNACE) ||
                    (player.getOpenInventory().getType() == InventoryType.SMOKER))
            {
              if ((!player.getOpenInventory().getTitle().contains("§보§관§ §가§능")) || (!player.getOpenInventory().getTitle().contains("§")))
              {
                if (!player.getOpenInventory().getTitle().startsWith(Constant.TRASH_CAN) &&
                        !player.getOpenInventory().getTitle().startsWith(Constant.CUSTOM_RECIPE_CREATE_GUI) &&
                        !player.getOpenInventory().getTitle().contains(Constant.CUSTOM_RECIPE_MENU) &&
                        !player.getOpenInventory().getTitle().contains(Constant.CUSTOM_RECIPE_CRAFTING_MENU))
                {
                  event.setCancelled(true);
                  return;
                }
              }
            }
          }
        }
      }
    }

    // 휴대용 셜커 상자를 연 상태에서는 셜커 상자를 건드릴 수 없도록 함
    if (event.getInventory().getType() == InventoryType.SHULKER_BOX && title instanceof TranslatableComponent translatableComponent)
    {
      List<Component> args = translatableComponent.args();
      if (args.size() == 2 && args.get(1) instanceof TextComponent textComponent && textComponent.content().startsWith(Constant.ITEM_PORTABLE_SHULKER_BOX_GUI))
      {
        if (clickType == ClickType.SWAP_OFFHAND)
        {
          ItemStack offhand = playerInventory.getItemInOffHand();
          if (ItemStackUtil.itemExists(offhand))
          {
            if (Constant.SHULKER_BOXES.contains(offhand.getType()))
            {
              event.setCancelled(true);
              return;
            }
          }
        }
        if (clickType == ClickType.NUMBER_KEY)
        {
          ItemStack item = player.getInventory().getItem(event.getHotbarButton());
          if (ItemStackUtil.itemExists(item))
          {
            if (Constant.SHULKER_BOXES.contains(item.getType()))
            {
              event.setCancelled(true);
              return;
            }
          }
        }
        if (slotNumber < 9 && ItemStackUtil.itemExists(event.getCurrentItem()))
        {
          if (Constant.SHULKER_BOXES.contains(event.getCurrentItem().getType()))
          {
            event.setCancelled(true);
            return;
          }
        }
      }
    }

    if (!event.isCancelled())
    {
      this.itemLore(event, player);
      this.gui(event);
      this.gui2(event);
      this.unbindingShears(event);
    }
  }

  private void unbindingShears(InventoryClickEvent event)
  {
    ItemStack cursor = event.getCursor(), current = event.getCurrentItem();
    CustomMaterial customMaterial = CustomMaterial.itemStackOf(cursor);
    if (cursor != null && customMaterial == CustomMaterial.UNBINDING_SHEARS && current != null && current.hasItemMeta() && current.getItemMeta().hasEnchant(Enchantment.BINDING_CURSE))
    {
      if (NBTAPI.isRestricted(current, RestrictionType.NO_UNBINDING_SHEARS))
      {
        return;
      }
      Player player = (Player) event.getWhoClicked();
      ClickType clickType = event.getClick();
      SlotType slotType = event.getSlotType();
      if (clickType == ClickType.RIGHT && slotType == SlotType.ARMOR)
      {
        PlayerInventory playerInventory = player.getInventory();
        if (playerInventory.firstEmpty() == -1)
        {
          MessageUtil.sendWarn(player, "인벤토리 공간이 부족하여 %s을(를) 사용할 수 없습니다!", cursor);
          return;
        }
        player.setItemOnCursor(null);
        current = current.clone();
        event.setCurrentItem(null);
        playerInventory.addItem(current);
        Method.playSound(player, Sound.ENTITY_SHEEP_SHEAR, SoundCategory.PLAYERS);
      }
    }
  }

  private void itemLore(InventoryClickEvent event, Player player)
  {
    // 소리 블록 설명 업데이트 감지
    if (player.getGameMode() == GameMode.CREATIVE) // 소리 블록을 픽블록 했을 때 해당 블록의 음높이와 악기를 아이템 설명에 복사하는 기능
    {
      UUID uuid = player.getUniqueId();
      boolean copyBlockData = UserData.COPY_BLOCK_DATA.getBoolean(uuid);
      boolean copyBlockDataWhenSneaking = !UserData.COPY_BLOCK_DATA_WHEN_SNEAKING.getBoolean(uuid) || player.isSneaking();

      if (copyBlockData && copyBlockDataWhenSneaking)
      {
        ItemStack cursor = event.getCursor();
        if (ItemStackUtil.itemExists(cursor))
        {
          Block block = player.getTargetBlock(null, 6);
          Material blockType = block.getType();
          if (cursor.getType() != Material.NOTE_BLOCK || block.getType() != Material.NOTE_BLOCK)
          {
            if (BlockDataInfo.getBlockDataKeys(blockType) != null)
            {
              try
              {
                String blockDataString = block.getBlockData().getAsString();
                blockDataString = blockDataString.split("\\[")[1].replace("]", "");
                String[] dataArray = blockDataString.split(",");
                NBTItem nbtItem = new NBTItem(cursor);
                NBTCompound blockStateTag = nbtItem.addCompound(CucumberyTag.MINECRAFT_BLOCK_STATE_TAG_KEY);
                for (String data : dataArray)
                {
                  String[] split = data.split("=");
                  String key = split[0];
                  switch (key)
                  {
                    case "facing":
                      if (!UserData.COPY_BLOCK_DATA_FACING.getBoolean(uuid))
                      {
                        continue;
                      }
                    case "waterlogged":
                      if (!UserData.COPY_BLOCK_DATA_WATERLOGGED.getBoolean(uuid))
                      {
                        continue;
                      }
                  }
                  String value = split[1];
                  blockStateTag.setString(key, value);
                }
                if (!blockStateTag.getKeys().isEmpty())
                {
                  cursor = nbtItem.getItem();
                  event.setCursor(cursor);
                  ItemStackUtil.updateInventory(player);
                }
              }
              catch (Exception e)
              {
Cucumbery.getPlugin().getLogger().warning(                e.getMessage());
              }
            }
          }
        }
      }

      boolean copyPitch = UserData.COPY_NOTE_BLOCK_PITCH.getBoolean(uuid);
      boolean copyInstrument = UserData.COPY_NOTE_BLOCK_INSTRUMENT.getBoolean(uuid);
      boolean copyWhenSneaking = !UserData.COPY_NOTE_BLOCK_VALUE_WHEN_SNEAKING.getBoolean(uuid) || player.isSneaking();
      if (copyWhenSneaking && (copyPitch || copyInstrument)) // 음높이 또는 악기 복사 기능을 하나 이상 켠 경우
      {
        ItemStack cursor = event.getCursor();
        Set<Material> transparent = new HashSet<>();
        for (Material material : Material.values())
        {
          if (!material.isOccluding())
          {
            transparent.add(material);
          }
        }
        Block block = player.getTargetBlock(transparent, 6);
        if (Objects.requireNonNull(cursor).getType() == Material.NOTE_BLOCK && block.getType() == Material.NOTE_BLOCK)
        {
          try
          {
            String blockDataString = block.getBlockData().getAsString();
            blockDataString = blockDataString.split("\\[")[1].replace("]", "");
            String[] dataArray = blockDataString.split(",");
            NBTItem nbtItem = new NBTItem(cursor);
            NBTCompound blockStateTag = nbtItem.addCompound(CucumberyTag.MINECRAFT_BLOCK_STATE_TAG_KEY);
            for (String data : dataArray)
            {
              String[] split = data.split("=");
              String key = split[0];
              String value = split[1];
              if (key.equals("instrument") && copyInstrument)
              {
                blockStateTag.setString(key, value);
              }
              if (key.equals("note") && copyPitch)
              {
                blockStateTag.setString(key, value);
              }
            }
            cursor = nbtItem.getItem();
            event.setCursor(cursor);
            ItemStackUtil.updateInventory(player);
          }
          catch (Exception ignored)
          {

          }
        }
      }
    }

    int noteSlot = event.getSlot(); // 소리 블록을 인벤토리가 가득 찬 상태에서 휠클릭 하면 간헐적으로 인벤토리에 기존 소리 블록이 생기는 버그를 고치기 위해 필요한 슬롯 변수

    if (Method.usingLoreFeature(player))
    {
      // 석재 절단기 아이템 제작 설명 추가
      if (event.getInventory().getType() == InventoryType.STONECUTTER)
      {
        if ((event.getClick() == ClickType.SHIFT_LEFT || event.getClick() == ClickType.SHIFT_RIGHT) &&
                event.getSlot() == 1 &&
                event.getAction() == InventoryAction.MOVE_TO_OTHER_INVENTORY &&
                event.getSlotType() == InventoryType.SlotType.RESULT)
        {
          ItemStack ingre = event.getInventory().getItem(0); // 절단할 돌을 넣는 곳에 있는 아이템
          ItemStack current = Objects.requireNonNull(event.getCurrentItem()).clone(); // 결과물 슬롯에 있는 아이템 (1~2개)
          int ingreAmount = Objects.requireNonNull(ingre).getAmount(); // 재료의 개수
          int currentAmount = current.getAmount(); // 결과물의 개수
          if (ItemStackUtil.countSpace(player.getInventory(), current) == 1 && currentAmount == 2)
          {
            event.setCancelled(true);
            return;
          }
          Bukkit.getServer().getScheduler().runTaskLater(Cucumbery.getPlugin(), () ->
          {
            for (int i = 0; i < player.getInventory().getSize(); i++)
            {
              ItemStack item = player.getInventory().getItem(i);
              if (item != null && item.getType() == current.getType() && !item.getItemMeta().hasLore())
              {
                player.getInventory().remove(item);
              }
            }
            int space = ItemStackUtil.countSpace(player.getInventory(), current); // 해당 아이템이 인벤토리에 들어갈 수 있는 최대 수
            int giveAmount = (ingreAmount - 1) * currentAmount; // 지급할 아이템의 양
            int realAmount = Math.min(space, giveAmount); // 실제로 지급할 아이템의 양
            if (realAmount > 0)
            {
              ingre.setAmount(ingreAmount - 1);
            }
            for (int i = 0; i < ingreAmount - 1; i++)
            {
              if (space < currentAmount)
              {
                event.setCancelled(true);
                break;
              }
              else
              {
                player.getInventory().addItem(current);
                current.setAmount(currentAmount);
                ingre.setAmount(ingre.getAmount() - 1);
                space -= currentAmount;
              }
            }
            if (ingre.getAmount() == 0)
            {
              event.setCurrentItem(null);
            }
            player.updateInventory();
          }, 0L);
        }
      }

      // 꾸러미에 아이템 넣고 뺄 때 설명 업데이트
      ItemStack currentItem = event.getCurrentItem(), cursorItem = event.getCursor();
      if (ItemStackUtil.itemExists(currentItem) && currentItem.getType() == Material.BUNDLE || ItemStackUtil.itemExists(cursorItem) && cursorItem.getType() == Material.BUNDLE)
      {
        Bukkit.getScheduler().runTaskLater(Cucumbery.getPlugin(), () -> ItemStackUtil.updateInventory(player), 0L);
      }
    }


    if (event.getInventory().getType() == InventoryType.CARTOGRAPHY) // 지도 제작대를 이용한 뒤 결과 아이템 설명 업데이트
    {
      Bukkit.getScheduler().runTaskLater(Cucumbery.getPlugin(), () ->
              ItemStackUtil.updateInventory(player), 0L);
    }
    if (event.getInventory().getType() == InventoryType.GRINDSTONE)
    {
      GrindstoneInventory grindstoneInventory = (GrindstoneInventory) event.getInventory();
      ItemStack result = grindstoneInventory.getResult();
      Bukkit.getScheduler().runTaskLater(Cucumbery.getPlugin(), () ->
      {
        if (result != null)
        {
          ItemLore.setItemLore(result, new ItemLoreView(player));
        }
      }, 0L);
    }
    if (player.getGameMode() == GameMode.CREATIVE)
    {
      // 픽블록 기능이 크리에이티브 인벤토리에서만 호출되야함
      if (player.getOpenInventory().getType() != InventoryType.CREATIVE)
      {
        return;
      }
      ItemStack current = player.getInventory().getItemInMainHand();
      ItemStack cursor = Objects.requireNonNull(event.getCursor()).clone();
      ItemLore.setItemLore(cursor, new ItemLoreView(player));
      ItemLore.setItemLore(event.getCursor(), new ItemLoreView(player));
      boolean barHasEmptySlot = false;
      ItemStack[] hotBars = new ItemStack[9];
      List<Material> types = new ArrayList<>();
      for (int i = 0; i < 9; i++)
      {
        ItemStack hotBarItem = player.getInventory().getItem(i);
        if (hotBarItem != null)
        {
          hotBars[i] = hotBarItem;
          types.add(hotBarItem.getType());
        }
        else
        {
          hotBars[i] = new ItemStack(Material.AIR);
          types.add(Material.AIR);
          barHasEmptySlot = true;
        }
      }

      // 픽블록으로 꺼낸 블록이 핫바에 존재하지 않는 블록일 때
      if (event.getSlot() >= 0 && event.getSlot() <= 8 && !types.contains(cursor.getType()))
      {
        int firstEmpty = player.getInventory().firstEmpty();
        if (firstEmpty != -1)
        {
          player.getInventory().setItem(firstEmpty, new ItemStack(Material.AIR));
        }
      }

      else if (current.getType() == Material.AIR) // 손에 아무것도 안들고 있을때 아이템을 픽븍록 할 때
      {
        if (event.getSlot() == player.getInventory().getHeldItemSlot()) // 픽블록 한 슬롯과 플레이어가 들고 있는 슬롯이 같은지 확인
        {
          for (int i = 0; i < hotBars.length; i++)
          {
            ItemStack hotBar = hotBars[i].clone();
            hotBar.setAmount(1);
            if (ItemStackUtil.isPickBlockable(cursor.getType()) && hotBar.equals(cursor) && cursor.equals(ItemStackUtil.loredItemStack(cursor.getType())) && i != event.getSlot())
            {
              if (!check.contains(player))
              {
                check.add(player);
                final int j = i;
                Bukkit.getServer().getScheduler().runTaskLater(Cucumbery.getPlugin(), () ->
                {
                  player.getInventory().setItem(event.getSlot(), null);
                  player.getInventory().setHeldItemSlot(j);
                  check.remove(player);
                }, 2L);
              }
              break;
            }
          }
        }
      }
      else if (!barHasEmptySlot) // 핫바가 가득 찬 상태에서 픽블록 할 경우
      {
        for (int i = 0; i < hotBars.length; i++)
        {
          ItemStack hotBar = hotBars[i].clone();
          hotBar.setAmount(1);
          if (ItemStackUtil.isPickBlockable(cursor.getType()) && hotBar.equals(cursor))
          {
            ItemStack currentClone = current.clone();
            currentClone.setAmount(1);
            if (cursor.equals(currentClone)) // 손에 들고 있는 아이템이랑 픽블록 아이템의 종류가 같을 때
            {
              int firstEmpty = player.getInventory().firstEmpty();
              if (firstEmpty != -1)
              {
                player.getInventory().setItem(firstEmpty, new ItemStack(Material.AIR));
              }
              Bukkit.getServer().getScheduler().runTaskLater(Cucumbery.getPlugin(), () ->
              {
                player.getInventory().setItemInMainHand(current);
                Method.heldItemSound(player, current);
              }, 0L);
              break;
            }
            else if (i != event.getSlot())
            {
              if (!check.contains(player))
              {
                check.add(player);
                final int j = i;
                int firstEmpty = player.getInventory().firstEmpty();
                if (firstEmpty != -1)
                {
                  player.getInventory().setItem(firstEmpty, new ItemStack(Material.AIR));
                }
                Bukkit.getServer().getScheduler().runTaskLater(Cucumbery.getPlugin(), () ->
                {
                  player.getInventory().setItem(event.getSlot(), current);
                  player.getInventory().setHeldItemSlot(j);
                  check.remove(player);
                }, 2L);
              }
              break;
            }
          }
        }
      }
      if (!check.contains(player))
      {
        check.add(player);
        Bukkit.getServer().getScheduler().runTaskLater(Cucumbery.getPlugin(), () -> check.remove(player), 2L);
      }
    }
  }

  /**
   * 커스텀 레시피 GUI 전용 혹은 클릭해서 아이템을 일부 혹은 전체를 넣고 뺄 수 있는 인벤토리
   *
   * @param event 이벤트
   */
  private void gui2(InventoryClickEvent event)
  {
    if (event.isCancelled())
    {
      return;
    }
    Player player = (Player) event.getWhoClicked();
    UUID uuid = player.getUniqueId();
    if (Variable.scrollReinforcing.contains(uuid))
    {
      event.setCancelled(true);
      return;
    }
    String title = event.getView().getTitle();
    if (!title.startsWith(Constant.CUSTOM_RECIPE_CREATE_GUI))
    {
      return;
    }
    if (event.getSlotType() == SlotType.OUTSIDE)
    {
      return;
    }
    if (Objects.requireNonNull(event.getClickedInventory()).getType() == InventoryType.PLAYER)
    {
      return;
    }
    if (player.getOpenInventory().getType() != InventoryType.CHEST)
    {
      return;
    }
    ItemStack item = event.getCurrentItem();
    if (!ItemStackUtil.hasItemMeta(item))
    {
      return;
    }
    int random = Method.random(1, 10000);
    boolean ohYes = random >= 9700;
    if (ohYes && Cucumbery.config.getBoolean("gui-easter-egg-sound"))
    {
      Method.playSound(player, Sound.ENTITY_VILLAGER_AMBIENT);
    }
    Method.playSound(player, Sound.BLOCK_NOTE_BLOCK_HAT, 1F, 1.4F);
    String itemName = Objects.requireNonNull(item).getItemMeta().getDisplayName();
    if (itemName.startsWith("§와") && itemName.contains("§와"))
    {
      event.setCancelled(true);
      return;
    }

    int s = event.getSlot();

    if (s != 4 && s < 27)
    {
      event.setCancelled(true);
    }

    ClickType clickType = event.getClick();
    if (s == 0 && clickType == ClickType.SHIFT_LEFT)
    {
      event.getInventory().setItem(0, null);
      player.closeInventory();
      player.updateInventory();
      String category = Method.deformat(title.split(Method.format("category:", "§"))[1].split(Method.format("recipe:", "§"))[0], "§");
      String recipe = Method.deformat(title.split(Method.format("recipe:", "§"))[1], "§");
      CustomConfig customRecipeListConfig = CustomConfig.getCustomConfig("data/CustomRecipe/" + category + ".yml");
      YamlConfiguration config = customRecipeListConfig.getConfig();
      config.set("recipes." + recipe, null);
      ConfigurationSection configurationSection = config.getConfigurationSection("recipes");
      boolean removeCategory = configurationSection == null || configurationSection.getKeys(false).isEmpty();
      if (removeCategory)
      {
        customRecipeListConfig.delete();
        Variable.customRecipes.remove(category);
      }
      else
      {
        customRecipeListConfig.saveConfig();
        Variable.customRecipes.put(category, config);
      }
      String categoryDisplay = config.getString("extra.display");
      if (categoryDisplay == null)
      {
        categoryDisplay = category;
      }
      String recipeDisplay = config.getString("recipes." + recipe + ".extra.display");
      if (recipeDisplay == null)
      {
        recipeDisplay = recipe;
      }
      MessageUtil.sendMessage(player, Prefix.INFO_CUSTOM_RECIPE, "커스텀 레시피 목록 %s(%s)에서 %s(%s) 레시피를 제거했습니다", category, ComponentUtil.create("§e" + categoryDisplay), recipe, ComponentUtil.create("§e" + recipeDisplay));
      for (File logFile : CustomRecipeUtil.getPlayersCraftLog())
      {
        CustomRecipeUtil.removePlayerCraftLog(logFile.getName().substring(0, logFile.getName().length() - 4), category, removeCategory ? null : recipe);
        CustomRecipeUtil.removePlayerLastCraftLog(logFile.getName().substring(0, logFile.getName().length() - 4), category, removeCategory ? null : recipe);
      }
    }

    // 생성/편집 버튼을 누름
    if (s == 22)
    {
      event.getInventory().setItem(22, null);
      player.closeInventory();
    }
  }

  private void gui(InventoryClickEvent event)
  {
    if (event.isCancelled())
    {
      return;
    }
    if (event.getInventory().getLocation() != null)
    {
      return;
    }
    Player player = (Player) event.getWhoClicked();
    UUID uuid = player.getUniqueId();
    if (Variable.scrollReinforcing.contains(uuid))
    {
      event.setCancelled(true);
      return;
    }
    ClickType clickType = event.getClick();
    Component title = event.getView().title();
    if (event.getView().getTitle().startsWith(Constant.CANCEL_STRING) || GUIManager.isGUITitle(title))
    {
      event.setCancelled(true);
    }
    else
    {
      return;
    }
    if (event.getSlotType() == SlotType.OUTSIDE)
    {
      return;
    }
    if (Objects.requireNonNull(event.getClickedInventory()).getType() == InventoryType.PLAYER)
    {
      return;
    }
    if (player.getOpenInventory().getType() != InventoryType.CHEST)
    {
      return;
    }
    ItemStack item = event.getCurrentItem();
    if (!ItemStackUtil.hasItemMeta(item))
    {
      return;
    }
    Material type = Objects.requireNonNull(item).getType();
    String invName = event.getView().getTitle();
    String itemName = ComponentUtil.serialize(ItemNameUtil.itemName(item));
    if (itemName.equals(""))
    {
      return;
    }
    int random = Method.random(1, 10000);
    boolean ohYes = random >= 9700;
    if (ohYes && Cucumbery.config.getBoolean("gui-easter-egg-sound"))
    {
      Method.playSound(player, Sound.ENTITY_VILLAGER_AMBIENT);
    }
    if ((GUIManager.isGUITitle(title) || invName.contains(Constant.CANCEL_STRING)) && clickType == ClickType.DOUBLE_CLICK)
    {
      event.setCancelled(true);
      return;
    }
    Method.playSound(player, Sound.UI_BUTTON_CLICK);
    InventoryView lastInventory = GUIManager.getLastInventory(uuid);
    int slot = event.getSlot();
    if (GUIManager.isGUITitle(title))
    {
      String key = GUIManager.getGUIKey(title);
      if (Constant.POTION_EFFECTS.equals(key))
      {
        if (event.getClick() == ClickType.RIGHT || event.getClick() == ClickType.LEFT)
        {
          NBTItem nbtItem = new NBTItem(item);
          String removeEffect = nbtItem.getString("removeEffect");
          if (removeEffect != null)
          {
            try
            {
              if (event.getClick() == ClickType.RIGHT)
              {
                if (removeEffect.startsWith("potion:"))
                {
                  removeEffect = removeEffect.substring("potion:".length());
                  PotionEffectType potionEffectType = PotionEffectType.getByName(removeEffect);
                  if (!CustomEffectManager.hasEffect(player, CustomEffectType.NO_BUFF_REMOVE) && potionEffectType != null)
                  {
                    player.removePotionEffect(potionEffectType);
                  }
                }
              }
              if (removeEffect.startsWith("custom:"))
              {
                removeEffect = removeEffect.substring("custom:".length());
                CustomEffectType effectType = CustomEffectType.valueOf(removeEffect);
                if (event.getClick() == ClickType.RIGHT)
                {
                  Integer amplifier = nbtItem.getInteger("removeEffectAmplifier");
                  if (amplifier == null)
                  {
                    CustomEffectManager.removeEffect(player, effectType, RemoveReason.GUI);
                  }
                  else
                  {
                    CustomEffectManager.removeEffect(player, effectType, amplifier, RemoveReason.GUI);
                  }
                  if (effectType == CustomEffectType.CONTINUAL_SPECTATING && player.getGameMode() == GameMode.SPECTATOR && player.getSpectatorTarget() instanceof Player)
                  {
                    player.setSpectatorTarget(null);
                  }
                  if (effectType == CustomEffectType.CONTINUAL_SPECTATING_EXEMPT)
                  {
                    player.closeInventory();
                  }
                  if (effectType == CustomEffectType.CONTINUAL_RIDING && player.getVehicle() != null)
                  {
                    player.leaveVehicle();
                  }
                  if (effectType == CustomEffectType.CONTINUAL_RIDING_EXEMPT)
                  {
                    player.closeInventory();
                  }
                }
                else
                {
                  if (effectType == CustomEffectType.CONTINUAL_SPECTATING && player.getGameMode() == GameMode.SPECTATOR && player.getSpectatorTarget() instanceof Player)
                  {
                    CustomEffectManager.addEffect(player, CustomEffectType.CONTINUAL_SPECTATING_EXEMPT);
                    player.closeInventory();
                    player.setSpectatorTarget(null);
                  }
                  if (effectType == CustomEffectType.CONTINUAL_RIDING && player.getVehicle() != null)
                  {
                    CustomEffectManager.addEffect(player, CustomEffectType.CONTINUAL_RIDING_EXEMPT);
                    player.closeInventory();
                    player.leaveVehicle();
                  }
                  if (effectType == CustomEffectType.POSITION_MEMORIZE)
                  {
                    CustomEffectManager.removeEffect(player, CustomEffectType.POSITION_MEMORIZE, RemoveReason.PLUGIN);
                  }
                }
              }
            }
            catch (Exception ignored)
            {

            }
          }
        }
      }
      if (lastInventory != null && slot == 45)
      {
        player.openInventory(lastInventory);
      }
      if (key.startsWith("stash-") && key.replace("stash-", "").equals(uuid.toString()))
      {
        boolean slotValid = (slot == 49 && type != Material.BARRIER) || slot <= 44;
        List<ItemStack> stash = Variable.itemStash.get(uuid);
        if (slot == 49 && type != Material.BARRIER)
        {
          List<ItemStack> clone = new ArrayList<>(stash);
          stash.clear();
          AddItemUtil.addItem(player, clone);
          SoundPlay.playSound(player, Sound.BLOCK_NOTE_BLOCK_PLING, SoundCategory.PLAYERS, 2);
          MessageUtil.sendMessage(player, Prefix.INFO_STASH, "보관된 아이템 전부를 수령했습니다!");
        }
        if (slot <= 44)
        {
          ItemStack itemStack = stash.get(slot);
          if (ItemStackUtil.countSpace(player, itemStack) >= itemStack.getAmount())
          {
            stash.remove(slot);
            AddItemUtil.addItem(player, itemStack);
            MessageUtil.sendMessage(player, Prefix.INFO_STASH, "%s을(를) 수령했습니다!", itemStack);
            SoundPlay.playSound(player, Sound.BLOCK_NOTE_BLOCK_PLING, SoundCategory.PLAYERS, 2);
          }
          else
          {
            slotValid = false;
          }
        }
        if (slotValid)
        {
          Variable.itemStash.put(uuid, stash);
          boolean slotEmpty = Variable.itemStash.get(uuid).isEmpty();
          if (!slotEmpty)
          {
            GUIManager.openGUI(player, GUIType.ITEM_STASH, false);
          }
          else
          {
            player.closeInventory();
          }
        }
      }
    }
    if (invName.equalsIgnoreCase(Constant.CANCEL_STRING + Constant.MAIN_MENU))
    {
      if (lastInventory != null && slot == 0)
      {
        player.openInventory(lastInventory);
      }

      if (slot == 3)
      {
        GUIManager.openGUI(player, GUIType.SERVER_SETTINGS);
      }

//      else if (slot == 5 && Cucumbery.config.getBoolean("rpg-enabled") && !Cucumbery.config.getStringList("no-rpg-enabled-worlds").contains(player.getLocation().getWorld().getName()))
//      {
//        StatGUI.getStatGUI().statGUI(player);
//      }

      else if (slot == 5)
      {
        RecipeInventoryMainMenu.openRecipeInventory(player, 1, true);
      }
    }
    else if (invName.equals(Constant.CANCEL_STRING + Constant.SERVER_SETTINGS))
    {
      if (CustomEffectManager.hasEffect(player, CustomEffectTypeCooldown.COOLDOWN_GUI_BUTTON))
      {
//        MessageUtil.sendWarn(player, "좀 천천히 누르삼!");
        return;
      }
      boolean saveConfig = false;
      switch (slot)
      {
        case 1:
          UserData.LISTEN_JOIN.setToggle(uuid);
          saveConfig = true;
          break;
        case 2:
          UserData.SHOW_JOIN_TITLE.setToggle(uuid);
          saveConfig = true;
          break;
        case 3:
          UserData.LISTEN_CHAT.setToggle(uuid);
          saveConfig = true;
          break;
        case 4:
          UserData.LISTEN_COMMAND.setToggle(uuid);
          saveConfig = true;
          break;
        case 5:
          UserData.LISTEN_GLOBAL.setToggle(uuid);
          saveConfig = true;
          break;
        case 6:
          UserData.SERVER_RESOURCEPACK.setToggle(uuid);
          saveConfig = true;
          break;
        case 10:
          UserData.LISTEN_QUIT.setToggle(uuid);
          saveConfig = true;
          break;
        case 11:
          UserData.SHOW_ACTIONBAR_ON_ATTACK.setToggle(uuid);
          saveConfig = true;
          break;
        case 12:
          UserData.SHOW_ACTIONBAR_ON_ATTACK_PVP.setToggle(uuid);
          saveConfig = true;
          break;
        case 13:
          UserData.FIREWORK_LAUNCH_ON_AIR.setToggle(uuid);
          saveConfig = true;
          break;
        case 14:
          UserData.TRAMPLE_SOIL.setToggle(uuid);
          saveConfig = true;
          break;
        case 15:
          UserData.TRAMPLE_SOIL_ALERT.setToggle(uuid);
          saveConfig = true;
          break;
        case 18:
          if (Permission.GUI_SERVER_SETTINGS_ADMIN.has(player))
          {
            GUIManager.openGUI(player, GUIManager.GUIType.SERVER_SETTINGS_ADMIN);
          }
          break;
        case 19:
          UserData.SHOW_DEATH_SELF_MESSAGE.setToggle(uuid);
          saveConfig = true;
          break;
        case 20:
          UserData.LISTEN_HELDITEM.setToggle(uuid);
          saveConfig = true;
          break;
        case 21:
          UserData.LISTEN_CONTAINER.setToggle(uuid);
          saveConfig = true;
          break;
        case 22:
          UserData.LISTEN_ITEM_DROP.setToggle(uuid);
          saveConfig = true;
          break;
        case 23:
          UserData.SHOW_ACTIONBAR_ON_ITEM_DROP.setToggle(uuid);
          saveConfig = true;
          break;
        case 24:
          UserData.SHOW_ACTIONBAR_ON_ITEM_PICKUP.setToggle(uuid);
          saveConfig = true;
          break;
        case 28:
          UserData.SHOW_DEATH_MESSAGE.setToggle(uuid);
          saveConfig = true;
          break;
        case 29:
          UserData.SHOW_ITEM_BREAK_TITLE.setToggle(uuid);
          saveConfig = true;
          break;
        case 30:
          UserData.NOTIFY_IF_INVENTORY_IS_FULL.setToggle(uuid);
          saveConfig = true;
          break;
        case 31:
          UserData.SHOW_ACTIONBAR_WHEN_ITEM_IS_COOLDOWN.setToggle(uuid);
          saveConfig = true;
          break;
//        case 32:
//          UserData.SHOW_DROPPED_ITEM_CUSTOM_NAME.setToggle(uuid);
//          saveConfig = true;
//          break;
        case 33:
          UserData.SHOW_DAMAGE_INDICATOR.setToggle(uuid);
          saveConfig = true;
          break;
        case 37:
          UserData.SHOW_DEATH_PVP_MESSAGE.setToggle(uuid);
          saveConfig = true;
          break;
        case 39:
          GUIManager.openGUI(player, GUIType.ITEM_DROP_MODE_MENU);
          break;
        case 41:
          GUIManager.openGUI(player, GUIType.ITEM_PICKUP_MODE_MENU);
          break;
//        case 41:
//          GUI.openGUI(player, GUIType.ITEM_USE_MODE_MENU);
//          break;
        case 7:
          if (player.getGameMode() == GameMode.CREATIVE)
          {
            UserData.COPY_NOTE_BLOCK_VALUE_WHEN_SNEAKING.setToggle(uuid);
            saveConfig = true;
          }
          break;
        case 16:
          if (player.getGameMode() == GameMode.CREATIVE)
          {
            UserData.COPY_NOTE_BLOCK_INSTRUMENT.setToggle(uuid);
            saveConfig = true;
          }
          break;
        case 25:
          if (player.getGameMode() == GameMode.CREATIVE)
          {
            UserData.COPY_NOTE_BLOCK_PITCH.setToggle(uuid);
            saveConfig = true;
          }
          break;
        case 34:
          if (player.getGameMode() == GameMode.CREATIVE)
          {
            UserData.PLAY_NOTE_BLOCK_WHEN_SNEAKING_IN_CREATIVE_MODE.setToggle(uuid);
            saveConfig = true;
          }
          break;
        case 43:
          if (player.getGameMode() == GameMode.CREATIVE)
          {
            UserData.INVERT_NOTE_BLOCK_PITCH_WHEN_SNEAKING_IN_CREATIVE_MODE.setToggle(uuid);
            saveConfig = true;
          }
          break;
        case 45:
          if (lastInventory != null)
          {
            player.openInventory(lastInventory);
          }
          break;
        case 53:
          GUIManager.openGUI(player, GUIType.MAIN_MENU);
          break;
        default:
          break;
      }
      if (saveConfig)
      {
        CustomEffectManager.addEffect(player, CustomEffectTypeCooldown.COOLDOWN_GUI_BUTTON);
        GUIManager.openGUI(player, GUIType.SERVER_SETTINGS);
      }
    }
    else if (invName.equals(Constant.CANCEL_STRING + Constant.SERVER_SETTINGS_ADMIN))
    {
      boolean saveConfig = false;
      switch (slot)
      {
        case 0:
          UserData.PLAY_JOIN.setToggle(uuid);
          saveConfig = true;
          break;
        case 1:
          UserData.PLAY_JOIN_FORCE.setToggle(uuid);
          saveConfig = true;
          break;
        case 2:
          UserData.PLAY_QUIT.setToggle(uuid);
          saveConfig = true;
          break;
        case 3:
          UserData.PLAY_QUIT_FORCE.setToggle(uuid);
          saveConfig = true;
          break;
        case 4:
          UserData.LISTEN_JOIN_FORCE.setToggle(uuid);
          saveConfig = true;
          break;
        case 5:
          UserData.LISTEN_QUIT_FORCE.setToggle(uuid);
          saveConfig = true;
          break;
        case 6:
          UserData.PLAY_CHAT.setToggle(uuid);
          saveConfig = true;
          break;
        case 7:
          UserData.PLAY_CHAT_FORCE.setToggle(uuid);
          saveConfig = true;
          break;
        case 8:
          UserData.LISTEN_CHAT_FORCE.setToggle(uuid);
          saveConfig = true;
          break;
        case 9:
          UserData.LISTEN_GLOBAL_FORCE.setToggle(uuid);
          saveConfig = true;
          break;
        case 10:
          player.sendMessage("아ㅏ ㅏㅏㅏ하기시리ㅓ 귀차나");
          break;
        case 45:
          if (lastInventory != null)
          {
            player.openInventory(lastInventory);
          }
          break;
        case 53:
          GUIManager.openGUI(player, GUIType.MAIN_MENU);
          break;
        default:
          break;
      }
      if (saveConfig)
      {
        GUIManager.openGUI(player, GUIType.SERVER_SETTINGS_ADMIN);
      }
    }
    else if (invName.equals(Constant.CANCEL_STRING + Constant.ITEM_DROP_MODE_MENU))
    {
      boolean saveConfig = false;
      switch (slot)
      {
        case 11:
          UserData.ITEM_DROP_MODE.set(uuid, "normal");
          saveConfig = true;
          break;
        case 13:
          UserData.ITEM_DROP_MODE.set(uuid, "sneak");
          saveConfig = true;
          break;
        case 15:
          UserData.ITEM_DROP_MODE.set(uuid, "disabled");
          saveConfig = true;
          break;
        case 18:
          if (lastInventory != null)
          {
            player.openInventory(lastInventory);
          }
          break;
        case 26:
          GUIManager.openGUI(player, GUIType.MAIN_MENU);
          break;
        default:
          break;
      }
      if (saveConfig)
      {
        GUIManager.openGUI(player, GUIType.ITEM_DROP_MODE_MENU);
      }
    }
    else if (invName.equals(Constant.CANCEL_STRING + Constant.ITEM_PICKUP_MODE_MENU))
    {
      boolean saveConfig = false;
      switch (slot)
      {
        case 11:
          UserData.ITEM_PICKUP_MODE.set(uuid, "normal");
          saveConfig = true;
          break;
        case 13:
          UserData.ITEM_PICKUP_MODE.set(uuid, "sneak");
          saveConfig = true;
          break;
        case 15:
          UserData.ITEM_PICKUP_MODE.set(uuid, "disabled");
          saveConfig = true;
          break;
        case 18:
          if (lastInventory != null)
          {
            player.openInventory(lastInventory);
          }
          break;
        case 26:
          GUIManager.openGUI(player, GUIType.MAIN_MENU);
          break;
        default:
          break;
      }
      if (saveConfig)
      {
        GUIManager.openGUI(player, GUIType.ITEM_PICKUP_MODE_MENU);
      }
    }
/*    else if (invName.equals(Constant.CANCEL_STRING + "§8스탯"))
    {
      long[] stat = StatManager.getStatManager().getStat(player);
      if ((slot == 53 || slot == 45) && type == Material.BOOKSHELF)
      {
        Method.playSound(player, Sound.BLOCK_NOTE_BLOCK_HAT, 1F, 1.4F);
        GUI.openGUI(player, GUIType.MAIN_MENU);
      }
      else if (slot == 20)
      {
        StatManager.getStatManager().statUp(stat, player, 1, event.getClick());
        StatGUI.getStatGUI().statGUI(player);
      }
      else if (slot == 21)
      {
        StatManager.getStatManager().statUp(stat, player, 2, event.getClick());
        StatGUI.getStatGUI().statGUI(player);
      }
      else if (slot == 22)
      {
        StatManager.getStatManager().statUp(stat, player, 3, event.getClick());
        StatGUI.getStatGUI().statGUI(player);
      }
      else if (slot == 23)
      {
        StatManager.getStatManager().statUp(stat, player, 4, event.getClick());
        StatGUI.getStatGUI().statGUI(player);
      }
      else if (slot == 24)
      {
        StatManager.getStatManager().statUp(stat, player, 5, event.getClick());
        StatGUI.getStatGUI().statGUI(player);
      }
    }*/
    else
    {
      boolean isMenuItem = (slot >= 10 && slot <= 16) || (slot >= 19 && slot <= 25) || (slot >= 28 && slot <= 34);
      if (invName.contains(Constant.CUSTOM_RECIPE_RECIPE_LIST_MENU))
      {
        if (CustomEffectManager.hasEffect(player, CustomEffectTypeCooldown.COOLDOWN_GUI_BUTTON))
        {
//          MessageUtil.sendWarn(player, "좀 천천히 누르삼!");
          return;
        }
        if (slot == 36 && lastInventory != null)
        {
          player.openInventory(lastInventory);
        }
        int page = Integer.parseInt(invName.split(Method.format("page:", "§"))[1].replace("§", ""));
        if (slot == 39 && type == Material.SPRUCE_BOAT)
        {
          RecipeInventoryMainMenu.openRecipeInventory(player, --page, true);
        }
        if (slot == 41 && type == Material.SPRUCE_BOAT)
        {
          RecipeInventoryMainMenu.openRecipeInventory(player, ++page, true);
        }
        if (isMenuItem)
        {
          if (!itemName.equals("§c[비공개 레시피 목록]"))
          {
            String category = new NBTItem(item).getString("category");
            // 카테고리 이름 앞에 rg255,204;{카테고리 이름} 컬러코드 제거
            // category = category.substring(2, category.length());
            List<String> itemLore = item.getItemMeta().getLore();
            if (itemLore == null || itemLore.isEmpty())
            {
              return;
            }
            String lastLore = itemLore.get(itemLore.size() - 1);
            if (!lastLore.equals("§c[접근 조건 불충족]"))
            {
              RecipeInventoryCategory.openRecipeInventory(player, page, category, 1, true);
            }
          }
        }
      }
      else if (invName.contains(Constant.CUSTOM_RECIPE_MENU))
      {
        String categorySplitter = Method.format("category:", "§");
        String mainSplitter = Method.format("mainpage:", "§");
        int page = Integer.parseInt(invName.split(Method.format("page:", "§"))[1].split(Method.format("category:", "§"))[0].replace("§", ""));
        String category = invName.split(categorySplitter)[1].split(mainSplitter)[0].replace("§", "");
        int mainPage = Integer.parseInt(invName.split(mainSplitter)[1].replace("§", ""));
        if (slot == 36 || slot == 44)
        {
          RecipeInventoryMainMenu.openRecipeInventory(player, mainPage, true);
        }
        if (slot == 39 && type == Material.SPRUCE_BOAT)
        {
          RecipeInventoryCategory.openRecipeInventory(player, mainPage, category, --page, true);
        }
        if (slot == 41 && type == Material.SPRUCE_BOAT)
        {
          RecipeInventoryCategory.openRecipeInventory(player, mainPage, category, ++page, true);
        }
        if (isMenuItem)
        {
          ItemMeta itemMeta = item.getItemMeta();
          List<String> itemLore = itemMeta.getLore();
          if (itemLore == null || itemLore.isEmpty())
          {
            return;
          }
          String recipe = new NBTItem(item).getString("recipe");
          YamlConfiguration config = Variable.customRecipes.get(category);
          if (itemLore.size() > 1)
          {
            if (event.getClick() == ClickType.SHIFT_LEFT || event.getClick() == ClickType.SHIFT_RIGHT)
            {
              if (CustomEffectManager.hasEffect(player, CustomEffectTypeCooldown.COOLDOWN_GUI_BUTTON))
              {
//            MessageUtil.sendWarn(player, "좀 천천히 누르삼!");
                return;
              }
              CustomEffectManager.addEffect(player, CustomEffectTypeCooldown.COOLDOWN_GUI_BUTTON, 10);
              String lastLore = itemLore.get(itemLore.size() - 1);
              String lastSecondLore = itemLore.get(itemLore.size() - 2);
              if (!lastLore.equals("§b[아이템을 제작하는 중입니다]") && !lastLore.equals("§c[추가 제작 조건 불충족]") && !lastLore.equals("§c[재료 부족]") && !lastSecondLore.equals("§c[인벤토리 공간 부족]"))
              {
                if (lastLore.equals("§e시프트 클릭§7으로 빠르게 아이템 제작 시간 스킵 가능"))
                {
                  double skipCost = config.getDouble("recipes." + recipe + ".extra.crafting-time-skip.cost");
                  if (skipCost > 0)
                  {
                    long requireTimeToCraft = config.getLong("recipes." + recipe + ".extra.crafting-time");
                    CustomConfig playerCraftingTimeConfigFile = CustomConfig.getCustomConfig("data/CustomRecipe/CraftingTime/" + player.getUniqueId() + ".yml");
                    YamlConfiguration playerCraftingTimeConfig = Variable.craftingTime.get(player.getUniqueId());
                    long playerCraftingTime = playerCraftingTimeConfig.getLong("crafting-time." + category + "." + recipe);
                    long currentTime = System.currentTimeMillis();
                    boolean timeRelative = config.getBoolean("recipes." + recipe + ".extra.crafting-time-skip.time-relative");
                    double finalCost = timeRelative ? skipCost * (playerCraftingTime - currentTime) / requireTimeToCraft : skipCost;
                    Cucumbery.eco.withdrawPlayer(player, finalCost);
                    playerCraftingTimeConfig.set("crafting-time." + category + "." + recipe, currentTime);
                    playerCraftingTimeConfigFile.saveConfig();
                    Variable.craftingTime.put(player.getUniqueId(), playerCraftingTimeConfig);
                    RecipeInventoryCategory.openRecipeInventory(player, mainPage, category, page, false);
                    return;
                  }
                }
                ConfigurationSection configurationSection = config.getConfigurationSection("recipes");
                if (configurationSection == null || !configurationSection.contains(recipe))
                {
                  player.closeInventory();
                  MessageUtil.sendWarn(player, "관리자에 의하여 레시피가 변형/삭제되었습니다");
                  return;
                }

                String nbtItemStack = config.getString("recipes." + recipe + ".result");
                ItemStack result = ItemSerializer.deserialize(nbtItemStack);
                boolean resultIsNull = !ItemStackUtil.itemExists(result);
                if (resultIsNull)
                {
                  MessageUtil.sendError(player, "레시피를 여는데 일시적인 오류가 발생했거나 올바르지 않은 레시피입니다. 현상이 지속적으로 발생한다면, 관리자에게 문의해주세요. (오류 코드 : Result)");
                  RecipeInventoryCategory.openRecipeInventory(player, mainPage, category, page, true);
                  return;
                }
                List<Integer> ingredientAmounts = new ArrayList<>();
                List<ItemStack> ingredients = new ArrayList<>();
                List<String> ingredientsString = new ArrayList<>();
                for (int i = 1; i <= 27; i++)
                {
                  String ingredientString = config.getString("recipes." + recipe + ".ingredients." + i + ".item");
                  if (ingredientString == null)
                  {
                    break;
                  }
                  ingredientsString.add(ingredientString);
                  ingredientAmounts.add(config.getInt("recipes." + recipe + ".ingredients." + i + ".amount"));
                  ItemStack ingredient = ingredientString.startsWith("predicate:") ? ItemStackUtil.getItemStackPredicate(ingredientString.substring(10)) : ItemSerializer.deserialize(ingredientString);
                  if (!ItemStackUtil.itemExists(ingredient))
                  {
                    player.closeInventory();
                    MessageUtil.sendError(player, "올바르지 않은 레시피입니다. 관리자에게 문의해주세요. (rg255,204;" + i + "번째&r 재료 아이템 손상)");
                    return;
                  }
                  ingredients.add(ingredient);
                }
                NBTItem resultNBTItem = new NBTItem(result);
                NBTCompound merge = resultNBTItem.getCompound("MergeNBT");
                if (merge != null)
                {
                  List<ItemStack> itemStacks = InventoryClick.removeItem(player, category, config, recipe, ingredientsString, ingredients, ingredientAmounts, true);
                  ItemStack ingredient = itemStacks == null || itemStacks.isEmpty() ? ingredients.get(0).clone() : itemStacks.get(0).clone();
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
                      if (duraTag != null)
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

                boolean quickObtain = lastLore.equals("§e시프트 클릭§7으로 빠르게 아이템 수령 가능");

                List<ItemStack> itemStacks = quickObtain ? null : InventoryClick.removeItem(player, category, config, recipe, ingredientsString, ingredients, ingredientAmounts, false);
                if (quickObtain || (itemStacks != null && itemStacks.size() >= ingredients.size()))
                {
                  InventoryClick.chanceGiveItem(player, category, config, recipe, result);
                }
                RecipeInventoryCategory.openRecipeInventory(player, mainPage, category, page, false);
              }
            }
            else
            {
              RecipeInventoryRecipe.openRecipeInventory(player, mainPage, category, page, recipe, true);
            }
          }
        }
      }
      else if (invName.contains(Constant.CUSTOM_RECIPE_CRAFTING_MENU))
      {
        if (CustomEffectManager.hasEffect(player, CustomEffectTypeCooldown.COOLDOWN_GUI_BUTTON))
        {
//          MessageUtil.sendWarn(player, "좀 천천히 누르삼!");
          return;
        }
        String category = invName.split(Method.format("category:", "§"))[1].split(Constant.CUSTOM_RECIPE_CRAFTING_MENU)[0].replace("§", "");
        String mainSplitter = Method.format("mainpage:", "§");
        String splitter = Method.format("categorypage:", "§");
        String recipe = invName.split(Method.format("recipe:", "§"))[1].split(mainSplitter)[0].replace("§", "");
        int mainPage = Integer.parseInt(invName.split(mainSplitter)[1].split(splitter)[0].replace("§", ""));
        int categoryPage = Integer.parseInt(invName.split(splitter)[1].replace("§", ""));
        if (slot == 45 || slot == 53)
        {
          RecipeInventoryMainMenu.openRecipeInventory(player, mainPage, true);
        }
        if (slot == 47 || slot == 51)
        {
          RecipeInventoryCategory.openRecipeInventory(player, mainPage, category, categoryPage, true);
        }
        if (slot == 49 && item.getType() != Material.BARRIER && item.getType() != Material.CLOCK)
        {
          File file = new File(Cucumbery.getPlugin().getDataFolder() + "/data/CustomRecipe/" + category + ".yml");
          if (!file.exists())
          {
            player.closeInventory();
            MessageUtil.sendWarn(player, "관리자에 의하여 레시피가 변형/삭제되었습니다");
            return;
          }
          if (CustomEffectManager.hasEffect(player, CustomEffectTypeCooldown.COOLDOWN_GUI_BUTTON))
          {
//            MessageUtil.sendWarn(player, "좀 천천히 누르삼!");
            return;
          }
          CustomEffectManager.addEffect(player, CustomEffectTypeCooldown.COOLDOWN_GUI_BUTTON, 2);
          YamlConfiguration config = Variable.customRecipes.get(category);
          if (item.getType() == Material.ENDER_PEARL)
          {
            double skipCost = config.getDouble("recipes." + recipe + ".extra.crafting-time-skip.cost");
            if (skipCost > 0)
            {
              long requireTimeToCraft = config.getLong("recipes." + recipe + ".extra.crafting-time");
              CustomConfig playerCraftingTimeConfigFile = CustomConfig.getCustomConfig("data/CustomRecipe/CraftingTime/" + player.getUniqueId().toString() + ".yml");
              YamlConfiguration playerCraftingTimeConfig = Variable.craftingTime.get(player.getUniqueId());
              long playerCraftingTime = playerCraftingTimeConfig.getLong("crafting-time." + category + "." + recipe);
              long currentTime = System.currentTimeMillis();
              boolean timeRelative = config.getBoolean("recipes." + recipe + ".extra.crafting-time-skip.time-relative");
              double finalCost = timeRelative ? skipCost * (playerCraftingTime - currentTime) / requireTimeToCraft : skipCost;
              Cucumbery.eco.withdrawPlayer(player, finalCost);
              playerCraftingTimeConfig.set("crafting-time." + category + "." + recipe, currentTime);
              playerCraftingTimeConfigFile.saveConfig();
              Variable.craftingTime.put(player.getUniqueId(), playerCraftingTimeConfig);
              RecipeInventoryRecipe.openRecipeInventory(player, mainPage, category, categoryPage, recipe, false);
              return;
            }
          }
          ConfigurationSection configurationSection = config.getConfigurationSection("recipes");
          if (configurationSection == null || !configurationSection.contains(recipe))
          {
            player.closeInventory();
            MessageUtil.sendWarn(player, "관리자에 의하여 레시피가 변형/삭제되었습니다");
            return;
          }
          String nbtItemStack = config.getString("recipes." + recipe + ".result");
          ItemStack result = ItemSerializer.deserialize(nbtItemStack);
          boolean resultIsNull = !ItemStackUtil.itemExists(result);
          if (resultIsNull)
          {
            MessageUtil.sendError(player, "레시피를 여는데 일시적인 오류가 발생했거나 올바르지 않은 레시피입니다. 현상이 지속적으로 발생한다면, 관리자에게 문의해주세요. (오류 코드 : Result 2)");
            RecipeInventoryCategory.openRecipeInventory(player, mainPage, category, categoryPage, true);
            return;
          }
          List<Integer> ingredientAmounts = new ArrayList<>();
          List<ItemStack> ingredients = new ArrayList<>();
          List<String> ingredientsString = new ArrayList<>();
          for (int i = 1; i <= 27; i++)
          {
            String ingredientString = config.getString("recipes." + recipe + ".ingredients." + i + ".item");
            if (ingredientString == null)
            {
              break;
            }
            ingredientsString.add(ingredientString);
            ingredientAmounts.add(config.getInt("recipes." + recipe + ".ingredients." + i + ".amount"));
            ItemStack ingredient = ingredientString.startsWith("predicate:") ? ItemStackUtil.getItemStackPredicate(ingredientString.substring(10)) : ItemSerializer.deserialize(ingredientString);
            if (!ItemStackUtil.itemExists(ingredient))
            {
              player.closeInventory();
              MessageUtil.sendError(player, "올바르지 않은 레시피입니다. 관리자에게 문의해주세요. (rg255,204;" + i + "번째&r 재료 아이템 손상)");
              return;
            }
            ingredients.add(ingredient);
          }
          NBTItem resultNBTItem = new NBTItem(result);
          NBTCompound merge = resultNBTItem.getCompound("MergeNBT");
          if (merge != null)
          {
            List<ItemStack> itemStacks = InventoryClick.removeItem(player, category, config, recipe, ingredientsString, ingredients, ingredientAmounts, true);
            ItemStack ingredient = itemStacks == null || itemStacks.isEmpty() ? ingredients.get(0).clone() : itemStacks.get(0).clone();
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
                  long cur = duraTag.getLong(CucumberyTag.CUSTOM_DURABILITY_CURRENT_KEY) + bonusDurability;
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

          boolean quickObtain = item.getType() == Material.HOPPER;
          List<ItemStack> itemStacks = quickObtain ? null : InventoryClick.removeItem(player, category, config, recipe, ingredientsString, ingredients, ingredientAmounts, false);
          if (quickObtain || (itemStacks != null && itemStacks.size() >= ingredients.size()))
          {
            InventoryClick.chanceGiveItem(player, category, config, recipe, result);
          }
          RecipeInventoryRecipe.openRecipeInventory(player, mainPage, category, categoryPage, recipe, false);
        }
      }
    }
  }

}
