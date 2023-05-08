package com.jho5245.cucumbery.util.itemlore;

import com.jho5245.cucumbery.Cucumbery;
import com.jho5245.cucumbery.util.nbt.CucumberyTag;
import com.jho5245.cucumbery.util.nbt.NBTAPI;
import com.jho5245.cucumbery.util.no_groups.Method;
import com.jho5245.cucumbery.util.storage.component.util.ComponentUtil;
import com.jho5245.cucumbery.util.storage.data.Constant.CucumberyHideFlag;
import de.tr7zw.changeme.nbtapi.NBTCompound;
import de.tr7zw.changeme.nbtapi.NBTList;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Calendar;
import java.util.List;

public class ItemLore2ExpireDate
{
  protected static void setItemLore(@NotNull List<Component> lore, @Nullable NBTCompound itemTag, @Nullable NBTList<String> hideFlags)
  {
    String expireDate = NBTAPI.getString(itemTag, CucumberyTag.EXPIRE_DATE_KEY);
    if (expireDate != null && !NBTAPI.arrayContainsValue(hideFlags, CucumberyHideFlag.EXPIRE_DATE))
    {
      lore.add(Component.empty());
      String prefix = "&e";
      boolean relative = expireDate.startsWith("~");
      if (relative)
      {
        prefix += "획득 후 ";
        expireDate = expireDate.replace("~", "");
      }
      Calendar calendar = Calendar.getInstance();
      calendar.add(Calendar.HOUR_OF_DAY, Cucumbery.config.getInt("adjust-time-difference-value"));
      long expireMills = Method.getTimeDifference(calendar, expireDate);
      if (!relative && expireDate.contains("시"))
      {
        try
        {
          String[] split =  expireDate.split("시")[0].split(" ");
          int hour = Integer.parseInt(split[split.length - 1]);
          if (hour == 0)
          {
            expireDate = expireDate.replace("00시", "오전 12시").replace("0시", "오전 12시");
          }
          else if (hour < 12)
          {
            expireDate = expireDate.replace(hour + "시", "오전 " + hour + "시").replace("0" + hour + "시", "오전 " + hour + "시");
          }
          else
          {
            expireDate = expireDate.replace(hour + "시", "오후 " + (hour - 12) + "시");
          }
        }
        catch (Exception ignored)
        {

        }
      }
      if (!relative && expireMills <= 20000)
      {
        lore.add(ComponentUtil.create("&e유효 기간이 만료되었습니다"));
      }
      else
      {
        lore.add(ComponentUtil.create(prefix + expireDate + (relative ? "동안" : "까지") + " 사용 가능"));
      }
    }
  }
}
