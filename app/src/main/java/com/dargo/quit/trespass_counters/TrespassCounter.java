package com.dargo.quit.trespass_counters;


import java.util.Date;

public class TrespassCounter {
    final private Date dateOfDay;
    final private int trespassesPerDay;

    TrespassCounter(Date dateOfDay, int trespassesPerDay) {
        this.dateOfDay = dateOfDay;
        this.trespassesPerDay = trespassesPerDay;
    }

    public Date getDateOfDay() {
        return dateOfDay;
    }

    public int getTrespassesPerDay() {
        return trespassesPerDay;
    }
}
