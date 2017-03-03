package com.example.gson.spotify;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.naming.NamingException;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import sun.misc.BASE64Encoder;

/**
 * Hello world!
 *
 */
public class AppWithoutSpotifyJavaAPI {

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
		getToken(clientId, clientSecretKey, httpClient, gson);

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

	private static void getToken(String clientId, String clientSecretKey, HttpClient httpClient, Gson gson) throws UnsupportedEncodingException, IOException, ClientProtocolException {
		BASE64Encoder base64Encoder = new BASE64Encoder();

		String encodedClientIdKey = base64Encoder.encode((clientId + ":" + clientSecretKey).getBytes());
		HttpPost httpPostRequest = new HttpPost("https://accounts.spotify.com/api/token");
		httpPostRequest.setHeader("Content-Type", "application/x-www-form-urlencoded");
		httpPostRequest.addHeader("Authorization", "Basic " + encodedClientIdKey);

		List<NameValuePair> nameValuePairs = new ArrayList<>();
		nameValuePairs.add(new BasicNameValuePair("grant_type", "client_credentials"));
		nameValuePairs.add(new BasicNameValuePair("Content-Type", "application/x-www-form-urlencoded"));
		
//		JsonObject json = new JsonObject();
//		json.addProperty("grant_type", "client_credentials");

//		StringEntity stringEntity = new StringEntity(json.toString(), ContentType.APPLICATION_FORM_URLENCODED);
//		httpPostRequest.setEntity(stringEntity);
		httpPostRequest.setEntity(new UrlEncodedFormEntity(nameValuePairs));

		HttpResponse post = httpClient.execute(httpPostRequest);
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
