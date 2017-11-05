package net.mylesputnam.lastfm.scraper.scraper;

import com.google.inject.Guice;
import com.google.inject.Injector;

import net.mylesputnam.lastfm.scraper.appconfig.modules.ScrapingModule;
import net.mylesputnam.lastfm.scraper.db.validation.DatabaseValidator;
import net.mylesputnam.lastfm.scraper.webrequests.LastFmRequest;
import net.mylesputnam.lastfm.scraper.webrequests.RequestSender;
import net.mylesputnam.lastfm.scraper.webrequests.UserDataRequest;

public class ScraperBuilder {
	public final Injector scraperInjector;
	
	public static ScraperBuilder create(ScrapingModule scraperModule) {
		return new ScraperBuilder(scraperModule);
	}
	
	private ScraperBuilder(ScrapingModule scraperGuiceModule) {
		this.scraperInjector = Guice.createInjector(scraperGuiceModule);
	}
	
	public LastFmRequest getFirstRequest() {
		verifyDatabaseSchema();
		
		return new UserDataRequest("mylesmyles07");
	}
	
	public RequestSender getRequestSender() {
		return this.scraperInjector.getInstance(RequestSender.class);
	}
	
	private void verifyDatabaseSchema() {
		DatabaseValidator validator = scraperInjector.getInstance(DatabaseValidator.class);
		validator.validateDatabase();
	}
	
}
