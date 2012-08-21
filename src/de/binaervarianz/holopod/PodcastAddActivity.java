package de.binaervarianz.holopod;

import java.io.IOException;
import java.util.ArrayList;

import org.apache.http.client.ClientProtocolException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.jsoup.select.Elements;

import de.binaervarianz.holopod.db.Channel;
import de.binaervarianz.holopod.db.DatabaseHandler;
import de.binaervarianz.holopod.db.Picture;
import de.binaervarianz.holopod.db.PictureHandler;

import android.app.ActionBar;
import android.app.ListActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class PodcastAddActivity extends ListActivity {

	DatabaseHandler db;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);

		ActionBar actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);

		new GetFeedsTask().execute(getIntent().getExtras().getString("URL"));
		db = new DatabaseHandler(this);

	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			Intent goHome = new Intent(this, PodcastListActivity.class);
			goHome.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(goHome);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}

	}

	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		String url = ((TextView) v).getText().toString();
		new AddChannelTask().execute(url);
	}

	public class GetFeedsTask extends
			AsyncTask<String, Void, ArrayList<String>> {

		@Override
		protected ArrayList<String> doInBackground(String... urls) {
			SharedPreferences settings = PreferenceManager
					.getDefaultSharedPreferences(PodcastAddActivity.this);

			ArrayList<String> feeds = new ArrayList<String>();
			ArrayList<String> podcastFeeds = new ArrayList<String>();
			try {
				Document mainpage = Jsoup.connect(urls[0]).get();
				Elements imports = mainpage.select("link[href]");
				for (Element link : imports) {
					if (link.attr("type").equalsIgnoreCase(
							"application/rss+xml")) {
						feeds.add(link.attr("abs:href"));
					}
				}

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if (settings.getBoolean(SettingsFragment.SETTING_FILTER_PODCASTS,
					true)) {
				if (!feeds.isEmpty()) {
					for (String feed : feeds) {
						try {
							Document feedSource = Jsoup.connect(feed)
									.ignoreContentType(true)
									.parser(Parser.xmlParser()).get();
							if (!feedSource.getElementsByTag("enclosure")
									.isEmpty()) {
								podcastFeeds.add(feed);
							}

						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
				return podcastFeeds;
			} else {
				return feeds;
			}

		}

		@Override
		protected void onPreExecute() {
			PodcastAddActivity.this
					.setProgressBarIndeterminateVisibility(Boolean.TRUE);
		}

		@Override
		protected void onPostExecute(ArrayList<String> feeds) {
			setListAdapter(new ArrayAdapter<String>(PodcastAddActivity.this,
					android.R.layout.simple_list_item_1, feeds));
			PodcastAddActivity.this
					.setProgressBarIndeterminateVisibility(Boolean.FALSE);
		}

	}

	public class AddChannelTask extends AsyncTask<String, Void, Channel> {

		@Override
		protected Channel doInBackground(String... urls) {
			Channel channel = new Channel(urls[0]);
			try {
				Document feedSource = Jsoup.connect(urls[0])
						.ignoreContentType(true).parser(Parser.xmlParser())
						.get();
				if (!feedSource.select("channel title").isEmpty()) {
					channel.setTitle(feedSource.select("channel title").first()
							.text());
				}
				if (!feedSource.select("channel itunes|subtitle").isEmpty()) {
					channel.setSubtitle(feedSource
							.select("channel itunes|subtitle").first().text());
				}
				if (!feedSource.select("channel itunes|image").isEmpty()) {

					Picture channel_image = new Picture(feedSource
							.select("channel itunes|image").first()
							.attr("href"));
					PictureHandler pic_db = new PictureHandler(
							getApplicationContext());
					channel.setImage(pic_db.addPicture(channel_image));
				}
				if (!feedSource.select("channel description").isEmpty()) {
					channel.setDescription(feedSource
							.select("channel description").first().text());
				} else {
					if (!feedSource.select("channel itunes|summary").isEmpty()) {
						channel.setDescription(feedSource
								.select("channel itunes|summary").first()
								.text());
					}
				}
				if (!feedSource.select("channel itunes|author").isEmpty()) {
					channel.setAuthor(feedSource
							.select("channel itunes|author").first().text());
				}
				if (!feedSource.select("channel copyright").isEmpty()) {
					channel.setCopyright(feedSource.select("channel copyright")
							.first().text());
				}

				if (db.addChannel(channel)) {
				}

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return channel;
		}

		@Override
		protected void onPreExecute() {
			PodcastAddActivity.this
					.setProgressBarIndeterminateVisibility(Boolean.TRUE);
		}

		@Override
		protected void onPostExecute(Channel channel) {
			PodcastAddActivity.this
					.setProgressBarIndeterminateVisibility(Boolean.FALSE);
			if (channel.getId() > 0) {
				Intent goDetails = new Intent(getApplicationContext(),
						PodcastDetailsActivity.class);
				goDetails.putExtra("Podcast", channel);
				startActivity(goDetails);
				finish();
			} else {
				// TODO add warning that add has failed
			}
		}

	}
}
