package com.dargo.quit.trespass_counters;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import com.dargo.quit.R;
import com.dargo.quit.habits.ConstSQLiteHabits;
import com.dargo.quit.habits.Habit;
import com.dargo.quit.utils.HomeButtonHider;
import com.dargo.quit.utils.OptionsItemSelector;

import java.util.ArrayList;
import java.util.List;

public class TrespassesByDayListActivity extends AppCompatActivity {

  ListView listView;
  TrespassesByDayListAdapter listAdapter;
  Habit defaultHabit;
  private OptionsItemSelector optionsSelector;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_trespasses_by_day_list);
    this.defaultHabit = new ConstSQLiteHabits(getBaseContext()).getDefaultHabit();
    this.optionsSelector = new OptionsItemSelector(this);
    HomeButtonHider.hide(getSupportActionBar());
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
    startActivity(optionsSelector.select(item));
    return true;
  }

  @Override
  protected void onResume() {
    super.onResume();
    cleanListView();
    populateListView();
  }
}
