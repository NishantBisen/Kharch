package com.dailyexpense.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import static com.dailyexpense.database.Constants.*;

public class DatabaseAdapterClass {

	DataBaseHelper helper;
	Context context;
	Constants constants;

	public static final String CREATE_DATABASE = "create table " + TABLE_NAME
			+ " ( " + ID + " integer primary key autoincrement, " + DATE
			+ " varchar[255], " + AMOUNT + " real, " + SPENT_ON
			+ " varchar[255], " + NOTE + " varchar[255], " + MONTH
			+ " varchar[20]" + ");";

	public static final String DROP_TABLE = "DROP TABLE IF EXISTS "
			+ TABLE_NAME;

	public DatabaseAdapterClass(Context context) {
		helper = new DataBaseHelper(context);
		this.context = context;
	}

	public long insertData(String date, double amount, String spenton,
			String note, String month) {
		SQLiteDatabase db = helper.getWritableDatabase();
		ContentValues contentValues = new ContentValues();
		contentValues.put(DATE, date);
		contentValues.put(AMOUNT, amount);
		contentValues.put(SPENT_ON, spenton);
		contentValues.put(NOTE, note);
		contentValues.put(MONTH, month);
		long id = db.insert(TABLE_NAME, null, contentValues);
		return id;
	}

	public Cursor fetchData() {
		SQLiteDatabase db = helper.getWritableDatabase();
		String[] columns = { ID, DATE, AMOUNT, SPENT_ON, NOTE, MONTH };
		Cursor cursor = db.query(TABLE_NAME, columns, null, null, null, null,
				null);
		if (cursor != null)
			cursor.moveToFirst();
		return cursor;
	}

	public Cursor fetchDataWhere(String month) {
		SQLiteDatabase db = helper.getWritableDatabase();
		String where = MONTH + " = " + month;
		String[] columns = { ID, DATE, AMOUNT, SPENT_ON, NOTE, MONTH };
		Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME
				+ " where MONTH = '" + month + "'", null);
		// Cursor cursor = db.query(TABLE_NAME, columns , where, null, null,
		// null, null, null);
		// Log.v("Cursor", ""+cursor);
		if (cursor != null)
			cursor.moveToFirst();
		return cursor;
	}

	public void deleteData(int id) {
		SQLiteDatabase db = helper.getWritableDatabase();
		// db.execSQL("delete from "+TABLE_NAME+" where ='"+id+"'");
		db.delete(TABLE_NAME, ID + "=" + id, null);
	}

	static class DataBaseHelper extends SQLiteOpenHelper {
		Context context;

		public DataBaseHelper(Context context) {
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
			this.context = context;
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			try {
				db.execSQL(CREATE_DATABASE);
				Log.v("DataBAseCreated", "Create");
			} catch (SQLException e) {
				// TODO: handle exception
			}

		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			try {
				db.execSQL(DROP_TABLE);
				onCreate(db);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
