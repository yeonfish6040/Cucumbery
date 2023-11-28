package com.jho5245.cucumbery.commands.brigadier;

import com.jho5245.cucumbery.commands.brigadier.base.ArgumentUtil;
import com.jho5245.cucumbery.commands.brigadier.base.CommandBase;
import com.jho5245.cucumbery.util.no_groups.MessageUtil;
import com.jho5245.cucumbery.util.storage.component.util.ComponentUtil;
import com.jho5245.cucumbery.util.storage.component.util.sendercomponent.SenderComponentUtil;
import com.jho5245.cucumbery.util.storage.data.Constant;
import dev.jorel.commandapi.CommandAPI;
import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.Argument;
import dev.jorel.commandapi.arguments.FloatArgument;
import dev.jorel.commandapi.arguments.MultiLiteralArgument;
import dev.jorel.commandapi.exceptions.WrapperCommandSyntaxException;
import dev.jorel.commandapi.wrappers.NativeProxyCommandSender;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.List;

import static com.jho5245.cucumbery.commands.brigadier.base.ArgumentUtil.HIDE_OUTPUT;
import static com.jho5245.cucumbery.commands.brigadier.base.ArgumentUtil.ONE_ENTITY;

public class CommandExplode extends CommandBase
{
  private final Location DEFAULT_LOCATION = new Location(Bukkit.getWorlds().get(0), 0, 0, 0);
  private final Entity DEFAULT_ENTITY = null;
  private final float DEFAULT_POWER = 4f;
  private final String DEFAULT_PROPERTY = "break-only";
  private final boolean DEFAULT_HIDE_OUTPUT = false;

  private final Argument LOCATION = ArgumentUtil.LOCATION;
  private final Argument POWER = new FloatArgument("폭발 강도", 0, 500);
  private final Argument EXPLODE_PROPERTY = new MultiLiteralArgument("args", List.of("none", "fire-only", "break-only", "all"));

  private final List<Argument<?>> list01 = Arrays.asList(HIDE_OUTPUT);
  private final List<Argument<?>> list02 = Arrays.asList(POWER);
  private final List<Argument<?>> list03 = Arrays.asList(POWER, HIDE_OUTPUT);
  private final List<Argument<?>> list04 = Arrays.asList(POWER, EXPLODE_PROPERTY);
  private final List<Argument<?>> list05 = Arrays.asList(POWER, EXPLODE_PROPERTY, HIDE_OUTPUT);
  private final List<Argument<?>> list06 = Arrays.asList(EXPLODE_PROPERTY);
  private final List<Argument<?>> list07 = Arrays.asList(EXPLODE_PROPERTY, HIDE_OUTPUT);
  private final List<Argument<?>> list08 = Arrays.asList(EXPLODE_PROPERTY, POWER);
  private final List<Argument<?>> list09 = Arrays.asList(EXPLODE_PROPERTY, POWER, HIDE_OUTPUT);

  private final List<Argument<?>> list10 = Arrays.asList(ONE_ENTITY);
  private final List<Argument<?>> list11 = Arrays.asList(ONE_ENTITY, HIDE_OUTPUT);
  private final List<Argument<?>> list12 = Arrays.asList(ONE_ENTITY, POWER);
  private final List<Argument<?>> list13 = Arrays.asList(ONE_ENTITY, POWER, HIDE_OUTPUT);
  private final List<Argument<?>> list14 = Arrays.asList(ONE_ENTITY, POWER, EXPLODE_PROPERTY);
  private final List<Argument<?>> list15 = Arrays.asList(ONE_ENTITY, POWER, EXPLODE_PROPERTY, HIDE_OUTPUT);
  private final List<Argument<?>> list16 = Arrays.asList(ONE_ENTITY, EXPLODE_PROPERTY);
  private final List<Argument<?>> list17 = Arrays.asList(ONE_ENTITY, EXPLODE_PROPERTY, HIDE_OUTPUT);
  private final List<Argument<?>> list18 = Arrays.asList(ONE_ENTITY, EXPLODE_PROPERTY, POWER);
  private final List<Argument<?>> list19 = Arrays.asList(ONE_ENTITY, EXPLODE_PROPERTY, POWER, HIDE_OUTPUT);

  private final List<Argument<?>> list20 = Arrays.asList(LOCATION);
  private final List<Argument<?>> list21 = Arrays.asList(LOCATION, HIDE_OUTPUT);
  private final List<Argument<?>> list22 = Arrays.asList(LOCATION, POWER);
  private final List<Argument<?>> list23 = Arrays.asList(LOCATION, POWER, HIDE_OUTPUT);
  private final List<Argument<?>> list24 = Arrays.asList(LOCATION, POWER, EXPLODE_PROPERTY);
  private final List<Argument<?>> list25 = Arrays.asList(LOCATION, POWER, EXPLODE_PROPERTY, HIDE_OUTPUT);
  private final List<Argument<?>> list26 = Arrays.asList(LOCATION, EXPLODE_PROPERTY);
  private final List<Argument<?>> list27 = Arrays.asList(LOCATION, EXPLODE_PROPERTY, HIDE_OUTPUT);
  private final List<Argument<?>> list28 = Arrays.asList(LOCATION, EXPLODE_PROPERTY, POWER);
  private final List<Argument<?>> list29 = Arrays.asList(LOCATION, EXPLODE_PROPERTY, POWER, HIDE_OUTPUT);

  private final List<Argument<?>> list30 = Arrays.asList(LOCATION, ONE_ENTITY);
  private final List<Argument<?>> list31 = Arrays.asList(LOCATION, ONE_ENTITY, HIDE_OUTPUT);
  private final List<Argument<?>> list32 = Arrays.asList(LOCATION, ONE_ENTITY, POWER);
  private final List<Argument<?>> list33 = Arrays.asList(LOCATION, ONE_ENTITY, POWER, HIDE_OUTPUT);
  private final List<Argument<?>> list34 = Arrays.asList(LOCATION, ONE_ENTITY, POWER, EXPLODE_PROPERTY);
  private final List<Argument<?>> list35 = Arrays.asList(LOCATION, ONE_ENTITY, POWER, EXPLODE_PROPERTY, HIDE_OUTPUT);
  private final List<Argument<?>> list36 = Arrays.asList(LOCATION, ONE_ENTITY, EXPLODE_PROPERTY);
  private final List<Argument<?>> list37 = Arrays.asList(LOCATION, ONE_ENTITY, EXPLODE_PROPERTY, HIDE_OUTPUT);
  private final List<Argument<?>> list38 = Arrays.asList(LOCATION, ONE_ENTITY, EXPLODE_PROPERTY, POWER);
  private final List<Argument<?>> list39 = Arrays.asList(LOCATION, ONE_ENTITY, EXPLODE_PROPERTY, POWER, HIDE_OUTPUT);

  private final List<Argument<?>> list40 = Arrays.asList(ONE_ENTITY, LOCATION);
  private final List<Argument<?>> list41 = Arrays.asList(ONE_ENTITY, LOCATION, HIDE_OUTPUT);
  private final List<Argument<?>> list42 = Arrays.asList(ONE_ENTITY, LOCATION, POWER);
  private final List<Argument<?>> list43 = Arrays.asList(ONE_ENTITY, LOCATION, POWER, HIDE_OUTPUT);
  private final List<Argument<?>> list44 = Arrays.asList(ONE_ENTITY, LOCATION, POWER, EXPLODE_PROPERTY);
  private final List<Argument<?>> list45 = Arrays.asList(ONE_ENTITY, LOCATION, POWER, EXPLODE_PROPERTY, HIDE_OUTPUT);
  private final List<Argument<?>> list46 = Arrays.asList(ONE_ENTITY, LOCATION, EXPLODE_PROPERTY);
  private final List<Argument<?>> list47 = Arrays.asList(ONE_ENTITY, LOCATION, EXPLODE_PROPERTY, HIDE_OUTPUT);
  private final List<Argument<?>> list48 = Arrays.asList(ONE_ENTITY, LOCATION, EXPLODE_PROPERTY, POWER);
  private final List<Argument<?>> list49 = Arrays.asList(ONE_ENTITY, LOCATION, EXPLODE_PROPERTY, POWER, HIDE_OUTPUT);

  @NotNull
  private Component explosionComponent(float power, boolean setFire, boolean breakBlocks, @Nullable Entity source)
  {
    Component explosionComponent = ComponentUtil.translate("폭발").color(Constant.THE_COLOR);
    Component hover = ComponentUtil.translate("폭발");
    hover = hover.append(Component.text("\n"));
    hover = hover.append(ComponentUtil.translate("강도 : %s", Constant.THE_COLOR_HEX + Constant.Sosu2.format(power)));
    hover = hover.append(Component.text("\n"));
    hover = hover.append(ComponentUtil.translate("블록 파괴 여부 : %s", Constant.THE_COLOR_HEX + breakBlocks));
    hover = hover.append(Component.text("\n"));
    hover = hover.append(ComponentUtil.translate("불 번짐 여부 : %s", Constant.THE_COLOR_HEX + setFire));
    if (source != null)
    {
      hover = hover.append(Component.text("\n"));
      hover = hover.append(ComponentUtil.translate("폭발 출처 : %s", SenderComponentUtil.senderComponent(source, null, true)));
    }
    return explosionComponent.hoverEvent(hover);
  }

  @NotNull
  private Location getLocation(@NotNull NativeProxyCommandSender sender)
  {
    CommandSender commandSender = sender.getCallee();
    if (commandSender instanceof BlockCommandSender blockCommandSender)
    {
      return blockCommandSender.getBlock().getLocation();
    }
    else if (commandSender instanceof Entity entity)
    {
      return entity.getLocation();
    }
    return DEFAULT_LOCATION;
  }

  private void explode(@NotNull NativeProxyCommandSender sender, @NotNull Location location, @Nullable Entity source, float power, @NotNull String property, boolean hideOutput) throws WrapperCommandSyntaxException
  {
    World world = location.getWorld();
    boolean setFire = property.equals("fire-only") || property.equals("all");
    boolean breakBlocks = property.equals("break-only") || property.equals("all");
    if (!hideOutput)
    {
      Component explosionComponent = explosionComponent(power, setFire, breakBlocks, source);
      MessageUtil.info(sender,"%s에 %s을(를) 일으켰습니다", location, explosionComponent);
      MessageUtil.sendAdminMessage(sender, "%s에 %s을(를) 일으켰습니다", location, explosionComponent);
    }
    if (!world.createExplosion(location, power, setFire, breakBlocks, source))
    {
      throw CommandAPI.failWithString("폭발을 일으킬 수 없습니다");
    }
  }

  public void registerCommand(String command, String permission, String... aliases)
  {
    CommandAPICommand commandAPICommand = getCommandBase(command, permission, aliases);

    {
      commandAPICommand = commandAPICommand.executesNative((sender, args) ->
      {
        Location location = getLocation(sender);
        Entity source = DEFAULT_ENTITY;
        float power = DEFAULT_POWER;
        String property = DEFAULT_PROPERTY;
        boolean hideOutput = DEFAULT_HIDE_OUTPUT;
        explode(sender, location, source, power, property, hideOutput);
      });
      commandAPICommand.register();

      commandAPICommand = getCommandBase(command, permission, aliases).withArguments(list01);
      commandAPICommand = commandAPICommand.executesNative((sender, args) ->
      {
        Location location = getLocation(sender);
        Entity source = DEFAULT_ENTITY;
        float power = DEFAULT_POWER;
        String property = DEFAULT_PROPERTY;
        boolean hideOutput = (boolean) args.get(0);
        explode(sender, location, source, power, property, hideOutput);
      });
      commandAPICommand.register();

      commandAPICommand = getCommandBase(command, permission, aliases).withArguments(list02);
      commandAPICommand = commandAPICommand.executesNative((sender, args) ->
      {
        Location location = getLocation(sender);
        Entity source = DEFAULT_ENTITY;
        float power = (float) args.get(0);
        String property = DEFAULT_PROPERTY;
        boolean hideOutput = DEFAULT_HIDE_OUTPUT;
        explode(sender, location, source, power, property, hideOutput);
      });
      commandAPICommand.register();

      commandAPICommand = getCommandBase(command, permission, aliases).withArguments(list03);
      commandAPICommand = commandAPICommand.executesNative((sender, args) ->
      {
        Location location = getLocation(sender);
        Entity source = DEFAULT_ENTITY;
        float power = (float) args.get(0);
        String property = DEFAULT_PROPERTY;
        boolean hideOutput = (boolean) args.get(1);
        explode(sender, location, source, power, property, hideOutput);
      });
      commandAPICommand.register();

      commandAPICommand = getCommandBase(command, permission, aliases).withArguments(list04);
      commandAPICommand = commandAPICommand.executesNative((sender, args) ->
      {
        Location location = getLocation(sender);
        Entity source = DEFAULT_ENTITY;
        float power = (float) args.get(0);
        String property = (String) args.get(1);
        boolean hideOutput = DEFAULT_HIDE_OUTPUT;
        explode(sender, location, source, power, property, hideOutput);
      });
      commandAPICommand.register();

      commandAPICommand = getCommandBase(command, permission, aliases).withArguments(list05);
      commandAPICommand = commandAPICommand.executesNative((sender, args) ->
      {
        Location location = getLocation(sender);
        Entity source = DEFAULT_ENTITY;
        float power = (float) args.get(0);
        String property = (String) args.get(1);
        boolean hideOutput = (boolean) args.get(2);
        explode(sender, location, source, power, property, hideOutput);
      });
      commandAPICommand.register();

      commandAPICommand = getCommandBase(command, permission, aliases).withArguments(list06);
      commandAPICommand = commandAPICommand.executesNative((sender, args) ->
      {
        Location location = getLocation(sender);
        Entity source = DEFAULT_ENTITY;
        float power = DEFAULT_POWER;
        String property = (String) args.get(0);
        boolean hideOutput = DEFAULT_HIDE_OUTPUT;
        explode(sender, location, source, power, property, hideOutput);
      });
      commandAPICommand.register();

      commandAPICommand = getCommandBase(command, permission, aliases).withArguments(list07);
      commandAPICommand = commandAPICommand.executesNative((sender, args) ->
      {
        Location location = getLocation(sender);
        Entity source = DEFAULT_ENTITY;
        float power = DEFAULT_POWER;
        String property = (String) args.get(0);
        boolean hideOutput = (boolean) args.get(1);
        explode(sender, location, source, power, property, hideOutput);
      });
      commandAPICommand.register();

      commandAPICommand = getCommandBase(command, permission, aliases).withArguments(list08);
      commandAPICommand = commandAPICommand.executesNative((sender, args) ->
      {
        Location location = getLocation(sender);
        Entity source = DEFAULT_ENTITY;
        float power = (float) args.get(1);
        String property = (String) args.get(0);
        boolean hideOutput = DEFAULT_HIDE_OUTPUT;
        explode(sender, location, source, power, property, hideOutput);
      });
      commandAPICommand.register();

      commandAPICommand = getCommandBase(command, permission, aliases).withArguments(list09);
      commandAPICommand = commandAPICommand.executesNative((sender, args) ->
      {
        Location location = getLocation(sender);
        Entity source = DEFAULT_ENTITY;
        float power = (float) args.get(1);
        String property = (String) args.get(0);
        boolean hideOutput = (boolean) args.get(2);
        explode(sender, location, source, power, property, hideOutput);
      });
      commandAPICommand.register();
    }

    {
      commandAPICommand = getCommandBase(command, permission, aliases).withArguments(list10);
      commandAPICommand = commandAPICommand.executesNative((sender, args) ->
      {
        Location location = getLocation(sender);
        Entity source = (Entity) args.get(0);
        float power = DEFAULT_POWER;
        String property = DEFAULT_PROPERTY;
        boolean hideOutput = DEFAULT_HIDE_OUTPUT;
        explode(sender, location, source, power, property, hideOutput);
      });
      commandAPICommand.register();

      commandAPICommand = getCommandBase(command, permission, aliases).withArguments(list11);
      commandAPICommand = commandAPICommand.executesNative((sender, args) ->
      {
        Location location = getLocation(sender);
        Entity source = (Entity) args.get(0);
        float power = DEFAULT_POWER;
        String property = DEFAULT_PROPERTY;
        boolean hideOutput = (boolean) args.get(1);
        explode(sender, location, source, power, property, hideOutput);
      });
      commandAPICommand.register();

      commandAPICommand = getCommandBase(command, permission, aliases).withArguments(list12);
      commandAPICommand = commandAPICommand.executesNative((sender, args) ->
      {
        Location location = getLocation(sender);
        Entity source = (Entity) args.get(0);
        float power = (float) args.get(1);
        String property = DEFAULT_PROPERTY;
        boolean hideOutput = DEFAULT_HIDE_OUTPUT;
        explode(sender, location, source, power, property, hideOutput);
      });
      commandAPICommand.register();

      commandAPICommand = getCommandBase(command, permission, aliases).withArguments(list13);
      commandAPICommand = commandAPICommand.executesNative((sender, args) ->
      {
        Location location = getLocation(sender);
        Entity source = (Entity) args.get(0);
        float power = (float) args.get(1);
        String property = DEFAULT_PROPERTY;
        boolean hideOutput = (boolean) args.get(2);
        explode(sender, location, source, power, property, hideOutput);
      });
      commandAPICommand.register();

      commandAPICommand = getCommandBase(command, permission, aliases).withArguments(list14);
      commandAPICommand = commandAPICommand.executesNative((sender, args) ->
      {
        Location location = getLocation(sender);
        Entity source = (Entity) args.get(0);
        float power = (float) args.get(1);
        String property = (String) args.get(2);
        boolean hideOutput = DEFAULT_HIDE_OUTPUT;
        explode(sender, location, source, power, property, hideOutput);
      });
      commandAPICommand.register();

      commandAPICommand = getCommandBase(command, permission, aliases).withArguments(list15);
      commandAPICommand = commandAPICommand.executesNative((sender, args) ->
      {
        Location location = getLocation(sender);
        Entity source = (Entity) args.get(0);
        float power = (float) args.get(1);
        String property = (String) args.get(2);
        boolean hideOutput = (boolean) args.get(3);
        explode(sender, location, source, power, property, hideOutput);
      });
      commandAPICommand.register();

      commandAPICommand = getCommandBase(command, permission, aliases).withArguments(list16);
      commandAPICommand = commandAPICommand.executesNative((sender, args) ->
      {
        Location location = getLocation(sender);
        Entity source = (Entity) args.get(0);
        float power = DEFAULT_POWER;
        String property = (String) args.get(1);
        boolean hideOutput = DEFAULT_HIDE_OUTPUT;
        explode(sender, location, source, power, property, hideOutput);
      });
      commandAPICommand.register();

      commandAPICommand = getCommandBase(command, permission, aliases).withArguments(list17);
      commandAPICommand = commandAPICommand.executesNative((sender, args) ->
      {
        Location location = getLocation(sender);
        Entity source = (Entity) args.get(0);
        float power = DEFAULT_POWER;
        String property = (String) args.get(1);
        boolean hideOutput = (boolean) args.get(2);
        explode(sender, location, source, power, property, hideOutput);
      });
      commandAPICommand.register();

      commandAPICommand = getCommandBase(command, permission, aliases).withArguments(list18);
      commandAPICommand = commandAPICommand.executesNative((sender, args) ->
      {
        Location location = getLocation(sender);
        Entity source = (Entity) args.get(0);
        float power = (float) args.get(2);
        String property = (String) args.get(1);
        boolean hideOutput = DEFAULT_HIDE_OUTPUT;
        explode(sender, location, source, power, property, hideOutput);
      });
      commandAPICommand.register();

      commandAPICommand = getCommandBase(command, permission, aliases).withArguments(list19);
      commandAPICommand = commandAPICommand.executesNative((sender, args) ->
      {
        Location location = getLocation(sender);
        Entity source = (Entity) args.get(0);
        float power = (float) args.get(2);
        String property = (String) args.get(1);
        boolean hideOutput = (boolean) args.get(3);
        explode(sender, location, source, power, property, hideOutput);
      });
      commandAPICommand.register();
    }

    {
      commandAPICommand = getCommandBase(command, permission, aliases).withArguments(list20);
      commandAPICommand = commandAPICommand.executesNative((sender, args) ->
      {
        Location location = (Location) args.get(0);
        Entity source = DEFAULT_ENTITY;
        float power = DEFAULT_POWER;
        String property = DEFAULT_PROPERTY;
        boolean hideOutput = DEFAULT_HIDE_OUTPUT;
        explode(sender, location, source, power, property, hideOutput);
      });
      commandAPICommand.register();

      commandAPICommand = getCommandBase(command, permission, aliases).withArguments(list21);
      commandAPICommand = commandAPICommand.executesNative((sender, args) ->
      {
        Location location = (Location) args.get(0);
        Entity source = DEFAULT_ENTITY;
        float power = DEFAULT_POWER;
        String property = DEFAULT_PROPERTY;
        boolean hideOutput = (boolean) args.get(1);
        explode(sender, location, source, power, property, hideOutput);
      });
      commandAPICommand.register();

      commandAPICommand = getCommandBase(command, permission, aliases).withArguments(list22);
      commandAPICommand = commandAPICommand.executesNative((sender, args) ->
      {
        Location location = (Location) args.get(0);
        Entity source = DEFAULT_ENTITY;
        float power = (float) args.get(1);
        String property = DEFAULT_PROPERTY;
        boolean hideOutput = DEFAULT_HIDE_OUTPUT;
        explode(sender, location, source, power, property, hideOutput);
      });
      commandAPICommand.register();

      commandAPICommand = getCommandBase(command, permission, aliases).withArguments(list23);
      commandAPICommand = commandAPICommand.executesNative((sender, args) ->
      {
        Location location = (Location) args.get(0);
        Entity source = DEFAULT_ENTITY;
        float power = (float) args.get(1);
        String property = DEFAULT_PROPERTY;
        boolean hideOutput = (boolean) args.get(2);
        explode(sender, location, source, power, property, hideOutput);
      });
      commandAPICommand.register();

      commandAPICommand = getCommandBase(command, permission, aliases).withArguments(list24);
      commandAPICommand = commandAPICommand.executesNative((sender, args) ->
      {
        Location location = (Location) args.get(0);
        Entity source = DEFAULT_ENTITY;
        float power = (float) args.get(1);
        String property = (String) args.get(2);
        boolean hideOutput = DEFAULT_HIDE_OUTPUT;
        explode(sender, location, source, power, property, hideOutput);
      });
      commandAPICommand.register();

      commandAPICommand = getCommandBase(command, permission, aliases).withArguments(list25);
      commandAPICommand = commandAPICommand.executesNative((sender, args) ->
      {
        Location location = (Location) args.get(0);
        Entity source = DEFAULT_ENTITY;
        float power = (float) args.get(1);
        String property = (String) args.get(2);
        boolean hideOutput = (boolean) args.get(3);
        explode(sender, location, source, power, property, hideOutput);
      });
      commandAPICommand.register();

      commandAPICommand = getCommandBase(command, permission, aliases).withArguments(list26);
      commandAPICommand = commandAPICommand.executesNative((sender, args) ->
      {
        Location location = (Location) args.get(0);
        Entity source = DEFAULT_ENTITY;
        float power = DEFAULT_POWER;
        String property = (String) args.get(1);
        boolean hideOutput = DEFAULT_HIDE_OUTPUT;
        explode(sender, location, source, power, property, hideOutput);
      });
      commandAPICommand.register();

      commandAPICommand = getCommandBase(command, permission, aliases).withArguments(list27);
      commandAPICommand = commandAPICommand.executesNative((sender, args) ->
      {
        Location location = (Location) args.get(0);
        Entity source = DEFAULT_ENTITY;
        float power = DEFAULT_POWER;
        String property = (String) args.get(1);
        boolean hideOutput = (boolean) args.get(2);
        explode(sender, location, source, power, property, hideOutput);
      });
      commandAPICommand.register();

      commandAPICommand = getCommandBase(command, permission, aliases).withArguments(list28);
      commandAPICommand = commandAPICommand.executesNative((sender, args) ->
      {
        Location location = (Location) args.get(0);
        Entity source = DEFAULT_ENTITY;
        float power = (float) args.get(2);
        String property = (String) args.get(1);
        boolean hideOutput = DEFAULT_HIDE_OUTPUT;
        explode(sender, location, source, power, property, hideOutput);
      });
      commandAPICommand.register();

      commandAPICommand = getCommandBase(command, permission, aliases).withArguments(list29);
      commandAPICommand = commandAPICommand.executesNative((sender, args) ->
      {
        Location location = (Location) args.get(0);
        Entity source = DEFAULT_ENTITY;
        float power = (float) args.get(2);
        String property = (String) args.get(1);
        boolean hideOutput = (boolean) args.get(3);
        explode(sender, location, source, power, property, hideOutput);
      });
      commandAPICommand.register();
    }

    {
      commandAPICommand = getCommandBase(command, permission, aliases).withArguments(list30);
      commandAPICommand = commandAPICommand.executesNative((sender, args) ->
      {
        Location location = (Location) args.get(0);
        Entity source = (Entity) args.get(1);
        float power = DEFAULT_POWER;
        String property = DEFAULT_PROPERTY;
        boolean hideOutput = DEFAULT_HIDE_OUTPUT;
        explode(sender, location, source, power, property, hideOutput);
      });
      commandAPICommand.register();

      commandAPICommand = getCommandBase(command, permission, aliases).withArguments(list31);
      commandAPICommand = commandAPICommand.executesNative((sender, args) ->
      {
        Location location = (Location) args.get(0);
        Entity source = (Entity) args.get(1);
        float power = DEFAULT_POWER;
        String property = DEFAULT_PROPERTY;
        boolean hideOutput = (boolean) args.get(2);
        explode(sender, location, source, power, property, hideOutput);
      });
      commandAPICommand.register();

      commandAPICommand = getCommandBase(command, permission, aliases).withArguments(list32);
      commandAPICommand = commandAPICommand.executesNative((sender, args) ->
      {
        Location location = (Location) args.get(0);
        Entity source = (Entity) args.get(1);
        float power = (float) args.get(2);
        String property = DEFAULT_PROPERTY;
        boolean hideOutput = DEFAULT_HIDE_OUTPUT;
        explode(sender, location, source, power, property, hideOutput);
      });
      commandAPICommand.register();

      commandAPICommand = getCommandBase(command, permission, aliases).withArguments(list33);
      commandAPICommand = commandAPICommand.executesNative((sender, args) ->
      {
        Location location = (Location) args.get(0);
        Entity source = (Entity) args.get(1);
        float power = (float) args.get(2);
        String property = DEFAULT_PROPERTY;
        boolean hideOutput = (boolean) args.get(3);
        explode(sender, location, source, power, property, hideOutput);
      });
      commandAPICommand.register();

      commandAPICommand = getCommandBase(command, permission, aliases).withArguments(list34);
      commandAPICommand = commandAPICommand.executesNative((sender, args) ->
      {
        Location location = (Location) args.get(0);
        Entity source = (Entity) args.get(1);
        float power = (float) args.get(2);
        String property = (String) args.get(3);
        boolean hideOutput = DEFAULT_HIDE_OUTPUT;
        explode(sender, location, source, power, property, hideOutput);
      });
      commandAPICommand.register();

      commandAPICommand = getCommandBase(command, permission, aliases).withArguments(list35);
      commandAPICommand = commandAPICommand.executesNative((sender, args) ->
      {
        Location location = (Location) args.get(0);
        Entity source = (Entity) args.get(1);
        float power = (float) args.get(2);
        String property = (String) args.get(3);
        boolean hideOutput = (boolean) args.get(4);
        explode(sender, location, source, power, property, hideOutput);
      });
      commandAPICommand.register();

      commandAPICommand = getCommandBase(command, permission, aliases).withArguments(list36);
      commandAPICommand = commandAPICommand.executesNative((sender, args) ->
      {
        Location location = (Location) args.get(0);
        Entity source = (Entity) args.get(1);
        float power = DEFAULT_POWER;
        String property = (String) args.get(2);
        boolean hideOutput = DEFAULT_HIDE_OUTPUT;
        explode(sender, location, source, power, property, hideOutput);
      });
      commandAPICommand.register();

      commandAPICommand = getCommandBase(command, permission, aliases).withArguments(list37);
      commandAPICommand = commandAPICommand.executesNative((sender, args) ->
      {
        Location location = (Location) args.get(0);
        Entity source = (Entity) args.get(1);
        float power = DEFAULT_POWER;
        String property = (String) args.get(2);
        boolean hideOutput = (boolean) args.get(3);
        explode(sender, location, source, power, property, hideOutput);
      });
      commandAPICommand.register();

      commandAPICommand = getCommandBase(command, permission, aliases).withArguments(list38);
      commandAPICommand = commandAPICommand.executesNative((sender, args) ->
      {
        Location location = (Location) args.get(0);
        Entity source = (Entity) args.get(1);
        float power = (float) args.get(2);
        String property = (String) args.get(3);
        boolean hideOutput = DEFAULT_HIDE_OUTPUT;
        explode(sender, location, source, power, property, hideOutput);
      });
      commandAPICommand.register();

      commandAPICommand = getCommandBase(command, permission, aliases).withArguments(list39);
      commandAPICommand = commandAPICommand.executesNative((sender, args) ->
      {
        Location location = (Location) args.get(0);
        Entity source = (Entity) args.get(1);
        float power = (float) args.get(3);
        String property = (String) args.get(2);
        boolean hideOutput = (boolean) args.get(4);
        explode(sender, location, source, power, property, hideOutput);
      });
      commandAPICommand.register();
    }

    {
      commandAPICommand = getCommandBase(command, permission, aliases).withArguments(list40);
      commandAPICommand = commandAPICommand.executesNative((sender, args) ->
      {
        Location location = (Location) args.get(1);
        Entity source = (Entity) args.get(0);
        float power = DEFAULT_POWER;
        String property = DEFAULT_PROPERTY;
        boolean hideOutput = DEFAULT_HIDE_OUTPUT;
        explode(sender, location, source, power, property, hideOutput);
      });
      commandAPICommand.register();

      commandAPICommand = getCommandBase(command, permission, aliases).withArguments(list41);
      commandAPICommand = commandAPICommand.executesNative((sender, args) ->
      {
        Location location = (Location) args.get(1);
        Entity source = (Entity) args.get(0);
        float power = DEFAULT_POWER;
        String property = DEFAULT_PROPERTY;
        boolean hideOutput = (boolean) args.get(2);
        explode(sender, location, source, power, property, hideOutput);
      });
      commandAPICommand.register();

      commandAPICommand = getCommandBase(command, permission, aliases).withArguments(list42);
      commandAPICommand = commandAPICommand.executesNative((sender, args) ->
      {
        Location location = (Location) args.get(1);
        Entity source = (Entity) args.get(0);
        float power = (float) args.get(2);
        String property = DEFAULT_PROPERTY;
        boolean hideOutput = DEFAULT_HIDE_OUTPUT;
        explode(sender, location, source, power, property, hideOutput);
      });
      commandAPICommand.register();

      commandAPICommand = getCommandBase(command, permission, aliases).withArguments(list43);
      commandAPICommand = commandAPICommand.executesNative((sender, args) ->
      {
        Location location = (Location) args.get(1);
        Entity source = (Entity) args.get(0);
        float power = (float) args.get(2);
        String property = DEFAULT_PROPERTY;
        boolean hideOutput = (boolean) args.get(3);
        explode(sender, location, source, power, property, hideOutput);
      });
      commandAPICommand.register();

      commandAPICommand = getCommandBase(command, permission, aliases).withArguments(list44);
      commandAPICommand = commandAPICommand.executesNative((sender, args) ->
      {
        Location location = (Location) args.get(1);
        Entity source = (Entity) args.get(0);
        float power = (float) args.get(2);
        String property = (String) args.get(3);
        boolean hideOutput = DEFAULT_HIDE_OUTPUT;
        explode(sender, location, source, power, property, hideOutput);
      });
      commandAPICommand.register();

      commandAPICommand = getCommandBase(command, permission, aliases).withArguments(list45);
      commandAPICommand = commandAPICommand.executesNative((sender, args) ->
      {
        Location location = (Location) args.get(1);
        Entity source = (Entity) args.get(0);
        float power = (float) args.get(2);
        String property = (String) args.get(3);
        boolean hideOutput = (boolean) args.get(4);
        explode(sender, location, source, power, property, hideOutput);
      });
      commandAPICommand.register();

      commandAPICommand = getCommandBase(command, permission, aliases).withArguments(list46);
      commandAPICommand = commandAPICommand.executesNative((sender, args) ->
      {
        Location location = (Location) args.get(1);
        Entity source = (Entity) args.get(0);
        float power = DEFAULT_POWER;
        String property = (String) args.get(2);
        boolean hideOutput = DEFAULT_HIDE_OUTPUT;
        explode(sender, location, source, power, property, hideOutput);
      });
      commandAPICommand.register();

      commandAPICommand = getCommandBase(command, permission, aliases).withArguments(list47);
      commandAPICommand = commandAPICommand.executesNative((sender, args) ->
      {
        Location location = (Location) args.get(1);
        Entity source = (Entity) args.get(0);
        float power = DEFAULT_POWER;
        String property = (String) args.get(2);
        boolean hideOutput = (boolean) args.get(3);
        explode(sender, location, source, power, property, hideOutput);
      });
      commandAPICommand.register();

      commandAPICommand = getCommandBase(command, permission, aliases).withArguments(list48);
      commandAPICommand = commandAPICommand.executesNative((sender, args) ->
      {
        Location location = (Location) args.get(1);
        Entity source = (Entity) args.get(0);
        float power = (float) args.get(2);
        String property = (String) args.get(3);
        boolean hideOutput = DEFAULT_HIDE_OUTPUT;
        explode(sender, location, source, power, property, hideOutput);
      });
      commandAPICommand.register();

      commandAPICommand = getCommandBase(command, permission, aliases).withArguments(list49);
      commandAPICommand = commandAPICommand.executesNative((sender, args) ->
      {
        Location location = (Location) args.get(1);
        Entity source = (Entity) args.get(0);
        float power = (float) args.get(3);
        String property = (String) args.get(2);
        boolean hideOutput = (boolean) args.get(4);
        explode(sender, location, source, power, property, hideOutput);
      });
      commandAPICommand.register();
    }
  }
}
