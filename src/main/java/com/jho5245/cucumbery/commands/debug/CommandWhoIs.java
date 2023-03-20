package com.jho5245.cucumbery.commands.debug;

import com.destroystokyo.paper.event.server.AsyncTabCompleteEvent.Completion;
import com.jho5245.cucumbery.Cucumbery;
import com.jho5245.cucumbery.util.no_groups.*;
import com.jho5245.cucumbery.util.storage.component.util.ComponentUtil;
import com.jho5245.cucumbery.util.storage.component.util.ItemNameUtil;
import com.jho5245.cucumbery.util.storage.data.Constant;
import com.jho5245.cucumbery.util.storage.data.Permission;
import com.jho5245.cucumbery.util.storage.data.Prefix;
import com.jho5245.cucumbery.util.storage.data.TranslatableKeyParser;
import com.jho5245.cucumbery.util.storage.no_groups.CustomConfig.UserData;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import org.bukkit.*;
import org.bukkit.Statistic.Type;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.block.Biome;
import org.bukkit.block.Block;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.*;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.potion.PotionEffect;
import org.bukkit.projectiles.BlockProjectileSource;
import org.bukkit.projectiles.ProjectileSource;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class CommandWhoIs implements CucumberyCommandExecutor
{
  @Override
  public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args)
  {
    try
    {
      if (!Method.hasPermission(sender, Permission.CMD_WHOIS, true))
      {
        return true;
      }
      if (!MessageUtil.checkQuoteIsValidInArgs(sender, args = MessageUtil.wrapWithQuote(args)))
      {
        return !(sender instanceof BlockCommandSender);
      }
      String usage = cmd.getUsage().replace("/<command> ", "");
      if (args.length < 2)
      {
        if (args.length == 0)
        {
          if (sender instanceof Player)
          {
            Bukkit.getServer().dispatchCommand(sender, "cwhois " + sender.getName() + " state");
            return true;
          }
        }
        else
        {
          if (SelectorUtil.getPlayer(sender, args[0], false) == null)
          {
            Bukkit.getServer().dispatchCommand(sender, "cwhois " + args[0] + " offline");
          }
          else
          {
            Bukkit.getServer().dispatchCommand(sender, "cwhois " + args[0] + " state");
          }
          return true;
        }
        MessageUtil.shortArg(sender, 1, args);
        MessageUtil.commandInfo(sender, label, usage);
        return true;
      }
      else if (args.length == 2 ||
              (args[1].equals("stats") && args.length == 3) ||
              (args[1].equals("stats_general") && args.length == 3) ||
              (args[1].equals("stats_entity") && args.length <= 4) ||
              (args[1].equals("stats_material") && args.length <= 4))
      {
        if (!Method.equals(args[1], "state", "pos", "name", "effect", "stats", "stats_general", "stats_entity", "stats_material", "offline"))
        {
          MessageUtil.wrongArg(sender, 2, args);
          return true;
        }
        OfflinePlayer offlinePlayer = SelectorUtil.getOfflinePlayer(sender, args[0]);
        if (offlinePlayer == null)
        {
          return true;
        }
        Player player = offlinePlayer.getPlayer();
        if (player == null && !Method.equals(args[1], "name", "stats", "stats_general", "stats_entity", "stats_material", "offline"))
        {
          MessageUtil.wrongArg(sender, 2, args);
          MessageUtil.info(sender, "해당 정보 유형은 온라인 상태의 플레이어에게만 사용할 수 있습니다");
          return true;
        }
        if (args.length == 2 && args[1].equalsIgnoreCase("pos"))
        {
          if (player == null)
          {
            return true;
          }
          Location spawnPointLocation = player.getBedSpawnLocation();
          String spawnPointWorld = "";
          double spawnPointX = 0d;
          double spawnPointY = 0d;
          double spawnPointZ = 0d;
          boolean hasSpawnPoint = false;
          if (spawnPointLocation != null)
          {
            spawnPointWorld = spawnPointLocation.getWorld().getName();
            spawnPointX = spawnPointLocation.getBlockX();
            spawnPointY = spawnPointLocation.getBlockY();
            spawnPointZ = spawnPointLocation.getBlockZ();
            hasSpawnPoint = true;
          }
          double distance = -1D;
          Location location = player.getLocation();
          if (sender instanceof Player playerSender)
          {
            Location loc2 = playerSender.getLocation();
            if (location.getWorld().getName().equals(loc2.getWorld().getName()))
            {
              distance = location.distance(loc2);
            }
          }
          World world = location.getWorld();
          Biome biome = world.getBiome(location.getBlockX(), location.getBlockY(), location.getBlockZ());
          String distanceStr = Constant.Sosu4.format(distance);
          String locationWorld = world.getName();
          String locationX = Constant.Sosu4.format(location.getX());
          String locationY = Constant.Sosu4.format(location.getY());
          String locationZ = Constant.Sosu4.format(location.getZ());
          String locationYaw = Constant.Sosu4.format(location.getYaw());
          String locationPitch = Constant.Sosu4.format(location.getPitch());
          String locationBiome = biome.toString();
          MessageUtil.sendMessage(sender, Prefix.INFO_WHOIS, Constant.separatorSubString(3));
          MessageUtil.sendMessage(sender, Prefix.INFO_WHOIS, "%s의 정보", player);
          MessageUtil.sendMessage(sender, Prefix.INFO_WHOIS, "&2-스폰 포인트-");
          if (hasSpawnPoint)
          {
            MessageUtil.sendMessage(sender, Prefix.INFO_WHOIS, "월드 : rg255,204;" + spawnPointWorld);
            MessageUtil.sendMessage(sender, Prefix.INFO_WHOIS, "X : rg255,204;" + Constant.Jeongsu.format(spawnPointX));
            MessageUtil.sendMessage(sender, Prefix.INFO_WHOIS, "Y : rg255,204;" + Constant.Jeongsu.format(spawnPointY));
            MessageUtil.sendMessage(sender, Prefix.INFO_WHOIS, "Z : rg255,204;" + Constant.Jeongsu.format(spawnPointZ));
            if (sender instanceof Player p)
            {
              MessageUtil.sendMessage(sender, Prefix.INFO_WHOIS, "거리 : rg255,204;" + Constant.Sosu4.format(Method2.distance(spawnPointLocation, p.getLocation())) + "rg255,204;m");
            }
          }
          else
          {
            MessageUtil.sendMessage(sender, Prefix.INFO_WHOIS, "&c없음");
          }
          MessageUtil.sendMessage(sender, Prefix.INFO_WHOIS, "&2-현재 위치-");
          MessageUtil.sendMessage(sender, Prefix.INFO_WHOIS, "월드 : rg255,204;" + locationWorld);
          MessageUtil.sendMessage(sender, Prefix.INFO_WHOIS, "X : rg255,204;" + locationX);
          MessageUtil.sendMessage(sender, Prefix.INFO_WHOIS, "Y : rg255,204;" + locationY);
          MessageUtil.sendMessage(sender, Prefix.INFO_WHOIS, "Z : rg255,204;" + locationZ);
          MessageUtil.sendMessage(sender, Prefix.INFO_WHOIS, "Yaw : rg255,204;" + locationYaw);
          MessageUtil.sendMessage(sender, Prefix.INFO_WHOIS, "Pitch : rg255,204;" + locationPitch);
          MessageUtil.sendMessage(sender, Prefix.INFO_WHOIS, "거리 : rg255,204;" + distanceStr + "rg255,204;m");
          MessageUtil.sendMessage(sender, Prefix.INFO_WHOIS, "생물 군계 : %s(%s, %s)",
                  ComponentUtil.translate(biome.translationKey(), Constant.THE_COLOR),
                  Constant.THE_COLOR_HEX + biome,
                  Constant.THE_COLOR_HEX + biome.getKey());
          MessageUtil.sendMessage(sender, Prefix.INFO_WHOIS, Constant.separatorSubString(3));
        }
        else if (args.length == 2 && args[1].equalsIgnoreCase("name"))
        {
          String display = UserData.DISPLAY_NAME.getString(offlinePlayer);
          if (display == null)
          {
            display = offlinePlayer.getName();
          }
          if (display == null)
          {
            display = offlinePlayer.getUniqueId().toString();
          }
          String listName = UserData.PLAYER_LIST_NAME.getString(offlinePlayer);
          if (listName == null)
          {
            listName = offlinePlayer.getName();
          }
          if (listName == null)
          {
            listName = offlinePlayer.getUniqueId().toString();
          }
          @Nullable Component displayComponent = ComponentUtil.create(display), listComponent = ComponentUtil.create(listName);
          String playerDisplayName = ComponentUtil.serialize(displayComponent),
                  playerListName = ComponentUtil.serialize(listComponent);
          displayComponent = displayComponent.clickEvent(ClickEvent.copyToClipboard(playerDisplayName));
          if (displayComponent.hoverEvent() == null)
          {
            displayComponent = displayComponent.hoverEvent(ComponentUtil.translate("chat.copy.click"));
          }
          listComponent = listComponent.clickEvent(ClickEvent.copyToClipboard(playerListName));
          if (listComponent.hoverEvent() == null)
          {
            listComponent = listComponent.hoverEvent(ComponentUtil.translate("chat.copy.click"));
          }
          if (displayComponent.color() == null)
          {
            displayComponent = displayComponent.color(Constant.THE_COLOR);
          }
          if (listComponent.color() == null)
          {
            listComponent = listComponent.color(Constant.THE_COLOR);
          }
          if (Method.isUUID(playerDisplayName))
          {
            displayComponent = null;
          }
          if (Method.isUUID(playerListName))
          {
            listComponent = null;
          }
          MessageUtil.sendMessage(sender, Prefix.INFO_WHOIS, Constant.separatorSubString(3));
          MessageUtil.sendMessage(sender, Prefix.INFO_WHOIS, "%s의 이름 정보", offlinePlayer);
          MessageUtil.sendMessage(sender, Prefix.INFO_WHOIS, "UUID : %s", Constant.THE_COLOR_HEX + offlinePlayer.getUniqueId());
          MessageUtil.sendMessage(sender, Prefix.INFO_WHOIS, "닉네임 : %s", displayComponent != null ? displayComponent : ComponentUtil.translate("&c알 수 없음"));
          if (displayComponent != null)
          {
            MessageUtil.sendMessage(sender, Prefix.INFO_WHOIS, "닉네임(NBT) : %s", Component.text(display).hoverEvent(ComponentUtil.translate("chat.copy.click")).clickEvent(ClickEvent.copyToClipboard(display)));
          }
          MessageUtil.sendMessage(sender, Prefix.INFO_WHOIS, "목록 닉네임 : %s", listComponent != null ? listComponent : ComponentUtil.translate("&c알 수 없음"));
          if (listComponent != null)
          {
            MessageUtil.sendMessage(sender, Prefix.INFO_WHOIS, "목록 닉네임(NBT) : %s", Component.text(listName).hoverEvent(ComponentUtil.translate("chat.copy.click")).clickEvent(ClickEvent.copyToClipboard(listName)));
          }
          MessageUtil.sendMessage(sender, Prefix.INFO_WHOIS, Constant.separatorSubString(3));
        }
        else if (args[1].equalsIgnoreCase("state"))
        {
          if (player == null)
          {
            return true;
          }
          int entityID = player.getEntityId();
          float exp = player.getExp();
          int level = player.getLevel();
          int expToLevel = player.getExpToLevel();
          int totalExp = player.getTotalExperience();
          String fallDistance = Constant.Sosu4.format(player.getFallDistance());
          int fireTicks = player.getFireTicks();
          int foodLevel = player.getFoodLevel();
          String saturation = Constant.Sosu4.format(player.getSaturation());
          String exhaustion = Constant.Sosu4.format(4F - player.getExhaustion());
          GameMode gamemode = player.getGameMode();
          String gm = switch (gamemode)
                  {
                    case SPECTATOR -> "관전";
                    case ADVENTURE -> "모험";
                    case SURVIVAL -> "서바이벌";
                    case CREATIVE -> "크리에이티브";
                  };
          String healthPoint = Constant.Sosu4.format(player.getHealth());
          String maxHealthPoint = Method.attributeString(player, Attribute.GENERIC_MAX_HEALTH);
          String healthScale = Constant.Sosu4.format(player.getHealthScale());
          String lastDamage = Constant.Sosu4.format(player.getLastDamage());
          EntityDamageEvent lastDamageCause = player.getLastDamageCause();
          DamageCause damageCause = null;
          String causeString = null;
          if (lastDamageCause != null)
          {
            damageCause = lastDamageCause.getCause();
            causeString = damageCause.toString();
          }
          Entity damager; // 피해를 준 개체
          String damagerString = "§c없음";
          Entity damagerShooter; // 피해를 준 개체가 발사체면 그 발사체를 쏜 개체
          Player shooterPlayer; // 발사체를 쏜 개체가 플레이어
          boolean isProjectile = false;
          String shooterString = "§c발사체 주인 없음";
          if (lastDamageCause instanceof EntityDamageByEntityEvent)
          {
            damager = ((EntityDamageByEntityEvent) lastDamageCause).getDamager();
            isProjectile = damager instanceof Projectile;
            if (isProjectile)
            {
              ProjectileSource source = ((Projectile) damager).getShooter();
              if (source instanceof LivingEntity)
              {
                damagerShooter = (LivingEntity) ((Projectile) damager).getShooter();
                if (damagerShooter != null)
                {
                  shooterString = damagerShooter.getName();
                }
                if (damagerShooter instanceof Player)
                {
                  shooterPlayer = (Player) damagerShooter;
                  shooterString = shooterPlayer.getName();
                }
              }
              else if (source instanceof BlockProjectileSource blockSource)
              {
                Block block = blockSource.getBlock();
                shooterString = (ItemNameUtil.itemName(block.getType())).toString();
              }
            }
            damagerString = damager.toString();
          }
          int air = player.getMaximumAir();
          int noDmgTicksMax = player.getMaximumNoDamageTicks();
          int noDmgTicks = player.getNoDamageTicks();
          StringBuilder passangers = new StringBuilder("&7없음");
          player.getPassengers();
          for (Entity entity : player.getPassengers())
          {
            passangers.append("rg255,204;").append((entity)).append("&r, ");
          }
          passangers = new StringBuilder("&r개체 수 : rg255,204;" + player.getPassengers().size() + "&r개 : " + passangers.substring(0, passangers.length() - 2));
          if (player.getPassengers().isEmpty())
          {
            passangers = new StringBuilder("&7탑승 중인 개체들 없음");
          }
          WeatherType weatherType = player.getPlayerWeather();
          String weather = "rg255,204;없음";
          if (weatherType != null)
          {
            weather = switch (weatherType)
                    {
                      case DOWNFALL -> "맑음";
                      case CLEAR -> "비";
                    };
          }
          int portalCooldown = player.getPortalCooldown();
          int remainAir = player.getRemainingAir();
          int sleepTicks = player.getSleepTicks();
          Entity spectatorTarget = player.getSpectatorTarget();
          String targetType = "§e없음";
          if (spectatorTarget != null)
          {
            targetType = spectatorTarget.getName();
          }
          Entity vehicle = player.getVehicle();
          String vehicleType = "§e없음";
          if (vehicle != null)
          {
            vehicleType = vehicle.getName();
          }
          String speed = Constant.Sosu4.format(player.getWalkSpeed());
          boolean isDead = player.isDead();
          String dead = isDead ? "&4사망" : "&a생존";
          boolean fly = player.getAllowFlight();
          String flyStr = fly ? "&a비행 가능" : "&7비행 불가능";
          boolean isFlying = player.isFlying();
          boolean isGliding = player.isGliding();
          boolean isGlowing = player.isGlowing();
          String flying = isFlying ? "&a비행 중" : "&c비행 중이 아님";
          String gliding = isGliding ? "&a겉날개 활강 중" : "&c겉날개 활강 중이 아님";
          String glowing = isGlowing ? "&a발광 상태 효과 적용 중" : "&c발광 포션 상태 적용 중이 아님";
          String op = player.isOp() ? "&a관리자(오피)" : "&7관리자 아님(오피 아님)";
          boolean isSleeping = player.isSleeping();
          boolean isSneaking = player.isSneaking();
          boolean isSprinting = player.isSprinting();
          String sleep = isSleeping ? "&a침대에서 자고 있음" : "&c침대에서 자고 있지 않음";
          String sneak = isSneaking ? "&c웅크리고 있음" : "&a웅크리고 있지 않음";
          String sprint = isSprinting ? "&a달리기 중" : "&c달리고 있지 않음";
          double percentage = player.getHealth() / Objects.requireNonNull(player.getAttribute(Attribute.GENERIC_MAX_HEALTH)).getValue();
          if (percentage < 0.0D)
          {
            percentage = 0.0D;
          }
          if (percentage > 1.0D)
          {
            percentage = 1.0D;
          }
          MessageUtil.sendMessage(sender, Prefix.INFO_WHOIS, Constant.separatorSubString(3));
          MessageUtil.sendMessage(sender, Prefix.INFO_WHOIS, "%s의 정보", player);
          MessageUtil.sendMessage(sender, Prefix.INFO_WHOIS, "HP바 : &a" + healthScale);
          if (percentage < 0.05D)
          {
            MessageUtil.sendMessage(sender, Prefix.INFO_WHOIS, "HP : &4" + healthPoint + "&6 / &a" + maxHealthPoint);
          }
          else if (percentage < 0.3D)
          {
            MessageUtil.sendMessage(sender, Prefix.INFO_WHOIS, "HP : &c" + healthPoint + "&6 / &a" + maxHealthPoint);
          }
          else if (percentage < 0.5D)
          {
            MessageUtil.sendMessage(sender, Prefix.INFO_WHOIS, "HP : rg255,204;" + healthPoint + "&6 / &a" + maxHealthPoint);
          }
          else
          {
            MessageUtil.sendMessage(sender, Prefix.INFO_WHOIS, "HP : &a" + healthPoint + "&6 / &a" + maxHealthPoint);
          }
          Vector velocity = player.getVelocity();
          MessageUtil.sendMessage(sender, Prefix.INFO_WHOIS, "허기 : rg255,204;" + foodLevel);
          MessageUtil.sendMessage(sender, Prefix.INFO_WHOIS, "포화도 : rg255,204;" + saturation);
          MessageUtil.sendMessage(sender, Prefix.INFO_WHOIS, "영양 : rg255,204;" + exhaustion);
          MessageUtil.sendMessage(sender, Prefix.INFO_WHOIS, "레벨 : rg255,204;" + Constant.Jeongsu.format(level));
          MessageUtil.sendMessage(sender, Prefix.INFO_WHOIS,
                  "경험치 : rg255,204;" +
                          Constant.Jeongsu.format(exp * expToLevel) +
                          "&r / rg255,204;" +
                          Constant.Jeongsu.format(expToLevel) +
                          "&r (rg255,204;" +
                          Constant.Sosu4.format(player.getExp() * 100.0D) +
                          "%&r)");
          MessageUtil.sendMessage(sender, Prefix.INFO_WHOIS, "총 경험치 : rg255,204;" + Constant.Jeongsu.format(totalExp));
          MessageUtil.sendMessage(sender, Prefix.INFO_WHOIS, "게임 모드 : rg255,204;" + gm + " 모드");
          MessageUtil.sendMessage(sender, Prefix.INFO_WHOIS, "플라이 상태 : rg255,204;" + flyStr + "&r, rg255,204;" + flying);
          MessageUtil.sendMessage(sender, Prefix.INFO_WHOIS, "관리자 상태 : rg255,204;" + op);
          MessageUtil.sendMessage(sender, Prefix.INFO_WHOIS, "생존 여부 : rg255,204;" + dead);
          MessageUtil.sendMessage(sender, Prefix.INFO_WHOIS, gliding);
          MessageUtil.sendMessage(sender, Prefix.INFO_WHOIS, glowing);
          MessageUtil.sendMessage(sender, Prefix.INFO_WHOIS, sneak);
          MessageUtil.sendMessage(sender, Prefix.INFO_WHOIS, sprint);
          MessageUtil.sendMessage(sender, Prefix.INFO_WHOIS, sleep);
          MessageUtil.sendMessage(sender, Prefix.INFO_WHOIS, "날씨 : rg255,204;" + weather);
          MessageUtil.sendMessage(sender, Prefix.INFO_WHOIS, "개체 아이디 : rg255,204;" + entityID);
          MessageUtil.sendMessage(sender, Prefix.INFO_WHOIS, "산소 : rg255,204;" + Constant.Jeongsu.format(remainAir) + "&r / rg255,204;" + Constant.Jeongsu.format(air));
          MessageUtil.sendMessage(sender, Prefix.INFO_WHOIS, "낙사 중 거리 : rg255,204;" + fallDistance + "m");
          MessageUtil.sendMessage(sender, Prefix.INFO_WHOIS, "불 타고 있는 시간 : rg255,204;" + Constant.Jeongsu.format(fireTicks));
          MessageUtil.sendMessage(sender, Prefix.INFO_WHOIS, "가장 최근에 받은 피해량 : rg255,204;" + lastDamage);
          if (damageCause != null)
          {
            MessageUtil.sendMessage(sender, Prefix.INFO_WHOIS, "가장 최근에 받은 피해 원인 : rg255,204;" + causeString);
          }
          if (isProjectile)
          {
            MessageUtil.sendMessage(sender, Prefix.INFO_WHOIS, "가장 최근에 받은 피해 제공자 : §e" + shooterString, false);
            MessageUtil.sendMessage(sender, Prefix.INFO_WHOIS, "가장 최근에 받은 피해 제공 발사체 : §e" + damagerString, false);
          }
          else
          {
            MessageUtil.sendMessage(sender, Prefix.INFO_WHOIS, "가장 최근에 받은 피해 제공자 : §e" + damagerString, false);
          }
          MessageUtil.sendMessage(sender, Prefix.INFO_WHOIS, "무적 시간 : rg255,204;" + Constant.Jeongsu.format(noDmgTicks));
          MessageUtil.sendMessage(sender, Prefix.INFO_WHOIS, "최대 무적 시간 : rg255,204;" + Constant.Jeongsu.format(noDmgTicksMax));
          MessageUtil.sendMessage(sender, Prefix.INFO_WHOIS, "걷는 속도 : rg255,204;" + speed);
          MessageUtil.sendMessage(sender, Prefix.INFO_WHOIS, "비행 속도 : rg255,204;" + Constant.Sosu4.format(player.getFlySpeed()));
          MessageUtil.sendMessage(sender, Prefix.INFO_WHOIS, "움직이는 속도 : rg255,204;" + Constant.Sosu4.format(velocity.getX()) + ", " + Constant.Sosu4.format(velocity.getY()) + ", " + Constant.Sosu4.format(velocity.getZ()));
          MessageUtil.sendMessage(sender, Prefix.INFO_WHOIS, "자신이 탑승 중인 개체 : rg255,204;" + vehicleType);
          MessageUtil.sendMessage(sender, Prefix.INFO_WHOIS, "-자신을 탑승 하고 있는 개체-");
          MessageUtil.sendMessage(sender, Prefix.INFO_WHOIS, passangers.toString());
          MessageUtil.sendMessage(sender, Prefix.INFO_WHOIS, "포탈 이동 시간 : rg255,204;" + portalCooldown);
          MessageUtil.sendMessage(sender, Prefix.INFO_WHOIS, "관전 중인 개체 : §e" + targetType, false);
          MessageUtil.sendMessage(sender, Prefix.INFO_WHOIS, "잠 자고 있는 시간 : rg255,204;" + Constant.Jeongsu.format(sleepTicks));
          if (Cucumbery.using_Vault_Economy)
          {
            String world = player.getLocation().getWorld().getName();
            MessageUtil.sendMessage(sender, Prefix.INFO_WHOIS, "소지 금액 : rg255,204;" + Constant.Sosu2.format(Cucumbery.eco.getBalance(player, world)) + "원&r (rg255,204;" + world + "&r 월드 기준)");
          }
          MessageUtil.sendMessage(sender, Prefix.INFO_WHOIS, "---------Attribute---------");
          for (Attribute attrs : Attribute.values())
          {
            AttributeInstance attr = player.getAttribute(attrs);
            if (attr == null || attrs == Attribute.GENERIC_MAX_HEALTH)
            {
              continue;
            }
            MessageUtil.sendMessage(sender, Prefix.INFO_WHOIS, ComponentUtil.translate(attrs.translationKey()), " : rg255,204;" + Method.attributeString(player, attrs));
          }
          MessageUtil.sendMessage(sender, Prefix.INFO_WHOIS, Constant.separatorSubString(3));
        }
        else if (args.length <= 3 && args[1].equalsIgnoreCase("stats"))
        {
          int page = 1;
          if (args.length == 3)
          {
            try
            {
              page = Integer.parseInt(args[2]);
            }
            catch (Exception e)
            {
              MessageUtil.noArg(sender, Prefix.ONLY_INTEGER, args[2]);
              return true;
            }
          }
          if (!MessageUtil.checkNumberSize(sender, page, 1, 4))
          {
            return true;
          }
          MessageUtil.sendMessage(sender, Prefix.INFO_WHOIS, Constant.separatorSubString(3));
          MessageUtil.sendMessage(sender, Prefix.INFO_WHOIS, "%s의 통계 정보 (%s 페이지)", offlinePlayer, Constant.THE_COLOR_HEX + page);
          switch (page)
          {
            case 1:
              int leaveGame = offlinePlayer.getStatistic(Statistic.LEAVE_GAME);
              int playOneTick = offlinePlayer.getStatistic(Statistic.PLAY_ONE_MINUTE);
              int timeSinceDeath = offlinePlayer.getStatistic(Statistic.TIME_SINCE_DEATH);
              int sneakTime = offlinePlayer.getStatistic(Statistic.SNEAK_TIME);
              int walkOneCm = offlinePlayer.getStatistic(Statistic.WALK_ONE_CM);
              int crouchOneCm = offlinePlayer.getStatistic(Statistic.CROUCH_ONE_CM);
              int sprintOneCm = offlinePlayer.getStatistic(Statistic.SPRINT_ONE_CM);
              int swimOneCm = offlinePlayer.getStatistic(Statistic.SWIM_ONE_CM);
              int fallOneCm = offlinePlayer.getStatistic(Statistic.FALL_ONE_CM);
              int climbOneCm = offlinePlayer.getStatistic(Statistic.CLIMB_ONE_CM);
              int flyOneCm = offlinePlayer.getStatistic(Statistic.FLY_ONE_CM);
              int minecartOneCm = offlinePlayer.getStatistic(Statistic.MINECART_ONE_CM);
              int boatOneCm = offlinePlayer.getStatistic(Statistic.BOAT_ONE_CM);
              int pigOneCm = offlinePlayer.getStatistic(Statistic.PIG_ONE_CM);
              int horseOneCm = offlinePlayer.getStatistic(Statistic.HORSE_ONE_CM);
              int aviateOneCm = offlinePlayer.getStatistic(Statistic.AVIATE_ONE_CM);
              int jump = offlinePlayer.getStatistic(Statistic.JUMP);
              int damageDealt = offlinePlayer.getStatistic(Statistic.DAMAGE_DEALT);
              int damageTaken = offlinePlayer.getStatistic(Statistic.DAMAGE_TAKEN);
              int deaths = offlinePlayer.getStatistic(Statistic.DEATHS);
              int mobKills = offlinePlayer.getStatistic(Statistic.MOB_KILLS);
              int animalsBred = offlinePlayer.getStatistic(Statistic.ANIMALS_BRED);
              int playerKills = offlinePlayer.getStatistic(Statistic.PLAYER_KILLS);
              MessageUtil.sendMessage(sender, Prefix.INFO_WHOIS, "종료 횟수 : rg255,204;" + leaveGame);
              MessageUtil.sendMessage(sender, Prefix.INFO_WHOIS, "플레이 시간 : %s", MessageUtil.periodRealTimeAndGameTime(playOneTick));
              MessageUtil.sendMessage(sender, Prefix.INFO_WHOIS, "마지막 죽음 이후 지난 시간 : %s", MessageUtil.periodRealTimeAndGameTime(timeSinceDeath));
              MessageUtil.sendMessage(sender, Prefix.INFO_WHOIS, "웅크린 시간 : %s", MessageUtil.periodRealTimeAndGameTime(sneakTime));
              MessageUtil.sendMessage(sender, Prefix.INFO_WHOIS, "걸어간 거리 : rg255,204;" + Constant.Sosu2.format(walkOneCm / 100.0D) + "m");
              MessageUtil.sendMessage(sender, Prefix.INFO_WHOIS, "웅크리고 걸은 거리 : rg255,204;" + Constant.Sosu2.format(crouchOneCm / 100.0D) + "m");
              MessageUtil.sendMessage(sender, Prefix.INFO_WHOIS, "달려간 거리 : rg255,204;" + Constant.Sosu2.format(sprintOneCm / 100.0D) + "m");
              MessageUtil.sendMessage(sender, Prefix.INFO_WHOIS, "수영한 거리 : rg255,204;" + Constant.Sosu2.format(swimOneCm / 100.0D) + "m");
              MessageUtil.sendMessage(sender, Prefix.INFO_WHOIS, "떨어진 거리 : rg255,204;" + Constant.Sosu2.format(fallOneCm / 100.0D) + "m");
              MessageUtil.sendMessage(sender, Prefix.INFO_WHOIS, "올라간 거리 : rg255,204;" + Constant.Sosu2.format(climbOneCm / 100.0D) + "m");
              MessageUtil.sendMessage(sender, Prefix.INFO_WHOIS, "날아간 거리 : rg255,204;" + Constant.Sosu2.format(flyOneCm / 100.0D) + "m");
              MessageUtil.sendMessage(sender, Prefix.INFO_WHOIS, "광산 수레를 타고 이동한 거리 : rg255,204;" + Constant.Sosu2.format(minecartOneCm / 100.0D) + "m");
              MessageUtil.sendMessage(sender, Prefix.INFO_WHOIS, "보트를 타고 이동한 거리 : rg255,204;" + Constant.Sosu2.format(boatOneCm / 100.0D) + "m");
              MessageUtil.sendMessage(sender, Prefix.INFO_WHOIS, "돼지를 타고 이동한 거리 : rg255,204;" + Constant.Sosu2.format(pigOneCm / 100.0D) + "m");
              MessageUtil.sendMessage(sender, Prefix.INFO_WHOIS, "말을 타고 이동한 거리 : rg255,204;" + Constant.Sosu2.format(horseOneCm / 100.0D) + "m");
              MessageUtil.sendMessage(sender, Prefix.INFO_WHOIS, "겉날개로 날아간 거리 : rg255,204;" + Constant.Sosu2.format(aviateOneCm / 100.0D) + "m");
              MessageUtil.sendMessage(sender, Prefix.INFO_WHOIS, "점프 횟수 : rg255,204;" + jump);
              MessageUtil.sendMessage(sender, Prefix.INFO_WHOIS, "입힌 피해 : rg255,204;" + Constant.Sosu2.format(damageDealt / 10.0D));
              MessageUtil.sendMessage(sender, Prefix.INFO_WHOIS, "받은 피해 : rg255,204;" + Constant.Sosu2.format(damageTaken / 10.0D));
              MessageUtil.sendMessage(sender, Prefix.INFO_WHOIS, "사망 횟수 : rg255,204;" + deaths);
              MessageUtil.sendMessage(sender, Prefix.INFO_WHOIS, "몹 사냥 횟수 : rg255,204;" + mobKills);
              MessageUtil.sendMessage(sender, Prefix.INFO_WHOIS, "동물을 교배한 횟수 : rg255,204;" + animalsBred);
              MessageUtil.sendMessage(sender, Prefix.INFO_WHOIS, "플레이어를 죽인 횟수 : rg255,204;" + playerKills);
              break;
            case 2:
              int fishCaught = offlinePlayer.getStatistic(Statistic.FISH_CAUGHT);
              int talkedToVillager = offlinePlayer.getStatistic(Statistic.TALKED_TO_VILLAGER);
              int tradedWithVillager = offlinePlayer.getStatistic(Statistic.TRADED_WITH_VILLAGER);
              int cakeSlicesEaten = offlinePlayer.getStatistic(Statistic.CAKE_SLICES_EATEN);
              int cauldronFilled = offlinePlayer.getStatistic(Statistic.CAULDRON_FILLED);
              int cauldronUsed = offlinePlayer.getStatistic(Statistic.CAULDRON_USED);
              int armorCleaned = offlinePlayer.getStatistic(Statistic.ARMOR_CLEANED);
              int bannerCleaned = offlinePlayer.getStatistic(Statistic.BANNER_CLEANED);
              int brewingstandInteraction = offlinePlayer.getStatistic(Statistic.BREWINGSTAND_INTERACTION);
              int beaconInteraction = offlinePlayer.getStatistic(Statistic.BEACON_INTERACTION);
              int dropperInspected = offlinePlayer.getStatistic(Statistic.DROPPER_INSPECTED);
              int hopperInspected = offlinePlayer.getStatistic(Statistic.HOPPER_INSPECTED);
              int dispenserInspected = offlinePlayer.getStatistic(Statistic.DISPENSER_INSPECTED);
              int noteblockPlayed = offlinePlayer.getStatistic(Statistic.NOTEBLOCK_PLAYED);
              int noteblockTuned = offlinePlayer.getStatistic(Statistic.NOTEBLOCK_TUNED);
              int flowerPotted = offlinePlayer.getStatistic(Statistic.FLOWER_POTTED);
              int trappedChestTriggered = offlinePlayer.getStatistic(Statistic.TRAPPED_CHEST_TRIGGERED);
              int enderchestOpened = offlinePlayer.getStatistic(Statistic.ENDERCHEST_OPENED);
              int itemEnchanted = offlinePlayer.getStatistic(Statistic.ITEM_ENCHANTED);
              int recordPlayed = offlinePlayer.getStatistic(Statistic.RECORD_PLAYED);
              int furnaceInteraction = offlinePlayer.getStatistic(Statistic.FURNACE_INTERACTION);
              int craftingTableInteraction = offlinePlayer.getStatistic(Statistic.CRAFTING_TABLE_INTERACTION);
              int chestOpened = offlinePlayer.getStatistic(Statistic.CHEST_OPENED);
              int sleepInBed = offlinePlayer.getStatistic(Statistic.SLEEP_IN_BED);
              int shulkerBoxOpened = offlinePlayer.getStatistic(Statistic.SHULKER_BOX_OPENED);
              MessageUtil.sendMessage(sender, Prefix.INFO_WHOIS, "물고기를 잡은 횟수 : rg255,204;" + fishCaught);
              MessageUtil.sendMessage(sender, Prefix.INFO_WHOIS, "주민과 대화한 횟수 : rg255,204;" + talkedToVillager);
              MessageUtil.sendMessage(sender, Prefix.INFO_WHOIS, "주민과 거래한 횟수 : rg255,204;" + tradedWithVillager);
              MessageUtil.sendMessage(sender, Prefix.INFO_WHOIS, "먹은 케이크 조각 개수 : rg255,204;" + cakeSlicesEaten);
              MessageUtil.sendMessage(sender, Prefix.INFO_WHOIS, "가마솥을 채운 횟수 : rg255,204;" + cauldronFilled);
              MessageUtil.sendMessage(sender, Prefix.INFO_WHOIS, "가마솥에서 물을 뜬 횟수 : rg255,204;" + cauldronUsed);
              MessageUtil.sendMessage(sender, Prefix.INFO_WHOIS, "갑옷을 닦은 횟수 : rg255,204;" + armorCleaned);
              MessageUtil.sendMessage(sender, Prefix.INFO_WHOIS, "현수막을 씻은 횟수 : rg255,204;" + bannerCleaned);
              MessageUtil.sendMessage(sender, Prefix.INFO_WHOIS, "양조기 사용 횟수 : rg255,204;" + brewingstandInteraction);
              MessageUtil.sendMessage(sender, Prefix.INFO_WHOIS, "신호기 사용 횟수 : rg255,204;" + beaconInteraction);
              MessageUtil.sendMessage(sender, Prefix.INFO_WHOIS, "공급기를 들여다본 횟수 : rg255,204;" + dropperInspected);
              MessageUtil.sendMessage(sender, Prefix.INFO_WHOIS, "깔때기를 들여다본 횟수 : rg255,204;" + hopperInspected);
              MessageUtil.sendMessage(sender, Prefix.INFO_WHOIS, "발사기를 들여다본 횟수 : rg255,204;" + dispenserInspected);
              MessageUtil.sendMessage(sender, Prefix.INFO_WHOIS, "소리 블록 연주 횟수 : rg255,204;" + noteblockPlayed);
              MessageUtil.sendMessage(sender, Prefix.INFO_WHOIS, "소리 블록 조율 횟수 : rg255,204;" + noteblockTuned);
              MessageUtil.sendMessage(sender, Prefix.INFO_WHOIS, "식물을 화분에 심은 횟수 : rg255,204;" + flowerPotted);
              MessageUtil.sendMessage(sender, Prefix.INFO_WHOIS, "작동시킨 덫 상자 횟수 : rg255,204;" + trappedChestTriggered);
              MessageUtil.sendMessage(sender, Prefix.INFO_WHOIS, "엔더 상자를 열어본 횟수 : rg255,204;" + enderchestOpened);
              MessageUtil.sendMessage(sender, Prefix.INFO_WHOIS, "아이템에 마법을 부여한 횟수 : rg255,204;" + itemEnchanted);
              MessageUtil.sendMessage(sender, Prefix.INFO_WHOIS, "음반을 재생한 횟수 : rg255,204;" + recordPlayed);
              MessageUtil.sendMessage(sender, Prefix.INFO_WHOIS, "화로 사용 횟수 : rg255,204;" + furnaceInteraction);
              MessageUtil.sendMessage(sender, Prefix.INFO_WHOIS, "제작대 사용 횟수 : rg255,204;" + craftingTableInteraction);
              MessageUtil.sendMessage(sender, Prefix.INFO_WHOIS, "상자를 열어본 횟수 : rg255,204;" + chestOpened);
              MessageUtil.sendMessage(sender, Prefix.INFO_WHOIS, "침대에서 잔 횟수 : rg255,204;" + sleepInBed);
              MessageUtil.sendMessage(sender, Prefix.INFO_WHOIS, "셜커 상자를 연 횟수 : rg255,204;" + shulkerBoxOpened);
              break;
            case 3:
              for (EntityType entity : EntityType.values())
              {
                if (!entity.isAlive())
                {
                  continue;
                }
                try
                {
                  MessageUtil.sendMessage(sender, Prefix.INFO_WHOIS, "%s을(를) %s번 죽임",
                          ComponentUtil.create(entity).color(Constant.THE_COLOR), Constant.THE_COLOR_HEX + offlinePlayer.getStatistic(Statistic.KILL_ENTITY, entity));
                }
                catch (IllegalArgumentException | NullPointerException ignored)
                {
                }
              }
              break;
            case 4:
              for (EntityType entity : EntityType.values())
              {
                if (!entity.isAlive())
                {
                  continue;
                }
                try
                {
                  MessageUtil.sendMessage(sender, Prefix.INFO_WHOIS, Constant.THE_COLOR_HEX + (entity) + "&r에게 rg255,204;" + offlinePlayer.getStatistic(Statistic.ENTITY_KILLED_BY, entity) + "rg255,204;번 &r죽음");
                }
                catch (IllegalArgumentException | NullPointerException ignored)
                {
                }
              }
          }
          MessageUtil.sendMessage(sender, Prefix.INFO_WHOIS, Constant.separatorSubString(3));
        }
        else if (args.length == 3 && args[1].equals("stats_general"))
        {
          Statistic stats;
          try
          {
            stats = Statistic.valueOf(args[2].toUpperCase());
          }
          catch (Exception e)
          {
            MessageUtil.noArg(sender, Prefix.ARGS_WRONG, args[2]);
            return true;
          }
          switch (stats)
          {            default -> {
            Component key = ComponentUtil.translate(TranslatableKeyParser.getKey(stats));
            String statisticName = ComponentUtil.serialize(key);
            double value = offlinePlayer.getStatistic(stats);
            String valueString = Constant.THE_COLOR_HEX;
            String suffix = "";
            if (statisticName.endsWith("시간"))
            {
              valueString += Method.timeFormatMilli((long) (value * 50));
            }
            else
            {
              if (stats.toString().endsWith("CM"))
              {
                value /= 100d;
                suffix = "m";
              }
              else if (statisticName.endsWith("개수"))
              {
                suffix = "개";
              }
              else if (statisticName.endsWith("횟수"))
              {
                suffix = "회";
              }
              valueString += Constant.Sosu2.format(value);
            }
            MessageUtil.sendMessage(sender, Prefix.INFO_WHOIS, Constant.separatorSubString(3));
            MessageUtil.sendMessage(sender, Prefix.INFO_WHOIS, ComponentUtil.translate("%s의 %s은(는) %s입니다", offlinePlayer, key.color(Constant.THE_COLOR),
                            ComponentUtil.translate("%s" + suffix, valueString)));
          }
            case BREAK_ITEM, CRAFT_ITEM, DROP, MINE_BLOCK, PICKUP, USE_ITEM -> MessageUtil.sendError(sender, "해당 값은 rg255,204;/whois <플레이어 ID> stats_material <통계> <아이템 이름>&r 명령어를 사용해주세요.");
            case ENTITY_KILLED_BY, KILL_ENTITY -> MessageUtil.sendError(sender, "해당 값은 rg255,204;/whois <플레이어 ID> stats_entity <통계> <몹 이름>&r 명령어를 사용해주세요.");
          }
        }
        else if (args.length >= 3 && args[1].equals("stats_material"))
        {
          if (args.length == 3)
          {
            MessageUtil.shortArg(sender, 4, args);
            MessageUtil.sendMessage(sender, Prefix.INFO, "/" + label + " stats_material <통계> <아이템 이름>");
            return true;
          }
          Statistic stats;
          try
          {
            stats = Statistic.valueOf(args[2].toUpperCase());
          }
          catch (Exception e)
          {
            MessageUtil.noArg(sender, Prefix.ARGS_WRONG, args[2]);
            return true;
          }
          Material type;
          try
          {
            type = Material.valueOf(args[3].toUpperCase());
          }
          catch (Exception e)
          {
            MessageUtil.noArg(sender, Prefix.ARGS_WRONG, args[3]);
            return true;
          }
          switch (stats)
          {
            default -> MessageUtil.sendError(sender, "해당 값은 rg255,204;/whois <플레이어 ID> stats_general <통계>&r 명령어를 사용해주세요.");
            case BREAK_ITEM, CRAFT_ITEM, DROP, MINE_BLOCK, PICKUP, USE_ITEM -> {
              Component key = ComponentUtil.translate(TranslatableKeyParser.getKey(stats));
              int value = offlinePlayer.getStatistic(stats, type);
              MessageUtil.sendMessage(sender, Prefix.INFO_WHOIS, Constant.separatorSubString(3));
              MessageUtil.sendMessage(
                      sender, Prefix.INFO_WHOIS, "%s의 %s %s은(는) %s입니다", offlinePlayer, type, key,
                              ComponentUtil.translate("%s회", Constant.THE_COLOR_HEX + Constant.Sosu2.format(value)));
            }
            case ENTITY_KILLED_BY, KILL_ENTITY -> MessageUtil.sendError(sender, "해당 값은 rg255,204;/whois <플레이어 ID> stats_entity <통계> <몹 이름>&r 명령어를 사용해주세요.");
          }
        }
        else if (args.length >= 3 && args[1].equals("stats_entity"))
        {
          if (args.length == 3)
          {
            MessageUtil.shortArg(sender, 4, args);
            MessageUtil.sendMessage(sender, Prefix.INFO, "/" + label + " stats_entity <통계> <몹 이름>");
            return true;
          }
          Statistic stats;
          try
          {
            stats = Statistic.valueOf(args[2].toUpperCase());
          }
          catch (Exception e)
          {
            MessageUtil.noArg(sender, Prefix.ARGS_WRONG, args[2]);
            return true;
          }
          EntityType type;
          try
          {
            type = EntityType.valueOf(args[3].toUpperCase());
          }
          catch (Exception e)
          {
            MessageUtil.noArg(sender, Prefix.ARGS_WRONG, args[3]);
            return true;
          }
          switch (stats)
          {
            default -> MessageUtil.sendError(sender, "해당 값은 rg255,204;/whois <플레이어 ID> stats_general <통계>&r 명령어를 사용해주세요.");
            case BREAK_ITEM, CRAFT_ITEM, DROP, MINE_BLOCK, PICKUP, USE_ITEM -> MessageUtil.sendError(sender, "해당 값은 rg255,204;/whois <플레이어 ID> stats_material <통계> <아이템 이름>&r 명령어를 사용해주세요.");
            case ENTITY_KILLED_BY, KILL_ENTITY -> {
              int value = offlinePlayer.getStatistic(stats, type);
              MessageUtil.sendMessage(sender, Prefix.INFO_WHOIS, Constant.separatorSubString(3));
              if (stats == Statistic.KILL_ENTITY)
              {
                MessageUtil.sendMessage(
                        sender, Prefix.INFO_WHOIS, "%s은(는) %s", offlinePlayer,
                                ComponentUtil.translate(TranslatableKeyParser.getKey(stats),
                                        Constant.THE_COLOR_HEX + Constant.Sosu2.format(value), ComponentUtil.translate(type.translationKey()).color(Constant.THE_COLOR)));
              }
              else
              {

                MessageUtil.sendMessage(
                        sender, Prefix.INFO_WHOIS, ComponentUtil.translate("%s은(는) %s", offlinePlayer,
                                ComponentUtil.translate(TranslatableKeyParser.getKey(stats),
                                        ComponentUtil.translate(type.translationKey()).color(Constant.THE_COLOR), Constant.THE_COLOR_HEX + Constant.Sosu2.format(value))));
              }
            }
          }
        }
        else if (args.length == 2 && args[1].equalsIgnoreCase("effect"))
        {
          if (player == null)
          {
            return true;
          }
          MessageUtil.sendMessage(sender, Prefix.INFO_WHOIS, Constant.separatorSubString(3));
          MessageUtil.sendMessage(sender, Prefix.INFO_WHOIS, ComponentUtil.translate("%s의 상태 효과 정보", player));
          List<PotionEffect> potionEffects = new ArrayList<>(player.getActivePotionEffects());
          if (potionEffects.isEmpty())
          {
            MessageUtil.sendMessage(sender, Prefix.INFO_WHOIS, ComponentUtil.translate("&c&o상태 효과 없음"));
          }
          else
          {
            Component message = Component.empty();
            for (int i = 0; i < potionEffects.size(); i++)
            {
              PotionEffect potionEffect = potionEffects.get(i);
              message = message.append(ComponentUtil.create(potionEffect).append(ComponentUtil.translate("(%s)", potionEffect.getAmplifier() + 1)));
              if (i + 1 < potionEffects.size())
              {
                message = message.append(ComponentUtil.translate("&7, "));
              }
            }
            MessageUtil.sendMessage(sender, Prefix.INFO_WHOIS, message);
          }
          MessageUtil.sendMessage(sender, Prefix.INFO_WHOIS, Constant.separatorSubString(3));
        }
        else if (args.length == 2 && args[1].equals("offline"))
        {
          MessageUtil.sendMessage(sender, Prefix.INFO_WHOIS, Constant.separatorSubString(3));
          MessageUtil.sendMessage(sender, Prefix.INFO_WHOIS, "%s의 정보", offlinePlayer);
          MessageUtil.sendMessage(sender, Prefix.INFO_WHOIS, "접속 상태 : " + (offlinePlayer.isOnline() ? "&a온라인" : "&c오프라인"));
          if (Bukkit.getServer().hasWhitelist())
          {
            MessageUtil.sendMessage(sender, Prefix.INFO_WHOIS, "화이트리스트 상태 : " + (offlinePlayer.isWhitelisted() ? "&a화이트리스트에 등록됨" : "&c화이트리스트에 등록되지 않음"));
          }
          MessageUtil.sendMessage(sender, Prefix.INFO_WHOIS, "차단 상태 : " + (offlinePlayer.isBanned() ? "&4차단당함" : "&a차단당하지 않음"));
          MessageUtil.sendMessage(sender, Prefix.INFO_WHOIS, "관리자 상태 : rg255,204;" + (offlinePlayer.isOp() ? "&a관리자(오피)" : "&7관리자 아님(오피 아님)"));
          long firstPlayed = offlinePlayer.getFirstPlayed();
          if (!offlinePlayer.hasPlayedBefore() && firstPlayed == 0)
          {
            MessageUtil.sendMessage(sender, Prefix.INFO_WHOIS, "rg255,204;한 번도 접속한 기록이 없음");
          }
          else
          {
            MessageUtil.sendMessage(sender, Prefix.INFO_WHOIS, "접속 횟수 : rg255,204;" + ((offlinePlayer.isOnline() ? 1 : 0) + offlinePlayer.getStatistic(Statistic.LEAVE_GAME)));
          }
          long lastLogin = offlinePlayer.getLastLogin();
          long lastSeen = offlinePlayer.getLastSeen();
          long current = System.currentTimeMillis();
          Calendar calendar = Calendar.getInstance();
          int timeDiffer = Cucumbery.config.getInt("adjust-time-difference-value");
          if (firstPlayed != 0)
          {
            calendar.setTimeInMillis(firstPlayed);
            calendar.add(Calendar.HOUR, timeDiffer);
            MessageUtil.sendMessage(sender, Prefix.INFO_WHOIS, "최초 접속 시각 : rg255,204;"
                    + Method.getCurrentTime(calendar, true, false) + " (" + Method.timeFormatMilli(current - firstPlayed, false) + " 전)");
          }
          if (lastLogin != 0)
          {
            calendar.setTimeInMillis(lastLogin);
            calendar.add(Calendar.HOUR, timeDiffer);
            MessageUtil.sendMessage(sender, Prefix.INFO_WHOIS, "마지막으로 접속한 시각 : rg255,204;" +
                    Method.getCurrentTime(calendar, true, false) + " (" + Method.timeFormatMilli(current - lastLogin, false) + " 전)");
          }
          if (lastSeen != 0 && !offlinePlayer.isOnline())
          {
            calendar.setTimeInMillis(lastSeen);
            calendar.add(Calendar.HOUR, timeDiffer);
            MessageUtil.sendMessage(sender, Prefix.INFO_WHOIS, "마지막으로 퇴장한 시각 : rg255,204;" +
                    Method.getCurrentTime(calendar, true, false) + " (" + Method.timeFormatMilli(current - lastSeen, false) + " 전)");
          }
          if (Cucumbery.using_Vault_Economy)
          {
            MessageUtil.sendMessage(sender, Prefix.INFO_WHOIS, "소지 금액 : rg255,204;" + Constant.Sosu2.format(Cucumbery.eco.getBalance(offlinePlayer)) + "원");
          }
          Location spawnPointLocation = offlinePlayer.getBedSpawnLocation();
          MessageUtil.sendMessage(sender, Prefix.INFO_WHOIS, "&2-스폰 포인트-");
          if (spawnPointLocation != null)
          {
            String spawnPointWorld = spawnPointLocation.getWorld().getName();
            double spawnPointX = spawnPointLocation.getBlockX();
            double spawnPointY = spawnPointLocation.getBlockY();
            double spawnPointZ = spawnPointLocation.getBlockZ();
            MessageUtil.sendMessage(sender, Prefix.INFO_WHOIS, "월드 : rg255,204;" + spawnPointWorld);
            MessageUtil.sendMessage(sender, Prefix.INFO_WHOIS, "X : rg255,204;" + Constant.Jeongsu.format(spawnPointX));
            MessageUtil.sendMessage(sender, Prefix.INFO_WHOIS, "Y : rg255,204;" + Constant.Jeongsu.format(spawnPointY));
            MessageUtil.sendMessage(sender, Prefix.INFO_WHOIS, "Z : rg255,204;" + Constant.Jeongsu.format(spawnPointZ));
            MessageUtil.sendMessage(sender, Prefix.INFO_WHOIS, "거리 : rg255,204;" + (sender instanceof Player player1 ? Constant.Sosu4.format(Method2.distance(spawnPointLocation, player1.getLocation())) +
                    "m" : "-1m"));
          }
          else
          {
            MessageUtil.sendMessage(sender, Prefix.INFO_WHOIS, "&c없음");
          }
          MessageUtil.sendMessage(sender, Prefix.INFO_WHOIS, Constant.separatorSubString(3));
        }
        else
        {
          MessageUtil.sendError(sender, "명령어에 잘못된 인수가 있거나 불완전한 명령어입니다");
          MessageUtil.commandInfo(sender, label, usage);
          return true;
        }
      }
      else
      {
        MessageUtil.longArg(sender, 2, args);
        MessageUtil.commandInfo(sender, label, usage);
        return true;
      }
      return true;
    }
    catch (Exception ignored)
    {

    }
    return true;
  }

  @Override
  public @NotNull List<Completion> completion(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args, @NotNull Location location)
  {
    int length = args.length;
    if (length == 1)
    {
      if (label.equals("whois"))
      {
        return CommandTabUtil.tabCompleterPlayer(sender, args, "<플레이어>");
      }
      return CommandTabUtil.tabCompleterOfflinePlayer(sender, args, "<플레이어>");
    }
    else if (length == 2)
    {
      Player player = Method.getPlayer(sender, args[0], false);
      OfflinePlayer offlinePlayer = null;
      if (Method.isUUID(args[0]))
      {
        offlinePlayer = Bukkit.getOfflinePlayer(UUID.fromString(args[0]));
      }
      else if (!args[0].equals(""))
      {
        offlinePlayer = Bukkit.getOfflinePlayerIfCached(args[0]);
      }
      if (args[0].equals("") || (player == null && (offlinePlayer == null || !offlinePlayer.isOnline())))
      {
        if (Method.equals(args[1], "pos", "state", "effect"))
        {
          return CommandTabUtil.errorMessage("해당 정보 유형은 온라인 상태의 플레이어에게만 사용할 수 있습니다");
        }
        return CommandTabUtil.tabCompleterList(args, "[정보 유형]", false,
                Completion.completion("name", ComponentUtil.translate("닉네임")),
                Completion.completion("stats", ComponentUtil.translate("전체적인 통계")),
                Completion.completion("stats_general", ComponentUtil.translate("일반 통계")),
                Completion.completion("stats_entity", ComponentUtil.translate("개체 유형의 통계")),
                Completion.completion("stats_material", ComponentUtil.translate("아이템 유형의 통계")),
                Completion.completion("offline", ComponentUtil.translate("접속 통계")));
      }
      boolean hasPotionEffects = player != null && !player.getActivePotionEffects().isEmpty();
      return CommandTabUtil.tabCompleterList(args, "[정보 유형]", false,
              Completion.completion("state", ComponentUtil.translate("정보")),
              Completion.completion("pos", ComponentUtil.translate("위치 및 스폰 포인트 위치")),
              Completion.completion(Constant.TAB_COMPLETER_QUOTE_ESCAPE + "effect" + (hasPotionEffects ? "" : "(적용 중인 표과 없음)"), ComponentUtil.translate("적용 중인 효과")),
              Completion.completion("name", ComponentUtil.translate("닉네임")),
              Completion.completion("stats", ComponentUtil.translate("전체적인 통계")),
              Completion.completion("stats_general", ComponentUtil.translate("일반 통계")),
              Completion.completion("stats_entity", ComponentUtil.translate("개체 유형의 통계")),
              Completion.completion("stats_material", ComponentUtil.translate("아이템 유형의 통계")),
              Completion.completion("offline", ComponentUtil.translate("접속 통계")));
    }
    else if (length == 3)
    {
      switch (args[1])
      {
        case "stats" -> {
          return CommandTabUtil.tabCompleterIntegerRadius(args, 1, 4, "[페이지]");
        }
        case "stats_general" -> {
          return CommandTabUtil.tabCompleterList(args, Statistic.values(), "<일반 통계>", statistic -> statistic.getType() != Type.UNTYPED);
        }
        case "stats_entity" -> {
          return CommandTabUtil.tabCompleterList(args, Statistic.values(), "<개체 통계>", statistic -> statistic.getType() != Type.ENTITY);
        }
        case "stats_material" -> {
          return CommandTabUtil.tabCompleterList(args, Statistic.values(), "<아이템 통계>", statistic -> statistic.getType() != Type.ITEM && statistic.getType() != Type.BLOCK);
        }
      }
    }
    else if (length == 4)
    {
      switch (args[1])
      {
        case "stats_entity" -> {
          return CommandTabUtil.tabCompleterList(args, EntityType.values(), "<개체 유형>", entityType -> !entityType.isAlive());
        }
        case "stats_material" -> {
          switch (args[2])
          {
            case "mine_block" -> {
              return CommandTabUtil.tabCompleterList(args, Material.values(), "<블록>", material -> !material.isBlock());
            }
            case "break_item" -> {
              return CommandTabUtil.tabCompleterList(args, Material.values(), "<내구도가 있는 아이템>", material -> material.getMaxDurability() == 0);
            }
            case "use_item", "drop", "pickup", "craft_item" -> {
              return CommandTabUtil.tabCompleterList(args, Material.values(), "<아이템>", material -> !material.isItem() || material.isAir());
            }
          }
        }
      }
    }
    return CommandTabUtil.ARGS_LONG;
  }
}
