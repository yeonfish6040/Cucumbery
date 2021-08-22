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
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.*;
import org.bukkit.event.entity.*;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.projectiles.BlockProjectileSource;
import org.bukkit.projectiles.ProjectileSource;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class DeathManager
{
  public static String BRACKET = Variable.deathMessages.getString("death-messages.prefix.bracket");

  static
  {
    if (BRACKET == null)
    {
      BRACKET = "&6[%s]";
    }
  }

  public static Component DEATH_PREFIX = ComponentUtil.createTranslate(
          BRACKET, ComponentUtil.createTranslate(Variable.deathMessages.getString("death-messages.prefix.death")));

  public static Component DEATH_PREFIX_PVP = ComponentUtil.createTranslate(
          BRACKET, ComponentUtil.createTranslate(Variable.deathMessages.getString("death-messages.prefix.pvp")));

  public static final Component BAD_RESPAWM_POINT = ComponentUtil.createTranslate("chat.square_brackets", ComponentUtil.createTranslate("death.attack.badRespawnPoint.link"))
          .hoverEvent(HoverEvent.showText(Component.text("MC-28723"))).clickEvent(ClickEvent.openUrl("https://bugs.mojang.com/browse/MCPE-28723"));

  public static void what(EntityDeathEvent event)
  {
    Component timeFormat = ComponentUtil.createTranslate("사망 시각 : %s", "&e" + Method.getCurrentTime(Calendar.getInstance(), true, false));
    DEATH_PREFIX_PVP = DEATH_PREFIX_PVP.hoverEvent(HoverEvent.showText(timeFormat));
    DEATH_PREFIX = DEATH_PREFIX.hoverEvent(HoverEvent.showText(timeFormat));
    LivingEntity entity = event.getEntity();
    // 접두사 기능 붙여넣기
    boolean usePrefix = Variable.deathMessages.getBoolean("death-messages.prefix.enable");
    Location location = entity.getLocation();
    boolean success = entity instanceof Player;
    boolean isPlayerDeath = event instanceof PlayerDeathEvent;
    PlayerDeathEvent playerDeathEvent = isPlayerDeath ? (PlayerDeathEvent) event : null;
    if (entity.customName() != null)
    {
      success = location.getNearbyPlayers(100).size() > 0;
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

    EntityDamageEvent damageCause = entity.getLastDamageCause();
    if (damageCause != null)
    {
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

      if (damageCause != null)
      {
        float fallDistance = entity.getFallDistance();
        EntityDamageEvent.DamageCause cause = damageCause.getCause();

        MessageUtil.broadcastDebug(cause);
        switch (cause)
        {
          case CONTACT -> {
            if (damageCause instanceof EntityDamageByBlockEvent blockEvent)
            {
              Block block = blockEvent.getDamager();
              if (block != null)
              {
                Material type = block.getType();
                  extraArgs.add(ComponentUtil.create(ItemStackUtil.getItemStackFromBlock(block)));
                if (type == Material.SWEET_BERRY_BUSH)
                {
                  key = "sweet_berry_bush";
                }
                else
                {
                  key = "cactus";
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
              }
            }
            else
            {
              key = "melee";
            }
          }
          case ENTITY_SWEEP_ATTACK -> key = "melee";
          case MAGIC -> key = "magic";
          case PROJECTILE -> {
            if (damageCause instanceof EntityDamageByEntityEvent damageByEntityEvent)
            {
              Entity damagerEntity = damageByEntityEvent.getDamager();
              if (damagerEntity instanceof Trident)
              {
                key = "trident";
              }
              else if (damagerEntity instanceof Fireball)
              {
                key = "fireball";
              }
              else
              {
                key = "arrow";
              }
            }
            else
            {
              key = "arrow";
            }
          }
          case SUFFOCATION -> key = "suffocation";
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
          }
          case FIRE -> key = "fire_block";
          case FIRE_TICK -> key += "fire";
          case MELTING -> key = "melting";
          case LAVA -> key = "lava";
          case DROWNING -> key = "drown";
          case BLOCK_EXPLOSION -> {
            if (damageCause instanceof EntityDamageByBlockEvent damageByBlockEvent)
            {
              Block block = damageByBlockEvent.getDamager();
              if (block != null)
              {
                Material type = block.getType();
              }
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
                key = "fireworks_crossbow";
                if (ItemStackUtil.itemExists(weapon) && weapon.getType() == Material.FIREWORK_ROCKET)
                {
                  key = "fireworks";
                }
              }
              else
              {
                key = "explosion";
              }
            }
          }
          case VOID -> {
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
          }
          case LIGHTNING -> key = "lightning_bolt";
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
                  case POINTED_DRIPSTONE -> key = "stalagmite";
                }
              }
            }
          }
          case THORNS -> key = "thorns";
          case DRAGON_BREATH -> key = "dragon_breath";
          case CUSTOM -> key = "generic";
          case FLY_INTO_WALL -> key = "elytra";
          case HOT_FLOOR -> key = "magma_block";
          case CRAMMING -> key = "cramming";
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
          deathMessages = Messages.GENERIC;
        }
        List<String> keys = deathMessages.getKeys();
        // 조건부 데스 메시지가 있으면 데스 메시지 치환
        Condition.changeDeathMessages(event, key, keys);
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
        if (playerDeathEvent != null)
        {
          playerDeathEvent.deathMessage(null);
        }
        Variable.victimAndDamager.remove(entity.getUniqueId());
        Variable.victimAndBlockDamager.remove(entity.getUniqueId());
        Variable.lastTrampledBlock.remove(entity.getUniqueId());
        Variable.lastTrampledBlockType.remove(entity.getUniqueId());
        if (damager != null)
        {
          if (damager instanceof LivingEntity livingEntity)
          {
            Variable.attackerAndWeapon.remove(livingEntity.getUniqueId());
          }
        }
        // 모든 플레이어에게 데스메시지 보냄
        MessageUtil.broadcastPlayer(deathMessage);
        String x = Constant.Sosu2.format(location.getBlockX());
        String y = Constant.Sosu2.format(location.getBlockY());
        String z = Constant.Sosu2.format(location.getBlockZ());
        deathMessage = deathMessage.append(ComponentUtil.create("&7 - " + worldName + ", " + x + ", " + y + ", " + z));
        // 콘솔에 디버그를 보내기 위함
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
    Entity entity = event.getEntity();
    EntityDamageEvent entityDamageEvent = entity.getLastDamageCause();
    if (entityDamageEvent instanceof EntityDamageByEntityEvent damageEvent && damageEvent.getCause() != EntityDamageEvent.DamageCause.FALL)
    {
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
        if (tntPrimer != null && !entity.equals(tntPrimer))
        {
          return tntPrimer;
        }

        return damager;
      }

      return entity;
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
      if (damager instanceof AreaEffectCloud)
      {
        return (AreaEffectCloud) damager;
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
