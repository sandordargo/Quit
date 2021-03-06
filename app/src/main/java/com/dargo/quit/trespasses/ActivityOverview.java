package com.dargo.quit.trespasses;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.dargo.quit.R;
import com.dargo.quit.habits.AddHabitDialogFragment;
import com.dargo.quit.habits.Habit;
import com.dargo.quit.utils.HomeButtonHider;
import com.dargo.quit.utils.ListAdapterCallback;
import com.dargo.quit.utils.OptionsItemSelector;
import com.dargo.quit.utils.QuitSqliteDBHelper;
import com.jjoe64.graphview.GraphView;

import java.util.Date;

public class ActivityOverview extends AppCompatActivity implements ListAdapterCallback {

  private TrespassListHandler trespassListHandler;
  private OptionsItemSelector optionsSelector;
  private DefaultHabitManager defaultHabitManager;
  private GraphViewDisplayer graphDisplayer;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_overview);
    this.defaultHabitManager = new DefaultHabitManager(this, this, R.id.defaultHabitOverviewToolbar);
    this.defaultHabitManager.setDefaultHabit();
    this.trespassListHandler = new TrespassListHandler(R.id.overview_listview, this, this, this);
    this.optionsSelector = new OptionsItemSelector(this);
    this.graphDisplayer = new GraphViewDisplayer((GraphView) findViewById(R.id.trespassesGraph), getBaseContext());
    setSupportActionBar((Toolbar) findViewById(R.id.overview_toolbar));
    setupAddHabitButton();
    setupNewTrespassButton();
    HomeButtonHider.hide(getSupportActionBar());
    refreshDisplay();
  }

  private void refreshGraphView() {
    this.graphDisplayer.display(getDefaultabit());
  }

  private void setupNewTrespassButton() {
    FloatingActionButton addNewTrespassButton = (FloatingActionButton) findViewById(R.id.addTrespassButtonOverviewActivity);
    addNewTrespassButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        new ConstSQLiteTrespasses(getBaseContext()).add(getDefaultabit());
        refreshDisplay();
        refreshGraphView();
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

  public void refreshDisplay() {
    trespassListHandler.populateListView(getDefaultabit());
    refreshGraphView();
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
    refreshDisplay();
    refreshGraphView();
  }

  public void callTimePicker(long id, Date date) {
    EditTrespassTimeDialogFragment fragment = EditTrespassTimeDialogFragment.make(id, date, "OVERVIEW");
    fragment.show(getFragmentManager(), "Edit trespass date");
  }

  public void updateTrespassDate(long trespassId, Date newDate) {
    Trespass trespass = new SQLiteTrespass(
            new QuitSqliteDBHelper(getBaseContext()).getWritableDatabase(), trespassId);
    trespass.updateDate(newDate);
    refreshDisplay();
    refreshGraphView();
  }

  public Habit getDefaultabit() {
    return this.defaultHabitManager.getDefaultHabit();
  }
}
