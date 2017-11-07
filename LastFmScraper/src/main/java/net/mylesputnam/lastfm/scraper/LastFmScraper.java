package net.mylesputnam.lastfm.scraper;

import net.mylesputnam.lastfm.scraper.db.validation.DatabaseValidator;
import net.mylesputnam.lastfm.scraper.scraper.Scraper;
import net.mylesputnam.lastfm.scraper.scraper.ScraperBuilder;
import net.mylesputnam.lastfm.scraper.scraper.ScraperTaskFetcher;
import net.mylesputnam.lastfm.scraper.settings.PropertiesFetcher;
import net.mylesputnam.lastfm.scraper.settings.ScraperSettings;

public class LastFmScraper {
	public static void start() {
		ScraperSettings settings = new ScraperSettings(PropertiesFetcher.getPropertiesFromDisk());
		settings.exitProgramIfSettingsAreInvalid();
		
		ScraperBuilder builder = settings.initConfigAndGetScraperBuilder();
		DatabaseValidator dbValidator = builder.getDatabaseValidator();
		dbValidator.validateDatabase();
		
		ScraperTaskFetcher scraperTaskFetcher = builder.getScraperTaskFetcher();
		Scraper.start(scraperTaskFetcher);
	}
	
	public static void main(String[] args) {
		LastFmScraper.start();
	}
}
