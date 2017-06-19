package com.dargo.quit;

public interface Habit {
  long getId();
  String getName();
  boolean isDefault();
  void makeDefault();
  void updateName(String newName);
}
