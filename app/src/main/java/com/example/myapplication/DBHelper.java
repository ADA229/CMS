package com.example.myapplication;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {

    // Database name and version
    private static final String DATABASE_NAME = "UserDB";
    private static final int DATABASE_VERSION = 1;

    // Table and columns
    private static final String TABLE_USERS = "users";
    private static final String COLUMN_USERNAME = "username";
    private static final String COLUMN_PASSWORD = "password";
    private static final String COLUMN_USER_TYPE = "user_type";

    // Constructor
    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create users table with user type
        String CREATE_USERS_TABLE = "CREATE TABLE " + TABLE_USERS + "("
                + COLUMN_USERNAME + " TEXT PRIMARY KEY,"
                + COLUMN_PASSWORD + " TEXT,"
                + COLUMN_USER_TYPE + " TEXT)";
        db.execSQL(CREATE_USERS_TABLE);

        // Insert sample users
        insertUser(db, "shopkeeper1", "shop123", "shopkeeper");
        insertUser(db, "customer1", "cust123", "customer");
        insertUser(db, "customer2", "cust456", "customer");
    }

    private static void insertUser(SQLiteDatabase db, String username, String password, String userType) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_USERNAME, username);
        values.put(COLUMN_PASSWORD, password);
        values.put(COLUMN_USER_TYPE, userType);
        db.insert(TABLE_USERS, null, values);
    }

    // Validates user credentials
    public boolean validateUser(String username, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        // Ensure that the table name and column names are correct
        Cursor cursor = db.rawQuery("SELECT " + COLUMN_USERNAME + " FROM " + TABLE_USERS +
                        " WHERE " + COLUMN_USERNAME + "=? AND " + COLUMN_PASSWORD + "=?",
                new String[]{username, password});
        int count = cursor.getCount();
        cursor.close();
        return count > 0;
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        onCreate(db);
    }

    public boolean usernameExists(String user) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_USERS, new String[]{COLUMN_USERNAME},
                COLUMN_USERNAME + "=?", new String[]{user}, null, null, null);
        boolean exists = cursor.getCount() > 0;
        cursor.close();
        return exists;
    }

    public void addUser(String user, String pass, String userType) {
        if (!usernameExists(user)) {
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(COLUMN_USERNAME, user);
            values.put(COLUMN_PASSWORD, pass);
            values.put(COLUMN_USER_TYPE, userType);
            db.insert(TABLE_USERS, null, values);
            db.close();
        } else {
            throw new IllegalArgumentException("Username already exists");
        }
    }

    public String getUserType(String username) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_USERS, new String[]{COLUMN_USER_TYPE},
                COLUMN_USERNAME + "=?", new String[]{username}, null, null, null);

        String userType = null;

        if (cursor != null && cursor.moveToFirst()) {
            userType = cursor.getString(0); // Get user type
            cursor.close();
        }
        return userType; // Return user type or null
    }
}