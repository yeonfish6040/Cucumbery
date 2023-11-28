package com.jho5245.cucumbery.custom.customeffect;

import com.jho5245.cucumbery.Cucumbery;
import com.jho5245.cucumbery.commands.sound.CommandSong;
import com.jho5245.cucumbery.custom.customeffect.CustomEffect.DisplayType;
import com.jho5245.cucumbery.custom.customeffect.children.group.*;
import com.jho5245.cucumbery.custom.customeffect.type.*;
import com.jho5245.cucumbery.events.entity.EntityCustomEffectRemoveEvent.RemoveReason;
import com.jho5245.cucumbery.util.gui.GUIManager;
import com.jho5245.cucumbery.util.no_groups.MessageUtil;
import com.jho5245.cucumbery.util.no_groups.Method;
import com.jho5245.cucumbery.util.no_groups.Method2;
import com.jho5245.cucumbery.util.no_groups.Scheduler;
import com.jho5245.cucumbery.util.storage.component.util.ComponentUtil;
import com.jho5245.cucumbery.util.storage.data.Constant;
import com.jho5245.cucumbery.util.storage.data.Variable;
import com.jho5245.cucumbery.util.storage.no_groups.CustomConfig.UserData;
import com.jho5245.cucumbery.util.storage.no_groups.ItemStackUtil;
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
import org.bukkit.entity.FishHook.HookState;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.*;
import org.bukkit.inventory.InventoryView.Property;
import org.bukkit.plugin.IllegalPluginAccessException;
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
        Bukkit.getScheduler().runTaskLater(Cucumbery.getPlugin(), () ->
                CustomEffectManager.removeEffect(entity, customEffect.getType(), customEffect.getAmplifier(), RemoveReason.TIME_OUT), 0L);
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
    customEffects.addAll(new ArrayList<>(CustomEffectManager.getEffects(player, DisplayType.BOSS_BAR_ONLY)));
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
          // 메모리 트롤링?
          try
          {
            player.hideBossBar(Variable.customEffectBossBarMap.remove(uuid));
          }
          catch (NullPointerException ignored)
          {

          }
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
              ComponentUtil.translate("rg255,204;적용 중인 효과 목록 : %s개", "&2" + customEffects.size())
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
  }

  public static void displayGUI(@NotNull Player player)
  {
    InventoryView inventoryView = player.getOpenInventory();
    Component title = inventoryView.title();
    if (GUIManager.isGUITitle(title) && title instanceof TranslatableComponent translatableComponent && translatableComponent.args().get(1) instanceof TextComponent textComponent && textComponent.content().equals(Constant.POTION_EFFECTS))
    {
      CustomEffectGUI.openGUI(player, false);
    }
  }

  public static void fly(@NotNull Player player)
  {
    if (CustomEffectManager.hasEffect(player, CustomEffectType.FLY_NOT_ENABLED) && !(player.getGameMode() == GameMode.CREATIVE || player.getGameMode() == GameMode.SPECTATOR))
    {
      player.setAllowFlight(false);
      return;
    }
    if (CustomEffectManager.hasEffect(player, CustomEffectType.FLY) || CustomEffectManager.hasEffect(player, CustomEffectType.FLY_REMOVE_ON_QUIT))
    {
      player.setAllowFlight(true);
    }
  }

  public static void ascension(@NotNull Entity entity)
  {
    if (CustomEffectManager.hasEffect(entity, CustomEffectType.ASCENSION))
    {
      if (entity.isOnGround())
      {
        Bukkit.getScheduler().runTaskLater(Cucumbery.getPlugin(), () ->
                CustomEffectManager.addEffect(entity, CustomEffectType.ASCENSION_COOLDOWN), 0L);
        return;
      }
      if (CustomEffectManager.hasEffect(entity, CustomEffectType.ASCENSION_COOLDOWN))
      {
        return;
      }
      int amplifier = CustomEffectManager.getEffect(entity, CustomEffectType.ASCENSION).getAmplifier() + 1;
      Vector vector = entity.getVelocity();

      Bukkit.getScheduler().runTaskLater(Cucumbery.getPlugin(), () ->
      {
        if (entity instanceof Player player && player.isSneaking())
        {
          entity.setVelocity(new Vector(vector.getX(), -0.08 * amplifier, vector.getZ()));
        }
        else
        {
          entity.setVelocity(new Vector(vector.getX(), 0.08 * amplifier, vector.getZ()));
        }
      }, 0L);
    }
  }

  public static void superiorLevitation(@NotNull Entity entity)
  {
    CustomEffect customEffect = CustomEffectManager.getEffectNullable(entity, CustomEffectType.SUPERIOR_LEVITATION);
    if (customEffect != null)
    {
      int amplifier = customEffect.getAmplifier();
      Location location = entity.getLocation();
      Vector vector = entity.getVelocity();
      entity.teleportAsync(new Location(location.getWorld(), location.getX(), location.getY() + (amplifier + 1) * 0.9 / 20, location.getZ(), location.getYaw(), location.getPitch()));
      entity.setVelocity(new Vector(vector.getX(), 0.08, vector.getZ()));
    }
  }

  public static void rune(@NotNull Player player)
  {
    CustomEffect customEffect = CustomEffectManager.getEffectNullable(player, CustomEffectTypeRune.RUNE_USING);
    if (customEffect instanceof StringCustomEffect stringCustomEffect)
    {
      String s = stringCustomEffect.getString();
      MessageUtil.sendTitle(player, s, ItemStackUtil.itemExists(player.getInventory().getItemInMainHand()) ? "룬을 해방하려면 마우스를 순서대로 클릭하세요. R = 우클릭, L = 좌클릭" : "&c룬을 해방하려면 손에 아이템을 들고 있어야 합니다! R = 우클릭, L = 좌클릭", 0, 3, 0);
    }
  }

  public static void gaesans(@NotNull Player player)
  {
    if (CustomEffectManager.hasEffect(player, CustomEffectType.GAESANS) && player.isSneaking() && ((Entity) player).isOnGround())
    {
      player.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 2, 0, false, false, false));
    }
  }

  public static void masterOfFishing(@NotNull Player player)
  {
    if (CustomEffectManager.hasEffect(player, CustomEffectType.MASTER_OF_FISHING) || CustomEffectManager.hasEffect(player, CustomEffectType.MASTER_OF_FISHING_D))
    {
      FishHook fishHook = player.getFishHook();
      if (fishHook != null && fishHook.getState() == HookState.BOBBING)
      {
        fishHook.setWaitTime(1);
      }
    }
  }

  public static void dynamicLight(@NotNull Player player)
  {
    if (CustomEffectManager.hasEffect(player, CustomEffectType.DYNAMIC_LIGHT))
    {
      PlayerInventory inventory = player.getInventory();
      Material mainHand = inventory.getItemInMainHand().getType(), offHand = inventory.getItemInOffHand().getType();
      if (Constant.OPTIFINE_DYNAMIC_LIGHT_ITEMS.contains(mainHand) || Constant.OPTIFINE_DYNAMIC_LIGHT_ITEMS.contains(offHand))
      {
        player.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, 4, 0, false, false, false));
      }
    }
  }

  public static void vanillaEffect(@NotNull Entity entity)
  {
    if (!(entity instanceof LivingEntity livingEntity))
    {
      return;
    }
    // CustomEffect에서 VanillaEffect 적용 로직 변경 (기존: 커스텀 효과 지속 시간동안 짧은 바닐라 효과 무한 지급, 현재: 커스텀 효과 지속 시간동안 해당 효과가 없을 경우 같은 지속시간만큼 바닐라 효과 지급) 2023.11.28 추가
    for (CustomEffect customEffect : CustomEffectManager.getEffects(livingEntity))
    {
      CustomEffectType customEffectType = customEffect.getType();
      if (customEffectType.getNamespacedKey().getNamespace().equals("minecraft"))
      {
        PotionEffectType potionEffectType = PotionEffectType.getByName(customEffectType.getNamespacedKey().getKey());
        if (potionEffectType == null)
        {
          throw new NullPointerException("Invalid Potion Effect Type: " + customEffectType.getIdString());
        }
        livingEntity.addPotionEffect(new PotionEffect(potionEffectType, customEffect.getDuration(), customEffect.getAmplifier(), false, false, false));
      }
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
    if (CustomEffectManager.hasEffect(player, CustomEffectTypeReinforce.STAR_CATCH_PREPARE))
    {
      Integer penalty = Variable.starCatchPenalty.get(player.getUniqueId());
      MessageUtil.sendTitle(player, ComponentUtil.translate("rg255,204;스타캐치"), ComponentUtil.translate(
              penalty != null && penalty >= 20 ? "연속해서 강화를 시도하면 스타캐치 난이도가 증가합니다" :
                      "별이 초록색일 때 스타캐치 클릭하면 강화 성공률 증가!"
      ), 0, 100, 0);
    }
    if (CustomEffectManager.hasEffect(player, CustomEffectTypeReinforce.STAR_CATCH_PROCESS))
    {
      int duration = CustomEffectManager.getEffect(player, CustomEffectTypeReinforce.STAR_CATCH_PROCESS).getDuration();
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
          case 0 ->
          {
            if (duration >= i * 20 - 14 && duration <= i * 20 - 6)
            {
              ok = true;
              break Outter;
            }
          }
          case 1 ->
          {
            if (duration >= i * 20 - 13 && duration <= i * 20 - 7)
            {
              ok = true;
              break Outter;
            }
          }
          case 2 ->
          {
            if (duration >= i * 20 - 11 && duration <= i * 20 - 8)
            {
              ok = true;
              break Outter;
            }
          }
          case 3 ->
          {
            if (duration >= i * 20 - 10 && duration <= i * 20 - 9)
            {
              ok = true;
              break Outter;
            }
          }
          default ->
          {
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
      MessageUtil.sendTitle(player, ComponentUtil.translate(meh.replace("★", "%s").replace("-", "─"), (ok ? "&a" : "&c") + "★"), Constant.THE_COLOR_HEX + Constant.Jeongsu.format(duration / 20), 0, 5, 0);
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
      Collection<Entity> nearbyPlayers = Method2.getNearbyEntitiesAsync(player.getLocation(), spreadAmplifier + 1);
      for (Entity entity : nearbyPlayers)
      {
        if (!(entity instanceof Player p) || p.getGameMode() == GameMode.SPECTATOR)
        {
          continue;
        }
        CustomEffectManager.addEffect(entity, customEffectSpread);
        if (detoxicate)
        {
          CustomEffectManager.addEffect(entity, CustomEffectManager.getEffect(player, CustomEffectType.VAR_DETOXICATE));
        }
        if (pneumonia)
        {
          CustomEffectManager.addEffect(entity, CustomEffectManager.getEffect(player, CustomEffectType.VAR_PNEUMONIA));
        }
        if (podagra)
        {
          CustomEffectManager.addEffect(entity, CustomEffectManager.getEffect(player, CustomEffectType.VAR_PODAGRA));
        }
        if (stomachache)
        {
          CustomEffectManager.addEffect(entity, CustomEffectManager.getEffect(player, CustomEffectType.VAR_STOMACHACHE));
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

  @SuppressWarnings("all")
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
          Bukkit.getScheduler().runTaskLater(Cucumbery.getPlugin(), () ->
                  player.hideEntity(Cucumbery.getPlugin(), entity), 0L);
        }
      }
    }
    else
    {
      for (Player player : Bukkit.getOnlinePlayers())
      {
        if (entity != player && !player.canSee(entity))
        {
          Bukkit.getScheduler().runTaskLater(Cucumbery.getPlugin(), () ->
                  player.showEntity(Cucumbery.getPlugin(), entity), 0L);
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
      Bukkit.getScheduler().runTaskLater(Cucumbery.getPlugin(), () ->
      {
        player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 2, 0, false, false, false));
        PotionEffect potionEffect = player.getPotionEffect(PotionEffectType.REGENERATION);
        if (potionEffect == null || (potionEffect.getDuration() <= 48 && potionEffect.getAmplifier() == 0))
        {
          player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 98, 0, false, false, false));
        }
      }, 0L);
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
        try
        {
          Bukkit.getScheduler().runTaskLater(Cucumbery.getPlugin(), () -> darknessTerrorTimer.add(player.getUniqueId()), 40L);
        }
        catch (IllegalPluginAccessException ignored)
        {

        }
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
    if (!UserData.LISTEN_GLOBAL.getBoolean(player) || !Cucumbery.using_NoteBlockAPI || CommandSong.radioSongPlayer == null || CommandSong.song == null)
    {
      CustomEffectManager.removeEffect(player, CustomEffectType.SERVER_RADIO_LISTENING);
    }
    else
    {
      Song song = CommandSong.song;
      float speed = song.getSpeed();
      int length = song.getLength();
      int amplifier = (int) Math.floor(length / 100d / speed);
      amplifier = Math.min(4, Math.max(0, amplifier));
      CustomEffect customEffect = CustomEffectManager.getEffectNullable(player, CustomEffectType.SERVER_RADIO_LISTENING);
      if (customEffect == null || customEffect.getAmplifier() != amplifier)
      {
        CustomEffectManager.removeEffect(player, CustomEffectType.SERVER_RADIO_LISTENING);
        CustomEffectManager.addEffect(player, new CustomEffect(CustomEffectType.SERVER_RADIO_LISTENING, CustomEffectType.SERVER_RADIO_LISTENING.getDefaultDuration(), amplifier));
      }
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
    List<Entity> entities = Method2.getNearbyEntitiesAsync(entity, 15);
    for (Entity loop : entities)
    {
      if (loop instanceof Parrot parrot && parrot.getOwner() == entity)
      {
        Bukkit.getScheduler().runTaskLater(Cucumbery.getPlugin(), () ->
                CustomEffectManager.addEffect(entity, new CustomEffect(CustomEffectType.PARROTS_CHEER)), 0L);
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
