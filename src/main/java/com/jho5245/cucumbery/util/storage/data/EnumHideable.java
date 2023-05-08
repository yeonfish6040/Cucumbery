package com.jho5245.cucumbery.util.storage.data;

/**
 * Some enum that has hideable elements from {@link org.bukkit.command.TabCompleter} or {@link com.destroystokyo.paper.event.server.AsyncTabCompleteEvent}
 */
public interface EnumHideable
{
  /**
   * @return 이 <code>enum</code>은 TabComplete 할 때 목록에 표시되지 않습니다. 하지만 여전히 명령어로 사용할 수는 있습니다.
   */
  boolean isHiddenEnum();
}
