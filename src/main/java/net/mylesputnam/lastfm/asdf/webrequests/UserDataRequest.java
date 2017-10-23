package net.mylesputnam.lastfm.asdf.webrequests;

import com.google.inject.Inject;

import net.mylesputnam.lastfm.asdf.queues.LastFmRequestQueue;

public class UserDataRequest implements LastFmRequest {
	
	@Inject LastFmRequestQueue requestQueue;
	private final String requestParams;
	
	public UserDataRequest(String userName) {
		requestParams = "method=user.getinfo&user=" + userName;
	}
	
	@Override
	public String getRequestParams() {
		return requestParams;
	}
	
	@Override
	public void handleSuccess(String requestSuccess) {
		System.out.println("Yay, Request was made! Here's the response:");
		System.out.println(requestSuccess);
	}
	
	@Override
	public void handleError(Exception requestException) {
		System.out.println("Oh no, we got an exception... here it is:");
		requestException.printStackTrace();
	}
	
	@Override
	public boolean isRequestFinished() {
		return true;
	}
	
	public void schedule() {
		requestQueue.addRequest(this);
	}
}
