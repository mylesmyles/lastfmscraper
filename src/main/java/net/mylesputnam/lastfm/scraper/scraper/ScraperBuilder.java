package net.mylesputnam.lastfm.scraper.scraper;

import java.sql.Connection;

import com.google.inject.Guice;
import com.google.inject.Injector;

import net.mylesputnam.lastfm.scraper.appconfig.modules.ScrapingModule;
import net.mylesputnam.lastfm.scraper.db.SchemaValidator;
import net.mylesputnam.lastfm.scraper.db.ScraperDatabaseConnector;
import net.mylesputnam.lastfm.scraper.queues.LastFmRequestQueue;
import net.mylesputnam.lastfm.scraper.webrequests.LastFmRequest;

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
		return null;
	}
	
	private void verifyDatabaseSchema() {
		SchemaValidator validator = scraperInjector.getInstance(SchemaValidator.class);
		validator.validateSchema();
	}
	
	public LastFmRequestQueue getRequestQueue() {
		return scraperInjector.getInstance(LastFmRequestQueue.class);
	}
}
