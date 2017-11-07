package net.mylesputnam.lastfm.scraper.webrequests;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import com.google.inject.Inject;

import net.mylesputnam.lastfm.scraper.appconfig.bindings.LastFmRequestDelay;

public class RequestSpacer {

	private final LinkedBlockingQueue<CountDownLatch> requestLine;
	private final ScheduledExecutorService requestDelayTickThread;
	private final int minimumRequestDelay;
	
	@Inject
	public RequestSpacer(@LastFmRequestDelay Integer minimumRequestDelay) {
		this.minimumRequestDelay = minimumRequestDelay;
		this.requestLine = new LinkedBlockingQueue<>();
		this.requestDelayTickThread = Executors.newSingleThreadScheduledExecutor();
		scheduleWaitForNextElement();
	}
	
	//possibly implement some sort of "remove self from queue" here when interrupted, counting down the latch so the spacer knows the thread is gone
	public void reserveRequest() throws InterruptedException {
		CountDownLatch latch = new CountDownLatch(1);
		requestLine.add(latch);
		latch.await();
	}
	
	private void scheduleWaitForNextElement() {
		requestDelayTickThread.schedule(this::waitForElement, minimumRequestDelay, TimeUnit.MILLISECONDS);
	}
	
	private void waitForElement() {
		CountDownLatch latch = null;
		try {
			latch = requestLine.take();
		}
		catch (InterruptedException e) {}
		
		scheduleWaitForNextElement();
		
		if(latch != null) {
			latch.countDown();
		}
	}
}
