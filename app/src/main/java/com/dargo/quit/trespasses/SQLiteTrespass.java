package com.dargo.quit.trespasses;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.text.SimpleDateFormat;
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
            date = new Date(cursor.getLong(cursor.getColumnIndex("COMMIT_DATE")));
        }
        cursor.close();

        return date;
    }

    @Override
    public String getFormattedDate() {
        return new SimpleDateFormat("MM/dd/yyyy HH:mm:ss").format(getDate());
    }

    @Override
    public void updateDate(Date newDate) {
        ContentValues valuesWithNewDate = new ContentValues();
        valuesWithNewDate.put("COMMIT_DATE", newDate.getTime());
        db.update("TRESPASSES", valuesWithNewDate, "ID=" + String.valueOf(this.getId()), null);
    }
}
