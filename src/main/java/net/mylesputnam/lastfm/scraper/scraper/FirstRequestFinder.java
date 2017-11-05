package net.mylesputnam.lastfm.scraper.scraper;

import com.google.inject.Inject;

import net.mylesputnam.lastfm.scraper.webrequests.LastFmRequest;
import net.mylesputnam.lastfm.scraper.webrequests.UserDataRequest;

public class FirstRequestFinder {
	
	@Inject
	public LastFmRequest getFirstRequest() {
		return new UserDataRequest("mylesmyles07");
	}
}
