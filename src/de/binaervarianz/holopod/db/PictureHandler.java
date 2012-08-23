package de.binaervarianz.holopod.db;

import de.binaervarianz.holopod.R;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Log;
import android.webkit.WebView.FindListener;

public class PictureHandler extends SQLiteOpenHelper {

	// Database Version
	private static final int DATABASE_VERSION = 2;

	// Database Name
	// TODO check size of db and move to external storage if to big
	private static final String DATABASE_NAME = "pictures.db";

	// Tables
	private static final String TABLE_PICTURES = "pictures";

	// Columns
	private static final String PICTURES_ID = "_id";
	private static final String PICTURES_BLOB = "picture";
	private static final String PICTURES_HASH = "hash";

	private static final String[] PICTURE_FIELDS = { PICTURES_ID,
			PICTURES_BLOB, PICTURES_HASH };

	private Context context;
	
	public PictureHandler(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		this.context = context;
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// @formatter:off
		String CREATE_PICTURES_TABLE = "CREATE TABLE " + TABLE_PICTURES + " ("
				+ PICTURES_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
				+ PICTURES_BLOB + " BLOB NOT NULL,"
				+ PICTURES_HASH + " BLOB"
				// add again when check for duplicate pictures is working
//				+ PICTURES_HASH + " BLOB UNIQUE"
				+ ")";
		// @formatter:on
		db.execSQL(CREATE_PICTURES_TABLE);
		ContentValues values = new ContentValues();
		Picture picture = new Picture(BitmapFactory.decodeResource(this.context.getResources(), R.drawable.nopicture));
		values.put(PICTURES_ID, 0);
		values.put(PICTURES_BLOB, picture.toByteArray());
		values.put(PICTURES_HASH, picture.getHash());
		db.insert(TABLE_PICTURES, null, values);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_PICTURES);
		onCreate(db);
	}

	public long addPicture(Picture picture) {
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(PICTURES_BLOB, picture.toByteArray());
		values.put(PICTURES_HASH, picture.getHash());
		long result = db.insert(TABLE_PICTURES, null, values);
		if (result > 0) {
			picture.setId(result);
		}
		db.close();
		// TODO check with hash if picture already in db and return its id in
		// case
		return result;
	}

	public Picture getPicture(long id) {
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor result = db.query(TABLE_PICTURES, PICTURE_FIELDS, PICTURES_ID
				+ "=?", new String[] { String.valueOf(id) }, null, null, null,
				"1");
		if (!result.isAfterLast()) {
			result.moveToFirst();
			db.close();
			return new Picture(result.getLong(0), result.getBlob(1),
					result.getBlob(2));
		} else {
			db.close();
			Log.i("PicHandler", Long.toString(id));
			//return new Picture(id, BitmapFactory.decodeResource(this.context.getResources(), R.drawable.nopicture)); // no picture found with this id
			return null;
		}

	}

	public Boolean removePicture(Picture picture) {
		SQLiteDatabase db = this.getWritableDatabase();
		int rmCnt = db.delete(TABLE_PICTURES, PICTURES_ID + " = ?",
				new String[] { String.valueOf(picture.getId()) });
		db.close();
		return (rmCnt > 0) ? true : false;
	}
	
	public Boolean removePictureById(long id) {
		SQLiteDatabase db = this.getWritableDatabase();
		int rmCnt = db.delete(TABLE_PICTURES, PICTURES_ID + " = ?",
				new String[] { String.valueOf(id) });
		db.close();
		return (rmCnt > 0) ? true : false;
	}
}
