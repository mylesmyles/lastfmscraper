package net.mylesputnam.lastfm.scraper.webrequests;

public interface LastFmRequest {
	public String getRequestParams();
	public void handleError(Exception requestException);
	public void handleSuccess(String requestResponse);
	public boolean isRequestFinished();
}
