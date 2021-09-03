package com.jho5245.cucumbery.deathmessages;

import com.jho5245.cucumbery.util.storage.data.Variable;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

import java.util.ArrayList;
import java.util.List;

public class CustomDeathMessage
{
  public static List<CustomDeathMessage> customMessages = new ArrayList<>();

  public static void register()
  {
    customMessages.clear();
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
            customMessages.add(new CustomDeathMessage("death-messages.custom-messages." + key + ".messages", conditionList.toArray(conditionArray)));
          }
        }
      }
    }
  }

  private final Condition[] conditions;

  private final String key;

  CustomDeathMessage(String key, Condition... conditions)
  {
    this.key = key;
    this.conditions = conditions;
  }

  public Condition[] getConditions()
  {
    return conditions;
  }

  public List<String> getKeys()
  {
    return Variable.deathMessages.getStringList(key);
  }
}
