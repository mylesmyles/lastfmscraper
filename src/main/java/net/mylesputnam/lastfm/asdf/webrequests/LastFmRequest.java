package net.mylesputnam.lastfm.asdf.webrequests;

public interface LastFmRequest {
	public String getRequestParams();
	public void handleError(Exception requestException);
	public void handleSuccess(String requestResponse);
	public boolean isRequestFinished();
}
