package com.jho5245.cucumbery.commands.brigadier;

import com.jho5245.cucumbery.commands.brigadier.base.CommandBase;
import com.jho5245.cucumbery.util.storage.no_groups.ItemStackUtil;
import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.*;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@SuppressWarnings("all") public class CommandSetItem extends CommandBase
{
	private final List<Argument<?>> argument = new ArrayList<>();

	{
		argument.add(new EntitySelectorArgument.ManyEntities("개체"));
		argument.add( new MultiLiteralArgument("args", List.of("mainhand", "offhand", "helmet", "chestplate", "leggings", "boots")));
	}

	private final List<Argument<?>> argument2 = new ArrayList<>();

	{
		argument2.add( new EntitySelectorArgument.ManyEntities("개체"));
		argument2.add( new MultiLiteralArgument("args", List.of("mainhand", "offhand", "helmet", "chestplate", "leggings", "boots")));
		argument2.add(new BooleanArgument("강제로 변경"));
	}

	@Override @SuppressWarnings("unchecked") public void registerCommand(String command, String permission, String... aliases)
	{
		CommandAPICommand commandAPICommand = getCommandBase(command, permission, aliases);
		commandAPICommand = commandAPICommand.withArguments(argument);
		commandAPICommand = commandAPICommand.executesPlayer((player, args) -> {
			ItemStack item = player.getInventory().getItemInMainHand();
			if (!ItemStackUtil.itemExists(item))
			{
				item = new ItemStack(Material.AIR);
			}
			for (Entity entity : (Collection<Entity>) args.get(0))
			{
				if (entity instanceof LivingEntity)
				{
					LivingEntity livingEntity = (LivingEntity) entity;
					EntityEquipment equipment = livingEntity.getEquipment();
					try
					{
						switch ((String) args.get(1))
						{
							case "mainhand":
								if (!ItemStackUtil.itemExists(equipment.getItemInMainHand()))
									equipment.setItemInMainHand(item);
								break;
							case "offhand":
								if (!ItemStackUtil.itemExists(equipment.getItemInOffHand()))
									equipment.setItemInOffHand(item);
								break;
							case "helmet":
								if (!ItemStackUtil.itemExists(equipment.getHelmet()))
									equipment.setHelmet(item);
								break;
							case "chestplate":
								if (!ItemStackUtil.itemExists(equipment.getChestplate()))
									equipment.setChestplate(item);
								break;
							case "leggings":
								if (!ItemStackUtil.itemExists(equipment.getLeggings()))
									equipment.setLeggings(item);
								break;
							case "boots":
								if (!ItemStackUtil.itemExists(equipment.getBoots()))
									equipment.setBoots(item);
								break;
						}
					}
					catch (Exception ignored)
					{

					}
				}
			}
		});
		commandAPICommand.register();



		commandAPICommand = getCommandBase(command, permission, aliases);
		commandAPICommand = commandAPICommand.withArguments(argument2);
		commandAPICommand = commandAPICommand.executesPlayer((player, args) -> {
			ItemStack item = player.getInventory().getItemInMainHand();
			if (!ItemStackUtil.itemExists(item))
			{
				item = new ItemStack(Material.AIR);
			}
			boolean force = (boolean) args.get(2);
			for (Entity entity : (Collection<Entity>) args.get(0))
			{
				if (entity instanceof LivingEntity)
				{
					LivingEntity livingEntity = (LivingEntity) entity;
					EntityEquipment equipment = livingEntity.getEquipment();
					try
					{
						switch ((String) args.get(1))
						{
							case "mainhand":
								if (force || !ItemStackUtil.itemExists(equipment.getItemInMainHand()))
									equipment.setItemInMainHand(item);
								break;
							case "offhand":
								if (force || !ItemStackUtil.itemExists(equipment.getItemInOffHand()))
									equipment.setItemInOffHand(item);
								break;
							case "helmet":
								if (force || !ItemStackUtil.itemExists(equipment.getHelmet()))
									equipment.setHelmet(item);
								break;
							case "chestplate":
								if (force || !ItemStackUtil.itemExists(equipment.getChestplate()))
									equipment.setChestplate(item);
								break;
							case "leggings":
								if (force || !ItemStackUtil.itemExists(equipment.getLeggings()))
									equipment.setLeggings(item);
								break;
							case "boots":
								if (force || !ItemStackUtil.itemExists(equipment.getBoots()))
									equipment.setBoots(item);
								break;
						}
					}
					catch (Exception ignored)
					{

					}
				}
			}
		});
		commandAPICommand.register();
	}
}
