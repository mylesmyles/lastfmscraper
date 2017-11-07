package net.mylesputnam.lastfm.scraper.exceptions;

import java.sql.SQLException;

public class DbException extends RuntimeException {
	private static final long serialVersionUID = 2315513715555470638L;
	public DbException(String message, SQLException parentException) {
		super(message, parentException);
	}

}
