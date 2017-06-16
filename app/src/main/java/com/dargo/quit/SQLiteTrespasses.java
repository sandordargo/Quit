package com.dargo.quit;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

public class SQLiteTrespasses implements Trespasses {
    SQLiteDatabase db;
    Context context;
    public SQLiteTrespasses(Context context) {
        this.context = context;
    }

    @Override
    public Iterable<Trespass> iterate() {
        this.db = new QuitSqliteDBHelper(context).getReadableDatabase();
        String[] projection = {"ID"};

        List<Trespass> trespasses = new ArrayList<>();
        Cursor cursor = db.query("TRESPASSES", projection, null,
                null, null, null, null);
        while(cursor.moveToNext()) {
            int id = cursor.getInt(
                    cursor.getColumnIndexOrThrow("ID"));
            trespasses.add(new SQLiteTrespass(db, id));
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
        String[] projection = {"ID"};
        String selection = "HABIT_ID=?";
        String[] selectionArgs = {String.valueOf(habit.getId())};
        List<Trespass> trespasses = new ArrayList<>();
        Cursor cursor = db.query("TRESPASSES", projection, selection,
                selectionArgs, null, null, null);
        while(cursor.moveToNext()) {
            int id = cursor.getInt(
                    cursor.getColumnIndexOrThrow("ID"));
            trespasses.add(new SQLiteTrespass(db, id));
        }
        cursor.close();

        return trespasses;
    }

    @Override
    public Trespass add(Habit habit) {
        SQLiteDatabase db = new QuitSqliteDBHelper(context).getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("HABIT_ID", habit.getId());
        values.put("COMMIT_DATE", System.currentTimeMillis());
        long newRowId = db.insert("TRESPASSES", null, values);
        return new SQLiteTrespass(new QuitSqliteDBHelper(context).getReadableDatabase(), newRowId);
    }

    @Override
    public boolean delete(Trespass trespass) {
        SQLiteDatabase db = new QuitSqliteDBHelper(context).getWritableDatabase();
        return db.delete("TRESPASSES", "ID=" + trespass.getId(), null) > 0;
    }
}
