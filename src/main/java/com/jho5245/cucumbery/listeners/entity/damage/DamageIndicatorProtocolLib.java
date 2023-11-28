package com.jho5245.cucumbery.listeners.entity.damage;

import com.comphenix.protocol.PacketType.Play.Server;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.reflect.StructureModifier;
import com.comphenix.protocol.wrappers.WrappedChatComponent;
import com.comphenix.protocol.wrappers.WrappedDataValue;
import com.comphenix.protocol.wrappers.WrappedDataWatcher.Registry;
import com.google.common.collect.Lists;
import com.jho5245.cucumbery.Cucumbery;
import com.jho5245.cucumbery.util.no_groups.Method;
import com.jho5245.cucumbery.util.storage.component.util.ComponentUtil;
import com.jho5245.cucumbery.util.storage.no_groups.CustomConfig.UserData;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector3f;

import java.util.List;
import java.util.UUID;

public class DamageIndicatorProtocolLib
{
  protected static void displayDamage(boolean viewSelf, @NotNull Entity entity, @NotNull Location location, @NotNull Component finalDisplay, float sizeModifier)
  {
    int entityId = Method.random(1, Integer.MAX_VALUE);
    ProtocolManager protocolManager = ProtocolLibrary.getProtocolManager();
    PacketContainer packet = protocolManager.createPacket(Server.SPAWN_ENTITY);
    packet.getIntegers().write(0, entityId);
    packet.getEntityTypeModifier().write(0, EntityType.TEXT_DISPLAY);
    // Set location
    packet.getDoubles().write(0, location.getX());
    packet.getDoubles().write(1, location.getY());
    packet.getDoubles().write(2, location.getZ());
    // Set UUID
    packet.getUUIDs().write(0, UUID.randomUUID());
    for (Player player : Bukkit.getOnlinePlayers())
    {
      if (!viewSelf && player == entity)
      {
        continue;
      }
      if (!UserData.SHOW_DAMAGE_INDICATOR.getBoolean(player))
      {
        continue;
      }
      if (player.getGameMode() == GameMode.SPECTATOR && player.getSpectatorTarget() == entity && !UserData.SHOW_DAMAGE_INDICATOR_SPECTATING_ENTITY.getBoolean(player))
      {
        continue;
      }
      protocolManager.sendServerPacket(player, packet);

//      edit.getIntegers().write(0, entityId);
//      Entity e = edit.getEntityModifier(player.getWorld()).read(0);
//      WrappedDataWatcher dataWatcher = WrappedDataWatcher.getEntityWatcher(e).deepClone();
//      dataWatcher.setObject(0, (byte) 0x20);
//      Serializer serializer = Registry.getChatComponentSerializer(true);
//      WrappedDataWatcherObject optChatFieldWatcher = new WrappedDataWatcherObject(2, serializer);
//      Optional<Object> optChatField = Optional.of(WrappedChatComponent.fromJson(ComponentUtil.serializeAsJson(finalDisplay)).getHandle());
//      dataWatcher.setObject(optChatFieldWatcher, optChatField);
//      dataWatcher.setObject(3, true);
//      dataWatcher.setObject(15, (byte) 0x10);
//      edit.getWatchableCollectionModifier().write(0, dataWatcher.getWatchableObjects());
//      protocolManager.sendServerPacket(player, edit);

//      StructureModifier<List<WrappedDataValue>> watchableAccessor = edit.getDataValueCollectionModifier();
//      List<WrappedDataValue> values = Lists.newArrayList(
//              new WrappedDataValue(0, Registry.get(Byte.class), (byte) 0x20),
//              new WrappedDataValue(2, Registry.getChatComponentSerializer(true), Optional.of(
//                      WrappedChatComponent.fromJson(ComponentUtil.serializeAsJson(finalDisplay)).getHandle()
//              )),
//              new WrappedDataValue(3, Registry.get(Boolean.class), true),
//              new WrappedDataValue(15, Registry.get(Byte.class), (byte) 0x10)
//      );
//      watchableAccessor.write(0, values);
//      Bukkit.getScheduler().runTaskLater(Cucumbery.getPlugin(), () ->
//      {

      //                  new WrappedDataValue(22, Registry.getChatComponentSerializer(true), Optional.of(
//                          wrappedChatComponent.getHandle()
//                  )),

      PacketContainer edit = protocolManager.createPacket(Server.ENTITY_METADATA);
      StructureModifier<List<WrappedDataValue>> watchableAccessor = edit.getDataValueCollectionModifier();
      WrappedChatComponent wrappedChatComponent = WrappedChatComponent.fromJson(ComponentUtil.serializeAsJson(finalDisplay));
      List<WrappedDataValue> values = Lists.newArrayList(
              new WrappedDataValue(11, Registry.get(Vector3f.class), new Vector3f(0f, 0.2f * sizeModifier, 0f)), // Translation
              new WrappedDataValue(12, Registry.get(Vector3f.class), new Vector3f(1.2f * sizeModifier, 1.2f * sizeModifier, 1.2f * sizeModifier)), // Scale
              new WrappedDataValue(15, Registry.get(Byte.class), (byte) 3), // Billboard
              new WrappedDataValue(16, Registry.get(Integer.class), (15 << 4 | 15 << 20)), // Brightness override
              new WrappedDataValue(17, Registry.get(Float.class), 2f), // view range
              new WrappedDataValue(19, Registry.get(Float.class), 0f), // shadow strength
              new WrappedDataValue(23, Registry.getChatComponentSerializer(), wrappedChatComponent.getHandle()), // text
              new WrappedDataValue(25, Registry.get(Integer.class), 0), // background color
              new WrappedDataValue(26, Registry.get(Byte.class), (byte) -1), // text opacity
              new WrappedDataValue(27, Registry.get(Byte.class), (byte) 0x01) // shadow / see through / default bgcolor / alignment
      );
      watchableAccessor.write(0, values);
      edit.getIntegers().write(0, entityId);
      protocolManager.sendServerPacket(player, edit);

      Bukkit.getScheduler().runTaskLater(Cucumbery.getPlugin(), () ->
      {
        PacketContainer edit2 = protocolManager.createPacket(Server.ENTITY_METADATA);
        StructureModifier<List<WrappedDataValue>> watchableAccessor2 = edit2.getDataValueCollectionModifier();
        List<WrappedDataValue> values2 = Lists.newArrayList(
                new WrappedDataValue(8, Registry.get(Integer.class), -1), // interpolation delay
                new WrappedDataValue(9, Registry.get(Integer.class), 10), // position/roation interpolation duration
                new WrappedDataValue(11, Registry.get(Vector3f.class), new Vector3f(0f, 0.4f * sizeModifier, 0f)) // translation
        );
        watchableAccessor2.write(0, values2);
        edit2.getIntegers().write(0, entityId);
        protocolManager.sendServerPacket(player, edit2);
      }, 2L);

      Bukkit.getScheduler().runTaskLater(Cucumbery.getPlugin(), () ->
      {
        PacketContainer edit2 = protocolManager.createPacket(Server.ENTITY_METADATA);
        StructureModifier<List<WrappedDataValue>> watchableAccessor2 = edit2.getDataValueCollectionModifier();
        List<WrappedDataValue> values2 = Lists.newArrayList(
                new WrappedDataValue(8, Registry.get(Integer.class), -1), // interpolation delay
                new WrappedDataValue(9, Registry.get(Integer.class), 5), // position/roation interpolation duration
                new WrappedDataValue(11, Registry.get(Vector3f.class), new Vector3f(0f, 0.5f * sizeModifier, 0f)), // translation
                new WrappedDataValue(26, Registry.get(Byte.class), (byte) -127) // text opacity
        );
        watchableAccessor2.write(0, values2);
        edit2.getIntegers().write(0, entityId);
        protocolManager.sendServerPacket(player, edit2);
      }, 12L);

      Bukkit.getScheduler().runTaskLater(Cucumbery.getPlugin(), () ->
      {
        PacketContainer edit2 = protocolManager.createPacket(Server.ENTITY_METADATA);
        StructureModifier<List<WrappedDataValue>> watchableAccessor2 = edit2.getDataValueCollectionModifier();
        List<WrappedDataValue> values2 = Lists.newArrayList(
                new WrappedDataValue(26, Registry.get(Byte.class), (byte) 127) // text opacity
        );
        watchableAccessor2.write(0, values2);
        edit2.getIntegers().write(0, entityId);
        protocolManager.sendServerPacket(player, edit2);
      }, 15L);

      Bukkit.getScheduler().runTaskLater(Cucumbery.getPlugin(), () ->
      {
        PacketContainer edit2 = protocolManager.createPacket(Server.ENTITY_METADATA);
        StructureModifier<List<WrappedDataValue>> watchableAccessor2 = edit2.getDataValueCollectionModifier();
        List<WrappedDataValue> values2 = Lists.newArrayList(
                new WrappedDataValue(8, Registry.get(Integer.class), -1), // interpolation delay
                new WrappedDataValue(9, Registry.get(Integer.class), 5), // position/roation interpolation duration
                new WrappedDataValue(11, Registry.get(Vector3f.class), new Vector3f(0f, 0.6f * sizeModifier, 0f)), // translation
                new WrappedDataValue(26, Registry.get(Byte.class), (byte) 5) // text opacity
        );
        watchableAccessor2.write(0, values2);
        edit2.getIntegers().write(0, entityId);
        protocolManager.sendServerPacket(player, edit2);
      }, 16L);


      Bukkit.getScheduler().runTaskLater(Cucumbery.getPlugin(), () ->
      {
        PacketContainer remove = protocolManager.createPacket(Server.ENTITY_DESTROY);
        remove.getIntLists().write(0, List.of(entityId));
        protocolManager.sendServerPacket(player, remove);
      }, 23L);
    }
  }
}
