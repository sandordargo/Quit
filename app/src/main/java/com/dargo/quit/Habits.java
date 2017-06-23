package com.dargo.quit;

public interface Habits {
  Iterable<Habit> iterate();
  Habit add(String name);
  boolean delete(Habit habit);
  Habit getDefaultHabit();
}
