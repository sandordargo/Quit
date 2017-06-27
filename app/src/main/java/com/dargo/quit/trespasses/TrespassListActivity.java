package com.dargo.quit.trespasses;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.dargo.quit.R;
import com.dargo.quit.habits.AddHabitDialogFragment;
import com.dargo.quit.habits.Habit;
import com.dargo.quit.utils.ListAdapterCallback;
import com.dargo.quit.utils.OptionsItemSelector;
import com.dargo.quit.utils.QuitSqliteDBHelper;

import java.util.Date;

public class TrespassListActivity extends AppCompatActivity implements ListAdapterCallback {

  TrespassListHandler trespassListHandler;
  private OptionsItemSelector optionsSelector;
  DefaultHabitManager defaultHabitManager;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    this.optionsSelector = new OptionsItemSelector(this);
    this.defaultHabitManager = new DefaultHabitManager(this, this, R.id.defaultHabitToolbar);
    this.defaultHabitManager.setDefaultHabit();
    this.trespassListHandler = new TrespassListHandler(R.id.listview, this, this, this);
    setupAddHabitButton();
    setupNewTrespassButton();
    populateListView();
  }

  private void setupAddHabitButton() {
    FloatingActionButton addNewHabitButton = (FloatingActionButton) findViewById(R.id.floatingActionButton);
    addNewHabitButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        AddHabitDialogFragment fragment = AddHabitDialogFragment.make("TRESPASS_LIST");
        fragment.show(getFragmentManager(), "AddHabitDialogFragment");
      }
    });
  }

  private void setupNewTrespassButton() {
    FloatingActionButton addNewTrespassButton = (FloatingActionButton) findViewById(R.id.addTrespassButton);
    addNewTrespassButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        new ConstSQLiteTrespasses(getBaseContext()).add(getDefaultHabit());
        populateListView();
      }
    });
  }

  public void populateListView() {
    trespassListHandler.populateListView(getDefaultHabit());
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
    EditTrespassTimeDialogFragment fragment = EditTrespassTimeDialogFragment.make(id, date, "TRESPASS_LIST");
    fragment.show(getFragmentManager(), "Edit trespass date");
  }

  public void updateTrespassDate(long trespassId, Date newDate) {
    Trespass trespass = new SQLiteTrespass(
            new QuitSqliteDBHelper(getBaseContext()).getWritableDatabase(), trespassId);
    trespass.updateDate(newDate);
    populateListView();
  }

  private Habit getDefaultHabit() {
    return  this.defaultHabitManager.getDefaultHabit();
  }
}
