package com.jho5245.cucumbery.deathmessages;

import com.jho5245.cucumbery.util.storage.data.Variable;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

import java.util.ArrayList;
import java.util.List;

public class CustomDeathMessage
{
  public static List<CustomDeathMessage> customMessages = new ArrayList<>();

  static
  {
    YamlConfiguration config = Variable.deathMessages;
    if (config.getBoolean("death-messages.enable"))
    {
      ConfigurationSection section = config.getConfigurationSection("death-messages.custom-messages");
      if (section != null)
      {
        for (String key : section.getKeys(false))
        {
          ConfigurationSection custom = section.getConfigurationSection(key);
          if (custom != null)
          {
            List<String> messages = custom.getStringList("messages");
            ConfigurationSection conditions = custom.getConfigurationSection("conditions");
            List<Condition> conditionList = new ArrayList<>();
            if (conditions != null)
            {
              for (String condition : conditions.getKeys(false))
              {
                ConfigurationSection conditionSection = conditions.getConfigurationSection(condition);
                if (conditionSection != null)
                {
                  String k = conditionSection.getString("key");
                  String v = conditionSection.getString("value");
                  try
                  {
                    if (k != null && v != null)
                    {
                      conditionList.add(new Condition(ConditionType.valueOf(k.toUpperCase()), v));
                    }
                  }
                  catch (Exception e)
                  {
                    e.printStackTrace();
                  }
                }
              }
            }
            Condition[] conditionArray = new Condition[conditionList.size()];
            customMessages.add(new CustomDeathMessage(new ArrayList<>(messages), conditionList.toArray(conditionArray)));
          }
        }
      }
    }
//    customMessages.add(new CustomDeathMessage(
//            new ArrayList<>(Arrays.asList(
//                    "%1$s이(가) 세계 밖으로 오.",
//                    "%1$s이(가) 세계 밖으로 샌즈피클."
//            )),
//            new Condition(ConditionType.DEATH_TYPE, "death.attack.outOfWorld"),
//            new Condition(ConditionType.PLAYER_NAME, "(.*)5245")
//    ));
//
//    customMessages.add(new CustomDeathMessage(
//            new ArrayList<>(Arrays.asList(
//                    "%1$s이(가) %3$s을(를) 사용한 %2$s에게 구센 당했습니다.",
//                    "%1$s이(가) %3$s을(를) 사용한 %2$s에게 와구센 당했습니다."
//            )),
//            new Condition(ConditionType.DEATH_TYPE, "death.attack.arrow.item"),
//            new Condition(ConditionType.PLAYER_NAME, "gusen1116")
//    ));
  }

  private final Condition[] conditions;

  private final List<String> keys;

  CustomDeathMessage(List<String> keys, Condition... conditions)
  {
    this.keys = new ArrayList<>(keys);
    this.conditions = conditions;
  }

  public Condition[] getConditions()
  {
    return conditions;
  }

  public List<String> getKeys()
  {
    return new ArrayList<>(keys);
  }
}
