package net.mylesputnam.lastfm.scraper.queues;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import com.google.inject.Inject;

import net.mylesputnam.lastfm.scraper.webrequests.LastFmRequest;
import net.mylesputnam.lastfm.scraper.webrequests.RequestSender;

public class LastFmWebRequestScheduler {
	private final RequestSender requestSender;
	private LastFmRequestQueue requestQueue;
	
	private ScheduledExecutorService dispatchThread;
	
	@Inject
	public LastFmWebRequestScheduler(RequestSender requestSender) {
		this.requestSender = requestSender;
		
	}
	
	public void begin(LastFmRequestQueue queue) {
		this.requestQueue = queue;
		this.dispatchThread = Executors.newSingleThreadScheduledExecutor();
		dispatchNextMessage();
	}
	
	private void doMessageDispatch() {
		LastFmRequest requestToSchedule = null;
		try {
			requestToSchedule = requestQueue.waitForNextRequest();
		} catch (InterruptedException e) {}
		
		dispatchNextMessage();
		if(requestToSchedule != null) {
			requestSender.sendRequest(requestToSchedule);
		}
	}
	
	private void dispatchNextMessage() {
		dispatchThread.schedule(this::doMessageDispatch, 5, TimeUnit.MILLISECONDS);
	}
}
