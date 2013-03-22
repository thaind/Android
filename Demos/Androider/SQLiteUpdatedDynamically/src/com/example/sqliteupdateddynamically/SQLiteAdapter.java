package com.example.sqliteupdateddynamically;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
public class SQLiteAdapter {
	public static final String MYDATABASE_NAME = "MY_DATABASE";
	public static final String MYDATABASE_TABLE = "MY_TABLE";
	public static final int MYDATABASE_VERSION = 1;
	public static final String KEY_ID = "_id";
	public static final String KEY_CONTENT1 = "Content1";
	public static final String KEY_CONTENT2 = "Content2";
	// create table MY_DATABASE (ID integer primary key, Content text not null);
	private static final String SCRIPT_CREATE_DATABASE =
	"create table " + MYDATABASE_TABLE + " ("
	+ KEY_ID + " integer primary key autoincrement, "
	+ KEY_CONTENT1 + " text not null, "
	+ KEY_CONTENT2 + " text not null);";
	private SQLiteHelper sqLiteHelper;
	private SQLiteDatabase sqLiteDatabase;
	private Context context;
	public SQLiteAdapter(Context c) {
		context = c;
	}
	public SQLiteAdapter openToRead() throws android.database.SQLException {
		sqLiteHelper = new SQLiteHelper(context, MYDATABASE_NAME, null,
				MYDATABASE_VERSION);
		sqLiteDatabase = sqLiteHelper.getReadableDatabase();
		return this;
	}
	public SQLiteAdapter openToWrite() throws android.database.SQLException {
		sqLiteHelper = new SQLiteHelper(context, MYDATABASE_NAME, null,
				MYDATABASE_VERSION);
		sqLiteDatabase = sqLiteHelper.getWritableDatabase();
		return this;
	}
	public void close() {
		sqLiteHelper.close();
	}
	public long insert(String content1, String content2) {
		ContentValues contentValues = new ContentValues();
		contentValues.put(KEY_CONTENT1, content1);
		contentValues.put(KEY_CONTENT2, content2);
		return sqLiteDatabase.insert(MYDATABASE_TABLE, null, contentValues);
	}
	public int deleteAll() {
		return sqLiteDatabase.delete(MYDATABASE_TABLE, null, null);
	}
	public Cursor queueAll() {
		String[] columns = new String[] { KEY_ID, KEY_CONTENT1, KEY_CONTENT2 };
		Cursor cursor = sqLiteDatabase.query(MYDATABASE_TABLE, columns,
		null, null, null, null, null);
		return cursor;
	}
	public class SQLiteHelper extends SQLiteOpenHelper {
		public SQLiteHelper(Context context, String name,
		CursorFactory factory, int version) {
			super(context, name, factory, version);
		}
		@Override
		public void onCreate(SQLiteDatabase db) {
			// TODO Auto-generated method stub
			db.execSQL(SCRIPT_CREATE_DATABASE);
		}
		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			// TODO Auto-generated method stub
		}
	}
}
