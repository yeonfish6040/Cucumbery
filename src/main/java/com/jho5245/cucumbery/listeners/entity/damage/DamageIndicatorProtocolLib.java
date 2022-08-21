package com.jho5245.cucumbery.listeners.entity.damage;

import com.comphenix.protocol.PacketType.Play.Server;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.WrappedChatComponent;
import com.comphenix.protocol.wrappers.WrappedDataWatcher.Registry;
import com.comphenix.protocol.wrappers.WrappedDataWatcher.WrappedDataWatcherObject;
import com.comphenix.protocol.wrappers.WrappedWatchableObject;
import com.jho5245.cucumbery.Cucumbery;
import com.jho5245.cucumbery.util.no_groups.Method;
import com.jho5245.cucumbery.util.storage.component.util.ComponentUtil;
import com.jho5245.cucumbery.util.storage.no_groups.CustomConfig.UserData;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class DamageIndicatorProtocolLib
{
  protected static void displayDamage(boolean viewSelf, @NotNull Entity entity, @NotNull Location location, @NotNull Component finalDisplay)
  {
    int entityId = Method.random(1, Integer.MAX_VALUE);
    ProtocolManager protocolManager = ProtocolLibrary.getProtocolManager();
    PacketContainer packet = protocolManager.createPacket(Server.SPAWN_ENTITY);
    packet.getIntegers().write(0, entityId);
    packet.getEntityTypeModifier().write(0, EntityType.ARMOR_STAND);
    // Set optional velocity (/8000)
    packet.getIntegers().write(1, 0);
    packet.getIntegers().write(2, 0);
    packet.getIntegers().write(3, 0);
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
      try
      {
        protocolManager.sendServerPacket(player, packet);
        PacketContainer edit = protocolManager.createPacket(Server.ENTITY_METADATA);
        List<WrappedWatchableObject> list = new ArrayList<>();
        list.add(new WrappedWatchableObject(new WrappedDataWatcherObject(0, Registry.get(Byte.class)), (byte) 0x20)); // invisible
        list.add(new WrappedWatchableObject(new WrappedDataWatcherObject(2, Registry.getChatComponentSerializer(true)),
                Optional.of(
                        WrappedChatComponent.fromJson(ComponentUtil.serializeAsJson(finalDisplay)).getHandle()
                ))
        ); // customname
        list.add(new WrappedWatchableObject(new WrappedDataWatcherObject(3, Registry.get(Boolean.class)), true)); // customnamevisible = true
        list.add(new WrappedWatchableObject(new WrappedDataWatcherObject(15, Registry.get(Byte.class)), (byte) 0x10)); // marker
        edit.getWatchableCollectionModifier().write(0, list);
        edit.getIntegers().write(0, entityId);
        protocolManager.sendServerPacket(player, edit);
        PacketContainer teleport = protocolManager.createPacket(Server.ENTITY_TELEPORT);
        teleport.getIntegers().write(0, entityId);
        for (int i = 0; i < 20; i++)
        {
          int finalI = i;
          Bukkit.getScheduler().runTaskLater(Cucumbery.getPlugin(), () ->
          {
            try
            {
              teleport.getDoubles().write(0, location.getX());
              teleport.getDoubles().write(1, location.getY() + (finalI * 0.02));
              teleport.getDoubles().write(2, location.getZ());
              protocolManager.sendServerPacket(player, teleport);
            }
            catch (InvocationTargetException e)
            {
              e.printStackTrace();
            }
          }, i);
        }
        Bukkit.getScheduler().runTaskLater(Cucumbery.getPlugin(), () -> {
          PacketContainer remove = protocolManager.createPacket(Server.ENTITY_DESTROY);
          remove.getIntLists().write(0, List.of(entityId));
          try
          {
            protocolManager.sendServerPacket(player, remove);
          }
          catch (InvocationTargetException e)
          {
            e.printStackTrace();
          }
        }, 20L);
      }
      catch (InvocationTargetException e)
      {
        e.printStackTrace();
      }
    }
  }
}
