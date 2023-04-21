package com.jho5245.cucumbery.util.no_groups;

import com.jho5245.cucumbery.util.storage.component.util.ComponentUtil;
import com.jho5245.cucumbery.util.storage.data.Variable;
import net.kyori.adventure.bossbar.BossBar;
import net.kyori.adventure.bossbar.BossBar.Color;
import net.kyori.adventure.bossbar.BossBar.Flag;
import net.kyori.adventure.bossbar.BossBar.Overlay;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class BossBarMessage
{
  private final long sentTimeMillis;
  private final long expireTimeMillis;
  private final BossBar bossBar;
  private Component message;
  private int displayTimeTicks;
  private float progress;
  private Color color;
  private Overlay overlay;
  private Flag flag;

  public BossBarMessage(@NotNull Component message, int displayTimeTicks, @NotNull Color color, @NotNull Overlay overlay, @Nullable Flag flag)
  {
    this.message = message;
    this.displayTimeTicks = displayTimeTicks;
    this.sentTimeMillis = System.currentTimeMillis();
    this.expireTimeMillis = System.currentTimeMillis() + displayTimeTicks * 50L;
    this.color = color;
    this.overlay = overlay;
    this.flag = flag;
    this.progress = 1f;
    if (flag == null)
    {
      this.bossBar = BossBar.bossBar(message, 1f, color, overlay);
    }
    else
    {
      this.bossBar = BossBar.bossBar(message, 1f, color, overlay, new HashSet<>(Collections.singletonList(flag)));
    }
  }

  public static void tickAsync()
  {
    Variable.sendBossBarMap.keySet().removeIf(uuid -> Variable.sendBossBarMap.get(uuid).isEmpty());
    for (UUID uuid : Variable.sendBossBarMap.keySet())
    {
      List<BossBarMessage> bossBarMessages = Variable.sendBossBarMap.get(uuid);
      for (BossBarMessage bossBarMessage : bossBarMessages)
      {
        long sent = bossBarMessage.getSentTimeMillis(), expire = bossBarMessage.getExpireTimeMillis(), current = System.currentTimeMillis();
        long diff = expire - sent, remain = expire - current;
        float progress = 1f * remain / diff;
        BossBar bossBar = bossBarMessage.getBossBar();
        bossBarMessage.progress = progress;
        Player player = Bukkit.getPlayer(uuid);
        if (player != null)
        {
          if (progress > 0f)
          {
            player.showBossBar(bossBar.name(ComponentUtil.stripEvent(bossBar.name())).progress(progress));
          }
          else
          {
            player.hideBossBar(bossBar);
          }
        }
      }
      bossBarMessages.removeIf(bossBarMessage -> bossBarMessage.getProgress() <= 0f);
      Variable.sendBossBarMap.put(uuid, bossBarMessages);
    }
  }

  @NotNull
  public Component getMessage()
  {
    return message;
  }

  public void setMessage(@NotNull Component message)
  {
    this.message = message;
  }

  public BossBar getBossBar()
  {
    return bossBar;
  }

  public float getProgress()
  {
    return progress;
  }

  public long getSentTimeMillis()
  {
    return sentTimeMillis;
  }

  public long getExpireTimeMillis()
  {
    return expireTimeMillis;
  }

  public void show(@NotNull Collection<Player> players)
  {
    for (Player player : players)
    {
      UUID uuid = player.getUniqueId();
      List<BossBarMessage> bossBarMessages;
      if (Variable.sendBossBarMap.containsKey(uuid))
      {
        bossBarMessages = Variable.sendBossBarMap.get(uuid);
      }
      else
      {
        bossBarMessages = new ArrayList<>();
      }
      bossBarMessages.add(this);
      Variable.sendBossBarMap.put(uuid, bossBarMessages);
    }
  }

  public int getDisplayTimeTicks()
  {
    return displayTimeTicks;
  }

  public void setDisplayTimeTicks(int displayTimeTicks)
  {
    this.displayTimeTicks = displayTimeTicks;
  }

  public Color getColor()
  {
    return color;
  }

  public void setColor(Color color)
  {
    this.color = color;
  }

  public Overlay getOverlay()
  {
    return overlay;
  }

  public void setOverlay(Overlay overlay)
  {
    this.overlay = overlay;
  }

  public Flag getFlag()
  {
    return flag;
  }

  public void setFlag(Flag flag)
  {
    this.flag = flag;
  }
}
