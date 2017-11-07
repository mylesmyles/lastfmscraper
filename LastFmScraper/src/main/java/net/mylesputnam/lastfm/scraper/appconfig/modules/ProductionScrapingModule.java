package net.mylesputnam.lastfm.scraper.appconfig.modules;

import java.util.Map;

import net.mylesputnam.lastfm.scraper.db.ScraperDatabaseConnector;
import net.mylesputnam.lastfm.scraper.db.requests.update.DbUpdateRequestScheduler;
import net.mylesputnam.lastfm.scraper.db.validation.DatabaseValidator;
import net.mylesputnam.lastfm.scraper.scraper.ScraperTaskFetcher;
import net.mylesputnam.lastfm.scraper.settings.ScraperSetting;
import net.mylesputnam.lastfm.scraper.webrequests.LastFmWebRequestScheduler;
import net.mylesputnam.lastfm.scraper.webrequests.RequestSpacer;

public class ProductionScrapingModule extends ScrapingModule {

	public ProductionScrapingModule(Map<ScraperSetting, String> settings) {
		super(settings);
	}

	@Override
	protected void configure() {
		initSettings();
		bind(LastFmWebRequestScheduler.class);
		bind(RequestSpacer.class);
		bind(ScraperDatabaseConnector.class);
		bind(DatabaseValidator.class);
		bind(DbUpdateRequestScheduler.class);
		bind(ScraperTaskFetcher.class);
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
