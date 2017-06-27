package com.dargo.quit.habits;

public interface Habit {
  long getId();
  String getName();
  boolean isDefault();
  void makeDefault();
  void updateName(String newName);
}
