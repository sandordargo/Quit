package com.dargo.quit.trespasses;

import com.dargo.quit.habits.Habit;

public interface Trespasses {
    Iterable<Trespass> iterate();
    Iterable<Trespass> iterate(Habit habit);
    Trespass add(Habit habit);
    boolean delete(Trespass trespass);
}
