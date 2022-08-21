package com.jho5245.cucumbery.util.no_groups;

import com.jho5245.cucumbery.Cucumbery;
import com.jho5245.cucumbery.util.no_groups.ColorUtil.Type;
import com.jho5245.cucumbery.util.nbt.NBTAPI;
import com.jho5245.cucumbery.util.storage.no_groups.CustomConfig;
import com.jho5245.cucumbery.util.storage.no_groups.ItemStackUtil;
import com.jho5245.cucumbery.util.storage.component.util.ComponentUtil;
import com.jho5245.cucumbery.util.storage.component.util.ItemNameUtil;
import com.jho5245.cucumbery.util.storage.component.util.sendercomponent.SenderComponentUtil;
import com.jho5245.cucumbery.util.storage.data.Constant;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.attribute.Attributable;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageModifier;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.maxgamer.quickshop.QuickShop;
import org.maxgamer.quickshop.api.shop.Shop;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PlaceHolderUtil
{
  private PlaceHolderUtil()
  {

  }

  /**
   * 수식 연산 처리 정규식
   */
  private static final Pattern PATTERN_EVAL = Pattern.compile("\\{(eval:|calc:|evalp:|calcp:)([^}]+)}");

  private static final Pattern PATTERN_EVAL_PLACE_HOLDER = Pattern.compile("%(eval:|calc:|evalp:|calcp:)([^%]+)%");

  /**
   * 랜덤 수치 정규식
   */
  private static final Pattern PATTERN_RANDOM = Pattern.compile("%random(.*)");

  /**
   * 고정 랜덤 수치 정규식
   */
  private static final Pattern[] FIXED_PATTERNS;

  static
  {
    FIXED_PATTERNS = new Pattern[100];
    for (int i = 1; i <= 100; i++)
    {
      FIXED_PATTERNS[i - 1] = Pattern.compile("%fixed_random_group_" + i + "_(.*)");
    }
  }

  private static final Random RANDOM = new Random();

  private static final Pattern PATTERN_CONSONANT = Pattern.compile("%consonant_(.*)");

  private static final Pattern PATTERN_10_TO_16 = Pattern.compile("%10to16_(.*)");

  private static final Pattern PATTERN_16_TO_10 = Pattern.compile("%16to10_(.*)");

  private static final Pattern PATTERN_CHAR = Pattern.compile("%char_(.*)");

  private static final Pattern PATTERN_CHAR_NUMBER = Pattern.compile("%char_number_(.)");

  private static final Pattern PATTERN_CUSTOM_CALENDAR = Pattern.compile("%custom_calendar_(.*)");

  private static final Pattern PATTERN_QUICKSHOP_PRICE = Pattern.compile("%quickshop_price_(.*)");

  private static final Pattern PATTERN_WEATHER = Pattern.compile("%weather_(.*)");

  private static final Pattern PATTERN_RANDOM_KEYS = Pattern.compile("%randomkeys;(.*)");

  private static final Pattern PATTERN_UUID_TO_INT_ARRAY = Pattern.compile("%uuid_to_int_array_(.*)");

  private static final Pattern PATTERN_INT_ARRAY_TO_UUID = Pattern.compile("%int_array_to_uuid_(.*)");

  /**
   * 해당 문자열에 있는 모든 수식을 연산하여 반환합니다. 형식은 {eval:수식} {calc:수식}이며, 만약 소수 정밀도가 높은 연산이 필요하면 {evalp:수식} 또는 {calcp:수식} 을 사용하면 됩니다.
   *
   * @param input 값
   * @return 반환값
   */
  public static String evalString(String input)
  {
    Matcher matcher = PATTERN_EVAL.matcher(input);
    while (matcher.find())
    {
      String m = matcher.group();
      String str = matcher.group(2);
      input = input.replace(m, eval(str, matcher.group(1).contains("p")));
    }
    return input;
  }

  @NotNull
  static String placeholder(@NotNull String cmd, @NotNull Entity entity)
  {
    if (Cucumbery.using_PlaceHolderAPI && entity instanceof Player)
    {
      // just for some cases
      try
      {
        cmd = PlaceholderAPI.setPlaceholders((Player) entity, cmd);
      }
      catch (Exception ignored)
      {
      }
    }
    String customName = entity.getCustomName();
    String uuid = entity.getUniqueId().toString();
    cmd = cmd.replace("%entity_", "%" + uuid + "_entity_");
    cmd = cmd.replace("%" + entity.getName().toLowerCase() + "_player_", "%" + uuid + "_entity_");
    cmd = cmd.replace("%" + entity.getName().toLowerCase() + "_entity_", "%" + uuid + "_entity_");
    cmd = cmd.replace("%" + entity.getName() + "_player_", "%" + uuid + "_entity_");
    cmd = cmd.replace("%" + entity.getName() + "_entity_", "%" + uuid + "_entity_");
    cmd = cmd.replace("%" + entity.getName().toUpperCase() + "_player_", "%" + uuid + "_entity_");
    cmd = cmd.replace("%" + entity.getName().toUpperCase() + "_entity_", "%" + uuid + "_entity_");
    cmd = cmd.replace("%" + uuid + "_player_", "%" + uuid + "_entity_");
    cmd = cmd.replace("%" + uuid + "_entity_name%", entity.getName());
    cmd = cmd.replace("%player_", "%" + uuid + "_entity_");
    cmd = cmd.replace("%" + uuid + "_entity_uuid%", uuid);
    if (customName != null)
    {
      cmd = cmd.replace("%" + uuid + "_entity_custom_name%", customName);
    }
    if (cmd.contains("%" + uuid + "_entity_location"))
    {
      Location location = entity.getLocation();
      cmd = cmd.replace("%" + uuid + "_entity_location_x_p%", Constant.rawFormat.format(location.getX()));
      cmd = cmd.replace("%" + uuid + "_entity_location_y_p%", Constant.rawFormat.format(location.getY()));
      cmd = cmd.replace("%" + uuid + "_entity_location_z_p%", Constant.rawFormat.format(location.getZ()));
      cmd = cmd.replace("%" + uuid + "_entity_location_yaw_p%", Constant.rawFormat.format(location.getYaw()));
      cmd = cmd.replace("%" + uuid + "_entity_location_pitch_p%", Constant.rawFormat.format(location.getPitch()));
      cmd = cmd.replace("%" + uuid + "_entity_location_x%", Constant.Sosu2rawFormat.format(location.getX()));
      cmd = cmd.replace("%" + uuid + "_entity_location_y%", Constant.Sosu2rawFormat.format(location.getY()));
      cmd = cmd.replace("%" + uuid + "_entity_location_z%", Constant.Sosu2rawFormat.format(location.getZ()));
      cmd = cmd.replace("%" + uuid + "_entity_location_yaw%", Constant.Sosu2rawFormat.format(location.getYaw()));
      cmd = cmd.replace("%" + uuid + "_entity_location_pitch%", Constant.Sosu2rawFormat.format(location.getPitch()));
      cmd = cmd.replace("%" + uuid + "_entity_location_block_x%", location.getBlockX() + "");
      cmd = cmd.replace("%" + uuid + "_entity_location_block_y%", location.getBlockY() + "");
      cmd = cmd.replace("%" + uuid + "_entity_location_block_z%", location.getBlockZ() + "");
      cmd = cmd.replace("%" + uuid + "_entity_location_world%", location.getWorld().getName());
      cmd = cmd.replace("%" + uuid + "_entity_location_world_display%", Method.getWorldDisplayName(location.getWorld()));
      cmd = cmd.replace("%" + uuid + "_entity_location_world_display_strip_color%", MessageUtil.stripColor(Method.getWorldDisplayName(location.getWorld())));
    }
    if (entity instanceof org.bukkit.entity.Damageable damageable)
    {
      double health = damageable.getHealth();
      cmd = cmd.replace("%" + uuid + "_entity_health%", Constant.Sosu2rawFormat.format(health));
      cmd = cmd.replace("%" + uuid + "_entity_health_p%", Constant.rawFormat.format(health));
    }
    if (entity instanceof Attributable attributeable)
    {
      AttributeInstance attributeInstance = attributeable.getAttribute(Attribute.GENERIC_MAX_HEALTH);
      double maxHealth = attributeInstance != null ? attributeInstance.getValue() : 0;
      double baseMaxHealth = attributeInstance != null ? attributeInstance.getBaseValue() : 0;
      cmd = cmd.replace("%" + uuid + "_entity_max_health%", Constant.Sosu2rawFormat.format(maxHealth));
      cmd = cmd.replace("%" + uuid + "_entity_max_health_p%", Constant.rawFormat.format(maxHealth));
      cmd = cmd.replace("%" + uuid + "_entity_base_max_health%", Constant.Sosu2rawFormat.format(baseMaxHealth));
      cmd = cmd.replace("%" + uuid + "_entity_base_max_health_p%", Constant.rawFormat.format(baseMaxHealth));
    }
    if (entity instanceof Player player)
    {
      String displayName = CustomConfig.UserData.DISPLAY_NAME.getString(player.getUniqueId());
      if (displayName == null)
      {
        displayName = ComponentUtil.serialize(SenderComponentUtil.senderComponent(player));
      }
      cmd = cmd.replace("%" + uuid + "_entity_display_name%", displayName);
      cmd = cmd.replace("%" + uuid + "_entity_display_name_strip_color%", MessageUtil.stripColor(displayName));
      cmd = cmd.replace("%" + uuid + "_entity_foodlevel%", player.getFoodLevel() + "");
      cmd = cmd.replace("%" + uuid + "_entity_saturation%", Constant.Sosu2rawFormat.format(player.getSaturation()));
      cmd = cmd.replace("%" + uuid + "_entity_saturation_p%", Constant.rawFormat.format(player.getSaturation()));
      cmd = cmd.replace("%" + uuid + "_entity_level%", player.getLevel() + "");
      cmd = cmd.replace("%" + uuid + "_entity_total_experience%", player.getTotalExperience() + "");
      cmd = cmd.replace("%" + uuid + "_entity_game_mode%", player.getGameMode().toString());
      if (Cucumbery.using_Vault_Economy)
      {
        cmd = cmd.replace("%" + uuid + "_entity_money_raw%", Constant.Sosu2rawFormat.format(Cucumbery.eco.getBalance(player)));
        cmd = cmd.replace("%" + uuid + "_entity_money%", Constant.Sosu2.format(Cucumbery.eco.getBalance(player)));
      }
      PlayerInventory playerInventory = player.getInventory();
      ItemStack mainHand = playerInventory.getItemInMainHand();
      ItemStack offHand = playerInventory.getItemInOffHand();
      boolean mainHandExists = ItemStackUtil.itemExists(mainHand);
      boolean offHandExists = ItemStackUtil.itemExists(offHand);
      int mainHandAmount = mainHandExists ? mainHand.getAmount() : 0;
      int offHandAmount = offHandExists ? offHand.getAmount() : 0;
      Material mainHandType = mainHand.getType();
      Material offHandType = offHand.getType();
      String mainHandDisplay = ComponentUtil.serialize(ItemNameUtil.itemName(mainHand));
      String offHandDisplay = ComponentUtil.serialize(ItemNameUtil.itemName(offHand));
      cmd = cmd.replace("%" + uuid + "_entity_inventory_main_hand_amount%", mainHandAmount + "");
      cmd = cmd.replace("%" + uuid + "_entity_inventory_off_hand_amount%", offHandAmount + "");
      cmd = cmd.replace("%" + uuid + "_entity_inventory_main_hand_display%", mainHandDisplay);
      cmd = cmd.replace("%" + uuid + "_entity_inventory_off_hand_display%", offHandDisplay);
      cmd = cmd.replace("%" + uuid + "_entity_inventory_main_hand_display_strip_color%", MessageUtil.stripColor(mainHandDisplay));
      cmd = cmd.replace("%" + uuid + "_entity_inventory_off_hand_display_strip_color%", MessageUtil.stripColor(offHandDisplay));
      cmd = cmd.replace("%" + uuid + "_entity_inventory_main_hand_type%", MessageUtil.stripColor(ComponentUtil.serialize(ItemNameUtil.itemName(mainHandType))));
      cmd = cmd.replace("%" + uuid + "_entity_inventory_off_hand_type%", MessageUtil.stripColor(ComponentUtil.serialize(ItemNameUtil.itemName(offHandType))));
    }
    return cmd;
  }


  @NotNull
  public static String placeholder(@NotNull CommandSender sender, @NotNull String cmd, @Nullable Object extraParams)
  {
    return placeholder(sender, cmd, extraParams, false);
  }

  /**
   * 명령어의 플레이스 홀더를 사용합니다.
   *
   * @param cmd 시행할 명령어
   * @return 플레이스 홀더가 넣어진 반환될 명령어
   */
  @NotNull
  public static String placeholder(@NotNull CommandSender sender, @NotNull String cmd, @Nullable Object extraParams, boolean parseOnlyExtraParams)
  {
    if (extraParams == null && parseOnlyExtraParams)
    {
      return cmd;
    }
    int repeat = 0;
    // Pattern placeHolderPattern = Pattern.compile("%(.*){1,100}%");
    // Matcher placeHolderMatcher = placeHolderPattern.matcher(cmd);
    // while (placeHolderMatcher.find())
    // {
    // MessageUtil.broadcastDebug(placeHolderMatcher.find() + ", " + repeat + ":" + cmd);
    // if (repeat > 5)
    // break;
    // repeat++;
    while (cmd.replaceFirst("%", "").contains("%"))
    {
      if (repeat > 100)
      {
        break;
      }
      repeat++;
      if (extraParams != null)
      {
        if (extraParams instanceof EntityDamageByEntityEvent event)
        {
          Entity entity = event.getEntity();
          String uuid = entity.getUniqueId().toString();
          String name = entity.getName();
          cmd = cmd.replace("%original_damage%", Constant.Sosu2.format(event.getOriginalDamage(DamageModifier.BASE)));
          cmd = cmd.replace("%original_damage_raw%", Constant.Sosu2rawFormat.format(event.getOriginalDamage(DamageModifier.BASE)));
          cmd = cmd.replace("%damage%", Constant.Sosu2.format(event.getDamage()));
          cmd = cmd.replace("%damage_raw%", Constant.Sosu2rawFormat.format(event.getDamage()));
          cmd = cmd.replace("%final_damage%", Constant.Sosu2.format(event.getFinalDamage()));
          cmd = cmd.replace("%final_damage_raw%", Constant.Sosu2rawFormat.format(event.getFinalDamage()));
          cmd = cmd.replace("%target_uuid%", uuid);
          cmd = cmd.replace("%target_entity_uuid%", uuid);
          String customName = entity.getCustomName();
          if (customName != null)
          {
            cmd = cmd.replace("%target_custom_name%", customName);
          }
          if (entity instanceof Player)
          {
            cmd = cmd.replace("%target_player_uuid%", uuid);
            cmd = cmd.replace("%target_player%", name);
            cmd = cmd.replace("%target_player_name%", name);
            cmd = cmd.replace("%target_name%", name);
          }
          else
          {
            cmd = cmd.replace("%target_name%", (entity.getName()));
          }
        }
        if (extraParams instanceof PlayerInteractEvent event)
        {
          ItemStack item = event.getItem();

          cmd = cmd.replace("%item_amount%", (ItemStackUtil.itemExists(item) ? item.getAmount() : 0) + "");
        }
        if (extraParams instanceof PlayerItemConsumeEvent event)
        {
          ItemStack item = event.getItem();
          int foodLevel = ItemStackUtil.getFoodLevel(item.getType());
          double saturation = ItemStackUtil.getSaturation(item.getType());
          cmd = cmd.replace("%food_level%", foodLevel + "");
          cmd = cmd.replace("%saturation%", saturation + "");
        }
      }
      cmd = cmd.replace("%tps%", Constant.Sosu2.format(Bukkit.getTPS()[0]));
      cmd = cmd.replace("%tps_p%", Bukkit.getTPS()[0] + "");
      cmd = cmd.replace("%current_millis%", System.currentTimeMillis() + "");
      cmd = cmd.replace("%current_runtime%", Cucumbery.runTime + "");
      cmd = cmd.replace("%current_uptime%", Cucumbery.runTime + "");
      cmd = cmd.replace("%online_players%", Bukkit.getServer().getOnlinePlayers().size() + "");
      int users = 0;
      int ops = 0;
      for (Player player : Bukkit.getServer().getOnlinePlayers())
      {
        if (player.isOp())
        {
          ops++;
        }
        else
        {
          users++;
        }
      }
      cmd = cmd.replace("%online_users%", users + "");
      cmd = cmd.replace("%online_ops%", ops + "");
      cmd = cmd.replace("%max_players%", Bukkit.getServer().getMaxPlayers() + "");
      cmd = cmd.replace("%world_amount%", Bukkit.getServer().getWorlds().size() + "");

      if (cmd.contains("%uuid_to_int_array_"))
      {
        Matcher matcher = PATTERN_UUID_TO_INT_ARRAY.matcher(cmd);
        while (matcher.find())
        {
          String value = matcher.group(1);
          if (value.contains("%"))
          {
            value = value.split("%")[0];
          }
          if (Method.isUUID(value))
          {
            int[] array = NBTAPI.getIntArrayFromUUID(UUID.fromString(value));
            cmd = cmd.replace("%uuid_to_int_array_" + value + "%", array[0] + "," + array[1] + "," + array[2] + "," + array[3]);
          }
        }
      }

      if (cmd.contains("%int_array_to_uuid_"))
      {
        Matcher matcher = PATTERN_INT_ARRAY_TO_UUID.matcher(cmd);
        while (matcher.find())
        {
          String value = matcher.group(1);
          if (value.contains("%"))
          {
            value = value.split("%")[0];
          }
          String[] split = value.split(",");
          if (split.length == 4)
          {
            try
            {
              int[] array = new int[] {Integer.parseInt(split[0]), Integer.parseInt(split[1]), Integer.parseInt(split[2]), Integer.parseInt(split[3])};
              UUID uuid = NBTAPI.getUUIDFromIntArray(array);
              cmd = cmd.replace("%int_array_to_uuid_" + split[0] + "," + split[1] + "," + split[2] + "," + split[3] + "%", uuid.toString());
            }
            catch (Exception ignored)
            {

            }
          }
        }
      }

      if (cmd.contains("%char_"))
      {
        Matcher matcher = PATTERN_CHAR.matcher(cmd);
        while (matcher.find())
        {
          String value = matcher.group(1);
          if (value.contains("%"))
          {
            value = value.split("%")[0];
          }
          try
          {
            char c = (char) Integer.parseInt(value);
            cmd = cmd.replace("%char_" + value + "%", c + "");
          }
          catch (Exception ignored)
          {

          }
        }
      }
      if (cmd.contains("%char_number_"))
      {
        Matcher matcher = PATTERN_CHAR_NUMBER.matcher(cmd);
        while (matcher.find())
        {
          String value = matcher.group(1);
          try
          {
            char c = value.charAt(0);
            cmd = cmd.replace("%char_number_" + value + "%", ((int) c) + "");
          }
          catch (Exception ignored)
          {

          }
        }
      }
      if (cmd.contains("%10to16"))
      {
        Matcher matcher = PATTERN_10_TO_16.matcher(cmd);
        while (matcher.find())
        {
          String params = matcher.group(1);
          if (params.contains("%"))
          {
            params = params.split("%")[0];
          }
          try
          {
            long parse = Long.parseLong(params);
            String replace = Long.toHexString(parse);
            cmd = cmd.replaceFirst("%10to16_" + params + "%", replace);
          }
          catch (Exception ignored)
          {

          }
        }
      }
      if (cmd.contains("%16to10"))
      {
        Matcher matcher = PATTERN_16_TO_10.matcher(cmd);
        while (matcher.find())
        {
          String params = matcher.group(1);
          if (params.contains("%"))
          {
            params = params.split("%")[0];
          }
          try
          {
            long parse = Long.parseLong(params, 16);
            cmd = cmd.replaceFirst("%16to10_" + params + "%", parse + "");
          }
          catch (Exception ignored)
          {

          }
        }
      }

      if (cmd.contains("%consonant"))
      {
        try
        {
          Matcher matcher = PATTERN_CONSONANT.matcher(cmd);
          while (matcher.find())
          {
            String params = matcher.group(1);
            if (params.contains("%"))
            {
              params = params.split("%")[0];
            }
            String[] split = MessageUtil.splitEscape(params,';');
            String value = split[0];
            String key = split[1];
            String parseValue = switch (key)
                    {
                      case "을/를", "를/을" -> MessageUtil.getFinalConsonant(value, MessageUtil.ConsonantType.을를);
                      case "이/가", "가/이" -> MessageUtil.getFinalConsonant(value, MessageUtil.ConsonantType.이가);
                      case "은/는", "는/은" -> MessageUtil.getFinalConsonant(value, MessageUtil.ConsonantType.은는);
                      case "과/와", "와/과" -> MessageUtil.getFinalConsonant(value, MessageUtil.ConsonantType.와과);
                      case "으로/로", "로/으로" -> MessageUtil.getFinalConsonant(value, MessageUtil.ConsonantType.으로);
                      case "이라/라", "라/이라" -> MessageUtil.getFinalConsonant(value, MessageUtil.ConsonantType.이라);
                      default -> key;
                    };
            cmd = cmd.replaceFirst("%consonant_" + params + "%", parseValue);
          }
        }
        catch (Exception ignored)
        {

        }
      }

      if (Cucumbery.using_Vault_Economy && cmd.contains("%online_total_money%"))
      {
        double total = 0d;
        for (Player player : Bukkit.getOnlinePlayers())
        {
          total += Cucumbery.eco.getBalance(player);
        }
        cmd = cmd.replace("%online_total_money%", total + "");
      }

      if (Cucumbery.using_Vault_Economy && cmd.contains("%online_users_total_money%"))
      {
        double total = 0d;
        for (Player player : Bukkit.getOnlinePlayers())
        {
          if (player.hasPermission("wa.sans.ppap.undertale"))
          {
            continue;
          }
          total += Cucumbery.eco.getBalance(player);
        }
        cmd = cmd.replace("%online_users_total_money%", total + "");
      }

      if (cmd.contains("%weather_"))
      {
        try
        {
          // %weather_clear_<worldname>%
          Matcher matcher = PATTERN_WEATHER.matcher(cmd);
          while (matcher.find())
          {
            String params = matcher.group(1);
            if (params.contains("%"))
            {
              params = params.split("%")[0];
            }
            String[] split = params.split("_");
            String key = split[0], worldName = split[1];
            World world = Bukkit.getWorld(worldName);
            switch (key)
            {
              case "clear":
                if (world != null && !world.hasStorm())
                {
                  cmd = cmd.replace("%weather_" + key + "_" + worldName + "%", "true");
                }
                else
                {
                  cmd = cmd.replace("%weather_" + key + "_" + worldName + "%", "false");
                }
                break;
              case "rain":
                if (world != null && world.hasStorm() && !world.isThundering())
                {
                  cmd = cmd.replace("%weather_" + key + "_" + worldName + "%", "true");
                }
                else
                {
                  cmd = cmd.replace("%weather_" + key + "_" + worldName + "%", "false");
                }
                break;
              case "storm":
                if (world != null && world.hasStorm() && world.isThundering())
                {
                  cmd = cmd.replace("%weather_" + key + "_" + worldName + "%", "true");
                }
                else
                {
                  cmd = cmd.replace("%weather_" + key + "_" + worldName + "%", "false");
                }
                break;
            }
          }
        }
        catch (Exception ignored)
        {

        }
      }

      if (cmd.contains("%fixed_random_player%"))
      {
        int random = Method.random(1, Bukkit.getOnlinePlayers().size());
        int count = 0;
        for (Player player : Bukkit.getOnlinePlayers())
        {
          count++;
          if (count == random)
          {
            cmd = cmd.replace("%fixed_random_player%", player.getName());
          }
        }
      }

      if (cmd.contains("%random_player%"))
      {
        int random = Method.random(1, Bukkit.getOnlinePlayers().size());
        int count = 0;
        for (Player player : Bukkit.getOnlinePlayers())
        {
          count++;
          if (count == random)
          {
            cmd = cmd.replaceFirst("%random_player%", player.getName());
          }
        }
      }

      if (cmd.contains("%fixed_random_group_"))
      {
        for (int i = 1; i <= 100; i++)
        {
          Pattern pattern = FIXED_PATTERNS[i - 1];
          try
          {
            Matcher matcher = pattern.matcher(cmd);
            while (matcher.find())
            {
              String params = matcher.group(1);
              if (params.contains("%"))
              {
                params = params.split("%")[0];
              }
              String[] split = params.split("to");
              int from = Integer.parseInt(split[0]), to = Integer.parseInt(split[1]);
              int random = Method.random(from, to);
              cmd = cmd.replace("%fixed_random_group_" + i + "_" + params + "%", random + "");
            }
          }
          catch (Exception ignored)
          {
          }
        }
      }
      if (cmd.contains("%random"))
      {
        try
        {
          Matcher matcher = PATTERN_RANDOM.matcher(cmd);
          while (matcher.find())
          {
            String params = matcher.group(1);
            if (params.contains("%"))
            {
              params = params.split("%")[0];
            }
            String[] split = params.split("to");
            int from = Integer.parseInt(split[0]), to = Integer.parseInt(split[1]);
            int random = Method.random(from, to);
            cmd = cmd.replaceFirst("%random" + params + "%", random + "");
          }
        }
        catch (Exception ignored)
        {
        }
      }

      if (cmd.contains("%randomkeys"))
      {
        try
        {
          Matcher matcher = PATTERN_RANDOM_KEYS.matcher(cmd);
          while (matcher.find())
          {
            String params = matcher.group(1);
            if (params.contains("%"))
            {
              params = params.split("%")[0];
            }
            String[] split = params.split(";");
            int random = Method.random(1, split.length);
            cmd = cmd.replace("%randomkeys;" + params + "%", split[random - 1]);
          }
        }
        catch (Exception ignored)
        {

        }
      }

      double mathRandom = Math.random();
      cmd = cmd.replace("%fixed_random_p%", mathRandom + "");
      cmd = cmd.replace("%fixed_random%", Constant.Sosu2rawFormat.format(mathRandom));
      cmd = cmd.replaceFirst("%random_p%", Math.random() + "");
      cmd = cmd.replaceFirst("%random%", Constant.Sosu2rawFormat.format(Math.random()));
      cmd = cmd.replace("%fixed_random_boolean%", RANDOM.nextBoolean() + "");
      cmd = cmd.replace("%fixed_random_sign%", RANDOM.nextBoolean() ? "1" : "-1");
      cmd = cmd.replaceFirst("%random_boolean%", RANDOM.nextBoolean() + "");
      cmd = cmd.replaceFirst("%random_sign%", RANDOM.nextBoolean() ? "1" : "-1");

      cmd = cmd.replace("%bool_number_true%", "1");
      cmd = cmd.replace("%bool_number_false%", "0");

      if (Cucumbery.using_QuickShop)
      {
        if (cmd.contains("%quickshop_price_"))
        {
          Matcher matcher = PATTERN_QUICKSHOP_PRICE.matcher(cmd);
          while (matcher.find())
          {
            String params = matcher.group(1);
            if (params.contains("%"))
            {
              params = params.split("%")[0];
            }
            String[] split = params.split("_");
            if (split.length == 4)
            {
              try
              {
                Shop shop = QuickShop.getInstance().getShopManager().getShop(new Location(Bukkit.getWorld(split[0]), Integer.parseInt(split[1]), Integer.parseInt(split[2]),
                        Integer.parseInt(split[3])));
                double price = shop.getPrice();
                cmd = cmd.replace("%quickshop_price_" + split[0] + "_" + split[1] + "_" + split[2] + "_" + split[3] + "%", Constant.rawFormat.format(price) + "");
              }
              catch (Exception ignored)
              {
                cmd = cmd.replace("%quickshop_price_" + split[0] + "_" + split[1] + "_" + split[2] + "_" + split[3] + "%", "-1");
              }
            }
          }
        }
      }

      if (cmd.contains("%custom_calendar_"))
      {
        try
        {
          Matcher matcher = PATTERN_CUSTOM_CALENDAR.matcher(cmd);
          while (matcher.find())
          {
            String params = matcher.group(1);
            if (params.contains("%"))
            {
              params = params.split("%")[0];
            }
            DateFormat formatter = new SimpleDateFormat(params);
            Calendar calendar = Calendar.getInstance();
            int adjustTimeZone = Cucumbery.config.getInt("adjust-time-difference-value");
            calendar.add(Calendar.HOUR, adjustTimeZone);
            cmd = cmd.replaceFirst("%custom_calendar_" + params + "%", formatter.format(calendar.getTime()).replace("AM", "오전").replace("PM", "오후"));
          }
        }
        catch (Exception ignored)
        {
        }
      }

      if (cmd.contains("%time_zone%"))
      {
        Calendar calendar = Calendar.getInstance();
        TimeZone timeZone = calendar.getTimeZone();
        int diff = (timeZone.getRawOffset() / 1000 / 3600) + 1;
        cmd = cmd.replace("%time_zone%", "GMT " + (diff >= 0 ? "+" : "") + diff);
      }
      if (cmd.contains("%calendar"))
      {
        String dateFormat = "yyyy년 MM월 dd일 kk시 mm분 ss초";
        DateFormat formatter = new SimpleDateFormat(dateFormat);
        Calendar calendar = Calendar.getInstance();
        int adjustTimeZone = Cucumbery.config.getInt("adjust-time-difference-value");
        calendar.add(Calendar.HOUR, adjustTimeZone);
        cmd = cmd.replace("%calendar%", formatter.format(calendar.getTime()));
        Pattern pattern = Pattern.compile("%calendar_(.*)");
        Matcher matcher = pattern.matcher(cmd);
        while (matcher.find())
        {
          String params = matcher.group(1);
          if (params.contains("%"))
          {
            params = params.split("%")[0];
          }
          final String originalParams = params;
          params = params.replace("y", "년").replace("mo", "개월").replace("d", "일").replace("h", "시간").replace("m", "분").replace("s", "초");
          String[] split = params.split("_");
          int yearDiff = 0, monthDiff = 0, dateDiff = 0, hourDiff = 0, minuteDiff = 0, secondDiff = 0;
          for (String param : split)
          {
            try
            {
              if (param.endsWith("년"))
              {
                yearDiff = Integer.parseInt(param.replace("년", ""));
              }
              if (param.endsWith("개월"))
              {
                monthDiff = Integer.parseInt(param.replace("개월", ""));
              }
              if (param.endsWith("일"))
              {
                dateDiff = Integer.parseInt(param.replace("일", ""));
              }
              if (param.endsWith("시간"))
              {
                hourDiff = Integer.parseInt(param.replace("시간", ""));
              }
              if (param.endsWith("분"))
              {
                minuteDiff = Integer.parseInt(param.replace("분", ""));
              }
              if (param.endsWith("초"))
              {
                secondDiff = Integer.parseInt(param.replace("초", ""));
              }
            }
            catch (Exception ignored)
            {
            }
          }
          calendar.add(Calendar.YEAR, yearDiff);
          calendar.add(Calendar.MONTH, monthDiff);
          calendar.add(Calendar.DATE, dateDiff);
          calendar.add(Calendar.HOUR_OF_DAY, hourDiff);
          calendar.add(Calendar.MINUTE, minuteDiff);
          calendar.add(Calendar.SECOND, secondDiff);
          cmd = cmd.replace("%calendar_" + originalParams + "%", formatter.format(calendar.getTime()).replace("24시", "00시"));
          // MessageUtil.broadcastDebug("y:" + yearDiff + ", mo:" + monthDiff + ", d:" + dateDiff + ", h:" + hourDiff + ", m:" + minuteDiff + ", s:" + secondDiff);
        }
      }
      if (cmd.contains("%12calendar"))
      {
        String dateFormat = "yyyy년 MM월 dd일 aa hh시 mm분 ss초";
        DateFormat formatter = new SimpleDateFormat(dateFormat);
        Calendar calendar = Calendar.getInstance();
        int adjustTimeZone = Cucumbery.config.getInt("adjust-time-difference-value");
        calendar.add(Calendar.HOUR, adjustTimeZone);
        cmd = cmd.replace("%12calendar%", formatter.format(calendar.getTime()).replace("AM", "오전").replace("PM", "오후"));
        Pattern pattern = Pattern.compile("%12calendar_(.*)");
        Matcher matcher = pattern.matcher(cmd);
        while (matcher.find())
        {
          String params = matcher.group(1);
          if (params.contains("%"))
          {
            params = params.split("%")[0];
          }
          final String originalParams = params;
          params = params.replace("y", "년").replace("mo", "개월").replace("d", "일").replace("h", "시간").replace("m", "분").replace("s", "초");
          String[] split = params.split("_");
          int yearDiff = 0, monthDiff = 0, dateDiff = 0, hourDiff = 0, minuteDiff = 0, secondDiff = 0;
          for (String param : split)
          {
            try
            {
              if (param.endsWith("년"))
              {
                yearDiff = Integer.parseInt(param.replace("년", ""));
              }
              if (param.endsWith("개월"))
              {
                monthDiff = Integer.parseInt(param.replace("개월", ""));
              }
              if (param.endsWith("일"))
              {
                dateDiff = Integer.parseInt(param.replace("일", ""));
              }
              if (param.endsWith("시간"))
              {
                hourDiff = Integer.parseInt(param.replace("시간", ""));
              }
              if (param.endsWith("분"))
              {
                minuteDiff = Integer.parseInt(param.replace("분", ""));
              }
              if (param.endsWith("초"))
              {
                secondDiff = Integer.parseInt(param.replace("초", ""));
              }
            }
            catch (Exception ignored)
            {
            }
          }
          calendar.add(Calendar.YEAR, yearDiff);
          calendar.add(Calendar.MONTH, monthDiff);
          calendar.add(Calendar.DATE, dateDiff);
          calendar.add(Calendar.HOUR_OF_DAY, hourDiff);
          calendar.add(Calendar.MINUTE, minuteDiff);
          calendar.add(Calendar.SECOND, secondDiff);
          cmd = cmd.replace("%12calendar_" + originalParams + "%", formatter.format(calendar.getTime()).replace("AM", "오전").replace("PM", "오후").replace("24시", "00시"));
        }
      }
      Pattern pattern = Pattern.compile("%(.*)_player_(.*)");
      Matcher matcher = pattern.matcher(cmd);
      while (matcher.find())
      {
        String otherPlayerName = matcher.group(1);
        if (otherPlayerName.contains("%"))
        {
          String[] split = otherPlayerName.split("%");
          if (otherPlayerName.startsWith("%") && split.length > 1)
          {
            otherPlayerName = split[1];
          }
          else
          {
            otherPlayerName = split[0];
          }
        }
        if (otherPlayerName.contains("_player_"))
        {
          otherPlayerName = otherPlayerName.split("_player_")[0];
        }
        Player otherPlayer = SelectorUtil.getPlayer(sender, otherPlayerName, false);
        if (otherPlayer != null)
        {
          cmd = placeholder(cmd, otherPlayer);
          break;
        }
      }
      pattern = Pattern.compile("%(.*)_entity_(.*)");
      matcher = pattern.matcher(cmd);
      while (matcher.find())
      {
        String entityUUID = matcher.group(1);
        if (entityUUID.contains("%"))
        {
          String[] split = entityUUID.split("%");
          if (entityUUID.startsWith("%") && split.length > 1)
          {
            entityUUID = split[1];
          }
          else
          {
            entityUUID = split[0];
          }
        }
        if (entityUUID.contains("_entity_"))
        {
          entityUUID = entityUUID.split("_entity_")[0];
        }
        try
        {
          Entity entity = Method2.getEntityAsync(UUID.fromString(entityUUID));
          if (entity != null)
          {
            cmd = placeholder(cmd, entity);
          }
        }
        catch (Throwable ignore)
        {
        }
      }
      if (sender instanceof Player)
      {
        cmd = placeholder(cmd, (Player) sender);
      }
      if (cmd.contains("%eval"))
      {
        matcher = PATTERN_EVAL_PLACE_HOLDER.matcher(cmd);
        while (matcher.find())
        {
          String m = matcher.group();
          String str = matcher.group(2);
          cmd = cmd.replace(m, eval(str, matcher.group(1).contains("p")));
        }
      }
    }
    return getGradientString(cmd);
  }


  public static final Pattern PATTERN_GRADIENT = Pattern.compile("%gradient_\\[([#&§0-9a-fA-FrgbRGB,;]+)]_((.*)[^%])");

  public static String getGradientString(String s)
  {
    for (int i = 0; i < 10; i++)
    {
      Matcher m = PATTERN_GRADIENT.matcher(s);
      while (m.find())
      {
        String[] colorStrings = m.group(1).split(";");
        String content = m.group(2);
        if (content.contains("%"))
        {
          content = content.split("%")[0];
        }
        List<ColorUtil> colors = new ArrayList<>();
        for (String colorString : colorStrings)
        {
          if (colorString.startsWith("#"))
          {
            colors.add(new ColorUtil(Type.HEX, colorString));
          }
          else if (colorString.startsWith("&") || colorString.startsWith("§"))
          {
            colors.add(new ColorUtil(Type.MFC, colorString));
          }
          else if (colorString.contains(","))
          {
            colors.add(new ColorUtil(Type.RGB, colorString));
          }
        }
        s = s.replace("%gradient_["+m.group(1)+"]_"+content+"%", getGradientString(colors, content));
      }
    }
    return s;
  }

  public static String getGradientString(List<ColorUtil> colors, String string)
  {
    StringBuilder master = new StringBuilder();
    String cleanString = string.replaceAll("&[0-9a-fA-FrRkKxXmMnNLloO]", "");
    int length = cleanString.length();
    if (colors.isEmpty())
    {
      return string;
    }
    else if (colors.size() == 1)
    {
      colors.add(colors.get(0));
    }
    if (string.length() == 1)
    {
      ColorUtil colorParser = colors.get(0);
      return "rgb"+colorParser.getRed()+","+colorParser.getGreen()+","+colorParser.getBlue()+";"+string;
    }
    while (colors.size() > cleanString.length())
    {
      colors.remove(colors.size()-1);
    }
    int blockSize = length / (colors.size() - 1);
    int left = length % (colors.size() - 1);
    int pos = 0;
    boolean bold = false;
    boolean italic = false;
    boolean strike = false;
    boolean underline = false;
    boolean random = false;
    for (int n = 0; n < colors.size()-1; n++)
    {
      int gradientLength;
      if (n == colors.size()-2)
      {
        gradientLength = blockSize+left;
        blockSize = gradientLength;
      }
      else
      {
        gradientLength = blockSize;
      }
      List<ColorUtil> gradient = ColorUtil.getGradient(colors.get(n), colors.get(n+1), gradientLength+1);
      for (int m = 0; m < blockSize; m++)
      {
        char c = cleanString.charAt(pos);
        char s = string.charAt(pos);
        while (s != c)
        {
          if (s == '&')
          {
            switch (string.charAt(pos + 1))
            {
              case 'r' -> {
                bold = false;
                italic = false;
                strike = false;
                underline = false;
                random = false;
              }
              case 'l' -> bold = true;
              case 'm' -> strike = true;
              case 'n' -> underline = true;
              case 'o' -> italic = true;
              case 'k' -> random = true;
            }
            string = string.replaceFirst("&"+string.charAt(pos+1), "");
            s = string.charAt(pos);
          }
          else
          {
            s = c;
          }
        }
        ColorUtil color = gradient.get(m);
        master.append(color.getMFC());
        if (bold)
        {
          master.append("&l");
        }
        if (strike)
        {
          master.append("&m");
        }
        if (underline)
        {
          master.append("&n");
        }
        if (italic)
        {
          master.append("&o");
        }
        if (random)
        {
          master.append("&k");
        }
        master.append(cleanString.charAt(pos));
        pos++;
      }
    }
    return master.toString();
  }

  private static String eval(String str, boolean precision)
  {
    str = str.replace("PI", Math.PI + "");
    str = str.replace("Pi", Math.PI + "");
    str = str.replace("pI", Math.PI + "");
    str = str.replace("pi", Math.PI + "");
    str = str.replace("true", "1");
    str = str.replace("false", "0");
    char[] chars = str.toCharArray();
    for (int i = 0; i < chars.length; i++)
    {
      char c = chars[i];
      if (c == 'e' || c == 'E')
      {
        if (i == 0 || i == chars.length - 1)
        {
          chars[i] = '이';
        }
        else
        {
          char before = chars[i - 1], after = chars[i + 1];
          if ((before == '(' || before == '+' || before == '-' || before == '/' || before == '%' || before == ' ' || before == 'm' || before == 'M' || before == '^') &&
                  (after == ')' || after == '+' || after == '-' || after == '/' || after == '%' || after == ' ' || after == 'm' || after == 'M' || after == '^'))
          {
            chars[i] = '이';
          }
        }
      }
    }
    str = new String(chars);
    str = str.replace("이", Math.E + "");
    return eval2(str, precision);
  }

  private static String eval2(String str, boolean precision)
  {
    Double evalValue = eval3(str);
    if (evalValue.isNaN())
    {
      return "NaN";
    }
    return precision ? Constant.rawFormat.format(eval3(str)) : Constant.Sosu2rawFormat.format(eval3(str));
  }

  private static Double eval3(final String str)
  {
    try
    {
      return (new Object()
      {
        int pos = -1, ch;

        void nextChar()
        {
          ch = (++pos < str.length()) ? str.charAt(pos) : -1;
        }

        boolean eat(int charToEat)
        {
          while (ch == ' ')
          {
            nextChar();
          }
          if (ch == charToEat)
          {
            nextChar();
            return true;
          }
          return false;
        }

        double parse()
        {
          nextChar();
          double x = parseExpression();
          if (pos < str.length())
          {
            throw new RuntimeException("Unexpected: " + (char) ch);
          }
          return x;
        }

        // Grammar:
        // expression = term | expression `+` term | expression `-` term
        // term = factor | term `*` factor | term `/` factor
        // factor = `+` factor | `-` factor | `(` expression `)`
        // | number | functionName factor | factor `^` factor

        double parseExpression()
        {
          double x = parseTerm();
          for (; ; )
          {
            if (eat('+'))
            {
              x += parseTerm(); // addition
            }
            else if (eat('-'))
            {
              x -= parseTerm(); // subtraction
            }
            else
            {
              return x;
            }
          }
        }

        double parseTerm()
        {
          double x = parseFactor();
          for (; ; )
          {
            if (eat('*'))
            {
              x *= parseFactor(); // multiplication
            }
            else if (eat('/'))
            {
              x /= parseFactor(); // division
            }
            else if (eat('%'))
            {
              x %= parseFactor(); // remain
            }
            else
            {
              return x;
            }
          }
        }

        double parseFactor()
        {
          if (eat('+'))
          {
            return parseFactor(); // unary plus
          }
          if (eat('-'))
          {
            return -parseFactor(); // unary minus
          }

          double x;
          int startPos = this.pos;
          if (eat('('))
          { // parentheses
            x = parseExpression();
            eat(')');
          }
          else if ((ch >= '0' && ch <= '9') || ch == '.')
          { // numbers
            while ((ch >= '0' && ch <= '9') || ch == '.')
            {
              nextChar();
            }
            x = Double.parseDouble(str.substring(startPos, this.pos));
          }
          else if ((ch >= 'a' && ch <= 'z') || (ch >= 'A' && ch <= 'Z'))
          { // functions
            while ((ch >= 'a' && ch <= 'z') || (ch >= 'A' && ch <= 'Z'))
            {
              nextChar();
            }
            String func = str.substring(startPos, this.pos);
            x = parseFactor();
            x = switch (func.toLowerCase())
                    {
                      case "sqrt" -> Math.sqrt(x);
                      case "cbrt" -> Math.cbrt(x);
                      case "sin" -> Math.sin(Math.toRadians(x));
                      case "cos" -> Math.cos(Math.toRadians(x));
                      case "tan" -> Math.tan(Math.toRadians(x));
                      case "csc" -> 1d / Math.sin(Math.toRadians(x));
                      case "sec" -> 1d / Math.cos(Math.toRadians(x));
                      case "cot" -> 1d / Math.tan(Math.toRadians(x));
                      case "asin" -> Math.asin(Math.toRadians(x));
                      case "acos" -> Math.acos(Math.toRadians(x));
                      case "atan" -> Math.atan(Math.toRadians(x));
                      case "acsc" -> 1d / Math.asin(Math.toRadians(x));
                      case "asec" -> 1d / Math.acos(Math.toRadians(x));
                      case "acot" -> 1d / Math.atan(Math.toRadians(x));
                      case "rsin" -> Math.sin(x);
                      case "rcos" -> Math.cos(x);
                      case "rtan" -> Math.tan(x);
                      case "rcsc" -> 1d / Math.sin(x);
                      case "rsec" -> 1d / Math.cos(x);
                      case "rcot" -> 1d / Math.tan(x);
                      case "rasin" -> Math.asin(x);
                      case "racos" -> Math.acos(x);
                      case "ratan" -> Math.atan(x);
                      case "racsc" -> 1d / Math.asin(x);
                      case "rasec" -> 1d / Math.acos(x);
                      case "racot" -> 1d / Math.atan(x);
                      case "log" -> Math.log10(x);
                      case "ln" -> Math.log(x);
                      case "abs" -> Math.abs(x);
                      case "round" -> Math.round(x);
                      case "ceil" -> Math.ceil(x);
                      case "floor" -> Math.floor(x);
                      default -> throw new RuntimeException("Unknown function: " + func);
                    };
          }
          else
          {
            throw new RuntimeException("Unexpected: " + (char) ch);
          }

          if (eat('^'))
          {
            x = Math.pow(x, parseFactor()); // exponentiation
          }
          else if (eat('m')) // Math.min
          {
            x = Math.min(x, parseFactor());
          }
          else if (eat('M')) // Math.max
          {
            x = Math.max(x, parseFactor());
          }
          else if (eat('l') || eat('L'))
          {
            x = Math.log(parseFactor()) / Math.log(x);
          }
          else if (eat('>'))
          {
            x = x > parseFactor() ? 1 : 0;
          }
          else if (eat('≥'))
          {
            x = x >= parseFactor() ? 1 : 0;
          }
          else if (eat('≤'))
          {
            x = x <= parseFactor() ? 1 : 0;
          }
          else if (eat('<'))
          {
            x = x < parseFactor() ? 1 : 0;
          }
          else if (eat('='))
          {
            x = x == parseFactor() ? 1 : 0;
          }
          else if (eat('≠'))
          {
            x = x != parseFactor() ? 1 : 0;
          }
          return x;
        }
      }.parse());
    }
    catch (Exception e)
    {
      return Double.NaN;
    }
  }
}
