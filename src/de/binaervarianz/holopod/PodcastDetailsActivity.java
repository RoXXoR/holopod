package de.binaervarianz.holopod;

import de.binaervarianz.holopod.db.Channel;
import de.binaervarianz.holopod.db.DatabaseHandler;
import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

public class PodcastDetailsActivity extends Activity {
	Channel podcast;
	DatabaseHandler db;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.podcast_details);
		podcast = (Channel) getIntent().getSerializableExtra("Podcast");
		db = new DatabaseHandler(this);
		ActionBar actionBar = getActionBar();
		if (actionBar != null) {
			actionBar.setDisplayOptions(ActionBar.DISPLAY_HOME_AS_UP
					| ActionBar.DISPLAY_SHOW_TITLE);
			actionBar.setTitle(podcast.toString());
		}
		
		TextView title = (TextView) findViewById(R.id.podcast_detail_title);
		title.setText(podcast.getTitle());
		TextView subtitle = (TextView) findViewById(R.id.podcast_detail_subtitle);
		subtitle.setText(podcast.getUrl());
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.podcast_details, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			Intent goHome = new Intent(this, PodcastListActivity.class);
			goHome.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(goHome);
			return true;

		case R.id.menu_unsubsribe:
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setCancelable(false)
					.setMessage(R.string.altert_msg_unsubscribe)
					.setPositiveButton(R.string.altert_ok_unsubscribe,
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int id) {
									db.removeChannel(podcast);
									finish();
								}
							})
					.setNegativeButton(R.string.altert_no_unsubscribe,
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int id) {
									dialog.cancel();
								}
							});
			builder.create();
			builder.show();
			return true;
		case R.id.menu_settings:
			startActivity(new Intent(getApplicationContext(),
					SettingsActivity.class));

			return true;
		default:
			return super.onOptionsItemSelected(item);
		}

	}

}
