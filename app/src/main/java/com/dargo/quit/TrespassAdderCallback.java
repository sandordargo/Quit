package com.dargo.quit;


import java.util.Date;

public interface TrespassAdderCallback {
    void callTimePicker(long id, Date date);
    void updateTrespassDate(long trespassId, Date newDate);
}

