package com.dargo.quit;

import java.util.Date;

public interface Trespasses {
    Iterable<Trespass> iterate();
    Trespass add(Date date);
}
