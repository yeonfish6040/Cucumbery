package com.jho5245.cucumbery.util.storage;

import com.google.common.base.Predicates;
import com.jho5245.cucumbery.Cucumbery;
import com.jho5245.cucumbery.util.MessageUtil;
import com.jho5245.cucumbery.util.Method;
import com.jho5245.cucumbery.util.Method2;
import com.jho5245.cucumbery.util.storage.data.Constant;
import com.jho5245.cucumbery.util.storage.data.Prefix;
import com.jho5245.cucumbery.util.storage.data.Variable;
import com.xxmicloxx.NoteBlockAPI.model.Song;
import de.tr7zw.changeme.nbtapi.NBTItem;
import dev.jorel.commandapi.wrappers.NativeProxyCommandSender;
import io.papermc.paper.inventory.ItemRarity;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.*;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.format.TextDecoration.State;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.*;
import org.bukkit.attribute.Attributable;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.block.Banner;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.data.BlockData;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.*;
import org.bukkit.entity.Villager.Profession;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.json.simple.parser.JSONParser;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ComponentUtil
{
  private static final JSONParser JSON_PARSER = new JSONParser();
  /**
   * 컴포넌트 변환 URL
   */
  private static final Pattern URL = Pattern.compile("^(?:(https?)://)?([-\\w_.]{2,}\\.[a-z]{2,4})(/\\S*)?$");
  private static final Pattern p = Pattern.compile("%s"), p2 = Pattern.compile("%[0-9]+\\$s");
  private static final Pattern yeet = Pattern.compile("(%s|%[0-9]+\\$s)([^가-힣ㄱ-ㅎA-Za-z0-9]+|)" +
          "(이\\(가\\)|\\(이\\)가|가\\(이\\)|\\(가\\)이|을\\(를\\)|\\(을\\)를|를\\(을\\)|\\(를\\)을|와\\(과\\)|\\(와\\)과|과\\(와\\)|\\(과\\)와|은\\(는\\)|\\(은\\)는|는\\(은\\)|\\(는\\)은|\\(으\\)로|으\\(로\\)|\\(이\\)라|이\\(라\\))");

  /**
   * 아이템의 이름을 컴포넌트 형태로 반환합니다.
   *
   * @param type 아이템의 종류
   * @return 컴포넌트 형태의 아이템 이름
   */
  public static Component itemName(@NotNull Material type)
  {
    return itemName(new ItemStack(type), null);
  }

  /**
   * 아이템의 이름을 컴포넌트 형태로 반환합니다.
   *
   * @param type 아이템의 종류
   * @return 컴포넌트 형태의 아이템 이름
   */
  public static Component itemName(@NotNull Material type, @Nullable TextColor defaultColor)
  {
    return itemName(new ItemStack(type), defaultColor);
  }

  /**
   * 아이템의 이름을 컴포넌트 형태로 반환합니다.
   *
   * @param itemStack 아이템
   * @return 컴포넌트 형태의 아이템 이름
   */
  @NotNull
  public static Component itemName(@NotNull ItemStack itemStack)
  {
    return itemName(itemStack, null);
  }

  /**
   * 아이템의 이름을 컴포넌트 형태로 반환합니다.
   *
   * @param itemStack    아이템
   * @param defaultColor 색상이 없을 경우 적용할 기본 값
   * @return 컴포넌트 형태의 아이템 이름
   */
  @NotNull
  public static Component itemName(@NotNull ItemStack itemStack, @Nullable TextColor defaultColor)
  {
    Material material = itemStack.getType();
    ItemMeta itemMeta = itemStack.getItemMeta();

    Component component;

    // 아이템의 이름이 있을 경우 이름을 가져온다
    if (itemMeta != null && itemMeta.hasDisplayName())
    {
      component = itemMeta.displayName();

      if (component == null) // 이런 경우는 없다.
      {
        component = Component.empty();
      }

      // 아이템의 기울임꼴 값이 없는 경우 기본적으로 아이템의 이름이 기울어져 있으므로 기울임 효과를 추가한다.
      if (component.decorations().get(TextDecoration.ITALIC) == State.NOT_SET)
      {
        component = component.decoration(TextDecoration.ITALIC, State.TRUE);
      }
    }

    // 아이템의 이름이 없을 경우 번역된 아이템 이름을 가져온다.
    else
    {
      String id = material.translationKey();
      component = Component.translatable(id);
      // 특정 아이템은 다른 번역 규칙을 가지고 있으므로 해당 규칙을 적용한다.
      switch (material)
      {
        case WHEAT -> component = Component.translatable("item.minecraft.wheat");
        case PLAYER_HEAD, PLAYER_WALL_HEAD -> {
          String playerName = PlayerHeadInfo.getPlayerHeadInfo(itemStack, PlayerHeadInfo.PlayerHeadInfoType.NAME);
          if (playerName != null)
          {
            component = Component.translatable("block.minecraft.player_head.named").args(Component.text(playerName));
          }
        }
        case POTION, SPLASH_POTION, LINGERING_POTION, TIPPED_ARROW -> {
          PotionMeta potionMeta = (PotionMeta) itemMeta;
          if (potionMeta != null)
          {
            String potionId = potionMeta.getBasePotionData().getType().toString().toLowerCase();
            switch (potionMeta.getBasePotionData().getType())
            {
              case AWKWARD, FIRE_RESISTANCE, INVISIBILITY, LUCK, MUNDANE, NIGHT_VISION, POISON, SLOW_FALLING, SLOWNESS, STRENGTH, THICK, TURTLE_MASTER, WATER, WATER_BREATHING, WEAKNESS -> component = Component.translatable(id + ".effect." + potionId);
              case UNCRAFTABLE -> component = Component.translatable(id + ".effect.empty");
              case JUMP -> component = Component.translatable(id + ".effect.leaping");
              case REGEN -> component = Component.translatable(id + ".effect.regeneration");
              case SPEED -> component = Component.translatable(id + ".effect.swiftness");
              case INSTANT_HEAL -> component = Component.translatable(id + ".effect.healing");
              case INSTANT_DAMAGE -> component = Component.translatable(id + ".effect.harming");
            }
          }
        }
        case WRITTEN_BOOK -> {
          BookMeta bookMeta = (BookMeta) itemMeta;
          if (bookMeta != null && bookMeta.hasTitle())
          {
            component = bookMeta.title();
            if (component == null || ComponentUtil.serialize(component).isEmpty())
            {
              component = Component.translatable(id);
            }
          }
        }
        case COMPASS -> {
          CompassMeta compassMeta = (CompassMeta) itemMeta;
          if (compassMeta != null && compassMeta.hasLodestone())
          {
            component = Component.translatable("item.minecraft.lodestone_compass");
          }
        }
        case SHIELD -> {
          BlockStateMeta blockStateMeta = (BlockStateMeta) itemMeta;
          if (blockStateMeta != null && blockStateMeta.hasBlockState())
          {
            BlockState blockState = blockStateMeta.getBlockState();
            Banner bannerState = (Banner) blockState;
            DyeColor baseColor = bannerState.getBaseColor();
            component = Component.translatable(id + "." + baseColor.toString().toLowerCase());
          }
        }
      }
      // 이름이 없는 아이템은 기울임 효과가 없으므로 반드시 false로 한다.
      component = component.decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE);
    }

    // 아이템의 등급이 있고 아이템 이름에 색깔이 없을 경우와 기본 아이템 색깔이 흰색이 아닐 경우 색깔을 추가한다.
    if (material.isItem() && material != Material.AIR)
    {
      ItemRarity itemRarity = material.getItemRarity();
      @Nullable TextColor textColor = component.color();
      // 아이템에 마법이 부여되어 있을 경우 기본 아이템 이름의 색깔이 변경되므로 해당 색깔을 추가한다.
      boolean hasEnchants = itemMeta != null && itemMeta.hasEnchants();
      if (textColor == null)
      {
        switch (itemRarity)
        {
          case UNCOMMON -> textColor = hasEnchants ? NamedTextColor.AQUA : NamedTextColor.YELLOW;
          case RARE -> textColor = hasEnchants ? NamedTextColor.LIGHT_PURPLE : NamedTextColor.AQUA;
          case EPIC -> textColor = NamedTextColor.LIGHT_PURPLE;
          default -> textColor = hasEnchants ? NamedTextColor.AQUA : null;
        }
        component = component.color(textColor);
      }
    }
    if (component.color() == null && defaultColor != null)
    {
      component = component.color(defaultColor);
    }
    return component;
  }

  /**
   * 콘솔, 명령 블록, 개체, 플레이어, 접속 중이지 않은 플레이어의 정보를 표시할 컴포넌트를 반환합니다.
   *
   * @param object 정보를 가져올 오브젝트
   * @return 해당 오브젝트의 정보를 가진 컴포넌트
   */
  @NotNull
  public static Component senderComponent(@NotNull Object object)
  {
    return senderComponent(object, Constant.THE_COLOR);
  }

  /**
   * 콘솔, 명령 블록, 개체, 플레이어, 접속 중이지 않은 플레이어의 정보를 표시할 컴포넌트를 반환합니다.
   *
   * @param object       정보를 가져올 오브젝트
   * @param defaultColor 해당 개체의 컴포넌트에 색상이 없을 때 기본 색상
   * @return 해당 오브젝트의 정보를 가진 컴포넌트
   */
  @NotNull
  public static Component senderComponent(@NotNull Object object, @Nullable TextColor defaultColor)
  {
    if (Cucumbery.using_CommandAPI && object instanceof NativeProxyCommandSender sender)
    {
      CommandSender caller = sender.getCaller(), callee = sender.getCallee();
      if (caller.equals(callee))
      {
        return senderComponent(caller);
      }
      return senderComponent(callee);
//      return ComponentUtil.createTranslate("%s에 의한 %s", senderComponent(sender.getCaller(), defaultColor), senderComponent(sender.getCallee(), defaultColor));
    }
    if (object instanceof UUID uuid)
    {
      Entity entity = Bukkit.getEntity(uuid);
      if (entity != null)
      {
        return senderComponent(entity, defaultColor);
      }
    }
    if (object instanceof List<?> list && !list.isEmpty())
    {
      if (list.stream().allMatch(Predicates.instanceOf(UUID.class)::apply))
      {
        @SuppressWarnings("all")
        List<UUID> uuids = (List<UUID>) list;
        List<Entity> entities = new ArrayList<>();
        for (UUID uuid : uuids)
        {
          Entity entity = Bukkit.getEntity(uuid);
          if (entity != null)
          {
            entities.add(entity);
          }
        }
        if (!entities.isEmpty())
        {
          return senderComponent(entities, defaultColor);
        }
      }
      if (list.stream().allMatch(Predicates.instanceOf(Entity.class)::apply))
      {
        @SuppressWarnings("all")
        List<Entity> entities = (List<Entity>) list;
        if (entities.size() == 1)
        {
          return senderComponent(entities.get(0), defaultColor);
        }
        boolean isPlayer = entities.stream().allMatch(Predicates.instanceOf(Player.class)::apply);
        Component component = ComponentUtil.createTranslate(isPlayer ? "플레이어 %s명" : "개체 %s개", list.size()).color(defaultColor);
        Component hover = Component.empty();
        for (int i = 0; i < entities.size(); i++)
        {
          Entity entity = entities.get(i);
          if (i == 20)
          {
            hover = hover.append(ComponentUtil.createTranslate("&7&o" + (isPlayer ? "외 %s명 더..." : "container.shulkerBox.more"), entities.size() - 20));
            break;
          }
          Location location = entity.getLocation();
          Component senderComponent = senderComponent(entity, defaultColor);
          Component locationComponent = locationComponent(location);
          Component typeComponent = Component.translatable(entity.getType().translationKey());
          if (entity instanceof Player player && !player.getName().equals(ComponentUtil.serialize(player.displayName())))
          {
            hover = hover.append(ComponentUtil.createTranslate("&7%s@%s [%s/%s]", senderComponent
                    , locationComponent, player.getName(), typeComponent
            ));
          }
          else
          {
            hover = hover.append(ComponentUtil.createTranslate("&7%s@%s [%s]", senderComponent
                    , locationComponent, typeComponent
            ));
          }
          if (i + 1 != entities.size())
          {
            hover = hover.append(Component.text("\n"));
          }
        }
        component = component.hoverEvent(HoverEvent.showText(hover));
        return component;
      }
    }
    if (object instanceof ConsoleCommandSender)
    {
      Component component = ComponentUtil.createTranslate("&d서버");
      Component hover = Component.empty().append(ComponentUtil.createTranslate("&d서버"));
      hover = hover.append(Component.text("\n"));
      hover = hover.append(ComponentUtil.createTranslate("유형 : %s", Constant.THE_COLOR_HEX + Bukkit.getServer().getName()));
      hover = hover.append(Component.text("\n"));
      hover = hover.append(ComponentUtil.createTranslate("버전 : %s", Constant.THE_COLOR_HEX + Bukkit.getServer().getVersion()));
      hover = hover.append(Component.text("\n"));
      hover = hover.append(ComponentUtil.createTranslate("접속 인원 : %s", Constant.THE_COLOR_HEX + Bukkit.getServer().getOnlinePlayers().size()));
      return component.hoverEvent(hover).clickEvent(ClickEvent.runCommand("/version"));
    }
    else if (object instanceof BlockCommandSender blockCommandSender)
    {
      Block block = blockCommandSender.getBlock();
      Location location = block.getLocation();
      String name = blockCommandSender.getName();
      Component component = ComponentUtil.create(name);
      if (component.color() == null)
      {
        component = component.color(NamedTextColor.LIGHT_PURPLE);
      }

      Component hover = ComponentUtil.create(name);
      hover = hover.append(Component.text("\n"));
      hover = hover.append(ComponentUtil.createTranslate("유형 : %s", block.getType()));
      hover = hover.append(Component.text("\n"));
      hover = hover.append(ComponentUtil.createTranslate("좌표 : %s", location));
      int x = location.getBlockX(), y = location.getBlockY(), z = location.getBlockZ();
      return component.hoverEvent(hover).clickEvent(ClickEvent.suggestCommand("/atp @s " + location.getWorld().getName() + " " + x + " " + (y + 1) + " " + z + " ~ 180"));
    }
    else if (object instanceof Entity entity)
    {
      Component nameComponent;
      if (entity instanceof Player player)
      {
        nameComponent = player.displayName().hoverEvent(null).clickEvent(null);
        if (Cucumbery.using_Vault_Chat)
        {
          try
          {
            String prefix = Cucumbery.chat.getPlayerPrefix(player), suffix = Cucumbery.chat.getPlayerSuffix(player);
            if (prefix != null)
            {
              nameComponent = ComponentUtil.create(false, prefix, nameComponent);
            }
            if (suffix != null)
            {
              nameComponent = ComponentUtil.create(false, nameComponent, suffix);
            }
          }
          catch (Exception e)
          {
            e.printStackTrace();
          }
        }
      }
      else
      {
        nameComponent = entity.customName();
        if (nameComponent != null)
        {
          nameComponent = nameComponent.hoverEvent(null).clickEvent(null);
        }
      }
      if (entity instanceof FallingBlock fallingBlock && nameComponent == null)
      {
        nameComponent = itemName(fallingBlock.getBlockData().getMaterial());
      }
      if (nameComponent == null)
      {
        nameComponent = Component.translatable(entity.getType().translationKey());
      }
      UUID uuid = entity.getUniqueId();
      String click = "/minecraft:tp " + uuid;
      Component hover = Component.empty().append(nameComponent);
      if (entity instanceof Player player)
      {
        String name = player.getName();
        GameMode gameMode = player.getGameMode();
        hover = hover
                .append(Component.text("\n"))
                .append(ComponentUtil.createTranslate("ID : %s", Constant.THE_COLOR_HEX + name))
                .append(Component.text("\n"))
                .append(ComponentUtil.createTranslate("UUID : %s", Constant.THE_COLOR_HEX + uuid))
                .append(Component.text("\n"))
                .append(ComponentUtil.createTranslate("접속 횟수 : %s회", Constant.THE_COLOR_HEX + player.getStatistic(Statistic.LEAVE_GAME) + 1))
                .append(Component.text("\n"))
                .append(ComponentUtil.createTranslate("플레이 시간 : %s", Constant.THE_COLOR_HEX + Method.timeFormatMilli(player.getStatistic(Statistic.PLAY_ONE_MINUTE) * 50L, false)))
                .append(Component.text("\n"))
                .append(ComponentUtil.createTranslate("게임 모드 : %s", ComponentUtil.createTranslate(Constant.THE_COLOR_HEX + "gameMode." + gameMode.toString().toLowerCase())))
        ;
        click = "/socialmenu " + name;
      }
      if (!(object instanceof Player))
      {
        hover = hover.append(Component.text("\n"));
        hover = hover.append(ComponentUtil.createTranslate("유형 : %s", ComponentUtil.createTranslate(Constant.THE_COLOR_HEX + entity.getType().translationKey())));
        hover = hover.append(Component.text("\n"));
        hover = hover.append(ComponentUtil.createTranslate("UUID : %s", Constant.THE_COLOR_HEX + uuid));
      }
      if (entity instanceof Item item)
      {
        ItemStack itemStack = item.getItemStack();
        List<Component> lore = ItemStackUtil.getItemInfoAsComponents(itemStack, ComponentUtil.createTranslate("&e[아이템]"), true);
        for (Component lor : lore)
        {
          hover = hover.append(Component.text("\n"));
          hover = hover.append(lor);
        }
      }
      if (entity instanceof ItemFrame itemFrame)
      {
        ItemStack item = itemFrame.getItem();
        if (ItemStackUtil.itemExists(item))
        {
          List<Component> lore = ItemStackUtil.getItemInfoAsComponents(item, ComponentUtil.createTranslate("&e[아이템]"), true);
          for (Component lor : lore)
          {
            hover = hover.append(Component.text("\n"));
            hover = hover.append(lor);
          }
        }
      }
      if (entity instanceof FallingBlock fallingBlock)
      {
        BlockData blockData = fallingBlock.getBlockData();
        Material type = blockData.getMaterial();
        ItemStack itemStack = ItemStackUtil.loredItemStack(type);
        ItemMeta itemMeta = itemStack.getItemMeta();
        if (itemMeta instanceof BlockDataMeta blockDataMeta)
        {
          blockDataMeta.setBlockData(blockData);
          itemStack.setItemMeta(blockDataMeta);
        }
        List<Component> lore = ItemStackUtil.getItemInfoAsComponents(itemStack, ComponentUtil.createTranslate("&e[아이템]"), true);
        for (Component lor : lore)
        {
          hover = hover.append(Component.text("\n"));
          hover = hover.append(lor);
        }
      }
      if (entity instanceof Villager villager)
      {
        Profession profession = villager.getProfession();
        String key = Constant.THE_COLOR_HEX + "entity.minecraft.villager." + profession.toString().toLowerCase();
        if (profession != Profession.NONE)
        {
          hover = hover.append(Component.text("\n"));
          hover = hover.append(ComponentUtil.createTranslate("직업 : %s", ComponentUtil.createTranslate(key)));
        }
        int villagerExperience = villager.getVillagerExperience();
        if (villagerExperience > 0)
        {
          hover = hover.append(Component.text("\n"));
          hover = hover.append(ComponentUtil.createTranslate("레벨 : %s (%s)",
                  Constant.THE_COLOR_HEX + villager.getVillagerLevel(), Constant.THE_COLOR_HEX + villagerExperience));
        }
      }
      if (entity instanceof Tameable tameable && tameable.isTamed())
      {
        AnimalTamer animalTamer = tameable.getOwner();
        if (animalTamer != null)
        {
          hover = hover.append(Component.text("\n"));
          Component component = ComponentUtil.createTranslate("주인 : %s", animalTamer);
          hover = hover.append(component);
        }
      }
      if (entity instanceof ExperienceOrb experienceOrb)
      {
        int experience = experienceOrb.getExperience();
        hover = hover.append(Component.text("\n"));
        hover = hover.append(ComponentUtil.createTranslate("경험치 : %s", Constant.THE_COLOR_HEX + experience));
      }
      Location location = entity.getLocation();
      hover = hover.append(Component.text("\n"));
      hover = hover.append(ComponentUtil.createTranslate("좌표 : %s", location));
      if (entity instanceof Damageable damageable && entity instanceof Attributable attributable)
      {
        double health = damageable.getHealth();
        AttributeInstance maxHealthInstance = attributable.getAttribute(Attribute.GENERIC_MAX_HEALTH);
        if (maxHealthInstance != null)
        {
          double maxHealth = maxHealthInstance.getValue();
          String color = Method2.getPercentageColor(health, maxHealth);
          String healthDisplay = color + Constant.Sosu2.format(health);
          String maxHealthDisplay = "g255;" + Constant.Sosu2.format(maxHealth);
          hover = hover.append(Component.text("\n"));
          hover = hover.append(ComponentUtil.createTranslate("HP : %s", ComponentUtil.createTranslate("&7%s / %s", healthDisplay, maxHealthDisplay)));
        }
      }
      if (defaultColor != null && nameComponent.color() == null)
      {
        nameComponent = nameComponent.color(defaultColor);
      }
      if (object instanceof Player player)
      {
        if (player.isOp() || player.hasPermission("asdf"))
        {
          hover = hover
                  .append(Component.text("\n"))
                  .append(ComponentUtil.createTranslate("#52ee52;관리자입니다."));
        }
        hover = hover
                .append(Component.text("\n"))
                .append(ComponentUtil.create(Constant.ITEM_LORE_SEPARATOR))
                .append(Component.text("\n"))
                .append(ComponentUtil.createTranslate("클릭하여 소셜 메뉴 열기 : %s", "&7/socialmenu " + player.getName()));
        nameComponent = nameComponent.clickEvent(ClickEvent.runCommand(click));
      }
      else
      {
        nameComponent = nameComponent.clickEvent(ClickEvent.suggestCommand(click));
      }
      nameComponent = nameComponent.hoverEvent(hover);
      return nameComponent;
    }
    else if (object instanceof OfflinePlayer offlinePlayer)
    {
      UUID uuid = offlinePlayer.getUniqueId();
      String displayName = Method.getDisplayName(offlinePlayer);
      Component component = ComponentUtil.create(displayName);
      if (component.color() == null)
      {
        component = component.color(Constant.THE_COLOR);
      }
      Component hover = ComponentUtil.create(displayName);
      hover = hover.append(Component.text("\n"));
      hover = hover.append(ComponentUtil.createTranslate("UUID : %s", Constant.THE_COLOR_HEX + uuid));
      hover = hover.append(Component.text("\n"));
      hover = hover.append(Component.translatable("chat.copy.click"));
      component = component.hoverEvent(HoverEvent.showText(hover));
      component = component.clickEvent(ClickEvent.copyToClipboard(uuid.toString()));
      return component;
    }
    return Component.empty();
  }

  @NotNull
  public static Component locationComponent(@NotNull Location location)
  {
    World world = location.getWorld();
    String worldName = world.getName();
    Component worldComponent = ComponentUtil.create(world);
    double x = location.getX(), y = location.getY(), z = location.getZ(), yaw = location.getYaw(), pitch = location.getPitch();
    Component xComponent = Component.text(Constant.Sosu2.format(x)).color(Constant.THE_COLOR);
    Component yComponent = Component.text(Constant.Sosu2.format(y)).color(Constant.THE_COLOR);
    Component zComponent = Component.text(Constant.Sosu2.format(z)).color(Constant.THE_COLOR);
    Component yawComponent = Component.text(Constant.Sosu2.format(yaw)).color(Constant.THE_COLOR);
    Component pitchComponent = Component.text(Constant.Sosu2.format(pitch)).color(Constant.THE_COLOR);
    return createTranslate("&7%s, %s, %s, %s", worldComponent, xComponent, yComponent, zComponent)
            .hoverEvent(ComponentUtil.createTranslate(
                    "클릭하여 %s, %s, %s, %s, %s, %s 좌표로 텔레포트합니다.",
                    Constant.THE_COLOR_HEX + worldName, xComponent, yComponent, zComponent, yawComponent, pitchComponent
            ))
            .clickEvent(ClickEvent.suggestCommand("/atp @s " + worldName + " " + x + " " + y + " " + z + " " + yaw + " " + pitch));
  }

  @NotNull
  public static Component itemStackComponent(@NotNull ItemStack itemStack)
  {
    return itemStackComponent(itemStack, itemStack.getAmount(), null);
  }

  @NotNull
  @SuppressWarnings("unused")
  public static Component itemStackComponent(@NotNull ItemStack itemStack, int amount)
  {
    return itemStackComponent(itemStack, amount, null);
  }

  @NotNull
  public static Component itemStackComponent(@NotNull ItemStack itemStack, @Nullable TextColor defaultColor)
  {
    return itemStackComponent(itemStack, itemStack.getAmount(), defaultColor);
  }

  @NotNull
  public static Component itemStackComponent(@NotNull ItemStack itemStack, int amount, @Nullable TextColor defaultColor)
  {
    if (defaultColor == null)
    {
      defaultColor = NamedTextColor.GRAY;
    }
    itemStack = itemStack.clone();
    NBTItem nbtItem = new NBTItem(itemStack);
    for (String key : nbtItem.getKeys())
    {
      if (!Method.equals(key, "display", "Enchantments", "Damage", "HideFlags", "Color", "CustomModelData", "StoredEnchantments", "Potion", "Items", "SkullOwner"))
      {
        nbtItem.removeKey(key);
      }
    }
    itemStack.setItemMeta(nbtItem.getItem().getItemMeta());
    Component itemName = itemName(itemStack, defaultColor);
    if (itemName instanceof TextComponent textComponent && textComponent.content().equals(""))
    {
      List<Component> children = new ArrayList<>(itemName.children());
      for (int i = 0; i < children.size(); i++)
      {
        Component child = children.get(i).hoverEvent(itemStack.asHoverEvent());
        children.set(i, child);
      }
      itemName = itemName.children(children);
    }
    else
    {
      itemName = itemName.hoverEvent(itemStack.asHoverEvent());
    }
    if (amount == 1 && itemStack.getType().getMaxStackSize() == 1)
    {
      return itemName;
    }

    return ComponentUtil.createTranslate("&f%s %s개", itemName, amount).color(defaultColor).hoverEvent(itemStack.asHoverEvent());
  }

  /**
   * 컴포넌트를 생성합니다.
   *
   * @param objects 문자열 또는 다른 컴포넌트
   * @return 컴포넌트
   */
  @NotNull
  @SuppressWarnings("all")
  public static Component create(@NotNull Object... objects)
  {
    Component component = Component.empty();
    boolean n2s = objects.length > 0 && !objects[0].equals(false);
    for (Object object : objects)
    {
      Component senderComponent = ComponentUtil.senderComponent(object);
      if (!senderComponent.equals(Component.empty()))
      {
        component = component.append(senderComponent);
        continue;
      }
      if (object instanceof Component)
      {
        Component concat = (Component) object;
        if (concat.equals(Component.empty()))
        {
          continue;
        }
        component = component.append(concat.children(Collections.emptyList()));
        for (Component child : concat.children())
        {
          component = component.append(ComponentUtil.create(child));
        }
      }
      else if (object instanceof List<?> list)
      {
        for (Object c : list)
        {
          Component concat = ComponentUtil.create(c);
          component = component.append(concat);
        }
      }
      else if (object instanceof Component[] components)
      {
        for (Component c : components)
        {
          Component concat = (Component) c;
          component = component.append(ComponentUtil.create(concat));
        }
      }
      else if (object instanceof Material material)
      {
        ItemStack itemStack = ItemStackUtil.loredItemStack(material);
        Component concat = ComponentUtil.itemName(itemStack, TextColor.color(255, 204, 0));
        concat = concat.hoverEvent(itemStack.asHoverEvent());
        component = component.append(concat);
      }
      else if (object instanceof ItemStack itemStack)
      {
        itemStack = itemStack.clone();
        NBTItem nbtItem = new NBTItem(itemStack);
        for (String key : nbtItem.getKeys())
        {
          if (!Method.equals(key, "display", "Enchantments", "Damage", "HideFlags", "Color", "CustomModelData", "StoredEnchantments", "Potion", "Items", "SkullOwner"))
          {
            nbtItem.removeKey(key);
          }
        }
        itemStack.setItemMeta(nbtItem.getItem().getItemMeta());
        Component concat = ComponentUtil.itemName(itemStack, TextColor.color(255, 204, 0));
        if (concat instanceof TextComponent textComponent && textComponent.content().equals(""))
        {
          List<Component> children = new ArrayList<>(concat.children());
          for (int i = 0; i < children.size(); i++)
          {
            Component child = children.get(i).hoverEvent(itemStack.asHoverEvent());
            children.set(i, child);
          }
          concat = concat.children(children);
        }
        else
        {
          concat = concat.hoverEvent(itemStack.asHoverEvent());
        }
        component = component.append(concat);
      }
      else if (object instanceof Prefix prefix)
      {
        Component concat = prefix.get();
        component = component.append(concat);
      }
      else if (object instanceof World world)
      {
        String worldName = Method.getWorldDisplayName(world);
        Component concat = ComponentUtil.create(worldName);
        if (concat.color() == null)
        {
          concat = concat.color(Constant.THE_COLOR);
        }
        concat = concat.clickEvent(ClickEvent.suggestCommand("/whatis " + world.getName()));
        int playerCount = world.getPlayerCount();
        Component hover = Component.empty().append(ComponentUtil.create(worldName));

        String environmentKey = switch (world.getEnvironment())
                {
                  case NORMAL -> "오버월드";
                  case NETHER -> "네더";
                  case THE_END -> "디 엔드";
                  case CUSTOM -> "사용자 지정";
                };
        hover = hover.append(Component.text("\n"));
        hover = hover.append(ComponentUtil.createTranslate("유형 : %s", Component.translatable(environmentKey).color(Constant.THE_COLOR)));
        hover = hover.append(Component.text("\n"));
        hover = hover.append(ComponentUtil.createTranslate("ID : %s", Constant.THE_COLOR_HEX + world.getName()));
        hover = hover.append(Component.text("\n"));
        hover = hover.append(ComponentUtil.createTranslate("UUID : %s", Constant.THE_COLOR_HEX + world.getUID()));
        hover = hover.append(Component.text("\n"));
        Location spawnLocation = world.getSpawnLocation();
        hover = hover.append(ComponentUtil.createTranslate("스폰 포인트 : %s",
                ComponentUtil.createTranslate("&7%s, %s, %s", Constant.THE_COLOR_HEX + spawnLocation.getBlockX(), Constant.THE_COLOR_HEX + spawnLocation.getBlockY(), Constant.THE_COLOR_HEX + spawnLocation.getBlockX())
        ));
        hover = hover.append(Component.text("\n"));
        hover = hover.append(ComponentUtil.createTranslate("난이도 : %s", ComponentUtil.createTranslate(Constant.THE_COLOR_HEX + world.getDifficulty().translationKey())));
        hover = hover.append(Component.text("\n"));
        hover = hover.append(ComponentUtil.createTranslate("Y축 제한 범위 : %s", Constant.THE_COLOR_HEX + world.getMinHeight() + "~" + world.getMaxHeight()));
        hover = hover.append(Component.text("\n"));
        hover = hover.append(ComponentUtil.createTranslate("플레이어 수 : %s", ComponentUtil.createTranslate("%s명", Constant.THE_COLOR_HEX + playerCount)));
        concat = concat.hoverEvent(hover.asHoverEvent());
        component = component.append(concat);
      }
      else if (object instanceof Location location)
      {
        component = component.append(locationComponent(location));
      }
      else if (Cucumbery.using_NoteBlockAPI && object instanceof Song song)
      {
        String display = song.getPath().getName();
        display = display.substring(0, display.length() - 4);
        Component concat = Component.text(display).color(Constant.THE_COLOR).hoverEvent(Component.translatable(display));
        component = component.append(concat);
      }
      else if (objects.length == 1 || !(object instanceof Boolean))
      {
        String string = object.toString();
        if (string.startsWith("translate:"))
        {
          string = string.substring(10);
          String[] split = MessageUtil.splitEscape(string, ';');
          string = split[0];
          String[] newArray = new String[split.length - 1];
          for (int i = 1; i < split.length; i++)
          {
            newArray[i - 1] = split[i];
          }
          split = newArray;
          Component concat = ComponentUtil.createTranslate(string, split);
          component = component.append(ComponentUtil.create(concat));
        }
        else if (string.startsWith("selector:"))
        {
          string = string.substring(9);
          component = component.append(Component.selector(string));
        }
        else if (string.startsWith("keybind:"))
        {
          string = string.substring(8);
          component = component.append(Component.keybind(string));
        }
        else
        {
          try
          {
            JSON_PARSER.parse(string);
            Component concat = GsonComponentSerializer.gson().deserialize(string);
            component = component.append(ComponentUtil.create(concat));
          }
          catch (Exception e)
          {
            Component concat = ComponentUtil.create2(string, n2s);
            component = component.append(ComponentUtil.create(concat));
          }
        }
      }
    }
    if (ComponentUtil.isPlainComponent(component))
    {
      List<Component> children = component.children();
      if (children.size() > 0)
      {
        if (!children.get(0).equals(Component.empty())
                && (
                ComponentUtil.isPlainComponent(children.get(0)) || children.size() == 1
        )
        )
        {
          component = children.get(0);
          for (int i = 1; i < children.size(); i++)
          {
            Component child = children.get(i);
            if (!child.equals(Component.empty()))
            {
              component = component.append(child);
            }
          }
        }
      }
    }
    List<Component> children = new ArrayList<>();
    for (Component child : component.children())
    {
      if (!child.equals(Component.empty()))
      {
        children.add(child);
      }
    }
    component = component.children(children);
    if (component instanceof TextComponent && ((TextComponent) component).content().equals(""))
    {
      List<Component> children2 = component.children();
      if (children2.size() == 1)
      {
        return children2.get(0);
      }
    }
    return component;
  }

  /**
   * 텍스트 컴포넌트를 생성합니다.
   *
   * @param value 컴포넌트의 문자열 값
   * @return 텍스트 컴포넌트
   */
  public static Component create2(String value)
  {
    return ComponentUtil.create2(value, true);
  }

  /**
   * 텍스트 컴포넌트를 생성합니다.
   *
   * @param value 컴포넌트의 문자열 값
   * @param n2s   색깔 코드 포맷 여부
   * @return 텍스트 컴포넌트
   */
  public static Component create2(String value, boolean n2s)
  {
    List<TextComponent> components = ComponentUtil.fromLegacyText(value, n2s);
    TextComponent component = Component.empty();
    for (TextComponent textComponent : components)
    {
      component = component.append(textComponent);
    }
    return component;
  }

  /**
   * 번역 컴포넌트를 생성합니다.
   *
   * @param key 컴포넌트의 키 값
   * @return 번역 컴포넌트
   */
  public static TranslatableComponent createTranslate(String key)
  {
    return ComponentUtil.createTranslate(key, true);
  }

  @NotNull
  private static TranslatableComponent yeet(@NotNull String key, @NotNull TranslatableComponent component)
  {
    String vanillaKey = Variable.lang.getString(key.replace(".", "-"));
    boolean needChange = false;
    if (vanillaKey != null)
    {
      for (MessageUtil.ConsonantType type : MessageUtil.ConsonantType.values())
      {
        if (vanillaKey.contains(type.toString()))
        {
          needChange = true;
          break;
        }
      }
      if (needChange)
      {
        String serial = serialize(component);
        String editKey = yeet2(vanillaKey, serial);
        if (!editKey.equals(vanillaKey))
        {
          component = component.key(editKey);
        }
      }
    }
    else
    {
      for (MessageUtil.ConsonantType type : MessageUtil.ConsonantType.values())
      {
        if (key.contains(type.toString()))
        {
          needChange = true;
          break;
        }
      }
      if (needChange)
      {
        String serial = serialize(component);
        String editKey = yeet2(key, serial);
        if (!editKey.equals(key))
        {
          component = component.key(editKey);
        }
      }
    }
    return component;
  }

  @NotNull
  private static String yeet2(@NotNull String key, @NotNull String serial)
  {
    Matcher matcher = yeet.matcher(key);
    String[] split = serial.split("이\\(가\\)|\\(이\\)가|가\\(이\\)|\\(가\\)이|을\\(를\\)|\\(을\\)를|를\\(을\\)|\\(를\\)을|와\\(과\\)|\\(와\\)과|과\\(와\\)|\\(과\\)와|은\\(는\\)|\\(은\\)는|는\\(은\\)|\\(는\\)은|\\(으\\)로|으\\(로\\)|\\(이\\)라|이\\(라\\)");
    int loop = 0;
    while (matcher.find())
    {
      try
      {
        String a3 = matcher.group(3);
        String replacer = MessageUtil.getFinalConsonant(split[loop], MessageUtil.ConsonantType.valueOf(a3.replace("(", "").replace(")", "")));
        if (!a3.equals(replacer))
        {
          key = key.replaceFirst(a3.replace("(", "\\(").replace(")", "\\)"), replacer);
        }
        loop++;
      }
      catch (Exception ignored)
      {
      }
    }
    return key;
  }

  /**
   * 번역 컴포넌트를 생성합니다.
   *
   * @param key  컴포넌트의 키 값
   * @param args 포맷할 매개 변수
   * @return 번역 컴포넌트
   */
  @NotNull
  @SuppressWarnings("all")
  public static TranslatableComponent createTranslate(@NotNull String key, @NotNull Object... args)
  {
    boolean n2s = args.length > 0 && !args[0].equals(false);
    TranslatableComponent component = ComponentUtil.fromLegacyTextTranslate(key, n2s);
    List<Component> componentArgs = new ArrayList<>();
    for (Object obj : args)
    {
      Component senderComponent = ComponentUtil.senderComponent(obj);
      if (!senderComponent.equals(Component.empty()))
      {
        if (senderComponent.decoration(TextDecoration.ITALIC) == TextDecoration.State.FALSE)
        {
          senderComponent = senderComponent.decoration(TextDecoration.ITALIC, TextDecoration.State.NOT_SET);
        }
        componentArgs.add(senderComponent);
        continue;
      }
      if (obj instanceof List<?> list)
      {
        for (Object o : list)
        {
          Component arg = ComponentUtil.create(n2s, o);
          if (arg.decoration(TextDecoration.ITALIC) == TextDecoration.State.FALSE)
          {
            arg = arg.decoration(TextDecoration.ITALIC, TextDecoration.State.NOT_SET);
          }
          componentArgs.add(arg);
        }
      }
      if (obj instanceof Object[] array)
      {
        for (Object value : array)
        {
          componentArgs.add(ComponentUtil.create(value));
        }
      }
      if (!(obj instanceof Boolean))
      {
        Component arg = ComponentUtil.create(n2s, obj);
        if (arg.decoration(TextDecoration.ITALIC) == TextDecoration.State.FALSE)
        {
          arg = arg.decoration(TextDecoration.ITALIC, TextDecoration.State.NOT_SET);
        }
        componentArgs.add(arg);
      }
    }
    Component[] ch = new Component[component.args().size()];
    ch = componentArgs.toArray(ch);
    component = component.args(componentArgs);
    component = yeet(component.key(), component);
    return component;
  }

  @SuppressWarnings("unused")
  private static List<TextComponent> fromLegacyText(@NotNull String message)
  {
    return fromLegacyText(message, true);
  }

  /**
   * 구버전의 텍스트를 컴포넌트로 변환합니다.
   *
   * @param message 구버전 텍스트
   * @return 컴포넌트
   */
  private static List<TextComponent> fromLegacyText(@NotNull String message, boolean n2s)
  {
    if (n2s)
    {
      message = MessageUtil.n2s(message);
    }
    List<TextComponent> components = new ArrayList<>();
    StringBuilder builder = new StringBuilder();
    TextComponent component = Component.empty();
    Matcher matcher = URL.matcher(message);
    for (int i = 0; i < message.length(); ++i)
    {
      char c = message.charAt(i);
      TextComponent old;
      if (c == 167)
      {
        ++i;
        if (i >= message.length())
        {
          break;
        }

        c = message.charAt(i);
        if (c >= 'A' && c <= 'Z')
        {
          c = (char) (c + 32);
        }

        @Nullable ChatColor format;
        if (c == 'x' && i + 12 < message.length())
        {
          StringBuilder hex = new StringBuilder("#");

          for (int j = 0; j < 6; ++j)
          {
            hex.append(message.charAt(i + 2 + j * 2));
          }
          try
          {
            format = ChatColor.of(hex.toString());
          }
          catch (IllegalArgumentException var11)
          {
            format = null;
          }
          i += 12;
        }
        else
        {
          format = ChatColor.getByChar(c);
        }

        if (format != null)
        {
          if (!builder.isEmpty())
          {
            old = component;
            old = old.content(builder.toString());
            builder = new StringBuilder();
            components.add(old);
          }
          if (format == ChatColor.BOLD)
          {
            component = component.decoration(TextDecoration.BOLD, true);
          }
          else if (format == ChatColor.ITALIC)
          {
            component = component.decoration(TextDecoration.ITALIC, true);
          }
          else if (format == ChatColor.UNDERLINE)
          {
            component = component.decoration(TextDecoration.UNDERLINED, true);
          }
          else if (format == ChatColor.STRIKETHROUGH)
          {
            component = component.decoration(TextDecoration.STRIKETHROUGH, true);
          }
          else if (format == ChatColor.MAGIC)
          {
            component = component.decoration(TextDecoration.OBFUSCATED, true);
          }
          else if (format == ChatColor.RESET)
          {
            component = Component.empty();
            component = component.color(null);
            component = component.decoration(TextDecoration.BOLD, TextDecoration.State.NOT_SET);
            component = component.decoration(TextDecoration.ITALIC, TextDecoration.State.NOT_SET);
            component = component.decoration(TextDecoration.UNDERLINED, TextDecoration.State.NOT_SET);
            component = component.decoration(TextDecoration.STRIKETHROUGH, TextDecoration.State.NOT_SET);
            component = component.decoration(TextDecoration.OBFUSCATED, TextDecoration.State.NOT_SET);
          }
          else
          {
            component = Component.empty();
            Color color = format.getColor();
            component = component.color(TextColor.color(color.getRed(), color.getGreen(), color.getBlue()));
            component = component.decoration(TextDecoration.BOLD, TextDecoration.State.NOT_SET);
            component = component.decoration(TextDecoration.ITALIC, false);
            component = component.decoration(TextDecoration.UNDERLINED, TextDecoration.State.NOT_SET);
            component = component.decoration(TextDecoration.STRIKETHROUGH, TextDecoration.State.NOT_SET);
            component = component.decoration(TextDecoration.OBFUSCATED, TextDecoration.State.NOT_SET);
          }
        }
        else
        {
          if (c == 'p' || c == 'q')
          {
            if (!builder.isEmpty())
            {
              old = component;
              old = old.content(builder.toString());
              builder = new StringBuilder();
              components.add(old);
            }

            switch (c)
            {
              case 'p' -> component = component.color(component.color() != null ? NamedTextColor.WHITE : null);
              case 'q' -> {
                component = component.decoration(TextDecoration.BOLD, component.decoration(TextDecoration.BOLD) == TextDecoration.State.TRUE ? TextDecoration.State.FALSE : TextDecoration.State.NOT_SET);
                component = component.decoration(TextDecoration.ITALIC, component.decoration(TextDecoration.ITALIC) == TextDecoration.State.TRUE ? TextDecoration.State.FALSE : TextDecoration.State.NOT_SET);
                component = component.decoration(TextDecoration.UNDERLINED, component.decoration(TextDecoration.UNDERLINED) == TextDecoration.State.TRUE ? TextDecoration.State.FALSE : TextDecoration.State.NOT_SET);
                component = component.decoration(TextDecoration.STRIKETHROUGH, component.decoration(TextDecoration.STRIKETHROUGH) == TextDecoration.State.TRUE ? TextDecoration.State.FALSE : TextDecoration.State.NOT_SET);
                component = component.decoration(TextDecoration.OBFUSCATED, component.decoration(TextDecoration.OBFUSCATED) == TextDecoration.State.TRUE ? TextDecoration.State.FALSE : TextDecoration.State.NOT_SET);
              }
            }
          }
        }

      }
      else
      {
        int pos = message.indexOf(32, i);
        if (pos == -1)
        {
          pos = message.length();
        }

        if (matcher.region(i, pos).find())
        {
          if (!builder.isEmpty())
          {
            old = component;
            old = old.content(builder.toString());
            builder = new StringBuilder();
            components.add(old);
          }

          old = component;
          String urlString = message.substring(i, pos);
          component = component.content(urlString);
          String url = urlString.startsWith("http") ? urlString : "http://" + urlString;
          component = component.clickEvent(ClickEvent.openUrl(url));
          component = component.hoverEvent(HoverEvent.showText(ComponentUtil.createTranslate("클릭하여 %s 주소로 연결합니다.", Component.text(url).color(NamedTextColor.YELLOW))));
          components.add(component.decoration(TextDecoration.UNDERLINED, TextDecoration.State.TRUE));
          i += pos - i - 1;
          component = old;
        }
        else
        {
          builder.append(c);
        }
      }
    }

    component = component.content(builder.toString());
    components.add(component);
    return components;
  }

/*
  private static TranslatableComponent fromLegacyTextTranslate(@NotNull String message)
  {
    return fromLegacyTextTranslate(message, true);
  }*/

  @SuppressWarnings("all")
  private static TranslatableComponent fromLegacyTextTranslate(@NotNull String message, boolean n2s)
  {
    if (n2s)
    {
      message = MessageUtil.n2s(message);
    }
    TranslatableComponent components = null;
    StringBuilder builder = new StringBuilder();
    TranslatableComponent component = Component.translatable("");
    for (int i = 0; i < message.length(); ++i)
    {
      char c = message.charAt(i);
      TranslatableComponent old;
      if (c == 167)
      {
        ++i;
        if (i >= message.length())
        {
          break;
        }

        c = message.charAt(i);
        if (c >= 'A' && c <= 'Z')
        {
          c = (char) (c + 32);
        }
        ChatColor format;
        if (c == 'x' && i + 12 < message.length())
        {
          StringBuilder hex = new StringBuilder("#");

          for (int j = 0; j < 6; ++j)
          {
            hex.append(message.charAt(i + 2 + j * 2));
          }

          try
          {
            format = ChatColor.of(hex.toString());
          }
          catch (IllegalArgumentException var11)
          {
            format = null;
          }
          i += 12;
        }
        else
        {
          format = ChatColor.getByChar(c);
        }

        if (format != null)
        {
          if (builder.length() > 0)
          {
            old = component;
            old = old.key(builder.toString());
            builder = new StringBuilder();
            if (components == null)
            {
              components = old;
            }
            else
            {
              components = components.append(old);
            }
          }

          if (format == ChatColor.BOLD)
          {
            component = component.decoration(TextDecoration.BOLD, true);
          }
          else if (format == ChatColor.ITALIC)
          {
            component = component.decoration(TextDecoration.ITALIC, true);
          }
          else if (format == ChatColor.UNDERLINE)
          {
            component = component.decoration(TextDecoration.UNDERLINED, true);
          }
          else if (format == ChatColor.STRIKETHROUGH)
          {
            component = component.decoration(TextDecoration.STRIKETHROUGH, true);
          }
          else if (format == ChatColor.MAGIC)
          {
            component = component.decoration(TextDecoration.OBFUSCATED, true);
          }
          else if (format == ChatColor.RESET)
          {
            component = Component.translatable("");
            component = component.color(null);
            component = component.decoration(TextDecoration.BOLD, TextDecoration.State.NOT_SET);
            component = component.decoration(TextDecoration.ITALIC, TextDecoration.State.NOT_SET);
            component = component.decoration(TextDecoration.UNDERLINED, TextDecoration.State.NOT_SET);
            component = component.decoration(TextDecoration.STRIKETHROUGH, TextDecoration.State.NOT_SET);
            component = component.decoration(TextDecoration.OBFUSCATED, TextDecoration.State.NOT_SET);
          }
          else
          {
            component = Component.translatable("");
            Color color = format.getColor();
            component = component.color(TextColor.color(color.getRed(), color.getGreen(), color.getBlue()));
            component = component.decoration(TextDecoration.BOLD, TextDecoration.State.NOT_SET);
            component = component.decoration(TextDecoration.ITALIC, false);
            component = component.decoration(TextDecoration.UNDERLINED, TextDecoration.State.NOT_SET);
            component = component.decoration(TextDecoration.STRIKETHROUGH, TextDecoration.State.NOT_SET);
            component = component.decoration(TextDecoration.OBFUSCATED, TextDecoration.State.NOT_SET);
          }
        }
        else
        {
          if (c == 'p' || c == 'q')
          {
            if (builder.length() > 0)
            {
              old = component;
              old = old.key(builder.toString());
              builder = new StringBuilder();
              if (components == null)
              {
                components = old;
              }
              else
              {
                components = components.append(old);
              }
            }


            switch (c)
            {
              case 'p' -> component = component.color(component.color() != null ? NamedTextColor.WHITE : null);
              case 'q' -> {
                component = component.decoration(TextDecoration.BOLD, component.decoration(TextDecoration.BOLD) == TextDecoration.State.TRUE ? TextDecoration.State.FALSE : TextDecoration.State.NOT_SET);
                component = component.decoration(TextDecoration.ITALIC, component.decoration(TextDecoration.ITALIC) == TextDecoration.State.TRUE ? TextDecoration.State.FALSE : TextDecoration.State.NOT_SET);
                component = component.decoration(TextDecoration.UNDERLINED, component.decoration(TextDecoration.UNDERLINED) == TextDecoration.State.TRUE ? TextDecoration.State.FALSE : TextDecoration.State.NOT_SET);
                component = component.decoration(TextDecoration.STRIKETHROUGH, component.decoration(TextDecoration.STRIKETHROUGH) == TextDecoration.State.TRUE ? TextDecoration.State.FALSE : TextDecoration.State.NOT_SET);
                component = component.decoration(TextDecoration.OBFUSCATED, component.decoration(TextDecoration.OBFUSCATED) == TextDecoration.State.TRUE ? TextDecoration.State.FALSE : TextDecoration.State.NOT_SET);
              }
            }
          }
        }

      }
      else
      {
        builder.append(c);
      }
    }

    component = component.key(builder.toString());
    if (components == null)
    {
      components = component;
    }
    else
    {
      components = components.append(component);
    }
    return components;
  }

  @NotNull
  public static String serializeAsJson(@NotNull Component component)
  {
    return GsonComponentSerializer.gson().serialize(component);
  }

  @NotNull
  public static String serialize(@NotNull Component component)
  {
    StringBuilder builder = new StringBuilder();
    TextColor textColor = component.color();
    boolean italic = component.decoration(TextDecoration.ITALIC) == TextDecoration.State.TRUE;
    boolean bold = component.decoration(TextDecoration.BOLD) == TextDecoration.State.TRUE;
    boolean underlined = component.decoration(TextDecoration.UNDERLINED) == TextDecoration.State.TRUE;
    boolean strikethrough = component.decoration(TextDecoration.STRIKETHROUGH) == TextDecoration.State.TRUE;
    boolean obfuscated = component.decoration(TextDecoration.OBFUSCATED) == TextDecoration.State.TRUE;
    String color = null;
    if (textColor != null)
    {
      if (textColor.equals(NamedTextColor.BLACK))
      {
        color = "§0";
      }
      else if (textColor.equals(NamedTextColor.DARK_BLUE))
      {
        color = "§1";
      }
      else if (textColor.equals(NamedTextColor.DARK_GREEN))
      {
        color = "§2";
      }
      else if (textColor.equals(NamedTextColor.DARK_AQUA))
      {
        color = "§3";
      }
      else if (textColor.equals(NamedTextColor.DARK_RED))
      {
        color = "§4";
      }
      else if (textColor.equals(NamedTextColor.DARK_PURPLE))
      {
        color = "§5";
      }
      else if (textColor.equals(NamedTextColor.GOLD))
      {
        color = "§6";
      }
      else if (textColor.equals(NamedTextColor.GRAY))
      {
        color = "§7";
      }
      else if (textColor.equals(NamedTextColor.DARK_GRAY))
      {
        color = "§8";
      }
      else if (textColor.equals(NamedTextColor.BLUE))
      {
        color = "§9";
      }
      else if (textColor.equals(NamedTextColor.GREEN))
      {
        color = "§a";
      }
      else if (textColor.equals(NamedTextColor.AQUA))
      {
        color = "§b";
      }
      else if (textColor.equals(NamedTextColor.RED))
      {
        color = "§c";
      }
      else if (textColor.equals(NamedTextColor.LIGHT_PURPLE))
      {
        color = "§d";
      }
      else if (textColor.equals(NamedTextColor.YELLOW))
      {
        color = "§e";
      }
      else if (textColor.equals(NamedTextColor.WHITE))
      {
        color = "§f";
      }
      else
      {
        color = "§x" + Method.format(textColor.asHexString().substring(1), "§");
      }
    }
    if (color != null)
    {
      builder.append(color);
    }
    if (italic)
    {
      builder.append("§o");
    }
    if (bold)
    {
      builder.append("§l");
    }
    if (underlined)
    {
      builder.append("§n");
    }
    if (strikethrough)
    {
      builder.append("§m");
    }
    if (obfuscated)
    {
      builder.append("§k");
    }
    if (component instanceof TextComponent textComponent)
    {
      builder.append(textComponent.content());
    }
    if (component instanceof TranslatableComponent translatableComponent)
    {
      String key = translatableComponent.key();
      String serial = Variable.lang.getString(key.replace(".", "-"));
      if (serial != null)
      {
        key = serial;
      }
      String test = key.replace("%%", "");
      int keyContains = (int) (p.matcher(test).results().count() + p2.matcher(test).results().count());
      List<Component> args = translatableComponent.args();
      List<String> argsSerial = new ArrayList<>();
      for (Component arg : args)
      {
        argsSerial.add(ComponentUtil.serialize(arg));
      }
      int argsSerialSize = argsSerial.size();
      if (argsSerialSize < keyContains)
      {
        for (int i = 0; i < keyContains - argsSerialSize; i++)
        {
          argsSerial.add("");
        }
      }
      try
      {
        key = String.format(key, argsSerial.toArray());
      }
      catch (Exception ignored)
      {
      }
      builder.append(key);
    }
    if (component instanceof SelectorComponent selectorComponent)
    {
      StringBuilder pattern = new StringBuilder(selectorComponent.pattern());
      try
      {
        List<Entity> entities = Bukkit.selectEntities(Bukkit.getConsoleSender(), pattern.toString());
        pattern = new StringBuilder();
        StringBuilder key = new StringBuilder("&7");
        List<Component> args = new ArrayList<>();
        for (Entity entity : entities)
        {
          key.append("%s, ");
          if (entity instanceof Player player)
          {
            args.add(Component.text(player.getName()));
          }
          else
          {
            args.add(Component.translatable(entity.getType().translationKey()));
          }
        }
        key = new StringBuilder(key.substring(0, key.length() - 2));
        Component translate = ComponentUtil.createTranslate(key.toString(), args);
        pattern.append(ComponentUtil.serialize(translate));
      }
      catch (Exception ignored)
      {
      }
      builder.append(pattern);
    }
    if (component instanceof KeybindComponent keybindComponent)
    {
      builder.append(ComponentUtil.serialize(Component.translatable(keybindComponent.keybind())));
    }
    List<Component> children = component.children();
    for (Component child : children)
    {
      builder.append(ComponentUtil.serialize(child));
    }
    return builder.toString();
  }

  public static boolean isPlainComponent(@NotNull Component component)
  {
    if (component instanceof TextComponent textComponent)
    {
      if (textComponent.content().equals(""))
      {
        return true;
      }
    }
    return component.color() == null
            && component.hoverEvent() == null
            && component.clickEvent() == null
            && component.decoration(TextDecoration.ITALIC) == TextDecoration.State.NOT_SET
            && component.decoration(TextDecoration.BOLD) == TextDecoration.State.NOT_SET
            && component.decoration(TextDecoration.STRIKETHROUGH) == TextDecoration.State.NOT_SET
            && component.decoration(TextDecoration.OBFUSCATED) == TextDecoration.State.NOT_SET
            && component.decoration(TextDecoration.UNDERLINED) == TextDecoration.State.NOT_SET;
  }
}
