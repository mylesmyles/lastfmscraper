package net.mylesputnam.lastfm.scraper.webrequests;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import net.mylesputnam.lastfm.scraper.appconfig.bindings.LastFmApiKey;
import net.mylesputnam.lastfm.scraper.appconfig.bindings.LastFmApiRootUrl;

@Singleton
public class RequestSender {
	
	private final String apiUrl;
	private final String apiKey;
	private final RequestSpacer requestSpacer;
	
	@Inject
	public RequestSender(@LastFmApiRootUrl String apiUrl, @LastFmApiKey String apiKey, RequestSpacer requestSpacer) {
		this.apiUrl = apiUrl;
		this.apiKey = apiKey;
		this.requestSpacer = requestSpacer;
	}
	
	public void sendRequest(LastFmRequest request) {
		String requestParams = request.getRequestParams();
		String response = null;
		try {
			response = get(requestParams);
		}
		catch(Exception e) {
			request.handleError(e);
			return;
		}
		
		request.handleSuccess(response);
	}
	
	private String get(String params) throws InterruptedException, IOException {
		requestSpacer.reserveRequest();
		URL url = new URL(buildUrl(params));
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
	
	private String buildUrl(String params) {
		return new StringBuilder().append(apiUrl).append("?").append(params)
				.append("&api_key=").append(apiKey).toString();
	}
}
