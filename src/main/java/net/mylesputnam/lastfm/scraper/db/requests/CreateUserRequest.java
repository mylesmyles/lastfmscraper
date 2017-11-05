package net.mylesputnam.lastfm.scraper.db.requests;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import net.mylesputnam.lastfm.scraper.data.LastFmUser;
import net.mylesputnam.lastfm.scraper.db.Col;
import net.mylesputnam.lastfm.scraper.db.Table;

public class CreateUserRequest implements DbUpdateRequest {
	
	public final LastFmUser user;
	public CreateUserRequest(LastFmUser user) {
		this.user = user;
	}
	
	@Override
	public void makeUpdateRequest(Connection connection) throws SQLException {
		PreparedStatement insertUser = connection.prepareStatement(
				"INSERT INTO " + Table.USER.name + "(" + String.join(", ", Col.USER_NAME.name, Col.USER_URL.name, Col.USER_CREATED.name) + ") " +
				"VALUES (?, ?, NOW()) " +
				"ON CONFLICT DO NOTHING");
		insertUser.setString(1, user.username);
		insertUser.setString(2, user.url);
		insertUser.executeUpdate();
	}
}
