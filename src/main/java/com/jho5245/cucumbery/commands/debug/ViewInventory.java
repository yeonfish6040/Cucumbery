package com.jho5245.cucumbery.commands.debug;

import com.jho5245.cucumbery.util.MessageUtil;
import com.jho5245.cucumbery.util.Method;
import com.jho5245.cucumbery.util.SelectorUtil;
import com.jho5245.cucumbery.util.storage.ComponentUtil;
import com.jho5245.cucumbery.util.storage.CreateItemStack;
import com.jho5245.cucumbery.util.storage.ItemStackUtil;
import com.jho5245.cucumbery.util.storage.data.Constant;
import com.jho5245.cucumbery.util.storage.data.Permission;
import com.jho5245.cucumbery.util.storage.data.Prefix;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.*;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ViewInventory implements CommandExecutor
{
  public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, String[] args)
  {
    if (!Method.hasPermission(sender, Permission.CMD_ITEMDATA, true))
    {
      return !(sender instanceof BlockCommandSender);
    }
    if (!MessageUtil.checkQuoteIsValidInArgs(sender, args = MessageUtil.wrapWithQuote(args)))
    {
      return !(sender instanceof BlockCommandSender);
    }
    if (!(sender instanceof Player player))
    {
      MessageUtil.sendError(sender, Prefix.ONLY_PLAYER);
      return true;
    }
    if (args.length == 1)
    {
      Entity entity = SelectorUtil.getEntity(sender, args[0]);
      if (entity == null)
      {
        return true;
      }

      if (entity instanceof Merchant merchant)
      {
        List<MerchantRecipe> merchantRecipes = merchant.getRecipes();
        if (merchantRecipes.size() > 0)
        {
          player.openMerchant(merchant, true);
          MessageUtil.info(player, ComponentUtil.createTranslate("%s의 거래 목록을 참조합니다.", merchant));
          MessageUtil.sendAdminMessage(player, null, ComponentUtil.createTranslate("[%s: %s의 거래 목록을 참조합니다.]", player, merchant));
        }
        else
        {
          MessageUtil.sendWarn(ComponentUtil.createTranslate("%s은(는) 거래 목록을 가지고 있지 않습니다.", merchant));
        }
      }
      else if (entity instanceof InventoryHolder inventoryHolder)
      {
        Inventory inventory = inventoryHolder.getInventory();
        ItemStack[] contents = inventory.getContents();
        if (!Method.inventoryEmpty(inventory))
        {
          Inventory clone = Bukkit.createInventory(null, 54, ComponentUtil.createTranslate("&8%s의 인벤토리 (복사본)", ComponentUtil.senderComponent(inventoryHolder, NamedTextColor.DARK_GRAY)));
          clone.setContents(contents);
          player.openInventory(clone);
          MessageUtil.info(player, ComponentUtil.createTranslate("%s의 인벤토리 내용을 참조합니다.", inventoryHolder));
          MessageUtil.sendAdminMessage(player, null, ComponentUtil.createTranslate("[%s: %s의 인벤토리 내용을 참조합니다.]", player, inventoryHolder));
        }
        else
        {
          MessageUtil.sendWarn(player, ComponentUtil.createTranslate("%s은(는) 인벤토리에 아이템을 가지고 있지 않습니다.", inventoryHolder));
        }
      }
      else if (entity instanceof LivingEntity livingEntity)
      {
        EntityEquipment entityEquipment = livingEntity.getEquipment();
        if (entityEquipment != null)
        {
          Inventory inventory = Bukkit.createInventory(null, 18, ComponentUtil.createTranslate("&8%s이(가) 장착하고 있는 아이템", ComponentUtil.senderComponent(livingEntity, NamedTextColor.DARK_GRAY)));
          inventory.setItem(1, CreateItemStack.create(Material.IRON_SWORD, ComponentUtil.createTranslate(Constant.THE_COLOR_HEX + "주로 사용하는 손")));
          inventory.setItem(2, CreateItemStack.create(Material.BOW, ComponentUtil.createTranslate(Constant.THE_COLOR_HEX + "다른 손")));
          inventory.setItem(4, CreateItemStack.create(Material.LEATHER_HELMET, ComponentUtil.createTranslate(Constant.THE_COLOR_HEX + "머리")));
          inventory.setItem(5, CreateItemStack.create(Material.LEATHER_CHESTPLATE, ComponentUtil.createTranslate(Constant.THE_COLOR_HEX + "몸")));
          inventory.setItem(6, CreateItemStack.create(Material.LEATHER_LEGGINGS, ComponentUtil.createTranslate(Constant.THE_COLOR_HEX + "다리")));
          inventory.setItem(7, CreateItemStack.create(Material.LEATHER_BOOTS, ComponentUtil.createTranslate(Constant.THE_COLOR_HEX + "발")));

          ItemStack mainHand = entityEquipment.getItemInMainHand(), offHand = entityEquipment.getItemInOffHand();
          ItemStack helmet = entityEquipment.getHelmet(), chestplate = entityEquipment.getChestplate(), leggings = entityEquipment.getLeggings(), boots = entityEquipment.getBoots();

          ItemStack notExists = CreateItemStack.create(Material.BARRIER, ComponentUtil.createTranslate("&c없음"));
          mainHand = ItemStackUtil.itemExists(mainHand) ? mainHand : notExists;
          offHand = ItemStackUtil.itemExists(offHand) ? offHand : notExists;
          helmet = ItemStackUtil.itemExists(helmet) ? helmet : notExists;
          chestplate = ItemStackUtil.itemExists(chestplate) ? chestplate : notExists;
          leggings = ItemStackUtil.itemExists(leggings) ? leggings : notExists;
          boots = ItemStackUtil.itemExists(boots) ? boots : notExists;
          inventory.setItem(10, mainHand);
          inventory.setItem(11, offHand);
          inventory.setItem(13, helmet);
          inventory.setItem(14, chestplate);
          inventory.setItem(15, leggings);
          inventory.setItem(16, boots);
          player.openInventory(inventory);
          MessageUtil.info(player, ComponentUtil.createTranslate("%s의 장착 중인 아이템을 참조합니다.", livingEntity));
          MessageUtil.sendAdminMessage(player, null, ComponentUtil.createTranslate("[%s: %s의 장착 중인 아이템을 참조합니다.]", player, livingEntity));
        }
        else
        {
          MessageUtil.sendWarn(ComponentUtil.createTranslate("%s은(는) 장착 중인 아이템이 없습니다.", livingEntity));
        }
      }
      else
      {
        MessageUtil.sendError(player, ComponentUtil.createTranslate("%s은(는) 인벤토리를 참조할 수 있는 개체가 아닙니다.", entity));
      }
    }
    else
    {
      if (args.length == 0)
      {
        MessageUtil.shortArg(sender, 1, args);
      }
      else
      {
        MessageUtil.longArg(sender, 1, args);
      }
      MessageUtil.commandInfo(sender, label, Method.getUsage(cmd));
    }
    return true;
  }
}
