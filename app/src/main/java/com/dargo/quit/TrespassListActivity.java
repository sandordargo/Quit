package com.dargo.quit;

import android.content.Intent;
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

import java.util.ArrayList;
import java.util.List;

public class TrespassListActivity extends AppCompatActivity implements ListAdapterCallback {

  ListView listView;
  TrespassListAdapter trespassListAdapter;
  Habit defaultHabit;
  boolean habitIsBeingRead;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    habitIsBeingRead = false;
    setDefaultHabit();
    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);

    FloatingActionButton addNewHabitButton = (FloatingActionButton) findViewById(R.id.floatingActionButton);
    addNewHabitButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        new AddHabitDialogFragment().show(getFragmentManager(), "AddHabitDialogFragment");
      }
    });

    FloatingActionButton addNewTrespassButton = (FloatingActionButton) findViewById(R.id.addTrespassButton);
    addNewTrespassButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        new SQLiteTrespasses(getBaseContext()).add(defaultHabit);
        populateListView();
      }
    });
    populateListView();
  }

  private void setDefaultHabit() {
    boolean isThereADefaultHabit = false;
    defaultHabit = null;
    Iterable<Habit> habits = new SQLiteHabits(getBaseContext()).iterate();
    if (!habits.iterator().hasNext() && !habitIsBeingRead) {
      habitIsBeingRead = true;
      new AddHabitDialogFragment().show(getFragmentManager(), "AddHabitDialogFragment");
      makeFirstHabitDefault();
      return;
    }

    for (Habit habit : habits) {
      if (habit.isDefault()) {
        isThereADefaultHabit = true;
        this.defaultHabit = habit;
      }
    }

    if (!isThereADefaultHabit) {
      makeFirstHabitDefault();
    }
    updateTitle();
  }

  private void makeFirstHabitDefault() {
    Iterable<Habit> habits = new SQLiteHabits(getBaseContext()).iterate();
    if (habits.iterator().hasNext()) {
      Habit habit = habits.iterator().next();
      habit.makeDefault();
      this.defaultHabit = habit;
      return;
    }
  }

  public void populateListView() {
    listView = (ListView) findViewById(R.id.listview);
    List<Trespass> values = new ArrayList<>();
    for (Trespass trespass : new SQLiteTrespasses(getBaseContext()).iterate(defaultHabit)) {
      values.add(trespass);
    }
    trespassListAdapter = new TrespassListAdapter(this, values);
    trespassListAdapter.setCallback(this);
    listView.setAdapter(trespassListAdapter);
  }

  public void cleanListView() {
    listView = (ListView) findViewById(R.id.listview);
    List<Trespass> emptyList = new ArrayList<>();
    trespassListAdapter = new TrespassListAdapter(this, emptyList);
    trespassListAdapter.setCallback(this);
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
      case R.id.list_habits:
        Intent intent = new Intent(this, HabitsManagementActivity.class);
        startActivity(intent);
        return true;
      default:
        return super.onOptionsItemSelected(item);
    }
  }

  public void onUserAddsNewHabit(String newHabit) {
    Habit habit = new SQLiteHabits(getBaseContext()).add(newHabit);
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
}
