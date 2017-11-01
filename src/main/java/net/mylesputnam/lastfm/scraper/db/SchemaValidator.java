package net.mylesputnam.lastfm.scraper.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.Map.Entry;

import com.google.inject.Inject;

import net.mylesputnam.lastfm.scraper.appconfig.bindings.PostgresDatabaseSchema;

public class SchemaValidator {
	
	private final Connection connection;
	private final String schema;

	@Inject
	public SchemaValidator(ScraperDatabaseConnector dbConnector, @PostgresDatabaseSchema String schema) {
		this.connection = dbConnector.dbConnection();
		this.schema = schema;
	}
	
	public void validateSchema() {
		try {
			createSchemaIfNotExists(schema);
			setupTable("users", getColumnToTypeMapForUsers());
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	private void createSchemaIfNotExists(String schema) throws SQLException {
		PreparedStatement createSchemaStatement = connection.prepareStatement(
		      "CREATE SCHEMA IF NOT EXISTS " + schema);
		createSchemaStatement.executeUpdate();
		createSchemaStatement.close();
	}
	
	private void setupTable(String tableName, LinkedHashMap<String, String> columnToTypeMap) throws SQLException {
		createTableIfNotExists(tableName);
		for(Entry<String,String> column : columnToTypeMap.entrySet()) {
			String columnName = column.getKey();
			String columnType = column.getValue();
			setupColumn(tableName, columnName, columnType);
		}
	}
	
	private void setupColumn(String tableName, String columnName, String columnType) throws SQLException {
		PreparedStatement existingColumnStatment = connection.prepareStatement(
			   "SELECT column_name, data_type, udt_name, character_maximum_length "
              +"FROM information_schema.columns WHERE table_name=? AND column_name=?");
		existingColumnStatment.setString(1, tableName);
		existingColumnStatment.setString(2, columnName);
		ResultSet existingColumnResult = existingColumnStatment.executeQuery();
		
		if(shouldCreateNewColumn(existingColumnResult, columnName, columnType)) {
			PreparedStatement dropExisting = connection.prepareStatement(
			       "ALTER TABLE " + tableName + " DROP COLUMN IF EXISTS " + columnName);
			dropExisting.executeUpdate();
			dropExisting.close();
			
			PreparedStatement createNewColumn = connection.prepareStatement("ALTER TABLE " + tableName + " ADD COLUMN " + columnName + " " + columnType);
			createNewColumn.executeUpdate();
			createNewColumn.close();
		}
		
		existingColumnStatment.close();
	}
	
	private boolean shouldCreateNewColumn(ResultSet queryResult, String columnName, String columnType) {
		return true;
	}
	
	private boolean isVarChar(String columnType) {
		return columnType.startsWith("varchar(");
	}
	
	private void users() throws SQLException {
		
	}
	
	private LinkedHashMap<String, String> getColumnToTypeMapForUsers() {
		LinkedHashMap<String,String> columnsForUsers = new LinkedHashMap<>();
		columnsForUsers.put("name", "varchar(150)");
		columnsForUsers.put("url", "varchar(200)");
		columnsForUsers.put("created", "timestamptz");
		columnsForUsers.put("modified", "timestamptz");
		return columnsForUsers;
	}
	
	private void createTableIfNotExists(String tableName) throws SQLException {
		PreparedStatement statement = connection
		     .prepareStatement("CREATE TABLE IF NOT EXISTS "+tableName+" ()");
		statement.executeUpdate();
		statement.close();
	}
}
