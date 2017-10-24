package net.mylesputnam.lastfm.scraper;

import com.google.inject.Injector;

import net.mylesputnam.lastfm.scraper.appconfig.modules.ScrapingModule;
import net.mylesputnam.lastfm.scraper.settings.PropertiesFetcher;
import net.mylesputnam.lastfm.scraper.settings.ScraperSettings;
import net.mylesputnam.lastfm.scraper.webrequests.RequestSender;
import net.mylesputnam.lastfm.scraper.webrequests.UserDataRequest;

public class LastFmScraper {
	public static void start() {
		ScraperSettings settings = new ScraperSettings(PropertiesFetcher.getPropertiesFromDisk());
		settings.exitProgramIfSettingsAreInvalid();
		Injector injector = settings.initConfigAndGetInjector(ScrapingModule.class);
		
		RequestSender sender = injector.getInstance(RequestSender.class);
		UserDataRequest request = new UserDataRequest("mylesmyles07");
//		request.schedule();
		sender.sendRequest(request);
	}
	
	public static void main(String[] args) {
		LastFmScraper.start();
	}
}
