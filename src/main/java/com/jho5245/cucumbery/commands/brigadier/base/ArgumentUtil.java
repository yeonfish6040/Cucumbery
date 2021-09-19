package com.jho5245.cucumbery.commands.brigadier.base;

import dev.jorel.commandapi.arguments.*;
import dev.jorel.commandapi.arguments.EntitySelectorArgument.EntitySelector;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

public class ArgumentUtil
{
  @NotNull
  public static Argument of(@NotNull ArgumentType type)
  {
    return of(type, new Object[]{});
  }

  @NotNull
  public static Argument of(@NotNull ArgumentType type, @NotNull Object... params)
  {
    String key = null;
    double min = -Double.MAX_VALUE, max = Double.MAX_VALUE;
    if (params.length > 0)
    {
      if (type != ArgumentType.LITERAL && params[0] instanceof String string)
      {
        key = string;
      }
      if (params.length > 1 && params.length < 4)
      {
        if (params[1] instanceof Number number)
        {
          min = number.doubleValue();
        }
        if (params.length == 3 && params[2] instanceof Number number)
        {
          max = number.doubleValue();
        }
      }
    }
    return switch (type)
    {
      case BOOLEAN -> new BooleanArgument(key != null ? key : "값");
      case HIDE_OUTPUT -> new BooleanArgument(key != null ? key : "명령어 출력 숨김 여부");
      case LITERAL -> new MultiLiteralArgument(Arrays.stream(params).map(Object::toString).toArray(String[]::new));
      case INTEGER -> new IntegerArgument(key != null ? key : "값", (int) Math.max(Integer.MIN_VALUE, min), (int) Math.min(Integer.MAX_VALUE, max));
      case LONG -> new LongArgument(key != null ? key : "값", (long) Math.max(Long.MIN_VALUE, min), (long) Math.min(Long.MAX_VALUE, max));
      case FLOAT -> new FloatArgument(key != null ? key : "값", (float) Math.max(-Float.MAX_VALUE, min), (float) Math.min(Float.MAX_VALUE, max));
      case DOUBLE -> new DoubleArgument(key != null ? key : "값", min, max);
      case ONE_PLAYER -> new PlayerArgument(key != null ? key : "플레이어");
      case MANY_PLAYERS -> new EntitySelectorArgument(key != null ? key : "여러 플레이어", EntitySelector.MANY_PLAYERS);
      case ONE_ENTITY -> new EntitySelectorArgument(key != null ? key : "개체", EntitySelector.ONE_ENTITY);
      case MANY_ENTITIES -> new EntitySelectorArgument(key != null ? key : "여러 개체", EntitySelector.MANY_ENTITIES);
      case ITEMSTACK -> new ItemStackArgument(key != null ? key : "아이템");
      case LOCATION -> new LocationArgument(key != null ? key : "좌표");
    };
  }

  public enum ArgumentType
  {
    BOOLEAN,
    HIDE_OUTPUT,
    LITERAL,
    INTEGER,
    LONG,
    FLOAT,
    DOUBLE,
    ITEMSTACK,
    LOCATION,
    MANY_ENTITIES,
    MANY_PLAYERS,
    ONE_ENTITY,
    ONE_PLAYER
  }

  public static final Argument HIDE_OUTPUT = of(ArgumentType.HIDE_OUTPUT);
  public static final Argument ONE_PLAYER = of(ArgumentType.ONE_PLAYER);
  public static final Argument MANY_PLAYERS = of(ArgumentType.MANY_PLAYERS);
  public static final Argument ONE_ENTITY = of(ArgumentType.ONE_ENTITY);
  public static final Argument MANY_ENTITIES = of(ArgumentType.MANY_ENTITIES);
  public static final Argument ITEMSTACK = of(ArgumentType.ITEMSTACK);
  public static final Argument LOCATION = of(ArgumentType.LOCATION);
}
