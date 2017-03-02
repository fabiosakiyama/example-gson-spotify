package com.example.gson.spotify;

import java.util.List;

public class ArtistSearchPojo {

	private ArtistItemsPojo artists;

	public ArtistItemsPojo getArtists() {
		return this.artists;
	}

	public void setArtists(ArtistItemsPojo artists) {
		this.artists = artists;
	}

}

class ArtistItemsPojo {

	private List<ArtistPojo> items;

	public List<ArtistPojo> getItems() {
		return this.items;
	}

	public void setItems(List<ArtistPojo> items) {
		this.items = items;
	}

}
