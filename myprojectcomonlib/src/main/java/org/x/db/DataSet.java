package org.x.db;

import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;

import android.content.Context;
import android.database.Cursor;

import com.mongodb.BasicDBObject;

public class DataSet extends CursorLoader {
	protected SqliteConnect connect = null;
	protected String rawQuery = null;
	protected String[] args = null;

	public DataSet(Context context, SqliteConnect connect, String rawQuery,
			String[] args) {
		super(context);
		this.connect = connect;
		this.rawQuery = rawQuery;
		this.args = args;
	}

	@Override
	protected Cursor buildCursor() {
		return (connect.db().rawQuery(rawQuery, args));
	}

	@Override
	public void dump(String prefix, FileDescriptor fd, PrintWriter writer,
			String[] args) {
		super.dump(prefix, fd, writer, args);
		writer.print(prefix);
		writer.print("rawQuery=");
		writer.println(rawQuery);
		writer.print(prefix);
		writer.print("args=");
		writer.println(Arrays.toString(args));
	}

	public long getCount(String sql) {
		Cursor cursor = connect.db().rawQuery(sql, null);
		if (cursor.moveToNext()) {
			return cursor.getLong(0);
		}
		return 0;
	}

	private int readRecords(Cursor cursor, ArrayList<BasicDBObject> records,
			int offset, int size) {
		boolean readAll = size == 0;
		int recordCount = cursor.getCount();
		if (recordCount == 0) {
			return recordCount;
		}
		cursor.move(offset);
		String[] fieldNames = cursor.getColumnNames();
		while (cursor.moveToNext() && (readAll || size > 0)) {
			size--;
			for (int i = 0; i < fieldNames.length; i++) {
				int fieldType = cursor.getType(i);
				BasicDBObject item = new BasicDBObject();
				switch (fieldType) {
				case Cursor.FIELD_TYPE_BLOB:
					item.append(fieldNames[i], cursor.getBlob(i));
					break;
				case Cursor.FIELD_TYPE_FLOAT:
					item.append(fieldNames[i], cursor.getFloat(i));
					break;
				case Cursor.FIELD_TYPE_INTEGER:
					item.append(fieldNames[i], cursor.getInt(i));
					break;
				case Cursor.FIELD_TYPE_STRING:
					item.append(fieldNames[i], cursor.getString(i));
					break;
				case Cursor.FIELD_TYPE_NULL:
					item.append(fieldNames[i], "");
					break;
				}
				records.add(item);
			}
		}
		return recordCount;
	}

	public DataResult query(String sql, String[] params, int offset, int size) {
		Cursor cursor = connect.db().rawQuery(sql, params);
		DataResult dr = new DataResult();
		try {
			dr.recordCount = readRecords(cursor, dr.records, offset, size);
		} finally {
			cursor.close();
		}
		return dr;
	}

	public ArrayList<BasicDBObject> queryObjects(String sql, String[] params,
			int start, int end) {
		int size = end - start;
		Cursor cursor = null;
		if (size > 0) {
			int index = params.length;
			String[] values = new String[index + 2];
			System.arraycopy(params, 0, values, 0, params.length);
			values[index++] = String.valueOf(start);
			values[index++] = String.valueOf(size);
			cursor = connect.db().rawQuery(sql + " limit ?,?", values);
		} else {
			cursor = connect.db().rawQuery(sql, params);
			size = 0;
		}
		ArrayList<BasicDBObject> records = new ArrayList<BasicDBObject>();
		try {
			readRecords(cursor, records, start, size);
		} finally {
			cursor.close();
		}
		return records;
	}

	public void update(String sql, Object[] params) {
		connect.db().execSQL(sql, params);
	}

}
