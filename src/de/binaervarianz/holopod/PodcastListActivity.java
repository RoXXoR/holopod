package de.binaervarianz.holopod;

import de.binaervarianz.holopod.db.Channel;
import de.binaervarianz.holopod.db.DatabaseHandler;

import android.os.Bundle;
import android.app.ListActivity;
import android.content.Intent;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.MenuItem.OnActionExpandListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class PodcastListActivity extends ListActivity {
	private static final String TAG = "HoloPod";
	ListView podcasts;
	DatabaseHandler db;
	ArrayAdapter<Channel> adapter;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		db = new DatabaseHandler(this);
		podcasts = (ListView) findViewById(R.id.listViewPodcasts);
	}

	public void onStart() {
		super.onStart();
		adapter = new ArrayAdapter<Channel>(this,
				android.R.layout.simple_list_item_1, db.getSubscriptions());
		setListAdapter(adapter);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.podcast_list, menu);
		MenuItem AddPodcastActionItem = menu.findItem(R.id.AddPodcast);
		AddPodcastActionItem
				.setOnActionExpandListener(new OnActionExpandListener() {
					@Override
					public boolean onMenuItemActionExpand(MenuItem item) {
						EditText add_input = (EditText) item.getActionView();
						add_input.setText("http://cre.fm/");
						add_input.requestFocus();
						add_input.setOnEditorActionListener(onDonePressed);
						return true;
					}

					@Override
					public boolean onMenuItemActionCollapse(MenuItem item) {
						return true; // Return true to expand action view
					}
				});
		return super.onCreateOptionsMenu(menu);
	}

	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.RefreshPodcast:
			Toast.makeText(this, "MenuItem " + item.getTitle() + " selected.",
					Toast.LENGTH_SHORT).show();
			adapter.notifyDataSetChanged();
			return true;

		case R.id.menu_settings:
			startActivity(new Intent(PodcastListActivity.this,
					SettingsActivity.class));

			return true;

		default:
			return super.onOptionsItemSelected(item);
		}
	}

	private TextView.OnEditorActionListener onDonePressed = new TextView.OnEditorActionListener() {
		public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
			if (event == null || event.getAction() == KeyEvent.ACTION_UP) {
				Intent AddPodcastIntent = new Intent(PodcastListActivity.this,
						PodcastAddActivity.class);
				AddPodcastIntent.putExtra("URL", v.getText().toString());
				startActivity(AddPodcastIntent);
			}
			return true;
		}
	};

	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		Channel channel = (Channel) adapter.getItem(position);
		Intent goDetails = new Intent(getApplicationContext(),
				PodcastDetailsActivity.class);
		goDetails.putExtra("Podcast", channel);
		startActivity(goDetails);
	}
}
