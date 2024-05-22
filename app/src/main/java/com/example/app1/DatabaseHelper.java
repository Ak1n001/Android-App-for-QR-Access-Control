package com.example.app1;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "users.db";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_NAME = "user_table";
    private static final String COL_1 = "ID";
    private static final String COL_2 = "USERNAME";
    private static final String COL_3 = "PASSWORD";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);

    }
    public void onCreate(SQLiteDatabase db) {
        // Drop existing table if it exists
        try{
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        }
        catch (SQLiteException e) {
            Log.e("DatabaseHelper", "Error dropping table: " + e.getMessage());
            System.out.println(e.getMessage());
        }

        String createTable = "CREATE TABLE " + TABLE_NAME + " (" +
                COL_1 + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_2 + " TEXT, " +
                COL_3 + " TEXT)";
        db.execSQL(createTable);

    }

    public void add(String a,String b){
        SQLiteDatabase db = this.getReadableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_2, a);
        contentValues.put(COL_3, b);
        db.insertWithOnConflict(TABLE_NAME, null, contentValues, SQLiteDatabase.CONFLICT_IGNORE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public String getUserId(String username) {
        SQLiteDatabase db = this.getReadableDatabase();
        String userId = null;
        Cursor cursor = db.query(TABLE_NAME, new String[]{COL_1}, COL_2+"=?", new String[]{username}, null, null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                userId = cursor.getString(cursor.getColumnIndexOrThrow("ID"));
            }
            cursor.close();
        }
        return userId;
    }

    public boolean validateUser(String username, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME, new String[]{COL_1}, COL_2 + "=? AND " + COL_3 + "=?", new String[]{username, password},
                null, null, null);

        boolean isValid = (cursor != null && cursor.getCount() > 0);
        if (cursor != null) {
            cursor.close();
        }
        return isValid;
    }
}
