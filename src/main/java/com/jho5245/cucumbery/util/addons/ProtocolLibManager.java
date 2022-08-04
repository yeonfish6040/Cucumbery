package com.jho5245.cucumbery.util.addons;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.reflect.StructureModifier;
import com.comphenix.protocol.wrappers.WrappedParticle;
import com.jho5245.cucumbery.Cucumbery;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.entity.Player;

public class ProtocolLibManager
{
  public static void manage()
  {
    ProtocolManager protocolManager = ProtocolLibrary.getProtocolManager();
    protocolManager.addPacketListener(new PacketAdapter(Cucumbery.getPlugin(), ListenerPriority.HIGH, PacketType.Play.Server.WORLD_PARTICLES)
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
//    protocolManager.addPacketListener(new PacketAdapter(Cucumbery.getPlugin(), ListenerPriority.NORMAL, PacketType.Play.Client.BLOCK_DIG)
//    {
//      @Override
//      public void onPacketReceiving(PacketEvent event)
//      {
//        PacketContainer packet = event.getPacket();
//        Player player = event.getPlayer();
//        BlockPosition blockPosition = packet.getBlockPositionModifier().read(0);
//        Location location = blockPosition.toLocation(player.getWorld());
//        int i = packet.getIntegers().read(0);
//        event.setCancelled(true);
//        player.sendBlockDamage(location, (float) Math.random());
//        if (i > 0)
//        {
//          packet.getIntegers().write(0, 0);
//          CustomEffectManager.addEffect(player, new LocationCustomEffectImple(CustomEffectType.CUSTOM_MINING_SPEED_MODE_PROGRESS, location), ApplyReason.PLUGIN, false, false);
//        }
//        MessageUtil.broadcastDebug(packet.getByteArrays().size() + ", i : " + i);
//      }
//    });
  }
}
