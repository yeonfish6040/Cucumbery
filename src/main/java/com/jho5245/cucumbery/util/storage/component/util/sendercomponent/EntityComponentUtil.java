package com.jho5245.cucumbery.util.storage.component.util.sendercomponent;

import com.destroystokyo.paper.entity.villager.Reputation;
import com.destroystokyo.paper.entity.villager.ReputationType;
import com.jho5245.cucumbery.Cucumbery;
import com.jho5245.cucumbery.util.Method;
import com.jho5245.cucumbery.util.Method2;
import com.jho5245.cucumbery.util.itemlore.ItemLore;
import com.jho5245.cucumbery.util.itemlore.TropicalFishLore;
import com.jho5245.cucumbery.util.storage.ItemStackUtil;
import com.jho5245.cucumbery.util.storage.component.ItemStackComponent;
import com.jho5245.cucumbery.util.storage.component.util.ComponentUtil;
import com.jho5245.cucumbery.util.storage.component.util.ItemNameUtil;
import com.jho5245.cucumbery.util.storage.data.Constant;
import net.kyori.adventure.text.Component;
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
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class EntityComponentUtil
{
  @NotNull
  public static Component entityComponent(@NotNull Entity entity, @Nullable TextColor defaultColor)
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
            nameComponent = Component.empty().append(ComponentUtil.create(false, prefix)).append(nameComponent);
          }
          if (suffix != null)
          {
            nameComponent = nameComponent.append(ComponentUtil.create(false, suffix));
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
        nameComponent = Component.translatable(key);
      }
      if (entity instanceof MushroomCow mushroomCow && mushroomCow.getVariant() == MushroomCow.Variant.BROWN)
      {
        nameComponent = ComponentUtil.createTranslate("%s %s", Component.translatable("color.minecraft.brown"), nameComponent);
      }
      if (entity instanceof Villager villager)
      {
        nameComponent = ComponentUtil.createTranslate(entity.getType().translationKey() + "." + villager.getProfession().toString().toLowerCase());
      }
      if (entity instanceof Ageable ageable && !ageable.isAdult())
      {
        nameComponent = ComponentUtil.createTranslate("%s %s", "아기", nameComponent);
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
              .append(ComponentUtil.createTranslate("ID : %s", Constant.THE_COLOR_HEX + name));
      if (Cucumbery.config.getBoolean("use-hover-event-for-entities.uuid"))
      {
        hover = hover
                .append(Component.text("\n"))
                .append(ComponentUtil.createTranslate("UUID : %s", Constant.THE_COLOR_HEX + uuid));
      }
      if (Cucumbery.config.getBoolean("use-hover-event-for-entities.player-join-count"))
      {
        hover = hover
                .append(Component.text("\n"))
                .append(ComponentUtil.createTranslate("접속 횟수 : %s회", Constant.THE_COLOR_HEX + player.getStatistic(Statistic.LEAVE_GAME) + 1));
      }
      if (Cucumbery.config.getBoolean("use-hover-event-for-entities.player-play-time"))
      {
        hover = hover
                .append(Component.text("\n"))
                .append(ComponentUtil.createTranslate("플레이 시간 : %s", Constant.THE_COLOR_HEX + Method.timeFormatMilli(player.getStatistic(Statistic.PLAY_ONE_MINUTE) * 50L, false)));
      }
      if (Cucumbery.config.getBoolean("use-hover-event-for-entities.player-game-mode"))
      {
        hover = hover
                .append(Component.text("\n"))
                .append(ComponentUtil.createTranslate("게임 모드 : %s", ComponentUtil.createTranslate(Constant.THE_COLOR_HEX + "gameMode." + gameMode.toString().toLowerCase())));
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
      Component typeComponent = Component.translatable(key);
      if (entity instanceof MushroomCow mushroomCow && mushroomCow.getVariant() == MushroomCow.Variant.BROWN)
      {
        typeComponent = ComponentUtil.createTranslate("%s %s", Component.translatable("color.minecraft.brown"), typeComponent);
      }
      if (entity instanceof Ageable ageable && !ageable.isAdult())
      {
        typeComponent = ComponentUtil.createTranslate("%s %s", "아기", typeComponent);
      }

      if (Cucumbery.config.getBoolean("use-hover-event-for-entities.entity-type"))
      {
        hover = hover
                .append(Component.text("\n"))
                .append(ComponentUtil.createTranslate("유형 : %s", typeComponent.color(Constant.THE_COLOR)));
      }
      if (Cucumbery.config.getBoolean("use-hover-event-for-entities.uuid"))
      {
        hover = hover
                .append(Component.text("\n"))
                .append(ComponentUtil.createTranslate("UUID : %s", Constant.THE_COLOR_HEX + uuid));
      }
    }
    if (entity instanceof Rabbit rabbit && rabbit.getRabbitType() != Type.THE_KILLER_BUNNY)
    {
      hover = hover.append(Component.text("\n"));
      hover = hover.append(ComponentUtil.createTranslate("종 : %s", Constant.THE_COLOR_HEX + rabbit.getRabbitType().toString().toLowerCase().replace("_", " ")));
    }
    if (entity instanceof Parrot parrot)
    {
      Variant variant = parrot.getVariant();
      Color color = DyeColor.valueOf(variant.toString()).getColor();
      hover = hover.append(Component.text("\n"));
      hover = hover.append(ComponentUtil.createTranslate("색상 : %s", Component.translatable("color.minecraft." + variant.toString().toLowerCase()).color(TextColor.color(color.getRed(), color.getGreen(), color.getBlue()))));
    }
    if (entity instanceof Axolotl axolotl)
    {
      Axolotl.Variant variant = axolotl.getVariant();
      String variantString = variant.toString().replace("LUCY", "PINK").replace("WILD", "BROWN").replace("GOLD", "YELLOW");
      Color color = DyeColor.valueOf(variantString).getColor();
      hover = hover.append(Component.text("\n"));
      hover = hover.append(ComponentUtil.createTranslate("색상 : %s", Component.translatable("color.minecraft." + variantString.toLowerCase()).color(TextColor.color(color.getRed(), color.getGreen(), color.getBlue()))));
    }
    if (entity instanceof Wolf wolf)
    {
      DyeColor collarColor = wolf.getCollarColor();
      Color color = collarColor.getColor();
      hover = hover.append(Component.text("\n"));
      hover = hover.append(ComponentUtil.createTranslate("목줄 색상 : %s", Component.translatable("color.minecraft." + collarColor.toString().toLowerCase()).color(TextColor.color(color.getRed(), color.getGreen(), color.getBlue()))));
    }
    if (entity instanceof Cat cat)
    {
      Cat.Type type = cat.getCatType();
      hover = hover.append(Component.text("\n"));
      hover = hover.append(ComponentUtil.createTranslate("종 : %s", Constant.THE_COLOR_HEX + type.toString().toLowerCase().replace("_", " ")));
      DyeColor collarColor = cat.getCollarColor();
      Color color = collarColor.getColor();
      hover = hover.append(Component.text("\n"));
      hover = hover.append(ComponentUtil.createTranslate("목줄 색상 : %s", Component.translatable("color.minecraft." + collarColor.toString().toLowerCase()).color(TextColor.color(color.getRed(), color.getGreen(), color.getBlue()))));
    }
    if (entity instanceof Panda panda)
    {
      Gene mainGene = panda.getMainGene(), hiddenGene = panda.getHiddenGene();
      hover = hover.append(Component.text("\n"));
      hover = hover.append(ComponentUtil.createTranslate("주전자 : %s", Constant.THE_COLOR_HEX + mainGene.toString().toLowerCase()));
      hover = hover.append(Component.text("\n"));
      hover = hover.append(ComponentUtil.createTranslate("부전자 : %s", Constant.THE_COLOR_HEX + hiddenGene.toString().toLowerCase()));
    }
    if (entity instanceof Snowman snowman && snowman.isDerp())
    {
      hover = hover.append(Component.text("\n"));
      hover = hover.append(Component.translatable("derp."));
    }
    if (entity instanceof Colorable colorable)
    {
      DyeColor dyeColor = colorable.getColor();
      if (dyeColor != null)
      {
        Color color = dyeColor.getColor();
        hover = hover.append(Component.text("\n"));
        hover = hover.append(ComponentUtil.createTranslate("색상 : %s", Component.translatable("color.minecraft." + dyeColor.toString().toLowerCase()).color(TextColor.color(color.getRed(), color.getGreen(), color.getBlue()))));
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
                Component.translatable("merchant.level." + villager.getVillagerLevel()).color(Constant.THE_COLOR), Constant.THE_COLOR_HEX + villagerExperience));
      }
      Villager.Type villagerType = villager.getVillagerType();
      Biome biome = Biome.valueOf(villagerType.toString().replace("SNOW", "SNOWY_TUNDRA"));
      hover = hover.append(Component.text("\n"));
      hover = hover.append(ComponentUtil.createTranslate("기후 : %s", Component.translatable("biome.minecraft." + biome.toString().toLowerCase()).color(Constant.THE_COLOR)));
      Map<UUID, Reputation> reputationMap = villager.getReputations();
      Set<UUID> uuidSet = reputationMap.keySet();
      if (!uuidSet.isEmpty())
      {
        hover = hover.append(Component.text("\n"));
        hover = hover.append(ComponentUtil.create(Constant.ITEM_LORE_SEPARATOR));
        hover = hover.append(Component.text("\n"));
        hover = hover.append(ComponentUtil.createTranslate("%s의 평판", nameComponent));
        for (UUID targetUUID : uuidSet)
        {
          Object target = Bukkit.getEntity(targetUUID);
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
          hover = hover.append(ComponentUtil.createTranslate("%s : %s", target, Constant.THE_COLOR_HEX + sum));
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
          List<Component> lore = ItemStackUtil.getItemInfoAsComponents(item, ComponentUtil.createTranslate("&e[%s이(가) 물고 있는 아이템]", nameComponent), true);
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
        List<Component> lore = ItemStackUtil.getItemInfoAsComponents(item, ComponentUtil.createTranslate("&e[들고 있는 아이템]"), true);
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
        hover = hover.append(ComponentUtil.createTranslate("목표 습격 지점 : %s", targetBlock.getLocation()));
      }
    }
    if (entity instanceof PufferFish pufferFish)
    {
      hover = hover.append(Component.text("\n"));
      hover = hover.append(ComponentUtil.createTranslate("크기 : %s", Constant.THE_COLOR_HEX + pufferFish.getPuffState()));
    }
    if (entity instanceof TropicalFish tropicalFish)
    {
      hover = hover.append(Component.text("\n"));
      hover = hover.append(ComponentUtil.createTranslate("종 : %s",
              Component.translatable(TropicalFishLore.getTropicalFishKey(tropicalFish.getBodyColor(), tropicalFish.getPatternColor(), tropicalFish.getPattern())).color(Constant.THE_COLOR)));
    }
    if (entity instanceof Phantom phantom)
    {
      hover = hover.append(Component.text("\n"));
      hover = hover.append(ComponentUtil.createTranslate("크기 : %s", Constant.THE_COLOR_HEX + phantom.getSize()));
      UUID spawningEntityUUID = phantom.getSpawningEntity();
      if (spawningEntityUUID != null)
      {
        Entity spawningEntity = Bukkit.getEntity(spawningEntityUUID);
        if (spawningEntity != null)
        {
          hover = hover.append(Component.text("\n"));
          hover = hover.append(ComponentUtil.createTranslate("잠 안자서 얘 소환한 놈 : %s", spawningEntity));
        }
      }
    }
    if (entity instanceof Goat goat && goat.isScreaming())
    {
      hover = hover.append(Component.text("\n"));
      hover = hover.append(ComponentUtil.createTranslate("&e빠아아아아아악"));
    }
    if (entity instanceof AbstractHorse abstractHorse)
    {
      AttributeInstance movementSpeedInstance = abstractHorse.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED);
      if (movementSpeedInstance != null)
      {
        hover = hover.append(Component.text("\n"));
        hover = hover.append(ComponentUtil.createTranslate("이동 속도 : 약 %sm/s", Constant.THE_COLOR_HEX + Constant.Sosu2.format(movementSpeedInstance.getValue() * 43)));
      }
      double jumpStrength = abstractHorse.getJumpStrength();
      hover = hover.append(Component.text("\n"));
      hover = hover.append(ComponentUtil.createTranslate("점프 강도 : %sm", Constant.THE_COLOR_HEX + Constant.Sosu2.format(jumpStrength * 2)));
    }
    if (entity instanceof Horse horse)
    {
      Horse.Color horseColor = horse.getColor();
      Style horseStyle = horse.getStyle();
      hover = hover.append(Component.text("\n"));
      hover = hover.append(ComponentUtil.createTranslate("색상 : %s", Constant.THE_COLOR_HEX + horseColor.toString().toLowerCase().replace("_", " ")));
      hover = hover.append(Component.text("\n"));
      hover = hover.append(ComponentUtil.createTranslate("종 : %s", Constant.THE_COLOR_HEX + horseStyle.toString().toLowerCase().replace("_", " ")));
    }
    if (entity instanceof Llama llama)
    {
      Llama.Color llamaColor = llama.getColor();
      hover = hover.append(Component.text("\n"));
      hover = hover.append(ComponentUtil.createTranslate("색상 : %s", Constant.THE_COLOR_HEX + llamaColor.toString().toLowerCase().replace("_", " ")));
    }
    if (entity instanceof Slime slime)
    {
      hover = hover.append(Component.text("\n"));
      hover = hover.append(ComponentUtil.createTranslate("크기 : %s", Constant.THE_COLOR_HEX + slime.getSize()));
    }
    if (entity instanceof Turtle turtle)
    {
      Location home = turtle.getHome();
      hover = hover.append(Component.text("\n"));
      hover = hover.append(ComponentUtil.createTranslate("집 좌표 : %s", home));
    }
    if (entity instanceof Vex vex)
    {
      Mob mob = vex.getSummoner();
      if (mob != null)
      {
        hover = hover.append(Component.text("\n"));
        hover = hover.append(ComponentUtil.createTranslate("소환수 : %s", mob));
      }
    }
    if (entity instanceof Witch witch)
    {
      ItemStack drinkingPotion = witch.getDrinkingPotion();
      if (ItemStackUtil.itemExists(drinkingPotion))
      {
        List<Component> lore = ItemStackUtil.getItemInfoAsComponents(drinkingPotion, ComponentUtil.createTranslate("&e[%s(이)가 마시고 있는 물약]", nameComponent), true);
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
        hover = hover.append(ComponentUtil.createTranslate("직업 : %s", ComponentUtil.createTranslate(key)));
      }
      Villager.Type villagerType = zombieVillager.getVillagerType();
      Biome biome = Biome.valueOf(villagerType.toString().replace("SNOW", "SNOWY_TUNDRA"));
      hover = hover.append(Component.text("\n"));
      hover = hover.append(ComponentUtil.createTranslate("기후 : %s", Component.translatable("biome.minecraft." + biome.toString().toLowerCase()).color(Constant.THE_COLOR)));

      OfflinePlayer offlinePlayer = zombieVillager.getConversionPlayer();
      if (offlinePlayer != null)
      {
        hover = hover.append(Component.text("\n"));
        hover = hover.append(ComponentUtil.createTranslate("치료 지원자 : %s", offlinePlayer));
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
    if (entity instanceof Animals animals)
    {
      UUID breedCause = animals.getBreedCause();
      if (breedCause != null)
      {
        Entity breedCauseEntity = Bukkit.getEntity(breedCause);
        if (breedCauseEntity != null)
        {
          hover = hover.append(Component.text("\n"));
          hover = hover.append(ComponentUtil.createTranslate("사육사 : %s", breedCauseEntity));
        }
      }
    }
    if (entity instanceof CommandMinecart commandMinecart)
    {
      String command = commandMinecart.getCommand();
      if (!command.isEmpty())
      {
        hover = hover.append(Component.text("\n"));
        hover = hover.append(ComponentUtil.createTranslate("명령어 : %s", Component.text(command).color(Constant.THE_COLOR)));
      }
    }
    if (entity instanceof PoweredMinecart poweredMinecart)
    {
      int fuel = poweredMinecart.getFuel();
      hover = hover.append(Component.text("\n"));
      hover = hover.append(ComponentUtil.createTranslate("연료 : %s", Component.text(fuel).color(Constant.THE_COLOR)));
    }
    if (entity instanceof InventoryHolder inventoryHolder && !(inventoryHolder instanceof LivingEntity))
    {
      Component customNameLore = ComponentUtil.createTranslate("[%s의 내용물]", nameComponent);
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
              hover = hover.append(ComponentUtil.createTranslate("&7&ocontainer.shulkerBox.more", itemStackList.size() - i));
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
      hover = hover.append(Component.text("\n"));
      hover = hover.append(ComponentUtil.createTranslate("격수 : %s", projectileSource != null ? projectileSource : ComponentUtil.createTranslate("&c없음")));
      if (projectile instanceof ThrownPotion thrownPotion)
      {
        ItemStack item = thrownPotion.getItem();
        List<Component> lore = ItemStackUtil.getItemInfoAsComponents(item, ComponentUtil.createTranslate("&e[아이템]"), true);
        for (Component lor : lore)
        {
          hover = hover.append(Component.text("\n"));
          hover = hover.append(lor);
        }
      }
      if (projectile instanceof ThrowableProjectile throwableProjectile)
      {
        ItemStack item = throwableProjectile.getItem();
        List<Component> lore = ItemStackUtil.getItemInfoAsComponents(item, ComponentUtil.createTranslate("&e[아이템]"), true);
        for (Component lor : lore)
        {
          hover = hover.append(Component.text("\n"));
          hover = hover.append(lor);
        }
      }
      if (projectile instanceof AbstractArrow abstractArrow && !(abstractArrow instanceof Trident))
      {
        ItemStack itemStack = abstractArrow.getItemStack();
        List<Component> lore = ItemStackUtil.getItemInfoAsComponents(itemStack, ComponentUtil.createTranslate("&e[아이템]"), true);
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
          hover = hover.append(ComponentUtil.createTranslate("걸린 개체 : %s", hookedEntity));
        }
      }
      if (projectile instanceof ShulkerBullet shulkerBullet)
      {
        Entity target = shulkerBullet.getTarget();
        if (target != null)
        {
          hover = hover.append(Component.text("\n"));
          hover = hover.append(ComponentUtil.createTranslate("목표 개체 : %s", target));
        }
      }
      if (projectile instanceof Firework firework)
      {
        UUID spawningEntityUUID = firework.getSpawningEntity();
        if (spawningEntityUUID != null)
        {
          Entity spawningEntity = Bukkit.getEntity(spawningEntityUUID);
          if (spawningEntity != null)
          {
            hover = hover.append(Component.text("\n"));
            hover = hover.append(ComponentUtil.createTranslate("폭죽을 쏜 개체 : %s" + spawningEntity));
          }
        }
        ItemStack item = new ItemStack(Material.FIREWORK_ROCKET);
        item.setItemMeta(firework.getFireworkMeta());
        List<Component> lore = ItemStackUtil.getItemInfoAsComponents(item, ComponentUtil.createTranslate("&e[아이템]"), true);
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
      hover = hover.append(ComponentUtil.createTranslate("폭발 강도 : %s", Constant.THE_COLOR_HEX + Constant.Sosu2.format(yield)));
      hover = hover.append(Component.text("\n"));
      hover = hover.append(ComponentUtil.createTranslate("불 번짐 여부 : %s" + Constant.THE_COLOR_HEX + explosive.isIncendiary()));
    }
    if (entity instanceof TNTPrimed tntPrimed)
    {
      int fuseTicks = tntPrimed.getFuseTicks();
      if (fuseTicks > 0)
      {
        hover = hover.append(Component.text("\n"));
        hover = hover.append(ComponentUtil.createTranslate("폭발 대기 시간 : %s", Method.timeFormatMilli(fuseTicks * 50)));
      }
      Entity source = tntPrimed.getSource();
      if (source != null)
      {
        hover = hover.append(Component.text("\n"));
        hover = hover.append(ComponentUtil.createTranslate("불 붙인 개체 : %s", source));
      }
    }
    if (entity instanceof EnderSignal enderSignal)
    {
      hover = hover.append(Component.text("\n"));
      hover = hover.append(ComponentUtil.createTranslate("목표 지점 : %s", enderSignal.getTargetLocation()));
      ItemStack item = enderSignal.getItem();
      List<Component> lore = ItemStackUtil.getItemInfoAsComponents(item, ComponentUtil.createTranslate("&e[아이템]"), true);
      for (Component lor : lore)
      {
        hover = hover.append(Component.text("\n"));
        hover = hover.append(lor);
      }
    }
    if (entity instanceof Painting painting)
    {
      hover = hover.append(Component.text("\n"));
      hover = hover.append(ComponentUtil.createTranslate("종류 : %s", Constant.THE_COLOR_HEX + painting.getArt().toString().toLowerCase().replace("_", " ")));
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
      if (itemFrame.isFixed())
      {
        hover = hover.append(Component.text("\n"));
        hover = hover.append(ComponentUtil.createTranslate("&a&o고정됨"));
      }
      if (!itemFrame.isVisible())
      {
        hover = hover.append(Component.text("\n"));
        hover = hover.append(ComponentUtil.createTranslate("&7&o투명함"));
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
    if (entity instanceof ExperienceOrb experienceOrb)
    {
      int experience = experienceOrb.getExperience();
      hover = hover.append(Component.text("\n"));
      hover = hover.append(ComponentUtil.createTranslate("경험치 : %s", Constant.THE_COLOR_HEX + experience));
    }

    if (Cucumbery.config.getBoolean("use-hover-event-for-entities.unfair-play-mode.enabled") && Cucumbery.config.getBoolean("use-hover-event-for-entities.unfair-play-mode.location"))
    {
      Location location = entity.getLocation();
      hover = hover.append(Component.text("\n"));
      hover = hover.append(ComponentUtil.createTranslate("좌표 : %s", location));
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
          hover = hover.append(ComponentUtil.createTranslate("HP : %s", ComponentUtil.createTranslate("&7%s / %s", healthDisplay, maxHealthDisplay)));
        }
      }
    }
    if (defaultColor != null && nameComponent.color() == null)
    {
      nameComponent = nameComponent.color(defaultColor);
    }
    if (entity instanceof Player player)
    {
      hover = hover
              .append(Component.text("\n"))
              .append(ComponentUtil.createTranslate("현재 시각 : %s", Constant.THE_COLOR_HEX + Method.getCurrentTime(Calendar.getInstance())));
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
      hover = hover.append(Component.text("\n"));
      hover = hover.append(Component.text("minecraft:" + entity.getType().toString().toLowerCase(), NamedTextColor.DARK_GRAY));
      nameComponent = nameComponent.clickEvent(ClickEvent.suggestCommand(click));
    }
    if (Cucumbery.config.getBoolean("use-hover-event-for-entities.enabled"))
    {
      nameComponent = nameComponent.hoverEvent(hover);
    }
    return nameComponent;
  }
}
