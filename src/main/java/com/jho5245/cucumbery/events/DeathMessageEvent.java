package com.jho5245.cucumbery.events;

import com.jho5245.cucumbery.custom.customeffect.CustomEffectManager;
import com.jho5245.cucumbery.custom.customeffect.type.CustomEffectType;
import com.jho5245.cucumbery.util.no_groups.MessageUtil;
import com.jho5245.cucumbery.util.storage.component.util.ComponentUtil;
import com.jho5245.cucumbery.util.storage.data.Variable;
import com.jho5245.cucumbery.util.storage.no_groups.CustomConfig.UserData;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.EntityEvent;
import org.jetbrains.annotations.NotNull;

import java.util.function.Predicate;

import static com.jho5245.cucumbery.deathmessages.DeathManager.DEATH_PREFIX;
import static com.jho5245.cucumbery.deathmessages.DeathManager.DEATH_PREFIX_PVP;

/**
 * Called when after {@link org.bukkit.event.entity.PlayerDeathEvent} or {@link org.bukkit.event.entity.EntityDeathEvent} on certain conditions, <p>sending custom death message with {@link Component}
 */
public class DeathMessageEvent extends EntityEvent
{
  private static final HandlerList handlers = new HandlerList();
  private Component deathMessage;
  private final boolean isPvP;

  private final Object damager;

  public DeathMessageEvent(@NotNull Entity what, @NotNull Component deathMessage, boolean isPvP, @NotNull Object damager)
  {
    super(what);
    this.deathMessage = deathMessage;
    this.isPvP = isPvP;
    this.damager = damager;
  }

  /**
   * Sets the death message
   * @param deathMessage to set
   */
  public void setDeathMessage(@NotNull Component deathMessage)
  {
    this.deathMessage = deathMessage;
  }

  /**
   * Gets the death message
   * @return the death message component
   */
  @NotNull
  public Component getDeathMessage()
  {
    return deathMessage;
  }

  /**
   * Sends the death message to players as if the {@link DeathMessageEvent#getEntity()} is died.
   */
  public void showMessage()
  {
    Predicate<Player> playerPredicate = player -> !(
            ((player == entity || player == damager) && UserData.SHOW_DEATH_SELF_MESSAGE.getBoolean(player)) ||
                    (!(player == entity || player == damager) && UserData.SHOW_DEATH_MESSAGE.getBoolean(player) && (!isPvP || UserData.SHOW_DEATH_PVP_MESSAGE.getBoolean(player)))
    );
    MessageUtil.broadcastPlayer(playerPredicate, deathMessage);
    if (CustomEffectManager.hasEffect(entity, CustomEffectType.CURSE_OF_BEANS))
    {
      for (Player online : Bukkit.getOnlinePlayers())
      {
        if (!playerPredicate.test(online))
        {
          if (!CustomEffectManager.hasEffect(online, CustomEffectType.CURSE_OF_BEANS))
          {
            MessageUtil.sendMessage(online, deathMessage);
          }
        }
      }
    }
    boolean usePrefix = Variable.deathMessages.getBoolean("death-messages.prefix.enable");
    Component insiderPrefix = Component.empty();
    if (usePrefix)
    {
      insiderPrefix = isPvP ? DEATH_PREFIX_PVP : DEATH_PREFIX;
    }
    if (CustomEffectManager.hasEffect(entity, CustomEffectType.INSIDER))
    {
      for (Player online : Bukkit.getOnlinePlayers())
      {
        if (entity != online)
        {
          MessageUtil.sendTitle(online, insiderPrefix, deathMessage, 5, 100, 15);
        }
      }
    }
  }

  /**
   * Sends the death message and location to console.
   */
  public void showConsoleMessage()
  {
    Location location = entity.getLocation();
    int x = location.getBlockX();
    int y = location.getBlockY();
    int z = location.getBlockZ();
    // 콘솔에 디버그를 보내기 위함
    MessageUtil.consoleSendMessage(deathMessage.append(ComponentUtil.create("&7 - " + location.getWorld().getName() + ", " + x + ", " + y + ", " + z)));
  }

  @Override
  public @NotNull HandlerList getHandlers()
  {
    return handlers;
  }

  @NotNull
  public static HandlerList getHandlerList()
  {
    return handlers;
  }
}
