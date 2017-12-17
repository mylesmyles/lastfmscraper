package net.mylesputnam.lastfm.api.requests;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

class RequestScheduler {
	static final long DEFAULT_REQUEST_DELAY_MS = 250;

	private final LinkedBlockingQueue<CountDownLatch> requestQueue;
	private final ScheduledExecutorService requestDelayThread;
	private final long requestDelayMs;
	
	public static RequestScheduler createWithDefaultRequestDelay() {
		return new RequestScheduler(DEFAULT_REQUEST_DELAY_MS);
	}
	
	public static RequestScheduler createWithRequestDelay(long requestDelayMs) {
		return new RequestScheduler(requestDelayMs);
	}
	
	public RequestScheduler(long requestDelayMs) {
		this.requestDelayMs = requestDelayMs;
		this.requestQueue = new LinkedBlockingQueue<>();
		this.requestDelayThread = Executors.newSingleThreadScheduledExecutor();
		waitForNextRequestAfterDelay(0);
	}
	
	public void reserveRequest() throws InterruptedException {
		CountDownLatch latch = new CountDownLatch(1);
		requestQueue.add(latch);
		
		try {
			latch.await();
		}
		catch (InterruptedException e) {
			latch.countDown();
			throw new InterruptedException("Thread was interrupted while waiting to make a request");
		}
	}
	
	public int threadsWaiting() {
		return requestQueue.size();
	}
	
	private void waitForNextRequestAfterDelay(long delayMs) {
		requestDelayThread.schedule(this::releaseNextRequest, delayMs, TimeUnit.MILLISECONDS);
	}
	
	private void releaseNextRequest() {
		try {
			CountDownLatch latch = null;
			do {
				latch = requestQueue.take();
			} while(latch.getCount() < 1);
			
			latch.countDown();
		}
		catch (InterruptedException e) {}
		
		waitForNextRequestAfterDelay(requestDelayMs);
	}
}
