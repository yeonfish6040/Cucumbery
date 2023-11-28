package com.jho5245.cucumbery.util.storage.component.util.sendercomponent;

import com.destroystokyo.paper.entity.villager.Reputation;
import com.destroystokyo.paper.entity.villager.ReputationType;
import com.jho5245.cucumbery.Cucumbery;
import com.jho5245.cucumbery.util.itemlore.ItemLore;
import com.jho5245.cucumbery.util.itemlore.TropicalFishLore;
import com.jho5245.cucumbery.util.no_groups.Method;
import com.jho5245.cucumbery.util.no_groups.Method2;
import com.jho5245.cucumbery.util.storage.component.ItemStackComponent;
import com.jho5245.cucumbery.util.storage.component.util.ComponentUtil;
import com.jho5245.cucumbery.util.storage.component.util.ItemNameUtil;
import com.jho5245.cucumbery.util.storage.data.Constant;
import com.jho5245.cucumbery.util.storage.no_groups.CustomConfig.UserData;
import com.jho5245.cucumbery.util.storage.no_groups.ItemStackUtil;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.TranslatableComponent;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.*;
import org.bukkit.attribute.Attributable;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.block.Biome;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.*;
import org.bukkit.entity.FishHook.HookState;
import org.bukkit.entity.Horse.Style;
import org.bukkit.entity.Panda.Gene;
import org.bukkit.entity.Parrot.Variant;
import org.bukkit.entity.Rabbit.Type;
import org.bukkit.entity.Villager.Profession;
import org.bukkit.entity.minecart.CommandMinecart;
import org.bukkit.entity.minecart.PoweredMinecart;
import org.bukkit.inventory.*;
import org.bukkit.inventory.meta.BlockDataMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.material.Colorable;
import org.bukkit.projectiles.ProjectileSource;
import org.bukkit.scoreboard.Team;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class EntityComponentUtil
{
  public static final String VAULT_DISPLAY_NAME_FORMATTER = "cucumbery-vault-display-name-format";
  /**
   * a String to distinguish if display name is formatted with team prefix/suffix or color.
   */
  public static final String TEAM_DISPLAY_NAME_FORMATTER = "cucumbery-team-display-name-format";
  @NotNull
  public static Component entityComponent(@Nullable Player p, @NotNull Entity entity, @Nullable TextColor defaultColor)
  {
    boolean tmiMode = p != null && UserData.ENTITY_HOVER_EVENT_TMI_MODE.getBoolean(p);
    Component nameComponent;
    Team team = Bukkit.getScoreboardManager().getMainScoreboard().getEntityTeam(entity);
    if (entity instanceof Player player)
    {
      nameComponent = player.displayName().hoverEvent(null).clickEvent(null);
      if (nameComponent instanceof TranslatableComponent translatableComponent && translatableComponent.args().size() == 4 && translatableComponent.args().get(3) instanceof TextComponent textComponent &&
              textComponent.content().equals(TEAM_DISPLAY_NAME_FORMATTER))
      {
        nameComponent = translatableComponent.args().get(1);
      }
      if (nameComponent instanceof TranslatableComponent translatableComponent && translatableComponent.args().size() == 4 && translatableComponent.args().get(3) instanceof TextComponent textComponent &&
              textComponent.content().equals(VAULT_DISPLAY_NAME_FORMATTER))
      {
        nameComponent = translatableComponent.args().get(1);
      }
      if (Cucumbery.using_Vault_Chat)
      {
        try
        {
          TranslatableComponent translatableComponent = ComponentUtil.translate("%s%s%s");
          List<Component> args = new ArrayList<>();
          String prefix = Cucumbery.chat.getPlayerPrefix(player), suffix = Cucumbery.chat.getPlayerSuffix(player);
          args.add(prefix != null ? ComponentUtil.create(prefix) : Component.empty());
          args.add(nameComponent);
          args.add(suffix != null ? ComponentUtil.create(suffix) : Component.empty());
          args.add(Component.text(VAULT_DISPLAY_NAME_FORMATTER));
          nameComponent = translatableComponent.args(args);
        }
        catch (Exception e)
        {
Cucumbery.getPlugin().getLogger().warning(          e.getMessage());
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
    if (nameComponent instanceof TranslatableComponent translatableComponent && translatableComponent.args().size() == 4 && translatableComponent.args().get(3) instanceof TextComponent textComponent &&
            textComponent.content().equals(TEAM_DISPLAY_NAME_FORMATTER))
    {
      nameComponent = translatableComponent.args().get(1);
    }
    if (nameComponent == null)
    {
      String key = entity.getType().translationKey();
      if (entity instanceof Creeper creeper && creeper.isPowered())
      {
        key = "충전된 크리퍼";
      }
      if (entity instanceof Rabbit rabbit && rabbit.getRabbitType() == Type.THE_KILLER_BUNNY)
      {
        key = "entity.minecraft.killer_bunny";
      }
      if (entity instanceof FallingBlock fallingBlock)
      {
        nameComponent = ItemNameUtil.itemName(fallingBlock.getBlockData().getMaterial());
      }
      else
      {
        nameComponent = ComponentUtil.translate(key);
      }
      if (entity instanceof MushroomCow mushroomCow && mushroomCow.getVariant() == MushroomCow.Variant.BROWN)
      {
        nameComponent = ComponentUtil.translate("%s %s", ComponentUtil.translate("color.minecraft.brown"), nameComponent);
      }
      if (entity instanceof Villager villager)
      {
        nameComponent = ComponentUtil.translate(entity.getType().translationKey() + "." + villager.getProfession().toString().toLowerCase());
      }
      if (entity instanceof Ageable ageable && !ageable.isAdult())
      {
        nameComponent = ComponentUtil.translate("%s %s", "아기", nameComponent);
      }
    }
    if (team != null)
    {
      try
      {
        TranslatableComponent translatableComponent = ComponentUtil.translate("%s%s%s");
        if (team.hasColor())
        {
          translatableComponent = translatableComponent.color(team.color());
        }
        List<Component> args = new ArrayList<>();
        args.add(team.prefix());
        args.add(nameComponent);
        args.add(team.suffix());
        args.add(Component.text(TEAM_DISPLAY_NAME_FORMATTER));
        nameComponent = translatableComponent.args(args);
      }
      catch (IllegalStateException ignored)
      {

      }
      catch (Exception e)
      {
Cucumbery.getPlugin().getLogger().warning(        e.getMessage());
      }
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
              .append(ComponentUtil.translate("ID : %s", Constant.THE_COLOR_HEX + name));
      if (tmiMode || Cucumbery.config.getBoolean("use-hover-event-for-entities.uuid"))
      {
        hover = hover
                .append(Component.text("\n"))
                .append(ComponentUtil.translate("UUID : %s", Constant.THE_COLOR_HEX + uuid));
      }
      if (tmiMode || Cucumbery.config.getBoolean("use-hover-event-for-entities.player-join-count"))
      {
        hover = hover
                .append(Component.text("\n"))
                .append(ComponentUtil.translate("접속 횟수 : %s회", Constant.THE_COLOR_HEX + (player.getStatistic(Statistic.LEAVE_GAME) + 1)));
      }
      if (tmiMode || Cucumbery.config.getBoolean("use-hover-event-for-entities.player-play-time"))
      {
        hover = hover
                .append(Component.text("\n"))
                .append(ComponentUtil.translate("플레이 시간 : %s", Constant.THE_COLOR_HEX + Method.timeFormatMilli(player.getStatistic(Statistic.PLAY_ONE_MINUTE) * 50L, false)));
      }
      if (tmiMode || Cucumbery.config.getBoolean("use-hover-event-for-entities.player-game-mode"))
      {
        hover = hover
                .append(Component.text("\n"))
                .append(ComponentUtil.translate("게임 모드 : %s", ComponentUtil.translate(Constant.THE_COLOR_HEX + "gameMode." + gameMode.toString().toLowerCase())));
      }
      click = "/socialmenu " + name;
    }
    if (!(entity instanceof Player))
    {
      String key = entity.getType().translationKey();
      if (entity instanceof Creeper creeper && creeper.isPowered())
      {
        key = "충전된 크리퍼";
      }
      if (entity instanceof Rabbit rabbit && rabbit.getRabbitType() == Type.THE_KILLER_BUNNY)
      {
        key = "entity.minecraft.killer_bunny";
      }
      Component typeComponent = ComponentUtil.translate(key);
      if (entity instanceof MushroomCow mushroomCow && mushroomCow.getVariant() == MushroomCow.Variant.BROWN)
      {
        typeComponent = ComponentUtil.translate("%s %s", ComponentUtil.translate("color.minecraft.brown"), typeComponent);
      }
      if (entity instanceof Ageable ageable && !ageable.isAdult())
      {
        typeComponent = ComponentUtil.translate("%s %s", "아기", typeComponent);
      }

      if (tmiMode || Cucumbery.config.getBoolean("use-hover-event-for-entities.entity-type"))
      {
        hover = hover
                .append(Component.text("\n"))
                .append(ComponentUtil.translate("유형 : %s", typeComponent.color(Constant.THE_COLOR)));
      }
      if (tmiMode || Cucumbery.config.getBoolean("use-hover-event-for-entities.uuid"))
      {
        hover = hover
                .append(Component.text("\n"))
                .append(ComponentUtil.translate("UUID : %s", Constant.THE_COLOR_HEX + uuid));
      }
    }
    if (entity instanceof Rabbit rabbit && rabbit.getRabbitType() != Type.THE_KILLER_BUNNY)
    {
      hover = hover.append(Component.text("\n"));
      hover = hover.append(ComponentUtil.translate("종 : %s", Constant.THE_COLOR_HEX + rabbit.getRabbitType().toString().toLowerCase().replace("_", " ")));
    }
    if (entity instanceof Parrot parrot)
    {
      Variant variant = parrot.getVariant();
      Color color = DyeColor.valueOf(variant.toString()).getColor();
      hover = hover.append(Component.text("\n"));
      hover = hover.append(ComponentUtil.translate("색상 : %s", ComponentUtil.translate("color.minecraft." + variant.toString().toLowerCase()).color(TextColor.color(color.getRed(), color.getGreen(), color.getBlue()))));
    }
    if (entity instanceof Axolotl axolotl)
    {
      Axolotl.Variant variant = axolotl.getVariant();
      String variantString = variant.toString().replace("LUCY", "PINK").replace("WILD", "BROWN").replace("GOLD", "YELLOW");
      Color color = DyeColor.valueOf(variantString).getColor();
      hover = hover.append(Component.text("\n"));
      hover = hover.append(ComponentUtil.translate("색상 : %s", ComponentUtil.translate("color.minecraft." + variantString.toLowerCase()).color(TextColor.color(color.getRed(), color.getGreen(), color.getBlue()))));
    }
    if (entity instanceof Wolf wolf)
    {
      DyeColor collarColor = wolf.getCollarColor();
      Color color = collarColor.getColor();
      hover = hover.append(Component.text("\n"));
      hover = hover.append(ComponentUtil.translate("목줄 색상 : %s", ComponentUtil.translate("color.minecraft." + collarColor.toString().toLowerCase()).color(TextColor.color(color.getRed(), color.getGreen(), color.getBlue()))));
    }
    if (entity instanceof Cat cat)
    {
      Cat.Type type = cat.getCatType();
      hover = hover.append(Component.text("\n"));
      hover = hover.append(ComponentUtil.translate("종 : %s", Constant.THE_COLOR_HEX + type.toString().toLowerCase().replace("_", " ")));
      DyeColor collarColor = cat.getCollarColor();
      Color color = collarColor.getColor();
      hover = hover.append(Component.text("\n"));
      hover = hover.append(ComponentUtil.translate("목줄 색상 : %s", ComponentUtil.translate("color.minecraft." + collarColor.toString().toLowerCase()).color(TextColor.color(color.getRed(), color.getGreen(), color.getBlue()))));
    }
    if (entity instanceof Panda panda)
    {
      Gene mainGene = panda.getMainGene(), hiddenGene = panda.getHiddenGene();
      hover = hover.append(Component.text("\n"));
      hover = hover.append(ComponentUtil.translate("주전자 : %s", Constant.THE_COLOR_HEX + mainGene.toString().toLowerCase()));
      hover = hover.append(Component.text("\n"));
      hover = hover.append(ComponentUtil.translate("부전자 : %s", Constant.THE_COLOR_HEX + hiddenGene.toString().toLowerCase()));
    }
    if (entity instanceof Snowman snowman && snowman.isDerp())
    {
      hover = hover.append(Component.text("\n"));
      hover = hover.append(ComponentUtil.translate("derp."));
    }
    if (entity instanceof Colorable colorable)
    {
      DyeColor dyeColor = colorable.getColor();
      if (dyeColor != null)
      {
        Color color = dyeColor.getColor();
        hover = hover.append(Component.text("\n"));
        hover = hover.append(ComponentUtil.translate("색상 : %s", ComponentUtil.translate("color.minecraft." + dyeColor.toString().toLowerCase()).color(TextColor.color(color.getRed(), color.getGreen(), color.getBlue()))));
      }
    }
    if (entity instanceof Villager villager)
    {
      Profession profession = villager.getProfession();
      String key = Constant.THE_COLOR_HEX + "entity.minecraft.villager." + profession.toString().toLowerCase();
      if (profession != Profession.NONE)
      {
        hover = hover.append(Component.text("\n"));
        hover = hover.append(ComponentUtil.translate("직업 : %s", ComponentUtil.translate(key)));
      }
      int villagerExperience = villager.getVillagerExperience();
      if (villagerExperience > 0)
      {
        hover = hover.append(Component.text("\n"));
        hover = hover.append(ComponentUtil.translate("레벨 : %s (%s)",
                ComponentUtil.translate("merchant.level." + villager.getVillagerLevel()).color(Constant.THE_COLOR), Constant.THE_COLOR_HEX + villagerExperience));
      }
      Villager.Type villagerType = villager.getVillagerType();
      Biome biome = Biome.valueOf(villagerType.toString().replace("SNOW", "SNOWY_PLAINS"));
      hover = hover.append(Component.text("\n"));
      hover = hover.append(ComponentUtil.translate("기후 : %s", ComponentUtil.translate("biome.minecraft." + biome.toString().toLowerCase()).color(Constant.THE_COLOR)));
      Map<UUID, Reputation> reputationMap = villager.getReputations();
      Set<UUID> uuidSet = reputationMap.keySet();
      if (!uuidSet.isEmpty())
      {
        hover = hover.append(Component.text("\n"));
        hover = hover.append(ComponentUtil.create(Constant.SEPARATOR));
        hover = hover.append(Component.text("\n"));
        hover = hover.append(ComponentUtil.translate("%s의 평판", nameComponent));
        for (UUID targetUUID : uuidSet)
        {
          Object target = Method2.getEntityAsync(targetUUID);
          if (target == null)
          {
            target = Bukkit.getOfflinePlayer(targetUUID);
          }
          Reputation reputation = reputationMap.get(targetUUID);
          int sum = 0;
          sum += reputation.getReputation(ReputationType.MAJOR_POSITIVE);
          sum += reputation.getReputation(ReputationType.MINOR_POSITIVE);
          sum += reputation.getReputation(ReputationType.TRADING);
          sum -= reputation.getReputation(ReputationType.MAJOR_NEGATIVE);
          sum -= reputation.getReputation(ReputationType.MINOR_NEGATIVE);
          hover = hover.append(Component.text("\n"));
          hover = hover.append(ComponentUtil.translate("%s : %s", SenderComponentUtil.senderComponent(target, defaultColor, true), Constant.THE_COLOR_HEX + sum));
        }
      }
    }
    if (entity instanceof Merchant merchant)
    {
      hover = MerchantComponentUtil.hoverMerchantComponent(merchant, hover);
    }
    if (entity instanceof Fox fox)
    {
      EntityEquipment entityEquipment = fox.getEquipment();
      if (entityEquipment != null)
      {
        ItemStack item = entityEquipment.getItemInMainHand();
        if (ItemStackUtil.itemExists(item))
        {
          List<Component> lore = ItemStackUtil.getItemInfoAsComponents(item, ComponentUtil.translate("rg255,204;[%s이(가) 물고 있는 아이템]", nameComponent), true);
          for (Component lor : lore)
          {
            hover = hover.append(Component.text("\n"));
            hover = hover.append(lor);
          }
        }
      }
    }
    if (entity instanceof Enderman enderman)
    {
      BlockData blockData = enderman.getCarriedBlock();
      if (blockData != null)
      {
        ItemStack item = new ItemStack(blockData.getMaterial());
        BlockDataMeta blockDataMeta = (BlockDataMeta) item.getItemMeta();
        blockDataMeta.setBlockData(blockData);
        item.setItemMeta(blockDataMeta);
        ItemLore.setItemLore(item);
        List<Component> lore = ItemStackUtil.getItemInfoAsComponents(item, ComponentUtil.translate("rg255,204;[들고 있는 아이템]"), true);
        for (Component lor : lore)
        {
          hover = hover.append(Component.text("\n"));
          hover = hover.append(lor);
        }
      }
    }
    if (entity instanceof Raider raider)
    {
      Block targetBlock = raider.getPatrolTarget();
      if (targetBlock != null)
      {
        hover = hover.append(Component.text("\n"));
        hover = hover.append(ComponentUtil.translate("목표 습격 지점 : %s", targetBlock.getLocation()));
      }
    }
    if (entity instanceof PufferFish pufferFish)
    {
      hover = hover.append(Component.text("\n"));
      hover = hover.append(ComponentUtil.translate("크기 : %s", Constant.THE_COLOR_HEX + pufferFish.getPuffState()));
    }
    if (entity instanceof TropicalFish tropicalFish)
    {
      hover = hover.append(Component.text("\n"));
      hover = hover.append(ComponentUtil.translate("종 : %s",
              ComponentUtil.translate(TropicalFishLore.getTropicalFishKey(tropicalFish.getBodyColor(), tropicalFish.getPatternColor(), tropicalFish.getPattern())).color(Constant.THE_COLOR)));
    }
    if (entity instanceof Phantom phantom)
    {
      hover = hover.append(Component.text("\n"));
      hover = hover.append(ComponentUtil.translate("크기 : %s", Constant.THE_COLOR_HEX + phantom.getSize()));
      UUID spawningEntityUUID = phantom.getSpawningEntity();
      if (spawningEntityUUID != null)
      {
        Entity spawningEntity = Method2.getEntityAsync(spawningEntityUUID);
        if (spawningEntity != null)
        {
          hover = hover.append(Component.text("\n"));
          hover = hover.append(ComponentUtil.translate("잠 안자서 얘 소환한 놈 : %s", SenderComponentUtil.senderComponent(spawningEntity, defaultColor, true)));
        }
      }
    }
    if (entity instanceof Goat goat && goat.isScreaming())
    {
      hover = hover.append(Component.text("\n"));
      hover = hover.append(ComponentUtil.translate("rg255,204;빠아아아아아악"));
    }
    if (entity instanceof AbstractHorse abstractHorse)
    {
      AttributeInstance movementSpeedInstance = abstractHorse.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED);
      if (movementSpeedInstance != null)
      {
        hover = hover.append(Component.text("\n"));
        hover = hover.append(ComponentUtil.translate("이동 속도 : 약 %sm/s", Constant.THE_COLOR_HEX + Constant.Sosu2.format(movementSpeedInstance.getValue() * 43)));
      }
      double jumpStrength = abstractHorse.getJumpStrength();
      hover = hover.append(Component.text("\n"));
      hover = hover.append(ComponentUtil.translate("점프 강도 : %sm", Constant.THE_COLOR_HEX + Constant.Sosu2.format(jumpStrength * 2)));
    }
    if (entity instanceof Horse horse)
    {
      Horse.Color horseColor = horse.getColor();
      Style horseStyle = horse.getStyle();
      hover = hover.append(Component.text("\n"));
      hover = hover.append(ComponentUtil.translate("색상 : %s", Constant.THE_COLOR_HEX + horseColor.toString().toLowerCase().replace("_", " ")));
      hover = hover.append(Component.text("\n"));
      hover = hover.append(ComponentUtil.translate("종 : %s", Constant.THE_COLOR_HEX + horseStyle.toString().toLowerCase().replace("_", " ")));
    }
    if (entity instanceof Llama llama)
    {
      Llama.Color llamaColor = llama.getColor();
      hover = hover.append(Component.text("\n"));
      hover = hover.append(ComponentUtil.translate("색상 : %s", Constant.THE_COLOR_HEX + llamaColor.toString().toLowerCase().replace("_", " ")));
    }
    if (entity instanceof Slime slime)
    {
      hover = hover.append(Component.text("\n"));
      hover = hover.append(ComponentUtil.translate("크기 : %s", Constant.THE_COLOR_HEX + slime.getSize()));
    }
    if (entity instanceof Turtle turtle)
    {
      Location home = turtle.getHome();
      hover = hover.append(Component.text("\n"));
      hover = hover.append(ComponentUtil.translate("집 좌표 : %s", home));
    }
    if (entity instanceof Vex vex)
    {
      Mob mob = vex.getSummoner();
      if (mob != null)
      {
        hover = hover.append(Component.text("\n"));
        hover = hover.append(ComponentUtil.translate("소환수 : %s", SenderComponentUtil.senderComponent(mob, defaultColor, true)));
      }
    }
    if (entity instanceof Witch witch)
    {
      ItemStack drinkingPotion = witch.getDrinkingPotion();
      if (ItemStackUtil.itemExists(drinkingPotion))
      {
        List<Component> lore = ItemStackUtil.getItemInfoAsComponents(drinkingPotion, ComponentUtil.translate("rg255,204;[%s(이)가 마시고 있는 물약]", nameComponent), true);
        for (Component lor : lore)
        {
          hover = hover.append(Component.text("\n"));
          hover = hover.append(lor);
        }
      }
    }
    if (entity instanceof ZombieVillager zombieVillager)
    {
      Profession profession = zombieVillager.getVillagerProfession();
      String key = Constant.THE_COLOR_HEX + "entity.minecraft.villager." + profession.toString().toLowerCase();
      if (profession != Profession.NONE)
      {
        hover = hover.append(Component.text("\n"));
        hover = hover.append(ComponentUtil.translate("직업 : %s", ComponentUtil.translate(key)));
      }
      Villager.Type villagerType = zombieVillager.getVillagerType();
      Biome biome = Biome.valueOf(villagerType.toString().replace("SNOW", Biome.SNOWY_TAIGA.toString()));
      hover = hover.append(Component.text("\n"));
      hover = hover.append(ComponentUtil.translate("기후 : %s", ComponentUtil.translate("biome.minecraft." + biome.toString().toLowerCase()).color(Constant.THE_COLOR)));

      OfflinePlayer offlinePlayer = zombieVillager.getConversionPlayer();
      if (offlinePlayer != null)
      {
        hover = hover.append(Component.text("\n"));
        hover = hover.append(ComponentUtil.translate("치료 지원자 : %s", SenderComponentUtil.senderComponent(offlinePlayer, defaultColor, true)));
      }
    }
    if (entity instanceof Tameable tameable && tameable.isTamed())
    {
      AnimalTamer animalTamer = tameable.getOwner();
      if (animalTamer != null)
      {
        hover = hover.append(Component.text("\n"));
        Component component = ComponentUtil.translate("주인 : %s", SenderComponentUtil.senderComponent(animalTamer, defaultColor, true));
        hover = hover.append(component);
      }
    }
    if (entity instanceof Animals animals)
    {
      UUID breedCause = animals.getBreedCause();
      if (breedCause != null)
      {
        Entity breedCauseEntity = Method2.getEntityAsync(breedCause);
        if (breedCauseEntity != null)
        {
          hover = hover.append(Component.text("\n"));
          hover = hover.append(ComponentUtil.translate("사육사 : %s", SenderComponentUtil.senderComponent(breedCauseEntity, defaultColor, true)));
        }
      }
    }
    if (entity instanceof CommandMinecart commandMinecart)
    {
      String command = commandMinecart.getCommand();
      if (!command.isEmpty())
      {
        hover = hover.append(Component.text("\n"));
        hover = hover.append(ComponentUtil.translate("명령어 : %s", Component.text(command).color(Constant.THE_COLOR)));
      }
    }
    if (entity instanceof PoweredMinecart poweredMinecart)
    {
      int fuel = poweredMinecart.getFuel();
      hover = hover.append(Component.text("\n"));
      hover = hover.append(ComponentUtil.translate("연료 : %s", Component.text(fuel).color(Constant.THE_COLOR)));
    }
    if (entity instanceof InventoryHolder inventoryHolder && !(inventoryHolder instanceof LivingEntity))
    {
      Component customNameLore = ComponentUtil.translate("[%s의 내용물]", nameComponent);
      Inventory inventory = inventoryHolder.getInventory();
      if (!Method.inventoryEmpty(inventory))
      {
        hover = hover.append(Component.text("\n"));
        List<ItemStack> itemStackList = new ArrayList<>();
        for (ItemStack itemStack : inventory.getContents())
        {
          if (ItemStackUtil.itemExists(itemStack))
          {
            itemStackList.add(itemStack);
          }
        }
        if (itemStackList.size() == 1)
        {
          List<Component> lore = ItemStackUtil.getItemInfoAsComponents(itemStackList.get(0), customNameLore, true);
          for (Component lor : lore)
          {
            hover = hover.append(Component.text("\n"));
            hover = hover.append(lor);
          }
        }
        else
        {
          hover = hover.append(Component.text("\n"));
          hover = hover.append(customNameLore);
          for (int i = 0; i < itemStackList.size(); i++)
          {
            if (i == 9)
            {
              hover = hover.append(Component.text("\n"));
              hover = hover.append(ComponentUtil.translate("&7&ocontainer.shulkerBox.more", itemStackList.size() - i));
              break;
            }
            hover = hover.append(Component.text("\n"));
            ItemStack itemStack = itemStackList.get(i);
            hover = hover.append(ItemStackComponent.itemStackComponent(itemStack));
          }
        }
      }
    }
    if (entity instanceof Projectile projectile)
    {
      ProjectileSource projectileSource = projectile.getShooter();
      if (projectileSource != null)
      {
        hover = hover.append(Component.text("\n"));
        String shooterKey = "격수";
        if (projectile instanceof AbstractArrow || projectile instanceof Fireball || projectile instanceof ShulkerBullet)
        {
          shooterKey = "쏜 개체";
        }
        if (projectile instanceof ThrowableProjectile)
        {
          shooterKey = "던진 개체";
        }
        if (projectile instanceof FishHook)
        {
          shooterKey = "주인";
        }
        if (projectile instanceof LlamaSpit)
        {
          shooterKey = "뱉은 놈";
        }
        hover = hover.append(ComponentUtil.translate(shooterKey + " : %s", SenderComponentUtil.senderComponent(projectileSource, defaultColor, true)));
      }
      if (projectile instanceof ThrownPotion thrownPotion)
      {
        ItemStack item = thrownPotion.getItem();
        List<Component> lore = ItemStackUtil.getItemInfoAsComponents(item, ComponentUtil.translate("rg255,204;[아이템]"), true);
        for (Component lor : lore)
        {
          hover = hover.append(Component.text("\n"));
          hover = hover.append(lor);
        }
      }
      if (projectile instanceof ThrowableProjectile throwableProjectile)
      {
        ItemStack item = throwableProjectile.getItem();
        List<Component> lore = ItemStackUtil.getItemInfoAsComponents(item, ComponentUtil.translate("rg255,204;[아이템]"), true);
        for (Component lor : lore)
        {
          hover = hover.append(Component.text("\n"));
          hover = hover.append(lor);
        }
      }
      if (projectile instanceof AbstractArrow abstractArrow && !(abstractArrow instanceof Trident))
      {
        ItemStack itemStack = abstractArrow.getItemStack();
        List<Component> lore = ItemStackUtil.getItemInfoAsComponents(itemStack, ComponentUtil.translate("rg255,204;[아이템]"), true);
        for (Component lor : lore)
        {
          hover = hover.append(Component.text("\n"));
          hover = hover.append(lor);
        }
      }
      if (projectile instanceof FishHook fishHook)
      {
        Entity hookedEntity = fishHook.getHookedEntity();
        if (hookedEntity != null)
        {
          hover = hover.append(Component.text("\n"));
          hover = hover.append(ComponentUtil.translate("걸린 개체 : %s", hookedEntity));
        }
      }
      if (projectile instanceof ShulkerBullet shulkerBullet)
      {
        Entity target = shulkerBullet.getTarget();
        if (target != null)
        {
          hover = hover.append(Component.text("\n"));
          hover = hover.append(ComponentUtil.translate("목표 개체 : %s", target));
        }
      }
      if (projectile instanceof Firework firework)
      {
        UUID spawningEntityUUID = firework.getSpawningEntity();
        if (spawningEntityUUID != null)
        {
          Entity spawningEntity = Method2.getEntityAsync(spawningEntityUUID);
          if (spawningEntity != null)
          {
            hover = hover.append(Component.text("\n"));
            hover = hover.append(ComponentUtil.translate("폭죽을 쏜 개체 : %s" + spawningEntity));
          }
        }
        ItemStack item = new ItemStack(Material.FIREWORK_ROCKET);
        item.setItemMeta(firework.getFireworkMeta());
        List<Component> lore = ItemStackUtil.getItemInfoAsComponents(item, ComponentUtil.translate("rg255,204;[아이템]"), true);
        for (Component lor : lore)
        {
          hover = hover.append(Component.text("\n"));
          hover = hover.append(lor);
        }
      }
    }
    if (entity instanceof Explosive explosive)
    {
      float yield = explosive.getYield();
      hover = hover.append(Component.text("\n"));
      hover = hover.append(ComponentUtil.translate("폭발 강도 : %s", Constant.THE_COLOR_HEX + Constant.Sosu2.format(yield)));
      hover = hover.append(Component.text("\n"));
      hover = hover.append(ComponentUtil.translate("불 번짐 여부 : %s" + Constant.THE_COLOR_HEX + explosive.isIncendiary()));
    }
    if (entity instanceof TNTPrimed tntPrimed)
    {
      int fuseTicks = tntPrimed.getFuseTicks();
      if (fuseTicks > 0)
      {
        hover = hover.append(Component.text("\n"));
        hover = hover.append(ComponentUtil.translate("폭발 대기 시간 : %s", Method.timeFormatMilli(fuseTicks * 50)));
      }
      Entity source = tntPrimed.getSource();
      if (source != null)
      {
        hover = hover.append(Component.text("\n"));
        hover = hover.append(ComponentUtil.translate("불 붙인 개체 : %s", SenderComponentUtil.senderComponent(source, defaultColor, true)));
      }
    }
    if (entity instanceof EnderSignal enderSignal)
    {
      hover = hover.append(Component.text("\n"));
      hover = hover.append(ComponentUtil.translate("목표 지점 : %s", enderSignal.getTargetLocation()));
      ItemStack item = enderSignal.getItem();
      List<Component> lore = ItemStackUtil.getItemInfoAsComponents(item, ComponentUtil.translate("rg255,204;[아이템]"), true);
      for (Component lor : lore)
      {
        hover = hover.append(Component.text("\n"));
        hover = hover.append(lor);
      }
    }
    if (entity instanceof Painting painting)
    {
      hover = hover.append(Component.text("\n"));
      hover = hover.append(ComponentUtil.translate("종류 : %s", Constant.THE_COLOR_HEX + painting.getArt().toString().toLowerCase().replace("_", " ")));
    }
    if (entity instanceof Item item)
    {
      hover = hover.append(Component.text("\n"));
      hover = hover.append(ComponentUtil.translate("HP : %s", Constant.THE_COLOR_HEX + item.getHealth()));
      hover = hover.append(Component.text("\n"));
      hover = hover.append(ComponentUtil.translate("PickupDelay : %s", Constant.THE_COLOR_HEX + item.getPickupDelay()));
      hover = hover.append(Component.text("\n"));
      hover = hover.append(ComponentUtil.translate("isUnlimitedLifeTime : %s", Constant.THE_COLOR_HEX + item.isUnlimitedLifetime()));
      hover = hover.append(Component.text("\n"));
      hover = hover.append(ComponentUtil.translate("canMobPickup : %s", Constant.THE_COLOR_HEX + item.canMobPickup()));
      hover = hover.append(Component.text("\n"));
      hover = hover.append(ComponentUtil.translate("canPlayerPickup : %s", Constant.THE_COLOR_HEX + item.canPlayerPickup()));
      hover = hover.append(Component.text("\n"));
      hover = hover.append(ComponentUtil.translate("willAge : %s", Constant.THE_COLOR_HEX + item.willAge()));
      UUID owner = item.getOwner(), thrower = item.getThrower();
      if (owner != null)
      {
        hover = hover.append(Component.text("\n"));
        hover = hover.append(ComponentUtil.translate("소유자 : %s", SenderComponentUtil.senderComponent(owner, defaultColor, true)));
      }      
      if (thrower != null)
      {
        hover = hover.append(Component.text("\n"));
        hover = hover.append(ComponentUtil.translate("버린 개체 : %s", SenderComponentUtil.senderComponent(thrower, defaultColor, true)));
      }
      hover = hover.append(Component.text("\n"));
      ItemStack itemStack = item.getItemStack();
      List<Component> lore = ItemStackUtil.getItemInfoAsComponents(itemStack, ComponentUtil.translate("rg255,204;[아이템]"), true);
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
        List<Component> lore = ItemStackUtil.getItemInfoAsComponents(item, ComponentUtil.translate("rg255,204;[아이템]"), true);
        for (Component lor : lore)
        {
          hover = hover.append(Component.text("\n"));
          hover = hover.append(lor);
        }
      }
      if (itemFrame.isFixed())
      {
        hover = hover.append(Component.text("\n"));
        hover = hover.append(ComponentUtil.translate("&a&o고정됨"));
      }
      if (!itemFrame.isVisible())
      {
        hover = hover.append(Component.text("\n"));
        hover = hover.append(ComponentUtil.translate("&7&o투명함"));
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
      List<Component> lore = ItemStackUtil.getItemInfoAsComponents(itemStack, ComponentUtil.translate("rg255,204;[아이템]"), true);
      for (Component lor : lore)
      {
        hover = hover.append(Component.text("\n"));
        hover = hover.append(lor);
      }
    }
    if (entity instanceof ExperienceOrb experienceOrb)
    {
      int experience = experienceOrb.getExperience();
      hover = hover.append(Component.text("\n"));
      hover = hover.append(ComponentUtil.translate("경험치 : %s", Constant.THE_COLOR_HEX + experience));
    }
    if (entity instanceof FishHook fishHook)
    {
      boolean applyLure = fishHook.getApplyLure(), isInOpenWater = fishHook.isInOpenWater();
      int minWaitTime = fishHook.getMinWaitTime(), maxWaitTime = fishHook.getMaxWaitTime(), waitTime = fishHook.getWaitTime();
      @SuppressWarnings("deprecation")
      double biteChance = fishHook.getBiteChance();
      HookState hookState = fishHook.getState();
      Entity hookedEntity = fishHook.getHookedEntity();
      hover = hover.append(Component.text("\n"));
      hover = hover.append(ComponentUtil.translate("찌 상태 : %s", Constant.THE_COLOR_HEX + hookState));
      if (hookedEntity != null)
      {
        hover = hover.append(Component.text("\n"));
        hover = hover.append(ComponentUtil.translate("찌에 걸린 개체 : %s", SenderComponentUtil.senderComponent(hookedEntity, defaultColor, true)));
      }
      hover = hover.append(Component.text("\n"));
      hover = hover.append(ComponentUtil.translate("미끼 적용 여부 : %s, 열린 공간에 존재 여부 : %s", Constant.THE_COLOR_HEX + applyLure, Constant.THE_COLOR_HEX + isInOpenWater));
      hover = hover.append(Component.text("\n"));
      hover = hover.append(ComponentUtil.translate("입질 대기 시간 : %s~%s틱 (현재 %s틱)", Constant.THE_COLOR_HEX + minWaitTime, Constant.THE_COLOR_HEX + maxWaitTime, Constant.THE_COLOR_HEX + waitTime));
      hover = hover.append(Component.text("\n"));
      hover = hover.append(ComponentUtil.translate("입질 확률(구버전) : %s", Constant.THE_COLOR_HEX + biteChance));
    }
    if (entity instanceof EvokerFangs evokerFangs)
    {
      LivingEntity owner = evokerFangs.getOwner();
      Component summoner = owner != null ? SenderComponentUtil.senderComponent(owner, defaultColor, true) : ComponentUtil.translate("없음", NamedTextColor.RED);
      hover = hover.append(Component.text("\n"));
      hover = hover.append(ComponentUtil.translate("소환사 : %s", summoner));
    }

    if (Cucumbery.config.getBoolean("use-hover-event-for-entities.unfair-play-mode.enabled") && Cucumbery.config.getBoolean("use-hover-event-for-entities.unfair-play-mode.location"))
    {
      Location location = entity.getLocation();
      hover = hover.append(Component.text("\n"));
      hover = hover.append(ComponentUtil.translate("좌표 : %s", location));
    }
    if (Cucumbery.config.getBoolean("use-hover-event-for-entities.unfair-play-mode.enabled") && Cucumbery.config.getBoolean("use-hover-event-for-entities.unfair-play-mode.hp"))
    {
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
          hover = hover.append(ComponentUtil.translate("HP : %s", ComponentUtil.translate("&7%s / %s", healthDisplay, maxHealthDisplay)));
        }
      }
    }
    if (entity instanceof Player player)
    {
      hover = hover
              .append(Component.text("\n"))
              .append(ComponentUtil.translate("현재 시각 : %s", Constant.THE_COLOR_HEX + Method.getCurrentTime(Calendar.getInstance())));
      if (player.isOp() || player.hasPermission("asdf"))
      {
        hover = hover
                .append(Component.text("\n"))
                .append(ComponentUtil.translate("#52ee52;관리자입니다"));
      }
      hover = hover
              .append(Component.text("\n"))
              .append(ComponentUtil.create(Constant.SEPARATOR))
              .append(Component.text("\n"))
              .append(ComponentUtil.translate("클릭하여 소셜 메뉴 열기 : %s", "&7/socialmenu " + player.getName()));
      nameComponent = nameComponent.clickEvent(ClickEvent.runCommand(click));
    }
    else
    {
      hover = hover.append(Component.text("\n"));
      hover = hover.append(Component.text("minecraft:" + entity.getType().toString().toLowerCase(), NamedTextColor.DARK_GRAY));
      nameComponent = nameComponent.clickEvent(ClickEvent.suggestCommand(click));
    }
    if (tmiMode || Cucumbery.config.getBoolean("use-hover-event-for-entities.enabled"))
    {
      nameComponent = nameComponent.hoverEvent(hover);
    }
    if (defaultColor != null && nameComponent.color() == null)
    {
      nameComponent = nameComponent.color(defaultColor);
    }
    return nameComponent;
  }
}
