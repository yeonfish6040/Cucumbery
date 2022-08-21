package com.jho5245.cucumbery.util.no_groups;

import com.jho5245.cucumbery.Cucumbery;
import io.lumine.mythic.api.config.MythicConfig;
import io.lumine.mythic.api.mobs.MythicMob;
import io.lumine.mythic.core.mobs.ActiveMob;
import org.bukkit.entity.Entity;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * {@link io.lumine.mythic.api.MythicPlugin} 플러그인 관련 유틸 제공
 */
public class MythicMobManager
{
  /**
   * 개체에게 큐컴버리 태그가 있는지 확인합니다
   * @param entity 확인할 개체
   * @param tag 확인할 태그
   * @return 해당 개체가 태그를 가지고 있으면 true, 이외에는 false
   */
  public static boolean hasTag(@NotNull Entity entity, @NotNull String tag)
  {
    return getTags(entity).contains(tag);
  }

  /**
   * 해당 개체의 모든 태그를 참조합니다. 해당 리스트는 복사된 값입니다
   * @param entity 태그를 참조할 개체
   * @return 해당 개체의 태그 리스트 또는 빈 리스트
   */
  @NotNull
  public static List<String> getTags(@NotNull Entity entity)
  {
    List<String> list = new ArrayList<>();
    if (!Cucumbery.using_MythicMobs)
    {
      return list;
    }
    if (!Cucumbery.bukkitAPIHelper.isMythicMob(entity))
    {
      return list;
    }
    ActiveMob activeMob = Cucumbery.bukkitAPIHelper.getMythicMobInstance(entity);
    if (activeMob == null)
    {
      return list;
    }
    MythicMob mythicMob = Cucumbery.bukkitAPIHelper.getMythicMob(activeMob.getMobType());
    if (mythicMob == null)
    {
      return list;
    }
    MythicConfig config = mythicMob.getConfig();
    List<String> tags = config.getStringList("Options.CucumberyTags");
    if  (tags != null)
    {
      return tags;
    }
    return list;
  }
}
