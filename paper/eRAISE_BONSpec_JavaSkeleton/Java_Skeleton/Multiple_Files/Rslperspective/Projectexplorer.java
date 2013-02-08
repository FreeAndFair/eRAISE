

public /*@ nullable_by_default @*/ class Projectexplorer  {

  public /*@ pure @*/ Set<File> getRSLfiles(/*@ non_null @*/ Project project){}

  public void add(/*@ non_null @*/ Directory directory, /*@ non_null @*/ File file){}

  public void addDir(/*@ non_null @*/ Project project, /*@ non_null @*/ Directory directory){}

  //@ ensures file == null;
  public void delete(File file){}

  //@ ensures dir == null;
  public void deleteDir(Dir dir){}

  //@ ensures project == null;
  public void deletePrj(Project project){}
}

