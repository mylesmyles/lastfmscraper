package net.mylesputnam.lastfm.scraper.settings;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;

import net.mylesputnam.lastfm.scraper.appconfig.modules.ProductionScrapingModule;

public class ScraperSettings {
	private final Map<ScraperSetting, String> properties;
	
	public ScraperSettings(Map<ScraperSetting, String> properties) {
		this.properties = properties;
	}
	
	public void exitProgramIfSettingsAreInvalid() {
		List<String> anySettingErrors = getSettingsErrors(properties);
		if(!anySettingErrors.isEmpty()) {
			printErrors(anySettingErrors);
			System.out.println("Program is exiting. Please fix any noted settings errors");
			System.exit(0);
		}
	}
	
	public Injector initConfigAndGetInjector(Class<? extends Module> module) {
		return Guice.createInjector(new ProductionScrapingModule(properties));
	}
	
	private List<String> getSettingsErrors(Map<ScraperSetting, String> properties) {
		return properties.entrySet().stream()
			.map(entry -> entry.getKey().getAnyErrorMessages(entry.getValue()))
			.flatMap(errors -> errors.stream())
			.collect(Collectors.toList());
	}
	
	private void printErrors(List<String> errors) {
		errors.forEach(error -> System.out.println("[ERROR] " + error));
	}
}
