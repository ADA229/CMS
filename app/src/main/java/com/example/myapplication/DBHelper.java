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
    private static final String COLUMN_USER_TYPE = "user_type"; // New column for user type

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
                + COLUMN_USER_TYPE + " TEXT)"; // Include user type
        db.execSQL(CREATE_USERS_TABLE);

        // Insert sample users with user types
        insertUser(db, "hello", "world", "admin");
        insertUser(db, "user1", "pass1", "user");
        insertUser(db, "user2", "pass2", "user");
    }

    // Inserts a new user into the database
    private static void insertUser(SQLiteDatabase db, String username, String password, String userType) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_USERNAME, username);
        values.put(COLUMN_PASSWORD, password);
        values.put(COLUMN_USER_TYPE, userType); // Store user type
        db.insert(TABLE_USERS, null, values);
    }

    // Validates user credentials
    public boolean validateUser(String username, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_USERS, new String[]{COLUMN_USERNAME},
                COLUMN_USERNAME + "=? AND " + COLUMN_PASSWORD + "=?",
                new String[]{username, password}, null, null, null);
        int count = cursor.getCount();
        cursor.close();
        return count > 0;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if it exists
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        // Recreate the table
        onCreate(db);
    }

    public boolean usernameExists(String user) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_USERS, new String[]{COLUMN_USERNAME},
                COLUMN_USERNAME + "=?", new String[]{user}, null, null, null);
        int count = cursor.getCount();
        cursor.close();
        return count > 0; // Returns true if the username exists
    }

    // Add a new user with a specified user type
    public void addUser(String user, String pass, String userType) {
        if (!usernameExists(user)) {
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(COLUMN_USERNAME, user);
            values.put(COLUMN_PASSWORD, pass);
            values.put(COLUMN_USER_TYPE, userType); // Store user type
            db.insert(TABLE_USERS, null, values);
            db.close(); // Close the database connection
        } else {
            // Optionally, you can throw an exception or log a message
            throw new IllegalArgumentException("Username already exists");
        }
    }

    // Method to get user type by username
    public String getUserType(String username) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_USERS, new String[]{COLUMN_USER_TYPE},
                COLUMN_USERNAME + "=?", new String[]{username}, null, null, null);

        String userType = null; // Initialize userType to null

        if (cursor != null) {
            if (cursor.moveToFirst()) { // Ensure cursor is not empty
                userType = cursor.getString(0); // Get user type
            }
            cursor.close();
        }
        return userType; // Return user type or null
    }
}
