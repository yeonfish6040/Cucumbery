package com.jho5245.cucumbery.util.itemeditor;

import net.md_5.bungee.api.ChatColor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class ItemEditor
{
  @NotNull
  public static String create(@NotNull String text)
  {
    List<JsonComponent> components = parse(text);
    if (components.size() == 1)
      return components.get(0).toString();
    StringBuilder stringBuilder = new StringBuilder("[\"\",");
    for (JsonComponent component : components)
    {
      stringBuilder.append(component.toString()).append(",");
    }
    String string = stringBuilder.toString();
    string = string.substring(0, string.length() - 1);
    string += "]";
    return string;
  }

  @NotNull
  public static String createTranslate(@NotNull String key, @Nullable List<JsonComponent> arguments)
  {
    JsonComponent component = parse(key).get(0);
    JsonTranslatableComponent translatableComponent = new JsonTranslatableComponent(key, arguments);
    return translatableComponent.toString();
  }

  @NotNull
  private static JsonTranslatableComponent parseTranslate(@NotNull String text, @Nullable List<JsonComponent> arguments)
  {
    StringBuilder builder = new StringBuilder();
    JsonTranslatableComponent component = new JsonTranslatableComponent(text, arguments);


    for (int i = 0; i < text.length(); i++)
    {
      char c = text.charAt(i);
      JsonTranslatableComponent old;
      if (c == '§')
      {
        ++i;

        // 문자열의 맨 마지막의 문자가 §일 경우
        if (i >= text.length())
        {
          break;
        }

        // 만약 §일 경우에 그 다음 문자를 가져옴 (컬러코드 확인을 위함)
        c = text.charAt(i);

        // §A~§Z일 경우 소문자로 변환 §a~§z
        if (c >= 'A' && c <= 'Z')
        {
          c = (char) (c + 32);
        }
        ChatColor format;
        // 만약 §x 일 경우 (헥스 컬러 접두사)
        if (c == 'x' && i + 12 < text.length())
        {
          StringBuilder hex = new StringBuilder("#");
          for (int j = 0; j < 6; ++j)
          {
            hex.append(text.charAt(i + 2 + j * 2));
          }

          try
          {
            // 올바른 헥스컬러일 경우 헥스값 적용
            format = ChatColor.of(hex.toString());
          }
          catch (Exception e)
          {
            // 아닐 경우 빠꾸
            format = null;
          }

          i += 12; // 어쨌든 12글자가 헥스컬러만큼 쓰였으므로 12 더함
        }
        else // § 다음에 오는 문자가 x가 아닐 경우
        {
          format = ChatColor.getByChar(c);
        }
        // 컬러 코드가 유효하면
        if (format != null)
        {
          if (builder.length() > 0)
          {
            old = component;
            JsonProperty property = component.jsonProperty;
            old.text = builder.toString();
            return old;
          }
          JsonProperty property = component.jsonProperty;
          if (format == ChatColor.BOLD)
          {
            property.bold = true;
          }
          else if (format == ChatColor.ITALIC)
          {
            property.italic = true;
          }
          else if (format == ChatColor.UNDERLINE)
          {
            property.underlined = true;
          }
          else if (format == ChatColor.STRIKETHROUGH)
          {
            property.strikethrough = true;
          }
          else if (format == ChatColor.MAGIC)
          {
            property.obfuscated = true;
          }
          else if (format == ChatColor.RESET)
          {
//            component = new JsonComponent();
          }
          else
          {
//            component = new JsonComponent();
            property = component.jsonProperty;
            property.color = format.getName();
          }
        }
      }
      else // 문자가 §가 아닐 경우
      {
        builder.append(c);
      }
    }

    component.text = builder.toString();
//    list.add(component);
    return null;
  }

  @NotNull
  private static List<JsonComponent> parse(@NotNull String text)
  {
    List<JsonComponent> list = new ArrayList<>();
    StringBuilder builder = new StringBuilder();
    JsonComponent component = new JsonComponent();

    for (int i = 0; i < text.length(); i++)
    {
      char c = text.charAt(i);
      JsonComponent old;
      if (c == '§')
      {
        ++i;

        // 문자열의 맨 마지막의 문자가 §일 경우
        if (i >= text.length())
        {
          break;
        }

        // 만약 §일 경우에 그 다음 문자를 가져옴 (컬러코드 확인을 위함)
        c = text.charAt(i);

        // §A~§Z일 경우 소문자로 변환 §a~§z
        if (c >= 'A' && c <= 'Z')
        {
          c = (char) (c + 32);
        }
        ChatColor format;
        // 만약 §x 일 경우 (헥스 컬러 접두사)
        if (c == 'x' && i + 12 < text.length())
        {
          StringBuilder hex = new StringBuilder("#");
          for (int j = 0; j < 6; ++j)
          {
            hex.append(text.charAt(i + 2 + j * 2));
          }

          try
          {
            // 올바른 헥스컬러일 경우 헥스값 적용
            format = ChatColor.of(hex.toString());
          }
          catch (Exception e)
          {
            // 아닐 경우 빠꾸
            format = null;
          }

          i += 12; // 어쨌든 12글자가 헥스컬러만큼 쓰였으므로 12 더함
        }
        else // § 다음에 오는 문자가 x가 아닐 경우
        {
          format = ChatColor.getByChar(c);
        }
        // 컬러 코드가 유효하면
        if (format != null)
        {
          if (builder.length() > 0)
          {
            old = component;
            JsonProperty property = component.jsonProperty;
            component = new JsonComponent(component.text, property.clone());
            old.text = builder.toString();
            builder = new StringBuilder();
            list.add(old);
          }
          JsonProperty property = component.jsonProperty;
          if (format == ChatColor.BOLD)
          {
            property.bold = true;
          }
          else if (format == ChatColor.ITALIC)
          {
            property.italic = true;
          }
          else if (format == ChatColor.UNDERLINE)
          {
            property.underlined = true;
          }
          else if (format == ChatColor.STRIKETHROUGH)
          {
            property.strikethrough = true;
          }
          else if (format == ChatColor.MAGIC)
          {
            property.obfuscated = true;
          }
          else if (format == ChatColor.RESET)
          {
            component = new JsonComponent();
          }
          else
          {
            component = new JsonComponent();
            property = component.jsonProperty;
            property.color = format.getName();
          }
        }
      }
      else // 문자가 §가 아닐 경우
      {
        builder.append(c);
      }
    }

    component.text = builder.toString();
    list.add(component);
    return list;
  }
}
