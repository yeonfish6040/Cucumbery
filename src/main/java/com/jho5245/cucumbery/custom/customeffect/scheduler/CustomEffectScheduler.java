package com.jho5245.cucumbery.custom.customeffect.scheduler;

import com.jho5245.cucumbery.Cucumbery;
import com.jho5245.cucumbery.commands.sound.CommandSong;
import com.jho5245.cucumbery.custom.customeffect.CustomEffect;
import com.jho5245.cucumbery.custom.customeffect.CustomEffect.DisplayType;
import com.jho5245.cucumbery.custom.customeffect.CustomEffectGUI;
import com.jho5245.cucumbery.custom.customeffect.CustomEffectManager;
import com.jho5245.cucumbery.custom.customeffect.CustomEffectType;
import com.jho5245.cucumbery.custom.customeffect.children.group.PlayerCustomEffect;
import com.jho5245.cucumbery.custom.customeffect.children.group.RealDurationCustomEffect;
import com.jho5245.cucumbery.events.entity.EntityCustomEffectRemoveEvent.Reason;
import com.jho5245.cucumbery.util.no_groups.CreateGUI;
import com.jho5245.cucumbery.util.no_groups.MessageUtil;
import com.jho5245.cucumbery.util.no_groups.Method;
import com.jho5245.cucumbery.util.no_groups.Scheduler;
import com.jho5245.cucumbery.util.storage.component.util.ComponentUtil;
import com.jho5245.cucumbery.util.storage.data.Constant;
import com.jho5245.cucumbery.util.storage.data.Variable;
import com.jho5245.cucumbery.util.storage.no_groups.CustomConfig.UserData;
import com.xxmicloxx.NoteBlockAPI.model.Song;
import net.kyori.adventure.bossbar.BossBar;
import net.kyori.adventure.bossbar.BossBar.Color;
import net.kyori.adventure.bossbar.BossBar.Overlay;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.TranslatableComponent;
import org.bukkit.*;
import org.bukkit.World.Environment;
import org.bukkit.block.Block;
import org.bukkit.block.data.Waterlogged;
import org.bukkit.entity.*;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.InventoryView.Property;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

import java.util.*;

/**
 * see {@link Scheduler#tickSchedules()}
 */
public class CustomEffectScheduler
{
  public static final HashMap<UUID, BukkitTask> spreadTimerTask = new HashMap<>();
  public static final Set<UUID> spreadTimerSet = new HashSet<>();
  private static final Set<UUID> darknessTerrorTimer = new HashSet<>(), darknessTerrorTimer2 = new HashSet<>();
  private static final Set<UUID> podagraTimer = new HashSet<>();

  public static void tick(@NotNull Entity entity)
  {
    List<CustomEffect> customEffects = CustomEffectManager.getEffects(entity);
    if (customEffects.isEmpty())
    {
      return;
    }
    for (CustomEffect customEffect : customEffects)
    {
      if (customEffect instanceof RealDurationCustomEffect realDurationCustomEffect)
      {
        long current = System.currentTimeMillis(), endTime = realDurationCustomEffect.getEndTimeInMillis();
        int remain = (int) ((endTime - current) / 50L);
        // 시간이 다 되면 바로 없애는게 아니라 0.05초 지연을 줌 (실제 개체가 효과를 가지고 있는 판정은 생기지 않음)
        customEffect.setDuration(Math.max(1, remain));
      }
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
      customEffect.tick();
      if (customEffect.getDuration() == 0)
      {
        CustomEffectManager.removeEffect(entity, customEffect.getType(), customEffect.getAmplifier(), Reason.TIME_OUT);
      }
    }
  }

  public static void display(@NotNull Player player)
  {
    UUID uuid = player.getUniqueId();
    List<CustomEffect> customEffects = new ArrayList<>(CustomEffectManager.getEffects(player, DisplayType.ACTION_BAR));
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
    customEffects = new ArrayList<>(CustomEffectManager.getEffects(player, DisplayType.BOSS_BAR));
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
    potionEffects.removeIf(potionEffect ->
            (CustomEffectManager.hasEffect(player, CustomEffectType.FANCY_SPOTLIGHT) || CustomEffectManager.hasEffect(player, CustomEffectType.FANCY_SPOTLIGHT_ACTIVATED)) &&
                    potionEffect.getType().equals(PotionEffectType.REGENERATION) && potionEffect.getDuration() < 100 && potionEffect.getAmplifier() == 0 && !potionEffect.hasParticles() && !potionEffect.hasIcon()
    );
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
      boolean showDuration = size <= 10;
      if (!Variable.customEffectBossBarMap.containsKey(uuid))
      {
        BossBar bossBar = BossBar.bossBar(Component.empty(), 1f, BossBar.Color.GREEN, Overlay.PROGRESS);
        Variable.customEffectBossBarMap.put(uuid, bossBar);
      }
      BossBar bossBar = Variable.customEffectBossBarMap.get(uuid);
      Component display = Component.empty();
      if (!customEffects.isEmpty())
      {
        display = display.append(CustomEffectManager.getDisplay(customEffects, showDuration));
      }
      if (showPotionEffect)
      {
        if (!customEffects.isEmpty())
        {
          display = display.append(Component.text(showDuration ? ", " : ","));
        }
        display = display.append(CustomEffectManager.getVanillaDisplay(potionEffects, showDuration));
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

  public static void damageIndicator(@NotNull Entity entity)
  {
    if (!(entity instanceof ArmorStand))
    {
      return;
    }
    if (CustomEffectManager.hasEffect(entity, CustomEffectType.DAMAGE_INDICATOR))
    {
      Location location = entity.getLocation().add(0, 0.02, 0);
      entity.teleport(location);
    }
  }

  public static void townShield(@NotNull Player player)
  {
    if (CustomEffectManager.hasEffect(player, CustomEffectType.TOWN_SHIELD))
    {
      player.addPotionEffect(new PotionEffect(PotionEffectType.SATURATION, 2, 0, true, false, false));
    }
  }

  public static void spreadAndVariation(@NotNull Player player)
  {
    if (player.getGameMode() == GameMode.SPECTATOR)
    {
      return;
    }
    if (CustomEffectManager.hasEffect(player, CustomEffectType.SPREAD) && player.getGameMode() != GameMode.SPECTATOR)
    {
      CustomEffect customEffectSpread = CustomEffectManager.getEffect(player, CustomEffectType.SPREAD);
      int spreadDuration = customEffectSpread.getDuration(), spreadAmplifier = customEffectSpread.getAmplifier();
      int effectSize = 0;
      boolean detoxicate = CustomEffectManager.hasEffect(player, CustomEffectType.VAR_DETOXICATE);
      boolean pneumonia = CustomEffectManager.hasEffect(player, CustomEffectType.VAR_PNEUMONIA);
      boolean podagra = CustomEffectManager.hasEffect(player, CustomEffectType.VAR_PODAGRA);
      boolean stomachache = CustomEffectManager.hasEffect(player, CustomEffectType.VAR_STOMACHACHE);
      Collection<Player> nearbyPlayers = player.getWorld().getNearbyPlayers(player.getLocation(), spreadAmplifier + 1);
      for (Player nearbyPlayer : nearbyPlayers)
      {
        if (nearbyPlayer.getGameMode() == GameMode.SPECTATOR)
        {
          continue;
        }
        CustomEffectManager.addEffect(nearbyPlayer, customEffectSpread);
        if (detoxicate)
        {
          CustomEffectManager.addEffect(nearbyPlayer, CustomEffectManager.getEffect(player, CustomEffectType.VAR_DETOXICATE));
        }
        if (pneumonia)
        {
          CustomEffectManager.addEffect(nearbyPlayer, CustomEffectManager.getEffect(player, CustomEffectType.VAR_PNEUMONIA));
        }
        if (podagra)
        {
          CustomEffectManager.addEffect(nearbyPlayer, CustomEffectManager.getEffect(player, CustomEffectType.VAR_PODAGRA));
        }
        if (stomachache)
        {
          CustomEffectManager.addEffect(nearbyPlayer, CustomEffectManager.getEffect(player, CustomEffectType.VAR_STOMACHACHE));
        }
      }
      if (detoxicate)
      {
        effectSize++;
      }
      if (pneumonia)
      {
        effectSize++;
      }
      if (podagra)
      {
        effectSize++;
      }
      if (stomachache)
      {
        effectSize++;
      }

      UUID uuid = player.getUniqueId();
      if (!spreadTimerSet.contains(uuid))
      {
        spreadTimerSet.add(uuid);
        BukkitTask bukkitTask = Bukkit.getScheduler().runTaskLater(Cucumbery.getPlugin(), () -> spreadTimerSet.remove(uuid), 20 * 60L);
        spreadTimerTask.put(uuid, bukkitTask);
        if (Math.random() < 0.0008)
        {
          if (effectSize == 4)
          {
            switch ((int) (Math.random() * 4))
            {
              case 0 -> CustomEffectManager.removeEffect(player, CustomEffectType.VAR_PODAGRA);
              case 1 -> CustomEffectManager.removeEffect(player, CustomEffectType.VAR_STOMACHACHE);
              case 2 -> CustomEffectManager.removeEffect(player, CustomEffectType.VAR_PNEUMONIA);
              default -> CustomEffectManager.removeEffect(player, CustomEffectType.VAR_DETOXICATE);
            }
          }
          else if (effectSize == 1)
          {
            List<CustomEffectType> effectTypes = new ArrayList<>(Arrays.asList(CustomEffectType.VAR_PNEUMONIA, CustomEffectType.VAR_PODAGRA, CustomEffectType.VAR_DETOXICATE, CustomEffectType.VAR_STOMACHACHE));
            if (detoxicate)
            {
              effectTypes.remove(CustomEffectType.VAR_DETOXICATE);
            }
            if (pneumonia)
            {
              effectTypes.remove(CustomEffectType.VAR_PNEUMONIA);
            }
            if (podagra)
            {
              effectTypes.remove(CustomEffectType.VAR_PODAGRA);
            }
            if (stomachache)
            {
              effectTypes.remove(CustomEffectType.VAR_STOMACHACHE);
            }
            CustomEffectManager.addEffect(player, new CustomEffect(effectTypes.get((int) (Math.random() * effectTypes.size())), spreadDuration, spreadAmplifier));
          }
          else if (effectSize > 0)
          {
            boolean add = Math.random() < 0.5;
            List<CustomEffectType> effectTypes = new ArrayList<>(Arrays.asList(CustomEffectType.VAR_PNEUMONIA, CustomEffectType.VAR_PODAGRA, CustomEffectType.VAR_DETOXICATE, CustomEffectType.VAR_STOMACHACHE));
            if ((add && detoxicate) || (!add && !detoxicate))
            {
              effectTypes.remove(CustomEffectType.VAR_DETOXICATE);
            }
            if ((add && pneumonia) || (!add && !pneumonia))
            {
              effectTypes.remove(CustomEffectType.VAR_PNEUMONIA);
            }
            if ((add && podagra) || (!add && !podagra))
            {
              effectTypes.remove(CustomEffectType.VAR_PODAGRA);
            }
            if ((add && stomachache) || (!add && !stomachache))
            {
              effectTypes.remove(CustomEffectType.VAR_STOMACHACHE);
            }
            if (add)
            {
              CustomEffectManager.addEffect(player, new CustomEffect(effectTypes.get((int) (Math.random() * effectTypes.size())), spreadDuration, spreadAmplifier));
            }
            else
            {
              CustomEffectManager.removeEffect(player, effectTypes.get((int) (Math.random() * effectTypes.size())));
            }
          }
        }
      }

      if (CustomEffectManager.hasEffect(player, CustomEffectType.VAR_PODAGRA_ACTIVATED) && !player.isSneaking())
      {
        if (podagraTimer.contains(uuid))
        {
          return;
        }
        podagraTimer.add(uuid);
        Bukkit.getScheduler().runTaskLater(Cucumbery.getPlugin(), () -> podagraTimer.remove(uuid), 60);
        player.damage(0.2 + CustomEffectManager.getEffect(player, CustomEffectType.VAR_PODAGRA_ACTIVATED).getAmplifier() * 0.1);
        player.setNoDamageTicks(0);
      }
      if (pneumonia)
      {
        boolean skip = player.getGameMode() == GameMode.CREATIVE || player.getGameMode() == GameMode.SPECTATOR || player.hasPotionEffect(PotionEffectType.WATER_BREATHING);
        Block eyeBlock = player.getEyeLocation().getBlock();
        boolean isInWater = eyeBlock.getType() == Material.WATER || (eyeBlock.getBlockData() instanceof Waterlogged waterlogged && waterlogged.isWaterlogged());
        if (isInWater && !skip && player.getRemainingAir() > 0)
        {
          int amplifier = CustomEffectManager.getEffect(player, CustomEffectType.VAR_PNEUMONIA).getAmplifier();
          double multiplier = (amplifier + 1) * 0.1;
          double decimalNumer = multiplier - (int) multiplier;
          if (Math.random() < decimalNumer)
          {
            player.setRemainingAir((int) Math.floor(player.getRemainingAir() - multiplier));
          }
          else
          {
            player.setRemainingAir((int) Math.ceil(player.getRemainingAir() - multiplier));
          }
        }
        else if (player.getRemainingAir() > -16 && player.getRemainingAir() < player.getMaximumAir())
        {
          player.setRemainingAir(player.getRemainingAir() - 4);
        }
      }
    }
  }

  @SuppressWarnings("deprecation")
  public static void trueInvisibility(@NotNull Entity entity)
  {
    Set<String> scoreboardTags = entity.getScoreboardTags();
    if (scoreboardTags.contains("no_cucumbery_true_invisibility"))
    {
      return;
    }
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

  public static void fancySpotlight(@NotNull Player player)
  {
    if (CustomEffectManager.hasEffect(player, CustomEffectType.FANCY_SPOTLIGHT))
    {
      Location location = player.getEyeLocation();
      byte blockLight = location.getBlock().getLightFromBlocks();
      if (blockLight > 10)
      {
        CustomEffectManager.addEffect(player, CustomEffectType.FANCY_SPOTLIGHT_ACTIVATED);
      }
    }
    if (CustomEffectManager.hasEffect(player, CustomEffectType.FANCY_SPOTLIGHT_ACTIVATED))
    {
      player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 2, 0, false, false, false));
      PotionEffect potionEffect = player.getPotionEffect(PotionEffectType.REGENERATION);
      if (potionEffect == null || (potionEffect.getDuration() <= 48 && potionEffect.getAmplifier() == 0))
      {
        player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 98, 0, false, false, false));
      }
    }
  }

  public static void newbieShield(@NotNull Player player)
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

  public static void darknessTerror(@NotNull Player player)
  {
    if (CustomEffectManager.hasEffect(player, CustomEffectType.DARKNESS_TERROR_RESISTANCE))
    {
      CustomEffectManager.removeEffect(player, CustomEffectType.DARKNESS_TERROR_ACTIVATED);
      return;
    }
    if (!darknessTerrorTimer.contains(player.getUniqueId()))
    {
      if (!darknessTerrorTimer2.contains(player.getUniqueId()))
      {
        Bukkit.getScheduler().runTaskLater(Cucumbery.getPlugin(), () -> darknessTerrorTimer.add(player.getUniqueId()), 40L);
      }
      darknessTerrorTimer2.add(player.getUniqueId());
      return;
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

  public static void serverRadio(@NotNull Player player)
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

  public static void axolotlsGrace(@NotNull Entity entity)
  {

    if (!(entity instanceof AnimalTamer && entity instanceof Damageable damageable))
    {
      return;
    }
    if (damageable.getHealth() > 5d)
    {
      return;
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

  public static void trollInventoryProperty(@NotNull Player player)
  {
    InventoryView inventoryView = player.getOpenInventory();
    if (inventoryView.getType() == InventoryType.CRAFTING)
    {
      return;
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

  public static void stop(@NotNull Entity entity)
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
