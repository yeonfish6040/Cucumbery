package com.jho5245.cucumbery.listeners.inventory;

import com.jho5245.cucumbery.Cucumbery;
import com.jho5245.cucumbery.util.itemlore.ItemLore;
import com.jho5245.cucumbery.util.itemlore.ItemLoreView;
import com.jho5245.cucumbery.util.nbt.CucumberyTag;
import com.jho5245.cucumbery.util.nbt.NBTAPI;
import com.jho5245.cucumbery.util.blockplacedata.BlockPlaceDataConfig;
import com.jho5245.cucumbery.util.no_groups.MessageUtil;
import com.jho5245.cucumbery.util.no_groups.Method;
import com.jho5245.cucumbery.util.storage.data.Constant;
import com.jho5245.cucumbery.util.storage.data.Constant.AllPlayer;
import com.jho5245.cucumbery.util.storage.data.Constant.RestrictionType;
import com.jho5245.cucumbery.util.storage.data.Permission;
import com.jho5245.cucumbery.util.storage.data.Prefix;
import com.jho5245.cucumbery.util.storage.data.Variable;
import com.jho5245.cucumbery.util.storage.no_groups.CustomConfig.UserData;
import com.jho5245.cucumbery.util.storage.no_groups.ItemStackUtil;
import com.jho5245.cucumbery.util.storage.no_groups.SoundPlay;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.*;

import java.util.*;

public class InventoryOpen implements Listener
{
  public static HashMap<String, UUID> mcMMOBrewingStandMap = new HashMap<>();

  @SuppressWarnings("deprecation")
  @EventHandler
  public void onInventoryOpen(InventoryOpenEvent event)
  {
    if (event.isCancelled())
    {
      return;
    }
    if (event.getPlayer().getType() != EntityType.PLAYER)
    {
      return;
    }
    Player player = (Player) event.getPlayer();
    UUID uuid = player.getUniqueId();
    boolean isSpectator = player.getGameMode() == GameMode.SPECTATOR;
    if (!isSpectator && !Cucumbery.config.getBoolean("grant-default-permission-to-players") && !Permission.EVENT_OPENCONTAINER.has(player))
    {
      event.setCancelled(true);
      if (!Permission.EVENT_ERROR_HIDE.has(player) && !Variable.inventoryOpenAlertCooldown.contains(uuid))
      {
        Variable.inventoryOpenAlertCooldown.add(uuid);
        MessageUtil.sendTitle(player, "&c열기 불가!", "&r인벤토리 GUI를 열 권한이 없습니다", 5, 80, 15);
        SoundPlay.playSound(player, Constant.ERROR_SOUND);
        Bukkit.getServer().getScheduler().runTaskLater(Cucumbery.getPlugin(), () -> Variable.inventoryOpenAlertCooldown.remove(uuid), 100L);
      }
      return;
    }
    if (!isSpectator && !Permission.EVENT2_ANTI_ALLPLAYER.has(player) && AllPlayer.OPEN_CONTAINER.isEnabled())
    {
      event.setCancelled(true);
      if (!Variable.inventoryOpenAlertCooldown.contains(uuid))
      {
        Variable.inventoryOpenAlertCooldown.add(uuid);
        MessageUtil.sendMessage(player, Prefix.INFO_ALLPLAYER, "인벤토리 GUI를 열 수 없는 상태입니다");
        SoundPlay.playSound(player, Constant.ERROR_SOUND);
        Bukkit.getServer().getScheduler().runTaskLater(Cucumbery.getPlugin(), () -> Variable.inventoryOpenAlertCooldown.remove(uuid), 100L);
      }
      return;
    }
    String title = event.getView().getTitle();
    Inventory inventory = event.getInventory();
    final Location location = inventory.getLocation();
    if (inventory.getType() == InventoryType.BEACON)
    {
      Location targetLocation = location;
      if (targetLocation == null)
      {
        Block targetBlock = player.getTargetBlockExact(6);
        if (targetBlock != null)
        {
          targetLocation = targetBlock.getLocation();
        }
      }
      if (targetLocation != null)
      {
        BlockPlaceDataConfig blockPlaceDataConfig = BlockPlaceDataConfig.getInstance(targetLocation.getChunk());
        if (blockPlaceDataConfig != null)
        {
          ItemStack itemStack = blockPlaceDataConfig.getItemStack(targetLocation);
          if (ItemStackUtil.itemExists(itemStack) && itemStack.getType() == Material.BEACON && NBTAPI.isRestricted(player, itemStack, RestrictionType.NO_BLOCK_BEACON))
          {
            event.setCancelled(true);
            if (!Variable.inventoryOpenAlertCooldown.contains(uuid))
            {
              Variable.inventoryOpenAlertCooldown.add(uuid);
              MessageUtil.sendTitle(player, "&c사용 불가!", "사용할 수 없는 신호기입니다", 5, 80, 15);
              SoundPlay.playSound(player, Constant.ERROR_SOUND);
              Bukkit.getServer().getScheduler().runTaskLater(Cucumbery.getPlugin(), () -> Variable.inventoryOpenAlertCooldown.remove(uuid), 100L);
            }
            return;
          }
        }
      }
    }
    if (inventory.getType() == InventoryType.BREWING)
    {
      ItemStackUtil.updateInventory(player);
    }

    // gui cache
    InventoryView inventoryView = event.getView();
    InventoryType viewType = inventoryView.getTopInventory().getType();
    if (viewType != InventoryType.CRAFTING && location == null)
    {
      List<InventoryView> views = Variable.lastInventory.containsKey(uuid) ? Variable.lastInventory.get(uuid) : new ArrayList<>();
      if (views.isEmpty() || !views.get(views.size() - 1).title().equals(inventoryView.title()))
      {
        views.add(inventoryView);
      }
      if (views.size() > 2)
      {
        views.remove(0);
      }
      Variable.lastInventory.put(uuid, views);
    }
    if (isSpectator)
    {
      return;
    }
    if (Cucumbery.using_mcMMO && viewType == InventoryType.BREWING && inventory.getLocation() != null)
    {
      mcMMOBrewingStandMap.put(inventory.getLocation().toString(), uuid);
    }
    // 인벤토리가 좌표가 있는 설치된 블록이거나 휴대용 셜커 상자일 경우 아이템 설명 업데이트
    if (inventory.getViewers().size() <= 1 && location != null || location == null && inventory.getType() == InventoryType.SHULKER_BOX)
    {
      for (int i = 0; i < inventory.getSize(); i++)
      {
        ItemStack itemStack = inventory.getItem(i);
        if (itemStack != null)
        {
          ItemLore.setItemLore(itemStack, new ItemLoreView(player));
        }
      }
    }
    if (inventory.getType() == InventoryType.MERCHANT)
    {
      try
      {
        // 제삼자 거래 인벤토리(Shopkeeper 플러그인 등)인 경우에는 거래 아이템 설명 업데이트를 하지 않음 == 인벤토리의 실질적인 주인이 주민이 아닌 경우
        if (inventory.getHolder() != null)
        {
          MerchantInventory merchantInventory = (MerchantInventory) inventory;
          Merchant merchant = merchantInventory.getMerchant();
          List<MerchantRecipe> recipes = new ArrayList<>(merchant.getRecipes());
          for (int i = 0; i < recipes.size(); i++)
          {
            MerchantRecipe recipe = recipes.get(i);
            List<ItemStack> ingredients = recipe.getIngredients();
            ItemStack ingre1 = ingredients.get(0);
            ItemStack ingre2 = null;
            if (ingredients.size() == 2)
            {
              ingre2 = ingredients.get(1);
            }
            ItemStack result = recipe.getResult();
            if (Method.usingLoreFeature(player))
            {
              boolean recipeIsCorrect = true; // 레시피의 모든 아이템 설명이 플러그인의 아이템 기본 설명과 동일한가?
              if (ItemStackUtil.itemExists(ingre1))
              {
                if (!ItemStackUtil.hasLore(ingre1))
                {
                  recipeIsCorrect = false;
                  ItemLore.setItemLore(ingre1, new ItemLoreView(player));
                }
                else
                {
                  ItemStack clone = ingre1.clone();
                  ItemLore.setItemLore(clone, new ItemLoreView(player));
                  if (!ingre1.isSimilar(clone))
                  {
                    recipeIsCorrect = false;
                    ItemLore.setItemLore(ingre1, new ItemLoreView(player));
                  }
                }
              }
              if (ItemStackUtil.itemExists(ingre2))
              {
                if (!ItemStackUtil.hasLore(ingre2))
                {
                  recipeIsCorrect = false;
                  ItemLore.setItemLore(ingre2, new ItemLoreView(player));
                }
                else
                {
                  ItemStack clone = ingre2.clone();
                  ItemLore.setItemLore(clone, new ItemLoreView(player));
                  if (!ingre2.isSimilar(clone))
                  {
                    recipeIsCorrect = false;
                    ItemLore.setItemLore(ingre2, new ItemLoreView(player));
                  }
                }
              }
              if (!ItemStackUtil.hasLore(result))
              {
                recipeIsCorrect = false;
                ItemLore.setItemLore(result, new ItemLoreView(player));
              }
              else
              {
                ItemStack clone = result.clone();
                ItemLore.setItemLore(clone, new ItemLoreView(player));
                if (!result.isSimilar(clone))
                {
                  recipeIsCorrect = false;
                  ItemLore.setItemLore(result, new ItemLoreView(player));
                }
              }
              if (recipeIsCorrect)
              {
                continue;
              }
              MerchantRecipe newRecipe = new MerchantRecipe(result, recipe.getUses(), recipe.getMaxUses(), recipe.hasExperienceReward(), recipe.getVillagerExperience(), recipe.getPriceMultiplier());
              newRecipe.setIngredients(Arrays.asList(ingre1, ingre2));
              merchant.setRecipe(i, newRecipe);
              event.setCancelled(true);
              if (Cucumbery.config.getBoolean("send-actionbar-when-merchant-update-their-recipe"))
              {
                String actionbar = Cucumbery.config.getString("actionbar-when-merchant-update-their-recipe");
                if (actionbar == null)
                {
                  return;
                }
                MessageUtil.sendActionBar(player, MessageUtil.n2s(actionbar));
              }
            }
            else
            {
              boolean hasLore = false;
              if (ItemStackUtil.hasLore(ingre1))
              {
                hasLore = true;
                ItemLore.removeItemLore(ingre1);
              }
              if (ItemStackUtil.hasLore(ingre2))
              {
                hasLore = true;
                ItemLore.removeItemLore(Objects.requireNonNull(ingre2));
              }
              if (ItemStackUtil.hasLore(result))
              {
                hasLore = true;
                ItemLore.removeItemLore(result);
              }
              if (!hasLore)
              {
                continue;
              }
              MerchantRecipe newRecipe = new MerchantRecipe(result, recipe.getUses(), recipe.getMaxUses(), recipe.hasExperienceReward(), recipe.getVillagerExperience(), recipe.getPriceMultiplier());
              newRecipe.setIngredients(Arrays.asList(ingre1, ingre2));
              merchant.setRecipe(i, newRecipe);
              event.setCancelled(true);
              if (Cucumbery.config.getBoolean("send-actionbar-when-merchant-update-their-recipe"))
              {
                String actionbar = Cucumbery.config.getString("actionbar-when-merchant-update-their-recipe");
                if (actionbar == null)
                {
                  return;
                }
                MessageUtil.sendActionBar(player, MessageUtil.n2s(actionbar));
              }
            }
          }
        }
      }
      catch (Exception ignored)
      {
      }
    }
    if (!title.contains(Constant.CANCEL_STRING) &&
            !title.contains(Constant.CUSTOM_RECIPE_CREATE_GUI) &&
            !title.contains("의 인벤토리") &&
            !title.contains("'s Inventory") &&
            !UserData.EVENT_EXCEPTION_ACCESS.getBoolean(player.getUniqueId()) &&
            inventory.getType() != InventoryType.MERCHANT)
    {
      for (int i = 0; i < inventory.getSize(); i++)
      {
        ItemStack item = inventory.getItem(i);
        String expireDate = NBTAPI.getString(NBTAPI.getMainCompound(item), CucumberyTag.EXPIRE_DATE_KEY);
        if (expireDate != null)
        {
          ItemStackUtil.updateInventory(player, item);
          if (Method.isTimeUp(item, expireDate))
          {
            MessageUtil.info(player, "아이템 %s의 유효 기간이 지나서 아이템이 제거되었습니다", item);
            item.setAmount(0);
            player.updateInventory();
            if (player.getOpenInventory().getType() == InventoryType.CRAFTING || player.getOpenInventory().getType() == InventoryType.WORKBENCH)
            {
              ItemStack itemStack = inventory.getItem(0);
              if (ItemStackUtil.itemExists(itemStack))
              {
                itemStack.setAmount(0);
              }
            }
            if (player.getOpenInventory().getType() == InventoryType.ANVIL)
            {
              ItemStack itemStack = inventory.getItem(2);
              if (ItemStackUtil.itemExists(itemStack))
              {
                itemStack.setAmount(0);
              }
            }
          }
        }
      }
    }
    if (UserData.LISTEN_CONTAINER.getBoolean(player.getUniqueId()))
    {
      InventoryType inventoryType = inventory.getType();
      switch (inventoryType)
      {
        case ANVIL, SMITHING -> SoundPlay.playSound(player, Sound.BLOCK_ANVIL_USE, SoundCategory.PLAYERS, 1F, 2F);
        case BEACON -> SoundPlay.playSound(player, Sound.BLOCK_BEACON_ACTIVATE, SoundCategory.PLAYERS, 1F, 2F);
        case BREWING -> SoundPlay.playSound(player, Sound.BLOCK_BREWING_STAND_BREW, SoundCategory.PLAYERS, 1F, 2F);
        case DISPENSER, DROPPER, HOPPER -> SoundPlay.playSound(player, Sound.BLOCK_DISPENSER_DISPENSE, SoundCategory.PLAYERS, 1F, 2F);
        case ENCHANTING -> SoundPlay.playSound(player, Sound.BLOCK_ENCHANTMENT_TABLE_USE, SoundCategory.PLAYERS, 1F, 2F);
        case FURNACE -> SoundPlay.playSound(player, Sound.BLOCK_FURNACE_FIRE_CRACKLE, SoundCategory.PLAYERS, 1F, 1F);
        case MERCHANT -> SoundPlay.playSound(player, Sound.ENTITY_VILLAGER_YES, SoundCategory.PLAYERS, 1F, 1.5F);
        case WORKBENCH -> SoundPlay.playSound(player, Sound.ENTITY_HORSE_ARMOR, SoundCategory.PLAYERS, 1F, 2F);
        case BLAST_FURNACE -> SoundPlay.playSound(player, Sound.BLOCK_BLASTFURNACE_FIRE_CRACKLE, SoundCategory.PLAYERS, 1F, 1F);
        case CARTOGRAPHY -> SoundPlay.playSound(player, Sound.UI_CARTOGRAPHY_TABLE_TAKE_RESULT, SoundCategory.PLAYERS, 1F, 2F);
        case GRINDSTONE -> SoundPlay.playSound(player, Sound.BLOCK_GRINDSTONE_USE, SoundCategory.PLAYERS, 1F, 2F);
        case LOOM -> SoundPlay.playSound(player, Sound.UI_LOOM_SELECT_PATTERN, SoundCategory.PLAYERS, 1F, 2F);
        case SMOKER -> SoundPlay.playSound(player, Sound.BLOCK_SMOKER_SMOKE, SoundCategory.PLAYERS, 1F, 1F);
        case STONECUTTER -> SoundPlay.playSound(player, Sound.UI_STONECUTTER_TAKE_RESULT, SoundCategory.PLAYERS, 1F, 2F);
        default ->
        {
        }
      }
    }
  }
}
