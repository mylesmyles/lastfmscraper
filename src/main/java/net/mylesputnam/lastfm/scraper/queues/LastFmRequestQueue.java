package net.mylesputnam.lastfm.scraper.queues;

import java.util.concurrent.LinkedBlockingQueue;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import net.mylesputnam.lastfm.scraper.webrequests.LastFmRequest;

@Singleton
public class LastFmRequestQueue {
	private final LastFmWebRequestScheduler scheduler;
	public final LinkedBlockingQueue<LastFmRequest> requestQueue;
	
	@Inject
	public LastFmRequestQueue(LastFmWebRequestScheduler scheduler) {
		this.scheduler = scheduler;
		requestQueue = new LinkedBlockingQueue<>();
		this.scheduler.begin(this);
	}
	
	public void addRequest(LastFmRequest request) {
		requestQueue.add(request);
	}
	
	public LastFmRequest waitForNextRequest() throws InterruptedException {
		return requestQueue.take();
	}
}
