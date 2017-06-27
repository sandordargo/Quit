package com.dargo.quit.habits;

public interface Habits {
  Iterable<Habit> iterate();
  Habit add(String name);
  boolean delete(Habit habit);
  Habit getDefaultHabit();
}
