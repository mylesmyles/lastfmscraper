package net.mylesputnam.lastfm.scraper.scraper;

import net.mylesputnam.lastfm.scraper.webrequests.LastFmRequest;
import net.mylesputnam.lastfm.scraper.webrequests.RequestSender;

public class Scraper {
	public static void start(ScraperBuilder builder) {
		LastFmRequest request = builder.getFirstRequest();
		RequestSender sender = builder.getRequestSender();
		
		sender.sendRequest(request);
	}
}
