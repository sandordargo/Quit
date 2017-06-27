package com.dargo.quit.habits;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.dargo.quit.utils.QuitSqliteDBHelper;

import java.util.ArrayList;
import java.util.List;

public class ConstSQLiteHabits implements Habits {
    SQLiteDatabase db;
    Context context;
    public ConstSQLiteHabits(Context context) {
        this.context = context;
    }

    @Override
    public Iterable<Habit> iterate() {
        this.db = new QuitSqliteDBHelper(context).getReadableDatabase();
        String[] projection = {"ID", "NAME", "ISDEFAULT"};

        List<Habit> habits= new ArrayList<>();
        Cursor cursor = db.query("HABITS", projection, null,
                null, null, null, null);
        while(cursor.moveToNext()) {
            int id = cursor.getInt(
                    cursor.getColumnIndexOrThrow("ID"));
            boolean isDefault = cursor.getInt(cursor.getColumnIndex("ISDEFAULT")) > 0;
            habits.add(new ConstHabit(new SQLiteHabit(db, id),
                    cursor.getString(cursor.getColumnIndex("NAME")),
                    isDefault));
        }
        cursor.close();

        return habits;
    }

    @Override
    public Habit add(String name) {
        SQLiteDatabase db = new QuitSqliteDBHelper(context).getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("NAME", name);
        long newRowId = db.insert("HABITS", null, values);
        return new ConstHabit(new SQLiteHabit(new QuitSqliteDBHelper(context).getReadableDatabase(), newRowId), name, false);
    }

    @Override
    public boolean delete(Habit habit) {
        SQLiteDatabase db = new QuitSqliteDBHelper(context).getWritableDatabase();
        return db.delete("HABITS", "ID=" + habit.getId(), null) > 0;
    }

    @Override
    public Habit getDefaultHabit() {
        SQLiteDatabase db = new QuitSqliteDBHelper(context).getReadableDatabase();
        String[] projection = {"ID", "NAME"};
        String selection = "ISDEFAULT=?";
        String[] selectionArgs = {"1"};
        Habit habit = null;
        Cursor cursor = db.query("HABITS", projection, selection,
                selectionArgs, null, null, null);
        while(cursor.moveToNext()) {
            int id = cursor.getInt(
                    cursor.getColumnIndexOrThrow("ID"));
            String name = cursor.getString(cursor.getColumnIndexOrThrow("NAME"));
            habit = new ConstHabit(new SQLiteHabit(db, id), name, true);
            break;
        }
        cursor.close();
        db.close();
        return habit;
    }
}
