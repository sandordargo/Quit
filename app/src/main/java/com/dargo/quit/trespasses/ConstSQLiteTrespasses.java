package com.dargo.quit.trespasses;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.dargo.quit.utils.QuitSqliteDBHelper;
import com.dargo.quit.habits.Habit;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ConstSQLiteTrespasses implements Trespasses {
    SQLiteDatabase db;
    Context context;
    public ConstSQLiteTrespasses(Context context) {
        this.context = context;
    }

    @Override
    public Iterable<Trespass> iterate() {
        this.db = new QuitSqliteDBHelper(context).getReadableDatabase();
        String[] projection = {"ID", "COMMIT_DATE"};

        List<Trespass> trespasses = new ArrayList<>();
        Cursor cursor = db.query("TRESPASSES", projection, null,
                null, null, null, "COMMIT_DATE DESC");
        while(cursor.moveToNext()) {
            int id = cursor.getInt(
                    cursor.getColumnIndexOrThrow("ID"));
            Date date = new Date(cursor.getLong(cursor.getColumnIndex("COMMIT_DATE")));
            trespasses.add(new ConstTrespass(new SQLiteTrespass(db, id), date));
        }
        cursor.close();

        return trespasses;
    }

    @Override
    public Iterable<Trespass> iterate(Habit habit) {
        if (habit == null) {
            return new ArrayList<>();
        }
        this.db = new QuitSqliteDBHelper(context).getReadableDatabase();
        String[] projection = {"ID", "COMMIT_DATE"};
        String selection = "HABIT_ID=?";
        String[] selectionArgs = {String.valueOf(habit.getId())};
        List<Trespass> trespasses = new ArrayList<>();
        Cursor cursor = db.query("TRESPASSES", projection, selection,
                selectionArgs, null, null, "COMMIT_DATE DESC");
        while(cursor.moveToNext()) {
            int id = cursor.getInt(
                    cursor.getColumnIndexOrThrow("ID"));
            Date date = new Date(cursor.getLong(cursor.getColumnIndex("COMMIT_DATE")));
            trespasses.add(new ConstTrespass(new SQLiteTrespass(db, id), date));
        }
        cursor.close();

        return trespasses;
    }

    @Override
    public Trespass add(Habit habit) {
        SQLiteDatabase db = new QuitSqliteDBHelper(context).getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("HABIT_ID", habit.getId());
        long currentTimeStamp = System.currentTimeMillis();
        values.put("COMMIT_DATE", currentTimeStamp);
        long newRowId = db.insert("TRESPASSES", null, values);
        return new ConstTrespass(new SQLiteTrespass(new QuitSqliteDBHelper(context).getReadableDatabase(), newRowId), new Date(currentTimeStamp));
    }

    @Override
    public boolean delete(Trespass trespass) {
        SQLiteDatabase db = new QuitSqliteDBHelper(context).getWritableDatabase();
        return db.delete("TRESPASSES", "ID=" + trespass.getId(), null) > 0;
    }
}
