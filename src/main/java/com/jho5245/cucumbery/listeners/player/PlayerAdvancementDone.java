package com.jho5245.cucumbery.listeners.player;

import com.jho5245.cucumbery.util.storage.ComponentUtil;
import com.jho5245.cucumbery.util.storage.data.Prefix;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.TranslatableComponent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerAdvancementDoneEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class PlayerAdvancementDone implements Listener
{
  @EventHandler
  public void onPlayerAdvancementDone(PlayerAdvancementDoneEvent event)
  {
    Component message = event.message();
    if (message instanceof TranslatableComponent translatableComponent)
    {
      Component prefix = Prefix.INFO_ADVANCEMENT.get();
      List<Component> args = translatableComponent.args();
      List<Component> newArgs = new ArrayList<>();
      for (Component arg : args)
      {
        if (arg instanceof TranslatableComponent translatable)
        {
          TextColor color = translatable.color();
          if (color != null)
          {
            if (color.equals(NamedTextColor.DARK_PURPLE))
            {
              prefix = Prefix.INFO_ADVANCEMENT_CHALLENGE.get();
            }
          }
        }
        HoverEvent<?> hoverEvent = arg.hoverEvent();
        if (hoverEvent != null)
        {
          if (hoverEvent.action() == HoverEvent.Action.SHOW_ENTITY)
          {
            // 플레이어의 이름일 경우, 플레이어의 닉네임을 집어넣음
            if (arg instanceof TextComponent textArg)
            {
              String content = textArg.content(); // 텍스트 컴포넌트의 값 (개체의 ID) <- 플레이어일 수도 있고 아닐 수도 있음
              HoverEvent.ShowEntity showEntity = (HoverEvent.ShowEntity) hoverEvent.value();
              if (showEntity.type().value().equals("player")) // 플레이어라는 거다
              {
                try
                {
                  if (content.equals(""))
                  {
                    content = ((TextComponent) arg.children().get(0)).content();
                  }
                  Player p = Bukkit.getServer().getPlayerExact(content);
                  Component displayName = ComponentUtil.senderComponent(Objects.requireNonNull(p), TextColor.color(255, 204, 0));
                  newArgs.add(displayName);
                  continue;
                }
                catch (Exception ignored)
                {

                }
              }
            }
          }
        }
        newArgs.add(arg);
      }

      event.message(Component.empty().append(prefix).append(ComponentUtil.createTranslate(translatableComponent.key(), newArgs)));
    }
  }
}
