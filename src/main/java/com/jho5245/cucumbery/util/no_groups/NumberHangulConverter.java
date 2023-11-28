package com.jho5245.cucumbery.util.no_groups;

import com.jho5245.cucumbery.util.storage.data.Constant;
import net.kyori.adventure.text.format.TextColor;
import org.jetbrains.annotations.NotNull;

public class NumberHangulConverter
{
  private static final String SMALL_UNITS = "-십백천";
  private static final String LARGE_UNITS = "-만억조경";
  private static final String NUMBERS = "영일이삼사오육칠팔구";

  /**
   * 숫자를 한글로 변환합니다.
   *
   * @param input 변환할 숫자
   * @return 한글로 변환된 숫자
   */
  @NotNull
  public static String convert(long input)
  {
    int radix = 0;
    StringBuffer[] sbList = new StringBuffer[5];
    for (int i = 0; i < 5; i++)
    {
      sbList[i] = new StringBuffer();
    }

    outer:
    for (int i = 0; i < 5; i++)
    {
      int numTemp;
      for (int j = 0; j < 4; j++) // 일, 십, 백, 천
      {
        long temp = (long) (input * 1d / Math.pow(10, radix++));
        if (temp == 0)
        {
          break outer;
        }
        else
        {
          numTemp = (int) (temp % 10);
        }

        if (j == 0)
        {
          if (numTemp > 0)
          {
            sbList[i].insert(0, NUMBERS.charAt(numTemp));
          }
        }
        else
        {
          if (numTemp == 1)
          {
            sbList[i].insert(0, SMALL_UNITS.charAt(j));
          }
          else if (numTemp > 0)
          {
            sbList[i].insert(0, SMALL_UNITS.charAt(j));
            sbList[i].insert(0, NUMBERS.charAt(numTemp));
          }
        }
      }
    }

    StringBuilder result = new StringBuilder(sbList[0].toString());
    if (sbList[1].toString().equals("일"))
    {
      result.insert(0, "만");
    }
    else if (!sbList[1].toString().isEmpty())
    {
      result.insert(0, sbList[1] + "만");
    }
    for (int i = 2; i < 5; i++)
    {
      if (!sbList[i].toString().isEmpty())
      {
        result.insert(0, sbList[i].toString() + LARGE_UNITS.charAt(i));
      }
    }
    return result.toString();
  }

  /**
   * 한글을 숫자로 변환합니다. 올바르지 않은 한글일 경우, {@link NumberFormatException} 예외를 발생시킵니다.
   *
   * @param input 숫자로 변환할 한글
   * @return 숫자로 변환된 한글
   */
  public static long convert(@NotNull String input)
  {
    return -1;
  }

  /**
   * 숫자의 큰 자리수(만, 억, 조 단위)만 한글로 변환합니다
   *
   * @param input     변환될 숫자
   * @param withColor 색상 대입 여부
   * @return 한글로 변환된 숫자
   */
  @NotNull
  public static String convert2(double input, boolean withColor, TextColor color)
  {
    String c = withColor && color != null ? color.asHexString() + ";" : "";
    if (input < 10000d)
    {
      return c + Constant.Sosu2Floor.format(input);
    }
    else if (input < 1_0000_0000d)
    {
      double small = input % 1_0000d, big = input - small;
      String smallS = Constant.JeongsuRawFloor.format(small);
      smallS = smallS.equals("0") ? "" : smallS;
      return c + Constant.JeongsuRawFloor.format(big / 1_0000d) + (withColor ? "rg255,204;" : "") + "만" + c + smallS;
    }
    else if (input < 1_0000_0000_0000d)
    {
      double small = input % 1_0000_0000d, big = input - small;
      small /= 1_0000d;
      String smallS = Constant.JeongsuRawFloor.format(small);
      smallS = smallS.equals("0") ? "" : smallS + (withColor ? "rg255,204;" : "") + "만";
      return c + Constant.JeongsuRawFloor.format(big / 1_0000_0000d) + (withColor ? "&c" : "") + "억" + c + smallS;
    }
    else if (input < 1_0000_0000_0000_0000d)
    {
      double small = input % 1_0000_0000_0000d, big = input - small;
      small /= 1_0000_00000d;
      String smallS = Constant.JeongsuRawFloor.format(small);
      smallS = smallS.equals("0") ? "" : smallS + (withColor ? "&c" : "") + "억";
      return c + Constant.JeongsuRawFloor.format(big / 1_0000_0000_0000d) + (withColor ? "rgb255,30,30;" : "") + "조" + c + smallS;
    }
    else if (input < 1_0000_0000_0000_0000_0000d)
    {
      double small = input % 1_0000_0000_0000_0000d, big = input - small;
      small /= 1_0000_0000_0000d;
      String smallS = Constant.JeongsuRawFloor.format(small);
      smallS = smallS.equals("0") ? "" : smallS + (withColor ? "rgb255,30,30;" : "") + "조";
      return c + Constant.JeongsuRawFloor.format(big / 1_0000_0000_0000_0000d) + (withColor ? "&b" : "") + "경" + c + smallS;
    }
    else if (input < 1_0000_0000_0000_0000_0000_0000d)
    {
      double small = input % 1_0000_0000_0000_0000_0000d, big = input - small;
      small /= 1_0000_0000_0000_0000d;
      String smallS = Constant.JeongsuRawFloor.format(small);
      smallS = smallS.equals("0") ? "" : smallS + (withColor ? "&b" : "") + "경";
      return c + Constant.JeongsuRawFloor.format(big / 1_0000_0000_0000_0000_0000d) + (withColor ? "&3" : "") + "해" + c + smallS;
    }
    else if (input < 1_0000_0000_0000_0000_0000_0000_0000d)
    {
      double small = input % 1_0000_0000_0000_0000_0000_0000d, big = input - small;
      small /= 1_0000_0000_0000_0000_0000d;
      String smallS = Constant.JeongsuRawFloor.format(small);
      smallS = smallS.equals("0") ? "" : smallS + (withColor ? "&3" : "") + "해";
      return c + Constant.JeongsuRawFloor.format(big / 1_0000_0000_0000_0000_0000_0000d) + (withColor ? "&9" : "") + "자" + c + smallS;
    }
    else if (input < 1_0000_0000_0000_0000_0000_0000_0000_0000d)
    {
      double small = input % 1_0000_0000_0000_0000_0000_0000_0000d, big = input - small;
      small /= 1_0000_0000_0000_0000_0000_0000d;
      String smallS = Constant.JeongsuRawFloor.format(small);
      smallS = smallS.equals("0") ? "" : smallS + (withColor ? "&9" : "") + "자";
      return c + Constant.JeongsuRawFloor.format(big / 1_0000_0000_0000_0000_0000_0000_0000d) + (withColor ? "&a" : "") + "양" + c + smallS;
    }
    else if (input < 1_0000_0000_0000_0000_0000_0000_0000_0000_0000d)
    {
      double small = input % 1_0000_0000_0000_0000_0000_0000_0000d, big = input - small;
      small /= 1_0000_0000_0000_0000_0000_0000_0000d;
      String smallS = Constant.JeongsuRawFloor.format(small);
      smallS = smallS.equals("0") ? "" : smallS + (withColor ? "&a" : "") + "양";
      return c + Constant.JeongsuRawFloor.format(big / 1_0000_0000_0000_0000_0000_0000_0000_0000d) + (withColor ? "&2" : "") + "구" + c + smallS;
    }
    else if (input < 1_0000_0000_0000_0000_0000_0000_0000_0000_0000_0000d)
    {
      double small = input % 1_0000_0000_0000_0000_0000_0000_0000_0000d, big = input - small;
      small /= 1_0000_0000_0000_0000_0000_0000_0000_0000d;
      String smallS = Constant.JeongsuRawFloor.format(small);
      smallS = smallS.equals("0") ? "" : smallS + (withColor ? "&2" : "") + "구";

      return c + Constant.JeongsuRawFloor.format(big / 1_0000_0000_0000_0000_0000_0000_0000_0000_0000d) + (withColor ? "&1" : "") + "간" + c + smallS;
    }
    return "" + input;
  }
}
