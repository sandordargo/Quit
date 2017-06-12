package com.dargo.quit;

public interface Habits {
  Iterable<Habit> iterate();
  Habit add(String name);
}
