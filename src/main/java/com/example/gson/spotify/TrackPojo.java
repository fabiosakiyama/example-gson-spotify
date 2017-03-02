package com.example.gson.spotify;

public class TrackPojo {

	private String id;

	private String name;

	private int track_number;

	private int popularity;

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getTrack_number() {
		return this.track_number;
	}

	public void setTrack_number(int track_number) {
		this.track_number = track_number;
	}

	public int getPopularity() {
		return this.popularity;
	}

	public void setPopularity(int popularity) {
		this.popularity = popularity;
	}

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();

		sb.append("\n");
		sb.append("Track name: " + name);
		sb.append("\n");
		sb.append("Track number: " + track_number);
		sb.append("\n");
		sb.append("Popularity: " + popularity);

		return sb.toString();
	}

}