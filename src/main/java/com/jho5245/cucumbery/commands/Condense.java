package com.jho5245.cucumbery.commands;

import com.jho5245.cucumbery.Cucumbery;
import com.jho5245.cucumbery.util.MessageUtil;
import com.jho5245.cucumbery.util.Method;
import com.jho5245.cucumbery.util.SelectorUtil;
import com.jho5245.cucumbery.util.itemlore.ItemLore;
import com.jho5245.cucumbery.util.storage.ItemStackUtil;
import com.jho5245.cucumbery.util.storage.data.Permission;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class Condense implements CommandExecutor
{
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
	{
		if (!Method.hasPermission(sender, Permission.CMD_CONDENSE, true))
			return true;
		String usage = Method.getUsage(cmd), consoleUsage = usage.replace("[플레이어 ID]", "<플레이어 ID>");
		if (args.length <= 4)
		{
			if (args.length < 3 && !(sender instanceof Player))
			{
				MessageUtil.shortArg(sender, 3, args);
				MessageUtil.info(sender, consoleUsage);
				return true;
			}
			Player player;
			if (args.length < 3)
				player = (Player) sender;
			else
			{
				player = SelectorUtil.getPlayer(sender, args[2]);
				if (player == null)
					return true;
			}
			for (Condenser condenser : Condenser.values())
			{
				ItemStack ingredient = condenser.getIngredient();
				ItemStack result = condenser.getResult();
				if (Cucumbery.config.getBoolean("use-helpful-lore-feature"))
				{
					String worldName = player.getLocation().getWorld().getName();
					if (!Cucumbery.config.getStringList("no-use-helpful-lore-feature-worlds").contains(worldName))
					{
						ItemLore.setItemLore(ingredient);
						ItemLore.setItemLore(result);
					}
				}
				this.condense(player, ingredient, result);
				break;
			}
		}
		else
		{

		}
		return true;
	}

	@SuppressWarnings("unused")
	private void condense(Player player, ItemStack from, ItemStack to)
	{
		Inventory inv = player.getInventory();
		int countAmount = ItemStackUtil.countItem(inv, from); // 가지고 있는 재료의 개수
		int countSpace = ItemStackUtil.countSpace(inv, to); // 결과물을 넣을 인벤토리의 남은 공간
		int usedFrom = 0; // 사용된 아이템 재료 개수
		int madeTo = 0; // 만든 압축 블록 개수
		int leastIngreAmount = from.getAmount(); // 필요한 최소 재료 개수
		int fromMax = from.getMaxStackSize(); // 재료의 최대 스택 수
		int toMax = to.getMaxStackSize(); // 결과의 최대 스택 수
		if (countAmount < leastIngreAmount) // 가지고 있는 재료의 개수가 필요한 최소 재료 개수보다 적을 때
		{
			if (countSpace == 0) // 공간도 0칸이라면
			{
				player.sendMessage("재료, 공간 부족");
				return;
			}
			player.sendMessage("재료 부족");
			return;
		}
		if (countAmount <= fromMax && countSpace == 0) // 재료의 개수가 1세트가 안되면서 공간이 0칸이라면
		{
			player.sendMessage("공간 부족");
			return;
		}
		int expect = countAmount / leastIngreAmount; // 예상 제작 개수
		if (expect > countSpace && countSpace == 0) // 예상 제작 개수보다 공간이 작고, 공간이 0칸이라면
		{
			player.sendMessage("공간 부족");
			return;
		}
	}

	public enum Condenser
	{
		COAL(new ItemStack(Material.COAL, 9), new ItemStack(Material.COAL_BLOCK)),
		IRON_INGOT(new ItemStack(Material.IRON_INGOT, 9), new ItemStack(Material.COAL_BLOCK)),;

		private ItemStack ingredient;

		private ItemStack result;

		Condenser(ItemStack ingredient, ItemStack result)
		{
			this.ingredient = ingredient;
			this.result = result;
		}

		public ItemStack getIngredient()
		{
			return ingredient;
		}

		public ItemStack getResult()
		{
			return result;
		}
	}
}
