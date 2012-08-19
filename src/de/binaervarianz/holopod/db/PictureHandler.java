package de.binaervarianz.holopod.db;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase.CursorFactory;

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
//			PICTURES_BLOB, PICTURES_HASH };
			PICTURES_BLOB };

	public PictureHandler(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// @formatter:off
		String CREATE_PICTURES_TABLE = "CREATE TABLE " + TABLE_PICTURES + " ("
				+ PICTURES_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
				+ PICTURES_BLOB + " BLOB NOT NULL,"
//				+ PICTURES_HASH + " BLOB UNIQUE,"
				+ ")";
		// @formatter:on
		db.execSQL(CREATE_PICTURES_TABLE);

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub

	}

}
