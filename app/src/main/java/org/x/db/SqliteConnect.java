package org.x.db;

import org.x.conf.Const;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SqliteConnect extends SQLiteOpenHelper {
	protected static final int Version = 1;
	protected Context mContext;
	protected SQLiteDatabase mDatabase = null;
	protected String fileName = null;

	public SqliteConnect(Context context) {
		super(context, Const.DBName, null, Version);
		mContext = context;
		fileName = context.getApplicationInfo().dataDir + "/databases/";
		open();
	}

	public SQLiteDatabase open() {
		mDatabase = SQLiteDatabase.openDatabase(fileName, null,
				SQLiteDatabase.OPEN_READWRITE);
		return mDatabase;
	}

	public void close() {
		mDatabase.close();
		mDatabase = null;
	}

	public SQLiteDatabase db() {
		return mDatabase;
	}

	@Override
	public void onCreate(SQLiteDatabase db) {

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
	}

}
