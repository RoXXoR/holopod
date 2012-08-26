package de.binaervarianz.holopod;

import java.util.HashMap;

import de.binaervarianz.holopod.db.DatabaseHandler;
import de.binaervarianz.holopod.db.Episode;

import android.app.DownloadManager;
import android.app.Service;
import android.app.DownloadManager.Query;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.os.IBinder;
import android.util.Log;

public class ReceiverService extends Service {

	HashMap<Long, Episode> downloadQueue = new HashMap<Long, Episode>();
	private DownloadManager dm;
	private DatabaseHandler db;

	public ReceiverService() {
	}

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		registerReceiver(receiver, new IntentFilter(
				DownloadManager.ACTION_DOWNLOAD_COMPLETE));
		dm = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
		db = new DatabaseHandler(this);
	}

	public int onStartCommand(Intent intent, int flags, int startId) {
		Episode episode = (Episode) intent.getSerializableExtra("Episode");
		downloadQueue.put(episode.getDownloadId(), episode);
		return START_STICKY;
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		downloadQueue.clear();
		unregisterReceiver(receiver);
	}

	private BroadcastReceiver receiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			long downloadId;
			if (DownloadManager.ACTION_DOWNLOAD_COMPLETE.equals(action)) {
				downloadId = intent.getLongExtra(
						DownloadManager.EXTRA_DOWNLOAD_ID, 0);
				Query query = new Query();
				query.setFilterById(downloadId);
				
				Cursor cursor = dm.query(query); 
				if (cursor.moveToFirst()) { 
					int	columnIndex = cursor.getColumnIndex(DownloadManager.COLUMN_STATUS); 
					if (DownloadManager.STATUS_SUCCESSFUL == cursor.getInt(columnIndex))	{				 
						String localUri = cursor.getString(cursor.getColumnIndex(DownloadManager.COLUMN_LOCAL_URI));
						Log.i("Rcvr", localUri);
						Episode episode = (Episode) downloadQueue.get(downloadId);
						episode.setEncFilepath(localUri);
						episode.setEncOnDevice(true);
						
						db.updateEpisode(episode);
						downloadQueue.remove(downloadId);
					}
				}
				if (downloadQueue.isEmpty()) {
					stopSelf();
				}
			}
		}
	};
}
