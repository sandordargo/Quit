package com.dargo.quit;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import java.util.Date;

public class MainActivity extends AppCompatActivity {

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
        Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
            .setAction("Action", null).show();
        new AddHabitDialogFragment().show(getFragmentManager(), "AddHabitDialogFragment");
      }
    });

    FloatingActionButton addNewTrespassButton = (FloatingActionButton) findViewById(R.id.addTrespassButton);
    addNewTrespassButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
          Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
          Trespass trespass = new SQLiteTrespasses(getBaseContext()).add(new Date());
          Toast.makeText(getBaseContext(), "New trespass at " + trespass.getDate(), Toast.LENGTH_LONG).show();

      }
    });
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
