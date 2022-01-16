package com.jho5245.cucumbery.util.josautil;

/**
 * Created by Bae Yong-Ju on 2017-05-30.
 */
public class Pair<FIRST, SECOND>
{

  public FIRST first;
  public SECOND second;

  public Pair(FIRST first, SECOND second)
  {
    this.first = first;
    this.second = second;
  }

  @Override
  public int hashCode()
  {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((first == null) ? 0 : first.hashCode());
    result = prime * result + ((second == null) ? 0 : second.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object object)
  {
    return object instanceof Pair<?, ?> pair && this.first.equals(pair.first) &&
            this.second.equals(pair.second);
  }
}
