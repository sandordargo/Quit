package com.dargo.quit;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;


public class HabitsListAdapter extends ArrayAdapter {
    private final Activity context;
    private final List<Habit> habits;
    private ListAdapterCallback callback;


    public HabitsListAdapter(Activity context, List<Habit> habits) {
        super(context, R.layout.trespass_listview_row, habits);
        this.context = context;
        this.habits = habits;
    }

    public View getView(final int position, View view, final ViewGroup parent) {
        View rowView = context.getLayoutInflater().inflate(R.layout.habit_listview_row, null, true);
        TextView habitName = (TextView) rowView.findViewById(R.id.habitNameField);
        habitName.setText(habits.get(position).getName());

        Button deleteButton = (Button) rowView.findViewById(R.id.deleteHabitButton);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new SQLiteHabits(context).delete(habits.get(position));
                callback.populateListView();
            }
        });

        return rowView;
    }

    public void setCallback(ListAdapterCallback callback) {
        this.callback = callback;
    }
}
