package net.mylesputnam.lastfm.scraper.db.validation;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.google.inject.Inject;

import net.mylesputnam.lastfm.scraper.appconfig.bindings.PostgresDatabaseSchema;
import net.mylesputnam.lastfm.scraper.data.LastFmUser;
import net.mylesputnam.lastfm.scraper.db.ScraperDatabaseConnector;
import net.mylesputnam.lastfm.scraper.db.Table;
import net.mylesputnam.lastfm.scraper.db.requests.CreateUserRequest;
import net.mylesputnam.lastfm.scraper.exceptions.DbValidationException;

public class DatabaseValidator {
	
	private final Connection connection;
	private final String schema;

	@Inject
	public DatabaseValidator(ScraperDatabaseConnector dbConnector, @PostgresDatabaseSchema String schema) {
		this.connection = dbConnector.dbConnection();
		this.schema = schema;
	}
	
	public void validateDatabase() {
		try {
			createSchemaIfNotExists(schema);
			setupTables();
			ConstraintValidator.trySetupTableConstraints(connection);
		}
		catch (SQLException e) {
			throw new DbValidationException("Problem validating database!", e);
		}
		finally {
			closeConnection();
		}
	}
	
	private void createSchemaIfNotExists(String schema) throws SQLException {
		PreparedStatement createSchemaStatement = connection.prepareStatement(
				"CREATE SCHEMA IF NOT EXISTS " + schema);
		createSchemaStatement.executeUpdate();
		createSchemaStatement.close();
	}
	
	private void setupTables() throws SQLException {
		for(Table table : Table.values()) {
			TableValidator tableValidator = TableValidator.create(table);
			tableValidator.validateTable(connection);
		}
	}
	
	private void closeConnection() {
		try {
			connection.close();
		} catch (SQLException e) {
			System.out.println("Problem closing validation connection");
			e.printStackTrace();
		}
	}
}
