package com.dargo.quit;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements TrespassListAdapterCallback {

  ListView listView;
  TrespassListAdapter trespassListAdapter;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    showAllHabits();
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
        Habit habit = null;
        for (Habit aHabit : new SQLiteHabits(getBaseContext()).iterate()) {
          habit = aHabit;
          break;
        }
        new SQLiteTrespasses(getBaseContext()).add(habit);
        populateListView();
      }
    });
    populateListView();
  }

  public void populateListView() {
    listView = (ListView) findViewById(R.id.listview);
    List<Trespass> values = new ArrayList<>();
    for (Trespass trespass : new SQLiteTrespasses(getBaseContext()).iterate()) {
      values.add(trespass);
    }
    trespassListAdapter = new TrespassListAdapter(this, values);
    trespassListAdapter.setCallback(this);
    listView.setAdapter(trespassListAdapter);
  }

  private void showAllHabits() {
    String text = "";
    for (Habit habit : new SQLiteHabits(getBaseContext()).iterate())
      text += habit.getName() + ", ";
    Toast.makeText(getBaseContext(), "Habits are" + text + ".", Toast.LENGTH_LONG).show();
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    // Inflate the menu; this adds items to the action bar if it is present.
    getMenuInflater().inflate(R.menu.menu_main, menu);
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    // Handle action bar item clicks here. The action bar will
    // automatically handle clicks on the Home/Up button, so long
    // as you specify a parent activity in AndroidManifest.xml.
    int id = item.getItemId();

    //noinspection SimplifiableIfStatement
    if (id == R.id.action_settings) {
      return true;
    }

    return super.onOptionsItemSelected(item);
  }


  public void onUserAddsNewHabit(String newHabit) {
    Habit habit = new SQLiteHabits(getBaseContext()).add(newHabit);
    Toast.makeText(getBaseContext(), "New habit is " + habit.getName() + ".", Toast.LENGTH_LONG).show();
  }
}
