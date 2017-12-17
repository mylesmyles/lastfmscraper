package net.mylesputnam.lastfm.api.requests;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

class BasicLastFmRequest implements LastFmRequest {
	private final String url;
	private final List<RequestParam> requestParams;
	
	public static BasicLastFmRequest create(String url, List<RequestParam> requestParams) {
		return new BasicLastFmRequest(url, requestParams);
	}
	
	private BasicLastFmRequest(String url, List<RequestParam> requestParams) {
		this.url = url;
		this.requestParams = new LinkedList<>(requestParams);
	}

	@Override
	public String getRequestUrl() {
		return buildUrlString();
	}
	
	private String buildUrlString() {
		return url + "?" + getParamsAsString(requestParams);
	}
	
	private String getParamsAsString(List<RequestParam> params) {
		return String.join("&", params.stream().map(param -> param.getParamString())
				.collect(Collectors.toList()));
	}
}
