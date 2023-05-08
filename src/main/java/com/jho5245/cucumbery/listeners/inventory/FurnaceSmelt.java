package com.jho5245.cucumbery.listeners.inventory;

import com.jho5245.cucumbery.Cucumbery;
import com.jho5245.cucumbery.util.itemlore.ItemLore;
import com.jho5245.cucumbery.util.itemlore.ItemLoreView;
import com.jho5245.cucumbery.util.storage.no_groups.ItemStackUtil;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Furnace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.FurnaceSmeltEvent;
import org.bukkit.inventory.FurnaceInventory;
import org.bukkit.inventory.ItemStack;

public class FurnaceSmelt implements Listener
{
  @EventHandler
  public void onFurnaceSmelt(FurnaceSmeltEvent event)
  {
    if (event.isCancelled())
    {
      return;
    }
    ItemStack result = event.getResult();
    this.itemLore(event, result);
  }

  private void itemLore(FurnaceSmeltEvent event, ItemStack result)
  {
    if (!Cucumbery.config.getBoolean("use-helpful-lore-feature"))
    {
      return;
    }
    Player player = null;
    if (event.getBlock().getType() == Material.FURNACE)
    {
      Furnace furnace = (Furnace) event.getBlock().getState();
      FurnaceInventory inv = furnace.getInventory();
      if (!inv.getViewers().isEmpty())
      {
        player = (Player) inv.getViewers().get(0);
      }
      ItemStack smelting = inv.getSmelting(), fuel = inv.getFuel();
      if (ItemStackUtil.itemExists(fuel) && ItemStackUtil.itemExists(smelting))
      {
        if (fuel.getType() == Material.BUCKET && smelting.getType() == Material.WET_SPONGE)
        {
          Player finalPlayer = player;
          Bukkit.getServer().getScheduler().runTaskLater(Cucumbery.getPlugin(), () ->
          {
            if (ItemStackUtil.itemExists(furnace.getInventory().getFuel()))
            {
              ItemLore.setItemLore(furnace.getInventory().getFuel(), finalPlayer != null ? ItemLoreView.of(finalPlayer) : null);
            }
          }, 0L);
        }
      }
      if (player == null)
      {
        ItemStack invResult = inv.getResult();
        if (ItemStackUtil.itemExists(invResult))
        {
          Bukkit.getScheduler().runTaskLater(Cucumbery.getPlugin(), () ->
          inv.setResult(ItemLore.setItemLore(invResult)), 0L);
        }
      }
    }
    ItemLore.setItemLore(result, player != null ? ItemLoreView.of(player) : null);
    // Block block = event.getBlock();
    // int x = block.getX();
    // int y = block.getY();
    // int z = block.getZ();
    // Block block2 = block.getWorld().getBlockAt(new Location(block.getWorld(), x, y + 1, z));
    // if (block.getType() == Material.SMOKER)
    // {
    // ItemMeta itemMeta = result.getItemMeta();
    // List<String> lore = itemMeta.getLore();
    // lore.add("");
    // lore.add("§7이 아이템은 훈연기로 만들었다! 아주 맛잇다!");
    // if (block2.getType() == Material.DIAMOND_BLOCK)
    // {
    // lore.add("");
    // lore.add("§7다이아몬드 냄새가 나는 음식이다 존나비싸다!");
    // }
    // itemMeta.setLore(lore);
    // result.setItemMeta(itemMeta);
    // }
  }
}
