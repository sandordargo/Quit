package com.dargo.quit;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class SQLiteHabit implements Habit {
  private final SQLiteDatabase db;
  private final long id;

  public SQLiteHabit(SQLiteDatabase db, long id) {
    this.db = db;
    this.id = id;
  }

  @Override
  public long getId() {
    return this.id;
  }

  @Override
  public String getName() {
    String[] projection = {"NAME"};
    String selection = "ID = ?";
    String[] selectionArgs = { String.valueOf(this.id) };
    String name = "";

    Cursor cursor = db.query("HABITS", projection, selection,
        selectionArgs, null, null, null);
    if(cursor.getCount() > 0) {
      cursor.moveToFirst();
      name = cursor.getString(cursor.getColumnIndex("NAME"));
    }
    cursor.close();

    return name;
  }

  @Override
  public boolean isDefault() {
    String[] projection = {"ISDEFAULT"};
    String selection = "ID = ?";
    String[] selectionArgs = { String.valueOf(this.id) };
    boolean isDefault = false;

    Cursor cursor = db.query("HABITS", projection, selection,
            selectionArgs, null, null, null);
    if(cursor.getCount() > 0) {
      cursor.moveToFirst();
      isDefault = cursor.getInt(cursor.getColumnIndex("ISDEFAULT")) > 0;
    }
    cursor.close();

    return isDefault;
  }

  @Override
  public void makeDefault() {
    ContentValues nonDefaultValues = new ContentValues();
    nonDefaultValues.put("ISDEFAULT", "0");
    db.update("HABITS", nonDefaultValues, null, null);
    ContentValues defaultHabitValues = new ContentValues();
    defaultHabitValues.put("ISDEFAULT", "1");
    db.update("HABITS", defaultHabitValues, "ID=" + String.valueOf(this.getId()), null);
  }

}
