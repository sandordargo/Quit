package com.dargo.quit;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;


public class HabitsListAdapter extends ArrayAdapter {
    private final Activity context;
    private final List<Habit> habits;
    private ListAdapterCallback callback;
    private int selectedIndex = -1;

    public HabitsListAdapter(Activity context, List<Habit> habits) {
        super(context, R.layout.trespass_listview_row, habits);
        this.context = context;
        this.habits = habits;
        //TODO habitsGetDefault...
        for (int i = 0; i < habits.size(); i++) {
            if (habits.get(i).isDefault()) {
                selectedIndex = i;
                break;
            }
        }
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

        RadioButton radioButton = (RadioButton) rowView.findViewById(R.id.radio1);
        radioButton.setChecked(selectedIndex == position);
        radioButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                notifyDataSetChanged();
                selectedIndex = position;
                habits.get(selectedIndex).makeDefault();
            }
        });
        return rowView;
    }

    public void setCallback(ListAdapterCallback callback) {
        this.callback = callback;
    }
}
