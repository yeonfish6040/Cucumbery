package com.jho5245.cucumbery.deathmessages;

import org.bukkit.event.entity.EntityDeathEvent;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class Condition
{
  private final ConditionType conditionType;
  private final String value;

  public Condition(ConditionType conditionType, String value)
  {
    this.conditionType = conditionType;
    this.value = value;
  }

  public ConditionType getConditionType()
  {
    return conditionType;
  }

  public String getValue()
  {
    return value;
  }

  public static void changeDeathMessages(EntityDeathEvent event, String key, @NotNull List<String> original)
  {
    for (CustomDeathMessage customDeathMessage : CustomDeathMessage.customMessages)
    {
      Condition[] conditions = customDeathMessage.getConditions();
      if (ConditionChecker.check(event, key, conditions))
      {
        original.clear();
        original.addAll(customDeathMessage.getKeys());
      }
    }
  }
}
