package net.mylesputnam.lastfm.scraper.appconfig.modules;

import java.util.Map;

import net.mylesputnam.lastfm.scraper.db.ScraperDatabaseConnector;
import net.mylesputnam.lastfm.scraper.db.validation.DatabaseValidator;
import net.mylesputnam.lastfm.scraper.queues.LastFmRequestQueue;
import net.mylesputnam.lastfm.scraper.queues.LastFmWebRequestScheduler;
import net.mylesputnam.lastfm.scraper.settings.ScraperSetting;
import net.mylesputnam.lastfm.scraper.webrequests.RequestSender;
import net.mylesputnam.lastfm.scraper.webrequests.RequestSpacer;

public class ProductionScrapingModule extends ScrapingModule {

	public ProductionScrapingModule(Map<ScraperSetting, String> settings) {
		super(settings);
	}

	@Override
	protected void configure() {
		initSettings();
		bind(LastFmRequestQueue.class);
		bind(RequestSender.class);
		bind(RequestSpacer.class);
		bind(LastFmWebRequestScheduler.class);
		bind(ScraperDatabaseConnector.class);
		bind(DatabaseValidator.class);
	}

	private void initSettings() {
		for(ScraperSetting setting : settings.keySet()) {
			if(setting == ScraperSetting.POSTGRES_PORT || setting == ScraperSetting.LASTFM_REQUEST_DELAY) {
				Integer value = Integer.parseInt(settings.get(setting));
				bindConstant().annotatedWith(setting.bindingClass).to(value);
			}
			else {
				String value = settings.get(setting);
				bindConstant().annotatedWith(setting.bindingClass).to(value);
			}
		}
	}
}
