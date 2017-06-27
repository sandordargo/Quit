package com.dargo.quit.utils;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class QuitSqliteDBHelper extends SQLiteOpenHelper {
  private static final String SQL_CREATE_HABITS =
      "CREATE TABLE HABITS (ID INTEGER PRIMARY KEY AUTOINCREMENT," +
          "NAME TEXT, ISDEFAULT INTEGER DEFAULT 0)";
  private static final String SQL_CREATE_TRESPASSES =
      "CREATE TABLE TRESPASSES (ID INTEGER PRIMARY KEY AUTOINCREMENT," +
          "HABIT_ID INTEGER, COMMIT_DATE INTEGER)";

  private static final String SQL_DELETE_HABITS =
      "DROP TABLE IF EXISTS HABITS";

  private static final String SQL_DELETE_TRESPASSES =
      "DROP TABLE IF EXISTS TRESPASSES";

  public static final int DATABASE_VERSION = 1;
  public static final String DATABASE_NAME = "Habits.db";

  public QuitSqliteDBHelper(Context context) {
    super(context, DATABASE_NAME, null, DATABASE_VERSION);
  }

  @Override
  public void onCreate(SQLiteDatabase db) {
    db.execSQL(SQL_CREATE_HABITS);
    db.execSQL(SQL_CREATE_TRESPASSES);
  }

  @Override
  public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    // This database is only a cache for online data, so its upgrade policy is
    // to simply to discard the data and start over
    db.execSQL(SQL_DELETE_HABITS);
    db.execSQL(SQL_DELETE_TRESPASSES);
    onCreate(db);
  }

  @Override
  public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    onUpgrade(db, oldVersion, newVersion);
  }
}
