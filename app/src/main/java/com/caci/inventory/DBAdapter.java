package com.caci.inventory;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBAdapter {
    public static final String DB_NAME = "employee_db";
    public static final int DB_VERSION = 1;
    public static final String TABLE_NAME = "employee";
    public static final String C_ID = "_id";
    public static final String C_NAME = "name";
    public static final String C_EMAIL = "email";

    private static final String CREATE_TABLE_SQL = "create table " +
            TABLE_NAME + " ( " + C_ID + " integer primary key autoincrement, " + C_NAME + " text, " + C_EMAIL + " text)";

    private Context context;
    private DBHelper dbHelper;
    private SQLiteDatabase db;

    public DBAdapter(Context context) {
        this.context = context;
        dbHelper = new DBHelper();
    }
    //open database
    public void open() {
        db = dbHelper.getWritableDatabase();
    }
//close database

    public void close() {
        db.close();
    }
    //insert an employee
    public long insert(Employee employee) {
        ContentValues values = new ContentValues();
        values.put(C_NAME, employee.getName());
        values.put(C_EMAIL, employee.getEmail());
        return db.insert(TABLE_NAME, null, values);
    }
    //get all employee
    public Cursor getAll() {
        return db.query(TABLE_NAME, null, null, null, null, null, null);
    }
    // update a record
    public int update(long id, Employee employee) {
        ContentValues values = new ContentValues();
        values.put(C_NAME, employee.getName());
        values.put(C_EMAIL, employee.getEmail());
        return db.update(TABLE_NAME, values, C_ID + "= ?", new String[]{id + ""});
    }
    // delete a record
    public int delete(long id) {
        return db.delete(TABLE_NAME, C_ID + "= ?", new String[]{id + ""});
    }
    //get a single record
    public Cursor getEmployeeById(long id) {
        Cursor cursor = db.query(TABLE_NAME, null, C_ID + "= ?",
                new String[] { id + "" }, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
        }
        return cursor;
    }
    private class DBHelper extends SQLiteOpenHelper {
        public DBHelper() {
            super(context, DB_NAME, null, DB_VERSION);
        }
        @Override
        public void onCreate(SQLiteDatabase db) {
            try {
                db.execSQL(CREATE_TABLE_SQL);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
//your database upgrade logic goes here
            db.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME);
            onCreate(db);
        }

    }

}
