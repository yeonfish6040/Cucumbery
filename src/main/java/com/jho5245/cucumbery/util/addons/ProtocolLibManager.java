package com.jho5245.cucumbery.util.addons;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.PacketType.Play.Client;
import com.comphenix.protocol.PacketType.Play.Server;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.reflect.StructureModifier;
import com.comphenix.protocol.wrappers.EnumWrappers.PlayerDigType;
import com.comphenix.protocol.wrappers.WrappedParticle;
import com.jho5245.cucumbery.Cucumbery;
import com.jho5245.cucumbery.custom.customeffect.CustomEffectManager;
import com.jho5245.cucumbery.custom.customeffect.children.group.LocationCustomEffectImple;
import com.jho5245.cucumbery.custom.customeffect.type.CustomEffectType;
import com.jho5245.cucumbery.custom.customeffect.type.CustomEffectTypeCustomMining;
import com.jho5245.cucumbery.custom.customeffect.type.CustomEffectTypeMinecraft;
import com.jho5245.cucumbery.util.no_groups.Method2;
import com.jho5245.cucumbery.util.storage.data.Constant;
import com.jho5245.cucumbery.util.storage.data.CustomMaterial;
import com.jho5245.cucumbery.util.storage.data.Variable;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Constructor;

public class ProtocolLibManager
{
  //  private static final List<Item> items = new ArrayList<>();
//  private static final List<BukkitTask> tasks = new ArrayList<>();
  public static void manage()
  {
    ProtocolManager protocolManager = ProtocolLibrary.getProtocolManager();
    protocolManager.addPacketListener(new PacketAdapter(Cucumbery.getPlugin(), ListenerPriority.HIGH, Server.WORLD_PARTICLES)
    {
      @Override
      public void onPacketSending(PacketEvent event)
      {
        if (!Cucumbery.using_ProtocolLib)
        {
          return;
        }
        PacketContainer packet = event.getPacket();
        if (packet.getType() == PacketType.Play.Server.WORLD_PARTICLES)
        {
          StructureModifier<Object> modifier = packet.getModifier();
          int particleSize = (int) modifier.read(7);
          @SuppressWarnings("all")
          StructureModifier<WrappedParticle> particles = packet.getNewParticles();
          for (WrappedParticle<?> particle : particles.getValues())
          {
            int configSize = Cucumbery.config.getInt("use-damage-indicator.protocollib.max-vanilla-damage-indicator-particles");
            if (configSize < 0)
            {
              return;
            }
            if (particle.getParticle() == Particle.DAMAGE_INDICATOR && particleSize > configSize)
            {
              event.setCancelled(true);
              if (configSize == 0)
              {
                return;
              }
              double x = (double) modifier.read(0), y = (double) modifier.read(1), z = (double) modifier.read(2);
              float offsetX = (float) modifier.read(3), offSetY = (float) modifier.read(4), offsetZ = (float) modifier.read(5);
              float speed = (float) modifier.read(6);
              boolean force = (boolean) modifier.read(8);
              Player player = event.getPlayer();
              World world = player.getWorld();
              world.spawnParticle(Particle.DAMAGE_INDICATOR, x, y, z, configSize, offsetX, offSetY, offsetZ, speed, null, force);
              break;
            }
          }
        }
      }
    });
//    protocolManager.addPacketListener(new PacketAdapter(Cucumbery.getPlugin(), ListenerPriority.NORMAL, Server.SPAWN_ENTITY)
//    {
//      @Override
//      public void onPacketSending(PacketEvent event)
//      {
//        if (!Cucumbery.using_ProtocolLib)
//        {
//          return;
//        }
//        PacketContainer packet = event.getPacket();
//        if (packet.getType() == Server.SPAWN_ENTITY)
//        {
//          EntityType entityType = packet.getEntityTypeModifier().read(0);
//          if (entityType == EntityType.DROPPED_ITEM)
//          {
//            int id = packet.getIntegers().read(0);
//            Bukkit.getScheduler().runTaskLaterAsynchronously(Cucumbery.getPlugin(), () ->
//            {
//              Item item = (Item) Method2.getEntityById(id);
//              if (item != null)
//              {
//                Method.updateItem(item);
//              }
//            }, 0L);
//          }
//        }
//      }
//    });
    protocolManager.addPacketListener(new PacketAdapter(Cucumbery.getPlugin(), ListenerPriority.NORMAL, Server.ENTITY_EFFECT)
    {
      @Override
      public void onPacketSending(PacketEvent event)
      {
        if (!Cucumbery.using_ProtocolLib)
        {
          return;
        }
        PacketContainer packet = event.getPacket();
        if (packet.getType() == Server.ENTITY_EFFECT)
        {
          int id = packet.getIntegers().read(0);
          Entity entity = Method2.getEntityById(id);
          StructureModifier<PotionEffectType> effectTypes = packet.getEffectTypes();
          PotionEffectType potionEffectType = effectTypes.read(0);
          StructureModifier<Object> modifier = packet.getModifier();
          @SuppressWarnings("unused")
          byte amplifier = (byte) modifier.read(2);
          int duration = (int) modifier.read(3);
          if (entity instanceof Player player)
          {
            if (CustomEffectManager.hasEffect(player, CustomEffectTypeCustomMining.CUSTOM_MINING_SPEED_MODE))
            {
              if (potionEffectType.equals(PotionEffectType.FAST_DIGGING) && duration < 3 && amplifier == 0)
              {
                modifier.write(3, -1);
                event.setPacket(packet);
              }
              if (potionEffectType.equals(PotionEffectType.SLOW_DIGGING) && duration < 3 && amplifier == 0)
              {
                modifier.write(3, -1);
                event.setPacket(packet);
              }
              if (potionEffectType.equals(PotionEffectType.SLOW_DIGGING))
              {
                modifier.write(2, (byte) 127);
                event.setPacket(packet);
              }
            }

            if (CustomEffectManager.hasEffect(player, CustomEffectTypeMinecraft.SPEED) && potionEffectType.equals(PotionEffectType.SPEED))
            {
              modifier.write(3, CustomEffectManager.getEffect(player, CustomEffectTypeMinecraft.SPEED).getDuration());
              event.setPacket(packet);
            }

            if (CustomEffectManager.hasEffect(player, CustomEffectTypeMinecraft.SLOWNESS) && potionEffectType.equals(PotionEffectType.SLOW))
            {
              modifier.write(3, CustomEffectManager.getEffect(player, CustomEffectTypeMinecraft.SLOWNESS).getDuration());
              event.setPacket(packet);
            }

            if (CustomEffectManager.hasEffect(player, CustomEffectTypeMinecraft.HASTE) && potionEffectType.equals(PotionEffectType.FAST_DIGGING))
            {
              modifier.write(3, CustomEffectManager.getEffect(player, CustomEffectTypeMinecraft.HASTE).getDuration());
              event.setPacket(packet);
            }

            if (CustomEffectManager.hasEffect(player, CustomEffectTypeMinecraft.MINING_FATIGUE) && potionEffectType.equals(PotionEffectType.SLOW_DIGGING))
            {
              modifier.write(3, CustomEffectManager.getEffect(player, CustomEffectTypeMinecraft.MINING_FATIGUE).getDuration());
              event.setPacket(packet);
            }

            if (CustomEffectManager.hasEffect(player, CustomEffectTypeMinecraft.NIGHT_VISION) && potionEffectType.equals(PotionEffectType.NIGHT_VISION))
            {
              modifier.write(3, CustomEffectManager.getEffect(player, CustomEffectTypeMinecraft.NIGHT_VISION).getDuration());
              event.setPacket(packet);
            }

            if (CustomEffectManager.hasEffect(player, CustomEffectTypeMinecraft.DYNAMIC_LIGHT) && potionEffectType.equals(PotionEffectType.NIGHT_VISION))
            {
              PlayerInventory inventory = player.getInventory();
              Material mainHand = inventory.getItemInMainHand().getType(), offHand = inventory.getItemInOffHand().getType();
              if (Constant.OPTIFINE_DYNAMIC_LIGHT_ITEMS.contains(mainHand) || Constant.OPTIFINE_DYNAMIC_LIGHT_ITEMS.contains(offHand))
              {
                modifier.write(3, Integer.MAX_VALUE);
                event.setPacket(packet);
              }
            }

            if (CustomEffectManager.hasEffect(player, CustomEffectType.NIGHT_VISION_SPECTATOR) && potionEffectType.equals(PotionEffectType.NIGHT_VISION) && player.getSpectatorTarget() != null)
            {
              modifier.write(3, Integer.MAX_VALUE);
              event.setPacket(packet);
            }

            PlayerInventory playerInventory = player.getInventory();
            ItemStack helmet = playerInventory.getHelmet(), chestplate = playerInventory.getChestplate(), leggings = playerInventory.getLeggings(), boots = playerInventory.getBoots();
            CustomMaterial helmetType = CustomMaterial.itemStackOf(helmet), chestplateType = CustomMaterial.itemStackOf(chestplate), leggingsType = CustomMaterial.itemStackOf(leggings), bootsType = CustomMaterial.itemStackOf(boots);

            if ((helmetType == CustomMaterial.MINER_HELMET || helmetType == CustomMaterial.MINDAS_HELMET) && potionEffectType.equals(PotionEffectType.NIGHT_VISION) && duration < 3)
            {
              modifier.write(3, -1);
              event.setPacket(packet);
            }

            if (helmetType == CustomMaterial.MINER_HELMET &&
                    chestplateType == CustomMaterial.MINER_CHESTPLATE &&
                    leggingsType == CustomMaterial.MINER_LEGGINGS &&
                    bootsType == CustomMaterial.MINER_BOOTS && potionEffectType.equals(PotionEffectType.FAST_DIGGING) && duration < 5)
            {
              packet.getModifier().write(3, -1);
              event.setPacket(packet);
            }

            if (helmetType == CustomMaterial.FROG_HELMET &&
                    chestplateType == CustomMaterial.FROG_CHESTPLATE &&
                    leggingsType == CustomMaterial.FROG_LEGGINGS &&
                    bootsType == CustomMaterial.FROG_BOOTS && potionEffectType.equals(PotionEffectType.JUMP) && duration < 3)
            {
              packet.getModifier().write(3, -1);
              event.setPacket(packet);
            }
          }
        }
      }
    });
    protocolManager.addPacketListener(new PacketAdapter(Cucumbery.getPlugin(), ListenerPriority.NORMAL, Server.BLOCK_CHANGE)
    {
      @Override
      public void onPacketSending(PacketEvent event)
      {
        if (!Cucumbery.using_ProtocolLib)
        {
          return;
        }
        PacketContainer packet = event.getPacket();
        if (packet.getType() == Server.BLOCK_CHANGE)
        {
          Player player = event.getPlayer();
          StructureModifier<Object> modifier = packet.getModifier();
          int[] ary = getBlockPositionOf(modifier.read(0));
          Location location = new Location(player.getWorld(), ary[0], ary[1], ary[2]);
          if (Variable.customMiningCooldown.containsKey(location) || Variable.customMiningExtraBlocks.containsKey(location) || Variable.fakeBlocks.containsKey(location))
          {
            Material type = packet.getBlockData().read(0).getType();
            Material locationType = location.getBlock().getType();
            if (type == location.getBlock().getType() && !Variable.customMiningMode2BlockData.containsKey(location) ||
                    (Variable.customMiningMode2BlockData.containsKey(location) && locationType != Variable.customMiningMode2BlockData.get(location).getMaterial()))
            {
              if ((!Variable.fakeBlocks.containsKey(location) || Variable.fakeBlocks.get(location).getMaterial() != type) && type != Material.AIR)
              {
                event.setCancelled(true);
              }
            }
          }
          if (!event.isCancelled())
          {
            if (!Variable.customMiningCooldown.containsKey(location) && !Variable.customMiningExtraBlocks.containsKey(location) && !Variable.customMiningMode2BlockData.containsKey(location) && Variable.fakeBlocks.containsKey(location))
            {
              Material type = packet.getBlockData().read(0).getType();
              if (Variable.fakeBlocks.get(location).getMaterial() != Material.AIR && type == Material.AIR)
              {
                event.setCancelled(true);
              }
            }
          }
        }
      }
    });

    protocolManager.addPacketListener(new PacketAdapter(Cucumbery.getPlugin(), ListenerPriority.NORMAL, Client.BLOCK_DIG)
    {
      @Override
      public void onPacketReceiving(PacketEvent event)
      {
        if (!Cucumbery.using_ProtocolLib)
        {
          return;
        }
        PacketContainer packet = event.getPacket();
        Player player = event.getPlayer();
        if (player.isSneaking() && CustomEffectManager.hasEffect(player, CustomEffectTypeCustomMining.CUSTOM_MINING_SPEED_MODE))
        {
          PlayerDigType playerDigType = packet.getPlayerDigTypes().read(0);
          if (playerDigType == PlayerDigType.START_DESTROY_BLOCK)
          {
            if (CustomEffectManager.hasEffect(player, CustomEffectType.CURSE_OF_CREATIVITY) || CustomEffectManager.hasEffect(player, CustomEffectType.CURSE_OF_CREATIVITY_BREAK))
            {
              event.setCancelled(true);
              return;
            }
            int[] pos = getBlockPositionOf(packet.getModifier().read(0));
            Location location = new Location(player.getWorld(), pos[0], pos[1], pos[2]);
            if (!Variable.customMiningCooldown.containsKey(location) || Variable.customMiningExtraBlocks.containsKey(location))
            {
              // 가끔 hasEffect가 true인데 getEffect가 null을 반환함 왜?
              try
              {
                CustomEffectManager.addEffect(player, new LocationCustomEffectImple(CustomEffectTypeCustomMining.CUSTOM_MINING_SPEED_MODE_PROGRESS, location));
              }
              catch (IllegalStateException ignored)
              {

              }
              catch (Throwable t)
              {
Cucumbery.getPlugin().getLogger().warning(                t.getMessage());
              }
            }
          }
        }
      }
    });
//    protocolManager.addPacketListener(new PacketAdapter(Cucumbery.getPlugin(), ListenerPriority.NORMAL, Server.NBT_QUERY)
//    {
//      @Override
//      public void onPacketSending(PacketEvent event)
//      {
//        if (!Cucumbery.using_ProtocolLib)
//        {
//          return;
//        }
//        PacketContainer packet = event.getPacket();
//        if (packet.getType() == Server.NBT_QUERY)
//        {
//          Player player = event.getPlayer();
//          StructureModifier<Object> modifier = packet.getModifier();
//          MessageUtil.broadcastDebug(modifier.size() + ", ", player);
//        }
//      }
//    });
//    protocolManager.addPacketListener(new PacketAdapter(Cucumbery.getPlugin(), ListenerPriority.NORMAL, Client.BLOCK_DIG)
//    {
//      @Override
//      public void onPacketReceiving(PacketEvent event)
//      {
//        if (!Cucumbery.using_ProtocolLib)
//        {
//          return;
//        }
//        PacketContainer packet = event.getPacket();
//        if (packet.getType() == Client.BLOCK_DIG)
//        {
//          Player player = event.getPlayer();
//          if (CustomEffectManager.hasEffect(player, CustomEffectTypeCustomMining.CUSTOM_MINING_SPEED_MODE))
//          {
//            event.setCancelled(true);
////            StructureModifier<Object> modifier = packet.getModifier();
////            String pos = modifier.read(0).toString().replace("}", "");
////            String[] split = pos.split(", ");
////            int x = Integer.parseInt(split[0].split("x=")[1]);
////            int y = Integer.parseInt(split[1].split("y=")[1]);
////            int z = Integer.parseInt(split[2].split("z=")[1]);
////            Block block = player.getWorld().getBlockAt(x, y, z);
////            String eventType = modifier.read(2).toString();
////            if (eventType.startsWith("START"))
////            {
////              BlockDamageEvent blockDamageEvent = new BlockDamageEvent(player, block, player.getInventory().getItemInMainHand(), false);
////              Bukkit.getPluginManager().callEvent(blockDamageEvent);
////            }
////            else if (eventType.startsWith("ABORT"))
////            {
////              BlockDamageAbortEvent blockDamageAbortEvent = new BlockDamageAbortEvent(player, block, player.getInventory().getItemInMainHand());
////              Bukkit.getPluginManager().callEvent(blockDamageAbortEvent);
////            }
//          }
//        }
//      }
//    });
//    protocolManager.addPacketListener(new PacketAdapter(Cucumbery.getPlugin(), ListenerPriority.NORMAL, Server.BLOCK_BREAK)
//    {
//      @Override
//      public void onPacketSending(PacketEvent event)
//      {
//        if (!Cucumbery.using_ProtocolLib)
//        {
//          return;
//        }
//        PacketContainer packet = event.getPacket();
//        if (packet.getType() == Server.BLOCK_BREAK)
//        {
//          int id = packet.getIntegers().read(0);
//          Entity entity = Method2.getEntityById(id);
//          if (entity instanceof Player player && player.getGameMode() != GameMode.CREATIVE && CustomEffectManager.hasEffect(player, CustomEffectTypeCustomMining.CUSTOM_MINING_SPEED_MODE) && !CustomEffectManager.hasEffect(player, CustomEffectTypeCustomMining.CUSTOM_MINING_SPEED_MODE_2_NO_RESTORE))
//          {
//            event.setCancelled(true);
//          }
//        }
//      }
//    });
  }

  public static int[] getBlockPositionOf(@NotNull Object object)
  {
    String pos = object.toString().replace("}", "");
    String[] split = pos.split(", ");
    int x = Integer.parseInt(split[0].split("x=")[1]);
    int y = Integer.parseInt(split[1].split("y=")[1]);
    int z = Integer.parseInt(split[2].split("z=")[1]);
    return new int[]{x, y, z};
  }

  @NotNull
  public static Object getBlockPosition(int x, int y, int z)
  {
    try
    {
      Class<?> clazz = Class.forName("net.minecraft.core.BlockPosition");
      Constructor<?> constructor = clazz.getConstructors()[4];
      return constructor.newInstance(x, y, z);
    }
    catch (Exception e)
    {
Cucumbery.getPlugin().getLogger().warning(      e.getMessage());
    }
    throw new IllegalStateException();
  }

  @NotNull
  public static Object getBlockPosition(@NotNull Location location)
  {
    return getBlockPosition(location.getBlockX(), location.getBlockY(), location.getBlockZ());
  }
}
