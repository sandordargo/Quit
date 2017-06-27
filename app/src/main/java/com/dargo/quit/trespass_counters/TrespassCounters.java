package com.dargo.quit.trespass_counters;


import com.dargo.quit.habits.Habit;

public interface TrespassCounters {
    Iterable<TrespassCounter> trespassesPerDayFor(Habit habit);
}
