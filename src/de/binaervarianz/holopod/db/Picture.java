package de.binaervarianz.holopod.db;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

public class Picture implements Serializable {
	private static final long serialVersionUID = -7748564964039385615L;

	long _id = 0;
	byte[] picture;
	byte[] hash;

	// Constructor
	public Picture() {

	}

	// Constructor
	public Picture(long id, Bitmap picture, byte[] hash) {
		this._id = id;
		this.picture = toByteArray(picture);
		this.hash = hash;
	}

	public Picture(long id, byte[] picture, byte[] hash) {
		this._id = id;
		this.picture = picture;
		this.hash = hash;
	}

	public Picture(long id, Bitmap picture) {
		this._id = id;
		this.picture = toByteArray(picture);
		this.hash = hashPicture(this.picture);
	}

	public Picture(long id, byte[] picture) {
		this._id = id;
		this.picture = picture;
		this.hash = hashPicture(this.picture);
	}
	
	public Picture(String url) throws ClientProtocolException, IOException {
		setPicture(url);
	}
	
	public Picture(Bitmap picture) {
		setPicture(picture);
	}

	public long getId() {
		return this._id;
	}

	public void setId(long id) {
		this._id = id;
	}

	public Bitmap getPicture() {
		return toBitmap();
	}

	public void setPicture(Bitmap picture) {
		this.picture = toByteArray(picture);
		this.hash = hashPicture(this.picture);
	}

	public void setPicture(byte[] picture) {
		this.picture = picture;
		this.hash = hashPicture(this.picture);
	}

	public void setPicture(String url) throws ClientProtocolException,
			IOException {
		DefaultHttpClient mHttpClient = new DefaultHttpClient();
		HttpGet mHttpGet = new HttpGet(url);
		HttpResponse mHttpResponse = mHttpClient.execute(mHttpGet);
		if (mHttpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
			HttpEntity entity = mHttpResponse.getEntity();
			if (entity != null) {
				this.picture = EntityUtils.toByteArray(entity);
				Log.i("Picture", url);
			}
		}

	}

	public byte[] getHash() {
		return this.picture;
	}

	public byte[] toByteArray(Bitmap picture) {
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		picture.compress(Bitmap.CompressFormat.PNG, 0, stream);
		return stream.toByteArray();
	}

	public byte[] toByteArray() {
		return this.picture;
	}

	public Bitmap toBitmap(byte[] picture) {
		return BitmapFactory.decodeByteArray(picture, 0, picture.length);
	}

	public Bitmap toBitmap() {
		return toBitmap(this.picture);
	}

	private byte[] hashPicture(byte[] picture) {
		MessageDigest md;
		try {
			md = MessageDigest.getInstance("SHA-1");
			md.update(picture);
			return md.digest();
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
}
