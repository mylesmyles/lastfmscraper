package net.mylesputnam.lastfm.scraper.db.validation;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

import net.mylesputnam.lastfm.scraper.db.Col;
import net.mylesputnam.lastfm.scraper.db.Table;
import net.mylesputnam.lastfm.scraper.util.StaticList;

class TableValidator {
	public final String name;
	public final StaticList<ColumnValidator> columnValidators;

	public static TableValidator create(Table table) {
		
		return new TableValidator(table.name, table.columns);
	}
	
	private TableValidator(String name, StaticList<Col> columns) {
		this.name = name;
		List<ColumnValidator> columnValidators = new LinkedList<>();
		
		for(Col column : columns) {
			columnValidators.add(ColumnValidator.create(column));
		}
		this.columnValidators = StaticList.create(columnValidators);
	}
	
	public void validateTable(Connection connection) throws SQLException {
		createTableIfNotExists(connection);
		if(anyColumnsInTableAreInvalid(connection)) {
			dropTableAndRecreateColumns(connection);
		}
	}
	
	private void createTableIfNotExists(Connection connection) throws SQLException {
		PreparedStatement createStatement = connection
		     .prepareStatement("CREATE TABLE IF NOT EXISTS "+name+" ()");
		createStatement.executeUpdate();
		createStatement.close();
	}
	
	private void dropTableIfExists(Connection connection) throws SQLException {
		PreparedStatement dropStatement = connection
			.prepareStatement("DROP TABLE IF EXISTS " + name);
		dropStatement.executeUpdate();
		dropStatement.close();
	}
	
	private boolean anyColumnsInTableAreInvalid(Connection connection) throws SQLException {
		boolean foundInvalidColumn = false;
		for(ColumnValidator columnValidator : columnValidators) {
			if(columnValidator.columnIsInvalid(name, connection))
				foundInvalidColumn = true;
		}
		
		return foundInvalidColumn;
	}
	
	private void dropTableAndRecreateColumns(Connection connection) throws SQLException {
		dropTableIfExists(connection);
		createTableIfNotExists(connection);
		for(ColumnValidator columnValidator : columnValidators) {
			columnValidator.createColumn(name, connection);
		}
	}
}
