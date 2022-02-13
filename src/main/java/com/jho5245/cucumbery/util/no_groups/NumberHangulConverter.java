package com.jho5245.cucumbery.util.no_groups;

import org.jetbrains.annotations.NotNull;

public class NumberHangulConverter
{
  private static final String SMALL_UNITS = "-십백천";
  private static final String LARGE_UNITS = "-만억조경";
  private static final String NUMBERS = "영일이삼사오육칠팔구";

  /**
   * 숫자를 한글로 변환합니다.
   * @param input 변환할 숫자
   * @return 한글로 변환된 숫자
   */
  @NotNull
  public static String convert(long input)
  {
    int radix = 0;
    StringBuffer[] sbList = new StringBuffer[5];
    for (int i = 0; i < 5; i ++)
    {
      sbList[i] = new StringBuffer();
    }

    outer:
    for (int i = 0 ; i < 5; i++)
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
    else if (!sbList[1].toString().equals(""))
    {
      result.insert(0, sbList[1] + "만");
    }
    for (int i = 2; i < 5; i++)
    {
      if (!sbList[i].toString().equals(""))
      {
        result.insert(0, sbList[i].toString() + LARGE_UNITS.charAt(i));
      }
    }
    return result.toString();
  }

  /**
   * 한글을 숫자로 변환합니다. 올바르지 않은 한글일 경우, {@link NumberFormatException} 예외를 발생시킵니다.
   * @param input 숫자로 변환할 한글
   * @return 숫자로 변환된 한글
   */
  public static long convert(@NotNull String input)
  {
    return -1;
  }
}
