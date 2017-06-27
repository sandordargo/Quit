package com.dargo.quit.trespasses;

import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.dargo.quit.R;
import com.dargo.quit.habits.AddHabitDialogFragment;
import com.dargo.quit.habits.Habit;
import com.dargo.quit.trespass_counters.ConstSQLiteTrespassCounters;
import com.dargo.quit.trespass_counters.TrespassCounter;
import com.dargo.quit.utils.ListAdapterCallback;
import com.dargo.quit.utils.OptionsItemSelector;
import com.dargo.quit.utils.QuitSqliteDBHelper;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.ValueDependentColor;
import com.jjoe64.graphview.helper.DateAsXAxisLabelFormatter;
import com.jjoe64.graphview.series.BarGraphSeries;
import com.jjoe64.graphview.series.DataPoint;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ActivityOverview extends AppCompatActivity implements ListAdapterCallback {

  private TrespassListHandler trespassListHandler;
  private OptionsItemSelector optionsSelector;
  private DefaultHabitManager defaultHabitManager;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_overview);
    this.defaultHabitManager = new DefaultHabitManager(this, this, R.id.defaultHabitOverviewToolbar);
    this.defaultHabitManager.setDefaultHabit();
    this.trespassListHandler = new TrespassListHandler(R.id.overview_listview, this, this, this);
    this.optionsSelector = new OptionsItemSelector(this);
    setSupportActionBar((Toolbar) findViewById(R.id.overview_toolbar));
    setupAddHabitButton();
    setupNewTrespassButton();
    populateListView();
    setupGraphView();
  }

  private void setupGraphView() {
    List<DataPoint> dataPoints = new ArrayList<>();
    for (TrespassCounter trespass :
            new ConstSQLiteTrespassCounters(getBaseContext()).trespassesPerDayFor(getDefaultabit())) {
      dataPoints.add(new DataPoint(trespass.getDateOfDay().getTime(), trespass.getTrespassesPerDay()));
    }
    DataPoint[] dataPointsArray = new DataPoint[dataPoints.size()];
    dataPointsArray = dataPoints.toArray(dataPointsArray);
    BarGraphSeries<DataPoint> series1 = new BarGraphSeries<>(dataPointsArray);

    GraphView graph = (GraphView) findViewById(R.id.trespassesGraph);
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

  private void setupNewTrespassButton() {
    FloatingActionButton addNewTrespassButton = (FloatingActionButton) findViewById(R.id.addTrespassButtonOverviewActivity);
    addNewTrespassButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        new ConstSQLiteTrespasses(getBaseContext()).add(getDefaultabit());
        populateListView();
      }
    });
  }

  private void setupAddHabitButton() {
    FloatingActionButton addNewHabitButton = (FloatingActionButton) findViewById(R.id.floatingActionButtonOverviewActivity);
    addNewHabitButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        AddHabitDialogFragment fragment = AddHabitDialogFragment.make("OVERVIEW");
        fragment.show(getFragmentManager(), "AddHabitDialogFragment");
      }
    });
  }

  public void populateListView() {
    trespassListHandler.populateListView(getDefaultabit());
  }

  public void cleanListView() {
    trespassListHandler.cleanListView();
  }
  
  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    // Inflate the menu; this adds items to the action bar if it is present.
    getMenuInflater().inflate(R.menu.menu_main, menu);
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    startActivity(optionsSelector.select(item));
    return true;
  }

  public void onUserAddsNewHabit(String newHabit) {
    this.defaultHabitManager.onUserAddsNewHabit(newHabit);
  }

  @Override
  protected void onResume() {
    super.onResume();
    defaultHabitManager.setDefaultHabit();
    cleanListView();
    populateListView();
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

  public Habit getDefaultabit() {
    return this.defaultHabitManager.getDefaultHabit();
  }
}
