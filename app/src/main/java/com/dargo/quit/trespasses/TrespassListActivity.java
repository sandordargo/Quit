package com.dargo.quit.trespasses;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.dargo.quit.R;
import com.dargo.quit.habits.AddHabitDialogFragment;
import com.dargo.quit.habits.ConstSQLiteHabits;
import com.dargo.quit.habits.Habit;
import com.dargo.quit.habits.HabitsManagementActivity;
import com.dargo.quit.trespass_counters.TrespassesByDayListActivity;
import com.dargo.quit.utils.ListAdapterCallback;
import com.dargo.quit.utils.QuitSqliteDBHelper;

import java.util.Date;

public class TrespassListActivity extends AppCompatActivity implements ListAdapterCallback {

  Habit defaultHabit;
  boolean habitIsBeingRead;
  TrespassListHandler trespassListHandler;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    habitIsBeingRead = false;
    setDefaultHabit();
    trespassListHandler = new TrespassListHandler(R.id.listview, this, this, this);

    FloatingActionButton addNewHabitButton = (FloatingActionButton) findViewById(R.id.floatingActionButton);
    addNewHabitButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        AddHabitDialogFragment fragment = AddHabitDialogFragment.make("TRESPASS_LIST");
        fragment.show(getFragmentManager(), "AddHabitDialogFragment");
      }
    });

    FloatingActionButton addNewTrespassButton = (FloatingActionButton) findViewById(R.id.addTrespassButton);
    addNewTrespassButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        new ConstSQLiteTrespasses(getBaseContext()).add(defaultHabit);
        populateListView();
      }
    });
    populateListView();
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
    trespassListHandler.populateListView(defaultHabit);
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
    switch (item.getItemId()) {
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
      TextView toolbarText = (TextView) findViewById(R.id.defaultHabitToolbar);
      toolbarText.setText(defaultHabit.getName());
    }
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
}
