package de.binaervarianz.holopod.db;

import java.io.Serializable;

public class Channel implements Serializable {
	private static final long serialVersionUID = -4012775285807608513L;

	long _id = 0;
	String url;
	String title = "";
	String subtitle;
	String description;
	String link;
	long image; // key to picture.db picture._id
	String author;
	String copyright;
	String lastupdated;

	// Constructor
	public Channel() {

	}

	// Constructor
	public Channel(long id, String url, String title, String subtitle,
			String description, String link, long image, String author,
			String copyright, String lastupdated) {
		this._id = id;
		this.url = url;
		this.title = title;
		this.subtitle = subtitle;
		this.description = description;
		this.link = link;
		this.image = image;
		this.author = author;
		this.copyright = copyright;
		this.lastupdated = lastupdated;

	}

	// Constructor
	public Channel(String url, String title, String subtitle,
			String description, String link, long image, String author,
			String copyright, String lastupdated) {
		this.url = url;
		this.title = title;
		this.subtitle = subtitle;
		this.description = description;
		this.link = link;
		this.image = image;
		this.author = author;
		this.copyright = copyright;
		this.lastupdated = lastupdated;
	}

	// Constructor
	public Channel(long id, String url, String title) {
		this._id = id;
		this.url = url;
		this.title = title;
	}

	// Constructor
	public Channel(String url, String title) {
		this.url = url;
		this.title = title;
	}

	// Constructor
	public Channel(long id, String url) {
		this._id = id;
		this.url = url;
	}

	// Constructor
	public Channel(String url) {
		this.url = url;
	}

	// public methods
	public long getId() {
		return this._id;
	}

	public void setId(long id) {
		this._id = id;
	}

	public String getUrl() {
		return this.url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getTitle() {
		return this.title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String toString() {
		return (this.title.isEmpty()) ? getUrl() : getTitle();
	}

	public String getSubtitle() {
		return this.subtitle;
	}

	public void setSubtitle(String subtitle) {
		this.subtitle = subtitle;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getLink() {
		return this.link;
	}

	public void setLink(String link) {
		this.link = link;
	}

	public long getImage() {
		return this.image;
	}

	public void setImage(long image_key) {
		this.image = image_key;
	}

	public String getAuthor() {
		return this.author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getCopyright() {
		return this.copyright;
	}

	public void setCopyright(String copyright) {
		this.copyright = copyright;
	}
	
	public String getLastupdated() {
		return this.lastupdated;
	}

	public void setLastupdated(String lastupdated) {
		this.lastupdated = lastupdated;
	}
}
