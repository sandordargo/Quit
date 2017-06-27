package com.dargo.quit.habits;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;

import com.dargo.quit.utils.ListAdapterCallback;
import com.dargo.quit.R;

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
        setUpTextView(position, rowView);
        setUpDeleteButton(position, rowView);
        setUpEditButton(position, rowView);
        setUpRadioButton(position, rowView);
        return rowView;
    }

    private void setUpTextView(int position, View rowView) {
        TextView habitName = (TextView) rowView.findViewById(R.id.habitNameField);
        habitName.setText(habits.get(position).getName());
    }

    private void setUpEditButton(final int position, View rowView) {
        Button editButton = (Button) rowView.findViewById(R.id.editHabitButton);
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditHabitDialogFragment fragment = EditHabitDialogFragment.make(
                        habits.get(position).getId(), habits.get(position).getName());
                fragment.show(context.getFragmentManager(), "Edit habit name");
            }
        });
    }

    private void setUpDeleteButton(final int position, View rowView) {
        Button deleteButton = (Button) rowView.findViewById(R.id.deleteHabitButton);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new ConstSQLiteHabits(context).delete(habits.get(position));
                callback.populateListView();
            }
        });
    }

    private void setUpRadioButton(final int position, View rowView) {
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
    }

    public void setCallback(ListAdapterCallback callback) {
        this.callback = callback;
    }
}
