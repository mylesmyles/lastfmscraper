package net.mylesputnam.lastfm.api.requests;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import net.mylesputnam.lastfm.api.exceptions.LastFmApiException;

public class LastFmApiRequestor {
	private final RequestScheduler requestSpacer;
	private final ExecutorService requestThreads;
	
	public static LastFmApiRequestor createWithDefaultRequestDelay() {
		return new LastFmApiRequestor(RequestScheduler.createWithDefaultRequestDelay());
	}
	
	public static LastFmApiRequestor createWithCustomRequestDelay(long requestDelay) {
		return new LastFmApiRequestor(RequestScheduler.createWithRequestDelay(requestDelay));
	}
	
	private LastFmApiRequestor(RequestScheduler requestScheduler) {
		this.requestSpacer = requestScheduler;
		this.requestThreads = Executors.newFixedThreadPool(5);
	}
	
	public String request(final LastFmRequest apiRequest) {
		Future<String> response = asyncRequest(apiRequest);
		try {
			return response.get();
		}
		catch (InterruptedException | ExecutionException e) {
			throw new LastFmApiException("Problem making request to LastFM!", e);
		}
	}
	
	public Future<String> asyncRequest(final LastFmRequest apiRequest) {
		return requestThreads.submit(() -> makeRequest(apiRequest));
	}
	
	private String makeRequest(LastFmRequest request) {
		try {
			URL requestUrl = new URL(request.getRequestUrl());
			return getResponse(requestUrl);
		}
		catch(Exception e) {
			throw new LastFmApiException("Problem making request to LastFM!", e);
		}
	}
	
	private String getResponse(URL url) throws InterruptedException, IOException {
		requestSpacer.reserveRequest();
		URLConnection connection = url.openConnection();
		InputStream response = connection.getInputStream();
		BufferedReader responseReader = new BufferedReader(new InputStreamReader(response));
		StringBuffer responseText = new StringBuffer();
		String line;
		while((line = responseReader.readLine()) != null) {
			responseText.append(line);
			responseText.append(System.lineSeparator());
		}
		responseReader.close();
		return responseText.toString();
	}
}
