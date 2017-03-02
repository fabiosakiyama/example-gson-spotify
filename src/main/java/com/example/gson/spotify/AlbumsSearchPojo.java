package com.example.gson.spotify;

import java.util.List;

import java.util.ArrayList;

public class AlbumsSearchPojo {

	private List<AlbumPojo> items = new ArrayList<>();

	public List<AlbumPojo> getItems() {
		return this.items;
	}

	public void setItems(List<AlbumPojo> items) {
		this.items = items;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for (AlbumPojo albumPojo : items) {
			sb.append(albumPojo);
		}
		return sb.toString();
	}
}
