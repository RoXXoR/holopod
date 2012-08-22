package de.binaervarianz.holopod.db;


import java.io.Serializable;


public class Episode implements Serializable {
	private static final long serialVersionUID = -7311381002303717513L;

	long _id = 0;
	long channel;
	String title;
	String subtitle;
	String description;
	String link;
	long image;
	String author;
	String keywords;
	long lastupdated;
	
	String enc_url;
	long enc_size;			// tag:length
	long enc_rcvsize;		// downloaded bytes
	long enc_duration;
	String enc_type;
	String enc_filepath;
	Boolean enc_onDevice;	// file present on device?
	long enc_pausedTime;	// time in enclosure file where last paused 
	long enc_dlDate;		// timestamp when enclosure was downloaded completely
	long pubDate;
	Boolean archive;		// not available in rss-feed anymore but on device
	
	
	public Episode() {
		
	}

}
