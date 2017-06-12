package com.dargo.quit;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

public class SQLiteHabits implements Habits {
  SQLiteDatabase db;
  Context context;
  public SQLiteHabits(Context context) {
    this.context = context;
  }

  @Override
  public Iterable<Habit> iterate() {
    this.db = new HabitsSqliteDBHelper(context).getReadableDatabase();
    String[] projection = {"ID"};

    List<Habit> habits= new ArrayList<>();
    Cursor cursor = db.query("HABITS", projection, null,
        null, null, null, null);
    while(cursor.moveToNext()) {
      int id = cursor.getInt(
          cursor.getColumnIndexOrThrow("ID"));
      habits.add(new SQLiteHabit(db, id));
    }
    cursor.close();

    return habits;
  }

  @Override
  public Habit add(String name) {
    SQLiteDatabase db = new HabitsSqliteDBHelper(context).getWritableDatabase();
    ContentValues values = new ContentValues();
    values.put("NAME", name);
    long newRowId = db.insert("HABITS", null, values);
    return new SQLiteHabit(new HabitsSqliteDBHelper(context).getReadableDatabase(), newRowId);
  }
}
