package de.binaervarianz.holopod.db;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHandler extends SQLiteOpenHelper {

	// Database Version
	private static final int DATABASE_VERSION = 2;

	// Database Name
	private static final String DATABASE_NAME = "podcast.db";

	// Tables
	private static final String TABLE_CHANNEL = "channel";
	private static final String TABLE_EPISODE = "episode";

	// Columns
	private static final String CHANNEL_ID = "_id";
	private static final String CHANNEL_URL = "url";
	private static final String CHANNEL_TITLE = "title";
	private static final String CHANNEL_SUBTITLE = "subtitle";
	private static final String CHANNEL_DESCRIPTION = "description";
	private static final String CHANNEL_LINK = "link";
	private static final String CHANNEL_IMAGE = "image";
	private static final String CHANNEL_AUTHOR = "author";
	private static final String CHANNEL_COPYRIGHT = "copyright";
	private static final String CHANNEL_LASTUPDATED = "lastupdated";

	private static final String[] CHANNEL_FIELDS = { CHANNEL_ID, CHANNEL_URL,
			CHANNEL_TITLE };

	public DatabaseHandler(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// @formatter:off
		String CREATE_CHANNEL_TABLE = "CREATE TABLE " + TABLE_CHANNEL + " ("
				+ CHANNEL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
				+ CHANNEL_URL + " TEXT NOT NULL,"
				+ CHANNEL_TITLE + " TEXT,"
				+ CHANNEL_SUBTITLE + " TEXT,"
				+ CHANNEL_DESCRIPTION + " TEXT,"
				+ CHANNEL_LINK + " TEXT,"
				+ CHANNEL_IMAGE + " TEXT,"
				+ CHANNEL_AUTHOR + " TEXT,"
				+ CHANNEL_COPYRIGHT + " TEXT,"
				+ CHANNEL_LASTUPDATED + " TEXT"
				+ ")";
		// @formatter:on
		db.execSQL(CREATE_CHANNEL_TABLE);

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_CHANNEL);
		onCreate(db);

	}

	// Add Channel (subscribe)
	public Boolean addChannel(Channel channel) {
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(CHANNEL_TITLE, channel.getTitle());
		values.put(CHANNEL_URL, channel.getUrl());
		values.put(CHANNEL_SUBTITLE, channel.getSubtitle());
		// TODO check if URL is already present in database
		long result = db.insert(TABLE_CHANNEL, null, values);
		if (result > 0) {
			channel.setId(result);
		}
		db.close();

		// TODO return false if already in database
		return true;
	}

	public Channel getChannel(int id) {
		SQLiteDatabase db = this.getReadableDatabase();

		Cursor result = db.query(TABLE_CHANNEL, CHANNEL_FIELDS, CHANNEL_ID
				+ "=?", new String[] { String.valueOf(id) }, null, null, null,
				"1");

		if (result != null) {
			result.moveToFirst();
			return new Channel(result.getInt(0), result.getString(1),
					result.getString(2));
		} else {
			return null; // no channel with this id found in database
		}
	}

	public Channel getChannel(String url) {
		SQLiteDatabase db = this.getReadableDatabase();

		Cursor result = db.query(TABLE_CHANNEL, CHANNEL_FIELDS, CHANNEL_URL
				+ "=?", new String[] { String.valueOf(url) }, null, null, null,
				"1");
		if (result != null) {
			result.moveToFirst();
			return new Channel(result.getInt(0), result.getString(1),
					result.getString(2));
		} else {
			return null; // no channel with this id found in database
		}
	}

	// get all channels in database
	public List<Channel> getSubscriptions() {
		List<Channel> channelList = new ArrayList<Channel>();
		SQLiteDatabase db = this.getReadableDatabase();

		Cursor result = db.rawQuery("SELECT * FROM " + TABLE_CHANNEL, null);
		// create channel list from results
		if (result.moveToFirst()) {
			do {
				Channel channel = new Channel(result.getInt(0),
						result.getString(1), result.getString(2));
				channelList.add(channel);
			} while (result.moveToNext());
		}
		db.close();
		return channelList;
	}

	// remove channel
	public Boolean removeChannel(Channel channel) {
		SQLiteDatabase db = this.getWritableDatabase();
		int rmCnt = db.delete(TABLE_CHANNEL, CHANNEL_ID + " = ?",
				new String[] { String.valueOf(channel.getId()) });
		db.close();
		return (rmCnt > 0) ? true : false;
	}
}
