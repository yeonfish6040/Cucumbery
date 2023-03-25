package com.jho5245.cucumbery.util.no_groups;

public class TPSMeter implements Runnable
{
  public static int TICK_COUNT = 0;
  public static long[] TICKS = new long[600];
  public static long LAST_TICK = 0L;

  public static double getTPS()
  {
    return getTPS(100);
  }

  public static double getTPS(int ticks)
  {
    try
    {
      if (TICK_COUNT < ticks)
      {
        return 20.0D;
      }
      int target = (TICK_COUNT - 1 - ticks) % TICKS.length;
      long elapsed = System.currentTimeMillis() - TICKS[target];
      return ticks / (elapsed / 1000.0D);
    }
    catch (ArrayIndexOutOfBoundsException e)
    {
      return 20.0d;
    }
  }

  public static long getElapsed(int tickID)
  {
    if (TICK_COUNT - tickID >= TICKS.length)
    {
    }

    long time = TICKS[(tickID % TICKS.length)];
    return System.currentTimeMillis() - time;
  }

  public void run()
  {
    TICKS[(TICK_COUNT % TICKS.length)] = System.currentTimeMillis();

    TICK_COUNT += 1;
  }
}
