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
import com.jho5245.cucumbery.events.entity.EntityCustomEffectRemoveEvent.RemoveReason;
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
        if (customEffect.getDuration() != -1)
        {
          customEffect.setDuration(Math.max(1, remain));
        }
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
        CustomEffectManager.removeEffect(entity, customEffect.getType(), customEffect.getAmplifier(), RemoveReason.TIME_OUT);
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
      MessageUtil.sendActionBar(player, CustomEffectManager.getDisplay(player, customEffects, customEffects.size() <= 10));
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
    List<PotionEffect> potionEffects = CustomEffectManager.removeDisplay(player, player.getActivePotionEffects());
    boolean showPotionEffect = Cucumbery.config.getBoolean("show-vanilla-potion-effects-on-bossbar") && !potionEffects.isEmpty();
    if (!showPotionEffect && customEffects.isEmpty())
    {
      if (Cucumbery.config.getBoolean("show-boss-bar-when-no-effects"))
      {
        if (!Variable.customEffectBossBarMap.containsKey(uuid))
        {
          BossBar bossBar = BossBar.bossBar(Component.empty(), 0f, Color.WHITE, Overlay.PROGRESS);
          Variable.customEffectBossBarMap.put(uuid, bossBar);
        }
        BossBar bossBar = Variable.customEffectBossBarMap.get(uuid);
        player.showBossBar(bossBar.color(Color.WHITE).progress(0f)
                .name(
                        ComponentUtil.translate("&7적용 중인 효과 없음")
                ));
      }
      else
      {
        if (Variable.customEffectBossBarMap.containsKey(uuid))
        {
          player.hideBossBar(Variable.customEffectBossBarMap.get(uuid));
          Variable.customEffectBossBarMap.remove(uuid);
        }
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
        display = display.append(CustomEffectManager.getDisplay(player, customEffects, showDuration));
      }
      if (showPotionEffect)
      {
        if (!customEffects.isEmpty())
        {
          display = display.append(Component.text(showDuration ? ", " : ","));
        }
        display = display.append(CustomEffectManager.getVanillaDisplay(player, potionEffects, showDuration));
      }
      bossBar.name(ComponentUtil.stripEvent(display));
      boolean allIsPositive = true, allIsNegative = true;
      long totalTick = 0, currentTick = 0;
      for (CustomEffect customEffect : customEffects)
      {
        int initDuration = customEffect.getInitDuration();
        int duration = customEffect.getDuration();
        totalTick += (initDuration == -1 || customEffect.isTimeHidden() ? 0 : initDuration);
        currentTick += (duration == -1 || customEffect.isTimeHidden() ? 0 : duration);
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
        int initDuration = -1;
        int duration = potionEffect.getDuration();
        if (Variable.potionEffectApplyMap.containsKey(uuid))
        {
          HashMap<String, Integer> hashMap = Variable.potionEffectApplyMap.get(uuid);
          if (hashMap.containsKey(potionEffect.getType().translationKey()))
          {
            initDuration = hashMap.get(potionEffect.getType().translationKey());
            if (duration > initDuration)
            {
              hashMap.put(potionEffect.getType().translationKey(), duration);
              initDuration = duration;
            }
          }
        }
        totalTick += (initDuration == -1 || initDuration > 20 * 60 * 60 * 24 * 365 ? 0 : initDuration);
        currentTick += (duration == -1 || duration > 20 * 60 * 60 * 24 * 365 ? 0 : duration);
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
      float progress = 1f;
      if (totalTick > 0)
      {
        progress = 1f * currentTick / totalTick;
      }
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

  public static void vanillaEffect(@NotNull Entity entity)
  {
    if (!(entity instanceof LivingEntity livingEntity))
    {
      return;
    }
    if (CustomEffectManager.hasEffect(livingEntity, CustomEffectType.MINECRAFT_SPEED))
    {
      CustomEffect customEffect = CustomEffectManager.getEffect(livingEntity, CustomEffectType.MINECRAFT_SPEED);
      livingEntity.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 2, customEffect.getAmplifier(), false, false, false));
    }
    if (CustomEffectManager.hasEffect(livingEntity, CustomEffectType.MINECRAFT_SLOWNESS))
    {
      CustomEffect customEffect = CustomEffectManager.getEffect(livingEntity, CustomEffectType.MINECRAFT_SLOWNESS);
      livingEntity.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 2, customEffect.getAmplifier(), false, false, false));
    }
    if (CustomEffectManager.hasEffect(livingEntity, CustomEffectType.MINECRAFT_HASTE))
    {
      CustomEffect customEffect = CustomEffectManager.getEffect(livingEntity, CustomEffectType.MINECRAFT_HASTE);
      livingEntity.addPotionEffect(new PotionEffect(PotionEffectType.FAST_DIGGING, 2, customEffect.getAmplifier(), false, false, false));
    }
    if (CustomEffectManager.hasEffect(livingEntity, CustomEffectType.MINECRAFT_MINING_FATIGUE))
    {
      CustomEffect customEffect = CustomEffectManager.getEffect(livingEntity, CustomEffectType.MINECRAFT_MINING_FATIGUE);
      livingEntity.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_DIGGING, 2, customEffect.getAmplifier(), false, false, false));
    }
    if (CustomEffectManager.hasEffect(livingEntity, CustomEffectType.MINECRAFT_INSTANT_HEALTH))
    {
      CustomEffect customEffect = CustomEffectManager.getEffect(livingEntity, CustomEffectType.MINECRAFT_INSTANT_HEALTH);
      livingEntity.addPotionEffect(new PotionEffect(PotionEffectType.HEAL, 2, customEffect.getAmplifier(), false, false, false));
    }
    if (CustomEffectManager.hasEffect(livingEntity, CustomEffectType.MINECRAFT_INSTANT_DAMAGE))
    {
      CustomEffect customEffect = CustomEffectManager.getEffect(livingEntity, CustomEffectType.MINECRAFT_INSTANT_DAMAGE);
      livingEntity.addPotionEffect(new PotionEffect(PotionEffectType.HARM, 2, customEffect.getAmplifier(), false, false, false));
    }
    if (CustomEffectManager.hasEffect(livingEntity, CustomEffectType.MINECRAFT_JUMP_BOOST))
    {
      CustomEffect customEffect = CustomEffectManager.getEffect(livingEntity, CustomEffectType.MINECRAFT_JUMP_BOOST);
      livingEntity.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, 2, customEffect.getAmplifier(), false, false, false));
    }
    if (CustomEffectManager.hasEffect(livingEntity, CustomEffectType.MINECRAFT_NAUSEA))
    {
      CustomEffect customEffect = CustomEffectManager.getEffect(livingEntity, CustomEffectType.MINECRAFT_NAUSEA);
      livingEntity.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 62, customEffect.getAmplifier(), false, false, false));
    }
    if (CustomEffectManager.hasEffect(livingEntity, CustomEffectType.MINECRAFT_REGENERATION))
    {
      CustomEffect customEffect = CustomEffectManager.getEffect(livingEntity, CustomEffectType.MINECRAFT_REGENERATION);
      PotionEffect potionEffect = livingEntity.getPotionEffect(PotionEffectType.REGENERATION);
      if (potionEffect == null || (potionEffect.getDuration() <= 48 && potionEffect.getAmplifier() == 0))
      {
        livingEntity.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 98, customEffect.getAmplifier(), false, false, false));
      }
    }
    if (CustomEffectManager.hasEffect(livingEntity, CustomEffectType.MINECRAFT_RESISTANCE))
    {
      CustomEffect customEffect = CustomEffectManager.getEffect(livingEntity, CustomEffectType.MINECRAFT_RESISTANCE);
      livingEntity.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 2, customEffect.getAmplifier(), false, false, false));
    }
    if (CustomEffectManager.hasEffect(livingEntity, CustomEffectType.MINECRAFT_RESISTANCE))
    {
      CustomEffect customEffect = CustomEffectManager.getEffect(livingEntity, CustomEffectType.MINECRAFT_RESISTANCE);
      livingEntity.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 2, customEffect.getAmplifier(), false, false, false));
    }
    if (CustomEffectManager.hasEffect(livingEntity, CustomEffectType.MINECRAFT_FIRE_RESISTANCE))
    {
      CustomEffect customEffect = CustomEffectManager.getEffect(livingEntity, CustomEffectType.MINECRAFT_FIRE_RESISTANCE);
      livingEntity.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, 2, customEffect.getAmplifier(), false, false, false));
    }
    if (CustomEffectManager.hasEffect(livingEntity, CustomEffectType.MINECRAFT_WATER_BREATHING))
    {
      CustomEffect customEffect = CustomEffectManager.getEffect(livingEntity, CustomEffectType.MINECRAFT_WATER_BREATHING);
      livingEntity.addPotionEffect(new PotionEffect(PotionEffectType.WATER_BREATHING, 2, customEffect.getAmplifier(), false, false, false));
    }
    if (CustomEffectManager.hasEffect(livingEntity, CustomEffectType.MINECRAFT_INVISIBILITY))
    {
      CustomEffect customEffect = CustomEffectManager.getEffect(livingEntity, CustomEffectType.MINECRAFT_INVISIBILITY);
      livingEntity.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 2, customEffect.getAmplifier(), false, false, false));
    }
    if (CustomEffectManager.hasEffect(livingEntity, CustomEffectType.MINECRAFT_BLINDNESS))
    {
      CustomEffect customEffect = CustomEffectManager.getEffect(livingEntity, CustomEffectType.MINECRAFT_BLINDNESS);
      livingEntity.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 21, customEffect.getAmplifier(), false, false, false));
    }
    if (CustomEffectManager.hasEffect(livingEntity, CustomEffectType.MINECRAFT_NIGHT_VISION))
    {
      CustomEffect customEffect = CustomEffectManager.getEffect(livingEntity, CustomEffectType.MINECRAFT_NIGHT_VISION);
      livingEntity.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, 2, customEffect.getAmplifier(), false, false, false));
    }
    if (CustomEffectManager.hasEffect(livingEntity, CustomEffectType.MINECRAFT_HUNGER))
    {
      CustomEffect customEffect = CustomEffectManager.getEffect(livingEntity, CustomEffectType.MINECRAFT_HUNGER);
      livingEntity.addPotionEffect(new PotionEffect(PotionEffectType.HUNGER, 2, customEffect.getAmplifier(), false, false, false));
    }
    if (CustomEffectManager.hasEffect(livingEntity, CustomEffectType.MINECRAFT_WEAKNESS))
    {
      CustomEffect customEffect = CustomEffectManager.getEffect(livingEntity, CustomEffectType.MINECRAFT_WEAKNESS);
      livingEntity.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, 2, customEffect.getAmplifier(), false, false, false));
    }
    if (CustomEffectManager.hasEffect(livingEntity, CustomEffectType.MINECRAFT_POISON))
    {
      CustomEffect customEffect = CustomEffectManager.getEffect(livingEntity, CustomEffectType.MINECRAFT_POISON);
      PotionEffect potionEffect = livingEntity.getPotionEffect(PotionEffectType.POISON);
      if (potionEffect == null || (potionEffect.getDuration() <= 48 && potionEffect.getAmplifier() == 0))
      {
        livingEntity.addPotionEffect(new PotionEffect(PotionEffectType.POISON, 98, customEffect.getAmplifier(), false, false, false));
      }
    }
    if (CustomEffectManager.hasEffect(livingEntity, CustomEffectType.MINECRAFT_WITHER))
    {
      CustomEffect customEffect = CustomEffectManager.getEffect(livingEntity, CustomEffectType.MINECRAFT_WITHER);
      PotionEffect potionEffect = livingEntity.getPotionEffect(PotionEffectType.WITHER);
      if (potionEffect == null || (potionEffect.getDuration() <= 48 && potionEffect.getAmplifier() == 0))
      {
        livingEntity.addPotionEffect(new PotionEffect(PotionEffectType.WITHER, 98, customEffect.getAmplifier(), false, false, false));
      }
    }
    if (CustomEffectManager.hasEffect(livingEntity, CustomEffectType.MINECRAFT_HEALTH_BOOST))
    {
      CustomEffect customEffect = CustomEffectManager.getEffect(livingEntity, CustomEffectType.MINECRAFT_HEALTH_BOOST);
      livingEntity.addPotionEffect(new PotionEffect(PotionEffectType.HEALTH_BOOST, 2, customEffect.getAmplifier(), false, false, false));
    }
    if (CustomEffectManager.hasEffect(livingEntity, CustomEffectType.MINECRAFT_ABSORPTION))
    {
      CustomEffect customEffect = CustomEffectManager.getEffect(livingEntity, CustomEffectType.MINECRAFT_ABSORPTION);
      livingEntity.addPotionEffect(new PotionEffect(PotionEffectType.ABSORPTION, 2, customEffect.getAmplifier(), false, false, false));
    }
    if (CustomEffectManager.hasEffect(livingEntity, CustomEffectType.MINECRAFT_SATURATION))
    {
      CustomEffect customEffect = CustomEffectManager.getEffect(livingEntity, CustomEffectType.MINECRAFT_SATURATION);
      livingEntity.addPotionEffect(new PotionEffect(PotionEffectType.SATURATION, 2, customEffect.getAmplifier(), false, false, false));
    }
    if (CustomEffectManager.hasEffect(livingEntity, CustomEffectType.MINECRAFT_GLOWING))
    {
      CustomEffect customEffect = CustomEffectManager.getEffect(livingEntity, CustomEffectType.MINECRAFT_GLOWING);
      livingEntity.addPotionEffect(new PotionEffect(PotionEffectType.GLOWING, 2, customEffect.getAmplifier(), false, false, false));
    }
    if (CustomEffectManager.hasEffect(livingEntity, CustomEffectType.MINECRAFT_LEVITATION))
    {
      CustomEffect customEffect = CustomEffectManager.getEffect(livingEntity, CustomEffectType.MINECRAFT_LEVITATION);
      livingEntity.addPotionEffect(new PotionEffect(PotionEffectType.LEVITATION, 2, customEffect.getAmplifier(), false, false, false));
    }
    if (CustomEffectManager.hasEffect(livingEntity, CustomEffectType.MINECRAFT_LUCK))
    {
      CustomEffect customEffect = CustomEffectManager.getEffect(livingEntity, CustomEffectType.MINECRAFT_LUCK);
      livingEntity.addPotionEffect(new PotionEffect(PotionEffectType.LUCK, 2, customEffect.getAmplifier(), false, false, false));
    }
    if (CustomEffectManager.hasEffect(livingEntity, CustomEffectType.MINECRAFT_UNLUCK))
    {
      CustomEffect customEffect = CustomEffectManager.getEffect(livingEntity, CustomEffectType.MINECRAFT_UNLUCK);
      livingEntity.addPotionEffect(new PotionEffect(PotionEffectType.UNLUCK, 2, customEffect.getAmplifier(), false, false, false));
    }
    if (CustomEffectManager.hasEffect(livingEntity, CustomEffectType.MINECRAFT_SLOW_FALLING))
    {
      CustomEffect customEffect = CustomEffectManager.getEffect(livingEntity, CustomEffectType.MINECRAFT_SLOW_FALLING);
      livingEntity.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_FALLING, 2, customEffect.getAmplifier(), false, false, false));
    }
    if (CustomEffectManager.hasEffect(livingEntity, CustomEffectType.MINECRAFT_CONDUIT_POWER))
    {
      CustomEffect customEffect = CustomEffectManager.getEffect(livingEntity, CustomEffectType.MINECRAFT_CONDUIT_POWER);
      livingEntity.addPotionEffect(new PotionEffect(PotionEffectType.CONDUIT_POWER, 2, customEffect.getAmplifier(), false, false, false));
    }
    if (CustomEffectManager.hasEffect(livingEntity, CustomEffectType.MINECRAFT_DOLPHINS_GRACE))
    {
      CustomEffect customEffect = CustomEffectManager.getEffect(livingEntity, CustomEffectType.MINECRAFT_DOLPHINS_GRACE);
      livingEntity.addPotionEffect(new PotionEffect(PotionEffectType.DOLPHINS_GRACE, 2, customEffect.getAmplifier(), false, false, false));
    }
    if (CustomEffectManager.hasEffect(livingEntity, CustomEffectType.MINECRAFT_BAD_OMEN))
    {
      CustomEffect customEffect = CustomEffectManager.getEffect(livingEntity, CustomEffectType.MINECRAFT_BAD_OMEN);
      livingEntity.addPotionEffect(new PotionEffect(PotionEffectType.BAD_OMEN, 2, customEffect.getAmplifier(), false, false, false));
    }
    if (CustomEffectManager.hasEffect(livingEntity, CustomEffectType.MINECRAFT_HERO_OF_THE_VILLAGE))
    {
      CustomEffect customEffect = CustomEffectManager.getEffect(livingEntity, CustomEffectType.MINECRAFT_HERO_OF_THE_VILLAGE);
      livingEntity.addPotionEffect(new PotionEffect(PotionEffectType.HERO_OF_THE_VILLAGE, 2, customEffect.getAmplifier(), false, false, false));
    }
    if (CustomEffectManager.hasEffect(livingEntity, CustomEffectType.MINECRAFT_DARKNESS))
    {
      CustomEffect customEffect = CustomEffectManager.getEffect(livingEntity, CustomEffectType.MINECRAFT_DARKNESS);
      livingEntity.addPotionEffect(new PotionEffect(PotionEffectType.DARKNESS, 60, customEffect.getAmplifier(), false, false, false));
    }
  }

  public static void gliding(@NotNull Player player)
  {
    if (player.isGliding())
    {
      CustomEffectManager.addEffect(player, CustomEffectType.GLIDING);
    }
  }

  public static void starCatch(@NotNull Player player)
  {
    if (CustomEffectManager.hasEffect(player, CustomEffectType.STAR_CATCH_PROCESS))
    {
      int duration = CustomEffectManager.getEffect(player, CustomEffectType.STAR_CATCH_PROCESS).getDuration();
      boolean ok = false;
      Integer penalty = Variable.starCatchPenalty.get(player.getUniqueId());
      if (penalty == null)
      {
        penalty = 0;
      }
      int level = Math.min(4, penalty / 20);
      Outter:
      for (int i = 1; i <= 10; i++)
      {
        switch (level)
        {
          case 0 -> {
            if (duration >= i * 20 - 14 && duration <= i * 20 - 6)
            {
              ok = true;
              break Outter;
            }
          }
          case 1 -> {
            if (duration >= i * 20 - 13 && duration <= i * 20 - 7)
            {
              ok = true;
              break Outter;
            }
          }
          case 2 -> {
            if (duration >= i * 20 - 11 && duration <= i * 20 - 8)
            {
              ok = true;
              break Outter;
            }
          }
          case 3 -> {
            if (duration >= i * 20 - 10 && duration <= i * 20 - 9)
            {
              ok = true;
              break Outter;
            }
          }
          default -> {
            if (duration == i * 20 - 10)
            {
              ok = true;
              break Outter;
            }
          }
        }
      }
      String meh = "---------------------";
      char[] m = meh.toCharArray();
      int offset = duration % 40;
      if (offset <= 20)
      {
        m[offset] = '★';
      }
      else
      {
        m[40 - offset] = '★';
      }
      if (offset == 0 || offset == 20)
      {
        player.playSound(player.getLocation(), "star_catch_" + (level + 1), SoundCategory.PLAYERS, 2F, 1F);
      }
      meh = "" + new String(m) + "";
      MessageUtil.sendTitle(player, ComponentUtil.translate(meh.replace("★", "%s").replace("-", "─"), (ok ? "&a" : "&c") + "★"), "&e" + Constant.Jeongsu.format(duration / 20), 0, 5, 0);
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

  public static void trueInvisibility(@NotNull Entity entity)
  {
    Set<String> scoreboardTags = entity.getScoreboardTags();
    if (scoreboardTags.contains("no_cucumbery_true_invisibility") || CustomEffectManager.hasEffect(entity, CustomEffectType.COMBO_EXPERIENCE))
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
