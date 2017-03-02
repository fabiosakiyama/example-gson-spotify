package com.example.gson.spotify;

public class ArtistPojo {

	private String href;

	private String id;

	private String name;

	private String type;

	private String uri;

	public String getHref() {
		return this.href;
	}

	public void setHref(String href) {
		this.href = href;
	}

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
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

	public String getUri() {
		return this.uri;
	}

	public void setUri(String uri) {
		this.uri = uri;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(getHref());
		sb.append("\n");
		sb.append(getId());
		sb.append("\n");
		sb.append(getName());
		sb.append("\n");
		sb.append(getType());
		sb.append("\n");
		sb.append(getUri());
		sb.append("\n");
		return sb.toString();
	}
}
