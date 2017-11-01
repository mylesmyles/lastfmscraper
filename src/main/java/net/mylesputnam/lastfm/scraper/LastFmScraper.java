package net.mylesputnam.lastfm.scraper;

import net.mylesputnam.lastfm.scraper.scraper.Scraper;
import net.mylesputnam.lastfm.scraper.settings.PropertiesFetcher;
import net.mylesputnam.lastfm.scraper.settings.ScraperSettings;

public class LastFmScraper {
	public static void start() {
		ScraperSettings settings = new ScraperSettings(PropertiesFetcher.getPropertiesFromDisk());
		settings.exitProgramIfSettingsAreInvalid();
		
		Scraper.start(settings.initConfigAndGetScraperBuilder());
	}
	
	public static void main(String[] args) {
		LastFmScraper.start();
	}
}
