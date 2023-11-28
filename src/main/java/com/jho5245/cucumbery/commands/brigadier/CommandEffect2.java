package com.jho5245.cucumbery.commands.brigadier;

import com.google.common.base.Predicates;
import com.jho5245.cucumbery.commands.brigadier.base.CommandBase;
import com.jho5245.cucumbery.util.no_groups.MessageUtil;
import com.jho5245.cucumbery.util.no_groups.Method;
import com.jho5245.cucumbery.util.storage.component.util.ComponentUtil;
import com.jho5245.cucumbery.util.storage.component.util.sendercomponent.SenderComponentUtil;
import com.jho5245.cucumbery.util.storage.data.Constant;
import com.jho5245.cucumbery.util.storage.data.TranslatableKeyParser;
import dev.jorel.commandapi.CommandAPI;
import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.*;
import dev.jorel.commandapi.exceptions.WrapperCommandSyntaxException;
import dev.jorel.commandapi.wrappers.NativeProxyCommandSender;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.event.HoverEvent.Action;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static com.jho5245.cucumbery.commands.brigadier.base.ArgumentUtil.*;

@SuppressWarnings("unchecked")
public class CommandEffect2 extends CommandBase
{
  private final Argument<Boolean> OVERRIDE = new BooleanArgument("기존 효과 제거");

  private final Argument<Double> DURATION_SECOND = new DoubleArgument("지속 시간(초)", 0.05, Integer.MAX_VALUE / 20d);

  private final Argument<String> INFINITE_DURATION = new MultiLiteralArgument("infinite", List.of("infinite"));

  private final Argument<Integer> AMPLIFIER = new IntegerArgument("농도 레벨", 0, 255);

  private final Argument<String> PROPERTY = new MultiLiteralArgument("args", List.of("default", "hide-all", "hide-particle", "show-all"));

  private final Argument<Boolean> HIDE_PARTICLE = new BooleanArgument("입자 숨김");

  private final Argument<Boolean> HIDE_ICON = new BooleanArgument("우측 상단 아이콘 숨김");

  private final Argument<Boolean> HIDE_AMBIENT = new BooleanArgument("우측 상단 효과 숨김");

  private final List<Argument<?>> list1_1 = List.of(MANY_ENTITIES, POTION_EFFECT_TYPE);
  private final List<Argument<?>> list1_2 = List.of(MANY_ENTITIES, POTION_EFFECT_TYPE, OVERRIDE);
  private final List<Argument<?>> list1_3 = List.of(MANY_ENTITIES, POTION_EFFECT_TYPE, OVERRIDE, HIDE_OUTPUT);

  private final List<Argument<?>> list2_4 = List.of(MANY_ENTITIES, POTION_EFFECT_TYPE, DURATION_SECOND);
  private final List<Argument<?>> list2_5 = List.of(MANY_ENTITIES, POTION_EFFECT_TYPE, DURATION_SECOND, OVERRIDE);
  private final List<Argument<?>> list2_6 = List.of(MANY_ENTITIES, POTION_EFFECT_TYPE, DURATION_SECOND, OVERRIDE, HIDE_OUTPUT);
  private final List<Argument<?>> list2_7 = List.of(MANY_ENTITIES, POTION_EFFECT_TYPE, INFINITE_DURATION);
  private final List<Argument<?>> list2_8 = List.of(MANY_ENTITIES, POTION_EFFECT_TYPE, INFINITE_DURATION, OVERRIDE);
  private final List<Argument<?>> list2_9 = List.of(MANY_ENTITIES, POTION_EFFECT_TYPE, INFINITE_DURATION, OVERRIDE, HIDE_OUTPUT);

  private final List<Argument<?>> list3_4 = List.of(MANY_ENTITIES, POTION_EFFECT_TYPE, DURATION_SECOND, AMPLIFIER);
  private final List<Argument<?>> list3_5 = List.of(MANY_ENTITIES, POTION_EFFECT_TYPE, DURATION_SECOND, AMPLIFIER, OVERRIDE);
  private final List<Argument<?>> list3_6 = List.of(MANY_ENTITIES, POTION_EFFECT_TYPE, DURATION_SECOND, AMPLIFIER, OVERRIDE, HIDE_OUTPUT);
  private final List<Argument<?>> list3_7 = List.of(MANY_ENTITIES, POTION_EFFECT_TYPE, INFINITE_DURATION, AMPLIFIER);
  private final List<Argument<?>> list3_8 = List.of(MANY_ENTITIES, POTION_EFFECT_TYPE, INFINITE_DURATION, AMPLIFIER, OVERRIDE);
  private final List<Argument<?>> list3_9 = List.of(MANY_ENTITIES, POTION_EFFECT_TYPE, INFINITE_DURATION, AMPLIFIER, OVERRIDE, HIDE_OUTPUT);

  private final List<Argument<?>> list4_4 = List.of(MANY_ENTITIES, POTION_EFFECT_TYPE, DURATION_SECOND, AMPLIFIER, PROPERTY);
  private final List<Argument<?>> list4_5 = List.of(MANY_ENTITIES, POTION_EFFECT_TYPE, DURATION_SECOND, AMPLIFIER, PROPERTY, OVERRIDE);
  private final List<Argument<?>> list4_6 = List.of(MANY_ENTITIES, POTION_EFFECT_TYPE, DURATION_SECOND, AMPLIFIER, PROPERTY, OVERRIDE, HIDE_OUTPUT);
  private final List<Argument<?>> list4_7 = List.of(MANY_ENTITIES, POTION_EFFECT_TYPE, INFINITE_DURATION, AMPLIFIER, PROPERTY);
  private final List<Argument<?>> list4_8 = List.of(MANY_ENTITIES, POTION_EFFECT_TYPE, INFINITE_DURATION, AMPLIFIER, PROPERTY, OVERRIDE);
  private final List<Argument<?>> list4_9 = List.of(MANY_ENTITIES, POTION_EFFECT_TYPE, INFINITE_DURATION, AMPLIFIER, PROPERTY, OVERRIDE, HIDE_OUTPUT);

  private final List<Argument<?>> list5_4 = List.of(MANY_ENTITIES, POTION_EFFECT_TYPE, DURATION_SECOND, AMPLIFIER, HIDE_PARTICLE, HIDE_ICON, HIDE_AMBIENT);
  private final List<Argument<?>> list5_5 = List.of(MANY_ENTITIES, POTION_EFFECT_TYPE, DURATION_SECOND, AMPLIFIER, HIDE_PARTICLE, HIDE_ICON, HIDE_AMBIENT, OVERRIDE);
  private final List<Argument<?>> list5_6 = List.of(MANY_ENTITIES, POTION_EFFECT_TYPE, DURATION_SECOND, AMPLIFIER, HIDE_PARTICLE, HIDE_ICON, HIDE_AMBIENT, OVERRIDE, HIDE_OUTPUT);
  private final List<Argument<?>> list5_7 = List.of(MANY_ENTITIES, POTION_EFFECT_TYPE, INFINITE_DURATION, AMPLIFIER, HIDE_PARTICLE, HIDE_ICON, HIDE_AMBIENT);
  private final List<Argument<?>> list5_8 = List.of(MANY_ENTITIES, POTION_EFFECT_TYPE, INFINITE_DURATION, AMPLIFIER, HIDE_PARTICLE, HIDE_ICON, HIDE_AMBIENT, OVERRIDE);
  private final List<Argument<?>> list5_9 = List.of(MANY_ENTITIES, POTION_EFFECT_TYPE, INFINITE_DURATION, AMPLIFIER, HIDE_PARTICLE, HIDE_ICON, HIDE_AMBIENT, OVERRIDE, HIDE_OUTPUT);

  /**
   * gets booleans from property
   *
   * @param property what to get from
   * @return 3 length array of (hideParticle, hideIcon, hideAmbient)
   */
  public static boolean[] getBooleansFromProperty(@NotNull String property)
  {
    return switch (property)
            {
              case "show-all" -> new boolean[]{false, false, false};
              case "hide-all" -> new boolean[]{true, true, true};
              case "hide-particle" -> new boolean[]{true, false, true};
              default -> new boolean[]{false, false, true};
            };
  }

  private void giveEffect(@NotNull NativeProxyCommandSender sender, @NotNull Collection<Entity> entities, @NotNull PotionEffectType potionEffectType,
                          int duration, int amplifier, @NotNull String property, boolean override, boolean hideOutput) throws WrapperCommandSyntaxException
  {
    boolean[] booleans = getBooleansFromProperty(property);
    giveEffect(sender, entities, potionEffectType, duration, amplifier, booleans[0], booleans[1], booleans[2], override, hideOutput);
  }

  private void giveEffect(@NotNull NativeProxyCommandSender sender, @NotNull Collection<Entity> entities, @NotNull PotionEffectType potionEffectType,
                          int duration, int amplifier, boolean hideParticles, boolean hideIcon, boolean hideAmbient, boolean override, boolean hideOutput) throws WrapperCommandSyntaxException
  {
    if (duration == -1)
    {
      duration = Integer.MAX_VALUE;
    }
    PotionEffect potionEffect = new PotionEffect(potionEffectType, duration, amplifier, !hideAmbient, !hideParticles, !hideIcon);
    List<Entity> successEntities = new ArrayList<>();
    for (Entity entity : entities)
    {
      if (entity instanceof LivingEntity livingEntity)
      {
        if (override && livingEntity.hasPotionEffect(potionEffectType))
        {
          livingEntity.removePotionEffect(potionEffectType);
        }
        if (livingEntity.addPotionEffect(potionEffect))
        {
          PotionEffect after = livingEntity.getPotionEffect(potionEffectType);
          if (after != null && potionEffect.getDuration() == after.getDuration())
          {
            successEntities.add(livingEntity);
          }
        }
      }
    }
    if (!hideOutput)
    {
      List<Entity> failureEntities = new ArrayList<>(entities);
      failureEntities.removeAll(successEntities);
      boolean successEntitiesIsEmpty = successEntities.isEmpty();
      if (!failureEntities.isEmpty())
      {
        Component failureEntitiesComponent = SenderComponentUtil.senderComponent(failureEntities);
        HoverEvent<?> hoverEvent = failureEntitiesComponent.hoverEvent();
        Component hover = hoverEvent != null && hoverEvent.action() == Action.SHOW_TEXT ? (Component) hoverEvent.value() : Component.empty();
        if (failureEntities.size() > 1)
        {
          List<Entity> list = new ArrayList<>(failureEntities);
          hover = failureEntitiesComponent.hoverEvent(null).color(null);
          for (int i = 0; i < list.size(); i++)
          {
            Entity entity = list.get(i);
            hover = hover.append(Component.text("\n"));
            if (i == 30)
            {
              hover = hover.append(ComponentUtil.translate("&7&o" + (failureEntities.stream().allMatch(Predicates.instanceOf(Player.class)::apply)
                      ? "외 %s명 더..." : "container.shulkerBox.more"), Component.text(failureEntities.size() - 30)));
              break;
            }
            LivingEntity livingEntity = entity instanceof LivingEntity ? (LivingEntity) entity : null;
            PotionEffect applied = livingEntity != null ? livingEntity.getPotionEffect(potionEffectType) : null;
            String duraString = Method.timeFormatMilli(applied != null ? applied.getDuration() : 0);
            if (duraString.length() > 7)
            {
              duraString = duraString.substring(0, duraString.length() - 5) + "...";
            }
            Component extra = applied != null ?
                    ComponentUtil.translate("&7 - %s, %s단계", Constant.THE_COLOR_HEX + duraString, (applied.getAmplifier() + 1)) :
                    ComponentUtil.translate("rg255,204; - 효과 면역 개체");
            Component concat = SenderComponentUtil.senderComponent(entity, Constant.THE_COLOR, true, extra);
            hover = hover.append(concat);
          }
        }
        else
        {
          Entity entity = failureEntities.iterator().next();
          hover = hover.append(Component.text("\n"));
          hover = hover.append(Component.text("\n"));
          hover = hover.append(ComponentUtil.translate("&7" + TranslatableKeyParser.getKey(potionEffectType)));
          LivingEntity livingEntity = entity instanceof LivingEntity ? (LivingEntity) entity : null;
          PotionEffect applied = livingEntity != null ? livingEntity.getPotionEffect(potionEffectType) : null;
          hover = hover.append(Component.text("\n"));
          if (applied != null)
          {
            hover = hover.append(ComponentUtil.translate("&7지속 시간 : %s", Constant.THE_COLOR_HEX + Method.timeFormatMilli(applied.getDuration() * 50L)));
            hover = hover.append(Component.text("\n"));
            hover = hover.append(ComponentUtil.translate("&7기존 농도 레벨 : %s단계", (applied.getAmplifier() + 1)));
            hover = hover.append(ComponentUtil.translate("&7 > 부여될 농도 레벨 : %s단계", (amplifier + 1)));
          }
          else
          {
            hover = hover.append(ComponentUtil.translate("rg255,204;효과 면역 개체"));
          }
        }
        failureEntitiesComponent = failureEntitiesComponent.hoverEvent(hover);
        MessageUtil.sendWarnOrError(successEntitiesIsEmpty, sender,
                ComponentUtil.translate("%s에게 %s 효과를 적용할 수 없습니다 (대상이 효과에 면역이 있거나 더 강한 효과를 가지고 있습니다.)", failureEntitiesComponent, potionEffect));
      }
      if (!successEntitiesIsEmpty)
      {
        MessageUtil.info(sender, ComponentUtil.translate("%s에게 %s 효과를 적용했습니다", successEntities, potionEffect));
        MessageUtil.sendAdminMessage(sender, "%s에게 %s 효과를 적용했습니다", successEntities, potionEffect);
        List<Audience> infoTarget = new ArrayList<>(successEntities);
        infoTarget.remove(sender.getCallee());
        MessageUtil.info(infoTarget, ComponentUtil.translate("%1$s이(가) 당신에게 %2$s 효과를 적용했습니다", sender, potionEffect));
      }
    }
    else if (successEntities.isEmpty() && sender.getCallee() instanceof BlockCommandSender)
    {
      throw CommandAPI.failWithString("이 효과를 적용할 수 없습니다 (대상이 효과에 면역이 있거나 더 강한 효과를 가지고 있습니다.)");
    }
  }

  @Override
  @SuppressWarnings("unchecked")
  public void registerCommand(String command, String permission, String... aliases)
  {
    CommandAPICommand commandAPICommand = getCommandBase(command, permission, aliases);

    {
      commandAPICommand = commandAPICommand.withArguments(list1_1);
      commandAPICommand = commandAPICommand.executesNative((sender, args) ->
      {
        Collection<Entity> entities = (Collection<Entity>) args.get(0);
        PotionEffectType potionEffectType = (PotionEffectType) args.get(1);
        giveEffect(sender, entities, potionEffectType, 30 * 20, 0, "default", false, false);
      });
      commandAPICommand.register();

      commandAPICommand = getCommandBase(command, permission, aliases);
      commandAPICommand = commandAPICommand.withArguments(list1_2);
      commandAPICommand = commandAPICommand.executesNative((sender, args) ->
      {
        Collection<Entity> entities = (Collection<Entity>) args.get(0);
        PotionEffectType potionEffectType = (PotionEffectType) args.get(1);
        giveEffect(sender, entities, potionEffectType, 30 * 20, 0, "default", (Boolean) args.get(2), false);
      });
      commandAPICommand.register();

      commandAPICommand = getCommandBase(command, permission, aliases);
      commandAPICommand = commandAPICommand.withArguments(list1_3);
      commandAPICommand = commandAPICommand.executesNative((sender, args) ->
      {
        Collection<Entity> entities = (Collection<Entity>) args.get(0);
        PotionEffectType potionEffectType = (PotionEffectType) args.get(1);
        giveEffect(sender, entities, potionEffectType, 30 * 20, 0, "default", (Boolean) args.get(2), (Boolean) args.get(3));
      });
      commandAPICommand.register();
    }

    {
      commandAPICommand = getCommandBase(command, permission, aliases);
      commandAPICommand = commandAPICommand.withArguments(list2_4);
      commandAPICommand = commandAPICommand.executesNative((sender, args) ->
      {
        Collection<Entity> entities = (Collection<Entity>) args.get(0);
        PotionEffectType potionEffectType = (PotionEffectType) args.get(1);
        giveEffect(sender, entities, potionEffectType, (int) ((Double) args.get(2) * 20), 0, "default", false, false);
      });
      commandAPICommand.register();

      commandAPICommand = getCommandBase(command, permission, aliases);
      commandAPICommand = commandAPICommand.withArguments(list2_5);
      commandAPICommand = commandAPICommand.executesNative((sender, args) ->
      {
        Collection<Entity> entities = (Collection<Entity>) args.get(0);
        PotionEffectType potionEffectType = (PotionEffectType) args.get(1);
        giveEffect(sender, entities, potionEffectType, (int) ((Double) args.get(2) * 20), 0, "default", (Boolean) args.get(3), false);
      });
      commandAPICommand.register();

      commandAPICommand = getCommandBase(command, permission, aliases);
      commandAPICommand = commandAPICommand.withArguments(list2_6);
      commandAPICommand = commandAPICommand.executesNative((sender, args) ->
      {
        Collection<Entity> entities = (Collection<Entity>) args.get(0);
        PotionEffectType potionEffectType = (PotionEffectType) args.get(1);
        giveEffect(sender, entities, potionEffectType, (int) ((Double) args.get(2) * 20), 0, "default", (Boolean) args.get(3), (Boolean) args.get(4));
      });
      commandAPICommand.register();

      commandAPICommand = getCommandBase(command, permission, aliases);
      commandAPICommand = commandAPICommand.withArguments(list2_7);
      commandAPICommand = commandAPICommand.executesNative((sender, args) ->
      {
        Collection<Entity> entities = (Collection<Entity>) args.get(0);
        PotionEffectType potionEffectType = (PotionEffectType) args.get(1);
        giveEffect(sender, entities, potionEffectType, -1, 0, "default", false, false);
      });
      commandAPICommand.register();

      commandAPICommand = getCommandBase(command, permission, aliases);
      commandAPICommand = commandAPICommand.withArguments(list2_8);
      commandAPICommand = commandAPICommand.executesNative((sender, args) ->
      {
        Collection<Entity> entities = (Collection<Entity>) args.get(0);
        PotionEffectType potionEffectType = (PotionEffectType) args.get(1);
        giveEffect(sender, entities, potionEffectType, -1, 0, "default", (Boolean) args.get(3), false);
      });
      commandAPICommand.register();

      commandAPICommand = getCommandBase(command, permission, aliases);
      commandAPICommand = commandAPICommand.withArguments(list2_9);
      commandAPICommand = commandAPICommand.executesNative((sender, args) ->
      {
        Collection<Entity> entities = (Collection<Entity>) args.get(0);
        PotionEffectType potionEffectType = (PotionEffectType) args.get(1);
        giveEffect(sender, entities, potionEffectType, -1, 0, "default", (Boolean) args.get(3), (Boolean) args.get(4));
      });
      commandAPICommand.register();
    }

    {
      commandAPICommand = getCommandBase(command, permission, aliases);
      commandAPICommand = commandAPICommand.withArguments(list3_4);
      commandAPICommand = commandAPICommand.executesNative((sender, args) ->
      {
        Collection<Entity> entities = (Collection<Entity>) args.get(0);
        PotionEffectType potionEffectType = (PotionEffectType) args.get(1);
        giveEffect(sender, entities, potionEffectType, (int) ((Double) args.get(2) * 20), (Integer) args.get(3), "default", false, false);
      });
      commandAPICommand.register();

      commandAPICommand = getCommandBase(command, permission, aliases);
      commandAPICommand = commandAPICommand.withArguments(list3_5);
      commandAPICommand = commandAPICommand.executesNative((sender, args) ->
      {
        Collection<Entity> entities = (Collection<Entity>) args.get(0);
        PotionEffectType potionEffectType = (PotionEffectType) args.get(1);
        giveEffect(sender, entities, potionEffectType, (int) ((Double) args.get(2) * 20), (Integer) args.get(3), "default", (Boolean) args.get(4), false);
      });
      commandAPICommand.register();

      commandAPICommand = getCommandBase(command, permission, aliases);
      commandAPICommand = commandAPICommand.withArguments(list3_6);
      commandAPICommand = commandAPICommand.executesNative((sender, args) ->
      {
        Collection<Entity> entities = (Collection<Entity>) args.get(0);
        PotionEffectType potionEffectType = (PotionEffectType) args.get(1);
        giveEffect(sender, entities, potionEffectType, (int) ((Double) args.get(2) * 20), (Integer) args.get(3), "default", (Boolean) args.get(4), (Boolean) args.get(5));
      });
      commandAPICommand.register();

      commandAPICommand = getCommandBase(command, permission, aliases);
      commandAPICommand = commandAPICommand.withArguments(list3_7);
      commandAPICommand = commandAPICommand.executesNative((sender, args) ->
      {
        Collection<Entity> entities = (Collection<Entity>) args.get(0);
        PotionEffectType potionEffectType = (PotionEffectType) args.get(1);
        giveEffect(sender, entities, potionEffectType, -1, (Integer) args.get(3), "default", false, false);
      });
      commandAPICommand.register();

      commandAPICommand = getCommandBase(command, permission, aliases);
      commandAPICommand = commandAPICommand.withArguments(list3_8);
      commandAPICommand = commandAPICommand.executesNative((sender, args) ->
      {
        Collection<Entity> entities = (Collection<Entity>) args.get(0);
        PotionEffectType potionEffectType = (PotionEffectType) args.get(1);
        giveEffect(sender, entities, potionEffectType, -1, (Integer) args.get(3), "default", (Boolean) args.get(4), false);
      });
      commandAPICommand.register();

      commandAPICommand = getCommandBase(command, permission, aliases);
      commandAPICommand = commandAPICommand.withArguments(list3_9);
      commandAPICommand = commandAPICommand.executesNative((sender, args) ->
      {
        Collection<Entity> entities = (Collection<Entity>) args.get(0);
        PotionEffectType potionEffectType = (PotionEffectType) args.get(1);
        giveEffect(sender, entities, potionEffectType, -1, (Integer) args.get(3), "default", (Boolean) args.get(4), (Boolean) args.get(5));
      });
      commandAPICommand.register();
    }

    {
      commandAPICommand = getCommandBase(command, permission, aliases);
      commandAPICommand = commandAPICommand.withArguments(list4_4);
      commandAPICommand = commandAPICommand.executesNative((sender, args) ->
      {
        Collection<Entity> entities = (Collection<Entity>) args.get(0);
        PotionEffectType potionEffectType = (PotionEffectType) args.get(1);
        giveEffect(sender, entities, potionEffectType, (int) ((Double) args.get(2) * 20), (Integer) args.get(3), (String) args.get(4), false, false);
      });
      commandAPICommand.register();

      commandAPICommand = getCommandBase(command, permission, aliases);
      commandAPICommand = commandAPICommand.withArguments(list4_5);
      commandAPICommand = commandAPICommand.executesNative((sender, args) ->
      {
        Collection<Entity> entities = (Collection<Entity>) args.get(0);
        PotionEffectType potionEffectType = (PotionEffectType) args.get(1);
        giveEffect(sender, entities, potionEffectType, (int) ((Double) args.get(2) * 20), (Integer) args.get(3), (String) args.get(4), (Boolean) args.get(5), false);
      });
      commandAPICommand.register();

      commandAPICommand = getCommandBase(command, permission, aliases);
      commandAPICommand = commandAPICommand.withArguments(list4_6);
      commandAPICommand = commandAPICommand.executesNative((sender, args) ->
      {
        Collection<Entity> entities = (Collection<Entity>) args.get(0);
        PotionEffectType potionEffectType = (PotionEffectType) args.get(1);
        giveEffect(sender, entities, potionEffectType, (int) ((Double) args.get(2) * 20), (Integer) args.get(3), (String) args.get(4), (Boolean) args.get(5), (Boolean) args.get(6));
      });
      commandAPICommand.register();

      commandAPICommand = getCommandBase(command, permission, aliases);
      commandAPICommand = commandAPICommand.withArguments(list4_7);
      commandAPICommand = commandAPICommand.executesNative((sender, args) ->
      {
        Collection<Entity> entities = (Collection<Entity>) args.get(0);
        PotionEffectType potionEffectType = (PotionEffectType) args.get(1);
        giveEffect(sender, entities, potionEffectType, -1, (Integer) args.get(3), (String) args.get(4), false, false);
      });
      commandAPICommand.register();

      commandAPICommand = getCommandBase(command, permission, aliases);
      commandAPICommand = commandAPICommand.withArguments(list4_8);
      commandAPICommand = commandAPICommand.executesNative((sender, args) ->
      {
        Collection<Entity> entities = (Collection<Entity>) args.get(0);
        PotionEffectType potionEffectType = (PotionEffectType) args.get(1);
        giveEffect(sender, entities, potionEffectType, -1, (Integer) args.get(3), (String) args.get(4), (Boolean) args.get(5), false);
      });
      commandAPICommand.register();

      commandAPICommand = getCommandBase(command, permission, aliases);
      commandAPICommand = commandAPICommand.withArguments(list4_9);
      commandAPICommand = commandAPICommand.executesNative((sender, args) ->
      {
        Collection<Entity> entities = (Collection<Entity>) args.get(0);
        PotionEffectType potionEffectType = (PotionEffectType) args.get(1);
        giveEffect(sender, entities, potionEffectType, -1, (Integer) args.get(3), (String) args.get(4), (Boolean) args.get(5), (Boolean) args.get(6));
      });
      commandAPICommand.register();
    }

    {
      commandAPICommand = getCommandBase(command, permission, aliases);
      commandAPICommand = commandAPICommand.withArguments(list5_4);
      commandAPICommand = commandAPICommand.executesNative((sender, args) ->
      {
        Collection<Entity> entities = (Collection<Entity>) args.get(0);
        PotionEffectType potionEffectType = (PotionEffectType) args.get(1);
        giveEffect(sender, entities, potionEffectType, (int) ((Double) args.get(2) * 20), (Integer) args.get(3), (Boolean) args.get(4), (Boolean) args.get(5), (Boolean) args.get(6), false, false);
      });
      commandAPICommand.register();

      commandAPICommand = getCommandBase(command, permission, aliases);
      commandAPICommand = commandAPICommand.withArguments(list5_5);
      commandAPICommand = commandAPICommand.executesNative((sender, args) ->
      {
        Collection<Entity> entities = (Collection<Entity>) args.get(0);
        PotionEffectType potionEffectType = (PotionEffectType) args.get(1);
        giveEffect(sender, entities, potionEffectType, (int) ((Double) args.get(2) * 20), (Integer) args.get(3), (Boolean) args.get(4), (Boolean) args.get(5), (Boolean) args.get(6), (Boolean) args.get(7), false);
      });
      commandAPICommand.register();

      commandAPICommand = getCommandBase(command, permission, aliases);
      commandAPICommand = commandAPICommand.withArguments(list5_6);
      commandAPICommand = commandAPICommand.executesNative((sender, args) ->
      {
        Collection<Entity> entities = (Collection<Entity>) args.get(0);
        PotionEffectType potionEffectType = (PotionEffectType) args.get(1);
        giveEffect(sender, entities, potionEffectType, (int) ((Double) args.get(2) * 20), (Integer) args.get(3), (Boolean) args.get(4), (Boolean) args.get(5), (Boolean) args.get(6), (Boolean) args.get(7), (Boolean) args.get(8));
      });
      commandAPICommand.register();

      commandAPICommand = getCommandBase(command, permission, aliases);
      commandAPICommand = commandAPICommand.withArguments(list5_7);
      commandAPICommand = commandAPICommand.executesNative((sender, args) ->
      {
        Collection<Entity> entities = (Collection<Entity>) args.get(0);
        PotionEffectType potionEffectType = (PotionEffectType) args.get(1);
        giveEffect(sender, entities, potionEffectType, -1, (Integer) args.get(3), (Boolean) args.get(4), (Boolean) args.get(5), (Boolean) args.get(6), false, false);
      });
      commandAPICommand.register();

      commandAPICommand = getCommandBase(command, permission, aliases);
      commandAPICommand = commandAPICommand.withArguments(list5_8);
      commandAPICommand = commandAPICommand.executesNative((sender, args) ->
      {
        Collection<Entity> entities = (Collection<Entity>) args.get(0);
        PotionEffectType potionEffectType = (PotionEffectType) args.get(1);
        giveEffect(sender, entities, potionEffectType, -1, (Integer) args.get(3), (Boolean) args.get(4), (Boolean) args.get(5), (Boolean) args.get(6), (Boolean) args.get(7), false);
      });
      commandAPICommand.register();

      commandAPICommand = getCommandBase(command, permission, aliases);
      commandAPICommand = commandAPICommand.withArguments(list5_9);
      commandAPICommand = commandAPICommand.executesNative((sender, args) ->
      {
        Collection<Entity> entities = (Collection<Entity>) args.get(0);
        PotionEffectType potionEffectType = (PotionEffectType) args.get(1);
        giveEffect(sender, entities, potionEffectType, -1, (Integer) args.get(3), (Boolean) args.get(4), (Boolean) args.get(5), (Boolean) args.get(6), (Boolean) args.get(7), (Boolean) args.get(8));
      });
      commandAPICommand.register();
    }
  }
}























