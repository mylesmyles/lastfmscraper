package net.mylesputnam.lastfm.scraper.db.validation;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import net.mylesputnam.lastfm.scraper.db.Col;

class ColumnValidator {
	public final Col column;
	
	public static ColumnValidator create(Col column) {
		return new ColumnValidator(column);
	}
	
	private ColumnValidator(Col column) {
		this.column = column;
	}
	
	public boolean columnIsInvalid(String table, Connection connection) throws SQLException {
		PreparedStatement existingColumnStatment = connection.prepareStatement
		      ("SELECT data_type, udt_name, character_maximum_length "
              +"FROM information_schema.columns WHERE table_name=? AND column_name=?", 
                    ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
		existingColumnStatment.setString(1, table);
		existingColumnStatment.setString(2, column.name);
		ResultSet existingColumnResult = existingColumnStatment.executeQuery();
		boolean shouldCreateNewColumn = shouldCreateNewColumn(existingColumnResult);
		existingColumnStatment.close();
		
		return shouldCreateNewColumn;
	}
	
	public void createColumn(String table, Connection connection) throws SQLException {
			PreparedStatement dropExisting = connection.prepareStatement(
			       "ALTER TABLE " + table + " DROP COLUMN IF EXISTS " + column.name);
			dropExisting.executeUpdate();
			dropExisting.close();
			
			PreparedStatement createNewColumn = connection.prepareStatement("ALTER TABLE " + table + " ADD COLUMN " + column.name + " " + column.type);
			createNewColumn.executeUpdate();
			createNewColumn.close();
	}
	
	private boolean shouldCreateNewColumn(ResultSet existingColumnResult) throws SQLException {
		if(!existingColumnResult.first())
			return true;
		
		String dataType = existingColumnResult.getString("data_type");
		String udtDataType = existingColumnResult.getString("udt_name");
		String characterMaxLength = existingColumnResult.getString("character_maximum_length");
		
		if(isVarchar()) {
			return shouldCreateNewVarcharCol(udtDataType, characterMaxLength);
		}
		else {
			return column.type != null && !column.type.equalsIgnoreCase(dataType) && !column.type.equalsIgnoreCase(udtDataType);
		}
	}
	
	private boolean isVarchar() {
		return column.type.startsWith("varchar(") && column.type.endsWith(")");
	}
	
	private int getValidatorVarcharCharCount() {
		if(!isVarchar())
			return -1;
		
		int varcharCount = -1;
		
		int indexOfOpenParen = column.type.indexOf('(');
		int indexOfCloseParen = column.type.indexOf(')');
		
		if(indexOfOpenParen != -1 && indexOfCloseParen != -1 
				&& indexOfOpenParen < indexOfCloseParen && indexOfCloseParen < column.type.length()) {
			String varcharCountString = column.type.substring(indexOfOpenParen+1, indexOfCloseParen);
			try {
				varcharCount = Integer.parseInt(varcharCountString);
			}
			catch(NumberFormatException e) {}
		}
		
		return varcharCount;
	}
	
	private boolean shouldCreateNewVarcharCol(String udtDataType, String maxCharsFromDb) {
		if(udtDataType == null || !udtDataType.equalsIgnoreCase("varchar"))
			return true;
		
		int count = getValidatorVarcharCharCount();
		int countFromDb = -1;
		try {
			countFromDb = Integer.parseInt(maxCharsFromDb);
		}
		catch(NumberFormatException e) {}
		
		if(count == -1 || countFromDb == -1) {
			return true;
		}
		else {
			return countFromDb < count;
		}
	}
}
