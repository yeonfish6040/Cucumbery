package com.jho5245.cucumbery.commands.brigadier;

import com.jho5245.cucumbery.Cucumbery;
import com.jho5245.cucumbery.util.Method;
import com.jho5245.cucumbery.util.storage.CustomConfig;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.tree.LiteralCommandNode;
import dev.jorel.commandapi.Brigadier;
import dev.jorel.commandapi.IStringTooltip;
import dev.jorel.commandapi.StringTooltip;
import dev.jorel.commandapi.arguments.*;
import dev.jorel.commandapi.wrappers.FloatRange;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings({"unchecked", "rawtypes"})
public class ExtraExecuteArgument
{
  IStringTooltip[] worldNames;
  StringTooltip[] weatherTypes;

  {
    List<String> worldNames = CustomConfig.getCustomConfig("data/brigadier_tab_list.yml").getConfig().getStringList("worldNames");
    this.worldNames = new StringTooltip[worldNames.size() + 1];
    for (int i = 0; i < worldNames.size(); i++)
    {
      String worldName = worldNames.get(i);
      String display = Method.getWorldDisplayName(worldName);
      this.worldNames[i] = !display.equals(worldName) ? StringTooltip.of(worldName, display) : StringTooltip.none(display);
    }
    this.worldNames[this.worldNames.length - 1] = StringTooltip.of("__current__", "현재 월드");
  }

  {
    weatherTypes = new StringTooltip[3];
    weatherTypes[0] = StringTooltip.of("clear", "맑음");
    weatherTypes[1] = StringTooltip.of("rain", "비");
    weatherTypes[2] = StringTooltip.of("thunder", "폭풍우");
  }

  private void registerRandom()
  {
    //Register literal "random"
    LiteralCommandNode random = Brigadier.fromLiteralArgument(new LiteralArgument("random")).build();

    //Declare arguments like normal
    List<Argument> arguments = new ArrayList<>();
    arguments.add(new DoubleArgument("분자", 0));
    arguments.add(new DoubleArgument("분모", 1));

    //Get brigadier argument objects
    ArgumentBuilder numerator = Brigadier.fromArgument(arguments, "분자");
    ArgumentBuilder denominator = Brigadier.fromArgument(arguments, "분모")
            //Fork redirecting to "execute" and state our predicate
            .fork(Brigadier.getRootNode().getChild("execute"), Brigadier.fromPredicate((sender, args) ->
            {
              //Parse arguments like normal

              //Return boolean with a num/denom chance
              return Math.random() * (double) args[1] <= (double) args[0];
            }, arguments));

    //Add <numerator> <denominator> as a child of randomchance
    random.addChild(numerator.then(denominator).build());

    //Add (randomchance <numerator> <denominator>) as a child of (execute -> if)
    Brigadier.getRootNode().getChild("execute").getChild("if").addChild(random);
  }

  private void registerRandomChance()
  {
    LiteralCommandNode randomChance = Brigadier.fromLiteralArgument(new LiteralArgument("randomchance")).build();

    List<Argument> arguments = new ArrayList<>();
    arguments.add(new DoubleArgument("확률(%)", 0, 100));

    ArgumentBuilder builder1 = Brigadier.fromArgument(arguments, "확률(%)");
    builder1.fork(Brigadier.getRootNode().getChild("execute"), Brigadier.fromPredicate((sender, args) ->
    {
      double chance = (double) args[0];

      return Math.random() * 100d <= chance;
    }, arguments));

    randomChance.addChild(builder1.build());
    Brigadier.getRootNode().getChild("execute").getChild("if").addChild(randomChance);
  }

  private void registerIfHasPermission()
  {
    LiteralCommandNode hasPermission = Brigadier.fromLiteralArgument(new LiteralArgument("haspermission")).build();

    List<Argument> arguments = new ArrayList<>();
    arguments.add(new EntitySelectorArgument("플레이어", EntitySelectorArgument.EntitySelector.ONE_PLAYER));
    arguments.add(new StringArgument("퍼미션 노드"));

    ArgumentBuilder builder1 = Brigadier.fromArgument(arguments, "플레이어");
    ArgumentBuilder builder2 = Brigadier.fromArgument(arguments, "퍼미션 노드");
    builder2.fork(Brigadier.getRootNode().getChild("execute"), Brigadier.fromPredicate((sender, args) ->
    {
      Player player = (Player) args[0];
      String permission = (String) args[1];
      return player.hasPermission(permission);
    }, arguments));

    hasPermission.addChild(builder1.then(builder2).build());
    Brigadier.getRootNode().getChild("execute").getChild("if").addChild(hasPermission);
  }

  private void registerUnlessHasPermission()
  {
    LiteralCommandNode hasPermission = Brigadier.fromLiteralArgument(new LiteralArgument("haspermission")).build();

    List<Argument> arguments = new ArrayList<>();
    arguments.add(new EntitySelectorArgument("플레이어", EntitySelectorArgument.EntitySelector.ONE_PLAYER));
    arguments.add(new StringArgument("퍼미션 노드"));

    ArgumentBuilder builder1 = Brigadier.fromArgument(arguments, "플레이어");
    ArgumentBuilder builder2 = Brigadier.fromArgument(arguments, "퍼미션 노드");
    builder2.fork(Brigadier.getRootNode().getChild("execute"), Brigadier.fromPredicate((sender, args) ->
    {
      Player player = (Player) args[0];
      String permission = (String) args[1];
      return !player.hasPermission(permission);
    }, arguments));

    hasPermission.addChild(builder1.then(builder2).build());
    Brigadier.getRootNode().getChild("execute").getChild("unless").addChild(hasPermission);
  }

  private void registerIfIsInWorld()
  {
    LiteralCommandNode isInWorld = Brigadier.fromLiteralArgument(new LiteralArgument("world")).build();

    List<Argument> arguments = new ArrayList<>();
    arguments.add(new EntitySelectorArgument("개체", EntitySelectorArgument.EntitySelector.ONE_ENTITY));
    arguments.add(new StringArgument("월드").replaceSuggestionsT(info -> worldNames));

    ArgumentBuilder builder1 = Brigadier.fromArgument(arguments, "개체");
    ArgumentBuilder builder2 = Brigadier.fromArgument(arguments, "월드");
    builder2.fork(Brigadier.getRootNode().getChild("execute"), Brigadier.fromPredicate((sender, args) ->
    {
      Entity entity = (Entity) args[0];
      String worldName = (String) args[1];
      World world;
      if (worldName.equals("__current__"))
      {
        if (sender instanceof Player player)
        {
          world = player.getWorld();
        }
        else if (sender instanceof BlockCommandSender blockCommandSender)
        {
          world = blockCommandSender.getBlock().getWorld();
        }
        else
        {
          world = Bukkit.getWorlds().get(0);
        }
      }
      else
      {
        world = Bukkit.getWorld(worldName);
      }
      return world != null && entity.getWorld().getName().equals(world.getName());
    }, arguments));

    isInWorld.addChild(builder1.then(builder2).build());
    Brigadier.getRootNode().getChild("execute").getChild("if").addChild(isInWorld);
  }

  private void registerUnlessIsInWorld()
  {
    LiteralCommandNode isInWorld = Brigadier.fromLiteralArgument(new LiteralArgument("world")).build();

    List<Argument> arguments = new ArrayList<>();
    arguments.add(new EntitySelectorArgument("개체", EntitySelectorArgument.EntitySelector.ONE_ENTITY));
    arguments.add(new StringArgument("월드").replaceSuggestionsT(info -> worldNames));

    ArgumentBuilder builder1 = Brigadier.fromArgument(arguments, "개체");
    ArgumentBuilder builder2 = Brigadier.fromArgument(arguments, "월드");
    builder2.fork(Brigadier.getRootNode().getChild("execute"), Brigadier.fromPredicate((sender, args) ->
    {
      Entity entity = (Entity) args[0];
      String worldName = (String) args[1];
      World world;
      if (worldName.equals("__current__"))
      {
        if (sender instanceof Player player)
        {
          world = player.getWorld();
        }
        else if (sender instanceof BlockCommandSender blockCommandSender)
        {
          world = blockCommandSender.getBlock().getWorld();
        }
        else
        {
          world = Bukkit.getWorlds().get(0);
        }
      }
      else
      {
        world = Bukkit.getWorld(worldName);
      }
      return !(world != null && entity.getWorld().getName().equals(world.getName()));
    }, arguments));

    isInWorld.addChild(builder1.then(builder2).build());
    Brigadier.getRootNode().getChild("execute").getChild("unless").addChild(isInWorld);
  }

  private void registerIfWeather()
  {
    LiteralCommandNode weather = Brigadier.fromLiteralArgument(new LiteralArgument("weather")).build();

    List<Argument> arguments = new ArrayList<>();
    arguments.add(new StringArgument("월드").replaceSuggestionsT(info -> worldNames));
    arguments.add(new StringArgument("날씨").replaceSuggestionsT(info -> weatherTypes));

    ArgumentBuilder builder1 = Brigadier.fromArgument(arguments, "월드");
    ArgumentBuilder builder2 = Brigadier.fromArgument(arguments, "날씨");
    builder2.fork(Brigadier.getRootNode().getChild("execute"), Brigadier.fromPredicate((sender, args) ->
    {
      String worldName = (String) args[0];
      String weatherType = (String) args[1];
      @Nullable World world;
      if (worldName.equals("__current__"))
      {
        if (sender instanceof Player player)
        {
          world = player.getWorld();
        }
        else if (sender instanceof BlockCommandSender blockCommandSender)
        {
          world = blockCommandSender.getBlock().getWorld();
        }
        else
        {
          world = null;
        }
      }
      else
      {
        world = Bukkit.getWorld(worldName);
      }
      return world != null && switch (weatherType)
              {
                case "clear" -> !world.hasStorm();
                case "rain" -> world.hasStorm() && !world.isThundering();
                case "thunder" -> world.hasStorm() && world.isThundering();
                default -> false;
              };
    }, arguments));

    weather.addChild(builder1.then(builder2).build());
    Brigadier.getRootNode().getChild("execute").getChild("if").addChild(weather);
  }

  private void registerUnlessWeather()
  {
    LiteralCommandNode weather = Brigadier.fromLiteralArgument(new LiteralArgument("weather")).build();

    List<Argument> arguments = new ArrayList<>();
    arguments.add(new StringArgument("월드").replaceSuggestionsT(info -> worldNames));
    arguments.add(new StringArgument("날씨").replaceSuggestionsT(info -> weatherTypes));

    ArgumentBuilder builder1 = Brigadier.fromArgument(arguments, "월드");
    ArgumentBuilder builder2 = Brigadier.fromArgument(arguments, "날씨");
    builder2.fork(Brigadier.getRootNode().getChild("execute"), Brigadier.fromPredicate((sender, args) ->
    {
      String worldName = (String) args[0];
      String weatherType = (String) args[1];
      @Nullable World world;
      if (worldName.equals("__current__"))
      {
        if (sender instanceof Player player)
        {
          world = player.getWorld();
        }
        else if (sender instanceof BlockCommandSender blockCommandSender)
        {
          world = blockCommandSender.getBlock().getWorld();
        }
        else
        {
          world = null;
        }
      }
      else
      {
        world = Bukkit.getWorld(worldName);
      }
      return world == null || switch (weatherType)
              {
                case "clear" -> world.hasStorm();
                case "rain" -> !(world.hasStorm() && !world.isThundering());
                case "thunder" -> !(world.hasStorm() && world.isThundering());
                default -> true;
              };
    }, arguments));

    weather.addChild(builder1.then(builder2).build());
    Brigadier.getRootNode().getChild("execute").getChild("unless").addChild(weather);
  }

  private void registerIfPeriod()
  {
    LiteralCommandNode period1 = Brigadier.fromLiteralArgument(new LiteralArgument("period")).build();

    List<Argument> arguments = new ArrayList<>();
    arguments.add(new LongArgument("주기(틱)", 1, Long.MAX_VALUE));
    arguments.add(new LongArgument("오프셋"));

    ArgumentBuilder builder1 = Brigadier.fromArgument(arguments, "주기(틱)");
    ArgumentBuilder builder2 = Brigadier.fromArgument(arguments, "오프셋");
    builder2.fork(Brigadier.getRootNode().getChild("execute"), Brigadier.fromPredicate((sender, args) ->
    {
      long period = (long) args[0];
      long offSet = -(long) args[1];
      return (Cucumbery.runTime + offSet) % period == 0;
    }, arguments));

    period1.addChild(builder1.then(builder2).build());
    Brigadier.getRootNode().getChild("execute").getChild("if").addChild(period1);
  }

  private void registerUnlessPeriod()
  {
    LiteralCommandNode period1 = Brigadier.fromLiteralArgument(new LiteralArgument("period")).build();

    List<Argument> arguments = new ArrayList<>();
    arguments.add(new LongArgument("주기(틱)", 1, Long.MAX_VALUE));
    arguments.add(new LongArgument("오프셋"));

    ArgumentBuilder builder1 = Brigadier.fromArgument(arguments, "주기(틱)");
    ArgumentBuilder builder2 = Brigadier.fromArgument(arguments, "오프셋");
    builder2.fork(Brigadier.getRootNode().getChild("execute"), Brigadier.fromPredicate((sender, args) ->
    {
      long period = (long) args[0];
      long offSet = -(long) args[1];
      return (Cucumbery.runTime + offSet) % period != 0;
    }, arguments));

    period1.addChild(builder1.then(builder2).build());
    Brigadier.getRootNode().getChild("execute").getChild("unless").addChild(period1);
  }

  private void registerIfRealPeriod()
  {
    LiteralCommandNode period1 = Brigadier.fromLiteralArgument(new LiteralArgument("real-period")).build();

    List<Argument> arguments = new ArrayList<>();
    arguments.add(new LongArgument("주기(틱)", 1, Long.MAX_VALUE));
    arguments.add(new LongArgument("오프셋"));

    ArgumentBuilder builder1 = Brigadier.fromArgument(arguments, "주기(틱)");
    ArgumentBuilder builder2 = Brigadier.fromArgument(arguments, "오프셋");
    builder2.fork(Brigadier.getRootNode().getChild("execute"), Brigadier.fromPredicate((sender, args) ->
    {
      long period = (long) args[0];
      long offSet = -(long) args[1];
      return (System.currentTimeMillis() / 50 + offSet) % period == 0;
    }, arguments));

    period1.addChild(builder1.then(builder2).build());
    Brigadier.getRootNode().getChild("execute").getChild("if").addChild(period1);
  }

  private void registerUnlessRealPeriod()
  {
    LiteralCommandNode period1 = Brigadier.fromLiteralArgument(new LiteralArgument("real-period")).build();

    List<Argument> arguments = new ArrayList<>();
    arguments.add(new LongArgument("주기(틱)", 1, Long.MAX_VALUE));
    arguments.add(new LongArgument("오프셋"));

    ArgumentBuilder builder1 = Brigadier.fromArgument(arguments, "주기(틱)");
    ArgumentBuilder builder2 = Brigadier.fromArgument(arguments, "오프셋");
    builder2.fork(Brigadier.getRootNode().getChild("execute"), Brigadier.fromPredicate((sender, args) ->
    {
      long period = (long) args[0];
      long offSet = -(long) args[1];
      return (System.currentTimeMillis() / 50 + offSet) % period != 0;
    }, arguments));

    period1.addChild(builder1.then(builder2).build());
    Brigadier.getRootNode().getChild("execute").getChild("unless").addChild(period1);
  }

  private void registerIfMoneyRadius()
  {
    LiteralCommandNode moneyRadius = Brigadier.fromLiteralArgument(new LiteralArgument("money")).build();
    List<Argument> arguments = new ArrayList<>();
    arguments.add(new EntitySelectorArgument("플레이어", EntitySelectorArgument.EntitySelector.ONE_PLAYER));
    arguments.add(new FloatRangeArgument("소지 금액"));
    ArgumentBuilder builder = Brigadier.fromArgument(arguments, "플레이어");
    ArgumentBuilder builder2 = Brigadier.fromArgument(arguments, "소지 금액");
    builder2.fork(Brigadier.getRootNode().getChild("execute"), Brigadier.fromPredicate((sender, args) ->
    {
      Player player = (Player) args[0];
      FloatRange floatRange = (FloatRange) args[1];
      try
      {
        return floatRange.isInRange((float) Cucumbery.eco.getBalance(player));
      }
      catch (Error | Exception e)
      {
        return false;
      }
    }, arguments));

    moneyRadius.addChild(builder.then(builder2).build());
    Brigadier.getRootNode().getChild("execute").getChild("if").addChild(moneyRadius);
  }

  private void registerUnlessMoneyRadius()
  {
    LiteralCommandNode moneyRadius = Brigadier.fromLiteralArgument(new LiteralArgument("money")).build();
    List<Argument> arguments = new ArrayList<>();
    arguments.add(new EntitySelectorArgument("플레이어", EntitySelectorArgument.EntitySelector.ONE_PLAYER));
    arguments.add(new FloatRangeArgument("소지 금액"));
    ArgumentBuilder builder = Brigadier.fromArgument(arguments, "플레이어");
    ArgumentBuilder builder2 = Brigadier.fromArgument(arguments, "소지 금액");
    builder2.fork(Brigadier.getRootNode().getChild("execute"), Brigadier.fromPredicate((sender, args) ->
    {
      Player player = (Player) args[0];
      FloatRange floatRange = (FloatRange) args[1];
      try
      {
        return !floatRange.isInRange((float) Cucumbery.eco.getBalance(player));
      }
      catch (Error | Exception e)
      {
        return true;
      }
    }, arguments));

    moneyRadius.addChild(builder.then(builder2).build());
    Brigadier.getRootNode().getChild("execute").getChild("unless").addChild(moneyRadius);
  }

  public void registerArgument()
  {
    this.registerRandom();
    this.registerRandomChance();

    this.registerIfHasPermission();
    this.registerUnlessHasPermission();

    this.registerIfIsInWorld();
    this.registerUnlessIsInWorld();

    this.registerIfWeather();
    this.registerUnlessWeather();

    this.registerIfPeriod();
    this.registerUnlessPeriod();

    this.registerIfRealPeriod();
    this.registerUnlessRealPeriod();

    this.registerIfMoneyRadius();
    this.registerUnlessMoneyRadius();
  }
}
