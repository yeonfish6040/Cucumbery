package com.jho5245.cucumbery.deathmessages;

import com.jho5245.cucumbery.Cucumbery;
import com.jho5245.cucumbery.util.MessageUtil;
import com.jho5245.cucumbery.util.Method;
import com.jho5245.cucumbery.util.itemlore.ItemLore;
import com.jho5245.cucumbery.util.storage.ComponentUtil;
import com.jho5245.cucumbery.util.storage.ItemStackUtil;
import com.jho5245.cucumbery.util.storage.data.Constant;
import com.jho5245.cucumbery.util.storage.data.Variable;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.*;
import org.bukkit.event.entity.*;
import org.bukkit.inventory.AbstractHorseInventory;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BlockDataMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.projectiles.BlockProjectileSource;
import org.bukkit.projectiles.ProjectileSource;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class DeathManager
{
  public static final Component BAD_RESPAWM_POINT = ComponentUtil.createTranslate("chat.square_brackets", ComponentUtil.createTranslate("death.attack.badRespawnPoint.link"))
          .hoverEvent(HoverEvent.showText(Component.text("MC-28723"))).clickEvent(ClickEvent.openUrl("https://bugs.mojang.com/browse/MCPE-28723"));
  public static final Component downloadURL = ComponentUtil.createTranslate("&9&l여기")
          .hoverEvent(HoverEvent.showText(ComponentUtil.createTranslate("클릭하여 %s 주소로 이동합니다.", "&ehttps://cucumbery.com/api/builds/dev/latest/download")))
          .clickEvent(ClickEvent.openUrl("https://cucumbery.com/api/builds/dev/latest/download"));
  public static final Component reportBugURL = ComponentUtil.createTranslate("&9&l여기")
          .hoverEvent(HoverEvent.showText(ComponentUtil.createTranslate("클릭하여 %s 주소로 이동합니다.", "&ehttps://github.com/jho5245/Cucumbery/issues")))
          .clickEvent(ClickEvent.openUrl("https://github.com/jho5245/Cucumbery/issues"));
  private static final int COOLDOWN_IN_TICKS = 5;
  public static String BRACKET = Variable.deathMessages.getString("death-messages.prefix.bracket", "&6[%s]");
  public static Component DEATH_PREFIX = ComponentUtil.createTranslate(
          BRACKET, ComponentUtil.createTranslate(Variable.deathMessages.getString("death-messages.prefix.death")));
  public static Component DEATH_PREFIX_PVP = ComponentUtil.createTranslate(
          BRACKET, ComponentUtil.createTranslate(Variable.deathMessages.getString("death-messages.prefix.pvp")));
  private static boolean goBack = false;

  public static void what(EntityDeathEvent event)
  {
    Component timeFormat = ComponentUtil.createTranslate("사망 시각 : %s", "&e" + Method.getCurrentTime(Calendar.getInstance(), true, false));
    DEATH_PREFIX_PVP = DEATH_PREFIX_PVP.hoverEvent(HoverEvent.showText(timeFormat));
    DEATH_PREFIX = DEATH_PREFIX.hoverEvent(HoverEvent.showText(timeFormat));
    LivingEntity entity = event.getEntity();
    if (!(entity instanceof Player))
    {
      if (goBack)
      {
        return;
      }
      goBack = true;
      Bukkit.getServer().getScheduler().runTaskLater(Cucumbery.getPlugin(), () ->
              goBack = false, COOLDOWN_IN_TICKS);
    }

    // 접두사 기능 붙여넣기
    boolean usePrefix = Variable.deathMessages.getBoolean("death-messages.prefix.enable");
    Location location = entity.getLocation();
    boolean success = entity instanceof Player;
    boolean isPlayerDeath = event instanceof PlayerDeathEvent;
    PlayerDeathEvent playerDeathEvent = isPlayerDeath ? (PlayerDeathEvent) event : null;
    if (entity.customName() != null)
    {
      success = success || location.getNearbyPlayers(100).size() > 0;
    }
    if (entity instanceof Tameable tameable)
    {
      success = success || tameable.isTamed();
    }
    if (entity instanceof Villager villager)
    {
      success = success || villager.getProfession() != Villager.Profession.NONE;
    }
    if (entity instanceof IronGolem ironGolem)
    {
      success = success || ironGolem.isPlayerCreated();
    }
    if (entity instanceof AbstractHorse horse)
    {
      AbstractHorseInventory inventory = horse.getInventory();
      success = success || !Method.inventoryEmpty(inventory);
    }
    if (entity instanceof Boss)
    {
      success = true;
    }

    EntityDamageEvent damageCause = entity.getLastDamageCause();
    if (damageCause == null)
    {
      damageCause = new EntityDamageEvent(entity, EntityDamageEvent.DamageCause.VOID, Double.MAX_VALUE);
    }
    EntityDamageEvent.DamageCause cause = damageCause.getCause();
    if (cause == EntityDamageEvent.DamageCause.LIGHTNING)
    {
      success = true;
    }
    if (cause == EntityDamageEvent.DamageCause.VOID)
    {
      if (damageCause.getDamage() < 100)
      {
        success = true;
      }
    }

    if (success)
    {
      Component entityComponent = ComponentUtil.senderComponent(entity);
      List<Component> args = new ArrayList<>();
      World world = location.getWorld();
      String worldName = world.getName();
      if (!(entity instanceof Player))
      {
        entityComponent = entityComponent.clickEvent(ClickEvent.suggestCommand("/atp @s " + worldName + " "
                + location.getX() + " " + location.getY() + " " + location.getZ() + " " + location.getYaw() + " " + location.getPitch()));
      }
      args.add(entityComponent);
      List<Component> extraArgs = new ArrayList<>();
      String key = "";
      Object damager = getDamager(event);
      ItemStack weapon = getWeapon(event);
      ItemStack lastTrampledBlock = getLastTrampledBlock(entity.getUniqueId());
      float fallDistance = entity.getFallDistance();
//      MessageUtil.broadcastDebug(cause);
      switch (cause)
      {
        case CONTACT -> {
          if (damageCause instanceof EntityDamageByBlockEvent blockEvent)
          {
            Block block = blockEvent.getDamager();
            if (block != null)
            {
              Material type = block.getType();
              ItemStack itemStack = ItemStackUtil.getItemStackFromBlock(block);
              ItemMeta itemMeta = itemStack.getItemMeta();
              if (itemMeta instanceof BlockDataMeta blockDataMeta)
              {
                blockDataMeta.setBlockData(block.getBlockData());
                itemStack.setItemMeta(blockDataMeta);
                ItemLore.setItemLore(itemStack);
              }
              extraArgs.add(ComponentUtil.create(itemStack));
              if (type == Material.SWEET_BERRY_BUSH)
              {
                key = "sweet_berry_bush";
              }
              else if (type == Material.POINTED_DRIPSTONE)
              {
                key = "stalagmite";
              }
              else if (type == Material.CACTUS)
              {
                key = "cactus";
              }
              else
              {
                key = "contact";
              }
            }
          }
        }
        case ENTITY_ATTACK -> {
          if (damageCause instanceof EntityDamageByEntityEvent damageByEntityEvent)
          {
            Entity damagerEntity = damageByEntityEvent.getDamager();
            if (damagerEntity instanceof AreaEffectCloud)
            {
              key = "magic";
            }
            else
            {
              key = "melee";
              if (weapon != null)
              {
                Material type = weapon.getType();
                switch (type)
                {
                  case DIAMOND_SWORD, GOLDEN_SWORD, IRON_SWORD, NETHERITE_SWORD, STONE_SWORD -> key += "_sword";
                }
              }
            }
          }
          else
          {
            key = "entity_attack";
          }
        }
        case ENTITY_SWEEP_ATTACK -> key = "melee_sweep";
        case MAGIC -> key = "magic";
        case PROJECTILE -> {
          if (damageCause instanceof EntityDamageByEntityEvent damageByEntityEvent)
          {
            Entity damagerEntity = damageByEntityEvent.getDamager();
            if (damagerEntity instanceof Trident)
            {
              key = "trident";
            }
            else if (damagerEntity instanceof Fireball fireball)
            {
              if (fireball instanceof DragonFireball)
              {
                key = "dragon_fireball";
              }
              else if (fireball instanceof SizedFireball sizedFireball)
              {
                key = "fireball";
                extraArgs.add(ComponentUtil.create(sizedFireball.getDisplayItem()));
              }
              else if (fireball instanceof WitherSkull)
              {
                key = "wither_skull";
              }
              else
              {
                key = "fireball";
              }
            }
            else if (damagerEntity instanceof ShulkerBullet)
            {
              key = "shulker_bullet";
            }
            else if (damagerEntity instanceof AbstractArrow)
            {
              key = "arrow";
            }
            else
            {
              key = "projectile";
            }
          }
          else
          {
            key = "projectile";
          }
        }
        case SUFFOCATION -> {
          key = "suffocation";
          Block block = entity.getEyeLocation().getBlock();
          Material type = block.getType();
          extraArgs.add(ComponentUtil.create(ItemStackUtil.getItemStackFromBlock(entity.getEyeLocation().getBlock())));
        }
        case FALL -> {
          if (damageCause instanceof EntityDamageByEntityEvent damageByEntityEvent && damageByEntityEvent.getDamager() instanceof EnderPearl enderPearl && entity.equals(enderPearl.getShooter()))
          {
            key = "ender_pearl";
            extraArgs.add(ComponentUtil.create(enderPearl.getItem()));
          }
          else
          {
            key = "fall";
            if (lastTrampledBlock != null)
            {
              key += "_block";
              extraArgs.add(ComponentUtil.create(lastTrampledBlock));
            }
            if (fallDistance >= 6)
            {
              key += "_high";
            }
          }
          extraArgs.add(ComponentUtil.create(Constant.Sosu2.format(entity.getFallDistance())).color(Constant.THE_COLOR));
        }
        case FIRE -> key = "fire_block";
        case FIRE_TICK -> key += "fire";
        case MELTING -> key = "melting";
        case LAVA -> {
          key = "lava";
          Block block = location.getBlock();
          if (block.getType() == Material.LAVA_CAULDRON)
          {
            key += "_cauldron";
            extraArgs.add(ComponentUtil.create(ItemStackUtil.getItemStackFromBlock(block)));
          }
        }
        case DROWNING -> key = "drown";
        case BLOCK_EXPLOSION -> {
          if (damageCause instanceof EntityDamageByBlockEvent damageByBlockEvent)
          {
            Block block = damageByBlockEvent.getDamager();
            MessageUtil.broadcastDebug(block + "");
          }
          key += "bad_respawn";
          args.add(BAD_RESPAWM_POINT);
        }
        case ENTITY_EXPLOSION -> {
          if (damageCause instanceof EntityDamageByEntityEvent damageByEntityEvent)
          {
            Entity damagerEntity = damageByEntityEvent.getDamager();
            if (damagerEntity instanceof Firework)
            {
              key = "fireworks";
              if (damager != null)
              {
                if (ItemStackUtil.itemExists(weapon) && weapon.getType() == Material.CROSSBOW)
                {
                  key = "fireworks_crossbow";
                }
              }
            }
            else
            {
              key = "explosion";
            }
          }
        }
        case VOID -> {
          if (damageCause.getDamage() > Math.pow(2, 127))
          {
            key = "kill";
            extraArgs.add(Component.text(entity.getName()));
          }
          else
          {
            key = "void";
            if (lastTrampledBlock != null)
            {
              extraArgs.add(ComponentUtil.create(lastTrampledBlock));
              key += "_block";
            }
            if (fallDistance > 350)
            {
              key += "_high";
            }
            extraArgs.add(ComponentUtil.create(Constant.Sosu2.format(entity.getFallDistance())).color(Constant.THE_COLOR));
          }
        }
        case LIGHTNING -> {
          key = "lightning_bolt";
          if (damageCause instanceof EntityDamageByEntityEvent damageByEntityEvent)
          {
            Entity damagerEntity = damageByEntityEvent.getDamager();
            if (damagerEntity instanceof LightningStrike lightningStrike)
            {
              extraArgs.add(ComponentUtil.senderComponent(lightningStrike));
            }
          }
        }
        case SUICIDE -> key = "suicide";
        case STARVATION -> key = "starve";
        case POISON -> key = "poison";
        case WITHER -> key = "wither";
        case FALLING_BLOCK -> {
          if (damageCause instanceof EntityDamageByEntityEvent damageByEntityEvent)
          {
            Entity damagerEntity = damageByEntityEvent.getDamager();
            if (damagerEntity instanceof FallingBlock fallingBlock)
            {
              Material material = fallingBlock.getBlockData().getMaterial();
              switch (material)
              {
                case ANVIL -> key = "anvil";
                case POINTED_DRIPSTONE -> key = "falling_stalactite";
              }
              extraArgs.add(ComponentUtil.create(fallingBlock));
            }
          }
        }
        case THORNS -> key = "thorns";
        case DRAGON_BREATH -> key = "dragon_breath";
        case CUSTOM -> key = "generic";
        case FLY_INTO_WALL -> {
          key = "elytra";
          Vector vector = location.getDirection();
          Location locationClone = location.clone();
          locationClone.add(vector.setX(vector.getX() / 2).setY(vector.getY() / 2).setZ(vector.getZ() / 2));
          extraArgs.add(ComponentUtil.create(ItemStackUtil.getItemStackFromBlock(
                  locationClone.getBlock()
          )));
        }
        case HOT_FLOOR -> {
          key = "magma_block";
          if (damageCause instanceof EntityDamageByBlockEvent damageByBlockEvent)
          {
            Block block = damageByBlockEvent.getDamager();
            if (block != null)
            {
              extraArgs.add(ComponentUtil.create(ItemStackUtil.getItemStackFromBlock(block)));
            }
          }
        }
        case CRAMMING -> {
          key = "cramming";
          Collection<LivingEntity> livingEntities = world.getNearbyLivingEntities(location, 1.5d);
          livingEntities.removeIf(e -> e.equals(entity));
          if (livingEntities.size() == 0)
          {
            key += "_none";
          }
          else
          {
            extraArgs.add(ComponentUtil.senderComponent(livingEntities));
          }
          Integer i = world.getGameRuleValue(GameRule.MAX_ENTITY_CRAMMING);
          if (i != null)
          {
            if (i.equals(1) && livingEntities.size() == 1)
            {
              key += "_solo";
            }
          }
        }
        case DRYOUT -> key = "dryOut";
        case FREEZE -> key = "freeze";
      }
      if (damager != null)
      {
        if (damager instanceof ItemStack itemStack)
        {
          args.add(ComponentUtil.create(itemStack));
        }
        else
        {
          args.add(ComponentUtil.senderComponent(damager));
          double distance = -1;
          try
          {
            distance = ((Entity) damager).getLocation().distance(entity.getLocation());
          }
          catch (Exception ignored)
          {

          }
          extraArgs.add(ComponentUtil.create(Constant.Sosu2.format(distance)).color(Constant.THE_COLOR));
        }
        key += "_combat";
      }

      if (ItemStackUtil.itemExists(weapon))
      {
        args.add(ComponentUtil.create(weapon));
        key += "_item";
      }
      else
      {
        Projectile projectile = getDamagerProjectile(event);
        if (projectile != null && projectile.getShooter() == null)
        {
          key += "_unknown";
        }
      }

      MessageUtil.broadcastDebug(key);
      Messages deathMessages;
      try
      {
        deathMessages = Messages.valueOf(key.toUpperCase());
      }
      catch (Exception e)
      {
        deathMessages = Messages.UNKNOWN;
        extraArgs.add(reportBugURL);
      }
      List<String> keys = deathMessages.getKeys();
      // 조건부 데스 메시지가 있으면 데스 메시지 치환
      Condition.changeDeathMessages(event, key, keys);

      if (deathMessages == Messages.UNKNOWN)
      {
        keys.clear();
        extraArgs.add(Component.text(key));
        keys.add("%1$s이(가) 알 수 없는 이유로 죽었습니다. 죄송합니다! 이 메시지가 뜨면 개발자가 일을 안 한겁니다! %" + (extraArgs.size() + args.size() - 1) + "$s에서 해당 버그를 제보해주세요! 키 : %" + (extraArgs.size() + args.size()) + "$s");
      }

      if (key.equals("kill_combat"))
      {
        extraArgs.add(downloadURL);
        keys.add("%1$s이(가) 알 수 없는 이유로 죽었습니다. 죄송합니다! 이 메시지가 뜨면 개발자가 일을 안 한겁니다! %2$s에서 해당 버그를 제보해주세요! 사실 이 메시지는 이스터 에그이며, 플러그인 다운로드 주소는 %" + (extraArgs.size() + args.size()) + "$s입니다!");
      }
      // 해당 키 목록에서 랜덤 키를 하나 가져옴
      if (keys.size() != 0)
      {
        int random = Method.random(0, keys.size() - 1);
        key = keys.get(random);
      }
      args.addAll(extraArgs);
      Component deathMessage = ComponentUtil.createTranslate(key, args);
      if (ComponentUtil.serializeAsJson(deathMessage).length() > 100000)
      {
        for (int i = 0; i < args.size(); i++)
        {
          Component component = args.get(i);
          if (component.hoverEvent() != null && Objects.requireNonNull(component.hoverEvent()).action() == HoverEvent.Action.SHOW_ITEM)
          {
            args.set(i, args.get(i).hoverEvent(null));
          }
        }
        deathMessage = ComponentUtil.createTranslate("death.attack.message_too_long", ComponentUtil.createTranslate(key, args));
      }
      if (usePrefix)
      {
        boolean isPvP = isPlayerDeath && (DeathManager.getDamager(event) instanceof Player && !entity.equals(DeathManager.getDamager(event)));
        Projectile projectile = getDamagerProjectile(event);
        if (projectile != null)
        {
          ProjectileSource projectileSource = projectile.getShooter();
          if (entity.equals(projectileSource))
          {
            isPvP = false;
          }
        }
        AreaEffectCloud areaEffectCloud = getDamagerAreaEffectCloud(event);
        if (areaEffectCloud != null)
        {
          ProjectileSource projectileSource = areaEffectCloud.getSource();
          if (entity.equals(projectileSource))
          {
            isPvP = false;
          }
        }
        deathMessage = Component.empty().append(isPvP ? DEATH_PREFIX_PVP : DEATH_PREFIX).append(deathMessage);
      }
      Variable.victimAndDamager.remove(entity.getUniqueId());
      Variable.victimAndBlockDamager.remove(entity.getUniqueId());
      Variable.lastTrampledBlock.remove(entity.getUniqueId());
      Variable.lastTrampledBlockType.remove(entity.getUniqueId());
      if (damager != null)
      {
        if (damager instanceof Entity e)
        {
          Variable.attackerAndWeapon.remove(e.getUniqueId());
        }
      }
      // 모든 플레이어에게 데스메시지 보냄
      if (event.isCancelled())
      {
        List<String> cancelledMessages = Variable.deathMessages.getStringList("death-messages.event-cancelled-messages");
        if (cancelledMessages.size() > 0)
        {
          String message = cancelledMessages.get(Method.random(0, cancelledMessages.size() - 1));
          deathMessage = deathMessage.append(ComponentUtil.createTranslate(message, entity));
        }
      }
      if (key.equals("none"))
      {
        deathMessage = null;
      }
      if (playerDeathEvent != null)
      {
        MessageUtil.consoleSendMessage(ComponentUtil.createTranslate("&7죽은 위치 : %s", ComponentUtil.locationComponent(location)));
        if (event.isCancelled() && deathMessage != null)
        {
          MessageUtil.broadcast(deathMessage);
        }
        playerDeathEvent.deathMessage(deathMessage);
      }
      else if (deathMessage != null)
      {
        MessageUtil.broadcastPlayer(deathMessage);
        int x = location.getBlockX();
        int y = location.getBlockY();
        int z = location.getBlockZ();
        // 콘솔에 디버그를 보내기 위함
        deathMessage = deathMessage.append(ComponentUtil.create("&7 - " + worldName + ", " + x + ", " + y + ", " + z));
        MessageUtil.consoleSendMessage(deathMessage);
      }
    }
  }

  @Nullable
  protected static ItemStack getLastTrampledBlock(@NotNull UUID uuid)
  {
    if (Variable.lastTrampledBlock.containsKey(uuid))
    {
      return Variable.lastTrampledBlock.get(uuid);
    }
    return null;
  }


  @Nullable
  protected static Object getDamager(EntityDeathEvent event)
  {
    LivingEntity entity = event.getEntity();
    Player player = entity.getKiller();
    if (player != null)
    {
      return player;
    }
    EntityDamageEvent entityDamageEvent = entity.getLastDamageCause();
    if (entityDamageEvent instanceof EntityDamageByEntityEvent damageEvent && damageEvent.getCause() != EntityDamageEvent.DamageCause.FALL)
    {
      EntityDamageEvent.DamageCause cause = entityDamageEvent.getCause();
      Entity damager = damageEvent.getDamager();
      if (damager instanceof LivingEntity)
      {
        return damager;
      }
      else if (damager instanceof Projectile projectile)
      {
        ProjectileSource projectileSource = projectile.getShooter();
        if (projectileSource == null)
        {
          if (cause != EntityDamageEvent.DamageCause.PROJECTILE)
          {
            return damager;
          }
          if (Variable.victimAndBlockDamager.containsKey(entity.getUniqueId()))
          {
            ItemStack itemStack = Variable.victimAndBlockDamager.get(entity.getUniqueId());
            if (ItemStackUtil.itemExists(itemStack) && itemStack.getType() == Material.DISPENSER)
            {
              return itemStack;
            }
            return itemStack;
          }
          return damager;
        }
        if (projectileSource instanceof LivingEntity)
        {
          return projectileSource;
        }
      }
      else if (damager instanceof AreaEffectCloud areaEffectCloud)
      {
        ProjectileSource projectileSource = areaEffectCloud.getSource();
        if (projectileSource == null)
        {
          if (Variable.victimAndBlockDamager.containsKey(entity.getUniqueId()))
          {
            ItemStack itemStack = Variable.victimAndBlockDamager.get(entity.getUniqueId());
            if (ItemStackUtil.itemExists(itemStack) && itemStack.getType() == Material.DISPENSER)
            {
              return itemStack;
            }
            return itemStack;
          }
          return damager;
        }
        if (projectileSource instanceof LivingEntity)
        {
          return projectileSource;
        }
      }
      else if (damager instanceof TNTPrimed tntPrimed)
      {
        Entity tntPrimer = tntPrimed.getSource();
        if (tntPrimer != null)
        {
          return tntPrimer;
        }

        return damager;
      }
      else if (!(
              (cause == EntityDamageEvent.DamageCause.FALLING_BLOCK && damager instanceof FallingBlock)
                      || (cause == EntityDamageEvent.DamageCause.LIGHTNING && damager instanceof LightningStrike)
      ))
      {
        return damager;
      }
    }
    if (Variable.victimAndDamager.containsKey(entity.getUniqueId()))
    {
      return Variable.victimAndDamager.get(entity.getUniqueId());
    }
    if (Variable.victimAndBlockDamager.containsKey(entity.getUniqueId()))
    {
      ItemStack itemStack = Variable.victimAndBlockDamager.get(entity.getUniqueId());
      if (ItemStackUtil.itemExists(itemStack) && itemStack.getType() == Material.DISPENSER)
      {
        return itemStack;
      }
      return itemStack;
    }
    return null;
  }

  @Nullable
  protected static Projectile getDamagerProjectile(EntityDeathEvent event)
  {
    LivingEntity livingEntity = event.getEntity();
    EntityDamageEvent entityDamageEvent = livingEntity.getLastDamageCause();
    if (entityDamageEvent instanceof EntityDamageByEntityEvent damageEvent)
    {
      Entity damager = damageEvent.getDamager();
      if (damager instanceof Projectile projectile)
      {
        return projectile;
      }
    }
    return null;
  }

  @Nullable
  protected static AreaEffectCloud getDamagerAreaEffectCloud(EntityDeathEvent event)
  {
    LivingEntity livingEntity = event.getEntity();
    EntityDamageEvent entityDamageEvent = livingEntity.getLastDamageCause();
    if (entityDamageEvent instanceof EntityDamageByEntityEvent damageEvent)
    {
      Entity damager = damageEvent.getDamager();
      if (damager instanceof AreaEffectCloud areaEffectCloud)
      {
        return areaEffectCloud;
      }
    }
    return null;
  }

  protected static boolean isMelee(EntityDeathEvent event)
  {
    LivingEntity livingEntity = event.getEntity();
    EntityDamageEvent entityDamageEvent = livingEntity.getLastDamageCause();
    if (entityDamageEvent instanceof EntityDamageByEntityEvent damageEvent)
    {
      Entity damager = damageEvent.getDamager();
      return damager instanceof LivingEntity;
    }
    return false;
  }

  @Nullable
  protected static ItemStack getWeapon(EntityDeathEvent event)
  {
    Object damagerObject = getDamager(event);
    if (damagerObject instanceof LivingEntity damager)
    {
      EntityEquipment entityEquipment = damager.getEquipment();
      if (entityEquipment != null)
      {
        ItemStack mainHand = entityEquipment.getItemInMainHand(), offHand = entityEquipment.getItemInOffHand();
        Projectile projectile = getDamagerProjectile(event);
        AreaEffectCloud areaEffectCloud = getDamagerAreaEffectCloud(event);
        if (isMelee(event))
        {
          return mainHand;
        }
        ItemStack weapon = null;
        if (areaEffectCloud != null)
        {
          weapon = Variable.projectile.get(areaEffectCloud.getUniqueId());

          Bukkit.getServer().getScheduler().runTaskLater(Cucumbery.getPlugin(), () ->
                  Variable.projectile.remove(areaEffectCloud.getUniqueId()), areaEffectCloud.getDuration());
        }
        if (projectile != null)
        {
          weapon = Variable.projectile.get(projectile.getUniqueId());
          Variable.projectile.remove(projectile.getUniqueId());
        }
        if (weapon == null && Variable.attackerAndWeapon.containsKey(damager.getUniqueId()))
        {
          return Variable.attackerAndWeapon.get(damager.getUniqueId());
        }
        return weapon;
      }
    }
    else
    {
      Projectile projectile = getDamagerProjectile(event);
      if (damagerObject != null && damagerObject.equals(projectile))
      {
        return null;
      }
      if (projectile instanceof EnderPearl && event.getEntity().getLastDamageCause() instanceof EntityDamageByEntityEvent damageByEntityEvent && damageByEntityEvent.getCause() == EntityDamageEvent.DamageCause.FALL)
      {
        return null;
      }
      if (projectile != null && Variable.projectile.containsKey(projectile.getUniqueId()))
      {
        return Variable.projectile.get(projectile.getUniqueId());
      }
      if (projectile != null && projectile.getShooter() instanceof BlockProjectileSource blockProjectileSource)
      {
        Block block = blockProjectileSource.getBlock();
        if (Variable.blockAttackerAndWeapon.containsKey(block.getLocation().toString()))
        {
          return Variable.blockAttackerAndWeapon.get(block.getLocation().toString());
        }
      }
      if (projectile instanceof ThrowableProjectile throwableProjectile)
      {
        return throwableProjectile.getItem();
      }
      else if (projectile instanceof AbstractArrow abstractArrow)
      {
        return Method.usingLoreFeature(projectile.getLocation()) ? ItemLore.setItemLore(abstractArrow.getItemStack()) : abstractArrow.getItemStack();
      }
      else if (projectile instanceof ThrownPotion thrownPotion)
      {
        return thrownPotion.getItem();
      }
      else if (projectile instanceof Firework firework)
      {
        ItemStack itemStack = new ItemStack(Material.FIREWORK_ROCKET);
        itemStack.setItemMeta(firework.getFireworkMeta());
        return Method.usingLoreFeature(projectile.getLocation()) ? ItemLore.setItemLore(itemStack) : itemStack;
      }
      AreaEffectCloud areaEffectCloud = getDamagerAreaEffectCloud(event);
      if (areaEffectCloud != null)
      {
        if (Variable.projectile.containsKey(areaEffectCloud.getUniqueId()))
        {
          return Variable.projectile.get(areaEffectCloud.getUniqueId());
        }
        if (areaEffectCloud.getSource() instanceof BlockProjectileSource blockProjectileSource)
        {
          Block block = blockProjectileSource.getBlock();
          if (Variable.blockAttackerAndWeapon.containsKey(block.getLocation().toString()))
          {
            return Variable.blockAttackerAndWeapon.get(block.getLocation().toString());
          }
        }
      }
    }
    return null;
  }
}
