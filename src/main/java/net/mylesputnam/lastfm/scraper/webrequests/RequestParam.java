package net.mylesputnam.lastfm.scraper.webrequests;

public class RequestParam {
	public final String key;
	public final String value;
	
	public RequestParam(String key, String value) {
		this.key = key;
		this.value = value;
	}
	
	public String getParamString() {
		return key + "=" + value;
	}
}
