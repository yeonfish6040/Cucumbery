package com.jho5245.cucumbery.util.storage.component.util.sendercomponent;

import com.google.common.base.Predicates;
import com.jho5245.cucumbery.Cucumbery;
import com.jho5245.cucumbery.util.no_groups.MessageUtil;
import com.jho5245.cucumbery.util.no_groups.Method;
import com.jho5245.cucumbery.util.storage.component.util.ComponentUtil;
import com.jho5245.cucumbery.util.storage.component.util.ItemNameUtil;
import com.jho5245.cucumbery.util.storage.data.Constant;
import dev.jorel.commandapi.wrappers.NativeProxyCommandSender;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.block.Block;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.*;
import org.bukkit.entity.Rabbit.Type;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class SenderComponentUtil
{
  /**
   * 콘솔, 명령 블록, 개체, 플레이어, 접속 중이지 않은 플레이어의 정보를 표시할 컴포넌트를 반환합니다.
   *
   * @param object 정보를 가져올 오브젝트
   * @return 해당 오브젝트의 정보를 가진 컴포넌트
   */
  @NotNull
  public static Component senderComponent(@NotNull Object object)
  {
    return senderComponent(object, Constant.THE_COLOR);
  }

  /**
   * 호버링 텍스트에 들어갈 개체의 좌표와 유형을 나타냅니다. 만약 좌표를 가져올 수 없는 객체라면 그대로 반환합니다.
   *
   * @param object        좌표와 유형을 나타낼 개체
   * @param hoverTextMode false면 그대로 반환
   * @return 좌표와 유형이 나타난 개체 컴포넌트
   */
  @NotNull
  public static Component senderComponent(@NotNull Object object, @Nullable TextColor defaultColor, boolean hoverTextMode)
  {
    return senderComponent(object, defaultColor, hoverTextMode, null);
  }
  /**
   * 호버링 텍스트에 들어갈 개체의 좌표와 유형을 나타냅니다. 만약 좌표를 가져올 수 없는 객체라면 그대로 반환합니다.
   *
   * @param object        좌표와 유형을 나타낼 개체
   * @param hoverTextMode false면 그대로 반환
   * @param extraComponent 추가로 붙일 컴포넌트
   * @return 좌표와 유형이 나타난 개체 컴포넌트
   */
  @NotNull
  public static Component senderComponent(@NotNull Object object, @Nullable TextColor defaultColor, boolean hoverTextMode, @Nullable Component extraComponent)
  {
    Component component;
    if (object instanceof Entity entity)
    {
      Component nameComponent;
      if (entity instanceof Player player)
      {
        nameComponent = player.displayName().hoverEvent(null).clickEvent(null);
      }
      else
      {
        nameComponent = entity.customName();
        if (nameComponent != null)
        {
          nameComponent = nameComponent.hoverEvent(null).clickEvent(null);
        }
      }
      if (nameComponent == null)
      {
        String key = entity.getType().translationKey();
        if (entity instanceof Creeper creeper && creeper.isPowered())
        {
          key = "충전된 크리퍼";
        }
        if (entity instanceof Rabbit rabbit && rabbit.getRabbitType() == Type.THE_KILLER_BUNNY)
        {
          key = "entity.minecraft.killer_bunny";
        }
        if (entity instanceof FallingBlock fallingBlock)
        {
          nameComponent = ItemNameUtil.itemName(fallingBlock.getBlockData().getMaterial());
        }
        else
        {
          nameComponent = ComponentUtil.translate(key);
        }
        if (entity instanceof MushroomCow mushroomCow && mushroomCow.getVariant() == MushroomCow.Variant.BROWN)
        {
          nameComponent = ComponentUtil.translate("%s %s", ComponentUtil.translate("color.minecraft.brown"), nameComponent);
        }
        if (entity instanceof Villager villager)
        {
          nameComponent = ComponentUtil.translate(entity.getType().translationKey() + "." + villager.getProfession().toString().toLowerCase());
        }
        if (entity instanceof Ageable ageable && !ageable.isAdult())
        {
          nameComponent = ComponentUtil.translate("%s %s", "아기", nameComponent);
        }
      }
      if (defaultColor != null && nameComponent.color() == null)
      {
        nameComponent = nameComponent.color(defaultColor);
      }
      component = nameComponent;
    }
    else
    {
      component = senderComponent(object, defaultColor).hoverEvent(null).clickEvent(null);
    }
    if (!hoverTextMode)
    {
      return component;
    }
    Location location = null;
    Component typeComponent = null; // ComponentUtil.translate(entity.getType().translationKey());
    boolean customNameNull = false;
    if (object instanceof BlockCommandSender blockCommandSender)
    {
      Block block = blockCommandSender.getBlock();
      location = block.getLocation();
      typeComponent = ComponentUtil.translate(block.getType().translationKey());
    }
    else if (object instanceof Entity entity)
    {
      location = entity.getLocation();
      typeComponent = ComponentUtil.translate(entity.getType().translationKey());
      customNameNull = entity.customName() == null;
    }
    if (location != null)
    {
      if (object instanceof Player player && !player.getName().equals(MessageUtil.stripColor(ComponentUtil.serialize(player.displayName()))))
      {
        component = ComponentUtil.translate("&7%s@%s [%s]", component, location, player.getName());
      }
      else
      {
        component = ComponentUtil.translate(customNameNull ? "&7%s@%s" : "&7%s@%s [%s]", component, location, typeComponent);
      }
    }
    if (extraComponent != null)
    {
      component = component.append(extraComponent);
    }
    return component;
  }

  /**
   * 콘솔, 명령 블록, 개체, 플레이어, 접속 중이지 않은 플레이어의 정보를 표시할 컴포넌트를 반환합니다.
   *
   * @param object       정보를 가져올 오브젝트
   * @param defaultColor 해당 개체의 컴포넌트에 색상이 없을 때 기본 색상
   * @return 해당 오브젝트의 정보를 가진 컴포넌트
   */
  @NotNull
  public static Component senderComponent(@NotNull Object object, @Nullable TextColor defaultColor)
  {
    return senderComponent(null, object, defaultColor);
  }

  /**
   * 콘솔, 명령 블록, 개체, 플레이어, 접속 중이지 않은 플레이어의 정보를 표시할 컴포넌트를 반환합니다.
   *
   * @param player 이 컴포넌트를 보는 플레이어
   * @param object 정보를 가져올 오브젝트
   * @param defaultColor 해당 개체의 컴포넌트에 색상이 없을 때 기본 색상
   * @return 해당 오브젝트의 정보를 가진 컴포넌트
   */
  @NotNull
  public static Component senderComponent(@Nullable Player player, @NotNull Object object, @Nullable TextColor defaultColor)
  {
    if (Cucumbery.using_CommandAPI && object instanceof NativeProxyCommandSender sender)
    {
      CommandSender caller = sender.getCaller(), callee = sender.getCallee();
      if (caller.equals(callee))
      {
        return senderComponent(player, caller, defaultColor);
      }
      return ComponentUtil.translate("%s(%s 에 의한)", senderComponent(player, callee, defaultColor), senderComponent(player, caller, defaultColor));
    }
    if (object instanceof UUID uuid)
    {
      Entity entity = Bukkit.getEntity(uuid);
      if (entity != null)
      {
        return senderComponent(player, entity, defaultColor);
      }
    }
    if (object instanceof List<?> list && !list.isEmpty())
    {
      if (list.stream().allMatch(Predicates.instanceOf(UUID.class)::apply))
      {
        @SuppressWarnings("all")
        List<UUID> uuids = (List<UUID>) list;
        List<Entity> entities = new ArrayList<>();
        for (UUID uuid : uuids)
        {
          Entity entity = Bukkit.getEntity(uuid);
          if (entity != null)
          {
            entities.add(entity);
          }
        }
        if (!entities.isEmpty())
        {
          return senderComponent(player, entities, defaultColor);
        }
      }
      if (list.stream().allMatch(Predicates.instanceOf(Entity.class)::apply))
      {
        @SuppressWarnings("all")
        List<Entity> entities = (List<Entity>) list;
        if (entities.size() == 1)
        {
          return senderComponent(player, entities.get(0), defaultColor);
        }
        int displaySize = Math.max(1, Cucumbery.config.getInt("max-entity-display-size"));
        if (entities.size() <= displaySize)
        {
          StringBuilder key = new StringBuilder("&7");
          List<Component> args = new ArrayList<>();
          for (Entity entity : entities)
          {
            key.append("%s, ");
            args.add(senderComponent(player, entity, defaultColor));
          }
          key = new StringBuilder(key.substring(0, key.length() - 2));
          return ComponentUtil.translate(key.toString(), args);
        }
        boolean isPlayer = entities.stream().allMatch(Predicates.instanceOf(Player.class)::apply);
        Component component = ComponentUtil.translate(isPlayer ? "플레이어 %s명" : "개체 %s개", Component.text(list.size())).color(defaultColor);
        Component hover = ComponentUtil.translate(isPlayer ? "플레이어 %s명" : "개체 %s개", Component.text(list.size()));
        for (int i = 0; i < entities.size(); i++)
        {
          hover = hover.append(Component.text("\n"));
          Entity entity = entities.get(i);
          if (i == 30)
          {
            hover = hover.append(ComponentUtil.translate("&7&o" + (isPlayer ? "외 %s명 더..." : "외 %s개 더..."), Component.text(entities.size() - 30)));
            break;
          }
          hover = hover.append(senderComponent(entity, defaultColor, true));
        }
        component = component.hoverEvent(HoverEvent.showText(hover));
        return component;
      }
    }
    if (object instanceof ConsoleCommandSender commandSender)
    {
      Component component = ComponentUtil.translate("&d서버");
      Component name = commandSender.name();
      if (name.color() == null)
      {
        name = name.color(Constant.THE_COLOR);
      }
      Component hover = Component.empty().append(ComponentUtil.translate("&d서버"));
      hover = hover.append(Component.text("\n"));
      hover = hover.append(ComponentUtil.translate("유형 : %s", Constant.THE_COLOR_HEX + Bukkit.getName()));
      hover = hover.append(Component.text("\n"));
      hover = hover.append(ComponentUtil.translate("이름 : %s", name));
      hover = hover.append(Component.text("\n"));
      hover = hover.append(ComponentUtil.translate("버전 : %s", Constant.THE_COLOR_HEX + Bukkit.getVersion()));
      hover = hover.append(Component.text("\n"));
      hover = hover.append(ComponentUtil.translate("접속 인원 : %s명", Constant.THE_COLOR_HEX + Bukkit.getOnlinePlayers().size()));
      if (player == null || player.hasPermission("asdf"))
      {
        hover = hover.append(Component.text("\n"));
        hover = hover.append(ComponentUtil.translate("월드 수 : %s개", Constant.THE_COLOR_HEX + Bukkit.getWorlds().size()));
        hover = hover.append(Component.text("\n"));
        hover = hover.append(Component.text("\n"));
        hover = hover.append(ComponentUtil.translate("&7클릭하여 서버 버전 참조"));
        component = component.clickEvent(ClickEvent.runCommand("/version"));
      }
      component = component.hoverEvent(hover);
      return component;
    }
    else if (object instanceof BlockCommandSender blockCommandSender)
    {
      Block block = blockCommandSender.getBlock();
      Material type = block.getType();
      Location location = block.getLocation();
      Component name = blockCommandSender.name();
      boolean nameNull = name.equals(Component.text("@"));
      Component defaultName = ComponentUtil.translate(block.translationKey());
      defaultName = switch (type)
              {
                case COMMAND_BLOCK -> defaultName.color(TextColor.color(215, 180, 157));
                case REPEATING_COMMAND_BLOCK -> defaultName.color(TextColor.color(169, 153, 214));
                case CHAIN_COMMAND_BLOCK -> defaultName.color(TextColor.color(168, 209, 191));
                default -> defaultName.color(NamedTextColor.LIGHT_PURPLE);
              };
      Component component = nameNull ? defaultName : name;
      Component hover = nameNull ? defaultName.color(null) : Component.empty().append(name.hoverEvent(null).clickEvent(null));
      if (component.color() == null)
      {
        component = component.color(defaultName.color());
      }
      hover = hover.append(Component.text("\n"));
      hover = hover.append(ComponentUtil.translate("유형 : %s", defaultName));
      int x = location.getBlockX(), y = location.getBlockY(), z = location.getBlockZ();
      if (player == null || player.hasPermission("asdf"))
      {
        hover = hover.append(Component.text("\n"));
        hover = hover.append(ComponentUtil.translate("좌표 : %s", location));
        hover = hover.append(Component.text("\n"));
        hover = hover.append(Component.text("\n"));
        hover = hover.append(ComponentUtil.translate("&7클릭하여 해당 명령 블록 위치로 텔레포트"));
        component = component.clickEvent(ClickEvent.suggestCommand("/atp @s " + location.getWorld().getName() + " " + (x + 0.5) + " " + (y + 1) + " " + (z + 0.5) + " ~ 180"));
      }
      component = component.hoverEvent(hover);
      return component;
    }
    else if (object instanceof Entity entity)
    {
      return EntityComponentUtil.entityComponent(player, entity, defaultColor);
    }
    else if (object instanceof OfflinePlayer offlinePlayer)
    {
      UUID uuid = offlinePlayer.getUniqueId();
      String displayName = Method.getDisplayName(offlinePlayer);
      Component component = ComponentUtil.create(displayName);
      if (component.color() == null)
      {
        component = component.color(Constant.THE_COLOR);
      }
      Component hover = Component.empty().append(ComponentUtil.create(displayName));
      String name = offlinePlayer.getName();
      if (name != null)
      {
        hover = hover.append(Component.text("\n"));
        hover = hover.append(ComponentUtil.translate("ID : %s", Constant.THE_COLOR_HEX + name));
      }
      hover = hover.append(Component.text("\n"));
      hover = hover.append(ComponentUtil.translate("UUID : %s", Constant.THE_COLOR_HEX + uuid));
      hover = hover.append(Component.text("\n"));
      hover = hover.append(ComponentUtil.translate("chat.copy.click"));
      component = component.hoverEvent(HoverEvent.showText(hover));
      component = component.clickEvent(ClickEvent.copyToClipboard(uuid.toString()));
      return component;
    }
    return Component.empty();
  }
}
