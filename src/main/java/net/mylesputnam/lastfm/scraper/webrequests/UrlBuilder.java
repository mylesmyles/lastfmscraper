package net.mylesputnam.lastfm.scraper.webrequests;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.stream.Collectors;

public class UrlBuilder {
	public static URL buildUrl(String baseUrl, List<RequestParam> params) throws MalformedURLException {
		String urlString = baseUrl + "?" + getParamsAsString(params);
		return new URL(urlString);
	}
	
	private static String getParamsAsString(List<RequestParam> params) {
		return String.join("&", params.stream().map(param -> param.getParamString())
				.collect(Collectors.toList()));
	}
}
