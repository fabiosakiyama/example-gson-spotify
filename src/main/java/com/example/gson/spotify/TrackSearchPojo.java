package com.example.gson.spotify;

import java.util.List;

public class TrackSearchPojo {

	private TrackItemsPojo tracks;
	
	public TrackItemsPojo getTracks() {
		return this.tracks;
	}

	public void setTracks(TrackItemsPojo tracks) {
		this.tracks = tracks;
	}
}

class TrackItemsPojo {
	
	private List<TrackPojo> items;
	
	public List<TrackPojo> getItems() {
		return this.items;
	}

	public void setItems(List<TrackPojo> items) {
		this.items = items;
	}
	
	
}