package com.dargo.quit;


import java.util.Date;

public interface Trespass {
    long getId();
    Date getDate();

    String getFormattedDate();
    void updateDate(Date newDate);
}
