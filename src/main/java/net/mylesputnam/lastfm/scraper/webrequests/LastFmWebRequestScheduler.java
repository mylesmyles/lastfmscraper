package net.mylesputnam.lastfm.scraper.webrequests;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.LinkedList;
import java.util.List;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import net.mylesputnam.lastfm.scraper.appconfig.bindings.LastFmApiKey;
import net.mylesputnam.lastfm.scraper.appconfig.bindings.LastFmApiRootUrl;
import net.mylesputnam.lastfm.scraper.db.requests.update.DbUpdateRequestScheduler;

@Singleton
public class LastFmWebRequestScheduler {
	private final String apiUrl;
	private final RequestParam apiKeyParam;
	private final RequestSpacer requestSpacer;
	private final DbUpdateRequestScheduler updateRequestScheduler;
	
	@Inject
	public LastFmWebRequestScheduler(@LastFmApiRootUrl String apiUrl, @LastFmApiKey String apiKey, RequestSpacer requestSpacer, DbUpdateRequestScheduler updateRequestScheduler) {
		this.apiUrl = apiUrl;
		this.apiKeyParam = new RequestParam("api_key", apiKey);
		this.requestSpacer = requestSpacer;
		this.updateRequestScheduler = updateRequestScheduler;
	}
	
	public void makeRequestAndSaveResults(LastFmWebRequest request) {
		while(!request.isComplete()) {
			String response = null;
			try {
				List<RequestParam> paramsWithApiKey = addApiKey(request.getNextParams());
				URL requestUrl = UrlBuilder.buildUrl(apiUrl, paramsWithApiKey);
				response = get(requestUrl);
				request.handleResponse(response);
			}
			catch(Exception e) {
				e.printStackTrace();
				return;
			}
		}
		
		updateRequestScheduler.scheduleUpdates(request.getRecordsToSave());
	}
	
	private String get(URL url) throws InterruptedException, IOException {
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
	
	private List<RequestParam> addApiKey(List<RequestParam> params) {
		List<RequestParam> paramsWithKey = new LinkedList<>(params);
		paramsWithKey.add(apiKeyParam);
		return paramsWithKey;
	}
}
