package com.dargo.quit.trespasses;


import java.util.Date;

public interface Trespass {
    long getId();
    Date getDate();

    String getFormattedDate();
    void updateDate(Date newDate);
}
