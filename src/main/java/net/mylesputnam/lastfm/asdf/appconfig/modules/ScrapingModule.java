package net.mylesputnam.lastfm.asdf.appconfig.modules;

import java.util.Map;

import com.google.inject.AbstractModule;

import net.mylesputnam.lastfm.asdf.settings.ScraperSetting;

public abstract class ScrapingModule extends AbstractModule {
	protected final Map<ScraperSetting, String> settings;
	
	public ScrapingModule(Map<ScraperSetting, String> settings) {
		this.settings = settings;
	}
}
