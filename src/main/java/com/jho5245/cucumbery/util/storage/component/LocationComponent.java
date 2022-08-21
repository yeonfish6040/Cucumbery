package com.jho5245.cucumbery.util.storage.component;

import com.jho5245.cucumbery.util.storage.component.util.ComponentUtil;
import com.jho5245.cucumbery.util.storage.data.Constant;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import org.bukkit.Location;
import org.bukkit.World;
import org.jetbrains.annotations.NotNull;

public class LocationComponent
{
  @NotNull
  public static Component locationComponent(@NotNull Location location)
  {
    World world = location.getWorld();
    String worldName = world.getName();
    double x = location.getX(), y = location.getY(), z = location.getZ(), yaw = location.getYaw(), pitch = location.getPitch();
    Component xComponent = Component.text(Constant.Sosu2.format(x)).color(Constant.THE_COLOR);
    Component yComponent = Component.text(Constant.Sosu2.format(y)).color(Constant.THE_COLOR);
    Component zComponent = Component.text(Constant.Sosu2.format(z)).color(Constant.THE_COLOR);
    Component yawComponent = Component.text(Constant.Sosu2.format(yaw)).color(Constant.THE_COLOR);
    Component pitchComponent = Component.text(Constant.Sosu2.format(pitch)).color(Constant.THE_COLOR);
    return ComponentUtil.translate("&7%s, %s, %s, %s", world, xComponent, yComponent, zComponent)
            .hoverEvent(ComponentUtil.translate(
                    "클릭하여 %s, %s, %s, %s, %s, %s 좌표로 텔레포트합니다",
                    Constant.THE_COLOR_HEX + worldName, xComponent, yComponent, zComponent, yawComponent, pitchComponent
            ))
            .clickEvent(ClickEvent.suggestCommand("/atp @s " + worldName + " " + x + " " + y + " " + z + " " + yaw + " " + pitch));
  }
}
