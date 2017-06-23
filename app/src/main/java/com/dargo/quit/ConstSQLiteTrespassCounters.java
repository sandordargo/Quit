package com.dargo.quit;


import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ConstSQLiteTrespassCounters implements TrespassCounters {
    SQLiteDatabase db;
    Context context;
    public ConstSQLiteTrespassCounters(Context context) {
        this.context = context;
    }

    @Override
    public Iterable<TrespassCounter> trespassesPerDayFor(Habit habit) {
        if (habit == null) {
            return new ArrayList<>();
        }
        this.db = new QuitSqliteDBHelper(context).getReadableDatabase();
        String[] projection = {"COMMIT_DATE, DATE(COMMIT_DATE/1000, 'unixepoch') AS COMMIT_DAY",
                "COUNT(1) as DAILY_COUNT"};
        String selection = "HABIT_ID=?";
        String[] selectionArgs = {String.valueOf(habit.getId())};
        List<TrespassCounter> trespassCounters = new ArrayList<>();
        Cursor cursor = db.query("TRESPASSES", projection, selection,
                selectionArgs, "COMMIT_DAY", null, "COMMIT_DAY DESC");
        while(cursor.moveToNext()) {
            Date date = new Date(cursor.getLong(cursor.getColumnIndex("COMMIT_DATE")));
            trespassCounters.add(new TrespassCounter(date,
                    cursor.getInt(cursor.getColumnIndex("DAILY_COUNT"))));
        }
        cursor.close();
        this.db.close();
        return trespassCounters;
    }
}
