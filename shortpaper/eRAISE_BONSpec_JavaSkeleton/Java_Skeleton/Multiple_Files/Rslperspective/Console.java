

public /*@ nullable_by_default @*/ class Console  {

  public void clear(){}

  //@ ensures channel == 1 || channel == 2;
  public void update(List<Char> message, Channel channel){}
}

