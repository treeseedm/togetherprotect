package org.x.db;

import android.support.v4.content.AsyncTaskLoader;
import android.content.Context;
import android.database.Cursor;

abstract public class CursorLoader extends AsyncTaskLoader<Cursor> {
	abstract protected Cursor buildCursor();

	Cursor lastCursor = null;

	public CursorLoader(Context context) {
		super(context);
	}

	@Override
	public Cursor loadInBackground() {
		Cursor cursor = buildCursor();
		if (cursor != null) {
			cursor.getCount();
		}
		return (cursor);
	}

	@Override
	public void deliverResult(Cursor cursor) {
		if (isReset()) {
			if (cursor != null) {
				cursor.close();
			}
			return;
		}
		Cursor oldCursor = lastCursor;
		lastCursor = cursor;
		if (isStarted()) {
			super.deliverResult(cursor);
		}
		if (oldCursor != null && oldCursor != cursor && !oldCursor.isClosed()) {
			oldCursor.close();
		}
	}

	@Override
	protected void onStartLoading() {
		if (lastCursor != null) {
			deliverResult(lastCursor);
		}
		if (takeContentChanged() || lastCursor == null) {
			forceLoad();
		}
	}

	@Override
	protected void onStopLoading() {
		// Attempt to cancel the current load task if possible.
		cancelLoad();
	}

	@Override
	public void onCanceled(Cursor cursor) {
		if (cursor != null && !cursor.isClosed()) {
			cursor.close();
		}
	}

	@Override
	protected void onReset() {
		super.onReset();
		onStopLoading();
		if (lastCursor != null && !lastCursor.isClosed()) {
			lastCursor.close();
		}
		lastCursor = null;
	}
}
