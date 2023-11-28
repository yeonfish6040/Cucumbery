package com.jho5245.cucumbery.commands.brigadier;

import com.jho5245.cucumbery.commands.brigadier.base.CommandBase;
import com.jho5245.cucumbery.util.no_groups.CommandArgumentUtil;
import com.jho5245.cucumbery.util.no_groups.MessageUtil;
import com.jho5245.cucumbery.util.storage.data.Constant;
import de.tr7zw.changeme.nbtapi.NBTContainer;
import de.tr7zw.changeme.nbtapi.NBTEntity;
import de.tr7zw.changeme.nbtapi.NBTType;
import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.Argument;
import dev.jorel.commandapi.arguments.IntegerArgument;
import dev.jorel.commandapi.wrappers.NativeProxyCommandSender;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Explosive;
import org.bukkit.entity.Mob;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;
import org.bukkit.util.Consumer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

import static com.jho5245.cucumbery.commands.brigadier.base.ArgumentUtil.*;

public class CommandSummon2 extends CommandBase
{
  private final Argument<Integer> AMOUNT = new IntegerArgument("마리수", 1, 10000);
  private final List<Argument<?>> list1_1 = List.of(ENTITY_TYPE);
  private final List<Argument<?>> list1_2 = List.of(ENTITY_TYPE, HIDE_OUTPUT);
  private final List<Argument<?>> list2_1 = List.of(ENTITY_TYPE, AMOUNT);
  private final List<Argument<?>> list2_2 = List.of(ENTITY_TYPE, LOCATION);
  private final List<Argument<?>> list2_3 = List.of(ENTITY_TYPE, NBT_CONTAINER);
  private final List<Argument<?>> list2_4 = List.of(ENTITY_TYPE, AMOUNT, HIDE_OUTPUT);
  private final List<Argument<?>> list2_5 = List.of(ENTITY_TYPE, LOCATION, HIDE_OUTPUT);
  private final List<Argument<?>> list2_6 = List.of(ENTITY_TYPE, NBT_CONTAINER, HIDE_OUTPUT);
  private final List<Argument<?>> list3_1 = List.of(ENTITY_TYPE, AMOUNT, LOCATION);
  private final List<Argument<?>> list3_2 = List.of(ENTITY_TYPE, AMOUNT, NBT_CONTAINER);
  private final List<Argument<?>> list3_3 = List.of(ENTITY_TYPE, AMOUNT, LOCATION, HIDE_OUTPUT);
  private final List<Argument<?>> list3_4 = List.of(ENTITY_TYPE, AMOUNT, NBT_CONTAINER, HIDE_OUTPUT);
  private final List<Argument<?>> list4_1 = List.of(ENTITY_TYPE, LOCATION, NBT_CONTAINER);
  private final List<Argument<?>> list4_2 = List.of(ENTITY_TYPE, LOCATION, NBT_CONTAINER, HIDE_OUTPUT);
  private final List<Argument<?>> list5_1 = List.of(ENTITY_TYPE, AMOUNT, LOCATION, NBT_CONTAINER);
  private final List<Argument<?>> list5_2 = List.of(ENTITY_TYPE, AMOUNT, LOCATION, NBT_CONTAINER, HIDE_OUTPUT);

  private void summon(@NotNull NativeProxyCommandSender sender, @NotNull EntityType entityType, int amount, @Nullable Location location, @Nullable NBTContainer nbtContainer, boolean hideOutput)
  {
    if (location == null)
    {
      location = CommandArgumentUtil.senderLocation(sender);
    }
    Entity entity = null;
    for (int i = 0; i < amount; i++)
    {
      Consumer<Entity> consumer = e ->
      {
        if (nbtContainer != null)
        {
          NBTEntity nbtEntity = new NBTEntity(e);
          nbtEntity.mergeCompound(nbtContainer);
        }
      };
      entity = location.getWorld().spawnEntity(location, entityType, SpawnReason.COMMAND, consumer);
      if (nbtContainer != null)
      {
        if (entity instanceof Explosive explosive)
        {
          if (nbtContainer.hasKey("ExplodePower") && nbtContainer.getType("ExplodePower") == NBTType.NBTTagFloat)
          {
            explosive.setYield(nbtContainer.getFloat("ExplodePower"));
          }
          if (nbtContainer.hasKey("SetFire") && nbtContainer.getType("SetFire") == NBTType.NBTTagByte)
          {
            explosive.setIsIncendiary(nbtContainer.getBoolean("SetFire"));
          }
        }
      }
    }
    if (!hideOutput && entity != null)
    {
      MessageUtil.info(sender, "새로운 %s을(를) %s" + (entity instanceof Mob ? "마리" : "개") + " 소환했습니다", entity, Constant.THE_COLOR_HEX + amount);
      MessageUtil.sendAdminMessage(sender, "새로운 %s을(를) %s" + (entity instanceof Mob ? "마리" : "개") + " 소환했습니다", entity, Constant.THE_COLOR_HEX + amount);
    }
  }

  public void registerCommand(String command, String permission, String... aliases)
  {
    CommandAPICommand commandAPICommand = CommandBase.getCommandBase(command, permission, aliases);
    {
      commandAPICommand = commandAPICommand.withArguments(list1_1);
      commandAPICommand = commandAPICommand.executesNative((sender, args) ->
      {
        summon(sender, (EntityType) args.get(0), 1, null, null, false);
      });
      commandAPICommand.register();

      commandAPICommand = CommandBase.getCommandBase(command, permission, aliases);
      commandAPICommand = commandAPICommand.withArguments(list1_2);
      commandAPICommand = commandAPICommand.executesNative((sender, args) ->
      {
        summon(sender, (EntityType) args.get(0), 1, null, null, (Boolean) args.get(1));
      });
      commandAPICommand.register();
    }

    {
      commandAPICommand = CommandBase.getCommandBase(command, permission, aliases);
      commandAPICommand = commandAPICommand.withArguments(list2_1);
      commandAPICommand = commandAPICommand.executesNative((sender, args) ->
      {
        summon(sender, (EntityType) args.get(0), (Integer) args.get(1), null, null, false);
      });
      commandAPICommand.register();

      commandAPICommand = CommandBase.getCommandBase(command, permission, aliases);
      commandAPICommand = commandAPICommand.withArguments(list2_2);
      commandAPICommand = commandAPICommand.executesNative((sender, args) ->
      {

        summon(sender, (EntityType) args.get(0), 1, (Location) args.get(1), null, false);
      });
      commandAPICommand.register();

      commandAPICommand = CommandBase.getCommandBase(command, permission, aliases);
      commandAPICommand = commandAPICommand.withArguments(list2_3);
      commandAPICommand = commandAPICommand.executesNative((sender, args) ->
      {
        summon(sender, (EntityType) args.get(0), 1, null, (NBTContainer) args.get(1), false);
      });
      commandAPICommand.register();

      commandAPICommand = CommandBase.getCommandBase(command, permission, aliases);
      commandAPICommand = commandAPICommand.withArguments(list2_4);
      commandAPICommand = commandAPICommand.executesNative((sender, args) ->
      {
        summon(sender, (EntityType) args.get(0), (Integer) args.get(1), null, null, (Boolean) args.get(2));
      });
      commandAPICommand.register();

      commandAPICommand = CommandBase.getCommandBase(command, permission, aliases);
      commandAPICommand = commandAPICommand.withArguments(list2_5);
      commandAPICommand = commandAPICommand.executesNative((sender, args) ->
      {

        summon(sender, (EntityType) args.get(0), 1, (Location) args.get(1), null,  (Boolean) args.get(2));
      });
      commandAPICommand.register();

      commandAPICommand = CommandBase.getCommandBase(command, permission, aliases);
      commandAPICommand = commandAPICommand.withArguments(list2_6);
      commandAPICommand = commandAPICommand.executesNative((sender, args) ->
      {
        summon(sender, (EntityType) args.get(0), 1, null, (NBTContainer) args.get(1),  (Boolean) args.get(2));
      });
      commandAPICommand.register();
    }

    {
      commandAPICommand = CommandBase.getCommandBase(command, permission, aliases);
      commandAPICommand = commandAPICommand.withArguments(list3_1);
      commandAPICommand = commandAPICommand.executesNative((sender, args) ->
      {
        summon(sender, (EntityType) args.get(0), (Integer) args.get(1), (Location) args.get(2), null, false);
      });
      commandAPICommand.register();

      commandAPICommand = CommandBase.getCommandBase(command, permission, aliases);
      commandAPICommand = commandAPICommand.withArguments(list3_2);
      commandAPICommand = commandAPICommand.executesNative((sender, args) ->
      {
        summon(sender, (EntityType) args.get(0), (Integer) args.get(1), null, (NBTContainer) args.get(2), false);
      });
      commandAPICommand.register();

      commandAPICommand = CommandBase.getCommandBase(command, permission, aliases);
      commandAPICommand = commandAPICommand.withArguments(list3_3);
      commandAPICommand = commandAPICommand.executesNative((sender, args) ->
      {
        summon(sender, (EntityType) args.get(0), (Integer) args.get(1), (Location) args.get(2), null, (Boolean) args.get(3));
      });
      commandAPICommand.register();

      commandAPICommand = CommandBase.getCommandBase(command, permission, aliases);
      commandAPICommand = commandAPICommand.withArguments(list3_4);
      commandAPICommand = commandAPICommand.executesNative((sender, args) ->
      {
        summon(sender, (EntityType) args.get(0), (Integer) args.get(1), null, (NBTContainer) args.get(2), (Boolean) args.get(3));
      });
      commandAPICommand.register();
    }

    {
      commandAPICommand = CommandBase.getCommandBase(command, permission, aliases);
      commandAPICommand = commandAPICommand.withArguments(list4_1);
      commandAPICommand = commandAPICommand.executesNative((sender, args) ->
      {
        summon(sender, (EntityType) args.get(0), 1, (Location) args.get(1), (NBTContainer) args.get(2), false);
      });
      commandAPICommand.register();

      commandAPICommand = CommandBase.getCommandBase(command, permission, aliases);
      commandAPICommand = commandAPICommand.withArguments(list4_2);
      commandAPICommand = commandAPICommand.executesNative((sender, args) ->
      {
        summon(sender, (EntityType) args.get(0), 1, (Location) args.get(1), (NBTContainer) args.get(2), (Boolean) args.get(3));
      });
      commandAPICommand.register();
    }

    {
      commandAPICommand = CommandBase.getCommandBase(command, permission, aliases);
      commandAPICommand = commandAPICommand.withArguments(list5_1);
      commandAPICommand = commandAPICommand.executesNative((sender, args) ->
      {
        summon(sender, (EntityType) args.get(0), (Integer) args.get(1), (Location) args.get(2), (NBTContainer) args.get(3), false);
      });
      commandAPICommand.register();

      commandAPICommand = CommandBase.getCommandBase(command, permission, aliases);
      commandAPICommand = commandAPICommand.withArguments(list5_2);
      commandAPICommand = commandAPICommand.executesNative((sender, args) ->
      {
        summon(sender, (EntityType) args.get(0), (Integer) args.get(1), (Location) args.get(2), (NBTContainer) args.get(3), (Boolean) args.get(4));
      });
      commandAPICommand.register();
    }
  }
}
