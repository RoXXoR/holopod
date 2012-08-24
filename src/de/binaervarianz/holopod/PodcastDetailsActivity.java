package de.binaervarianz.holopod;

import de.binaervarianz.holopod.db.Channel;
import de.binaervarianz.holopod.db.DatabaseHandler;
import de.binaervarianz.holopod.db.Episode;
import de.binaervarianz.holopod.db.Picture;
import de.binaervarianz.holopod.db.PictureHandler;
import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RectShape;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Space;
import android.widget.TextView;

public class PodcastDetailsActivity extends Activity {
	Channel podcast;
	DatabaseHandler db;
	ArrayAdapter<Episode> adapter;

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
		PictureHandler pic_db = new PictureHandler(this);
		Bitmap channelImage = pic_db.getPicture(podcast.getImage()).toBitmap();

		View banner = (View) findViewById(R.id.podcast_detail_banner);
		banner.setBackgroundColor(channelImage.getPixel(5,
				channelImage.getHeight() / 2));

		ImageView image = (ImageView) findViewById(R.id.podcast_detail_image);
		image.setImageBitmap(channelImage);
		TextView title = (TextView) findViewById(R.id.podcast_detail_title);
		title.setText(podcast.getSubtitle());
		TextView subtitle = (TextView) findViewById(R.id.podcast_detail_subtitle);
		subtitle.setText(podcast.getSubtitle());
		TextView link = (TextView) findViewById(R.id.podcast_detail_link);
		link.setText(podcast.getLink());
		TextView description = (TextView) findViewById(R.id.podcast_detail_description);
		description.setText(podcast.getDescription());
		TextView author = (TextView) findViewById(R.id.podcast_detail_author);
		author.setText("by " + podcast.getAuthor());
		TextView copyright = (TextView) findViewById(R.id.podcast_detail_copyright);
		copyright.setText(podcast.getCopyright());

		ListView episodeList = (ListView) findViewById(R.id.podcast_detail_episode_listView);
		adapter = new ArrayAdapter<Episode>(this,
				android.R.layout.simple_list_item_1,
				db.getEpisodesByChannel(podcast));
		episodeList.setAdapter(adapter);
		episodeList.setClickable(true);
		episodeList
				.setOnItemClickListener(new AdapterView.OnItemClickListener() {
					@Override
					public void onItemClick(AdapterView<?> l, View v,
							int position, long id) {
						Episode listItem = (Episode) adapter.getItem(position);
						Intent goDetails = new Intent(getApplicationContext(),
								EpisodeDetailsActivity.class);
						goDetails.putExtra("Episode", listItem);
						startActivity(goDetails);
					}
				});

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
					.setTitle(podcast.getTitle())
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
