package net.mylesputnam.lastfm.scraper.db.requests.update;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import net.mylesputnam.lastfm.scraper.data.LastFmUser;
import net.mylesputnam.lastfm.scraper.db.Col;
import net.mylesputnam.lastfm.scraper.db.Table;

public class InsertUserRequest implements DbUpdateRequest {
	
	public final LastFmUser user;
	public InsertUserRequest(LastFmUser user) {
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

	@Override
	public String toString() {
		return "InsertUserRequest [user=" + user + "]";
	}
}
