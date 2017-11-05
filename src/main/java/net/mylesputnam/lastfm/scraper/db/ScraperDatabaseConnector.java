package net.mylesputnam.lastfm.scraper.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

import com.google.inject.Inject;

import net.mylesputnam.lastfm.scraper.appconfig.bindings.PostgresAddress;
import net.mylesputnam.lastfm.scraper.appconfig.bindings.PostgresDatabaseName;
import net.mylesputnam.lastfm.scraper.appconfig.bindings.PostgresDatabaseSchema;
import net.mylesputnam.lastfm.scraper.appconfig.bindings.PostgresPort;
import net.mylesputnam.lastfm.scraper.appconfig.bindings.PostgresUsername;
import net.mylesputnam.lastfm.scraper.exceptions.DbConnectionError;

public class ScraperDatabaseConnector {
	
	private final int dbConnectionAttempts = 3;
	private final int connectionRetryDelayMs = 400;
	
	private final String username;
	private final String password;
	private final String address;
	private final Integer port;
	private final String databaseName;
	private final String databaseSchema;
	
	@Inject
	public ScraperDatabaseConnector(
			@PostgresUsername String username, @PostgresUsername String password,
			@PostgresAddress String address, @PostgresPort Integer port,
			@PostgresDatabaseName String databaseName, @PostgresDatabaseSchema String databaseSchema) {
		this.username = username;
		this.password = password;
		this.address = address;
		this.port = port;
		this.databaseName = databaseName;
		this.databaseSchema = databaseSchema;
	}
	
	public Connection dbConnection() {
		int attemptsLeft = dbConnectionAttempts;
		int retryDelayMs = 0;
		while(attemptsLeft > 0) {
			try {
				Thread.sleep(retryDelayMs);
				return attemptDbConnection();
			}
			catch(SQLException | InterruptedException e) {
				attemptsLeft--;
				if(attemptsLeft > 0) {
					System.out.println("Connection to PostgreSQL failed! Will try again in " + connectionRetryDelayMs + "ms. " 
							+ attemptsLeft +" attempts left" + System.lineSeparator() + System.lineSeparator());
					retryDelayMs = connectionRetryDelayMs;
				}
			}
		}
		
		throw new DbConnectionError(
				"Could not connect to database after retrying " + dbConnectionAttempts + " times!");
	}
	
	private Connection attemptDbConnection() throws SQLException {
		String dbUrl = "jdbc:postgresql://"+address+":"+port+"/"+databaseName;
		Properties dbConnectionProps = dbRequestProps();
		return DriverManager.getConnection(dbUrl, dbConnectionProps);
	}

	private Properties dbRequestProps() {
		Properties dbConnectionProps = new Properties();
		dbConnectionProps.setProperty("user", username);
		dbConnectionProps.setProperty("password", password);
		dbConnectionProps.setProperty("currentSchema", databaseSchema);
		
		return dbConnectionProps;
	}
}
