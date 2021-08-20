package com.jho5245.cucumbery.commands.debug;

import com.jho5245.cucumbery.Cucumbery;
import com.jho5245.cucumbery.util.MessageUtil;
import com.jho5245.cucumbery.util.MessageUtil.ConsonantType;
import com.jho5245.cucumbery.util.Method;
import com.jho5245.cucumbery.util.storage.ComponentUtil;
import com.jho5245.cucumbery.util.storage.CustomConfig;
import com.jho5245.cucumbery.util.storage.data.Constant;
import com.jho5245.cucumbery.util.storage.data.Permission;
import com.jho5245.cucumbery.util.storage.data.Prefix;
import net.kyori.adventure.text.event.ClickEvent;
import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.block.Biome;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.*;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.projectiles.BlockProjectileSource;
import org.bukkit.projectiles.ProjectileSource;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

import java.util.Calendar;
import java.util.Collection;
import java.util.Objects;

public class WHOIS implements CommandExecutor
{
  public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args)
  {
    if (!Method.hasPermission(sender, Permission.CMD_WHOIS, true))
    {
      return true;
    }
    String usage = cmd.getUsage().replace("/<command> ", "");
    if (args.length < 2)
    {
      if (args.length == 0)
      {
        if (sender instanceof Player)
        {
          Bukkit.getServer().dispatchCommand(sender, "whois " + sender.getName() + " state");
          return true;
        }
      }
      else
      {
        if (Method.getPlayer(sender, args[0], false) == null)
        {
          Bukkit.getServer().dispatchCommand(sender, "whois " + args[0] + " offline");
        }
        else
        {
          Bukkit.getServer().dispatchCommand(sender, "whois " + args[0] + " state");
        }
        return true;
      }
      MessageUtil.shortArg(sender, 1, args);
      MessageUtil.commandInfo(sender, label, usage);
      return true;
    }
    else if (args.length <= 2 ||
            (args[1].equals("stats") && args.length <= 3) ||
            (args[1].equals("stats_general") && args.length <= 3) ||
            (args[1].equals("stats_entity") && args.length <= 4) ||
            (args[1].equals("stats_material") && args.length <= 4))
    {
      if (!Method.equals(args[1], "state", "pos", "name", "effect", "stats", "stats_general", "stats_entity", "stats_material", "offline"))
      {
        MessageUtil.wrongArg(sender, 2, args);
        return true;
      }
      OfflinePlayer offlinePlayer = Method.getOfflinePlayer(sender, args[0]);
      Player player;
      if (offlinePlayer == null)
      {
        return true;
      }
      player = offlinePlayer.getPlayer();
      if (player == null && !Method.equals(args[1], "name", "stats", "stats_general", "stats_entity", "stats_material", "offline"))
      {
        MessageUtil.wrongArg(sender, 2, args);
        MessageUtil.info(sender, "해당 정보 유형은 온라인 상태의 플레이어에게만 사용할 수 있습니다.");
        return true;
      }
      String playerName = Method.getName(offlinePlayer);
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
        // Block block = location.getBlock();
        // double temperature = block.getTemperature(), humidity = block.getHumidity();
        if (sender instanceof Player)
        {
          Player playerSender = (Player) sender;
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
        MessageUtil.sendMessage(sender, Prefix.INFO_WHOIS, "-------------------------------------------------");
        MessageUtil.sendMessage(sender, Prefix.INFO_WHOIS, "&e" + playerName + "&6의 정보");
        MessageUtil.sendMessage(sender, Prefix.INFO_WHOIS, "&2-스폰 포인트-");
        if (hasSpawnPoint)
        {
          MessageUtil.sendMessage(sender, Prefix.INFO_WHOIS, "월드 : &e" + spawnPointWorld);
          MessageUtil.sendMessage(sender, Prefix.INFO_WHOIS, "X : &e" + Constant.Jeongsu.format(spawnPointX));
          MessageUtil.sendMessage(sender, Prefix.INFO_WHOIS, "Y : &e" + Constant.Jeongsu.format(spawnPointY));
          MessageUtil.sendMessage(sender, Prefix.INFO_WHOIS, "Z : &e" + Constant.Jeongsu.format(spawnPointZ));
          if (sender instanceof Player)
          {
            MessageUtil.sendMessage(sender, Prefix.INFO_WHOIS, "거리 : &e" + Constant.Sosu4.format(spawnPointLocation.distance(((Player) sender).getLocation())) + "&em");
          }
        }
        else
        {
          MessageUtil.sendMessage(sender, Prefix.INFO_WHOIS, "&c없음");
        }
        MessageUtil.sendMessage(sender, Prefix.INFO_WHOIS, "&2-현재 위치-");
        MessageUtil.sendMessage(sender, Prefix.INFO_WHOIS, "월드 : &e" + locationWorld);
        MessageUtil.sendMessage(sender, Prefix.INFO_WHOIS, "X : &e" + locationX);
        MessageUtil.sendMessage(sender, Prefix.INFO_WHOIS, "Y : &e" + locationY);
        MessageUtil.sendMessage(sender, Prefix.INFO_WHOIS, "Z : &e" + locationZ);
        MessageUtil.sendMessage(sender, Prefix.INFO_WHOIS, "Yaw : &e" + locationYaw);
        MessageUtil.sendMessage(sender, Prefix.INFO_WHOIS, "Pitch : &e" + locationPitch);
        MessageUtil.sendMessage(sender, Prefix.INFO_WHOIS, "거리 : &e" + distanceStr + "&em");
        MessageUtil.sendMessage(sender, Prefix.INFO_WHOIS, "생물 군계 : &e" + locationBiome + "&e(&r" + biome.toString() + "&e)");
        MessageUtil.sendMessage(sender, Prefix.INFO_WHOIS, "-------------------------------------------------");
      }
      else if (args.length == 2 && args[1].equalsIgnoreCase("name"))
      {
        String playerDisplayName = CustomConfig.UserData.DISPLAY_NAME.getString(offlinePlayer.getUniqueId()), playerListName = CustomConfig.UserData.PLAYER_LIST_NAME.getString(offlinePlayer.getUniqueId());
        if (playerDisplayName == null)
        {
          playerDisplayName = Method.getName(offlinePlayer);
        }
        if (Method.isUUID(playerDisplayName))
        {
          playerDisplayName = "알 수 없음";
        }
        if (playerListName == null)
        {
          playerListName = Method.getName(offlinePlayer);
        }
        if (Method.isUUID(playerListName))
        {
          playerListName = "알 수 없음";
        }
        MessageUtil.sendMessage(sender, Prefix.INFO_WHOIS, "-------------------------------------------------");
        MessageUtil.sendMessage(sender, Prefix.INFO_WHOIS, "&e" + playerName + "&6의 이름 정보");
        MessageUtil.sendMessage(sender, Prefix.INFO_WHOIS, "UUID : &e" + offlinePlayer.getUniqueId().toString());

        MessageUtil.sendMessage(sender, Prefix.INFO_WHOIS, "닉네임 : &e" + playerDisplayName);
        MessageUtil.sendMessage(sender, ComponentUtil.create(MessageUtil.n2s(Prefix.INFO_WHOIS + "닉네임(색깔 없음) : &e") + playerDisplayName.replace("§", "&"), "클릭하여 클립보드에 복사", ClickEvent.Action.COPY_TO_CLIPBOARD,
                        playerDisplayName.replace("§", "&")));
        MessageUtil.sendMessage(sender, Prefix.INFO_WHOIS, "목록 닉네임 : &e" + playerListName);
        MessageUtil.sendMessage(sender, ComponentUtil.create(MessageUtil.n2s(Prefix.INFO_WHOIS + "목록 닉네임(색깔 없음) : &e") + playerListName.replace("§", "&"), "클릭하여 클립보드에 복사", ClickEvent.Action.COPY_TO_CLIPBOARD,
                        playerListName.replace("§", "&")));
        MessageUtil.sendMessage(sender, Prefix.INFO_WHOIS, "-------------------------------------------------");
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
              shooterString = damagerShooter.getName();
              if (damagerShooter instanceof Player)
              {
                shooterPlayer = (Player) damagerShooter;
                shooterString = shooterPlayer.getName();
              }
            }
            else if (source instanceof BlockProjectileSource)
            {
              BlockProjectileSource blockSource = (BlockProjectileSource) source;
              Block block = blockSource.getBlock();
              shooterString = (ComponentUtil.itemName(block.getType())).toString();
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
          passangers.append("&e").append((entity)).append("&r, ");
        }
        passangers = new StringBuilder("&r개체 수 : &e" + player.getPassengers().size() + "&r개 : " + passangers.substring(0, passangers.length() - 2));
        if (player.getPassengers().size() == 0)
        {
          passangers = new StringBuilder("&7탑승 중인 개체들 없음");
        }
        WeatherType weatherType = player.getPlayerWeather();
        String weather = "&e없음";
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
//        player.isSilent();
        boolean isSleeping = player.isSleeping();
        boolean isSneaking = player.isSneaking();
        boolean isSprinting = player.isSprinting();
        String sleep = isSleeping ? "&a침대에서 자고 있음" : "&c침대에서 자고 있지 않음";
        String sneak = isSneaking ? "&c웅크리고 있음" : "&a웅크리고 있지 않음";
        String sprint = isSprinting ? "&a달리기 중" : "&c달리고 있지 않음";
//        player.isWhitelisted();
//        player.getCompassTarget();
        double percentage = player.getHealth() / Objects.requireNonNull(player.getAttribute(Attribute.GENERIC_MAX_HEALTH)).getValue();
        if (percentage < 0.0D)
        {
          percentage = 0.0D;
        }
        if (percentage > 1.0D)
        {
          percentage = 1.0D;
        }
        MessageUtil.sendMessage(sender, Prefix.INFO_WHOIS, "-------------------------------------------------");
        MessageUtil.sendMessage(sender, Prefix.INFO_WHOIS, "&e" + playerName + "&6의 정보");
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
          MessageUtil.sendMessage(sender, Prefix.INFO_WHOIS, "HP : &e" + healthPoint + "&6 / &a" + maxHealthPoint);
        }
        else
        {
          MessageUtil.sendMessage(sender, Prefix.INFO_WHOIS, "HP : &a" + healthPoint + "&6 / &a" + maxHealthPoint);
        }
        Vector velocity = player.getVelocity();
        MessageUtil.sendMessage(sender, Prefix.INFO_WHOIS, "허기 : &e" + foodLevel);
        MessageUtil.sendMessage(sender, Prefix.INFO_WHOIS, "포화도 : &e" + saturation);
        MessageUtil.sendMessage(sender, Prefix.INFO_WHOIS, "영양 : &e" + exhaustion);
        MessageUtil.sendMessage(sender, Prefix.INFO_WHOIS, "레벨 : &e" + Constant.Jeongsu.format(level));
        MessageUtil.sendMessage(sender, Prefix.INFO_WHOIS,
                        "경험치 : &e" +
                        Constant.Jeongsu.format(exp * expToLevel) +
                        "&r / &e" +
                        Constant.Jeongsu.format(expToLevel) +
                        "&r (&e" +
                        Constant.Sosu4.format(player.getExp() * 100.0D) +
                        "%&r)");
        MessageUtil.sendMessage(sender, Prefix.INFO_WHOIS, "총 경험치 : &e" + Constant.Jeongsu.format(totalExp));
        MessageUtil.sendMessage(sender, Prefix.INFO_WHOIS, "게임 모드 : &e" + gm + " 모드");
        MessageUtil.sendMessage(sender, Prefix.INFO_WHOIS, "플라이 상태 : &e" + flyStr + "&r, &e" + flying);
        MessageUtil.sendMessage(sender, Prefix.INFO_WHOIS, "관리자 상태 : &e" + op);
        MessageUtil.sendMessage(sender, Prefix.INFO_WHOIS, "생존 여부 : &e" + dead);
        MessageUtil.sendMessage(sender, Prefix.INFO_WHOIS, gliding);
        MessageUtil.sendMessage(sender, Prefix.INFO_WHOIS, glowing);
        MessageUtil.sendMessage(sender, Prefix.INFO_WHOIS, sneak);
        MessageUtil.sendMessage(sender, Prefix.INFO_WHOIS, sprint);
        MessageUtil.sendMessage(sender, Prefix.INFO_WHOIS, sleep);
        MessageUtil.sendMessage(sender, Prefix.INFO_WHOIS, "날씨 : &e" + weather);
        MessageUtil.sendMessage(sender, Prefix.INFO_WHOIS, "개체 아이디 : &e" + entityID);
        MessageUtil.sendMessage(sender, Prefix.INFO_WHOIS, "산소 : &e" + Constant.Jeongsu.format(remainAir) + "&r / &e" + Constant.Jeongsu.format(air));
        MessageUtil.sendMessage(sender, Prefix.INFO_WHOIS, "낙사 중 거리 : &e" + fallDistance + "m");
        MessageUtil.sendMessage(sender, Prefix.INFO_WHOIS, "불 타고 있는 시간 : &e" + Constant.Jeongsu.format(fireTicks));
        MessageUtil.sendMessage(sender, Prefix.INFO_WHOIS, "가장 최근에 받은 피해량 : &e" + lastDamage);
        if (damageCause != null)
        {
          MessageUtil.sendMessage(sender, Prefix.INFO_WHOIS, "가장 최근에 받은 피해 원인 : &e" + causeString);
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
        MessageUtil.sendMessage(sender, Prefix.INFO_WHOIS, "무적 시간 : &e" + Constant.Jeongsu.format(noDmgTicks));
        MessageUtil.sendMessage(sender, Prefix.INFO_WHOIS, "최대 무적 시간 : &e" + Constant.Jeongsu.format(noDmgTicksMax));
        MessageUtil.sendMessage(sender, Prefix.INFO_WHOIS, "걷는 속도 : &e" + speed);
        MessageUtil.sendMessage(sender, Prefix.INFO_WHOIS, "비행 속도 : &e" + Constant.Sosu4.format(player.getFlySpeed()));
        MessageUtil.sendMessage(sender, Prefix.INFO_WHOIS, "움직이는 속도 : &e" + Constant.Sosu4.format(velocity.getX()) + ", " + Constant.Sosu4.format(velocity.getY()) + ", " + Constant.Sosu4.format(velocity.getZ()));
        MessageUtil.sendMessage(sender, Prefix.INFO_WHOIS, "자신이 탑승 중인 개체 : &e" + vehicleType);
        MessageUtil.sendMessage(sender, Prefix.INFO_WHOIS, "-자신을 탑승 하고 있는 개체-");
        MessageUtil.sendMessage(sender, Prefix.INFO_WHOIS, passangers.toString());
        MessageUtil.sendMessage(sender, Prefix.INFO_WHOIS, "포탈 이동 시간 : &e" + portalCooldown);
        MessageUtil.sendMessage(sender, Prefix.INFO_WHOIS, "관전 중인 개체 : §e" + targetType, false);
        MessageUtil.sendMessage(sender, Prefix.INFO_WHOIS, "잠 자고 있는 시간 : &e" + Constant.Jeongsu.format(sleepTicks));
        if (Cucumbery.using_Vault)
        {
          String world = player.getLocation().getWorld().getName();
          MessageUtil.sendMessage(sender, Prefix.INFO_WHOIS, "소지 금액 : &e" + Constant.Sosu2.format(Cucumbery.eco.getBalance(player, world)) + "원&r (&e" + world + "&r 월드 기준)");
        }
        MessageUtil.sendMessage(sender, Prefix.INFO_WHOIS, "---------Attribute---------");
        for (Attribute attrs : Attribute.values())
        {
          AttributeInstance attr = player.getAttribute(attrs);
          if (attr == null || attrs == Attribute.GENERIC_MAX_HEALTH)
          {
            continue;
          }
          MessageUtil.sendMessage(sender, Prefix.INFO_WHOIS, attrs.toString() + " : &e" + Method.attributeString(player, attrs));
        }
        MessageUtil.sendMessage(sender, Prefix.INFO_WHOIS, "-------------------------------------------------");
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
        MessageUtil.sendMessage(sender, Prefix.INFO_WHOIS, "-------------------------------------------------");
        MessageUtil.sendMessage(sender, Prefix.INFO_WHOIS, "&e" + playerName + "&r의 통계 정보 (&e" + page + "&r 페이지)");
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
            MessageUtil.sendMessage(sender, Prefix.INFO_WHOIS, "종료 횟수 : &e" + leaveGame);
            MessageUtil.sendMessage(sender, Prefix.INFO_WHOIS, "플레이 시간 : &e" + MessageUtil.periodRealTimeAndGameTime(playOneTick));
            MessageUtil.sendMessage(sender, Prefix.INFO_WHOIS, "마지막 죽음 이후 지난 시간 : &e" + MessageUtil.periodRealTimeAndGameTime(timeSinceDeath));
            MessageUtil.sendMessage(sender, Prefix.INFO_WHOIS, "웅크린 시간 : &e" + MessageUtil.periodRealTimeAndGameTime(sneakTime));
            MessageUtil.sendMessage(sender, Prefix.INFO_WHOIS, "걸어간 거리 : &e" + Constant.Sosu2.format(walkOneCm / 100.0D) + "m");
            MessageUtil.sendMessage(sender, Prefix.INFO_WHOIS, "웅크리고 걸은 거리 : &e" + Constant.Sosu2.format(crouchOneCm / 100.0D) + "m");
            MessageUtil.sendMessage(sender, Prefix.INFO_WHOIS, "달려간 거리 : &e" + Constant.Sosu2.format(sprintOneCm / 100.0D) + "m");
            MessageUtil.sendMessage(sender, Prefix.INFO_WHOIS, "수영한 거리 : &e" + Constant.Sosu2.format(swimOneCm / 100.0D) + "m");
            MessageUtil.sendMessage(sender, Prefix.INFO_WHOIS, "떨어진 거리 : &e" + Constant.Sosu2.format(fallOneCm / 100.0D) + "m");
            MessageUtil.sendMessage(sender, Prefix.INFO_WHOIS, "올라간 거리 : &e" + Constant.Sosu2.format(climbOneCm / 100.0D) + "m");
            MessageUtil.sendMessage(sender, Prefix.INFO_WHOIS, "날아간 거리 : &e" + Constant.Sosu2.format(flyOneCm / 100.0D) + "m");
            MessageUtil.sendMessage(sender, Prefix.INFO_WHOIS, "광산 수레를 타고 이동한 거리 : &e" + Constant.Sosu2.format(minecartOneCm / 100.0D) + "m");
            MessageUtil.sendMessage(sender, Prefix.INFO_WHOIS, "보트를 타고 이동한 거리 : &e" + Constant.Sosu2.format(boatOneCm / 100.0D) + "m");
            MessageUtil.sendMessage(sender, Prefix.INFO_WHOIS, "돼지를 타고 이동한 거리 : &e" + Constant.Sosu2.format(pigOneCm / 100.0D) + "m");
            MessageUtil.sendMessage(sender, Prefix.INFO_WHOIS, "말을 타고 이동한 거리 : &e" + Constant.Sosu2.format(horseOneCm / 100.0D) + "m");
            MessageUtil.sendMessage(sender, Prefix.INFO_WHOIS, "겉날개로 날아간 거리 : &e" + Constant.Sosu2.format(aviateOneCm / 100.0D) + "m");
            MessageUtil.sendMessage(sender, Prefix.INFO_WHOIS, "점프 횟수 : &e" + jump);
            MessageUtil.sendMessage(sender, Prefix.INFO_WHOIS, "입힌 피해 : &e" + Constant.Sosu2.format(damageDealt / 10.0D));
            MessageUtil.sendMessage(sender, Prefix.INFO_WHOIS, "받은 피해 : &e" + Constant.Sosu2.format(damageTaken / 10.0D));
            MessageUtil.sendMessage(sender, Prefix.INFO_WHOIS, "사망 횟수 : &e" + deaths);
            MessageUtil.sendMessage(sender, Prefix.INFO_WHOIS, "몹 사냥 횟수 : &e" + mobKills);
            MessageUtil.sendMessage(sender, Prefix.INFO_WHOIS, "동물을 교배한 횟수 : &e" + animalsBred);
            MessageUtil.sendMessage(sender, Prefix.INFO_WHOIS, "플레이어를 죽인 횟수 : &e" + playerKills);
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
            MessageUtil.sendMessage(sender, Prefix.INFO_WHOIS, "물고기를 잡은 횟수 : &e" + fishCaught);
            MessageUtil.sendMessage(sender, Prefix.INFO_WHOIS, "주민과 대화한 횟수 : &e" + talkedToVillager);
            MessageUtil.sendMessage(sender, Prefix.INFO_WHOIS, "주민과 거래한 횟수 : &e" + tradedWithVillager);
            MessageUtil.sendMessage(sender, Prefix.INFO_WHOIS, "먹은 케이크 조각 개수 : &e" + cakeSlicesEaten);
            MessageUtil.sendMessage(sender, Prefix.INFO_WHOIS, "가마솥을 채운 횟수 : &e" + cauldronFilled);
            MessageUtil.sendMessage(sender, Prefix.INFO_WHOIS, "가마솥에서 물을 뜬 횟수 : &e" + cauldronUsed);
            MessageUtil.sendMessage(sender, Prefix.INFO_WHOIS, "갑옷을 닦은 횟수 : &e" + armorCleaned);
            MessageUtil.sendMessage(sender, Prefix.INFO_WHOIS, "현수막을 씻은 횟수 : &e" + bannerCleaned);
            MessageUtil.sendMessage(sender, Prefix.INFO_WHOIS, "양조기 사용 횟수 : &e" + brewingstandInteraction);
            MessageUtil.sendMessage(sender, Prefix.INFO_WHOIS, "신호기 사용 횟수 : &e" + beaconInteraction);
            MessageUtil.sendMessage(sender, Prefix.INFO_WHOIS, "공급기를 들여다본 횟수 : &e" + dropperInspected);
            MessageUtil.sendMessage(sender, Prefix.INFO_WHOIS, "깔때기를 들여다본 횟수 : &e" + hopperInspected);
            MessageUtil.sendMessage(sender, Prefix.INFO_WHOIS, "발사기를 들여다본 횟수 : &e" + dispenserInspected);
            MessageUtil.sendMessage(sender, Prefix.INFO_WHOIS, "소리 블록 연주 횟수 : &e" + noteblockPlayed);
            MessageUtil.sendMessage(sender, Prefix.INFO_WHOIS, "소리 블록 조율 횟수 : &e" + noteblockTuned);
            MessageUtil.sendMessage(sender, Prefix.INFO_WHOIS, "식물을 화분에 심은 횟수 : &e" + flowerPotted);
            MessageUtil.sendMessage(sender, Prefix.INFO_WHOIS, "작동시킨 덫 상자 횟수 : &e" + trappedChestTriggered);
            MessageUtil.sendMessage(sender, Prefix.INFO_WHOIS, "엔더 상자를 열어본 횟수 : &e" + enderchestOpened);
            MessageUtil.sendMessage(sender, Prefix.INFO_WHOIS, "아이템에 마법을 부여한 횟수 : &e" + itemEnchanted);
            MessageUtil.sendMessage(sender, Prefix.INFO_WHOIS, "음반을 재생한 횟수 : &e" + recordPlayed);
            MessageUtil.sendMessage(sender, Prefix.INFO_WHOIS, "화로 사용 횟수 : &e" + furnaceInteraction);
            MessageUtil.sendMessage(sender, Prefix.INFO_WHOIS, "제작대 사용 횟수 : &e" + craftingTableInteraction);
            MessageUtil.sendMessage(sender, Prefix.INFO_WHOIS, "상자를 열어본 횟수 : &e" + chestOpened);
            MessageUtil.sendMessage(sender, Prefix.INFO_WHOIS, "침대에서 잔 횟수 : &e" + sleepInBed);
            MessageUtil.sendMessage(sender, Prefix.INFO_WHOIS, "셜커 상자를 연 횟수 : &e" + shulkerBoxOpened);
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
                MessageUtil.sendMessage(
                        sender, Prefix.INFO_WHOIS +
                                "&e" +
                                (entity) +
                                "&r" +
                                MessageUtil.getFinalConsonant((entity.name()), MessageUtil.ConsonantType.을_를) +
                                " &e" +
                                offlinePlayer.getStatistic(Statistic.KILL_ENTITY, entity) +
                                "&e번 &r죽임");
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
                MessageUtil.sendMessage(sender, Prefix.INFO_WHOIS, "&e" + (entity) + "&r에게 &e" + offlinePlayer.getStatistic(Statistic.ENTITY_KILLED_BY, entity) + "&e번 &r죽음");
              }
              catch (IllegalArgumentException | NullPointerException ignored)
              {
              }
            }
        }
        MessageUtil.sendMessage(sender, Prefix.INFO_WHOIS, "-------------------------------------------------");
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
        {
          case ANIMALS_BRED:
          case ARMOR_CLEANED:
          case AVIATE_ONE_CM:
          case BANNER_CLEANED:
          case BEACON_INTERACTION:
          case BELL_RING:
          case BOAT_ONE_CM:
          case BREWINGSTAND_INTERACTION:
          case CAKE_SLICES_EATEN:
          case CAULDRON_FILLED:
          case CAULDRON_USED:
          case CHEST_OPENED:
          case CLEAN_SHULKER_BOX:
          case CLIMB_ONE_CM:
          case CRAFTING_TABLE_INTERACTION:
          case CROUCH_ONE_CM:
          case DAMAGE_ABSORBED:
          case DAMAGE_BLOCKED_BY_SHIELD:
          case DAMAGE_DEALT:
          case DAMAGE_DEALT_ABSORBED:
          case DAMAGE_DEALT_RESISTED:
          case DAMAGE_RESISTED:
          case DAMAGE_TAKEN:
          case DEATHS:
          case DISPENSER_INSPECTED:
          case DROPPER_INSPECTED:
          case DROP_COUNT:
          case ENDERCHEST_OPENED:
          case FALL_ONE_CM:
          case FISH_CAUGHT:
          case FLOWER_POTTED:
          case FLY_ONE_CM:
          case FURNACE_INTERACTION:
          case HOPPER_INSPECTED:
          case HORSE_ONE_CM:
          case INTERACT_WITH_ANVIL:
          case INTERACT_WITH_BLAST_FURNACE:
          case INTERACT_WITH_CAMPFIRE:
          case INTERACT_WITH_CARTOGRAPHY_TABLE:
          case INTERACT_WITH_GRINDSTONE:
          case INTERACT_WITH_LECTERN:
          case INTERACT_WITH_LOOM:
          case INTERACT_WITH_SMOKER:
          case INTERACT_WITH_STONECUTTER:
          case ITEM_ENCHANTED:
          case JUMP:
          case LEAVE_GAME:
          case MINECART_ONE_CM:
          case MOB_KILLS:
          case NOTEBLOCK_PLAYED:
          case NOTEBLOCK_TUNED:
          case OPEN_BARREL:
          case PIG_ONE_CM:
          case PLAYER_KILLS:
          case PLAY_ONE_MINUTE:
          case RAID_TRIGGER:
          case RAID_WIN:
          case RECORD_PLAYED:
          case SHULKER_BOX_OPENED:
          case SLEEP_IN_BED:
          case SNEAK_TIME:
          case SPRINT_ONE_CM:
          case SWIM_ONE_CM:
          case TALKED_TO_VILLAGER:
          case TIME_SINCE_DEATH:
          case TIME_SINCE_REST:
          case TRADED_WITH_VILLAGER:
          case TRAPPED_CHEST_TRIGGERED:
          case WALK_ONE_CM:
          case WALK_ON_WATER_ONE_CM:
          case WALK_UNDER_WATER_ONE_CM:
          case INTERACT_WITH_SMITHING_TABLE:
          case STRIDER_ONE_CM:
          case TARGET_HIT:
            String statisticName = stats.toString();
            MessageUtil.sendMessage(sender, Prefix.INFO_WHOIS, "-------------------------------------------------");
            MessageUtil.sendMessage(
                    sender, Prefix.INFO_WHOIS +
                            "&e" +
                            playerName +
                            "&r이(가) &e" +
                            statisticName +
                            "&r" +
                            MessageUtil.getFinalConsonant(statisticName, ConsonantType.은_는) +
                            " &e" +
                            offlinePlayer.getStatistic(stats) +
                            "&r" +
                            (statisticName.endsWith("개수") ? "개" : (statisticName.endsWith("횟수") ? "회" : "")) +
                            "입니다.");
            break;
          default:
          case BREAK_ITEM:
          case CRAFT_ITEM:
          case DROP:
          case MINE_BLOCK:
          case PICKUP:
          case USE_ITEM:
            MessageUtil.sendError(sender, "해당 값은 &e/whois <플레이어 ID> stats_material <통계> <아이템 이름>&r 명령어를 사용해주세요.");
            break;
          case ENTITY_KILLED_BY:
          case KILL_ENTITY:
            MessageUtil.sendError(sender, "해당 값은 &e/whois <플레이어 ID> stats_entity <통계> <몹 이름>&r 명령어를 사용해주세요.");
            break;
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
          case ANIMALS_BRED:
          case ARMOR_CLEANED:
          case AVIATE_ONE_CM:
          case BANNER_CLEANED:
          case BEACON_INTERACTION:
          case BELL_RING:
          case BOAT_ONE_CM:
          case BREWINGSTAND_INTERACTION:
          case CAKE_SLICES_EATEN:
          case CAULDRON_FILLED:
          case CAULDRON_USED:
          case CHEST_OPENED:
          case CLEAN_SHULKER_BOX:
          case CLIMB_ONE_CM:
          case CRAFTING_TABLE_INTERACTION:
          case CROUCH_ONE_CM:
          case DAMAGE_ABSORBED:
          case DAMAGE_BLOCKED_BY_SHIELD:
          case DAMAGE_DEALT:
          case DAMAGE_DEALT_ABSORBED:
          case DAMAGE_DEALT_RESISTED:
          case DAMAGE_RESISTED:
          case DAMAGE_TAKEN:
          case DEATHS:
          case DISPENSER_INSPECTED:
          case DROPPER_INSPECTED:
          case DROP_COUNT:
          case ENDERCHEST_OPENED:
          case FALL_ONE_CM:
          case FISH_CAUGHT:
          case FLOWER_POTTED:
          case FLY_ONE_CM:
          case FURNACE_INTERACTION:
          case HOPPER_INSPECTED:
          case HORSE_ONE_CM:
          case INTERACT_WITH_ANVIL:
          case INTERACT_WITH_BLAST_FURNACE:
          case INTERACT_WITH_CAMPFIRE:
          case INTERACT_WITH_CARTOGRAPHY_TABLE:
          case INTERACT_WITH_GRINDSTONE:
          case INTERACT_WITH_LECTERN:
          case INTERACT_WITH_LOOM:
          case INTERACT_WITH_SMOKER:
          case INTERACT_WITH_STONECUTTER:
          case ITEM_ENCHANTED:
          case JUMP:
          case LEAVE_GAME:
          case MINECART_ONE_CM:
          case MOB_KILLS:
          case NOTEBLOCK_PLAYED:
          case NOTEBLOCK_TUNED:
          case OPEN_BARREL:
          case PIG_ONE_CM:
          case PLAYER_KILLS:
          case PLAY_ONE_MINUTE:
          case RAID_TRIGGER:
          case RAID_WIN:
          case RECORD_PLAYED:
          case SHULKER_BOX_OPENED:
          case SLEEP_IN_BED:
          case SNEAK_TIME:
          case SPRINT_ONE_CM:
          case SWIM_ONE_CM:
          case TALKED_TO_VILLAGER:
          case TIME_SINCE_DEATH:
          case TIME_SINCE_REST:
          case TRADED_WITH_VILLAGER:
          case TRAPPED_CHEST_TRIGGERED:
          case WALK_ONE_CM:
          case WALK_ON_WATER_ONE_CM:
          case WALK_UNDER_WATER_ONE_CM:
            MessageUtil.sendError(sender, "해당 값은 &e/whois <플레이어 ID> stats_general <통계>&r 명령어를 사용해주세요.");
            break;
          default:
          case BREAK_ITEM:
          case CRAFT_ITEM:
          case DROP:
          case MINE_BLOCK:
          case PICKUP:
          case USE_ITEM:
            String statisticName = stats.name();
            MessageUtil.sendMessage(sender, Prefix.INFO_WHOIS, "-------------------------------------------------");
            MessageUtil.sendMessage(
                    sender, Prefix.INFO_WHOIS +
                            "&e" +
                            playerName +
                            "&r이(가) &e" +
                            statisticName +
                            "&r" +
                            MessageUtil.getFinalConsonant(statisticName, ConsonantType.은_는) +
                            " &e" +
                            offlinePlayer.getStatistic(stats, type) +
                            "&r회입니다.");
            break;
          case ENTITY_KILLED_BY:
          case KILL_ENTITY:
            MessageUtil.sendError(sender, "해당 값은 &e/whois <플레이어 ID> stats_entity <통계> <몹 이름>&r 명령어를 사용해주세요.");
            break;
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
          case ANIMALS_BRED:
          case ARMOR_CLEANED:
          case AVIATE_ONE_CM:
          case BANNER_CLEANED:
          case BEACON_INTERACTION:
          case BELL_RING:
          case BOAT_ONE_CM:
          case BREWINGSTAND_INTERACTION:
          case CAKE_SLICES_EATEN:
          case CAULDRON_FILLED:
          case CAULDRON_USED:
          case CHEST_OPENED:
          case CLEAN_SHULKER_BOX:
          case CLIMB_ONE_CM:
          case CRAFTING_TABLE_INTERACTION:
          case CROUCH_ONE_CM:
          case DAMAGE_ABSORBED:
          case DAMAGE_BLOCKED_BY_SHIELD:
          case DAMAGE_DEALT:
          case DAMAGE_DEALT_ABSORBED:
          case DAMAGE_DEALT_RESISTED:
          case DAMAGE_RESISTED:
          case DAMAGE_TAKEN:
          case DEATHS:
          case DISPENSER_INSPECTED:
          case DROPPER_INSPECTED:
          case DROP_COUNT:
          case ENDERCHEST_OPENED:
          case FALL_ONE_CM:
          case FISH_CAUGHT:
          case FLOWER_POTTED:
          case FLY_ONE_CM:
          case FURNACE_INTERACTION:
          case HOPPER_INSPECTED:
          case HORSE_ONE_CM:
          case INTERACT_WITH_ANVIL:
          case INTERACT_WITH_BLAST_FURNACE:
          case INTERACT_WITH_CAMPFIRE:
          case INTERACT_WITH_CARTOGRAPHY_TABLE:
          case INTERACT_WITH_GRINDSTONE:
          case INTERACT_WITH_LECTERN:
          case INTERACT_WITH_LOOM:
          case INTERACT_WITH_SMOKER:
          case INTERACT_WITH_STONECUTTER:
          case ITEM_ENCHANTED:
          case JUMP:
          case LEAVE_GAME:
          case MINECART_ONE_CM:
          case MOB_KILLS:
          case NOTEBLOCK_PLAYED:
          case NOTEBLOCK_TUNED:
          case OPEN_BARREL:
          case PIG_ONE_CM:
          case PLAYER_KILLS:
          case PLAY_ONE_MINUTE:
          case RAID_TRIGGER:
          case RAID_WIN:
          case RECORD_PLAYED:
          case SHULKER_BOX_OPENED:
          case SLEEP_IN_BED:
          case SNEAK_TIME:
          case SPRINT_ONE_CM:
          case SWIM_ONE_CM:
          case TALKED_TO_VILLAGER:
          case TIME_SINCE_DEATH:
          case TIME_SINCE_REST:
          case TRADED_WITH_VILLAGER:
          case TRAPPED_CHEST_TRIGGERED:
          case WALK_ONE_CM:
          case WALK_ON_WATER_ONE_CM:
          case WALK_UNDER_WATER_ONE_CM:
            MessageUtil.sendError(sender, "해당 값은 &e/whois <플레이어 ID> stats_general <통계>&r 명령어를 사용해주세요.");
            break;
          default:
          case BREAK_ITEM:
          case CRAFT_ITEM:
          case DROP:
          case MINE_BLOCK:
          case PICKUP:
          case USE_ITEM:
            MessageUtil.sendError(sender, "해당 값은 &e/whois <플레이어 ID> stats_material <통계> <아이템 이름>&r 명령어를 사용해주세요.");
            break;
          case ENTITY_KILLED_BY:
          case KILL_ENTITY:
            String statisticName = stats.toString();
            MessageUtil.sendMessage(sender, Prefix.INFO_WHOIS, "-------------------------------------------------");
            MessageUtil.sendMessage(
                    sender, Prefix.INFO_WHOIS +
                            "&e" +
                            playerName +
                            "&r이(가) &e" +
                            statisticName +
                            "&r" +
                            MessageUtil.getFinalConsonant(statisticName, ConsonantType.은_는) +
                            " &e" +
                            offlinePlayer.getStatistic(stats, type) +
                            "&r회입니다.");
            break;
        }
      }
      else if (args.length == 2 && args[1].equalsIgnoreCase("effect"))
      {
        if (player == null)
        {
          return true;
        }
        MessageUtil.sendMessage(sender, Prefix.INFO_WHOIS, "-------------------------------------------------");
        MessageUtil.sendMessage(sender, Prefix.INFO_WHOIS, "&e" + playerName + "&r의 상태 효과 정보");
        Collection<PotionEffect> potionEffects = player.getActivePotionEffects();
        if (potionEffects.size() == 0)
        {
          MessageUtil.sendMessage(sender, Prefix.INFO_WHOIS, "&c&o상태 효과 없음");
        }
        else
        {
          for (PotionEffect potionEffect : potionEffects)
          {
            String msg = Prefix.INFO_WHOIS.toString();
            String hasParticle = potionEffect.hasParticles() ? "&r" : "&r, &a입자 숨김";
            String isAmbient = potionEffect.isAmbient() ? "&r" : "&r, &a우측 상단 효과 빛남";
            String hasIcon = potionEffect.hasIcon() ? "&r" : "&r, &a우측 상단 아이콘 숨김";
            int amplifier = potionEffect.getAmplifier();
            String duration = Method.timeFormatMilli(potionEffect.getDuration() * 50L);
            PotionEffectType potionEffectType = potionEffect.getType();
            msg = msg + "효과 이름 : &e" + potionEffectType.getName() + "(" + potionEffectType.getName() + ")&r, 농도 레벨 : &e" + (amplifier + 1) + "단계&r, 지속 시간 : &e" + duration + hasParticle + isAmbient + hasIcon;

            MessageUtil.sendMessage(sender, msg);
          }
        }
        MessageUtil.sendMessage(sender, Prefix.INFO_WHOIS, "-------------------------------------------------");
      }
      else if (args.length == 2 && args[1].equals("offline"))
      {
        MessageUtil.sendMessage(sender, Prefix.INFO_WHOIS, "-------------------------------------------------");
        MessageUtil.sendMessage(sender, Prefix.INFO_WHOIS, offlinePlayer, "의 정보");
        MessageUtil.sendMessage(sender, Prefix.INFO_WHOIS, "접속 상태 : " + (offlinePlayer.isOnline() ? "&a온라인" : "&c오프라인"));
        if (Bukkit.getServer().hasWhitelist())
        {
          MessageUtil.sendMessage(sender, Prefix.INFO_WHOIS, "화이트리스트 상태 : " + (offlinePlayer.isWhitelisted() ? "&a화이트리스트에 등록됨" : "&c화이트리스트에 등록되지 않음"));
        }
        MessageUtil.sendMessage(sender, Prefix.INFO_WHOIS, "차단 상태 : " + (offlinePlayer.isBanned() ? "&4차단당함" : "&a차단당하지 않음"));
        MessageUtil.sendMessage(sender, Prefix.INFO_WHOIS, "관리자 상태 : &e" + (offlinePlayer.isOp() ? "&a관리자(오피)" : "&7관리자 아님(오피 아님)"));
        long firstPlayed = offlinePlayer.getFirstPlayed();
        if (!offlinePlayer.hasPlayedBefore() && firstPlayed == 0)
        {
          MessageUtil.sendMessage(sender, Prefix.INFO_WHOIS, "&e한 번도 접속한 기록이 없음");
        }
        else
        {
          MessageUtil.sendMessage(sender, Prefix.INFO_WHOIS, "접속 횟수 : &e" + ((offlinePlayer.isOnline() ? 1 : 0) + offlinePlayer.getStatistic(Statistic.LEAVE_GAME)));
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
          MessageUtil.sendMessage(sender, Prefix.INFO_WHOIS, "최초 접속 시각 : &e"
                  + Method.getCurrentTime(calendar, true, false) + " (" + Method.timeFormatMilli(current - firstPlayed,false) + " 전)");
        }
        if (lastLogin != 0)
        {
          calendar.setTimeInMillis(lastLogin);
          calendar.add(Calendar.HOUR, timeDiffer);
          MessageUtil.sendMessage(sender, Prefix.INFO_WHOIS, "마지막으로 접속한 시각 : &e" +
                  Method.getCurrentTime(calendar, true, false) + " (" + Method.timeFormatMilli(current - lastLogin, false) + " 전)");
        }
        if (lastSeen != 0 && !offlinePlayer.isOnline())
        {
          calendar.setTimeInMillis(lastSeen);
          calendar.add(Calendar.HOUR, timeDiffer);
          MessageUtil.sendMessage(sender, Prefix.INFO_WHOIS, "마지막으로 퇴장한 시각 : &e" +
                  Method.getCurrentTime(calendar, true, false) + " (" + Method.timeFormatMilli(current - lastSeen, false) + " 전)");
        }
        if (Cucumbery.using_Vault)
        {
          MessageUtil.sendMessage(sender, Prefix.INFO_WHOIS, "소지 금액 : &e" + Constant.Sosu2.format(Cucumbery.eco.getBalance(offlinePlayer)) + "원");
        }
        Location spawnPointLocation = offlinePlayer.getBedSpawnLocation();
        MessageUtil.sendMessage(sender, Prefix.INFO_WHOIS, "&2-스폰 포인트-");
        if (spawnPointLocation != null)
        {
          String spawnPointWorld = spawnPointLocation.getWorld().getName();
          double spawnPointX = spawnPointLocation.getBlockX();
          double spawnPointY = spawnPointLocation.getBlockY();
          double spawnPointZ = spawnPointLocation.getBlockZ();
          MessageUtil.sendMessage(sender, Prefix.INFO_WHOIS, "월드 : &e" + spawnPointWorld);
          MessageUtil.sendMessage(sender, Prefix.INFO_WHOIS, "X : &e" + Constant.Jeongsu.format(spawnPointX));
          MessageUtil.sendMessage(sender, Prefix.INFO_WHOIS, "Y : &e" + Constant.Jeongsu.format(spawnPointY));
          MessageUtil.sendMessage(sender, Prefix.INFO_WHOIS, "Z : &e" + Constant.Jeongsu.format(spawnPointZ));
          MessageUtil.sendMessage(sender, Prefix.INFO_WHOIS + "거리 : &e" + (sender instanceof Player ? Constant.Sosu4.format(spawnPointLocation.distance(((Player) sender).getLocation())) +
                  "m" : "-1m"));
        }
        else
        {
          MessageUtil.sendMessage(sender, Prefix.INFO_WHOIS, "&c없음");
        }
        MessageUtil.sendMessage(sender, Prefix.INFO_WHOIS, "-------------------------------------------------");
      }
      else
      {
        MessageUtil.sendError(sender, "명령어에 잘못된 인수가 있거나 불완전한 명령어입니다.");
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
}
