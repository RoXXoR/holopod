package de.binaervarianz.holopod.db;


import java.io.Serializable;


public class Episode implements Serializable {
	private static final long serialVersionUID = -7311381002303717513L;

	long _id = 0;
	long channel;
	String title;
	String subtitle;
	String description;
	String author;
	String keywords;
	String link;
	String enc_url;
	String enc_size;		// tag:length
	String enc_rcvsize;		// downloaded bytes
	String enc_duration;
	String enc_type;
	String enc_filepath;
	String enc_onDevice;
	String enc_playTime;
	String pubDate;
	String archive;			// not available in rss-feed anymore but on device
	
	
	public Episode() {
		
	}

}
