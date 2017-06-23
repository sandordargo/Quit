package com.dargo.quit;

public interface Trespasses {
    Iterable<Trespass> iterate();
    Iterable<Trespass> iterate(Habit habit);
    Trespass add(Habit habit);
    boolean delete(Trespass trespass);
}
