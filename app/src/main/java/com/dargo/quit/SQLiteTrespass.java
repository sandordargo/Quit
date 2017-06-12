package com.dargo.quit;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.Date;

public class SQLiteTrespass implements Trespass {
    private final SQLiteDatabase db;
    private final long id;

    public SQLiteTrespass(SQLiteDatabase db, long id) {
        this.db = db;
        this.id = id;
    }

    @Override
    public long getId() {
        return this.id;
    }

    @Override
    public Date getDate() {
        String[] projection = {"COMMIT_DATE"};
        String selection = "ID = ?";
        String[] selectionArgs = { String.valueOf(this.id) };
        Date date = new Date();

        Cursor cursor = db.query("TRESPASSES", projection, selection,
                selectionArgs, null, null, null);
        if(cursor.getCount() > 0) {
            cursor.moveToFirst();
            date = new Date(cursor.getColumnIndex("COMMIT_DATE")*1000);
        }
        cursor.close();

        return date;
    }

}
