package com.jho5245.cucumbery.util.storage.component.util.sendercomponent;

import com.google.common.base.Predicates;
import com.jho5245.cucumbery.Cucumbery;
import com.jho5245.cucumbery.util.Method;
import com.jho5245.cucumbery.util.storage.component.LocationComponent;
import com.jho5245.cucumbery.util.storage.component.util.ComponentUtil;
import com.jho5245.cucumbery.util.storage.data.Constant;
import dev.jorel.commandapi.wrappers.NativeProxyCommandSender;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.block.Block;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
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
    Component component = senderComponent(object, defaultColor).hoverEvent(null).clickEvent(null);
    if (!hoverTextMode)
    {
      return component;
    }
    Location location = null;
    Component typeComponent = null; // Component.translatable(entity.getType().translationKey());
    if (object instanceof BlockCommandSender blockCommandSender)
    {
      Block block = blockCommandSender.getBlock();
      location = block.getLocation();
      typeComponent = Component.translatable(block.getType().translationKey());
    }
    else if (object instanceof Entity entity)
    {
      location = entity.getLocation();
      typeComponent = Component.translatable(entity.getType().translationKey());
    }
    if (location != null)
    {
      Component locationComponent = LocationComponent.locationComponent(location);
      if (object instanceof Player player && !player.getName().equals(ComponentUtil.serialize(player.displayName())))
      {
        component = ComponentUtil.createTranslate("&7%s@%s [%s/%s]", component, locationComponent, player.getName(), typeComponent);
      }
      else
      {
        component = ComponentUtil.createTranslate("&7%s@%s [%s]", component, locationComponent, typeComponent);
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
    if (Cucumbery.using_CommandAPI && object instanceof NativeProxyCommandSender sender)
    {
      CommandSender caller = sender.getCaller(), callee = sender.getCallee();
      if (caller.equals(callee))
      {
        return senderComponent(caller);
      }
      return senderComponent(callee);
//      return ComponentUtil.createTranslate("%s에 의한 %s", senderComponent(sender.getCaller(), defaultColor), senderComponent(sender.getCallee(), defaultColor));
    }
    if (object instanceof UUID uuid)
    {
      Entity entity = Bukkit.getEntity(uuid);
      if (entity != null)
      {
        return senderComponent(entity, defaultColor);
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
          return senderComponent(entities, defaultColor);
        }
      }
      if (list.stream().allMatch(Predicates.instanceOf(Entity.class)::apply))
      {
        @SuppressWarnings("all")
        List<Entity> entities = (List<Entity>) list;
        if (entities.size() == 1)
        {
          return senderComponent(entities.get(0), defaultColor);
        }
        boolean isPlayer = entities.stream().allMatch(Predicates.instanceOf(Player.class)::apply);
        Component component = ComponentUtil.createTranslate(isPlayer ? "플레이어 %s명" : "개체 %s개", Component.text(list.size())).color(defaultColor);
        Component hover = ComponentUtil.createTranslate(isPlayer ? "플레이어 %s명" : "개체 %s개", Component.text(list.size()));
        for (int i = 0; i < entities.size(); i++)
        {
          hover = hover.append(Component.text("\n"));
          Entity entity = entities.get(i);
          if (i == 30)
          {
            hover = hover.append(ComponentUtil.createTranslate("&7&o" + (isPlayer ? "외 %s명 더..." : "container.shulkerBox.more"), Component.text(entities.size() - 30)));
            break;
          }
          hover = hover.append(senderComponent(entity, defaultColor, true));
        }
        component = component.hoverEvent(HoverEvent.showText(hover));
        return component;
      }
    }
    if (object instanceof ConsoleCommandSender)
    {
      Component component = ComponentUtil.createTranslate("&d서버");
      Component hover = Component.empty().append(ComponentUtil.createTranslate("&d서버"));
      hover = hover.append(Component.text("\n"));
      hover = hover.append(ComponentUtil.createTranslate("유형 : %s", Constant.THE_COLOR_HEX + Bukkit.getServer().getName()));
      hover = hover.append(Component.text("\n"));
      hover = hover.append(ComponentUtil.createTranslate("버전 : %s", Constant.THE_COLOR_HEX + Bukkit.getServer().getVersion()));
      hover = hover.append(Component.text("\n"));
      hover = hover.append(ComponentUtil.createTranslate("접속 인원 : %s", Constant.THE_COLOR_HEX + Bukkit.getServer().getOnlinePlayers().size()));
      return component.hoverEvent(hover).clickEvent(ClickEvent.runCommand("/version"));
    }
    else if (object instanceof BlockCommandSender blockCommandSender)
    {
      Block block = blockCommandSender.getBlock();
      Location location = block.getLocation();
      String name = blockCommandSender.getName();
      Component component = ComponentUtil.create(name);
      if (component.color() == null)
      {
        component = component.color(NamedTextColor.LIGHT_PURPLE);
      }

      Component hover = ComponentUtil.create(name);
      hover = hover.append(Component.text("\n"));
      hover = hover.append(ComponentUtil.createTranslate("유형 : %s", block.getType()));
      hover = hover.append(Component.text("\n"));
      hover = hover.append(ComponentUtil.createTranslate("좌표 : %s", location));
      int x = location.getBlockX(), y = location.getBlockY(), z = location.getBlockZ();
      return component.hoverEvent(hover).clickEvent(ClickEvent.suggestCommand("/atp @s " + location.getWorld().getName() + " " + (x + 0.5) + " " + (y + 1) + " " + (z + 0.5) + " ~ 180"));
    }
    else if (object instanceof Entity entity)
    {
      return EntityComponentUtil.entityComponent(entity, defaultColor);
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
      Component hover = ComponentUtil.create(displayName);
      hover = hover.append(Component.text("\n"));
      hover = hover.append(ComponentUtil.createTranslate("UUID : %s", Constant.THE_COLOR_HEX + uuid));
      hover = hover.append(Component.text("\n"));
      hover = hover.append(Component.translatable("chat.copy.click"));
      component = component.hoverEvent(HoverEvent.showText(hover));
      component = component.clickEvent(ClickEvent.copyToClipboard(uuid.toString()));
      return component;
    }
    return Component.empty();
  }

}
