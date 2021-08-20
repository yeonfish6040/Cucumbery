package com.jho5245.cucumbery.commands;

import com.jho5245.cucumbery.util.MessageUtil;
import com.jho5245.cucumbery.util.Method;
import com.jho5245.cucumbery.util.storage.ItemStackUtil;
import com.jho5245.cucumbery.util.storage.data.Permission;
import com.jho5245.cucumbery.util.storage.data.Prefix;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.jetbrains.annotations.NotNull;

public class Hat implements CommandExecutor
{
	public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args)
	{
		if (!Method.hasPermission(sender, Permission.CMD_HAT, true))
			return true;
		if (!(sender instanceof Player))
		{
			MessageUtil.sendError(sender, Prefix.ONLY_PLAYER);
			return true;
		}
		if (args.length == 0)
		{
			Player player = (Player) sender;
			PlayerInventory inv = player.getInventory();
			ItemStack mainHand = inv.getItemInMainHand(), helmet = inv.getHelmet();
			if (!ItemStackUtil.itemExists(mainHand))
			{
				MessageUtil.sendError(sender, Prefix.NO_HOLDING_ITEM);
				return true;
			}
			boolean hasHelmet = ItemStackUtil.itemExists(helmet);
			mainHand = mainHand.clone();
			inv.setItemInMainHand(null);
			if (hasHelmet)
			{
				helmet = helmet.clone();
				inv.addItem(helmet);
			}
			inv.setHelmet(mainHand);
			MessageUtil.sendMessage(player, Prefix.INFO_HAT, "주로 사용하는 손에 들고 있는 아이템을 머리에 장착하였습니다.");
		}
		else
		{
			MessageUtil.longArg(sender, 0, args);
			MessageUtil.commandInfo(sender, label, "");
			return true;
		}
		return true;
	}
}
