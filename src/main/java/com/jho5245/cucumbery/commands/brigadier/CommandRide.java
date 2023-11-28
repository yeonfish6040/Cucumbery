package com.jho5245.cucumbery.commands.brigadier;

import com.jho5245.cucumbery.commands.brigadier.base.CommandBase;
import com.jho5245.cucumbery.util.no_groups.MessageUtil;
import com.jho5245.cucumbery.util.no_groups.Method;
import com.jho5245.cucumbery.util.storage.component.util.ComponentUtil;
import com.jho5245.cucumbery.util.storage.data.Prefix;
import dev.jorel.commandapi.CommandAPI;
import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.Argument;
import dev.jorel.commandapi.arguments.BooleanArgument;
import dev.jorel.commandapi.arguments.EntitySelectorArgument;
import dev.jorel.commandapi.arguments.LiteralArgument;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.entity.*;
import org.bukkit.permissions.Permissible;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class CommandRide extends CommandBase
{
	public static final HashSet<UUID> RIDE_AREA_EFFECT_CLOUDS = new HashSet<>();

	private final List<Argument<?>> ride1 = new ArrayList<>();

	private final List<Argument<?>> rideOff1 = new ArrayList<>();

	private final List<Argument<?>> ride2 = new ArrayList<>();

	private final List<Argument<?>> ride2Hide = new ArrayList<>();

	private final List<Argument<?>> rideOff2 = new ArrayList<>();

	private final List<Argument<?>> rideOff2Hide = new ArrayList<>();

	{
		ride1.add(new EntitySelectorArgument.OneEntity("탑승할 개체"));
	}

	{
		rideOff1.add(new LiteralArgument("--off"));
	}

	{
		ride2.add(new EntitySelectorArgument.ManyEntities("탑승시킬 개체"));
		ride2.add(new EntitySelectorArgument.OneEntity("탑승할 개체"));
	}

	{
		ride2Hide.add(new EntitySelectorArgument.ManyEntities("탑승시킬 개체"));
		ride2Hide.add(new EntitySelectorArgument.OneEntity("탑승할 개체"));
		ride2Hide.add(new BooleanArgument("명령어 출력 숨김 여부"));
	}

	{
		rideOff2.add(new EntitySelectorArgument.ManyEntities("탑승시킬 개체"));
		rideOff2.add(new LiteralArgument("--off"));
	}

	{
		rideOff2Hide.add(new EntitySelectorArgument.ManyEntities("탑승시킬 개체"));
		rideOff2Hide.add(new LiteralArgument("--off"));
		rideOff2Hide.add(new BooleanArgument("명령어 출력 숨김 여부"));
	}

	private boolean ride(@NotNull Entity passenger, @NotNull Entity vehicle)
	{
		if (vehicle instanceof Player && !(passenger instanceof Animals || passenger instanceof Item || (passenger instanceof ArmorStand armorStand
				&& armorStand.isMarker()) || passenger instanceof Display || passenger instanceof Vehicle))
		{
			AreaEffectCloud areaEffectCloud = vehicle.getWorld().spawn(vehicle.getLocation(), AreaEffectCloud.class, cloud ->
			{
				try
				{
					cloud.setRadius(0);
				}
				catch (Throwable ignored)
				{
				}
				try
				{
					cloud.setGravity(false);
				}
				catch (Throwable ignored)
				{
				}
				try
				{
					cloud.setInvulnerable(true);
				}
				catch (Throwable ignored)
				{
				}
				try
				{
					cloud.setDuration(Integer.MAX_VALUE);
				}
				catch (Throwable ignored)
				{
				}
				try
				{
					cloud.setParticle(Particle.BLOCK_CRACK, Material.AIR.createBlockData());
				}
				catch (Throwable ignored)
				{
				}
				try
				{
					cloud.setWaitTime(0);
				}
				catch (Throwable ignored)
				{
				}
				if (vehicle.addPassenger(cloud))
				{
					cloud.addPassenger(passenger);
					RIDE_AREA_EFFECT_CLOUDS.add(cloud.getUniqueId());
				}
			});
			areaEffectCloud.addScoreboardTag("cucumbery-command-ride");
			return isRiding(passenger, vehicle);
		}
		else
		{
			return (!(vehicle instanceof AreaEffectCloud) || !vehicle.getScoreboardTags().contains("cucumbery-command-ride")) && vehicle.addPassenger(passenger);
		}
	}

	private boolean isRiding(@NotNull Entity passenger, @NotNull Entity vehicle)
	{
		Entity _vehicle = passenger.getVehicle();
		if (_vehicle instanceof AreaEffectCloud)
		{
			while (_vehicle instanceof AreaEffectCloud)
			{
				_vehicle = _vehicle.getVehicle();
				if (_vehicle == vehicle)
				{
					return true;
				}
			}
		}
		else
		{
			return _vehicle == vehicle;
		}
		return false;
	}

	@SuppressWarnings("unchecked")
	public void registerCommand(String command, String permission, String... aliases)
	{
		/*
		 * /ride <탑승할 개체>
		 */
		CommandAPICommand commandAPICommand = getCommandBase(command, permission, aliases);
		commandAPICommand = commandAPICommand.withArguments(ride1);
		commandAPICommand = commandAPICommand.executesNative((sender, args) ->
		{
			if (!(sender.getCallee() instanceof Entity entity))
			{
				MessageUtil.sendError(sender, Prefix.ONLY_PLAYER);
				return;
			}
			Entity vehicle = (Entity) args.get(0);
			if (vehicle == null)
			{
				MessageUtil.sendError(sender, "개체를 찾을 수 없습니다");
				return;
			}
			if (entity == vehicle)
			{
				Method.playErrorSound(entity);
				MessageUtil.sendError(entity, "자기 자신은 탑승할 수 없습니다");
				return;
			}

			if (entity.getVehicle() == vehicle)
			{
				MessageUtil.sendError(entity, ComponentUtil.translate("변경 사항이 없습니다. 이미 %s을(를) 탑승하고 있는 상태입니다", vehicle));
				return;
			}

			Location origin = entity.getLocation();
			boolean success = entity.teleport(vehicle);
			success = ride(entity, vehicle) && success;
			if (!success)
			{
				entity.teleport(origin);
				Method.playErrorSound(entity);
				MessageUtil.sendError(sender, ComponentUtil.translate("%s을(를) 탑승할 수 없습니다. 이미 해당 개체가 자신을 탑승하고 있는 상태이거나 너무 멀리 있습니다", vehicle));
				return;
			}
			MessageUtil.info(vehicle, ComponentUtil.translate("%s이(가) 당신을 탑승합니다", entity));
			MessageUtil.info(sender, ComponentUtil.translate("%s을(를) 탑승합니다", vehicle));
			MessageUtil.sendAdminMessage(sender, Collections.singletonList(vehicle), "%s을(를) 탑승합니다", vehicle);
		});
		commandAPICommand.register();


		/*
		 * /ride --off
		 */
		commandAPICommand = getCommandBase(command, permission, aliases);
		commandAPICommand = commandAPICommand.withArguments(rideOff1);
		commandAPICommand = commandAPICommand.executesNative((sender, args) ->
		{
			if (!(sender.getCallee() instanceof Entity))
			{
				throw CommandAPI.failWithString("개체만 사용할 수 있습니다");
			}
			@SuppressWarnings("all") Entity entity = (Entity) sender.getCallee();
			Entity vehicle = entity.getVehicle();
			while (vehicle instanceof AreaEffectCloud && vehicle.getScoreboardTags().contains("cucumbery-command-ride"))
			{
				vehicle = vehicle.getVehicle();
			}
			if (vehicle == null)
			{
				if (entity instanceof Player)
				{
					MessageUtil.sendError(entity, "개체를 탑승하고 있지 않습니다");
				}
				return;
			}
			entity.teleport(entity);
			MessageUtil.info(vehicle, ComponentUtil.translate("%s이(가) 당신의 탑승을 중지했습니다", entity));
			MessageUtil.info(entity, ComponentUtil.translate("더 이상 %s을(를) 탑승하지 않습니다", vehicle));
			MessageUtil.sendAdminMessage(sender, Collections.singletonList(vehicle), "더 이상 %s을(를) 탑승하지 않습니다", vehicle);
		});
		commandAPICommand.register();

		/*
		 * /ride <탑승시킬 개체> <탑승할 개체>
		 */

		commandAPICommand = getCommandBase(command, permission, aliases);
		commandAPICommand = commandAPICommand.withArguments(ride2);
		commandAPICommand = commandAPICommand.executesNative((sender, args) ->
		{
			Collection<Entity> entities = (Collection<Entity>) args.get(0);
			List<Entity> successEntities = new ArrayList<>();
			Entity vehicle = (Entity) args.get(1);
			for (Entity entity : entities)
			{
				// 개체와 탑승자가 동일하지 않고 탑승 중인 상태가 아닐 때
				if (vehicle != entity && (entity.getVehicle() == null || entity.getVehicle() != vehicle) && !vehicle.getPassengers().contains(entity))
				{
					Location origin = entity.getLocation();
					boolean ride = entity.teleport(vehicle);
					ride = ride(entity, vehicle) && ride;
					if (!ride || !isRiding(entity, vehicle))
					{
						entity.teleport(origin);
						continue;
					}
					successEntities.add(entity);
					MessageUtil.info(entity, ComponentUtil.translate("%s에 의해 %s을(를) 탑승합니다", sender, vehicle));
				}
			}
			List<Entity> failureEntities = new ArrayList<>(entities);
			failureEntities.removeAll(successEntities);
			failureEntities.removeIf(entity -> entity instanceof AreaEffectCloud && entity.getScoreboardTags().contains("cucumbery-command-ride"));
			if (!failureEntities.isEmpty())
			{
				MessageUtil.sendWarnOrError(successEntities.isEmpty(), sender,
						ComponentUtil.translate("%s은(는) 해당 개체 위에 탑승 중인 개체이거나 이미 해당 개체가 %s을(를) 탑승하고 있는 상태입니다", failureEntities, vehicle));
			}
			if (!successEntities.isEmpty())
			{
				List<Permissible> permissibles = new ArrayList<>(successEntities);
				permissibles.add(vehicle);
				MessageUtil.info(vehicle, ComponentUtil.translate("%s에 의해 %s이(가) 당신을 탑승합니다", sender, successEntities));
				MessageUtil.info(sender, ComponentUtil.translate("%s이(가) %s을(를) 탑승합니다", successEntities, vehicle));
				MessageUtil.sendAdminMessage(sender, permissibles, "%s을(를) %s에게 탑승시켰습니다", successEntities, vehicle);
			}
			else if (!(sender.getCallee() instanceof Player))
			{
				throw CommandAPI.failWithString("조건에 맞는 개체를 찾을 수 없거나 해당 개체 위에 탑승 중인 개체이거나 이미 해당 개체가 탑승하고 있는 상태입니다");
			}
		});
		commandAPICommand.register();

		/*
		 * /ride <탑승시킬 개체> <탑승할 개체> [명령어 출력 숨김 여부]
		 */

		commandAPICommand = getCommandBase(command, permission, aliases);
		commandAPICommand = commandAPICommand.withArguments(ride2Hide);
		commandAPICommand = commandAPICommand.executesNative((sender, args) ->
		{
			Collection<Entity> entities = (Collection<Entity>) args.get(0);
			List<Entity> successEntities = new ArrayList<>();
			Entity vehicle = (Entity) args.get(1);
			boolean hideOutput = (boolean) args.get(2);
			for (Entity entity : entities)
			{
				// 개체와 탑승자가 동일하지 않고 탑승 중인 상태가 아닐 때
				if (vehicle != entity && (entity.getVehicle() == null || entity.getVehicle() != vehicle) && !vehicle.getPassengers().contains(entity))
				{
					Location origin = entity.getLocation();
					boolean ride = entity.teleport(vehicle);
					ride = ride(entity, vehicle) && ride;
					if (!ride || !isRiding(entity, vehicle))
					{
						entity.teleport(origin);
						continue;
					}
					successEntities.add(entity);
					if (!hideOutput)
					{
						MessageUtil.info(entity, ComponentUtil.translate("%s에 의해 %s을(를) 탑승합니다", sender, vehicle));
					}
				}
			}
			if (!hideOutput)
			{
				List<Entity> failureEntities = new ArrayList<>(entities);
				failureEntities.removeAll(successEntities);
				failureEntities.removeIf(entity -> entity instanceof AreaEffectCloud && entity.getScoreboardTags().contains("cucumbery-command-ride"));
				if (!failureEntities.isEmpty())
				{
					MessageUtil.sendWarnOrError(successEntities.isEmpty(), sender,
							ComponentUtil.translate("%s은(는) 해당 개체 위에 탑승 중인 개체이거나 이미 해당 개체가 %s을(를) 탑승하고 있는 상태입니다", failureEntities, vehicle));
				}

			}
			if (!hideOutput && !successEntities.isEmpty())
			{
				List<Permissible> permissibles = new ArrayList<>(successEntities);
				permissibles.add(vehicle);
				MessageUtil.info(vehicle, ComponentUtil.translate("%s에 의해 %s이(가) 당신을 탑승합니다", sender, successEntities));
				MessageUtil.info(sender, ComponentUtil.translate("%s이(가) %s을(를) 탑승합니다", successEntities, vehicle));
				MessageUtil.sendAdminMessage(sender, permissibles, "%s을(를) %s에게 탑승시켰습니다", successEntities, vehicle);
			}
			else if (!(sender.getCallee() instanceof Player))
			{
				throw CommandAPI.failWithString("조건에 맞는 개체를 찾을 수 없거나 해당 개체 위에 탑승 중인 개체이거나 이미 해당 개체가 탑승하고 있는 상태입니다");
			}
		});
		commandAPICommand.register();

		/*
		 * /ride <탑승시킬 개체> --off
		 */

		commandAPICommand = getCommandBase(command, permission, aliases);
		commandAPICommand = commandAPICommand.withArguments(rideOff2);
		commandAPICommand = commandAPICommand.executesNative((sender, args) ->
		{
			Collection<Entity> entities = (Collection<Entity>) args.get(0);
			List<Entity> successEntities = new ArrayList<>();
			List<Entity> vehicles = new ArrayList<>();
			for (Entity entity : entities)
			{
				Entity vehicle = entity.getVehicle();
				while (vehicle instanceof AreaEffectCloud && vehicle.getScoreboardTags().contains("cucumbery-command-ride"))
				{
					vehicle = vehicle.getVehicle();
				}
				if (vehicle != null)
				{
					if (!vehicles.contains(vehicle))
					{
						vehicles.add(vehicle);
					}
					if (entity.teleport(entity) || entity.getVehicle() == null)
					{
						successEntities.add(entity);
						MessageUtil.info(entity, ComponentUtil.translate("%s에 의해 %s을(를) 탑승하지 않습니다", sender, vehicle));
					}
				}
			}
			List<Entity> failureEntities = new ArrayList<>(entities);
			failureEntities.removeAll(successEntities);
			failureEntities.removeIf(entity -> entity instanceof AreaEffectCloud && entity.getScoreboardTags().contains("cucumbery-command-ride"));
			if (!failureEntities.isEmpty())
			{
				MessageUtil.sendWarnOrError(successEntities.isEmpty(), sender, ComponentUtil.translate("%s은(는) 다른 개체를 탑승하고 있는 상태가 아닙니다", failureEntities));
			}
			if (!successEntities.isEmpty())
			{
				List<Permissible> permissibles = new ArrayList<>(successEntities);
				permissibles.addAll(vehicles);
				MessageUtil.info(vehicles, ComponentUtil.translate("%s에 의해 %s이(가) 당신을 탑승하지 않습니다", sender, successEntities));
				MessageUtil.info(sender, ComponentUtil.translate("%s을(를) %s에게서 탑승을 중지시켰습니다", successEntities, vehicles));
				MessageUtil.sendAdminMessage(sender, permissibles, "%s을(를) %s에게서 탑승을 중지시켰습니다", successEntities, vehicles);
			}
			else if (!(sender.getCallee() instanceof Player))
			{
				throw CommandAPI.failWithString("조건에 맞는 개체를 찾을 수 없거나 이미 해당 개체가 다른 개체를 탑승하고 있지 않습니다");
			}
		});
		commandAPICommand.register();

		/*
		 * /ride <탑승시킬 개체> --off [명령어 출력 숨김 여부]
		 */

		commandAPICommand = getCommandBase(command, permission, aliases);
		commandAPICommand = commandAPICommand.withArguments(rideOff2Hide);
		commandAPICommand = commandAPICommand.executesNative((sender, args) ->
		{
			Collection<Entity> entities = (Collection<Entity>) args.get(0);
			boolean hideOutput = (boolean) args.get(1);
			List<Entity> successEntities = new ArrayList<>();
			List<Entity> vehicles = new ArrayList<>();
			for (Entity entity : entities)
			{
				Entity vehicle = entity.getVehicle();
				while (vehicle instanceof AreaEffectCloud && vehicle.getScoreboardTags().contains("cucumbery-command-ride"))
				{
					vehicle = vehicle.getVehicle();
				}
				if (vehicle != null)
				{
					if (!vehicles.contains(vehicle))
					{
						vehicles.add(vehicle);
					}
					if (entity.teleport(entity) || entity.getVehicle() == null)
					{
						successEntities.add(entity);
						if (!hideOutput)
						{
							MessageUtil.info(entity, ComponentUtil.translate("%s에 의해 %s을(를) 탑승하지 않습니다", sender, vehicle));
						}
					}
				}
			}
			if (!hideOutput)
			{
				List<Entity> failureEntities = new ArrayList<>(entities);
				failureEntities.removeAll(successEntities);
				failureEntities.removeIf(entity -> entity instanceof AreaEffectCloud && entity.getScoreboardTags().contains("cucumbery-command-ride"));
				if (!failureEntities.isEmpty())
				{
					MessageUtil.sendWarnOrError(successEntities.isEmpty(), sender, ComponentUtil.translate("%s은(는) 다른 개체를 탑승하고 있는 상태가 아닙니다", failureEntities));
				}
			}
			if (!hideOutput && !successEntities.isEmpty())
			{
				List<Permissible> permissibles = new ArrayList<>(successEntities);
				permissibles.addAll(vehicles);
				MessageUtil.info(vehicles, ComponentUtil.translate("%s에 의해 %s이(가) 당신을 탑승하지 않습니다", sender, successEntities));
				MessageUtil.info(sender, ComponentUtil.translate("%s을(를) %s에게서 탑승을 중지시켰습니다", successEntities, vehicles));
				MessageUtil.sendAdminMessage(sender, permissibles, "%s을(를) %s에게서 탑승을 중지시켰습니다", successEntities, vehicles);
			}
			else if (!(sender.getCallee() instanceof Player))
			{
				throw CommandAPI.failWithString("조건에 맞는 개체를 찾을 수 없거나 이미 해당 개체가 다른 개체를 탑승하고 있지 않습니다");
			}
		});
		commandAPICommand.register();
	}
}
