package net.mylesputnam.lastfm.scraper.db.validation;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import net.mylesputnam.lastfm.scraper.db.Col;
import net.mylesputnam.lastfm.scraper.db.Table;

class ConstraintValidator {
	public static void trySetupTableConstraints(Connection connection) {
		ConstraintValidator validator = new ConstraintValidator(connection);
		validator.tryAddConstraints();
	}
	
	private final Connection connection;
	
	private ConstraintValidator(Connection connection) {
		this.connection = connection;
	}
	
	private void tryAddConstraints() {
		addPrimaryKeyConstraint(Table.USER, Col.USER_NAME);
		addPrimaryKeyConstraint(Table.ARTIST, Col.ARTIST_NAME);
		
		addForeignKeyConstraint(Table.LISTENS, Col.LISTENS_USER_NAME, Table.USER, Col.USER_NAME);
		addForeignKeyConstraint(Table.LISTENS, Col.LISTENS_ARTIST_NAME, Table.ARTIST, Col.ARTIST_NAME);
	}
	
	private void addPrimaryKeyConstraint(Table table, Col column) {
		try {
			PreparedStatement primaryKeyConstraint = connection.prepareStatement("ALTER TABLE " + table.name + " ADD PRIMARY KEY (" + column.name + ")");
			primaryKeyConstraint.executeUpdate();
		}
		catch (SQLException e) {
			System.out.println("Problem adding primary key constraint for "+table.name+":"+column.name+". It probably already exsits:");
			e.printStackTrace();
		}
	}
	
	private void addForeignKeyConstraint(Table childTable, Col childColumn, Table parentTable, Col parentColumn) {
		try {
			PreparedStatement fkConstraint = connection.prepareStatement(
					"ALTER TABLE " + childTable.name + " " + 
					"ADD CONSTRAINT fk_" + childColumn.name + " " + 
					"FOREIGN KEY (" + childColumn.name + ")" + " " + 
					"REFERENCES " + parentTable.name + " (" + parentColumn.name + ")");
			fkConstraint.executeUpdate();
		}
		catch (SQLException e) {
			System.out.println("Problem adding foriegn key constraint for "+childTable.name+":"+childColumn.name+". It probably already exsits:");
			e.printStackTrace();
		}
	}
}
