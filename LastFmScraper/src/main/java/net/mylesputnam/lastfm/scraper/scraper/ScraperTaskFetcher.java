package net.mylesputnam.lastfm.scraper.scraper;

import com.google.inject.Inject;

import net.mylesputnam.lastfm.scraper.appconfig.bindings.ScraperUserSeed;
import net.mylesputnam.lastfm.scraper.db.requests.get.ScraperTaskInfoGetter;
import net.mylesputnam.lastfm.scraper.exceptions.DbException;
import net.mylesputnam.lastfm.scraper.exceptions.NoScraperTaskFoundException;
import net.mylesputnam.lastfm.scraper.webrequests.LastFmWebRequestScheduler;
import net.mylesputnam.lastfm.scraper.webrequests.SeedUserRequest;

public class ScraperTaskFetcher {
	private static final int DB_REQUEST_ATTEMPTS = 3;
	
	private final LastFmWebRequestScheduler webRequestScheduler;
	private final ScraperTaskInfoGetter scraperTaskInfoGetter;
	private final String seedUsername;
	
	@Inject
	public ScraperTaskFetcher(LastFmWebRequestScheduler webRequestScheduler, ScraperTaskInfoGetter scraperTaskInfoGetter, @ScraperUserSeed String seedUsername) {
		this.webRequestScheduler = webRequestScheduler;
		this.scraperTaskInfoGetter = scraperTaskInfoGetter;
		this.seedUsername = seedUsername;
	}
	
	public ScraperTask fetchNextTask() throws NoScraperTaskFoundException {
		int requestsLeft = DB_REQUEST_ATTEMPTS;
		while(requestsLeft > 0) {
			requestsLeft--;
			try {
				return fetchNextTaskFromDb();
			}
			catch(DbException e) {
				System.out.println("Problem getting task from database! Trying again...");
				e.printStackTrace();
			}
		}
		
		throw new NoScraperTaskFoundException("Encountered problems connecting to the scraper database, could not create next scraper task");
	}
	
	private ScraperTask fetchNextTaskFromDb() {
		if(!scraperTaskInfoGetter.seedUserExists()) {
			System.out.println("Scraper seed doesn't exist!");
			return ScraperTask.create(webRequestScheduler, new SeedUserRequest(seedUsername));
		}
		else {
			return ScraperTask.create(webRequestScheduler, new SeedUserRequest(seedUsername));
		}
	}
}
