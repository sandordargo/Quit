package com.dargo.quit;


import java.text.SimpleDateFormat;
import java.util.Date;

public class ConstTrespass implements Trespass {
    private final Trespass origin;
    private final Date didAt;

    public ConstTrespass(Trespass origin, Date didAt) {
        this.origin = origin;
        this.didAt = didAt;
    }

    @Override
    public long getId() {
        return this.origin.getId();
    }

    @Override
    public Date getDate() {
        return this.didAt;
    }

    @Override
    public String getFormattedDate() {
        return new SimpleDateFormat("MM/dd/yyyy HH:mm:ss").format(getDate());
    }
}
