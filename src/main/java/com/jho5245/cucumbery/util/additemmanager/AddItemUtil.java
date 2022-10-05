package com.jho5245.cucumbery.util.additemmanager;

import com.jho5245.cucumbery.Cucumbery;
import com.jho5245.cucumbery.util.gui.GUIManager;
import com.jho5245.cucumbery.util.itemlore.ItemLore;
import com.jho5245.cucumbery.util.itemlore.ItemLoreView;
import com.jho5245.cucumbery.util.nbt.CucumberyTag;
import com.jho5245.cucumbery.util.nbt.NBTAPI;
import com.jho5245.cucumbery.util.no_groups.MessageUtil;
import com.jho5245.cucumbery.util.no_groups.Method;
import com.jho5245.cucumbery.util.storage.data.Permission;
import com.jho5245.cucumbery.util.storage.data.Variable;
import com.jho5245.cucumbery.util.storage.no_groups.CustomConfig.UserData;
import com.jho5245.cucumbery.util.storage.no_groups.ItemStackUtil;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class AddItemUtil
{
  private static final Set<UUID> stashAlertCooldown = new HashSet<>();

  @NotNull
  public static Data addItemResult2(@NotNull CommandSender sender, @NotNull InventoryHolder inventoryHolder, @NotNull ItemStack itemStack, int amount)
  {
    return addItemResult2(sender, new ArrayList<>(Collections.singletonList(inventoryHolder)), itemStack, amount);
  }

  @NotNull
  public static Data addItemResult2(@NotNull CommandSender sender, @NotNull Collection<? extends InventoryHolder> inventoryHolder, @NotNull ItemStack itemStack, int amount)
  {
    final ItemStack original = itemStack.clone();
    HashMap<UUID, Integer> hashMap = new HashMap<>();
    List<UUID> failure = new ArrayList<>();
    List<UUID> uuids = new ArrayList<>();
    for (InventoryHolder holder : inventoryHolder)
    {
      itemStack = original;
      if (holder instanceof Entity entity)
      {
        if (entity instanceof Player player)
        {
          if (!UserData.EVENT_EXCEPTION_ACCESS.getBoolean(player))
          {
            String expireDate = NBTAPI.getString(NBTAPI.getMainCompound(itemStack), CucumberyTag.EXPIRE_DATE_KEY);
            if (expireDate != null)
            {
              Method.isTimeUp(itemStack, expireDate);
            }
          }
          ItemLore.setItemLore(itemStack, ItemLoreView.of(player));
        }
        UUID uuid = entity.getUniqueId();
        uuids.add(uuid);
        Collection<ItemStack> lostItem = holder.getInventory().addItem(itemStack).values();
        int lostAmount = !lostItem.isEmpty() ? lostItem.iterator().next().getAmount() : 0;
        hashMap.put(uuid, lostAmount);
        if (!(lostItem.isEmpty() && lostAmount == 0))
        {
          failure.add(uuid);
        }
      }
    }
    return new Data(sender, uuids, hashMap, failure, original, amount);
  }

  /**
   * 플레이어에게 아이템을 지급합니다. 만약 인벤토리가 가득 찬 상태면 자동으로 보관함에 지급됩니다.
   * @param player 아이템을 지급할 플레이어
   * @param itemStacks 지급할 아이템
   */
  public static void addItem(@NotNull Player player, @NotNull ItemStack... itemStacks)
  {
    int lostAmount = 0;
    for (ItemStack itemStack : itemStacks)
    {
      addItemResult2(player, player, itemStack, itemStack.getAmount()).stash();
      int space = ItemStackUtil.countSpace(player, itemStack);
      if (itemStack.getAmount() > space)
      {
        lostAmount += itemStack.getAmount() - space;
      }
    }

    UUID uuid = player.getUniqueId();
    if (lostAmount > 0 && Variable.itemStash.containsKey(uuid) && !Variable.itemStash.get(uuid).isEmpty())
    {
      if (Permission.CMD_STASH.has(player) && !stashAlertCooldown.contains(uuid) && !GUIManager.isGUITitle(player.getOpenInventory().title()))
      {
        MessageUtil.sendWarn(player,"&c인벤토리가 가득 차서 아이템이 보관함에 저장되었습니다. %s 명령어로 확인하세요!", "rg255,204;/stash");
        stashAlertCooldown.add(uuid);
        Bukkit.getScheduler().runTaskLater(Cucumbery.getPlugin(), () -> stashAlertCooldown.remove(uuid), 40L);
      }
    }
  }

  /**
   * 플레이어에게 아이템을 지급합니다. 만약 인벤토리가 가득 찬 상태면 자동으로 보관함에 지급됩니다.
   * @param player 아이템을 지급할 플레이어
   * @param itemStacks 지급할 아이템
   */
  public static void addItem(@NotNull Player player, @NotNull List<ItemStack> itemStacks)
  {
    ItemStack[] ary = new ItemStack[itemStacks.size()];
    for (int i = 0; i < ary.length; i++)
    {
      ary[i] = itemStacks.get(i);
    }
    addItem(player, ary);
  }
}
