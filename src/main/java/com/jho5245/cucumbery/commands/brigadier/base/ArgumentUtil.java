package com.jho5245.cucumbery.commands.brigadier.base;

import de.tr7zw.changeme.nbtapi.NBTContainer;
import dev.jorel.commandapi.arguments.*;
import dev.jorel.commandapi.wrappers.Rotation;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

@SuppressWarnings("unchecked")
public class ArgumentUtil
{
  @NotNull
  public static Argument<?> of(@NotNull ArgumentType type)
  {
    return of(type, new Object[]{});
  }

  @NotNull
  public static Argument<?> of(@NotNull ArgumentType type, @NotNull Object... params)
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
      case LITERAL -> new MultiLiteralArgument("args", List.of(Arrays.stream(params).map(Object::toString).toArray(String[]::new)));
      case INTEGER -> new IntegerArgument(key != null ? key : "값", (int) Math.max(Integer.MIN_VALUE, min), (int) Math.min(Integer.MAX_VALUE, max));
      case LONG -> new LongArgument(key != null ? key : "값", (long) Math.max(Long.MIN_VALUE, min), (long) Math.min(Long.MAX_VALUE, max));
      case FLOAT -> new FloatArgument(key != null ? key : "값", (float) Math.max(-Float.MAX_VALUE, min), (float) Math.min(Float.MAX_VALUE, max));
      case DOUBLE -> new DoubleArgument(key != null ? key : "값", min, max);
      case ONE_PLAYER -> new PlayerArgument(key != null ? key : "플레이어");
      case MANY_PLAYERS -> new EntitySelectorArgument.ManyPlayers(key != null ? key : "여러 플레이어");
      case ONE_ENTITY -> new EntitySelectorArgument.OneEntity(key != null ? key : "개체");
      case MANY_ENTITIES -> new EntitySelectorArgument.ManyEntities(key != null ? key : "여러 개체");
      case ITEMSTACK -> new ItemStackArgument(key != null ? key : "아이템");
      case LOCATION -> new LocationArgument(key != null ? key : "좌표");
      case LOCATION_BLOCK -> new LocationArgument(key != null ? key : "좌표", LocationType.BLOCK_POSITION);
      case LOCATION_2D -> new Location2DArgument(key != null ? key : "좌표");
      case ROTATION -> new RotationArgument(key != null ? key : "방향");
      case POTION_EFFECT_TYPE -> new PotionEffectArgument(key != null ? key : "효과 유형");
      case ENTITY_TYPE -> new EntityTypeArgument(key != null ? key : "개체 유형");
      case NBT_CONTAINER -> new NBTCompoundArgument<NBTContainer>(key != null ? key : "nbt");
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
    MANY_ENTITIES,
    MANY_PLAYERS,
    ONE_ENTITY,
    ONE_PLAYER,
    LOCATION,
    LOCATION_BLOCK,
    LOCATION_2D,
    ROTATION,
    POTION_EFFECT_TYPE,

    ENTITY_TYPE,

    NBT_CONTAINER,
  }

  public static final Argument<Boolean> HIDE_OUTPUT = (Argument<Boolean>) of(ArgumentType.HIDE_OUTPUT);
  public static final Argument<Player> ONE_PLAYER = (Argument<Player>) of(ArgumentType.ONE_PLAYER);
  public static final Argument<Collection<Player>> MANY_PLAYERS = (Argument<Collection<Player>>) of(ArgumentType.MANY_PLAYERS);
  public static final Argument<Entity> ONE_ENTITY = (Argument<Entity>) of(ArgumentType.ONE_ENTITY);
  public static final Argument<Collection<Entity>> MANY_ENTITIES = (Argument<Collection<Entity>>) of(ArgumentType.MANY_ENTITIES);
  public static final Argument<ItemStack> ITEMSTACK = (Argument<ItemStack>) of(ArgumentType.ITEMSTACK);
  public static final Argument<Location> LOCATION = (Argument<Location>) of(ArgumentType.LOCATION);
  public static final Argument<Location> LOCATION_BLOCK = (Argument<Location>) of(ArgumentType.LOCATION_BLOCK);
  public static final Argument<Rotation> ROTATION = (Argument<Rotation>) of(ArgumentType.ROTATION);
  public static final Argument<PotionEffectType> POTION_EFFECT_TYPE = (Argument<PotionEffectType>) of(ArgumentType.POTION_EFFECT_TYPE);

  public static final Argument<EntityType> ENTITY_TYPE = (Argument<EntityType>) of(ArgumentType.ENTITY_TYPE);

  public static final Argument<NBTContainer> NBT_CONTAINER = (Argument<NBTContainer>) of(ArgumentType.NBT_CONTAINER);
}













