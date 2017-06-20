package com.dargo.quit;


public class ConstHabit implements Habit {
    private final Habit origin;
    private final String name;
    private final boolean isDefault;

    public ConstHabit(Habit origin, String name, boolean isDefault) {
        this.origin = origin;
        this.name = name;
        this.isDefault = isDefault;
    }

    @Override
    public long getId() {
        return origin.getId();
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public boolean isDefault() {
        return this.isDefault;
    }

    @Override
    public void makeDefault() {
        this.origin.makeDefault();
    }

    @Override
    public void updateName(String newName) {
        this.origin.updateName(newName);
    }
}
