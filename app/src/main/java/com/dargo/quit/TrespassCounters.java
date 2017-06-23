package com.dargo.quit;


public interface TrespassCounters {
    Iterable<TrespassCounter> trespassesPerDayFor(Habit habit);
}
