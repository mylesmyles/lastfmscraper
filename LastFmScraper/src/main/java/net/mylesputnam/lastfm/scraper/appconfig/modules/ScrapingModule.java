package net.mylesputnam.lastfm.scraper.appconfig.modules;

import java.util.Map;

import com.google.inject.AbstractModule;

import net.mylesputnam.lastfm.scraper.settings.ScraperSetting;

public abstract class ScrapingModule extends AbstractModule {
	protected final Map<ScraperSetting, String> settings;
	
	public ScrapingModule(Map<ScraperSetting, String> settings) {
		this.settings = settings;
	}
}
