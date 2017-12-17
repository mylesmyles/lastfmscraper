package net.mylesputnam.lastfm.api.requests;

import java.util.Arrays;
import java.util.List;

public class LastFmRequestBuilder {
	public static final String DEFAULT_URL = "http://ws.audioscrobbler.com/2.0/";
	
	private String url;
	private boolean isJsonFormat;
	private final OrderedParamSet defaultParams;
	
	public static LastFmRequestBuilder create(String apiKey) {
		return new LastFmRequestBuilder(DEFAULT_URL, RequestParam.apiKey(apiKey));
	}
	
	public LastFmRequestBuilder withUrl(String url) {
		this.url = url;
		return this;
	}
	
	public LastFmRequestBuilder withJsonFormat() {
		isJsonFormat = true;
		return this;
	}
	
	public LastFmRequestBuilder withXmlFormat() {
		isJsonFormat = false;
		return this;
	}
	
	public LastFmRequestBuilder withMethod(String method) {
		defaultParams.add(RequestParam.method(method));
		return this;
	}
	
	public LastFmRequestBuilder withDefaultParam(RequestParam defaultParam) {
		defaultParams.add(defaultParam);
		return this;
	}
	
	private LastFmRequestBuilder(String url, RequestParam...defaultParams) {
		this.url = url;
		this.isJsonFormat = false;
		this.defaultParams = new OrderedParamSet(Arrays.asList(defaultParams));
	}
	
	public LastFmRequest buildRequest(RequestParam...params) {
		return buildRequest(Arrays.asList(params));
	}
	
	public LastFmRequest buildRequest(final List<RequestParam> params) {
		OrderedParamSet paramSet = new OrderedParamSet(params);
		if(isJsonFormat) {
			paramSet.add(RequestParam.jsonFormat());
		}
		
		OrderedParamSet combined = OrderedParamSet.combine(defaultParams, paramSet);
		List<RequestParam> allParams = combined.asList();
		return BasicLastFmRequest.create(url, allParams);
	}
}
