package com.example.gson.spotify;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Properties;

import javax.naming.NamingException;

import org.apache.http.client.ClientProtocolException;

import com.wrapper.spotify.Api;
import com.wrapper.spotify.exceptions.WebApiException;
import com.wrapper.spotify.methods.AlbumsForArtistRequest;
import com.wrapper.spotify.methods.ArtistSearchRequest;
import com.wrapper.spotify.methods.authentication.ClientCredentialsGrantRequest;
import com.wrapper.spotify.models.Artist;
import com.wrapper.spotify.models.AudioFeature;
import com.wrapper.spotify.models.Page;
import com.wrapper.spotify.models.SimpleAlbum;
import com.wrapper.spotify.models.SimpleTrack;

/**
 * Hello world!
 *
 */
public class AppWithSpotifyJavaAPI {

	private static String saddestSong;

	private static double lowestValence = 100;

	private static String saddestAlbum;

	private static double albumstWithLowestValence = 100;

	public static void main(String[] args) throws ClientProtocolException, IOException, NamingException, WebApiException {
		// Parameters
		String artistName = "Radiohead";

		Properties properties = new Properties();
		properties.load(new FileInputStream("src/spotify.properties"));
		String clientId = properties.getProperty("clientid");
		String clientSecretKey = properties.getProperty("clientsecretkey");

		final Api api = Api.builder().clientId(clientId).clientSecret(clientSecretKey).build();

		final ClientCredentialsGrantRequest request = api.clientCredentialsGrant().build();

		// Getting access token
		api.setAccessToken(request.get().getAccessToken());

		ArtistSearchRequest artistSearchRequest = api.searchArtists(artistName).limit(1).build();
		Artist artist = artistSearchRequest.get().getItems().get(0);

		AlbumsForArtistRequest albumsForArtistRequest = api.getAlbumsForArtist(artist.getId()).build();
		List<SimpleAlbum> items = albumsForArtistRequest.get().getItems();
		for (SimpleAlbum simpleAlbum : items) {
			System.out.println("Starting to analyze album: " + simpleAlbum.getName());
			double currentAlbumValence = 0;
			Page<SimpleTrack> page = api.getTracksForAlbum(simpleAlbum.getName()).build().get();
			List<SimpleTrack> tracks = page.getItems();
			for (SimpleTrack simpleTrack : tracks) {
				AudioFeature audioFeature = api.getAudioFeature(simpleTrack.getId()).build().get();
				double valence = audioFeature.getValence();
				if (lowestValence > valence) {
					lowestValence = valence;
					saddestSong = simpleTrack.getName();
				}
				currentAlbumValence += valence;
			}
			if (currentAlbumValence < albumstWithLowestValence) {
				albumstWithLowestValence = currentAlbumValence;
				saddestAlbum = simpleAlbum.getName();
			}
		}

		// Only based on spotify valence measure.
		System.out.println("Artist: " + artistName);
		System.out.println("Saddest Song: " + saddestSong);
		System.out.println("Song Valence: " + lowestValence);
		System.out.println("Saddest Album: " + saddestAlbum);
		System.out.println("Album valence: " + albumstWithLowestValence);

	}
}
