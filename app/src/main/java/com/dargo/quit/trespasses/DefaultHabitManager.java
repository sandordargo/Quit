package com.dargo.quit.trespasses;


import android.app.Activity;
import android.content.Context;
import android.widget.TextView;
import android.widget.Toast;

import com.dargo.quit.habits.AddHabitDialogFragment;
import com.dargo.quit.habits.ConstSQLiteHabits;
import com.dargo.quit.habits.Habit;

public class DefaultHabitManager {
    private Habit defaultHabit;
    private final Context context;
    private final Activity activity;
    private int toolbarId;
    private boolean habitIsBeingRead;

    DefaultHabitManager(Context context, Activity activity, int toolbarId) {
        this.context = context;
        this.activity = activity;
        this.toolbarId = toolbarId;
        this.habitIsBeingRead = false;
    }

    void setDefaultHabit() {
        boolean isThereADefaultHabit = false;
        defaultHabit = new ConstSQLiteHabits(context).getDefaultHabit();
        if (defaultHabit == null && !habitIsBeingRead) {
            habitIsBeingRead = true;
            new AddHabitDialogFragment().show(activity.getFragmentManager(), "AddHabitDialogFragment");
            makeFirstHabitDefault();
            return;
        } else {
            isThereADefaultHabit = true;
        }

        if (!isThereADefaultHabit) {
            makeFirstHabitDefault();
        }
        updateTitle();
    }

    private void makeFirstHabitDefault() {
        Iterable<Habit> habits = new ConstSQLiteHabits(context).iterate();
        if (habits.iterator().hasNext()) {
            Habit habit = habits.iterator().next();
            habit.makeDefault();
            this.defaultHabit = habit;
        }
    }

    private void updateTitle() {
        if (defaultHabit != null) {
            TextView toolbarText = (TextView) activity.findViewById(this.toolbarId);
            toolbarText.setText(defaultHabit.getName());
        }
    }

    public void onUserAddsNewHabit(String newHabit) {
        Habit habit = new ConstSQLiteHabits(context).add(newHabit);
        Toast.makeText(context, "Added new habit: " + habit.getName() + ".", Toast.LENGTH_LONG).show();
        habitIsBeingRead = false;
        if (defaultHabit == null) {
            this.defaultHabit = habit;
            updateTitle();
        }
    }

    Habit getDefaultHabit() {
        return this.defaultHabit;
    }
}
