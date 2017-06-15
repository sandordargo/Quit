package com.dargo.quit;

public interface Trespasses {
    Iterable<Trespass> iterate();
    Trespass add(Habit habit);

    boolean delete(Trespass trespass);
}
