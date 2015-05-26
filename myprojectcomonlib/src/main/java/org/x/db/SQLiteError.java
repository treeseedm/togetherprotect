package org.x.db;

import android.database.sqlite.SQLiteException;

public class SQLiteError extends SQLiteException {

	private static final long serialVersionUID = 1L;

	public SQLiteError() {
	}

	public SQLiteError(String error) {
		super(error);
	}
}
