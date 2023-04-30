package com.jho5245.cucumbery.listeners.inventory;

import com.jho5245.cucumbery.Cucumbery;
import com.jho5245.cucumbery.util.additemmanager.AddItemUtil;
import com.jho5245.cucumbery.util.itemlore.ItemLore;
import com.jho5245.cucumbery.util.nbt.CucumberyTag;
import com.jho5245.cucumbery.util.nbt.NBTAPI;
import com.jho5245.cucumbery.util.no_groups.ItemSerializer;
import com.jho5245.cucumbery.util.no_groups.MessageUtil;
import com.jho5245.cucumbery.util.no_groups.Method;
import com.jho5245.cucumbery.util.storage.component.util.ComponentUtil;
import com.jho5245.cucumbery.util.storage.component.util.ItemNameUtil;
import com.jho5245.cucumbery.util.storage.data.Constant;
import com.jho5245.cucumbery.util.storage.data.Prefix;
import com.jho5245.cucumbery.util.storage.data.Variable;
import com.jho5245.cucumbery.util.storage.no_groups.CustomConfig;
import com.jho5245.cucumbery.util.storage.no_groups.ItemStackUtil;
import com.jho5245.cucumbery.util.storage.no_groups.SoundPlay;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.TranslatableComponent;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.block.ShulkerBox;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryCloseEvent.Reason;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.BlockStateMeta;

import java.io.File;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class InventoryClose implements Listener
{
  @EventHandler
  public void onInventoryClose(InventoryCloseEvent event)
  {
    if (event.getPlayer() instanceof Player player)
    {
      UUID uuid = player.getUniqueId();
      Inventory inventory = event.getInventory();
      Reason reason = event.getReason();
      if (reason != Reason.OPEN_NEW)
      {
        Variable.lastInventory.remove(uuid);
      }
      InventoryType inventoryType = inventory.getType();
      String title = event.getView().getTitle();
      if (inventoryType == InventoryType.SHULKER_BOX && event.getView().title() instanceof TranslatableComponent translatableComponent)
      {
        List<Component> args = translatableComponent.args();
        if (args.size() == 2 && args.get(1) instanceof TextComponent textComponent && textComponent.content().startsWith(Constant.ITEM_PORTABLE_SHULKER_BOX_GUI))
        {
          ItemStack[] contents = event.getInventory().getContents();
          PlayerInventory playerInventory = player.getInventory();
          ItemStack shulker;
          if (textComponent.content().endsWith("MAIN_HAND"))
          {
            shulker = playerInventory.getItemInMainHand();
          }
          else
          {
            shulker = playerInventory.getItemInOffHand();
          }
          if (ItemStackUtil.itemExists(shulker) && Constant.SHULKER_BOXES.contains(shulker.getType()))
          {
            BlockStateMeta boxMeta = (BlockStateMeta) shulker.getItemMeta();
            ShulkerBox shulkerBox = (ShulkerBox) boxMeta.getBlockState();
            shulkerBox.getInventory().setContents(contents);
            boxMeta.setBlockState(shulkerBox);
            shulker.setItemMeta(boxMeta);
            SoundPlay.playSound(player, Sound.BLOCK_SHULKER_BOX_CLOSE, SoundCategory.BLOCKS);
            // 휴대용 셜커 상자의 설명 업데이트
            Bukkit.getScheduler().runTaskLater(Cucumbery.getPlugin(), () ->
            ItemStackUtil.updateInventory(player), 0L);
            return;
          }
        }
      }
      if (inventory.getType() == InventoryType.BREWING)
      {
        // mcMMO 연금술 기능 충돌로 인한 양조기 GUI찾을 닫을 때 인벤토리 업데이트
        if (Cucumbery.using_mcMMO)
        {
          Bukkit.getServer().getScheduler().runTaskLater(Cucumbery.getPlugin(), () ->
          {
            ItemStackUtil.updateInventory(player);
            player.updateInventory();
          }, 0L);
        }
      }
      if (title.contains(Constant.VIRTUAL_CHEST_MENU_PREFIX) && !title.contains(Constant.VIRTUAL_CHEST_ADMIN_MENU_PREFIX))
      {
        String chestName = title.split("§6가상창고 - §e")[1];
        CustomConfig virtualChestConfig = CustomConfig.getCustomConfig("data/VirtualChest/" + player.getUniqueId().toString() + "/" + chestName + ".yml");
        if (Method.inventoryEmpty(inventory))
        {
          virtualChestConfig.delete();
          File folder = new File(Cucumbery.getPlugin().getDataFolder() + "/data/VirtualChest/" + player.getUniqueId().toString());
          if (folder.exists() && folder.listFiles() != null && Objects.requireNonNull(folder.listFiles()).length == 0)
          {
            boolean success = folder.delete();
            if (!success)
            {
              System.err.println("[Cucumbery] could not delete " + player.getUniqueId() + " folder!");
            }
          }
          return;
        }
        else
        {
          YamlConfiguration config = virtualChestConfig.getConfig();
          ItemStack[] contents = inventory.getContents();
          for (int i = 1; i <= contents.length; i++)
          {
            config.set("items." + i, ItemSerializer.serialize(contents[i - 1]));
          }
          virtualChestConfig.saveConfig();
        }
      }
      else if (title.contains(Constant.VIRTUAL_CHEST_ADMIN_MENU_PREFIX))
      {
        String chestName = title.split("§6가상창고 - §e")[1].split(Method.format("owneruuid:", "§"))[0];
        String chestUuid = title.split(Method.format("owneruuid:", "§"))[1].replace("§", "");
        CustomConfig virtualChestConfig = CustomConfig.getCustomConfig("data/VirtualChest/" + chestUuid + "/" + chestName + ".yml");
        if (Method.inventoryEmpty(inventory))
        {
          virtualChestConfig.delete();
          File folder = new File(Cucumbery.getPlugin().getDataFolder() + "/data/VirtualChest/" + chestUuid);
          if (folder.exists() && folder.listFiles() != null && Objects.requireNonNull(folder.listFiles()).length == 0)
          {
            boolean success = folder.delete();
            if (!success)
            {
              System.err.println("[Cucumbery] could not delete " + player.getUniqueId().toString() + " folder!");
            }
          }
          return;
        }
        else
        {
          YamlConfiguration config = virtualChestConfig.getConfig();
          ItemStack[] contents = inventory.getContents();
          for (int i = 1; i <= contents.length; i++)
          {
            config.set("items." + i, ItemSerializer.serialize(contents[i - 1]));
          }
          virtualChestConfig.saveConfig();
        }
      }
      if (title.startsWith(Constant.CUSTOM_RECIPE_CREATE_GUI))
      {
        // 레시피 삭제 버튼을 누른 경우에 인벤토리가 닫히면 리턴
        if (!ItemStackUtil.itemExists(inventory.getItem(0)))
        {
          return;
        }
        // 확인 버튼을 누르지 않을 경우에 리턴
        if (ItemStackUtil.itemExists(inventory.getItem(22)))
        {
          MessageUtil.sendMessage(player, Prefix.INFO_CUSTOM_RECIPE, "레시피 " + (title.contains("§레§시§피§ §편§집§3편집") ? "편집" : "생성") + "을 취소했습니다");
          if (!title.contains("§레§시§피§ §편§집§3편집"))
          {
            for (int i = 27; i <= 53; i++)
            {
              ItemStack returnLost = inventory.getItem(i);
              if (ItemStackUtil.itemExists(returnLost))
              {
                AddItemUtil.addItem(player, returnLost);
              }
            }
            ItemStack result = inventory.getItem(4);
            if (ItemStackUtil.itemExists(result))
            {
              AddItemUtil.addItem(player, result);
            }
          }
          return;
        }
        ItemStack result = inventory.getItem(4);
        if (!ItemStackUtil.itemExists(result))
        {
          MessageUtil.sendError(player, "레시피의 결과물이 되는 아이템을 중앙 상단에 놔두고 다시 시도해주세요. 결과물 아이템이 없어 레시피 " + (title.contains("§레§시§피§ §편§집§3편집") ? "편집" : "생성") + "에 실패했습니다");
          if (!title.contains("§레§시§피§ §편§집§3편집"))
          {
            for (int i = 27; i <= 53; i++)
            {
              ItemStack returnLost = inventory.getItem(i);
              if (ItemStackUtil.itemExists(returnLost))
              {
                AddItemUtil.addItem(player, returnLost);
              }
            }
          }
          player.updateInventory();
          return;
        }
        Inventory ingredientInventory = Bukkit.createInventory(null, 27);
        for (int i = 27; i <= 53; i++)
        {
          ItemStack ingredientCheck = inventory.getItem(i);
          if (ItemStackUtil.itemExists(ingredientCheck))
          {
            ItemLore.removeItemLore(ingredientCheck);
            ingredientInventory.addItem(ingredientCheck);
          }
        }
        if (ingredientInventory.firstEmpty() == 0)
        {
          MessageUtil.sendError(player, "레시피의 재료로 되는 아이템을 하단 9×3 슬롯에 놔두고 다시 시도해주세요. 재료 아이템이 없어 레시피 " + (title.contains("§레§시§피§ §편§집§3편집") ? "편집" : "생성") + "에 실패했습니다");
          if (!title.contains("§레§시§피§ §편§집§3편집"))
          {
            AddItemUtil.addItem(player, result);
          }
          return;
        }
        String category = Method.deformat(title.split(Method.format("category:", "§"))[1].split(Method.format("recipe:", "§"))[0], "§");
        String recipe = Method.deformat(title.split(Method.format("recipe:", "§"))[1], "§");
        CustomConfig customRecipeListConfig = CustomConfig.getCustomConfig("data/CustomRecipe/" + category + ".yml");
        YamlConfiguration config = customRecipeListConfig.getConfig();
        if (!config.contains("extra.permissions.hide-if-no-base"))
        {
          config.set("extra.permissions.hide-if-no-base", false);
        }
        if (!config.contains("extra.display"))
        {
          config.set("extra.display", category);
        }
        String resultSerial = ItemSerializer.serialize(ItemLore.removeItemLore(result, true));
        boolean[] reusable = null;
        if (title.contains("§레§시§피§ §편§집§3편집"))
        { // 편집하는 레시피일 경우 재료 코드 삭제
          ConfigurationSection ingredients = config.getConfigurationSection("recipes." + recipe + ".ingredients");
          if (ingredients != null)
          {
            reusable = new boolean[ingredients.getKeys(false).size()];
            for (int i = 0; i < reusable.length; i++)
            {
              try
              {
                reusable[i] = config.getBoolean("recipes." + recipe + ".ingredients." + (i + 1) + ".reusable");
              }
              catch (Exception ignored)
              {
              }
            }
          }
          config.set("recipes." + recipe + ".ingredients", null);
        }
        if (config.getString("recipes." + recipe + ".extra.display") == null)
        {
          config.set("recipes." + recipe + ".extra.display", ComponentUtil.serializeAsJson(ItemNameUtil.itemName(result)));
        }
        config.set("recipes." + recipe + ".result", resultSerial);
        int configSlot = 0;
        for (int i = 0; i < ingredientInventory.getSize(); i++)
        {
          ItemStack ingredient = ingredientInventory.getItem(i);
          if (ItemStackUtil.itemExists(ingredient))
          {
            configSlot++;
            ingredient = ingredient.clone();
            ingredient.setAmount(1);
            String ingredientSerial = ItemSerializer.serialize(ingredient);
            int amount = ItemStackUtil.countItem(ingredientInventory, ingredient);
            if (ItemStackUtil.hasDisplayName(ingredient))
            {
              Component display = ingredient.getItemMeta().displayName();
              if (display instanceof TextComponent textComponent)
              {
                String text = textComponent.content();
                if (text.startsWith("predicate:"))
                {
                  ingredientSerial = text;
                }
              }
            }
            config.set("recipes." + recipe + ".ingredients." + configSlot + ".amount", amount);
            if (reusable != null && reusable.length >= configSlot && reusable[configSlot - 1])
            {
              config.set("recipes." + recipe + ".ingredients." + configSlot + ".reusable", true);
            }
            config.set("recipes." + recipe + ".ingredients." + configSlot + ".item", ingredientSerial);
            for (int j = i; j < ingredientInventory.getSize(); j++)
            {
              ItemStack dupItem = ingredientInventory.getItem(j);
              if (ItemStackUtil.itemExists(dupItem) && ItemStackUtil.itemEquals(dupItem, ingredient))
              {
                ingredientInventory.clear(j);
              }
            }
          }
        }
        customRecipeListConfig.saveConfig();
        Variable.customRecipes.put(category, config);
        MessageUtil.sendMessage(player, Prefix.INFO_CUSTOM_RECIPE, "커스텀 레시피 목록 %s에 %s이(라)는 이름의 레시피를 " + (title.contains("§레§시§피§ §편§집§3편집") ? "편집" : "생성") + "했습니다", Constant.THE_COLOR_HEX + category, Constant.THE_COLOR_HEX + recipe);
        // player.sendMessage(category.replace("§", "&") + ", " + recipe.replace("§", "&"));
        player.updateInventory();
        return;
      }
      if (inventory.getLocation() != null)
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
              MessageUtil.info(player, ComponentUtil.translate("아이템 %s의 유효 기간이 지나서 아이템이 제거되었습니다", item));
              Objects.requireNonNull(item).setAmount(0);
              player.updateInventory();
              if (player.getOpenInventory().getType() == InventoryType.CRAFTING || player.getOpenInventory().getType() == InventoryType.WORKBENCH)
              {
                ItemStack inventoryItem = inventory.getItem(0);
                if (ItemStackUtil.itemExists(inventoryItem))
                {
                  inventoryItem.setAmount(0);
                }
              }
              if (player.getOpenInventory().getType() == InventoryType.ANVIL)
              {
                ItemStack inventoryItem = inventory.getItem(2);
                if (ItemStackUtil.itemExists(inventoryItem))
                {
                  inventoryItem.setAmount(0);
                }
              }
            }
          }
        }
      }
      if (player.getGameMode() != GameMode.SPECTATOR && CustomConfig.UserData.LISTEN_CONTAINER.getBoolean(player.getUniqueId()))
      {
        switch (inventoryType)
        {
          case ANVIL, SMITHING -> SoundPlay.playSound(player, Sound.BLOCK_ANVIL_DESTROY, SoundCategory.PLAYERS, 1F, 2F);
          case BEACON -> SoundPlay.playSound(player, Sound.BLOCK_BEACON_DEACTIVATE, SoundCategory.PLAYERS, 1F, 1.5F);
          case BREWING -> SoundPlay.playSound(player, Sound.BLOCK_BREWING_STAND_BREW, SoundCategory.PLAYERS, 1F, 1.5F);
          case DISPENSER, DROPPER, HOPPER -> SoundPlay.playSound(player, Sound.BLOCK_DISPENSER_DISPENSE, SoundCategory.PLAYERS, 1F, 1.5F);
          case ENCHANTING -> SoundPlay.playSound(player, Sound.BLOCK_ENCHANTMENT_TABLE_USE, SoundCategory.PLAYERS, 1F, 1.5F);
          case ENDER_CHEST -> {
            if (inventory.getLocation() == null)
            {
              SoundPlay.playSound(player, Sound.BLOCK_ENDER_CHEST_CLOSE, SoundCategory.PLAYERS);
            }
          }
          case FURNACE -> SoundPlay.playSound(player, Sound.BLOCK_FURNACE_FIRE_CRACKLE, SoundCategory.PLAYERS, 1F, 1F);
          case MERCHANT -> SoundPlay.playSound(player, Sound.ENTITY_VILLAGER_NO, SoundCategory.PLAYERS, 1F, 1.5F);
          case WORKBENCH -> SoundPlay.playSound(player, Sound.ENTITY_HORSE_ARMOR, SoundCategory.PLAYERS, 1F, 1.5F);
          case BLAST_FURNACE -> SoundPlay.playSound(player, Sound.BLOCK_BLASTFURNACE_FIRE_CRACKLE, SoundCategory.PLAYERS, 1F, 1F);
          case CARTOGRAPHY -> SoundPlay.playSound(player, Sound.UI_CARTOGRAPHY_TABLE_TAKE_RESULT, SoundCategory.PLAYERS, 1F, 1.5F);
          case GRINDSTONE -> SoundPlay.playSound(player, Sound.BLOCK_GRINDSTONE_USE, SoundCategory.PLAYERS, 1F, 1.5F);
          case LOOM -> SoundPlay.playSound(player, Sound.UI_LOOM_SELECT_PATTERN, SoundCategory.PLAYERS, 1F, 1.5F);
          case SMOKER -> SoundPlay.playSound(player, Sound.BLOCK_SMOKER_SMOKE, SoundCategory.PLAYERS, 1F, 1F);
          case STONECUTTER -> SoundPlay.playSound(player, Sound.UI_STONECUTTER_TAKE_RESULT, SoundCategory.PLAYERS, 1F, 1.5F);
          default ->
          {
          }
        }
      }
    }
  }
}
