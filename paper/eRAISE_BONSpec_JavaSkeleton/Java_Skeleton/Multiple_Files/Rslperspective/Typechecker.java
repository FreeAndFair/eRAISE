

public /*@ nullable_by_default @*/ class Typechecker  {

  public /*@ pure @*/ Signature getSignature(/*@ non_null @*/ Entity entity){}

  public /*@ pure @*/ Set<Theorem> getTheorems(/*@ non_null @*/ Context context){}

  public /*@ pure @*/ Set<Error> typeCheck(Entity entity, /*@ non_null @*/ Context context){}
}

