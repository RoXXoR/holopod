package de.binaervarianz.holopod.db;

import java.io.Serializable;

public class Channel implements Serializable{
	private static final long serialVersionUID = -4012775285807608513L;

	int _id;
	String url;
	String title = "";
	String subtitle;
	String description;
	String link;
	String image;
	String author;
	String copyright;
	String lastupdated;

	// Constructor
	public Channel() {

	}

	// Constructor
	public Channel(int id, String url, String title, String subtitle,
			String description, String link, String image, String author,
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
			String description, String link, String image, String author,
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
	public Channel(int id, String url, String title) {
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
	public Channel(int id, String url) {
		this._id = id;
		this.url = url;
	}

	// Constructor
	public Channel(String url) {
		this.url = url;
	}

	// public methods
	public int getId() {
		return this._id;
	}

	public void setId(int id) {
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
}
