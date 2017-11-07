package net.mylesputnam.lastfm.scraper.scraper;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import net.mylesputnam.lastfm.scraper.webrequests.LastFmWebRequest;
import net.mylesputnam.lastfm.scraper.webrequests.LastFmWebRequestScheduler;

public class ScraperTask {
	private final LastFmWebRequestScheduler webRequestScheduler;
	private final List<LastFmWebRequest> webRequests;
	private final CountDownLatch waitForUpdatesCompleteLatch;
	private final ExecutorService webRequestThreads;
	
	public static ScraperTask create(LastFmWebRequestScheduler webRequestScheduler, LastFmWebRequest request) {
		return new ScraperTask(webRequestScheduler, Collections.singletonList(request));
	}
	
	public static ScraperTask create(LastFmWebRequestScheduler webRequestScheduler, List<LastFmWebRequest> requests) {
		return new ScraperTask(webRequestScheduler, requests);
	}
	
	private ScraperTask(LastFmWebRequestScheduler webRequestScheduler, List<LastFmWebRequest> requests) {
		this.webRequestScheduler = webRequestScheduler;
		this.webRequests = requests;
		this.waitForUpdatesCompleteLatch = new CountDownLatch(webRequests.size());
		this.webRequestThreads = Executors.newFixedThreadPool(5);
	}
	
	public void runTask() {
		for(LastFmWebRequest request : webRequests) {
			request.onSaveComplete(this::notifyRequestComplete);
			webRequestThreads.execute(() -> webRequestScheduler.makeRequestAndSaveResults(request));
		}
		
		try {
			waitForUpdatesCompleteLatch.await();
		} catch (InterruptedException e) {
			System.out.println("I was interrupted!");
			e.printStackTrace();
		}
	}
	
	private void notifyRequestComplete() {
		this.waitForUpdatesCompleteLatch.countDown();
	}
}
