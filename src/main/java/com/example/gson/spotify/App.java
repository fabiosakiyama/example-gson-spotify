package com.example.gson.spotify;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;
import java.util.Properties;

import javax.naming.NamingException;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

import com.google.gson.Gson;

/**
 * Hello world!
 *
 */
public class App {

	private static String mostPopularSongName;

	private static int popularity;

	public static void main(String[] args) throws ClientProtocolException, IOException, NamingException {
		// Parameters
		String artistName = "Foo Fighters";

		Properties properties = new Properties();
		properties.load(new FileInputStream("src/spotify.properties"));
		String clientId = properties.getProperty("clientid");
		String clientSecretKey = properties.getProperty("clientsecretkey");
		Gson gson = new Gson();
		HttpClient httpClient = HttpClientBuilder.create().build();

		// Trying to get token
		// getToken(httpClient);

		// Given the artist name, finds it's spotify id
		String artistId = getArtistID(artistName, gson, httpClient);

		// Now that we have the artist spotify id, we can get his albums
		List<AlbumPojo> items = getArtistAlbums(artistId, gson, httpClient);

		// For each album found, we'll search for it's songs, and for each song,
		// we can check it's popularity to find the most popular song.
		for (AlbumPojo albumPojo : items) {
			String albumName = albumPojo.getName();
			System.out.println("Album: " + albumName);
			List<TrackPojo> tracks = getAlbumTracks(albumName, gson, httpClient);
			for (TrackPojo trackPojo : tracks) {
				checkMostPopular(trackPojo);
				System.out.println(trackPojo);
			}
			System.out.println("\n\n");
			System.out.println("------------------------------------------------------------");
		}

		System.out.println(artistName);
		System.out.println("Most popular song on spotify: " + mostPopularSongName);
		System.out.println("Popularity: " + popularity);
	}

	private static List<TrackPojo> getAlbumTracks(String albumName, Gson gson, HttpClient httpClient) throws ClientProtocolException, IOException {
		String urlEncoded = URLEncoder.encode(":" + albumName, "UTF-8");
		HttpGet httpGet = new HttpGet("https://api.spotify.com/v1/search?q=album" + urlEncoded + "&type=track");
		HttpResponse response = httpClient.execute(httpGet);
		String trackJson = EntityUtils.toString(response.getEntity(), "UTF-8");
		TrackSearchPojo trackSearchPojo = gson.fromJson(trackJson, TrackSearchPojo.class);
		return trackSearchPojo.getTracks().getItems();
	}

	private static void getToken(String clientId, String clientSecretKey, HttpClient httpClient) throws UnsupportedEncodingException, IOException, ClientProtocolException {
		// TODO ... Still not working
		HttpPost httpPost = new HttpPost("https://accounts.spotify.com/api/token");
		httpPost.addHeader("Authorization", "Basic " + clientId + ':' + clientSecretKey);
		StringEntity entity = new StringEntity("{\"grant_type\":\"client_credentials\"}");
		httpPost.setEntity(entity);
		HttpResponse post = httpClient.execute(httpPost);
		System.out.println(post);
	}

	private static List<AlbumPojo> getArtistAlbums(String artistId, Gson gson, HttpClient httpClient) throws ClientProtocolException, IOException {
		HttpGet httpGet = new HttpGet("https://api.spotify.com/v1/artists/" + artistId + "/albums");
		System.out.println("Executing GET request : " + httpGet.getURI());
		HttpResponse albumResponse = httpClient.execute(httpGet);
		String json = EntityUtils.toString(albumResponse.getEntity(), "UTF-8");
		System.out.println("-----------------------------------------------------");
		System.out.println(json);
		System.out.println("-----------------------------------------------------");
		AlbumsSearchPojo albumsSearchPojo = gson.fromJson(json, AlbumsSearchPojo.class);
		System.out.println(albumsSearchPojo);
		return albumsSearchPojo.getItems();
	}

	private static String getArtistID(String artistName, Gson gson, HttpClient httpClient) throws IOException, ClientProtocolException {
		String artistNameEncoded = URLEncoder.encode(artistName, "UTF-8");
		HttpGet httpGet = new HttpGet("https://api.spotify.com/v1/search?q=" + artistNameEncoded + "&type=artist&limit=1");
		System.out.println("Executing GET request : " + httpGet.getURI());
		HttpResponse getArtistReponse = httpClient.execute(httpGet);
		String searchArtistJson = EntityUtils.toString(getArtistReponse.getEntity(), "UTF-8");
		ArtistSearchPojo artistSearchPojo = gson.fromJson(searchArtistJson, ArtistSearchPojo.class);
		List<ArtistPojo> items = artistSearchPojo.getArtists().getItems();
		if (items.size() != 1) {
			throw new RuntimeException("Search artist ID is supposed to return only 1 result...");
		}
		return items.get(0).getId();
	}

	private static void checkMostPopular(TrackPojo trackPojo) {
		if (trackPojo.getPopularity() > popularity) {
			popularity = trackPojo.getPopularity();
			mostPopularSongName = trackPojo.getName();
		}
	}
}
