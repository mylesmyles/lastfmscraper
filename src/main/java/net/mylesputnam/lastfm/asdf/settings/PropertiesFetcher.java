package net.mylesputnam.lastfm.asdf.settings;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import com.google.common.base.Strings;

public class PropertiesFetcher {
	
	private static final String PROPERTIES_FILE_NAME = "lastfmscraper.properties";
	
	private final Map<ScraperSetting, String> propertiesMap;
	
	private PropertiesFetcher() {
		propertiesMap = new HashMap<>();
	}
	
	public static Map<ScraperSetting, String> getPropertiesFromDisk() {
		PropertiesFetcher fileFetcher = new PropertiesFetcher();
		return fileFetcher.fetchProperties();
	}
	
	public Map<ScraperSetting, String> fetchProperties() {
		prepopulateProperties();
		
		loadIfExists("src/main/resources/");
		loadIfExists("~/");
		loadIfExists("./");
		
		return propertiesMap;
	}
	
	private void prepopulateProperties() {
		Arrays.asList(ScraperSetting.values()).stream()
			.forEach(scraperProp -> propertiesMap.put(scraperProp, ""));
	}
	
	private void loadIfExists(String propertiesDirectory) {
		Path propertiesPath = Paths.get(propertiesDirectory + PROPERTIES_FILE_NAME);
		try {
			Properties propertiesFromFile = loadFileAsProperties(propertiesPath);
			
			for(ScraperSetting property : ScraperSetting.values()) {
				String propertyFromFile = getCleanStringFromProperties(property, propertiesFromFile);
				if(property != null) {
					propertiesMap.put(property, propertyFromFile);
				}
			}
		}
		catch(IOException e) {
			return;
		}
	}
	
	private Properties loadFileAsProperties(Path filepath) throws IOException {
		FileInputStream propFile = new FileInputStream(filepath.toFile());
		Properties props = new Properties();
		props.load(propFile);
		propFile.close();
		
		return props;
	}
	
	private String getCleanStringFromProperties(ScraperSetting property, Properties properties) {
		String propertyFromFile = properties.getProperty(property.propertyValue);
		String trimmedProperty = Strings.nullToEmpty(propertyFromFile).trim();
		return Strings.emptyToNull(trimmedProperty);
	}
}
