package com.dargo.quit;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class TrespassesByDayListActivity extends AppCompatActivity {

  ListView listView;
  TrespassesByDayListAdapter listAdapter;
  Habit defaultHabit;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_trespasses_by_day_list);
    setDefaultHabit();
    populateListView();
  }

  private void setDefaultHabit() {
    Iterable<Habit> habits = new ConstSQLiteHabits(getBaseContext()).iterate();
    if (habits.iterator().hasNext()) {
      Habit habit = habits.iterator().next();
      habit.makeDefault();
      this.defaultHabit = habit;
    }
  }

  public void populateListView() {
    listView = (ListView) findViewById(R.id.trespasses_by_day_listview);
    List<TrespassCounter> values = new ArrayList<>();
    for (TrespassCounter trespass :
            new ConstSQLiteTrespassCounters(getBaseContext()).trespassesPerDayFor(defaultHabit)) {
      values.add(trespass);
    }
    listAdapter = new TrespassesByDayListAdapter(this, values);
    listView.setAdapter(listAdapter);
  }

  public void cleanListView() {
    listView = (ListView) findViewById(R.id.trespasses_by_day_listview);
    List<TrespassCounter> emptyList = new ArrayList<>();
    listAdapter = new TrespassesByDayListAdapter(this, emptyList);
    listView.setAdapter(listAdapter);
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.menu_main, menu);
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()) {
      case R.id.list_habits:
        Intent intent = new Intent(this, HabitsManagementActivity.class);
        startActivity(intent);
        return true;
      default:
        return super.onOptionsItemSelected(item);
    }
  }

  @Override
  protected void onResume() {
    super.onResume();
    cleanListView();
    populateListView();
  }
}
