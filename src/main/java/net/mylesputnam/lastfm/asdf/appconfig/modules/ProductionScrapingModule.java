package net.mylesputnam.lastfm.asdf.appconfig.modules;

import java.util.Map;

import net.mylesputnam.lastfm.asdf.db.DatabaseConnection;
import net.mylesputnam.lastfm.asdf.db.ScraperDatabase;
import net.mylesputnam.lastfm.asdf.queues.LastFmRequestQueue;
import net.mylesputnam.lastfm.asdf.queues.LastFmWebRequestScheduler;
import net.mylesputnam.lastfm.asdf.settings.ScraperSetting;
import net.mylesputnam.lastfm.asdf.webrequests.RequestSender;
import net.mylesputnam.lastfm.asdf.webrequests.RequestSpacer;

public class ProductionScrapingModule extends ScrapingModule {

	public ProductionScrapingModule(Map<ScraperSetting, String> settings) {
		super(settings);
	}

	@Override
	protected void configure() {
		initSettings();
		bind(ScraperDatabase.class).to(DatabaseConnection.class);
		bind(LastFmRequestQueue.class);
		bind(RequestSender.class);
		bind(RequestSpacer.class);
		bind(LastFmWebRequestScheduler.class);
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
