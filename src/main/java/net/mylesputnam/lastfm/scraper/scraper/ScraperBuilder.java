package net.mylesputnam.lastfm.scraper.scraper;

import com.google.inject.Guice;
import com.google.inject.Injector;

import net.mylesputnam.lastfm.scraper.appconfig.modules.ScrapingModule;
import net.mylesputnam.lastfm.scraper.db.validation.DatabaseValidator;

public class ScraperBuilder {
	public final Injector scraperInjector;
	
	public static ScraperBuilder create(ScrapingModule scraperModule) {
		return new ScraperBuilder(scraperModule);
	}
	
	private ScraperBuilder(ScrapingModule scraperGuiceModule) {
		this.scraperInjector = Guice.createInjector(scraperGuiceModule);
	}
	
	public DatabaseValidator getDatabaseValidator() {
		return scraperInjector.getInstance(DatabaseValidator.class);
	}
	
	public ScraperTaskFetcher getScraperTaskFetcher() {
		return scraperInjector.getInstance(ScraperTaskFetcher.class);
	}
}
