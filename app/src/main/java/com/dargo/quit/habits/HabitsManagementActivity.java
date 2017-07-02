package com.dargo.quit.habits;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import com.dargo.quit.R;
import com.dargo.quit.utils.HomeButtonHider;
import com.dargo.quit.utils.ListAdapterCallback;
import com.dargo.quit.utils.OptionsItemSelector;
import com.dargo.quit.utils.QuitSqliteDBHelper;

import java.util.ArrayList;
import java.util.List;


public class HabitsManagementActivity extends AppCompatActivity implements ListAdapterCallback {
    private ListView listView;
    private HabitsListAdapter habitsListAdapter;
    private OptionsItemSelector optionsSelector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_habits_management);
        this.optionsSelector = new OptionsItemSelector(this);
        HomeButtonHider.hide(getSupportActionBar());
        refreshDisplay();
    }

    @Override
    public void refreshDisplay() {
        listView = (ListView) findViewById(R.id.habits_listview);
        List<Habit> values = new ArrayList<>();
        for (Habit habit : new ConstSQLiteHabits(getBaseContext()).iterate()) {
            values.add(habit);
        }
        habitsListAdapter = new HabitsListAdapter(this, values);
        habitsListAdapter.setCallback(this);
        listView.setAdapter(habitsListAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        startActivity(optionsSelector.select(item));
        return true;
    }

    public void updateHabitName(long habitId, String habitName) {
        Habit habit = new SQLiteHabit(
                new QuitSqliteDBHelper(getBaseContext()).getWritableDatabase(), habitId);
        habit.updateName(habitName);
        refreshDisplay();
    }
}
