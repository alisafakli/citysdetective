package com.example.citydetective.webservice;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHandler extends SQLiteOpenHelper{
		
	// All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 2;
 
    // Database Name
    private static final String DATABASE_NAME = "android_api";
 
    // Login table name
    private static final String TABLE_LOGIN = "login";
    private static final String TABLE_SERVER_MESSAGE = "ServerMessage";
    
    /*
     * {"tag":"login","success":1,"error":0,
     * "user":{"id":"6","ad":"123","soyad":"123",
     * "mail":"123@123.co","telefon":"123"}}

     * 
     */
 
    // Login Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "name";
    private static final String KEY_SURNAME = "surname";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_PASSWORD = "password";
    private static final String KEY_TELEPHONE = "telephone";

	private static final String KEY_MESSAGE = "message";

	private static final String KEY_IDS = "ids";

	private static final String KEY_TIME = "message_time";

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

	// Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_LOGIN_TABLE = "CREATE TABLE " + TABLE_LOGIN + "("
                + KEY_ID + " INTEGER PRIMARY KEY,"
                + KEY_NAME + " TEXT,"
                + KEY_SURNAME + " TEXT,"
                + KEY_EMAIL + " TEXT UNIQUE,"
                + KEY_PASSWORD + " TEXT,"
                + KEY_TELEPHONE + " TEXT"
                + " )";
        db.execSQL(CREATE_LOGIN_TABLE);
        
        String CREATE_SERVER_MESSAGE = "CREATE TABLE " + TABLE_SERVER_MESSAGE + "("
                + KEY_IDS + " TEXT PRIMARY KEY not null ,"
                + KEY_MESSAGE + " TEXT,"
                + KEY_TIME + " TEXT"
                + " )";
        db.execSQL(CREATE_SERVER_MESSAGE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_LOGIN);
 
        // Create tables again
        onCreate(db);
    }
    
    /**
     * Storing user details in database
     * */
    public void addUser(String id, String name, String surname, String email,String password,String telephone) {
        SQLiteDatabase db = this.getWritableDatabase();
 
        ContentValues values = new ContentValues();
        values.put(KEY_ID, id); // Name
        values.put(KEY_NAME, name); // Surname
        values.put(KEY_SURNAME, surname); // Email
        values.put(KEY_EMAIL, email); // BirthDate
        values.put(KEY_PASSWORD, password); // BirthDate
        values.put(KEY_TELEPHONE, telephone); // Surname
 
        // Inserting Row
        db.insert(TABLE_LOGIN, null, values);
        db.close(); // Closing database connection
    }
    
    public void addMessage(String id, String message, String message_time) {
        SQLiteDatabase db = this.getWritableDatabase();
 
        ContentValues values = new ContentValues();
        values.put(KEY_IDS, id); // id
        values.put(KEY_MESSAGE, message); // message
        values.put(KEY_TIME, message_time); // time
 
        // Inserting Row
        db.insert(TABLE_SERVER_MESSAGE, null, values);
        db.close(); // Closing database connection
    }
    public static ArrayList<ServerMessage> arr;
    public ArrayList<ServerMessage> getMessageDetails(){
        //HashMap<String,String> user = new HashMap<String,String>();
        String selectQuery = "SELECT  * FROM " + TABLE_SERVER_MESSAGE;
 
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // Move to first row
        arr = new ArrayList<ServerMessage>();
        
        cursor.moveToFirst();
        if(cursor.getCount() > 0){
        	while (cursor.moveToNext()) {
        		try{
        		ServerMessage sm = new ServerMessage();
        		sm.setId(cursor.getString(cursor.getColumnIndex("ids")));
        		//Log.e("KEY_ID", cursor.getString(cursor.getColumnIndex("ids")));
        		sm.setName(cursor.getString(cursor.getColumnIndex("message")));
        		//Log.e("KEY_msg", cursor.getString(cursor.getColumnIndex("message")));
        		sm.setTime(cursor.getString(cursor.getColumnIndex("message_time")));
        		//Log.e("KEY_time", cursor.getString(cursor.getColumnIndex("message_time")));
        		arr.add(sm);
        		}catch(Exception ex){
//        			 cursor.close();
//        		        db.close();
//        		        // return user
//        		        return arr;
        		}
        	}
        }
        cursor.close();
        db.close();
        // return user
        return arr;
    }
    public void resetTablesPN(){
        SQLiteDatabase db = this.getWritableDatabase();
        // Delete All Rows
        db.delete(TABLE_SERVER_MESSAGE, null, null);
        db.close();
    }
    /**
     * Getting user data from database
     * */
    public HashMap<String, String> getUserDetails(){
        HashMap<String,String> user = new HashMap<String,String>();
        String selectQuery = "SELECT  * FROM " + TABLE_LOGIN;
 
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // Move to first row
            
        cursor.moveToFirst();
        if(cursor.getCount() > 0){
            user.put(KEY_ID, cursor.getString(0));
            user.put(KEY_NAME, cursor.getString(1));
            user.put(KEY_SURNAME, cursor.getString(2));
            user.put(KEY_EMAIL, cursor.getString(3));
            user.put(KEY_PASSWORD, cursor.getString(4));
            user.put(KEY_TELEPHONE, cursor.getString(5));
        }
        cursor.close();
        db.close();
        // return user
        return user;
    }
    
    public String getUserId(){
    	String selectQuery = "SELECT id FROM " + TABLE_LOGIN;
    	SQLiteDatabase db = this.getReadableDatabase();
    	Cursor cursor = db.rawQuery(selectQuery, null);
    	cursor.moveToFirst();
        String id = cursor.getString(0);
        
        return id;
    }
    public void updatePhone(String userid , String newphone){
    	String selectQuery = "UPDATE login SET telephone = "+newphone;
    	SQLiteDatabase db = this.getReadableDatabase();
    	db.execSQL(selectQuery);
    	//Cursor cursor = db.rawQuery(countQuery, null);
        db.close();
        // cursor.close();
    	
//    	String countQuery = "Update "+TABLE_LOGIN +" SET "+KEY_TELEPHONE +" = "+ newphone + " Where id = "+userid ;
//    	SQLiteDatabase db = this.getReadableDatabase();
//        Cursor cursor = db.rawQuery(countQuery, null);
//        db.close();
//        cursor.close();
    }
    public void updatePassword(String userid , String password){
    	String selectQuery = "UPDATE login SET password = "+password;
    	SQLiteDatabase db = this.getReadableDatabase();
    	db.execSQL(selectQuery);
    	//Cursor cursor = db.rawQuery(countQuery, null);
        db.close();
        // cursor.close();
    	
//    	String countQuery = "Update "+TABLE_LOGIN +" SET "+KEY_TELEPHONE +" = "+ newphone + " Where id = "+userid ;
//    	SQLiteDatabase db = this.getReadableDatabase();
//        Cursor cursor = db.rawQuery(countQuery, null);
//        db.close();
//        cursor.close();
    }
    /**UPDATE t SET id = id + 1;
     * Getting user login status
     * return true if rows are there in table
     * */
    public int getRowCount() {
        String countQuery = "SELECT  * FROM " + TABLE_LOGIN;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int rowCount = cursor.getCount();
        db.close();
        cursor.close();
 
        // return row count
        return rowCount;
    }
    
    /**
     * Re crate database
     * Delete all tables and create them again
     * */
    public void resetTables(){
        SQLiteDatabase db = this.getWritableDatabase();
        // Delete All Rows
        db.delete(TABLE_LOGIN, null, null);
        db.close();
    }
}
