package com.dargo.quit.trespasses;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.dargo.quit.utils.ListAdapterCallback;
import com.dargo.quit.utils.QuitSqliteDBHelper;
import com.dargo.quit.R;
import com.dargo.quit.trespass_counters.TrespassesByDayListActivity;
import com.dargo.quit.habits.AddHabitDialogFragment;
import com.dargo.quit.habits.ConstSQLiteHabits;
import com.dargo.quit.habits.Habit;
import com.dargo.quit.habits.HabitsManagementActivity;
import com.dargo.quit.trespass_counters.ConstSQLiteTrespassCounters;
import com.dargo.quit.trespass_counters.TrespassCounter;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.ValueDependentColor;
import com.jjoe64.graphview.helper.DateAsXAxisLabelFormatter;
import com.jjoe64.graphview.series.BarGraphSeries;
import com.jjoe64.graphview.series.DataPoint;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ActivityOverview extends AppCompatActivity implements ListAdapterCallback {

  ListView listView;
  TrespassListAdapter trespassListAdapter;
  Habit defaultHabit;
  boolean habitIsBeingRead;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_overview);
    habitIsBeingRead = false;
    setDefaultHabit();
    Toolbar toolbar = (Toolbar) findViewById(R.id.overview_toolbar);
    setSupportActionBar(toolbar);

    FloatingActionButton addNewHabitButton = (FloatingActionButton) findViewById(R.id.floatingActionButtonOverviewActivity);
    addNewHabitButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        AddHabitDialogFragment fragment = AddHabitDialogFragment.make("OVERVIEW");
        fragment.show(getFragmentManager(), "AddHabitDialogFragment");
      }
    });

    FloatingActionButton addNewTrespassButton = (FloatingActionButton) findViewById(R.id.addTrespassButtonOverviewActivity);
    addNewTrespassButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        new ConstSQLiteTrespasses(getBaseContext()).add(defaultHabit);
        populateListView();
      }
    });
    populateListView();

    List<TrespassCounter> values = new ArrayList<>();
    for (TrespassCounter trespass :
            new ConstSQLiteTrespassCounters(getBaseContext()).trespassesPerDayFor(defaultHabit)) {
      values.add(trespass);
    }

    GraphView graph = (GraphView) findViewById(R.id.trespassesGraph);

    int size = values.size();
    DataPoint[] dataPoints = new DataPoint[size];
    for (int i = 0 ; i<size; i++) {
      DataPoint v = new DataPoint(values.get(i).getDateOfDay().getTime(), values.get(i).getTrespassesPerDay());
      dataPoints[i] = v;
    }
    BarGraphSeries<DataPoint> series1 = new BarGraphSeries<>(dataPoints);


    graph.addSeries(series1);
    series1.setValueDependentColor(new ValueDependentColor<DataPoint>() {
      @Override
      public int get(DataPoint data) {
        return Color.rgb((int) data.getX()*255/4, (int) Math.abs(data.getY()*255/6), 100);
      }
    });


    series1.setSpacing(50);
    series1.setDrawValuesOnTop(true);
    //graph.getViewport().setMaxY(series1.getHighestValueY()+5);
    //graph.getViewport().setYAxisBoundsManual(true);
    
    graph.getGridLabelRenderer().setLabelFormatter(new DateAsXAxisLabelFormatter(getBaseContext()));

    Date first = new Date((long) series1.getHighestValueX());
    Date last = new Date((long) series1.getLowestValueX());
    Toast.makeText(getBaseContext(), String.valueOf(first), Toast.LENGTH_LONG).show();
    graph.getViewport().setMinX(first.getTime() - 24*60*60*1000);
    graph.getViewport().setMaxX(last.getTime() + 24*60*60*1000);
    graph.getViewport().setXAxisBoundsManual(true);

    graph.getGridLabelRenderer().setHumanRounding(false);
  }

  private void setDefaultHabit() {
    boolean isThereADefaultHabit = false;
    defaultHabit = new ConstSQLiteHabits(getBaseContext()).getDefaultHabit();
    if (defaultHabit == null && !habitIsBeingRead) {
      habitIsBeingRead = true;
      new AddHabitDialogFragment().show(getFragmentManager(), "AddHabitDialogFragment");
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
    Iterable<Habit> habits = new ConstSQLiteHabits(getBaseContext()).iterate();
    if (habits.iterator().hasNext()) {
      Habit habit = habits.iterator().next();
      habit.makeDefault();
      this.defaultHabit = habit;
      return;
    }
  }

  public void populateListView() {
    listView = (ListView) findViewById(R.id.overview_listview);
    List<Trespass> values = new ArrayList<>();
    for (Trespass trespass : new ConstSQLiteTrespasses(getBaseContext()).iterate(defaultHabit)) {
      values.add(trespass);
    }
    trespassListAdapter = new TrespassListAdapter(this, values, this);
    listView.setAdapter(trespassListAdapter);
  }

  public void cleanListView() {
    listView = (ListView) findViewById(R.id.overview_listview);
    List<Trespass> emptyList = new ArrayList<>();
    trespassListAdapter = new TrespassListAdapter(this, emptyList, this);
    listView.setAdapter(trespassListAdapter);
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    // Inflate the menu; this adds items to the action bar if it is present.
    getMenuInflater().inflate(R.menu.menu_main, menu);
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()) {
      case R.id.list_trespasses_for_one_habit:
        startActivity(new Intent(this, TrespassListActivity.class));
        return true;
      case R.id.list_habits:
        startActivity(new Intent(this, HabitsManagementActivity.class));
        return true;
      case R.id.list_trespasses_by_day_menu_item:
        startActivity(new Intent(this, TrespassesByDayListActivity.class));
        return true;
      default:
        return super.onOptionsItemSelected(item);
    }
  }

  public void onUserAddsNewHabit(String newHabit) {
    Habit habit = new ConstSQLiteHabits(getBaseContext()).add(newHabit);
    Toast.makeText(getBaseContext(), "Added new habit: " + habit.getName() + ".", Toast.LENGTH_LONG).show();
    habitIsBeingRead = false;
    if (defaultHabit == null) {
      this.defaultHabit = habit;
      updateTitle();
    }
  }

  @Override
  protected void onResume() {
    super.onResume();
    setDefaultHabit();
    cleanListView();
    populateListView();
  }

  private void updateTitle() {
    if (defaultHabit != null) {
      TextView toolbarText = (TextView) findViewById(R.id.defaultHabitOverviewToolbar);
      toolbarText.setText(defaultHabit.getName());
    }
  }

  public void callTimePicker(long id, Date date) {
    EditTrespassTimeDialogFragment fragment = EditTrespassTimeDialogFragment.make(id, date, "OVERVIEW");
    fragment.show(getFragmentManager(), "Edit trespass date");
  }

  public void updateTrespassDate(long trespassId, Date newDate) {
    Trespass trespass = new SQLiteTrespass(
            new QuitSqliteDBHelper(getBaseContext()).getWritableDatabase(), trespassId);
    trespass.updateDate(newDate);
    populateListView();
  }
}
