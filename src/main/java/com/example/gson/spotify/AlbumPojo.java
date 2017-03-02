package com.example.gson.spotify;

import java.util.ArrayList;
import java.util.List;

public class AlbumPojo {

	private String album_type;
	
	private List<ArtistPojo> artists = new ArrayList<>();
	
	private String name;
	
	private String type;

	public String getAlbum_type() {
		return this.album_type;
	}

	public void setAlbum_type(String album_type) {
		this.album_type = album_type;
	}

	public List<ArtistPojo> getArtists() {
		return this.artists;
	}

	public void setArtists(List<ArtistPojo> artists) {
		this.artists = artists;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return this.type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("\n");
		sb.append("Album name: " + getName());
		sb.append("\n");
		return sb.toString();
	}
}
