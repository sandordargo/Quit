package com.dargo.quit;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
    public Trespass add(Date date) {
        SQLiteDatabase db = new QuitSqliteDBHelper(context).getWritableDatabase();
        ContentValues values = new ContentValues();
        long newRowId = db.insert("TRESPASSES", null, values);
        return new SQLiteTrespass(new QuitSqliteDBHelper(context).getReadableDatabase(), newRowId);
    }
}
