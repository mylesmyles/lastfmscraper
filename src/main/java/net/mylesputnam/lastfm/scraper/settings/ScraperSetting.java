package net.mylesputnam.lastfm.scraper.settings;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.google.common.base.Strings;

import net.mylesputnam.lastfm.scraper.appconfig.bindings.LastFmApiKey;
import net.mylesputnam.lastfm.scraper.appconfig.bindings.LastFmApiRootUrl;
import net.mylesputnam.lastfm.scraper.appconfig.bindings.LastFmRegisteredTo;
import net.mylesputnam.lastfm.scraper.appconfig.bindings.LastFmRequestDelay;
import net.mylesputnam.lastfm.scraper.appconfig.bindings.LastFmSharedSecretKey;
import net.mylesputnam.lastfm.scraper.appconfig.bindings.PostgresAddress;
import net.mylesputnam.lastfm.scraper.appconfig.bindings.PostgresPort;
import net.mylesputnam.lastfm.scraper.appconfig.bindings.PostgresSchemaName;
import net.mylesputnam.lastfm.scraper.appconfig.bindings.PostgresUserPassword;
import net.mylesputnam.lastfm.scraper.appconfig.bindings.PostgresUsername;
import net.mylesputnam.lastfm.scraper.appconfig.bindings.ScraperUserSeed;



public enum ScraperSetting {
	LASTFM_API_KEY("lastfm.apikey","last.fm API key",LastFmApiKey.class),
	LASTFM_SHARED_SECRET_KEY("lastfm.sharedsecret","last.fm shared secret key",LastFmSharedSecretKey.class),
	LASTFM_REGISTERED_TO("lastfm.registeredto","last.fm user registered to API account",LastFmRegisteredTo.class),
	LASTFM_API_ROOT_URL("lastfm.api.url","last.fm API root URL",LastFmApiRootUrl.class),
	LASTFM_REQUEST_DELAY("lastfm.request.delay", "Milliseconds between each request",LastFmRequestDelay.class),
	SCRAPER_USER_SEED("app.userseed","last.fm user for initial datapoint",ScraperUserSeed.class),
	POSTGRES_USERNAME("postgres.user.name","postgres database user username",PostgresUsername.class),
	POSTGRES_USER_PASSWORD("postgres.user.password","postgres database user password",PostgresUserPassword.class),
	POSTGRES_SCHEMA_NAME("postgres.schema","postgres schema name",PostgresSchemaName.class),
	POSTGRES_ADDRESS("postgres.address","postgres database IP address or url",PostgresAddress.class),
	POSTGRES_PORT("postgres.port","postgres database port",PostgresPort.class);
	
	public final String propertyValue;
	public final String propertyDescription;
	public final Class<? extends Annotation> bindingClass;
	
	private static final Set<ScraperSetting> propertiesThatCanBeEmpty = new HashSet<>(Arrays.asList(POSTGRES_USER_PASSWORD));
	private static final Set<ScraperSetting> isInteger = new HashSet<>(Arrays.asList(POSTGRES_PORT, LASTFM_REQUEST_DELAY));
	
	private ScraperSetting(String propertyValue, String propertyDescription, Class<? extends Annotation> bindingClass) {
		this.propertyValue = propertyValue;
		this.propertyDescription = propertyDescription;
		this.bindingClass = bindingClass;
	}
	
	public List<String> getAnyErrorMessages(String value) {
		List<String> errors = new ArrayList<>();
		errors.addAll(getErrorsForEmptyValueIfNecessary(value));
		errors.addAll(getErrorsForNonIntegerIfNecessary(value));
		return errors;
	}
	
	public String getFullDescription() {
		return this.propertyDescription + " (" + propertyValue + ")";
	}
	
	private List<String> getErrorsForEmptyValueIfNecessary(String value) {
		if(this.mustBeNonEmpty() && Strings.isNullOrEmpty(value)) {
			return Collections.singletonList(getFullDescription() + " cannot be empty! Please set the corresponding value in "
					+ "lastfmscraper.properties");
		}
		else {
			return Collections.emptyList();
		}
	}
	
	private boolean mustBeNonEmpty() {
		return !propertiesThatCanBeEmpty.contains(this);
	}
	
	private List<String> getErrorsForNonIntegerIfNecessary(String value) {
		if(this.mustBePositiveInteger()) {
			boolean isNonNegativeInt = false;
			try {
				int intValue = Integer.parseInt(value);
				isNonNegativeInt = intValue >= 0;
			}
			catch(Exception e) {
				isNonNegativeInt = false;
			}
			
			if(!isNonNegativeInt) {
				return Collections.singletonList(getFullDescription() + " must be a non-negative integer! Please set the corresponding value in "
						+ "lastfmscraper.properties to a non-negative integer");
			}
		}
		return Collections.emptyList();
	}
	
	private boolean mustBePositiveInteger() {
		return isInteger.contains(this);
	}
}
