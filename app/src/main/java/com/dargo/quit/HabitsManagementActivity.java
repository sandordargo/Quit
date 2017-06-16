package com.dargo.quit;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;


public class HabitsManagementActivity extends AppCompatActivity implements ListAdapterCallback {
    private ListView listView;
    private HabitsListAdapter habitsListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_habits_management);
        populateListView();
    }

    @Override
    public void populateListView() {
        listView = (ListView) findViewById(R.id.habits_listview);
        List<Habit> values = new ArrayList<>();
        for (Habit habit : new SQLiteHabits(getBaseContext()).iterate()) {
            values.add(habit);
        }
        habitsListAdapter = new HabitsListAdapter(this, values);
        habitsListAdapter.setCallback(this);
        listView.setAdapter(habitsListAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return true;
    }
}
