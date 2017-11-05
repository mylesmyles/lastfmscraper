package net.mylesputnam.lastfm.scraper.db.requests;

import java.sql.Connection;
import java.sql.SQLException;

public interface DbUpdateRequest {
	public void makeUpdateRequest(Connection connection) throws SQLException;
}
