package net.mylesputnam.lastfm.scraper.db.requests.get;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import net.mylesputnam.lastfm.scraper.appconfig.bindings.ScraperUserSeed;
import net.mylesputnam.lastfm.scraper.db.Col;
import net.mylesputnam.lastfm.scraper.db.ScraperDatabaseConnector;
import net.mylesputnam.lastfm.scraper.db.Table;
import net.mylesputnam.lastfm.scraper.exceptions.DbException;

@Singleton
public class ScraperTaskInfoGetter {
	private final ScraperDatabaseConnector dbConnector;
	private final String seedUsername;

	@Inject
	public ScraperTaskInfoGetter(ScraperDatabaseConnector dbConnector, @ScraperUserSeed String seedUserName) {
		this.dbConnector = dbConnector;
		this.seedUsername = seedUserName;
	}
	
	public boolean seedUserExists() {
		return userExists(seedUsername);
	}
	
	public boolean userExists(String username) {
		try {
			Connection connection = dbConnector.newDbConnection();
			PreparedStatement selectUser = connection.prepareStatement("SELECT " + Col.USER_NAME + " FROM " + Table.USER + " WHERE " + Col.USER_NAME + "=?",
					ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			selectUser.setString(1, username);
			ResultSet results = selectUser.executeQuery();
			return results.first();
		} catch (SQLException e) {
			throw new DbException("Could not check if user exists!", e);
		}
	}
}
