package com.jho5245.cucumbery.customeffect.scheduler;

import com.jho5245.cucumbery.Cucumbery;
import com.jho5245.cucumbery.commands.sound.CommandSong;
import com.jho5245.cucumbery.customeffect.CustomEffect;
import com.jho5245.cucumbery.customeffect.CustomEffect.DisplayType;
import com.jho5245.cucumbery.customeffect.CustomEffectGUI;
import com.jho5245.cucumbery.customeffect.CustomEffectManager;
import com.jho5245.cucumbery.customeffect.CustomEffectType;
import com.jho5245.cucumbery.customeffect.children.group.PlayerCustomEffect;
import com.jho5245.cucumbery.customeffect.scheduler.metasasis.MetasasisScheduler;
import com.jho5245.cucumbery.events.entity.EntityCustomEffectPreRemoveEvent;
import com.jho5245.cucumbery.events.entity.EntityCustomEffectRemoveEvent;
import com.jho5245.cucumbery.util.CreateGUI;
import com.jho5245.cucumbery.util.MessageUtil;
import com.jho5245.cucumbery.util.Method;
import com.jho5245.cucumbery.util.storage.CustomConfig.UserData;
import com.jho5245.cucumbery.util.storage.component.util.ComponentUtil;
import com.jho5245.cucumbery.util.storage.data.Constant;
import com.jho5245.cucumbery.util.storage.data.Variable;
import com.xxmicloxx.NoteBlockAPI.model.Song;
import net.kyori.adventure.bossbar.BossBar;
import net.kyori.adventure.bossbar.BossBar.Color;
import net.kyori.adventure.bossbar.BossBar.Overlay;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.TranslatableComponent;
import org.bukkit.*;
import org.bukkit.World.Environment;
import org.bukkit.entity.*;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.InventoryView.Property;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class CustomEffectScheduler
{
  private static final Set<UUID> darknessTerrorTimer = new HashSet<>();
  private static final Set<UUID> darknessTerrorTimer2 = new HashSet<>();

  public static void schedule(@NotNull Cucumbery cucumbery)
  {
    Bukkit.getServer().getScheduler().runTaskTimer(cucumbery, () ->
    {
      tick();
      display();
      trueInvisibility();
      fancySpotlight();
      newbieShield();
      darknessTerror();
      serverRadio();
      axolotlsGrace();
      trollInventoryProperty();
      stop();
    }, 0L, 1L);
    MetasasisScheduler.schedule(cucumbery);
  }

  private static void tick()
  {
    for (World world : Bukkit.getWorlds())
    {
      for (Entity entity : world.getEntities())
      {
        UUID uuid = entity.getUniqueId();
        List<CustomEffect> customEffects = CustomEffectManager.getEffects(entity);
        if (customEffects.isEmpty())
        {
          continue;
        }
        List<CustomEffect> removed = new ArrayList<>();
        for (CustomEffect customEffect : customEffects)
        {
          customEffect.tick();
          if (customEffect instanceof PlayerCustomEffect playerCustomEffect)
          {
            Player player = playerCustomEffect.getPlayer();
            UUID uuid1 = player.getUniqueId();
            if (!player.isOnline() || !player.isValid())
            {
              Player newPlayer = Bukkit.getPlayer(uuid1);
              if (newPlayer != null)
              {
                playerCustomEffect.setPlayer(newPlayer);
              }
            }
          }
          if (customEffect.getDuration() == 0)
          {
            EntityCustomEffectPreRemoveEvent event = new EntityCustomEffectPreRemoveEvent(entity, customEffect);
            Cucumbery.getPlugin().getPluginManager().callEvent(event);
            removed.add(customEffect);
          }
        }
        customEffects.removeIf(effect -> effect.getDuration() == 0);
        CustomEffectManager.effectMap.put(uuid, customEffects);
        for (CustomEffect remove : removed)
        {
          EntityCustomEffectRemoveEvent event = new EntityCustomEffectRemoveEvent(entity, remove);
          Cucumbery.getPlugin().getPluginManager().callEvent(event);
        }
      }
    }
  }

  private static void display()
  {
    for (Player player : Bukkit.getOnlinePlayers())
    {
      UUID uuid = player.getUniqueId();
      List<CustomEffect> customEffects = CustomEffectManager.getEffects(player, DisplayType.ACTION_BAR);
      if (!customEffects.isEmpty())
      {
        customEffects.removeIf(CustomEffect::isHidden);
        @NotNull List<CustomEffect> finalCustomEffects = new ArrayList<>(customEffects);
        customEffects.removeIf(e ->
        {
          CustomEffectType customEffectType = e.getType();
          for (CustomEffect effect : finalCustomEffects)
          {
            if (effect.getType() == customEffectType && !customEffectType.isStackDisplayed() && effect.getAmplifier() > e.getAmplifier())
            {
              return true;
            }
          }
          return false;
        });
        MessageUtil.sendActionBar(player, CustomEffectManager.getDisplay(customEffects, customEffects.size() <= 10));
      }
      customEffects = CustomEffectManager.getEffects(player, DisplayType.BOSS_BAR);
      customEffects.removeIf(CustomEffect::isHidden);
      @NotNull List<CustomEffect> finalCustomEffects = new ArrayList<>(customEffects);
      customEffects.removeIf(e ->
      {
        CustomEffectType customEffectType = e.getType();
        for (CustomEffect effect : finalCustomEffects)
        {
          if (effect.getType() == customEffectType && !customEffectType.isStackDisplayed() && effect.getAmplifier() > e.getAmplifier())
          {
            return true;
          }
        }
        return false;
      });
      List<PotionEffect> potionEffects = new ArrayList<>(player.getActivePotionEffects());
      potionEffects.removeIf(potionEffect -> potionEffect.getDuration() <= 2 && !potionEffect.hasParticles() && !potionEffect.hasIcon());
      boolean showPotionEffect = Cucumbery.config.getBoolean("show-vanilla-potion-effects-on-bossbar") && !potionEffects.isEmpty();
      if (!showPotionEffect && customEffects.isEmpty())
      {
        if (Variable.customEffectBossBarMap.containsKey(uuid))
        {
          player.hideBossBar(Variable.customEffectBossBarMap.get(uuid));
          Variable.customEffectBossBarMap.remove(uuid);
        }
      }
      else
      {
        int size = customEffects.size() + potionEffects.size();
        if (!Variable.customEffectBossBarMap.containsKey(uuid))
        {
          BossBar bossBar = BossBar.bossBar(Component.empty(), 1f, BossBar.Color.GREEN, Overlay.PROGRESS);
          Variable.customEffectBossBarMap.put(uuid, bossBar);
        }
        BossBar bossBar = Variable.customEffectBossBarMap.get(uuid);
        Component display = Component.empty();
        if (!customEffects.isEmpty())
        {
          display = display.append(CustomEffectManager.getDisplay(customEffects, size <= 10));
        }
        if (showPotionEffect)
        {
          if (!customEffects.isEmpty())
          {
            display = display.append(Component.text(", "));
          }
          display = display.append(CustomEffectManager.getVanillaDisplay(potionEffects, size <= 10));
        }
        bossBar.name(ComponentUtil.stripEvent(display));
        float progress = 0f;
        boolean allIsPositive = true, allIsNegative = true;
        for (CustomEffect customEffect : customEffects)
        {
          int initDuration = customEffect.getInitDuration();
          progress += (initDuration == -1 || customEffect.isTimeHidden() ? 1f : 1f * customEffect.getDuration() / initDuration) / size;
        }
        for (CustomEffect customEffect : customEffects)
        {
          if (customEffect.getType().isNegative())
          {
            allIsPositive = false;
          }
          else
          {
            allIsNegative = false;
          }
        }
        for (PotionEffect potionEffect : potionEffects)
        {
          if (CustomEffectManager.isVanillaNegative(potionEffect.getType()))
          {
            allIsPositive = false;
          }
          else
          {
            allIsNegative = false;
          }
        }
        if (allIsNegative)
        {
          bossBar.color(Color.RED);
        }
        else if (allIsPositive)
        {
          bossBar.color(Color.GREEN);
        }
        else
        {
          if (size >= 10)
          {
            bossBar.color(Color.PURPLE);
          }
          else
          {
            bossBar.color(Color.YELLOW);
          }
        }
        progress += 1f * potionEffects.size() / size;
        progress = Math.min(Math.max(progress, 0f), 1f);
        player.showBossBar(bossBar.progress(progress));
      }
      customEffects = CustomEffectManager.getEffects(player, DisplayType.PLAYER_LIST);
      if (!customEffects.isEmpty())
      {
        Component component = Component.empty();
        component = component.append(Component.text("\n"));
        component = component.append(
                ComponentUtil.translate("&e적용 중인 효과 목록 : %s개", "&2" + customEffects.size())
        );
        component = component.append(Component.text("\n"));
        for (CustomEffect customEffect : customEffects)
        {
          int duration = customEffect.getDuration();
          int amplifier = customEffect.getAmplifier();
          component = component.append(Component.text("\n"));
          component = component.append(
                  ComponentUtil.translate(amplifier == 0 ? "%1$s%2$s" : "%1$s %3$s%2$s", customEffect,
                          (duration != -1 && duration != customEffect.getInitDuration() - 1) ?
                                  " (" + Method.timeFormatMilli(duration * 50L, duration < 200, 1, true) + ")" :
                                  ""
                          , amplifier + 1)
          );
        }
        player.sendPlayerListFooter(ComponentUtil.stripEvent(component));
      }

      InventoryView inventoryView = player.getOpenInventory();
      Component title = inventoryView.title();
      if (CreateGUI.isGUITitle(title) && title instanceof TranslatableComponent translatableComponent && translatableComponent.args().get(1) instanceof TextComponent textComponent && textComponent.content().equals(Constant.POTION_EFFECTS))
      {
        CustomEffectGUI.openGUI(player, false);
      }
    }
  }

  @SuppressWarnings("deprecation")
  private static void trueInvisibility()
  {
    for (World world : Bukkit.getWorlds())
    {
      for (Entity entity : world.getEntities())
      {
        Set<String> scoreboardTags = entity.getScoreboardTags();
        if (scoreboardTags.contains("true_invisibility") || CustomEffectManager.hasEffect(entity, CustomEffectType.TRUE_INVISIBILITY))
        {
          for (Player player : Bukkit.getOnlinePlayers())
          {
            if (entity != player && player.canSee(entity))
            {
              player.hideEntity(Cucumbery.getPlugin(), entity);
            }
          }
        }
        else
        {
          for (Player player : Bukkit.getOnlinePlayers())
          {
            if (entity != player && !player.canSee(entity))
            {
              player.showEntity(Cucumbery.getPlugin(), entity);
            }
          }
        }
      }
    }
  }

  private static void fancySpotlight()
  {
    for (Player player : Bukkit.getOnlinePlayers())
    {
      if (CustomEffectManager.hasEffect(player, CustomEffectType.FANCY_SPOTLIGHT))
      {
        Location location = player.getEyeLocation();
        byte blockLight = location.getBlock().getLightFromBlocks();
        if (blockLight > 10)
        {
          CustomEffectManager.addEffect(player, CustomEffectType.FANCY_SPOTLIGHT_ACTIVATED);
          player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 2,0, false, false, false));
          player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 2,0, false, false, false));
        }
      }
    }
  }

  private static void newbieShield()
  {
    for (Player player : Bukkit.getOnlinePlayers())
    {
      int playTime = player.getStatistic(Statistic.PLAY_ONE_MINUTE);
      if (playTime < 20 * 60 * 20)
      {
        CustomEffectManager.addEffect(player, new CustomEffect(CustomEffectType.NEWBIE_SHIELD, 2, 2));
      }
      else if (playTime < 20 * 60 * 40)
      {
        CustomEffectManager.addEffect(player, new CustomEffect(CustomEffectType.NEWBIE_SHIELD, 2, 1));
      }
      else if (playTime < 20 * 60 * 60)
      {
        CustomEffectManager.addEffect(player, new CustomEffect(CustomEffectType.NEWBIE_SHIELD));
      }
    }
  }

  private static void darknessTerror()
  {
    for (Player player : Bukkit.getOnlinePlayers())
    {
      if (CustomEffectManager.hasEffect(player, CustomEffectType.DARKNESS_TERROR_RESISTANCE))
      {
        CustomEffectManager.removeEffect(player, CustomEffectType.DARKNESS_TERROR_ACTIVATED);
        continue;
      }
      if (!darknessTerrorTimer.contains(player.getUniqueId()))
      {
        if (!darknessTerrorTimer2.contains(player.getUniqueId()))
        {
          Bukkit.getScheduler().runTaskLater(Cucumbery.getPlugin(), () -> darknessTerrorTimer.add(player.getUniqueId()), 40L);
        }
        darknessTerrorTimer2.add(player.getUniqueId());
        continue;
      }
      darknessTerrorTimer.remove(player.getUniqueId());
      darknessTerrorTimer2.remove(player.getUniqueId());
      Location location = player.getEyeLocation();
      Material mainHand = player.getInventory().getItemInMainHand().getType(), offHand = player.getInventory().getItemInOffHand().getType();
      if (CustomEffectManager.hasEffect(player, CustomEffectType.DARKNESS_TERROR) &&
              location.getWorld().getEnvironment() == Environment.NORMAL &&
              player.getGameMode() != GameMode.CREATIVE && player.getGameMode() != GameMode.SPECTATOR && !player.hasPotionEffect(PotionEffectType.NIGHT_VISION) &&
              !location.getBlock().isSolid() && location.getBlock().getLightLevel() == 0
              && !(Constant.OPTIFINE_DYNAMIC_LIGHT_ITEMS.contains(mainHand) || Constant.OPTIFINE_DYNAMIC_LIGHT_ITEMS.contains(offHand)))
      {
        CustomEffectManager.addEffect(player, new CustomEffect(CustomEffectType.DARKNESS_TERROR_ACTIVATED));
      }
      else
      {
        CustomEffectManager.removeEffect(player, CustomEffectType.DARKNESS_TERROR_ACTIVATED);
      }
    }
  }

  private static void serverRadio()
  {
    for (Player player : Bukkit.getOnlinePlayers())
    {
      if (!Cucumbery.using_NoteBlockAPI || CommandSong.radioSongPlayer == null || CommandSong.song == null || !UserData.LISTEN_GLOBAL.getBoolean(player))
      {
        CustomEffectManager.removeEffect(player, CustomEffectType.SERVER_RADIO_LISTENING);
      }
      else
      {
        Song song = CommandSong.song;
        float speed = song.getSpeed();
        int length = song.getLength();
        int amplifier = (int) Math.floor(length / 100d / speed);
        amplifier = Math.min(2, Math.max(0, amplifier));
        CustomEffectManager.addEffect(player, new CustomEffect(CustomEffectType.SERVER_RADIO_LISTENING, CustomEffectType.SERVER_RADIO_LISTENING.getDefaultDuration(), amplifier));
      }
    }
  }

  private static void axolotlsGrace()
  {
    for (World world : Bukkit.getWorlds())
    {
      for (Entity entity : world.getEntities())
      {
        if (!(entity instanceof AnimalTamer && entity instanceof Damageable damageable))
        {
          continue;
        }
        if (damageable.getHealth() > 5d)
        {
          continue;
        }
        List<Entity> entities = entity.getNearbyEntities(15, 15, 15);
        for (Entity loop : entities)
        {
          if (loop instanceof Parrot parrot && parrot.getOwner() == entity)
          {
            CustomEffectManager.addEffect(entity, new CustomEffect(CustomEffectType.PARROTS_CHEER));
            break;
          }
        }
      }
    }
  }

  private static void trollInventoryProperty()
  {
    for (Player player : Bukkit.getOnlinePlayers())
    {
      InventoryView inventoryView = player.getOpenInventory();
      if (inventoryView.getType() == InventoryType.CRAFTING)
      {
        continue;
      }
      if (CustomEffectManager.hasEffect(player, CustomEffectType.TROLL_INVENTORY_PROPERTY))
      {
        int min = CustomEffectManager.hasEffect(player, CustomEffectType.TROLL_INVENTORY_PROPERTY_MIN) ? CustomEffectManager.getEffect(player, CustomEffectType.TROLL_INVENTORY_PROPERTY_MIN).getAmplifier() : 0;
        CustomEffect customEffect = CustomEffectManager.getEffect(player, CustomEffectType.TROLL_INVENTORY_PROPERTY);
        for (Property property : Property.values())
        {
          player.setWindowProperty(property, Method.random(min, customEffect.getAmplifier()));
        }
      }
    }
  }

  private static void stop()
  {
    for (World world : Bukkit.getWorlds())
    {
      for (Entity entity : world.getEntities())
      {
        if (CustomEffectManager.hasEffect(entity, CustomEffectType.STOP))
        {
          if (!(entity instanceof LivingEntity))
          {
            entity.setVelocity(new Vector(0, 0.04, 0));
          }
        }
      }
    }
  }
}
