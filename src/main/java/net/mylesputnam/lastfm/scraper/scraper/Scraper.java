package net.mylesputnam.lastfm.scraper.scraper;

import net.mylesputnam.lastfm.scraper.exceptions.NoScraperTaskFoundException;

public class Scraper {
	public static void start(ScraperTaskFetcher scraperTaskFetcher) {
		try {
//			while(true) { //TODO: add handler for when application gets exit signal
			ScraperTask scraperTask = scraperTaskFetcher.fetchNextTask();
			scraperTask.runTask();
			System.out.println("Task is complete!");
//			}
		}
		catch (NoScraperTaskFoundException e) {
			System.out.println("Exiting application: Could not identify anything to pull from lastfm!");
		}
	}
}
