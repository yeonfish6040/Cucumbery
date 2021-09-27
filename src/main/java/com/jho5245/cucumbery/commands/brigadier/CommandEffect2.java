package com.jho5245.cucumbery.commands.brigadier;

import com.jho5245.cucumbery.commands.brigadier.base.CommandBase;
import com.jho5245.cucumbery.util.Method;
import com.jho5245.cucumbery.util.MessageUtil;
import dev.jorel.commandapi.CommandAPI;
import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.*;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class CommandEffect2 extends CommandBase
{
	private final List<Argument> argumentList1 = Arrays.asList(new EntitySelectorArgument("개체", EntitySelectorArgument.EntitySelector.MANY_ENTITIES), new PotionEffectArgument("효과"),
																														 new IntegerArgument("지속 시간(틱)", -1, Integer.MAX_VALUE));

	private final List<Argument> argumentList2 = Arrays.asList(new EntitySelectorArgument("개체", EntitySelectorArgument.EntitySelector.MANY_ENTITIES), new PotionEffectArgument("효과"),
																														 new IntegerArgument("지속 시간(틱)", -1, Integer.MAX_VALUE), new IntegerArgument("강도", 0, 255));

	private final List<Argument> argumentList3 = Arrays.asList(new EntitySelectorArgument("개체", EntitySelectorArgument.EntitySelector.MANY_ENTITIES), new PotionEffectArgument("효과"),
																														 new IntegerArgument("지속 시간(틱)", -1, Integer.MAX_VALUE), new IntegerArgument("강도", 0, 255), new BooleanArgument("우측 상단 효과 빛남"));

	private final List<Argument> argumentList4 = Arrays.asList(new EntitySelectorArgument("개체", EntitySelectorArgument.EntitySelector.MANY_ENTITIES), new PotionEffectArgument("효과"),
																														 new IntegerArgument("지속 시간(틱)", -1, Integer.MAX_VALUE), new IntegerArgument("강도", 0, 255), new BooleanArgument("우측 상단 효과 빛남"),
																														 new BooleanArgument("입자 숨김"));

	private final List<Argument> argumentList5 = Arrays.asList(new EntitySelectorArgument("개체", EntitySelectorArgument.EntitySelector.MANY_ENTITIES), new PotionEffectArgument("효과"),
																														 new IntegerArgument("지속 시간(틱)", -1, Integer.MAX_VALUE), new IntegerArgument("강도", 0, 255), new BooleanArgument("우측 상단 효과 빛남"),
																														 new BooleanArgument("입자 숨김"), new BooleanArgument("우측 상단 아이콘 숨김"));

	private final List<Argument> argumentList6 = Arrays.asList(new EntitySelectorArgument("개체", EntitySelectorArgument.EntitySelector.MANY_ENTITIES), new PotionEffectArgument("효과"),
																														 new IntegerArgument("지속 시간(틱)", -1, Integer.MAX_VALUE), new IntegerArgument("강도", 0, 255), new BooleanArgument("우측 상단 효과 빛남"),
																														 new BooleanArgument("입자 숨김"), new BooleanArgument("우측 상단 아이콘 숨김"), new BooleanArgument("기존 효과 제거"));

	private final List<Argument> argumentList7 = Arrays.asList(new EntitySelectorArgument("개체", EntitySelectorArgument.EntitySelector.MANY_ENTITIES), new PotionEffectArgument("효과"),
																														 new IntegerArgument("지속 시간(틱)", -1, Integer.MAX_VALUE), new IntegerArgument("강도", 0, 255), new BooleanArgument("우측 상단 효과 빛남"),
																														 new BooleanArgument("입자 숨김"), new BooleanArgument("우측 상단 아이콘 숨김"), new BooleanArgument("기존 효과 제거"), new BooleanArgument("명령어 출력 숨김 여부"));

	@SuppressWarnings("unchecked") public void registerCommand(String command, String permission, String... aliases)
	{
		CommandAPICommand commandAPICommand = getCommandBase(command, permission, aliases);
		commandAPICommand = commandAPICommand.withArguments(argumentList1);
		commandAPICommand = commandAPICommand.executesNative((sender, args) -> {
			CommandSender commandSender = sender.getCallee();
			try
			{
				commandSender.getName();
			}
			catch (Exception e)
			{
				commandSender = sender.getCaller();
			}
			Collection<Entity> entities = (Collection<Entity>) args[0];
			int successCount = 0;
			PotionEffectType potionEffectType = (PotionEffectType) args[1];
			int duration = (int) args[2];
			if (duration == -1)
			{
				duration = Integer.MAX_VALUE;
			}
			PotionEffect potionEffect = new PotionEffect(potionEffectType, duration, 0, true, true, true);
			for (Entity entity : entities)
			{
				if (entity instanceof LivingEntity)
				{
					LivingEntity livingEntity = (LivingEntity) entity;
					livingEntity.addPotionEffect(potionEffect);
					successCount++;
				}
			}
			if (successCount == 0)
			{
				CommandAPI.fail("개체를 찾을 수 없습니다.");
			}
			else
			{
				String hasParticle = "&r";
				String isAmbient = "&r";
				String hasIcon = "&r";
				String extra = "지속 시간 : &e"+Method.timeFormatMilli(duration*50L)+"&r, 농도 레벨 : &e1단계"+hasParticle+isAmbient+hasIcon;
				MessageUtil.info(commandSender, "&e"+successCount+"&r개의 개체에게 &e"+potionEffectType+"&r 효과("+extra+"&r)를 부여하였습니다.");
			}
		});
		commandAPICommand.register();

		commandAPICommand = getCommandBase(command, permission, aliases);
		commandAPICommand = commandAPICommand.withArguments(argumentList2);
		commandAPICommand = commandAPICommand.executesNative((sender, args) -> {
			CommandSender commandSender = sender.getCallee();
			try
			{
				commandSender.getName();
			}
			catch (Exception e)
			{
				commandSender = sender.getCaller();
			}
			Collection<Entity> entities = (Collection<Entity>) args[0];
			int successCount = 0;
			PotionEffectType potionEffectType = (PotionEffectType) args[1];
			int duration = (int) args[2];
			if (duration == -1)
			{
				duration = Integer.MAX_VALUE;
			}
			int amplifier = (int) args[3];
			PotionEffect potionEffect = new PotionEffect(potionEffectType, duration, amplifier, true, true, true);
			for (Entity entity : entities)
			{
				if (entity instanceof LivingEntity)
				{
					LivingEntity livingEntity = (LivingEntity) entity;
					livingEntity.addPotionEffect(potionEffect);
					successCount++;
				}
			}
			if (successCount == 0)
			{
				CommandAPI.fail("개체를 찾을 수 없습니다.");
			}
			else
			{
				String hasParticle = "&r";
				String isAmbient = "&r";
				String hasIcon = "&r";
				String extra = "지속 시간 : &e"+Method.timeFormatMilli(duration*50L)+"&r, 농도 레벨 : &e"+(amplifier+1)+"단계"+hasParticle+isAmbient+hasIcon;
				MessageUtil.info(commandSender, "&e"+successCount+"&r개의 개체에게 &e"+potionEffectType+"&r 효과("+extra+"&r)를 부여하였습니다.");
			}
		});
		commandAPICommand.register();

		commandAPICommand = getCommandBase(command, permission, aliases);
		commandAPICommand = commandAPICommand.withArguments(argumentList3);
		commandAPICommand = commandAPICommand.executesNative((sender, args) -> {
			CommandSender commandSender = sender.getCallee();
			try
			{
				commandSender.getName();
			}
			catch (Exception e)
			{
				commandSender = sender.getCaller();
			}
			Collection<Entity> entities = (Collection<Entity>) args[0];
			int successCount = 0;
			PotionEffectType potionEffectType = (PotionEffectType) args[1];
			int duration = (int) args[2];
			if (duration == -1)
			{
				duration = Integer.MAX_VALUE;
			}
			int amplifier = (int) args[3];
			boolean hideAmbient = (boolean) args[4];
			PotionEffect potionEffect = new PotionEffect(potionEffectType, duration, amplifier, !hideAmbient, true, true);
			for (Entity entity : entities)
			{
				if (entity instanceof LivingEntity)
				{
					LivingEntity livingEntity = (LivingEntity) entity;
					livingEntity.addPotionEffect(potionEffect);
					successCount++;
				}
			}
			if (successCount == 0)
			{
				CommandAPI.fail("개체를 찾을 수 없습니다.");
			}
			else
			{
				String hasParticle = "&r";
				String isAmbient = !hideAmbient ? "&r" : "&r, &a우측 상단 효과 빛남";
				String hasIcon = "&r";
				String extra = "지속 시간 : &e"+Method.timeFormatMilli(duration*50L)+"&r, 농도 레벨 : &e"+(amplifier+1)+"단계"+hasParticle+isAmbient+hasIcon;
				MessageUtil.info(commandSender, "&e"+successCount+"&r개의 개체에게 &e"+potionEffectType+"&r 효과("+extra+"&r)를 부여하였습니다.");
			}
		});
		commandAPICommand.register();

		commandAPICommand = getCommandBase(command, permission, aliases);
		commandAPICommand = commandAPICommand.withArguments(argumentList4);
		commandAPICommand = commandAPICommand.executesNative((sender, args) -> {
			CommandSender commandSender = sender.getCallee();
			try
			{
				commandSender.getName();
			}
			catch (Exception e)
			{
				commandSender = sender.getCaller();
			}
			Collection<Entity> entities = (Collection<Entity>) args[0];
			int successCount = 0;
			PotionEffectType potionEffectType = (PotionEffectType) args[1];
			int duration = (int) args[2];
			if (duration == -1)
			{
				duration = Integer.MAX_VALUE;
			}
			int amplifier = (int) args[3];
			boolean hideAmbient = (boolean) args[4];
			boolean hideParticles = (boolean) args[5];
			PotionEffect potionEffect = new PotionEffect(potionEffectType, duration, amplifier, !hideAmbient, !hideParticles, true);
			for (Entity entity : entities)
			{
				if (entity instanceof LivingEntity)
				{
					LivingEntity livingEntity = (LivingEntity) entity;
					livingEntity.addPotionEffect(potionEffect);
					successCount++;
				}
			}
			if (successCount == 0)
			{
				CommandAPI.fail("개체를 찾을 수 없습니다.");
			}
			else
			{
				String hasParticle = !hideParticles ? "&r" : "&r, &a입자 숨김";
				String isAmbient = !hideAmbient ? "&r" : "&r, &a우측 상단 효과 빛남";
				String hasIcon = "&r";
				String extra = "지속 시간 : &e"+Method.timeFormatMilli(duration*50L)+"&r, 농도 레벨 : &e"+(amplifier+1)+"단계"+hasParticle+isAmbient+hasIcon;
				MessageUtil.info(commandSender, "&e"+successCount+"&r개의 개체에게 &e"+potionEffectType+"&r 효과("+extra+"&r)를 부여하였습니다.");
			}
		});
		commandAPICommand.register();

		commandAPICommand = getCommandBase(command, permission, aliases);
		commandAPICommand = commandAPICommand.withArguments(argumentList5);
		commandAPICommand = commandAPICommand.executesNative((sender, args) -> {
			CommandSender commandSender = sender.getCallee();
			try
			{
				commandSender.getName();
			}
			catch (Exception e)
			{
				commandSender = sender.getCaller();
			}
			Collection<Entity> entities = (Collection<Entity>) args[0];
			int successCount = 0;
			PotionEffectType potionEffectType = (PotionEffectType) args[1];
			int duration = (int) args[2];
			if (duration == -1)
			{
				duration = Integer.MAX_VALUE;
			}
			int amplifier = (int) args[3];
			boolean hideAmbient = (boolean) args[4];
			boolean hideParticles = (boolean) args[5];
			boolean hideIcon = (boolean) args[6];
			PotionEffect potionEffect = new PotionEffect(potionEffectType, duration, amplifier, !hideAmbient, !hideParticles, !hideIcon);
			for (Entity entity : entities)
			{
				if (entity instanceof LivingEntity)
				{
					LivingEntity livingEntity = (LivingEntity) entity;
					livingEntity.addPotionEffect(potionEffect);
					successCount++;
				}
			}
			if (successCount == 0)
			{
				CommandAPI.fail("개체를 찾을 수 없습니다.");
			}
			else
			{
				String hasParticle = !hideParticles ? "&r" : "&r, &a입자 숨김";
				String isAmbient = !hideAmbient ? "&r" : "&r, &a우측 상단 효과 빛남";
				String hasIcon = !hideIcon ? "&r" : "&r, &a우측 상단 아이콘 숨김";
				String extra = "지속 시간 : &e"+Method.timeFormatMilli(duration*50L)+"&r, 농도 레벨 : &e"+(amplifier+1)+"단계"+hasParticle+isAmbient+hasIcon;
				MessageUtil.info(commandSender, "&e"+successCount+"&r개의 개체에게 &e"+potionEffectType+"&r 효과("+extra+"&r)를 부여하였습니다.");
			}
		});
		commandAPICommand.register();
		commandAPICommand = getCommandBase(command, permission, aliases);
		commandAPICommand = commandAPICommand.withArguments(argumentList6);
		commandAPICommand = commandAPICommand.executesNative((sender, args) -> {
			CommandSender commandSender = sender.getCallee();
			try
			{
				commandSender.getName();
			}
			catch (Exception e)
			{
				commandSender = sender.getCaller();
			}
			Collection<Entity> entities = (Collection<Entity>) args[0];
			int successCount = 0;
			PotionEffectType potionEffectType = (PotionEffectType) args[1];
			int duration = (int) args[2];
			if (duration == -1)
			{
				duration = Integer.MAX_VALUE;
			}
			int amplifier = (int) args[3];
			boolean hideAmbient = (boolean) args[4];
			boolean hideParticles = (boolean) args[5];
			boolean hideIcon = (boolean) args[6];
			boolean removeBefore = (boolean) args[7];
			PotionEffect potionEffect = new PotionEffect(potionEffectType, duration, amplifier, !hideAmbient, !hideParticles, !hideIcon);
			for (Entity entity : entities)
			{
				if (entity instanceof LivingEntity)
				{
					LivingEntity livingEntity = (LivingEntity) entity;
					if (removeBefore && livingEntity.hasPotionEffect(potionEffectType))
					{
						livingEntity.removePotionEffect(potionEffectType);
					}
					livingEntity.addPotionEffect(potionEffect);
					successCount++;
				}
			}
			if (successCount == 0)
			{
				CommandAPI.fail("개체를 찾을 수 없습니다.");
			}
			else
			{
				String hasParticle = !hideParticles ? "&r" : "&r, &a입자 숨김";
				String isAmbient = !hideAmbient ? "&r" : "&r, &a우측 상단 효과 빛남";
				String hasIcon = !hideIcon ? "&r" : "&r, &a우측 상단 아이콘 숨김";
				String extra = "지속 시간 : &e"+Method.timeFormatMilli(duration*50L)+"&r, 농도 레벨 : &e"+(amplifier+1)+"단계"+hasParticle+isAmbient+hasIcon;
				MessageUtil.info(commandSender, "&e"+successCount+"&r개의 개체에게 &e"+potionEffectType+"&r 효과("+extra+"&r)를 부여하였습니다.");
			}
		});
		commandAPICommand.register();

		commandAPICommand = getCommandBase(command, permission, aliases);
		commandAPICommand = commandAPICommand.withArguments(argumentList7);
		commandAPICommand = commandAPICommand.executesNative((sender, args) -> {
			CommandSender commandSender = sender.getCallee();
			try
			{
				commandSender.getName();
			}
			catch (Exception e)
			{
				commandSender = sender.getCaller();
			}
			Collection<Entity> entities = (Collection<Entity>) args[0];
			int successCount = 0;
			PotionEffectType potionEffectType = (PotionEffectType) args[1];
			int duration = (int) args[2];
			if (duration == -1)
			{
				duration = Integer.MAX_VALUE;
			}
			int amplifier = (int) args[3];
			boolean hideAmbient = (boolean) args[4];
			boolean hideParticles = (boolean) args[5];
			boolean hideIcon = (boolean) args[6];
			boolean removeBefore = (boolean) args[7];
			boolean hideOutput = (boolean) args[8];
			PotionEffect potionEffect = new PotionEffect(potionEffectType, duration, amplifier, !hideAmbient, !hideParticles, !hideIcon);
			for (Entity entity : entities)
			{
				if (entity instanceof LivingEntity)
				{
					LivingEntity livingEntity = (LivingEntity) entity;
					if (removeBefore && livingEntity.hasPotionEffect(potionEffectType))
					{
						livingEntity.removePotionEffect(potionEffectType);
					}
					livingEntity.addPotionEffect(potionEffect);
					successCount++;
				}
			}
			if (successCount == 0)
			{
				CommandAPI.fail("개체를 찾을 수 없습니다.");
			}
			else if (!hideOutput)
			{
				String hasParticle = !hideParticles ? "&r" : "&r, &a입자 숨김";
				String isAmbient = !hideAmbient ? "&r" : "&r, &a우측 상단 효과 빛남";
				String hasIcon = !hideIcon ? "&r" : "&r, &a우측 상단 아이콘 숨김";
				String extra = "지속 시간 : &e"+Method.timeFormatMilli(duration*50L)+"&r, 농도 레벨 : &e"+(amplifier+1)+"단계"+hasParticle+isAmbient+hasIcon;
				MessageUtil.info(commandSender, "&e"+successCount+"&r개의 개체에게 &e"+potionEffectType+"&r 효과("+extra+"&r)를 부여하였습니다.");
			}
		});
		commandAPICommand.register();
	}
}
